#!/usr/bin/env python3
"""
Simple wrapper to run the quests generator from the scripts directory.
"""

import sys
from pathlib import Path

# Add the scripts directory to the path so we can import migrations
scripts_dir = Path(__file__).parent
sys.path.insert(0, str(scripts_dir))

# Now import and run the generator
from migrations.run_generator import main

if __name__ == '__main__':
    main()




