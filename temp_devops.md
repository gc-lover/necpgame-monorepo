---
description: DevOps Rules (Kubernetes, Docker, CI/CD)
globs: ["**/infrastructure/**", "**/Dockerfile*", "**/.github/workflows/**"]
alwaysApply: false
---
# DevOps Rules

## 1. Responsibilities

- **Infra**: K8s manifests in `infrastructure/`.
- **CI/CD**: efficient pipelines.
- **Observability**: Prometheus/Grafana.

## 2. Standards

- **Resources**: Requests/Limits required.
- **Probes**: Liveness/Readiness required.
- **Images**: Alpine/Distroless.
