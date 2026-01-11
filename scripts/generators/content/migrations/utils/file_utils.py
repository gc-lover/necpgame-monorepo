"""
File utilities for migration generators.
"""

import hashlib
from pathlib import Path


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
