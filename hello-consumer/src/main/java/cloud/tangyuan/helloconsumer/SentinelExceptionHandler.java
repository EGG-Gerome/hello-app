package cloud.tangyuan.helloconsumer;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        String msg = "Unknown exception";
        int status = 429;
        if(e instanceof FlowException){
            msg = "请求被限流了";
        } else if (e instanceof ParamFlowException) {
            msg = "请求被热点参数限流了";
        } else if (e instanceof DegradeException) {
            msg = "请求被熔断降级了";
        } else if (e instanceof SystemBlockException) {
            msg = "系统入口流量被限流了";
        } else if (e instanceof AuthorityException) {
            msg = "没有权限访问";
            status = 403;
        }
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(status);
        httpServletResponse.getWriter().println("""
                {
                    "code": %d,
                    "msg": "%s"
                }
                """.formatted(status, msg));
    }
}
