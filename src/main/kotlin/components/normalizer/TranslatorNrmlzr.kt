package components.normalizer

import com.google.gson.Gson
import messaging.JsonLoanRequest
import messaging.RequestObject
import utils.XMLParser

class JsonTranslator() {

    val jsonParser = Gson()
    val xmlParser = XMLParser(RequestObject::class.java)

    fun <T> isTypeOfJson(string: String, type: Class<T>): T {
        return jsonParser.fromJson(string, type)
    }

    fun isJsonRequest(jsonString: String): Boolean {
        try {
            val resultObj = jsonParser.fromJson(jsonString, JsonLoanRequest::class.java)
            return resultObj.ssn != null && resultObj.interestRate != null
        } catch (e: Exception) {
            return false
        }


    }

    fun convertToXml(jsonString: String): String {
        val jsonObj = jsonParser.fromJson(jsonString, RequestObject::class.java)
        return xmlParser.toXML(jsonObj)
    }
}

class XmlTranslator {

    val xmlParser = XMLParser(RequestObject::class.java)
    var responseXmlParser = XMLParser(JsonLoanRequest::class.java)

    fun convertXml(text: String): String {
        val obj = responseXmlParser.fromXML(text)
        val xmlObj = RequestObject()
        xmlObj.ssn = obj.ssn
        xmlObj.interestRate = obj.interestRate
        return xmlParser.toXML(xmlObj)

    }
}