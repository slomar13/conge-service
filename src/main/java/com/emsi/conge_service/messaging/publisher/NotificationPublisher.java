package com.emsi.conge_service.messaging.publisher;

import com.emsi.conge_service.messaging.dto.NotificationEvent;
import com.emsi.conge_service.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final JmsTemplate jmsTemplate;

    public void publish(NotificationEvent event) {
        jmsTemplate.convertAndSend(
                JmsConfig.TOPIC,
                event
        );
    }
}
