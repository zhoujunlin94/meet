package io.github.zhoujunlin94.meet.web.filter;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author zhoujunlin
 * @date 2023年09月23日 15:28
 * @desc
 */
@Slf4j
@Component
@ConditionalOnClass(name = {SeataFilter.ROOT_CONTEXT_CLAZZ})
public class SeataFilter implements Filter {

    public static final String ROOT_CONTEXT_CLAZZ = "io.seata.core.context.RootContext";

    private static final String XID = "TX_XID";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //获取XID
        String xid = request.getHeader(XID);
        try {
            if (StrUtil.isNotBlank(xid)) {
                // 绑定
                Method bindMethod = ReflectUtil.getMethodByName(Class.forName(ROOT_CONTEXT_CLAZZ), "bind");
                ReflectUtil.invokeStatic(bindMethod, xid);
            }
        } catch (Exception e) {
            log.error("SeataFilter传递XID:{}出错", xid, e);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
