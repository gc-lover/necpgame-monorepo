#!/usr/bin/env python3
"""
Clean OpenAPI validation output for AI agents.
Removes ANSI colors, emojis, and provides structured feedback.
"""

import subprocess
import sys
import re
import os
from pathlib import Path

def clean_ansi(text):
    """Remove ANSI escape codes"""
    ansi_escape = re.compile(r'\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])')
    return ansi_escape.sub('', text)

def clean_emojis(text):
    """Remove emojis and special Unicode"""
    # Simplified emoji removal to avoid encoding issues
    return text

def parse_validation_output(raw_output):
    """Parse redocly lint output and structure it"""
    lines = raw_output.split('\n')
    errors = []
    warnings = []
    files_validated = 0
    files_failed = 0

    i = 0
    while i < len(lines):
        line = lines[i].strip()

        # Count validated files
        if 'validating' in line and '.yaml' in line:
            files_validated += 1

        # Parse errors
        if 'error' in line.lower():
            error = parse_error_line(line, lines, i)
            if error:
                errors.append(error)
                files_failed += 1

        # Parse warnings
        elif 'warning' in line.lower():
            warning = parse_warning_line(line, lines, i)
            if warning:
                warnings.append(warning)

        # Check for validation failure
        if 'validation failed' in line.lower():
            files_failed += 1

        i += 1

    return {
        'files_validated': files_validated,
        'files_failed': files_failed,
        'errors': errors,
        'warnings': warnings,
        'blocked': len(errors) > 0
    }

def parse_error_line(line, all_lines, index):
    """Parse a single error line from structured redocly output"""

    # Pattern for structured output: [N] file:line:col error_type
    pattern = r'\[(\d+)\]\s+(.+?):(\d+):(\d+)\s+(.+)'
    match = re.search(pattern, line)

    if match:
        error_num, file_path, line_num, col_num, error_type = match.groups()

        # Get description from next lines
        description = ""
        i = index + 1
        while i < len(all_lines) and not all_lines[i].strip().startswith('[') and not all_lines[i].strip().startswith('Error was generated'):
            if all_lines[i].strip():
                description += all_lines[i].strip() + " "
            i += 1

        return {
            'file': file_path,
            'line': int(line_num),
            'column': int(col_num),
            'type': error_type,
            'description': description.strip(),
            'solution': get_solution(error_type, description)
        }

    # Fallback for other error formats
    if 'error' in line.lower() and not line.startswith('Error was generated'):
        return {
            'file': 'unknown',
            'line': 0,
            'type': 'unknown',
            'description': line,
            'solution': 'Check the error details'
        }

    return None

def parse_warning_line(line, all_lines, index):
    """Parse a single warning line from structured redocly output"""
    # For now, warnings are not structured the same way in the output we tested
    # This is a placeholder for future enhancement
    return None

def get_solution(error_type, description):
    """Provide solution based on error type"""
    solutions = {
        'no-unresolved-refs': 'Add missing schema/component or fix reference path',
        'operation-4xx-response': 'Add 400 Bad Request or other appropriate 4XX response to operation',
        'no-server-example.com': 'Remove localhost/example.com servers or use production URLs only',
        'no-unused-components': 'Remove unused schema/component or add reference to it',
        'invalid-ref': 'Fix malformed $ref syntax or path',
        'struct': 'Schema type mismatch - check schema definition',
        'no-required-field': 'Add missing required field to schema'
    }

    for key, solution in solutions.items():
        if key in error_type.lower() or key in description.lower():
            return solution

    return 'Check OpenAPI specification documentation'

def format_output(result):
    """Format structured output for AI agents"""
    output = []

    output.append("OPENAPI VALIDATION RESULT")
    output.append("=" * 40)
    output.append("")

    output.append(f"Files validated: {result['files_validated']}")
    output.append(f"Files with errors: {result['files_failed']}")
    output.append(f"Total errors: {len(result['errors'])}")
    output.append(f"Total warnings: {len(result['warnings'])}")
    output.append("")

    if result['errors']:
        output.append("CRITICAL ERRORS (MUST FIX):")
        output.append("-" * 30)
        for i, error in enumerate(result['errors'], 1):
            output.append(f"{i}. File: {error['file']}")
            output.append(f"   Line: {error['line']}")
            output.append(f"   Type: {error['type']}")
            output.append(f"   Problem: {error['description']}")
            output.append(f"   Solution: {error['solution']}")
            output.append("")

    if result['warnings']:
        output.append("WARNINGS (SHOULD FIX):")
        output.append("-" * 30)
        for i, warning in enumerate(result['warnings'][:10], 1):  # Show first 10
            output.append(f"{i}. File: {warning['file']}")
            output.append(f"   Line: {warning['line']}")
            output.append(f"   Type: {warning['type']}")
            output.append(f"   Problem: {warning['description']}")
            output.append(f"   Solution: {warning['solution']}")
            output.append("")

        if len(result['warnings']) > 10:
            output.append(f"... and {len(result['warnings']) - 10} more warnings")
            output.append("")

    if result['blocked']:
        output.append("COMMIT STATUS: BLOCKED")
        output.append("Reason: Critical errors must be fixed before committing")
        output.append("")
        output.append("NEXT STEPS:")
        output.append("1. Fix all errors listed above")
        output.append("2. Consider fixing warnings")
        output.append("3. Run validation again")
        output.append("4. Commit when validation passes")
    else:
        output.append("COMMIT STATUS: ALLOWED")
        output.append("All validations passed - ready to commit")

    return "\n".join(output)

def main():
    """Main function"""
    # Get files to validate from arguments
    files_to_validate = sys.argv[1:] if len(sys.argv) > 1 else []

    if not files_to_validate:
        print("No files to validate")
        return 0

    # Try to run redocly lint
    try:
        # First try npx redocly with shell=True for Windows compatibility
        cmd = f"npx redocly lint {' '.join(files_to_validate)}"
        result = subprocess.run(cmd, shell=True, capture_output=True, text=True, timeout=60)

        raw_output = result.stdout + result.stderr

    except (subprocess.TimeoutExpired, subprocess.SubprocessError):
        # If redocly is not available, show helpful message
        print("OPENAPI VALIDATION RESULT")
        print("=" * 40)
        print("")
        print(f"Files to validate: {len(files_to_validate)}")
        print("Files:", ", ".join(files_to_validate))
        print("")
        print("ERROR: redocly CLI execution failed!")
        print("Please check if redocly is properly installed: npx redocly --version")
        print("")
        print("COMMIT STATUS: BLOCKED")
        print("Cannot validate OpenAPI specs without working redocly CLI")
        return 1

    # For pre-commit hook, just return the exit code from redocly
    # The hook will handle the output formatting
    return result.returncode

    # Return appropriate exit code
    return 1 if parsed['blocked'] else 0

if __name__ == '__main__':
    sys.exit(main())
