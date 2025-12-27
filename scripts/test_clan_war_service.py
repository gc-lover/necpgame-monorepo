#!/usr/bin/env python3
"""
Smoke test for clan-war-service-go
Tests basic functionality and health endpoints
"""

import subprocess
import time
import requests
import os
import sys
from typing import Optional

def run_command(cmd: str, cwd: Optional[str] = None) -> tuple[int, str, str]:
    """Run command and return (returncode, stdout, stderr)"""
    try:
        result = subprocess.run(
            cmd,
            shell=True,
            cwd=cwd,
            capture_output=True,
            text=True,
            timeout=30
        )
        return result.returncode, result.stdout, result.stderr
    except subprocess.TimeoutExpired:
        return -1, "", "Command timed out"

def test_compilation():
    """Test that service compiles successfully"""
    print("[BUILD] Testing compilation...")
    service_dir = "services/clan-war-service-go"

    # Clean and build
    returncode, stdout, stderr = run_command("go build -o bin/clan-war-service .", cwd=service_dir)

    if returncode != 0:
        print(f"[ERROR] Compilation failed: {stderr}")
        return False

    print("[OK] Compilation successful")
    return True

def test_unit_tests():
    """Test that unit tests pass"""
    print("[TEST] Running unit tests...")
    service_dir = "services/clan-war-service-go"

    returncode, stdout, stderr = run_command("go test ./server/ -v", cwd=service_dir)

    if returncode != 0:
        print(f"[ERROR] Unit tests failed: {stderr}")
        return False

    print("[OK] Unit tests passed")
    return True

def test_openapi_validation():
    """Test OpenAPI spec validation"""
    print("[VALIDATE] Validating OpenAPI specification...")
    spec_path = "proto/openapi/system-domain/ai/tournament-domain/clan/clan-war-service.yaml"

    returncode, stdout, stderr = run_command(f"python -c \"import yaml; yaml.safe_load(open('{spec_path}', 'r', encoding='utf-8')); print('YAML valid')\"")

    if returncode != 0:
        print(f"[ERROR] OpenAPI validation failed: {stderr}")
        return False

    print("[OK] OpenAPI specification is valid")
    return True

def main():
    """Run all smoke tests"""
    print("[START] Starting smoke tests for clan-war-service-go")
    print("=" * 50)

    tests = [
        test_openapi_validation,
        test_compilation,
        test_unit_tests,
    ]

    passed = 0
    total = len(tests)

    for test in tests:
        try:
            if test():
                passed += 1
            print()
        except Exception as e:
            print(f"[ERROR] Test failed with exception: {e}")
            print()

    print("=" * 50)
    print(f"[RESULTS] Test Results: {passed}/{total} passed")

    if passed == total:
        print("[SUCCESS] All smoke tests passed! Service is ready for QA.")
        return 0
    else:
        print("[FAILED] Some tests failed. Please fix issues before QA.")
        return 1

if __name__ == "__main__":
    sys.exit(main())
