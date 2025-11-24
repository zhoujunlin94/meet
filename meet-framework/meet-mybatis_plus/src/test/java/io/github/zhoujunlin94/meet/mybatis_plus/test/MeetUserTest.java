package io.github.zhoujunlin94.meet.mybatis_plus.test;

import io.github.zhoujunlin94.meet.mybatis_plus.MeetMybatisPlusApplication;
import io.github.zhoujunlin94.meet.mybatis_plus.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.mybatis_plus.repository.MeetUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2025年11月21日 13:52
 * @desc
 */
@Slf4j
@SpringBootTest(classes = MeetMybatisPlusApplication.class)
public class MeetUserTest {

    @Resource
    private MeetUserRepository meetUserRepository;

    @Test
    public void testMeetUserMapper() {
        MeetUser meetUser = meetUserRepository.getById(1);
        log.info("meetUser: {}", meetUser);
    }


    @Test
    public void testAll() {
        List<MeetUser> meetUsers = meetUserRepository.list();
        log.info("meetUsers: {}", meetUsers);
    }


}
