package messaging

import utils.XMLParser


/**
 * Serves as a test to fire a xmlMessage through the whole system
 */
open class Sender {

    fun send(severity: String) {

        val connector = MsgFactory.buildMessageConnector()

        val aggMessage = AggRequest()
        aggMessage.numOfBanks = "2"
        aggMessage.ssn = "1605789787"


        val xmlParser = XMLParser(AggRequest::class.java)
        val message: String = xmlParser.toXML(aggMessage)


        for (i in 1..1) {
            connector.basicPublish(EXCHANGE.DEFAULT, arrayOf(severity), message = message)
        }


/*
        connector.declareQueue("endpoint", true)
        connector.bindQueueToExchange("endpoint", EXCHANGE.DEFAULT, arrayOf("endpoint"))
         val consumer = object : DefaultConsumer(connector.channel) {
           override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
             println(String(body!!, Charsets.UTF_8))
         }
         }
        val queueingConsumer = QueueingConsumer(connector.channel)
        connector.channel.basicConsume("endpoint", true, queueingConsumer)
        val delivery = queueingConsumer.nextDelivery()
        val msg = String(delivery.body!!, Charsets.UTF_8)
        println(msg)
*/
        connector.channel.close()
        connector.connection.close()
    }
}

fun main(args: Array<String>) {
    Sender().send("agg")
}