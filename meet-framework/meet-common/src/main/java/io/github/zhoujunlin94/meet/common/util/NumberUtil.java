package io.github.zhoujunlin94.meet.common.util;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author zhoujunlin
 * @date 2024/10/22 21:35
 */
public final class NumberUtil {

    public static String stripTrailingZeros(BigDecimal value) {
        return Optional.ofNullable(value).map(BigDecimal::stripTrailingZeros).map(BigDecimal::toPlainString)
                .orElse("0");
    }

}
