#!/usr/bin/env python3
"""
Emoji and Special Characters Ban Validation Script

Validates that files do not contain forbidden emoji and special Unicode characters.
This is a Python replacement for the old shell script.

Usage:
    python scripts/validate-emoji-ban.py <file1> [file2] ...

Exit codes:
    0 - No emoji found (success)
    1 - Emoji/special characters found (failure)
"""

import sys
import os
import re
from pathlib import Path

# Forbidden Unicode character ranges (emoji and special characters)
FORBIDDEN_RANGES = [
    # Emoji ranges
    (0x1F600, 0x1F64F),  # Emoticons
    (0x1F300, 0x1F5FF),  # Misc Symbols and Pictographs
    (0x1F680, 0x1F6FF),  # Transport and Map
    (0x1F1E0, 0x1F1FF),  # Flags
    (0x2600, 0x26FF),    # Misc symbols
    (0x2700, 0x27BF),    # Dingbats
    (0x1F926, 0x1F937),  # Gestures
    (0x1F645, 0x1F64F),  # Gestures
    (0x1F680, 0x1F6C5),  # Transport
    (0x1F170, 0x1F251),  # Enclosed Characters
    # Special decorative characters
    (0x2750, 0x2757),    # Heavy punctuation
    (0x2760, 0x2767),    # Ornamental punctuation
    (0x2770, 0x2775),    # Dingbat arrows
    (0x2780, 0x2789),    # Dingbat circled sans-serif numbers
    (0x2794, 0x2797),    # Heavy arrows
    (0x27A0, 0x27AF),    # Heavy arrows
    (0x27B0, 0x27BF),    # Curved arrows
    (0x2B00, 0x2BFF),    # Misc arrows and geometric shapes
]

def is_forbidden_unicode(char):
    """Check if character is in forbidden Unicode ranges"""
    code = ord(char)
    for start, end in FORBIDDEN_RANGES:
        if start <= code <= end:
            return True

    # Additional specific forbidden characters (decorative symbols)
    forbidden_chars = ['\u25ba', '\u25c4', '\u25b2', '\u25bc', '\u25c6', '\u25c7', '\u25cf', '\u25cb', '\u25a0', '\u25a1']
    if char in forbidden_chars:
        return True

    return False

def check_file_for_emoji(file_path):
    """Check a single file for forbidden Unicode characters"""
    violations = []

    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            lines = f.readlines()

        for line_num, line in enumerate(lines, 1):
            for col, char in enumerate(line):
                if is_forbidden_unicode(char):
                    violations.append({
                        'line': line_num,
                        'column': col + 1,
                        'char': char,
                        'code': ord(char),
                        'context': line.strip()[:100]
                    })

    except Exception as e:
        print(f"[ERROR] Could not check file {file_path}: {e}")
        return []

    return violations

def main():
    import argparse

    parser = argparse.ArgumentParser(description='Validate files for forbidden emoji and special characters')
    parser.add_argument('--openapi', action='store_true', help='Check OpenAPI files only')
    parser.add_argument('files', nargs='*', help='Files to check')

    args = parser.parse_args()

    if not args.files:
        print("Usage: python scripts/validate-emoji-ban.py [--openapi] <file1> [file2] ...")
        sys.exit(1)

    all_violations = []
    files_checked = 0

    for file_path in args.files:
        if not os.path.exists(file_path):
            continue

        # Skip system files
        if any(skip in file_path for skip in ['.githooks/', '.cursor/', 'scripts/git-security/', 'scripts/linting/']):
            continue

        # Skip framework and migration docs
        if any(skip in file_path for skip in ['scripts/framework.py', 'scripts/SCRIPT_MIGRATION_GUIDE.md']):
            continue

        # If --openapi flag is used, only check OpenAPI files
        if args.openapi and not (file_path.endswith(('.yaml', '.yml')) and 'proto/openapi/' in file_path):
            continue

        violations = check_file_for_emoji(file_path)
        if violations:
            all_violations.extend([(file_path, v) for v in violations])
        files_checked += 1

    if all_violations:
        print(f"[CRITICAL] EMOJI DETECTED: Found {len(all_violations)} forbidden Unicode characters!")
        print()

        for file_path, violation in all_violations:
            print(f"File: {file_path}:{violation['line']}:{violation['column']}")
            print(f"Character: [FORBIDDEN_CHAR] (U+{violation['code']:04X})")
            print(f"Context: {violation['context']}")
            print()

        print("WHY THIS IS FORBIDDEN:")
        print("- [FORBIDDEN] Emoji break script execution on Windows")
        print("- [FORBIDDEN] Special Unicode characters cause terminal issues")
        print("- [FORBIDDEN] Compatibility problems across platforms")
        print()

        print("SOLUTIONS:")
        print("- Replace emoji with ASCII: [OK], [ERROR], [WARNING], [SYMBOL]")
        print("- Remove decorative Unicode characters")
        print("- Use plain text comments")
        print()

        sys.exit(1)
    else:
        if files_checked > 0:
            print(f"[SUCCESS] Emoji validation passed for {files_checked} files")
        else:
            print("[INFO] No files to check for emoji")
        sys.exit(0)

if __name__ == '__main__':
    main()
