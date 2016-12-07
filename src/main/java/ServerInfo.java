import javax.jws.HandlerChain;
import javax.jws.WebService;

/**
 * Created by simon on 07/12/2016.
 */
@WebService
@HandlerChain(file = "handler-chain.xml")
public class ServerInfo {
    public String getServerName() {
        return "simons server";
    }

}
