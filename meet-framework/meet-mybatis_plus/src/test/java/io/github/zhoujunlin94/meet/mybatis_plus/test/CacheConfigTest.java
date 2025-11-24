package io.github.zhoujunlin94.meet.mybatis_plus.test;

import io.github.zhoujunlin94.meet.mybatis_plus.MeetMybatisPlusApplication;
import io.github.zhoujunlin94.meet.mybatis_plus.entity.base.CacheCfg;
import io.github.zhoujunlin94.meet.mybatis_plus.repository.CacheConfigRepository;
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
    private CacheConfigRepository cacheConfigRepository;

    @Test
    public void testCacheConfigMapper() {
        CacheCfg cacheCfg = cacheConfigRepository.getById("test0");
        log.info("cacheCfg: {}", cacheCfg);
    }

}
