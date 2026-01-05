#!/usr/bin/env python3
"""
QA Script: Test Gameplay Service OpenAPI Specification
Tests OpenAPI spec validation, structure, and compliance.
Issue: #1495 - QA Testing
"""

import json
import yaml
import sys
import os
from pathlib import Path
from typing import Dict, Any, List

def load_openapi_spec(spec_path: str) -> Dict[str, Any]:
    """Load OpenAPI specification from YAML file."""
    try:
        with open(spec_path, 'r', encoding='utf-8') as f:
            if spec_path.endswith(('.yaml', '.yml')):
                return yaml.safe_load(f)
            else:
                return json.load(f)
    except Exception as e:
        print(f"ERROR: Failed to load OpenAPI spec from {spec_path}: {e}")
        return None

def validate_openapi_structure(spec: Dict[str, Any]) -> List[str]:
    """Validate basic OpenAPI structure and required fields."""
    errors = []

    # Check required top-level fields
    required_fields = ['openapi', 'info', 'paths']
    for field in required_fields:
        if field not in spec:
            errors.append(f"Missing required field: {field}")

    # Check OpenAPI version
    if 'openapi' in spec:
        version = spec['openapi']
        if not version.startswith('3.'):
            errors.append(f"Unsupported OpenAPI version: {version} (expected 3.x)")

    # Check info section
    if 'info' in spec:
        info = spec['info']
        if 'title' not in info:
            errors.append("Missing info.title")
        if 'version' not in info:
            errors.append("Missing info.version")

    # Check paths section
    if 'paths' in spec:
        paths = spec['paths']
        if not isinstance(paths, dict):
            errors.append("paths must be a dictionary")
        elif len(paths) == 0:
            errors.append("No API paths defined")

        # Check health endpoint exists
        if '/health' not in paths:
            errors.append("Missing required /health endpoint")

    return errors

def validate_gameplay_endpoints(spec: Dict[str, Any]) -> List[str]:
    """Validate gameplay-specific API endpoints."""
    errors = []
    warnings = []

    if 'paths' not in spec:
        return errors

    paths = spec['paths']
    expected_endpoints = [
        '/health',
        '/api/v1/gameplay/affixes/active',
        '/api/v1/gameplay/affixes',
        '/api/v1/gameplay/instances/{instanceId}/affixes',
        '/api/v1/gameplay/affixes/rotation/current',
        '/api/v1/analytics/affixes/popularity',
    ]

    # Check required endpoints exist
    for endpoint in expected_endpoints:
        if endpoint not in paths:
            errors.append(f"Missing required endpoint: {endpoint}")

    # Validate affix endpoints have proper HTTP methods
    affix_endpoints = [
        '/api/v1/gameplay/affixes',
        '/api/v1/gameplay/affixes/{id}',
    ]

    for endpoint in affix_endpoints:
        if endpoint in paths:
            methods = list(paths[endpoint].keys())
            # Remove OpenAPI specific keys
            methods = [m for m in methods if not m.startswith('$') and m.lower() != 'parameters']

            if endpoint == '/api/v1/gameplay/affixes':
                if 'get' not in [m.lower() for m in methods]:
                    errors.append(f"{endpoint} missing GET method")
                if 'post' not in [m.lower() for m in methods]:
                    errors.append(f"{endpoint} missing POST method")
            elif '{id}' in endpoint:
                if 'get' not in [m.lower() for m in methods]:
                    warnings.append(f"{endpoint} missing GET method")
                if 'put' not in [m.lower() for m in methods]:
                    warnings.append(f"{endpoint} missing PUT method")
                if 'delete' not in [m.lower() for m in methods]:
                    warnings.append(f"{endpoint} missing DELETE method")

    return errors

def validate_schemas(spec: Dict[str, Any]) -> List[str]:
    """Validate OpenAPI schemas and data models."""
    errors = []
    warnings = []

    if 'components' not in spec or 'schemas' not in spec['components']:
        errors.append("Missing components.schemas section")
        return errors

    schemas = spec['components']['schemas']

    # Check required schemas for gameplay service
    required_schemas = [
        'Affix',
        'CreateAffixRequest',
        'UpdateAffixRequest',
        'HealthResponse',
    ]

    for schema in required_schemas:
        if schema not in schemas:
            errors.append(f"Missing required schema: {schema}")

    # Validate Affix schema structure
    if 'Affix' in schemas:
        affix_schema = schemas['Affix']
        required_properties = ['id', 'name', 'description', 'category', 'reward_modifier', 'difficulty_modifier']

        if 'properties' in affix_schema:
            properties = affix_schema['properties']
            for prop in required_properties:
                if prop not in properties:
                    errors.append(f"Affix schema missing property: {prop}")
        else:
            errors.append("Affix schema missing properties section")

    return errors

def check_performance_requirements(spec: Dict[str, Any]) -> List[str]:
    """Check if API spec meets performance requirements."""
    warnings = []

    # Check if performance targets are documented
    description = spec.get('info', {}).get('description', '')
    perf_keywords = ['<50ms', 'P99 Latency', '<10ms', 'P95']

    perf_mentions = sum(1 for keyword in perf_keywords if keyword in description)
    if perf_mentions < 2:
        warnings.append("Performance requirements not adequately documented in API description")

    return warnings

def main():
    """Main QA validation function."""
    spec_path = "proto/openapi/gameplay-service/main.yaml"

    if not os.path.exists(spec_path):
        print(f"ERROR: OpenAPI spec not found at {spec_path}")
        return 1

    print(f"🔍 QA Testing: Gameplay Service OpenAPI Specification")
    print(f"📁 Spec file: {spec_path}")
    print("-" * 60)

    # Load specification
    spec = load_openapi_spec(spec_path)
    if spec is None:
        return 1

    all_errors = []
    all_warnings = []

    # Run validations
    print("✅ Running structure validation...")
    errors = validate_openapi_structure(spec)
    all_errors.extend(errors)

    print("✅ Running endpoint validation...")
    errors = validate_gameplay_endpoints(spec)
    all_errors.extend(errors)

    print("✅ Running schema validation...")
    errors = validate_schemas(spec)
    all_errors.extend(errors)

    print("✅ Checking performance requirements...")
    warnings = check_performance_requirements(spec)
    all_warnings.extend(warnings)

    # Report results
    print("\n" + "=" * 60)
    print("📊 VALIDATION RESULTS")
    print("=" * 60)

    if not all_errors:
        print("✅ PASSED: No critical errors found")
    else:
        print(f"❌ FAILED: {len(all_errors)} critical error(s) found:")
        for error in all_errors:
            print(f"  • {error}")

    if all_warnings:
        print(f"⚠️  WARNINGS: {len(all_warnings)} warning(s) found:")
        for warning in all_warnings:
            print(f"  • {warning}")

    # Summary
    print("\n" + "-" * 60)
    if not all_errors:
        print("🎉 QA PASSED: OpenAPI specification is valid and complete")
        return 0
    else:
        print("💥 QA FAILED: OpenAPI specification has critical issues")
        return 1

if __name__ == "__main__":
    sys.exit(main())
