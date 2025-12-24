"""
Culture migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class CultureMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for culture migrations.
    Follows Single Responsibility Principle - handles only culture processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-culture-migrations",
            description="Generate Liquibase YAML migrations for culture from knowledge base",
            content_type="culture",
            input_dirs=[
                "knowledge/canon/culture",
                "knowledge/canon/lore/culture"
            ],
            output_dir="infrastructure/liquibase/data/knowledge/culture",
            table_name="knowledge.lore_entries"
        )

    def process_content_file(self, yaml_file: Path) -> Dict[str, Any]:
        """Process culture YAML file into database record format."""
        try:
            import yaml
            with open(yaml_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f) or {}
        except Exception as e:
            print(f"Error loading {yaml_file}: {e}")
            return {}

        metadata = spec.get('metadata', {})

        # Skip README files - they should be handled by documentation generator
        if 'readme' in yaml_file.name.lower():
            print(f"Skipping README file {yaml_file.name} - handled by documentation generator")
            return {}

        # Skip files that don't have culture content
        content = spec.get('content', {})
        if not content:
            print(f"Skipping {yaml_file.name} - no culture content found")
            return {}

        return {
            'title': metadata.get('title', yaml_file.stem.replace('_', ' ').title()),
            'content': spec.get('content', {}).get('text', ''),
            'category': 'culture',  # Fixed category for culture entries
            'tags': json.dumps(spec.get('metadata', {}).get('tags', ['culture']), ensure_ascii=False),
            'related_entities': json.dumps([], ensure_ascii=False)  # Will be populated based on content analysis
        }
