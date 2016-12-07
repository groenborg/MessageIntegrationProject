import com.rabbitmq.client.*


class Recv {


    fun consume() {

        val queue = MessageConnector.getTunnel()

        queue.declareQueue(QUEUES.NORMALIZER, durable = true)
        queue.bindQueueToExchange(QUEUES.NORMALIZER, EXHANGE.DEFAULT, arrayOf("black", "info"))


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
    Recv().consume()
}