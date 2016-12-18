package messaging

import components.aggregator.Aggregator
import components.creditscore.CreditEnricher
import components.normalizer.Normalizer
import components.rulebase.RuleEnricher
import components.recipientlist.RecipientList
import components.recipientlist.BankTranslator1
import components.recipientlist.BankTranslator2
import components.recipientlist.BankTranslator3
import components.recipientlist.BankTranslator4

fun main(args: Array<String>) {


    CreditEnricher().bindQueue("credit").startConsume()
    RuleEnricher().bindQueue("rule").startConsume()
    RecipientList().bindQueue("recipient")
    BankTranslator1().bindQueue("translator1")
    BankTranslator2().bindQueue("translator2")
    BankTranslator3().bindQueue("translator3")
    BankTranslator4().bindQueue("translator4")


    //Normalizer().bindQueue("").startConsume()

    Aggregator().bindQueue("agg").startConsume()
    Normalizer().bindQueue("normalizer").startConsume()

}


