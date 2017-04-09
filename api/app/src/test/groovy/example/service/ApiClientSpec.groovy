package example.service

import java.net.URI

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate

import example.model.SMS
import example.repository.SMSRepository
import example.service.ApiClient
import example.service.PubSubQueue
import groovy.time.TimeCategory
import spock.lang.Specification

@ContextConfiguration
class ApiClientSpec extends Specification {

	ApiClient apiClient
	SMSRepository smsRepository
	PubSubQueue publisherQueue
	RestTemplate restTemplate

	def setup() {
		apiClient = new ApiClient()
	}

	def "should process() be expired"() {
		given: "one sms entry"
		def date = new Date()
		use( TimeCategory ) {
			date = date - 5.minutes
		}
		def sms = [expire: date] as SMS
		when: ""
		sms = apiClient.process(sms)
		then: ""
		sms.expire < new Date()
	}

	def "should process() returns a succefull object"() {
		given: "one sms entry"
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date] as SMS
		smsRepository = Mock(SMSRepository)
		smsRepository.save(_) >> sms
		def response = new ResponseEntity<String>(HttpStatus.CREATED)
		restTemplate = Mock(RestTemplate)
		restTemplate.exchange(_, _, _, _) >> response
		apiClient.restTemplate = restTemplate
		apiClient.smsRepository = smsRepository
		when: ""
		sms = apiClient.process(sms)
		then: ""
		sms.expire == date
	}


	def "should put() returns a succefull object"() {
		given: ""
		def sms = [expire: new Date()] as SMS
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.CREATED)
		restTemplate = Mock(RestTemplate)
		restTemplate.exchange(_, _, _, _) >> response
		apiClient.restTemplate = restTemplate
		when: ""
		sms = apiClient.put(sms)
		then: ""
		sms.status == HttpStatus.CREATED.value
	}


	def "should put() returns a not validated object"() {
		given: ""
		def sms = [expire: new Date()] as SMS
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED)
		restTemplate = Mock(RestTemplate)
		restTemplate.exchange(_, _, _, _) >> response
		apiClient.restTemplate = restTemplate
		when: ""
		sms = apiClient.put(sms)
		then: ""
		sms.status == HttpStatus.METHOD_NOT_ALLOWED.value
	}


	def "should put() throw an exception on HttpStatus response"() {
		given: ""
		def sms = [expire: new Date()] as SMS
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR)
		restTemplate = Mock(RestTemplate)
		restTemplate.exchange(_, _, _, _) >> response
		apiClient.restTemplate = restTemplate
		when: ""
		sms = apiClient.put(sms)
		then: ""
		def ex = thrown(Exception)
		ex.message != null
	}
}
