#!/usr/bin/env python3
"""
NECPGAME Base Validator
SOLID: Base class for all validators
"""

from typing import List, Dict, Any, Tuple

from scripts.core.logger import Logger


class ValidationResult:
    """Result of validation"""

    def __init__(self):
        self.errors: List[str] = []
        self.warnings: List[str] = []
        self.valid = True

    def add_error(self, message: str):
        self.errors.append(message)
        self.valid = False

    def add_warning(self, message: str):
        self.warnings.append(message)

    def has_errors(self) -> bool:
        return len(self.errors) > 0

    def has_warnings(self) -> bool:
        return len(self.warnings) > 0


class BaseValidator:
    """
    Base class for all validators.
    Single Responsibility: Provide validation framework.
    """

    def __init__(self, logger: Logger):
        self.logger = logger
        self.result = ValidationResult()

    def validate(self, target: Any) -> ValidationResult:
        """Main validation method - override in subclasses"""
        raise NotImplementedError("Subclasses must implement validate() method")

    def reset(self):
        """Reset validation result"""
        self.result = ValidationResult()

    def log_results(self, target_name: str):
        """Log validation results"""
        if self.result.valid:
            self.logger.info(f"PASS {target_name} validation passed")
        else:
            self.logger.error(f"FAIL {target_name} validation failed")

        if self.result.errors:
            for error in self.result.errors:
                self.logger.error(f"  ERROR: {error}")

        if self.result.warnings:
            for warning in self.result.warnings:
                self.logger.warning(f"  WARNING: {warning}")
