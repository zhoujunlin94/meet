package io.github.zhoujunlin94.meet.tk_mybatis;

import io.github.zhoujunlin94.meet.tk_mybatis.beanfactorypostprocessor.DataSourceBeanFactoryProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:33
 */
@Configuration
public class MeetTkMybatisAutoConfiguration {

    @Bean
    public DataSourceBeanFactoryProcessor dataSourceBeanFactoryProcessor() {
        return new DataSourceBeanFactoryProcessor();
    }

}
