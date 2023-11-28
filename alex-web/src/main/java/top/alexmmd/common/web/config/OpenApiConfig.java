package top.alexmmd.common.web.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyonghui
 * @since 2023年11月28日 10:39:00
 */
@Configuration
@AutoConfigureBefore(SpringDocConfiguration.class)
@EnableConfigurationProperties(OpenApiConfigProperties.class)
@ConditionalOnProperty(prefix = "openapi", name = "enable", havingValue = "true")
public class OpenApiConfig {

    @Resource
    private OpenApiConfigProperties openApiConfigProperties;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(openApiConfigProperties.getTitle())
                                .description(openApiConfigProperties.getDescription())
                                .contact(new Contact()
                                        .name(openApiConfigProperties.getContactName())
                                        .email(openApiConfigProperties.getContactEmail())
                                        .url(openApiConfigProperties.getContactUrl())
                                )
                                .license(new License().name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                                .version(openApiConfigProperties.getVersion())
                )
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Full Documentation")
                        .url("https://springdoc.org/")
                );
    }
}

