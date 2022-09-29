package top.alexmmd.common.rocketmq.consumer;

import static org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;

import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 顺序消费抽象类
 *
 * @author wangyonghui
 * @date 2022年09月29日 13:13:00
 */
@Slf4j
public abstract class AbstractRocketMqOrderlyListener<T> extends
        AbstractMqListener<T> implements MessageListenerOrderly {

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages,
            ConsumeOrderlyContext context) {
        try {
            super.consumer(new String(messages.get(0).getBody(), StandardCharsets.UTF_8));
        } catch (RuntimeException runtime) {
            log.error("RocketMq异步消费失败,{},", this.getClass().getSimpleName(), runtime);
            return ConsumeOrderlyStatus.SUCCESS;
        } catch (Exception e) {
            log.error("RocketMq异步消费失败,{},", this.getClass().getSimpleName(), e);
            return SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

}

