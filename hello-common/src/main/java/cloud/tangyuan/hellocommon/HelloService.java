package cloud.tangyuan.hellocommon;
public interface HelloService {
    public String sayHello(String username);
    public String sayHello();
    public Result testUser(User user);

    // provider 的服务方法
    public String sayHello(String username, String key);
    // consumer 通过此方法向 provider 注册 CallbackListener
    public void addListener(String key, CallbackListener listener);
}
