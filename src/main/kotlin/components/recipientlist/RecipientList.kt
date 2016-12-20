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

class RecipientList : IMessageComponent {

    /** The idea is, that the Recipient List should obtain a list of recipients from the Rule Enricher
     * A REAL recipient list contains two parts:
     * 1. The computation of recipients, which also includes receiving it from a different component.
     * 2. Traversing all recipients and sending a copy of the Message */

    private val connector = MsgFactory.buildMessageConnector();
    private val queue = QUEUES.RECIPIENT_LIST
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

                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
    }

    // Since this RecipientList functions as a Router, it will not enrich the data
    override fun componentAction(msg: String) {
        println("I WAS RECIPIENT ACTION")
        println()

        val data = XMLParser(RuleRequestObject::class.java).fromXML(msg)

        val newRequestObject = RequestObject(data.ssn, data.amount, data.currency, data.duration, data.creditScore)
        val xmlRequestObject = XMLParser(RequestObject::class.java).toXML(newRequestObject)

        val rules = data.rule

        // checks if rules-object is valid
        if (rules != null) {
            // iterates through every rule
            for (rule in rules) {
                // checks if both values are valid for rule-checking
                if (rule.min != null && rule.max != null) {
                    var min = rule.min!!.toInt()
                    var max = rule.max!!.toInt()
                    // Iterates through every bank and sends to the recipient if the creditscore is in the proper range
                    if (data.creditScore!!.toInt() >= min && data.creditScore!!.toInt() < max) {
                        for (bank in rule.bank!!) {
                            connector.basicPublish(exchange, arrayOf("translator" + bank.bankno), xmlRequestObject)
                         }
                        // Sending message to the aggregator
                        var aggMsg = "<AggRequest><ssn>" + data.ssn + "</ssn>" + "<numOfBanks>" + rule.bank!!.size + "</numOfBanks></AggRequest>";
                        connector.basicPublish(exchange, arrayOf("agg"), aggMsg);
                    }
                }
            }
        }
    }
}

