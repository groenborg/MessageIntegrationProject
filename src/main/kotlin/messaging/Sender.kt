package messaging

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import components.MsgFactory
import messaging.EXCHANGE
import messaging.MsgFactory

open class Sender {

    fun send(severity: String) {

        val queue = MsgFactory.buildMessageConnector()
        val message: String = "hello, world"

        for (i in 1..10) {
            queue.basicPublish(EXCHANGE.DEFAULT, arrayOf(severity), message = message)
        }
        println("[SENDER]:[SENT][MESSAGE] -- '$message'")

        queue.channel.close()
        queue.connection.close()
    }

}

fun main(args: Array<String>) {

    Sender().send("black")
    Sender().send("only")
    Sender().send("info")

}