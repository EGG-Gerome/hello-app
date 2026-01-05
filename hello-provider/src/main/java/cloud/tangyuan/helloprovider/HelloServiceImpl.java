package cloud.tangyuan.helloprovider;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;

import cloud.tangyuan.hellocommon.CallbackListener;
import cloud.tangyuan.hellocommon.HelloService;
import cloud.tangyuan.hellocommon.Result;
import cloud.tangyuan.hellocommon.User;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.dubbo.config.annotation.Argument;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.beans.factory.annotation.Value;

//@DubboService(timeout = 3000, retries = 3, loadbalance = "roundrobin", weight = 100)
@DubboService(
        // 指明 addListener() 方法的 listener 参数是回调类型的参数
        methods = {
                @Method(name = "addListener",
                arguments = {@Argument(index = 1, callback = true)})
        }
)
public class HelloServiceImpl implements HelloService {
    // 存放消费者所注册的 CallbackListener 对象
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    public HelloServiceImpl(){
        // 创建定时向消费者推送当前时间信息的线程
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        for(Map.Entry<String, CallbackListener> entry:
                        listeners.entrySet()){
                            try{
                                // 回调消费者的 CallbackListener 的 report() 方法
                                entry.getValue().report("Current time: %s".formatted(new Date()));
                            } catch (Throwable throwable){
                                listeners.remove(entry.getKey());
                            }
                        }
                        Thread.sleep(5000);  // 通过睡眠定时向消费者发送时间信息
                    } catch (Throwable throwable){
                        throwable.printStackTrace();
                    }
                }
            }
        });
        t.setDaemon(true);      // 作为后台线程运行
        t.start();
    }

    @Override
    public void addListener(String key,CallbackListener listener){
        // 加入消费者注册的 CallbackListener 对象
        listeners.put(key, listener);
    }
    @Override
    public String  sayHello(String userame, String key){
        CallbackListener listener = listeners.get(key);
        // 回调消费者 CallbackListener 对象的 report() 方法
        listener.report("%s打过招呼。".formatted(userame));
        return "Hello, %s".formatted(userame);
    }

    @Value("${server.port}")
    private String servicePort;

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    @SentinelResource(value = "say", blockHandler = "handleBlock")
    public String sayHello(String username){
        return "Hello, %s<br>Service Name: %s<br>Service Port: %s".formatted(username, serviceName, servicePort);
    }

    @Override
    @SentinelResource(value = "say", blockHandler = "handleBlock")
    public String sayHello() {
        return "Hello, Friends<br>Service Name: %s<br>Service Port: %s".formatted(serviceName, servicePort);
    }

    // 处理请求被拒绝的情况
    public String handleBlock(String username, BlockException blockException){
        blockException.printStackTrace();
        return "%s, request is blocked.".formatted(username);
    }
    public String handleBlock(BlockException blockException){
        blockException.printStackTrace();
        return "Friend, request is blocked.";
    }


    //    @Override
//    public String sayHello(String username) {
//        try {
//            // 模拟超时效果:sleep 10秒
//            Thread.sleep(10000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "Hello, %s".formatted(username);
//    }
//    @Override
//    public String sayHello() {
//        try {
//            // 模拟超时效果:sleep 10秒
//            Thread.sleep(10000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "Hello, Friend";
//    }
    
    @Override
    public Result testUser(User user) {
        System.out.println("id: %s, name: %s %s".formatted(user.getId(),
                user.getName().getFirstname(), user.getName().getLastname()));
        return new Result(100, "OK");
    }
}
