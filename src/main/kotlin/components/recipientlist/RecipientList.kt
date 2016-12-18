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

    // Since this RecipientList functions as a Router, it will not enrich the data
    override fun componentAction(msg: String) {
        val data = XMLParser(RuleRequestObject::class.java).fromXML(msg);

        //Example on how to print rules
        val rules = data.rules;

                if (rules != null) {
                    for (rule in rules.rule.orEmpty()) {
                        var min = 0;
                        var max = 0;
                        if(rule.min != null && rule.max != null){
                            min = Integer.parseInt(rule.min)
                            max = Integer.parseInt(rule.max)
                        }

                        for (bank in rule.bank.orEmpty()) {
                            when (Integer.parseInt(data.creditScore)){
                                in min..max -> connector.basicPublish(exchange, arrayOf("translator" + bank.bankNo), msg)
                            }
                        }
                    }
                }

        // Sending message to the aggregator
        var aggMsg = "<AggRequest><ssn>" + data.ssn + "</ssn>" + "<numOfBanks>" + countDistinctBanks(data) + "</numOfBanks></AggRequest>";
        connector.basicPublish(exchange, arrayOf("agg"), aggMsg);
    }

    fun countDistinctBanks(ruleRequestObject: RuleRequestObject): Int{
        val numbers: MutableList<Int> = mutableListOf()
        val rules = ruleRequestObject.rules;
        if(rules != null){
            for (rule in rules.rule.orEmpty()){
                for(bank in rule.bank.orEmpty()){
                    val tempBankNo = Integer.parseInt(bank.bankNo)
                    if(!consistsInArray(tempBankNo, numbers)){
                        numbers.add(tempBankNo)
                    }
                } // Iterating banks
            } // Iterating rules
        }
        return numbers.size+1
    }

    fun consistsInArray(number: Int, collection: List<Int>): Boolean{
        for(i in collection.indices){
            if(number == collection.get(i)){
                return true;
            }
        }
        return false;
    }
}

