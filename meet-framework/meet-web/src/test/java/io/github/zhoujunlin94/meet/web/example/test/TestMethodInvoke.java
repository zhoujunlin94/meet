package io.github.zhoujunlin94.meet.web.example.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import io.github.zhoujunlin94.meet.web.component.BeanMethodReflectInvoker;
import io.github.zhoujunlin94.meet.web.example.ExampleApp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2025年12月16日 16:32
 * @desc
 */
@Slf4j
@SpringBootTest(classes = ExampleApp.class)
public class TestMethodInvoke {


    @Resource
    private BeanMethodReflectInvoker beanMethodReflectInvoker;

    @Test
    public void testBeanMethodInvoke() {
        Object result = beanMethodReflectInvoker.invoke("demoBean", "demo", Map.of("a", "a", "b", "b"));
        log.warn(result.toString());

        result = beanMethodReflectInvoker.invoke("demoBean", "demo", Map.of("c", 1, "d", 2));
        log.warn(result.toString());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("c", 1);
        paramMap.put("d", null);
        String paramStr = JSON.toJSONString(paramMap, JSONWriter.Feature.WriteNulls);
        log.warn(paramStr);
        Map<String, Object> paramMap2 = JSONObject.parseObject(paramStr, new TypeReference<>() {
        });
        log.warn(paramMap2.toString());

        result = beanMethodReflectInvoker.invoke("demoBean", "demo", paramMap2);
        log.warn(result.toString());

        result = beanMethodReflectInvoker.invoke("demoBean", "demo", Map.of());
        log.warn(result.toString());

    }

}
