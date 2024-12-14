package io.github.zhoujunlin94.meet.tk_mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024年03月08日 14:39
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface BatchInsertMapper<T> {

    /**
     * 批量新增 以list中第一个元素中非空属性作为插入字段
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    @Options(useGeneratedKeys = true, keyProperty = "list.id")
    int batchInsertSelective(@Param("list") List<T> recordList);


}
