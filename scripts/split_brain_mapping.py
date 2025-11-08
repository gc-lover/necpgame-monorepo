#!/usr/bin/env python3
"""Split brain-mapping.yaml into the active file and archive parts."""
from __future__ import annotations

from pathlib import Path
from typing import List, Dict, Any

KEEP_LINE_LIMIT = 320
PART_LINE_LIMIT = 380
TARGET_FILE = Path("API-SWAGGER/tasks/config/brain-mapping.yaml")


def load_entries(text: str) -> List[str]:
    entries: List[str] = []
    current: List[str] = []
    for line in text.splitlines():
        if line.startswith("  - source:"):
            if current:
                entries.append("\n".join(current).rstrip())
            current = [line]
        else:
            if current:
                current.append(line)
    if current:
        entries.append("\n".join(current).rstrip())
    return entries


def get_task_id(entry_text: str) -> str:
    for entry_line in entry_text.split("\n"):
        if "task_id:" in entry_line:
            return entry_line.split(":", 1)[1].strip().strip('"')
    return "unknown"


def build_parts(entries: List[str]) -> List[Dict[str, Any]]:
    parts: List[Dict[str, Any]] = []
    index = 1
    start = 0
    while start < len(entries):
        chunk: List[str] = []
        chunk_lines = 4  # header + empty line placeholder
        i = start
        while i < len(entries):
            entry = entries[i]
            entry_lines = entry.count("\n") + 1
            extra = 1 if chunk else 0
            if chunk_lines + extra + entry_lines > PART_LINE_LIMIT:
                break
            if extra:
                chunk_lines += 1
            chunk.append(entry)
            chunk_lines += entry_lines
            i += 1
        if not chunk:
            chunk.append(entries[start])
            i = start + 1
        parts.append(
            {
                "index": index,
                "entries": chunk,
                "first_task": get_task_id(chunk[0]),
                "last_task": get_task_id(chunk[-1]),
            }
        )
        start = i
        index += 1
    return parts


def render_main(entries: List[str], parts: List[Dict[str, Any]]) -> str:
    base_header = [
        "# Mapping of .BRAIN documents to API-SWAGGER tasks",
        "#",
        "# This file stores only the most recent active records.",
        "# Move older records to archive parts and keep each file under 400 lines.",
        "#",
    ]
    archive_lines = ["# Archive parts:"]
    if parts:
        for part in parts:
            archive_lines.append(
                f"#   brain-mapping_{part['index']:04d}.yaml (tasks {part['first_task']} - {part['last_task']})"
            )
    else:
        archive_lines.append("#   none yet")
    format_block = [
        "# Record fields:",
        "# - source: path to the .BRAIN document",
        "# - target: path to the API file in API-SWAGGER",
        "# - task_id: identifier of the task",
        "# - task_file: path to the task description",
        "# - status: queued | assigned | in_progress | completed | failed",
        "# - created: creation timestamp",
        "# - version: .BRAIN document version",
        "# - updated: last update timestamp",
        "# - completed: completion timestamp (optional)",
        "#",
    ]
    header_full = base_header[:1] + archive_lines + base_header[1:] + format_block
    body_lines: List[str] = header_full + ["mappings:"]
    if entries:
        body_lines.append("")
        body_lines.append("\n\n".join(entries))
    content = "\n".join(body_lines).rstrip("\n") + "\n"
    return content


def main() -> None:
    if not TARGET_FILE.exists():
        raise FileNotFoundError(TARGET_FILE)
    text = TARGET_FILE.read_text(encoding="utf-8")
    entries = load_entries(text)
    if not entries:
        raise ValueError("No mapping entries found")

    main_entries: List[str] = []
    current_lines = len(render_main([], [] ).splitlines())  # baseline with empty data
    for entry in entries:
        entry_lines = entry.count("\n") + 1
        extra = 1 if main_entries else 0
        if current_lines + extra + entry_lines > KEEP_LINE_LIMIT:
            break
        if extra:
            current_lines += 1
        main_entries.append(entry)
        current_lines += entry_lines
    remaining_entries = entries[len(main_entries):]

    while True:
        parts = build_parts(remaining_entries)
        main_content = render_main(main_entries, parts)
        total_lines = len(main_content.rstrip("\n").splitlines())
        if total_lines <= 400:
            break
        if not main_entries:
            raise RuntimeError("Unable to keep main file under 400 lines")
        # move the oldest entry from main to the archive queue
        remaining_entries.insert(0, main_entries.pop())

    parts = build_parts(remaining_entries)
    main_content = render_main(main_entries, parts)

    # Remove existing archive parts before writing new ones
    for old_part in TARGET_FILE.parent.glob("brain-mapping_*.yaml"):
        old_part.unlink()

    TARGET_FILE.write_text(main_content, encoding="utf-8")

    for part in parts:
        part_path = TARGET_FILE.parent / f"brain-mapping_{part['index']:04d}.yaml"
        part_header = [
            f"# Archive brain-mapping part {part['index']:04d}",
            f"# Task range: {part['first_task']} - {part['last_task']}",
            "#",
            "mappings:",
            "",
        ]
        part_body = "\n\n".join(part["entries"])
        part_content = "\n".join(part_header + [part_body]).rstrip("\n") + "\n"
        part_path.write_text(part_content, encoding="utf-8")

    print(f"Main file lines: {len(main_content.rstrip('\n').splitlines())}")
    for part in parts:
        part_path = TARGET_FILE.parent / f"brain-mapping_{part['index']:04d}.yaml"
        line_count = len(part_path.read_text(encoding='utf-8').rstrip('\n').splitlines())
        print(
            f"Part {part['index']:04d} lines: {line_count} (tasks {part['first_task']} - {part['last_task']})"
        )


if __name__ == "__main__":
    main()
