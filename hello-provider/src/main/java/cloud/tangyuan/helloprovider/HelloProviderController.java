package cloud.tangyuan.helloprovider;

import org.springframework.beans.factory.annotation.Value;
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

}