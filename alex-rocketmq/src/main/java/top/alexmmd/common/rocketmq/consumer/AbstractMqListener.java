package top.alexmmd.common.rocketmq.consumer;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import top.alexmmd.common.rocketmq.annos.RocketMqListener;
import top.alexmmd.common.rocketmq.queue.AbstractQueueListener;

/**
 * mq消费抽象类
 *
 * @author wangyonghui
 * @since 2022年09月29日 11:21:00
 */
@Slf4j
public abstract class AbstractMqListener<T> extends AbstractQueueListener<T> implements
        MessageListener {

    @Resource
    private RocketMQProperties properties;

    @PostConstruct
    @SuppressWarnings({"AliDeprecation", "deprecation"})
    public void init() throws MQClientException {
        RocketMqListener annotation = this.getClass().getAnnotation(RocketMqListener.class);

        String topic = this.getByProperties(annotation.topic());
        String consumerGroup = this.getByProperties(annotation.consumerGroup());
        Assert.notNull(topic, "rocketmq配置异常,topic不能为空!");
        Assert.notNull(consumerGroup, "rocketmq配置异常,consumerGroup不能为空!");

        DefaultMQPushConsumer defaultMqPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        String nameServer = properties.getNameServer();
        Assert.notNull(nameServer, "rocketmq配置异常,nameSever不能为空!");
        defaultMqPushConsumer.setNamesrvAddr(nameServer);
        defaultMqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMqPushConsumer.subscribe(topic, "*");

        //是否顺序消息,顺序消息单线程消费
        defaultMqPushConsumer.registerMessageListener(this);
        defaultMqPushConsumer.start();
    }

    private String getByProperties(String property) {
        String key = null;
        if (StrUtil.startWith(property, "${")) {
            key = StrUtil.subBetween(property, "${", "}");
        }
        return key;
    }
}
