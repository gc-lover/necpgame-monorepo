#!/usr/bin/env python3
"""
Fix All OpenAPI Warnings Script
Исправляет ВСЕ OpenAPI warnings массово
"""

import os
import glob
import re
import subprocess
import json

def run_lint(file_path):
    """Запускает redocly lint и возвращает результат"""
    try:
        result = subprocess.run([
            'npx', '--yes', '@redocly/cli', 'lint', file_path,
            '--format', 'json'
        ], capture_output=True, text=True, cwd=os.getcwd())

        if result.returncode == 0:
            return None  # Нет ошибок

        try:
            return json.loads(result.stdout)
        except:
            return None
    except:
        return None

def has_warning_type(lint_result, warning_type):
    """Проверяет, есть ли определенный тип warning"""
    if not lint_result or 'warnings' not in lint_result:
        return False

    for warning in lint_result['warnings']:
        if warning.get('rule') == warning_type:
            return True
    return False

def add_4xx_responses(file_path):
    """Добавляет 4XX responses для операций без них"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        original_content = content

        # Для GET операций добавляем 429
        content = re.sub(
            r'(    get:.*?\n.*?responses:\n(?:        \'[^']+\'.*?\n)*)(?!.*?(?:\'4\d+\'|\'429\').*?\n)',
            r'\1        \'429\':\n          description: Too many requests - rate limit exceeded\n          headers:\n            Retry-After:\n              schema:\n                type: integer\n                example: 60\n          content:\n            application/json:\n              schema:\n                $ref: ../../common-schemas.yaml#/components/schemas/Error\n',
            content,
            flags=re.DOTALL
        )

        # Для POST операций добавляем 400
        content = re.sub(
            r'(    post:.*?\n.*?responses:\n(?:        \'[^']+\'.*?\n)*)(?!.*?(?:\'4\d+\'|\'400\').*?\n)',
            r'\1        \'400\':\n          description: Invalid request data\n          content:\n            application/json:\n              schema:\n                $ref: ../../common-schemas.yaml#/components/schemas/Error\n',
            content,
            flags=re.DOTALL
        )

        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True

    except Exception as e:
        print(f"Ошибка при добавлении 4XX responses в {file_path}: {e}")

    return False

def main():
    """Основная функция"""
    print("Исправляем ВСЕ OpenAPI warnings массово...")

    # Находим все main.yaml и service.yaml файлы
    yaml_files = glob.glob("proto/openapi/**/*main.yaml", recursive=True)
    yaml_files.extend(glob.glob("proto/openapi/**/*service.yaml", recursive=True))

    total_files = len(yaml_files)
    fixed_files = 0

    print(f"Найдено файлов для обработки: {total_files}")

    for i, file_path in enumerate(yaml_files, 1):
        print(f"[{i}/{total_files}] Проверяем: {os.path.basename(file_path)}")

        # Проверяем warnings
        lint_result = run_lint(file_path)

        if not lint_result:
            continue

        has_changes = False

        # Добавляем 4XX responses если есть соответствующие warnings
        if has_warning_type(lint_result, 'operation-4xx-response'):
            if add_4xx_responses(file_path):
                print(f"  ✓ Добавлены 4XX responses")
                has_changes = True

        if has_changes:
            fixed_files += 1

    print("
Готово!"    print(f"Обработано файлов: {total_files}")
    print(f"Исправлено файлов: {fixed_files}")
    print("\nПримечание: Некоторые warnings (localhost серверы) оставлены как есть.")
    print("Примечание: Для полного исправления может потребоваться ручная доработка.")

if __name__ == "__main__":
    main()
