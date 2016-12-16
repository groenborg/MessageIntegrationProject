package components.recipientlist

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES
import messaging.RequestObject
import utils.XMLParser


class RecipientList : IMessageComponent {

    private val connector = MsgFactory.buildMessageConnector();
    private val queue = QUEUES.RECIPIENT_LIST
    private val exchange = EXCHANGE.DEFAULT

    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf(severity))
        return this
    }

    override fun startConsume() {
        println("[RECEIVER]:[STATUS] - Waiting for [*] messages. To exit press CTRL+C")

        val consumer = object : DefaultConsumer(connector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                val message = String(body!!, Charsets.UTF_8)
                val routingKey = envelope?.routingKey
                println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")

                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    override fun componentAction(msg: String) {
        val data = XMLParser(RequestObject::class.java).fromXML(msg);

        when (data.creditScore.toInt()) {
            in 720..800 -> connector.basicPublish(exchange, severity = arrayOf("EXCELLENT"), message = msg)
            in 680..719 -> connector.basicPublish(exchange, severity = arrayOf("GOOD"), message = msg)
            in 620..679 -> connector.basicPublish(exchange, severity = arrayOf("AVERAGE"), message = msg)
            in 580..619 -> connector.basicPublish(exchange, severity = arrayOf("POOR"), message = msg)
            in 500..579 -> connector.basicPublish(exchange, severity = arrayOf("BAD"), message = msg)
            in 0..500 -> connector.basicPublish(exchange, severity = arrayOf("MISERABLE"), message = msg)
        }
    }

}