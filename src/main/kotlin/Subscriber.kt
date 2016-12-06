import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope


class MySubscriber {

    fun startListening() {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.username = "user"
        factory.password = "password"

        val connection = factory.newConnection()
        val channel = connection.createChannel()
        val severity: String = "only"


        channel.exchangeDeclare(MsgArgs.EXCHANGE_NAME, "direct")
        channel.queueDeclare("only_queue", true, false, false, null)

        channel.queueBind("only_queue", MsgArgs.EXCHANGE_NAME, severity)



        println("[RECEIVER]:[STATUS] - Waiting for [*] messages. To exit press CTRL+C")

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

                val message = String(body!!, Charsets.UTF_8)
                val routingKey = envelope?.routingKey
                println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")
            }
        }
        channel.basicConsume("only_queue", true, consumer)


    }


}

fun main(args: Array<String>) {

    MySubscriber().startListening()

}