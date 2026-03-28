# Keycloak Legacy Password Hash Provider Extension

Keycloak extension for validating legacy password hashes during user migration, with support for:

- `argon`, `argon2`, `argon2i`, `argon2id` (Argon2 PHC strings)
- `bcrypt` (standard bcrypt and TYPO3 bcrypt pre-processing)
- `md5` (legacy unsalted MD5)

## Why This Extension Exists

When migrating users from PHP applications (for example Symfony), passwords are already stored as hashes generated outside Keycloak.
Without a custom provider, Keycloak cannot validate many of those legacy formats directly, which usually forces immediate password reset for all users.

This extension solves that migration gap:

- It allows Keycloak to verify existing password hashes at login time.
- It keeps migration transparent for users (no forced reset on day one).
- It lets you phase out legacy hashes safely while moving identity to Keycloak.

## Design Goal

This project is intentionally **verification-only** for legacy hashes.

- `verify(...)` is implemented.
- `encode(...)` and `encodedCredential(...)` are intentionally not implemented.

Reason: the extension is for compatibility and migration, not for generating new legacy hashes.

## Typical Migration Use Case

1. Import users with existing hash values and algorithm IDs.
2. Let users authenticate with their current passwords.
3. Gradually move to your target password policy and lifecycle in Keycloak.

## Supported Algorithm IDs

For imported credentials, `credentialData.algorithm` must match one of:

- Argon2: `argon`, `argon2`, `argon2i`, `argon2id`
- BCrypt: `bcrypt`
- MD5: `md5`

## Example Credential Payloads

### Argon2id (PHP/Symfony style)

```json
{
  "type": "password",
  "credentialData": "{\"hashIterations\":1,\"algorithm\":\"argon2id\"}",
  "secretData": "{\"value\":\"$argon2id$v=19$m=65536,t=4,p=1$L0lWVkk4aS41bEg2VFp5Mg$nqhhieV5XW3dr5iTbf3fvpv0bO7/6bEMw2VGSAYMijg\"}"
}
```

### BCrypt

```json
{
  "type": "password",
  "credentialData": "{\"hashIterations\":12,\"algorithm\":\"bcrypt\"}",
  "secretData": "{\"value\":\"$2y$12$4A2DZnwG75qHDSr35BL0vOqdY7oXeA06zRDFsLshRbcdvbrHVR.Xa\"}"
}
```

### MD5

```json
{
  "type": "password",
  "credentialData": "{\"hashIterations\":1,\"algorithm\":\"md5\"}",
  "secretData": "{\"value\":\"5f4dcc3b5aa765d61d8327deb882cf99\"}"
}
```

## Security Notes

- MD5 support is provided only for migration from legacy systems.
- Keep legacy algorithms only as long as needed.
- Prefer strong password policy for all newly set passwords in Keycloak.

## Requirements

- JDK 17
- Keycloak 26.x

## Build

```bash
cd standalone-keycloak-password-hashprovider-extension
./mvnw -DskipTests package
```

Run tests:

```bash
./mvnw test
```

Target another Keycloak 26.x patch version:

```bash
./mvnw -Dkeycloak.version=26.1.0 -DskipTests package
```

## Artifact

Output JAR:

`extensions/extension-password-hashprovider/target/extension-password-hashprovider-1.0.0-SNAPSHOT.jar`

## Install in Keycloak

1. Copy JAR to `KEYCLOAK_HOME/providers/`
2. Rebuild Keycloak:

```bash
KEYCLOAK_HOME/bin/kc.sh build
```

3. Start Keycloak:

```bash
KEYCLOAK_HOME/bin/kc.sh start
```
