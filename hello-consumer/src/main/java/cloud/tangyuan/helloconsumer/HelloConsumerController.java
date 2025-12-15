package cloud.tangyuan.helloconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class HelloConsumerController {

//    这个复杂工厂模式以后再拓展（要写动态bean，ApplicationContext）
//    private final HelloFeignService helloFeignService;
//    public HelloConsumerController(HelloFeignService helloFeignService) {
//        this.helloFeignService = helloFeignService;
//    }
    @Autowired
    private HelloFeignService helloFeignService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate loadBalancedRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    // 使用 Feign 调用
    @GetMapping("/enter/{username}")
    public String sayHello(@PathVariable String username) {
        // 调用 hello-provider 微服务
        String response = helloFeignService.sayHello(username);
        // 添加消费者自身的信息
        return "HelloConsumer received: " + response;
    }

    @GetMapping("/enter")
    public String sayHello(){
        return "HelloConsumer received nothing\n" + "Hello " + helloFeignService.sayHello();
    }

    // 使用 DiscoveryClient 调用
    @GetMapping("/enter01/{username}")
    public String sayHello01(@PathVariable String username){
        List<ServiceInstance> instances = discoveryClient.getInstances("hello-provider-service");
        String rootURL = instances.get(0).getUri().toString();
        String serviceUrl = "%s/greet/%s".formatted(rootURL, username);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceUrl, String.class);
        String result = responseEntity.getBody();
        return result;
    }

    // 使用 LoadBalancerClient 调用
    @GetMapping("/enter02/{username}")
    public String sayHello02(@PathVariable String username){
        // 由 LoadBalancer 选择 hello-provider-service 微服务的实例
        ServiceInstance instance = loadBalancerClient.choose("hello-provider-service");
        String rootUrl = instance.getUri().toString();
        String serviceUrl = "%s/greet/%s".formatted(rootUrl, username);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceUrl, String.class);
        String result = responseEntity.getBody();
        return result;
    }

    // 使用 LoadBalanced RestTemplate 调用
    @GetMapping("/enter03/{username}")
    public String sayHello03(@PathVariable String username){
        // 指定微服务的虚拟地址
        String rootUrl = "http://hello-provider-service";
        String serviceUrl = "%s/greet/%s".formatted(rootUrl, username);
        ResponseEntity<String> responseEntity = loadBalancedRestTemplate.getForEntity(serviceUrl, String.class);
        String result = responseEntity.getBody().toString();
        return result;
    }

}