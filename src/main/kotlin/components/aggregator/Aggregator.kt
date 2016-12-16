package components.aggregator

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES
import messaging.RequestObject
import utils.XMLParser
import java.util.*


class Aggregator : IMessageComponent {

    var aggregate: Map<String, RequestObject> = HashMap()
    var xmlParser = XMLParser(RequestObject::class.java)

    val connector = MsgFactory.buildMessageConnector()
    val exchange = EXCHANGE.DEFAULT
    val queue = QUEUES.AGGREGATOR


    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf(severity))
        return this
    }

    override fun startConsume() {
        val consumer = object : DefaultConsumer(connector.channel) {

            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                val message = String(body!!, Charsets.UTF_8)
                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    override fun componentAction(msg: String) {
        val messageObject = xmlParser.fromXML(msg)
        var message: String = ""

        messageObject.currency = "AGGREGATOR"

        for ((a, b) in aggregate) {
            if (a == messageObject.ssn) {
                b.amount += messageObject.amount
            }
        }

        message = xmlParser.toXML(messageObject)
        connector.basicPublish(exchange, severity = arrayOf("endpoint"), message = message)
    }

}
