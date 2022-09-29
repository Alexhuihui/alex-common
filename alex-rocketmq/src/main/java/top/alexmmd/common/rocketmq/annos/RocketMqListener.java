package top.alexmmd.common.rocketmq.annos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 队列消费者
 *
 * @author wangyonghui
 * @date 2022年09月29日 11:09:00
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMqListener {

    /**
     * 可消费队列topic
     *
     * @return topic
     */
    String topic();

    /**
     * 可消费队列consumerGroup
     *
     * @return consumerGroup
     */
    String consumerGroup();
}
