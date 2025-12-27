#!/usr/bin/env python3
import sys
from pathlib import Path

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent / 'scripts'
sys.path.insert(0, str(scripts_dir))

try:
    from migrations.generators.interactives_generator import InteractivesMigrationGenerator
    print('Import successful')
    generator = InteractivesMigrationGenerator()
    print('Generator created successfully')
    print(f'Output dir: {generator.output_dir}')
except Exception as e:
    print(f'Error: {e}')
    import traceback
    traceback.print_exc()
