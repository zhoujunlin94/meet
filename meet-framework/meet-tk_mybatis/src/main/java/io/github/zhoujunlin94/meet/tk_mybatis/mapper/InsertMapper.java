package io.github.zhoujunlin94.meet.tk_mybatis.mapper;


/**
 * @author zhoujunlin
 * @date 2024年03月08日 14:39
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface InsertMapper<T> {

    /**
     * 新增/更新(唯一键冲突时)
     */
    @org.apache.ibatis.annotations.InsertProvider(type = InsertProvider.class, method = "dynamicSQL")
    int insertOrUpdate(T record);

    /**
     * 新增(唯一键冲突时不更新)
     * @param record
     * @return
     */
    @org.apache.ibatis.annotations.InsertProvider(type = InsertProvider.class, method = "dynamicSQL")
    int insertIgnore(T record);

}
