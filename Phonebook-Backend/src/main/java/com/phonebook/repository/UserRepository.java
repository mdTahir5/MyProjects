package com.phonebook.repository;

import com.phonebook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Case-insensitive lookup by email. Emails are normalised to lower case
     * before persisting, so this is an index-friendly equality lookup.
     */
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
