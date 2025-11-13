package com.assessment.banking.repository;

import com.assessment.banking.entity.TransferDirection;
import com.assessment.banking.request.TransactionSearchRequest;
import com.assessment.banking.response.TransactionResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long searchCount(TransactionSearchRequest request) {
        StringBuilder sb = getCountQuery(request);

        Query query = entityManager.createNativeQuery(sb.toString());

        if (request.getAccountId() != null) query.setParameter("accountId", request.getAccountId());
        if (request.getType() != null && !request.getType().isBlank()) query.setParameter("type", request.getType());
        if (request.getStartDate() != null && request.getEndDate() != null) {
            query.setParameter("startDate", request.getStartDate());
            query.setParameter("endDate", request.getEndDate());
        }

        Object result = query.getSingleResult();
        return result != null ? ((Number) result).longValue() : 0L;
    }

    private static StringBuilder getCountQuery(TransactionSearchRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM transaction t WHERE 1=1 ");

        if (request.getAccountId() != null) {
            sb.append(" AND t.account_id = :accountId ");
        }
        if (request.getType() != null && !request.getType().isBlank()) {
            sb.append(" AND t.type = :type ");
        }
        if (request.getStartDate() != null && request.getEndDate() != null) {
            sb.append(" AND t.created_at BETWEEN :startDate AND :endDate ");
        }
        return sb;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TransactionResponse> searchData(TransactionSearchRequest request) {
        StringBuilder sb = getSearchQuery(request);

        Query query = entityManager.createNativeQuery(sb.toString());

        if (request.getAccountId() != null) query.setParameter("accountId", request.getAccountId());
        if (request.getType() != null && !request.getType().isBlank()) query.setParameter("type", request.getType());
        if (request.getStartDate() != null && request.getEndDate() != null) {
            query.setParameter("startDate", request.getStartDate());
            query.setParameter("endDate", request.getEndDate());
        }

        int offset = (request.getPageNumber() - 1) * request.getPageSize();
        query.setParameter("pageSize", request.getPageSize());
        query.setParameter("offset", offset);

        return query.getResultList().stream()
                .map(row -> {
                    Object[] r = (Object[]) row;
                    UUID id = r[0] instanceof UUID ? (UUID) r[0] : UUID.nameUUIDFromBytes((byte[]) r[0]);
                    UUID accountId = r[1] instanceof UUID ? (UUID) r[1] : UUID.nameUUIDFromBytes((byte[]) r[1]);

                    return TransactionResponse.builder()
                            .id(id)
                            .accountId(accountId)
                            .type((String) r[2])
                            .direction(r[3] != null ? TransferDirection.valueOf(r[3].toString()) : null)
                            .amount((BigDecimal) r[4])
                            .createdAt(((Timestamp) r[5]).toLocalDateTime())
                            .build();
                }).toList();
    }

    private static StringBuilder getSearchQuery(TransactionSearchRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.id, t.account_id, t.type, t.direction, t.amount, t.created_at ");
        sb.append("FROM transaction t WHERE 1=1 ");

        if (request.getAccountId() != null) sb.append(" AND t.account_id = :accountId ");
        if (request.getType() != null && !request.getType().isBlank()) sb.append(" AND t.type = :type ");
        if (request.getStartDate() != null && request.getEndDate() != null) sb.append(" AND t.created_at BETWEEN :startDate AND :endDate ");

        sb.append(" ORDER BY t.created_at DESC ");
        sb.append(" LIMIT :pageSize OFFSET :offset ");
        return sb;
    }
}
