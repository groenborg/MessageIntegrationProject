package components.normalizer

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES


class Normalizer : IMessageComponent {

    val localConnector = MsgFactory.buildMessageConnector()
    val remoteConnector = MsgFactory.buildRemoteConnector()

    val jsonTranslator = JsonTranslator()
    val xmlTranslator = XmlTranslator()

    val remoteReplyQueue = QUEUES.CPH_REPLY_QUEUE
    val localQueue = QUEUES.NORMALIZER
    val localExchange = EXCHANGE.DEFAULT


    override fun bindQueue(severity: String): IMessageComponent {
        remoteConnector.declareQueue(remoteReplyQueue, true)
        localConnector.declareQueue(localQueue, true)
        localConnector.bindQueueToExchange(localQueue, localExchange, arrayOf(severity))
        return this
    }

    override fun startConsume() {
        localConsume()
        remoteConsumer()
    }

    fun localConsume() {
        val consumer = object : DefaultConsumer(localConnector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                componentAction(String(body!!, Charsets.UTF_8))
            }
        }
        consumer.channel.basicConsume(localQueue, true, consumer)
        println("[NORMALIZER]: now listening on local server")
    }

    fun remoteConsumer() {
        val consumer = object : DefaultConsumer(remoteConnector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                componentAction(String(body!!, Charsets.UTF_8))
            }
        }
        consumer.channel.basicConsume(remoteReplyQueue, true, consumer)
        println("[NORMALIZER]: now listening on remote server")
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