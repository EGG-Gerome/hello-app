package cloud.tangyuan.helloconsumer;

import cloud.tangyuan.hellocommon.HelloService;
import cloud.tangyuan.hellocommon.Name;
import cloud.tangyuan.hellocommon.Result;
import cloud.tangyuan.hellocommon.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class HelloConsumerController {
    // 指定异步调用 doTask() 方法，timeout设置为10秒，大于doTask的5秒执行时间
    @DubboReference(check = false, timeout = 10000, retries = 3,
            methods = {@Method(name = "doTask", async = true)})
    private final HelloService helloService;

    private final HelloFeignService helloFeignService;
    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate loadBalancedRestTemplate;
    private final RestTemplate restTemplate;

    // 构造函数注入，Spring Boot 3.0 推荐的注入方式
    public HelloConsumerController(
            HelloFeignService helloFeignService,
            DiscoveryClient discoveryClient,
            LoadBalancerClient loadBalancerClient,
            @Qualifier("loadBalancedRestTemplate") RestTemplate loadBalancedRestTemplate,
            RestTemplate restTemplate,
            HelloService helloService) {
        this.helloFeignService = helloFeignService;
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.restTemplate = restTemplate;
        this.helloService = helloService;
    }


    @GetMapping("/testasync")
    public String testAsync(){
        // 此方法立即返回 null
        helloService.doTask("保存文件");

        // 获得存放异步调用结果的 CompletableFuture 对象
        CompletableFuture<String> helloFuture =
                RpcContext.getContext().getCompletableFuture();

        // 指定收到异步调用结果时的操作，参数 result 表示调用结果
        helloFuture.whenComplete((result, exception) -> {
            if(exception == null) {
                System.out.println(result);
            } else {
                exception.printStackTrace();
            }
        });
       return "任务已经下达";
    }

    // 使用 Dubbo 调用
    @GetMapping("/enter/{username}")
    public String sayHello(@PathVariable String username){
        return helloService.sayHello(username);
    }
    @GetMapping("/enter")
    public String sayHello(){
        return helloService.sayHello();
    }

    @Value("${server.port}")
    private String servicePort;
    // 该注解表示 Spring 框架创建了控制器对象后就会调用 init() 方法
    @PostConstruct
    public void init(){
        try {
            // 向 provider 注册 MyCallbackListener 对象，以 servicePort 作为 key
            helloService.addListener(servicePort,
                    new MyCallbackListener());
            System.out.println("成功注册回调监听器");
        } catch (Exception e) {
            System.err.println("初始化回调监听器失败，可能provider尚未完全启动: " + e.getMessage());
            // 不抛出异常，允许服务继续启动
        }
    }
    @GetMapping(value = "/callback/{username}")
    public String testCallback(@PathVariable String username){
        // 调用提供者的 sayHello() 服务方法
        return helloService.sayHello(username, servicePort);
    }




//    // 使用 Feign 调用
//    @GetMapping("/enter/{username}")
//    public String sayHello(@PathVariable String username) {
//        // 调用 hello-provider 微服务
//        String response = helloFeignService.sayHello(username);
//        // 添加消费者自身的信息
//        return "HelloConsumer received: " + response;
//    }
//
//    @GetMapping("/enter")
//    public String sayHello(){
//        return "HelloConsumer received nothing\n" + "Hello " + helloFeignService.sayHello();
//    }

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

    // 简单测试端点，不依赖其他服务
    @GetMapping("/test")
    public String test() {
        return "Hello from HelloConsumer! This is a simple test endpoint.";
    }

    // 测试 DiscoveryClient 获取服务实例
    @GetMapping("/test-discovery")
    public String testDiscovery() {
        List<ServiceInstance> instances = discoveryClient.getInstances("hello-provider-service");
        if (instances.isEmpty()) {
            return "No instances found for hello-provider-service";
        }
        StringBuilder result = new StringBuilder("Found instances:\n");
        for (ServiceInstance instance : instances) {
            result.append("Instance: ")
                  .append(instance.getUri())
                  .append(" (IP: ")
                  .append(instance.getHost())
                  .append(":")
                  .append(instance.getPort())
                  .append(")\n");
        }
        return result.toString();
    }

    // 直接调用 provider 的本地地址，绕过服务发现
    @GetMapping("/test-direct")
    public String testDirect() {
        String serviceUrl = "http://localhost:8081/greet/Y";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceUrl, String.class);
        String result = responseEntity.getBody();
        return "Direct call result: " + result;
    }

    @GetMapping("/list")
    public String showServices(){
        // 获得微服务的名字列表
        List<String> services = discoveryClient.getServices();
        for(String service: services){
            System.out.println("服务名：%s".formatted(service));
        }
        // 根据微服务的名字列表获得相应的微服务实例列表
        List<ServiceInstance> instances = discoveryClient.getInstances("hello-provider-service");
        for(ServiceInstance instance: instances){
            System.out.println("URL: %s".formatted(instance.getUri()));
        }
        return "ok";
    }

    // 测试传递简单对象功能
    @GetMapping("/testname")
    public Result testName(){
        Name name = new Name("Gerome", "Windsor");
        return helloFeignService.testName(name);
    }

    @GetMapping("testuser")
    public Result testUser(){
        Name name = new Name("Gerome", "Windsor");
        User user = new User(01, name);
        return helloFeignService.testUser(user);
    }
}