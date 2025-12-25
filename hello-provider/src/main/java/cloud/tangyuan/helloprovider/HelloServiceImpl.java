package cloud.tangyuan.helloprovider;

import cloud.tangyuan.hellocommon.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String username) {
        return "Hello, %s".formatted(username);
    }
}
