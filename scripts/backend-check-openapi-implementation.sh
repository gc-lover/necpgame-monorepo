#!/bin/bash
# Backend Check OpenAPI Implementation - Validation Command
# Issue: #146050248

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
PROTO_DIR="$PROJECT_ROOT/proto/openapi"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check Python
if ! command -v python3 &> /dev/null; then
    echo -e "${RED}Error: python3 is required${NC}"
    exit 1
fi

# Check spectral
if ! command -v spectral &> /dev/null; then
    echo -e "${YELLOW}Warning: spectral not found, installing...${NC}"
    if command -v npm &> /dev/null; then
        npm install -g @stoplight/spectral-cli
    else
        echo -e "${RED}Error: npm required to install spectral${NC}"
        exit 1
    fi
fi

# Check ogen
if ! command -v ogen &> /dev/null; then
    echo -e "${YELLOW}Warning: ogen not found, installing...${NC}"
    go install github.com/ogen-go/ogen/cmd/ogen@latest
fi

echo -e "${BLUE}🔍 Backend OpenAPI Implementation Validator${NC}"
echo "=============================================="
echo ""

ERRORS=0
WARNINGS=0
SERVICES_CHECKED=0

# Find all OpenAPI specs
find_openapi_specs() {
    find "$PROTO_DIR" -name "*.yaml" -o -name "*.yml" | grep -v "notification-service/main.yaml" | sort
}

# Validate OpenAPI spec
validate_openapi_spec() {
    local spec_file="$1"
    local service_name=$(basename "$spec_file" .yaml)

    echo -e "${BLUE}📋 Validating: $service_name${NC}"
    ((SERVICES_CHECKED++))

    # Check file size (<600 lines)
    local lines=$(wc -l < "$spec_file")
    if [ "$lines" -gt 600 ]; then
        echo -e "  ${RED}❌ File size: $lines lines (exceeds 600 limit)${NC}"
        ((ERRORS++))
        return 1
    fi

    # Spectral linting
    echo -e "  🔍 Running Spectral validation..."
    if ! spectral lint "$spec_file" --ruleset .spectral.yaml 2>/dev/null; then
        echo -e "  ${RED}❌ Spectral validation failed${NC}"
        ((ERRORS++))
        return 1
    fi

    # ogen compatibility check
    echo -e "  🔧 Checking ogen compatibility..."
    if ! ogen validate "$spec_file" 2>/dev/null; then
        echo -e "  ${RED}❌ ogen validation failed${NC}"
        ((ERRORS++))
        return 1
    fi

    # Check if corresponding service exists
    local service_dir="$PROJECT_ROOT/services/${service_name}-go"
    if [ ! -d "$service_dir" ]; then
        echo -e "  ${YELLOW}WARNING  Warning: Service directory not found: $service_dir${NC}"
        ((WARNINGS++))
    else
        # Check if ogen generated files exist
        if [ ! -f "$service_dir/pkg/api/oas_server_gen.go" ]; then
            echo -e "  ${RED}❌ ogen generated files not found${NC}"
            ((ERRORS++))
            return 1
        fi

        # Check if handlers implement the interface
        if ! grep -q "CreateNotification" "$service_dir/server/handlers.go" 2>/dev/null; then
            echo -e "  ${YELLOW}WARNING  Warning: Handler interface may not be fully implemented${NC}"
            ((WARNINGS++))
        fi

        # Check for performance optimizations
        if ! grep -q "context.WithTimeout" "$service_dir/server/service.go" 2>/dev/null; then
            echo -e "  ${YELLOW}WARNING  Warning: Context timeouts not found${NC}"
            ((WARNINGS++))
        fi

        if ! grep -q "sync.Pool" "$service_dir/server/service.go" 2>/dev/null; then
            echo -e "  ${YELLOW}WARNING  Warning: Memory pooling not found${NC}"
            ((WARNINGS++))
        fi
    fi

    echo -e "  ${GREEN}OK $service_name validation passed${NC}"
    echo ""
    return 0
}

# Main validation loop
echo -e "${BLUE}🔍 Scanning OpenAPI specifications...${NC}"
echo ""

for spec_file in $(find_openapi_specs); do
    if ! validate_openapi_spec "$spec_file"; then
        continue
    fi
done

# Summary
echo "=============================================="
echo -e "${BLUE}📊 VALIDATION SUMMARY${NC}"
echo "=============================================="
echo ""
echo -e "Services checked: $SERVICES_CHECKED"
echo -e "${RED}Errors: $ERRORS${NC}"
echo -e "${YELLOW}Warnings: $WARNINGS${NC}"
echo ""

if [ "$ERRORS" -gt 0 ]; then
    echo -e "${RED}❌ VALIDATION FAILED${NC}"
    echo ""
    echo "Fix the following issues:"
    echo "- Ensure OpenAPI specs are valid and <600 lines"
    echo "- Run 'ogen generate' for all services"
    echo "- Implement Handler interfaces completely"
    echo "- Add performance optimizations (context timeouts, memory pooling)"
    echo ""
    exit 1
elif [ "$WARNINGS" -gt 0 ]; then
    echo -e "${YELLOW}WARNING  VALIDATION PASSED with warnings${NC}"
    echo ""
    echo "Consider addressing warnings for better implementation quality"
    exit 0
else
    echo -e "${GREEN}OK ALL VALIDATIONS PASSED${NC}"
    echo ""
    echo "All OpenAPI implementations are ready for production!"
    exit 0
fi