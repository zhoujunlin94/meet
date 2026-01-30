package io.github.zhoujunlin94.meet.feign.test;

import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.meet.feign.FeignApplication;
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
@SpringBootTest(classes = FeignApplication.class)
public class TestFeignClient {

    @Resource
    private DemoFeignClient demoFeignClient;

    @Test
    public void test() {
        System.out.println(demoFeignClient.hello(new JSONObject().fluentPut("handlerName", "helloHandler")
                .fluentPut("id", 88888888)).toJSONString());
    }

}
