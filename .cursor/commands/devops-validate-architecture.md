# DevOps Architecture Validation

Run automated architecture validation checks.

## Usage

```powershell
# Run all validations
powershell scripts/validate-architecture-simple.ps1

# Check file sizes only
powershell scripts/validate-architecture-simple.ps1 -Check file-sizes
```

## Checks Performed

- **File Sizes**: ≤600 lines per file
- **Project Structure**: Required directories exist
- **YAML Structure**: Metadata and issue references
- **Security**: No hardcoded secrets

## Result Codes

- `0`: All checks passed
- `1`: Errors found (fix required)