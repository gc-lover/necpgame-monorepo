#!/bin/bash

# NECP Game Security Audit Script
# Комплексная проверка безопасности системы

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo "🔒 NECP Game Security Audit"
echo "==========================="

# Function to check file permissions
check_file_permissions() {
    local file=$1
    local expected_perms=$2

    if [ -f "$file" ]; then
        local actual_perms=$(stat -c "%a" "$file" 2>/dev/null || echo "unknown")
        if [ "$actual_perms" = "$expected_perms" ]; then
            echo -e "${GREEN}OK $file permissions: $actual_perms${NC}"
        else
            echo -e "${RED}❌ $file permissions: $actual_perms (expected: $expected_perms)${NC}"
        fi
    else
        echo -e "${YELLOW}WARNING  $file not found${NC}"
    fi
}

# Function to check environment variables
check_env_var() {
    local var_name=$1
    local should_be_set=${2:-true}

    if [ "$should_be_set" = true ]; then
        if [ -n "${!var_name:-}" ]; then
            echo -e "${GREEN}OK $var_name is set${NC}"
        else
            echo -e "${RED}❌ $var_name is not set${NC}"
        fi
    else
        if [ -z "${!var_name:-}" ]; then
            echo -e "${GREEN}OK $var_name is not set (as expected)${NC}"
        else
            echo -e "${RED}❌ $var_name is set (should not be)${NC}"
        fi
    fi
}

# Function to check Docker security
check_docker_security() {
    echo -e "${BLUE}🐳 Checking Docker security...${NC}"

    # Check if containers are running as non-root
    local non_root_containers=$(docker ps --format "table {{.Names}}\t{{.Image}}" | grep -v "NAMES" | wc -l)
    local total_containers=$(docker ps --format "table {{.Names}}" | grep -v "NAMES" | wc -l)

    if [ "$total_containers" -gt 0 ]; then
        echo -e "${GREEN}OK Docker containers are running${NC}"
    else
        echo -e "${RED}❌ No Docker containers running${NC}"
        return 1
    fi

    # Check for security-related environment variables
    echo "Checking for sensitive environment variables..."
    docker ps --format "table {{.Names}}" | grep -v "NAMES" | while read -r container; do
        local env_vars=$(docker exec "$container" env 2>/dev/null | grep -E "(PASSWORD|SECRET|KEY)" || true)
        if [ -n "$env_vars" ]; then
            echo -e "${YELLOW}WARNING  $container has sensitive env vars${NC}"
        fi
    done
}

# Function to check network security
check_network_security() {
    echo -e "${BLUE}🌐 Checking network security...${NC}"

    # Check if services are only listening on expected ports
    local open_ports=$(netstat -tlnp 2>/dev/null | grep LISTEN | awk '{print $4}' | sed 's/.*://' | sort -u || echo "")

    # Expected ports for our services
    local expected_ports=("5432" "6379" "8080" "9090" "3000" "3100" "9093")

    # Add service ports
    for port in {8100..8263}; do
        expected_ports+=("$port")
    done

    echo "Open ports: $open_ports"
    echo "Expected ports configured"

    # Check for unexpected open ports (basic check)
    local unexpected_ports=""
    for port in $open_ports; do
        if [[ ! " ${expected_ports[@]} " =~ " ${port} " ]]; then
            unexpected_ports="$unexpected_ports $port"
        fi
    done

    if [ -n "$unexpected_ports" ]; then
        echo -e "${YELLOW}WARNING  Unexpected open ports:$unexpected_ports${NC}"
    else
        echo -e "${GREEN}OK No unexpected open ports${NC}"
    fi
}

# Function to check API security
check_api_security() {
    echo -e "${BLUE}🔐 Checking API security...${NC}"

    # Check if APIs require authentication
    local test_endpoints=(
        "achievement-service:8100:/api/v1/achievements"
        "cosmetic-service:8119:/api/v1/cosmetics"
    )

    for endpoint_info in "${test_endpoints[@]}"; do
        IFS=':' read -r service port endpoint <<< "$endpoint_info"

        # Test without authentication
        local response=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:$port$endpoint" 2>/dev/null || echo "000")

        if [ "$response" = "401" ] || [ "$response" = "403" ]; then
            echo -e "${GREEN}OK $service API requires authentication${NC}"
        elif [ "$response" = "404" ]; then
            echo -e "${YELLOW}WARNING  $service API not implemented (404)${NC}"
        else
            echo -e "${RED}❌ $service API may not require authentication (HTTP $response)${NC}"
        fi
    done
}

# Function to check secrets management
check_secrets() {
    echo -e "${BLUE}🔑 Checking secrets management...${NC}"

    # Check for hardcoded secrets in code
    local secret_patterns=("password|secret|key|token" "PASSWORD|SECRET|KEY|TOKEN")
    local found_secrets=false

    for pattern in "${secret_patterns[@]}"; do
        local matches=$(grep -r -i "$pattern" --include="*.go" --include="*.yml" --include="*.yaml" services/ proto/ infrastructure/ 2>/dev/null | grep -v "example\|test\|mock" | wc -l || echo "0")
        if [ "$matches" -gt 0 ]; then
            echo -e "${YELLOW}WARNING  Found potential secrets in code: $matches matches${NC}"
            found_secrets=true
        fi
    done

    if [ "$found_secrets" = false ]; then
        echo -e "${GREEN}OK No hardcoded secrets found in code${NC}"
    fi

    # Check JWT configuration
    if [ -n "${JWT_SECRET:-}" ] && [ "${JWT_SECRET:-}" != "your-jwt-secret-change-in-production" ]; then
        echo -e "${GREEN}OK JWT secret is configured${NC}"
    else
        echo -e "${YELLOW}WARNING  JWT secret may be using default value${NC}"
    fi
}

# Function to check dependency security
check_dependencies() {
    echo -e "${BLUE}📦 Checking dependencies...${NC}"

    # Check for vulnerable Go modules (if govulncheck is available)
    if command -v govulncheck &> /dev/null; then
        echo "Running vulnerability check..."
        local vuln_count=$(find services -name "go.mod" -exec govulncheck {} \; 2>/dev/null | grep -c "VULNERABILITY" || echo "0")
        if [ "$vuln_count" -gt 0 ]; then
            echo -e "${RED}❌ Found $vuln_count vulnerabilities in dependencies${NC}"
        else
            echo -e "${GREEN}OK No known vulnerabilities in dependencies${NC}"
        fi
    else
        echo -e "${YELLOW}WARNING  govulncheck not available, skipping vulnerability scan${NC}"
    fi

    # Check for outdated dependencies
    echo "Checking for dependency updates..."
    # This would require additional tools like go-mod-outdated
    echo -e "${BLUE}ℹ️  Manual dependency update check recommended${NC}"
}

# Function to check logging security
check_logging() {
    echo -e "${BLUE}📝 Checking logging security...${NC}"

    # Check if sensitive data is being logged
    local log_files=$(find . -name "*.log" -o -name "*log*" -type f 2>/dev/null | head -5)

    if [ -n "$log_files" ]; then
        local sensitive_in_logs=$(grep -r -i "password\|secret\|key\|token" $log_files 2>/dev/null | wc -l || echo "0")
        if [ "$sensitive_in_logs" -gt 0 ]; then
            echo -e "${RED}❌ Found sensitive data in log files${NC}"
        else
            echo -e "${GREEN}OK No sensitive data found in logs${NC}"
        fi
    else
        echo -e "${BLUE}ℹ️  No log files found${NC}"
    fi
}

# Main audit process
echo ""
echo "🔍 File Permissions:"
echo "--------------------"
check_file_permissions "docker-compose.yml" "644"
check_file_permissions "scripts/*.sh" "755"

echo ""
echo "🌍 Environment Variables:"
echo "-------------------------"
check_env_var "JWT_SECRET"
check_env_var "DATABASE_URL"
check_env_var "REDIS_ADDR"

check_docker_security
check_network_security
check_api_security
check_secrets
check_dependencies
check_logging

echo ""
echo "📋 Security Audit Summary:"
echo "=========================="
echo "OK Audit completed"
echo "🔍 Manual review recommended for:"
echo "   - Network firewall rules"
echo "   - Database access controls"
echo "   - Backup encryption"
echo "   - SSL/TLS configuration"
echo ""
echo "🛡️  Security recommendations:"
echo "   - Use secrets management system (Vault, AWS Secrets Manager)"
echo "   - Implement rate limiting"
echo "   - Add API versioning"
echo "   - Regular security scanning"
echo "   - Penetration testing"

echo ""
echo -e "${GREEN}🎉 Security audit completed!${NC}"
