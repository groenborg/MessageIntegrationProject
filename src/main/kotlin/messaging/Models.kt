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
    @JvmField var interestRate: String? = ""

}

class RuleRequest {

}

class RuleObject {


}

class JsonLoanRequest {
    @JvmField var ssn: String? = null
    @JvmField var interestRate: String? = null
}