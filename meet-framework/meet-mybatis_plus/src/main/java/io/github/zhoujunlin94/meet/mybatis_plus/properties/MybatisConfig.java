package io.github.zhoujunlin94.meet.mybatis_plus.properties;

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

    private boolean banner = false;
    private String metaObjectHandlerClazz;

    private List<InterceptorConfig> interceptors;


}
