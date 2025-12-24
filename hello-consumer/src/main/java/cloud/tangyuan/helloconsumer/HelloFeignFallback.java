package cloud.tangyuan.helloconsumer;

import cloud.tangyuan.hellocommon.Name;
import cloud.tangyuan.hellocommon.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TreeMap;

// 回调类加工厂类
@Component
public class HelloFeignFallback implements HelloFeignService{
    private Throwable cause;        // 表示当前异常
    public HelloFeignFallback(){}
    public HelloFeignFallback(Throwable cause){
        this.cause = cause;
    }

    @Override
    public String sayHello(@PathVariable String username) {
        // 输出详细异常信息
        String errorDetails = cause != null ? getStackTrace(cause) : "Unknown error";
        return "%s, something is wrong<br>%s".formatted(username, errorDetails);
    }

    @Override
    public String sayHello() {
        // 输出详细异常信息
        String errorDetails = cause != null ? getStackTrace(cause) : "Unknown error";
        return "something is wrong<br>%s".formatted(errorDetails);
    }

    // 实用方法：获得异常的详细信息
    public static String getStackTrace(Throwable cause){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try{
            cause.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            printWriter.close();
        }
    }

    @Override
    public Result testName(Name name) {
        return null;
    }
}




// 只有回调类 Fallback
//@Component
//public class HelloFeignFallback implements HelloFeignService{
//    @Override
//    public String sayHello(@PathVariable String username) {
//        return "%s, something is wrong".formatted(username);
//    }
//
//    @Override
//    public String sayHello() {
//        return "something is wrong";
//    }
//}
