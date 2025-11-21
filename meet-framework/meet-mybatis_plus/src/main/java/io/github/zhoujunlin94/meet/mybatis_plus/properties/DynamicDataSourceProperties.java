package io.github.zhoujunlin94.meet.mybatis_plus.properties;

import io.github.zhoujunlin94.meet.mybatis_plus.constant.MybatisPlusConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:30
 */
@Data
@ConfigurationProperties(prefix = MybatisPlusConstant.DYNAMIC_PREFIX)
public class DynamicDataSourceProperties {

    private Map<String, DatasourceConfig> datasource;

}
