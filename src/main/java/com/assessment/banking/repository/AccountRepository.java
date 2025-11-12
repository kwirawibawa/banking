package com.assessment.banking.repository;

import com.assessment.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query(value = "SELECT * FROM account WHERE email = :email AND is_active = true", nativeQuery = true)
    Optional<Account> findActiveByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM account WHERE name = :name AND is_active = true", nativeQuery = true)
    Optional<Account> findActiveByName(@Param("name") String name);

    @Modifying
    @Query(value = "UPDATE account SET name = :name, email = :email WHERE id = :id", nativeQuery = true)
    int updateAccountNative(@Param("id") UUID id,
                            @Param("name") String name,
                            @Param("email") String email);

    @Modifying
    @Query(value = "UPDATE account SET is_active = false WHERE id = :id", nativeQuery = true)
    int softDeleteAccountNative(@Param("id") UUID id);
}
