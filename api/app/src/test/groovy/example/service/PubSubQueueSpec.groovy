package example.service

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.test.context.ContextConfiguration

import example.model.SMS
import example.service.ApiClient
import example.service.PubSubQueue
import groovy.time.TimeCategory
import spock.lang.Specification

@ContextConfiguration
class PubSubQueueSpec extends Specification {

	PubSubQueue pubSubQueue
	RabbitTemplate rabbitTemplate
	Queue queue
	ApiClient apiClient

	def setup() {
		pubSubQueue = new PubSubQueue()
	}


	def "should rabbitTemplate be called"() {
		given: ""
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date] as SMS
		rabbitTemplate = Mock(RabbitTemplate)
		queue = ["queue"] as Queue
		pubSubQueue.queue = queue
		pubSubQueue.rabbitTemplate = rabbitTemplate
		when: ""
		pubSubQueue.send(sms)
		then: ""
		1 * rabbitTemplate.convertAndSend(queue.name, sms)
	}

  def "should apiClient be called"() {
    given: ""
    def date = new Date()
    use( TimeCategory ) {
      date = date + 5.minutes
    }
    def sms = [expire: date] as SMS
    apiClient = Mock(ApiClient)
    pubSubQueue.apiClient = apiClient
    when: ""
    pubSubQueue.receive(sms)
    then: ""
    1 * apiClient.process(sms)
  }

}
