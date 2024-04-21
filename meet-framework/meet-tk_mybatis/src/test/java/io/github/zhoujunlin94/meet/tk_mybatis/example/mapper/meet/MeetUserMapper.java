package io.github.zhoujunlin94.meet.tk_mybatis.example.mapper.meet;

import io.github.zhoujunlin94.meet.tk_mybatis.example.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.tk_mybatis.mapper.TKMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetUserMapper extends TKMapper<MeetUser> {
}