package io.github.zhoujunlin94.meet.feign;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhoujunlin
 * @date 2025-07-21 15:27
 */
@FeignClient(name = "demoFeignClient", url = "https://t-luffy.gaodunwangxiao.com")
public interface DemoFeignClient {

    @PostMapping(value = "/api/v1/test/glive1/update")
    String update(@RequestBody JSONObject jsonObject);

}
