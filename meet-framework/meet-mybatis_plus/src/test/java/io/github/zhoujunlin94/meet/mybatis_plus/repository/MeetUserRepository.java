package io.github.zhoujunlin94.meet.mybatis_plus.repository;

import io.github.zhoujunlin94.meet.mybatis_plus.entity.meet.MeetUser;
import io.github.zhoujunlin94.meet.mybatis_plus.mapper.meet.MeetUserMapper;
import org.springframework.stereotype.Repository;

/**
 * @author zhoujunlin
 * @date 2025年11月24日 9:55
 * @desc
 */
@Repository
public class MeetUserRepository extends BaseRepository<MeetUserMapper, MeetUser> {
}
