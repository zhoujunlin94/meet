package io.github.zhoujunlin94.meet.web.qlexpress;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.security.QLSecurityStrategy;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2025年12月23日 15:37
 * @desc
 */
@Component
public class QLExecuteComponent {

    private final Express4Runner runner =
            new Express4Runner(InitOptions.builder().securityStrategy(QLSecurityStrategy.open()).build());

    @Resource
    private ApplicationContext applicationContext;

    public Object execute(String script, Map<String, Object> context) {
        QLSpringContext springContext = new QLSpringContext(context, applicationContext);
        return runner.execute(script, springContext, QLOptions.DEFAULT_OPTIONS).getResult();
    }

}
