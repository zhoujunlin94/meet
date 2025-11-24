package io.github.zhoujunlin94.meet.tk_mybatis.p6spy;

import cn.hutool.core.util.StrUtil;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoujunlin
 * @date 2025年11月24日 14:07
 * @desc
 */
@Slf4j
public class P6SpyLogger implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (StrUtil.isNotBlank(sql)) {
            return String.format("SQL耗时: %d ms | 执行语句: %s", elapsed, P6Util.singleLine(sql));
        }
        return StrUtil.EMPTY;
    }

}
