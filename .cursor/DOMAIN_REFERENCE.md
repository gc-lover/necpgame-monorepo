# [ROCKET] Справочник Enterprise—Grade Домены NECPGAME

**КРИТИЧНО:** Все агенты ДОЛЖНЫ использовать enterprise—grade домены для новой архитектуры!

## [FORBIDDEN] EMOJI AND SPECIAL CHARACTERS ЗАПРЕТ

**КРИТИЧНО:** Запрещено использовать эмодзи и специальные Unicode символы в коде!

### Почему запрещено:
- [FORBIDDEN] Ломают выполнение скриптов на Windows
- [FORBIDDEN] Могут вызывать ошибки в терминале
- [FORBIDDEN] Создают проблемы с кодировкой
- [FORBIDDEN] Нарушают совместимость между ОС

### Что use вместо:
- [OK] `:smile:` вместо [EMOJI]
- [OK] `[FORBIDDEN]` вместо [FORBIDDEN]
- [OK] `[OK]` вместо [OK]
- [OK] `[ERROR]` вместо [ERROR]
- [OK] `[WARNING]` вместо [WARNING]

### Автоматическая проверка:
- Pre-commit hooks блокируют коммиты с эмодзи
- Git hooks проверяют staged файлы
- Исключения: `.cursor/rules/*` (документация), `.githooks/*`

---

## [WIN] МИРОВОЙ РЕКОРД ПО ОРГАНИЗАЦИИ API

- **Всего файлов:** 1,037 API спецификаций
- **Уровень организации:** 100% (все файлы в логических доменах)
- **Enterprise-grade архитектура:** Fortune 500 уровень

---

## [SYMBOL] КОНСОЛИДИРОВАННЫЕ ДОМЕНЫ (5 основных)

### [BUILDING] **system-domain** (553 файла)
**Назначение:** Enterprise-grade системная инфраструктура
**Модули:**
- `admin/` - Администрирование и управление
- `ai/` - ИИ и NPC системы (465 файлов!)
- `analytics/` - Аналитика и метрики
- `circuit-breakers/` - Система защиты
- `messaging/` - Очереди сообщений
- `network/` - Сетевая инфраструктура
- `paths/` - Маршрутизация
- `services/` - Сервис orchestration
- `support/` - Служба поддержки
- `sync/` - Синхронизация данных

**API:** `proto/openapi/system-domain/main.yaml`
**Examples использования:** Health checks, metrics, logging, monitoring

### [TARGET] **specialized-domain** (157 файлов)
**Назначение:** Продвинутые игровые механики и специализации
**Модули:**
- `combat/` - Боевые системы (30 файлов)
- `crafting/` - Крафт и производство
- `effects/` - Специальные эффекты
- `interactive/` - Интерактивные объекты
- `logistics/` - Логистика и транспорт
- `mechanics/` - Механики оружия
- `meta/` - Мета-механики
- `misc/` - Различные специализации (37 файлов)
- `movement/` - Движение и стелс
- `npc/` - NPC системы (12 файлов)
- `schemas/` - Общие схемы (33 файла)
- `services/` - Специализированные сервисы

**API:** `proto/openapi/specialized-domain/main.yaml`
**Examples использования:** Combat systems, crafting, NPC AI, weapon mechanics

### [PEOPLE] **social-domain** (91 файл)
**Назначение:** Социальные взаимодействия и групповой геймплей
**Модули:**
- `chat/` - Чат системы (10 файлов)
- `guilds/` - Гильдии (23 файла)
- `mentorship/` - Менторинг
- `notifications/` - Уведомления (9 файлов)
- `orders/` - Заказы и контракты
- `parties/` - Группы (7 файлов)
- `relationships/` - Отношения
- `reputation/` - Репутация
- `social/` - Социальные функции
- `voice-lobby/` - Голосовой чат

**API:** `proto/openapi/social-domain/main.yaml`
**Examples использования:** Parties, guilds, chat, social interactions

### [MONEY] **economy-domain** (31 файл)
**Назначение:** Экономические системы и торговля
**Модули:**
- `advertising/` - Реклама и маркетинг
- `analytics/` - Экономическая аналитика
- `auctions/` - Аукционы
- `contracts/` - Контракты
- `core/` - Основная экономика
- `dividends/` - Дивиденды
- `integration/` - Интеграции
- `protection/` - Защита
- `trading/` - Торговля (14 файлов)

**API:** `proto/openapi/economy-domain/main.yaml`
**Examples использования:** Trading, auctions, contracts, investments

### [WORLD] **world-domain** (57 файлов)
**Назначение:** Игровой мир, окружение и география
**Модули:**
- `advanced/` - Продвинутый геймплей (23 файла)
- `cities/` - Города
- `combat/` - Бои в мире
- `continents/` - Континенты
- `events/` - Мировые события (3 файла)
- `planets/` - Планеты
- `politics/` - Политика
- `sync/` - Синхронизация мира (17 файлов)

**API:** `proto/openapi/world-domain/main.yaml`
**Examples использования:** Cities, continents, world events, politics

---

## [TARGET] СПЕЦИАЛИЗИРОВАННЫЕ ДОМЕНЫ (10 дополнительных)

| Домен | Файлы | Назначение | API |
|-------|-------|------------|-----|
| **security-domain** | 48 | Безопасность и защита | `proto/openapi/security-domain/main.yaml` |
| **inventory-domain** | 39 | Инвентарь и предметы | `proto/openapi/inventory-domain/main.yaml` |
| **tournament-domain** | 68 | Турниры и соревнования | `proto/openapi/tournament-domain/main.yaml` |
| **content-domain** | 55 | Игровой контент | `proto/openapi/content-domain/main.yaml` |
| **cyberpunk-domain** | 27 | Киберпанковские механики | `proto/openapi/cyberpunk-domain/main.yaml` |
| **faction-domain** | 27 | Фракции и корпорации | `proto/openapi/faction-domain/main.yaml` |
| **auth-expansion-domain** | 20 | Расширенная аутентификация | `proto/openapi/auth-expansion-domain/main.yaml` |
| **progression-domain** | 18 | Прогрессия персонажей | `proto/openapi/progression-domain/main.yaml` |
| **arena-domain** | 12 | Арены и PvP | `proto/openapi/arena-domain/main.yaml` |
| **referral-domain** | 10 | Реферальные системы | `proto/openapi/referral-domain/main.yaml` |
| **legacy-domain** | 13 | Устаревшие API | `proto/openapi/legacy-domain/main.yaml` |
| **misc-domain** | 9 | Утилиты и вспомогательные | `proto/openapi/misc-domain/main.yaml` |
| **cosmetic-domain** | 8 | Косметические предметы | `proto/openapi/cosmetic-domain/main.yaml` |
| **integration-domain** | 4 | Внешние интеграции | `proto/openapi/integration-domain/main.yaml` |

---

## [GAME] ВЫБОР ДОМЕНА ПО ТИПУ ЗАДАЧИ

### API Designer
- **System services** → `system-domain`
- **Game mechanics** → `specialized-domain`
- **Social features** → `social-domain`
- **Economy** → `economy-domain`
- **World features** → `world-domain`
- **Security** → `security-domain`

### Backend Developer
- **Генерация кода:** `python scripts/generate-all-domains-go.py` (генерирует все домены)
- **Валидация:** `python scripts/validate-domains-openapi.py`
- **Performance:** Следовать domain-specific оптимизациям
- **Integration:** Использовать domain APIs для межсервисного взаимодействия

### Architect
- **Структура:** Проектировать сервисы в контексте enterprise-grade доменов
- **Performance:** Учитывать domain-specific нагрузку и требования
- **Integration:** Планировать взаимодействие между доменами

### Database Engineer
- **Schemas:** Оптимизировать таблицы для domain-specific queries
- **Indexes:** Создавать covering indexes для hot paths доменов
- **Partitioning:** Применять domain-aware partitioning стратегии

### Content Teams
- **Квесты:** `specialized-domain` (gameplay mechanics)
- **NPC:** `specialized-domain` (NPC systems)
- **Диалоги:** `social-domain` (social interactions)

---

## [FAST] ГЕНЕРАЦИЯ КОДА ИЗ ДОМЕНОВ

### Backend: Генерация из доменов
```bash
# Генерация из enterprise-grade домена
npx --yes @redocly/cli bundle proto/openapi/{domain}/main.yaml -o openapi-bundled.yaml
ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Examples:
ogen --target pkg/api --package api --clean proto/openapi/system-domain/main.yaml
ogen --target pkg/api --package api --clean proto/openapi/specialized-domain/main.yaml
```

### API Designer: Создание в доменах
```bash
# Создавать модули в enterprise-grade доменах
proto/openapi/{domain}/{module}/main.yaml  # <1000 строк
```

---

## [SYMBOL] ПРАВИЛА РАБОТЫ С ДОМЕНАМИ

### [OK] ОБЯЗАТЕЛЬНО:
- Все новые API создавать в enterprise-grade доменах
- Использовать domain-specific генерацию кода
- Следовать domain boundaries (не смешивать ответственности)
- Обновлять domain main.yaml при добавлении модулей
- Валидировать domain APIs перед передачей

### [ERROR] ЗАПРЕЩЕНО:
- Создавать файлы вне enterprise-grade доменов
- Игнорировать domain boundaries
- Создавать duplicate функциональность в разных доменах
- Не обновлять main.yaml домена

### [SEARCH] ВАЛИДАЦИЯ:
```bash
# Проверка доменной структуры
redocly lint proto/openapi/{domain}/main.yaml

# Проверка генерации кода
ogen --target /tmp/test --package test --clean proto/openapi/{domain}/main.yaml
```

---

## [SYMBOL]️ ENTERPRISE ACHIEVEMENTS

- [OK] **100% организация** - все файлы в логических доменах
- [OK] **Enterprise-grade архитектура** - Fortune 500 уровень
- [OK] **Domain-driven design** - четкие границы ответственности
- [OK] **Scalable** - готово к enterprise росту
- [OK] **Developer-friendly** - мгновенная навигация

**[WIN] ЭТА АРХИТЕКТУРА ПРЕДСТАВЛЯЕТ МИРОВОЙ РЕКОРД ПО ОРГАНИЗАЦИИ API!**

---

**СПРАВОЧНИК ОБНОВЛЯЕТСЯ:** При добавлении новых доменов или модулей обновлять этот файл!
