"""
Interactives migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class InteractivesMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for interactives migrations.
    Follows Single Responsibility Principle - handles only interactive processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-interactives-migrations",
            description="Generate Liquibase YAML migrations for interactives from knowledge base",
            content_type="interactives",
            input_dirs=[
                "knowledge/canon/interactive-objects",
                "knowledge/content/interactives"
            ],
            output_dir="infrastructure/liquibase/data/knowledge/interactives",
            table_name="knowledge.interactives"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid interactive content."""
        return ['interactive_definition']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract interactive-specific data."""
        metadata = spec.get('metadata', {})
        interactive_def = spec.get('interactive_definition', {})

        return {
            'interactive_id': metadata.get('id', f"interactive-{yaml_file.stem}"),
            'name': metadata.get('name', yaml_file.stem.replace('_', ' ').title()),
            'description': spec.get('description', ''),
            'category': interactive_def.get('category', 'container'),
            'location': interactive_def.get('location'),
            'interactable': interactive_def.get('interactable', True),
            'reusable': interactive_def.get('reusable', True),
            'requires_key': interactive_def.get('requires_key', False),
            'interaction_type': interactive_def.get('interaction_type', 'examine'),
            'properties': json.dumps(interactive_def.get('properties', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'requirements': json.dumps(interactive_def.get('requirements', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'rewards': json.dumps(interactive_def.get('rewards', {}), default=JsonSerializer.json_serializer, ensure_ascii=False)
        }
