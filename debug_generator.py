#!/usr/bin/env python3
import sys
from pathlib import Path

print("=== Debug Generator Test ===")

# Check current directory
print(f"Current dir: {Path.cwd()}")

# Add scripts directory to Python path for imports
scripts_dir = Path.cwd() / 'scripts'
sys.path.insert(0, str(scripts_dir))
print(f"Scripts dir: {scripts_dir}")

# Check if directories exist
knowledge_dir = Path.cwd() / 'knowledge' / 'content' / 'interactives'
print(f"Knowledge dir exists: {knowledge_dir.exists()}")
print(f"Knowledge dir: {knowledge_dir}")

if knowledge_dir.exists():
    yaml_files = list(knowledge_dir.glob("*.yaml"))
    print(f"Found YAML files: {len(yaml_files)}")
    for f in yaml_files[:3]:  # Show first 3
        print(f"  {f.name}")

print("\nTrying to import...")

try:
    from core.config import ConfigManager
    print("ConfigManager imported")

    config = ConfigManager()
    print(f"Config created: {config}")

    from migrations.generators.interactives_generator import InteractivesMigrationGenerator
    print("InteractivesMigrationGenerator imported")

    print("Creating generator...")
    generator = InteractivesMigrationGenerator()
    print("Generator created!")

    print(f"Project root: {generator.project_root}")
    print(f"Input dirs: {generator.input_dirs}")
    print(f"Output dir: {generator.output_dir}")

    knowledge_dirs = generator.get_knowledge_dirs()
    print(f"Knowledge dirs: {knowledge_dirs}")

except Exception as e:
    print(f"ERROR: {e}")
    import traceback
    traceback.print_exc()
