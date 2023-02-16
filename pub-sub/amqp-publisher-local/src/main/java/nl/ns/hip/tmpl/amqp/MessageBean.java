package nl.ns.hip.tmpl.amqp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Headers;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageBean {

	private static final String HOST_NAME;
	private static final List<String> MESSAGE_POOL = new ArrayList<String>();
	private static int MESSAGE_POOL_SIZE;
	private static Logger LOG = LoggerFactory.getLogger("nl.ns.hip.tmpl");
	private static int LOGGING_INTERVAL;
	private static AtomicLong incrementer = new AtomicLong(1);
	private static Random random = new Random();

	private String uuid;
	private String hostName;
	private Date time;
	private long counter;
	private String payload;

	static {
		InetAddress localHost = null;
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		HOST_NAME = (localHost != null ? localHost.getHostName() : "Unknown");
	}

	public MessageBean createMessage() {
		MessageBean message = new MessageBean();
		message.uuid = UUID.randomUUID().toString();
		message.hostName = HOST_NAME;
		message.time = new Date();
		message.counter = incrementer.getAndIncrement();
		message.payload = MESSAGE_POOL.get(random.nextInt(MESSAGE_POOL_SIZE));
		if (LOG.isInfoEnabled() && (message.counter % LOGGING_INTERVAL == 0)) {
			LOG.info(String.format("Published %s messages", message.counter));
		}
		return message;
	}

	public String getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return String.format("Message: {%s, %s, %s, %s}", uuid, hostName, df.format(time), counter);
	}

	@Autowired
	public void setPayloadSize(@Value("${payload.size:10}") int payloadSize, @Value("${messagePool.size:1000}") int messagePoolSize) {
		MESSAGE_POOL_SIZE = messagePoolSize;
		for (int i = 0; i < messagePoolSize; i++) {
			MESSAGE_POOL.add(RandomStringUtils.randomAlphanumeric(payloadSize));
		}
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

	public void setHeaders(@Headers Map<String, Object> headers, MessageBean body) {
		headers.put("TMPL_uuid", body.uuid);
		headers.put("TMPL_hostName", body.hostName);
		headers.put("TMPL_time", body.time.getTime());
		headers.put("TMPL_counter", body.counter);
		if (LOG.isTraceEnabled()) {
			LOG.trace("Headers: " + headers + "; Body: " + body);
		}
	}
}
