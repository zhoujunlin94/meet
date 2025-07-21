package io.github.zhoujunlin94.meet.feign;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhoujunlin
 * @date 2025-07-21 15:29
 */
@Slf4j
@EnableFeignClients
@SpringBootTest(classes = ExampleApp.class)
public class TestFeignClient {

    @Resource
    private DemoFeignClient demoFeignClient;

    @Test
    public void test() {
        demoFeignClient.update(new JSONObject().fluentPut("handlerName", "giftPackDetailHandler")
                .fluentPut("id", 88888888));
    }

}
