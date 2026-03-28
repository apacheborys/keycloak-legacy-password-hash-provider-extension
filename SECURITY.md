# Security Policy

## Supported Versions

Security fixes are applied to the active development line only.

| Version | Supported |
| --- | --- |
| `main` | Yes |
| older tags/branches | No |

## Reporting a Vulnerability

Please do not open public GitHub issues for sensitive vulnerabilities.

Report security issues privately with:

- clear description of the issue
- impact and attack scenario
- steps to reproduce
- affected versions/commits

If no private security channel is configured yet, contact the maintainer directly and request a private disclosure path.

## Scope Notes

This project handles verification of legacy password hashes for migration scenarios.
When reporting security issues, include whether the issue affects:

- hash verification correctness
- algorithm handling/parsing
- denial-of-service vectors (malformed inputs)
- dependency or supply-chain concerns

