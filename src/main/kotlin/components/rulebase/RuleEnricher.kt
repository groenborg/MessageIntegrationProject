package components.rulebase

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.*
import utils.XMLParser

/**
 * Created by simon on 09/12/2016.
 */

class RuleEnricher : IMessageComponent {

    private val connector = MsgFactory.buildMessageConnector()
    private val queue = QUEUES.ENRICHER_RULE
    private val exchange = EXCHANGE.DEFAULT


    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf(severity))
        return this
    }


    override fun startConsume() {
        val consumer = object : DefaultConsumer(connector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                val message = String(body!!, Charsets.UTF_8)
                val routingKey = envelope?.routingKey
                //println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")

                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
        println("[RuleEnricher]: now listening")
    }

    override fun componentAction(msg: String) {

        println("I WAS AN RULE ACTION")
        println()

        val service = GetRulesService_Service()
        val proxy = service.getRulesServicePort

        val rules = XMLParser(RuleObject::class.java).fromXML(proxy.rules)

        val rO = XMLParser(RequestObject::class.java).fromXML(msg)

        val newRequestObject = RuleRequestObject(rO.ssn!!, rO.amount!!, rO.currency!!, rO.duration!!, rO.creditScore!!, rules.rule!!)

        val xmlMessage = XMLParser(RuleRequestObject::class.java).toXML(newRequestObject)

        connector.basicPublish(exchange, arrayOf("recipient"), xmlMessage)

    }
}

