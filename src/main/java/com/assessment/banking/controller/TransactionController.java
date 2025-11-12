package com.assessment.banking.controller;

import com.assessment.banking.request.TransactionRequest;
import com.assessment.banking.request.TransferRequest;
import com.assessment.banking.response.ApiResponse;
import com.assessment.banking.response.TransactionResponse;
import com.assessment.banking.response.TransferResponse;
import com.assessment.banking.service.TransactionService;
import com.assessment.banking.util.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @RequestBody @Valid TransactionRequest request) {

        TransactionResponse response = switch (request.getType().toUpperCase()) {
            case "DEPOSIT" -> transactionService.deposit(request.getAccountId(), request.getAmount());
            case "WITHDRAW" -> transactionService.withdraw(request.getAccountId(), request.getAmount());
            default -> throw new IllegalArgumentException(CommonConstants.INVALID_TYPE + ": " + request.getType());
        };

        return ResponseEntity.ok(
                ApiResponse.<TransactionResponse>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_CREATE_TRANSACTION)
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(
            @RequestBody @Valid TransferRequest request) {

        TransferResponse response = transactionService.transfer(request);

        return ResponseEntity.ok(
                ApiResponse.<TransferResponse>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_TRANSFER)
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByAccount(
            @PathVariable UUID accountId) {

        List<TransactionResponse> transactions = transactionService.getTransactionsByAccount(accountId);

        return ResponseEntity.ok(
                ApiResponse.<List<TransactionResponse>>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_GET_TRANSACTION)
                        .data(transactions)
                        .build()
        );
    }
}

