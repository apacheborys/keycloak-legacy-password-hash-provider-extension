package com.inventage.keycloak.passwordhashprovider.bcrypt;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.crypto.JavaAlgorithm;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Typo3BCryptPasswordHashProvider implements PasswordHashProvider {
    private final String providerId;
    private static final String TYPO3_PASSWORD_PREPROCESSING_HASH_ALGORITHM = JavaAlgorithm.SHA384;

    public Typo3BCryptPasswordHashProvider(final String providerId, final int defaultIterations) {
        this.providerId = providerId;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        return providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        throw new UnsupportedOperationException("BCrypt encoding not implemented! Verification-only provider.");
    }

    @Override
    public String encode(final String rawPassword, final int iterations) {
        throw new UnsupportedOperationException("BCrypt encoding not implemented! Verification-only provider.");
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
        return verifyBCryptPassword(rawPassword, expectedHash);
    }

    boolean verifyBCryptPassword(final String rawPassword, final String expectedHash) {
        if (rawPassword == null || expectedHash == null || expectedHash.isBlank()) {
            return false;
        }
        if (verifyTypo3BCryptPassword(rawPassword, expectedHash)) {
            return true;
        }
        // fall-back to the standardBCrypt verification if password was not hashed with the typo3 implementation.
        try {
            return BCrypt.checkpw(rawPassword, expectedHash);
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    // Implementation based on: https://github.com/TYPO3/typo3/blob/main/typo3/sysext/core/Classes/Crypto/PasswordHashing/BcryptPasswordHash.php#L145
    boolean verifyTypo3BCryptPassword(final String rawPassword, final String expectedHash) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance(TYPO3_PASSWORD_PREPROCESSING_HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(rawPassword.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        String typo3Password = Base64.getEncoder().encodeToString(digest);
        try {
            return BCrypt.checkpw(typo3Password, expectedHash);
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

}
