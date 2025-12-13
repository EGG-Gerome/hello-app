package cloud.tangyuan.helloconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "hello-provider-service")
public interface HelloFeignService {
    @GetMapping(value = "/greet/{username}")
    String sayHello(@PathVariable("username") String username);
}