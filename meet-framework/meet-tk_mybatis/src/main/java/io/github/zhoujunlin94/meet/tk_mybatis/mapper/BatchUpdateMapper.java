package io.github.zhoujunlin94.meet.tk_mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024年03月08日 14:39
 * @desc
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface BatchUpdateMapper<T> {

    /**
     * 根据主键批量更新：Java实体类中属性不为空才会更新,否则更新为数据库原值
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = BatchUpdateProvider.class, method = "dynamicSQL")
    int batchUpdateSelective(List<T> recordList);


}
