Do make sure before running this application, shat StaticData is  mofied to user needs.

To run this vert.x app, follow steps mentioned below
1) Run Queue.groovy
   Go inside folder queue-verticle and run
   vertx run Queue.groovy -cluster -cluster-host localhost -repo vert-x.github.io

2) Run Consumer.groovy
   Go inside folder consumer-verticle and run	
   vertx run consumer-verticle/Consumer.groovy -cluster -cluster-host localhost -cluster-port 25501

3) Run Producer.groovy	
   Go inside folder producer-verticle and run   
   vertx run Producer.groovy -cluster -cluster-host localhost -cluster-port 25502

4) And checkout put on localhost:8080
