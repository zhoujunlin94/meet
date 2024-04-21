package io.github.zhoujunlin94.meet.tk_mybatis.test;

import io.github.zhoujunlin94.meet.tk_mybatis.ExampleApp;
import io.github.zhoujunlin94.meet.tk_mybatis.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.tk_mybatis.handler.meet.MeetUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
        MeetUser meetUser = new MeetUser().setUserId(10086).setUserName("zgyd");
        meetUserHandler.insertSelective(meetUser);
    }

}
