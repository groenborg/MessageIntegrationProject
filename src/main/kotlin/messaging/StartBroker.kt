package messaging

import components.aggregator.Aggregator
import components.creditscore.CreditEnricher
import components.normalizer.Normalizer
import components.recipientlist.*
import components.rulebase.RuleEnricher
import components.soapbank.BankEnricher

fun main(args: Array<String>) {


    CreditEnricher().bindQueue("credit").startConsume()

    RuleEnricher().bindQueue("rule").startConsume()

    RecipientList().bindQueue("recipient").startConsume()

    BankTranslator1().bindQueue("translator1").startConsume()
    BankTranslator2().bindQueue("translator2").startConsume()
    BankTranslator3().bindQueue("translator3").startConsume()
    BankTranslator4().bindQueue("translator4").startConsume()

    BankEnricher().bindQueue("soapbank").startConsume()

    Normalizer().bindQueue("normalizer").startConsume()

    Aggregator().bindQueue("agg").startConsume()

}


