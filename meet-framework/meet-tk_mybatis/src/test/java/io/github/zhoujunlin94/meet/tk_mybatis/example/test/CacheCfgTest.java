package io.github.zhoujunlin94.meet.tk_mybatis.example.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import io.github.zhoujunlin94.meet.tk_mybatis.example.ExampleApp;
import io.github.zhoujunlin94.meet.tk_mybatis.example.entity.base.CacheCfg;
import io.github.zhoujunlin94.meet.tk_mybatis.example.handler.base.CacheCfgHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024/4/21 10:05
 */
@Slf4j
@RunWith(SpringRunner.class)
// @ActiveProfiles(profiles = "dev")
@SpringBootTest(classes = ExampleApp.class)
public class CacheCfgTest {

    @Resource
    private CacheCfgHandler cacheCfgHandler;

    @Test
    public void insertCache() {
        CacheCfg cacheCfg = new CacheCfg().setKey("test").setValue("abc").setDesc("cess");
        cacheCfgHandler.insertSelective(cacheCfg);
    }

    @Test
    @Transactional(transactionManager = "baseTransactionManager")
    public void insertBatch() {
        for (int i = 0; i < 5; i++) {
            CacheCfg cacheCfg = new CacheCfg().setKey("test" + i).setValue("abc" + i).setDesc("cess" + i);
            cacheCfgHandler.insertSelective(cacheCfg);
            if (i == 3) {
                throw MeetException.meet("故意抛出一个异常");
            }
        }
    }

    @Test
    public void insertBatch2() {
        List<CacheCfg> retList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CacheCfg cacheCfg = new CacheCfg().setKey("test" + i).setValue("abc" + i).setDesc("cess" + i);
            retList.add(cacheCfg);
        }
        cacheCfgHandler.batchInsertSelective(retList);
    }

    @Test
    public void deleteCache() {
        cacheCfgHandler.deleteByPrimaryKey("test");
    }

    @Test
    public void updateCache() {
        CacheCfg cacheCfg = new CacheCfg().setKey("test0").setValue("abc1").setDesc("cess1");
        cacheCfgHandler.updateByPrimaryKeySelective(cacheCfg);
    }

    @Test
    public void getCache() {
        CacheCfg cacheCfg = cacheCfgHandler.selectByPrimaryKey("test0");
        System.out.println(cacheCfg);
    }

    @Test
    public void getFromCache() {
        String cache = cacheCfgHandler.selectFromCache("test0");
        System.out.println(cache);
        cache = cacheCfgHandler.selectFromCache("test0");
        System.out.println(cache);
    }

    @Test
    public void listCache() {
        PageInfo<CacheCfg> retPageInfo = PageHelper.startPage(1, 2)
                .doSelectPageInfo(() -> cacheCfgHandler.selectLikeKey("test"));
        System.out.println(retPageInfo);
    }

}
