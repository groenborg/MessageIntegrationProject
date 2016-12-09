package components

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope

/**
 * Created by simon on 09/12/2016.
 */


class RuleEnricher : IMessageComponent {

    private val connector = MsgFactory.buildMessageConnector()
    private val queue = QUEUES.ENRICHER_RULE
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

                componentAction()
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    override fun componentAction() {
        println("I WAS AN RULE ACTION")
    }


}


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

                componentAction()
            }
        }
        connector.channel.basicConsume(queue, true, consumer)

    }

    override fun componentAction() {
        println("I WAS AN CREDIT ACTION")
    }
}
