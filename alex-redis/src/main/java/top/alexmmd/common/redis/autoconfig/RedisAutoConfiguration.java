package top.alexmmd.common.redis.autoconfig;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.redis.aspect.RedisLockAspect;
import top.alexmmd.common.redis.delay.DelayClient;

/**
 * @author 汪永晖
 * @since 2021/12/21 13:35
 */
@SuppressWarnings("ALL")
@Configuration
@ConditionalOnBean(annotation = EnableRedis.class)
@EnableConfigurationProperties(RedisConfigProperties.class)
public class RedisAutoConfiguration {

    @Resource
    private RedisConfigProperties properties;

    /**
     * redisson客户端
     */
    @Bean
    public RedissonClient redissonClient() {
        return getRedissonClient();
    }

    @Bean
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new RedisLockAspect(redissonClient);
    }

    @Bean
    public DelayClient redisDelayClient(RedissonClient redissonClient) {
        return new DelayClient();
    }

    private RedissonClient getRedissonClient() {
        Config config = new Config();
        SingleServerConfig server = config.useSingleServer();
        String address = StrUtil.format("redis://{}:{}", properties.getHost(),
                properties.getPort());
        server.setAddress(address);
        server.setDatabase(properties.getDatabase());
        if (StrUtil.isNotEmpty(properties.getPassword())) {
            server.setPassword(properties.getPassword());
        }

        return Redisson.create(config);
    }
}
