package example.model

import org.springframework.test.context.ContextConfiguration

import example.model.SMS
import groovy.time.TimeCategory
import spock.lang.Specification

@ContextConfiguration
class SMSSpec extends Specification {

	def "should set an expire limit given timeExpire parameter greater zero"() {
		given: "one sms entry"
		def sms = [] as SMS
		when: "execute validation expire limit"
		sms.expireLimit(1)
		then: "expire is filled"
		sms.expire != null
	}

	def "should not set an expire limit given timeExpire parameter equals zero"() {
		given: "one sms entry"
		def sms = [] as SMS
		when: "execute validation expire limit"
		sms.expireLimit(10)
		then: "expire is not filled"
		sms.expire != null
	}

	def "should not set an expire limit given expire not null"() {
		given: "one sms entry"
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date] as SMS
		when: "execute validation expire limit"
		sms.expireLimit(10)
		then: "expire is not changed"
		sms.expire == date
	}

	def "should return object as map"() {
		given: "one sms entry"
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date] as SMS
		when: ""
		def map = sms.asMap()
		then: ""
		map.expire == date
	}

	def "should not be expired with null date"() {
		given: "one sms entry"
		def sms = [expire: null] as SMS
		when: ""
		def notExpired = sms.notExpired()
		then: ""
		notExpired == true
	}

	def "should not be expired with valid date"() {
		given: "one sms entry"
		def date = new Date()
		use( TimeCategory ) {
			date = date + 5.minutes
		}
		def sms = [expire: date] as SMS
		when: ""
		def notExpired = sms.notExpired()
		then: ""
		notExpired == true
	}

	def "should be expired with valid date"() {
		given: "one sms entry"
		def date = new Date()
		use( TimeCategory ) {
			date = date - 5.minutes
		}
		def sms = [expire: date] as SMS
		when: ""
		def notExpired = sms.notExpired()
		then: ""
		notExpired == false
	}
}
