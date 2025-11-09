---

---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:14
**api-readiness-notes:** Добавлены модификаторы для боевых лодаутов и PvE экспедиций, расширены интеграции. Документ готов к ДУАПИТАСК.
---

# Live Events System - Динамика Найт-Сити

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07 20:33  
**Последнее обновление:** 2025-11-08 00:14  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Persistent live events framework  
**Размер:** ~300 строк ✅  
**target-domain:** gameplay-world  
**target-microservice:** world-service (8086)  
**target-frontend-module:** modules/world/events

---

## Краткое описание

Live Events — глобальные и локальные активности, меняющие состояние мира, экономики и социальных систем. События синхронизируются с календарём, лором и прогрессом игроков.

---

## Категории

- City-Wide Crisis
- Corporate Ops
- Social Festivals
- Underground Alerts
- Environmental Shifts
- Season Specials

---

## Жизненный цикл

1. Planning — запись в `live_events_plan`, назначение NPC
2. Announcement — новости, UI баннеры, push-уведомления
3. Active Phase — модификаторы систем, временные квесты
4. Resolution — награды, изменение лорных флагов
5. Archive — история, косметика

---

## Данные

`live_events_plan`, `live_event_progress`, `live_event_effects` — структура UUID, JSONB, временные метки

---

## Примеры 2025–2026

- Blackwall Surge — нестабильность сети, редкие Blackwall Shards
- Neon Bash Tournament — подпольный турнир Neopulse
- Corporate Divide — столкновение Arasaka и Militech

---

## Модификаторы систем

- Arena System — новые правила, двойные очки
- Loot Hunt — дополнительные эвенты (Corp Sweep)
- Dungeons — аффиксы, открытие Hard Mode
- Loadouts — динамические профили (stormbreaker/safebearer/scout/stormrunner), адаптация к событиям ARC, автоподбор комплектов
- Economy — изменение цен
- Social — уникальные эмоты, декор

---

## Интеграции

- Announcement System, Support Ticket, Maintenance Mode, Battle Pass, Achievement System

---

## Взаимодействие с боевыми лодаутами

- События публикуют `world.events.loadouts-modifier`, обновляющий рекомендации в `combat-loadouts-system.md`.
- ARC Raiders-подобные экспедиции задействуют `threatAdaptationProfile`, повышая требования к тегу `extraction-support` в `progression-skills-mapping.md`.
- В расписании рейдов помечаются обязательные комплекты (EMP, сенсоры, эвак-маяки), UI подсвечивает неподготовленные лодауты и предлагает быстрые пресеты.

---

## NPC и лор

- Кураторы: DJ Rix, Commander Imani, Naoko Sato, Hana Ito и др.
- Решения игроков влияют на будущие эвенты

---

## UX

- Виджеты с таймерами, голосовые объявления, веб-компаньон

---

## Аналитика

- Participation Rate, Retention Lift, Economy Impact, Voice Lobby Activity

---

## История изменений

- v1.1.0 (2025-11-08 00:14) — Добавлены модификаторы и интеграции с боевыми лодаутами, уточнены требования ARC Raiders-подобных экспедиций.
- v1.0.0 (2025-11-07 20:33) — Базовое описание системы лайв-эвентов.

---

## Готовность

- Категории, циклы, эффекты и интеграции описаны, документ готов к ДУАПИТАСК


