package cloud.tangyuan.helloconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// prefix 用于指定配置属性名的前缀
@ConfigurationProperties(prefix = "db")
@Component
@Data
public class MyProperties {
    private String username;
    private String password;
}
