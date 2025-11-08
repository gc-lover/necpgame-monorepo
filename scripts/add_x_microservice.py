import re
from pathlib import Path
from typing import Dict, List, Optional, Sequence, Tuple

ROOT_DIR = Path(__file__).resolve().parent.parent
API_ROOT = ROOT_DIR / "API-SWAGGER" / "api" / "v1"

ServiceDefaults = Dict[str, Dict[str, str]]

SERVICE_DEFAULTS: ServiceDefaults = {
    "auth-service": {"port": "8081", "domain": "auth", "base_path": "/api/v1/auth", "package": "com.necpgame.authservice"},
    "character-service": {"port": "8082", "domain": "characters", "base_path": "/api/v1/characters", "package": "com.necpgame.characterservice"},
    "session-service": {"port": "8082", "domain": "session", "base_path": "/api/v1/session", "package": "com.necpgame.sessionservice"},
    "gameplay-service": {"port": "8083", "domain": "gameplay", "base_path": "/api/v1/gameplay", "package": "com.necpgame.gameplayservice"},
    "social-service": {"port": "8084", "domain": "social", "base_path": "/api/v1/social", "package": "com.necpgame.socialservice"},
    "economy-service": {"port": "8085", "domain": "economy", "base_path": "/api/v1/economy", "package": "com.necpgame.economyservice"},
    "world-service": {"port": "8086", "domain": "world", "base_path": "/api/v1/world", "package": "com.necpgame.worldservice"},
    "narrative-service": {"port": "8087", "domain": "narrative", "base_path": "/api/v1/narrative", "package": "com.necpgame.narrativeservice"},
    "admin-service": {"port": "8088", "domain": "admin", "base_path": "/api/v1/admin", "package": "com.necpgame.adminservice"},
    "combat-service": {"port": "8088", "domain": "combat", "base_path": "/api/v1/combat", "package": "com.necpgame.combatservice"},
    "realtime-service": {"port": "8089", "domain": "realtime", "base_path": "/api/v1/realtime", "package": "com.necpgame.realtimeservice"},
    "analytics-service": {"port": "8090", "domain": "analytics", "base_path": "/api/v1/analytics", "package": "com.necpgame.analyticsservice"},
    "notification-service": {"port": "8092", "domain": "notifications", "base_path": "/api/v1/notifications", "package": "com.necpgame.notificationservice"},
    "party-service": {"port": "8093", "domain": "party", "base_path": "/api/v1/party", "package": "com.necpgame.partyservice"},
    "party module": {"port": "8093", "domain": "party", "base_path": "/api/v1/party", "package": "com.necpgame.partymodule"},
    "mail-service": {"port": "8094", "domain": "mail", "base_path": "/api/v1/mail", "package": "com.necpgame.mailservice"},
    "inventory-service": {"port": "8096", "domain": "inventory", "base_path": "/api/v1/inventory", "package": "com.necpgame.inventoryservice"},
    "loot-service": {"port": "8097", "domain": "loot", "base_path": "/api/v1/loot", "package": "com.necpgame.lootservice"},
    "system-service": {"port": "8098", "domain": "system", "base_path": "/api/v1/system", "package": "com.necpgame.systemservice"},
}

DIRECTORY_SERVICE: Dict[Tuple[str, ...], str] = {
    ("auth",): "auth-service",
    ("characters",): "character-service",
    ("players",): "character-service",
    ("inventory",): "inventory-service",
    ("loot",): "loot-service",
    ("combat",): "gameplay-service",
    ("gameplay", "combat"): "gameplay-service",
    ("gameplay", "mechanics"): "gameplay-service",
    ("gameplay", "progression"): "gameplay-service",
    ("progression",): "gameplay-service",
    ("gameplay", "battle-pass"): "gameplay-service",
    ("gameplay", "actions"): "gameplay-service",
    ("gameplay", "clans"): "social-service",
    ("gameplay", "companions"): "gameplay-service",
    ("gameplay", "cosmetics"): "gameplay-service",
    ("gameplay", "cyberspace"): "gameplay-service",
    ("gameplay", "housing"): "gameplay-service",
    ("gameplay", "onboarding"): "gameplay-service",
    ("gameplay", "mvp"): "gameplay-service",
    ("gameplay", "economy"): "economy-service",
    ("economy",): "economy-service",
    ("trade",): "economy-service",
    ("trading",): "economy-service",
    ("gameplay", "social"): "social-service",
    ("social",): "social-service",
    ("guilds",): "social-service",
    ("party",): "party-service",
    ("npcs",): "social-service",
    ("mail",): "mail-service",
    ("notifications",): "notification-service",
    ("world",): "world-service",
    ("locations",): "world-service",
    ("lore",): "world-service",
    ("events",): "world-service",
    ("gameplay", "world"): "world-service",
    ("narrative",): "narrative-service",
    ("quests",): "narrative-service",
    ("meta",): "gameplay-service",
    ("matchmaking",): "gameplay-service",
    ("system",): "system-service",
    ("technical",): "admin-service",
    ("admin",): "admin-service",
    ("analytics",): "analytics-service",
    ("notifications", "chat"): "notification-service",
    ("internal",): "admin-service",
    ("mvp",): "gameplay-service",
    ("game",): "gameplay-service",
}

PATH_PREFIX_SERVICE: Dict[Tuple[str, ...], str] = {
    ("gameplay", "economy"): "economy-service",
    ("gameplay", "social"): "social-service",
    ("gameplay", "world"): "world-service",
    ("gameplay", "progression"): "gameplay-service",
    ("gameplay", "combat"): "gameplay-service",
    ("gameplay", "actions"): "gameplay-service",
    ("gameplay", "companions"): "gameplay-service",
    ("gameplay", "cosmetics"): "gameplay-service",
    ("gameplay", "cyberspace"): "gameplay-service",
    ("gameplay", "housing"): "gameplay-service",
    ("gameplay", "onboarding"): "gameplay-service",
    ("gameplay", "mechanics"): "gameplay-service",
    ("gameplay", "battle-pass"): "gameplay-service",
    ("gameplay", "loot"): "loot-service",
    ("social", "voice"): "social-service",
    ("social", "chat"): "social-service",
    ("social", "player-orders"): "social-service",
    ("gameplay", "clans"): "social-service",
    ("guilds",): "social-service",
    ("party",): "party-service",
    ("npcs",): "social-service",
    ("events",): "world-service",
    ("world", "player-orders"): "world-service",
    ("lore",): "world-service",
    ("narrative",): "narrative-service",
    ("quests",): "narrative-service",
    ("inventory",): "inventory-service",
    ("matchmaking",): "gameplay-service",
    ("analytics",): "analytics-service",
    ("notifications",): "notification-service",
    ("mail",): "mail-service",
    ("admin",): "admin-service",
    ("system",): "system-service",
    ("technical",): "admin-service",
    ("combat",): "gameplay-service",
    ("mvp",): "gameplay-service",
    ("game",): "gameplay-service",
}


def read_file(path: Path) -> List[str]:
    with path.open("r", encoding="utf-8") as handle:
        return handle.readlines()


def write_file(path: Path, lines: Sequence[str]) -> None:
    with path.open("w", encoding="utf-8") as handle:
        handle.writelines(lines)


def sanitize_service_name(name: str) -> str:
    return name.strip()


def find_microservice_from_comment(lines: List[str]) -> Optional[str]:
    pattern = re.compile(r"Microservice:\s*([^\(\r\n]+)")
    for line in lines[:40]:
        match = pattern.search(line)
        if match:
            return sanitize_service_name(match.group(1))
    return None


def infer_service_from_dirs(path: Path) -> Optional[str]:
    relative_parts = path.relative_to(API_ROOT).parts[:-1]
    for length in range(min(2, len(relative_parts)), 0, -1):
        key = tuple(relative_parts[:length])
        if key in DIRECTORY_SERVICE:
            return DIRECTORY_SERVICE[key]
    if relative_parts:
        key = (relative_parts[0],)
        if key in DIRECTORY_SERVICE:
            return DIRECTORY_SERVICE[key]
    return None


def split_first_path(path_key: str) -> List[str]:
    clean = path_key.lstrip("/")
    return [part for part in clean.split("/") if part]


def collect_first_path_segments(lines: List[str]) -> Optional[List[str]]:
    pattern = re.compile(r"^\s{2,}/([^:\s]+)")
    for line in lines:
        match = pattern.match(line)
        if match:
            return split_first_path(match.group(1))
    return None


def infer_service_from_paths(segments: Optional[List[str]]) -> Optional[str]:
    if not segments:
        return None
    for length in range(min(2, len(segments)), 0, -1):
        key = tuple(segments[:length])
        if key in PATH_PREFIX_SERVICE:
            return PATH_PREFIX_SERVICE[key]
    key = (segments[0],)
    return PATH_PREFIX_SERVICE.get(key)


def parse_port_from_comment(lines: List[str]) -> Optional[str]:
    pattern = re.compile(r"port\s*(\d+)")
    alt_pattern = re.compile(r"\((\d+)\)")
    for line in lines[:40]:
        match = pattern.search(line)
        if match:
            return match.group(1)
        alt = alt_pattern.search(line)
        if alt:
            return alt.group(1)
    return None


def derive_base_path(segments: Optional[List[str]]) -> str:
    if not segments:
        return "/api/v1"
    resource_segments = list(segments)
    if len(resource_segments) >= 2 and resource_segments[0] == "api" and resource_segments[1] == "v1":
        resource_segments = resource_segments[2:]
    if not resource_segments:
        return "/api/v1"
    if resource_segments[0] == "gameplay" and len(resource_segments) > 1:
        base_segments = resource_segments[:2]
    else:
        base_segments = resource_segments[:1]
    return "/api/v1/" + "/".join(base_segments)


def package_from_service(name: str) -> str:
    slug = re.sub(r"[^a-z0-9]", "", name.lower())
    return f"com.necpgame.{slug}"


def ensure_servers_block(lines: List[str], metadata: Dict[str, str]) -> List[str]:
    production_url = "https://api.necp.game/v1"
    local_url = "http://localhost:8080/api/v1"
    try:
        servers_index = next(idx for idx, line in enumerate(lines) if line.startswith("servers:"))
    except StopIteration:
        block = [
            "servers:\n",
            f"  - url: {production_url}\n",
            "    description: Production API Gateway\n",
            f"  - url: {local_url}\n",
            "    description: Local API Gateway\n",
        ]
        return list(lines) + block
    indent_match = re.match(r"^(\s*)", lines[servers_index])
    indent = indent_match.group(1) if indent_match else ""
    block_start = servers_index + 1
    block_end = block_start
    for idx in range(block_start, len(lines)):
        line = lines[idx]
        if not line.startswith(indent + " ") or line.strip() == "":
            break
        block_end = idx + 1
    block = lines[block_start:block_end]
    entry_indent = indent + "  "
    result_block = [
        f"{entry_indent}- url: {production_url}\n",
        f"{entry_indent}  description: Production API Gateway\n",
        f"{entry_indent}- url: {local_url}\n",
        f"{entry_indent}  description: Local API Gateway\n",
    ]
    return lines[:block_start] + result_block + lines[block_end:]


def ensure_x_microservice(lines: List[str], metadata: Dict[str, str]) -> List[str]:
    try:
        info_index = next(idx for idx, line in enumerate(lines) if line.startswith("info:"))
    except StopIteration:
        return lines
    indent_match = re.match(r"^(\s*)", lines[info_index])
    indent = indent_match.group(1) if indent_match else ""
    info_indent = indent + "  "
    block = [
        f"{info_indent}x-microservice:\n",
        f"{info_indent}  name: {metadata['name']}\n",
        f"{info_indent}  port: {metadata['port']}\n",
        f"{info_indent}  domain: {metadata['domain']}\n",
        f"{info_indent}  base-path: {metadata['base_path']}\n",
        f"{info_indent}  package: {metadata['package']}\n",
    ]
    start = None
    end = None
    for idx in range(info_index + 1, len(lines)):
        current = lines[idx]
        if current.startswith(info_indent + "x-microservice:"):
            start = idx
            end = idx + 1
            while end < len(lines) and lines[end].startswith(info_indent + "  "):
                end += 1
            break
        current_indent = len(current) - len(current.lstrip())
        if current.strip() and current_indent <= len(indent):
            break
    if start is not None:
        return lines[:start] + block + lines[end:]
    insert_index = info_index + 1
    for idx in range(info_index + 1, len(lines)):
        stripped = lines[idx].strip()
        current_indent = len(lines[idx]) - len(lines[idx].lstrip())
        if stripped and current_indent <= len(indent):
            insert_index = idx
            break
        insert_index = idx + 1
    return lines[:insert_index] + block + lines[insert_index:]


def determine_metadata(path: Path, lines: List[str]) -> Optional[Dict[str, str]]:
    segments = collect_first_path_segments(lines)
    service_name = find_microservice_from_comment(lines)
    if not service_name:
        service_name = infer_service_from_dirs(path)
    if not service_name:
        service_name = infer_service_from_paths(segments)
    if not service_name:
        return None
    service_name = sanitize_service_name(service_name)
    defaults = SERVICE_DEFAULTS.get(service_name)
    if not defaults:
        return None
    port = parse_port_from_comment(lines) or defaults["port"]
    domain = defaults["domain"]
    base_path = defaults["base_path"]
    if segments:
        derived_base_path = derive_base_path(segments)
        if derived_base_path.startswith("/api/v1"):
            base_path = derived_base_path
    package = defaults.get("package") or package_from_service(service_name)
    return {
        "name": service_name,
        "port": port,
        "domain": domain,
        "base_path": base_path,
        "package": package,
    }


def process_file(path: Path) -> str:
    lines = read_file(path)
    if not any(line.startswith("info:") for line in lines):
        return "skipped"
    metadata = determine_metadata(path, lines)
    if not metadata:
        rel_api = path.relative_to(API_ROOT).parts
        if len(rel_api) >= 2 and rel_api[0] == "shared" and rel_api[1] == "common":
            return "skipped"
        return "failed"
    updated = ensure_x_microservice(lines, metadata)
    updated = ensure_servers_block(updated, metadata)
    if updated != lines:
        write_file(path, updated)
        return "updated"
    return "unchanged"


def main() -> None:
    updated_files: List[str] = []
    unchanged_files: List[str] = []
    skipped_files: List[str] = []
    failed_files: List[str] = []

    for yaml_path in sorted(API_ROOT.rglob("*.yaml")):
        status = process_file(yaml_path)
        relative = str(yaml_path.relative_to(ROOT_DIR))
        if status == "updated":
            updated_files.append(relative)
        elif status == "unchanged":
            unchanged_files.append(relative)
        elif status == "skipped":
            skipped_files.append(relative)
        else:
            failed_files.append(relative)

    print("=== add_x_microservice summary ===")
    print(f"Updated files ({len(updated_files)}):")
    for file in updated_files:
        print(f"  - {file}")
    print(f"\nAlready compliant ({len(unchanged_files)}):")
    for file in unchanged_files:
        print(f"  - {file}")
    print(f"\nSkipped (no info section) ({len(skipped_files)}):")
    for file in skipped_files:
        print(f"  - {file}")
    if failed_files:
        print(f"\nFailed to determine metadata ({len(failed_files)}):")
        for file in failed_files:
            print(f"  - {file}")
    else:
        print("\nFailed to determine metadata (0)")


if __name__ == "__main__":
    main()


