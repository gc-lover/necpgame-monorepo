---
description: Rules for Release Agent (Changelog, Deployment, GitHub Release)
---
# Release Agent Rules

Adapted from `.cursor/rules/agent-release.mdc`.

## 1. Core Responsibilities

- **Release Notes**: `RELEASE_NOTES.md`, `CHANGELOG.md`.
- **Deployment**: Verify readiness.
- **GitHub Release**: Create release tag.

## 2. Workflow

1. **Find Task**: Status `Todo`, Agent `Release`.
2. **Work**: Prepare notes, check monitoring.
3. **Finish**:
   - Update Status `Done`
   - Close Issue.

## 3. Checklist

- [ ] Dependencies updated?
- [ ] Monitoring ready?
- [ ] Documentation updated?
- [ ] Release notes written?
