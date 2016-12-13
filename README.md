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


###Setting up the project

clone the github repository to your local machine. 

run `./gradlew build` or `gradle build` if your have gradle installed in
the terminal. That should fetch the dependencies and compile the kotlin

Next we need to set up the docker image for rabbit MQ

run `docker pull rabbitmq:3.6.6-management`. When the image is done
downloading start the image using: 

`docker run -d -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password --name msg-app rabbitmq:3.6.6-management`

This will setup the container with a default user with username: `user` and
password:`password`, and bind rabbitmq's message port and web interface port.

You can now access the RabbitMQ dashboard on: `localhost:15672`

####next step
When the docker image is runnig (`docker ps` - Should list `msg-app`),
you can run the examples.
 
 
###Using Gradle = Deprecated
If you do not want to run the files in your IDE, I have predefined two gradle tasks
which starts the sender and the receiver. These are of course only present in the latest commit.

**With gradle wrapper**

`./gradlew startSender` and `./gradlew startRecv`


**With local gradle**

`gradle startSender` and `gradle startRecv`

###I have made severe changes

I have created a factory that contains all necessary things to make the connetions

And a class for the 3 standard actions: Declare A queue, Bind the Queue and Start consuming

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
