# 🏗️ NECPGAME Architecture Validation

Automated tools for ensuring code quality and architectural compliance across the entire NECPGAME codebase.

## 🎯 Overview

This validation system prevents architectural violations and ensures consistent code quality by automatically checking:

- File size limits (≤600 lines)
- OpenAPI specification validity
- Go code quality patterns
- YAML structure compliance
- Database migration standards
- Security vulnerabilities
- Performance anti-patterns

## 🚀 Quick Start

### Run All Validations
```bash
bash scripts/validate-architecture.sh
```

### Check Specific Areas
```bash
# File sizes only
bash scripts/validate-architecture.sh --check=file-sizes

# OpenAPI specs only
bash scripts/validate-architecture.sh --check=openapi

# Security scan
bash scripts/validate-architecture.sh --check=security
```

## 📋 Validation Results

### OK PASSED
- All checks successful
- Commit allowed
- No action required

### WARNING WARNINGS
- Issues found but not critical
- Commit allowed but review recommended
- Consider fixing for better quality

### ❌ FAILED
- Critical errors found
- Commit blocked
- Must fix before proceeding

## 🔧 Available Checks

### File Size Validation
**Purpose:** Prevents monolithic files that are hard to maintain
**Limit:** 600 lines per file
**Checked files:** `*.yaml`, `*.go`, `*.sql`, `*.md`

**Fix oversized files:**
```bash
# Split OpenAPI specs
mkdir service-name/schemas/
mv schemas from spec.yaml to service-name/schemas/schemas.yaml
# Use $ref to link modules
```

### OpenAPI Validation
**Tool:** Redocly CLI
**Checks:** Syntax, structure, missing fields, circular refs

**Fix validation errors:**
```bash
redocly lint proto/openapi/service.yaml --format=stylish
# Apply suggested fixes
```

### Go Code Quality
**Checks:**
- Context usage (timeouts)
- Panic detection
- Error handling patterns

**Best practices:**
```go
// OK Good: Context with timeout
ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
defer cancel()

// ❌ Bad: No context
result := someFunction()
```

### Security Scanning
**Detects:**
- Hardcoded passwords/secrets
- Insecure patterns
- Token exposure

**Secure alternatives:**
```go
// OK Good: Environment variables
secret := os.Getenv("API_SECRET")

// ❌ Bad: Hardcoded
secret := "my-secret-key"
```

### Performance Analysis
**Detects:**
- Memory allocations (`make()`)
- Inefficient patterns
- Resource leaks

**Optimizations:**
```go
// OK Good: Reuse objects
user := &User{}
userPool.Get(user)

// ❌ Bad: Constant allocations
user := &User{}
```

## 🔄 Integration Points

### Git Hooks (Automatic)
Runs on every commit via `.githooks/pre-commit`
```bash
# Hook prevents commits with critical errors
git commit -m "feat: add new feature"
# OK Validation passed - commit allowed
```

### CI/CD Pipeline (GitHub Actions)
Runs on every push/PR via `.github/workflows/architecture-validation.yml`
```yaml
# Automatically validates PRs
on: [push, pull_request]
jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - run: bash scripts/validate-architecture.sh
```

### Manual Validation (Developers)
Run anytime during development:
```bash
# Before committing
bash scripts/validate-architecture.sh

# During development
watch -n 5 bash scripts/validate-architecture.sh
```

## 📊 Metrics & Reporting

The validation system provides detailed metrics:

```
🔍 Starting NECPGAME Architecture Validation...
==================================================

📏 Checking file sizes...
OK proto/openapi/guild-core-service.yaml has 116 lines (OK)
❌ large-file.yaml has 1200 lines (exceeds by 600)

🔍 Validating OpenAPI specifications...
OK proto/openapi/guild-core-service.yaml validated
❌ proto/openapi/invalid.yaml has 5 validation errors

==================================================
🏁 Architecture Validation Complete

Results:
  Errors: 2
  Warnings: 3
```

## 🛠️ Configuration

### Custom Rules
Edit `scripts/validate-architecture.sh` to add custom validation rules:

```bash
# Add custom check
echo "🔍 Checking custom rules..."
if [ condition ]; then
    log_error "Custom rule violated"
fi
```

### Ignoring Files
Add patterns to skip certain files:

```bash
# Skip generated files
if [[ $file =~ generated ]]; then
    continue
fi
```

## 🆘 Troubleshooting

### "Permission denied" on scripts
```bash
chmod +x scripts/validate-architecture.sh
```

### Redocly not found
```bash
npm install -g @redocly/cli
```

### Git hooks not working
```bash
# Ensure hooks are executable
chmod +x .githooks/pre-commit

# Configure Git to use hooks
git config core.hooksPath .githooks
```

### Validation too slow
```bash
# Run specific checks only
bash scripts/validate-architecture.sh --check=file-sizes
```

## 📈 Benefits

### For Developers
- **Early feedback** on code quality issues
- **Consistent standards** across the team
- **Automated checks** prevent human error
- **Faster reviews** with validated code

### For Architecture
- **Enforced standards** prevent technical debt
- **Scalable validation** grows with codebase
- **Automated compliance** ensures consistency
- **Quality gates** protect production

### For DevOps
- **Shift-left validation** catches issues early
- **CI/CD integration** prevents bad deployments
- **Monitoring capabilities** track code health
- **Tool ecosystem** supports multiple workflows

## 🎯 Next Steps

1. **Customize rules** for your specific needs
2. **Add team-specific checks** (naming conventions, etc.)
3. **Integrate with IDEs** for real-time feedback
4. **Extend to other languages** (C++, Python, etc.)
5. **Add performance benchmarks** for critical paths

---

**Questions?** See `.cursor/commands/devops-validate-architecture.md` for detailed command reference.