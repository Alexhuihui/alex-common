package top.alexmmd.common.web.config;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.base.utils.EnvUtil;
import top.alexmmd.common.base.web.support.IDGenerator;
import top.alexmmd.common.base.web.support.ISecurityContext;

/**
 * @author wangyonghui
 * @since 2023年11月25日 11:56:00
 */
@Configuration
public class CommonBeanConfiguration {

    @Value("${spring.profiles.active:default}")
    private String env;

    @Bean
    public EnvUtil getEnvUtil() {
        return new EnvUtil(env);
    }

    @Bean
    @ConditionalOnMissingBean(IDGenerator.class)
    public IDGenerator idGenerator() {
        return new IDGenerator() {
            @Override
            public Long longId() {
                return IdUtil.getSnowflake(1, 1).nextId();
            }

            @Override
            public String longStringId() {
                return IdUtil.getSnowflake(1, 1).nextIdStr();
            }

            @Override
            public String stringId() {
                return IdUtil.simpleUUID();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ISecurityContext.class)
    public ISecurityContext securityContext() {
        return new ISecurityContext() {
            @Override
            public String getName() {
                return "DefaultUser";
            }

            @Override
            public Long getId() {
                return IdUtil.getSnowflake(1, 1).nextId();
            }
        };
    }

}
