#!/usr/bin/env python3
import os
import re
import glob

def count_operations(filepath):
    """Count total operations and example operations in a YAML file"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        # Count all operationId lines
        total_ops = len(re.findall(r'operationId:', content))

        # Count example operations (createExample, getExample, etc.)
        example_ops = len(re.findall(r'operationId:\s*(createExample|getExample|updateExample|deleteExample|listExamples)', content))

        return total_ops, example_ops
    except Exception as e:
        return 0, 0

def main():
    base_path = 'proto/openapi'
    problematic_services = []

    # Find all main.yaml files
    for yaml_file in glob.glob(f'{base_path}/**/main.yaml', recursive=True):
        total_ops, example_ops = count_operations(yaml_file)

        if total_ops > 0:
            example_ratio = example_ops / total_ops

            if example_ratio >= 0.5:  # More than 50% example operations
                service_name = yaml_file.replace(f'{base_path}/', '').replace('/main.yaml', '')
                problematic_services.append({
                    'service': service_name,
                    'file': yaml_file,
                    'total_ops': total_ops,
                    'example_ops': example_ops,
                    'ratio': example_ratio
                })

    print("Services with 50%+ example operations:")
    print("=" * 50)

    for svc in sorted(problematic_services, key=lambda x: x['ratio'], reverse=True):
        print(f"{svc['service']}: {svc['ratio']:.1%} "
              f"Example: {svc['example_ops']}, Total: {svc['total_ops']}")

    print(f"\nTotal problematic services: {len(problematic_services)}")

    # Print list for removal
    print("\nServices to remove:")
    print("=" * 20)
    for svc in sorted(problematic_services, key=lambda x: x['service']):
        print(f"- {svc['service']}")

    return problematic_services

if __name__ == '__main__':
    main()