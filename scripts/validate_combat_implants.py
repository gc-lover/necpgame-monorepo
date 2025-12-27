#!/usr/bin/env python3
import yaml
import sys

def validate_parameters(spec):
    """Проверяет параметры на корректность"""
    errors = []

    paths = spec.get('paths', {})
    for path_name, path_def in paths.items():
        if not isinstance(path_def, dict):
            continue

        for method_name, method_def in path_def.items():
            if method_name not in ['get', 'post', 'put', 'delete', 'patch']:
                continue

            if 'parameters' in method_def:
                params = method_def['parameters']
                for param in params:
                    # Проверяем базовую структуру
                    if not all(key in param for key in ['name', 'in', 'schema']):
                        errors.append(f'Parameter missing required fields in {path_name} {method_name}: {param.get("name", "unknown")}')

                    # Проверяем schema
                    schema = param.get('schema', {})
                    param_type = schema.get('type')

                    # Проверяем соответствие default значений ограничениям
                    if 'default' in schema and param_type in ['integer', 'number']:
                        default_val = schema['default']
                        min_val = schema.get('minimum')
                        max_val = schema.get('maximum')

                        if min_val is not None and default_val < min_val:
                            errors.append(f'Parameter {param["name"]} default {default_val} < minimum {min_val}')
                        if max_val is not None and default_val > max_val:
                            errors.append(f'Parameter {param["name"]} default {default_val} > maximum {max_val}')

                    # Проверяем enum значения
                    if 'enum' in schema:
                        default_val = schema.get('default')
                        enum_values = schema['enum']
                        if default_val is not None and default_val not in enum_values:
                            errors.append(f'Parameter {param["name"]} default "{default_val}" not in enum {enum_values}')

    return errors

def check_enterprise_features(spec):
    """Проверяет enterprise-grade особенности"""
    warnings = []

    # Проверяем, что все схемы имеют оптимизации
    schemas = spec.get('components', {}).get('schemas', {})
    for schema_name, schema_def in schemas.items():
        if isinstance(schema_def, dict) and schema_def.get('type') == 'object':
            properties = schema_def.get('properties', {})
            if properties and 'description' not in schema_def:
                # Проверяем, есть ли BACKEND NOTE
                desc = schema_def.get('description', '')
                if 'BACKEND NOTE' not in desc:
                    warnings.append(f'Schema {schema_name} missing BACKEND NOTE for struct alignment')

    return warnings

if __name__ == '__main__':
    with open('proto/openapi/specialized-domain/combat/combat-implants-stats-service-go/main.yaml', 'r', encoding='utf-8') as f:
        spec = yaml.safe_load(f)

    print("Validating parameters...")
    param_errors = validate_parameters(spec)
    if param_errors:
        print("PARAMETER ERRORS:")
        for error in param_errors:
            print(f"  {error}")
    else:
        print("All parameters are valid!")

    print("\nChecking enterprise features...")
    warnings = check_enterprise_features(spec)
    if warnings:
        print("ENTERPRISE WARNINGS:")
        for warning in warnings:
            print(f"  {warning}")
    else:
        print("All enterprise features present!")

    if param_errors:
        sys.exit(1)
    else:
        print("\nValidation successful!")
