#!/usr/bin/env python3
"""
Тест OpenAPI Analyzer
"""

import sys
import os
sys.path.append('scripts')

def test_openapi_analyzer():
    """Тест работы OpenAPI анализатора"""
    try:
        from openapi.openapi_analyzer import OpenAPIAnalyzer, EndpointAnalysis, SchemaAnalysis
        from core.config import ConfigManager
        from core.logger import Logger

        print("[OK] OpenAPI Analyzer импортирован успешно")

        # Создаем компоненты
        config = ConfigManager()
        logger = Logger(config).create_script_logger("test")

        # Создаем анализатор
        analyzer = OpenAPIAnalyzer(logger)
        print("[OK] OpenAPI Analyzer создан успешно")

        # Проверяем анализатор на тестовых данных
        test_spec = {
            "openapi": "3.0.3",
            "paths": {
                "/api/v1/users": {
                    "get": {
                        "operationId": "getUsers",
                        "responses": {"200": {"description": "OK"}}
                    },
                    "post": {
                        "operationId": "createUser",
                        "security": [{"BearerAuth": []}],
                        "responses": {"201": {"description": "Created"}}
                    }
                },
                "/api/v1/users/{id}": {
                    "get": {
                        "operationId": "getUser",
                        "parameters": [{"name": "id", "in": "path"}],
                        "responses": {"200": {"description": "OK"}}
                    }
                }
            },
            "components": {
                "securitySchemes": {"BearerAuth": {"type": "http", "scheme": "bearer"}},
                "schemas": {
                    "User": {
                        "type": "object",
                        "properties": {"id": {"type": "string"}, "name": {"type": "string"}},
                        "required": ["id"]
                    }
                }
            },
            "security": [{"BearerAuth": []}]
        }

        analysis = analyzer.analyze_spec(test_spec)
        print("[OK] Анализ спецификации выполнен успешно")

        # Проверяем результаты
        assert len(analysis.endpoints) == 3, f"Ожидалось 3 endpoints, получено {len(analysis.endpoints)}"
        assert analysis.needs_auth_middleware == True, "Должен требовать auth middleware"
        assert len(analysis.crud_entities) > 0, "Должен найти CRUD entities"
        assert analysis.service_type == "rest", "Должен определить REST service"

        print("[OK] Все проверки пройдены!")
        print(f"[RESULTS] Результаты анализа:")
        print(f"   - Endpoints: {len(analysis.endpoints)}")
        print(f"   - Schemas: {len(analysis.schemas)}")
        print(f"   - CRUD entities: {len(analysis.crud_entities)}")
        print(f"   - Service type: {analysis.service_type}")
        print(f"   - Needs auth: {analysis.needs_auth_middleware}")
        print(f"   - Estimated QPS: {analysis.estimated_qps}")

        return True

    except Exception as e:
        print(f"[ERROR] {e}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    success = test_openapi_analyzer()
    if success:
        print("\n[SUCCESS] OpenAPI Analyzer работает корректно!")
    else:
        print("\n[ERROR] OpenAPI Analyzer имеет проблемы!")
        sys.exit(1)

