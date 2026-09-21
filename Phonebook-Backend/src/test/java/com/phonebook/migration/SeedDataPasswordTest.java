package com.phonebook.migration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the demo credentials advertised in the README.
 *
 * The seeded hash is read straight out of the Flyway migration, so documentation
 * and migration can never drift apart: if the hash is changed without updating
 * the documented password (or vice versa), this test fails.
 */
class SeedDataPasswordTest {

    /** The password the README tells a new developer to sign in with. */
    private static final String DOCUMENTED_PASSWORD = "Password@123";

    private static final Pattern HASH_PATTERN =
            Pattern.compile("\\$2a\\$\\d{2}\\$[./A-Za-z0-9]{53}");

    @Test
    @DisplayName("the seeded demo hash matches the documented demo password")
    void seededHashMatchesDocumentedPassword() throws IOException {
        String hash = readSeededHash();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertThat(encoder.matches(DOCUMENTED_PASSWORD, hash))
                .as("seed migration hash must match the documented password '%s'", DOCUMENTED_PASSWORD)
                .isTrue();
    }

    @Test
    @DisplayName("the seed migration stores only hashes, never a plaintext password")
    void seedMigrationStoresOnlyHashes() throws IOException {
        // Comments are allowed to document the password; the executable SQL is not.
        String statementsOnly = stripSqlComments(readMigration());

        assertThat(statementsOnly)
                .as("no executable statement may contain the plaintext password")
                .doesNotContain(DOCUMENTED_PASSWORD);

        assertThat(HASH_PATTERN.matcher(statementsOnly).find())
                .as("the executable SQL must contain a BCrypt hash")
                .isTrue();
    }

    /** Removes {@code --} line comments so only real SQL is inspected. */
    private static String stripSqlComments(String sql) {
        return sql.lines()
                .map(line -> {
                    int comment = line.indexOf("--");
                    return comment >= 0 ? line.substring(0, comment) : line;
                })
                .reduce("", (a, b) -> a + "\n" + b);
    }

    private static String readSeededHash() throws IOException {
        Matcher matcher = HASH_PATTERN.matcher(readMigration());
        assertThat(matcher.find()).as("no BCrypt hash found in V2 migration").isTrue();
        return matcher.group();
    }

    private static String readMigration() throws IOException {
        // The seed lives outside db/migration on purpose: only the `dev` profile
        // adds db/seed to the Flyway locations, so production never receives
        // these demo credentials.
        String resource = "db/seed/V2__seed_demo_data.sql";
        try (InputStream in = SeedDataPasswordTest.class.getClassLoader().getResourceAsStream(resource)) {
            assertThat(in).as("resource %s must exist", resource).isNotNull();
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
