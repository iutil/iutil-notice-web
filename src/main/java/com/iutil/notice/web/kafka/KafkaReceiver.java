package com.iutil.notice.web.kafka;

import com.iutil.notice.web.config.InjectionConfig;
import com.iutil.notice.web.websocket.IMsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * kafka接收者
 * @author Erwin Feng
 * @since 2019-05-11 08:59
 */
@Service
@Slf4j
public class KafkaReceiver {

    @Autowired
    private InjectionConfig injectionConfig;

    @Autowired
    private IMsgService iMsgService;

    @KafkaListener(topics = injectionConfig.KAFKA_TOPIC_NOTICE_WEB)
    public void apiLog(ConsumerRecord<?, ?> record) {
        try {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                // receiver kafka msg
                Object o = kafkaMessage.get();
                // send to web by WebSocket
                iMsgService.sendMsgToAllUser(o.toString());
            }
        } catch (Exception e) {
            log.error("receiver kafka msg exception : {}", e.getMessage());
            e.printStackTrace();
        }
    }

}
