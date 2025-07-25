package io.github.zhoujunlin94.mqtt.beanfactorypostprocessor;

import cn.hutool.core.collection.CollUtil;
import io.github.zhoujunlin94.mqtt.callback.MqttClientCallBack;
import io.github.zhoujunlin94.mqtt.properties.Client;
import io.github.zhoujunlin94.mqtt.properties.MqttClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:41
 */
@Slf4j
@Component
public class MqttClientBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private Environment environment;
    private ApplicationContext applicationContext;


    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        MqttClientProperties mqttClientProperties = Binder.get(this.environment).bind("mqtt", MqttClientProperties.class).get();
        if (Objects.isNull(mqttClientProperties) || CollUtil.isEmpty(mqttClientProperties.getClient())) {
            return;
        }
        Map<String, Client> clientPropertiesMap = mqttClientProperties.getClient();
        clientPropertiesMap.forEach((clientName, client) -> {
            client.check();
            GenericBeanDefinition mqttClientBeanDefinition = new GenericBeanDefinition();
            mqttClientBeanDefinition.setBeanClass(MqttClient.class);
            mqttClientBeanDefinition.setInstanceSupplier(() -> {
                try {
                    return mqttClient(client);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            });
            registry.registerBeanDefinition(clientName, mqttClientBeanDefinition);
        });

    }

    private MqttClient mqttClient(Client client) throws MqttException {
        MqttClient mqttClient = new MqttClient(client.getServerUri(), client.getId(), new MemoryPersistence());

        MqttConnectOptions options = new MqttConnectOptions();
        Optional.ofNullable(client.getOptions()).ifPresent(optionsProps -> {
            options.setCleanSession(optionsProps.isCleanSession());
            options.setKeepAliveInterval(optionsProps.getKeepAliveInterval());
            options.setConnectionTimeout(optionsProps.getConnectionTimeout());
            options.setAutomaticReconnect(optionsProps.isAutomaticReconnect());
        });

        mqttClient.setCallback(new MqttClientCallBack(applicationContext));

        mqttClient.connect(options);

        /**
         * $share/{GroupID}/{Topic}  通过mqtt的共享主题模式达到集群消费模式
         * 正常模式就是广播消费
         */
        if (CollUtil.isNotEmpty(client.getSubscribes())) {
            client.getSubscribes().forEach(subscribeTopic -> {
                try {
                    mqttClient.subscribe(subscribeTopic.getTopicFilter(), subscribeTopic.getQos());
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return mqttClient;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
