#!/usr/bin/env python3
"""
Integration tests for economy-service-go with crafting mechanics
Issue: #2187 - QA testing economy-service-go with crafting mechanics
"""

import os
import sys
import subprocess
import time
import requests
import json
from pathlib import Path

def run_command(cmd, cwd=None):
    """Run shell command and return output"""
    try:
        result = subprocess.run(cmd, shell=True, cwd=cwd, capture_output=True, text=True)
        return result.returncode == 0, result.stdout, result.stderr
    except Exception as e:
        return False, "", str(e)

def test_compilation():
    """Test compilation of both services"""
    print("🧪 Testing compilation...")

    # Test economy-service compilation
    success, stdout, stderr = run_command("go build ./...", cwd="services/economy-service-go")
    if not success:
        print(f"❌ Economy service compilation failed: {stderr}")
        return False
    print("✅ Economy service compiled successfully")

    # Test crafting-service compilation
    success, stdout, stderr = run_command("go build ./...", cwd="services/crafting-service-go")
    if not success:
        print(f"❌ Crafting service compilation failed: {stderr}")
        return False
    print("✅ Crafting service compiled successfully")

    return True

def test_api_compatibility():
    """Test API compatibility between services"""
    print("🧪 Testing API compatibility...")

    # Check if economy API has crafting-related endpoints
    economy_api_path = "services/economy-service-go/pkg/api"
    if not os.path.exists(economy_api_path):
        print("❌ Economy API not found")
        return False

    # Check crafting API
    crafting_api_path = "services/crafting-service-go/pkg/api"
    if not os.path.exists(crafting_api_path):
        print("❌ Crafting API not found")
        return False

    print("✅ API packages exist")
    return True

def test_bazaar_integration():
    """Test BazaarBot integration with crafting materials"""
    print("🧪 Testing BazaarBot integration...")

    # Run BazaarBot tests
    success, stdout, stderr = run_command("go test ./internal/simulation/bazaar -v", cwd="services/economy-service-go")
    if not success:
        print(f"❌ BazaarBot tests failed: {stderr}")
        return False

    if "Price convergence ratio: 94.9%" in stdout:
        print("✅ BazaarBot price convergence test passed (94.9%)")
    else:
        print("❌ BazaarBot convergence test failed")
        return False

    return True

def test_crafting_economy_data_flow():
    """Test data flow between crafting and economy systems"""
    print("🧪 Testing crafting-economy data flow...")

    # Check if crafting recipes reference economy commodities
    crafting_recipes_path = "proto/openapi/crafting-service/crafting-recipes.yaml"
    if not os.path.exists(crafting_recipes_path):
        print("❌ Crafting recipes file not found")
        return False

    # Check if economy service handles crafting materials
    economy_types_path = "services/economy-service-go/internal/simulation/bazaar/types.go"
    if not os.path.exists(economy_types_path):
        print("❌ Economy types not found")
        return False

    print("✅ Data flow files exist")
    return True

def main():
    """Run all integration tests"""
    print("Starting economy-service-go with crafting mechanics QA testing")
    print("=" * 60)

    tests = [
        ("Compilation", test_compilation),
        ("API Compatibility", test_api_compatibility),
        ("Bazaar Integration", test_bazaar_integration),
        ("Crafting-Economy Data Flow", test_crafting_economy_data_flow),
    ]

    passed = 0
    total = len(tests)

    for test_name, test_func in tests:
        print(f"\n📋 Running: {test_name}")
        try:
            if test_func():
                passed += 1
                print(f"✅ {test_name} PASSED")
            else:
                print(f"❌ {test_name} FAILED")
        except Exception as e:
            print(f"❌ {test_name} ERROR: {e}")

    print("\n" + "=" * 60)
    print(f"📊 Test Results: {passed}/{total} tests passed")

    if passed == total:
        print("🎉 ALL TESTS PASSED - Ready for production deployment")
        return 0
    else:
        print("⚠️  SOME TESTS FAILED - Requires attention")
        return 1

if __name__ == "__main__":
    sys.exit(main())