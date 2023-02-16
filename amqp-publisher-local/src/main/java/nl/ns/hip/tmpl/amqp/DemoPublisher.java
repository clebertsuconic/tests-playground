package nl.ns.hip.tmpl.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * A spring-boot application that includes a Camel route builder to setup the
 * Camel routes
 */
@SpringBootApplication
@ComponentScan({"nl.ns.hip"})
@ImportResource({ "classpath:spring/camel-context.xml" })
public class DemoPublisher {
	public static void main(String[] args) {
		SpringApplication.run(DemoPublisher.class, args);
	}
}
