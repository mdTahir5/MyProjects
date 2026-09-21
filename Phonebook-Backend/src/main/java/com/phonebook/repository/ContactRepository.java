package com.phonebook.repository;

import com.phonebook.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * All queries are scoped by {@code userId} so data access can never leak
 * across tenants (users).
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

  Page<Contact> findAllByUserId(Long userId, Pageable pageable);

  Optional<Contact> findByIdAndUserId(Long id, Long userId);

  boolean existsByUserIdAndPhone(Long userId, String phone);

  boolean existsByUserIdAndPhoneAndIdNot(Long userId, String phone, Long id);

  long countByUserId(Long userId);

  @Query("""
      select c from Contact c
      where c.user.id = :userId
        and (
              lower(c.name) like lower(concat('%', :term, '%'))
           or lower(coalesce(c.email, '')) like lower(concat('%', :term, '%'))
           or lower(c.phone) like lower(concat('%', :term, '%'))
        )
      """)
  Page<Contact> searchByUserId(@Param("userId") Long userId,
      @Param("term") String term,
      Pageable pageable);

  /**
   * Deletes a contact, but only when it belongs to {@code userId}.
   *
   * <p>Ownership is part of the predicate, so a contact owned by another user
   * simply matches zero rows - callers can treat that as "not found".</p>
   *
   * <p>{@code flushAutomatically} pushes pending changes before the bulk delete
   * (bulk statements bypass the persistence context), and
   * {@code clearAutomatically} then discards the now-stale entities so subsequent
   * reads in the same transaction observe the deletion.</p>
   */
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("delete from Contact c where c.id = :id and c.user.id = :userId")
  int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

  /**
   * Deletes every contact owned by {@code userId}.
   *
   * <p>Used when an account is deleted: the {@code fk_contacts_user} foreign key
   * would otherwise block the removal of the owning row.</p>
   */
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("delete from Contact c where c.user.id = :userId")
  int deleteAllByUserId(@Param("userId") Long userId);
}
