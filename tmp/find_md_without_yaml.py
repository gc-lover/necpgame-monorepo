import pathlib


def main() -> None:
    root = pathlib.Path("shared/docs/knowledge")
    md_files = sorted(root.rglob("*.md"))
    batch = []
    for md_path in md_files:
        if md_path.name.startswith("_"):
            continue
        if md_path.with_suffix(".yaml").exists():
            continue
        batch.append(md_path)
        if len(batch) == 5:
            break
    for item in batch:
        print(item.as_posix())


if __name__ == "__main__":
    main()

