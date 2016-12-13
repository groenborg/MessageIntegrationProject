package components.CreditScore

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent

/**
 * Created by Skroget on 13/12/2016.
 */


class CreditEnricher : IMessageComponent {

    private val connector = MsgFactory.buildMessageConnector()
    private val queue = QUEUES.ENRICHER_CREDIT
    private val exchange = EXHANGE.DEFAULT


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
        println("I WAS AN CREDIT ACTION")

        val service = CreditScoreService()
        val proxy = service.creditScoreServicePort

        println(proxy.creditScore("240790-1285"))

        /*
        var tmp = XMLParser(RequestObject::class.java).fromXML(msg)

        println("---------------------")
        println("SSN : " + tmp.ssn)
        println("Amount : " + tmp.amount)
        println("Duration : " + tmp.duration)
        */

    }
}

class RequestObject {
    @JvmField var ssn: String? = null
    @JvmField var amount: String? = null
    @JvmField var duration: String? = null
}