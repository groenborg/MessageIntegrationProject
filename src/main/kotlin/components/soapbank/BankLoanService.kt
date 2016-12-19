package components.soapbank

import java.net.MalformedURLException
import java.net.URL
import javax.xml.namespace.QName
import javax.xml.ws.Service
import javax.xml.ws.WebEndpoint
import javax.xml.ws.WebServiceClient
import javax.xml.ws.WebServiceException

/**
 * Created by Skroget on 19/12/2016.
 */


@WebServiceClient(name = "BankLoanService", targetNamespace = "http://soapbank.cphbusiness.com/", wsdlLocation = "http://localhost:8080/SOAPBank/BankLoanService?WSDL")
class BankLoanService : Service(BankLoanService.__getWsdlLocation(), BankLoanService.BANKLOANSERVICE_QNAME) {

    val bankLoanServicePort: BankLoanIF
        @WebEndpoint(name = "BankLoanServicePort")
        get() = super.getPort(QName("http://soapbank.cphbusiness.com/", "BankLoanServicePort"), BankLoanIF::class.java)

    companion object {

        private val BANKLOANSERVICE_WSDL_LOCATION: URL?
        private val BANKLOANSERVICE_EXCEPTION: WebServiceException?
        private val BANKLOANSERVICE_QNAME = QName("http://soapbank.cphbusiness.com/", "BankLoanService")

        init {
            var url: URL? = null
            var e: WebServiceException? = null
            try {
                url = URL("http://localhost:8080/SOAPBank/BankLoanService?WSDL")
            } catch (ex: MalformedURLException) {
                e = WebServiceException(ex)
            }

            BANKLOANSERVICE_WSDL_LOCATION = url
            BANKLOANSERVICE_EXCEPTION = e
        }

        private fun __getWsdlLocation(): URL? {
            if (BANKLOANSERVICE_EXCEPTION != null) {
                throw BANKLOANSERVICE_EXCEPTION
            }
            return BANKLOANSERVICE_WSDL_LOCATION
        }

        /*@JvmStatic fun main(args: Array<String>) {
            val s = BankLoanService()
            val p = s.bankLoanServicePort
            System.out.println(p.getInterestRate("240790-1285", 500, 10000, 12))
        }*/
    }

}
