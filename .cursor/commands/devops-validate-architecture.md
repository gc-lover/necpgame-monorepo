# DevOps Agent — Architecture Validation Commands

## [TARGET] Purpose

Commands for DevOps agent to validate NECPGAME project architecture and ensure code quality standards.

**Issue:** #1866

## [SYMBOL] Available Commands

### Architecture Validation

#### `validate—architecture-simple`

**Purpose:** Run basic architecture validation (file sizes, structure)
**Platform:** Windows PowerShell
**Usage:**

```bash
# From project root
./scripts/validate-architecture-simple.ps1
```

**Checks Performed:**

- File sizes (max 1000 lines, excludes generated files)
- Required directory structure
- Basic project integrity
- Enterprise-grade domain validation
- Service optimization checks

#### `validate-architecture-advanced`

**Purpose:** Run comprehensive architecture validation
**Platform:** Windows PowerShell
**Usage:**

```bash
# All checks
./scripts/validate-architecture.ps1

# Specific checks
./scripts/validate-architecture.ps1 -Check "file-sizes"
./scripts/validate-architecture.ps1 -Check "structure"
./scripts/validate-architecture.ps1 -Check "yaml"
./scripts/validate-architecture.ps1 -Check "security"
```

**Checks Performed:**

- All basic checks plus:
- YAML metadata validation
- Security pattern detection
- Issue reference validation

### Git Hooks Management

#### `install-git-hooks`

**Purpose:** Install pre-commit and pre-push hooks for automatic validation
**Platform:** Cross-platform (Bash)
**Usage:**

```bash
# From project root
./scripts/install-git-hooks.sh
```

**What it does:**

- Creates `.git/hooks/pre-commit` (basic validation)
- Creates `.git/hooks/pre-push` (full validation)
- Sets executable permissions
- Provides installation feedback

### CI/CD Validation

#### `validate-ci-cd`

**Purpose:** Run validation as CI/CD would
**Platform:** Windows PowerShell
**Usage:**

```bash
# Simulate CI/CD run
./scripts/validate-architecture-simple.ps1
echo "Exit code: $LASTEXITCODE"
```

**Exit Codes:**

- `0`: Validation passed
- `1`: Validation failed (errors found)

## [SYMBOL] Maintenance Commands

### Update Validation Limits

```powershell
# Edit file size limits in scripts
# File: scripts/validate-architecture-simple.ps1
# Line: if ($lines -gt 1000) {

# File: scripts/validate-architecture.ps1
# Line: if ($lines -gt 1000) {
```

### Add New Validation Checks

1. Add check logic to PowerShell scripts
2. Update parameter validation in advanced script
3. Add documentation to README
4. Test with existing codebase

### Cross-Platform Testing

```bash
# Test bash fallback (Linux/Mac)
echo "Testing bash compatibility..."

# Test PowerShell (Windows)
powershell -c "Write-Host 'PowerShell available'"
```

## [SYMBOL] Validation Results

### Success Output

```
[SEARCH] Starting NECPGAME Architecture Validation...
==================================================

[SYMBOL] Checking file sizes...
[OK] File validation completed

[BUILDING] Checking project structure...
[OK] Directory proto/openapi exists
[OK] Directory services exists
[OK] Directory knowledge exists
[OK] Directory infrastructure exists

==================================================
[SYMBOL] Architecture Validation Complete

Results:
  Errors: 0
  Warnings: 0

[OK] VALIDATION PASSED: No errors or warnings
```

### Error Output

```
[ERROR] ERROR: File large-file.go exceeds 1000 lines (1500 lines)
[ERROR] ERROR: Required directory missing: proto/openapi

[ERROR] VALIDATION FAILED: 2 errors found
Please fix all errors before committing
```

## [ALERT] Troubleshooting

### Common Issues

#### PowerShell Execution Policy

```powershell
# Fix execution policy
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Or run with bypass
powershell -ExecutionPolicy Bypass -File script.ps1
```

#### File Access Errors

- Ensure scripts are run from project root
- Check file permissions
- Verify PowerShell version (5.1+ recommended)

#### Git Hook Issues

```bash
# Check hook installation
ls -la .git/hooks/

# Test hooks manually
.git/hooks/pre-commit

# Reinstall if needed
./scripts/install-git-hooks.sh
```

### Performance Issues

- Large codebases may take 30-60 seconds
- Consider excluding vendor directories
- Use simple script for faster validation

## [SYMBOL] Quality Metrics

### Coverage

- **Files:** 10,000+ files scanned
- **Types:** YAML, Go, SQL, Markdown
- **Directories:** 4 required structures
- **Security:** Basic pattern matching

### Performance

- **Simple validation:** < 30 seconds
- **Advanced validation:** < 60 seconds
- **Git hooks:** < 10 seconds
- **Memory usage:** Minimal (< 50MB)

## [SYMBOL] Integration Points

### GitHub Actions

- Workflow: `.github/workflows/architecture-validation.yml`
- Triggers: Push, PR, manual
- Artifacts: Validation reports (30 days retention)

### CI/CD Pipeline

- Dependency in `ci-backend.yml`
- Quality gates in `quality-gates.yml`
- Blocking merges on validation failures

### Development Workflow

- Pre-commit: Basic validation
- Pre-push: Full validation
- IDE integration: Manual script execution

---

**Command Reference Version:** 2.0.0
**Issue:** #1866
**Last Updated:** December 2025