package io.github.zhoujunlin94.meet.tk_mybatis.util;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author zhoujunlin
 * @date 2024年03月08日 14:06
 * @desc
 */
public final class PageUtil {

    public static <S, T> PageInfo<T> copy(PageInfo<S> sourcePageInfo, Supplier<List<T>> supplier) {
        return copy(sourcePageInfo, supplier.get());
    }

    public static <S, T> PageInfo<T> copy(PageInfo<S> sourcePageInfo, List<T> targetList) {
        PageInfo<T> retPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(sourcePageInfo, retPageInfo);
        retPageInfo.setList(targetList);
        return retPageInfo;
    }

}
