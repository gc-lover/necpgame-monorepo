from __future__ import annotations

import argparse
from pathlib import Path

TRACKER_PATH = Path(".BRAIN/06-tasks/config/readiness-tracker.yaml")
MARKER = "mappings:\n"


def build_entry(args: argparse.Namespace) -> str:
    microservice = args.microservice if args.microservice is not None else "null"
    directory = args.directory if args.directory is not None else "null"
    frontend_module = args.frontend_module if args.frontend_module is not None else "null"

    lines = [
        f"  - path: \"{args.path}\"",
        f"    version: \"{args.version}\"",
        f"    status: \"{args.status}\"",
        f"    priority: \"{args.priority}\"",
        f"    checked: \"{args.checked}\"",
        f"    checker: \"{args.checker}\"",
        "    api_target:",
        f"      microservice: {microservice}",
        f"      directory: {directory}",
        f"      frontend_module: {frontend_module}",
        f"    notes: \"{args.notes}\"",
        "",
    ]
    return "\n".join(lines)


def add_entry(entry: str) -> bool:
    tracker_text = TRACKER_PATH.read_text(encoding="utf-8")
    if entry.strip() in tracker_text:
        return False

    if MARKER not in tracker_text:
        raise SystemExit("Marker 'mappings:' not found in readiness-tracker.")

    updated = tracker_text.replace(MARKER, MARKER + "\n" + entry, 1)
    TRACKER_PATH.write_text(updated, encoding="utf-8")
    return True


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Add an entry block to readiness-tracker.yaml")
    parser.add_argument("--path", required=True)
    parser.add_argument("--version", required=True)
    parser.add_argument("--status", required=True)
    parser.add_argument("--priority", required=True)
    parser.add_argument("--checked", required=True)
    parser.add_argument("--checker", required=True)
    parser.add_argument("--notes", required=True)
    parser.add_argument("--microservice")
    parser.add_argument("--directory")
    parser.add_argument("--frontend-module")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    entry = build_entry(args)
    added = add_entry(entry)
    if not added:
        print("Entry already present, tracker unchanged.")


if __name__ == "__main__":
    main()
