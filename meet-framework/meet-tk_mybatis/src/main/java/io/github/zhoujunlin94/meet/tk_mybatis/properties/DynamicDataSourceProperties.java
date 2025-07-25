package io.github.zhoujunlin94.meet.tk_mybatis.properties;

import lombok.Data;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:30
 */
@Data
public class DynamicDataSourceProperties {

    private Map<String, DatasourceConfig> datasource;

}
