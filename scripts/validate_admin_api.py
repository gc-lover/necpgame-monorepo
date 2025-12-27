#!/usr/bin/env python3
import yaml
import os
import sys

def validate_openapi_refs(file_path):
    """Проверяет $ref ссылки в OpenAPI файле"""
    errors = []

    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            spec = yaml.safe_load(f)

        def check_ref(ref, current_file):
            if not isinstance(ref, str) or not ref.startswith('$ref: '):
                return
            ref_path = ref.split('$ref: ')[1]
            if not ref_path.startswith('./') and not ref_path.startswith('../'):
                return

            # Разбираем путь
            if ref_path.startswith('./'):
                base_path = os.path.dirname(current_file)
                target = os.path.join(base_path, ref_path[2:])
            elif ref_path.startswith('../'):
                base_path = os.path.dirname(current_file)
                target = os.path.normpath(os.path.join(base_path, ref_path))
            else:
                return

            # Убираем фрагмент после #
            if '#' in target:
                target = target.split('#')[0]

            if not os.path.exists(target):
                errors.append(f'File not found: {target} (referenced from {current_file})')
            else:
                print(f'OK: {target}')

        def traverse(obj, path=''):
            if isinstance(obj, dict):
                for key, value in obj.items():
                    if key == '$ref':
                        check_ref(value, file_path)
                    else:
                        traverse(value, f'{path}.{key}')
            elif isinstance(obj, list):
                for i, item in enumerate(obj):
                    traverse(item, f'{path}[{i}]')

        traverse(spec)

    except Exception as e:
        errors.append(f'Error parsing {file_path}: {e}')

    return errors

if __name__ == '__main__':
    file_path = 'proto/openapi/system-domain/admin/admin-service.yaml'
    errors = validate_openapi_refs(file_path)

    if errors:
        print('ERRORS FOUND:')
        for error in errors:
            print(f'  {error}')
        sys.exit(1)
    else:
        print('All references are valid!')
