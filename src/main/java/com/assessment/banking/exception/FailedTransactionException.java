package com.assessment.banking.exception;

public class FailedTransactionException extends RuntimeException {
    public FailedTransactionException(String message) {
        super(message);
    }
}
