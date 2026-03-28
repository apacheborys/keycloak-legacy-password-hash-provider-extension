package com.inventage.keycloak.passwordhashprovider.argon;

import com.google.auto.service.AutoService;
import org.keycloak.credential.hash.PasswordHashProviderFactory;

@AutoService(PasswordHashProviderFactory.class)
public class Argon2iPasswordHashProviderFactory extends AbstractArgonPasswordHashProviderFactory {
    public static final String ID = "argon2i";

    public Argon2iPasswordHashProviderFactory() {
        super(ID);
    }
}
