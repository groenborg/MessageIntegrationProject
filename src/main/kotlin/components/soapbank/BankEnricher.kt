package components.soapbank

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.*
import utils.XMLParser

/**
 * Created by Skroget on 19/12/2016.
 */

class BankEnricher : IMessageComponent {

    private val connector = MsgFactory.buildMessageConnector()
    private val queue = QUEUES.ENRICHER_BANK
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
        println("A SOAP BANK ACTION OCCURRED")

        val service = BankLoanService()
        val proxy = service.bankLoanServicePort

        val rO = XMLParser(RequestObject::class.java).fromXML(msg)

        val interestRate = proxy.getInterestRate(rO.ssn.orEmpty(), rO.creditScore as Int, rO.amount as Double, rO.duration as Int)

        //new RequestObject
        //Parse
        //Send to Simon

        /*
        val newInterestRequest = RequestObject(rO.ssn, rO.amount, rO.currency, rO.duration, creditScore.toString())

        val xmlObject = XMLParser(RequestObject::class.java).toXML(newRequest)

        connector.basicPublish(exchange, arrayOf("rule"), xmlObject)
*/
    }
}