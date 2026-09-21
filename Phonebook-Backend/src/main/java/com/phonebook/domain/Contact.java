package com.phonebook.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

/**
 * A phonebook entry.
 *
 * <p>Ownership is expressed by the {@link #user} foreign key. Every service
 * operation is scoped to the authenticated user's id so one user can never
 * touch another user's contacts.</p>
 */
@Entity
@Table(
        name = "contacts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_contacts_user_phone", columnNames = {"user_id", "phone"})
        },
        indexes = {
                @Index(name = "idx_contacts_user_id", columnList = "user_id"),
                @Index(name = "idx_contacts_user_name", columnList = "user_id,name"),
                @Index(name = "idx_contacts_user_email", columnList = "user_id,email"),
                @Index(name = "idx_contacts_created_at", columnList = "created_at")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "fk_contacts_user"))
    private User user;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Contact() {
        // required by JPA
    }

    public Contact(User user, String name, String email, String phone) {
        this.user = user;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // ------------------------------------------------------------------
    // Identity is based on the immutable id
    // ------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contact{id=" + id + ", name='" + name + "', phone='" + phone + "'}";
    }
}
