#!/usr/bin/env python3
"""
Fix Common References Script
Исправляет ссылки на общие схемы в OpenAPI спецификациях
"""

import os
import glob
import re

def fix_common_refs():
    """Исправляет ссылки на common-schemas.yaml во всех YAML файлах"""
    print("Исправляем ссылки на общие схемы в OpenAPI спецификациях...")

    # Находим все YAML файлы в proto/openapi
    yaml_files = glob.glob("proto/openapi/**/*.yaml", recursive=True)

    for file_path in yaml_files:
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Заменяем неправильные ссылки на правильные
            original_content = content
            content = content.replace('../../misc-domain/common/common.yaml', '../../common-schemas.yaml')

            # Сохраняем файл только если были изменения
            if content != original_content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f"Исправлен: {file_path}")

        except Exception as e:
            print(f"Ошибка при обработке {file_path}: {e}")

    print("Готово! Все ссылки на общие схемы исправлены.")

if __name__ == "__main__":
    fix_common_refs()
