import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory

open class Sender {

    fun send(severity: String) {

        val queue = MessageConnector.getTunnel()
        val message: String = "hello, world"

        for (i in 1..10) {
            queue.basicPublish(EXHANGE.DEFAULT, arrayOf(severity), message = message)
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