"""
Enemies migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class EnemiesMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for enemies migrations.
    Follows Single Responsibility Principle - handles only enemy processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-enemies-migrations",
            description="Generate Liquibase YAML migrations for enemies from knowledge base",
            content_type="enemies",
            input_dirs=[
                "knowledge/canon/ai-enemies",
                "knowledge/content/enemies"
            ],
            output_dir="infrastructure/liquibase/data/knowledge/enemies",
            table_name="knowledge.enemies"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid enemy content."""
        return ['enemy_definition']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract enemy-specific data."""
        metadata = spec.get('metadata', {})
        enemy_def = spec.get('enemy_definition', {})

        return {
            'enemy_id': metadata.get('id', f"enemy-{yaml_file.stem}"),
            'name': metadata.get('name', yaml_file.stem.replace('_', ' ').title()),
            'description': spec.get('description', ''),
            'category': enemy_def.get('category', 'human'),
            'faction': enemy_def.get('faction'),
            'level_min': enemy_def.get('level_min', 1),
            'level_max': enemy_def.get('level_max', 1),
            'location': enemy_def.get('spawn_locations', ['unknown'])[0] if enemy_def.get('spawn_locations') else 'unknown',
            'behavior': enemy_def.get('behavior', 'aggressive'),
            'stats': json.dumps(enemy_def.get('stats', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'abilities': json.dumps(enemy_def.get('abilities', []), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'loot_table': json.dumps(enemy_def.get('loot_table', {}), default=JsonSerializer.json_serializer, ensure_ascii=False)
        }
