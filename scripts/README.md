# NECPGAME Scripts Directory Structure

This directory contains all automation scripts and tools for the NECPGAME MMOFPS project, organized in logical areas for code generation, validation, and testing.

## Directory Structure

### 📁 `core/`
**Foundation and utilities**
- `error-handling/` - Enterprise error handling, logging, middleware
- `base_script.py` - Base class for all scripts
- `command_runner.py` - Safe command execution
- `config.py` - Configuration management
- `file_manager.py` - File operations
- `logger.py` - Logging framework

### 📁 `generators/`
**Code and content generation**
- `services/` - Go service generation from OpenAPI
- `content/` - Game content generation (quests, NPCs, etc.)
- `templates/` - Code generation templates

### 📁 `validation/`
**Content validation**
- `validate-emoji-ban.py` - Emoji and special characters validation
- `validate-all-quests.py` - Quest YAML validation
- `base_validator.py` - Base validator classes
- `openapi_validator.py` - OpenAPI specification validation

### 📁 `tools/`
**Development and testing tools**
- `functional/` - API integration tests
- `security/` - Security tests
- `performance/` - Performance tests
- `openapi/` - OpenAPI specification tools
- `load-test/` - Load testing utilities
- `sql/` - Database utilities
- `ogen-migration/` - OpenAPI generator migration tools
- `performance-validation/` - Performance validation framework

## Usage

### Service Generation
```bash
# Generate all services
python generators/services/generate-all-services.py

# Generate specific problematic services
python generators/services/generate-problematic-services.py --service quest-service
```

### Content Migration
```bash
# Run all content migrations
python generators/content/migrations/run_generator.py

# Validate migrations
python generators/content/migrations/validate-all-migrations.py
```

### Unit Testing
```bash
# Run unit tests for all services
./run-unit-tests.sh

# Run tests for specific service
cd services/achievement-service-go
go test -tags=unit ./internal/...
```

### Integration Testing
```bash
# Run integration tests
python tools/functional/test_quest_api.py

# Run security tests
python tools/security/test_authentication.py
```


### Development Tools
```bash
# Validate OpenAPI specifications
python tools/openapi/validate-domains-openapi.py

# Run performance validation
python tools/performance-validation/performance-validator.py --service achievement-service

# Load testing
python tools/load-test/load-test-scenarios.py --concurrency 100
```

## Architecture Principles

- **SOLID**: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- **DRY**: Don't Repeat Yourself - shared code extracted to reusable components
- **Performance First**: Memory pooling, zero allocations in hot paths, optimized for MMOFPS scale
- **Enterprise Grade**: Comprehensive error handling, structured logging, graceful shutdown

## Performance Targets

- **Latency**: P99 < 50ms for game state operations
- **Throughput**: 1000+ RPS for matchmaking and combat
- **Memory**: < 100MB per service instance
- **Zero Allocations**: In hot paths (combat, matchmaking, state sync)