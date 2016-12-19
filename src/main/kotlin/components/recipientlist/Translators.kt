package components.recipientlist

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.*
import utils.XMLParser

/**
 * Created by christophermortensen on 13/12/2016.
 */

// JSON BANK (CPH)
class BankTranslator1 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector()
    private val remoteConnector = MsgFactory.buildRemoteConnector()

    private val queue = QUEUES.BANK_TRANSLATOR1
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
                //println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")

                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    override fun componentAction(msg: String) {
        // TODO optional translation

        val cphQueue = QUEUES.CPH_REPLY_QUEUE
        val cphExchange = EXCHANGE.CPH_JSON_BANK

        remoteConnector.declareQueue(queue, true)
        remoteConnector.basicRequestReplyPublish(cphExchange, AMQP.BasicProperties.Builder().replyTo(cphQueue).build(), msg)
    }

}

// XML BANK (CPH)
class BankTranslator2 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector()
    private val remoteConnector = MsgFactory.buildRemoteConnector()

    private val queue = QUEUES.BANK_TRANSLATOR2
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
                //println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")

                componentAction(message)
            }
        }

    }

    override fun componentAction(msg: String) {
        // TODO optional translation

        val cphQueue = QUEUES.CPH_REPLY_QUEUE
        val cphExchange = EXCHANGE.CPH_XML_BANK

        remoteConnector.declareQueue(cphQueue, true)
        remoteConnector.basicRequestReplyPublish(cphExchange, AMQP.BasicProperties.Builder().replyTo(cphQueue).build(), msg)
    }

}

// XML SOAP BANK (CUSTOM)
class BankTranslator3 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector()

    private val queue = QUEUES.BANK_TRANSLATOR3
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
        // TODO optional translation
       connector.basicPublish(exchange, arrayOf("soapbank"), msg)
    }

}

// XML RABBITMQ BANK (CUSTOM)
class BankTranslator4 : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector()

    private val queue = QUEUES.BANK_TRANSLATOR4
    private val exchange = EXCHANGE.DEFAULT;

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
                //println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")

                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    override fun componentAction(msg: String) {
        // TODO optional translation
        connector.basicPublish(exchange, arrayOf("rabbitmqbank"), msg)
    }

}

