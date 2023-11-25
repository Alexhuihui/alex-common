package top.alexmmd.common.rocketmq.consumer;

import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.RECONSUME_LATER;

import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author wangyonghui
 * @since 2022年09月29日 13:12:00
 */
@Slf4j
public abstract class AbstractRocketMqConcurrentlyListener<T> extends
        AbstractMqListener<T> implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages,
            ConsumeConcurrentlyContext context) {
        try {
            super.consumer(new String(messages.get(0).getBody(), StandardCharsets.UTF_8));
        } catch (RuntimeException runtime) {
            log.error("RocketMq异步消费失败,{},", this.getClass().getSimpleName(), runtime);
            return CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("RocketMq异步消费失败,{},", this.getClass().getSimpleName(), e);
            return RECONSUME_LATER;
        }
        return CONSUME_SUCCESS;
    }

}
