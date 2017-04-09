package example.controller

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration

import example.controller.SMSController
import example.model.SMS
import example.repository.SMSRepository
import groovy.time.TimeCategory
import example.service.ApiClient
import spock.lang.Specification

@ContextConfiguration
class SMSControllerSpec extends Specification {

	SMSController smsController
	ApiClient apiClient
	SMSRepository smsRepository
	Logger log

	def setup() {
		smsController = new SMSController()
		smsController.queueExpire = 0
		apiClient = Mock(ApiClient)
		log = Mock(Logger)
		smsRepository = Mock(SMSRepository)
	}

	def "should returns a succefull response"() {
		given: "mock dependencies controller"
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date, body: "hello world", status:HttpStatus.CREATED.value] as SMS
		smsRepository.save(_) >> sms
		apiClient.process(_) >> sms
		smsController.apiClient = apiClient
		smsController.smsRepository = smsRepository
		when: "execute put"
		def response = smsController.put(sms)
		then: "should have http status CREATED"
		response.statusCode == HttpStatus.CREATED
	}

	def "should throw an execption on save repository"() {
		given: ""
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date, body: "hello world", status:HttpStatus.CREATED.value] as SMS
		smsRepository.save(_) >> { throw new Exception() }
		apiClient.process(_) >> sms
		smsController.log = log
		smsController.apiClient = apiClient
		smsController.smsRepository = smsRepository
		when: ""
		def response = smsController.put(sms)
		then: ""
		1 * smsController.log.error(_)
	}

	def "should returns a not validated response"() {
		given: "mock dependencies controller"
		def date = new Date()
		use( TimeCategory ) {
			date = date - 5.minutes
		}
		def sms = [expire: date, body: "hello world"] as SMS
		when: "execute put"
		def response = smsController.put(sms)
		then: "should have http status CREATED"
		response.statusCode == HttpStatus.BAD_REQUEST
	}

	def "should returns a not validated response because body content"() {
		given: "mock dependencies controller"
		def sms = [body: '0'.multiply(161)] as SMS
		when: "execute put"
		def response = smsController.put(sms)
		then: ""
		response.statusCode == HttpStatus.BAD_REQUEST
	}

	def "should invalidade because expire and returns false"() {
		given: "mock dependencies controller"
		def date = new Date()
		use( TimeCategory ) {
			date = date - 5.minutes
		}
		def sms = [expire: date] as SMS
		when: ""
		def validated = smsController.validate(sms)
		then: ""
		validated == false
	}


	def "should invalidade because body and returns false"() {
		given: "mock dependencies controller"
		def sms = [body: '0'.multiply(161)] as SMS
		when: ""
		def validated = smsController.validate(sms)
		then: ""
		validated == false
	}
}
