#!/bin/bash
# Check deployment status
# Usage: ./check-status.sh [namespace]

set -e

NAMESPACE="${1:-necpgame}"

echo "========================================="
echo "NECPGAME - Deployment Status"
echo "========================================="
echo "Namespace: $NAMESPACE"
echo "========================================="

echo ""
echo "Pods:"
kubectl get pods -n "$NAMESPACE" -o wide

echo ""
echo "Services:"
kubectl get services -n "$NAMESPACE"

echo ""
echo "Deployments:"
kubectl get deployments -n "$NAMESPACE"

echo ""
echo "HPA:"
kubectl get hpa -n "$NAMESPACE" 2>/dev/null || echo "No HPA configured"

echo ""
echo "PDB:"
kubectl get pdb -n "$NAMESPACE" 2>/dev/null || echo "No PDB configured"

echo ""
echo "Ingress:"
kubectl get ingress -n "$NAMESPACE" 2>/dev/null || echo "No Ingress configured"

echo ""
echo "Events (last 10):"
kubectl get events -n "$NAMESPACE" --sort-by='.lastTimestamp' | tail -10

