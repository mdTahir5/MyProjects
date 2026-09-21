package com.phonebook.controller;

import com.phonebook.dto.common.PageResponse;
import com.phonebook.dto.contact.ContactRequest;
import com.phonebook.dto.contact.ContactResponse;
import com.phonebook.security.CurrentUserProvider;
import com.phonebook.service.ContactService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Contact CRUD.
 *
 * <p>
 * The whole controller sits behind
 * {@code .requestMatchers("/api/**").authenticated()},
 * and every call derives the owner from {@link CurrentUserProvider} - the
 * client
 * can never supply a {@code userId}.
 * </p>
 */
@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    /** Hard cap so a client cannot request an unbounded page size. */
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 12;

    private final ContactService contactService;
    private final CurrentUserProvider currentUserProvider;

    public ContactController(ContactService contactService, CurrentUserProvider currentUserProvider) {
        this.contactService = contactService;
        this.currentUserProvider = currentUserProvider;
    }

    /**
     * Lists the authenticated user's contacts, optionally filtered by a search
     * term.
     */
    @GetMapping
    public ResponseEntity<PageResponse<ContactResponse>> list(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "sort", defaultValue = "name,asc") String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        Long userId = currentUserProvider.requireUserId();
        return ResponseEntity.ok(contactService.list(userId, search, pageable));
    }

    /** Returns a single contact owned by the authenticated user. */
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getById(@PathVariable("id") Long id) {
        Long userId = currentUserProvider.requireUserId();
        return ResponseEntity.ok(contactService.getById(userId, id));
    }

    /** Creates a contact for the authenticated user. */
    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactRequest request,
            UriComponentsBuilder uriBuilder) {
        Long userId = currentUserProvider.requireUserId();
        ContactResponse created = contactService.create(userId, request);

        URI location = uriBuilder.path("/api/contacts/{id}").buildAndExpand(created.id()).toUri();
        log.debug("Created contact {} for user {}", created.id(), userId);
        return ResponseEntity.created(location).body(created);
    }

    /** Updates a contact owned by the authenticated user. */
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable("id") Long id,
            @Valid @RequestBody ContactRequest request) {
        Long userId = currentUserProvider.requireUserId();
        return ResponseEntity.ok(contactService.update(userId, id, request));
    }

    /** Deletes a contact owned by the authenticated user. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Long userId = currentUserProvider.requireUserId();
        contactService.delete(userId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ------------------------------------------------------------------

    /**
     * Validates and clamps the paging parameters.
     *
     * <p>
     * Only a whitelisted set of sort properties is accepted so the client
     * cannot sort by an arbitrary (or non-existent) column.
     * </p>
     */
    private Pageable buildPageable(int page, int size, String sort) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(Math.max(1, size), MAX_PAGE_SIZE);
        Sort safeSort = Sort.by(Sort.Order.asc("name"));

        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String property = parts[0].trim();
            if (ALLOWED_SORTS.contains(property)) {
                Sort.Direction direction = parts.length > 1
                        ? Sort.Direction.fromOptionalString(parts[1].trim()).orElse(Sort.Direction.ASC)
                        : Sort.Direction.ASC;
                safeSort = Sort.by(new Sort.Order(direction, property));
            }
        }
        return PageRequest.of(safePage, safeSize, safeSort);
    }

    private static final java.util.Set<String> ALLOWED_SORTS = java.util.Set.of("name", "email", "phone", "createdAt",
            "updatedAt");
}
