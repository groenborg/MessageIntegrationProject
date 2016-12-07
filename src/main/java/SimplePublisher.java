import javax.xml.ws.Endpoint;


public class SimplePublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/ws/server", new ServerInfo());

        System.out.println("Service is published!");
    }
}
