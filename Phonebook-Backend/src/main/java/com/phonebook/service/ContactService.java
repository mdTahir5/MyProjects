package com.phonebook.service;

import com.phonebook.domain.Contact;
import com.phonebook.domain.User;
import com.phonebook.dto.common.PageResponse;
import com.phonebook.dto.contact.ContactRequest;
import com.phonebook.dto.contact.ContactResponse;
import com.phonebook.exception.DuplicateResourceException;
import com.phonebook.exception.ResourceNotFoundException;
import com.phonebook.repository.ContactRepository;
import com.phonebook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Contact CRUD, always scoped to the authenticated owner.
 *
 * <p>
 * Every method takes an explicit {@code userId} that comes from the security
 * context - never from the request payload. Lookups use
 * {@code findByIdAndUserId}, so an id belonging to another user simply does not
 * exist as far as this service is concerned (404, not 403: no information
 * leak).
 * </p>
 */
@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    // ------------------------------------------------------------------
    // Read
    // ------------------------------------------------------------------

    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> list(Long userId, String search, Pageable pageable) {
        Page<Contact> page = StringUtils.hasText(search)
                ? contactRepository.searchByUserId(userId, search.trim(), pageable)
                : contactRepository.findAllByUserId(userId, pageable);

        log.debug("Listed {} contact(s) for user id={} (page={}, size={})",
                page.getNumberOfElements(), userId, page.getNumber(), page.getSize());
        return PageResponse.from(page, ContactResponse::from);
    }

    @Transactional(readOnly = true)
    public ContactResponse getById(Long userId, Long contactId) {
        return ContactResponse.from(requireOwnedContact(userId, contactId));
    }

    @Transactional(readOnly = true)
    public long count(Long userId) {
        return contactRepository.countByUserId(userId);
    }

    // ------------------------------------------------------------------
    // Write
    // ------------------------------------------------------------------

    @Transactional
    public ContactResponse create(Long userId, ContactRequest request) {
        User owner = requireUser(userId);
        String phone = normalisePhone(request.phone());

        if (contactRepository.existsByUserIdAndPhone(userId, phone)) {
            throw new DuplicateResourceException(
                    "You already have a contact with the phone number " + phone, "phone");
        }

        Contact contact = new Contact(
                owner,
                request.name().trim(),
                normaliseEmail(request.email()),
                phone);

        Contact saved = contactRepository.save(contact);
        log.info("Created contact id={} for user id={}", saved.getId(), userId);
        return ContactResponse.from(saved);
    }

    @Transactional
    public ContactResponse update(Long userId, Long contactId, ContactRequest request) {
        Contact contact = requireOwnedContact(userId, contactId);
        String phone = normalisePhone(request.phone());

        if (contactRepository.existsByUserIdAndPhoneAndIdNot(userId, phone, contactId)) {
            throw new DuplicateResourceException(
                    "You already have a contact with the phone number " + phone, "phone");
        }

        contact.setName(request.name().trim());
        contact.setEmail(normaliseEmail(request.email()));
        contact.setPhone(phone);

        Contact saved = contactRepository.save(contact);
        log.info("Updated contact id={} for user id={}", saved.getId(), userId);
        return ContactResponse.from(saved);
    }

    @Transactional
    public void delete(Long userId, Long contactId) {
        // Ownership is part of the delete predicate: a contact owned by someone
        // else simply matches zero rows.
        int deleted = contactRepository.deleteByIdAndUserId(contactId, userId);
        if (deleted == 0) {
            throw ResourceNotFoundException.contact(contactId);
        }
        log.info("Deleted contact id={} for user id={}", contactId, userId);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    /**
     * Loads a contact that belongs to {@code userId}.
     *
     * @throws ResourceNotFoundException when it does not exist <em>for this
     *                                   user</em>
     */
    private Contact requireOwnedContact(Long userId, Long contactId) {
        return contactRepository.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> ResourceNotFoundException.contact(contactId));
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
    }

    /** Collapses whitespace so "+1 555 0100" and "+1 555 0100" are one number. */
    private static String normalisePhone(String phone) {
        return phone == null ? "" : phone.trim().replaceAll("\\s+", " ");
    }

    private static String normaliseEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
