package com.assessment.banking.service;

import com.assessment.banking.entity.Account;
import com.assessment.banking.entity.Transaction;
import com.assessment.banking.entity.TransactionType;
import com.assessment.banking.entity.TransferDirection;
import com.assessment.banking.exception.FailedTransactionException;
import com.assessment.banking.repository.AccountRepository;
import com.assessment.banking.repository.TransactionRepository;
import com.assessment.banking.request.TransferRequest;
import com.assessment.banking.response.TransactionResponse;
import com.assessment.banking.response.TransferResponse;
import com.assessment.banking.util.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public TransactionResponse deposit(UUID accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new FailedTransactionException(CommonConstants.ACCOUNT_NOT_FOUND));

        account.setBalance(account.getBalance().add(amount));

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return buildTransactionResponse(transaction);
    }

    @Transactional
    @Override
    public TransactionResponse withdraw(UUID accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new FailedTransactionException(CommonConstants.ACCOUNT_NOT_FOUND));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new FailedTransactionException(CommonConstants.INSUFFICIENT_BALANCE);
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return buildTransactionResponse(transaction);
    }

    @Transactional
    @Override
    public TransferResponse transfer(TransferRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new FailedTransactionException(CommonConstants.FAILED_TRANSFER_SENDER);
        }

        Account from = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new FailedTransactionException(CommonConstants.SENDER_ACCOUNT_NOT_FOUND));
        Account to = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new FailedTransactionException(CommonConstants.RECEIVER_ACCOUNT_NOT_FOUND));

        if (from.getBalance().compareTo(request.getAmount()) < 0) {
            throw new FailedTransactionException(CommonConstants.INSUFFICIENT_BALANCE_SENDER);
        }

        from.setBalance(from.getBalance().subtract(request.getAmount()));
        Transaction withdraw = Transaction.builder()
                .account(from)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .direction(TransferDirection.OUTGOING)
                .createdAt(LocalDateTime.now())
                .build();

        to.setBalance(to.getBalance().add(request.getAmount()));
        Transaction deposit = Transaction.builder()
                .account(to)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .direction(TransferDirection.INCOMING)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(withdraw);
        transactionRepository.save(deposit);
        accountRepository.save(from);
        accountRepository.save(to);

        return TransferResponse.builder()
                .fromAccountId(from.getId().toString())
                .fromAccountName(from.getName())
                .fromAccountNumber(from.getAccountNumber())
                .toAccountId(to.getId().toString())
                .toAccountName(to.getName())
                .toAccountNumber(to.getAccountNumber())
                .amount(request.getAmount())
                .createdAt(withdraw.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getTransactionsByAccount(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountNative(accountId);

        return transactions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TransactionResponse buildTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .accountId(t.getAccount().getId())
                .amount(t.getAmount())
                .type(String.valueOf(t.getType()))
                .createdAt(t.getCreatedAt())
                .direction(t.getDirection())
                .build();
    }
}
