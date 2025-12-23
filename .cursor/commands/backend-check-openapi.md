# Backend Check OpenAPI — Validation Command

# Issue: #1878

**Purpose:** Validate OpenAPI specifications before backend development to ensure:

— Schema compliance and required fields

- Security definitions completeness
- ogen compatibility for code generation
- Project standards adherence
- Enterprise-grade domain structure
- Performance optimizations applied

## Usage

```bash
# Validate all OpenAPI specs in project
./scripts/backend-check-openapi.sh

# Validate specific service spec
./scripts/backend-check-openapi.sh proto/openapi/combat-service.yaml

# Validate service directory (modular specs)
./scripts/backend-check-openapi.sh proto/openapi/combat-service/
```

## Validation Steps

### 1. Enterprise-Grade Domain Check

```bash
# Validate domain structure and enterprise-grade compliance
python scripts/validate-domains-openapi.py proto/openapi/your-domain/main.yaml

# Check if optimizations are applied
python scripts/batch-optimize-openapi-struct-alignment.py proto/openapi/your-domain/main.yaml --dry-run

# Update TYPE and CHECK fields in GitHub project
# TYPE: API (since we're working with OpenAPI)
# CHECK: 1 (validated and ready)
```

### 2. Spectral Linting

```bash
# Check OpenAPI compliance with Spectral
if ! command -v spectral >/dev/null 2>&1; then
    echo "[ERROR] ERROR: spectral CLI not found"
    echo "Install with: npm install -g @stoplight/spectral-cli"
    exit 1
fi

# Validate against project ruleset
spectral lint "$spec_file" --ruleset .spectral.yaml
```

### 2. ogen Compatibility Check

```bash
# Check if ogen can generate code from spec
if command -v ogen >/dev/null 2>&1; then
    echo "[SYMBOL] Checking ogen compatibility..."
    if ! ogen validate "$spec_file"; then
        echo "[ERROR] ERROR: ogen validation failed"
        echo "Fix OpenAPI spec for ogen compatibility"
        exit 1
    fi
else
    echo "[WARNING] WARNING: ogen not installed, skipping compatibility check"
fi
```

### 3. Schema Validation

- [OK] Required fields present (title, version, paths)
- [OK] Security schemes defined
- [OK] Response schemas complete
- [OK] Parameter definitions valid

### 4. Project Standards Check

- [OK] File size <1000 lines (or properly modularized)
- [OK] Consistent naming conventions
- [OK] Error response schemas standardized
- [OK] Pagination patterns followed

## Implementation Script

Create `scripts/backend-check-openapi.sh`:

```bash
#!/bin/bash
# Backend OpenAPI Validation Script
# Issue: #1878

set -e

SPEC_PATH=${1:-"proto/openapi"}

echo "[SEARCH] Validating OpenAPI specifications..."
echo "=========================================="
echo ""

ERRORS=0
WARNINGS=0

# Find all OpenAPI files
find_openapi_files() {
    if [ -f "$SPEC_PATH" ]; then
        echo "$SPEC_PATH"
    elif [ -d "$SPEC_PATH" ]; then
        find "$SPEC_PATH" -name "*.yaml" -o -name "*.yml" | sort
    else
        echo "[ERROR] ERROR: Path not found: $SPEC_PATH"
        exit 1
    fi
}

# Validate single OpenAPI file
validate_openapi_file() {
    local file="$1"
    echo "[SYMBOL] Validating: $file"

    # Check file size
    local lines=$(wc -l < "$file")
    if [ "$lines" -gt 500 ]; then
        echo "[WARNING]  WARNING: Spec exceeds 1000 lines ($lines lines)"
        echo "   Consider splitting into modules"
        WARNINGS=$((WARNINGS + 1))
    fi

    # Spectral validation
    if command -v spectral >/dev/null 2>&1; then
        echo "  [SEARCH] Running Spectral validation..."
        if ! spectral lint "$file" --ruleset .spectral.yaml 2>/dev/null; then
            echo "[ERROR] ERROR: Spectral validation failed"
            spectral lint "$file" --ruleset .spectral.yaml || true
            ERRORS=$((ERRORS + 1))
            return 1
        fi
    else
        echo "[WARNING]  WARNING: Spectral not installed, skipping validation"
        WARNINGS=$((WARNINGS + 1))
    fi

    # ogen compatibility check
    if command -v ogen >/dev/null 2>&1; then
        echo "  [SYMBOL] Checking ogen compatibility..."
        if ! ogen validate "$file" 2>/dev/null; then
            echo "[ERROR] ERROR: ogen validation failed"
            ogen validate "$file" || true
            ERRORS=$((ERRORS + 1))
            return 1
        fi
    else
        echo "[WARNING]  WARNING: ogen not installed, skipping compatibility check"
        WARNINGS=$((WARNINGS + 1))
    fi

    echo "[OK] $file validation passed"
    echo ""
}

# Main validation loop
for file in $(find_openapi_files); do
    if ! validate_openapi_file "$file"; then
        continue
    fi
done

# Summary
echo "=========================================="
echo "[SYMBOL] VALIDATION SUMMARY"
echo "=========================================="
echo ""
echo "[ERROR] Errors: $ERRORS"
echo "[WARNING]  Warnings: $WARNINGS"
echo ""

if [ "$ERRORS" -gt 0 ]; then
    echo "[ERROR] VALIDATION FAILED"
    echo ""
    echo "Fix errors before proceeding with backend development:"
    echo "- Install spectral: npm install -g @stoplight/spectral-cli"
    echo "- Install ogen: go install github.com/ogen-go/ogen/cmd/ogen@latest"
    echo "- Fix OpenAPI spec issues"
    echo ""
    exit 1
elif [ "$WARNINGS" -gt 0 ]; then
    echo "[WARNING]  VALIDATION PASSED with warnings"
    echo ""
    echo "Consider addressing warnings for better code generation"
    exit 0
else
    echo "[OK] VALIDATION PASSED"
    echo ""
    echo "OpenAPI specs ready for backend development!"
    exit 0
fi
```

## Spectral Ruleset (.spectral.yaml)

Create project-specific Spectral rules:

```yaml
extends: ["spectral:oas", "spectral:asyncapi"]

rules:
  # Required fields
  info-title: error
  info-version: error
  paths-defined: error

  # Security requirements
  security-defined: warning
  security-scheme-defined: error

  # Response schemas
  response-schema-defined: error
  success-response-defined: error

  # Project standards
  operation-id-camel-case: warning
  parameter-description: warning
  schema-description: warning

  # NECPGAME specific rules
  necpgame-pagination-pattern:
    description: "Ensure pagination follows project pattern"
    given: "$.paths[*][*]"
    then:
      - field: parameters
        function: schema
        functionOptions:
          schema:
            type: array
            contains:
              properties:
                name:
                  enum: ["limit", "offset"]

  necpgame-error-responses:
    description: "Standard error response schemas"
    given: "$.paths[*][*].responses"
    then:
      - field: "400"
        function: truthy
      - field: "500"
        function: truthy
```

## Integration with Backend Workflow

### Pre-commit Hook

```bash
# .git/hooks/pre-commit
#!/bin/bash

# Validate OpenAPI specs before commit
if git diff --cached --name-only | grep -q "proto/openapi/"; then
    echo "[SEARCH] Validating OpenAPI specs..."
    if ! ./scripts/backend-check-openapi.sh; then
        echo "[ERROR] OpenAPI validation failed. Fix issues before commit."
        exit 1
    fi
fi
```

### CI/CD Integration

```yaml
# .github/workflows/backend-validation.yml
name: Backend OpenAPI Validation

on:
  pull_request:
    paths:
      - 'proto/openapi/**'

jobs:
  validate-openapi:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '18'

      - name: Install spectral
        run: npm install -g @stoplight/spectral-cli

      - name: Install ogen
        uses: actions/setup-go@v4
        with:
          go-version: '1.21'
      - run: go install github.com/ogen-go/ogen/cmd/ogen@latest

      - name: Validate OpenAPI specs
        run: ./scripts/backend-check-openapi.sh
```

## Error Handling

### Common Issues & Solutions

**Spectral Errors:**

- Missing required fields → Add to OpenAPI spec
- Invalid schema → Fix JSON Schema definitions
- Security issues → Add security schemes

**ogen Compatibility:**

- Unsupported features → Use ogen-compatible patterns
- Type conflicts → Resolve schema conflicts
- Missing references → Add proper $ref links

**Project Standards:**

- File too large → Split into modules
- Naming inconsistencies → Follow conventions
- Missing pagination → Add standard pagination

## Success Criteria

[OK] **All validations pass:**

- Spectral linting successful
- ogen compatibility confirmed
- Project standards met
- No blocking errors

[WARNING] **Warnings acceptable:**

- Missing optional tools (spectral/ogen)
- File size warnings (<1000 lines)
- Minor style issues

[ERROR] **Blocking errors:**

- Invalid OpenAPI syntax
- ogen incompatibility
- Missing required schemas
- Security definition issues

---

**Issue:** #1878
**Status:** Ready for backend development validation
