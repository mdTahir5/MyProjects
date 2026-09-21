package com.phonebook.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for {@code DELETE /api/auth/account}.
 *
 * <p>
 * The client must echo the literal word {@code DELETE} in {@link #confirmation}
 * to prove the action was deliberate. Validation happens in two places: the
 * bean
 * constraints here reject an empty payload outright, and
 * {@code AuthService#deleteAccount} enforces the exact expected value so the
 * rule
 * lives next to the destructive write rather than only in the transport layer.
 * </p>
 */
public record DeleteAccountRequest(

                @NotBlank(message = "Please type DELETE to confirm account deletion") @Size(max = 20, message = "Confirmation text is too long") String confirmation) {
}
