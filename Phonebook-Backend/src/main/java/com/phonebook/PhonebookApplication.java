package com.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application entry point.
 *
 * <p>JPA auditing is enabled so {@code @CreatedDate} / {@code @LastModifiedDate}
 * fields are maintained automatically, and scheduling is enabled for the
 * periodic revoked-token cleanup job.</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
@EnableScheduling
public class PhonebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhonebookApplication.class, args);
    }
}
