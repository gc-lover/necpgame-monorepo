#!/usr/bin/env python3
"""
NECPGAME Project Configuration Manager
SOLID: Single Responsibility - manages only configuration
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, Optional
from dataclasses import dataclass


@dataclass
class ProjectConfig:
    """Configuration data class"""
    code_quality: Dict[str, Any]
    paths: Dict[str, str]
    openapi: Dict[str, Any]
    database: Dict[str, Any]
    validation: Dict[str, Any]
    logging: Dict[str, Any]
    performance: Dict[str, Any]
    security: Dict[str, Any]
    tools: Dict[str, Any]
    issues: Dict[str, Any]
    domains: Dict[str, Any]
    backend: Dict[str, Any]
    content: Dict[str, Any]
    github: Dict[str, Any]


class ConfigManager:
    """
    Manages project configuration.
    Single Responsibility: Load and provide configuration.
    """

    def __init__(self, config_path: Optional[str] = None):
        self.config_path = Path(config_path or "project-config.yaml")
        self._config: Optional[ProjectConfig] = None

    def load_config(self) -> ProjectConfig:
        """Load configuration from YAML file"""
        if self._config is not None:
            return self._config

        if not self.config_path.exists():
            raise FileNotFoundError(f"Configuration file not found: {self.config_path}")

        with open(self.config_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        self._config = ProjectConfig(**data)
        return self._config

    def get(self, *keys: str) -> Any:
        """Get nested configuration value"""
        config = self.load_config()
        value = config

        for key in keys:
            if isinstance(value, dict):
                value = value.get(key)
            else:
                value = getattr(value, key, None)

            if value is None:
                return None

        return value

    def get_project_root(self) -> Path:
        """Get project root directory"""
        return Path(self.get('paths', 'project_root') or ".")

    def get_openapi_dir(self) -> Path:
        """Get OpenAPI directory"""
        return self.get_project_root() / self.get('paths', 'openapi_dir')

    def get_services_dir(self) -> Path:
        """Get services directory"""
        return self.get_project_root() / self.get('paths', 'services_dir')

    def get_migrations_dir(self) -> Path:
        """Get migrations directory"""
        return self.get_project_root() / self.get('paths', 'migrations_dir')

    def get_max_file_lines(self) -> int:
        """Get maximum file lines limit"""
        return self.get('code_quality', 'max_file_lines') or 1000

    def get_forbidden_extensions(self) -> list:
        """Get forbidden file extensions"""
        return self.get('code_quality', 'forbidden_extensions') or []

    def is_valid_file_type(self, file_path: Path) -> bool:
        """Check if file type is allowed"""
        forbidden = self.get_forbidden_extensions()
        return file_path.suffix not in forbidden

    def get_github_field_ids(self) -> Dict[str, str]:
        """Get GitHub field IDs"""
        github_config = self.get('github')
        if not github_config:
            return {}
        return {
            'type_field_id': github_config.get('type_field_id'),
            'check_field_id': github_config.get('check_field_id'),
        }

    def get_github_type_options(self) -> Dict[str, str]:
        """Get GitHub TYPE field options"""
        github_config = self.get('github')
        if not github_config:
            return {}
        return github_config.get('type_options', {})

    def get_github_check_options(self) -> Dict[str, str]:
        """Get GitHub CHECK field options"""
        github_config = self.get('github')
        if not github_config:
            return {}
        return github_config.get('check_options', {})
