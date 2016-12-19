package components.normalizer

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES


class Normalizer : IMessageComponent {

    var localConnector = MsgFactory.buildMessageConnector()
    val remoteConnector = MsgFactory.buildRemoteConnector()

    val jsonTranslator = JsonTranslator()
    val xmlTranslator = XmlTranslator()

    val replyQueue = QUEUES.CPH_REPLY_QUEUE
    val localExchange = EXCHANGE.DEFAULT


    override fun bindQueue(severity: String): IMessageComponent {
        remoteConnector.declareQueue(replyQueue, true)
        return this
    }

    override fun startConsume() {
        val consumer = object : DefaultConsumer(remoteConnector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                componentAction(String(body!!, Charsets.UTF_8))
            }
        }
        consumer.channel.basicConsume(replyQueue, true, consumer)
        println("[NORMALIZER]: now listening")
    }

    override fun componentAction(msg: String) {
        println("I WAS A NORMALIZER ACTION")
        println(msg)

        var convertedMessage: String = ""

        if (jsonTranslator.isJsonRequest(msg)) {
            convertedMessage = jsonTranslator.convertToXml(msg)
        } else {
            convertedMessage = xmlTranslator.convertXml(msg)
        }
        localConnector.basicPublish(localExchange, arrayOf("agg"), message = convertedMessage)
    }

}