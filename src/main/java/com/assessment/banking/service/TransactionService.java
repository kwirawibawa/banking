package com.assessment.banking.service;

import com.assessment.banking.request.TransactionRequest;
import com.assessment.banking.request.TransferRequest;
import com.assessment.banking.response.TransactionResponse;
import com.assessment.banking.response.TransferResponse;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    @Transactional
    TransactionResponse deposit(UUID accountId, BigDecimal amount);

    @Transactional
    TransactionResponse withdraw(UUID accountId, BigDecimal amount);

    @Transactional
    TransferResponse transfer(TransferRequest request);

    @Transactional(readOnly = true)
    List<TransactionResponse> getTransactionsByAccount(UUID accountId);
}
