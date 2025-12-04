package io.github.zhoujunlin94.meet.mybatis_plus.mapper.base;

import io.github.zhoujunlin94.meet.mybatis_plus.entity.base.CacheCfg;
import io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.MeetMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhoujunlin
 * @date 2025年11月21日 13:59
 * @desc
 */
@Mapper
public interface CacheConfigMapper extends MeetMapper<CacheCfg> {
}
