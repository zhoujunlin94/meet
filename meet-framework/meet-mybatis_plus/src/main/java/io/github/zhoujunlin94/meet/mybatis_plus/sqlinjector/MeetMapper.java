package io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author zhoujunlin
 * @date 2025年12月04日 13:23
 * @desc
 */
public interface MeetMapper<T> extends BaseMapper<T> {

    int insertIgnore(T entity);

    int insertOnDuplicateKeyUpdate(T entity);

}
