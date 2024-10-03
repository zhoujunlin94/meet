package io.github.zhoujunlin94.meet.tk_mybatis.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2023年04月15日 15:58
 * @desc 打印sql执行时间以及执行sql拦截器   获取执行sql,损耗性能  谨慎开启
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
    public Object intercept(Invocation invocation) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SQL执行");

        Object ret = invocation.proceed();

        try {
            stopWatch.stop();

            stopWatch.start("SQL拼接打印");
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
            stopWatch.stop();

            StringBuilder logInfo = new StringBuilder("\n\n|=================[PRINT_SQL]======================\n")
                    .append("|=> ").append(sqlId).append("\n|\n")
                    .append("|=> ").append(sql).append("\n|\n");
            for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
                logInfo.append("|=> ").append(taskInfo.getTaskName()).append("执行耗时:").append(taskInfo.getTimeMillis()).append("ms\n");
            }
            logInfo.append("|=================[PRINT_SQL]======================\n");

            log.warn(logInfo.toString());
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
        String sql = replaceWhiteSpace(boundSql.getSql());

        if (CollUtil.isNotEmpty(parameterMappings) && Objects.nonNull(sqlParameter)) {
            // sql语句里面的?替换成真实的参数
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(sqlParameter.getClass())) {
                sql = replaceFirstQuestionMark(sql, getParameterValue(sqlParameter));
            } else {
                MetaObject metaObject = configuration.newMetaObject(sqlParameter);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    // 一个一个把对应的值替换进去 按顺序把?替换成对应的值
                    String propertyName = parameterMapping.getProperty();
                    Object value = null;
                    if (metaObject.hasGetter(propertyName)) {
                        value = metaObject.getValue(propertyName);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    }
                    if (value != null) {
                        sql = replaceFirstQuestionMark(sql, getParameterValue(value));
                    }
                }
            }
        }
        return sql;
    }


    private static String replaceWhiteSpace(String sql) {
        StringBuilder sb = new StringBuilder(sql.length());
        boolean inWhitespace = false;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!inWhitespace) {
                    sb.append(' ');
                    inWhitespace = true;
                }
            } else {
                sb.append(c);
                inWhitespace = false;
            }
        }
        return sb.toString();
    }


    private static String replaceFirstQuestionMark(String sql, String replacement) {
        StringBuilder sb = new StringBuilder();
        boolean replaced = false;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (!replaced && c == '?') {
                sb.append(replacement);
                replaced = true;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
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
            value = "'" + DatePattern.NORM_DATETIME_MS_FORMAT.format(obj) + "'";
        } else {
            value = Objects.nonNull(obj) ? obj.toString() : StrUtil.EMPTY;
        }
        // 对特殊字符进行转义，方便之后处理替换
        return Objects.nonNull(value) ? makeQueryStringAllRegExp(value) : StrUtil.EMPTY;
    }


    /**
     * 转义正则特殊字符 （$()*+.[]?\^{}
     * \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    private static String makeQueryStringAllRegExp(String queryString) {
        if (StrUtil.isBlank(queryString)) {
            return queryString;
        }

        StringBuilder result = new StringBuilder(queryString.length());
        for (int i = 0; i < queryString.length(); i++) {
            char c = queryString.charAt(i);
            switch (c) {
                case '\\':
                    result.append("\\\\");
                    break;
                case '*':
                    result.append("\\*");
                    break;
                case '+':
                    result.append("\\+");
                    break;
                case '|':
                    result.append("\\|");
                    break;
                case '{':
                    result.append("\\{");
                    break;
                case '}':
                    result.append("\\}");
                    break;
                case '(':
                    result.append("\\(");
                    break;
                case ')':
                    result.append("\\)");
                    break;
                case '^':
                    result.append("\\^");
                    break;
                case '$':
                    result.append("\\$");
                    break;
                case '[':
                    result.append("\\[");
                    break;
                case ']':
                    result.append("\\]");
                    break;
                case '?':
                    result.append("\\?");
                    break;
                case ',':
                    result.append("\\,");
                    break;
                case '.':
                    result.append("\\.");
                    break;
                case '&':
                    result.append("\\&");
                    break;
                default:
                    result.append(c);
                    break;
            }
        }

        return result.toString();
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
