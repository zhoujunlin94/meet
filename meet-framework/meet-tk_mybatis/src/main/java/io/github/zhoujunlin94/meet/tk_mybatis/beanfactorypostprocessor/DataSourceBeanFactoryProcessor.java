package io.github.zhoujunlin94.meet.tk_mybatis.beanfactorypostprocessor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import io.github.zhoujunlin94.meet.tk_mybatis.constant.Constant;
import io.github.zhoujunlin94.meet.tk_mybatis.properties.DatasourceConfig;
import io.github.zhoujunlin94.meet.tk_mybatis.properties.InterceptorConfig;
import io.github.zhoujunlin94.meet.tk_mybatis.properties.MultipleDataSourceProperties;
import io.github.zhoujunlin94.meet.tk_mybatis.properties.MybatisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.mapper.ClassPathMapperScanner;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:53
 */
@Slf4j
@Component
public class DataSourceBeanFactoryProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware, ResourceLoaderAware {

    private Environment environment;
    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        MultipleDataSourceProperties multipleDataSourceProperties = Binder.get(this.environment).bind(Constant.MULTIPLE_PREFIX, MultipleDataSourceProperties.class).get();
        if (Objects.isNull(multipleDataSourceProperties) || CollUtil.isEmpty(multipleDataSourceProperties.getDatasource())) {
            return;
        }

        multipleDataSourceProperties.getDatasource().forEach((name, datasourceConfig) -> {
            String datasourceName = name + "Datasource";
            String dataSourceTransactionManagerName = name + "TransactionManager";
            String sqlSessionFactoryName = name + "SqlSessionFactoryName";
            // 1. 数据源
            buildDataSource(datasourceName, datasourceConfig, registry);
            // 2. 事务管理器
            buildDataSourceTransactionManager(datasourceName, dataSourceTransactionManagerName, registry);
            // 3. 构建SqlSessionFactory
            buildSqlSessionFactory(datasourceName, sqlSessionFactoryName, datasourceConfig, registry);
            // 4. 注册mapper
            registerMapper(sqlSessionFactoryName, datasourceConfig, registry);
        });

    }

    private void registerMapper(String sqlSessionFactoryName, DatasourceConfig datasourceConfig, BeanDefinitionRegistry registry) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        scanner.setAnnotationClass(Mapper.class);
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryName);

        List<String> basePackages = StrUtil.splitTrim(datasourceConfig.getMybatis().getBasePackages(), StrUtil.COMMA);

        try {
            scanner.setMapperProperties(this.environment);
        } catch (Exception e) {
            log.warn("只有 Spring Boot 环境中可以通过 Environment(配置文件,环境变量,运行参数等方式) 配置通用 Mapper，" +
                    "其他环境请通过 @MapperScan 注解中的 mapperHelperRef 或 properties 参数进行配置!" +
                    "如果你使用 tk.mybatis.mapper.session.Configuration 配置的通用 Mapper，你可以忽略该错误!", e);
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    private void buildSqlSessionFactory(String datasourceName, String sqlSessionFactoryName, DatasourceConfig datasourceConfig, BeanDefinitionRegistry registry) {
        GenericBeanDefinition sqlSessionFactoryBeanDefinition = new GenericBeanDefinition();
        sqlSessionFactoryBeanDefinition.setBeanClass(SqlSessionFactory.class);

        sqlSessionFactoryBeanDefinition.setInstanceSupplier(() -> {
            try {
                datasourceConfig.checkMyBatis();
                MybatisConfig mybatisConfig = datasourceConfig.getMybatis();

                SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
                sqlSessionFactoryBean.setDataSource(applicationContext.getBean(datasourceName, DataSource.class));
                sqlSessionFactoryBean.setTypeAliasesPackage(mybatisConfig.getTypeAliasesPackage());
                sqlSessionFactoryBean.setMapperLocations(
                        new PathMatchingResourcePatternResolver().getResources(mybatisConfig.getMapperLocation())
                );
                // 插件
                if (CollUtil.isNotEmpty(mybatisConfig.getInterceptors())) {
                    List<Interceptor> interceptors = new ArrayList<>();
                    for (InterceptorConfig interceptorConfig : mybatisConfig.getInterceptors()) {
                        interceptorConfig.checkInterceptor();

                        Interceptor interceptor = ReflectUtil.newInstance(interceptorConfig.getClazz());
                        if (Objects.nonNull(interceptorConfig.getProperties())) {
                            interceptor.setProperties(interceptorConfig.getProperties());
                        }
                        interceptors.add(interceptor);
                    }

                    if (CollUtil.isNotEmpty(interceptors)) {
                        sqlSessionFactoryBean.setPlugins(interceptors.toArray(new Interceptor[]{}));
                    }
                }

                Configuration configuration = Objects.requireNonNull(sqlSessionFactoryBean.getObject()).getConfiguration();
                configuration.setMapUnderscoreToCamelCase(true);
                return sqlSessionFactoryBean.getObject();
            } catch (Exception e) {
                log.error("配置Mybatis出错", e);
                throw MeetException.meet("配置Mybatis出错");
            }
        });

        registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionFactoryBeanDefinition);
    }

    private void buildDataSourceTransactionManager(String datasourceName, String dataSourceTransactionManagerName, BeanDefinitionRegistry registry) {
        GenericBeanDefinition dataSourceTransactionManagerBeanDefinition = new GenericBeanDefinition();
        dataSourceTransactionManagerBeanDefinition.setBeanClass(DataSourceTransactionManager.class);

        dataSourceTransactionManagerBeanDefinition.setInstanceSupplier(() -> new DataSourceTransactionManager(applicationContext.getBean(datasourceName, DataSource.class)));

        registry.registerBeanDefinition(dataSourceTransactionManagerName, dataSourceTransactionManagerBeanDefinition);
    }

    private void buildDataSource(String datasourceName, DatasourceConfig datasourceConfig, BeanDefinitionRegistry registry) {
        GenericBeanDefinition datasourceBeanDefinition = new GenericBeanDefinition();
        datasourceBeanDefinition.setBeanClass(DataSource.class);

        datasourceBeanDefinition.setInstanceSupplier(() -> {
            datasourceConfig.checkDatasource();

            Properties configHikari = datasourceConfig.getHikari();
            configHikari.put("jdbcUrl", datasourceConfig.getUrl());
            configHikari.put("driverClassName", datasourceConfig.getDriverClassName());
            configHikari.put("username", datasourceConfig.getUsername());
            configHikari.put("password", datasourceConfig.getPassword());

            final HikariConfig hikariConfig = new HikariConfig(configHikari);
            return new HikariDataSource(hikariConfig);
        });

        registry.registerBeanDefinition(datasourceName, datasourceBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
