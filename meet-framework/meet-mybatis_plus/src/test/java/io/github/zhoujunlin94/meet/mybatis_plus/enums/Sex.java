package io.github.zhoujunlin94.meet.mybatis_plus.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhoujunlin
 * @date 2025年11月24日 10:12
 */
@Getter
@RequiredArgsConstructor
public enum Sex {
    /**
     * 性别
     */
    MALE("男"),
    FEMALE("女");

    private final String description;

}
