# Architecture Validation Tools

## Overview

This document describes the comprehensive architecture validation tools for the NECPGAME project. These tools ensure code quality, architectural consistency, and prevent common mistakes before they reach production.

## Table of Contents

- [Quick Start](#quick-start)
- [File Size Limits](#file-size-limits)
- [Validation Scripts](#validation-scripts)
- [Git Hooks](#git-hooks)
- [CI/CD Integration](#cicd-integration)
- [Troubleshooting](#troubleshooting)

## Quick Start

### 1. Install Git Hooks

```bash
# Install automated validation hooks
./scripts/install-git-hooks.sh

# Verify installation
ls -la .git/hooks/pre-commit .git/hooks/pre-push
```

### 2. Run Validation

```bash
# Simple validation (PowerShell - Windows)
.\scripts\validate-architecture-simple.ps1

# Advanced validation (PowerShell - Windows)
.\scripts\validate-architecture.ps1

# Cross-platform validation (Python)
python scripts/validate-architecture.py
```

### 3. Check Results

```bash
# View validation logs
tail -f validation.log

# Check git status after validation
git status
```

## File Size Limits

### Current Limits

| File Type | Max Lines | Notes |
|-----------|-----------|-------|
| Source Code | 1,500 | Excludes generated files |
| OpenAPI Specs | 1,500 | Includes all components |
| Generated Files | Unlimited | `.gen.go`, bundled specs |
| Documentation | Unlimited | `.md`, `.txt` files |

### Exclusions

The following file patterns are excluded from line limits:

```bash
# Generated files (unlimited)
**/*.gen.go
**/ogen-generated/**/*
**/bundled-openapi/**/*

# Large data files
**/*.sql  # Migration files
**/*.json # Large data exports
**/test-data/**/*

# Documentation
**/*.md
**/*.txt
**/docs/**/*
```

### Configuration

File size limits are configured in `scripts/core/config.py`:

```python
def get_max_file_lines(self) -> int:
    """Get maximum file lines limit"""
    return self.get('code_quality', 'max_file_lines') or 1500
```

## Validation Scripts

### 1. Simple Architecture Validation (`validate-architecture-simple.ps1`)

**Purpose:** Basic architecture compliance checks

**Checks Performed:**
- File size limits
- Forbidden file types
- Basic structural validation
- Cross-platform compatibility

**Usage:**
```powershell
# Basic validation
.\scripts\validate-architecture-simple.ps1

# Validate specific directory
.\scripts\validate-architecture-simple.ps1 -Path "services/"

# Verbose output
.\scripts\validate-architecture-simple.ps1 -Verbose
```

**Output:**
```
[VALIDATION] Starting simple architecture validation...
[INFO] Checking file sizes...
[INFO] Checking forbidden extensions...
[SUCCESS] Architecture validation passed: 0 errors, 0 warnings
```

### 2. Advanced Architecture Validation (`validate-architecture.ps1`)

**Purpose:** Comprehensive architecture analysis

**Checks Performed:**
- All simple validation checks
- Domain separation validation
- Service architecture compliance
- Dependency analysis
- Performance hints validation
- Enterprise-grade patterns

**Usage:**
```powershell
# Full validation
.\scripts\validate-architecture.ps1

# Validate specific service
.\scripts\validate-architecture.ps1 -ServiceName "auth-service"

# Generate report
.\scripts\validate-architecture.ps1 -ReportPath "validation-report.json"
```

**Output:**
```
[VALIDATION] Starting advanced architecture validation...
[INFO] Analyzing domain separation...
[INFO] Checking enterprise patterns...
[WARNING] Missing performance hints in auth-service
[SUCCESS] Architecture validation completed: 0 errors, 1 warning
Report saved: validation-report.json
```

### 3. Cross-Platform Validation (`validate-architecture.py`)

**Purpose:** Platform-independent validation

**Requirements:**
- Python 3.8+
- No external dependencies

**Usage:**
```bash
# Basic validation
python scripts/validate-architecture.py

# Validate specific patterns
python scripts/validate-architecture.py --patterns "*.go" "*.yaml"

# Generate detailed report
python scripts/validate-architecture.py --report validation-report.json
```

**Features:**
- Cross-platform file operations
- JSON/YAML parsing validation
- Architecture pattern matching
- Comprehensive error reporting

## Git Hooks

### Pre-commit Hook

**Location:** `.git/hooks/pre-commit`

**Checks:**
- Git safety (dangerous commands)
- Emoji ban validation
- Secrets scanning
- Script language enforcement

**Failure Behavior:**
- Blocks commit if critical issues found
- Shows detailed error messages
- Suggests fixes where possible

### Pre-push Hook

**Location:** `.git/hooks/pre-push`

**Checks:**
- All pre-commit checks
- Architecture validation
- Cross-platform compatibility
- Performance regression detection

**Failure Behavior:**
- Blocks push to remote
- Requires manual override for urgent fixes
- Generates detailed validation report

### Installation

```bash
# Automated installation
./scripts/install-git-hooks.sh

# Manual installation
cp .githooks/pre-commit .git/hooks/
cp .githooks/pre-push .git/hooks/
chmod +x .git/hooks/pre-commit .git/hooks/pre-push
```

### Customization

Hooks can be customized by editing files in `.githooks/` directory:

```
.githooks/
├── pre-commit      # Pre-commit validation
├── pre-push        # Pre-push validation
└── README.md       # Hook documentation
```

## CI/CD Integration

### GitHub Actions

Add to `.github/workflows/ci.yml`:

```yaml
- name: Validate Architecture
  run: |
    python scripts/validate-architecture.py --strict
    ./scripts/validate-architecture-simple.ps1

- name: Check File Sizes
  run: |
    python scripts/check-file-sizes.py --max-lines 1500

- name: Lint Code
  run: |
    golangci-lint run
    yamllint .
```

### Jenkins Pipeline

```groovy
stage('Architecture Validation') {
    steps {
        sh 'python scripts/validate-architecture.py --strict'
        powershell './scripts/validate-architecture-simple.ps1'
    }
}
```

### Local Development

```bash
# Run all validations
make validate

# Run specific validation
make validate-architecture
make validate-file-sizes
```

## Troubleshooting

### Common Issues

#### 1. PowerShell Execution Policy

**Error:**
```
File cannot be loaded because running scripts is disabled on this system.
```

**Solution:**
```powershell
# Set execution policy for current user
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Or run with bypass
PowerShell -ExecutionPolicy Bypass -File script.ps1
```

#### 2. File Size Limit Exceeded

**Error:**
```
File size limit exceeded: services/auth-service/main.go (2000 lines)
```

**Solutions:**
- Split large files into smaller modules
- Move utility functions to separate packages
- Use code generation for repetitive code
- Document exceptions in `scripts/core/config.py`

#### 3. Git Hook Not Executing

**Error:**
```
Hooks not running
```

**Solutions:**
```bash
# Check hook permissions
ls -la .git/hooks/pre-commit

# Make executable
chmod +x .git/hooks/pre-commit

# Reinstall hooks
./scripts/install-git-hooks.sh
```

#### 4. Validation Script Not Found

**Error:**
```
validate-architecture.ps1 not found
```

**Solutions:**
- Check script location: `scripts/validate-architecture.ps1`
- Ensure PowerShell is available
- Use Python fallback: `python scripts/validate-architecture.py`

### Debug Mode

Enable debug logging:

```bash
# PowerShell scripts
$DebugPreference = "Continue"
.\scripts\validate-architecture.ps1 -Debug

# Python scripts
python scripts/validate-architecture.py --debug
```

### Performance Issues

For large codebases:

```bash
# Run validation in parallel
python scripts/validate-architecture.py --parallel 4

# Skip heavy checks for quick validation
python scripts/validate-architecture.py --quick
```

## Configuration

### Validation Rules

Rules are defined in `scripts/core/config.py`:

```python
# File size limits
MAX_FILE_LINES = 1500

# Forbidden patterns
FORBIDDEN_EXTENSIONS = ['.exe', '.dll', '.so']

# Architecture patterns
DOMAIN_SEPARATION = True
ENTERPRISE_PATTERNS = True
```

### Custom Rules

Add custom validation rules:

```python
# In validate-architecture.py
def custom_validation(file_path: Path) -> List[str]:
    """Custom validation logic"""
    errors = []

    # Your custom checks here

    return errors
```

## Metrics & Reporting

### Validation Metrics

```json
{
  "timestamp": "2024-01-11T20:00:00Z",
  "files_checked": 1250,
  "errors_found": 3,
  "warnings_found": 12,
  "execution_time": 45.2,
  "success_rate": 97.6
}
```

### Dashboard Integration

Metrics can be exported to monitoring dashboards:

```bash
# Export to Prometheus format
python scripts/validate-architecture.py --metrics prometheus > metrics.txt

# Export to JSON for custom dashboards
python scripts/validate-architecture.py --metrics json > metrics.json
```

## Best Practices

### For Developers

1. **Run validation before commits**
   ```bash
   python scripts/validate-architecture.py
   ```

2. **Fix issues immediately**
   - Address all errors before pushing
   - Review warnings regularly

3. **Keep files under size limits**
   - Split large files proactively
   - Use code generation where appropriate

### For Teams

1. **Integrate into workflow**
   - Add validation to CI/CD
   - Run daily validation reports
   - Monitor trends over time

2. **Customize rules**
   - Add project-specific validations
   - Adjust limits based on needs
   - Document exceptions clearly

3. **Monitor effectiveness**
   - Track validation success rates
   - Measure time to fix issues
   - Review false positives regularly

## Contributing

### Adding New Validations

1. Add validation logic to appropriate script
2. Update documentation
3. Test on multiple platforms
4. Update CI/CD pipelines

### Reporting Issues

Use GitHub Issues with label `validation-tools`

### Code Standards

- Follow existing code patterns
- Add comprehensive error messages
- Include usage examples
- Document all parameters and options

---

## Quick Reference

| Command | Purpose | Platform |
|---------|---------|----------|
| `./scripts/install-git-hooks.sh` | Install git hooks | Cross-platform |
| `.\scripts\validate-architecture-simple.ps1` | Basic validation | Windows |
| `python scripts/validate-architecture.py` | Full validation | Cross-platform |
| `make validate` | Run all validations | Cross-platform |

**File Limits:** 1,500 lines max (exclusions apply)
**Git Hooks:** Automatic validation on commit/push
**CI/CD:** Integrated validation in pipelines