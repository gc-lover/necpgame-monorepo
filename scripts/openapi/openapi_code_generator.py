#!/usr/bin/env python3
"""
OpenAPI Code Generator - Расширенная версия с поддержкой external refs
Решение проблемы: Кодогенерация без bundling зависимостей

Цель: Генерировать Go код напрямую из OpenAPI с external references,
обходя ограничения ogen.

Использование:
    python scripts/openapi/openapi_code_generator.py proto/openapi/companion-domain/main.yaml --target generated-go
"""

import os
import yaml
import json
import argparse
from pathlib import Path
from typing import Dict, Any, Set, Optional, List
from urllib.parse import urlparse
import re
import subprocess
import tempfile

class OpenAPICodeGenerator:
    """Расширенный генератор кода с поддержкой external references."""

    def __init__(self):
        self.temp_dir = None

    def generate(self, input_file: Path, target_dir: Path, package: str = "api") -> bool:
        """Основной метод генерации кода."""
        print(f"[CODEGEN] Generating Go code from {input_file} -> {target_dir}")

        # Создаем временную директорию для bundled файла
        with tempfile.TemporaryDirectory() as temp_dir:
            self.temp_dir = Path(temp_dir)
            bundled_file = self.temp_dir / "bundled.yaml"

            # Бандлим спецификацию
            if not self._bundle_specification(input_file, bundled_file):
                print("[ERROR] Failed to bundle specification")
                return False

            # Генерируем код с помощью ogen
            if not self._generate_with_ogen(bundled_file, target_dir, package):
                print("[ERROR] Failed to generate code with ogen")
                return False

        print(f"[SUCCESS] Code generated successfully in {target_dir}")
        return True

    def _bundle_specification(self, input_file: Path, output_file: Path) -> bool:
        """Бандлим спецификацию с помощью Python bundler."""
        try:
            # Импортируем наш bundler
            from openapi_bundler import OpenAPIBundler

            bundler = OpenAPIBundler(input_file.parent)
            return bundler.bundle(input_file, output_file)

        except ImportError:
            print("[WARNING] Python bundler not available, trying direct ogen...")
            # Fallback: копируем файл как есть
            import shutil
            shutil.copy2(input_file, output_file)
            return True

    def _generate_with_ogen(self, bundled_file: Path, target_dir: Path, package: str) -> bool:
        """Генерируем код с помощью ogen."""
        try:
            # Используем локально установленный ogen
            cmd = [
                "ogen",
                "--target", str(target_dir),
                "--package", package,
                "--clean",
                str(bundled_file)
            ]

            result = subprocess.run(cmd, capture_output=True, text=True, cwd=target_dir.parent)

            if result.returncode == 0:
                print("[CODEGEN] ogen generation successful")
                return True
            else:
                print(f"[ERROR] ogen failed: {result.stderr}")
                return False

        except FileNotFoundError:
            print("[ERROR] ogen not found. Install with: go install github.com/ogen-go/ogen/cmd/ogen@latest")
            return False

    def validate_generation(self, target_dir: Path) -> bool:
        """Валидируем сгенерированный код."""
        try:
            # Проверяем, что директория создана
            if not target_dir.exists():
                print(f"[VALIDATE] Target directory not created: {target_dir}")
                return False

            # Проверяем наличие основных файлов
            expected_files = ["oas_schemas_gen.go", "oas_client_gen.go", "oas_server_gen.go"]
            missing_files = []

            for filename in expected_files:
                if not (target_dir / filename).exists():
                    missing_files.append(filename)

            if missing_files:
                print(f"[VALIDATE] Missing generated files: {missing_files}")
                return False

            # Проверяем компиляцию
            if not self._test_compilation(target_dir):
                print("[VALIDATE] Generated code does not compile")
                return False

            print("[VALIDATE] Code generation validation passed")
            return True

        except Exception as e:
            print(f"[VALIDATE] Validation error: {e}")
            return False

    def _test_compilation(self, target_dir: Path) -> bool:
        """Тестируем компиляцию сгенерированного кода."""
        try:
            # Создаем go.mod если нужно
            mod_file = target_dir.parent / "go.mod"
            if not mod_file.exists():
                # Инициализируем модуль
                cmd = ["go", "mod", "init", f"test-{target_dir.name}"]
                result = subprocess.run(cmd, cwd=target_dir.parent, capture_output=True, text=True)
                if result.returncode != 0:
                    print(f"[COMPILE] Failed to init module: {result.stderr}")
                    return False

            # Запускаем go mod tidy
            cmd = ["go", "mod", "tidy"]
            result = subprocess.run(cmd, cwd=target_dir.parent, capture_output=True, text=True)
            if result.returncode != 0:
                print(f"[COMPILE] go mod tidy failed: {result.stderr}")
                return False

            # Компилируем
            cmd = ["go", "build", "./..."]
            result = subprocess.run(cmd, cwd=target_dir.parent, capture_output=True, text=True)
            if result.returncode != 0:
                print(f"[COMPILE] Compilation failed: {result.stderr}")
                return False

            return True

        except Exception as e:
            print(f"[COMPILE] Compilation test error: {e}")
            return False


def main():
    parser = argparse.ArgumentParser(description='OpenAPI Code Generator with external refs support')
    parser.add_argument('input', help='Input OpenAPI file')
    parser.add_argument('--target', '-t', required=True, help='Target directory for generated code')
    parser.add_argument('--package', '-p', default='api', help='Go package name')
    parser.add_argument('--validate', action='store_true', help='Validate generated code')

    args = parser.parse_args()

    input_file = Path(args.input)
    target_dir = Path(args.target)

    if not input_file.exists():
        print(f"[ERROR] Input file not found: {input_file}")
        return 1

    target_dir.mkdir(parents=True, exist_ok=True)

    generator = OpenAPICodeGenerator()

    try:
        success = generator.generate(input_file, target_dir, args.package)
        if not success:
            return 1

        if args.validate:
            if not generator.validate_generation(target_dir):
                return 1

        print(f"[SUCCESS] Code generated in: {target_dir}")
        return 0

    except Exception as e:
        print(f"[ERROR] {e}")
        return 1


if __name__ == '__main__':
    exit(main())
