package loanservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ILoanRequest {

    @WebMethod
    @WebResult(name = "xml object")
    String requestLoan(@WebParam(name = "ssn") String ssn, @WebParam(name = "amount") int amount,
                       @WebParam(name = "currency") String currency, @WebParam(name = "duration") int duration);

}
