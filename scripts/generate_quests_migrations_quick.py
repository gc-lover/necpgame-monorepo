# Lightweight quests migration generator (backend autofix)
# Scans quest YAML and writes SQL migrations into infrastructure/liquibase/migrations/data/quests

import json
import re
from datetime import datetime
from pathlib import Path

import yaml


ROOT = Path(__file__).resolve().parents[1]
MIGRATIONS_DIR = ROOT / "infrastructure" / "liquibase" / "migrations"
QUESTS_BASE = ROOT / "knowledge" / "canon" / "lore" / "_03-lore" / "timeline-author" / "quests"

SKIP_TOKENS = ["template", "readme", "index", "list", "tracker", "spread", "prioritization"]


def next_migration_number() -> int:
    max_num = 0
    for sql in MIGRATIONS_DIR.glob("V*__*.sql"):
        m = re.match(r"V(\d+)__", sql.name)
        if m:
            max_num = max(max_num, int(m.group(1)))
    return max_num + 1


def escape_sql(value: str) -> str:
    # Escape backslashes first, then single quotes for SQL literals
    return value.replace("\\", "\\\\").replace("'", "''")


def migration_name(relative_path: Path) -> str:
    path_str = str(relative_path).replace("\\", "/")
    name = path_str.replace("/", "_").replace("-", "_")
    name = "_".join(filter(None, name.split("_")))
    return name or "root"


def load_yaml(path: Path) -> dict:
    with path.open("r", encoding="utf-8") as f:
        return yaml.safe_load(f) or {}


def dumps(data) -> str:
    return json.dumps(
        data,
        ensure_ascii=False,
        default=lambda o: o.isoformat() if hasattr(o, "isoformat") else str(o),
    )


def main() -> None:
    quest_files = [
        p
        for p in QUESTS_BASE.rglob("quest-*.yaml")
        if not any(token in p.name.lower() for token in SKIP_TOKENS)
    ]

    if not quest_files:
        print("No quest files found")
        return

    quest_files.sort()
    data_dir = MIGRATIONS_DIR / "data" / "quests"
    data_dir.mkdir(parents=True, exist_ok=True)

    mig_num = next_migration_number()
    total = 0

    for quest_file in quest_files:
        data = load_yaml(quest_file)
        metadata = data.get("metadata", {}) or {}
        quest_def = data.get("quest_definition", {}) or {}
        summary = data.get("summary", {}) or {}

        quest_id = metadata.get("id")
        if not quest_id:
            continue

        version_raw = str(metadata.get("version", "1.0.0"))
        version_suffix = version_raw.replace(".", "_").replace("-", "_")

        rel = quest_file.relative_to(QUESTS_BASE)
        mig_base = migration_name(rel.with_suffix(""))
        mig_file = data_dir / f"V{mig_num}__data_quest_{mig_base}_v{version_suffix}.sql"

        title = metadata.get("title") or summary.get("goal") or ""
        description = summary.get("essence") or summary.get("goal") or summary.get("problem") or ""
        quest_type = quest_def.get("quest_type", "side")
        level_min = quest_def.get("level_min")
        level_max = quest_def.get("level_max")

        requirements = dumps(quest_def.get("requirements", {}))
        objectives = dumps(quest_def.get("objectives", []))
        rewards = dumps(quest_def.get("rewards", {}))
        branches = dumps(quest_def.get("branches", []))
        content_data = dumps(data)

        sql_lines = [
            "-- Content quests auto-import",
            f"-- Source: {rel}",
            f"-- Version: {version_raw}",
            f"-- Generated: {datetime.utcnow().isoformat()}Z",
            "",
            "BEGIN;",
            "",
            f"-- Quest: {quest_id}",
            (
                "INSERT INTO gameplay.quest_definitions "
                "(quest_id, title, description, quest_type, level_min, level_max, "
                "requirements, objectives, rewards, branches, content_data, version, is_active) "
                "VALUES ("
                f"'{escape_sql(str(quest_id))}', "
                f"'{escape_sql(title)}', "
                f"'{escape_sql(description)}', "
                f"'{escape_sql(str(quest_type))}', "
                f"{'NULL' if level_min is None else level_min}, "
                f"{'NULL' if level_max is None else level_max}, "
                f"'{escape_sql(requirements)}'::jsonb, "
                f"'{escape_sql(objectives)}'::jsonb, "
                f"'{escape_sql(rewards)}'::jsonb, "
                f"'{escape_sql(branches)}'::jsonb, "
                f"'{escape_sql(content_data)}'::jsonb, "
                "1, "
                "true"
                ") "
                "ON CONFLICT (quest_id) DO UPDATE SET "
                "title = EXCLUDED.title, "
                "description = EXCLUDED.description, "
                "quest_type = EXCLUDED.quest_type, "
                "level_min = EXCLUDED.level_min, "
                "level_max = EXCLUDED.level_max, "
                "requirements = EXCLUDED.requirements, "
                "objectives = EXCLUDED.objectives, "
                "rewards = EXCLUDED.rewards, "
                "branches = EXCLUDED.branches, "
                "content_data = EXCLUDED.content_data, "
                "updated_at = CURRENT_TIMESTAMP;"
            ),
            "",
            "COMMIT;",
            "",
            "-- Total imported: 1 quest",
            "",
        ]

        mig_file.write_text("\n".join(sql_lines), encoding="utf-8")
        print(f"Generated {mig_file.name}")
        mig_num += 1
        total += 1

    print(f"Done: {total} migrations written to {data_dir}")


if __name__ == "__main__":
    main()

