package io.github.zhoujunlin94.meet.tk_mybatis;

import io.github.zhoujunlin94.meet.tk_mybatis.beanfactorypostprocessor.DataSourceBeanFactoryProcessor;
import io.github.zhoujunlin94.meet.tk_mybatis.properties.DynamicDataSourceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:33
 */
@AutoConfiguration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class MeetTkMybatisAutoConfiguration {

    @Bean
    public DataSourceBeanFactoryProcessor dataSourceBeanFactoryProcessor() {
        return new DataSourceBeanFactoryProcessor();
    }

}
