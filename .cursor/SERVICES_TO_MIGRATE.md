# Список сервисов для миграции на ogen-go

**Обновлено:** 2025-12-04  
**Статус:** Большинство сервисов имеют ogen код, но handlers не обновлены

---

## ✅ Полностью мигрированы (4 сервиса)

- ✅ `combat-actions-service-go`
- ✅ `combat-ai-service-go`
- ✅ `combat-damage-service-go`
- ✅ `achievement-service-go`

**Reference:** `services/combat-combos-service-ogen-go/` (полный пример)

---

## 🚧 Приоритет 1: In Progress (код сгенерирован, handlers нужны)

### Combat Services (13 сервисов)
- [ ] `combat-extended-mechanics-service-go` - код есть, handlers нужны
- [ ] `combat-hacking-service-go` - код есть, handlers нужны
- [ ] `combat-sessions-service-go` - код есть, handlers нужны
- [ ] `combat-turns-service-go` - код есть, handlers нужны
- [ ] `combat-implants-core-service-go` - код есть, handlers нужны
- [ ] `combat-implants-maintenance-service-go` - код есть, handlers нужны
- [ ] `combat-implants-stats-service-go` - код есть, handlers нужны
- [ ] `combat-sandevistan-service-go` - код есть, handlers нужны
- [ ] `projectile-core-service-go` - код есть, handlers нужны
- [ ] `hacking-core-service-go` - код есть, handlers нужны
- [ ] `gameplay-weapon-special-mechanics-service-go` - проверить
- [ ] `weapon-progression-service-go` - проверить
- [ ] `weapon-resource-service-go` - проверить

### Movement & World (5 сервисов)
- [ ] `movement-service-go` - проверить
- [ ] `world-service-go` - проверить
- [ ] `world-events-analytics-service-go` - проверить
- [ ] `world-events-core-service-go` - проверить
- [ ] `world-events-scheduler-service-go` - проверить

### Quest Services (5 сервисов)
- [ ] `quest-core-service-go` - проверить
- [ ] `quest-rewards-events-service-go` - проверить
- [ ] `quest-skill-checks-conditions-service-go` - проверить
- [ ] `quest-state-dialogue-service-go` - проверить
- [ ] `gameplay-progression-core-service-go` - проверить

### Chat & Social (9 сервисов)
- [ ] `chat-service-go` - код есть, handlers нужны
- [ ] `social-chat-channels-service-go` - проверить
- [ ] `social-chat-commands-service-go` - проверить
- [ ] `social-chat-format-service-go` - проверить
- [ ] `social-chat-history-service-go` - проверить
- [ ] `social-chat-messages-service-go` - проверить
- [ ] `social-chat-moderation-service-go` - проверить
- [ ] `social-player-orders-service-go` - проверить
- [ ] `social-reputation-core-service-go` - код есть, handlers нужны

### Core Gameplay (13 сервисов)
- [ ] `leaderboard-service-go` - проверить
- [ ] `league-service-go` - проверить
- [ ] `loot-service-go` - проверить
- [ ] `gameplay-service-go` - проверить
- [ ] `progression-experience-service-go` - проверить
- [ ] `progression-paragon-service-go` - проверить
- [ ] `battle-pass-service-go` - проверить
- [ ] `seasonal-challenges-service-go` - проверить
- [ ] `companion-service-go` - проверить
- [ ] `cosmetic-service-go` - проверить
- [ ] `housing-service-go` - проверить
- [ ] `mail-service-go` - проверить
- [ ] `referral-service-go` - проверить

### Character Engram (5 сервисов)
- [ ] `character-engram-compatibility-service-go` - проверить
- [ ] `character-engram-core-service-go` - проверить
- [ ] `character-engram-cyberpsychosis-service-go` - проверить
- [ ] `character-engram-historical-service-go` - проверить
- [ ] `character-engram-security-service-go` - проверить

### Stock/Economy (12 сервисов)
- [ ] `stock-analytics-charts-service-go` - проверить
- [ ] `stock-analytics-tools-service-go` - проверить
- [ ] `stock-dividends-service-go` - проверить
- [ ] `stock-events-service-go` - проверить
- [ ] `stock-futures-service-go` - проверить
- [ ] `stock-indices-service-go` - проверить
- [ ] `stock-integration-service-go` - проверить
- [ ] `stock-margin-service-go` - проверить
- [ ] `stock-options-service-go` - код есть
- [ ] `stock-protection-service-go` - код есть
- [ ] `economy-service-go` - проверить
- [ ] `trade-service-go` - код есть

### Admin & Support (12 сервисов)
- [ ] `admin-service-go` - проверить
- [ ] `support-service-go` - проверить
- [ ] `maintenance-service-go` - код есть
- [ ] `feedback-service-go` - проверить
- [ ] `clan-war-service-go` - проверить
- [ ] `faction-core-service-go` - проверить
- [ ] `reset-service-go` - код есть
- [ ] `client-service-go` - проверить
- [ ] `realtime-gateway-go` ⚠️ (check protocol - может быть protobuf)
- [ ] `ws-lobby-go` ⚠️ (check protocol - может быть protobuf)
- [ ] `voice-chat-service-go` ⚠️ (check protocol - может быть protobuf)

---

## ❌ Приоритет 2: Not Started (нужна полная миграция)

**Эти сервисы имеют ogen код, но нужна проверка handlers:**

- [ ] `combat-combos-service-go` - код есть, но есть отдельная версия `-ogen-go`
- [ ] `matchmaking-go` - проверить (есть `matchmaking-service-go`)
- [ ] `economy-player-market-service-go` - код есть (`handlers_ogen.go`)
- [ ] `character-service-go` - проверить
- [ ] `matchmaking-service-go` - код есть
- [ ] `inventory-service-go` - код есть (`handlers_ogen.go`)
- [ ] `party-service-go` - код есть
- [ ] `social-service-go` - код есть (`handlers_ogen.go`, `http_server_ogen.go`)

---

## 📋 План миграции

### Этап 1: Завершить In Progress (67 сервисов)
**Фокус:** Обновить handlers для сервисов с уже сгенерированным ogen кодом

1. **Combat Services** (13) - HIGH PRIORITY
2. **Movement & World** (5) - HIGH PRIORITY
3. **Quest Services** (5)
4. **Chat & Social** (9)
5. **Core Gameplay** (13)
6. **Character Engram** (5)
7. **Stock/Economy** (12)
8. **Admin & Support** (12)

### Этап 2: Полная миграция (8 сервисов)
**Фокус:** Сервисы с частичной миграцией или без ogen кода

---

## 🎯 Промпт для агента

```
Рефакторинг сервиса {service-name} на ogen-go.

Роль: Backend Developer
Документация:
- .cursor/OGEN_MIGRATION_GUIDE.md
- .cursor/ogen/02-MIGRATION-STEPS.md
- .cursor/CODE_GENERATION_TEMPLATE.md

Reference: services/combat-combos-service-ogen-go/

Требования:
1. Если ogen код уже есть → обновить handlers на typed responses
2. Если ogen кода нет → сгенерировать код и мигрировать handlers
3. Реализовать SecurityHandler
4. Создать benchmarks
5. Валидировать: /backend-validate-optimizations #{issue}

Issue: #{number}
```

---

## 📊 Статистика

- **Полностью мигрированы:** 4/86 (4.7%)
- **In Progress (код есть):** ~67 сервисов
- **Not Started:** ~8 сервисов
- **Всего:** ~86 сервисов

---

## ⚠️ Важно

1. **Проверь протокол** для:
   - `realtime-gateway-go` - может быть protobuf
   - `ws-lobby-go` - может быть protobuf
   - `voice-chat-service-go` - может быть protobuf

2. **См. `.cursor/PROTOCOL_SELECTION_GUIDE.md`** перед миграцией этих сервисов

3. **Reference implementation:** `services/combat-combos-service-ogen-go/`

