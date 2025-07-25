package io.github.zhoujunlin94.meet.common.timetask;

import io.github.zhoujunlin94.meet.common.util.Base62Generator;

/**
 * @author zhoujunlin
 * @date 2025-07-25 13:37
 */
public class TestBase62Generator {

    public static void main(String[] args) {
        String str = Base62Generator.encodeBase62(600, 6);
        // 00009G
        System.out.println(str);
        // 600
        System.out.println(Base62Generator.decodeBase62(str));
    }

}
