package com.assessment.banking.util;

public final class CommonConstants {

    private CommonConstants() {

    }

    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String VALIDATION_FAILED = "VALIDATION_FAILED";

    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String EMAIL_EXISTS = "Email already exists";
    public static final String NAME_EXISTS = "Name already exists";
    public static final String UPDATE_ACCOUNT_FAILED = "Update account failed";
    public static final String DEACTIVATE_ACCOUNT_FAILED = "Failed to deactivate account";

    public static final String SUCCESS_CREATE_ACCOUNT = "Account successfully created";
    public static final String SUCCESS_GET_ACCOUNT = "Successfully retrieved account";
    public static final String SUCCESS_UPDATE_ACCOUNT = "Account successfully updated";
    public static final String SUCCESS_DEACTIVATE_ACCOUNT = "Account successfully deactivated";

    public static final String INVALID_TYPE = "Invalid transaction type";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String FAILED_TRANSFER_SENDER = "Cannot transfer to the same account";
    public static final String SENDER_ACCOUNT_NOT_FOUND = "Sender account not found";
    public static final String RECEIVER_ACCOUNT_NOT_FOUND = "Receiver account not found";
    public static final String INSUFFICIENT_BALANCE_SENDER = "Insufficient balance in sender account";

    public static final String SUCCESS_CREATE_TRANSACTION = "Transaction successfully created";
    public static final String SUCCESS_GET_TRANSACTION = "Successfully retrieved transactions";
    public static final String SUCCESS_TRANSFER = "Transfer successfully processed";
    public static final String SUCCESS_SEARCH_TRANSACTION = "Transactions search completed successfully";
}