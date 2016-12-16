package components

import messaging.RequestObject


class Aggregator : IMessageComponent {

    var aggreagtes: Array<RequestObject> = arrayOf()

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
