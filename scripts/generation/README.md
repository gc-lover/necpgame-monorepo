# Go Service Generation System

## Architecture Overview

This system follows SOLID and DRY principles with a component-based architecture:

### Core Components

- **`go_service_generator.py`** - Main orchestrator that coordinates all components
- **`bundler.py`** - Handles OpenAPI specification bundling using redocly
- **`code_generator.py`** - Generates Go API code using ogen
- **`structure_creator.py`** - Creates service directory structure and files
- **`module_initializer.py`** - Initializes Go modules and dependencies
- **`tester.py`** - Tests compilation of generated code

### SOLID Principles

- **Single Responsibility**: Each component has one specific job
- **Open/Closed**: Components can be extended without modification
- **Liskov Substitution**: All components implement consistent interfaces
- **Interface Segregation**: Clean, focused component interfaces
- **Dependency Inversion**: High-level modules don't depend on low-level modules

### DRY Principle

- Shared code is extracted into reusable components
- No code duplication across different generation phases
- Consistent error handling and logging patterns

## Usage

```bash
# Generate service with full pipeline
python scripts/generation/go_service_generator.py example-domain

# Dry run to see what would be generated
python scripts/generation/go_service_generator.py example-domain --dry-run

# Skip certain steps
python scripts/generation/go_service_generator.py example-domain --skip-bundle --skip-test
```

## Performance Optimizations

The generated code includes:
- Memory pooling for JSON operations
- Prepared statements for database queries
- Connection pooling for HTTP clients
- Structured logging with zap
- Graceful shutdown handling
- Worker pools for concurrent processing

## Component Details

### OpenAPIBundler
Bundles OpenAPI specifications from multiple files into a single spec using redocly CLI.

### GoCodeGenerator
Uses ogen to generate type-safe Go client/server code from OpenAPI specifications.

### ServiceStructureCreator
Creates the complete Go service structure including:
- main.go with performance optimizations
- HTTP server with middleware
- Service layer with business logic
- Repository layer with data access
- Makefile for build automation

### GoModuleInitializer
Initializes Go modules and manages dependencies.

### CompilationTester
Validates that generated code compiles successfully.
