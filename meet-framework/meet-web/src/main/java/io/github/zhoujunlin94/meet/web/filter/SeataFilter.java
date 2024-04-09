package io.github.zhoujunlin94.meet.web.filter;

import cn.hutool.core.util.StrUtil;
import io.seata.core.context.RootContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zhoujunlin
 * @date 2023年09月23日 15:28
 * @desc
 */
@Component
@ConditionalOnClass(RootContext.class)
public class SeataFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //手动绑定XID
        String xid = request.getHeader(RootContext.KEY_XID);
        if (StrUtil.isNotBlank(xid)) {
            RootContext.bind(xid);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
