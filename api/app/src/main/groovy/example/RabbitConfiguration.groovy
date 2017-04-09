package example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RabbitConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RabbitConfiguration.class)

	@Autowired
	Environment env

	@Bean String queueHost() {
		env.getProperty("queue.host")
	}

	@Bean String queueUser() {
		env.getProperty("queue.user")
	}

	@Bean String queuePwd() {
		env.getProperty("queue.pwd")
	}

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

	@Bean ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(queueHost())
		connectionFactory.setUsername(queueUser())
		connectionFactory.setPassword(queuePwd())
		return connectionFactory
	}

	@Bean RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory())
	}

	@Bean Queue queue() {
		return new Queue(queueName(), false)
	}
	
}
