package messaging

/***
 * Request Object for Messages
 */
class RequestObject {

    constructor()

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
}


class AggRequest {
    @JvmField var ssn: String? = null
    @JvmField var numOfBanks: String? = null
}

class RuleRequestObject {

    constructor(ssn: String?, amount: String?, currency: String?, duration: String?, creditScore: String?, rules: RuleObject?) {
        this.ssn = ssn
        this.amount = amount
        this.currency = currency
        this.duration = duration
        this.creditScore = creditScore
        this.rules = rules
    }

    @JvmField var ssn: String? = ""
    @JvmField var amount: String? = ""
    @JvmField var currency: String? = ""
    @JvmField var duration: String? = ""
    @JvmField var creditScore: String? = ""
    @JvmField var rules: RuleObject? = null
}

class RuleObject {
    @JvmField var rule: Array<Rule>? = null
}


class Rule {
    @JvmField var min: String? = ""
    @JvmField var max: String? = ""
    @JvmField var bank: Array<Bank>? = null
}

class LoanOffer {
    constructor(ssn: String?, interestRate: String?, bankName: String?) {
        this.ssn = ssn
        this.interestRate = interestRate
        this.bankName = bankName
    }
    @JvmField var ssn: String? = null
    @JvmField var interestRate: String? = null
    @JvmField var bankName: String? = null
}

class Bank {
    @JvmField var bankNo: String? = ""
}

