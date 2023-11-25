package top.alexmmd.common.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.base.utils.EnvUtil;

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

}
