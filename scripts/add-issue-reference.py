#!/usr/bin/env python3
"""
Script to add Issue reference to YAML files that don't have github_issue.

Usage: python add-issue-reference.py <issue_number> <file_list>
"""

import sys
import os

def add_issue_reference(file_path, issue_number):
    """Add Issue reference to the beginning of YAML file if not present."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Check if Issue reference already exists
        if f'# Issue: #{issue_number}' in content:
            print(f"SKIP: {file_path} - Issue reference already exists")
            return False

        # Find the metadata section
        if content.startswith('metadata:'):
            # Add Issue reference before metadata
            new_content = f'# Issue: #{issue_number}\n{content}'
        elif content.startswith('# Issue:'):
            # Already has some issue reference, skip
            print(f"SKIP: {file_path} - Already has issue reference")
            return False
        else:
            print(f"SKIP: {file_path} - No metadata section found")
            return False

        # Write back the file
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)

        print(f"OK: {file_path} - Added Issue #{issue_number}")
        return True

    except Exception as e:
        print(f"ERROR: {file_path} - {e}")
        return False

def main():
    if len(sys.argv) < 3:
        print("Usage: python add-issue-reference.py <issue_number> <file1> <file2> ...")
        sys.exit(1)

    issue_number = sys.argv[1]
    files = sys.argv[2:]

    processed = 0
    for file_path in files:
        if os.path.exists(file_path) and file_path.endswith('.yaml'):
            if add_issue_reference(file_path, issue_number):
                processed += 1

    print(f"\nProcessed {processed} files with Issue #{issue_number}")

if __name__ == '__main__':
    main()