#!/usr/bin/env python3
"""
Quest Validation Script
Validates all quest YAML files in the knowledge/canon/lore/timeline-author/quests/ directory
for proper structure, required fields, and data consistency.

Issue: #616
"""

import argparse
import sys
from pathlib import Path
from typing import Dict, Any, List
from dataclasses import dataclass
from enum import Enum


class ValidationSeverity(Enum):
    ERROR = "ERROR"
    WARNING = "WARNING"
    INFO = "INFO"


@dataclass
class ValidationIssue:
    file_path: str
    severity: ValidationSeverity
    field: str
    message: str
    line_number: int = None


class QuestValidator:
    """Validates quest YAML structure and content"""

    def __init__(self):
        self.issues: List[ValidationIssue] = []

    def validate_quest_file(self, quest_file: Path) -> bool:
        """Validate a single quest file"""
        try:
            import yaml
            with open(quest_file, 'r', encoding='utf-8') as f:
                quest_data = yaml.safe_load(f)
        except Exception as e:
            self.issues.append(ValidationIssue(
                file_path=str(quest_file),
                severity=ValidationSeverity.ERROR,
                field="file",
                message=f"Failed to parse YAML: {e}"
            ))
            return False

        return self._validate_quest_structure(quest_data, quest_file)

    def _validate_quest_structure(self, quest_data: Dict[str, Any], quest_file: Path) -> bool:
        """Validate quest data structure"""
        file_path = str(quest_file)

        # Check required top-level sections
        required_sections = ['metadata', 'summary', 'content']
        for section in required_sections:
            if section not in quest_data:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.ERROR,
                    field=section,
                    message=f"Missing required section: {section}"
                ))

        # Validate metadata
        if 'metadata' in quest_data:
            self._validate_metadata(quest_data['metadata'], file_path)

        # Validate summary
        if 'summary' in quest_data:
            self._validate_summary(quest_data['summary'], file_path)

        # Validate content
        if 'content' in quest_data:
            self._validate_content(quest_data['content'], file_path)

        # Validate quest_definition if present
        if 'quest_definition' in quest_data:
            self._validate_quest_definition(quest_data['quest_definition'], file_path)

        # Check for emoji ban compliance
        self._validate_emoji_free(quest_data, file_path)

        return len([i for i in self.issues if i.file_path == file_path and i.severity == ValidationSeverity.ERROR]) == 0

    def _validate_metadata(self, metadata: Dict[str, Any], file_path: str):
        """Validate metadata section"""
        required_fields = ['id', 'title', 'document_type', 'category', 'status', 'version']

        for field in required_fields:
            if field not in metadata:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.ERROR,
                    field=f"metadata.{field}",
                    message=f"Missing required metadata field: {field}"
                ))

        # Validate ID format
        if 'id' in metadata:
            quest_id = metadata['id']
            if not quest_id.startswith('canon-quest-'):
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.WARNING,
                    field="metadata.id",
                    message=f"Quest ID should start with 'canon-quest-': {quest_id}"
                ))

        # Validate version format
        if 'version' in metadata:
            version = metadata['version']
            if not isinstance(version, str) or not version.replace('.', '').isdigit():
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.WARNING,
                    field="metadata.version",
                    message=f"Version should be a semantic version string: {version}"
                ))

        # Validate status
        if 'status' in metadata:
            valid_statuses = ['draft', 'review', 'approved', 'published']
            if metadata['status'] not in valid_statuses:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.WARNING,
                    field="metadata.status",
                    message=f"Invalid status. Should be one of: {', '.join(valid_statuses)}"
                ))

    def _validate_summary(self, summary: Dict[str, Any], file_path: str):
        """Validate summary section"""
        required_fields = ['problem', 'goal', 'essence']

        for field in required_fields:
            if field not in summary:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.ERROR,
                    field=f"summary.{field}",
                    message=f"Missing required summary field: {field}"
                ))

    def _validate_content(self, content: Dict[str, Any], file_path: str):
        """Validate content section"""
        if 'sections' not in content:
            self.issues.append(ValidationIssue(
                file_path=file_path,
                severity=ValidationSeverity.ERROR,
                field="content.sections",
                message="Missing content.sections"
            ))
            return

        sections = content['sections']
        if not isinstance(sections, list):
            self.issues.append(ValidationIssue(
                file_path=file_path,
                severity=ValidationSeverity.ERROR,
                field="content.sections",
                message="content.sections should be a list"
            ))
            return

        required_section_ids = ['overview', 'stages']
        section_ids = [s.get('id') for s in sections if isinstance(s, dict)]

        for required_id in required_section_ids:
            if required_id not in section_ids:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.ERROR,
                    field="content.sections",
                    message=f"Missing required section: {required_id}"
                ))

    def _validate_quest_definition(self, quest_def: Dict[str, Any], file_path: str):
        """Validate quest_definition section"""
        required_fields = ['quest_type', 'level_min', 'level_max']

        for field in required_fields:
            if field not in quest_def:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.ERROR,
                    field=f"quest_definition.{field}",
                    message=f"Missing required quest_definition field: {field}"
                ))

        # Validate objectives
        if 'objectives' in quest_def:
            objectives = quest_def['objectives']
            if not isinstance(objectives, list) or len(objectives) == 0:
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.WARNING,
                    field="quest_definition.objectives",
                    message="quest_definition.objectives should be a non-empty list"
                ))

        # Validate rewards
        if 'rewards' in quest_def:
            rewards = quest_def['rewards']
            if not isinstance(rewards, dict):
                self.issues.append(ValidationIssue(
                    file_path=file_path,
                    severity=ValidationSeverity.WARNING,
                    field="quest_definition.rewards",
                    message="quest_definition.rewards should be a dictionary"
                ))

    def _validate_emoji_free(self, quest_data: Dict[str, Any], file_path: str):
        """Check for forbidden Unicode characters (emoji ban)"""
        import re

        def check_value(value, path=""):
            if isinstance(value, str):
                # Check for emoji and special Unicode characters
                emoji_pattern = r'[\U0001F600-\U0001F64F\U0001F300-\U0001F5FF\U0001F680-\U0001F6FF\U0001F1E0-\U0001F1FF\u2600-\u26FF\u2700-\u27BF]'
                special_unicode = r'[\u25A0-\u25FF\u27C0-\u27EF\u29F0-\u29FF\u2B00-\u2BFF]'

                if re.search(emoji_pattern, value):
                    self.issues.append(ValidationIssue(
                        file_path=file_path,
                        severity=ValidationSeverity.ERROR,
                        field=path,
                        message="Forbidden emoji detected in content"
                    ))

                if re.search(special_unicode, value):
                    self.issues.append(ValidationIssue(
                        file_path=file_path,
                        severity=ValidationSeverity.WARNING,
                        field=path,
                        message="Special Unicode character detected (consider using ASCII equivalent)"
                    ))

            elif isinstance(value, dict):
                for k, v in value.items():
                    check_value(v, f"{path}.{k}" if path else k)
            elif isinstance(value, list):
                for i, item in enumerate(value):
                    check_value(item, f"{path}[{i}]")

        check_value(quest_data)


def main():
    parser = argparse.ArgumentParser(
        description='Validate all quest YAML files for proper structure and content'
    )
    parser.add_argument(
        '--quests-dir', '-d',
        type=str,
        default='knowledge/canon/lore/timeline-author/quests',
        help='Root directory containing quest YAML files'
    )
    parser.add_argument(
        '--output-file', '-o',
        type=str,
        help='Output file for validation report (JSON format)'
    )
    parser.add_argument(
        '--max-files', '-m',
        type=int,
        default=None,
        help='Maximum number of files to validate'
    )
    parser.add_argument(
        '--fail-on-warnings',
        action='store_true',
        help='Treat warnings as errors'
    )

    args = parser.parse_args()

    quests_dir = Path(args.quests_dir)

    # Validate input directory
    if not quests_dir.exists():
        print(f"ERROR: Quests directory not found: {quests_dir}")
        sys.exit(1)

    # Find all YAML files
    yaml_files = list(quests_dir.rglob('*.yaml'))
    yaml_files.sort()

    if not yaml_files:
        print(f"ERROR: No YAML files found in {quests_dir}")
        sys.exit(1)

    print(f"Found {len(yaml_files)} YAML files to validate")

    if args.max_files:
        yaml_files = yaml_files[:args.max_files]
        print(f"Limiting to first {args.max_files} files")

    # Initialize validator
    validator = QuestValidator()

    # Validate each file
    validated = 0
    for quest_file in yaml_files:
        print(f"Validating: {quest_file.relative_to(quests_dir)}")
        validator.validate_quest_file(quest_file)
        validated += 1

    # Generate report
    errors = [i for i in validator.issues if i.severity == ValidationSeverity.ERROR]
    warnings = [i for i in validator.issues if i.severity == ValidationSeverity.WARNING]

    print("\n=== VALIDATION REPORT ===")
    print(f"Files validated: {validated}")
    print(f"Total issues: {len(validator.issues)}")
    print(f"Errors: {len(errors)}")
    print(f"Warnings: {len(warnings)}")

    if errors:
        print("\n[ERROR] ERRORS:")
        for error in errors[:20]:  # Show first 20 errors
            print(f"  {error.file_path}: {error.field} - {error.message}")

        if len(errors) > 20:
            print(f"  ... and {len(errors) - 20} more errors")

    if warnings:
        print("\n[WARNING] WARNINGS:")
        for warning in warnings[:10]:  # Show first 10 warnings
            print(f"  {warning.file_path}: {warning.field} - {warning.message}")

        if len(warnings) > 10:
            print(f"  ... and {len(warnings) - 10} more warnings")

    # Save detailed report if requested
    if args.output_file:
        import json
        report = {
            'summary': {
                'files_validated': validated,
                'total_issues': len(validator.issues),
                'errors': len(errors),
                'warnings': len(warnings)
            },
            'issues': [
                {
                    'file': i.file_path,
                    'severity': i.severity.value,
                    'field': i.field,
                    'message': i.message,
                    'line': i.line_number
                }
                for i in validator.issues
            ]
        }

        with open(args.output_file, 'w', encoding='utf-8') as f:
            json.dump(report, f, ensure_ascii=False, indent=2)

        print(f"\nDetailed report saved to: {args.output_file}")

    # Exit code based on errors
    has_failures = len(errors) > 0 or (args.fail_on_warnings and len(warnings) > 0)

    if has_failures:
        print("\n[ERROR] Validation FAILED")
        sys.exit(1)
    else:
        print("\n[SUCCESS] Validation PASSED")
        sys.exit(0)


if __name__ == '__main__':
    main()
