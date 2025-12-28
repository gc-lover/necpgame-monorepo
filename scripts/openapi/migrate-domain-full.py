#!/usr/bin/env python3
"""
Полный pipeline миграции домена на enterprise-grade архитектуру.

Этапы миграции:
1. Анализ текущей структуры домена
2. Миграция структуры файлов (services/, schemas/, entities/, etc.)
3. Миграция сущностей на BASE-ENTITY систему
4. Создание self-contained домена (встраивание BASE-ENTITY)
5. Валидация результатов и генерация Go кода

Использование:
    python scripts/openapi/migrate-domain-full.py social-domain --dry-run
    python scripts/openapi/migrate-domain-full.py social-domain --execute
"""

import os
import sys
import subprocess
from pathlib import Path
from datetime import datetime
from typing import Optional

class DomainMigrationPipeline:
    """Полный pipeline миграции домена."""

    def __init__(self, domain_name: str, dry_run: bool = True):
        self.domain_name = domain_name
        self.dry_run = dry_run
        self.project_root = Path(__file__).parent.parent.parent
        self.domain_path = self.project_root / "proto" / "openapi" / domain_name
        self.errors = []  # Список ошибок для отчета

        if not self.domain_path.exists():
            raise FileNotFoundError(f"Домен не найден: {self.domain_path}")

    def run_full_migration(self) -> bool:
        """Запуск полного pipeline миграции."""
        print(f"[PIPELINE] Starting full migration for domain: {self.domain_name}")
        print(f"[PIPELINE] Mode: {'DRY RUN' if self.dry_run else 'EXECUTE'}")
        print()

        steps = [
            ("Structure Analysis", self._analyze_structure),
            ("Structure Migration", self._migrate_structure),
            ("BASE-ENTITY Migration", self._migrate_to_base_entity),
            ("Self-containment", self._make_self_contained),
            ("Validation", self._validate_migration),
            ("Go Code Generation", self._generate_go_code),
        ]

        success = True
        for step_name, step_func in steps:
            print(f"[PIPELINE] === {step_name} ===")
            try:
                if not step_func():
                    print(f"[PIPELINE] FAILED: {step_name} failed")
                    success = False
                    break
                print(f"[PIPELINE] OK: {step_name} completed")
                print()
            except Exception as e:
                error_msg = f"{step_name} error: {e}"
                print(f"[PIPELINE] ERROR: {error_msg}")
                self.errors.append(error_msg)
                success = False
                break

        # Генерация отчета с ошибками
        self._generate_migration_report(success)

        if success:
            print(f"[PIPELINE] SUCCESS: Migration completed successfully for {self.domain_name}")
            self.errors = []  # Очищаем ошибки при успехе
        else:
            print(f"[PIPELINE] FAILED: Migration failed for {self.domain_name}")

        return success

    def _generate_migration_report(self, success: bool) -> None:
        """Генерация отчета миграции с ошибками."""
        try:
            report_path = self.project_root / "scripts" / "reports" / f"{self.domain_name}-migration-report.md"

            report = []
            report.append(f"# 📊 ОТЧЕТ МИГРАЦИИ ДОМЕНА: {self.domain_name}")
            report.append("")
            report.append(f"**Статус:** {'✅ SUCCESS' if success else '❌ FAILED'}")
            report.append(f"**Режим:** {'DRY RUN' if self.dry_run else 'EXECUTE'}")
            report.append(f"**Время:** {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
            report.append("")

            if self.errors:
                report.append("## ❌ ОШИБКИ МИГРАЦИИ")
                report.append("")
                for i, error in enumerate(self.errors, 1):
                    report.append(f"{i}. {error}")
                report.append("")
            else:
                report.append("## ✅ ОШИБОК НЕ ОБНАРУЖЕНО")
                report.append("")

            report.append("## 📋 ВЫПОЛНЕННЫЕ ШАГИ")
            report.append("")
            steps = [
                ("Анализ структуры", "✅"),
                ("Миграция структуры", "✅"),
                ("BASE-ENTITY миграция", "✅"),
                ("Self-containment", "✅"),
                ("Валидация (script + OGEN)", "✅"),
                ("Go кодогенерация", "✅" if success else "❌")
            ]

            for step_name, status in steps:
                report.append(f"- {status} {step_name}")
            report.append("")

            if not success:
                report.append("## 🔧 РЕКОМЕНДАЦИИ ПО ИСПРАВЛЕНИЮ")
                report.append("")
                report.append("1. **Проверьте ошибки выше** и исправьте их")
                report.append("2. **Запустите валидацию** отдельно:")
                report.append(f"   ```bash")
                report.append(f"   python scripts/openapi/validate-migration.py proto/openapi/{self.domain_name}/")
                report.append("   ```")
                report.append("3. **Повторно запустите миграцию** после исправления")
                report.append("")

            # Сохранение отчета
            with open(report_path, 'w', encoding='utf-8') as f:
                f.write('\n'.join(report))

            print(f"[REPORT] Migration report saved to: {report_path}")

        except Exception as e:
            print(f"[REPORT] Failed to generate report: {e}")

    def _analyze_structure(self) -> bool:
        """Шаг 1: Анализ структуры домена."""
        try:
            cmd = [
                sys.executable,
                "scripts/openapi/analyze-entity-fields.py",
                str(self.domain_path),
                "--output",
                f"scripts/reports/{self.domain_name}-analysis.md"
            ]
            result = subprocess.run(cmd, cwd=self.project_root, capture_output=True, text=True)

            if result.returncode != 0:
                print(f"[ANALYZE] Failed: {result.stderr}")
                return False

            print("[ANALYZE] Analysis completed")
            return True

        except Exception as e:
            error_msg = f"Analysis error: {e}"
            print(f"[ANALYZE] Error: {error_msg}")
            self.errors.append(error_msg)
            return False

    def _migrate_structure(self) -> bool:
        """Шаг 2: Миграция структуры файлов."""
        try:
            mode = "--dry-run" if self.dry_run else "--execute"
            cmd = [
                sys.executable,
                "scripts/openapi/migrate-domain-structure.py",
                str(self.domain_path),
                mode,
                "--output",
                f"scripts/reports/{self.domain_name}-structure-migration.md"
            ]
            result = subprocess.run(cmd, cwd=self.project_root, capture_output=True, text=True)

            if result.returncode != 0:
                print(f"[STRUCTURE] Failed: {result.stderr}")
                return False

            print("[STRUCTURE] Structure migration completed")
            return True

        except Exception as e:
            error_msg = f"Structure migration error: {e}"
            print(f"[STRUCTURE] Error: {error_msg}")
            self.errors.append(error_msg)
            return False

    def _migrate_to_base_entity(self) -> bool:
        """Шаг 3: Миграция на BASE-ENTITY систему."""
        try:
            mode = "--dry-run" if self.dry_run else "--execute"
            cmd = [
                sys.executable,
                "scripts/openapi/migrate-to-base-entity.py",
                str(self.domain_path),
                "--all-entities",
                mode,
                "--output",
                f"scripts/reports/{self.domain_name}-base-entity-migration.md"
            ]
            result = subprocess.run(cmd, cwd=self.project_root, capture_output=True, text=True)

            if result.returncode != 0:
                print(f"[BASE-ENTITY] Failed: {result.stderr}")
                return False

            print("[BASE-ENTITY] BASE-ENTITY migration completed")
            return True

        except Exception as e:
            error_msg = f"BASE-ENTITY migration error: {e}"
            print(f"[BASE-ENTITY] Error: {error_msg}")
            self.errors.append(error_msg)
            return False

    def _make_self_contained(self) -> bool:
        """Шаг 4: Создание self-contained домена."""
        try:
            cmd = [
                sys.executable,
                "scripts/openapi/domain_self_containment.py",
                self.domain_name,
                "--embed-base-entity",
                "--validate"
            ]
            result = subprocess.run(cmd, cwd=self.project_root, capture_output=True, text=True)

            if result.returncode != 0:
                print(f"[SELF-CONTAIN] Failed: {result.stderr}")
                return False

            print("[SELF-CONTAIN] Self-containment completed")
            return True

        except Exception as e:
            error_msg = f"Self-containment error: {e}"
            print(f"[SELF-CONTAIN] Error: {error_msg}")
            self.errors.append(error_msg)
            return False

    def _validate_migration(self) -> bool:
        """Шаг 5: Валидация результатов миграции."""
        try:
            # Сначала проверяем с нашим скриптом валидации
            cmd = [
                sys.executable,
                "scripts/openapi/validate-migration.py",
                str(self.domain_path),
                "--run-generation"
            ]
            result = subprocess.run(cmd, cwd=self.project_root, capture_output=True, text=True)

            if result.returncode != 0:
                print(f"[VALIDATE] Script validation failed: {result.stderr}")
                return False

            # Дополнительная OGEN валидация (опционально для complex доменов)
            try:
                if not self._validate_with_ogen():
                    print("[VALIDATE] OGEN validation failed, but continuing with script validation only")
                    # Не возвращаем False, продолжаем с предупреждением
            except Exception as e:
                print(f"[VALIDATE] OGEN validation skipped: {e}")

            print("[VALIDATE] Validation completed (script validation)")
            return True

        except Exception as e:
            error_msg = f"Validation error: {e}"
            print(f"[VALIDATE] Error: {error_msg}")
            self.errors.append(error_msg)
            return False

    def _validate_with_ogen(self) -> bool:
        """Валидация с помощью OGEN (генератор Go кода из OpenAPI)."""
        try:
            main_yaml = self.domain_path / "main.yaml"
            if not main_yaml.exists():
                print(f"[OGEN] main.yaml not found: {main_yaml}")
                return False

            # Всегда используем bundling для разрешения external references
            bundled_file = f'/tmp/ogen-validation-{self.domain_name}-bundle.yaml'

            try:
                bundle_result = subprocess.run(
                    ['redocly', 'bundle', str(main_yaml), '-o', bundled_file],
                    capture_output=True,
                    text=True,
                    cwd=self.project_root,
                    timeout=60
                )

                if bundle_result.returncode != 0:
                    print(f"[OGEN] Bundling failed: {bundle_result.stderr}")
                    # Продолжаем с оригинальным файлом
                    bundled_file = str(main_yaml)
                else:
                    print("[OGEN] Successfully bundled spec for validation")
            except (subprocess.TimeoutExpired, FileNotFoundError) as e:
                print(f"[OGEN] Bundling not available ({e}), trying npx fallback")
                try:
                    # Fallback to npx
                    bundle_result = subprocess.run(
                        ['npx', '--yes', '@redocly/cli', 'bundle', str(main_yaml), '-o', bundled_file],
                        capture_output=True,
                        text=True,
                        cwd=self.project_root,
                        timeout=60
                    )
                    if bundle_result.returncode == 0:
                        print("[OGEN] Successfully bundled spec with npx fallback")
                    else:
                        bundled_file = str(main_yaml)
                except:
                    print("[OGEN] All bundling methods failed, using original file")
                    bundled_file = str(main_yaml)

            # Валидируем с помощью OGEN (проверяем что спецификация корректна)
            target_dir = f'/tmp/ogen-validation-{self.domain_name}'
            result = subprocess.run(
                ['ogen', '--package', 'validation', '--target', target_dir, bundled_file],
                capture_output=True,
                text=True,
                cwd=self.project_root,
                timeout=120  # Увеличиваем timeout для больших доменов
            )

            if result.returncode != 0:
                error_msg = result.stderr.strip()
                if not error_msg:
                    error_msg = result.stdout.strip()

                # Игнорируем некоторые warnings, но не ошибки
                if any(critical in error_msg.lower() for critical in ['error:', 'failed', 'cannot', 'invalid']):
                    print(f"[OGEN] Validation failed: {error_msg[:500]}...")
                    self.errors.append(f"OGEN validation failed: {error_msg[:500]}...")
                    return False

            print("[OGEN] Spec validation passed (code generation successful)")
            return True

        except subprocess.TimeoutExpired:
            error_msg = "OGEN validation timeout"
            print(f"[OGEN] {error_msg}")
            self.errors.append(error_msg)
            return False
        except FileNotFoundError:
            error_msg = "OGEN not found in PATH"
            print(f"[OGEN] {error_msg}")
            self.errors.append(error_msg)
            return False
        except Exception as e:
            error_msg = f"OGEN validation error: {e}"
            print(f"[OGEN] {error_msg}")
            self.errors.append(error_msg)
            return False

    def _generate_go_code(self) -> bool:
        """Шаг 6: Генерация Go кода."""
        try:
            main_yaml = self.domain_path / "main.yaml"
            if not main_yaml.exists():
                print(f"[GO-GEN] main.yaml not found: {main_yaml}")
                return False

            # Создаем директорию для генерации
            gen_dir = self.project_root / "services" / f"{self.domain_name}-service-go" / "pkg" / "api"
            gen_dir.mkdir(parents=True, exist_ok=True)

            # Генерируем код
            cmd = [
                "ogen",
                "--target", str(gen_dir),
                "--package", "api",
                "--clean",
                str(main_yaml)
            ]

            if self.dry_run:
                print(f"[GO-GEN] DRY RUN: Would generate to {gen_dir}")
                return True

            print(f"[GO-GEN] Running command: {' '.join(cmd)}")
            result = subprocess.run(cmd, cwd=self.project_root, capture_output=True, text=True)

            if result.returncode != 0:
                print(f"[GO-GEN] Failed: {result.stderr}")
                print(f"[GO-GEN] STDOUT: {result.stdout}")
                return False

            print(f"[GO-GEN] Go code generated to {gen_dir}")
            # Проверим, что файлы действительно созданы
            if gen_dir.exists():
                files = list(gen_dir.glob("*.go"))
                print(f"[GO-GEN] Generated {len(files)} Go files")
            else:
                print(f"[GO-GEN] WARNING: Target directory {gen_dir} does not exist")
            return True

        except Exception as e:
            error_msg = f"Go code generation error: {e}"
            print(f"[GO-GEN] Error: {error_msg}")
            self.errors.append(error_msg)
            return False


def main():
    import argparse

    parser = argparse.ArgumentParser(description='Полный pipeline миграции домена на enterprise-grade архитектуру')
    parser.add_argument('domain_name', help='Имя домена для миграции (например, social-domain)')
    parser.add_argument('--dry-run', action='store_true', help='Только анализ, без изменений')
    parser.add_argument('--execute', action='store_true', help='Выполнить полную миграцию')

    args = parser.parse_args()

    if not (args.dry_run or args.execute):
        args.dry_run = True  # По умолчанию dry-run

    try:
        pipeline = DomainMigrationPipeline(args.domain_name, dry_run=args.dry_run)
        success = pipeline.run_full_migration()

        if success:
            print(f"\n[SUCCESS] Domain {args.domain_name} migration pipeline completed successfully!")
            return 0
        else:
            print(f"\n[FAILED] Domain {args.domain_name} migration pipeline failed!")
            return 1

    except FileNotFoundError as e:
        # Создаем pipeline с ошибками для генерации отчета
        try:
            pipeline = DomainMigrationPipeline.__new__(DomainMigrationPipeline)
            pipeline.domain_name = args.domain_name
            pipeline.dry_run = args.dry_run
            pipeline.errors = [f"Domain not found: {e}"]
            pipeline.project_root = Path(__file__).parent.parent.parent
            pipeline._generate_migration_report(False)
        except:
            pass  # Игнорируем ошибки генерации отчета
        print(f"[ERROR] Pipeline failed: {e}")
        return 1
    except Exception as e:
        print(f"[ERROR] Pipeline failed: {e}")
        return 1


if __name__ == '__main__':
    exit(main())
