import com.rabbitmq.client.ConnectionFactory


object MsgArgs {
    val QUEUE_NAME: String = "my_queue"
    val EXCHANGE_NAME: String = "routing_exchange"
}


open class Sender {


    fun send(severity: String) {

        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.username = "user"
        factory.password = "password"

        val connection = factory.newConnection()
        val channel = connection.createChannel()
        val message: String = "hello, world"


        channel.exchangeDeclare(MsgArgs.EXCHANGE_NAME, "direct")


        for (i in 1..10) {
            channel.basicPublish(MsgArgs.EXCHANGE_NAME, severity, null, message.toByteArray())
        }

        println("[SENDER]:[SENT][MESSAGE] -- '$message'")

        channel.close()
        connection.close()
    }


}

fun main(args: Array<String>) {

    Sender().send("black")
    Sender().send("only")
    Sender().send("info")

}