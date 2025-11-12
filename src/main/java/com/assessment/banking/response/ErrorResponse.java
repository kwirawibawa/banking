package com.assessment.banking.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int code;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}