#!/usr/bin/env python3
"""
Bundle OpenAPI specifications by resolving external $ref
"""

import yaml
import os
from pathlib import Path
from copy import deepcopy

def resolve_refs(data, base_path, visited=None):
    """Recursively resolve $ref in YAML data"""
    if visited is None:
        visited = set()

    if isinstance(data, dict):
        if '$ref' in data:
            ref = data['$ref']
            if ref and isinstance(ref, str) and (ref.startswith('../') or ref.startswith('./')):
                # Resolve external reference
                ref_path = (base_path / ref.split('#/')[0]).resolve()
                ref_key = ref.split('#/')[1] if '#/' in ref else None

                # Avoid infinite loops
                ref_id = str(ref_path) + ('#' + ref_key if ref_key else '')
                if ref_id in visited:
                    return data  # Return unresolved ref to avoid cycles
                visited.add(ref_id)

                if ref_path.exists():
                    with open(ref_path, 'r', encoding='utf-8') as f:
                        ref_data = yaml.safe_load(f)

                    if ref_key:
                        # Navigate to the specific component
                        component_path = ref_key.split('/')
                        current = ref_data
                        try:
                            for part in component_path:
                                if part in current:
                                    current = current[part]
                                else:
                                    # Try components/schemas/ prefix if not found
                                    if 'components' in current and 'schemas' in current['components']:
                                        if part in current['components']['schemas']:
                                            current = current['components']['schemas'][part]
                                        else:
                                            raise KeyError(f"Component {part} not found")
                                    else:
                                        raise KeyError(f"Component {part} not found")
                            resolved = resolve_refs(current, ref_path.parent, visited)
                            # Convert resolved component to inline definition to avoid ref issues
                            return resolved
                        except KeyError:
                            print(f'Component {ref_key} not found in {ref_path}')
                            return data
                    else:
                        return resolve_refs(ref_data, ref_path.parent, visited)
                else:
                    print(f'Reference not found: {ref_path}')
                    return data
            else:
                # Fix internal refs to use proper OpenAPI format
                if ref and ref.startswith('#/') and not ref.startswith('#/components/'):
                    # Convert root-level refs to components/schemas/ refs
                    if len(ref.split('/')) == 2:  # Like "#/BaseEntity"
                        component_name = ref.split('/')[-1]
                        data['$ref'] = f'#/components/schemas/{component_name}'
                return data

        # Recursively process all values
        result = {}
        for k, v in data.items():
            result[k] = resolve_refs(v, base_path, visited.copy())
        return result
    elif isinstance(data, list):
        return [resolve_refs(item, base_path, visited) for item in data]
    else:
        return data

def collect_all_dependencies(data, base_path, visited=None, collected=None):
    """Recursively collect all schema dependencies"""
    if visited is None:
        visited = set()
    if collected is None:
        collected = {}

    if isinstance(data, dict):
        if '$ref' in data:
            ref = data['$ref']
            if ref and isinstance(ref, str) and (ref.startswith('../') or ref.startswith('./')):
                # Resolve external reference
                ref_path = (base_path / ref.split('#/')[0]).resolve()
                ref_key = ref.split('#/')[1] if '#/' in ref else None

                # Avoid infinite loops
                ref_id = str(ref_path) + ('#' + ref_key if ref_key else '')
                if ref_id in visited:
                    return
                visited.add(ref_id)

                if ref_path.exists():
                    with open(ref_path, 'r', encoding='utf-8') as f:
                        ref_data = yaml.safe_load(f)

                    if ref_key:
                        # Navigate to the specific component
                        component_path = ref_key.split('/')
                        current = ref_data
                        try:
                            for part in component_path:
                                if part in current:
                                    current = current[part]
                                else:
                                    # Try components/schemas/ prefix if not found
                                    if 'components' in current and 'schemas' in current['components']:
                                        if part in current['components']['schemas']:
                                            current = current['components']['schemas'][part]
                                        else:
                                            raise KeyError(f"Component {part} not found")
                                    else:
                                        raise KeyError(f"Component {part} not found")

                            # Add the resolved component to collected components
                            if component_path[0] == 'components' and len(component_path) >= 3:
                                component_type = component_path[1]  # schemas, responses, etc.
                                component_name = component_path[2]

                                if component_type not in collected:
                                    collected[component_type] = {}
                                if component_name not in collected[component_type]:
                                    collected[component_type][component_name] = current

                            # Also collect all schemas from the referenced file
                            if 'components' in ref_data and 'schemas' in ref_data['components']:
                                if 'schemas' not in collected:
                                    collected['schemas'] = {}
                                for schema_name, schema_def in ref_data['components']['schemas'].items():
                                    if schema_name not in collected['schemas']:
                                        # Resolve internal refs in the schema before adding
                                        resolved_schema = resolve_refs(schema_def, ref_path.parent)
                                        collected['schemas'][schema_name] = resolved_schema

                            # Recursively collect dependencies of this component
                            collect_all_dependencies(current, ref_path.parent, visited, collected)

                        except KeyError:
                            print(f'Component {ref_key} not found in {ref_path}')
                    else:
                        # Collect all schemas from the entire file
                        if 'components' in ref_data and 'schemas' in ref_data['components']:
                            for schema_name, schema_def in ref_data['components']['schemas'].items():
                                if schema_name not in collected:
                                    collected[schema_name] = schema_def
                        # Recursively collect dependencies
                        collect_all_dependencies(ref_data, ref_path.parent, visited, collected)

        # Recursively process all values
        for k, v in data.items():
            collect_all_dependencies(v, base_path, visited, collected)

    elif isinstance(data, list):
        for item in data:
            collect_all_dependencies(item, base_path, visited, collected)

def bundle_openapi_spec(spec_path, output_path=None):
    """Bundle OpenAPI spec by resolving all external $ref"""
    if output_path is None:
        output_path = spec_path.parent / "bundled.yaml"

    with open(spec_path, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)

    # First pass: collect all dependencies
    collected_components = {}
    collect_all_dependencies(data, spec_path.parent, collected=collected_components)

    # Add collected components to the main spec
    if 'components' not in data:
        data['components'] = {}

    # Merge all component types
    for comp_type, components in collected_components.items():
        if comp_type not in data['components']:
            data['components'][comp_type] = {}
        for comp_name, comp_def in components.items():
            if comp_name not in data['components'][comp_type]:
                data['components'][comp_type][comp_name] = comp_def

    # Second pass: resolve refs
    bundled = resolve_refs(data, spec_path.parent)
    with open(output_path, 'w', encoding='utf-8') as f:
        yaml.dump(bundled, f, default_flow_style=False, allow_unicode=True, sort_keys=False)

    return output_path

if __name__ == "__main__":
    import sys
    if len(sys.argv) < 2:
        print("Usage: python bundle_openapi.py <spec_path> [output_path]")
        sys.exit(1)

    spec_path = Path(sys.argv[1])
    output_path = Path(sys.argv[2]) if len(sys.argv) > 2 else None

    bundled_path = bundle_openapi_spec(spec_path, output_path)
    print(f"Bundled spec created: {bundled_path}")
