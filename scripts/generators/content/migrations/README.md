# SOLID Content Migration Generators

This directory contains a refactored implementation of content migration generators following SOLID principles with performance optimizations and validation.

## Architecture Overview

```
scripts/migrations/
├── base.py                    # Abstract base classes and common logic
├── generators/               # Concrete generator implementations
│   ├── __init__.py
│   ├── quests_generator.py   # Quest migrations
│   ├── npcs_generator.py     # NPC migrations
│   ├── dialogues_generator.py # Dialogue migrations
│   ├── lore_generator.py     # Lore migrations
│   ├── enemies_generator.py  # Enemy migrations
│   ├── interactives_generator.py # Interactive objects
│   ├── items_generator.py    # Item migrations
│   └── culture_generator.py  # Culture migrations
└── utils/                    # Shared utilities
    ├── __init__.py
    ├── file_utils.py         # File operations
    └── json_utils.py         # JSON serialization
```

## SOLID Principles Applied

### 1. Single Responsibility Principle (SRP)
- Each generator class handles only one type of content
- Base classes provide common functionality without business logic
- Utility classes have single, focused responsibilities

### 2. Open/Closed Principle (OCP)
- New content types can be added without modifying existing code
- Base classes are extensible through inheritance
- Configuration-driven approach for different content types

### 3. Liskov Substitution Principle (LSP)
- All concrete generators can be used interchangeably through the base interface
- Contract defined by `BaseContentMigrationGenerator` is honored by all subclasses

### 4. Interface Segregation Principle (ISP)
- Clients depend only on methods they actually use
- Separate utility classes provide focused interfaces
- Abstract base class defines minimal required interface

### 5. Dependency Inversion Principle (DIP)
- High-level modules don't depend on low-level modules
- Both depend on abstractions (base classes and interfaces)
- Dependency injection through configuration objects

## Performance Optimizations

### ✅ Implemented Features

- **File Caching**: YAML files are cached to reduce I/O operations
- **Batch Processing**: Files are collected before processing for better performance
- **Progress Tracking**: Shows progress every 10 files for large datasets
- **Incremental Updates**: Only generates migrations for changed content
- **Validation**: Validates data before migration generation
- **Error Handling**: Graceful handling of malformed files

### Performance Metrics

- **Large Dataset Handling**: Optimized for 1000+ files per content type
- **Memory Efficiency**: Minimal memory usage with streaming processing
- **I/O Optimization**: Reduced file system operations through caching

## Content Types Supported

### ✅ Fully Implemented

1. **Quests** (`QuestMigrationGenerator`)
   - Schema: `gameplay.quest_definitions`
   - Sources: `knowledge/canon/narrative/quests/`, `knowledge/content/quests/`

2. **NPCs** (`NpcsMigrationGenerator`)
   - Schema: `narrative.npc_definitions`
   - Sources: `knowledge/canon/narrative/npc-lore/`

3. **Dialogues** (`DialoguesMigrationGenerator`)
   - Schema: `narrative.dialogue_nodes`
   - Sources: `knowledge/canon/narrative/dialogues/`

4. **Lore** (`LoreMigrationGenerator`)
   - Schema: `knowledge.lore_entries`
   - Sources: `knowledge/canon/lore/`, `knowledge/canon/culture/`

5. **Enemies** (`EnemiesMigrationGenerator`)
   - Schema: `knowledge.enemies`
   - Sources: `knowledge/canon/ai-enemies/`, `knowledge/content/enemies/`

6. **Interactives** (`InteractivesMigrationGenerator`)
   - Schema: `knowledge.interactives`
   - Sources: `knowledge/canon/interactive-objects/`, `knowledge/content/interactives/`

7. **Items** (`ItemsMigrationGenerator`)
   - Schema: `gameplay.items`
   - Sources: `knowledge/content/items/`, `knowledge/mechanics/gear/`, `knowledge/mechanics/weapons/`

8. **Culture** (`CultureMigrationGenerator`)
   - Schema: `knowledge.lore_entries` (with category 'culture')
   - Sources: `knowledge/canon/culture/`, `knowledge/canon/lore/culture/`

## Usage

### Running All Generators

```bash
python scripts/generate-all-content-migrations-solid.py
```

### Running Individual Generators

```bash
python scripts/generate-quests-migrations-refactored.py
python scripts/generate-npcs-migrations-refactored.py
python scripts/generate-dialogues-migrations-refactored.py
python scripts/generate-lore-migrations-refactored.py
python scripts/generate-enemies-migrations-refactored.py
python scripts/generate-interactives-migrations-refactored.py
python scripts/generate-items-migrations-refactored.py
python scripts/generate-culture-migrations-refactored.py
```

### Adding a New Content Type

1. Create a new configuration:
```python
config = ContentMigrationConfig(
    content_type="new_content",
    table_name="schema.new_content_table",
    directories=["knowledge/path/to/new/content"]
)
```

2. Create a concrete generator:
```python
class NewContentMigrationGenerator(BaseContentMigrationGenerator):
    def __init__(self):
        config = ContentMigrationConfig(...)
        super().__init__(config)

    def process_content_file(self, yaml_file: Path) -> Dict[str, Any]:
        # Implement content-specific processing logic
        pass
```

3. Register in the main runner:
```python
self.generators = [
    QuestMigrationGenerator,
    NpcsMigrationGenerator,
    NewContentMigrationGenerator,  # Add here
]
```

## Migration Strategy

### Incremental Updates
- **File Hashing**: Uses MD5 hashes to detect content changes
- **Version Control**: Each migration includes hash and timestamp
- **Conflict Resolution**: Replaces old migrations when content changes

### Filename Format
```
data_{content_type}_{filename}_{hash}_{timestamp}.yaml
```

Example:
```
data_quest_brexit-legacy-london-2078-2093_a1b2c3d4_20241224120000.yaml
```

## Benefits

- **Maintainability**: Changes to one content type don't affect others
- **Extensibility**: Easy to add new content types and utilities
- **Performance**: Optimized for large datasets with caching and batching
- **Reliability**: Validation and error handling for malformed data
- **Reusability**: Common functionality shared across generators
- **Readability**: Clear separation of concerns and responsibilities

## Migration from Legacy Scripts

The legacy scripts in the root `scripts/` directory work but have code duplication.
New SOLID-based generators should be used for all new development.

To migrate:
1. Implement new generators using the SOLID architecture
2. Test thoroughly with existing data
3. Update CI/CD pipelines to use new scripts
4. Deprecate old scripts after successful migration
