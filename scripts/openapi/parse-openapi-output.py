#!/usr/bin/env python3
"""
Parse existing OpenAPI validation output and format it cleanly for AI agents.
Usage: python parse-openapi-output.py < output.txt
Or: python parse-openapi-output.py --file output.txt
"""

import re
import sys


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
    files_with_issues = set()  # Track files that have errors or warnings
    files_validated = 0

    i = 0
    while i < len(lines):
        line = lines[i].strip()

        # Count all validated files
        if ('Validating' in line or 'validating' in line) and ('.yaml' in line or '.yml' in line):
            files_validated += 1

        # Parse errors and warnings from codeframe format
        if line.startswith('[') and ': ' in line:
            issue = parse_error_line(line, lines, i)
            if issue:
                if issue.get('is_warning', False):
                    warnings.append(issue)
                else:
                    errors.append(issue)
                files_with_issues.add(issue['file'])
            else:
                # Debug: couldn't parse line
                print(f"DEBUG: Failed to parse line: {line}", file=sys.stderr)

        i += 1

    return {
        'files_validated': files_validated,
        'files_with_issues': len(files_with_issues),
        'errors': errors,
        'warnings': warnings,
        'blocked': len(errors) > 0
    }


def parse_error_line(line, all_lines, index):
    """Parse a single error line from codeframe redocly output"""

    # Pattern for codeframe output: [N] file:line:col at path
    pattern = r'\[(\d+)\]\s+(.+?):(\d+):(\d+)\s+(.+)'
    match = re.search(pattern, line)

    if match:
        error_num, file_path, line_num, col_num, rest = match.groups()

        # Get description from next lines until "Error was generated" or next error
        description = ""
        error_type = "unknown"
        is_warning = False

        i = index + 1
        while i < len(all_lines):
            line_content = all_lines[i].strip()
            if not line_content:
                i += 1
                continue

            # Stop at next error/warning or end marker
            if line_content.startswith('[') or line_content.startswith(
                    'Error was generated') or line_content.startswith('Warning was generated'):
                if "Warning was generated" in line_content:
                    is_warning = True
                break

            description += line_content + " "

            # Try to extract error type from description
            if "Can't resolve" in description:
                error_type = "no-unresolved-refs"
            elif "Operation must have" in description and "4XX" in description:
                error_type = "operation-4xx-response"
            elif "Operation must have" in description and "2XX" in description:
                error_type = "operation-2xx-response"

            i += 1

        return {
            'file': file_path,
            'line': int(line_num),
            'column': int(col_num),
            'type': error_type,
            'description': description.strip(),
            'solution': get_solution(error_type, description),
            'is_warning': is_warning
        }

        return {
            'file': file_path,
            'line': int(line_num),
            'column': int(col_num),
            'type': error_type,
            'description': description.strip(),
            'solution': get_solution(error_type, description)
        }

    # Fallback for other error formats
    if 'error' in line.lower() and not line.startswith('Error was generated') and not line.startswith('['):
        return {
            'file': 'unknown',
            'line': 0,
            'type': 'unknown',
            'description': line,
            'solution': 'Check the error details'
        }

    return None


def parse_warning_line(line, all_lines, index):
    """Parse a single warning line"""
    # Pattern: file.yaml:line:col warning type description
    pattern = r'(.+?):(\d+):(\d+)\s+warning\s+(.+?)\s+(.+)'
    match = re.search(pattern, line)

    if match:
        file_path, line_num, col_num, warning_type, description = match.groups()
        return {
            'file': file_path,
            'line': int(line_num),
            'column': int(col_num),
            'type': warning_type,
            'description': description.strip(),
            'solution': get_solution(warning_type, description)
        }

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

    # Count real errors (excluding unknown/generic)
    real_errors = [e for e in result['errors'] if
                   e['file'] != 'unknown' and 'validation failed' not in e['description'].lower()]

    output.append(f"Files validated: {result['files_validated']}")
    output.append(f"Files with issues: {result['files_with_issues']}")
    output.append(f"Total errors: {len(real_errors)}")
    output.append(f"Total warnings: {len(result['warnings'])}")
    output.append("")

    # Group errors by file (skip unknown/generic errors)
    if result['errors']:
        output.append("CRITICAL ERRORS (MUST FIX):")
        output.append("-" * 30)

        errors_by_file = {}
        for error in result['errors']:
            # Skip generic "unknown" errors
            if error['file'] == 'unknown' or 'validation failed' in error['description'].lower():
                continue
            file = error['file']
            if file not in errors_by_file:
                errors_by_file[file] = []
            errors_by_file[file].append(error)

        if errors_by_file:  # Only show if we have real errors
            error_num = 1
            for file, file_errors in errors_by_file.items():
                output.append(f"File: {file}")
                for error in file_errors:
                    output.append(f"  {error_num}. Line {error['line']}: {error['type']} - {error['description']}")
                    output.append(f"     Solution: {error['solution']}")
                    error_num += 1
                output.append("")
        else:
            # No real errors, just generic validation failure
            output.append("No specific errors found - check validation output manually")
            output.append("")

    # Group warnings by file
    if result['warnings']:
        output.append("WARNINGS (SHOULD FIX):")
        output.append("-" * 30)

        warnings_by_file = {}
        for warning in result['warnings']:
            file = warning['file']
            if file not in warnings_by_file:
                warnings_by_file[file] = []
            warnings_by_file[file].append(warning)

        warning_num = 1
        for file, file_warnings in warnings_by_file.items():
            output.append(f"File: {file}")
            for warning in file_warnings:
                output.append(f"  {warning_num}. Line {warning['line']}: {warning['type']} - {warning['description']}")
                output.append(f"     Solution: {warning['solution']}")
                warning_num += 1
            output.append("")

        if len(result['warnings']) > 20:
            output.append(f"... and {len(result['warnings']) - 20} more warnings in other files")
            output.append("")

    if len(real_errors) > 0:
        output.append("COMMIT STATUS: BLOCKED")
        output.append("Reason: Critical errors must be fixed before committing")
        output.append("")
        output.append("NEXT STEPS:")
        output.append("1. Fix all errors listed above")
        output.append("2. Consider fixing warnings")
        output.append("3. Run validation again")
        output.append("4. Commit when all errors resolved")
    else:
        output.append("COMMIT STATUS: ALLOWED")
        output.append("All validations passed - ready to commit")

    return "\n".join(output)


def main():
    """Main function"""
    # Read from file if specified, otherwise from stdin
    if len(sys.argv) > 1 and sys.argv[1] == '--file':
        with open(sys.argv[2], 'r', encoding='utf-8', errors='replace') as f:
            raw_output = f.read()
    else:
        raw_output = sys.stdin.read()

    # Clean the output
    cleaned = clean_ansi(raw_output)
    cleaned = clean_emojis(cleaned)

    # Parse and format
    parsed = parse_validation_output(cleaned)
    formatted = format_output(parsed)

    print(formatted)

    # Return appropriate exit code
    return 1 if parsed['blocked'] else 0


if __name__ == '__main__':
    sys.exit(main())
