package io.github.zhoujunlin94.meet.mybatis_plus.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.google.common.collect.Lists;
import io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.MeetMapper;
import org.apache.ibatis.session.ResultHandler;

import java.util.Collection;
import java.util.List;

/**
 * @author zhoujunlin
 * @date 2025年08月11日 15:13
 */
public class BaseRepository<M extends MeetMapper<T>, T> extends CrudRepository<M, T> {

    protected LambdaQueryWrapper<T> lambdaQueryWrapper() {
        return Wrappers.lambdaQuery(getEntityClass());
    }

    protected LambdaUpdateWrapper<T> lambdaUpdateWrapper() {
        return Wrappers.lambdaUpdate(getEntityClass());
    }

    protected void excludeSelect(LambdaQueryWrapper<T> lambdaQueryWrapper, Collection<String> properties) {
        if (CollUtil.isEmpty(properties)) {
            return;
        }
        lambdaQueryWrapper.select(getEntityClass(), table -> !properties.contains(table.getProperty()));
    }

    public List<T> selectByIds(Collection<Integer> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return listByIds(ids);
    }

    public void selectStream(Wrapper<T> queryWrapper, ResultHandler<T> resultHandler) {
        baseMapper.selectList(queryWrapper, resultHandler);
    }


    public int insertIgnore(T entity) {
        return baseMapper.insertIgnore(entity);
    }

    public void insertOnDuplicateKeyUpdate(T entity) {
        baseMapper.insertOnDuplicateKeyUpdate(entity);
    }

}
