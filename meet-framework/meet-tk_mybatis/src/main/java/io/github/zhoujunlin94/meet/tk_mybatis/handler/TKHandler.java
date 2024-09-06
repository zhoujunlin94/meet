package io.github.zhoujunlin94.meet.tk_mybatis.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.TypeUtil;
import com.google.common.collect.Lists;
import io.github.zhoujunlin94.meet.tk_mybatis.mapper.TKMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.List;

/**
 * @author zhoujunlin
 * @date 2023年07月09日 17:02
 * @desc
 */
public class TKHandler<M extends TKMapper<T>, T> {

    @Autowired
    protected M baseMapper;

    private final Class<T> entityClass = (Class<T>) TypeUtil.getClass(TypeUtil.getTypeArgument(this.getClass(), 1));

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

    public int deleteByIds(Collection<Integer> ids) {
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

    public List<T> selectByIds(Collection<Integer> ids) {
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
        return CollUtil.isNotEmpty(recordList) ? this.baseMapper.insertList(recordList) : 0;
    }

    public int batchInsertSelective(List<T> recordList) {
        return CollUtil.isNotEmpty(recordList) ? this.baseMapper.batchInsertSelective(recordList) : 0;
    }

    public int insertOrUpdate(T record) {
        return this.baseMapper.insertOrUpdate(record);
    }

    public int batchUpdateSelective(List<T> recordList) {
        return CollUtil.isNotEmpty(recordList) ? this.baseMapper.batchUpdateSelective(recordList) : 0;
    }

    // =============================CRUD END===================================

}
