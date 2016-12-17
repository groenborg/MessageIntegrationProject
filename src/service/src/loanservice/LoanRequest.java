package loanservice;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import messaging.Connector;
import messaging.EXCHANGE;
import messaging.MsgFactory;
import messaging.RequestObject;
import utils.XMLParser;

import javax.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface = "loanservice.ILoanRequest")
public class LoanRequest implements ILoanRequest {

    private Connector connector = MsgFactory.Companion.buildMessageConnector();


    @Override
    public String requestLoan(String ssn, int amount, String currency, int duration) {


        //Lav alt det jazz som får request til at blive XML som enricherne skal bruge
        RequestObject obj = new RequestObject();
        XMLParser<RequestObject> parser = new XMLParser<RequestObject>(RequestObject.class);
        String xmlObject = parser.toXML(obj);


        //For at vi kan få vores message igen til sidst, laver vi en kanal med ssn som severity
        // og queue og binder det til default exchange
        Channel c = connector.getChannel();
        connector.declareQueue("ssn", true);
        connector.bindQueueToExchange("ssn", EXCHANGE.INSTANCE.getDEFAULT(), new String[]{"ssn"});
        QueueingConsumer consumer = new QueueingConsumer(c);


        try {
            // så starter jehg consumeren
            c.basicConsume("ssn",true,consumer);

            // Denne linie er magien.. Det er et synkront kald, så den venter til der kommer en besked
            // Der skulle gerne kun komme en, for den har en unik kanal til sig selv som
            // aggregatoren sender til!!
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            // få beskeden ned clienten som har requested
            String message = new String(delivery.getBody());


        } catch (Exception e) {
            System.out.println("Could not wait");
        }


        return "hello world";
    }
}
