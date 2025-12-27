"""
NPC migration generator for new format (metadata-based).
Handles NPC files with metadata, appearance, personality, background structure.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class NpcsV2MigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for new format NPC migrations.
    Handles metadata-based NPC structure with appearance, personality, background sections.
    """

    def __init__(self):
        super().__init__(
            name="generate-npcs-v2-migrations",
            description="Generate Liquibase YAML migrations for NPCs from new metadata-based format",
            content_type="npcs",
            input_dirs=[
                "knowledge/canon/narrative/npc-lore"
            ],
            output_dir="infrastructure/liquibase/data/narrative/npcs",
            table_name="narrative.npc_definitions"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid NPC content."""
        return ['metadata.id']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract NPC-specific data from new metadata format."""
        metadata = spec.get('metadata', {})

        # Extract basic fields
        name = metadata.get('name') or metadata.get('title', 'Unknown NPC')

        # Handle aliases if present
        aliases = metadata.get('aliases', [])
        if aliases:
            # Add aliases to name if they exist
            name = f"{name} ({', '.join(aliases)})"

        # Extract faction (try different possible locations)
        faction = metadata.get('faction')
        if not faction:
            # Try to extract from content if available
            content = spec.get('content', {})
            if isinstance(content, dict):
                for section in content.get('sections', []):
                    if isinstance(section, dict) and 'body' in section:
                        body = section.get('body', '')
                        if 'Faction:' in body or 'faction' in body.lower():
                            # Simple extraction - can be improved
                            pass

        # Extract location
        location = metadata.get('location')

        # Extract role
        role = metadata.get('role')

        # Build appearance JSON from available data
        appearance_data = {}

        # Try to get appearance section if it exists
        content = spec.get('content', {})
        if isinstance(content, dict):
            sections = content.get('sections', [])
            for section in sections:
                if isinstance(section, dict) and section.get('id') == 'appearance':
                    # This would be the full appearance section
                    # For now, extract basic info from metadata
                    break

        # If no detailed appearance, use metadata info
        if 'appearance' not in spec:
            appearance_data = {
                'age': metadata.get('age'),
                'gender': metadata.get('gender'),
                'ethnicity': metadata.get('ethnicity'),
                'height': metadata.get('height'),
                'build': metadata.get('build'),
                'hair': metadata.get('hair'),
                'eyes': metadata.get('eyes'),
                'clothing_style': metadata.get('clothing_style'),
                'distinctive_features': metadata.get('distinctive_features')
            }
        else:
            appearance_data = spec['appearance']

        # Build stats JSON (empty for now, can be expanded)
        stats_data = {}

        return {
            'name': name,
            'faction': faction,
            'location': location,
            'role': role,
            'appearance': json.dumps(appearance_data, default=JsonSerializer.json_serializer, ensure_ascii=False),
            'stats': json.dumps(stats_data, default=JsonSerializer.json_serializer, ensure_ascii=False)
        }

