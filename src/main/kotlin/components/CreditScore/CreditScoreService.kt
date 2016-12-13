package components.CreditScore

import javax.xml.ws.WebServiceClient
import javax.xml.ws.WebServiceException
import java.net.MalformedURLException
import javax.xml.ws.WebEndpoint
import java.net.URL
import javax.xml.namespace.QName
import javax.xml.ws.Service





/**
 * Created by Skroget on 13/12/2016.
 */


@WebServiceClient(name = "CreditScoreService", targetNamespace = "http://service.web.credit.bank.org/", wsdlLocation = "http://139.59.154.97:8080/CreditScoreService/CreditScoreService?wsdl")
class CreditScoreService : Service(CreditScoreService.__getWsdlLocation(), CreditScoreService.CREDITSCORESERVICE_QNAME) {

    /**

     * @return
     * *     returns CreditScoreService
     */
    val creditScoreServicePort: CreditScoreServiceIF
        @WebEndpoint(name = "CreditScoreServicePort")
        get() = super.getPort(QName("http://service.web.credit.bank.org/", "CreditScoreServicePort"), CreditScoreServiceIF::class.java)

    companion object {

        private val CREDITSCORESERVICE_WSDL_LOCATION: URL?
        private val CREDITSCORESERVICE_EXCEPTION: WebServiceException?
        private val CREDITSCORESERVICE_QNAME = QName("http://service.web.credit.bank.org/", "CreditScoreService")

        init {
            var urlTmp: URL? = null
            var e: WebServiceException? = null
            try {
                urlTmp = URL("http://139.59.154.97:8080/CreditScoreService/CreditScoreService?wsdl")
            } catch (ex: MalformedURLException) {
                e = WebServiceException(ex)
            }

            CREDITSCORESERVICE_WSDL_LOCATION = urlTmp
            CREDITSCORESERVICE_EXCEPTION = e
        }

        private fun __getWsdlLocation(): URL? {
            if (CREDITSCORESERVICE_EXCEPTION != null) {
                throw CREDITSCORESERVICE_EXCEPTION
            }
            return CREDITSCORESERVICE_WSDL_LOCATION
        }

    }

}
