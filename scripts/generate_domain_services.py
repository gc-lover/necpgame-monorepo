#!/usr/bin/env python3
"""
Domain Services Generator Script
Generates enterprise-grade Go services from OpenAPI specifications
"""

import os
import sys
import subprocess
from pathlib import Path
from typing import List

def generate_service(domain: str, dry_run: bool = False) -> bool:
    """Generate service for a specific domain using OPTIMIZED generator"""
    try:
        # Используем оптимизированную генерацию согласно PERFORMANCE_ENFORCEMENT.md
        cmd = [
            sys.executable,
            "scripts/generation/go_service_generator_fixed.py",
            domain  # Передаем domain как позиционный аргумент
        ]

        if dry_run:
            cmd.append("--dry-run")

        print(f"Generating OPTIMIZED service for {domain}...")
        result = subprocess.run(cmd, capture_output=True, text=True, cwd=Path.cwd())

        if result.returncode == 0:
            print(f"[OK] Successfully generated OPTIMIZED {domain}")
            if result.stdout:
                print(result.stdout)
            return True
        else:
            print(f"[ERROR] Failed to generate OPTIMIZED {domain}")
            if result.stderr:
                print("Error:", result.stderr)
            return False

    except Exception as e:
        print(f"[ERROR] Exception generating OPTIMIZED {domain}: {e}")
        return False

def main():
    # Domains to generate services for
    domains = [
        "companion-domain",
        "cyberspace-domain",
        "referral-domain",
        "cosmetic-domain",
        "guild-system-domain",
        "inventory-management-service",
        "ml-ai-domain"
    ]

    print(f"Starting generation of {len(domains)} domain services...")

    generated = 0
    failed = 0

    for domain in domains:
        # Check if OpenAPI spec exists
        spec_path = Path(f"proto/openapi/{domain}/main.yaml")
        if not spec_path.exists():
            print(f"[WARNING] Skipping {domain} - spec not found at {spec_path}")
            continue

        # Check if service already exists
        service_path = Path(f"services/{domain}-service-go")
        if service_path.exists():
            print(f"[INFO] Skipping {domain} - service already exists")
            continue

        if generate_service(domain):
            generated += 1
        else:
            failed += 1

    print(f"\nGeneration complete:")
    print(f"[OK] Generated: {generated}")
    print(f"[ERROR] Failed: {failed}")

if __name__ == "__main__":
    main()
