package top.alexmmd.common.file.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.file.annos.EnableFile;

/**
 * @author wangyonghui
 * @since 2023年08月01日 10:44:00
 */
@Configuration
@ConditionalOnBean(annotation = EnableFile.class)
public class FileAutoConfiguration {

}
