package nl.ns.hip.tmpl.amqp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageBean {

	private static Logger LOG = LoggerFactory.getLogger("nl.ns.hip.tmpl");
	private static int LOGGING_INTERVAL;
	private static AtomicLong incrementer = new AtomicLong(1);

	private String uuid;
	private String hostName;
	private Date time;
	private long counter;
	private String payload;

	public MessageBean createMessage(@Headers Map<String, Object> headers, @Body String body) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Headers: " + headers + "; Body: " + body);
		}
		MessageBean message = new MessageBean();
		message.uuid = (String)headers.get("TMPL_uuid");
		message.hostName = (String)headers.get("TMPL_hostName");
		message.time = new Date((Long)headers.get("TMPL_time"));
		message.counter = (Long)headers.get("TMPL_counter");
		message.payload = body;
		long receiveCounter = incrementer.getAndIncrement();
		if (LOG.isInfoEnabled() && (receiveCounter % LOGGING_INTERVAL == 0)) {
			LOG.info(String.format("Received %s messages", receiveCounter));
		}
		return message;
	}

	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return String.format("Message: {%s, %s, %s, %s}", uuid, hostName, df.format(time), counter);
	}

	@Autowired
	public void setLoggingInterval(@Value("${logging.interval:10}") int loggingInterval) {
		LOGGING_INTERVAL = loggingInterval;
	}

	public void logMessage(MessageBean body) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(body.toString());
		}
	}
}
