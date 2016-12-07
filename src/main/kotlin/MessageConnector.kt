import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

object EXHANGE {
    val DEFAULT = "routing_exchange"
}

object QUEUES {

    val ENRICHER_RULE = "er_queue"
    val ENRICHER_CREDIT = "ec_queue"
    val AGGREGATOR = "agg_queue"
    val NORMALIZER = "nmz_queue"
}


object MessageConnector {

    private val host: String = "localhost"
    private val username: String = "user"
    private val password: String = "password"
    private val factory = ConnectionFactory()

    fun getTunnel(): MessageTunnel {
        factory.host = host
        factory.username = username
        factory.password = password

        val connection = factory.newConnection()
        val channel = connection.createChannel()
        return MessageTunnel(connection, channel)
    }

}

class MessageTunnel(var connection: Connection, var channel: Channel) {

    fun bindQueueToExchange(queue: String, exchange: String, severity: Array<String>) {
        for (element in severity) {
            channel.queueBind(queue, exchange, element)
        }
    }

    fun declareQueue(name: String, durable: Boolean) {
        channel.queueDeclare(name, durable, false, false, null)
    }

    fun basicPublish(exhange: String, severity: Array<String>, message: String) {
        for (element in severity) {
            channel.basicPublish(exhange, element, null, message.toByteArray())
        }

    }
}