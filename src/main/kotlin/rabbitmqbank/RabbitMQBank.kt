package rabbitmqbank

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.*
import utils.XMLParser
import java.security.SecureRandom

/**
 * Created by cm on 19/12/2016.
 */
class RabbitMQBank : IMessageComponent {
    private val connector = MsgFactory.buildMessageConnector()

    private val queue = QUEUES.RABBIT_MQ_BANK
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
        val requestObject = XMLParser(RequestObject::class.java).fromXML(msg)

        val interestRate = computeInterestRate(requestObject.ssn.orEmpty(), requestObject.creditScore!!.toInt(), requestObject.amount!!.toDouble(), requestObject.duration!!.toInt())

        val newInterestRequest = LoanOffer(requestObject.ssn.orEmpty(), interestRate.toString(), "rabbitmqBank")
        val xmlInterestObject = XMLParser(RequestObject::class.java).toXML(newInterestRequest)

        connector.basicPublish(exchange, arrayOf("normalizer"), xmlInterestObject)

    }

    fun computeInterestRate(ssn: String, creditScore: Int, amount: Double, duration: Int): Int{
        // optional calculation of interest rate
        return SecureRandom().nextInt(10)+1
    }

}
