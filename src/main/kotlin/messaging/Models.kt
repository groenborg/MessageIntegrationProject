package messaging

/***
 * Request Object for Messages
 */
class RequestObject {

    constructor()

    constructor(ssn: String?, interestRate: String?, bankName: String?) {
        this.ssn = ssn
        this.interestRate = interestRate
        this.bankName = bankName

    }

    constructor(ssn: String?, amount: String?, currency: String?, duration: String?, creditScore: String?) {
        this.ssn = ssn
        this.amount = amount
        this.currency = currency
        this.duration = duration
        this.creditScore = creditScore
    }

    @JvmField var ssn: String? = ""
    @JvmField var amount: String? = ""
    @JvmField var currency: String? = ""
    @JvmField var duration: String? = ""
    @JvmField var creditScore: String? = ""
    @JvmField var interestRate: String? = ""
    @JvmField var bankName: String? = ""
}


class AggRequest {
    @JvmField var ssn: String? = null
    @JvmField var numOfBanks: String? = null
}

class RuleRequestObject {

    constructor()

    constructor(ssn: String?, amount: String?, currency: String?, duration: String?, creditScore: String?){
        this.ssn = ssn
        this.amount = amount
        this.currency = currency
        this.duration = duration
        this.creditScore = creditScore
    }

    constructor(ssn: String?, amount: String?, currency: String?, duration: String?, creditScore: String?, rule: Array<rule>) {
        this.ssn = ssn
        this.amount = amount
        this.currency = currency
        this.duration = duration
        this.creditScore = creditScore
        this.rule = rule
    }

    @JvmField var ssn: String? = ""
    @JvmField var amount: String? = ""
    @JvmField var currency: String? = ""
    @JvmField var duration: String? = ""
    @JvmField var creditScore: String? = ""
    @JvmField var rule: Array<rule>? = arrayOf()
}

class RuleObject {
    @JvmField var rule: Array<rule>? = null
}

class rule {
    @JvmField var min: String? = ""
    @JvmField var max: String? = ""
    @JvmField var bank: Array<bank>? = arrayOf()
}

class LoanOffer {
    @JvmField var ssn: String? = null
    @JvmField var interestRate: String? = null
    @JvmField var bankName: String? = null
}

class bank {
    @JvmField var bankno: String? = ""
}

