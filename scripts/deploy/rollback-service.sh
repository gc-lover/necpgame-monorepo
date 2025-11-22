#!/bin/bash
# Rollback a service deployment
# Usage: ./rollback-service.sh <service-name> [namespace]

set -e

if [ -z "$1" ]; then
    echo "Usage: $0 <service-name> [namespace]"
    echo "Example: $0 character-service-go"
    exit 1
fi

SERVICE="$1"
NAMESPACE="${2:-necpgame}"

echo "========================================="
echo "NECPGAME - Rollback Service"
echo "========================================="
echo "Service: $SERVICE"
echo "Namespace: $NAMESPACE"
echo "========================================="

# Check if deployment exists
if ! kubectl get deployment "$SERVICE" -n "$NAMESPACE" >/dev/null 2>&1; then
    echo "❌ Deployment $SERVICE not found in namespace $NAMESPACE"
    exit 1
fi

# Show current revision
CURRENT_REVISION=$(kubectl rollout history deployment/"$SERVICE" -n "$NAMESPACE" | tail -1 | awk '{print $1}')
echo "Current revision: $CURRENT_REVISION"

# Rollback
echo ""
echo "Rolling back..."
kubectl rollout undo deployment/"$SERVICE" -n "$NAMESPACE"

# Wait for rollout
echo ""
echo "Waiting for rollout..."
kubectl rollout status deployment/"$SERVICE" -n "$NAMESPACE" --timeout=5m

# Show new revision
NEW_REVISION=$(kubectl rollout history deployment/"$SERVICE" -n "$NAMESPACE" | tail -1 | awk '{print $1}')
echo ""
echo "========================================="
echo "OK Rollback completed!"
echo "========================================="
echo "New revision: $NEW_REVISION"

