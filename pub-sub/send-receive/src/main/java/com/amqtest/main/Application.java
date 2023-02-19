package com.amqtest.main;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.JMSException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.component.amqp.AMQPConfiguration;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;


/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

    private String parameters = "amqp.idleTimeout=120000&failover.maxReconnectAttempts=1&jms.prefetchPolicy.all=1&jms.forceAsyncAcks=true";
    private String uri = "failover://(amqp://localhost:61616)";
    private int maxConnections = 20;
    private int concurrentConsumers = 20;
    private Boolean transacted = true;
    private String user="admin";
    private String password="admin";
    protected Long beforeSend, afterSend;

    private static char[] msgBody = new char[6*1024];
    static {
        for (int i=0;i<6*1024;i++)
            msgBody[i]='a';
    }

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configure() throws Exception {

        //APP 4 consume message from topic
        from("amqp-out:topic:t.test.out::test.t.test.out")
            .id("APP4")
            .to("log:${headers}")
        ;
        
      //APP 3 consume message from topic
        from("amqp-out:topic:t.test.out::falcon.t.test.out")
            .id("APP3")
            .to("log:${headers}")
        ;

        //APP 2 consume message from route
        from("amqp-in:queue:q.test.in?cacheLevelName=CACHE_CONSUMER")
            .id("APP2")
            .to("amqp-out:topic:t.test.out")
        ;


        //produce messages to queue - APP1 main    
        from("{{timer.url}}")
            .id("APP1")

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
                .to("amqp-in:queue:q.test.in")
                .end()

             .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                      afterSend = System.currentTimeMillis();
                      exchange.getIn().setHeader("elapsed","Elapsed time: " + (afterSend - beforeSend) + " milliseconds!");                    
                }
             })
            .log("messages sent! ${headers.elapsed}")
        ;
    }

    @Bean(name = "amqp-in")
    AMQPComponent amqpComponentIN() throws JMSException, URISyntaxException {

        System.out.println("Creating AMQP COMPONENT IN");
        URI brokerURI = new URI(uri + "?" + parameters);

        JmsConnectionFactory qpidjmscf = new JmsConnectionFactory(user, password, brokerURI);
        
        JmsPoolConnectionFactory jmspcf = new JmsPoolConnectionFactory();
        jmspcf.setConnectionFactory(qpidjmscf);
        jmspcf.setMaxConnections(maxConnections);

        AMQPConfiguration amqpConfig = new AMQPConfiguration();
        amqpConfig.setConnectionFactory(jmspcf);
        amqpConfig.setConcurrentConsumers(concurrentConsumers);
        amqpConfig.setTransacted(transacted);
        
        return new AMQPComponent(amqpConfig);
    }
    
    @Bean(name = "amqp-out")
    AMQPComponent amqpComponentOUT() throws JMSException, URISyntaxException {

        System.out.println("Creating AMQP COMPONENT OUT");
        URI brokerURI = new URI(uri + "?" + parameters);

        JmsConnectionFactory qpidjmscf = new JmsConnectionFactory(user, password, brokerURI);
        
        JmsPoolConnectionFactory jmspcf = new JmsPoolConnectionFactory();
        jmspcf.setConnectionFactory(qpidjmscf);
        jmspcf.setMaxConnections(maxConnections);
        
        AMQPConfiguration amqpConfig = new AMQPConfiguration();
        amqpConfig.setConnectionFactory(jmspcf);
        amqpConfig.setConcurrentConsumers(concurrentConsumers);
        amqpConfig.setTransacted(transacted);
        
        return new AMQPComponent(amqpConfig);
    }
}
