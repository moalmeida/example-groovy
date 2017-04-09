

package example.service


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import example.model.SMS

@Service
class PubSubQueue {

	private static final Logger log = LoggerFactory.getLogger(PubSubQueue.class)

	@Autowired
	RabbitTemplate rabbitTemplate

	@Autowired
	ApiClient apiClient

	@Autowired
	Queue queue

	/**
	 * 
	 * @param sms
	 */
	@RabbitHandler
	void send(SMS sms) {
		rabbitTemplate.convertAndSend(queue.name, sms)
		log.debug("[o] sent ==> " + sms)
	}

	/**
	 *
	 * @param sms
	 */
	@RabbitListener(queues = "#{@queue}")
	void receive(SMS sms) {
		log.debug("[x] rcvd ==> " + sms)
		apiClient.process(sms)
	}
}
