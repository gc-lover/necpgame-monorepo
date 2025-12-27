"""
Base classes and utilities for content migration generators.
Following SOLID principles for maintainable and extensible code.
"""

import os
import sys
import hashlib
import json
from abc import ABC, abstractmethod
from pathlib import Path
from datetime import datetime
from typing import Dict, Any, List, Optional

from .utils import JsonSerializer


class ContentMigrationConfig:
    """Configuration for content migration generation."""

    def __init__(self, name: str, description: str, content_type: str,
                 input_dirs: List[str], output_dir: str, table_name: str):
        self.name = name
        self.description = description
        self.content_type = content_type
        self.input_dirs = input_dirs
        self.output_dir = output_dir
        self.table_name = table_name

    @property
    def get_output_dir(self) -> Path:
        """Get output directory for this content type."""
        return Path(self.output_dir)


class FileUtils:
    """Utility class for file operations."""

    @staticmethod
    def get_file_hash(file_path: Path) -> str:
        """Calculate MD5 hash of file content."""
        with open(file_path, 'rb') as f:
            return hashlib.md5(f.read()).hexdigest()

    @staticmethod
    def load_yaml(file_path: Path) -> dict:
        """Load and parse YAML file."""
        try:
            import yaml
            with open(file_path, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f) or {}
        except Exception as e:
            print(f"Error loading {file_path}: {e}")
            return {}


class JsonSerializer:
    """Utility class for JSON serialization."""

    @staticmethod
    def json_serializer(obj) -> str:
        """JSON serializer for datetime objects."""
        if isinstance(obj, datetime):
            return obj.isoformat()
        raise TypeError(f"Object of type {type(obj)} is not JSON serializable")


class BaseContentMigrationGenerator(ABC):
    """
    Abstract base class for content migration generators.
    Follows Single Responsibility Principle - each generator handles one content type.
    """

    def __init__(self, name: str, description: str, content_type: str,
                 input_dirs: List[str], output_dir: str, table_name: str):
        # Create config from parameters
        self.config = ContentMigrationConfig(
            name=name,
            description=description,
            content_type=content_type,
            input_dirs=input_dirs,
            output_dir=output_dir,
            table_name=table_name
        )
        self.project_root = Path(__file__).parent.parent.parent
        self.name = name
        self.description = description
        self.content_type = content_type
        self.input_dirs = [Path(d) for d in input_dirs]
        self.table_name = table_name
        self.output_dir = Path(output_dir)

        # Ensure output directory exists
        self.output_dir.mkdir(parents=True, exist_ok=True)
        self.yaml_cache = {}  # Cache for loaded YAML files to improve performance

    @abstractmethod
    def get_content_fields(self) -> List[str]:
        """Get list of fields that must be present for valid content."""
        pass

    @abstractmethod
    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract content-type specific data from YAML spec."""
        pass

    def process_content_file(self, yaml_file: Path) -> Dict[str, Any]:
        """Process content YAML file with common logic and content-specific extraction.
        This method provides the common processing logic and delegates specific data extraction
        to subclasses via abstract methods.
        """
        try:
            import yaml
            with open(yaml_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f) or {}
        except Exception as e:
            print(f"Error loading {yaml_file}: {e}")
            return {}

        # Skip files without proper YAML structure (no metadata)
        if not isinstance(spec, dict):
            print(f"Skipping {yaml_file.name} - not a valid YAML structure")
            return {}

        metadata = spec.get('metadata', {})

        # Skip files without metadata - they are not structured documents
        if not metadata:
            print(f"Skipping {yaml_file.name} - no metadata found (not a structured document)")
            return {}

        # Skip README files - they should be handled by documentation generator
        if 'readme' in yaml_file.name.lower():
            print(f"Skipping README file {yaml_file.name} - handled by documentation generator")
            return {}

        # Check if file has required content fields
        content_fields = self.get_content_fields()
        has_content = False

        for field in content_fields:
            field_path = field.split('.')
            current_obj = spec
            for path_part in field_path:
                current_obj = current_obj.get(path_part, {}) if isinstance(current_obj, dict) else {}
            if current_obj:
                has_content = True
                break

        if not has_content:
            print(f"Skipping {yaml_file.name} - no required content fields found: {content_fields}")
            return {}

        # Extract common data
        common_data = {
            'id': str(hash(yaml_file.name + str(datetime.now())))[:16],
            'metadata': json.dumps({
                'id': metadata.get('id'),
                'version': metadata.get('version', '1.0.0'),
                'source_file': str(yaml_file.relative_to(self.project_root) if yaml_file.is_relative_to(self.project_root) else yaml_file)
            }, default=JsonSerializer.json_serializer, ensure_ascii=False)
        }

        # Extract content-specific data
        specific_data = self.extract_specific_data(spec, yaml_file)

        # Combine and return
        return {**common_data, **specific_data}

    def get_knowledge_dirs(self) -> List[Path]:
        """Get list of knowledge directories to scan."""
        dirs = []
        for dir_path in self.input_dirs:
            full_path = self.project_root / dir_path
            if full_path.exists():
                dirs.append(full_path)
            else:
                print(f"Warning: Directory {full_path} does not exist")
        return dirs

    def validate_content_data(self, data: Dict[str, Any]) -> bool:
        """Validate processed content data before migration generation."""
        if not isinstance(data, dict):
            print(f"Warning: Content data must be a dictionary, got {type(data)}")
            return False

        required_fields = ['id', 'metadata']
        for field in required_fields:
            if field not in data:
                print(f"Warning: Missing required field '{field}' in content data")
                return False

        return True


    def should_update_migration(self, yaml_file: Path, file_hash: str) -> tuple[bool, str]:
        """Check if migration should be updated based on existing files."""
        # Look for existing migration files for this content
        existing_pattern = f"data_{self.config.content_type}_{yaml_file.stem}_*.yaml"
        existing_files = list(self.output_dir.glob(existing_pattern))

        if not existing_files:
            # No existing migration, need to create
            return True, ""

        # Check if any existing file has the same hash
        for existing_file in existing_files:
            filename_parts = existing_file.stem.split('_')
            if len(filename_parts) >= 4:
                existing_hash = filename_parts[-2]  # Hash is second to last part
                if existing_hash == file_hash[:8]:
                    # Same content, no need to update
                    return False, str(existing_file)

        # Different hash, need to update (old file will be replaced)
        return True, ""

    def generate_migration(self, content_data: Dict[str, Any], yaml_file: Path) -> str:
        """Generate Liquibase migration YAML for content data."""
        file_hash = FileUtils.get_file_hash(yaml_file)
        timestamp = datetime.now().strftime('%Y%m%d%H%M%S')

        # Check if we need to update
        needs_update, existing_file = self.should_update_migration(yaml_file, file_hash)
        if not needs_update:
            print(f"Skipping {yaml_file.name} - migration already up to date")
            return existing_file

        migration = {
            'databaseChangeLog': [{
                'changeSet': {
                    'id': f'{self.config.content_type}-{yaml_file.stem}-{file_hash[:8]}',
                    'author': 'content-migration-generator',
                    'changes': [{
                        'insert': {
                            'tableName': self.config.table_name,
                            'columns': []
                        }
                    }]
                }
            }]
        }

        # Add columns
        for key, value in content_data.items():
            migration['databaseChangeLog'][0]['changeSet']['changes'][0]['insert']['columns'].append({
                'column': {
                    'name': key,
                    'value': value
                }
            })

        # Generate filename with readable format: data_type_filename_hash_timestamp.yaml
        filename = f"data_{self.config.content_type}_{yaml_file.stem}_{file_hash[:8]}_{timestamp}.yaml"
        filepath = self.output_dir / filename

        # Remove old migration file if it exists
        if existing_file and Path(existing_file).exists():
            Path(existing_file).unlink()
            print(f"Replacing old migration: {Path(existing_file).name}")

        # Write migration file
        with open(filepath, 'w', encoding='utf-8') as f:
            import yaml
            yaml.dump(migration, f, default_flow_style=False, allow_unicode=True, sort_keys=False)

        return str(filepath)

    def run(self) -> List[str]:
        """Main execution method with performance optimizations."""
        print(f"Starting {self.config.content_type} migrations generation...")

        generated_files = []
        processed_count = 0
        skipped_count = 0

        # Collect all YAML files first (better performance for large directories)
        all_yaml_files = []
        for knowledge_dir in self.get_knowledge_dirs():
            print(f"Scanning {knowledge_dir}...")
            yaml_files = list(knowledge_dir.rglob("*.yaml"))
            all_yaml_files.extend(yaml_files)
            print(f"Found {len(yaml_files)} YAML files")

        total_files = len(all_yaml_files)
        print(f"Total {self.config.content_type} files to process: {total_files}")

        # Process files with progress indication
        for i, yaml_file in enumerate(all_yaml_files, 1):
            if i % 10 == 0 or i == total_files:  # Progress every 10 files or at the end
                print(f"Processing {i}/{total_files} files...")

            try:
                # Process content file
                content_data = self.process_content_file(yaml_file)

                if not content_data:
                    print(f"Skipping {yaml_file.name} - no valid data")
                    skipped_count += 1
                    continue

                # Validate data
                if not self.validate_content_data(content_data):
                    print(f"Skipping {yaml_file.name} - validation failed")
                    skipped_count += 1
                    continue

                # Generate migration
                migration_file = self.generate_migration(content_data, yaml_file)
                generated_files.append(migration_file)
                processed_count += 1

            except Exception as e:
                print(f"Error processing {yaml_file.name}: {e}")
                skipped_count += 1
                continue

        print(f"\nCompleted! Processed {processed_count} {self.config.content_type} files")
        print(f"Skipped {skipped_count} files (errors or invalid data)")
        print(f"Generated {len(generated_files)} migration files in {self.output_dir}")

        return generated_files
