package components.creditscore

/**
 * Created by Skroget on 13/12/2016.
 */


import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlType


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "creditScore", propOrder = arrayOf("ssn"))
class CreditScore {

    var ssn = -1

}
