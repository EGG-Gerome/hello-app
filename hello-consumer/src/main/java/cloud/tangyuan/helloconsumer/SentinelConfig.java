package cloud.tangyuan.helloconsumer;

import org.springframework.context.annotation.Configuration;

/**
 * Sentinel配置类，用于自定义URL资源注册行为
 */
@Configuration
public class SentinelConfig {
    // 移除了过于严格的UrlCleaner配置
    // 这样@SentinelResource注解的资源会被注册，URL资源也会被正常注册
    // 通过application.yml中的spring.cloud.sentinel.web-context-unify=false来区分不同URL资源
}

