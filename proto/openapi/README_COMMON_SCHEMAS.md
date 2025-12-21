# Общие схемы OpenAPI - Руководство по использованию

## Обзор

В проекте NECPGAME используется централизованная система общих схем для обеспечения консистентности API между всеми доменами.

## Структура файлов

```
proto/openapi/
├── common-schemas.yaml          # Основные общие схемы
├── [domain-name]/               # Специфические домены
│   └── [service-name]/
│       └── main.yaml            # Спецификация сервиса
```

## Как ссылаться на общие схемы

### Правильные пути

Все домены находятся на одном уровне вложенности, поэтому для ссылки на `common-schemas.yaml` используйте:

```yaml
$ref: ../../common-schemas.yaml#/components/schemas/Error
```

### Примеры использования

#### Error схема
```yaml
responses:
  '404':
    description: Resource not found
    content:
      application/json:
        schema:
          $ref: ../../common-schemas.yaml#/components/schemas/Error
```

#### UUID схема
```yaml
properties:
  id:
    $ref: ../../common-schemas.yaml#/components/schemas/UUID
```

#### Timestamp поля
```yaml
properties:
  created_at:
    $ref: ../../common-schemas.yaml#/components/schemas/CreatedAt
  updated_at:
    $ref: ../../common-schemas.yaml#/components/schemas/UpdatedAt
```

## Доступные общие схемы

### Базовые типы
- `UUID` - стандартный UUID формат
- `Timestamp` - ISO 8601 timestamp
- `Email` - email формат
- `URL` - URL формат

### Общие структуры
- `Error` - универсальная схема ошибок
- `BaseEntity` - базовая сущность с ID и timestamps
- `PaginationParams` - параметры пагинации
- `PaginationResponse` - ответ с пагинацией

### Специфические
- `Status` - enum статусов
- `Metadata` - дополнительная информация
- `CreatedAt`, `UpdatedAt` - timestamp поля

## Инструменты

### Исправление ссылок
```powershell
.\scripts\fix-common-refs.ps1
```

### Проверка валидности
```bash
npx @redocly/cli lint proto/openapi/[domain]/[service]/main.yaml
```

### Bundling
```bash
npx @redocly/cli bundle proto/openapi/[domain]/[service]/main.yaml -o bundle.yaml
```

### Генерация Go кода
```bash
ogen --target pkg/api --package api --clean bundle.yaml
```

## Лучшие практики

1. **Всегда используйте общие схемы** вместо создания локальных копий
2. **Проверяйте bundling** после изменений ссылок
3. **Тестируйте генерацию Go кода** для новых спецификаций
4. **Используйте скрипт исправления** при массовых изменениях

## Troubleshooting

### Ошибка "Can't resolve $ref"
- Проверьте правильность пути: `../../common-schemas.yaml`
- Убедитесь, что файл `common-schemas.yaml` существует в корне `proto/openapi/`

### Неправильные пути после копирования
- Запустите `.\scripts\fix-common-refs.ps1` для автоматического исправления

### Конфликты схем
- Используйте уникальные имена для локальных схем
- Общие схемы имеют приоритет над локальными

## Контакты

При проблемах с общими схемами обращайтесь к API Designer агенту.
