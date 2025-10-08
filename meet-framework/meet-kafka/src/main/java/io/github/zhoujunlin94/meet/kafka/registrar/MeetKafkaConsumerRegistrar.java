package io.github.zhoujunlin94.meet.kafka.registrar;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.kafka.interceptor.MDCRecordInterceptor;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaConsumerProperties;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProperties;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2025/10/8 20:12
 */
public class MeetKafkaConsumerRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;
    private static final String CONSUMER_FACTORY_SUFFIX = "_ConsumerFactory";

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        Binder binder = Binder.get(environment);
        MeetKafkaProperties meetKafkaProps = binder.bind("meet.kafka", Bindable.of(MeetKafkaProperties.class)).orElse(null);
        if (Objects.isNull(meetKafkaProps) || Objects.isNull(meetKafkaProps.getConsumer())) {
            return;
        }

        Map<String, ConsumerFactory<String, Object>> consumerFactoryMap = new HashMap<>();

        MeetKafkaConsumerProperties consumerProps = meetKafkaProps.getConsumer();
        Map<String, Object> mainProperties = consumerProps.buildProperties();
        ConsumerFactory<String, Object> mainConsumerFactory = new DefaultKafkaConsumerFactory<String, Object>(mainProperties, consumerProps::getKeyDeserializerInstance, consumerProps::getValueDeserializerInstance);
        String beanName = StrUtil.blankToDefault(consumerProps.getName(), "meet") + CONSUMER_FACTORY_SUFFIX;
        consumerFactoryMap.put(beanName, mainConsumerFactory);

        if (CollUtil.isNotEmpty(consumerProps.getItems())) {
            consumerProps.getItems().forEach(item -> {
                if (StrUtil.isNotBlank(item.getName()) && CollUtil.isNotEmpty(item.getBootstrapServers())) {
                    Map<String, Object> itemProperties = new HashMap<>();
                    itemProperties.putAll(mainProperties);
                    itemProperties.putAll(item.buildProperties());

                    ConsumerFactory<String, Object> itemConsumerFactory = new DefaultKafkaConsumerFactory<String, Object>(itemProperties, item::getKeyDeserializerInstance, item::getValueDeserializerInstance);
                    String itemBeanName = item.getName() + CONSUMER_FACTORY_SUFFIX;
                    consumerFactoryMap.put(itemBeanName, itemConsumerFactory);
                }
            });
        }

        consumerFactoryMap.forEach((consumerFactoryBeanName, consumerFactory) -> {
            GenericBeanDefinition kafkaListenerContainerFactoryDef = new GenericBeanDefinition();
            kafkaListenerContainerFactoryDef.setBeanClass(ConcurrentKafkaListenerContainerFactory.class);
            kafkaListenerContainerFactoryDef.setInstanceSupplier(() -> {
                ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
                kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
                kafkaListenerContainerFactory.setRecordInterceptor(new MDCRecordInterceptor<>());
                return kafkaListenerContainerFactory;
            });
            String kafkaListenerContainerFactoryBeanName = StrUtil.removeSuffix(consumerFactoryBeanName, CONSUMER_FACTORY_SUFFIX);
            registry.registerBeanDefinition(kafkaListenerContainerFactoryBeanName, kafkaListenerContainerFactoryDef);
        });

    }
}
