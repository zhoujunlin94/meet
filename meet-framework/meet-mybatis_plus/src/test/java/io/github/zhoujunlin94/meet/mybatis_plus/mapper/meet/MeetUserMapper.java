package io.github.zhoujunlin94.meet.mybatis_plus.mapper.meet;

import io.github.zhoujunlin94.meet.mybatis_plus.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.MeetMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhoujunlin
 * @date 2025年11月21日 13:50
 * @desc
 */
@Mapper
public interface MeetUserMapper extends MeetMapper<MeetUser> {
}
