package io.github.zhoujunlin94.meet.tk_mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2024-04-18-17:48
 */
@SpringBootApplication
public class ExampleApp {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleApp.class);

        Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);
        System.out.println(dataSourceMap);

        Map<String, DataSourceTransactionManager> transactionManagerMap = applicationContext.getBeansOfType(DataSourceTransactionManager.class);
        System.out.println(transactionManagerMap);


        Map<String, SqlSessionFactory> sqlSessionFactoryMap = applicationContext.getBeansOfType(SqlSessionFactory.class);
        System.out.println(sqlSessionFactoryMap);
    }

}
