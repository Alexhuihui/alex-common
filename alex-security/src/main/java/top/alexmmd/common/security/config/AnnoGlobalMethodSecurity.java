package top.alexmmd.common.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * 开启基于注解的接口访问控制
 *
 * @author wangyonghui
 * @since 2022年10月31日 13:43:00
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AnnoGlobalMethodSecurity {

}
