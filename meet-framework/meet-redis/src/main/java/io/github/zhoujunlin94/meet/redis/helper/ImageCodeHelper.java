package io.github.zhoujunlin94.meet.redis.helper;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.redis.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujl
 * @date 2021/5/7 9:52
 * @desc
 */
@Slf4j
public class ImageCodeHelper {

    private static final int WIDTH = 130;
    private static final int HEIGHT = 38;
    private static final int CODE_COUNT = 4;
    private static final int CIRCLE_COUNT = 8;

    public static String imgBase64Code(String redisKey) {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(WIDTH, HEIGHT, CODE_COUNT, CIRCLE_COUNT);
        // 验证码
        String verifyCode = captcha.getCode();
        log.info("获取验证码:{},redisKey:{}", verifyCode, redisKey);
        // 将验证码放入redis(先删除，后保存)
        RedisHelper.delete(redisKey);
        RedisHelper.setStr(redisKey, verifyCode, 600L, TimeUnit.SECONDS);
        return RedisConstant.IMG_BASE64_PREFIX + captcha.getImageBase64();
    }

    public static boolean checkValidCode(String redisKey, String validCode) {
        String codeInRedis = RedisHelper.getStr(redisKey);
        return StrUtil.equalsIgnoreCase(validCode, codeInRedis);
    }

}
