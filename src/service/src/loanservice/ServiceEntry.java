package loanservice;

import example.HelloWorld;
import javax.xml.ws.Endpoint;


public class ServiceEntry {

    public static void main(String[] argv) {
        Object implementor = new LoanRequest();
        String address = "http://localhost:9000/LoanRequest";
        Endpoint.publish(address, implementor);
    }
}
