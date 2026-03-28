# Standalone Keycloak Password HashProvider Extension

This standalone project provides a Keycloak password hash provider extension with support for validating legacy password hashes:

- `argon`, `argon2`, `argon2i`, `argon2id` (Argon2)
- `bcrypt` (standard bcrypt + TYPO3 bcrypt pre-processing)
- `md5` (legacy unsalted MD5)

`encode(...)` is intentionally not implemented for these providers. The extension is intended for migration/compatibility scenarios where old hashes must be verified and then replaced by a modern algorithm in Keycloak.

## Requirements

- JDK 17
- Keycloak 26.x

## Build

```bash
cd standalone-keycloak-password-hashprovider-extension
./mvnw -DskipTests package
```

To run tests:

```bash
./mvnw test
```

To target another Keycloak 26.x patch version:

```bash
./mvnw -Dkeycloak.version=26.1.0 -DskipTests package
```

## Artifact

Resulting JAR:

`extensions/extension-password-hashprovider/target/extension-password-hashprovider-1.0.0-SNAPSHOT.jar`

## Install in Keycloak

1. Copy the JAR into `KEYCLOAK_HOME/providers/`.
2. Rebuild Keycloak image:

```bash
KEYCLOAK_HOME/bin/kc.sh build
```

3. Start Keycloak:

```bash
KEYCLOAK_HOME/bin/kc.sh start
```

## Importing Users / Credential Algorithm IDs

When importing users with pre-hashed passwords, make sure `credentialData.algorithm` matches one of the registered provider IDs:

- Argon2: `argon`, `argon2`, `argon2i`, `argon2id`
- BCrypt: `bcrypt`
- MD5: `md5`

Example `credentials` entry in exported/imported user JSON:

```json
{
  "type": "password",
  "credentialData": "{\"hashIterations\":1,\"algorithm\":\"argon2id\"}",
  "secretData": "{\"value\":\"$argon2id$v=19$m=65536,t=4,p=1$L0lWVkk4aS41bEg2VFp5Mg$nqhhieV5XW3dr5iTbf3fvpv0bO7/6bEMw2VGSAYMijg\"}"
}
```
