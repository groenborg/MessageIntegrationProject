import com.rabbitmq.client.*
import java.nio.charset.Charset

/**
 * Created by simon on 06/12/2016.
 */

class Recv {


    fun consume() {


        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.username = "user"
        factory.password = "password"

        val connection = factory.newConnection()
        val channel = connection.createChannel()

        //channel.queueDeclare(MsgArgs.QUEUE_NAME, false, false, false, null)

        channel.exchangeDeclare(MsgArgs.EXCHANGE_NAME, "fanout")
        val randomQueue = channel.queueDeclare().queue
        channel.queueBind(randomQueue, MsgArgs.EXCHANGE_NAME, "")


        println("[RECEIVER]:[STATUS] - Waiting for [*] messages. To exit press CTRL+C")

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

                val message = String(body!!, Charsets.UTF_8)
                println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message'")
            }
        }
        channel.basicConsume(randomQueue, true, consumer)


    }

}

fun main(args: Array<String>) {
    Recv().consume()
}