package email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EMailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EMailApplication.class, args);
	}
}
