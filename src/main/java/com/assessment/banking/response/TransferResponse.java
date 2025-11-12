package com.assessment.banking.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {

    private String fromAccountId;
    private String fromAccountNumber;
    private String fromAccountName;

    private String toAccountId;
    private String toAccountNumber;
    private String toAccountName;

    private BigDecimal amount;
    private LocalDateTime createdAt;
}
