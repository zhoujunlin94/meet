package io.github.zhoujunlin94.meet.mybatis_plus.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.google.common.collect.Lists;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2025年08月11日 15:13
 */
public class BaseRepository<M extends BaseMapper<T>, T> extends CrudRepository<M, T> {

    protected LambdaQueryWrapper<T> lambdaQueryWrapper() {
        return Wrappers.lambdaQuery(getEntityClass());
    }

    protected LambdaUpdateWrapper<T> lambdaUpdateWrapper() {
        return Wrappers.lambdaUpdate(getEntityClass());
    }

    protected void excludeSelect(LambdaQueryWrapper<T> lambdaQueryWrapper, List<String> properties) {
        if (CollUtil.isEmpty(properties)) {
            return;
        }
        lambdaQueryWrapper.select(getEntityClass(), table -> !properties.contains(table.getProperty()));
    }

    public List<T> selectByIds(List<Integer> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return listByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchSave(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.saveBatch(list, DEFAULT_BATCH_SIZE);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateById(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.updateBatchById(list, DEFAULT_BATCH_SIZE);
    }

}
