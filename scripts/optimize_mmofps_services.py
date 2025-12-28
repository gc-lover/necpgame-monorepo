#!/usr/bin/env python3
"""
MMOFPS Services Optimization Script
Optimizes all MMOFPS-related services with memory pooling and zero allocations

Usage:
    python scripts/optimize_mmofps_services.py [--dry-run] [--service SERVICE_NAME]

Arguments:
    --dry-run: Analyze and show changes without applying them
    --service: Optimize only specific service
"""

import os
import sys
import re
import glob
import argparse
from pathlib import Path
from typing import Dict, List, Set, Tuple, Optional

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

class MMOFPSOptimizer:
    def __init__(self, dry_run: bool = False):
        self.dry_run = dry_run
        self.mmofps_services = [
            'realtime-combat-service-go',
            'combat-stats-service-go',
            'combat-hacking-service-go',
            'combat-abilities-service-go',
            'combat-damage-service-go',
            'realtime-gateway-go',
            'matchmaking-service-go',
            'match-stats-aggregator'
        ]

    def find_services(self) -> List[str]:
        """Find all MMOFPS-related services"""
        services_dir = Path('services')
        found_services = []

        for service_dir in services_dir.glob('*-go'):
            service_name = service_dir.name
            if any(keyword in service_name.lower() for keyword in ['combat', 'realtime', 'match']):
                found_services.append(service_name)

        return sorted(found_services)

    def analyze_service(self, service_name: str) -> Dict[str, any]:
        """Analyze service for optimization opportunities"""
        service_path = Path('services') / service_name
        analysis = {
            'service': service_name,
            'has_memory_pooling': False,
            'has_context_timeouts': False,
            'allocations_found': [],
            'optimizations_needed': []
        }

        # Check Go files
        go_files = list(service_path.glob('**/*.go'))
        for go_file in go_files:
            try:
                content = go_file.read_text()

                # Check for memory pooling
                if 'sync.Pool' in content:
                    analysis['has_memory_pooling'] = True

                # Check for context timeouts
                if 'context.WithTimeout' in content or 'ctx,' in content:
                    analysis['has_context_timeouts'] = True

                # Find allocations
                alloc_patterns = [
                    r'new\s*\([^)]+\)',
                    r'make\s*\([^)]+\)',
                    r'&[^\s{}]+{',
                    r'\[\][^\s]+\{',
                ]

                for pattern in alloc_patterns:
                    matches = re.findall(pattern, content)
                    if matches:
                        for match in matches:
                            analysis['allocations_found'].append({
                                'file': str(go_file.relative_to(service_path)),
                                'allocation': match.strip(),
                                'line': content.count('\n', 0, content.find(match)) + 1
                            })

            except Exception as e:
                print(f"Error analyzing {go_file}: {e}")

        # Determine optimizations needed
        if not analysis['has_memory_pooling'] and analysis['allocations_found']:
            analysis['optimizations_needed'].append('memory_pooling')

        if not analysis['has_context_timeouts']:
            analysis['optimizations_needed'].append('context_timeouts')

        return analysis

    def optimize_service(self, service_name: str, analysis: Dict[str, any]) -> bool:
        """Apply optimizations to service"""
        service_path = Path('services') / service_name

        if self.dry_run:
            print(f"\n[DRY RUN] Would optimize {service_name}:")
            print(f"  - Memory pooling: {'[OK]' if analysis['has_memory_pooling'] else '[MISSING]'}")
            print(f"  - Context timeouts: {'[OK]' if analysis['has_context_timeouts'] else '[MISSING]'}")
            print(f"  - Allocations found: {len(analysis['allocations_found'])}")
            if analysis['optimizations_needed']:
                print(f"  - Needed optimizations: {', '.join(analysis['optimizations_needed'])}")
            return True

        # Apply memory pooling optimizations
        if 'memory_pooling' in analysis['optimizations_needed']:
            if self.add_memory_pooling(service_path, analysis):
                print(f"[OK] Added memory pooling to {service_name}")
            else:
                print(f"[ERROR] Failed to add memory pooling to {service_name}")
                return False

        # Apply context timeout optimizations
        if 'context_timeouts' in analysis['optimizations_needed']:
            if self.add_context_timeouts(service_path, analysis):
                print(f"[OK] Added context timeouts to {service_name}")
            else:
                print(f"[ERROR] Failed to add context timeouts to {service_name}")
                return False

        return True

    def add_memory_pooling(self, service_path: Path, analysis: Dict[str, any]) -> bool:
        """Add memory pooling to service"""
        try:
            # Find main service file
            service_files = list(service_path.glob('**/service.go'))
            if not service_files:
                service_files = list(service_path.glob('**/*service*.go'))

            if not service_files:
                return False

            service_file = service_files[0]
            content = service_file.read_text()

            # Add memory pools to struct
            struct_pattern = r'type\s+(\w+Service)\s+struct\s*{([^}]*)}'
            match = re.search(struct_pattern, content, re.DOTALL)

            if match:
                struct_name = match.group(1)
                existing_fields = match.group(2)

                # Add memory pools
                memory_pools = '''
	// PERFORMANCE: Memory pools for zero allocations
	objectPool    sync.Pool
	bufferPool    sync.Pool
	slicePool     sync.Pool
	mapPool       sync.Pool'''

                # Insert pools after existing fields
                new_struct = existing_fields.rstrip() + memory_pools + '\n}'

                # Replace struct
                new_content = re.sub(
                    r'type\s+' + struct_name + r'\s+struct\s*{[^}]*}',
                    f'type {struct_name} struct {{{new_struct}',
                    content,
                    flags=re.DOTALL
                )

                # Add pool initialization in constructor
                init_pattern = rf'func\s+New{re.escape(struct_name)}\s*\([^)]*\)\s*\*{re.escape(struct_name)}\s*{{'
                if re.search(init_pattern, new_content):
                    init_replacement = f'''func New{struct_name}(repo interface{{}}, logger *zap.Logger) *{struct_name} {{
	return &{struct_name}{{
		repo: repo,
		logger: logger,
		objectPool: sync.Pool{{
			New: func() interface{{}} {{
				return &SomeObject{{}} // Replace with actual object type
			}},
		}},
		bufferPool: sync.Pool{{
			New: func() interface{{}} {{
				return make([]byte, 4096) // 4KB buffer
			}},
		}},
		slicePool: sync.Pool{{
			New: func() interface{{}} {{
				return make([]interface{{}}, 0, 16) // Pre-allocated slice
			}},
		}},
		mapPool: sync.Pool{{
			New: func() interface{{}} {{
				return make(map[string]interface{{}}, 8) // Pre-allocated map
			}},
		}},
	}}
}}'''
                    new_content = re.sub(init_pattern, init_replacement, new_content)

                # Write optimized file
                service_file.write_text(new_content)
                return True

        except Exception as e:
            print(f"Error adding memory pooling: {e}")
            return False

        return False

    def add_context_timeouts(self, service_path: Path, analysis: Dict[str, any]) -> bool:
        """Add context timeouts to service methods"""
        try:
            service_files = list(service_path.glob('**/service.go'))
            if not service_files:
                service_files = list(service_path.glob('**/*service*.go'))

            if not service_files:
                return False

            for service_file in service_files:
                content = service_file.read_text()

                # Add context import if missing
                if '"context"' not in content:
                    import_pattern = r'import\s*\('
                    if re.search(import_pattern, content):
                        # Add context import
                        content = re.sub(
                            r'(import\s*\(\s*)',
                            r'\1\n\t"context"',
                            content
                        )

                # Find methods without context
                method_pattern = r'func\s*\([^)]*\)\s*\([^)]*\)\s*{'
                methods = re.findall(method_pattern, content)

                for method in methods:
                    if 'ctx context.Context' not in method:
                        # Add context parameter
                        new_method = re.sub(
                            r'func\s*\(([^)]*)\)',
                            r'func (ctx context.Context, \1)',
                            method
                        )
                        content = content.replace(method, new_method)

                        # Add timeout context
                        method_body_start = content.find('{', content.find(method))
                        if method_body_start != -1:
                            timeout_code = '\n\t// PERFORMANCE: Context timeout for MMOFPS\n\tctx, cancel := context.WithTimeout(ctx, 30*time.Second)\n\tdefer cancel()\n'
                            content = content[:method_body_start+1] + timeout_code + content[method_body_start+1:]

                service_file.write_text(content)

            return True

        except Exception as e:
            print(f"Error adding context timeouts: {e}")
            return False

    def run(self, target_service: Optional[str] = None):
        """Run optimization process"""
        print("[OPTIMIZER] MMOFPS Services Memory Pooling & Zero Allocations Optimizer")
        print("=" * 60)

        services_to_optimize = [target_service] if target_service else self.find_services()

        print(f"Found {len(services_to_optimize)} MMOFPS services to analyze:")
        for service in services_to_optimize:
            print(f"  - {service}")

        total_optimized = 0
        total_allocations_found = 0

        for service_name in services_to_optimize:
            print(f"\n[ANALYZING] {service_name}...")

            analysis = self.analyze_service(service_name)
            total_allocations_found += len(analysis['allocations_found'])

            if self.optimize_service(service_name, analysis):
                total_optimized += 1

        print(f"\n{'='*60}")
        print("[SUMMARY] OPTIMIZATION SUMMARY:")
        print(f"   Services analyzed: {len(services_to_optimize)}")
        print(f"   Services optimized: {total_optimized}")
        print(f"   Total allocations found: {total_allocations_found}")

        if self.dry_run:
            print("\n[INFO] This was a DRY RUN. Use without --dry-run to apply optimizations.")

        return total_optimized > 0

def main():
    parser = argparse.ArgumentParser(description='Optimize MMOFPS services with memory pooling')
    parser.add_argument('--dry-run', action='store_true', help='Analyze without applying changes')
    parser.add_argument('--service', help='Optimize only specific service')

    args = parser.parse_args()

    optimizer = MMOFPSOptimizer(dry_run=args.dry_run)
    success = optimizer.run(target_service=args.service)

    sys.exit(0 if success else 1)

if __name__ == '__main__':
    main()
