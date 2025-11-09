# Crafting System API

Эта директория содержит модульную OpenAPI-спецификацию для Crafting System API.

## Структура

- `crafting-system.yaml` — корневой файл OpenAPI c метаданными, тегами, секцией `components` и ссылками на пути.
- `paths/` — отдельные файлы для каждого endpoint.
- `schemas/index.yaml` — агрегированный файл схем, используемых API.

## Правила

- Следуем принципу API First и ограничению в 400 строк на файл.
- Общие ответы, пагинация и схемы безопасности подключаются из `api/v1/shared/common` через `$ref`.

