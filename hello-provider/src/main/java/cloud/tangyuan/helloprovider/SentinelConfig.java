package cloud.tangyuan.helloprovider;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sentinel配置类，用于启用@SentinelResource注解的AOP支持
 */
@Configuration
public class SentinelConfig {
    
    /**
     * 注入SentinelResourceAspect，使@SentinelResource注解生效
     */
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
