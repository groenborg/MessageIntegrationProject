#Integration Assignment 

Assignment created for the system integration course, using Kotlin, rabbit MQ, 
gradle and Docker

###How to access examples

I have created 3 examples which uses rabbit MQ which can be accessed using 
the tags on github. The Examples are: 

1. Routing
1. Publish/Subscribe
1. HelloWorld

When the project have been cloned you can checkout the examples
locally using `git checkout <tag name>`, eg `git checkout HelloWorld`

####RuleBase Service - https://github.com/Robertelving/RuleBaseService
The rule base is a webservice that supplies rules on routing between the different banks. The service is accessed through a contentEnricher 

####Link to SOAP based Bank
https://github.com/Robertelving/LoanBrokerXMLBank

A Soap webservice bank implementation

####Link To Loanbroker EntryPoint
https://github.com/Robertelving/LoanBroker

This is the Webservice entrypoint to the Loanbroker component. 
A client can request LoanOffers through here.


###Setting up the project

clone the github repository to your local machine. 

run `./gradlew build` or `gradle build` if your have gradle installed in
the terminal. That should fetch the dependencies and compile the kotlin

Next we need to set up the docker image for rabbit MQ

run `docker pull rabbitmq:3.6.6-management`. When the image is done
downloading start the image using: 

`docker run -d -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password --name msg-app rabbitmq:3.6.6-management`

This will setup the container with a default user with username: `user` and
password:`password`, and bind rabbitmq's xmlMessage port and web interface port.

You can now access the RabbitMQ dashboard on: `localhost:15672`

####next step
When the docker image is runnig (`docker ps` - Should list `msg-app`)


###Regarding Credit-Score and Banks

We have taken inspiration from following website about credit-scores --> [source](http://www.freescore.com/good-bad-credit-score-range.aspx)

So we are using the following table for our solution:

| Credit Score | Credit Range  | Bank number |
|:------------:|:-------------:|:-----------:|
| Excellent    | 720 and Up    | #1, #2, #3  |
| Good         | 680 to 719    | #2, #3      |
| Average      | 620 to 679    | #2, #3      |
| Poor         | 580 to 619    | #3, #4      |
| Bad          | 500 to 579    | #3, #4      |
| Miserable    | Less than 500 | #3, #4      |

In a summary:
* Bank #1 takes only Excellent customers
* Bank #2 takes all positive scores above poor
* Bank #3 basically takes everyone in
* Bank #4 takes only bad customers

We are just using these facts as a proof of concept, even though it may look differently in the real world.  

####Rule Base

When receiving rules from our Rule Base the format COULD be the following in XML:

```
 <rules>

   </rule>
     <creditscore>EXCELLENT</creditscore>
     <maxscore>800</maxscore>
     <minscore>720</minscore>
     <banks>
       <bank>1</bank>
       <bank>2</bank>
       <bank>3</bank>
     </banks>
   </rule>
   
   ...
   
 </rules>
```

### The routing logic
The recipient-list is not a pre-defined component, since you can create it on multiple ways.
It is more accurate to call the Recipient List a Message Integration Pattern.
This type of router, can send a single Message to multiple recipients.

There are two major ways to implement the Recipient List:

**1. Let the routing-specific logic happen in a separate sender:**
-- The sender knows what the recipients want.
-- The Recipients obtain the Message without discussion.
-- The sender, should always perform checkings for every Message.

**2. Let the routing-specific logic happen in each recipient:**
-- The sender sends a Message to every recipient
-- It would likely use a Public-Subscribe-Channel having Message Filters in every recipient
-- The sender enriches the Message with a list of qualified receivers.
-- If the recipient is not at the list, it will simply discard the Message.
-- A recipient should always check every Message generated

In our solution we have chosen approach #1

###Content Enricher
A content enricher is a component that uses information from the incoming message to call an external service and "enrich" the message with the new data returned from the service.
![alt text](http://www.enterpriseintegrationpatterns.com/img/DataEnricher.gif "")

###Aggregator
An aggregator is a stagefull filter, which aggregates messages which have been divide by a *splitter*
and processed by several vendors. It saves all messages until completeness conditions have been met.
  
  
  To create an aggregator a message need to contain:
  1. **Correlation identifier** - Which message does it belong to
  1. **Completeness condition** - Conditions for which the message is ready to be sent
  1. **Aggregation algorithm** - A method on how we combine individual messages
  
  In our solution, our **Aggregation algorithm** uses to maps. One for an aggregation message, and
  one for a list of offers from the banks.
  
  There are different types of strategies you can use for your completeness conditions: 
  1. **Wait for all** - Wait for all eternity for the messages to come
  1. **Timeout** - Wait for a specific amount of time and send whatever you have
  1. **External event** - Let an external event trigger your sending mechanism

###Normalizer
A normalizer is a composite component, composed of a *Router* and a *Translator*. The Router reads the
 message format and directs it to the right translator for proper translation.
 
1. A *Translator* uses a filter to convert data from one format to another
1. A *Router* Is a component which directs messages into queues based on specific requirements

###Architecture

The basic architecture is as described in the assignment. The loanbroker is composed by 5 basic components: 

![alt text](https://media.githubusercontent.com/media/groenborg/MessageIntegrationProject/develop/content/adiagram.png)

1. Enrichers - Credit & Rule
1. Recipient List
2. Translators - One for each bank; 4 in all
3. Normalizer 
4. Aggregator

The components follow the patterns after which they are named, and all written in kotlin, a jvm based language, using the rabbitmq client -v3.6.5. 

All components use the Subscribe/Emit publishing pattern described by rabbitmq [subscribe/emit](https://www.rabbitmq.com/tutorials/tutorial-four-java.html), we use a routing key/severity to send messages between each component.  NOTE: in our design we used named queues; and that is not necessary when subscribing to an exchange with a routing key. This was a design decision to keep an overview of all the channels used.  

There are some flaws in the architecture

**Splitter-aggragator coupling** - This is weird design couples the splitter and aggregator together, so they can't be exchanged easily. The splitter keeps data over how many banks a message is distributed to, and the aggregator needs that information for its *completion condition*. The banks will not relay that extra information in their loan offer responses, so we were forced to make the coupling

**Solution** 
A solution could be to make the banks more verbose and let extra information slip through. That way we can send the extra information the aggregator needs and remove the coupling. 


##Quirks
The whole setup is not ideal. There are many scenarious and funny faults in the system that was hard to find and produced
outcomes that showed the code wasn't tested properly, and made it difficult to create a generic parsing system and 
message conventions.

#####SSN fault
sending SSN to the cphbusiness banks produced some funny errors. The ssn seemes 
to be casted to an int on their service, so following the services' own ssn convention, with 10 characters, we cannot use 
ssn' with people being born on days above 21. So people who were born later than the 21th in a month.. bad for you guys.
Also if you were born before the 10th in a month so you number would have a 0 as the first character, we also get an error.

##### the '-' fault
There were also inconsistencies in the format of the ssn. Some of the services required the ssn to be formatted like this:
`xxxxxx-xxxx` but other required the format to be like this: `xxxxxxxxxx`. That also affected the design
