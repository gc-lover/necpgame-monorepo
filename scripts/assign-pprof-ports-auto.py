#!/usr/bin/env python3
"""
Issue: #1584
Automatic pprof port assignment using deterministic hash function.
No need for manual port mapping - ports are generated from service names.
"""

import hashlib
import os
import re
import sys
from collections import defaultdict
from pathlib import Path
from typing import Dict, List, Tuple, Optional

# Port range for pprof: 6060-6999 (940 ports available)
PPROF_PORT_MIN = 6060
PPROF_PORT_MAX = 6999
PPROF_PORT_RANGE = PPROF_PORT_MAX - PPROF_PORT_MIN + 1

# Colors for output
GREEN = '\033[92m'
YELLOW = '\033[93m'
RED = '\033[91m'
RESET = '\033[0m'


def generate_port(service_name: str, collision_offset: int = 0) -> int:
    """
    Generate deterministic port number from service name using SHA256 hash.
    Ensures uniform distribution across port range.
    
    Args:
        service_name: Name of the service
        collision_offset: Offset to resolve hash collisions (0 = first attempt)
    """
    # Create SHA256 hash with offset for collision resolution
    data = f"{service_name}:{collision_offset}".encode('utf-8')
    hash_obj = hashlib.sha256(data)
    hash_bytes = hash_obj.digest()

    # Use first 4 bytes as uint32
    hash_value = int.from_bytes(hash_bytes[:4], byteorder='big')

    # Map to port range
    port = PPROF_PORT_MIN + (hash_value % PPROF_PORT_RANGE)

    return port


def generate_port_mapping(service_names: List[str]) -> Dict[str, int]:
    """
    Generate port mapping for all services, automatically resolving collisions.
    """
    port_mapping: Dict[str, int] = {}
    used_ports: Dict[int, str] = {}

    for service_name in sorted(service_names):  # Sort for deterministic order
        offset = 0
        while True:
            port = generate_port(service_name, offset)

            # Check for collision
            if port not in used_ports:
                port_mapping[service_name] = port
                used_ports[port] = service_name
                break

            # Collision detected - try with offset
            offset += 1
            if offset > 100:  # Safety limit
                raise RuntimeError(f"Could not find free port for {service_name} after 100 attempts")

    return port_mapping


def find_all_main_go_files(services_dir: str) -> List[Path]:
    """Find all main.go files in services directory."""
    services_path = Path(services_dir)
    main_files = list(services_path.rglob('main.go'))
    # Filter out cmd/ subdirectories
    main_files = [f for f in main_files if 'cmd/' not in str(f)]
    return main_files


def extract_service_name(file_path: Path) -> str:
    """Extract service name from file path."""
    parts = file_path.parts
    if 'services' in parts:
        idx = parts.index('services')
        if idx + 1 < len(parts):
            return parts[idx + 1]
    return file_path.parent.name


def find_pprof_port_in_file(file_path: Path) -> Optional[Tuple[int, int]]:
    """Find pprof port in main.go file. Returns (line_number, port) or None."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        for i, line in enumerate(lines, 1):
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
            import_pattern = r'(import\s*\([^)]*"net/http"[^)]*)'
            if re.search(import_pattern, content):
                content = re.sub(
                    r'("net/http")',
                    r'\1\n\t_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints',
                    content
                )
            else:
                content = re.sub(
                    r'(import\s*\()',
                    r'\1\n\t"net/http"\n\t_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints',
                    content
                )

        # Check if pprof server is already started
        if 'PPROF_ADDR' not in content:
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

            if 'httpServer :=' in content or 'httpServer, err :=' in content:
                content = re.sub(
                    r'(httpServer\s*:?=\s*[^\n]+\n)',
                    r'\1' + pprof_code,
                    content,
                    count=1
                )
            else:
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
    generate_config = '--generate-config' in sys.argv

    print(f"{GREEN}Scanning {services_dir} for main.go files...{RESET}")
    main_files = find_all_main_go_files(services_dir)
    print(f"Found {len(main_files)} main.go files\n")

    # Generate port mapping (automatically resolves collisions)
    print(f"{GREEN}Generating deterministic port mapping...{RESET}")
    service_names = [extract_service_name(f) for f in main_files]
    port_mapping = generate_port_mapping(service_names)

    # Verify no collisions
    port_to_services: Dict[int, List[str]] = defaultdict(list)
    for service, port in port_mapping.items():
        port_to_services[port].append(service)

    collisions = {port: services for port, services in port_to_services.items() if len(services) > 1}

    if collisions:
        print(f"{RED}❌ Collisions still exist (should not happen):{RESET}")
        for port, services in collisions.items():
            print(f"  Port {port}: {', '.join(services)}")
        print()
    else:
        print(f"{GREEN}OK All ports are unique!{RESET}\n")

    # Generate config file if requested
    if generate_config:
        config_path = 'infrastructure/pprof-ports-auto.yaml'
        print(f"{GREEN}Generating config file: {config_path}{RESET}")
        with open(config_path, 'w', encoding='utf-8') as f:
            f.write("# Issue: #1584\n")
            f.write("# Auto-generated pprof port mapping using deterministic hash\n")
            f.write("# Port range: 6060-6999 (940 ports)\n")
            f.write("# Generated by: scripts/assign-pprof-ports-auto.py\n\n")
            f.write("services:\n")

            # Sort by service name for readability
            for service in sorted(port_mapping.keys()):
                port = port_mapping[service]
                f.write(f"  {service}: {port}\n")

        print(f"{GREEN}OK Config file generated!{RESET}\n")

    # Track port usage and conflicts
    port_usage: Dict[int, List[Tuple[str, Path]]] = defaultdict(list)
    services_without_pprof: List[Tuple[str, Path]] = []
    services_with_wrong_port: List[Tuple[str, Path, int, int]] = []

    # Scan all files
    for main_file in main_files:
        service_name = extract_service_name(main_file)
        expected_port = port_mapping[service_name]
        pprof_info = find_pprof_port_in_file(main_file)

        if pprof_info:
            line_num, current_port = pprof_info
            port_usage[current_port].append((service_name, main_file))

            if current_port != expected_port:
                services_with_wrong_port.append((service_name, main_file, current_port, expected_port))
        else:
            services_without_pprof.append((service_name, main_file))

    # Report conflicts
    conflicts = {port: services for port, services in port_usage.items() if len(services) > 1}

    print(f"{YELLOW}=== Port Conflicts ==={RESET}")
    if conflicts:
        for port, services in conflicts.items():
            print(f"{RED}Port {port} used by:{RESET}")
            for service, path in services:
                expected = port_mapping[service]
                print(f"  - {service} ({path})")
                if port != expected:
                    print(f"    {YELLOW}Expected: {expected}{RESET}")
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
        print(f"{GREEN}All ports match expected values!{RESET}")

    # Report services without pprof
    print(f"\n{YELLOW}=== Services without pprof ==={RESET}")
    if services_without_pprof:
        for service, path in services_without_pprof:
            expected_port = port_mapping[service]
            print(f"{YELLOW}{service}:{RESET} missing pprof (expected port {expected_port})")
            if not dry_run:
                if add_pprof_to_file(path, expected_port, dry_run=False):
                    print(f"  {GREEN}OK Added pprof with port {expected_port}{RESET}")
    else:
        print(f"{GREEN}All services have pprof!{RESET}")

    # Summary
    print(f"\n{GREEN}=== Summary ==={RESET}")
    print(f"Total services: {len(main_files)}")
    print(f"Services with pprof: {len(main_files) - len(services_without_pprof)}")
    print(f"Services without pprof: {len(services_without_pprof)}")
    print(f"Port conflicts: {len(conflicts)}")
    print(f"Services with wrong ports: {len(services_with_wrong_port)}")
    print(f"Hash collisions: {len(collisions)}")

    if dry_run:
        print(f"\n{YELLOW}DRY RUN - No files were modified{RESET}")
    else:
        print(f"\n{GREEN}OK All updates completed!{RESET}")

    print(f"\n{GREEN}Usage:{RESET}")
    print(f"  python {sys.argv[0]} --dry-run          # Check conflicts")
    print(f"  python {sys.argv[0]}                    # Apply fixes")
    print(f"  python {sys.argv[0]} --generate-config  # Generate config file")


if __name__ == '__main__':
    main()
