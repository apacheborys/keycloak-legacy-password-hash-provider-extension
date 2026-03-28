package com.inventage.keycloak.passwordhashprovider.argon;

import com.google.auto.service.AutoService;
import org.keycloak.credential.hash.PasswordHashProviderFactory;

@AutoService(PasswordHashProviderFactory.class)
public class ArgonPasswordHashProviderFactory extends AbstractArgonPasswordHashProviderFactory {
    public static final String ID = "argon";

    public ArgonPasswordHashProviderFactory() {
        super(ID);
    }
}
