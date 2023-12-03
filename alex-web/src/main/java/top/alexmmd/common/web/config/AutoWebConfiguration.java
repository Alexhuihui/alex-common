package top.alexmmd.common.web.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.web.exception.GlobalExceptionHandler;

/**
 * @author wangyonghui
 * @since 2022年12月28日 15:32:00
 */
@Configuration
@ImportAutoConfiguration({OpenApiConfig.class, JacksonConfig.class,
        CommonBeanConfiguration.class})
@EnableConfigurationProperties(ILogConfiguration.class)
public class AutoWebConfiguration {

    @Bean
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "log", name = "enable", havingValue = "true")
    public ILogPrintAspect getLogPrintAspect(ILogConfiguration config) {
        return new ILogPrintAspect(config);
    }

}
