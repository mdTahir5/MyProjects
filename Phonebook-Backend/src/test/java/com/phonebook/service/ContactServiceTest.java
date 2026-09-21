package com.phonebook.service;

import com.phonebook.domain.AuthProvider;
import com.phonebook.domain.Contact;
import com.phonebook.domain.User;
import com.phonebook.dto.contact.ContactRequest;
import com.phonebook.dto.contact.ContactResponse;
import com.phonebook.exception.DuplicateResourceException;
import com.phonebook.exception.ResourceNotFoundException;
import com.phonebook.repository.ContactRepository;
import com.phonebook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Ownership and CRUD behaviour of {@link ContactService}.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ContactServiceTest {

        @Autowired
        private ContactService contactService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ContactRepository contactRepository;

        private Long ownerId;
        private Long otherUserId;
        private Long ownerContactId;

        @BeforeEach
        void setUp() {
                contactRepository.deleteAll();
                userRepository.deleteAll();

                User owner = userRepository.save(new User("Owner One", "owner@example.com",
                                "$2a$12$abcdefghijklmnopqrstuv", AuthProvider.LOCAL));
                User other = userRepository.save(new User("Other Two", "other@example.com",
                                "$2a$12$abcdefghijklmnopqrstuv", AuthProvider.LOCAL));

                ownerId = owner.getId();
                otherUserId = other.getId();

                Contact contact = contactRepository.save(
                                new Contact(owner, "Alice Johnson", "alice@example.com", "+15550100"));
                ownerContactId = contact.getId();
        }

        @Test
        @DisplayName("lists only the authenticated user's contacts")
        void listIsScopedToOwner() {
                assertThat(contactService.list(ownerId, null, PageRequest.of(0, 10)).totalElements()).isEqualTo(1);
                assertThat(contactService.list(otherUserId, null, PageRequest.of(0, 10)).totalElements()).isZero();
        }

        @Test
        @DisplayName("returns 404-equivalent when another user requests the contact")
        void cannotReadAnotherUsersContact() {
                assertThatThrownBy(() -> contactService.getById(otherUserId, ownerContactId))
                                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("cannot update another user's contact")
        void cannotUpdateAnotherUsersContact() {
                ContactRequest request = new ContactRequest("Hacked", "hack@example.com", "+15559999");
                assertThatThrownBy(() -> contactService.update(otherUserId, ownerContactId, request))
                                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("cannot delete another user's contact")
        void cannotDeleteAnotherUsersContact() {
                assertThatThrownBy(() -> contactService.delete(otherUserId, ownerContactId))
                                .isInstanceOf(ResourceNotFoundException.class);
                assertThat(contactRepository.findById(ownerContactId)).isPresent();
        }

        @Test
        @DisplayName("creates, updates and deletes a contact for the owner")
        void fullCrudLifecycle() {
                ContactResponse created = contactService.create(ownerId,
                                new ContactRequest("Bob Smith", "BOB@Example.com ", "+1 555 0101"));
                assertThat(created.id()).isNotNull();
                assertThat(created.email()).isEqualTo("bob@example.com"); // normalised

                ContactResponse updated = contactService.update(ownerId, created.id(),
                                new ContactRequest("Bob S.", "bob.s@example.com", "+1 555 0101"));
                assertThat(updated.name()).isEqualTo("Bob S.");

                contactService.delete(ownerId, created.id());
                assertThat(contactRepository.findById(created.id())).isEmpty();
        }

        @Test
        @DisplayName("rejects a duplicate phone number for the same user")
        void rejectsDuplicatePhone() {
                ContactRequest request = new ContactRequest("Duplicate", "dup@example.com", "+15550100");
                assertThatThrownBy(() -> contactService.create(ownerId, request))
                                .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("search matches name, email and phone")
        void searchMatchesAllFields() {
                assertThat(contactService.list(ownerId, "alice", PageRequest.of(0, 10)).totalElements())
                                .isEqualTo(1);
                assertThat(contactService.list(ownerId, "alice@example", PageRequest.of(0, 10)).totalElements())
                                .isEqualTo(1);
                assertThat(contactService.list(ownerId, "5550100", PageRequest.of(0, 10)).totalElements())
                                .isEqualTo(1);
                assertThat(contactService.list(ownerId, "nobody", PageRequest.of(0, 10)).totalElements())
                                .isZero();
        }
}
