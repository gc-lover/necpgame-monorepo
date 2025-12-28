#!/usr/bin/env python3
"""
OpenAPI Bundler - Python-based alternative to redocly bundle
Решение проблемы: Bundling без зависимости от Node.js/redocly

Цель: Разрешать external $ref ссылки и создавать bundled спецификации
для ogen кодогенерации.

Использование:
    python openapi_bundler.py proto/openapi/companion-domain/main.yaml --output bundled.yaml
"""

import os
import yaml
import json
import argparse
from pathlib import Path
from typing import Dict, Any, Set, Optional
from urllib.parse import urlparse
import re

class OpenAPIBundler:
    """Python-based OpenAPI bundler без зависимости от redocly."""

    def __init__(self, base_path: Optional[Path] = None):
        self.base_path = base_path or Path.cwd()
        self.visited_refs: Set[str] = set()  # Для предотвращения circular refs
        self.bundled_components: Dict[str, Any] = {
            'schemas': {},
            'responses': {},
            'parameters': {},
            'requestBodies': {},
            'headers': {},
            'examples': {},
            'securitySchemes': {}
        }

    def bundle(self, input_file: Path, output_file: Path) -> bool:
        """Основной метод бандлинга."""
        print(f"[BUNDLER] Bundling {input_file} -> {output_file}")

        try:
            # Загружаем основную спецификацию
            with open(input_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f)

            print(f"[BUNDLER] Loaded spec with {len(spec)} top-level keys")

            # Обрабатываем все $ref ссылки
            bundled_spec = self._resolve_all_refs(spec, input_file.parent)

            if bundled_spec is None:
                print("[ERROR] _resolve_all_refs returned None")
                return False

            print(f"[BUNDLER] Bundled spec has {len(bundled_spec)} top-level keys")

            # Сохраняем результат
            with open(output_file, 'w', encoding='utf-8') as f:
                yaml.dump(bundled_spec, f, default_flow_style=False, allow_unicode=True)

            print(f"[BUNDLER] Successfully bundled to {output_file}")
            return True

        except Exception as e:
            print(f"[ERROR] Bundling failed: {e}")
            import traceback
            traceback.print_exc()
            return False

    def _resolve_all_refs(self, spec: Dict[str, Any], base_dir: Path) -> Dict[str, Any]:
        """Рекурсивно разрешаем все $ref ссылки."""
        if not isinstance(spec, dict):
            return spec

        resolved_spec = {}

        for key, value in spec.items():
            if key == '$ref':
                # Разрешаем ссылку
                resolved_value = self._resolve_ref(value, base_dir)
                # Заменяем $ref на resolved содержимое
                resolved_spec.update(resolved_value)
            elif isinstance(value, dict):
                resolved_spec[key] = self._resolve_all_refs(value, base_dir)
            elif isinstance(value, list):
                resolved_spec[key] = [
                    self._resolve_all_refs(item, base_dir) if isinstance(item, dict) else item
                    for item in value
                ]
            else:
                resolved_spec[key] = value

        return resolved_spec

    def _resolve_ref(self, ref: str, base_dir: Path) -> Dict[str, Any]:
        """Разрешаем одну $ref ссылку."""
        if not isinstance(ref, str):
            return ref

        if ref in self.visited_refs:
            raise ValueError(f"Circular reference detected: {ref}")

        self.visited_refs.add(ref)

        try:
            # Парсим ссылку
            if ref.startswith('#/'):
                # Локальная ссылка - не обрабатываем здесь
                return {'$ref': ref}
            elif ref.startswith('../') or ref.startswith('./') or (not ref.startswith('http') and '/' in ref):
                # Относительная или абсолютная файловая ссылка
                resolved_path = self._resolve_file_path(ref, base_dir)
                return self._load_external_file(resolved_path)
            else:
                # Другие ссылки возвращаем как есть
                return {'$ref': ref}

        finally:
            self.visited_refs.remove(ref)

    def _resolve_file_path(self, ref: str, base_dir: Path) -> Path:
        """Разрешаем файловый путь из $ref."""
        if ref.startswith('../') or ref.startswith('./'):
            # Относительный путь
            return (base_dir / ref).resolve()
        elif ref.startswith('proto/openapi/'):
            # Абсолютный путь от корня проекта
            return (self.base_path / ref).resolve()
        else:
            # Предполагаем относительный путь
            return (base_dir / ref).resolve()

    def _load_external_file(self, file_path: Path) -> Dict[str, Any]:
        """Загружаем и обрабатываем внешний файл."""
        if not file_path.exists():
            raise FileNotFoundError(f"Referenced file not found: {file_path}")

        with open(file_path, 'r', encoding='utf-8') as f:
            content = yaml.safe_load(f)

        # Если это components, извлекаем их
        if 'components' in content:
            components = content['components']
            # Рекурсивно разрешаем ссылки в компонентах
            resolved = self._resolve_all_refs(components, file_path.parent)
            return resolved if resolved is not None else components

        # Если это прямой объект схемы
        resolved = self._resolve_all_refs(content, file_path.parent)
        return resolved if resolved is not None else content


def main():
    parser = argparse.ArgumentParser(description='Python-based OpenAPI bundler')
    parser.add_argument('input', help='Input OpenAPI file')
    parser.add_argument('--output', '-o', required=True, help='Output bundled file')
    parser.add_argument('--base-path', help='Base path for resolving references')

    args = parser.parse_args()

    input_file = Path(args.input)
    output_file = Path(args.output)
    base_path = Path(args.base_path) if args.base_path else input_file.parent

    if not input_file.exists():
        print(f"[ERROR] Input file not found: {input_file}")
        return 1

    bundler = OpenAPIBundler(base_path)

    try:
        success = bundler.bundle(input_file, output_file)
        if success:
            print(f"[SUCCESS] Bundled specification saved to: {output_file}")
            return 0
        else:
            print("[ERROR] Bundling failed")
            return 1
    except Exception as e:
        print(f"[ERROR] {e}")
        return 1


if __name__ == '__main__':
    exit(main())
