package components.soapbank

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlType

/**
 * Created by Skroget on 19/12/2016.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getInterestRate", propOrder = arrayOf("ssn", "creditscore", "amount", "duration"))
class GetInterestRate {

    protected var ssn: String? = null
    protected var creditscore: Int = 0
    protected var amount: Double = 0.toDouble()
    protected var duration: Int = 0

}