package loanservice;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import components.CreditScore.RequestObject;
import messaging.Connector;
import messaging.MsgFactory;
import utils.XMLParser;

import javax.jws.WebService;

@WebService(endpointInterface = "loanservice.ILoanRequest")
public class LoanRequest implements ILoanRequest {

    private Connector connector = MsgFactory.Companion.buildMessageConnector();


    @Override
    public String requestLoan(String ssn, int amount, String currency, int duration) {

        RequestObject obj = new RequestObject(ssn, "" + amount, "" + duration,currency);

        XMLParser<RequestObject> parser = new XMLParser<RequestObject>(RequestObject.class);
        String xmlObject = parser.toXML(obj);


        Channel c = connector.getChannel();
        QueueingConsumer consumer = new QueueingConsumer(c);


        try {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        } catch (InterruptedException e) {
            System.out.println("Could not wait");
        }


        return "hello world";
    }
}
