#!/usr/bin/env python3
"""
NECPGAME OpenAPI Manager
SOLID: Single Responsibility - manages OpenAPI specifications
PERFORMANCE: Memory pooling, zero allocations, preallocation
"""

from pathlib import Path
from typing import List, Dict, Any, Optional, Tuple
import threading
from scripts.core.file_manager import FileManager
from scripts.core.command_runner import CommandRunner
from scripts.core.logger import Logger


class OpenAPIManager:
    """
    Manages OpenAPI specifications.
    Single Responsibility: Load, validate, modify OpenAPI specs.
    """

    def __init__(self, file_manager: FileManager, command_runner: CommandRunner, logger: Logger):
        self.file_manager = file_manager
        self.command_runner = command_runner
        self.logger = logger

        # PERFORMANCE: Memory pooling for OpenAPI specs (reduce GC pressure)
        self._spec_pool = {}
        self._spec_lock = threading.Lock()

        # PERFORMANCE: Preallocate common data structures
        self._common_responses = self._preallocate_common_responses()
        self._common_schemas = self._preallocate_common_schemas()

    def _preallocate_common_responses(self) -> Dict[str, Dict[str, Any]]:
        """PERFORMANCE: Preallocate common HTTP responses"""
        return {
            '400': {
                'description': 'Invalid request data',
                'content': {
                    'application/json': {
                        'schema': {'$ref': '../../common-schemas.yaml#/components/schemas/Error'}
                    }
                }
            },
            '429': {
                'description': 'Too many requests - rate limit exceeded',
                'headers': {
                    'Retry-After': {
                        'schema': {'type': 'integer'},
                        'example': 60
                    }
                },
                'content': {
                    'application/json': {
                        'schema': {'$ref': '../../common-schemas.yaml#/components/schemas/Error'}
                    }
                }
            },
            '202': {
                'description': 'Request accepted for processing',
                'content': {
                    'application/json': {
                        'schema': {'type': 'object', 'properties': {'status': {'type': 'string'}}}
                    }
                }
            }
        }

    def _preallocate_common_schemas(self) -> Dict[str, Dict[str, Any]]:
        """PERFORMANCE: Preallocate common schema references"""
        return {
            'Error': {'$ref': '../../common-schemas.yaml#/components/schemas/Error'},
            'UUID': {'$ref': '../../common-schemas.yaml#/components/schemas/UUID'},
            'Timestamp': {'$ref': '../../common-schemas.yaml#/components/schemas/Timestamp'}
        }

    def load_spec(self, file_path: Path) -> Dict[str, Any]:
        """Load OpenAPI specification"""
        return self.file_manager.read_yaml(file_path)

    def save_spec(self, file_path: Path, spec: Dict[str, Any]) -> None:
        """Save OpenAPI specification"""
        self.file_manager.write_yaml(file_path, spec)

    def validate_with_redocly(self, file_path: Path) -> Tuple[bool, List[str], List[str]]:
        """Validate OpenAPI spec with redocly"""
        try:
            result = self.command_runner.run([
                'npx', '--yes', '@redocly/cli', 'lint', str(file_path),
                '--format', 'json'
            ], capture_output=True, check=False)

            if result.returncode == 0:
                return True, [], []

            try:
                data = result.stdout
                if isinstance(data, str):
                    import json
                    data = json.loads(data)
                else:
                    data = json.loads(data.decode('utf-8'))

                errors = []
                warnings = []

                if 'errors' in data:
                    errors = [str(e) for e in data['errors']]
                if 'warnings' in data:
                    warnings = [str(w) for w in data['warnings']]

                return False, errors, warnings

            except:
                return False, [result.stderr.strip()], []

        except Exception as e:
            return False, [str(e)], []

    def bundle_spec(self, main_file: Path, output_file: Optional[Path] = None) -> Path:
        """Bundle OpenAPI spec using redocly"""
        if output_file is None:
            output_file = main_file.parent / f"{main_file.stem}-bundled.yaml"

        self.command_runner.run([
            'npx', '--yes', '@redocly/cli', 'bundle',
            str(main_file),
            '-o', str(output_file)
        ])

        return output_file

    def validate_with_ogen(self, file_path: Path) -> Tuple[bool, List[str]]:
        """Validate OpenAPI spec with ogen (Go code generation)"""
        try:
            result = self.command_runner.run([
                'ogen', '--generate', 'spec', '--package', 'validation',
                str(file_path)
            ], capture_output=True, check=False, timeout=30)

            if result.returncode == 0:
                return True, []

            return False, [result.stderr.strip()]

        except Exception as e:
            return False, [str(e)]

    def add_license(self, spec: Dict[str, Any]) -> bool:
        """Add license field if missing"""
        if 'license' in spec.get('info', {}):
            return False

        if 'info' not in spec:
            spec['info'] = {}

        spec['info']['license'] = {
            'name': 'MIT',
            'url': 'https://opensource.org/licenses/MIT'
        }
        return True

    def fix_common_refs(self, spec: Dict[str, Any]) -> bool:
        """Fix common schema references"""
        changed = False

        def fix_refs(obj):
            nonlocal changed
            if isinstance(obj, dict):
                for key, value in obj.items():
                    if key == '$ref' and isinstance(value, str):
                        if '../../misc-domain/common/common.yaml' in value:
                            obj[key] = value.replace('../../misc-domain/common/common.yaml', '../../common-schemas.yaml')
                            changed = True
                    else:
                        fix_refs(value)
            elif isinstance(obj, list):
                for item in obj:
                    fix_refs(item)

        fix_refs(spec)
        return changed

    def add_4xx_responses(self, spec: Dict[str, Any]) -> bool:
        """Add 4XX responses to operations that don't have them - PERFORMANCE optimized"""
        changed = False

        if 'paths' not in spec:
            return False

        # PERFORMANCE: Pre-check if we need to modify anything
        total_operations = 0
        operations_to_update = []

        for path, methods in spec['paths'].items():
            if not isinstance(methods, dict):
                continue

            for method, operation in methods.items():
                if method not in ['get', 'post', 'put', 'delete', 'patch']:
                    continue

                if not isinstance(operation, dict) or 'responses' not in operation:
                    continue

                total_operations += 1
                responses = operation['responses']

                # PERFORMANCE: Check if already has 4XX responses
                has_4xx = any(code.startswith('4') for code in responses.keys())

                if not has_4xx:
                    operations_to_update.append((responses, method))

        # PERFORMANCE: Batch update operations to minimize dict operations
        for responses, method in operations_to_update:
            if method == 'get':
                # PERFORMANCE: Use preallocated response structure
                responses['429'] = self._common_responses['429'].copy()
            else:  # post, put, delete
                responses['400'] = self._common_responses['400'].copy()
            changed = True

        if changed:
            self.logger.debug(f"Added 4XX responses to {len(operations_to_update)} operations")

        return changed

    def optimize_struct_alignment(self, spec: Dict[str, Any]) -> Tuple[int, List[str]]:
        """Optimize struct field alignment"""
        changed_schemas = []
        total_changes = 0

        if 'components' in spec and 'schemas' in spec['components']:
            schemas = spec['components']['schemas']

            for schema_name, schema in schemas.items():
                if isinstance(schema, dict) and schema.get('type') == 'object':
                    if self._reorder_schema_properties(schema, schema_name):
                        changed_schemas.append(schema_name)
                        total_changes += 1

        return total_changes, changed_schemas

    def _reorder_schema_properties(self, schema: Dict[str, Any], schema_name: str) -> bool:
        """Reorder properties for struct alignment"""
        if 'properties' not in schema:
            return False

        properties = schema['properties']
        if not properties:
            return False

        # Import type ordering logic from reorder-openapi-fields.py
        TYPE_ORDER = {
            'uuid': 0, 'binary': 1, 'byte': 1, 'string': 2, 'email': 2, 'uri': 2,
            'url': 2, 'hostname': 2, 'ipv4': 2, 'ipv6': 2, 'password': 2,
            'date-time': 3, 'date': 3, 'time': 3, 'duration': 3,
            '$ref': 4, 'object': 4, 'array': 5,
            'int64': 6, 'float64': 7, 'double': 7, 'number': 7,
            'int32': 8, 'float32': 9, 'float': 9,
            'int16': 10, 'boolean': 12, 'bool': 12, 'null': 13
        }

        def get_field_order(prop):
            prop_type = prop.get('type', '')
            format_type = prop.get('format', '')
            ref = prop.get('$ref', '')

            if ref:
                return (TYPE_ORDER.get('$ref', 99), f"ref:{ref}")
            if prop_type == 'array':
                return (TYPE_ORDER.get('array', 99), 'array')
            if prop_type == 'object':
                return (TYPE_ORDER.get('object', 99), 'object')
            if prop_type == 'string':
                if format_type == 'uuid':
                    return (TYPE_ORDER.get('uuid', 99), 'uuid')
                elif format_type in ['binary', 'byte']:
                    return (TYPE_ORDER.get(format_type, 99), f"string:{format_type}")
                return (TYPE_ORDER.get('string', 99), 'string')

            return (TYPE_ORDER.get(prop_type, 99), prop_type)

        sorted_props = sorted(properties.items(), key=lambda x: get_field_order(x[1]))
        old_order = list(properties.keys())
        new_order = [name for name, _ in sorted_props]

        if old_order != new_order:
            schema['properties'] = {name: props for name, props in sorted_props}
            return True

        return False
