#!/usr/bin/env python3
"""
NECPGAME Rome Quests Validation Script
Validates all created Rome quests for structure compliance and data integrity

Usage:
    python scripts/validate-rome-quests.py

Author: Backend Agent
Issue: #validate-rome-quests
"""

import yaml
import os
import sys
from pathlib import Path
from typing import Dict, List, Any

class RomeQuestsValidator:
    """Validates Rome quest YAML files for structure and content"""

    def __init__(self):
        self.quests_dir = Path("knowledge/canon/narrative/quests")
        self.errors = []
        self.warnings = []

    def validate_all_rome_quests(self) -> bool:
        """Validate all Rome quest files"""
        print("Validating Rome Quests Structure")
        print("=" * 50)

        # Find all Rome quest files
        rome_quest_files = [
            "vatican-hack-rome-2077-2080.yaml",
            "colosseum-gladiators-rome-2080-2085.yaml",
            "pantheon-ai-rome-2085-2090.yaml",
            "tiber-cyber-river-rome-2090-2093.yaml",
            "spanish-steps-hackers-rome-2077-2080.yaml"
        ]

        all_valid = True

        for quest_file in rome_quest_files:
            file_path = self.quests_dir / quest_file
            if not file_path.exists():
                self.errors.append(f"Quest file not found: {quest_file}")
                all_valid = False
                continue

            print(f"\nValidating: {quest_file}")
            if not self.validate_quest_file(file_path):
                all_valid = False

        # Summary
        print("\n" + "=" * 50)
        print("VALIDATION SUMMARY")
        print("=" * 50)

        if self.errors:
            print(f"ERRORS ({len(self.errors)}):")
            for error in self.errors:
                print(f"   {error}")

        if self.warnings:
            print(f"WARNINGS ({len(self.warnings)}):")
            for warning in self.warnings:
                print(f"   {warning}")

        if all_valid and not self.errors:
            print("All Rome quests validated successfully!")
            return True
        else:
            print("Validation failed!")
            return False

    def validate_quest_file(self, file_path: Path) -> bool:
        """Validate a single quest file"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)
        except Exception as e:
            self.errors.append(f"{file_path.name}: Failed to parse YAML - {e}")
            return False

        # Validate metadata
        if not self.validate_metadata(data, file_path.name):
            return False

        # Validate summary
        if not self.validate_summary(data, file_path.name):
            return False

        # Validate content
        if not self.validate_content(data, file_path.name):
            return False

        # Validate quest_definition
        if not self.validate_quest_definition(data, file_path.name):
            return False

        # Validate review
        if not self.validate_review(data, file_path.name):
            return False

        print(f"   OK {file_path.name} - Valid")
        return True

    def validate_metadata(self, data: Dict, filename: str) -> bool:
        """Validate metadata section"""
        if 'metadata' not in data:
            self.errors.append(f"{filename}: Missing required section 'metadata'")
            return False

        metadata = data['metadata']
        required_fields = ['id', 'document_type', 'category', 'status', 'version']

        for field in required_fields:
            if field not in metadata:
                self.errors.append(f"{filename}: metadata.{field} - Missing required field")
                return False

        # Validate ID format
        if not metadata['id'].startswith('canon-quest-'):
            self.errors.append(f"{filename}: metadata.id must start with 'canon-quest-'")
            return False

        # Validate category
        if metadata['category'] != 'narrative':
            self.errors.append(f"{filename}: metadata.category must be 'narrative'")
            return False

        # Validate document_type
        if metadata['document_type'] != 'content':
            self.errors.append(f"{filename}: metadata.document_type must be 'content'")
            return False

        return True

    def validate_summary(self, data: Dict, filename: str) -> bool:
        """Validate summary section"""
        if 'summary' not in data:
            self.errors.append(f"{filename}: Missing required section 'summary'")
            return False

        summary = data['summary']
        required_fields = ['problem', 'goal', 'essence', 'key_points']

        for field in required_fields:
            if field not in summary:
                self.errors.append(f"{filename}: summary.{field} - Missing required field")
                return False

        # Validate key_points is a list
        if not isinstance(summary['key_points'], list):
            self.errors.append(f"{filename}: summary.key_points must be a list")
            return False

        if len(summary['key_points']) < 3:
            self.warnings.append(f"{filename}: summary.key_points should have at least 3 points")

        return True

    def validate_content(self, data: Dict, filename: str) -> bool:
        """Validate content section"""
        if 'content' not in data:
            self.errors.append(f"{filename}: Missing required section 'content'")
            return False

        content = data['content']

        if 'sections' not in content:
            self.errors.append(f"{filename}: content.sections - Missing required field")
            return False

        sections = content['sections']
        if not isinstance(sections, list):
            self.errors.append(f"{filename}: content.sections must be a list")
            return False

        # Check for required sections
        section_ids = [s.get('id') for s in sections]
        required_sections = ['overview', 'stages']

        for req_section in required_sections:
            if req_section not in section_ids:
                self.errors.append(f"{filename}: content.sections - Missing required section '{req_section}'")
                return False

        # Validate each section has required fields
        for section in sections:
            if 'id' not in section or 'title' not in section or 'body' not in section:
                self.errors.append(f"{filename}: content.sections - Section missing required fields (id, title, body)")
                return False

        return True

    def validate_quest_definition(self, data: Dict, filename: str) -> bool:
        """Validate quest_definition section"""
        if 'quest_definition' not in data:
            self.errors.append(f"{filename}: Missing required section 'quest_definition'")
            return False

        qd = data['quest_definition']

        # Validate quest_type
        if 'quest_type' not in qd:
            self.errors.append(f"{filename}: quest_definition.quest_type - Missing required field")
            return False

        if qd['quest_type'] != 'narrative_main':
            self.errors.append(f"{filename}: quest_definition.quest_type must be 'narrative_main'")
            return False

        # Validate status
        if qd.get('status') != 'available':
            self.errors.append(f"{filename}: quest_definition.status must be 'available'")
            return False

        # Validate rewards
        if 'rewards' not in qd:
            self.errors.append(f"{filename}: quest_definition.rewards - Missing required field")
            return False

        rewards = qd['rewards']
        if not isinstance(rewards, list):
            self.errors.append(f"{filename}: quest_definition.rewards must be a list")
            return False

        # Validate objectives
        if 'objectives' not in qd:
            self.errors.append(f"{filename}: quest_definition.objectives - Missing required field")
            return False

        objectives = qd['objectives']
        if not isinstance(objectives, list):
            self.errors.append(f"{filename}: quest_definition.objectives must be a list")
            return False

        if len(objectives) < 3:
            self.warnings.append(f"{filename}: quest_definition.objectives should have at least 3 objectives")

        return True

    def validate_review(self, data: Dict, filename: str) -> bool:
        """Validate review section"""
        if 'review' not in data:
            self.errors.append(f"{filename}: Missing required section 'review'")
            return False

        review = data['review']

        # Rating should be reasonable
        if 'rating' in review:
            rating = review['rating']
            if not isinstance(rating, (int, float)) or rating < 1 or rating > 10:
                self.warnings.append(f"{filename}: review.rating should be between 1-10")

        return True

def main():
    """Main validation function"""
    validator = RomeQuestsValidator()

    if validator.validate_all_rome_quests():
        print("\nAll validations passed!")
        sys.exit(0)
    else:
        print("\nValidation failed!")
        sys.exit(1)

if __name__ == "__main__":
    main()
