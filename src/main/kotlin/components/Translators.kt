package components

/**
 * Created by simon on 09/12/2016.
 */


class XmlTranslator : IMessageComponent{
    override fun bindQueue(severity: String): IMessageComponent {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startConsume() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun componentAction(msg : String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class JSONTranslator : IMessageComponent{

    override fun bindQueue(severity: String): IMessageComponent {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startConsume() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun componentAction(msg : String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}