package example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RabbitConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RabbitConfiguration.class)

	@Autowired
	Environment env
 
	@Bean String queueName() {
		env.getProperty("queue.name")
	}
 
	@Bean int queueExpire() {
		String expire = env.getProperty("queue.expire")
		if (expire.isInteger()) {
			return expire as Integer
		}
		return 0
	}
 
	@Bean Queue queue() {
		return [queueName(), false] as Queue
	}
	
}
