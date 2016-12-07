import javax.xml.bind.annotation.*;

@XmlRootElement(name = "getServerNameResponse", namespace = "http://ws.mkyong.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getServerNameResponse", namespace = "http://ws.mkyong.com/")
public class GetServerNameResponse {
    @XmlElement(name = "return", namespace = "")
    private String _return;

    /**
     * @return returns String
     */
    public String getReturn() {
        return this._return;
    }

    /**
     * @param _return the value for the _return property
     */
    public void setReturn(String _return) {
        this._return = _return;
    }

}
