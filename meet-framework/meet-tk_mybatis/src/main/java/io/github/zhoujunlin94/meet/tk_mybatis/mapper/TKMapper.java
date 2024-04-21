package io.github.zhoujunlin94.meet.tk_mybatis.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author zhoujunlin
 * @date 2024年03月08日 15:42
 * @desc
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface TKMapper<T> extends Mapper<T>, MySqlMapper<T>, BatchUpdateMapper<T>, BatchInsertMapper<T> {

}
