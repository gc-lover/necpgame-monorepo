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

    def __init__(self, yaml_path: str, api_url: str, dry_run: bool = False):
        self.yaml_path = Path(yaml_path)
        self.api_url = api_url
        self.dry_run = dry_run

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

        try:
            import requests
        except ImportError:
            print("ERROR: requests library not installed. Install with: pip install requests")
            return False

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

            # Validate each easter egg has required fields
            required_fields = ['id', 'name', 'category', 'difficulty', 'description']
            for i, egg in enumerate(easter_eggs):
                missing_fields = [field for field in required_fields if field not in egg]
                if missing_fields:
                    print(f"ERROR: Easter egg {i+1} missing required fields: {missing_fields}")
                    return False

            print("INFO: All easter eggs validated successfully")

            if self.dry_run:
                print("INFO: Dry run mode - skipping actual import")
                return True

            # Make actual HTTP request to import
            try:
                # Send YAML file path to import endpoint
                import_data = {"yaml_path": str(self.yaml_path)}

                response = requests.post(
                    self.api_url,
                    json=import_data,
                    headers={'Content-Type': 'application/json'},
                    timeout=30
                )

                if response.status_code == 200:
                    result = response.json()
                    print(f"SUCCESS: Imported {result.get('imported_count', 0)} easter eggs")
                    return True
                else:
                    print(f"ERROR: Import failed with status {response.status_code}")
                    print(f"Response: {response.text}")
                    return False

            except requests.exceptions.RequestException as e:
                print(f"ERROR: HTTP request failed: {e}")
                print("INFO: Service may not be running. Use --dry-run to validate structure only.")
                return False

        except yaml.YAMLError as e:
            print(f"ERROR: YAML parsing error: {e}")
            return False
        except Exception as e:
            print(f"ERROR: Unexpected error: {e}")
            return False

    def convert_for_json(self, obj):
        """Convert non-JSON serializable objects to strings"""
        if isinstance(obj, dict):
            return {key: self.convert_for_json(value) for key, value in obj.items()}
        elif isinstance(obj, list):
            return [self.convert_for_json(item) for item in obj]
        elif hasattr(obj, 'isoformat'):  # datetime objects
            return obj.isoformat()
        else:
            return obj

    def run_import(self) -> bool:
        """Run the complete import process"""
        print("INFO: Starting cyberspace easter eggs import")

        if not self.validate_yaml_file():
            return False

        # Test basic connectivity first
        try:
            import requests
            response = requests.get("http://localhost:8080/health", timeout=5)
            print(f"INFO: Service health check: {response.status_code}")
        except Exception as e:
            print(f"WARNING: Health check failed: {e}")

        if not self.make_import_request():
            return False

        print("INFO: Easter eggs import completed successfully")
        return True


def main():
    """Main entry point"""
    parser = argparse.ArgumentParser(description="Import cyberspace easter eggs from YAML")
    parser.add_argument("--yaml-path", required=True,
                       help="Path to YAML file containing easter eggs")
    parser.add_argument("--api-url", default="http://localhost:8080/api/v1/admin/import",
                       help="API endpoint URL for import")
    parser.add_argument("--dry-run", action="store_true",
                       help="Validate YAML structure without making HTTP request")

    args = parser.parse_args()

    importer = EasterEggsImporter(args.yaml_path, args.api_url, args.dry_run)

    if importer.run_import():
        print("SUCCESS: Easter eggs import completed successfully")
        sys.exit(0)
    else:
        print("FAILED: Easter eggs import failed")
        sys.exit(1)


if __name__ == "__main__":
    main()
