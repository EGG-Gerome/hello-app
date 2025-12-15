package cloud.tangyuan.helloconsumer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HelloConsumerProvider {

    // 配置负载均衡的 RestTemplate
    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate(){
        return new RestTemplate();
    }

    // 配置普通的 RestTemplate（用于直接调用，不使用负载均衡）
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
