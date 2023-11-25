package top.alexmmd.common.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangyonghui
 * @since 2023年11月25日 11:54:00
 */
@Configuration
@ConditionalOnProperty(prefix = "swagger", name = "enabled", havingValue = "true")
public class SwaggerConfiguration implements WebMvcConfigurer {


    @Value("${swagger.enabled:true}")
    private Boolean enabled;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

}
