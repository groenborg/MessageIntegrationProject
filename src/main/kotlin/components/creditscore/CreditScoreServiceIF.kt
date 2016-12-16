package components.creditscore

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.Action
import javax.xml.ws.RequestWrapper


/**
 * Created by Skroget on 13/12/2016.
 */

@WebService(name = "CreditScoreServiceIF", targetNamespace = "http://service.web.credit.bank.org/")
interface CreditScoreServiceIF {

    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "creditScore", targetNamespace = "http://service.web.credit.bank.org/", className = "creditscore")
    @Action(input = "http://service.web.credit.bank.org/CreditScoreService/creditScoreRequest", output = "http://service.web.credit.bank.org/CreditScoreServiceIF/creditScoreResponse")
    fun creditScore(
            @WebParam(name = "ssn", targetNamespace = "")
            ssn: String): Int

}
