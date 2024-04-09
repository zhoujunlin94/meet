package io.github.zhoujunlin94.meet.tk_mybatis.handler;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import io.github.zhoujunlin94.meet.common.util.ReflectUtils;
import io.github.zhoujunlin94.meet.tk_mybatis.mapper.TKMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2023年07月09日 17:02
 * @desc
 */
public class TKHandler<M extends TKMapper<T>, T> {

    @Autowired
    protected M baseMapper;

    private final Class<T> entityClass = currentEntityClass();

    private final Class<T> currentEntityClass() {
        return (Class<T>) ReflectUtils.getSuperClassGenericType(this.getClass(), TKHandler.class, 1);
    }

    protected String likeString(String value) {
        return "%" + value + "%";
    }

    protected Weekend<T> thisWeekend() {
        return Weekend.of(entityClass, true, true);
    }

    // =============================CRUD START===================================

    public int insertSelective(T record) {
        return this.baseMapper.insertSelective(record);
    }

    public int deleteByPrimaryKey(Object key) {
        return this.baseMapper.deleteByPrimaryKey(key);
    }

    public int deleteByIds(List<Integer> ids) {
        if (CollUtil.isEmpty(ids)) {
            return 0;
        }
        Weekend<T> weekend = Weekend.of(entityClass, true, true);
        weekend.createCriteria().andIn("id", ids);
        return this.baseMapper.deleteByExample(weekend);
    }

    public int updateByPrimaryKeySelective(T record) {
        return this.baseMapper.updateByPrimaryKeySelective(record);
    }

    public T selectByPrimaryKey(Object key) {
        return this.baseMapper.selectByPrimaryKey(key);
    }

    public List<T> selectByIds(List<Integer> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        Weekend<T> weekend = Weekend.of(entityClass, true, true);
        weekend.createCriteria().andIn("id", ids);
        return this.baseMapper.selectByExample(weekend);
    }

    public List<T> selectAll() {
        return this.baseMapper.selectAll();
    }

    public int batchInsert(List<T> recordList) {
        return this.baseMapper.insertList(recordList);
    }

    public int batchUpdate(List<T> recordList) {
        return this.baseMapper.batchUpdateSelective(recordList);
    }

    // =============================CRUD END===================================

}
