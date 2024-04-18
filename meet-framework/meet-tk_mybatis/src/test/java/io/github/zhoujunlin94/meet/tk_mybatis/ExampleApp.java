package io.github.zhoujunlin94.meet.tk_mybatis;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;

/**
 * @author zhoujunlin
 * @date 2024-04-18-17:48
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MapperAutoConfiguration.class, MybatisAutoConfiguration.class})
public class ExampleApp {

    public static void main(String[] args) {

        SpringApplication.run(ExampleApp.class);

    }

}
