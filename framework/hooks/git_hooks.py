#!/usr/bin/env python3
"""
NECPGAME Git Hooks Framework
Provides unbreakable validation with comprehensive monitoring and clear AI agent guidance.
"""
import os
import sys
import subprocess
from pathlib import Path
from typing import List, Dict, Any, Optional
from datetime import datetime

# Add framework to path
sys.path.insert(0, str(Path(__file__).parent.parent))

from framework.core.validation import (
    ValidationResult, ValidationSeverity,
    FileSizeValidator, ProjectStructureValidator, EmojiValidator
)
from framework.config import get_config


class GitHooksManager:
    """Manages Git hooks with unbreakable validation and monitoring."""

    def __init__(self):
        self.config = get_config()
        self.project_root = self._find_project_root()
        self.hook_type = self._detect_hook_type()

    def _find_project_root(self) -> Path:
        """Find project root by looking for .cursor directory."""
        current = Path.cwd()
        for _ in range(10):
            if (current / '.cursor').exists():
                return current
            if current.parent == current:
                break
            current = current.parent
        return Path.cwd()  # Fallback

    def _detect_hook_type(self) -> str:
        """Detect which Git hook is being executed."""
        script_name = Path(sys.argv[0]).name
        if 'pre-commit' in script_name:
            return 'pre-commit'
        elif 'pre-push' in script_name:
            return 'pre-push'
        elif 'post-checkout' in script_name:
            return 'post-checkout'
        else:
            return 'unknown'

    def _get_staged_files(self) -> List[Path]:
        """Get list of staged files."""
        try:
            result = subprocess.run([
                'git', 'diff', '--cached', '--name-only'
            ], capture_output=True, text=True, cwd=self.project_root)

            if result.returncode == 0 and result.stdout.strip():
                files = []
                for line in result.stdout.strip().split('\n'):
                    if line.strip():
                        files.append(self.project_root / line.strip())
                return files
        except Exception:
            pass
        return []

    def _has_openapi_changes(self, staged_files: List[Path]) -> bool:
        """Check if staged files include OpenAPI specifications."""
        for file_path in staged_files:
            if 'proto/openapi' in str(file_path) and file_path.suffix in ['.yaml', '.yml']:
                return True
        return False

    def _has_backend_changes(self, staged_files: List[Path]) -> bool:
        """Check if staged files include backend code."""
        for file_path in staged_files:
            if 'services/' in str(file_path) and file_path.suffix == '.go':
                return True
        return False

    def _detect_no_verify_bypass(self) -> bool:
        """Detect if user is trying to bypass hooks."""
        # Check for --no-verify flag in git command
        try:
            # Get the git command that triggered this hook
            with open(os.path.join(self.project_root, '.git', 'COMMIT_EDITMSG'), 'r') as f:
                last_commit_msg = f.read().strip()
        except:
            last_commit_msg = ""

        # Check environment variables and arguments
        if '--no-verify' in ' '.join(sys.argv) or '-n' in sys.argv:
            return True

        return False

    def _block_bypass_attempt(self) -> int:
        """Block bypass attempts with clear error message."""
        print("\nBLOCKED: --no-verify or -n flag detected")
        print("This repository enforces ZERO-TOLERANCE quality control")
        print("All commits must pass validation. Bypass attempts are logged.")
        print("\nFor AI agents: Remove --no-verify flag and fix validation errors")
        print("Documentation: .cursor/rules/always.mdc#git-policy")

        self._log_bypass_attempt()
        return 1

    def _log_bypass_attempt(self):
        """Log bypass attempts for security monitoring."""
        try:
            log_entry = {
                'timestamp': datetime.now().isoformat(),
                'user': os.environ.get('USER', os.environ.get('USERNAME', 'unknown')),
                'operation': 'bypass_attempt',
                'blocked': True,
                'details': f"Attempted bypass of {self.hook_type} hook"
            }

            log_file = self.project_root / '.git' / 'git-operations.log'
            with open(log_file, 'a') as f:
                f.write(f"{log_entry}\n")
        except Exception:
            pass  # Logging failure should not break validation

    def _log_validation_attempt(self, hook_type: str):
        """Log validation attempts."""
        try:
            log_entry = {
                'timestamp': datetime.now().isoformat(),
                'user': os.environ.get('USER', os.environ.get('USERNAME', 'unknown')),
                'operation': f'{hook_type}_validation',
                'blocked': False,
                'details': 'Validation executed'
            }

            log_file = self.project_root / '.git' / 'git-operations.log'
            with open(log_file, 'a') as f:
                f.write(f"{log_entry}\n")
        except Exception:
            pass

    def run_pre_commit_validation(self) -> int:
        """Run comprehensive pre-commit validation that cannot be bypassed."""
        print("NECPGAME FRAMEWORK: Pre-commit Validation")
        print("=" * 60)

        # Debug logging
        with open('.git/framework-debug.log', 'w') as f:
            f.write("Framework started\n")

        # BLOCK ALL BYPASS ATTEMPTS - ZERO TOLERANCE
        if self._detect_no_verify_bypass():
            return self._block_bypass_attempt()

        # Log the validation attempt
        self._log_validation_attempt("pre-commit")

        # Debug logging
        with open('.git/framework-debug.log', 'a') as f:
            f.write("Starting validation suite\n")

        # Run comprehensive validation suite
        result = ValidationResult()

        # 1. File size validation for staged files
        staged_files = self._get_staged_files()
        with open('.git/framework-debug.log', 'a') as f:
            f.write(f"Staged files: {[str(f) for f in staged_files]}\n")

        if staged_files:
            print(f"Validating {len(staged_files)} staged files...")
            file_validator = FileSizeValidator()
            file_results = file_validator.validate(staged_files)
            result.messages.extend(file_results.messages)
            with open('.git/framework-debug.log', 'a') as f:
                f.write(f"File validation: {len(file_results.messages)} messages\n")

        # 2. Project structure validation
        print("Validating project structure...")
        structure_validator = ProjectStructureValidator()
        structure_results = structure_validator.validate(self.project_root)
        result.messages.extend(structure_results.messages)

        # 3. OpenAPI validation and optimization for API spec changes
        if self._has_openapi_changes(staged_files):
            print("Validating OpenAPI specifications...")
            print(f"Found {len([f for f in staged_files if 'proto/openapi' in str(f)])} OpenAPI files")
            openapi_results = self._validate_openapi_specs()
            print(f"OpenAPI validation returned {len(openapi_results.messages)} messages")
            result.messages.extend(openapi_results.messages)

            # Auto-optimize OpenAPI specs if validation passed
            if not openapi_results.has_errors():
                print("Auto-optimizing OpenAPI specifications...")
                optimization_results = self._auto_optimize_openapi_specs()
                print(f"OpenAPI optimization returned {len(optimization_results.messages)} messages")
                result.messages.extend(optimization_results.messages)
            else:
                print("Skipping optimization due to validation errors")

        # 4. Backend optimization validation
        if self._has_backend_changes(staged_files):
            print("Validating backend optimizations...")
            backend_results = self._validate_backend_optimizations()
            result.messages.extend(backend_results.messages)

        # 5. Emoji and special Unicode characters validation
        if staged_files:
            print(f"Validating {len(staged_files)} files for forbidden Unicode characters...")
            emoji_validator = EmojiValidator()
            emoji_results = emoji_validator.validate(staged_files)
            print(f"Emoji validation completed: {len(emoji_results.messages)} issues found")
            result.messages.extend(emoji_results.messages)

        # Report results
        with open('.git/framework-debug.log', 'a') as f:
            f.write(f"Final result: {len(result.messages)} messages\n")
            for msg in result.messages:
                f.write(f"  {msg.severity}: {msg.message[:100]}\n")
            f.write("Calling _report_validation_results\n")

        return self._report_validation_results(result)

    def _validate_openapi_specs(self) -> ValidationResult:
        """Validate OpenAPI specifications using scripts validators."""
        result = ValidationResult()

        # Use the comprehensive OpenAPI domain validator from scripts
        validator_script = self.project_root / "scripts" / "validate-domains-openapi.py"
        if validator_script.exists():
            try:
                print("Running comprehensive OpenAPI domain validation...")
                process_result = subprocess.run([
                    sys.executable, str(validator_script)
                ], capture_output=True, text=True, cwd=self.project_root)

                if process_result.returncode != 0:
                    result.add_error(
                        "OPENAPI_VALIDATION_FAILED",
                        "OpenAPI domain validation failed",
                        documentation_url="scripts/validate-domains-openapi.py"
                    )
                    # Add stderr to the result for debugging
                    if process_result.stderr:
                        result.add_info(
                            "VALIDATION_ERROR_DETAILS",
                            f"Validation output: {process_result.stderr[:500]}..."
                        )
                else:
                    print("OpenAPI validation passed")

            except Exception as e:
                result.add_error(
                    "VALIDATOR_EXECUTION_FAILED",
                    f"Failed to execute OpenAPI validator: {e}",
                    documentation_url="scripts/validate-domains-openapi.py"
                )
        else:
            # Fallback to basic validation
            result.add_warning(
                "VALIDATOR_MISSING",
                "Advanced OpenAPI validator not found, using basic checks",
                documentation_url="scripts/validate-domains-openapi.py"
            )
            result = self._basic_openapi_validation()

        return result

    def _basic_openapi_validation(self) -> ValidationResult:
        """Basic OpenAPI validation as fallback."""
        result = ValidationResult()

        openapi_dir = self.project_root / "proto" / "openapi"
        if openapi_dir.exists():
            for yaml_file in openapi_dir.rglob("*.yaml"):
                try:
                    import yaml
                    with open(yaml_file, 'r', encoding='utf-8') as f:
                        spec = yaml.safe_load(f)

                    if 'openapi' not in spec:
                        result.add_error(
                            "INVALID_OPENAPI_SPEC",
                            f"Missing openapi version in {yaml_file.name}",
                            file_path=yaml_file,
                            documentation_url=".cursor/project-config.yaml#openapi-supported-versions"
                        )
                except Exception as e:
                    result.add_warning(
                        "OPENAPI_PARSE_ERROR",
                        f"Could not parse OpenAPI spec {yaml_file.name}: {e}",
                        file_path=yaml_file
                    )

        return result

    def _auto_optimize_openapi_specs(self) -> ValidationResult:
        """Auto-optimize OpenAPI specifications using scripts."""
        result = ValidationResult()

        # Use the comprehensive OpenAPI optimizer from scripts
        optimizer_script = self.project_root / "scripts" / "optimize-openapi-all.py"
        if optimizer_script.exists():
            try:
                print("Running comprehensive OpenAPI optimization...")
                process_result = subprocess.run([
                    sys.executable, str(optimizer_script),
                    str(self.project_root / "proto" / "openapi")
                ], capture_output=True, text=True, cwd=self.project_root)

                if process_result.returncode != 0:
                    result.add_error(
                        "OPENAPI_OPTIMIZATION_FAILED",
                        "OpenAPI optimization failed",
                        documentation_url="scripts/optimize-openapi-all.py"
                    )
                    if process_result.stderr:
                        result.add_info(
                            "OPTIMIZATION_ERROR_DETAILS",
                            f"Optimization output: {process_result.stderr[:500]}..."
                        )
                else:
                    print("OpenAPI optimization completed successfully")
                    # Re-stage optimized files
                    try:
                        subprocess.run([
                            'git', 'add', 'proto/openapi/'
                        ], cwd=self.project_root, capture_output=True)
                    except Exception:
                        pass  # Git add is optional

            except Exception as e:
                result.add_error(
                    "OPTIMIZER_EXECUTION_FAILED",
                    f"Failed to execute OpenAPI optimizer: {e}",
                    documentation_url="scripts/optimize-openapi-all.py"
                )
        else:
            result.add_warning(
                "OPTIMIZER_MISSING",
                "OpenAPI optimizer not found",
                documentation_url="scripts/optimize-openapi-all.py"
            )

        return result

    def _validate_backend_optimizations(self) -> ValidationResult:
        """Validate backend code optimizations using scripts validators."""
        result = ValidationResult()

        # Use comprehensive backend optimization validator from scripts
        validator_script = self.project_root / "scripts" / "validate-backend-optimizations.sh"
        if validator_script.exists():
            try:
                print("Running comprehensive backend optimization validation...")
                process_result = subprocess.run([
                    'bash', str(validator_script)
                ], capture_output=True, text=True, cwd=self.project_root)

                if process_result.returncode != 0:
                    result.add_error(
                        "BACKEND_OPTIMIZATION_VALIDATION_FAILED",
                        "Backend optimization validation failed",
                        documentation_url="scripts/validate-backend-optimizations.sh"
                    )
                    # Add stderr to the result for debugging
                    if process_result.stderr:
                        result.add_info(
                            "VALIDATION_ERROR_DETAILS",
                            f"Validation output: {process_result.stderr[:500]}..."
                        )
                else:
                    print("Backend optimization validation passed")

            except Exception as e:
                result.add_error(
                    "VALIDATOR_EXECUTION_FAILED",
                    f"Failed to execute backend validator: {e}",
                    documentation_url="scripts/validate-backend-optimizations.sh"
                )
        else:
            # Fallback to basic validation
            result.add_warning(
                "VALIDATOR_MISSING",
                "Backend optimization validator not found, using basic checks",
                documentation_url="scripts/validate-backend-optimizations.sh"
            )
            result = self._basic_backend_validation()

        return result

    def _basic_backend_validation(self) -> ValidationResult:
        """Basic backend validation as fallback."""
        result = ValidationResult()

        services_dir = self.project_root / "services"
        if services_dir.exists():
            for go_file in services_dir.rglob("*.go"):
                try:
                    with open(go_file, 'r', encoding='utf-8') as f:
                        content = f.read()

                    # Basic checks for required optimizations
                    if 'context.WithTimeout' not in content:
                        result.add_error(
                            "MISSING_CONTEXT_TIMEOUT",
                            f"Missing context timeouts in {go_file.name}",
                            file_path=go_file,
                            documentation_url=".cursor/BACKEND_OPTIMIZATION_CHECKLIST.md#context-timeouts"
                        )
                except Exception as e:
                    result.add_warning(
                        "FILE_READ_ERROR",
                        f"Could not read Go file {go_file.name}: {e}",
                        file_path=go_file
                    )

        return result

    def _report_validation_results(self, result: ValidationResult) -> int:
        """Report validation results and return appropriate exit code."""
        if not result.has_errors():
            return 0

        # Only show errors and problems - no successful validations
        print("\nVALIDATION ERRORS FOUND:")
        print("=" * 50)

        error_messages = [msg for msg in result.messages if msg.severity in [ValidationSeverity.CRITICAL, ValidationSeverity.ERROR]]

        for msg in error_messages:
            print(f"\nERROR: {msg.code}")
            print(f"File: {msg.file_path}")
            print(f"Message: {msg.message}")

            if hasattr(msg, 'suggestion') and msg.suggestion:
                print(f"Suggestion: {msg.suggestion}")

            if hasattr(msg, 'context') and msg.context:
                if 'all_violations' in msg.context:
                    print("All violations:")
                    for line_num, char, line in msg.context['all_violations']:
                        print(f"  Line {line_num}: Found {repr(char)} in '{line}'")
                elif 'sample_violations' in msg.context:
                    print("Sample violations:")
                    for line_num, char, line in msg.context['sample_violations']:
                        print(f"  Line {line_num}: Found {repr(char)} in '{line[:100]}...'")

            print("-" * 50)

        print(f"\nTOTAL ERRORS: {len(error_messages)}")
        print("\nREQUIRED ACTIONS FOR AI AGENT:")
        print("- Fix all validation errors listed above")
        print("- Remove forbidden characters and fix file size issues")
        print("- Ensure all files comply with project standards")
        print("- Run validation again after fixes")
        print("- Do not commit until all errors are resolved")

        return 49

    def run_pre_push_validation(self) -> int:
        """Run pre-push validation with additional checks."""
        print("NECPGAME FRAMEWORK: Pre-push Validation")
        print("=" * 60)

        # Log the validation attempt
        self._log_validation_attempt("pre-push")

        # Run validation (simplified for pre-push)
        result = ValidationResult()

        # Basic project structure check
        structure_validator = ProjectStructureValidator()
        structure_results = structure_validator.validate(self.project_root)
        result.messages.extend(structure_results.messages)

        return self._report_validation_results(result)

    def run_post_checkout_validation(self) -> int:
        """Run post-checkout validation."""
        print("NECPGAME FRAMEWORK: Post-checkout Validation")
        print("=" * 60)

        # This could include branch validation, workspace cleanup, etc.
        return 0


def main():
    """Main entry point for Git hooks."""
    try:
        print("Initializing GitHooksManager...")
        manager = GitHooksManager()
        print(f"Hook type: {manager.hook_type}")

        if manager.hook_type == 'pre-commit':
            print("Running pre-commit validation...")
            return manager.run_pre_commit_validation()
        elif manager.hook_type == 'pre-push':
            return manager.run_pre_push_validation()
        elif manager.hook_type == 'post-checkout':
            return manager.run_post_checkout_validation()
        else:
            print(f"Unknown hook type: {manager.hook_type}")
            return 1
    except Exception as e:
        print(f"ERROR in main(): {e}")
        import traceback
        traceback.print_exc()
        return 1


if __name__ == "__main__":
    sys.exit(main())
