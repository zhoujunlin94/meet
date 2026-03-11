package io.github.zhoujunlin94.meet.web.filter;

import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.web.wrapper.RequestBodyWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestBodyCacheFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (StrUtil.isNotBlank(request.getMethod()) &&
                (request.getMethod().equalsIgnoreCase("POST") ||
                        request.getMethod().equalsIgnoreCase("PUT") ||
                        request.getMethod().equalsIgnoreCase("DELETE") ||
                        request.getMethod().equalsIgnoreCase("PATCH"))) {
            RequestBodyWrapper wrappedRequest = new RequestBodyWrapper(request);
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
