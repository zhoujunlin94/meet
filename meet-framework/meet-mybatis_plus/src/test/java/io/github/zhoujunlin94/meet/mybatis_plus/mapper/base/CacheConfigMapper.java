package io.github.zhoujunlin94.meet.mybatis_plus.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.zhoujunlin94.meet.mybatis_plus.entity.base.CacheCfg;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhoujunlin
 * @date 2025年11月21日 13:59
 * @desc
 */
@Mapper
public interface CacheConfigMapper extends BaseMapper<CacheCfg> {
}
