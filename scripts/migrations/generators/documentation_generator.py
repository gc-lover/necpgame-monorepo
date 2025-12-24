"""
Documentation migration generator following SOLID principles.
Processes all YAML files with metadata from knowledge/ directory.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class DocumentationMigrationGenerator(BaseContentMigrationGenerator):
    """
    Universal documentation generator for all YAML files with metadata.
    Processes all project documentation from knowledge/ directory.
    """

    def __init__(self):
        super().__init__(
            name="generate-documentation-migrations",
            description="Generate Liquibase YAML migrations for all project documentation from knowledge base",
            content_type="documentation",
            input_dirs=[
                "knowledge/analysis",
                "knowledge/canon",
                "knowledge/content",
                "knowledge/design",
                "knowledge/implementation",
                "knowledge/mechanics"
            ],
            output_dir="infrastructure/liquibase/data/project/documentation",
            table_name="project.documentation"
        )

    def get_knowledge_dirs(self) -> List[Path]:
        """Get all knowledge directories to scan."""
        dirs = []
        for input_dir in self.input_dirs:
            full_path = self.project_root / input_dir
            if full_path.exists():
                # Recursively find all subdirectories
                dirs.extend([p for p in full_path.rglob("*") if p.is_dir()])
                # Also include the root directory
                dirs.append(full_path)
        return list(set(dirs))  # Remove duplicates

    def get_content_fields(self) -> List[str]:
        """Documentation generator accepts any file with metadata."""
        return []  # No additional field requirements - any file with metadata is valid

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract documentation-specific data."""
        metadata = spec.get('metadata', {})

        return {
            'doc_id': metadata.get('id'),
            'title': metadata.get('title', ''),
            'document_type': metadata.get('document_type', ''),
            'category': metadata.get('category', ''),
            'status': metadata.get('status', ''),
            'version': metadata.get('version', ''),
            'last_updated': metadata.get('last_updated'),
            'concept_approved': metadata.get('concept_approved', False),
            'concept_reviewed_at': metadata.get('concept_reviewed_at'),
            'owners': json.dumps(metadata.get('owners', []), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'tags': metadata.get('tags', []),
            'topics': metadata.get('topics', []),
            'related_systems': metadata.get('related_systems', []),
            'related_documents': json.dumps(metadata.get('related_documents', []), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'source': metadata.get('source', ''),
            'visibility': metadata.get('visibility', ''),
            'audience': metadata.get('audience', []),
            'risk_level': metadata.get('risk_level', ''),
            'content': json.dumps(spec, default=JsonSerializer.json_serializer, ensure_ascii=False),  # Full content!
            'source_file': str(yaml_file.relative_to(self.project_root))
        }
