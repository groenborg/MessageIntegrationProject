import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope


class MySubscriber {
/*
    fun startListening() {
        val queue = MessageConnector.getTunnel()
        val severity: String = "only"

        queue.bindQueueToExchange(QUEUES.NORMALIZER, EXHANGE.DEFAULT, arrayOf(severity))



        println("[RECEIVER]:[STATUS] - Waiting for [*] messages. To exit press CTRL+C")

        val consumer = object : DefaultConsumer(queue.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {

                val message = String(body!!, Charsets.UTF_8)
                val routingKey = envelope?.routingKey
                println("[RECEIVER]:[CAUGHT][MESSAGE] -- '$message' -- [ROUTING KEY]:$routingKey")
            }
        }
        queue.channel.basicConsume(QUEUES.NORMALIZER, true, consumer)


    }


}

fun main(args: Array<String>) {

    MySubscriber().startListening()
*/
}