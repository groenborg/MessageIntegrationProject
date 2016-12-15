package messaging

import components.CreditScore.CreditEnricher
import components.Normalizer
import components.RuleEnricher


fun main(args: Array<String>) {


    //RuleEnricher().bindQueue("rule").startConsume()
    CreditEnricher().bindQueue("credit").startConsume()
    //Normalizer().bindQueue("").startConsume()

}


