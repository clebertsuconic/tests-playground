package com.amqtest.main;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.JMSException;

import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.component.amqp.AMQPConfiguration;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AMQPComponentFactory {

    private String parameters = "amqp.idleTimeout=120000&failover.maxReconnectAttempts=1&jms.prefetchPolicy.all=1000&jms.forceAsyncAcks=true";
    private String urib1  = "failover://(amqp://localhost:61616)";
    private String urib2 = "failover://(amqp://localhost:61617)";
    private int maxConnections = 20;
    private int concurrentConsumers = 20;
    private Boolean transacted = true;
    private String user="artemis";
    private String password="artemis";
    

    @Bean(name = "amqp-b1")
    AMQPComponent amqpComponentIn() throws JMSException, URISyntaxException {

        System.out.println("Creating AMQP B1 COMPONENT");
        URI brokerURI = new URI(urib1 + "?" + parameters);

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

    @Bean(name = "amqp-b2")
    AMQPComponent amqpComponentOut() throws JMSException, URISyntaxException {

        System.out.println("Creating AMQP B2 COMPONENT");
        URI brokerURI = new URI(urib2 + "?" + parameters);

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
