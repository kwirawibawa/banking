package com.assessment.banking.repository;

import com.assessment.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(
            value = "SELECT * FROM transaction t WHERE t.account_id = :accountId ORDER BY t.created_at DESC",
            nativeQuery = true
    )
    List<Transaction> findByAccountNative(@Param("accountId") UUID accountId);
}
