#!/usr/bin/env python3
import os
import glob
import yaml

def fix_empty_paths():
    # Find all main.yaml files in proto/openapi
    pattern = "proto/openapi/**/main.yaml"
    files = glob.glob(pattern, recursive=True)

    fixed_count = 0

    for file_path in files:
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Check if file has empty paths section
            if 'paths:\n\ncomponents:' in content or 'paths:\n\n\ncomponents:' in content:
                print(f"Fixing {file_path}")

                # Replace empty paths with health endpoint
                new_content = content.replace(
                    'paths:\n\ncomponents:',
                    'paths:\n  /health:\n    get:\n      summary: Health check\n      operationId: getHealth\n      responses:\n        \'200\':\n          description: Service is healthy\n\ncomponents:'
                ).replace(
                    'paths:\n\n\ncomponents:',
                    'paths:\n  /health:\n    get:\n      summary: Health check\n      operationId: getHealth\n      responses:\n        \'200\':\n          description: Service is healthy\n\ncomponents:'
                )

                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)

                fixed_count += 1

        except Exception as e:
            print(f"Error processing {file_path}: {e}")

    print(f"Fixed {fixed_count} files")

if __name__ == "__main__":
    fix_empty_paths()
