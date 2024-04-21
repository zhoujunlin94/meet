package io.github.zhoujunlin94.meet.tk_mybatis;

import io.github.zhoujunlin94.meet.tk_mybatis.beanfactorypostprocessor.DataSourceBeanFactoryProcessor;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:33
 */
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MapperAutoConfiguration.class, MybatisAutoConfiguration.class})
public class MeetTKMybatisAutoConfiguration {

    @Bean
    public DataSourceBeanFactoryProcessor meetDataSourceBeanFactoryProcessor() {
        return new DataSourceBeanFactoryProcessor();
    }

}
