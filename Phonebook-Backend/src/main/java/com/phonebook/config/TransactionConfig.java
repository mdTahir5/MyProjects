package com.phonebook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

/**
 * Transaction configuration.
 *
 * <p>Declarative transactions are enabled and the JPA transaction manager is
 * exposed explicitly so {@code @Transactional(readOnly = true)} is honoured on
 * read paths (Hibernate then skips dirty-checking).</p>
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setNestedTransactionAllowed(false);
        return transactionManager;
    }
}
