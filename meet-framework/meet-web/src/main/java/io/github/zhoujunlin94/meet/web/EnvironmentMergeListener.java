package io.github.zhoujunlin94.meet.web;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author zhoujunlin
 * @date 2024-04-22-15:59
 */
public class EnvironmentMergeListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        StandardEnvironment thisEnvironment = new StandardEnvironment();
        thisEnvironment.getPropertySources().addLast(new MapPropertySource("MeetWeb",
                ImmutableMap.of(
                        "spring.mvc.pathmatch.matching-strategy", "ant_path_matcher"
                )));

        event.getEnvironment().merge(thisEnvironment);
    }
}
