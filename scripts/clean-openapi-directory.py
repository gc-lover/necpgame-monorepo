#!/usr/bin/env python3
"""
OpenAPI Directory Cleaner

Очищает OpenAPI директории от запрещенных файлов:
- Go файлы (сгенерированный код)
- Временные файлы (.tmp, .backup, .full)
- Файлы с temp_ префиксом
- Bundled файлы (дубликаты)

Использование:
    python scripts/clean-openapi-directory.py [--dry-run] [--domain DOMAIN]

Аргументы:
    --dry-run    : Только показать что будет удалено, не удалять
    --domain     : Очистить только указанный домен
    --all        : Очистить все домены (по умолчанию)

Примеры:
    python scripts/clean-openapi-directory.py --dry-run
    python scripts/clean-openapi-directory.py --domain social-domain
    python scripts/clean-openapi-directory.py --all
"""

import os
import sys
import argparse
import shutil
from pathlib import Path
from typing import List, Set


class OpenAPICleaner:
    """Класс для очистки OpenAPI директорий"""

    def __init__(self, openapi_root: str = "proto/openapi"):
        self.openapi_root = Path(openapi_root)
        self.removed_files: List[Path] = []
        self.removed_dirs: List[Path] = []

    def get_forbidden_patterns(self) -> Set[str]:
        """Возвращает паттерны запрещенных файлов"""
        return {
            # Go файлы (сгенерированный код)
            "*.go",
            # Временные файлы
            "*.tmp",
            "*.temp",
            "*.bak",
            "*.backup",
            # Полные/bundled версии
            "*.full",
            "*bundled*",
            "*bundle*",
            # Тестовые файлы
            "test-*",
            "*test*",
            # Модульные файлы Go
            "go.mod",
            "go.sum",
            # IDE файлы
            ".DS_Store",
            "Thumbs.db",
        }

    def get_forbidden_dir_patterns(self) -> Set[str]:
        """Возвращает паттерны запрещенных директорий"""
        return {
            # Сгенерированные директории
            "test-gen",
            "test",
            # Временные директории
            "temp_*",
            "tmp_*",
            "backup_*",
            # Go модули
            "modules",
            # IDE директории
            ".idea",
            "__pycache__",
        }

    def should_remove_file(self, file_path: Path) -> bool:
        """Проверяет, нужно ли удалить файл"""
        file_name = file_path.name

        # Проверяем точные совпадения с запрещенными паттернами
        for pattern in self.get_forbidden_patterns():
            if pattern.startswith("*"):
                # Паттерн с wildcard
                suffix = pattern[1:]  # "*.go" -> ".go"
                if file_name.endswith(suffix):
                    return True
            elif pattern.endswith("*"):
                # Паттерн с префиксом
                prefix = pattern[:-1]  # "temp_*" -> "temp_"
                if file_name.startswith(prefix):
                    return True
            elif file_name == pattern:
                return True

        return False

    def should_remove_dir(self, dir_path: Path) -> bool:
        """Проверяет, нужно ли удалить директорию"""
        dir_name = dir_path.name

        for pattern in self.get_forbidden_dir_patterns():
            if pattern.endswith("*"):
                prefix = pattern[:-1]
                if dir_name.startswith(prefix):
                    return True
            elif dir_name == pattern:
                return True

        return False

    def clean_directory(self, directory: Path, dry_run: bool = True) -> None:
        """Очищает директорию от запрещенных файлов"""

        if not directory.exists():
            print(f"[WARNING] Directory ne suschestvuet: {directory}")
            return

        print(f"[SCAN] Skaniruyu: {directory}")

        # Рекурсивно обходим все файлы и директории
        for root, dirs, files in os.walk(directory):
            root_path = Path(root)

            # Проверяем файлы
            for file in files:
                file_path = root_path / file
                if self.should_remove_file(file_path):
                    if dry_run:
                        print(f"[DELETE] Budet udalen file: {file_path}")
                    else:
                        try:
                            file_path.unlink()
                            self.removed_files.append(file_path)
                            print(f"[OK] Udalen file: {file_path}")
                        except Exception as e:
                            print(f"[ERROR] Oshibka udaleniya file {file_path}: {e}")

            # Проверяем директории (только пустые после удаления файлов)
            for dir_name in dirs[:]:  # Копия списка для безопасного удаления
                dir_path = root_path / dir_name
                if self.should_remove_dir(dir_path):
                    if dry_run:
                        print(f"[DELETE] Budet udalena directory: {dir_path}")
                    else:
                        try:
                            shutil.rmtree(dir_path)
                            self.removed_dirs.append(dir_path)
                            print(f"[OK] Udalena directory: {dir_path}")
                            dirs.remove(dir_name)  # Udalyaem iz spiska obkhoda
                        except Exception as e:
                            print(f"[ERROR] Oshibka udaleniya directory {dir_path}: {e}")

    def clean_domain(self, domain_name: str, dry_run: bool = True) -> None:
        """Очищает конкретный домен"""
        domain_path = self.openapi_root / domain_name
        if domain_path.exists():
            self.clean_directory(domain_path, dry_run)
        else:
            print(f"[WARNING] Domen ne naiden: {domain_name}")

    def clean_all_domains(self, dry_run: bool = True) -> None:
        """Очищает все домены"""
        if not self.openapi_root.exists():
            print(f"[ERROR] OpenAPI root ne naiden: {self.openapi_root}")
            return

        for item in self.openapi_root.iterdir():
            if item.is_dir() and not item.name.startswith('.'):
                self.clean_domain(item.name, dry_run)

    def print_summary(self) -> None:
        """Печатает сводку результатов"""
        if self.removed_files or self.removed_dirs:
            print(f"\n[SUMMARY] SVODKA OCHISTKI:")
            print(f"   Udaleno filev: {len(self.removed_files)}")
            print(f"   Udaleno direktoriev: {len(self.removed_dirs)}")

            if self.removed_files:
                print(f"\n[FILES] Udalennye file:")
                for file in self.removed_files[:10]:  # Pokazyvaem pervye 10
                    print(f"   - {file}")
                if len(self.removed_files) > 10:
                    print(f"   ... i eschyo {len(self.removed_files) - 10} filev")

            if self.removed_dirs:
                print(f"\n[DIRS] Udalennye direktori:")
                for dir_path in self.removed_dirs[:5]:  # Pokazyvaem pervye 5
                    print(f"   - {dir_path}")
                if len(self.removed_dirs) > 5:
                    print(f"   ... i eschyo {len(self.removed_dirs) - 5} direktoriev")
        else:
            print(f"\n[SUCCESS] Nechego udalyat - direktori uzhe chistye!")


def main():
    parser = argparse.ArgumentParser(
        description="Очистка OpenAPI директорий от запрещенных файлов",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Примеры использования:
  python scripts/clean-openapi-directory.py --dry-run
  python scripts/clean-openapi-directory.py --domain social-domain
  python scripts/clean-openapi-directory.py --all
        """
    )

    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Только показать что будет удалено, не удалять"
    )

    parser.add_argument(
        "--domain",
        type=str,
        help="Очистить только указанный домен"
    )

    parser.add_argument(
        "--all",
        action="store_true",
        default=True,
        help="Очистить все домены (по умолчанию)"
    )

    args = parser.parse_args()

    cleaner = OpenAPICleaner()

    if args.domain:
        print(f"[TARGET] Ochistka domena: {args.domain}")
        cleaner.clean_domain(args.domain, args.dry_run)
    else:
        print(f"[TARGET] Ochistka vsekh domenov v: {cleaner.openapi_root}")
        cleaner.clean_all_domains(args.dry_run)

    cleaner.print_summary()

    if args.dry_run:
        print(f"\n[INFO] Dlya realnogo udaleniya zapustite bez --dry-run")
    else:
        print(f"\n[SUCCESS] Ochistka zavershena!")


if __name__ == "__main__":
    main()
