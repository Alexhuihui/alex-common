package top.alexmmd.common.redis.delay;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import top.alexmmd.common.redis.autoconfig.RedisConfigProperties;

/**
 * @author 汪永晖
 * @date 2021/12/21 15:15
 */
@Slf4j
@SuppressWarnings("all")
public class DelayClient {

    private static DelayQueue delayQueue;

    static {
        String queueName = generateQueueName();
        log.info("queueName==={}", queueName);
        delayQueue = DelayQueue.create(queueName);
    }

    private static String generateQueueName() {
        String activeProfile = SpringUtil.getActiveProfile();
        RedisConfigProperties properties = SpringUtil.getBean(RedisConfigProperties.class);
        String delayQueueName = properties.getDelayQueueName();
        if (StrUtil.contains(delayQueueName, activeProfile)) {
            return delayQueueName;
        }
        String queueName = activeProfile + StrUtil.DASHED + delayQueueName;
        return queueName;
    }

    public static <T> void offer(String topic, T body, long delay) {
        delayQueue.offer(topic, body, delay);
    }

    public static <T> void offer(String topic, T body, Date date) {
        delayQueue.offer(topic, body, DateUtil.between(new Date(), date, DateUnit.SECOND));
    }

    public static <T> Boolean contains(String topic, T body) {
        return delayQueue.contains(topic, body);
    }

    public static <T> void remove(String topic, T body) {
        delayQueue.remove(topic, body);
    }
}
