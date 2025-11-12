package com.assessment.banking.service;

import com.assessment.banking.exception.FailedTransactionException;
import com.assessment.banking.request.AccountRequest;
import com.assessment.banking.response.AccountResponse;
import com.assessment.banking.entity.Account;
import com.assessment.banking.repository.AccountRepository;
import com.assessment.banking.util.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public AccountResponse createAccount(AccountRequest request) {
        accountRepository.findActiveByEmail(request.getEmail())
                .ifPresent(acc -> { throw new IllegalArgumentException(CommonConstants.EMAIL_EXISTS); });

        accountRepository.findActiveByName(request.getName())
                .ifPresent(acc -> { throw new IllegalArgumentException(CommonConstants.NAME_EXISTS); });

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .isActive(true)
                .build();

        account = accountRepository.save(account);

        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .isActive(account.getIsActive())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public AccountResponse getAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(CommonConstants.ACCOUNT_NOT_FOUND));

        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .isActive(account.getIsActive())
                .build();
    }

    @Transactional
    @Override
    public AccountResponse updateAccount(UUID accountId, AccountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(CommonConstants.ACCOUNT_NOT_FOUND));

        accountRepository.findActiveByEmail(request.getEmail())
                .filter(acc -> !acc.getId().equals(accountId))
                .ifPresent(acc -> { throw new IllegalArgumentException(CommonConstants.EMAIL_EXISTS); });

        accountRepository.findActiveByName(request.getName())
                .filter(acc -> !acc.getId().equals(accountId))
                .ifPresent(acc -> { throw new IllegalArgumentException(CommonConstants.NAME_EXISTS); });

        int updated = accountRepository.updateAccountNative(accountId, request.getName(), request.getEmail());
        if (updated == 0) {
            throw new FailedTransactionException(CommonConstants.UPDATE_ACCOUNT_FAILED);
        }

        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .isActive(account.getIsActive())
                .build();
    }

    @Transactional
    @Override
    public void deactivateAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(CommonConstants.ACCOUNT_NOT_FOUND));

        int updated = accountRepository.softDeleteAccountNative(accountId);
        if (updated == 0) {
            throw new FailedTransactionException(CommonConstants.DEACTIVATE_ACCOUNT_FAILED + ": " + account.getName());
        }
    }

    private String generateAccountNumber() {
        long number = ThreadLocalRandom.current().nextLong(0, 10_000_000_000L);
        return String.format("%010d", number);
    }
}

