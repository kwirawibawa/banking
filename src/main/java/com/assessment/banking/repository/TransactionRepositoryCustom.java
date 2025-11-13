package com.assessment.banking.repository;

import com.assessment.banking.request.TransactionSearchRequest;
import com.assessment.banking.response.TransactionResponse;

import java.util.List;

public interface TransactionRepositoryCustom {
    Long searchCount(TransactionSearchRequest request);

    List<TransactionResponse> searchData(TransactionSearchRequest request);
}
