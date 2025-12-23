#!/usr/bin/env python3
"""
NECPGAME OpenAPI Validator
SOLID: Single Responsibility - validates OpenAPI specifications
"""

from pathlib import Path
from typing import Dict, Any, List

from core.config import ConfigManager
from openapi.openapi_manager import OpenAPIManager
from validation.base_validator import BaseValidator, ValidationResult


class OpenAPIValidator(BaseValidator):
    """
    Validates OpenAPI specifications.
    Single Responsibility: Validate OpenAPI specs structure and content.
    """

    def __init__(self, openapi_manager: OpenAPIManager, config: ConfigManager, logger):
        super().__init__(logger)
        self.openapi = openapi_manager
        self.config = config

    def validate(self, spec_file: Path) -> ValidationResult:
        """Validate OpenAPI specification file"""
        self.reset()

        try:
            spec = self.openapi.load_spec(spec_file)
            self._validate_basic_structure(spec, spec_file)
            self._validate_go_generation_requirements(spec, spec_file)
            self._validate_with_tools(spec_file)

        except Exception as e:
            self.result.add_error(f"Failed to validate {spec_file}: {e}")

        return self.result

    def _validate_basic_structure(self, spec: Dict[str, Any], spec_file: Path):
        """Validate basic OpenAPI structure"""
        # Check required fields
        required_fields = self.config.get('openapi', 'required_fields') or ['openapi', 'info', 'paths']
        for field in required_fields:
            if field not in spec:
                self.result.add_error(f"Missing required field '{field}' in {spec_file}")

        # Check OpenAPI version
        if 'openapi' in spec:
            version = spec['openapi']
            expected = self.config.get('openapi', 'version')
            if expected and not version.startswith(expected.split('.')[0]):
                self.result.add_error(f"Unsupported OpenAPI version {version} in {spec_file} (need {expected})")

        # Check info section
        if 'info' in spec:
            info = spec['info']
            required_info = self.config.get('openapi', 'info_required') or ['title', 'version']
            for field in required_info:
                if field not in info:
                    self.result.add_error(f"Missing info.{field} in {spec_file}")

    def _validate_go_generation_requirements(self, spec: Dict[str, Any], spec_file: Path):
        """Validate requirements for Go code generation"""
        if 'paths' not in spec:
            return

        for path, methods in spec['paths'].items():
            if not isinstance(methods, dict):
                continue

            for method, operation in methods.items():
                if method not in ['get', 'post', 'put', 'delete', 'patch', 'options', 'head']:
                    continue

                if not isinstance(operation, dict):
                    continue

                # operationId is required for Go generation
                if 'operationId' not in operation:
                    self.result.add_error(f"Missing operationId for {method.upper()} {path} in {spec_file}")

                # Check responses
                if 'responses' not in operation:
                    self.result.add_error(f"Missing responses for {method.upper()} {path} in {spec_file}")
                else:
                    responses = operation['responses']
                    # Check for success responses
                    success_codes = ['200', '201']
                    if method.lower() == 'delete':
                        success_codes.append('204')
                    if '/ws' in path or 'websocket' in path.lower():
                        success_codes = ['101', '200']

                    if not any(code in responses for code in success_codes):
                        self.result.add_warning(f"No success response for {method.upper()} {path} in {spec_file}")

    def _validate_with_tools(self, spec_file: Path):
        """Validate with external tools"""
        # Validate with redocly
        valid, errors, warnings = self.openapi.validate_with_redocly(spec_file)
        for error in errors:
            self.result.add_error(f"Redocly: {error}")
        for warning in warnings:
            self.result.add_warning(f"Redocly: {warning}")

        # Validate with ogen (if available)
        try:
            valid, errors = self.openapi.validate_with_ogen(spec_file)
            for error in errors:
                self.result.add_error(f"Ogen: {error}")
        except:
            # ogen not available, skip
            pass
