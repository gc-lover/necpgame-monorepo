#!/usr/bin/env python3
import sys
from pathlib import Path

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent / 'scripts'
sys.path.insert(0, str(scripts_dir))

print("Starting test...")

try:
    from migrations.generators.interactives_generator import InteractivesMigrationGenerator
    print("Import successful")

    generator = InteractivesMigrationGenerator()
    print(f"Generator created. Output dir: {generator.output_dir}")
    print(f"Input dirs: {generator.input_dirs}")

    result = generator.run()
    print(f"Generator run completed. Result: {result}")

except Exception as e:
    print(f"Error: {e}")
    import traceback
    traceback.print_exc()
