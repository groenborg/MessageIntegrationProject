package messaging

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

object EXCHANGE {
    val DEFAULT = "routing_exchange"
    val CPH_XML_BANK = "cphbusiness.bankXML"
    val CPH_JSON_BANK = "cphbusiness.bankJSON"
}

object QUEUES {
    val CPH_REPLY_QUEUE = "src_reply_queue"
    val ENRICHER_RULE = "er_queue"
    val ENRICHER_CREDIT = "ec_queue"
    val ENRICHER_BANK = "eb_queue"
    val AGGREGATOR = "agg_queue"
    val NORMALIZER = "nmz_queue"
    val RECIPIENT_LIST = "rl_queue"
    val BANK_TRANSLATOR1 = "bt1_queue"
    val BANK_TRANSLATOR2 = "bt2_queue"
    val BANK_TRANSLATOR3 = "bt3_queue"
    val BANK_TRANSLATOR4 = "bt4_queue"
    val RABBIT_MQ_BANK = "rabbitmqbank_queue"
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

        fun buildRemoteConnector(): Connector {
            factory.host = "datdb.cphbusiness.dk"
            factory.username = "student"
            factory.password = "cph"
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

    fun basicPublish(exchange: String, severity: Array<String>, message: String) {
        for (element in severity) {
            channel.basicPublish(exchange, element, null, message.toByteArray())
        }

    }

    fun basicRequestReplyPublish(exchange: String, properties: AMQP.BasicProperties, message: String) {
        channel.basicPublish(exchange, "", properties, message.toByteArray())
    }

}