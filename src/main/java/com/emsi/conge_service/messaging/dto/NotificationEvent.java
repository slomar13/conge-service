package com.emsi.conge_service.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    private String eventType;   // CONGE_CREATED, CONGE_APPROVED, CONGE_REJECTED
    private String email;
    private String subject;
    private String message;
}

