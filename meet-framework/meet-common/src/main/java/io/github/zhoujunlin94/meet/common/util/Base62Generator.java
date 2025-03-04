package io.github.zhoujunlin94.meet.common.util;

/**
 * @author zhoujunlin
 * @date 2025-03-04-15:50
 */
public final class Base62Generator {

    /**
     * Base62 字符集
     */
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62_CHARS.length();

    /**
     * 将整数转换为固定长度的Base62字符串，不足补'0'
     */
    public static String encodeBase62(long num, int length) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62_CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }
        while (sb.length() < length) {
            sb.append('0');
        }
        return sb.reverse().toString();
    }

    /**
     * 62进制字符串转数字
     */
    public static long decodeBase62(String str) {
        long num = 0;
        for (char c : str.toCharArray()) {
            num = num * BASE + BASE62_CHARS.indexOf(c);
        }
        return num;
    }

    /*public static void main(String[] args) {
        long maxSixStrValue = decodeBase62("ZZZZZZ");
        System.out.println(maxSixStrValue);
        System.out.println(encodeBase62(maxSixStrValue, 6));
    }*/

}
