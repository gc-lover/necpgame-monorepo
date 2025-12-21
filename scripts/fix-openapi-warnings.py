#!/usr/bin/env python3
"""
Fix OpenAPI Warnings Script
Исправляет основные OpenAPI warnings массово
"""

import os
import glob
import re
import subprocess

def add_license_to_file(file_path):
    """Добавляет license поле в YAML файл, если его нет"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Проверяем, есть ли уже license
        if 'license:' in content:
            return False

        # Проверяем, есть ли info блок
        if 'info:' not in content:
            return False

        # Добавляем license после version
        pattern = r'(  version:.*$)'
        replacement = r'\1\n  license:\n    name: MIT\n    url: https://opensource.org/licenses/MIT'

        new_content = re.sub(pattern, replacement, content, flags=re.MULTILINE)

        if new_content != content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            return True

    except Exception as e:
        print(f"Ошибка при обработке {file_path}: {e}")

    return False

def fix_common_paths(file_path):
    """Исправляет пути к common-schemas.yaml"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        original_content = content
        content = content.replace('../../misc-domain/common/common.yaml', '../../common-schemas.yaml')

        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True

    except Exception as e:
        print(f"Ошибка при обработке {file_path}: {e}")

    return False

def main():
    """Основная функция"""
    print("Исправляем OpenAPI warnings массово...")

    # Находим все YAML файлы в proto/openapi
    yaml_files = glob.glob("proto/openapi/**/*.yaml", recursive=True)

    license_count = 0
    path_count = 0

    for file_path in yaml_files:
        # Добавляем license
        if add_license_to_file(file_path):
            print(f"Добавлен license: {file_path}")
            license_count += 1

        # Исправляем пути
        if fix_common_paths(file_path):
            print(f"Исправлен путь: {file_path}")
            path_count += 1

    print("\nГотово!")
    print(f"Добавлено license полей: {license_count}")
    print(f"Исправлено путей: {path_count}")
    print("\nПримечание: Warnings про localhost серверы оставлены (нормально для dev).")

if __name__ == "__main__":
    main()
