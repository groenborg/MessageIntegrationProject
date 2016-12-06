import com.rabbitmq.client.ConnectionFactory


object MsgArgs {
    val QUEUE_NAME: String = "my_queue"
    val EXCHANGE_NAME: String = "my_exchange"
}


open class Sender {


    fun send() {

        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.username = "user"
        factory.password = "password"

        val connection = factory.newConnection()
        val channel = connection.createChannel()
        val message: String = "hello, world"


        //channel.queueDeclare(MsgArgs.QUEUE_NAME, false, false, false, null)
        channel.exchangeDeclare(MsgArgs.EXCHANGE_NAME, "fanout")


        for (i in 1..10) {
            channel.basicPublish(MsgArgs.EXCHANGE_NAME, "", null, message.toByteArray())
        }

        println("[SENDER]:[SENT][MESSAGE] -- '$message'")

        channel.close()
        connection.close()
    }


}

fun main(args: Array<String>) {

    Sender().send()

}