package components.normalizer

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES
import messaging.RequestObject
import utils.XMLParser

class Normalizer : IMessageComponent {

    var connector = MsgFactory.buildMessageConnector()
    var xmlParser = XMLParser(RequestObject::class.java)

    val queue = QUEUES.ENRICHER_RULE
    val exchange = EXCHANGE.DEFAULT


    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, arrayOf(severity))
        return this
    }

    override fun startConsume() {
        val consumer = object : DefaultConsumer(connector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

                // Send Somewhere
                componentAction(String(body!!, Charsets.UTF_8))
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    override fun componentAction(msg: String) {
        val requestObject = xmlParser.fromXML(msg)
        var messageToSend: String = ""

        requestObject.currency = "NORMALIZER"
        messageToSend = xmlParser.toXML(requestObject)


        connector.basicPublish(exchange, arrayOf("agg"), message = messageToSend)

        println("[NORMALIZER: RECEIVED AND SENT]")
    }

}