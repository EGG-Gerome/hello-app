package cloud.tangyuan.helloconsumer;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 自定义请求来源解析器
 * 用于Sentinel授权规则,从请求参数中获取调用者来源
 */
@Component
public class MyOriginParser implements RequestOriginParser {
    
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 从请求参数中获取origin参数
        String origin = request.getParameter("origin");
        
        // 如果origin参数为空,返回"blank"作为默认值
        return origin == null ? "blank" : origin;
    }
}
