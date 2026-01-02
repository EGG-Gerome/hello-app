package cloud.tangyuan.helloconsumer;

public class MyExceptionHandler {
    public static String handleException(String username, Throwable throwable){
        throwable.printStackTrace();
        return "%s, something is wrong.".formatted(username);
    }
    
    public static String handleException(Throwable throwable){
        throwable.printStackTrace();
        return "Friend, something is wrong.";
    }
}
