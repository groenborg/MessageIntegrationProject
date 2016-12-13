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
    val RECIPIENT_LIST = "rl_queue"
    val TRANSLATOR_BANK1 = "b1_queue"
    val TRANSLATOR_BANK2 = "b2_queue"
    val TRANSLATOR_BANK3 = "b3_queue"
    val TRANSLATOR_BANK4 = "b4_queue"
}


class MsgFactory {

    companion object {

        val host: String = "localhost"
        val username: String = "user"
        val password: String = "password"
        val factory = ConnectionFactory()

        fun buildMessageConnector(): Connector {
            factory.host = host
            factory.username = username
            factory.password = password
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            return Connector(connection, channel)
        }

    }
}


class Connector(var connection: Connection, var channel: Channel) {

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