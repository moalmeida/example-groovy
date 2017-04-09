package example

import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification

@ContextConfiguration
class RabbitConfigurationSpec extends Specification {

	RabbitConfiguration rabbitConfiguration
	Environment env
	// ConnectionFactory connectionFactory

	def setup() {
		rabbitConfiguration = new RabbitConfiguration()
	}

	// def "should queueHost() be loaded by Environment"() {
	// 	given: ""
	// 	env = Mock(Environment)
	// 	env.getProperty(_) >> "TESTE"
	// 	rabbitConfiguration.env = env
	// 	when: ""
	// 	def property = rabbitConfiguration.queueHost()
	// 	then: ""
	// 	property == "TESTE"
	// }

	// def "should queueUser() be loaded by Environment"() {
	// 	given: ""
	// 	env = Mock(Environment)
	// 	env.getProperty(_) >> "TESTE"
	// 	rabbitConfiguration.env = env
	// 	when: ""
	// 	def property = rabbitConfiguration.queueUser()
	// 	then: ""
	// 	property == "TESTE"
	// }

	// def "should queuePwd() be loaded by Environment"() {
	// 	given: ""
	// 	env = Mock(Environment)
	// 	env.getProperty(_) >> "TESTE"
	// 	rabbitConfiguration.env = env
	// 	when: ""
	// 	def property = rabbitConfiguration.queuePwd()
	// 	then: ""
	// 	property == "TESTE"
	// }

	def "should queueName() be loaded by Environment"() {
		given: ""
		env = Mock(Environment)
		env.getProperty(_) >> "TESTE"
		rabbitConfiguration.env = env
		when: ""
		def property = rabbitConfiguration.queueName()
		then: ""
		property == "TESTE"
	}

	def "should queueExpire() be loaded by Environment"() {
		given: ""
		env = Mock(Environment)
		env.getProperty(_) >> 15
		rabbitConfiguration.env = env
		when: ""
		def property = rabbitConfiguration.queueExpire()
		then: ""
		property == 15
	}

	def "should queueExpire() be loaded by NaN Environment"() {
		given: ""
		env = Mock(Environment)
		env.getProperty(_) >> "NaN"
		rabbitConfiguration.env = env
		when: ""
		def property = rabbitConfiguration.queueExpire()
		then: ""
		property == 0
	}

	def "should queue() be loaded by Environment"() {
		given: ""
		env = Mock(Environment)
		env.getProperty(_) >> "TESTE"
		rabbitConfiguration.env = env
		when: ""
		def queue = rabbitConfiguration.queue()
		then: ""
		queue.name == "TESTE"
	}

	// def "should connectionFactory() be loaded by Environment"() {
	// 	given: ""
	// 	env = Mock(Environment)
	// 	env.getProperty(_ as String) >> {  String message ->
	// 		message.equals("queue.host") ? "localhost" :
	// 				message.equals("queue.user") ? "user" :
	// 				message.equals("queue.pwd") ? "pwd" :
	// 				""
	// 	}
	// 	rabbitConfiguration.env = env
	// 	when: ""
	// 	def property = rabbitConfiguration.connectionFactory()
	// 	then: ""
	// 	property.host == "localhost"
	// 	property.username == "user"
	// }

}
