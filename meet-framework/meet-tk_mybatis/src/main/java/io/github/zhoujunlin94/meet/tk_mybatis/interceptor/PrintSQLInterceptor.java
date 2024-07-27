package io.github.zhoujunlin94.meet.tk_mybatis.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.*;

/**
 * @author zhoujunlin
 * @date 2023年04月15日 15:58
 * @desc 打印sql执行时间以及执行sql拦截器
 */
@Slf4j
@Intercepts({
        @Signature(
                method = "query",
                type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class PrintSQLInterceptor implements Interceptor {

    private boolean enable = false;

    @Override
    public Object intercept(Invocation invocation) {
        Object ret = null;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            ret = invocation.proceed();
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTotalTimeMillis();

            // 获取执行方法的MappedStatement参数,不管是Executor的query方法还是update方法，第一个参数都是MappedStatement
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = null;
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }
            String sqlId = mappedStatement.getId();
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();
            // 打印mysql执行语句
            String sql = assembleSql(configuration, boundSql);
            log.warn("{}方法对应的sql语句===>\n \n{}\n执行时间===>{}ms", sqlId, sql, totalTimeMillis);
        } catch (Exception e) {
            log.warn("PrintSQLInterceptor.intercept error", e);
        }
        return ret;
    }


    /**
     * 组装完整的sql语句 -- 把对应的参数都代入到sql语句里面
     *
     * @param configuration Configuration
     * @param boundSql      BoundSql
     * @return sql完整语句
     */
    private static String assembleSql(Configuration configuration, BoundSql boundSql) {
        // 获取mapper里面方法上的参数
        Object sqlParameter = boundSql.getParameterObject();
        // sql语句里面需要的参数 -- 真实需要用到的参数 因为sqlParameter里面的每个参数不一定都会用到
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql原始语句(?还没有替换成我们具体的参数)  空格、制表符（tab）、换行符、回车符等换成 " "
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollUtil.isNotEmpty(parameterMappings) && Objects.nonNull(sqlParameter)) {
            // sql语句里面的?替换成真实的参数
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(sqlParameter.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(sqlParameter));
            } else {
                MetaObject metaObject = configuration.newMetaObject(sqlParameter);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    // 一个一个把对应的值替换进去 按顺序把?替换成对应的值
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 获取参数对应的string值
     *
     * @param obj 参数对应的值
     * @return string
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            value = Objects.nonNull(obj) ? obj.toString() : StrUtil.EMPTY;
        }
        // 对特殊字符进行转义，方便之后处理替换
        return Objects.nonNull(value) ? makeQueryStringAllRegExp(value) : value;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{}
     * \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    private static String makeQueryStringAllRegExp(String str) {
        if (str != null && !"".equals(str)) {
            return str.replace("\\", "\\\\").replace("*", "\\*")
                    .replace("+", "\\+").replace("|", "\\|")
                    .replace("{", "\\{").replace("}", "\\}")
                    .replace("(", "\\(").replace(")", "\\)")
                    .replace("^", "\\^").replace("$", "\\$")
                    .replace("[", "\\[").replace("]", "\\]")
                    .replace("?", "\\?").replace(",", "\\,")
                    .replace(".", "\\.").replace("&", "\\&");
        }
        return str;
    }

    @Override
    public Object plugin(Object target) {
        if (enable && target instanceof Executor) {
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
