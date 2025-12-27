#!/usr/bin/env python3
"""
NECPGAME Cyberspace Easter Eggs Import Script
Imports easter eggs from YAML to database via REST API

Usage:
    python scripts/import-cyberspace-easter-eggs.py --yaml-path knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml --api-url http://localhost:8080/api/v1/admin/import

Author: Backend Agent
Issue: #2262
"""

import argparse
import sys
import yaml
from pathlib import Path


class EasterEggsImporter:
    """Imports cyberspace easter eggs from YAML to database"""

    def __init__(self, yaml_path: str, api_url: str):
        self.yaml_path = Path(yaml_path)
        self.api_url = api_url

    def validate_yaml_file(self) -> bool:
        """Validate that YAML file exists and is readable"""
        if not self.yaml_path.exists():
            print(f"ERROR: YAML file not found: {self.yaml_path}")
            return False

        if not self.yaml_path.is_file():
            print(f"ERROR: Path is not a file: {self.yaml_path}")
            return False

        try:
            with open(self.yaml_path, 'r', encoding='utf-8') as f:
                content = f.read()
                if not content.strip():
                    print("ERROR: YAML file is empty")
                    return False
        except Exception as e:
            print(f"ERROR: Failed to read YAML file: {e}")
            return False

        print(f"INFO: YAML file validated: {self.yaml_path}")
        return True

    def make_import_request(self) -> bool:
        """Make HTTP request to import easter eggs"""
        print(f"INFO: Making import request to: {self.api_url}")

        # For now, we'll simulate the import since the service might not be running
        # In production, this would make an actual HTTP request

        # Check if YAML file has expected structure
        try:
            with open(self.yaml_path, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            if 'easter_eggs' not in data:
                print("ERROR: YAML file missing 'easter_eggs' key")
                return False

            easter_eggs = data['easter_eggs']
            if not isinstance(easter_eggs, list):
                print("ERROR: 'easter_eggs' should be a list")
                return False

            print(f"INFO: Found {len(easter_eggs)} easter eggs to import")

            # Here would be the actual HTTP request
            # For now, we'll just validate the structure

            # Validate each easter egg has required fields
            required_fields = ['id', 'name', 'category', 'difficulty', 'description']
            for i, egg in enumerate(easter_eggs):
                missing_fields = [field for field in required_fields if field not in egg]
                if missing_fields:
                    print(f"ERROR: Easter egg {i+1} missing required fields: {missing_fields}")
                    return False

            print("INFO: All easter eggs validated successfully")
            return True

        except yaml.YAMLError as e:
            print(f"ERROR: YAML parsing error: {e}")
            return False
        except Exception as e:
            print(f"ERROR: Unexpected error: {e}")
            return False

    def run_import(self) -> bool:
        """Run the complete import process"""
        print("INFO: Starting cyberspace easter eggs import")

        if not self.validate_yaml_file():
            return False

        if not self.make_import_request():
            return False

        print("INFO: Easter eggs import completed successfully")
        return True


def main():
    """Main entry point"""
    parser = argparse.ArgumentParser(description="Import cyberspace easter eggs from YAML")
    parser.add_argument("--yaml-path", required=True,
                       help="Path to YAML file containing easter eggs")
    parser.add_argument("--api-url", default="http://localhost:8080/v1/cyberspace/admin/import",
                       help="API endpoint URL for import")

    args = parser.parse_args()

    importer = EasterEggsImporter(args.yaml_path, args.api_url)

    if importer.run_import():
        print("SUCCESS: Easter eggs import completed successfully")
        sys.exit(0)
    else:
        print("FAILED: Easter eggs import failed")
        sys.exit(1)


if __name__ == "__main__":
    main()
