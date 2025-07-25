package io.github.zhoujunlin94.meet.common.timetask;

import io.github.zhoujunlin94.meet.common.util.UrlUtil;

/**
 * @author zhoujunlin
 * @date 2025-07-25 13:46
 */
public class TestUrlUtil {

    public static void main(String[] args) {
        System.out.println(UrlUtil.replaceUrlQuery("http://www.baidu.com?word=zhou", "word", "junlin"));
    }

}
