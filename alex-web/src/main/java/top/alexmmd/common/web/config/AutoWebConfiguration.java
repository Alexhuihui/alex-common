package top.alexmmd.common.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.web.exception.GlobalExceptionHandler;

/**
 * @author wangyonghui
 * @since 2022年12月28日 15:32:00
 */
@Configuration
public class AutoWebConfiguration {

    @Bean
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
