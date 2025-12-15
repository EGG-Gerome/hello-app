package cloud.tangyuan.helloprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class HelloProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloProviderApplication.class, args);
	}

}
