package messaging

import components.aggregator.Aggregator
import components.creditscore.CreditEnricher
import components.normalizer.Normalizer
import components.RuleEnricher


fun main(args: Array<String>) {


    //RuleEnricher().bindQueue("rule").startConsume()
    //CreditEnricher().bindQueue("credit").startConsume()

    //Normalizer().bindQueue("").startConsume()

    Aggregator().bindQueue("agg").startConsume()
    Normalizer().bindQueue("normalizer").startConsume()

}


