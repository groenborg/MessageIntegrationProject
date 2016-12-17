package components.rulebase

import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.Action
import javax.xml.ws.RequestWrapper


@WebService(name = "GetRulesService", targetNamespace = "http://loanbrokerrulebaseservice.mycompany.com/")
interface GetRulesService {

    val rules: String

}