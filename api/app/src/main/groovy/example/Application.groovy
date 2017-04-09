package example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@Import([RabbitConfiguration.class, RestConfiguration.class])
@EnableJpaRepositories
class Application {
	
	/**
	 * start application
	 */
	static main(args) {
		SpringApplication.run(Application.class, args);
	}
}
