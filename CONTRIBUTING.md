# Contributing

Thanks for your interest in improving this project.

## Development Setup

### Prerequisites

- JDK 17
- Maven Wrapper (already included)

### Build and test

```bash
./mvnw test
./mvnw -DskipTests package
```

## Contribution Workflow

1. Fork the repository.
2. Create a feature branch from `main`.
3. Make your changes with tests when applicable.
4. Run `./mvnw test` before opening a PR.
5. Open a pull request with a clear description of:
   - problem being solved
   - approach taken
   - migration/security impact (if any)

## Commit Message Guidance

Use concise, imperative commit messages. Example:

- `Add Argon2 alias provider factory`
- `Improve malformed hash handling in bcrypt verifier`

## Scope and Non-Goals

This extension is designed for migration compatibility and legacy hash verification.
It intentionally does not provide legacy hash generation for new passwords.

