#!/usr/bin/env python3
"""
Issue: #1584
Script to automatically assign pprof ports to all Go services from centralized config.
Scans all main.go files, detects conflicts, and updates ports based on infrastructure/pprof-ports.yaml
"""

import os
import re
import sys
import yaml
from collections import defaultdict
from pathlib import Path
from typing import Dict, List, Tuple, Optional

# Colors for output
GREEN = '\033[92m'
YELLOW = '\033[93m'
RED = '\033[91m'
RESET = '\033[0m'


def load_port_config(config_path: str) -> Dict[str, int]:
    """Load port mapping from YAML config."""
    with open(config_path, 'r', encoding='utf-8') as f:
        config = yaml.safe_load(f)
    return config.get('services', {})


def find_all_main_go_files(services_dir: str) -> List[Path]:
    """Find all main.go files in services directory."""
    services_path = Path(services_dir)
    main_files = list(services_path.rglob('main.go'))
    # Filter out cmd/ subdirectories (like realtime-gateway-go/cmd/)
    main_files = [f for f in main_files if 'cmd/' not in str(f)]
    return main_files


def extract_service_name(file_path: Path) -> str:
    """Extract service name from file path."""
    # services/foo-service-go/main.go -> foo-service-go
    parts = file_path.parts
    if 'services' in parts:
        idx = parts.index('services')
        if idx + 1 < len(parts):
            return parts[idx + 1]
    return file_path.parent.name


def find_pprof_port_in_file(file_path: Path) -> Optional[Tuple[int, int]]:
    """
    Find pprof port in main.go file.
    Returns (line_number, port) or None if not found.
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        for i, line in enumerate(lines, 1):
            # Match: getEnv("PPROF_ADDR", "localhost:XXXX")
            match = re.search(r'getEnv\(["\']PPROF_ADDR["\'],\s*["\']localhost:(\d+)["\']\)', line)
            if match:
                port = int(match.group(1))
                return (i, port)
    except Exception as e:
        print(f"{RED}Error reading {file_path}: {e}{RESET}")

    return None


def update_pprof_port(file_path: Path, new_port: int, dry_run: bool = False) -> bool:
    """Update pprof port in main.go file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Replace port in getEnv("PPROF_ADDR", "localhost:XXXX")
        pattern = r'(getEnv\(["\']PPROF_ADDR["\'],\s*["\']localhost:)(\d+)(["\']\))'
        replacement = rf'\g<1>{new_port}\g<3>'
        new_content = re.sub(pattern, replacement, content)

        if new_content != content:
            if not dry_run:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)
            return True
    except Exception as e:
        print(f"{RED}Error updating {file_path}: {e}{RESET}")

    return False


def add_pprof_to_file(file_path: Path, port: int, dry_run: bool = False) -> bool:
    """Add pprof server initialization to main.go if missing."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Check if pprof is already imported
        if 'net/http/pprof' not in content:
            # Add import
            import_pattern = r'(import\s*\([^)]*"net/http"[^)]*)'
            if re.search(import_pattern, content):
                # Add pprof import after net/http
                content = re.sub(
                    r'("net/http")',
                    r'\1\n\t_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints',
                    content
                )
            else:
                # Add to existing import block
                content = re.sub(
                    r'(import\s*\()',
                    r'\1\n\t"net/http"\n\t_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints',
                    content
                )

        # Check if pprof server is already started
        if 'PPROF_ADDR' not in content:
            # Find main() function and add pprof server after it starts
            # Look for pattern: func main() { ... httpServer := ...
            # Add pprof server before httpServer.Start()

            pprof_code = f'''
\t// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
\tgo func() {{
\t\tpprofAddr := getEnv("PPROF_ADDR", "localhost:{port}")
\t\tlog.Printf("pprof server starting on %s", pprofAddr)
\t\t// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
\t\tif err := http.ListenAndServe(pprofAddr, nil); err != nil {{
\t\t\tlog.Printf("pprof server error: %v", err)
\t\t}}
\t}}()
'''

            # Try to insert before httpServer.Start() or similar
            if 'httpServer :=' in content or 'httpServer, err :=' in content:
                # Insert after httpServer creation
                content = re.sub(
                    r'(httpServer\s*:?=\s*[^\n]+\n)',
                    r'\1' + pprof_code,
                    content,
                    count=1
                )
            else:
                # Insert at the beginning of main() after first log
                content = re.sub(
                    r'(func main\(\) \{[^}]*?log\.[^\n]+\n)',
                    r'\1' + pprof_code,
                    content,
                    count=1
                )

        if not dry_run:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
        return True
    except Exception as e:
        print(f"{RED}Error adding pprof to {file_path}: {e}{RESET}")
        return False


def main():
    """Main function."""
    dry_run = '--dry-run' in sys.argv
    services_dir = os.getenv('SERVICES_DIR', 'services')
    config_path = os.getenv('PPROF_CONFIG', 'infrastructure/pprof-ports.yaml')

    if not os.path.exists(config_path):
        print(f"{RED}Config file not found: {config_path}{RESET}")
        sys.exit(1)

    print(f"{GREEN}Loading port config from {config_path}...{RESET}")
    port_config = load_port_config(config_path)

    print(f"{GREEN}Scanning {services_dir} for main.go files...{RESET}")
    main_files = find_all_main_go_files(services_dir)
    print(f"Found {len(main_files)} main.go files")

    # Track port usage
    port_usage: Dict[int, List[Tuple[str, Path]]] = defaultdict(list)
    services_without_pprof: List[Tuple[str, Path]] = []
    services_with_wrong_port: List[Tuple[str, Path, int, int]] = []  # (service, path, current, expected)

    # Scan all files
    for main_file in main_files:
        service_name = extract_service_name(main_file)
        pprof_info = find_pprof_port_in_file(main_file)

        if pprof_info:
            line_num, port = pprof_info
            port_usage[port].append((service_name, main_file))

            # Check if port matches config
            expected_port = port_config.get(service_name)
            if expected_port and port != expected_port:
                services_with_wrong_port.append((service_name, main_file, port, expected_port))
        else:
            services_without_pprof.append((service_name, main_file))

    # Report conflicts
    conflicts = {port: services for port, services in port_usage.items() if len(services) > 1}

    print(f"\n{YELLOW}=== Port Conflicts ==={RESET}")
    if conflicts:
        for port, services in conflicts.items():
            print(f"{RED}Port {port} used by:{RESET}")
            for service, path in services:
                print(f"  - {service} ({path})")
    else:
        print(f"{GREEN}No port conflicts found!{RESET}")

    # Report services with wrong ports
    print(f"\n{YELLOW}=== Services with Wrong Ports ==={RESET}")
    if services_with_wrong_port:
        for service, path, current, expected in services_with_wrong_port:
            print(f"{YELLOW}{service}:{RESET} current={current}, expected={expected}")
            if not dry_run:
                update_pprof_port(path, expected, dry_run=False)
                print(f"  {GREEN}OK Updated to port {expected}{RESET}")
    else:
        print(f"{GREEN}All ports match config!{RESET}")

    # Report services without pprof
    print(f"\n{YELLOW}=== Services without pprof ==={RESET}")
    if services_without_pprof:
        for service, path in services_without_pprof:
            expected_port = port_config.get(service)
            if expected_port:
                print(f"{YELLOW}{service}:{RESET} missing pprof (expected port {expected_port})")
                if not dry_run:
                    if add_pprof_to_file(path, expected_port, dry_run=False):
                        print(f"  {GREEN}OK Added pprof with port {expected_port}{RESET}")
            else:
                print(f"{RED}{service}:{RESET} missing pprof AND not in config!")
    else:
        print(f"{GREEN}All services have pprof!{RESET}")

    # Summary
    print(f"\n{GREEN}=== Summary ==={RESET}")
    print(f"Total services: {len(main_files)}")
    print(f"Services with pprof: {len(main_files) - len(services_without_pprof)}")
    print(f"Services without pprof: {len(services_without_pprof)}")
    print(f"Port conflicts: {len(conflicts)}")
    print(f"Services with wrong ports: {len(services_with_wrong_port)}")

    if dry_run:
        print(f"\n{YELLOW}DRY RUN - No files were modified{RESET}")
    else:
        print(f"\n{GREEN}OK All updates completed!{RESET}")


if __name__ == '__main__':
    main()
