#!/usr/bin/env python3
import glob
import os
import re

# Define correct paths for different directories
PATH_CORRECTIONS = {
    'proto/openapi/world-domain/advanced/': '../../../common-schemas.yaml',
    'proto/openapi/arena-domain/matchmaking/': '../../common-schemas.yaml',
    'proto/openapi/auth-expansion-domain/': '../../common-schemas.yaml',
    'proto/openapi/specialized-domain/': '../common-schemas.yaml',
    'proto/openapi/social-domain/': '../common-schemas.yaml',
    'proto/openapi/system-domain/': '../common-schemas.yaml',
    'proto/openapi/misc-domain/': '../common-schemas.yaml',
    'proto/openapi/cyberpunk-domain/': '../common-schemas.yaml',
    'proto/openapi/faction-domain/': '../common-schemas.yaml',
    'proto/openapi/integration-domain/': '../common-schemas.yaml',
    'proto/openapi/analysis-domain/': '../common-schemas.yaml',
    'proto/openapi/economy-domain/': '../common-schemas.yaml',
    'proto/openapi/progression-domain/': '../common-schemas.yaml',
    'proto/openapi/referral-domain/': '../common-schemas.yaml',
    'proto/openapi/cosmetic-domain/': '../common-schemas.yaml',
}


def fix_file(file_path, correct_path):
    """Fix common-schemas.yaml references in a file"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Replace any common-schemas.yaml reference with correct path
        old_pattern = r'\$ref:\s*[\.\/]*common-schemas\.yaml'
        new_ref = f'$ref: {correct_path}'
        new_content = re.sub(old_pattern, new_ref, content)

        if new_content != content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Fixed {file_path}")
            return True
        else:
            return False
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False


def main():
    """Main function"""
    fixed_count = 0

    for base_dir, correct_path in PATH_CORRECTIONS.items():
        if os.path.exists(base_dir):
            # Find all YAML files in this directory recursively
            yaml_files = glob.glob(os.path.join(base_dir, '**', '*.yaml'), recursive=True)

            for yaml_file in yaml_files:
                if fix_file(yaml_file, correct_path):
                    fixed_count += 1

    print(f"Fixed {fixed_count} files")


if __name__ == '__main__':
    main()
