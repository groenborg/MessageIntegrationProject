package components.rulebase

import java.net.MalformedURLException
import java.net.URL
import javax.xml.namespace.QName
import javax.xml.ws.Service
import javax.xml.ws.WebEndpoint
import javax.xml.ws.WebServiceClient
import javax.xml.ws.WebServiceException

/**
 * Created by Skroget on 16/12/2016.
 */

@WebServiceClient(name = "GetRulesService", targetNamespace = "http://loanbrokerrulebaseservice.mycompany.com/", wsdlLocation = "http://localhost:8080/LoanBrokerRuleBaseService/GetRulesService?WSDL")
class GetRulesService_Service : Service(GetRulesService_Service.__getWsdlLocation(), GetRulesService_Service.GETRULESSERVICE_QNAME) {

    val getRulesServicePort: GetRulesService
        @WebEndpoint(name = "GetRulesServicePort")
        get() = super.getPort(QName("http://loanbrokerrulebaseservice.mycompany.com/", "GetRulesServicePort"), GetRulesService::class.java)

    companion object {

        private val GETRULESSERVICE_WSDL_LOCATION: URL?
        private val GETRULESSERVICE_EXCEPTION: WebServiceException?
        private val GETRULESSERVICE_QNAME = QName("http://loanbrokerrulebaseservice.mycompany.com/", "GetRulesService")

        init {
            var url: URL? = null
            var e: WebServiceException? = null
            try {
                url = URL("http://localhost:8080/LoanBrokerRuleBaseService/GetRulesService?WSDL")
            } catch (ex: MalformedURLException) {
                e = WebServiceException(ex)
            }

            GETRULESSERVICE_WSDL_LOCATION = url
            GETRULESSERVICE_EXCEPTION = e
        }

        private fun __getWsdlLocation(): URL? {
            if (GETRULESSERVICE_EXCEPTION != null) {
                throw GETRULESSERVICE_EXCEPTION
            }
            return GETRULESSERVICE_WSDL_LOCATION
        }
    }
}