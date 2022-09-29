package top.alexmmd.common.rocketmq.queue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import top.alexmmd.common.base.utils.ReflectUtil;

/**
 * @author wangyonghui
 * @date 2022年09月29日 11:23:00
 */
@Slf4j
@SuppressWarnings("ALL")
public abstract class AbstractQueueListener<T> implements IQueueListener {

    @Override
    public void consumer(String body) {
        try {
            //获取泛型
            Type[] types = ((ParameterizedType) this.getClass()
                    .getGenericSuperclass()).getActualTypeArguments();
            //jsonStr to real type
            Class<T> clazz = (Class<T>) types[0];
            T message = ReflectUtil.cast(body, clazz);
            handle(message);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @return 返回值
     */
    protected abstract void handle(T message);
}

