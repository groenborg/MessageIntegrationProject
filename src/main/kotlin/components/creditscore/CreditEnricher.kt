package components.creditscore

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.EXCHANGE
import messaging.MsgFactory
import messaging.QUEUES
import messaging.RequestObject
import utils.XMLParser


class CreditEnricher : IMessageComponent {

    private val connector = MsgFactory.buildMessageConnector()
    private val queue = QUEUES.ENRICHER_CREDIT
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
                //val routingKey = envelope?.routingKey
                //println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")

                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
        println("[CreditEnricher]: now listening")

    }

    override fun componentAction(msg: String) {
        println("A CREDIT ACTION OCCURRED")

        val service = CreditScoreService()
        val proxy = service.creditScoreServicePort

        val rO = XMLParser(RequestObject::class.java).fromXML(msg)

        val creditScore = proxy.creditScore(rO.ssn.orEmpty())

        println("CreditScore Was : " + creditScore!!)
        println()

        //Currently request stops if service return -1
        if(creditScore == -1){
            println("Error in SSN")
        }else {
            val newRequest = RequestObject(rO.ssn, rO.amount, rO.currency, rO.duration, creditScore.toString())

            val xmlObject = XMLParser(RequestObject::class.java).toXML(newRequest)

            connector.basicPublish(exchange, arrayOf("rule"), xmlObject)
        }
    }
}