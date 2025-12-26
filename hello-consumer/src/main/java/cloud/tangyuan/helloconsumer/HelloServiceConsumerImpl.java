package cloud.tangyuan.helloconsumer;

import cloud.tangyuan.hellocommon.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 消费者端的Dubbo服务实现，用于在Nacos中显示实例
 */
@DubboService
public class HelloServiceConsumerImpl implements HelloService {

    @Override
    public String sayHello(String username) {
        return "Hello from consumer: " + username;
    }

    @Override
    public String sayHello() {
        return "Hello from consumer";
    }
}
