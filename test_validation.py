#!/usr/bin/env python3
import subprocess
import sys

files_to_test = [
    "proto/openapi/specialized-domain/main.yaml",
    "proto/openapi/social-domain/main.yaml"
]

for file_path in files_to_test:
    print(f"Testing {file_path}...")
    try:
        result = subprocess.run(
            ["npx", "redocly", "lint", file_path],
            capture_output=True,
            text=True,
            timeout=30,
            shell=True  # For Windows
        )
        print(f"Exit code: {result.returncode}")
        if result.stdout:
            print(f"STDOUT: {result.stdout[:500]}...")
        if result.stderr:
            print(f"STDERR: {result.stderr[:500]}...")
        print("-" * 50)
    except Exception as e:
        print(f"Error: {e}")
        print("-" * 50)
