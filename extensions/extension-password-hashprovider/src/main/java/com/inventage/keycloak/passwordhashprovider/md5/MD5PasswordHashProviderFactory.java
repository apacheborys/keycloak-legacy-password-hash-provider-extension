package com.inventage.keycloak.passwordhashprovider.md5;

import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

@AutoService(PasswordHashProviderFactory.class)
public class MD5PasswordHashProviderFactory implements PasswordHashProviderFactory {
    public static final String ID = "md5";

    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return new MD5PasswordHashProvider(ID);
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void close() {
    }
}
