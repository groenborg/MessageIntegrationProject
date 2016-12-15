package loanservice;


import components.CreditScore.RequestObject;
import utils.XMLParser;

import javax.jws.WebService;

@WebService(endpointInterface = "loanservice.ILoanRequest")
public class LoanRequest implements ILoanRequest {


    @Override
    public String requestLoan(String ssn, int amount, String currency, int duration) {

        RequestObject obj = new RequestObject(ssn, "" + amount, "" + duration);

        XMLParser<RequestObject> d = new XMLParser<RequestObject>(RequestObject.class);
        String xmlObject = d.toXML(obj);

        System.out.println(xmlObject);

        System.out.println(ssn);
        System.out.println(amount);
        System.out.println(currency);
        System.out.println(duration);
        return "hello world";
    }
}
