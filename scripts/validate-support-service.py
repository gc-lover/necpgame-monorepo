#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Support Service Validation Script

This script validates the implementation of the support-service-go microservice.
It checks for proper structure, models, repositories, and basic functionality.
"""

import os
import sys
import json
from pathlib import Path

def check_file_exists(file_path, description):
    """Check if file exists"""
    if os.path.exists(file_path):
        print(f"[OK] {description}: {file_path}")
        return True
    else:
        print(f"[ERROR] MISSING {description}: {file_path}")
        return False

def check_directory_exists(dir_path, description):
    """Check if directory exists"""
    if os.path.exists(dir_path):
        print(f"[OK] {description}: {dir_path}")
        return True
    else:
        print(f"[ERROR] MISSING {description}: {dir_path}")
        return False

def validate_go_module():
    """Validate Go module structure"""
    print("\n=== VALIDATING GO MODULE ===")

    base_path = "services/support-service-go"
    checks = [
        (f"{base_path}/go.mod", "Go module file"),
        (f"{base_path}/main.go", "Main Go file"),
        (f"{base_path}/go.sum", "Go dependencies file"),
    ]

    all_passed = True
    for file_path, description in checks:
        if not check_file_exists(file_path, description):
            all_passed = False

    return all_passed

def validate_directory_structure():
    """Validate directory structure"""
    print("\n=== VALIDATING DIRECTORY STRUCTURE ===")

    base_path = "services/support-service-go"
    directories = [
        (f"{base_path}/internal", "Internal package directory"),
        (f"{base_path}/internal/models", "Models package"),
        (f"{base_path}/internal/repository", "Repository package"),
        (f"{base_path}/internal/repository/postgres", "PostgreSQL repository"),
        (f"{base_path}/internal/config", "Configuration package"),
        (f"{base_path}/internal/database", "Database package"),
    ]

    all_passed = True
    for dir_path, description in directories:
        if not check_directory_exists(dir_path, description):
            all_passed = False

    return all_passed

def validate_models():
    """Validate model files"""
    print("\n=== VALIDATING MODELS ===")

    model_files = [
        "services/support-service-go/internal/models/ticket.go",
        "services/support-service-go/internal/models/response.go",
        "services/support-service-go/internal/models/sla.go",
    ]

    # Define which structs should be in which files
    struct_requirements = {
        "ticket.go": ["Ticket"],
        "response.go": ["TicketResponse"],
        "sla.go": ["TicketSLAInfo", "SupportStatsResponse"],
    }

    all_passed = True

    for file_path in model_files:
        filename = os.path.basename(file_path)
        if not check_file_exists(file_path, f"Model file {filename}"):
            all_passed = False
            continue

        if filename not in struct_requirements:
            continue

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            for struct_name in struct_requirements[filename]:
                if f"type {struct_name} struct" in content:
                    print(f"[OK] Found struct {struct_name} in {filename}")
                else:
                    print(f"[ERROR] MISSING struct {struct_name} in {filename}")
                    all_passed = False

        except Exception as e:
            print(f"[ERROR] reading {file_path}: {e}")
            all_passed = False

    return all_passed

def validate_repository():
    """Validate repository implementation"""
    print("\n=== VALIDATING REPOSITORY ===")

    repo_files = [
        "services/support-service-go/internal/repository/interfaces.go",
        "services/support-service-go/internal/repository/postgres/ticket_repository.go",
        "services/support-service-go/internal/repository/postgres/ticket_response_repository.go",
        "services/support-service-go/internal/repository/postgres/sla_repository.go",
        "services/support-service-go/internal/repository/postgres/simple_repository.go",
    ]

    required_methods = {
        "ticket_repository.go": ["func.*Create", "func.*GetByID", "func.*Update", "func.*UpdateStatus", "func.*AssignAgent", "func.*Close"],
        "ticket_response_repository.go": ["func.*Create", "func.*GetByID", "func.*GetByTicketID", "func.*Update", "func.*Delete"],
        "sla_repository.go": ["func.*GetSLAInfo", "func.*UpdateSLAStatus", "func.*GetSLAStats", "func.*GetOverdueTickets"],
        "simple_repository.go": ["func NewSimpleRepository"],
    }

    all_passed = True

    for file_path in repo_files:
        if not check_file_exists(file_path, f"Repository file {os.path.basename(file_path)}"):
            all_passed = False
            continue

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            filename = os.path.basename(file_path)
            if filename in required_methods:
                for method_pattern in required_methods[filename]:
                    import re
                    if re.search(method_pattern, content):
                        print(f"[OK] Found method pattern {method_pattern} in {filename}")
                    else:
                        print(f"[ERROR] MISSING method pattern {method_pattern} in {filename}")
                        all_passed = False

        except Exception as e:
            print(f"[ERROR] ERROR reading {file_path}: {e}")
            all_passed = False

    return all_passed

def validate_config():
    """Validate configuration"""
    print("\n=== VALIDATING CONFIGURATION ===")

    config_files = [
        "services/support-service-go/internal/config/config.go",
    ]

    all_passed = True

    for file_path in config_files:
        if not check_file_exists(file_path, f"Config file {os.path.basename(file_path)}"):
            all_passed = False
            continue

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            if "type Config struct" in content:
                print(f"[OK] Found Config struct in {file_path}")
            else:
                print(f"[ERROR] MISSING Config struct in {file_path}")
                all_passed = False

            if "func Load" in content:
                print(f"[OK] Found Load function in {file_path}")
            else:
                print(f"[ERROR] MISSING Load function in {file_path}")
                all_passed = False

        except Exception as e:
            print(f"[ERROR] ERROR reading {file_path}: {e}")
            all_passed = False

    return all_passed

def validate_database():
    """Validate database connection"""
    print("\n=== VALIDATING DATABASE ===")

    db_files = [
        "services/support-service-go/internal/database/connection.go",
    ]

    all_passed = True

    for file_path in db_files:
        if not check_file_exists(file_path, f"Database file {os.path.basename(file_path)}"):
            all_passed = False
            continue

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            if "NewConnection" in content:
                print(f"[OK] Found NewConnection function in {file_path}")
            else:
                print(f"[ERROR] MISSING NewConnection function in {file_path}")
                all_passed = False

            if "pgxpool.NewWithConfig" in content or "sql.Open" in content:
                print(f"[OK] Found database connection code in {file_path}")
            else:
                print(f"[ERROR] MISSING database connection code in {file_path}")
                all_passed = False

        except Exception as e:
            print(f"[ERROR] ERROR reading {file_path}: {e}")
            all_passed = False

    return all_passed

def validate_openapi():
    """Validate OpenAPI specification"""
    print("\n=== VALIDATING OPENAPI SPEC ===")

    openapi_file = "proto/openapi/support-service/main.yaml"

    if not check_file_exists(openapi_file, "OpenAPI specification"):
        return False

    try:
        with open(openapi_file, 'r', encoding='utf-8') as f:
            content = f.read()

        required_fields = [
            "openapi:",
            "info:",
            "title:",
            "paths:",
            "/health:",
            "get:",
        ]

        all_passed = True
        for field in required_fields:
            if field in content:
                print(f"[OK] Found '{field}' in OpenAPI spec")
            else:
                print(f"[ERROR] MISSING '{field}' in OpenAPI spec")
                all_passed = False

        return all_passed

    except Exception as e:
        print(f"[ERROR] ERROR reading OpenAPI spec: {e}")
        return False

def validate_ticket_status_enum():
    """Validate ticket status enum"""
    print("\n=== VALIDATING TICKET STATUS ENUM ===")

    try:
        with open("services/support-service-go/internal/models/ticket.go", 'r', encoding='utf-8') as f:
            content = f.read()

        required_statuses = [
            "TicketStatusOpen",
            "TicketStatusInProgress",
            "TicketStatusPendingCustomer",
            "TicketStatusResolved",
            "TicketStatusClosed",
        ]

        all_passed = True
        for status in required_statuses:
            if status in content:
                print(f"[OK] Found ticket status {status}")
            else:
                print(f"[ERROR] MISSING ticket status {status}")
                all_passed = False

        return all_passed

    except Exception as e:
        print(f"[ERROR] ERROR validating ticket statuses: {e}")
        return False

def validate_ticket_priority_enum():
    """Validate ticket priority enum"""
    print("\n=== VALIDATING TICKET PRIORITY ENUM ===")

    try:
        with open("services/support-service-go/internal/models/ticket.go", 'r', encoding='utf-8') as f:
            content = f.read()

        required_priorities = [
            "TicketPriorityLow",
            "TicketPriorityNormal",
            "TicketPriorityHigh",
            "TicketPriorityUrgent",
            "TicketPriorityCritical",
        ]

        all_passed = True
        for priority in required_priorities:
            if priority in content:
                print(f"[OK] Found ticket priority {priority}")
            else:
                print(f"[ERROR] MISSING ticket priority {priority}")
                all_passed = False

        return all_passed

    except Exception as e:
        print(f"[ERROR] ERROR validating ticket priorities: {e}")
        return False

def validate_sla_status_enum():
    """Validate SLA status enum"""
    print("\n=== VALIDATING SLA STATUS ENUM ===")

    try:
        with open("services/support-service-go/internal/models/sla.go", 'r', encoding='utf-8') as f:
            content = f.read()

        required_sla_statuses = [
            "SLAStatusCompliant",
            "SLAStatusWarning",
            "SLAStatusBreached",
        ]

        all_passed = True
        for sla_status in required_sla_statuses:
            if sla_status in content:
                print(f"[OK] Found SLA status {sla_status}")
            else:
                print(f"[ERROR] MISSING SLA status {sla_status}")
                all_passed = False

        return all_passed

    except Exception as e:
        print(f"[ERROR] ERROR validating SLA statuses: {e}")
        return False

def main():
    """Main validation function"""
    print("SUPPORT SERVICE VALIDATION")
    print("=" * 50)

    # Change to project root
    os.chdir(Path(__file__).parent.parent)

    validation_results = []

    # Run all validations
    validation_results.append(("Go Module", validate_go_module()))
    validation_results.append(("Directory Structure", validate_directory_structure()))
    validation_results.append(("Models", validate_models()))
    validation_results.append(("Repository", validate_repository()))
    validation_results.append(("Configuration", validate_config()))
    validation_results.append(("Database", validate_database()))
    validation_results.append(("OpenAPI Spec", validate_openapi()))
    validation_results.append(("Ticket Status Enum", validate_ticket_status_enum()))
    validation_results.append(("Ticket Priority Enum", validate_ticket_priority_enum()))
    validation_results.append(("SLA Status Enum", validate_sla_status_enum()))

    # Summary
    print("\n" + "=" * 50)
    print("VALIDATION SUMMARY")
    print("=" * 50)

    passed_count = 0
    total_count = len(validation_results)

    for test_name, passed in validation_results:
        status = "[PASS]" if passed else "[FAIL]"
        print(f"{test_name}: {status}")
        if passed:
            passed_count += 1

    print(f"\nOverall: {passed_count}/{total_count} validations passed")

    if passed_count == total_count:
        print("ALL VALIDATIONS PASSED!")
        print("Support service implementation is complete and ready for testing.")
        return 0
    else:
        print(f"{total_count - passed_count} validations failed.")
        print("Please fix the issues and run validation again.")
        return 1

if __name__ == "__main__":
    sys.exit(main())
