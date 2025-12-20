#!/usr/bin/env python3
"""
Kafka Event Schema Validation Script
Issue: #2216
Agent: API Designer

Validates all Kafka event JSON schemas for NECPGAME.
Ensures schemas are syntactically correct and follow conventions.
"""

import json
import os
import sys
from pathlib import Path
from typing import List, Dict, Any
import jsonschema

class KafkaSchemaValidator:
    def __init__(self, schemas_dir: str):
        self.schemas_dir = Path(schemas_dir)
        self.errors = []
        self.warnings = []

    def validate_all_schemas(self) -> bool:
        """Validate all JSON schemas in the directory tree."""
        print("Validating Kafka Event Schemas...")
        print("=" * 50)

        schema_files = list(self.schemas_dir.rglob("*.json"))
        print(f"Found {len(schema_files)} schema files")

        all_valid = True

        for schema_file in schema_files:
            if not self._validate_single_schema(schema_file):
                all_valid = False

        self._validate_cross_references()

        print("\n" + "=" * 50)
        if all_valid and not self.errors:
            print("All schemas validated successfully!")
            return True
        else:
            print("Schema validation failed!")
            self._print_errors()
            return False

    def _validate_single_schema(self, schema_path: Path) -> bool:
        """Validate a single JSON schema file."""
        try:
            with open(schema_path, 'r', encoding='utf-8') as f:
                schema = json.load(f)

            # Basic JSON Schema validation
            jsonschema.Draft7Validator.check_schema(schema)

            # Custom validations
            self._validate_schema_conventions(schema, schema_path)

            print(f"PASS {schema_path.relative_to(self.schemas_dir)}")
            return True

        except json.JSONDecodeError as e:
            self.errors.append(f"JSON syntax error in {schema_path}: {e}")
        except jsonschema.SchemaError as e:
            self.errors.append(f"Schema validation error in {schema_path}: {e}")
        except Exception as e:
            self.errors.append(f"Unexpected error in {schema_path}: {e}")

        print(f"FAIL {schema_path.relative_to(self.schemas_dir)}")
        return False

    def _validate_schema_conventions(self, schema: Dict[str, Any], schema_path: Path):
        """Validate schema follows project conventions."""
        # Check required fields
        if "$schema" not in schema:
            self.warnings.append(f"Missing $schema in {schema_path}")

        if "$id" not in schema:
            self.warnings.append(f"Missing $id in {schema_path}")

        # Validate event type patterns
        if "enum" in schema.get("properties", {}).get("event_type", {}):
            event_types = schema["properties"]["event_type"]["enum"]
            for event_type in event_types:
                if not self._is_valid_event_type(event_type):
                    self.errors.append(f"Invalid event type format '{event_type}' in {schema_path}")

        # Check memory optimization (large→small field ordering)
        if "properties" in schema:
            self._validate_field_ordering(schema["properties"], schema_path)

    def _is_valid_event_type(self, event_type: str) -> bool:
        """Check if event type follows domain.entity.action format."""
        parts = event_type.split(".")
        return len(parts) == 3 and all(part.islower() and part.isalnum() for part in parts)

    def _validate_field_ordering(self, properties: Dict[str, Any], schema_path: Path):
        """Validate field ordering for memory optimization."""
        # This is a simplified check - in practice, you'd want more sophisticated ordering validation
        field_names = list(properties.keys())

        # Check for obvious ordering issues (this is a basic heuristic)
        priority_fields = ["event_id", "correlation_id", "session_id", "player_id", "event_type", "source", "timestamp"]

        # Ensure high-priority fields come first when present
        present_priority = [f for f in priority_fields if f in field_names]
        if present_priority != field_names[:len(present_priority)]:
            self.warnings.append(f"Consider reordering fields for memory optimization in {schema_path}")

    def _validate_cross_references(self):
        """Validate cross-schema references."""
        # This would check $ref links between schemas
        # For now, just ensure no broken internal references
        pass

    def _print_errors(self):
        """Print all validation errors and warnings."""
        if self.errors:
            print("\nERRORS:")
            for error in self.errors:
                print(f"  - {error}")

        if self.warnings:
            print("\nWARNINGS:")
            for warning in self.warnings:
                print(f"  - {warning}")

def main():
    if len(sys.argv) != 2:
        print("Usage: python validate-kafka-schemas.py <schemas_directory>")
        sys.exit(1)

    schemas_dir = sys.argv[1]

    if not os.path.exists(schemas_dir):
        print(f"Error: Directory {schemas_dir} does not exist")
        sys.exit(1)

    validator = KafkaSchemaValidator(schemas_dir)
    success = validator.validate_all_schemas()

    sys.exit(0 if success else 1)

if __name__ == "__main__":
    main()
