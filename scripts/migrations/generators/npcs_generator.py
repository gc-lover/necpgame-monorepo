"""
NPC migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class NpcsMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for NPC migrations.
    Follows Single Responsibility Principle - handles only NPC processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-npcs-migrations",
            description="Generate Liquibase YAML migrations for NPCs from knowledge base",
            content_type="npcs",
            input_dirs=[
                "knowledge/canon/narrative/npc-lore"
            ],
            output_dir="infrastructure/liquibase/data/narrative/npcs",
            table_name="narrative.npc_definitions"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid NPC content."""
        return ['content.identity']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract NPC-specific data."""
        identity = spec.get('content', {}).get('identity', {})
        context = spec.get('content', {}).get('context', {})

        return {
            'name': identity.get('name', 'Unknown NPC'),
            'faction': identity.get('faction_affiliation'),
            'location': context.get('locations', [None])[0] if context.get('locations') else None,
            'role': identity.get('type', 'unknown'),
            'appearance': json.dumps(spec.get('content', {}).get('appearance_style', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'stats': json.dumps(spec.get('content', {}).get('mechanics', {}).get('combat_stats', {}), default=JsonSerializer.json_serializer, ensure_ascii=False)
        }
