package top.alexmmd.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.alexmmd.common.security.annos.EnableSecurity;

/**
 * 安全自动配置
 *
 * @author wangyonghui
 * @since 2022年10月31日 14:04:00
 */
@Configuration
@ConditionalOnBean(annotation = EnableSecurity.class)
@Import(AnnoGlobalMethodSecurity.class)
public class AlexSecurityAutoConfiguration {

}
