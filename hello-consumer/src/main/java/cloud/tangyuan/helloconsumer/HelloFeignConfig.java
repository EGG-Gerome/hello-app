package cloud.tangyuan.helloconsumer;

import feign.Logger;
import feign.Request; 
import feign.RequestInterceptor;
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
    
    /**
     * 设置Feign的超时时间
     */
    @Bean
    public Request.Options requestOptions() {
        // 连接超时时间为1秒，读取超时时间为5秒
        return new Request.Options(1000, 5000);
    }
    
    /**
     * 添加请求拦截器，显式设置Accept-Encoding头，确保压缩生效
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("Accept-Encoding", "gzip, deflate");
        };
    }
}