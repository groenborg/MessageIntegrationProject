import loanservice.LoanRequest;

import javax.jws.HandlerChain;
import javax.jws.WebService;


public class SoapService {

    public static void main(String[] args) {
        LoanRequest r = new LoanRequest();

        String s = r.requestLoan("sdsd", 2, "dkk", 20875);


        System.out.println(s);


    }
}
