#!/bin/bash
# Deploy observability stack (Prometheus, Loki, Grafana, Tempo, Promtail)
# Usage: ./deploy-observability.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
K8S_DIR="$SCRIPT_DIR/../../k8s"

echo "========================================="
echo "NECPGAME - Deploy Observability Stack"
echo "========================================="

# Create monitoring namespace
kubectl create namespace monitoring --dry-run=client -o yaml | kubectl apply -f -

# Deploy observability components
echo ""
echo "Deploying Prometheus..."
kubectl apply -f "$K8S_DIR/prometheus-deployment.yaml"

echo ""
echo "Deploying Loki..."
kubectl apply -f "$K8S_DIR/loki-deployment.yaml"

echo ""
echo "Deploying Grafana..."
kubectl apply -f "$K8S_DIR/grafana-deployment.yaml"

echo ""
echo "Deploying Tempo..."
kubectl apply -f "$K8S_DIR/tempo-deployment.yaml"

echo ""
echo "Deploying Promtail..."
kubectl apply -f "$K8S_DIR/promtail-daemonset.yaml"

# Wait for rollout
echo ""
echo "Waiting for rollout..."
kubectl rollout status deployment/prometheus -n monitoring --timeout=5m || true
kubectl rollout status deployment/loki -n monitoring --timeout=5m || true
kubectl rollout status deployment/grafana -n monitoring --timeout=5m || true
kubectl rollout status deployment/tempo -n monitoring --timeout=5m || true

# Show status
echo ""
echo "========================================="
echo "OK Observability stack deployed!"
echo "========================================="
echo ""
echo "Pods status:"
kubectl get pods -n monitoring
echo ""
echo "Services status:"
kubectl get services -n monitoring
echo ""
echo "Access Grafana:"
echo "  kubectl port-forward -n monitoring svc/grafana 3000:3000"
echo "  Then open http://localhost:3000 (admin/admin)"
echo ""
echo "Access Prometheus:"
echo "  kubectl port-forward -n monitoring svc/prometheus 9090:9090"
echo "  Then open http://localhost:9090"

