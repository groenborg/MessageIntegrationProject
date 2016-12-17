package messaging

import com.rabbitmq.client.AMQP


class RemoteEntryRequestTester {

    var connector = MsgFactory.buildRemoteConnector()
    val xmlMessage = "<LoanRequest><ssn>12345678</ssn><creditScore>685</creditScore><loanAmount>1000.0</loanAmount><loanDuration>1973-01-01 01:00:00.0 CET</loanDuration></LoanRequest>"
    val jsonMessage = "{\"ssn\":1605789787,\"creditScore\":598,\"loanAmount\":10.0,\"loanDuration\":360}"


    fun test() {
        val replyQueue = connector.channel.queueDeclare(QUEUES.CPH_REPLY_QUEUE, true, false, false, null).queue
        val properties = AMQP.BasicProperties.Builder()
                .replyTo(replyQueue)
                .build()

        connector.basicRequestReplyPublish(EXCHANGE.CPH_XML_BANK, properties, xmlMessage)

        connector.channel.close()
        connector.connection.close()

    }
}

fun main(args: Array<String>) {
    val b = RemoteEntryRequestTester()
    b.test()
}