package cloud.tangyuan.helloconsumer;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class HelloFeignFallback implements HelloFeignService{
    @Override
    public String sayHello(@PathVariable String username) {
        return "%s, something is wrong".formatted(username);
    }

    @Override
    public String sayHello() {
        return "something is wrong";
    }
}
