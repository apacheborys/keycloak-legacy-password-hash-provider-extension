package com.inventage.keycloak.passwordhashprovider.argon;

import com.google.auto.service.AutoService;
import org.keycloak.credential.hash.PasswordHashProviderFactory;

@AutoService(PasswordHashProviderFactory.class)
public class Argon2PasswordHashProviderFactory extends AbstractArgonPasswordHashProviderFactory {
    public static final String ID = "argon2";

    public Argon2PasswordHashProviderFactory() {
        super(ID);
    }
}
