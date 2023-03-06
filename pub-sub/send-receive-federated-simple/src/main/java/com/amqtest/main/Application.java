package com.amqtest.main;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder  {

    protected Long beforeSend, afterSend;

    private static char[] msgBody = new char[6*1024];

    static {
        for (int i=0;i<6*1024;i++)
            msgBody[i]='a';
    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configure() throws Exception {

        System.out.println("Initializing routes!");

        // Consume the federated message - B1
        from("amqp-b1:queue:test.queue?cacheLevelName=CACHE_NONE")
            .id("federated-consumer-route-b1")
            .log("Message received B1: ${headers}")
        ;
        // Consume the federated message - B2
        from("amqp-b2:queue:test.queue?cacheLevelName=CACHE_NONE")
            .id("federated-consumer-route-b2")
            .log("Message received B2: ${headers}")
        ;


        // Produce messages to queue - B1
        from("{{timer.url}}")
            .id("federated-producer-route-b1")

            //now we send, taking elapsed time
            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                      beforeSend = System.currentTimeMillis();                    
                }
             })
           
            .loop(50*1000)
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(msgBody);
                    }
                })
                .to("amqp-b1:queue:test.queue")
            .end()

            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                      afterSend = System.currentTimeMillis();
                      exchange.getIn().setHeader("elapsed","Elapsed time: " + (afterSend - beforeSend) + " milliseconds!");                    
                }
             })
            .log("Messages sent! ${headers.elapsed} ")
        ;


        // Produce messages to queue - B2
        from("{{timer.url}}")
            .id("federated-producer-route-b2")

            //now we send, taking elapsed time
            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                      beforeSend = System.currentTimeMillis();                    
                }
             })
           
            .loop(50*1000)
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(msgBody);
                    }
                })
                .to("amqp-b2:queue:test.queue")
            .end()

            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                      afterSend = System.currentTimeMillis();
                      exchange.getIn().setHeader("elapsed","Elapsed time: " + (afterSend - beforeSend) + " milliseconds!");                    
                }
             })
            .log("Messages sent! ${headers.elapsed}")
        ;

        System.out.println("Routes Initialized!");

    }
    

   
}
