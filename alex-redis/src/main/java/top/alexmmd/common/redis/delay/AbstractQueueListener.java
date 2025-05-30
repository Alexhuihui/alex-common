package top.alexmmd.common.redis.delay;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import top.alexmmd.common.redis.api.IQueueListener;
import top.alexmmd.common.base.utils.ReflectUtil;

/**
 * 延迟队列消费抽象类
 *
 * @author 汪永晖
 * @since 2021/12/22 14:30
 */
@Slf4j
@SuppressWarnings("ALL")
public abstract class AbstractQueueListener<T> implements IQueueListener {

    @Override
    public void consumer(String body) {
        //获取泛型
        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();

        //jsonStr to real type
        Class<T> clazz = (Class<T>) types[0];
        T message = ReflectUtil.cast(body, clazz);
        handle(message);
    }

    protected abstract void handle(T body);
}
