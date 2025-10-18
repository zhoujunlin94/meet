package io.github.zhoujunlin94.meet.tk_mybatis.example.test;

import io.github.zhoujunlin94.meet.tk_mybatis.example.ExampleApp;
import io.github.zhoujunlin94.meet.tk_mybatis.example.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.tk_mybatis.example.handler.meet.MeetUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zhoujunlin
 * @date 2024/4/21 09:59
 */
@Slf4j
// @ActiveProfiles(profiles = "dev")
@SpringBootTest(classes = ExampleApp.class)
public class MeetUserTest {

    @Resource
    private MeetUserHandler meetUserHandler;

    @Test
    public void insertUser() {
        MeetUser meetUser = new MeetUser().setUserId(10000).setUserName("zgyd222");
        // 影响行数
        System.out.println(meetUserHandler.insertIgnore(meetUser));
        // ignore  没有id返回
        System.out.println(meetUser.getId());
    }

}
