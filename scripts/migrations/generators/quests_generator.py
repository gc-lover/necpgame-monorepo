"""
Quest migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class QuestMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for quest migrations.
    Follows Single Responsibility Principle - handles only quest processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-quests-migrations",
            description="Generate Liquibase YAML migrations for quests from knowledge base",
            content_type="quests",
            input_dirs=[
                "knowledge/canon/narrative/quests",
                "knowledge/canon/lore/timeline-author/quests",
                "knowledge/content/quests"
            ],
            output_dir="infrastructure/liquibase/data/gameplay/quests",
            table_name="gameplay.quest_definitions"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid quest content."""
        return ['quest_definition']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract quest-specific data."""
        metadata = spec.get('metadata', {})
        quest_def = spec.get('quest_definition', {})

        return {
            'quest_id': metadata.get('id', f"quest-{yaml_file.stem}"),
            'title': metadata.get('title', 'Unknown Quest'),
            'description': spec.get('summary', {}).get('essence', ''),
            'status': 'active',  # Default status from schema
            'level_min': quest_def.get('level_min'),
            'level_max': quest_def.get('level_max'),
            'rewards': json.dumps(quest_def.get('rewards', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'objectives': json.dumps(quest_def.get('objectives', []), default=JsonSerializer.json_serializer, ensure_ascii=False)
        }
