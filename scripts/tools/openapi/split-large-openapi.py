#!/usr/bin/env python3
"""
Split large OpenAPI files into smaller modules using $ref
Issue: #2090
"""

import yaml
import os
from pathlib import Path
from typing import Dict, List, Any

MAX_LINES = 600
SCHEMAS_DIR = "schemas"
PATHS_DIR = "paths"

def count_lines(file_path: Path) -> int:
    """Count lines in file"""
    with open(file_path, 'r', encoding='utf-8') as f:
        return len(f.readlines())

def escape_path(path: str) -> str:
    """Escape path for JSON Pointer (replace / with ~1)"""
    return path.replace('/', '~1')

def split_openapi_file(main_file: Path) -> bool:
    """Split large OpenAPI file into modules"""
    if not main_file.exists():
        print(f"File not found: {main_file}")
        return False
    
    lines = count_lines(main_file)
    if lines <= MAX_LINES:
        print(f"File {main_file} has {lines} lines, no need to split")
        return False
    
    print(f"Splitting {main_file} ({lines} lines)...")
    
    # Load main file
    try:
        with open(main_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)
    except yaml.YAMLError as e:
        print(f"  ERROR: Failed to parse YAML file: {e}")
        print(f"  Skipping {main_file} due to YAML syntax error")
        return False
    
    service_dir = main_file.parent
    service_name = service_dir.name
    schemas_dir = service_dir / SCHEMAS_DIR
    paths_dir = service_dir / PATHS_DIR
    
    # Create directories
    schemas_dir.mkdir(exist_ok=True)
    paths_dir.mkdir(exist_ok=True)
    
    # Backup original
    backup_file = main_file.with_suffix('.yaml.backup')
    with open(backup_file, 'w', encoding='utf-8') as f:
        with open(main_file, 'r', encoding='utf-8') as orig:
            f.write(orig.read())
    print(f"  Backup created: {backup_file}")
    
    # Extract and split paths - each path goes to separate file
    new_paths = {}
    if 'paths' in data and data['paths']:
        for path_name, path_def in data['paths'].items():
            # Create filename from path (e.g., /health -> health.yaml)
            path_filename = path_name.lstrip('/').replace('/', '-') or 'root'
            if not path_filename.endswith('.yaml'):
                path_filename += '.yaml'
            
            path_file = paths_dir / path_filename
            path_data = {
                'openapi': data.get('openapi', '3.0.3'),
                'paths': {
                    path_name: path_def
                }
            }
            with open(path_file, 'w', encoding='utf-8') as f:
                yaml.dump(path_data, f, default_flow_style=False, sort_keys=False, allow_unicode=True)
            
            # Use $ref for each path in main.yaml
            escaped_path = escape_path(path_name)
            new_paths[path_name] = {
                '$ref': f'./{PATHS_DIR}/{path_filename}#/paths/{escaped_path}'
            }
            print(f"  Created {path_file}")
    
    # Extract schemas - split into multiple files if too large
    new_schemas = {}
    if 'components' in data and 'schemas' in data['components']:
        schemas = data['components']['schemas']
        schemas_file = schemas_dir / f"{service_name}-schemas.yaml"
        schemas_data = {
            'openapi': data.get('openapi', '3.0.3'),
            'components': {
                'schemas': schemas
            }
        }
        with open(schemas_file, 'w', encoding='utf-8') as f:
            yaml.dump(schemas_data, f, default_flow_style=False, sort_keys=False, allow_unicode=True)
        print(f"  Created {schemas_file}")
        
        # Use $ref for each schema in main.yaml
        for schema_name in schemas.keys():
            new_schemas[schema_name] = {
                '$ref': f'./{SCHEMAS_DIR}/{service_name}-schemas.yaml#/components/schemas/{schema_name}'
            }
    
    # Extract responses
    new_responses = {}
    if 'components' in data and 'responses' in data['components']:
        responses = data['components']['responses']
        responses_file = schemas_dir / f"{service_name}-responses.yaml"
        responses_data = {
            'openapi': data.get('openapi', '3.0.3'),
            'components': {
                'responses': responses
            }
        }
        with open(responses_file, 'w', encoding='utf-8') as f:
            yaml.dump(responses_data, f, default_flow_style=False, sort_keys=False, allow_unicode=True)
        print(f"  Created {responses_file}")
        
        # Use $ref for each response in main.yaml
        for response_name in responses.keys():
            new_responses[response_name] = {
                '$ref': f'./{SCHEMAS_DIR}/{service_name}-responses.yaml#/components/responses/{response_name}'
            }
    
    # Create new main.yaml with $ref
    new_main = {
        'openapi': data.get('openapi', '3.0.3'),
        'info': data.get('info', {}),
        'servers': data.get('servers', []),
        'security': data.get('security', []),
        'tags': data.get('tags', []),
        'paths': new_paths,
        'components': {
            'schemas': new_schemas,
            'responses': new_responses
        }
    }
    
    # Keep other components if they exist
    if 'components' in data:
        for key in ['securitySchemes', 'parameters', 'requestBodies', 'headers', 'examples', 'links', 'callbacks']:
            if key in data['components']:
                new_main['components'][key] = data['components'][key]
    
    # Write new main.yaml
    with open(main_file, 'w', encoding='utf-8') as f:
        yaml.dump(new_main, f, default_flow_style=False, sort_keys=False, allow_unicode=True)
    print(f"  Updated {main_file}")
    
    new_lines = count_lines(main_file)
    print(f"  New main.yaml has {new_lines} lines (was {lines})")
    
    return True

def find_large_files(base_dir: Path) -> List[Path]:
    """Find OpenAPI files exceeding MAX_LINES"""
    large_files = []
    for main_file in base_dir.rglob("main.yaml"):
        if count_lines(main_file) > MAX_LINES:
            large_files.append(main_file)
    return sorted(large_files)

if __name__ == "__main__":
    import sys
    
    base_dir = Path("proto/openapi")
    if len(sys.argv) > 1:
        service_name = sys.argv[1]
        main_file = base_dir / service_name / "main.yaml"
        if main_file.exists():
            split_openapi_file(main_file)
        else:
            print(f"File not found: {main_file}")
    else:
        # Find and split all large files
        large_files = find_large_files(base_dir)
        print(f"Found {len(large_files)} files exceeding {MAX_LINES} lines")
        processed = 0
        failed = 0
        for main_file in large_files:
            print(f"\nProcessing {main_file}...")
            try:
                if split_openapi_file(main_file):
                    processed += 1
                else:
                    failed += 1
            except Exception as e:
                print(f"  ERROR: Failed to process {main_file}: {e}")
                failed += 1
        print(f"\n=== Summary ===")
        print(f"Processed: {processed}")
        print(f"Failed: {failed}")
        print(f"Total: {len(large_files)}")
