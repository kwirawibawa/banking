package com.assessment.banking.controller;

import com.assessment.banking.request.AccountRequest;
import com.assessment.banking.response.AccountResponse;
import com.assessment.banking.response.ApiResponse;
import com.assessment.banking.service.AccountService;
import com.assessment.banking.util.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@RequestBody @Valid AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);

        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_CREATE_ACCOUNT)
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable UUID id) {
        AccountResponse response = accountService.getAccount(id);

        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_GET_ACCOUNT)
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(@PathVariable UUID id, @RequestBody @Valid AccountRequest request) {
        AccountResponse response = accountService.updateAccount(id, request);

        return ResponseEntity.ok(
                ApiResponse.<AccountResponse>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_UPDATE_ACCOUNT)
                        .data(response)
                        .build()
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateAccount(@PathVariable UUID id) {
        accountService.deactivateAccount(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .message(CommonConstants.SUCCESS_DEACTIVATE_ACCOUNT)
                        .data(null)
                        .build()
        );
    }
}