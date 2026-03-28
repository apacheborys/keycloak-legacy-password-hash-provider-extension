package com.inventage.keycloak.passwordhashprovider.md5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.util.List;

class MD5PasswordHashProviderTest {

    private static MD5PasswordHashProvider md5PasswordHashProvider;

    @BeforeAll
    static void init_provider() {
        MD5PasswordHashProviderFactory factory = new MD5PasswordHashProviderFactory();
        md5PasswordHashProvider = (MD5PasswordHashProvider) factory.create(null);
    }

    @Test
    void valid_md5_hashes() {
        final List<String> hashes = List.of(
                "5f4dcc3b5aa765d61d8327deb882cf99", // password
                "098f6bcd4621d373cade4e832627b4f6", // test
                "827ccb0eea8a706c4c34a16891f84e7b"  // 12345
        );
        final List<String> passwords = List.of("password", "test", "12345");

        for (int i = 0; i < hashes.size(); i++) {
            Assertions.assertTrue(
                    md5PasswordHashProvider.verifyMd5Password(passwords.get(i), hashes.get(i)),
                    "Password " + passwords.get(i) + " should match hash " + hashes.get(i));
        }
    }

    @Test
    void valid_md5_hash_with_uppercase_hex() {
        final String hashUppercase = "5F4DCC3B5AA765D61D8327DEB882CF99"; // password
        Assertions.assertTrue(
                md5PasswordHashProvider.verifyMd5Password("password", hashUppercase),
                "Uppercase MD5 hash should be treated case-insensitively");
    }

    @Test
    void invalid_md5_hashes() {
        final List<String> hashes = List.of(
                "5f4dcc3b5aa765d61d8327deb882cf99", // password
                "098f6bcd4621d373cade4e832627b4f6"  // test
        );
        final List<String> passwords = List.of("wrong-password", "another");

        for (int i = 0; i < hashes.size(); i++) {
            Assertions.assertFalse(
                    md5PasswordHashProvider.verifyMd5Password(passwords.get(i), hashes.get(i)),
                    "Password " + passwords.get(i) + " should not match hash " + hashes.get(i));
        }
    }

    @Test
    void encoding_not_supported() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> md5PasswordHashProvider.encode("password", 1),
                "MD5 encoding must remain unimplemented");
    }

    @Test
    void malformed_md5_hashes_return_false() {
        Assertions.assertFalse(md5PasswordHashProvider.verifyMd5Password("password", "not-md5"));
        Assertions.assertFalse(md5PasswordHashProvider.verifyMd5Password("password", "5f4dcc3b5aa765d61d8327deb882cf9"));
        Assertions.assertFalse(md5PasswordHashProvider.verifyMd5Password(null, "5f4dcc3b5aa765d61d8327deb882cf99"));
    }

    @Test
    void verify_credential_model_for_md5() {
        final PasswordCredentialModel credential = PasswordCredentialModel.createFromValues(
                MD5PasswordHashProviderFactory.ID,
                null,
                1,
                "5f4dcc3b5aa765d61d8327deb882cf99"
        );
        Assertions.assertTrue(md5PasswordHashProvider.verify("password", credential));
    }
}
