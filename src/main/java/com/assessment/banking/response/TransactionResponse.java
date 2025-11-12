package com.assessment.banking.response;

import com.assessment.banking.entity.TransferDirection;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private UUID id;
    private UUID accountId;
    private String type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private TransferDirection direction;
}