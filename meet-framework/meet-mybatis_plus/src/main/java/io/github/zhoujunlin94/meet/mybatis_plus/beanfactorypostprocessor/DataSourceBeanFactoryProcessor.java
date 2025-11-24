package io.github.zhoujunlin94.meet.mybatis_plus.beanfactorypostprocessor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import io.github.zhoujunlin94.meet.mybatis_plus.constant.MybatisPlusConstant;
import io.github.zhoujunlin94.meet.mybatis_plus.properties.DatasourceConfig;
import io.github.zhoujunlin94.meet.mybatis_plus.properties.DynamicDataSourceProperties;
import io.github.zhoujunlin94.meet.mybatis_plus.properties.InterceptorConfig;
import io.github.zhoujunlin94.meet.mybatis_plus.properties.MybatisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
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
import org.springframework.util.StringUtils;

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
        DynamicDataSourceProperties dynamicDataSourceProperties = Binder.get(this.environment).bind(MybatisPlusConstant.DYNAMIC_PREFIX, DynamicDataSourceProperties.class).orElse(null);
        if (Objects.isNull(dynamicDataSourceProperties) || CollUtil.isEmpty(dynamicDataSourceProperties.getDatasource())) {
            return;
        }

        dynamicDataSourceProperties.getDatasource().forEach((name, datasourceConfig) -> {
            String datasourceName = name + "Datasource";
            String dataSourceTransactionManagerName = name + "TransactionManager";
            String sqlSessionFactoryName = name + "SqlSessionFactory";
            // 1. 数据源
            if (buildDataSource(datasourceName, datasourceConfig, registry)) {
                // 2. 事务管理器
                buildDataSourceTransactionManager(datasourceName, dataSourceTransactionManagerName, registry);
                // 3. 构建SqlSessionFactory
                if (buildSqlSessionFactory(datasourceName, sqlSessionFactoryName, datasourceConfig, registry)) {
                    // 4. 注册mapper
                    registerMapper(sqlSessionFactoryName, datasourceConfig, registry);
                }
            }
        });

    }

    private void registerMapper(String sqlSessionFactoryName, DatasourceConfig datasourceConfig, BeanDefinitionRegistry registry) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry, environment);
        if (Objects.nonNull(resourceLoader)) {
            scanner.setResourceLoader(resourceLoader);
        }

        scanner.setAnnotationClass(Mapper.class);
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryName);

        List<String> mapperPackages = StrUtil.splitTrim(datasourceConfig.getMybatis().getMapperPackage(), StrUtil.COMMA);

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(mapperPackages));
    }

    private boolean buildSqlSessionFactory(String datasourceName, String sqlSessionFactoryName, DatasourceConfig datasourceConfig, BeanDefinitionRegistry registry) {
        if (!datasourceConfig.checkMyBatis()) {
            return false;
        }

        GenericBeanDefinition sqlSessionFactoryBeanDefinition = new GenericBeanDefinition();
        sqlSessionFactoryBeanDefinition.setBeanClass(SqlSessionFactory.class);
        sqlSessionFactoryBeanDefinition.setDependsOn(datasourceName);

        sqlSessionFactoryBeanDefinition.setInstanceSupplier(() -> {
            try {
                MybatisConfig mybatisConfig = datasourceConfig.getMybatis();

                MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
                sqlSessionFactoryBean.setDataSource(applicationContext.getBean(datasourceName, DataSource.class));
                sqlSessionFactoryBean.setTypeAliasesPackage(mybatisConfig.getEntityPackage());
                if (StrUtil.isNotBlank(mybatisConfig.getMapperXmlLocation())) {
                    sqlSessionFactoryBean.setMapperLocations(
                            new PathMatchingResourcePatternResolver().getResources(mybatisConfig.getMapperXmlLocation())
                    );
                }
                // 插件
                if (CollUtil.isNotEmpty(mybatisConfig.getInterceptors())) {
                    List<Interceptor> interceptors = new ArrayList<>();
                    for (InterceptorConfig interceptorConfig : mybatisConfig.getInterceptors()) {
                        if (StrUtil.isBlank(interceptorConfig.getClazz())) {
                            continue;
                        }
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
                // 全局配置
                GlobalConfig globalConfig = new GlobalConfig();
                globalConfig.setBanner(mybatisConfig.isBanner());
                if (StrUtil.isNotBlank(mybatisConfig.getMetaObjectHandler())) {
                    globalConfig.setMetaObjectHandler(ReflectUtil.newInstance(mybatisConfig.getMetaObjectHandler()));
                }
                sqlSessionFactoryBean.setGlobalConfig(globalConfig);

                Configuration configuration = Objects.requireNonNull(sqlSessionFactoryBean.getObject()).getConfiguration();
                configuration.setMapUnderscoreToCamelCase(true);
                return sqlSessionFactoryBean.getObject();
            } catch (Exception e) {
                log.error("配置Mybatis出错", e);
                throw MeetException.meet("配置Mybatis出错");
            }
        });

        registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionFactoryBeanDefinition);

        return true;
    }

    private void buildDataSourceTransactionManager(String datasourceName, String dataSourceTransactionManagerName, BeanDefinitionRegistry registry) {
        GenericBeanDefinition dataSourceTransactionManagerBeanDefinition = new GenericBeanDefinition();
        dataSourceTransactionManagerBeanDefinition.setBeanClass(DataSourceTransactionManager.class);
        dataSourceTransactionManagerBeanDefinition.setDependsOn(datasourceName);
        dataSourceTransactionManagerBeanDefinition.setInstanceSupplier(() -> new DataSourceTransactionManager(applicationContext.getBean(datasourceName, DataSource.class)));

        registry.registerBeanDefinition(dataSourceTransactionManagerName, dataSourceTransactionManagerBeanDefinition);
    }

    private boolean buildDataSource(String datasourceName, DatasourceConfig datasourceConfig, BeanDefinitionRegistry registry) {
        if (!datasourceConfig.checkDatasource()) {
            return false;
        }

        GenericBeanDefinition datasourceBeanDefinition = new GenericBeanDefinition();
        datasourceBeanDefinition.setBeanClass(DataSource.class);

        datasourceBeanDefinition.setInstanceSupplier(() -> {
            Properties configHikari = datasourceConfig.getHikari().buildProperties();
            configHikari.put("jdbcUrl", datasourceConfig.getUrl());
            configHikari.put("driverClassName", datasourceConfig.getDriverClassName());
            configHikari.put("username", datasourceConfig.getUsername());
            configHikari.put("password", datasourceConfig.getPassword());

            final HikariConfig hikariConfig = new HikariConfig(configHikari);
            return new HikariDataSource(hikariConfig);
        });

        registry.registerBeanDefinition(datasourceName, datasourceBeanDefinition);

        return true;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
