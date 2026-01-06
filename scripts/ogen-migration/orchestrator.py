#!/usr/bin/env python3
"""
Ogen Migration Orchestrator

Core component for orchestrating the migration of services from oapi-codegen to ogen.
Handles service discovery, dependency resolution, migration planning, and execution.
"""

import asyncio
import json
import logging
import os
import subprocess
import sys
from dataclasses import dataclass, field
from datetime import datetime, timedelta
from pathlib import Path
from typing import Dict, List, Optional, Set, Tuple

import yaml


@dataclass
class ServiceInfo:
    """Information about a service to be migrated."""
    name: str
    path: Path
    openapi_spec: Path
    dependencies: Set[str] = field(default_factory=set)
    risk_level: str = "medium"  # low, medium, high
    complexity: str = "medium"  # simple, medium, complex
    status: str = "pending"  # pending, migrating, completed, failed
    migrated_at: Optional[datetime] = None
    error_message: Optional[str] = None


@dataclass
class MigrationPlan:
    """Plan for service migration."""
    service: ServiceInfo
    phase: str  # preparation, migration, validation, rollback
    estimated_duration: timedelta
    dependencies_satisfied: bool = False
    can_start: bool = False


class MigrationOrchestrator:
    """Main orchestrator for ogen migration process."""

    def __init__(self, base_path: Path, config_path: Optional[Path] = None):
        self.base_path = base_path
        self.config_path = config_path or base_path / "scripts" / "ogen-migration" / "config.yaml"
        self.services: Dict[str, ServiceInfo] = {}
        self.migration_queue: List[MigrationPlan] = []
        self.logger = logging.getLogger(__name__)

        # Load configuration
        self.config = self._load_config()

    def _load_config(self) -> Dict:
        """Load orchestrator configuration."""
        if self.config_path.exists():
            with open(self.config_path, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f)
        else:
            return self._get_default_config()

    def _get_default_config(self) -> Dict:
        """Get default configuration."""
        return {
            "phases": {
                "preparation": {"duration_days": 14},
                "pilot": {"duration_days": 21},
                "accelerated": {"duration_days": 84},
                "validation": {"duration_days": 28}
            },
            "risk_levels": {
                "low": {"max_concurrent": 5, "requires_review": False},
                "medium": {"max_concurrent": 3, "requires_review": True},
                "high": {"max_concurrent": 1, "requires_review": True}
            },
            "ogen_version": "latest",
            "parallel_migrations": 2
        }

    def discover_services(self) -> None:
        """Discover all services that need migration."""
        self.logger.info("Discovering services for migration...")

        # Find all service directories
        services_path = self.base_path / "services"
        if not services_path.exists():
            raise FileNotFoundError(f"Services directory not found: {services_path}")

        for service_dir in services_path.iterdir():
            if not service_dir.is_dir():
                continue

            service_name = service_dir.name
            openapi_spec = self._find_openapi_spec(service_dir)

            if openapi_spec:
                service_info = ServiceInfo(
                    name=service_name,
                    path=service_dir,
                    openapi_spec=openapi_spec,
                    risk_level=self._assess_risk_level(service_dir),
                    complexity=self._assess_complexity(service_dir)
                )

                # Find dependencies
                service_info.dependencies = self._find_dependencies(service_dir)

                self.services[service_name] = service_info
                self.logger.info(f"Discovered service: {service_name} (risk: {service_info.risk_level})")

        self.logger.info(f"Discovered {len(self.services)} services")

    def _find_openapi_spec(self, service_dir: Path) -> Optional[Path]:
        """Find OpenAPI specification in service directory."""
        # Look for main.yaml in proto/openapi/service-name directory
        spec_path = self.base_path / "proto" / "openapi" / service_dir.name / "main.yaml"
        if spec_path.exists():
            return spec_path

        # Look for any .yaml file in service proto directory
        proto_dir = service_dir / "proto"
        if proto_dir.exists():
            for yaml_file in proto_dir.glob("*.yaml"):
                return yaml_file

        return None

    def _assess_risk_level(self, service_dir: Path) -> str:
        """Assess risk level of service migration."""
        # Simple heuristic based on service characteristics
        go_files = list(service_dir.glob("**/*.go"))
        openapi_files = list((self.base_path / "proto" / "openapi" / service_dir.name).glob("*.yaml"))

        if len(go_files) < 10 and len(openapi_files) == 1:
            return "low"
        elif len(go_files) < 50 and len(openapi_files) <= 3:
            return "medium"
        else:
            return "high"

    def _assess_complexity(self, service_dir: Path) -> str:
        """Assess complexity of service migration."""
        go_files = list(service_dir.glob("**/*.go"))
        proto_files = list(service_dir.glob("**/*.proto"))

        if len(go_files) < 20 and not proto_files:
            return "simple"
        elif len(go_files) < 100:
            return "medium"
        else:
            return "complex"

    def _find_dependencies(self, service_dir: Path) -> Set[str]:
        """Find service dependencies."""
        dependencies = set()

        # Check go.mod for dependencies
        go_mod_path = service_dir / "go.mod"
        if go_mod_path.exists():
            with open(go_mod_path, 'r', encoding='utf-8') as f:
                content = f.read()
                # Simple check for service dependencies in imports
                for service_name in self.services.keys():
                    if f"github.com/gc-lover/necp-game/services/{service_name}" in content:
                        dependencies.add(service_name)

        return dependencies

    def create_migration_plan(self) -> None:
        """Create migration plan based on service dependencies and risk levels."""
        self.logger.info("Creating migration plan...")

        # Sort services by dependencies (topological sort)
        sorted_services = self._topological_sort()

        # Create migration plans
        for service_name in sorted_services:
            service = self.services[service_name]

            plan = MigrationPlan(
                service=service,
                phase=self._determine_phase(service),
                estimated_duration=self._estimate_duration(service),
                dependencies_satisfied=self._check_dependencies(service)
            )

            plan.can_start = plan.dependencies_satisfied
            self.migration_queue.append(plan)

        self.logger.info(f"Created migration plan for {len(self.migration_queue)} services")

    def _topological_sort(self) -> List[str]:
        """Perform topological sort of services based on dependencies."""
        # Simple topological sort implementation
        visited = set()
        result = []

        def visit(service_name: str):
            if service_name in visited:
                return
            visited.add(service_name)

            service = self.services[service_name]
            for dep in service.dependencies:
                if dep in self.services:
                    visit(dep)

            result.append(service_name)

        for service_name in self.services:
            visit(service_name)

        return result

    def _determine_phase(self, service: ServiceInfo) -> str:
        """Determine migration phase for service."""
        if service.risk_level == "low" and service.complexity == "simple":
            return "pilot"
        elif service.risk_level == "high" or service.complexity == "complex":
            return "accelerated"
        else:
            return "accelerated"

    def _estimate_duration(self, service: ServiceInfo) -> timedelta:
        """Estimate migration duration for service."""
        base_days = 1

        if service.complexity == "medium":
            base_days = 3
        elif service.complexity == "complex":
            base_days = 7

        if service.risk_level == "high":
            base_days *= 2

        return timedelta(days=base_days)

    def _check_dependencies(self, service: ServiceInfo) -> bool:
        """Check if all dependencies are satisfied."""
        for dep in service.dependencies:
            if dep in self.services:
                dep_service = self.services[dep]
                if dep_service.status != "completed":
                    return False
        return True

    async def execute_migration(self, dry_run: bool = False) -> None:
        """Execute the migration plan."""
        self.logger.info("Starting migration execution...")

        # Create semaphore for concurrent migrations
        semaphore = asyncio.Semaphore(self.config.get("parallel_migrations", 2))

        async def migrate_service(plan: MigrationPlan):
            async with semaphore:
                if not plan.can_start:
                    self.logger.info(f"Skipping {plan.service.name} - dependencies not satisfied")
                    return

                try:
                    self.logger.info(f"Starting migration of {plan.service.name}")
                    plan.service.status = "migrating"

                    if not dry_run:
                        success = await self._migrate_single_service(plan.service)
                        if success:
                            plan.service.status = "completed"
                            plan.service.migrated_at = datetime.now()
                            self.logger.info(f"Successfully migrated {plan.service.name}")
                        else:
                            plan.service.status = "failed"
                            self.logger.error(f"Failed to migrate {plan.service.name}")
                    else:
                        self.logger.info(f"DRY RUN: Would migrate {plan.service.name}")
                        plan.service.status = "completed"

                except Exception as e:
                    plan.service.status = "failed"
                    plan.service.error_message = str(e)
                    self.logger.error(f"Migration failed for {plan.service.name}: {e}")

        # Execute migrations concurrently
        tasks = [migrate_service(plan) for plan in self.migration_queue if plan.can_start]
        await asyncio.gather(*tasks)

        self.logger.info("Migration execution completed")

    async def _migrate_single_service(self, service: ServiceInfo) -> bool:
        """Migrate a single service to ogen."""
        try:
            # Step 1: Backup current code
            await self._backup_service(service)

            # Step 2: Generate ogen code
            await self._generate_ogen_code(service)

            # Step 3: Update imports and dependencies
            await self._update_imports(service)

            # Step 4: Run tests
            if not await self._run_tests(service):
                await self._rollback_service(service)
                return False

            # Step 5: Update build configuration
            await self._update_build_config(service)

            return True

        except Exception as e:
            self.logger.error(f"Migration failed for {service.name}: {e}")
            await self._rollback_service(service)
            return False

    async def _backup_service(self, service: ServiceInfo) -> None:
        """Create backup of service before migration."""
        backup_path = service.path.parent / f"{service.name}_backup_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
        self.logger.info(f"Creating backup: {backup_path}")

        # Simple copy for now - in real implementation use proper backup
        import shutil
        shutil.copytree(service.path, backup_path)

    async def _generate_ogen_code(self, service: ServiceInfo) -> None:
        """Generate ogen code from OpenAPI spec."""
        self.logger.info(f"Generating ogen code for {service.name}")

        cmd = [
            "ogen",
            "--target", str(service.path / "internal" / "ogen"),
            "--clean",
            str(service.openapi_spec)
        ]

        result = await asyncio.create_subprocess_exec(
            *cmd,
            stdout=asyncio.subprocess.PIPE,
            stderr=asyncio.subprocess.PIPE
        )

        stdout, stderr = await result.communicate()

        if result.returncode != 0:
            raise Exception(f"ogen generation failed: {stderr.decode()}")

        self.logger.info(f"Successfully generated ogen code for {service.name}")

    async def _update_imports(self, service: ServiceInfo) -> None:
        """Update Go imports to use new ogen generated code."""
        self.logger.info(f"Updating imports for {service.name}")

        # Find Go files that need import updates
        go_files = list(service.path.glob("**/*.go"))

        for go_file in go_files:
            await self._update_file_imports(go_file)

    async def _update_file_imports(self, file_path: Path) -> None:
        """Update imports in a single Go file."""
        # This is a simplified version - real implementation would use AST parsing
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Replace old oapi-codegen imports with ogen imports
        content = content.replace(
            'github.com/deepmap/oapi-codegen',
            'github.com/ogen-go/ogen'
        )

        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)

    async def _run_tests(self, service: ServiceInfo) -> bool:
        """Run tests for migrated service."""
        self.logger.info(f"Running tests for {service.name}")

        try:
            result = await asyncio.create_subprocess_exec(
                "go", "test", "./...",
                cwd=service.path,
                stdout=asyncio.subprocess.PIPE,
                stderr=asyncio.subprocess.PIPE
            )

            stdout, stderr = await result.communicate()
            return result.returncode == 0

        except Exception as e:
            self.logger.error(f"Test execution failed for {service.name}: {e}")
            return False

    async def _update_build_config(self, service: ServiceInfo) -> None:
        """Update build configuration for ogen."""
        self.logger.info(f"Updating build config for {service.name}")

        # Update go.mod if needed
        go_mod_path = service.path / "go.mod"
        if go_mod_path.exists():
            # Add ogen dependency if not present
            with open(go_mod_path, 'r', encoding='utf-8') as f:
                content = f.read()

            if 'github.com/ogen-go/ogen' not in content:
                # Simple append - real implementation should use proper go.mod parsing
                with open(go_mod_path, 'a', encoding='utf-8') as f:
                    f.write('\nrequire github.com/ogen-go/ogen latest\n')

    async def _rollback_service(self, service: ServiceInfo) -> None:
        """Rollback service to previous state."""
        self.logger.info(f"Rolling back {service.name}")

        # Find latest backup
        backup_pattern = f"{service.name}_backup_"
        backups = [d for d in service.path.parent.iterdir()
                  if d.is_dir() and d.name.startswith(backup_pattern)]

        if backups:
            latest_backup = max(backups, key=lambda x: x.stat().st_mtime)
            self.logger.info(f"Rolling back to {latest_backup}")

            # Remove current service directory
            import shutil
            shutil.rmtree(service.path)

            # Restore from backup
            shutil.copytree(latest_backup, service.path)

    def generate_report(self) -> Dict:
        """Generate migration report."""
        report = {
            "timestamp": datetime.now().isoformat(),
            "total_services": len(self.services),
            "completed": 0,
            "failed": 0,
            "pending": 0,
            "in_progress": 0,
            "services": []
        }

        for service in self.services.values():
            if service.status == "completed":
                report["completed"] += 1
            elif service.status == "failed":
                report["failed"] += 1
            elif service.status == "migrating":
                report["in_progress"] += 1
            else:
                report["pending"] += 1

            report["services"].append({
                "name": service.name,
                "status": service.status,
                "risk_level": service.risk_level,
                "complexity": service.complexity,
                "migrated_at": service.migrated_at.isoformat() if service.migrated_at else None,
                "error": service.error_message
            })

        return report

    def save_report(self, output_path: Optional[Path] = None) -> None:
        """Save migration report to file."""
        if output_path is None:
            output_path = self.base_path / "scripts" / "ogen-migration" / f"migration_report_{datetime.now().strftime('%Y%m%d_%H%M%S')}.json"

        report = self.generate_report()

        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)

        self.logger.info(f"Migration report saved to {output_path}")


async def main():
    """Main entry point."""
    logging.basicConfig(level=logging.INFO)

    # Initialize orchestrator
    base_path = Path(__file__).parent.parent.parent
    orchestrator = MigrationOrchestrator(base_path)

    # Parse command line arguments
    import argparse
    parser = argparse.ArgumentParser(description="Ogen Migration Orchestrator")
    parser.add_argument("--dry-run", action="store_true", help="Perform dry run without actual migration")
    parser.add_argument("--report-only", action="store_true", help="Only generate report without migration")
    parser.add_argument("--config", type=Path, help="Path to configuration file")

    args = parser.parse_args()

    if args.config:
        orchestrator.config_path = args.config

    # Discover services
    orchestrator.discover_services()

    # Create migration plan
    orchestrator.create_migration_plan()

    if args.report_only:
        orchestrator.save_report()
        return

    # Execute migration
    await orchestrator.execute_migration(dry_run=args.dry_run)

    # Generate final report
    orchestrator.save_report()


if __name__ == "__main__":
    asyncio.run(main())
