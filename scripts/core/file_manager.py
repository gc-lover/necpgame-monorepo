#!/usr/bin/env python3
"""
NECPGAME File Manager
SOLID: Single Responsibility - manages file operations
"""

import json
import os
import yaml
from pathlib import Path
from typing import List, Dict, Any, Optional, Union

from core.config import ConfigManager


class FileManager:
    """
    Handles all file operations.
    Single Responsibility: Read/write files, find files, validate paths.
    """

    def __init__(self, config_manager: ConfigManager):
        self.config = config_manager

    def read_yaml(self, file_path: Path) -> Dict[str, Any]:
        """Read YAML file"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f) or {}
        except Exception as e:
            raise ValueError(f"Failed to read YAML {file_path}: {e}")

    def write_yaml(self, file_path: Path, data: Dict[str, Any], create_dirs: bool = True) -> None:
        """Write YAML file"""
        if create_dirs:
            file_path.parent.mkdir(parents=True, exist_ok=True)

        try:
            with open(file_path, 'w', encoding='utf-8') as f:
                yaml.dump(data, f, allow_unicode=True, sort_keys=False,
                          default_flow_style=False, width=120)
        except Exception as e:
            raise ValueError(f"Failed to write YAML {file_path}: {e}")

    def read_json(self, file_path: Path) -> Dict[str, Any]:
        """Read JSON file"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                return json.load(f)
        except Exception as e:
            raise ValueError(f"Failed to read JSON {file_path}: {e}")

    def write_json(self, file_path: Path, data: Dict[str, Any], create_dirs: bool = True) -> None:
        """Write JSON file"""
        if create_dirs:
            file_path.parent.mkdir(parents=True, exist_ok=True)

        try:
            with open(file_path, 'w', encoding='utf-8') as f:
                json.dump(data, f, indent=2, ensure_ascii=False)
        except Exception as e:
            raise ValueError(f"Failed to write JSON {file_path}: {e}")

    def read_text(self, file_path: Path) -> str:
        """Read text file"""
        try:
            return file_path.read_text(encoding='utf-8')
        except Exception as e:
            raise ValueError(f"Failed to read text file {file_path}: {e}")

    def write_text(self, file_path: Path, content: str, create_dirs: bool = True) -> None:
        """Write text file"""
        if create_dirs:
            file_path.parent.mkdir(parents=True, exist_ok=True)

        try:
            file_path.write_text(content, encoding='utf-8')
        except Exception as e:
            raise ValueError(f"Failed to write text file {file_path}: {e}")

    def find_files(self, pattern: str, directory: Optional[Path] = None) -> List[Path]:
        """Find files matching pattern"""
        if directory is None:
            directory = self.config.get_project_root()

        return list(directory.glob(pattern))

    def find_yaml_files(self, directory: Optional[Path] = None) -> List[Path]:
        """Find all YAML files in directory"""
        return self.find_files("**/*.yaml", directory)

    def find_openapi_files(self) -> List[Path]:
        """Find all OpenAPI YAML files"""
        openapi_dir = self.config.get_openapi_dir()
        if not openapi_dir.exists():
            return []

        return self.find_yaml_files(openapi_dir)

    def is_valid_file_size(self, file_path: Path) -> bool:
        """Check if file size is within limits"""
        max_lines = self.config.get_max_file_lines()
        try:
            lines = len(self.read_text(file_path).splitlines())
            return lines <= max_lines
        except:
            return False

    def validate_file_type(self, file_path: Path) -> bool:
        """Validate file type against project policy"""
        return self.config.is_valid_file_type(file_path)
