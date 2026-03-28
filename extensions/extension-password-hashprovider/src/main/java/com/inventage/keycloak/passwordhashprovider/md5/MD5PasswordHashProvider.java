package com.inventage.keycloak.passwordhashprovider.md5;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Legacy MD5 verifier. Only supports verification of existing hashes; encoding is intentionally not implemented.
 */
public class MD5PasswordHashProvider implements PasswordHashProvider {
    private final String providerId;
    private static final Pattern MD5_HEX_PATTERN = Pattern.compile("^[a-fA-F0-9]{32}$");

    public MD5PasswordHashProvider(final String providerId) {
        this.providerId = providerId;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        return providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        throw new UnsupportedOperationException("MD5 encoding not implemented! Verification-only provider.");
    }

    @Override
    public String encode(final String rawPassword, final int iterations) {
        throw new UnsupportedOperationException("MD5 encoding not implemented! Verification-only provider.");
    }

    @Override
    public void close() {
    }

    @Override
    public boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        if (rawPassword == null || credential == null || credential.getPasswordSecretData() == null) {
            return false;
        }
        final String expectedHash = credential.getPasswordSecretData().getValue();
        if (expectedHash == null) {
            return false;
        }
        return verifyMd5Password(rawPassword, expectedHash);
    }

    boolean verifyMd5Password(final String rawPassword, final String expectedHash) {
        if (rawPassword == null || expectedHash == null || !MD5_HEX_PATTERN.matcher(expectedHash).matches()) {
            return false;
        }
        final String computedHash = md5Hex(rawPassword);
        return expectedHash.equalsIgnoreCase(computedHash);
    }

    String md5Hex(final String rawPassword) {
        final MessageDigest md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
        byte[] digest = md5Digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
