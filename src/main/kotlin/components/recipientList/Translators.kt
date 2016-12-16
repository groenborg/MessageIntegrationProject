package components.recipientList

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent

/**
 * Created by christophermortensen on 13/12/2016.
 */

// JSON BANK
class BankTranslator1 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector();
    private val queue = QUEUES.BANK_TRANSLATOR1
    private val exchange = EXHANGE.DEFAULT;

    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf("EXCELLENT"))
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

    override fun componentAction(msg : String) {
        // TODO: Do the actual conversion of the data and publish to banks
    }

}

// XML BANK
class BankTranslator2 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector();
    private val queue = QUEUES.BANK_TRANSLATOR2
    private val exchange = EXHANGE.DEFAULT;

    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf("EXCELLENT", "GOOD", "AVERAGE"))
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

    override fun componentAction(msg : String) {
        // TODO: Do the actual conversion of the data and publish to banks
    }

}

// JSON BANK
class BankTranslator3 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector();
    private val queue = QUEUES.BANK_TRANSLATOR3
    private val exchange = EXHANGE.DEFAULT;

    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf("EXCELLENT", "GOOD", "AVERAGE", "POOR", "BAD", "MISERABLE"))
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

    override fun componentAction(msg : String) {
        // TODO: Do the actual conversion of the data and publish to banks
    }

}

// JSON BANK
class BankTranslator4 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector();
    private val queue = QUEUES.BANK_TRANSLATOR4
    private val exchange = EXHANGE.DEFAULT;

    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf("POOR", "BAD", "MISERABLE"))
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

    override fun componentAction(msg : String) {
        // TODO: Do the actual conversion of the data and publish to banks
    }

}