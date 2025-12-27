package cloud.tangyuan.helloprovider;

import cloud.tangyuan.hellocommon.HelloService;
import cloud.tangyuan.hellocommon.Result;
import cloud.tangyuan.hellocommon.User;
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
    
    @Override
    public Result testUser(User user) {
        System.out.println("id: %s, name: %s %s".formatted(user.getId(),
                user.getName().getFirstname(), user.getName().getLastname()));
        return new Result(100, "OK");
    }
}
