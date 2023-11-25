package top.alexmmd.common.redis.delay;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import top.alexmmd.common.redis.api.IQueueListener;
import top.alexmmd.common.redis.api.Queue;
import top.alexmmd.common.redis.api.QueueConsumer;

/**
 * @author 汪永晖
 * @since 2021/12/21 14:07
 */
@Slf4j
@SuppressWarnings("ALL")
public abstract class AbstractQueue implements Queue {

    public static final String REDISSON_IS_SHUTDOWN = "Redisson is shutdown";
    protected String name;
    protected static Map<String, IQueueListener> listenerMap;
    protected static RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
    protected static String LOCK_PREFIX = "alex:queue:listeners";

    @SuppressWarnings("WeakerAccess")
    protected static ExecutorService consumerThreadPool;

    static {
        consumerThreadPool = Executors.newFixedThreadPool(10);
        // put topic and corresponding queueListener into the listenerMap
        listenerMap = new HashMap<>();
        QueueConsumer annotation;
        Map<String, IQueueListener> beansOfType = SpringUtil.getBeansOfType(IQueueListener.class);
        if (MapUtil.isNotEmpty(beansOfType)) {
            for (IQueueListener listener : beansOfType.values()) {
                // bind topic and corresponding listener
                annotation = listener.getClass().getAnnotation(QueueConsumer.class);
                if (ObjectUtil.isNull(annotation)) {
                    continue;
                }
                String topic = annotation.topic();
                if (StrUtil.isNotEmpty(topic)) {
                    listenerMap.put(topic, listener);
                }
                String[] topics = annotation.topics();
                if (ArrayUtil.isEmpty(topics)) {
                    continue;
                }
                for (String s : topics) {
                    listenerMap.put(s, listener);
                }
            }
        }
    }

    public AbstractQueue(String name) {
        this.name = name;
        consumerThreadPool.execute(() -> {
            log.info("开启一个线程 {} 监听队列 : {}", Thread.currentThread().getName(), name);
            while (true) {
                try {
                    Pair<String, String> take = this.take();
                    String topic = take.getKey();
                    String body = take.getValue();
                    consumerThreadPool.execute(consumer(name, topic, body));
                } catch (Exception e) {
                    log.error("监听队列线程错误，{}", e.getMessage());
                    if (REDISSON_IS_SHUTDOWN.equals(e.getMessage())) {
                        break;
                    }
                }
            }
        });
    }

    private Runnable consumer(String name, String topic, String body) {
        return () -> {
            log.info("线程： {} 监听队列：{}， topic: {}, body: {}, 开始处理", Thread.currentThread().getName()
                    , name, topic, body);
            RLock lock = redissonClient.getLock(LOCK_PREFIX + topic);
            try {
                lock.lock();
                IQueueListener queueListener = listenerMap.get(topic);
                if (ObjectUtil.isNull(queueListener)) {
                    log.error("topic {} 没有找到对应的监听器", topic);
                    return;
                }
                queueListener.consumer(body);
            } catch (Exception e) {
                log.error("消费失败");
            } finally {
                lock.unlock();
            }
        };
    }

    protected abstract Pair<String, String> take() throws InterruptedException;
}
