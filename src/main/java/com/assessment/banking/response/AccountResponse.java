package com.assessment.banking.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private UUID id;
    private String name;
    private String email;
    private String accountNumber;
    private BigDecimal balance;
    private boolean isActive;
}
