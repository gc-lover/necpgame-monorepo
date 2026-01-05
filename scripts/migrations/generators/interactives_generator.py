"""
Interactives migration generator following SOLID principles.
"""

import json
import sys
import uuid
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent.parent.parent
sys.path.insert(0, str(scripts_dir))

from core.config import ConfigManager
from migrations.base import BaseContentMigrationGenerator
from migrations.utils import JsonSerializer


class InteractivesMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for interactives migrations.
    Follows Single Responsibility Principle - handles only interactive processing.
    """

    def __init__(self):
        # Load config first to get output directory
        project_config = ConfigManager()
        output_dir = str(Path(project_config.get('paths', 'migrations_output_dir')) / "knowledge" / "interactives")

        super().__init__(
            name="generate-interactives-migrations",
            description="Generate Liquibase YAML migrations for interactives from knowledge base",
            content_type="interactives",
            input_dirs=[
                "knowledge/canon/interactive-objects",
                "knowledge/content/interactives"
            ],
            output_dir=output_dir,
            table_name="knowledge.interactives"
        )

    def get_content_fields(self) -> List[str]:
        """Required fields for valid interactive content."""
        return ['content']  # New structure uses 'content' field

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract interactive-specific data."""
        metadata = spec.get('metadata', {})
        content = spec.get('content', {})

        # Category mapping from YAML to DB allowed values
        category_mapping = {
            'information_access': 'terminal',
            'data_storage': 'device',
            'data_gateway': 'device',
            'power_control': 'device',
            'underground_trade': 'container',
            'faction_control': 'device',
            'container': 'container',
            'door': 'door',
            'terminal': 'terminal',
            'vehicle': 'vehicle',
            'device': 'device',
            'furniture': 'furniture',
            'decoration': 'decoration'
        }

        # Handle different content structures
        if 'interactives' in content:
            # New structure: content.interactives
            interactives_list = content.get('interactives', [])
            if interactives_list:
                # For now, process first interactive - can be extended for multiple
                interactive_data = interactives_list[0] if isinstance(interactives_list, list) else interactives_list
                yaml_category = interactive_data.get('category', 'container')
                db_category = category_mapping.get(yaml_category, 'container')  # Default to container if unknown

                return {
                    'id': str(uuid.uuid4()),
                    'interactive_id': f"{yaml_file.stem}-{interactive_data.get('name', 'unknown')}",
                    'name': interactive_data.get('display_name', interactive_data.get('name', yaml_file.stem)),
                    'description': interactive_data.get('description', ''),
                    'category': db_category,
                    'location': None,  # Will be set from mechanics if available
                    'interactable': True,
                    'reusable': True,
                    'requires_key': False,
                    'interaction_type': 'examine',
                    'properties': json.dumps(interactive_data.get('mechanics', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
                    'requirements': json.dumps({}, default=JsonSerializer.json_serializer, ensure_ascii=False),
                    'rewards': json.dumps({}, default=JsonSerializer.json_serializer, ensure_ascii=False)
                }

        # Fallback for old structure or no data found
        interactive_def = spec.get('interactive_definition', {})
        yaml_category = interactive_def.get('category', 'container')
        db_category = category_mapping.get(yaml_category, 'container')  # Default to container if unknown

        return {
            'id': str(uuid.uuid4()),
            'interactive_id': metadata.get('id', f"interactive-{yaml_file.stem}"),
            'name': metadata.get('name', yaml_file.stem.replace('_', ' ').title()),
            'description': spec.get('description', ''),
            'category': db_category,
            'location': interactive_def.get('location'),
            'interactable': interactive_def.get('interactable', True),
            'reusable': interactive_def.get('reusable', True),
            'requires_key': interactive_def.get('requires_key', False),
            'interaction_type': interactive_def.get('interaction_type', 'examine'),
            'properties': json.dumps(interactive_def.get('properties', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'requirements': json.dumps(interactive_def.get('requirements', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'rewards': json.dumps(interactive_def.get('rewards', {}), default=JsonSerializer.json_serializer, ensure_ascii=False)
        }
