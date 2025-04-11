package io.github.zhoujunlin94.meet.tk_mybatis.example.test;

import io.github.zhoujunlin94.meet.tk_mybatis.example.ExampleApp;
import io.github.zhoujunlin94.meet.tk_mybatis.example.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.tk_mybatis.example.handler.meet.MeetUserHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhoujunlin
 * @date 2024/4/21 09:59
 */
@Slf4j
@RunWith(SpringRunner.class)
// @ActiveProfiles(profiles = "dev")
@SpringBootTest(classes = ExampleApp.class)
public class MeetUserTest {

    @Resource
    private MeetUserHandler meetUserHandler;

    @Test
    public void insertUser() {
        MeetUser meetUser = new MeetUser().setUserId(100258).setUserName("zgyd222");
        System.out.println(meetUserHandler.insertIgnore(meetUser));
        System.out.println(meetUser.getId());
    }

}
