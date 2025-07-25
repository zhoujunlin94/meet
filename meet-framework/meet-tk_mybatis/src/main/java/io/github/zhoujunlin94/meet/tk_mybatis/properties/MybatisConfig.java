package io.github.zhoujunlin94.meet.tk_mybatis.properties;

import lombok.Data;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024-04-19-16:58
 */
@Data
public class MybatisConfig {

    private String entityPackage;
    private String mapperPackage;
    private String mapperXmlLocation;
    private List<InterceptorConfig> interceptors;

}
