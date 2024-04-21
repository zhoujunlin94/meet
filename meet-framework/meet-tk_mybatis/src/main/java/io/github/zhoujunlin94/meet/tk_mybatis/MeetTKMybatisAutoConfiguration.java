package io.github.zhoujunlin94.meet.tk_mybatis;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:33
 */
@Configuration
@ComponentScan
public class MeetTKMybatisAutoConfiguration {


    @Component
    @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MapperAutoConfiguration.class, MybatisAutoConfiguration.class, MeetTKMybatisAutoConfiguration.class})
    static class ExcludeAutoConfiguration {

    }

}
