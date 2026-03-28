package com.inventage.keycloak.passwordhashprovider.argon;

import org.keycloak.Config;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public abstract class AbstractArgonPasswordHashProviderFactory implements PasswordHashProviderFactory {
    private final String id;

    protected AbstractArgonPasswordHashProviderFactory(final String id) {
        this.id = id;
    }

    @Override
    public PasswordHashProvider create(final KeycloakSession session) {
        return new ArgonPasswordHashProvider(id);
    }

    @Override
    public void init(final Config.Scope config) {
    }

    @Override
    public void postInit(final KeycloakSessionFactory factory) {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void close() {
    }
}
