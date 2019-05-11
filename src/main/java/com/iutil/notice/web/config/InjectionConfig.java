package com.iutil.notice.web.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 注入配置
 * @author Erwin Feng
 * @since 2019-05-11 09:01
 */
@Component
@Getter
public class InjectionConfig {

    @Value("${iutil.kafka.topic}")
    private String KAFKA_TOPIC_NOTICE_WEB;

}
