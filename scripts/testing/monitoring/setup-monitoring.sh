#!/bin/bash

# Monitoring Setup Script for ogen Services
# Issue: #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework

set -e

MONITORING_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$MONITORING_DIR/../../../.." && pwd)"

echo "========================================"
echo "OGEN SERVICES MONITORING SETUP"
echo "========================================"
echo "Monitoring Dir: $MONITORING_DIR"
echo "Project Root: $PROJECT_ROOT"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Docker is available
if ! command -v docker &> /dev/null; then
    echo -e "${RED}ERROR: Docker is not installed or not in PATH${NC}"
    echo "Please install Docker to use monitoring stack."
    exit 1
fi

# Check if Docker Compose is available
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}ERROR: Docker Compose is not installed or not in PATH${NC}"
    echo "Please install Docker Compose to use monitoring stack."
    exit 1
fi

echo -e "${BLUE}Setting up monitoring stack...${NC}"

# Create monitoring directories
MONITORING_DATA_DIR="$PROJECT_ROOT/monitoring/data"
PROMETHEUS_DATA_DIR="$MONITORING_DATA_DIR/prometheus"
GRAFANA_DATA_DIR="$MONITORING_DATA_DIR/grafana"

mkdir -p "$PROMETHEUS_DATA_DIR"
mkdir -p "$GRAFANA_DATA_DIR"

echo "Created data directories:"
echo "  - $PROMETHEUS_DATA_DIR"
echo "  - $GRAFANA_DATA_DIR"

# Create docker-compose.yml for monitoring stack
cat > "$PROJECT_ROOT/docker-compose.monitoring.yml" << 'EOF'
version: '3.8'

services:
  prometheus:
    image: prom/prometheus:latest
    container_name: ogen-prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./scripts/testing/monitoring/prometheus-config.yml:/etc/prometheus/prometheus.yml
      - ./monitoring/data/prometheus:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--storage.tsdb.retention.time=200h'
      - '--web.enable-lifecycle'
    networks:
      - ogen-monitoring

  alertmanager:
    image: prom/alertmanager:latest
    container_name: ogen-alertmanager
    ports:
      - "9093:9093"
    volumes:
      - ./scripts/testing/monitoring/alertmanager.yml:/etc/alertmanager/config.yml
    networks:
      - ogen-monitoring

  grafana:
    image: grafana/grafana:latest
    container_name: ogen-grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
      - GF_USERS_ALLOW_SIGN_UP=false
    volumes:
      - ./monitoring/data/grafana:/var/lib/grafana
      - ./scripts/testing/monitoring/grafana-dashboard.json:/etc/grafana/provisioning/dashboards/ogen-dashboard.json
      - ./scripts/testing/monitoring/grafana-datasource.yml:/etc/grafana/provisioning/datasources/prometheus.yml
      - ./scripts/testing/monitoring/grafana-dashboard-provider.yml:/etc/grafana/provisioning/dashboards/dashboard-provider.yml
    networks:
      - ogen-monitoring

  node-exporter:
    image: prom/node-exporter:latest
    container_name: ogen-node-exporter
    ports:
      - "9100:9100"
    volumes:
      - /proc:/host/proc:ro
      - /sys:/host/sys:ro
      - /:/rootfs:ro
    command:
      - '--path.procfs=/host/proc'
      - '--path.rootfs=/rootfs'
      - '--path.sysfs=/host/sys'
      - '--collector.filesystem.mount-points-exclude=^/(sys|proc|dev|host|etc)($$|/)'
    networks:
      - ogen-monitoring

networks:
  ogen-monitoring:
    driver: bridge
EOF

echo "Created docker-compose.monitoring.yml"

# Create Alertmanager configuration
cat > "$MONITORING_DIR/alertmanager.yml" << 'EOF'
global:
  smtp_smarthost: 'localhost:587'
  smtp_from: 'alertmanager@necpgame.com'

route:
  group_by: ['alertname']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'email-notifications'
  routes:
  - match:
      severity: critical
    receiver: 'email-critical'

receivers:
- name: 'email-notifications'
  email_configs:
  - to: 'devops@necpgame.com'
    send_resolved: true

- name: 'email-critical'
  email_configs:
  - to: 'devops@necpgame.com,management@necpgame.com'
    send_resolved: true
EOF

echo "Created alertmanager.yml"

# Create Grafana datasource configuration
cat > "$MONITORING_DIR/grafana-datasource.yml" << 'EOF'
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true
    editable: true
EOF

echo "Created grafana-datasource.yml"

# Create Grafana dashboard provider configuration
cat > "$MONITORING_DIR/grafana-dashboard-provider.yml" << 'EOF'
apiVersion: 1

providers:
  - name: 'ogen'
    type: file
    disableDeletion: false
    updateIntervalSeconds: 10
    allowUiUpdates: true
    options:
      path: /etc/grafana/provisioning/dashboards
EOF

echo "Created grafana-dashboard-provider.yml"

echo ""
echo -e "${GREEN}✓ Monitoring configuration files created${NC}"

# Start monitoring stack
echo ""
echo -e "${BLUE}Starting monitoring stack...${NC}"

cd "$PROJECT_ROOT"
docker-compose -f docker-compose.monitoring.yml up -d

echo -e "${GREEN}✓ Monitoring stack started${NC}"

# Wait for services to be ready
echo ""
echo "Waiting for services to be ready..."
sleep 10

# Check if services are running
echo ""
echo "Checking service status..."

if curl -f -s http://localhost:9090/-/ready > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Prometheus is ready at http://localhost:9090${NC}"
else
    echo -e "${RED}✗ Prometheus is not ready${NC}"
fi

if curl -f -s http://localhost:3000/api/health > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Grafana is ready at http://localhost:3000 (admin/admin)${NC}"
else
    echo -e "${RED}✗ Grafana is not ready${NC}"
fi

if curl -f -s http://localhost:9093/-/ready > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Alertmanager is ready at http://localhost:9093${NC}"
else
    echo -e "${RED}✗ Alertmanager is not ready${NC}"
fi

echo ""
echo "========================================"
echo "MONITORING SETUP COMPLETE"
echo "========================================"
echo ""
echo "Access URLs:"
echo "  - Prometheus:    http://localhost:9090"
echo "  - Grafana:       http://localhost:3000 (admin/admin)"
echo "  - Alertmanager:  http://localhost:9093"
echo ""
echo "To add ogen service metrics to Prometheus:"
echo "1. Make sure your ogen services expose /metrics endpoint"
echo "2. Add service targets to prometheus-config.yml"
echo "3. Reload Prometheus configuration"
echo ""
echo "To stop monitoring stack:"
echo "  docker-compose -f docker-compose.monitoring.yml down"
echo ""
echo "To view logs:"
echo "  docker-compose -f docker-compose.monitoring.yml logs -f"






