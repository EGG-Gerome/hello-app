package cloud.tangyuan.helloconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloConsumerController {
    @Autowired
    private HelloFeignService helloFeignService;

    @GetMapping("enter/{username}")
    public String sayHello(@PathVariable String username) {
        // 调用 hello-provider 微服务
        String response = helloFeignService.sayHello(username);
        // 添加消费者自身的信息
        return "HelloConsumer received: " + response;
    }
    @GetMapping("enter")
    public String sayHello(){
        return "HelloConsumer received nothing\n" + "Hello " + helloFeignService.sayHello();
    }
}