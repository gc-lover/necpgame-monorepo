#!/usr/bin/env python3
import glob
import os
import yaml


def fix_remaining_paths():
    files_to_fix = [
        "proto/openapi/system-domain/ai/tournament-domain/clan/clan-war-service.yaml",
        "proto/openapi/system-domain/ai/system-domain/admin/main.yaml",
        "proto/openapi/system-domain/ai/system-domain/sync/main.yaml",
        "proto/openapi/system-domain/ai/social-domain/romance/main.yaml",
        "proto/openapi/system-domain/ai/security-domain/kafka/main.yaml",
        "proto/openapi/system-domain/ai/gameplay-domain/combat/main.yaml",
        "proto/openapi/system-domain/ai/gameplay-domain/freerun/main.yaml",
        "proto/openapi/system-domain/ai/gameplay-domain/progression/main.yaml",
        "proto/openapi/system-domain/ai/gameplay-domain/quests/main.yaml",
        "proto/openapi/system-domain/ai/economy-domain/analytics/main.yaml",
        "proto/openapi/system-domain/ai/economy-domain/market/main.yaml",
        "proto/openapi/system-domain/ai/combat-domain/analytics/main.yaml",
        "proto/openapi/world-domain/sync/realtime-combat-api.yaml"
    ]

    fixed_count = 0

    for file_path in files_to_fix:
        if not os.path.exists(file_path):
            print(f"File not found: {file_path}")
            continue

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Check if file has empty paths section
            if 'paths:\n\ncomponents:' in content or 'paths:\n  # ' in content or 'paths:\n\n\ncomponents:' in content:
                print(f"Fixing {file_path}")

                # Replace empty paths with health endpoint
                content = content.replace(
                    'paths:\n\ncomponents:',
                    'paths:\n  /health:\n    get:\n      summary: Health check\n      operationId: getHealth\n      responses:\n        \'200\':\n          description: Service is healthy\n\ncomponents:'
                ).replace(
                    'paths:\n\n\ncomponents:',
                    'paths:\n  /health:\n    get:\n      summary: Health check\n      operationId: getHealth\n      responses:\n        \'200\':\n          description: Service is healthy\n\ncomponents:'
                )

                # Handle paths with comments
                if 'paths:\n  # ' in content and 'components:' in content:
                    # Find the paths section and replace it
                    lines = content.split('\n')
                    paths_start = -1
                    components_start = -1

                    for i, line in enumerate(lines):
                        if line.strip() == 'paths:':
                            paths_start = i
                        elif line.strip() == 'components:':
                            components_start = i
                            break

                    if paths_start != -1 and components_start != -1:
                        # Replace paths section
                        new_lines = lines[:paths_start + 1] + [
                            '  /health:',
                            '    get:',
                            '      summary: Health check',
                            '      operationId: getHealth',
                            '      responses:',
                            '        \'200\':',
                            '          description: Service is healthy'
                        ] + lines[paths_start + 1:components_start] + lines[components_start:]
                        content = '\n'.join(new_lines)

                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)

                fixed_count += 1

        except Exception as e:
            print(f"Error processing {file_path}: {e}")

    print(f"Fixed {fixed_count} files")


if __name__ == "__main__":
    fix_remaining_paths()
