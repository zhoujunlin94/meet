package io.github.zhoujunlin94.meet.mybatis_plus.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.zhoujunlin94.meet.mybatis_plus.MeetMybatisPlusApplication;
import io.github.zhoujunlin94.meet.mybatis_plus.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.mybatis_plus.repository.MeetUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
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
        MDC.put("X-REQUEST-ID", "123456");
        MeetUser meetUser = meetUserRepository.getById(1);
        log.info("meetUser: {}", meetUser);
    }


    @Test
    public void testAll() {
        List<MeetUser> meetUsers = meetUserRepository.list();
        log.info("meetUsers: {}", meetUsers);
    }

    @Test
    public void testPage() {
        PageInfo<MeetUser> entityPageInfo = PageHelper.startPage(2, 2).doSelectPageInfo(() -> meetUserRepository.list());
        log.warn("entityPageInfo: {}", entityPageInfo);
    }


}
