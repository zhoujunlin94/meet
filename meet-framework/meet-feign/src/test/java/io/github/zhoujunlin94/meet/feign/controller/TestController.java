package io.github.zhoujunlin94.meet.feign.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoujunlin
 * @date 2025/7/21 21:42
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping(value = "/hello")
    public JSONObject hello(@RequestBody JSONObject jsonObject) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(60L);
        return jsonObject;
    }

}
