package messaging

/***
 * Request Object for Messages
 */
class RequestObject(@JvmField var ssn: String?, @JvmField var amount: String?, @JvmField var duration: String?, @JvmField var currency: String?) {
    @JvmField var creditScore: String = ""
}