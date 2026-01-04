// 这个其实叫 HelloConsumerConfig 更好，因为这个是 Bean 配置类
package cloud.tangyuan.helloprovider;

import cloud.tangyuan.hellocommon.Name;
import cloud.tangyuan.hellocommon.Result;
import cloud.tangyuan.hellocommon.User;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

//@RestController
//public class HelloProviderController {
//    // 关键点1：多路径用{}包裹（数组语法），显式指定value属性（也可省略value=，但{}必须有）
//    @GetMapping(value = {"/greet", "/greet/{username}"})
//    // 关键点2：设置required=false，允许username为null
//    public String  greet(@PathVariable(required = false) String username){
//        String name = (username == null ? "tourist" : username);
//        return "Hello " + name;
//    }
//}

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class HelloProviderController {
    // 把 servicePort 变量与 server.port 配置属性绑定
    @Value("${server.port}")
    private String servicePort;

    // 把 serviceName 变量与 spring.application.name 配置属性绑定
    @Value("${spring.application.name}")
    private String serviceName;

    @GetMapping("/greet")
    public String greet() {
        return greet("Stranger");
    }

    @GetMapping("/greet/{username}")
    public String greet(@PathVariable("username") String username){
        if(username!=null && username.equals("Monster")) {
            throw new IllegalArgumentException(username);
        }
        System.out.println("Before sleep: " + System.currentTimeMillis());
        String msg = null;
        try{
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg = "Hello, %s <br>Service Name: %s <br>Service Port: %s".formatted(username, serviceName, servicePort);
        System.out.println("After sleep: " + System.currentTimeMillis());
        return msg;
    }

    @GetMapping("/testname")
    public Result testName(@SpringQueryMap Name name){
        System.out.println("firstname: %s, lastname: %s".formatted(name.getFirstname(), name.getLastname()) );
        return new Result(100, "OK");
    }

    @PostMapping("/testuser")
    public Result testUser(@RequestBody User user){
        System.out.println("id: %s, name: %s %s".formatted(user.getId(),
                user.getName().getFirstname(), user.getName().getLastname()));
        return new Result(100, "OK");
    }
    
    // 用于测试Sentinel @SentinelResource注解的简单REST端点
    @GetMapping("/test-sentinel")
    @SentinelResource(value = "say", blockHandler = "handleTestBlock")
    public String testSentinel() {
        return "Test Sentinel Resource: say<br>Service Name: %s<br>Service Port: %s".formatted(serviceName, servicePort);
    }
    
    // 处理testSentinel方法的流控情况
    public String handleTestBlock(BlockException blockException) {
        blockException.printStackTrace();
        return "Test request is blocked by Sentinel";
    }
}