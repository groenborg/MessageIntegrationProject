package messaging

import components.aggregator.Aggregator
import components.creditscore.CreditEnricher
import components.normalizer.Normalizer
import components.rulebase.RuleEnricher


fun main(args: Array<String>) {

    //CreditEnricher().bindQueue("credit").startConsume()
    //RuleEnricher().bindQueue("rule").startConsume()

    //Normalizer().bindQueue("").startConsume()

    Aggregator().bindQueue("agg").startConsume()
    Normalizer().bindQueue("normalizer").startConsume()

}


