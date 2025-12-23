#!/usr/bin/env python3
"""
Fix common.yaml references in OpenAPI specifications
"""
import os
import re
from pathlib import Path


def fix_common_refs():
    """Fix all common.yaml references to use correct paths"""
    openapi_dir = Path("proto/openapi")
    common_schemas = openapi_dir / "common-schemas.yaml"

    # Pattern to match common.yaml references
    pattern = r"common\.yaml#(/components/[^'\"]*)"

    fixed_count = 0

    # Find all YAML files in openapi directory
    for yaml_file in openapi_dir.rglob("*.yaml"):
        try:
            with open(yaml_file, 'r', encoding='utf-8') as f:
                content = f.read()

            original_content = content

            # Replace common.yaml references with correct relative paths
            def replace_ref(match):
                ref_path = match.group(1)
                # Calculate relative path to common-schemas.yaml
                try:
                    rel_path = os.path.relpath(common_schemas, yaml_file.parent)
                    # Normalize path separators for cross-platform compatibility
                    rel_path = rel_path.replace('\\', '/')
                    return f"{rel_path}#{ref_path}"
                except ValueError:
                    # If relative path calculation fails, use a fallback
                    return f"../../../../common-schemas.yaml#{ref_path}"

            content = re.sub(pattern, replace_ref, content)

            if content != original_content:
                with open(yaml_file, 'w', encoding='utf-8') as f:
                    f.write(content)
                fixed_count += 1
                print(f"Fixed {yaml_file}")

        except Exception as e:
            print(f"Error processing {yaml_file}: {e}")

    print(f"Fixed {fixed_count} files")


if __name__ == "__main__":
    fix_common_refs()
