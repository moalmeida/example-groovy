package example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfiguration {

	@Autowired
	Environment env

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate()
	}

	@Bean String apiUrl() {
		return [
			env.getProperty("api.schema"),
			env.getProperty("api.host"),
			":",
			env.getProperty("api.port"),
			env.getProperty("api.path")
		].join("")
	}

}
