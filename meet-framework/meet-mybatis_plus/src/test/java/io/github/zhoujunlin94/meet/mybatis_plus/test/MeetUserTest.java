package io.github.zhoujunlin94.meet.mybatis_plus.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.zhoujunlin94.meet.mybatis_plus.MybatisPlusApplication;
import io.github.zhoujunlin94.meet.mybatis_plus.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.mybatis_plus.repository.MeetUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
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
@SpringBootTest(classes = MybatisPlusApplication.class)
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
    public void testInsertIgnore() {
        MeetUser meetUser = new MeetUser();
        meetUser.setId(1000000);
        meetUser.setUserName("testInsertIgnore");
        meetUser.setUserId(1234567);
        int insertIgnore = meetUserRepository.insertIgnore(meetUser);
        log.info("insertIgnore: {}", insertIgnore);
    }


    @Test
    public void testInsertOnDuplicateKeyUpdate() {
        MeetUser meetUser = new MeetUser();
        meetUser.setId(1000000);
        meetUser.setUserName("testInsertOnDuplicateKeyUpdate");
        meetUser.setUserId(1234567);
        meetUserRepository.insertOnDuplicateKeyUpdate(meetUser);
    }

    @Test
    public void testStream() {
        LambdaQueryWrapper<MeetUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(MeetUser::getId, 0);
        meetUserRepository.selectStream(lambdaQueryWrapper, new ResultHandler<>() {
            int count = 0;

            @Override
            public void handleResult(ResultContext<? extends MeetUser> resultContext) {
                MeetUser meetUser = resultContext.getResultObject();
                System.out.println("当前处理第" + (++count) + "条记录: " + meetUser);
            }
        });
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
