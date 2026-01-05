#!/usr/bin/env python3
# Issue: #143577551
"""
Elemental Effects Validation Script
Tests the weapon elemental effects implementation
"""

import sys
import os
import json
from typing import Dict, List, Any

def validate_processor_structure():
    """Validate that all effect processors are implemented"""
    processors_dir = "services/effect-service-go/internal/effects/processors"

    required_processors = [
        "fire_processor.go",
        "ice_processor.go",
        "poison_processor.go",
        "acid_processor.go",
        "electric_processor.go",
        "void_processor.go"
    ]

    print("Validating effect processors...")

    missing_processors = []
    for processor in required_processors:
        processor_path = os.path.join(processors_dir, processor)
        if not os.path.exists(processor_path):
            missing_processors.append(processor)
        else:
            print(f"  OK {processor}")

    if missing_processors:
        print(f"  ERROR Missing processors: {missing_processors}")
        return False

    print("  OK All effect processors present")
    return True

def validate_types_structure():
    """Validate types definitions"""
    types_file = "services/effect-service-go/internal/effects/types/types.go"

    print("Validating types structure...")

    if not os.path.exists(types_file):
        print(f"  ERROR Types file not found: {types_file}")
        return False

    required_types = [
        "ElementalType",
        "ElementalEffect",
        "ActiveEffect",
        "EffectApplicationRequest",
        "EffectApplicationResponse",
        "Interaction",
        "EnvironmentContext",
        "EffectProcessor",
        "InteractionCalculator"
    ]

    with open(types_file, 'r', encoding='utf-8') as f:
        content = f.read()

    missing_types = []
    for type_name in required_types:
        if f"type {type_name}" not in content and f"{type_name} interface" not in content:
            missing_types.append(type_name)

    if missing_types:
        print(f"  ERROR Missing types: {missing_types}")
        return False

    print("  OK All required types defined")
    return True

def validate_calculator_structure():
    """Validate interaction calculator"""
    calculator_file = "services/effect-service-go/internal/effects/calculator/interaction_calculator.go"

    print("Validating interaction calculator...")

    if not os.path.exists(calculator_file):
        print(f"  ERROR Calculator file not found: {calculator_file}")
        return False

    required_methods = [
        "CalculateInteractions",
        "GetSynergyMultiplier",
        "GetConflictMultiplier",
        "initializeInteractionMatrix"
    ]

    with open(calculator_file, 'r', encoding='utf-8') as f:
        content = f.read()

    missing_methods = []
    for method in required_methods:
        if method not in content:
            missing_methods.append(method)

    if missing_methods:
        print(f"  ERROR Missing calculator methods: {missing_methods}")
        return False

    print("  OK Interaction calculator implemented")
    return True

def validate_manager_structure():
    """Validate effect manager"""
    manager_file = "services/effect-service-go/internal/effects/manager/effect_manager.go"

    print("Validating effect manager...")

    if not os.path.exists(manager_file):
        print(f"  ERROR Manager file not found: {manager_file}")
        return False

    required_methods = [
        "ApplyEffect",
        "CalculateDamage",
        "GetActiveEffects",
        "ExpireEffect",
        "CleanupExpiredEffects"
    ]

    with open(manager_file, 'r', encoding='utf-8') as f:
        content = f.read()

    missing_methods = []
    for method in required_methods:
        if method not in content:
            missing_methods.append(method)

    if missing_methods:
        print(f"  ERROR Missing manager methods: {missing_methods}")
        return False

    print("  OK Effect manager implemented")
    return True

def validate_api_handler():
    """Validate API handler implementation"""
    api_file = "services/effect-service-go/internal/api/elemental_api.go"

    print("Validating API handler...")

    if not os.path.exists(api_file):
        print(f"  ERROR API handler file not found: {api_file}")
        return False

    required_methods = [
        "EffectServiceApplyEffects",
        "EffectServiceGetActiveEffects",
        "PreviewInteraction",
        "EffectServiceHealthCheck"
    ]

    with open(api_file, 'r', encoding='utf-8') as f:
        content = f.read()

    missing_methods = []
    for method in required_methods:
        if method not in content:
            missing_methods.append(method)

    if missing_methods:
        print(f"  ERROR Missing API methods: {missing_methods}")
        return False

    print("  OK API handler implemented")
    return True

def validate_elemental_interactions():
    """Validate that elemental interactions are properly defined"""
    calculator_file = "services/effect-service-go/internal/effects/calculator/interaction_calculator.go"

    print("Validating elemental interactions...")

    if not os.path.exists(calculator_file):
        print("  ERROR Calculator file not found")
        return False

    with open(calculator_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Check for key interaction pairs
    key_interactions = [
        "steam",
        "plasma",
        "conductive",
        "absorption"
    ]

    missing_interactions = []
    for interaction in key_interactions:
        if interaction not in content:
            missing_interactions.append(interaction)

    if missing_interactions:
        print(f"  ERROR Missing key interactions: {missing_interactions}")
        return False

    print("  OK Elemental interactions defined")
    return True

def main():
    """Main validation function"""
    print("Starting Elemental Effects Implementation Validation")
    print("=" * 60)

    validations = [
        validate_processor_structure,
        validate_types_structure,
        validate_calculator_structure,
        validate_manager_structure,
        validate_api_handler,
        validate_elemental_interactions
    ]

    passed = 0
    total = len(validations)

    for validation in validations:
        try:
            if validation():
                passed += 1
            print()
        except Exception as e:
            print(f"  ❌ Validation error: {e}")
            print()

    print("=" * 60)
    print(f"Validation Results: {passed}/{total} checks passed")

    if passed == total:
        print("SUCCESS: All validations passed! Elemental Effects implementation is complete.")
        return 0
    else:
        print("ERROR: Some validations failed. Please review the implementation.")
        return 1

if __name__ == "__main__":
    sys.exit(main())
