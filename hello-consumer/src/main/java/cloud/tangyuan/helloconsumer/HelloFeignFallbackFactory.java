package cloud.tangyuan.helloconsumer;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloFeignFallbackFactory implements FallbackFactory<HelloFeignFallback> {
    @Override
    public HelloFeignFallback create(Throwable cause) {
        return new HelloFeignFallback(cause);
    }
}
