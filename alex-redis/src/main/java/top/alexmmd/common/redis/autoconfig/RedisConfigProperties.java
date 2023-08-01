package top.alexmmd.common.redis.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置
 *
 * @author alex
 */
@Data
@ConfigurationProperties(prefix = "redis")
public class RedisConfigProperties {

    private String username;

    private String password;

    private String host = "localhost";

    private int port = 6379;

    private int database = 0;

    private String delayQueueName;

}
