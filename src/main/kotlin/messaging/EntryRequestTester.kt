package messaging

import utils.XMLParser


/**
 * Serves as a test to fire a message through the whole system
 */
open class Sender {

    fun send(severity: String) {

        val queue = MsgFactory.buildMessageConnector()

        val entryMessageObject = RequestObject("123456-4321","100","1000","dkk")
        val xmlParser = XMLParser(RequestObject::class.java)
        val message: String = xmlParser.toXML(entryMessageObject)

        for (i in 1..1) {
            queue.basicPublish(EXCHANGE.DEFAULT, arrayOf(severity), message = message)
        }
        println("[SENDER]:[SENT][MESSAGE] -- '$message'")

        queue.channel.close()
        queue.connection.close()
    }

}

fun main(args: Array<String>) {
    Sender().send("agg")
}