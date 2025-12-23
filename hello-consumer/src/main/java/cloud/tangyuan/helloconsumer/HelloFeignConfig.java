package cloud.tangyuan.helloconsumer;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign日志配置类
 */
@Configuration
public class HelloFeignConfig {
    /**
     * 设置OpenFeign的日志级别为FULL
     * FULL级别会记录请求和响应的所有信息，包括头信息、请求体、元数据等
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}