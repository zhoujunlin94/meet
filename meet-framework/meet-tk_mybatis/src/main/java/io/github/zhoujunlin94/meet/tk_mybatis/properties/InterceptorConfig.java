package io.github.zhoujunlin94.meet.tk_mybatis.properties;

import io.github.zhoujunlin94.meet.common.exception.MeetException;
import lombok.Data;

import java.util.Objects;
import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2024-04-19-16:48
 */
@Data
public class InterceptorConfig {

    private String clazz;

    private Properties properties;

    public void checkInterceptor() {
        if (Objects.isNull(clazz)) {
            throw MeetException.meet("请检查拦截器配置");
        }
    }

}
