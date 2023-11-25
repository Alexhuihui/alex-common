package top.alexmmd.common.rocketmq.config;

import cn.hutool.core.util.StrUtil;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.alexmmd.common.rocketmq.annos.EnableRocketMq;

/**
 * rocketmq自动配置类
 *
 * @author wangyonghui
 * @since 2022年09月29日 11:10:00
 */
@Configuration
@ConditionalOnBean(annotation = EnableRocketMq.class)
public class RocketMqAutoConfiguration {

    @Bean
    public DefaultMQProducer defaultMqProducer(RocketMQProperties rocketMqProperties) {
        RocketMQProperties.Producer producerConfig = rocketMqProperties.getProducer();
        String nameServer = rocketMqProperties.getNameServer();
        String groupName = producerConfig.getGroup();

        String accessChannel = rocketMqProperties.getAccessChannel();

        String ak = rocketMqProperties.getProducer().getAccessKey();
        String sk = rocketMqProperties.getProducer().getSecretKey();
        boolean isEnableMsgTrace = rocketMqProperties.getProducer().isEnableMsgTrace();
        String customizedTraceTopic = rocketMqProperties.getProducer().getCustomizedTraceTopic();

        DefaultMQProducer producer = RocketMQUtil.createDefaultMQProducer(groupName, ak, sk, isEnableMsgTrace, customizedTraceTopic);

        producer.setNamesrvAddr(nameServer);
        if (!StrUtil.isEmpty(accessChannel)) {
            producer.setAccessChannel(AccessChannel.valueOf(accessChannel));
        }
        producer.setSendMsgTimeout(producerConfig.getSendMessageTimeout());
        producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMessageBodyThreshold());
        producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.isRetryNextServer());

        return producer;
    }
}
