package example

import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate

import example.RestConfiguration
import spock.lang.Specification

@ContextConfiguration
class RestConfigurationSpec extends Specification {

	Environment env
	RestConfiguration restConfiguration
	RestTemplate restTemplate

	def setup() {
		restConfiguration = new RestConfiguration()
	}


	def "should rabbitTemplate be called"() {
		when: ""
		restTemplate = restConfiguration.restTemplate()
		then: ""
		restTemplate != null
	}

	def "should connectionFactory() be loaded by Environment"() {
		given: ""
		env = Mock(Environment)
		env.getProperty(_ as String) >> {
			String message ->
			message.equals("api.schema") ? "http://" :
			message.equals("api.host") ? "localhost" :
			message.equals("api.port") ? "0000" :
			message.equals("api.path") ? "/test" : ""
		}
		restConfiguration.env = env
		when: ""
		def property = restConfiguration.apiUrl()
		then: ""
		property == "http://localhost:0000/test"
	}
}
