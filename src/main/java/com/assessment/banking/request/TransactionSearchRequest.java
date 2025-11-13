package com.assessment.banking.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionSearchRequest {
    @NotNull(message = "Account ID is required")
    private UUID accountId;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int pageNumber = 1;
    private int pageSize = 10;
}
