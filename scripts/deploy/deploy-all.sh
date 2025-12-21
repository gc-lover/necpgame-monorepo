#!/bin/bash
# Deploy all services to Kubernetes
# Usage: ./deploy-all.sh [namespace]

set -e

NAMESPACE="${1:-necpgame}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
K8S_DIR="$SCRIPT_DIR/../../k8s"

echo "========================================="
echo "NECPGAME - Deploy All Services"
echo "========================================="
echo "Namespace: $NAMESPACE"
echo "========================================="

# Create namespace if not exists
kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -

# Deploy base resources
echo ""
echo "Step 1/4: Deploying base resources..."
kubectl apply -f "$K8S_DIR/configmap-common.yaml"
kubectl apply -f "$K8S_DIR/rbac-service-account.yaml"
kubectl apply -f "$K8S_DIR/networkpolicy-default.yaml"
kubectl apply -f "$K8S_DIR/resource-quota.yaml"
kubectl apply -f "$K8S_DIR/servicemonitor-common.yaml"
kubectl apply -f "$K8S_DIR/ingress.yaml"

# Deploy services
echo ""
echo "Step 2/4: Deploying services..."
kubectl apply -f "$K8S_DIR/character-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/inventory-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/movement-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/social-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/achievement-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/economy-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/support-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/reset-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/gameplay-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/admin-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/clan-war-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/companion-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/voice-chat-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/housing-service-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/realtime-gateway-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/ws-lobby-go-deployment.yaml"
kubectl apply -f "$K8S_DIR/matchmaking-go-deployment.yaml"

# Deploy autoscaling and HA
echo ""
echo "Step 3/4: Deploying autoscaling and HA..."
kubectl apply -f "$K8S_DIR/hpa-services.yaml"
kubectl apply -f "$K8S_DIR/pdb-services.yaml"

# Wait for rollout
echo ""
echo "Step 4/4: Waiting for rollout..."
kubectl rollout status deployment/character-service-go -n "$NAMESPACE" --timeout=5m || true
kubectl rollout status deployment/inventory-service-go -n "$NAMESPACE" --timeout=5m || true
kubectl rollout status deployment/movement-service-go -n "$NAMESPACE" --timeout=5m || true

# Show status
echo ""
echo "========================================="
echo "[OK] Deployment completed!"
echo "========================================="
echo ""
echo "Pods status:"
kubectl get pods -n "$NAMESPACE"
echo ""
echo "Services status:"
kubectl get services -n "$NAMESPACE"
echo ""
echo "HPA status:"
kubectl get hpa -n "$NAMESPACE" || echo "No HPA configured"

