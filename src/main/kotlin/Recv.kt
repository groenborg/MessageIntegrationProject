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
        val severity: Array<String> = arrayOf("black", "info")


        channel.exchangeDeclare(MsgArgs.EXCHANGE_NAME, "direct")
        val randomQueue = channel.queueDeclare().queue


        for (element in severity) {
            channel.queueBind(randomQueue, MsgArgs.EXCHANGE_NAME, element)
        }



        println("[RECEIVER]:[STATUS] - Waiting for [*] messages. To exit press CTRL+C")

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

                val message = String(body!!, Charsets.UTF_8)
                val routingKey = envelope?.routingKey
                println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")
            }
        }
        channel.basicConsume(randomQueue, true, consumer)


    }

}

fun main(args: Array<String>) {
    Recv().consume()
}