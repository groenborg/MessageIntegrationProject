package components.soapbank

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES
import messaging.RequestObject
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
        val consumer = object : DefaultConsumer(connector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                val message = String(body!!, Charsets.UTF_8)
                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)

    }

    override fun componentAction(msg: String) {
        println()
        println("A SOAP BANK ACTION OCCURRED")
        println()

        val service = BankLoanService()
        val proxy = service.bankLoanServicePort

        val rO = XMLParser(RequestObject::class.java).fromXML(msg)

        val interestRate = proxy.getInterestRate(rO.ssn!!, rO.creditScore!!.toInt(), rO.amount!!.toDouble(), rO.duration!!.toInt())


        //val t = XMLParser(LoanResponse::class.java).fromXML(requestXML)

        //connector.basicPublish(exchange, arrayOf("normalizer"), requestXML)

        // val newInterestRequest = LoanOffer(rO.ssn.orEmpty(), interestRate.toString(), "soapBank")

        //  val xmlObject = XMLParser(LoanOffer::class.java).toXML(newInterestRequest)

        //connector.basicPublish(exchange, arrayOf("normalizer"), xmlObject)


    }
}