# Development and Testing Tools

This directory contains development tools, testing utilities, and quality assurance frameworks for the NECPGAME project.

## Testing Framework

### Integration Tests
- **`functional/`** - API integration testing with real HTTP calls
- **`security/`** - Security and authentication testing
- **`performance/`** - Performance and load testing scenarios

### Development Tools
- **`openapi/`** - OpenAPI specification analysis and manipulation tools
- **`load-test/`** - Load testing execution and scenarios
- **`sql/`** - Database utilities and SQL processing tools
- **`ogen-migration/`** - OpenAPI generator migration and compatibility tools
- **`performance-validation/`** - Performance validation and benchmarking framework

## Key Features

### Automated Testing Pipeline
- Pre-deployment validation
- Performance regression detection
- Security vulnerability scanning
- API contract verification

### Performance Validation
- Latency benchmarking (<50ms targets)
- Load testing (1000+ RPS)
- Memory leak detection
- Concurrent operation validation

### Quality Assurance
- Code coverage analysis
- Static analysis integration
- API specification compliance
- Content validation

## Usage

```bash
# Run full test suite
./run-test-suite.sh

# Run performance validation
python performance-validation/performance-validator.py --service quest-service

# Run load testing
python load-test/load-test-scenarios.py --concurrency 100
```

## Testing Strategy

1. **Unit Tests**: Individual component validation
2. **Integration Tests**: Service interaction verification
3. **Performance Tests**: Load and latency validation
4. **Security Tests**: Vulnerability assessment
5. **E2E Tests**: Complete user workflow validation