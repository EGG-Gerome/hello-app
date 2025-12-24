package cloud.tangyuan.helloconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "hello-provider-service", url = "http://${provider.name}", path = "/user",
        configuration = HelloFeignConfig.class,     // 指定 HelloFeignConfig 日志配置类
//        fallback = HelloFeignFallback.class,        // 指定回调类，和 fallbackFactory 只能存在一个
        fallbackFactory = HelloFeignFallbackFactory.class)
public interface HelloFeignService {
    @GetMapping("/greet/{username}")
    String sayHello(@PathVariable("username") String username);

    @GetMapping("/greet")
    String sayHello();

}