package top.alexmmd.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangyonghui
 * @since 2023年11月28日 08:59:00
 */
@ConfigurationProperties(prefix = "openapi")
@Data
public class OpenApiConfigProperties {

    private Boolean enable;
    private String version;
    private String title;
    private String description;
    private String contactName;
    private String contactEmail;
    private String contactUrl;
}
