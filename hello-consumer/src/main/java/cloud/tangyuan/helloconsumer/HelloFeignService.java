package cloud.tangyuan.helloconsumer;

import cloud.tangyuan.hellocommon.Name;
import cloud.tangyuan.hellocommon.Result;
import cloud.tangyuan.hellocommon.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "hello-provider-service", url = "http://${provider.name}", path = "/user",
        configuration = HelloFeignConfig.class,     // 指定 HelloFeignConfig 日志配置类
//        fallback = HelloFeignFallback.class,        // 指定回调类，和 fallbackFactory 只能存在一个
        fallbackFactory = HelloFeignFallbackFactory.class)
public interface HelloFeignService {
    @GetMapping("/greet/{username}")
    String sayHello(@PathVariable("username") String username);

    @GetMapping("/greet")
    String sayHello();

    @GetMapping("/testname")
    Result testName(@SpringQueryMap Name name);

    @PostMapping("/testuser")
    Result testUser(@RequestBody User user);
}