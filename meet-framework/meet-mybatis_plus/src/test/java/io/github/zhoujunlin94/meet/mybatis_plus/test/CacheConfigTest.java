package io.github.zhoujunlin94.meet.mybatis_plus.test;

import io.github.zhoujunlin94.meet.mybatis_plus.MeetMybatisPlusApplication;
import io.github.zhoujunlin94.meet.mybatis_plus.entity.base.CacheCfg;
import io.github.zhoujunlin94.meet.mybatis_plus.mapper.base.CacheConfigMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhoujunlin
 * @date 2025年11月21日 14:00
 * @desc
 */
@Slf4j
@SpringBootTest(classes = MeetMybatisPlusApplication.class)
public class CacheConfigTest {

    @Resource
    private CacheConfigMapper cacheConfigMapper;

    @Test
    public void testCacheConfigMapper() {
        CacheCfg cacheCfg = cacheConfigMapper.selectById("test0");
        log.info("cacheCfg: {}", cacheCfg);
    }

}
