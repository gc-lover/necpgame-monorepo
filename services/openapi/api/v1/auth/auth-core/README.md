## Auth Core API

- `auth-core.yaml` — основной контракт auth-service: регистрация, login/logout, refresh, email/пароль/2FA, OAuth и управление ролями.
- `auth-core-models.yaml` — базовые схемы аккаунта, токенов, 2FA, JWT дескрипторов и вспомогательных параметров.
- `auth-core-models-operations.yaml` — структуры запросов/ответов, event payloadы `auth.*` и интеграционные модели.

Файлы укладываются в лимит ≤ 400 строк благодаря разбиению спецификации.

