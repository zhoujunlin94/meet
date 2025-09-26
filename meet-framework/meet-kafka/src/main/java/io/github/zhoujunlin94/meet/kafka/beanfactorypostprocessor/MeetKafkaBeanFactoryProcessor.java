package io.github.zhoujunlin94.meet.kafka.beanfactorypostprocessor;

import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:29
 * @desc
 */
@RequiredArgsConstructor
public class MeetKafkaBeanFactoryProcessor implements BeanDefinitionRegistryPostProcessor {

    private final MeetKafkaProperties meetKafkaProperties;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println(meetKafkaProperties);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
