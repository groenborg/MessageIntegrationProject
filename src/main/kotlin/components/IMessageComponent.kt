package components


interface IMessageComponent {

    /***
     * This is where we create and bind the queue
     */
    fun bindQueue(severity: String): IMessageComponent

    /***
     * This is here we handle
     */
    fun startConsume()


    /**
     * Method for implementing the components logic
     */
    fun componentAction(msg: String)

}