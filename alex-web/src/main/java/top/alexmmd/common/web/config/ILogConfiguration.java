package top.alexmmd.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = ILogConfiguration.ILogPREFIX)
public class ILogConfiguration {

    static final String ILogPREFIX = "log";

    private String[] ignoreMethods;

    private Boolean enable = Boolean.TRUE;

}
