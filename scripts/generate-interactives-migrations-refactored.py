#!/usr/bin/env python3
"""
Interactive migration generator using SOLID principles.
Delegates to unified runner to eliminate code duplication.
"""

import sys
from pathlib import Path

# Add the migrations module to the path
script_dir = Path(__file__).parent
sys.path.insert(0, str(script_dir))

# Import and run the unified generator
try:
    from migrations.run_generator import MigrationGeneratorRunner
except ImportError as e:
    print(f"Import error: {e}")
    import traceback
    traceback.print_exc()
    sys.exit(1)

def main():
    """Main entry point - delegate to unified runner."""
    runner = MigrationGeneratorRunner()

    # Create mock args object with interactives content type
    from argparse import Namespace
    mock_args = Namespace(content_type='interactives')

    # Override the parse_args method to return our mock args
    original_parse_args = runner.parser.parse_args
    runner.parser.parse_args = lambda: mock_args

    success = runner.run()
    return success

if __name__ == '__main__':
    success = main()
    sys.exit(0 if success else 1)
