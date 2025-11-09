#!/usr/bin/env python3
"""
CLI для управления YAML-очередями: add/move/remove/list.
Требует PyYAML: pip install pyyaml
"""

from __future__ import annotations

import argparse
import datetime as dt
import sys
from pathlib import Path

try:
    import yaml  # type: ignore
except ImportError as exc:  # pragma: no cover
    raise SystemExit(
        "Не установлен модуль PyYAML. Выполните: pip install pyyaml"
    ) from exc


def load_queue(path: Path) -> dict:
    if not path.exists():
        raise SystemExit(f"Файл очереди не найден: {path}")
    with path.open("r", encoding="utf-8") as fh:
        data = yaml.safe_load(fh) or {}
    if "items" not in data or data["items"] is None:
        data["items"] = []
    return data


def save_queue(path: Path, queue: dict) -> None:
    queue["last_updated"] = dt.datetime.now().strftime("%Y-%m-%d %H:%M")
    with path.open("w", encoding="utf-8") as fh:
        yaml.safe_dump(queue, fh, allow_unicode=True, sort_keys=False)
    print(f"Обновлён файл очереди: {path}")


def cmd_list(queue_path: Path) -> None:
    queue = load_queue(queue_path)
    status = queue.get("status", "unknown")
    print(f"Статус: {status}")
    for item in queue.get("items", []):
        owner = f" (owner: {item.get('owner')})" if item.get("owner") else ""
        print(f"{item.get('id')} — {item.get('title')}{owner}")


def cmd_add(queue_path: Path, args: argparse.Namespace) -> None:
    queue = load_queue(queue_path)
    items = queue.get("items", [])
    if any(it.get("id") == args.id for it in items):
        raise SystemExit(f"Запись с id '{args.id}' уже существует в {queue_path}")
    item = {
        "id": args.id,
        "title": args.title,
        "updated": dt.datetime.now().strftime("%Y-%m-%d %H:%M"),
    }
    if args.owner:
        item["owner"] = args.owner
    if args.api_spec:
        item["api_spec"] = args.api_spec
    if args.notes:
        item["notes"] = args.notes
    items.append(item)
    queue["items"] = items
    save_queue(queue_path, queue)


def cmd_remove(queue_path: Path, args: argparse.Namespace) -> None:
    queue = load_queue(queue_path)
    items = [it for it in queue.get("items", []) if it.get("id") != args.id]
    if len(items) == len(queue.get("items", [])):
        raise SystemExit(f"Элемент с id '{args.id}' не найден в {queue_path}")
    queue["items"] = items
    save_queue(queue_path, queue)


def cmd_move(source: Path, target: Path, args: argparse.Namespace) -> None:
    queue_src = load_queue(source)
    queue_tgt = load_queue(target)
    items_src = queue_src.get("items", [])
    try:
        idx = next(i for i, it in enumerate(items_src) if it.get("id") == args.id)
    except StopIteration as exc:
        raise SystemExit(f"Элемент с id '{args.id}' не найден в {source}") from exc
    item = items_src.pop(idx)
    if any(it.get("id") == args.id for it in queue_tgt.get("items", [])):
        raise SystemExit(f"Элемент с id '{args.id}' уже есть в {target}")
    item["updated"] = dt.datetime.now().strftime("%Y-%m-%d %H:%M")
    queue_src["items"] = items_src
    save_queue(source, queue_src)
    queue_tgt.setdefault("items", []).append(item)
    save_queue(target, queue_tgt)


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Управление YAML-очередями")
    sub = parser.add_subparsers(dest="command", required=True)

    list_p = sub.add_parser("list", help="Показать элементы очереди")
    list_p.add_argument("source", type=Path, help="Путь к файлу очереди")

    add_p = sub.add_parser("add", help="Добавить карточку")
    add_p.add_argument("source", type=Path, help="Путь к файлу очереди")
    add_p.add_argument("--id", required=True, help="Идентификатор карточки")
    add_p.add_argument("--title", required=True, help="Заголовок")
    add_p.add_argument("--owner", help="Владелец")
    add_p.add_argument("--api-spec", help="Путь к спецификации")
    add_p.add_argument("--notes", help="Дополнительные пометки")

    remove_p = sub.add_parser("remove", help="Удалить карточку")
    remove_p.add_argument("source", type=Path, help="Путь к файлу очереди")
    remove_p.add_argument("--id", required=True, help="Идентификатор карточки")

    move_p = sub.add_parser("move", help="Переместить карточку")
    move_p.add_argument("source", type=Path, help="Файл исходной очереди")
    move_p.add_argument("target", type=Path, help="Файл целевой очереди")
    move_p.add_argument("--id", required=True, help="Идентификатор карточки")

    return parser


def main(argv: list[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    if args.command == "list":
        cmd_list(args.source.resolve())
    elif args.command == "add":
        cmd_add(args.source.resolve(), args)
    elif args.command == "remove":
        cmd_remove(args.source.resolve(), args)
    elif args.command == "move":
        cmd_move(args.source.resolve(), args.target.resolve(), args)
    else:  # pragma: no cover
        raise SystemExit(f"Неизвестная команда: {args.command}")
    return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())


