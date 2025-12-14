#!/usr/bin/env python3
# Issue: #1858
# File size and structure validation tool

import os
import re
import yaml
import json
import sys
from pathlib import Path
from typing import Dict, List, Set
import argparse

class FileStructureValidator:
    """File size and structure validation"""

    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.errors: List[str] = []
        self.warnings: List[str] = []

        # File size limits (lines)
        self.size_limits = {
            'go': 500,
            'py': 500,
            'yaml': 500,
            'yml': 500,
            'md': 1000,
            'sql': 800,
            'cpp': 800,
            'h': 600,
            'java': 600,
            'js': 400,
            'ts': 400,
            'html': 300,
            'css': 300
        }

        # Structure patterns to check
        self.structure_patterns = {
            'go': {
                'package_declaration': r'^package\s+\w+',
                'imports': r'^import\s*\(',
                'function_declaration': r'^func\s+',
                'struct_declaration': r'^type\s+\w+\s+struct',
                'interface_declaration': r'^type\s+\w+\s+interface',
            },
            'python': {
                'imports': r'^(import\s+|from\s+.+import\s+)',
                'class_definition': r'^class\s+\w+',
                'function_definition': r'^def\s+\w+',
            },
            'yaml': {
                'metadata_section': r'^metadata:',
                'content_section': r'^content:',
            }
        }

    def validate_all_files(self) -> bool:
        """Validate all files in the project"""
        print("[INFO] Starting file structure validation...")

        total_files = 0
        valid_files = 0

        for file_path in self.project_root.rglob('*'):
            if file_path.is_file() and self._should_check_file(file_path):
                total_files += 1
                if self._validate_single_file(file_path):
                    valid_files += 1

        print(f"[INFO] Checked {total_files} files, {valid_files} valid")
        return self._report_results()

    def _validate_single_file(self, file_path: Path) -> bool:
        """Validate a single file"""
        try:
            # Check file size
            if not self._check_file_size(file_path):
                return False

            # Check file structure
            if not self._check_file_structure(file_path):
                return False

            # Check file encoding (should be UTF-8)
            if not self._check_file_encoding(file_path):
                return False

            return True

        except Exception as e:
            self.errors.append(f"Could not validate {file_path.name}: {e}")
            return False

    def _check_file_size(self, file_path: Path) -> bool:
        """Check file size against limits"""
        extension = file_path.suffix.lower().lstrip('.')

        if extension not in self.size_limits:
            return True  # No limit defined for this file type

        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                lines = f.readlines()

            line_count = len(lines)

            if line_count > self.size_limits[extension]:
                self.errors.append(
                    f"File {file_path.relative_to(self.project_root)} "
                    f"exceeds {self.size_limits[extension]} lines ({line_count} lines)"
                )
                return False

            # Additional check for very long lines (>200 chars)
            long_lines = [i+1 for i, line in enumerate(lines) if len(line.rstrip()) > 200]
            if long_lines:
                self.warnings.append(
                    f"File {file_path.name} has {len(long_lines)} very long lines: {long_lines[:5]}"
                )

        except UnicodeDecodeError:
            self.warnings.append(f"Could not read {file_path.name} as UTF-8")
        except Exception as e:
            self.warnings.append(f"Could not check size for {file_path.name}: {e}")

        return True

    def _check_file_structure(self, file_path: Path) -> bool:
        """Check file structure and formatting"""
        extension = file_path.suffix.lower().lstrip('.')

        if extension not in self.structure_patterns:
            return True  # No specific patterns for this file type

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            patterns = self.structure_patterns[extension]

            if extension == 'go':
                return self._validate_go_structure(content, file_path, patterns)
            elif extension == 'python':
                return self._validate_python_structure(content, file_path, patterns)
            elif extension == 'yaml':
                return self._validate_yaml_structure(content, file_path, patterns)

        except UnicodeDecodeError:
            self.warnings.append(f"Could not read {file_path.name} as UTF-8")
        except Exception as e:
            self.warnings.append(f"Could not check structure for {file_path.name}: {e}")

        return True

    def _validate_go_structure(self, content: str, file_path: Path, patterns: Dict) -> bool:
        """Validate Go file structure"""
        valid = True

        # Check for package declaration
        if not re.search(patterns['package_declaration'], content, re.MULTILINE):
            self.errors.append(f"Missing package declaration in {file_path.name}")
            valid = False

        # Check for proper import formatting
        if 'import' in content:
            if not re.search(patterns['imports'], content, re.MULTILINE):
                # Check for single line imports
                single_imports = re.findall(r'^import\s+"[^"]+"', content, re.MULTILINE)
                if not single_imports:
                    self.warnings.append(f"No proper import block in {file_path.name}")

        # Check for exported functions (capital letters)
        functions = re.findall(r'func\s+([A-Z]\w*)', content)
        if functions:
            self.warnings.append(f"File {file_path.name} contains exported functions: {functions[:3]}")

        # Check for TODO comments
        todos = re.findall(r'// TODO', content, re.IGNORECASE)
        if todos:
            self.warnings.append(f"File {file_path.name} has {len(todos)} TODO comments")

        return valid

    def _validate_python_structure(self, content: str, file_path: Path, patterns: Dict) -> bool:
        """Validate Python file structure"""
        valid = True

        lines = content.split('\n')

        # Check for imports at the top
        import_lines = []
        code_started = False

        for i, line in enumerate(lines):
            stripped = line.strip()
            if not stripped or stripped.startswith('#'):
                continue

            if re.match(patterns['imports'], stripped):
                if code_started:
                    self.warnings.append(f"Import after code in {file_path.name} at line {i+1}")
                import_lines.append(i+1)
            else:
                code_started = True

        # Check for proper docstrings
        if re.search(patterns['class_definition'], content):
            if '"""' not in content and "'''" not in content:
                self.warnings.append(f"Class in {file_path.name} lacks docstring")

        return valid

    def _validate_yaml_structure(self, content: str, file_path: Path, patterns: Dict) -> bool:
        """Validate YAML file structure"""
        valid = True

        try:
            data = yaml.safe_load(content)

            # Check for required sections in content files
            if 'knowledge/canon' in str(file_path):
                if 'metadata' not in data:
                    self.errors.append(f"Missing metadata section in {file_path.name}")
                    valid = False

                if 'content' not in data:
                    self.errors.append(f"Missing content section in {file_path.name}")
                    valid = False

                # Check metadata structure
                if 'metadata' in data:
                    meta = data['metadata']
                    required_meta = ['id', 'title', 'version']
                    for field in required_meta:
                        if field not in meta:
                            self.errors.append(f"Missing metadata.{field} in {file_path.name}")
                            valid = False

        except yaml.YAMLError as e:
            self.errors.append(f"YAML syntax error in {file_path.name}: {e}")
            valid = False

        return valid

    def _check_file_encoding(self, file_path: Path) -> bool:
        """Check file encoding"""
        try:
            with open(file_path, 'rb') as f:
                # Check for BOM (Byte Order Mark)
                bom = f.read(3)
                if bom == b'\xef\xbb\xbf':
                    self.warnings.append(f"File {file_path.name} has UTF-8 BOM")

            # Try to read as UTF-8
            with open(file_path, 'r', encoding='utf-8') as f:
                f.read()

        except UnicodeDecodeError:
            self.errors.append(f"File {file_path.name} is not valid UTF-8")
            return False

        return True

    def _should_check_file(self, file_path: Path) -> bool:
        """Check if file should be validated"""
        # Skip certain directories and files
        skip_patterns = [
            '.git/',
            'node_modules/',
            'vendor/',
            '.pytest_cache/',
            '__pycache__/',
            '*.pyc',
            '*.log',
            '*.tmp',
            '*.swp',
            '*.bak',
            'generated/',
            'dist/',
            'build/',
            '.next/',
            'coverage/',
            '*.min.js',
            '*.min.css'
        ]

        file_str = str(file_path)

        for pattern in skip_patterns:
            if pattern in file_str or file_path.name.startswith('.'):
                return False

        # Only check specific file types
        check_extensions = {'.go', '.py', '.yaml', '.yml', '.md', '.sql', '.cpp', '.h', '.java', '.js', '.ts'}

        return file_path.suffix.lower() in check_extensions

    def _report_results(self) -> bool:
        """Report validation results"""
        total_errors = len(self.errors)
        total_warnings = len(self.warnings)

        print("\n[RESULTS] File Structure Validation Results:")
        print(f"   Errors: {total_errors}")
        print(f"   Warnings: {total_warnings}")

        if total_errors > 0:
            print("\n[ERROR] ERRORS:")
            for error in self.errors[:10]:
                print(f"   - {error}")
            if len(self.errors) > 10:
                print(f"   ... and {len(self.errors) - 10} more")

        if total_warnings > 0:
            print("\n[WARNING] WARNINGS:")
            for warning in self.warnings[:10]:
                print(f"   - {warning}")
            if len(self.warnings) > 10:
                print(f"   ... and {len(self.warnings) - 10} more")

        if total_errors == 0:
            print("\n[SUCCESS] All file structure checks passed!")
            return True
        else:
            print(f"\n[ERROR] {total_errors} file structure violations found.")
            return False


def main():
    parser = argparse.ArgumentParser(description='NECPGAME File Structure Validator')
    parser.add_argument('--project-root', default='.', help='Project root directory')
    parser.add_argument('--file', help='Validate only specific file')
    parser.add_argument('--type', choices=['size', 'structure', 'encoding'],
                       help='Check only specific validation type')

    args = parser.parse_args()

    validator = FileStructureValidator(args.project_root)

    if args.file:
        file_path = Path(args.project_root) / args.file
        success = validator._validate_single_file(file_path)
    else:
        success = validator.validate_all_files()

    return 0 if success else 1


if __name__ == '__main__':
    sys.exit(main())