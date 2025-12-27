package cloud.tangyuan.helloprovider;

import cloud.tangyuan.hellocommon.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(timeout = 3000, retries = 3)
public class HelloServiceImpl implements HelloService {
    
    @Override
    public String sayHello(String username) {
        try {
            // 模拟超时效果:sleep 10秒
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hello, %s".formatted(username);
    }
    
    @Override
    public String sayHello() {
        try {
            // 模拟超时效果:sleep 10秒
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hello, Friend";
    }
}
