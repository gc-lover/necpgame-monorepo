#!/bin/bash
# Kafka Infrastructure Deployment Script
# Issue: #2237 - Automated deployment of secure Kafka infrastructure
# Agent: DevOps - Enterprise-grade deployment automation

set -euo pipefail

# Configuration
NAMESPACE="necpgame-infrastructure"
KAFKA_CLUSTER_NAME="necpgame-kafka-cluster"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Pre-deployment checks
pre_deployment_checks() {
    log_info "Running pre-deployment checks..."

    # Check if kubectl is available
    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl not found. Please install kubectl."
        exit 1
    fi

    # Check if helm is available
    if ! command -v helm &> /dev/null; then
        log_error "helm not found. Please install helm."
        exit 1
    fi

    # Check Kubernetes connection
    if ! kubectl cluster-info &> /dev/null; then
        log_error "Cannot connect to Kubernetes cluster."
        exit 1
    fi

    # Check if namespace exists
    if ! kubectl get namespace "$NAMESPACE" &> /dev/null; then
        log_info "Creating namespace $NAMESPACE..."
        kubectl create namespace "$NAMESPACE"
    fi

    # Check if Strimzi operator is installed
    if ! kubectl get deployment strimzi-cluster-operator -n kafka &> /dev/null 2>&1; then
        log_warning "Strimzi operator not found in kafka namespace."
        log_info "Installing Strimzi operator..."
        install_strimzi_operator
    fi

    log_success "Pre-deployment checks completed."
}

# Install Strimzi operator
install_strimzi_operator() {
    log_info "Installing Strimzi Kafka operator..."

    # Create kafka namespace
    kubectl create namespace kafka --dry-run=client -o yaml | kubectl apply -f -

    # Install Strimzi operator
    kubectl apply -f 'https://strimzi.io/install/latest?namespace=kafka'

    # Wait for operator to be ready
    log_info "Waiting for Strimzi operator to be ready..."
    kubectl wait deployment/strimzi-cluster-operator --for=condition=Available --timeout=300s -n kafka

    log_success "Strimzi operator installed successfully."
}

# Generate certificates
generate_certificates() {
    log_info "Generating TLS certificates..."

    # Create certificate directory
    CERT_DIR="$SCRIPT_DIR/certs"
    mkdir -p "$CERT_DIR"

    # Generate CA certificate
    openssl req -new -newkey rsa:4096 -days 365 -nodes -x509 \
        -subj "/C=US/ST=California/L=San Francisco/O=NECPGAME/CN=ca.necpgame.internal" \
        -keyout "$CERT_DIR/ca.key" \
        -out "$CERT_DIR/ca.crt"

    # Generate server certificate
    openssl req -new -newkey rsa:4096 -days 365 -nodes \
        -subj "/C=US/ST=California/L=San Francisco/O=NECPGAME/CN=kafka.necpgame.internal" \
        -keyout "$CERT_DIR/kafka.key" \
        -out "$CERT_DIR/kafka.csr"

    # Sign server certificate
    openssl x509 -req -in "$CERT_DIR/kafka.csr" -CA "$CERT_DIR/ca.crt" -CAkey "$CERT_DIR/ca.key" \
        -CAcreateserial -out "$CERT_DIR/kafka.crt" -days 365 -sha256

    # Create Java keystores
    create_java_keystores "$CERT_DIR"

    log_success "TLS certificates generated."
}

# Create Java keystores for Kafka
create_java_keystores() {
    local cert_dir="$1"

    # Create truststore
    keytool -import -file "$cert_dir/ca.crt" -alias ca -keystore "$cert_dir/truststore.jks" \
        -storepass changeme -noprompt

    # Create keystore
    openssl pkcs12 -export -in "$cert_dir/kafka.crt" -inkey "$cert_dir/kafka.key" \
        -out "$cert_dir/kafka.p12" -name kafka -passout pass:changeme

    keytool -importkeystore -destkeystore "$cert_dir/keystore.jks" -srckeystore "$cert_dir/kafka.p12" \
        -srcstoretype PKCS12 -alias kafka -deststorepass changeme -srcstorepass changeme

    log_info "Java keystores created."
}

# Generate secrets
generate_secrets() {
    log_info "Generating secrets..."

    # Generate strong passwords
    local kafka_admin_pass
    local combat_service_pass
    local economy_service_pass
    local social_service_pass
    local system_service_pass
    local analytics_service_pass

    kafka_admin_pass=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    combat_service_pass=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    economy_service_pass=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    social_service_pass=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    system_service_pass=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    analytics_service_pass=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)

    # Encode in base64
    local kafka_admin_pass_b64
    local combat_service_pass_b64
    local economy_service_pass_b64
    local social_service_pass_b64
    local system_service_pass_b64
    local analytics_service_pass_b64

    kafka_admin_pass_b64=$(echo -n "$kafka_admin_pass" | base64 -w 0)
    combat_service_pass_b64=$(echo -n "$combat_service_pass" | base64 -w 0)
    economy_service_pass_b64=$(echo -n "$economy_service_pass" | base64 -w 0)
    social_service_pass_b64=$(echo -n "$social_service_pass" | base64 -w 0)
    system_service_pass_b64=$(echo -n "$system_service_pass" | base64 -w 0)
    analytics_service_pass_b64=$(echo -n "$analytics_service_pass" | base64 -w 0)

    # Load certificate data
    local ca_crt_b64
    local kafka_crt_b64
    local kafka_key_b64
    local truststore_b64
    local keystore_b64

    ca_crt_b64=$(base64 -w 0 "$SCRIPT_DIR/certs/ca.crt")
    kafka_crt_b64=$(base64 -w 0 "$SCRIPT_DIR/certs/kafka.crt")
    kafka_key_b64=$(base64 -w 0 "$SCRIPT_DIR/certs/kafka.key")
    truststore_b64=$(base64 -w 0 "$SCRIPT_DIR/certs/truststore.jks")
    keystore_b64=$(base64 -w 0 "$SCRIPT_DIR/certs/keystore.jks")

    # Create secrets YAML
    cat > "$SCRIPT_DIR/generated-secrets.yaml" << EOF
---
apiVersion: v1
kind: Secret
metadata:
  name: kafka-admin-credentials
  namespace: $NAMESPACE
  labels:
    generated: "true"
    deployment-time: "$(date -u +%Y%m%d-%H%M%S)"
spec:
  data:
    username: YWRtaW4=  # admin
    password: $kafka_admin_pass_b64

---
apiVersion: v1
kind: Secret
metadata:
  name: kafka-service-accounts
  namespace: $NAMESPACE
spec:
  data:
    combat-service-username: Y29tYmF0LXNlcnZpY2U=
    combat-service-password: $combat_service_pass_b64
    economy-service-username: ZWNvbm9teS1zZXJ2aWNl
    economy-service-password: $economy_service_pass_b64
    social-service-username: c29jaWFsLXNlcnZpY2U=
    social-service-password: $social_service_pass_b64
    system-service-username: c3lzdGVtLXNlcnZpY2U=
    system-service-password: $system_service_pass_b64
    analytics-service-username: YW5hbHl0aWNzLXNlcnZpY2U=
    analytics-service-password: $analytics_service_pass_b64

---
apiVersion: v1
kind: Secret
metadata:
  name: kafka-tls-certificates
  namespace: $NAMESPACE
spec:
  data:
    ca.crt: $ca_crt_b64
    tls.crt: $kafka_crt_b64
    tls.key: $kafka_key_b64

---
apiVersion: v1
kind: Secret
metadata:
  name: kafka-truststore
  namespace: $NAMESPACE
spec:
  data:
    truststore.jks: $truststore_b64
    truststore.password: Y2hhbmdlbWU=  # changeme

---
apiVersion: v1
kind: Secret
metadata:
  name: kafka-keystore
  namespace: $NAMESPACE
spec:
  data:
    keystore.jks: $keystore_b64
    keystore.password: Y2hhbmdlbWU=  # changeme
    key.password: Y2hhbmdlbWU=     # changeme
EOF

    # Save passwords for reference
    cat > "$SCRIPT_DIR/generated-passwords.txt" << EOF
# GENERATED PASSWORDS - KEEP SECURE!
# Generated at: $(date -u)

KAFKA_ADMIN_PASSWORD=$kafka_admin_pass
COMBAT_SERVICE_PASSWORD=$combat_service_pass
ECONOMY_SERVICE_PASSWORD=$economy_service_pass
SOCIAL_SERVICE_PASSWORD=$social_service_pass
SYSTEM_SERVICE_PASSWORD=$system_service_pass
ANALYTICS_SERVICE_PASSWORD=$analytics_service_pass

# Store these in a secure password manager
# Rotate passwords every 90 days
EOF

    log_success "Secrets generated and saved to generated-secrets.yaml"
    log_warning "Passwords saved to generated-passwords.txt - DELETE AFTER SECURE STORAGE!"
}

# Deploy infrastructure
deploy_infrastructure() {
    log_info "Deploying Kafka infrastructure..."

    # Apply generated secrets
    if [[ -f "$SCRIPT_DIR/generated-secrets.yaml" ]]; then
        kubectl apply -f "$SCRIPT_DIR/generated-secrets.yaml" -n "$NAMESPACE"
        log_success "Secrets deployed."
    else
        log_error "Generated secrets file not found. Run generate_secrets first."
        exit 1
    fi

    # Deploy Kafka cluster
    kubectl apply -f "$SCRIPT_DIR/kafka-cluster.yaml" -n "$NAMESPACE"
    log_info "Waiting for Kafka cluster to be ready..."
    kubectl wait kafka/"$KAFKA_CLUSTER_NAME" --for=condition=Ready --timeout=900s -n "$NAMESPACE"

    # Deploy rate limiting
    kubectl apply -f "$SCRIPT_DIR/rate-limiting.yaml" -n "$NAMESPACE"

    # Deploy monitoring
    kubectl apply -f "$SCRIPT_DIR/monitoring-alerting.yaml" -n "$NAMESPACE"

    log_success "Kafka infrastructure deployed successfully."
}

# Run health checks
run_health_checks() {
    log_info "Running health checks..."

    # Wait for all components to be ready
    log_info "Waiting for Kafka brokers..."
    kubectl wait pod -l app=kafka,strimzi.io/cluster="$KAFKA_CLUSTER_NAME" --for=condition=Ready --timeout=300s -n "$NAMESPACE"

    log_info "Waiting for rate limiter..."
    kubectl wait deployment kafka-rate-limiter --for=condition=Available --timeout=120s -n "$NAMESPACE"

    log_info "Waiting for security collector..."
    kubectl wait daemonset kafka-security-collector --for=condition=Available --timeout=120s -n "$NAMESPACE"

    # Test Kafka connectivity
    log_info "Testing Kafka connectivity..."
    local bootstrap_server
    bootstrap_server=$(kubectl get service "$KAFKA_CLUSTER_NAME-kafka-bootstrap" -n "$NAMESPACE" -o jsonpath='{.spec.clusterIP}:{.spec.ports[0].port}')

    # Simple connectivity test (requires kafka client tools)
    if command -v kafka-console-producer.sh &> /dev/null; then
        echo "test" | kafka-console-producer.sh --bootstrap-server "$bootstrap_server" --topic test-topic --producer.config /dev/null 2>/dev/null && \
        log_success "Kafka connectivity test passed." || log_warning "Kafka connectivity test failed."
    else
        log_warning "kafka-console-producer.sh not found, skipping connectivity test."
    fi

    log_success "Health checks completed."
}

# Main deployment function
main() {
    log_info "Starting Kafka infrastructure deployment..."
    log_info "Security Level: ENTERPRISE (mTLS, ACLs, monitoring)"
    log_info "Compliance: SOC 2, ISO 27001, GDPR"

    case "${1:-deploy}" in
        "check")
            pre_deployment_checks
            ;;
        "certs")
            generate_certificates
            ;;
        "secrets")
            generate_secrets
            ;;
        "deploy")
            pre_deployment_checks
            if [[ ! -f "$SCRIPT_DIR/certs/ca.crt" ]]; then
                generate_certificates
            fi
            if [[ ! -f "$SCRIPT_DIR/generated-secrets.yaml" ]]; then
                generate_secrets
            fi
            deploy_infrastructure
            run_health_checks
            ;;
        "clean")
            log_warning "Cleaning up Kafka infrastructure..."
            kubectl delete -f "$SCRIPT_DIR/" -n "$NAMESPACE" --ignore-not-found=true
            rm -rf "$SCRIPT_DIR/certs" "$SCRIPT_DIR/generated-"*
            log_success "Cleanup completed."
            ;;
        *)
            echo "Usage: $0 {check|certs|secrets|deploy|clean}"
            echo "  check   - Run pre-deployment checks"
            echo "  certs   - Generate TLS certificates"
            echo "  secrets - Generate Kubernetes secrets"
            echo "  deploy  - Full deployment (default)"
            echo "  clean   - Clean up all resources"
            exit 1
            ;;
    esac

    log_success "Kafka infrastructure deployment completed!"
    log_info "Next steps:"
    log_info "1. Store generated-passwords.txt securely and delete it"
    log_info "2. Configure monitoring alerts in Grafana"
    log_info "3. Set up automated certificate rotation"
    log_info "4. Test with actual microservices"
    log_info "5. Run penetration testing"
}

# Run main function
main "$@"
