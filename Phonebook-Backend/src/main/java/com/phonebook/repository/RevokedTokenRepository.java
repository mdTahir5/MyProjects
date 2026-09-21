package com.phonebook.repository;

import com.phonebook.domain.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    boolean existsByJti(String jti);

    Optional<RevokedToken> findByJti(String jti);

    @Modifying
    @Query("delete from RevokedToken t where t.expiresAt < :now")
    int deleteAllExpiredBefore(@Param("now") Instant now);

    /**
     * Removes every deny-list entry owned by a user, so deleting an account does
     * not leave orphaned {@code fk_revoked_tokens_user} references behind.
     */
    @Modifying
    @Query("delete from RevokedToken t where t.user.id = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);
}
