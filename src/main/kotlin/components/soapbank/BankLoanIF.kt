package components.soapbank

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.Action
import javax.xml.ws.RequestWrapper

/**
 * Created by Skroget on 19/12/2016.
 */

@WebService(name = "BankLoanService", targetNamespace = "http://soapbank.cphbusiness.com/")
interface BankLoanIF {

    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getInterestRate", targetNamespace = "http://soapbank.cphbusiness.com/", className = "soapBank.GetInterestRate")
    @Action(input = "http://soapbank.cphbusiness.com/BankLoanService/getInterestRateRequest", output = "http://soapbank.cphbusiness.com/BankLoanService/getInterestRateResponse")
    fun getInterestRate(
            @WebParam(name = "ssn", targetNamespace = "")
            ssn: String,
            @WebParam(name = "creditscore", targetNamespace = "")
            creditscore: Int,
            @WebParam(name = "amount", targetNamespace = "")
            amount: Double,
            @WebParam(name = "duration", targetNamespace = "")
            duration: Int): Double

}
