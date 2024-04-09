package io.github.zhoujunlin94.meet.tk_mybatis.interceptor;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2023年04月15日 15:58
 * @desc 打印sql执行时间以及执行sql拦截器
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class PrintSQLInterceptor implements Interceptor {

    private boolean enable = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return invocation.proceed();
        } finally {
            stopWatch.stop();
            log.info("执行耗时:{}ms", stopWatch.getTotalTimeMillis());

            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = null;
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }
            log.info("执行SQL语句:{}", mappedStatement.getBoundSql(parameter).getSql());
        }
    }

    @Override
    public Object plugin(Object target) {
        if (enable && target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以给这个类定义一些成员比如开关啥的  实例化这个类的时候通过properties赋值
        try {
            this.enable = Boolean.parseBoolean(properties.getProperty("enable"));
        } catch (Exception e) {
            log.error("PrintSQLInterceptor属性初始化出错", e);
        }
    }

}
