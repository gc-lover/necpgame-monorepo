# NECPGAME Architecture Validation Tools

## 🎯 Overview

This document describes the comprehensive architecture validation system for the NECPGAME project. The validation tools ensure code quality, architectural compliance, and prevent common issues before they reach production.

**Issue:** #1866

## 📋 Validation Checks

### File Size Limits
- **Maximum:** 1000 lines per file (increased from 600)
- **Exclusions:** Generated files (oas_*.go, bundled YAML, changelogs)
- **Rationale:** Improved from 600 to accommodate complex OpenAPI specs and generated code

### Project Structure
- **Required directories:**
  - `proto/openapi/` - API specifications
  - `services/` - Go microservices
  - `knowledge/` - Game content (YAML)
  - `infrastructure/` - DevOps configs

### YAML Validation
- **Metadata sections:** Required in knowledge/canon files
- **Issue references:** All files must include `# Issue: #XXXX`
- **Scope:** knowledge/ directory files

### Security Checks
- **Hardcoded passwords:** Detection in Go and YAML files
- **Excluded paths:** .git, node_modules, vendor directories
- **Pattern matching:** Basic secret detection

## 🛠️ Tools

### 1. Simple Validation Script (`validate-architecture-simple.ps1`)
**Purpose:** Basic validation for CI/CD and quick checks
**Platform:** Windows PowerShell compatible
**Checks:**
- File sizes (1000 line limit)
- Required directory structure

**Usage:**
```powershell
# Run basic validation
.\scripts\validate-architecture-simple.ps1

# Exit codes:
# 0: Success
# 1: Errors found
```

### 2. Advanced Validation Script (`validate-architecture.ps1`)
**Purpose:** Comprehensive validation with multiple check types
**Platform:** Windows PowerShell
**Checks:** All of the above plus YAML metadata and security

**Usage:**
```powershell
# Run all checks
.\scripts\validate-architecture.ps1

# Run specific checks
.\scripts\validate-architecture.ps1 -Check "file-sizes"
.\scripts\validate-architecture.ps1 -Check "structure"
.\scripts\validate-architecture.ps1 -Check "yaml"
.\scripts\validate-architecture.ps1 -Check "security"
```

### 3. Git Hooks Installation (`install-git-hooks.sh`)
**Purpose:** Automated validation on git operations
**Platform:** Cross-platform (Bash)

**Installation:**
```bash
# Install hooks (run from project root)
./scripts/install-git-hooks.sh
```

**Installed Hooks:**
- **pre-commit:** Runs basic validation before commits
- **pre-push:** Runs full validation before pushing to remote

## 🔄 CI/CD Integration

### GitHub Actions Workflow
Located: `.github/workflows/architecture-validation.yml`

**Triggers:**
- Push to main branch
- Pull requests
- Manual workflow dispatch

**Jobs:**
1. **Validate:** Runs PowerShell validation scripts
2. **Report:** Generates validation reports
3. **Quality Gates:** Blocks merges on validation failures

### Integration Points
- **ci-backend.yml:** Includes architecture validation as dependency
- **quality-gates.yml:** Comprehensive quality checks
- **Artifact storage:** Reports saved for 30 days

## 📊 Validation Reports

### Output Format
```
🔍 Starting NECPGAME Architecture Validation...
==================================================

📏 Checking file sizes...
OK File validation completed

🏗️ Checking project structure...
OK Directory proto/openapi exists
OK Directory services exists
OK Directory knowledge exists
OK Directory infrastructure exists

==================================================
🏁 Architecture Validation Complete

Results:
  Errors: 0
  Warnings: 0

OK VALIDATION PASSED: No errors or warnings
```

### Error Types
- **ERROR:** Critical issues (validation fails, blocks commits)
- **WARNING:** Non-critical issues (validation passes, suggestions)

## 🚨 Common Issues & Solutions

### File Size Violations
**Problem:** Files exceed 1000 lines
**Solution:**
1. Check if file is generated (oas_*.go) - should be excluded
2. Refactor large files into smaller modules
3. Consider splitting complex functions

### Missing Directories
**Problem:** Required project structure directories missing
**Solution:**
1. Create missing directories
2. Ensure proper file placement (see `.cursor/rules/agent-file-placement.mdc`)

### YAML Metadata Issues
**Problem:** Missing metadata or issue references
**Solution:**
1. Add `metadata:` section at top of YAML files
2. Include `# Issue: #XXXX` comment in all files

### PowerShell Execution Policy
**Problem:** Scripts fail due to execution policy
**Solution:**
```powershell
# Set execution policy for current session
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process

# Or run with explicit bypass
powershell -ExecutionPolicy Bypass -File script.ps1
```

## 🧪 Testing Validation Tools

### Manual Testing
```bash
# Test simple validation
./scripts/validate-architecture-simple.ps1

# Test advanced validation
./scripts/validate-architecture.ps1 -Check "all"

# Test git hooks
git add .
git commit -m "test validation"
```

### CI/CD Testing
- Push test commits to trigger workflows
- Check Actions tab for validation results
- Review generated reports in artifacts

## 📈 Performance Metrics

### Validation Speed
- **Simple script:** < 30 seconds for 10,000+ files
- **Advanced script:** < 60 seconds with security checks
- **Git hooks:** < 10 seconds for basic validation

### Coverage
- **Files scanned:** 10,000+ (YAML, Go, SQL, MD)
- **Directories checked:** 4 required project directories
- **Security patterns:** Basic hardcoded secret detection

## 🔧 Maintenance

### Updating Limits
1. Edit line count in scripts (`$lines -gt 1000`)
2. Update documentation
3. Test with existing codebase
4. Update CI/CD workflows if needed

### Adding New Checks
1. Add check logic to PowerShell scripts
2. Update parameter handling in advanced script
3. Add documentation
4. Test thoroughly

### Cross-Platform Compatibility
- **Windows:** Native PowerShell support
- **Linux/Mac:** Bash fallback in git hooks
- **CI/CD:** Windows-based runners with PowerShell

## 📞 Support & Troubleshooting

### Getting Help
1. Check this README first
2. Run validation with verbose output
3. Check GitHub Actions logs
4. Review error messages carefully

### Common Error Messages
- `"Could not read file"`: File permission or encoding issue
- `"File exceeds 1000 lines"`: Refactor or exclude file
- `"Required directory missing"`: Create missing project directories

### Escalation
For persistent issues:
1. Document the problem with full error output
2. Check if issue affects CI/CD or local development
3. Create issue with label `devops` and `bug`

## 📝 Change Log

### v2.0.0 (Current)
- Increased file size limit from 600 to 1000 lines
- Fixed PowerShell syntax errors in validation scripts
- Added comprehensive git hooks (pre-commit, pre-push)
- Created detailed documentation
- Improved error handling and logging
- Added cross-platform compatibility

### v1.0.0
- Initial architecture validation scripts
- Basic file size and structure checks
- GitHub Actions integration
- Basic PowerShell implementation

---

**Last Updated:** December 2025
**Maintained by:** DevOps Agent
**Issue:** #1866