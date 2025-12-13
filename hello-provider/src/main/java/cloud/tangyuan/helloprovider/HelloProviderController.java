package cloud.tangyuan.helloprovider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloProviderController {
    // 关键点1：多路径用{}包裹（数组语法），显式指定value属性（也可省略value=，但{}必须有）
    @GetMapping(value = {"/greet", "/greet/{username}"})
    // 关键点2：设置required=false，允许username为null
    public String  greet(@PathVariable(required = false) String username){
        String name = (username == null ? "tourist" : username);
        return "Hello " + name;
    }
}