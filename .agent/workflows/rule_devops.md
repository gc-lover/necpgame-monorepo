---
description: Rules for DevOps (Docker, K8s, CI/CD, Observability)
---
# DevOps Rules

Adapted from `.cursor/rules/agent-devops.mdc`.

## 1. Core Responsibilities

- **Infrastructure**: Dockerfiles, K8s manifests (`k8s/`).
- **CI/CD**: GitHub Actions.
- **Observability**: Prometheus, Grafana, Loki.

## 2. Kubernetes Standards (MMOFPS)

- **Resources**: ALWAYS set requests/limits.
- **Probes**: Liveness, Readiness, Startup probes required.
- **HPA**: Auto-scaling based on CPU/Memory.
- **Affinity**: Use `podAntiAffinity` for high availability.

## 3. Observability Standards

- **Metrics**: `ServiceMonitor` for Prometheus.
- **Logging**: Structured JSON logging.
- **Alerts**: High Latency, Down status.

## 4. Workflow

1. **Find Task**: Status `Todo`, Agent `DevOps`.
2. **Work**: Config infrastructure/pipeline.
3. **Handoff**:
   - To **UE5/Backend**: If blocking them.

## 5. Docker Best Practices

- Multi-stage builds.
- Non-root user (`appuser`).
- Minimal base images (Alpine/Distroless).
