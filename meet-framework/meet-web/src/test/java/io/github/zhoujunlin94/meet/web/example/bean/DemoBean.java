package io.github.zhoujunlin94.meet.web.example.bean;

import org.springframework.stereotype.Component;

/**
 * @author zhoujunlin
 * @date 2025年12月16日 16:33
 * @desc
 */
@Component
public class DemoBean {

    public String demo() {
        return "demo";
    }

    public String demo(String a, String b) {
        return a + b;
    }

    public int demo(int c, int d) {
        return c + d;
    }


}
