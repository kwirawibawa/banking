package com.assessment.banking.service;

import com.assessment.banking.request.AccountRequest;
import com.assessment.banking.response.AccountResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AccountService {
    @Transactional
    AccountResponse createAccount(AccountRequest request);

    @Transactional(readOnly = true)
    AccountResponse getAccount(UUID id);

    @Transactional
    AccountResponse updateAccount(UUID accountId, AccountRequest request);

    @Transactional
    void deactivateAccount(UUID accountId);
}
