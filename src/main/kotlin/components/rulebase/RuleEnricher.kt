package components.rulebase

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES
import utils.XMLParser
import messaging.RuleObject
import messaging.Rule

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
        println("I WAS AN RULE ACTION")

        /*
        val service = GetRulesService_Service()
        val proxy = service.getRulesServicePort

        var rules = XMLParser(RuleObject::class.java).fromXML(proxy.rules)

        for (rule in rules.rule.orEmpty()){
            println()
            println("---------------------")
            println(rule.min)
            println(rule.max)
            for (bank in rule.bank.orEmpty()){
                println(bank.bankNo)
            }
            println("---------------------")
            println()
        }

        */

    }

}


