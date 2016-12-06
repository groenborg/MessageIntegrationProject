#Integration Assignment 

Assignment created for the system integration course, using Kotlin, rabbit MQ, 
gradle and Docker

###How to access examples

I have created 3 examples which uses rabbit MQ which can be accessed using 
the tags on github. The Examples are: 

1. Routing
1. Publish/Subscribe
1. HelloWorld

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
you can run the examples 




