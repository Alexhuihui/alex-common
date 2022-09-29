package top.alexmmd.common.redis.delay;

import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONUtil;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;

/**
 * @author 汪永晖
 * @date 2021/12/21 15:02
 */
@SuppressWarnings("ALL")
@Slf4j
public class DelayQueue extends AbstractQueue {

    private RDelayedQueue<Pair<String, String>> delayedQueue;
    private RBlockingDeque<Pair<String, String>> blockingDeque;

    public DelayQueue(String name, RDelayedQueue<Pair<String, String>> delayedQueue,
                      RBlockingDeque<Pair<String, String>> blockingDeque) {
        super(name);
        this.blockingDeque = blockingDeque;
        this.delayedQueue = delayedQueue;
    }

    public static DelayQueue create(String name) {
        RBlockingDeque<Pair<String, String>> blockingDeque = redissonClient.getBlockingDeque(name);
        RDelayedQueue<Pair<String, String>> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        return new DelayQueue(name, delayedQueue, blockingDeque);
    }

    public <T> void offer(String topic, T body, long delay) {
        delayedQueue.offer(Pair.of(topic, JSONUtil.toJsonStr(body)), delay, TimeUnit.SECONDS);
    }

    public <T> void remove(String topic, T body) {
        delayedQueue.remove(Pair.of(topic, JSONUtil.toJsonStr(body)));
    }

    @Override
    protected Pair<String, String> take() throws InterruptedException {
        return blockingDeque.take();
    }
}
