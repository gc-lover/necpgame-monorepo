"""
Dialogue migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class DialoguesMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for dialogue migrations.
    Follows Single Responsibility Principle - handles only dialogue processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-dialogues-migrations",
            description="Generate Liquibase YAML migrations for dialogues from knowledge base",
            content_type="dialogues",
            input_dirs=[
                "knowledge/canon/narrative/dialogues"
            ],
            output_dir="infrastructure/liquibase/data/narrative/dialogues",
            table_name="narrative.dialogue_nodes"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid dialogue content."""
        return ['content']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract dialogue-specific data."""
        return {
            'node_data': json.dumps(spec.get('content', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'conditions': json.dumps({}, ensure_ascii=False),
            'actions': json.dumps({}, ensure_ascii=False)
        }
