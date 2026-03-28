package com.inventage.keycloak.passwordhashprovider.argon;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


public class ArgonPasswordHashProvider implements PasswordHashProvider {
    private final String providerId;
    private static final Argon2PasswordEncoder ARGON2_PASSWORD_ENCODER = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public ArgonPasswordHashProvider(final String providerId) {
        this.providerId = providerId;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        return providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        throw new UnsupportedOperationException("Argon credential encoding not implemented! Verification-only provider.");
    }

    @Override
    public String encode(final String rawPassword, final int iterations) {
        throw new UnsupportedOperationException("Argon encoding not implemented! Verification-only provider.");
    }
    @Override
    public void close() {
    }

    @Override
    public boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        if (credential == null || credential.getPasswordSecretData() == null) {
            return false;
        }
        final String expectedHash = credential.getPasswordSecretData().getValue();
        return verifyArgonPassword(rawPassword, expectedHash);
    }

    boolean verifyArgonPassword(final String rawPassword, final String expectedHash) {
        if (rawPassword == null || expectedHash == null || expectedHash.isBlank()) {
            return false;
        }
        if (!expectedHash.startsWith("$argon2")) {
            return false;
        }
        try {
            return ARGON2_PASSWORD_ENCODER.matches(rawPassword, expectedHash);
        } catch (RuntimeException ignored) {
            return false;
        }
    }

}
