package top.alexmmd.common.rocketmq.queue;

/**
 * 队列监听
 *
 * @author wangyonghui
 * @date 2022年09月29日 11:24:00
 */
public interface IQueueListener {

    /**
     * 消费指定队列中的消息
     *
     * @param body 消息
     */
    void consumer(String body);
}
