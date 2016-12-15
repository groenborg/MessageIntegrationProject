package loanservice;


import javax.jws.WebService;

@WebService(endpointInterface = "loanservice.ILoanRequest")
public class LoanRequest implements ILoanRequest {


    @Override
    public String requestLoan(String ssn, int amount, String currency, int duration) {
        return "hello world";
    }
}
