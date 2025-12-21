#!/usr/bin/env python3
"""
Script Language Enforcement Validation Script

Validates that only Python scripts are allowed in the scripts/ directory.
This is a Python replacement for the old shell script.

Usage:
    python scripts/validate-script-types.py <file1> [file2] ...

Exit codes:
    0 - Only allowed script types (success)
    1 - Forbidden script types detected (failure)
"""

import sys
import os
from pathlib import Path

# Forbidden script extensions (only Python allowed)
FORBIDDEN_EXTENSIONS = {
    '.sh': 'Shell script',
    '.ps1': 'PowerShell script',
    '.bat': 'Batch script',
    '.cmd': 'Command script',
    '.pl': 'Perl script',
    '.rb': 'Ruby script',
    '.js': 'JavaScript/Node.js script',
    '.php': 'PHP script',
    '.lua': 'Lua script',
    '.tcl': 'Tcl script',
    '.awk': 'AWK script',
    '.sed': 'SED script',
}

# Allowed exceptions (system/infrastructure files)
ALLOWED_EXCEPTIONS = [
    # Git hooks (system infrastructure)
    '.githooks/',
    # Infrastructure automation
    'infrastructure/',
    # Git security tools
    'scripts/git-security/',
    # Legacy framework files (to be migrated)
    'scripts/framework.py',
]

def is_forbidden_script(file_path):
    """Check if file is a forbidden script type"""
    _, ext = os.path.splitext(file_path)

    # Check if extension is forbidden
    if ext.lower() in FORBIDDEN_EXTENSIONS:
        # Check if it's in allowed exceptions
        for allowed in ALLOWED_EXCEPTIONS:
            if allowed in file_path:
                return False, None
        return True, FORBIDDEN_EXTENSIONS[ext.lower()]

    return False, None

def main():
    if len(sys.argv) < 2:
        print("Usage: python scripts/validate-script-types.py <file1> [file2] ...")
        sys.exit(1)

    forbidden_scripts = []
    files_checked = 0

    for file_path in sys.argv[1:]:
        if not os.path.exists(file_path):
            continue

        # Only check files in scripts/ directory (unless it's an exception)
        if not file_path.startswith('scripts/') and not any(exc in file_path for exc in ALLOWED_EXCEPTIONS):
            continue

        files_checked += 1
        is_forbidden, script_type = is_forbidden_script(file_path)

        if is_forbidden:
            forbidden_scripts.append((file_path, script_type))

    if forbidden_scripts:
        print("[CRITICAL] FORBIDDEN SCRIPT TYPES DETECTED!")
        print(f"Found {len(forbidden_scripts)} forbidden script(s)")
        print()

        for file_path, script_type in forbidden_scripts:
            print(f"File: {file_path}")
            print(f"Type: {script_type}")
            print()

        print("SCRIPT LANGUAGE POLICY ENFORCEMENT:")
        print("• [OK] ALLOWED: .py (Python scripts)")
        print("• [ERROR] FORBIDDEN: .sh, .ps1, .bat, .cmd, .pl, .rb, .js (shell scripts)")
        print()

        print("WHY THIS IS ENFORCED:")
        print("• Python is cross-platform and maintainable")
        print("• Shell scripts cause platform compatibility issues")
        print("• Python has better error handling and testing")
        print("• Single language reduces cognitive load")
        print()

        print("MIGRATION REQUIRED:")
        print("1. Convert shell scripts to Python equivalents")
        print("2. Use python scripts/core/base_script.py for new scripts")
        print("3. Remove old shell scripts after migration")
        print()

        print("EXCEPTIONS (system files only):")
        print("• .githooks/*.sh - Git hooks (system infrastructure)")
        print("• infrastructure/**/*.sh - Infrastructure automation")
        print("• scripts/git-security/*.bat - Git security tools")
        print()

        sys.exit(1)
    else:
        if files_checked > 0:
            print(f"[SUCCESS] Script language validation passed for {files_checked} files")
            print("Only Python scripts detected.")
        else:
            print("[INFO] No script files to check")
        sys.exit(0)

if __name__ == '__main__':
    main()
