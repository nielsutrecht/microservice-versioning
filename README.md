# Micro service versioning example code

[Blog post on this subject](http://niels.nu/blog/2016/microservice-versioning.html)

This is an example project where we show two ways of solving the issue of protocol version inside the REST services.
Both these methods depend on the client telling the server which protocol version they expect. This is done through
a custom X-Protocol-Version header. If the client doesn't supply this header a 400-error will be thrown.

## View based versioning

Spring Boot uses Jackson internally and Jackson supports the concept of views on an object. A common scenario of these
kinds of views is that you want to expose only a part of a Person's details for unauthorized users and more information
for authorized / friends-of users. Similar to how for example your e-mail on your Facebook profile can be made available
only to your close friends. 

More info on how views work can be found [here](https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring).

## Adapter based versioning

Another demonstrated approach is one where we supply adapter functions for views. An adapter simply wraps the most current
response object (Order) in an adapter that translates it to the older version. This creates a simply and clear 'downgrade'
path for response objects.

## Building and running

The project can be built using Maven with mvn clean install and can then executed with "java -jar 
target/microservice-versioning<version>.jar