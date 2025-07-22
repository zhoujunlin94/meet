package io.github.zhoujunlin94.meet.feign.test;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhoujunlin
 * @date 2025-07-21 15:27
 */
@FeignClient(name = "demoFeignClient", url = "http://127.0.0.1:8080")
public interface DemoFeignClient {

    @PostMapping(value = "/test/hello")
    JSONObject hello(@RequestBody JSONObject jsonObject);

}
