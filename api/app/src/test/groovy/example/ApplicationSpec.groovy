package example


import org.springframework.boot.SpringApplication
import org.springframework.test.context.ContextConfiguration

import example.Application
import spock.lang.Specification

@ContextConfiguration
class ApplicationSpec extends Specification {

	Application application

	void setup() {
		GroovyMock(SpringApplication, global: true)
	}

	def "should boot up without errors"() {
		given: ""
		String[] args = []
		def application = Spy(Application){
			main(_, _) >> Spy(SpringApplication){
				run() >> {
				}
			}
		}
		// SpringApplication.run(_, _) >> "Test"
		when: ""
		application.main(args)
		then: ""
		1 * SpringApplication.run(_, _)
	}
}
