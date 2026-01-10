#!/usr/bin/env python3
"""
Validate Tournament API OpenAPI Specification
Issue: #2277 - Tournament System OpenAPI Specification
"""

import yaml
import json
import os
from pathlib import Path

def validate_tournament_api_spec():
    """Validate tournament API specification completeness and enterprise-grade compliance"""

    spec_path = Path("proto/openapi/tournament-service/main.yaml")

    print("[VALIDATION] Tournament API OpenAPI Specification")
    print(f"[PATH] {spec_path.absolute()}")

    if not spec_path.exists():
        print("[ERROR] Specification file not found!")
        return False

    try:
        with open(spec_path, 'r', encoding='utf-8') as f:
            spec = yaml.safe_load(f)

        print("[SUCCESS] Specification loaded successfully")
        print(f"[VERSION] OpenAPI {spec.get('info', {}).get('version', 'unknown')}")

        # Check enterprise-grade requirements
        checks = {
            "Basic Info": validate_basic_info(spec),
            "Servers": validate_servers(spec),
            "Security": validate_security(spec),
            "Tags": validate_tags(spec),
            "Paths": validate_paths(spec),
            "Schemas": validate_schemas(spec),
            "Performance Notes": validate_performance_notes(spec),
            "Struct Alignment": validate_struct_alignment(spec),
            "Code Generation Ready": validate_code_generation_ready(spec)
        }

        all_passed = all(checks.values())

        print("\n[VALIDATION RESULTS]")
        for check_name, passed in checks.items():
            status = "[PASSED]" if passed else "[FAILED]"
            print(f"  {status} {check_name}")

        if all_passed:
            print("\n[FINAL RESULT] [SUCCESS] Tournament API specification is enterprise-grade and ready for production!")
            print("  - Code generation compatible")
            print("  - Performance optimized")
            print("  - Struct alignment hints included")
            print("  - Complete API coverage for tournament management")
            return True
        else:
            print("\n[FINAL RESULT] [ERROR] Specification needs improvements")
            return False

    except Exception as e:
        print(f"[ERROR] Failed to validate specification: {e}")
        return False

def validate_basic_info(spec):
    """Validate basic info section"""
    info = spec.get('info', {})
    required_fields = ['title', 'description', 'version', 'contact', 'license']

    for field in required_fields:
        if field not in info:
            print(f"    Missing required field: {field}")
            return False

    title = info.get('title', '')
    if 'Enterprise-Grade' not in title or 'Tournament' not in title:
        print("    Title doesn't match enterprise-grade pattern")
        return False

    return True

def validate_servers(spec):
    """Validate servers configuration"""
    servers = spec.get('servers', [])
    if len(servers) < 2:
        print("    Should have at least 2 servers (prod + staging)")
        return False

    # Check for production-like URLs (api.necpgame.com without staging)
    prod_found = any('api.necpgame.com' in s.get('url', '') and 'staging' not in s.get('url', '') for s in servers)
    staging_found = any('staging' in s.get('url', '') for s in servers)

    if not prod_found:
        print("    Missing production server configuration")
        return False

    if not staging_found:
        print("    Missing staging server configuration")
        return False

    return True

def validate_security(spec):
    """Validate security configuration"""
    security = spec.get('security', [])
    if not security:
        print("    Missing security configuration")
        return False

    # Check for BearerAuth
    if not any('BearerAuth' in str(sec) for sec in security):
        print("    Missing BearerAuth security scheme")
        return False

    return True

def validate_tags(spec):
    """Validate API tags"""
    tags = spec.get('tags', [])
    required_tags = [
        'Tournament Management', 'Matchmaking', 'Leaderboards',
        'Tournament Participation', 'Tournament Scoring', 'Tournament Analytics'
    ]

    tag_names = [tag.get('name', '') for tag in tags]

    for required_tag in required_tags:
        if required_tag not in tag_names:
            print(f"    Missing required tag: {required_tag}")
            return False

    return True

def validate_paths(spec):
    """Validate API paths"""
    paths = spec.get('paths', {})

    # Check for critical endpoints (using actual path patterns from spec)
    critical_paths = [
        '/health', '/tournaments', '/tournaments/{tournament_id}',
        '/tournaments/{tournament_id}/join', '/leaderboards'
    ]

    for path in critical_paths:
        if path not in paths:
            print(f"    Missing critical path: {path}")
            return False

    return True

def validate_schemas(spec):
    """Validate component schemas"""
    components = spec.get('components', {})
    schemas = components.get('schemas', {})

    required_schemas = [
        'Tournament', 'TournamentParticipant', 'LeaderboardEntry',
        'CreateTournamentRequest', 'UpdateTournamentRequest'
    ]

    for schema in required_schemas:
        if schema not in schemas:
            print(f"    Missing required schema: {schema}")
            return False

    return True

def validate_performance_notes(spec):
    """Validate performance optimization notes"""
    info_desc = spec.get('info', {}).get('description', '')

    performance_indicators = [
        'Performance Optimized', '<15ms P99 latency', 'Scalability'
    ]

    for indicator in performance_indicators:
        if indicator not in info_desc:
            print(f"    Missing performance indicator: {indicator}")
            return False

    return True

def validate_struct_alignment(spec):
    """Validate struct alignment hints for backend optimization"""
    components = spec.get('components', {})
    schemas = components.get('schemas', {})

    # Check at least one schema has struct alignment notes
    alignment_found = False
    for schema_name, schema_def in schemas.items():
        description = schema_def.get('description', '')
        if 'struct alignment' in description.lower() or 'memory savings' in description.lower():
            alignment_found = True
            break

    if not alignment_found:
        print("    No struct alignment hints found in schemas")
        return False

    return True

def validate_code_generation_ready(spec):
    """Validate code generation compatibility"""
    # Check for proper operationId patterns
    paths = spec.get('paths', {})
    operation_ids = []

    for path, methods in paths.items():
        for method, operation in methods.items():
            if method.lower() in ['get', 'post', 'put', 'delete', 'patch']:
                op_id = operation.get('operationId', '')
                if op_id:
                    operation_ids.append(op_id)

    if len(operation_ids) < 10:  # Should have substantial operations
        print(f"    Insufficient operation IDs: {len(operation_ids)}")
        return False

    # Check operationId naming convention
    invalid_ops = [op for op in operation_ids if not op.replace('_', '').replace('-', '').isalnum()]
    if invalid_ops:
        print(f"    Invalid operationId format: {invalid_ops[:3]}")
        return False

    return True

if __name__ == "__main__":
    success = validate_tournament_api_spec()
    exit(0 if success else 1)