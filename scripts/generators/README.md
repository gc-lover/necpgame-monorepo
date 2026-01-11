# Code and Content Generators

This directory contains all tools for automatic generation of Go services and game content from specifications.

## Structure

### `services/` - Service Generation
Automated generation of Go microservices from OpenAPI specifications using ogen.

**Main Components:**
- `enhanced_service_generator.py` - Primary service generator (1377 lines)
- `bundle_openapi.py` - OpenAPI bundling without external dependencies
- `generate-all-services.py` - Bulk service generation
- `generate-problematic-services.py` - Fix and generate missing services

**Usage:**
```bash
# Generate all services
python services/generate-all-services.py

# Generate specific service
python services/generate-problematic-services.py --service quest-service
```

### `content/` - Content Generation
Generation of game content (quests, NPCs, items, lore) from YAML specifications.

**Main Components:**
- `generate-quest-test-data.py` - Generate test quest data
- `migrations/` - Complete content migration system

**Usage:**
```bash
# Generate quest test data
python content/generate-quest-test-data.py

# Run content migrations
python content/migrations/run_generator.py
```

### `templates/` - Code Templates
Reusable templates for service generation and optimization.

## Performance Features

- **Memory Pooling**: Zero allocations in hot paths
- **Concurrent Processing**: Parallel service generation
- **Validation**: Pre-generation validation of specifications
- **Error Recovery**: Graceful handling of generation failures

## Integration

Generators integrate with:
- OpenAPI specifications in `proto/openapi/`
- Content specifications in `knowledge/canon/`
- Database migrations via Liquibase
- Performance monitoring systems