package top.alexmmd.common.rocketmq.producer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;


/**
 * mq生产者
 *
 * @author wangyonghui
 * @date 2022年09月29日 13:14:00
 */
@Slf4j
public class MqProducer {

    private static final RocketMQTemplate ROCKET_MQ_TEMPLATE = SpringUtil.getBean(
            RocketMQTemplate.class);

    public static <T> void sendMessage(String topic, T body) {
        send(topic, null, body);
    }

    public static <T> void orderlyMessage(String topic, T body, String hashKey) {
        send(topic, hashKey, body);
    }

    /**
     * 发送mq消息
     *
     * @param topic   topic
     * @param hashKey 用于顺序队列的hashKey
     * @param body    消息体
     * @param <T>     T
     */
    private static <T> void send(String topic, String hashKey, T body) {

        try {
            //转换消息
            String message = JSONUtil.toJsonStr(body);
            //发送mq
            if (StrUtil.isEmpty(hashKey)) {
                ROCKET_MQ_TEMPLATE.convertAndSend(topic, message);
            } else {
                ROCKET_MQ_TEMPLATE.syncSendOrderly(topic, message, hashKey);
            }
        } catch (Exception e) {
            log.error("消息发送失败,{},{},", topic, JSONUtil.toJsonStr(body), e);
        }
    }
}
