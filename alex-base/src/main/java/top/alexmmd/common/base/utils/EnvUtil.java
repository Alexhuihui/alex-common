package top.alexmmd.common.base.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author wangyonghui
 * @since 2023年11月25日 11:57:00
 */
public class EnvUtil {

    private static String env;

    public EnvUtil(String env) {
        EnvUtil.env = env;
    }

    public static String getEnvKafka(String topic) {
        return StrUtil.isNotEmpty(env) ? env + "_" + topic : topic;
    }

    public static String getEnv(String value) {
        return env;
    }

    public static String getEnvRedis(String key) {
        return StrUtil.isNotEmpty(env) ? env + ":" + key : key;
    }

    public static String getEnvEsIndex(String index) {
        return StrUtil.isNotEmpty(env) ? env + "-" + index : index;
    }

}
