#!/usr/bin/env python3
"""
Secret Validation Script
Checks for common secrets and sensitive data in staged files
"""

import re
import sys
import subprocess
from pathlib import Path

# Secret patterns to check
SECRET_PATTERNS = {
    'GitHub Personal Access Token': [
        r'ghp_[A-Za-z0-9]{36}',
        r'github_pat_[A-Za-z0-9_]{82}',
        r'gho_[A-Za-z0-9]{36}',
        r'ghu_[A-Za-z0-9]{36}',
        r'ghs_[A-Za-z0-9]{36}',
        r'ghr_[A-Za-z0-9]{36}'
    ],
    'AWS Access Key': [
        r'AKIA[0-9A-Z]{16}',
        r'ASIA[0-9A-Z]{16}'
    ],
    'AWS Secret Key': [
        r'(?i)aws_secret_access_key\s*[:=]\s*["\']?[A-Za-z0-9/+=]{40}["\']?',
        r'(?i)aws_access_key_id\s*[:=]\s*["\']?[A-Za-z0-9]{20}["\']?'
    ],
    'Generic API Key': [
        r'(?i)(api[_-]?key|apikey)\s*[:=]\s*["\']?[A-Za-z0-9_\-]{20,}["\']?',
        r'(?i)(secret[_-]?key|secretkey)\s*[:=]\s*["\']?[A-Za-z0-9_\-]{20,}["\']?'
    ],
    'JWT Token': [
        r'eyJ[A-Za-z0-9_\-]*\.[A-Za-z0-9_\-]*\.[A-Za-z0-9_\-]*'
    ],
    'Private Key': [
        r'-----BEGIN\s+(RSA\s+)?PRIVATE\s+KEY-----',
        r'-----BEGIN\s+OPENSSH\s+PRIVATE\s+KEY-----'
    ],
    'Database Password': [
        r'(?i)(password|passwd|pwd)\s*[:=]\s*["\']?[A-Za-z0-9@#$%^&*()_\-+=]{8,}["\']?'
    ]
}

# Files to exclude from secret checking
EXCLUDED_FILES = {
    '.githooks/',
    '.cursor/',
    'scripts/framework.py',
    'test_validation.py',
    'fix_git_push.py'
}

def get_staged_files():
    """Get list of staged files"""
    try:
        result = subprocess.run(['git', 'diff', '--cached', '--name-only'],
                              capture_output=True, text=True, check=True)
        files = result.stdout.strip().split('\n')
        return [f for f in files if f]
    except subprocess.CalledProcessError:
        return []

def check_file_for_secrets(file_path):
    """Check a single file for secrets"""
    violations = []

    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()

        for secret_type, patterns in SECRET_PATTERNS.items():
            for pattern in patterns:
                matches = re.finditer(pattern, content, re.MULTILINE)
                for match in matches:
                    violations.append({
                        'type': secret_type,
                        'pattern': pattern,
                        'match': match.group(),
                        'line': content[:match.start()].count('\n') + 1,
                        'file': file_path
                    })

    except Exception as e:
        print(f"[WARNING] Could not read file {file_path}: {e}")

    return violations

def should_check_file(file_path):
    """Determine if file should be checked for secrets"""
    for excluded in EXCLUDED_FILES:
        if excluded in file_path:
            return False
    return True

def main():
    """Main validation function"""
    print("[CHECK] Secret Validation: Scanning for sensitive data...")

    staged_files = get_staged_files()
    if not staged_files:
        print("[INFO] No staged files to check")
        return 0

    all_violations = []

    for file_path in staged_files:
        if not Path(file_path).exists():
            continue

        if not should_check_file(file_path):
            continue

        violations = check_file_for_secrets(file_path)
        all_violations.extend(violations)

    if all_violations:
        print("[CRITICAL] SECURITY VIOLATIONS DETECTED!")
        print("COMMIT BLOCKED - SENSITIVE DATA FOUND")
        print("=" * 50)

        for violation in all_violations:
            print(f"File: {violation['file']}:{violation['line']}")
            print(f"Type: {violation['type']}")
            print(f"Match: {violation['match']}")
            print("-" * 30)

        print("=" * 50)
        print("Remove all sensitive data before committing!")
        print("Consider using environment variables or secure credential storage.")
        return 1

    print("[SUCCESS] Secret Validation: No sensitive data found.")
    return 0

if __name__ == '__main__':
    sys.exit(main())