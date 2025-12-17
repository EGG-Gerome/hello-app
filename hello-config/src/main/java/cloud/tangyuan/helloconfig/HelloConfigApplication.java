package cloud.tangyuan.helloconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.module.Configuration;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class HelloConfigApplication {

	public static void main(String[] args) throws Exception{
		ConfigurableApplicationContext applicationContext = SpringApplication.run(HelloConfigApplication.class, args);

		while (true) {
			// 读取配置属性
			String username = applicationContext
					.getEnvironment()
					.getProperty("db.username");
			String password = applicationContext
					.getEnvironment()
					.getProperty("db.password");
			String host = applicationContext
					.getEnvironment()
					.getProperty("db.host");
			System.out.println("db.username=%s, db.password=%s, db.host=%s".formatted(username, password, host));
			TimeUnit.SECONDS.sleep(1);	// 睡眠 1s
		}
	}

}
