package components.normalizer

import com.google.gson.Gson
import messaging.LoanOffer
import utils.XMLParser

class JsonTranslator() {

    val jsonParser = Gson()
    val xmlParser = XMLParser(LoanOffer::class.java)

    fun <T> isTypeOfJson(string: String, type: Class<T>): T {
        return jsonParser.fromJson(string, type)
    }

    fun isJsonRequest(jsonString: String): Boolean {
        try {
            val resultObj = jsonParser.fromJson(jsonString, LoanOffer::class.java)
            return resultObj.ssn != null && resultObj.interestRate != null
        } catch (e: Exception) {
            return false
        }
    }

    fun convertToXml(jsonString: String): String {
        val jsonObj = jsonParser.fromJson(jsonString, LoanOffer::class.java)
        jsonObj.bankName = "cphJsonBank"
        return xmlParser.toXML(jsonObj)
    }
}

class XmlTranslator {
    var responseXmlParser = XMLParser(LoanOffer::class.java)


    fun convertXml(text: String): String {
        val obj = responseXmlParser.fromXML(text)
        obj.bankName = "cphXmlBank"
        return responseXmlParser.toXML(obj)
    }
}