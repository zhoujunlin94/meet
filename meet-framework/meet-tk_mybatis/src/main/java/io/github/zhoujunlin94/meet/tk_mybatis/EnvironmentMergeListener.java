package io.github.zhoujunlin94.meet.tk_mybatis;

import com.google.common.collect.ImmutableMap;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;

/**
 * @author zhoujunlin
 * @date 2024-04-22-15:59
 */
public class EnvironmentMergeListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        StandardEnvironment thisEnvironment = new StandardEnvironment();
        thisEnvironment.getPropertySources().addLast(new MapPropertySource("MeetTKMybatis",
                ImmutableMap.of(
                        "spring.autoconfigure.exclude[0]", DataSourceAutoConfiguration.class.getName(),
                        "spring.autoconfigure.exclude[1]", MapperAutoConfiguration.class.getName(),
                        "spring.autoconfigure.exclude[2]", MybatisAutoConfiguration.class.getName()
                )));

        event.getEnvironment().merge(thisEnvironment);

    }
}
