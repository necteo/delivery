package com.spring.delivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SocketMessageForm {
    private boolean state;
    private String message;
    private Long userId;
    private String userEmail;

    public SocketMessageForm(boolean b) {
    }
}