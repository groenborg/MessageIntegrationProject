package components

class Normalizer : IMessageComponent {

    var connector = MsgFactory.buildMessageConnector()


    override fun bindQueue(severity: String): IMessageComponent {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startConsume() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun componentAction() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}