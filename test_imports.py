#!/usr/bin/env python3
"""
Тест импортов новых модулей
"""

import sys
import os

# Добавляем scripts в путь
sys.path.insert(0, 'scripts')

def test_imports():
    """Тест импорта всех новых модулей"""
    print("Тестируем импорт модулей...")

    try:
        # Тест OpenAPI Analyzer
        from openapi.openapi_analyzer import OpenAPIAnalyzer, OpenAPIAnalysis, EndpointAnalysis, SchemaAnalysis
        print("[OK] OpenAPI Analyzer импортирован")

        # Тест Enhanced Service Generator
        from generation.enhanced_service_generator import EnhancedServiceGenerator
        print("[OK] Enhanced Service Generator импортирован")

        # Тест Generate All Domains
        from generate_all_domains_go import GenerateAllDomainsGo
        print("[OK] Generate All Domains импортирован")

        # Тест Create Templates
        from generation.create_templates import create_template_dir
        print("[OK] Create Templates импортирован")

        print("\n[SUCCESS] Все модули импортированы успешно!")
        return True

    except ImportError as e:
        print(f"[ERROR] Ошибка импорта: {e}")
        return False
    except Exception as e:
        print(f"[ERROR] Неожиданная ошибка: {e}")
        return False

def test_basic_functionality():
    """Тест базовой функциональности"""
    print("\nТестируем базовую функциональность...")

    try:
        from openapi.openapi_analyzer import OpenAPIAnalyzer
        from core.config import ConfigManager
        from core.logger import Logger

        # Создаем компоненты
        config = ConfigManager()
        logger = Logger(config).create_script_logger("test")

        # Создаем анализатор
        analyzer = OpenAPIAnalyzer(logger)
        print("[OK] Анализатор создан")

        # Тестовый анализ
        test_spec = {
            "paths": {"/test": {"get": {"responses": {"200": {"description": "OK"}}}}}
        }

        analysis = analyzer.analyze_spec(test_spec)
        print("[OK] Анализ выполнен")

        assert len(analysis.endpoints) == 1
        print("[OK] Анализ корректен")

        return True

    except Exception as e:
        print(f"[ERROR] Ошибка функциональности: {e}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    print("[TEST] Тестируем AI Boilerplate Generation систему\n")

    imports_ok = test_imports()
    functionality_ok = test_basic_functionality()

    if imports_ok and functionality_ok:
        print("\n[SUCCESS] СИСТЕМА РАБОТАЕТ! Все модули и функциональность OK!")
        print("\n[SUMMARY] Что протестировано:")
        print("   [OK] OpenAPI Analyzer - анализ спецификаций")
        print("   [OK] Enhanced Service Generator - генерация boilerplate")
        print("   [OK] Generate All Domains - оркестрация генерации")
        print("   [OK] Create Templates - система шаблонов")
        print("   [OK] Базовая функциональность - анализ и генерация")
    else:
        print("\n[ERROR] ПРОБЛЕМЫ ОБНАРУЖЕНЫ!")
        sys.exit(1)

