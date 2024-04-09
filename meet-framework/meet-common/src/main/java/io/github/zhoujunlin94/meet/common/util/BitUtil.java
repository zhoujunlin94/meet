package io.github.zhoujunlin94.meet.common.util;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoujunlin
 * @date 2023年12月18日 19:14
 * @desc
 */
public final class BitUtil {

    /**
     * 对多个二进制数求和
     * 1 | 8 | 256 = 265
     */
    public static Integer sum(List<Integer> bitNums) {
        Integer ret = 0;
        if (CollUtil.isEmpty(bitNums)) {
            return ret;
        }
        for (Integer bitNum : bitNums) {
            ret |= bitNum;
        }
        return ret;
    }

    /**
     * 将一个数转换为二进制数的组合
     * 265 = 1,8,256
     */
    public static List<Integer> getBitNums(Integer number) {
        List<Integer> ret = new ArrayList<>();
        while (number > 0) {
            int highestOneBit = Integer.highestOneBit(number);
            ret.add(highestOneBit);
            number -= highestOneBit;
        }
        return ret;
    }

    /**
     * 判断一个数中是否存在某个二进制数
     * 256 & 8 = true
     */
    public static boolean containBitNum(Integer number, Integer bitNum) {
        return (number & bitNum) == bitNum;
    }

}
