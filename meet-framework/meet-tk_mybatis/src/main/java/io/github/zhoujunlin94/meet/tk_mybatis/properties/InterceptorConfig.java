package io.github.zhoujunlin94.meet.tk_mybatis.properties;

import lombok.Data;

import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2024-04-19-16:48
 */
@Data
public class InterceptorConfig {

    private String clazz;

    private Properties properties;

}
