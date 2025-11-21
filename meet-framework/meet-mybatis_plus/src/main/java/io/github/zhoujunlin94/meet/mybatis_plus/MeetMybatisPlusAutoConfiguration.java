package io.github.zhoujunlin94.meet.mybatis_plus;

import io.github.zhoujunlin94.meet.mybatis_plus.beanfactorypostprocessor.DataSourceBeanFactoryProcessor;
import io.github.zhoujunlin94.meet.mybatis_plus.properties.DynamicDataSourceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:33
 */
@AutoConfiguration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class MeetMybatisPlusAutoConfiguration {

    @Bean
    public DataSourceBeanFactoryProcessor dataSourceBeanFactoryProcessor() {
        return new DataSourceBeanFactoryProcessor();
    }

}
