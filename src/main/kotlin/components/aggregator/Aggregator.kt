package components.aggregator

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import components.IMessageComponent
import messaging.*
import utils.XMLParser
import java.util.*


class Aggregator : IMessageComponent {

    var loanOffers: MutableMap<String, MutableList<LoanOffer>> = HashMap()
    var aggregates: MutableMap<String, Int> = mutableMapOf()

    val loanOfferParser = XMLParser(LoanOffer::class.java)
    val aggRequestParser = XMLParser(AggRequest::class.java)

    val connector = MsgFactory.buildMessageConnector()
    val exchange = EXCHANGE.DEFAULT
    val queue = QUEUES.AGGREGATOR

    val nextSeverity = "endpoint"


    override fun bindQueue(severity: String): IMessageComponent {
        connector.declareQueue(queue, true)
        connector.bindQueueToExchange(queue, exchange, severity = arrayOf(severity))
        return this
    }

    override fun startConsume() {
        val consumer = object : DefaultConsumer(connector.channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                val message = String(body!!, Charsets.UTF_8)
                componentAction(message)
            }
        }
        connector.channel.basicConsume(queue, true, consumer)
        println("[AGGREGATOR]: now listening")
    }

    /**
     * Distributes messages based on types
     */
    override fun componentAction(msg: String) {
        println(msg)

        if (isAggMessage(msg)) {
            println("AGG MESSAGE")
            handleAggregate(aggRequestParser.fromXML(msg))
        } else {
            println("OFFER MESSAGE")
            handleLoanOffer(loanOfferParser.fromXML(msg))
        }
    }


    /**
     * Handles offer messages
     *
     * If an aggMessage exists we validate the exit conditions
     * and send
     *
     * if not we add the offers to the queue
     */
    fun handleLoanOffer(offer: LoanOffer) {
        val ssn = offer.ssn!!
        if (aggregates.containsKey(ssn)) {
            val numOfbanks = aggregates[ssn]

            loanOffers[ssn]!!.add(offer)

            if (numOfbanks!! <= loanOffers[ssn]!!.size) {
                sendAndClear(ssn)
            }
        } else {
            //no agg message arrived yet
            if (loanOffers.containsKey(ssn)) {
                loanOffers[ssn]!!.add(offer)
            } else {
                loanOffers.put(ssn, mutableListOf(offer))
            }
        }

        for ((k, v) in aggregates) {
            println("key: $k val: $v")
        }

        for ((k, v) in loanOffers) {
            println("key: $k size: " + v.size)

        }
    }


    /**
     * Handles the aggregate message
     *
     * if none exists, we add it to the map with an empty
     * array of loanOffers
     *
     * if there are more or equal offers to numOfBanks
     * we send the request to the endpoint
     */
    fun handleAggregate(agg: AggRequest) {

        val numOfBanks = agg.numOfBanks!!.toInt()
        val ssn = agg.ssn!!
        if (aggregates.containsKey(ssn)) {

            if (numOfBanks <= loanOffers[ssn]!!.size) {
                sendAndClear(ssn)
            }
        } else {
            aggregates.put(agg.ssn!!, agg.numOfBanks!!.toInt())
            if (loanOffers.containsKey(agg.ssn!!)) {
                //Handle if loan offers arrives before agg
                if (numOfBanks <= loanOffers[ssn]!!.size) {
                    sendAndClear(ssn)
                }
            } else {
                loanOffers.put(agg.ssn!!, mutableListOf())
            }

        }
        for ((k, v) in aggregates) {
            println("key: $k val: $v")
        }

        for ((k, v) in loanOffers) {
            println("key: $k size: " + v.size)

        }
    }

    /**
     * Sends the message and removes the entries from the list
     */
    fun sendAndClear(key: String) {
        val parser = XMLParser(Array<LoanOffer>::class.java)
        val message = parser.toXML(loanOffers[key]!!.toTypedArray())

        connector.basicPublish(exchange, arrayOf(key), message)

        aggregates.remove(key)
        loanOffers.remove(key)
    }


    /**
     * Validates if it's an aggregate Message from the splitter
     */
    fun isAggMessage(message: String): Boolean {
        return aggRequestParser.fromXML(message).numOfBanks != null
    }


}
