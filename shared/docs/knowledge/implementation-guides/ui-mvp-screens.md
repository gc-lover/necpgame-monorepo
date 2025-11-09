# UI MVP Screens — Спецификация

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** UI Guild  
**Связанные документы:** `ui-registration.md`, `ui-character-creation.md`, `ui-game-start.md`, `ui-main-game.md`

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** Все экраны MVP имеют описанные данные, состояния и события. Готово для постановки задач во `FRONT-WEB` и `BACK-JAVA`.

---

## 1. Цель

- Зафиксировать перечень экранов MVP текстовой версии.
- Связать UI требуемые данные с REST/Kafka контрактами.
- Обозначить статусы готовности и владельцев.

## 2. Перечень экранов

| Экран | Описание | Данные | Источники | Ready |
|-------|----------|--------|-----------|-------|
| Регистрация | Форма входа/регистрации, OAuth | `POST /auth/register`, `POST /auth/login` | auth-service | ✅ |
| Выбор сервера | Список серверов, пинг, населения | `GET /infrastructure/servers` | infrastructure-service | ✅ |
| Создание персонажа | Характеристики, происхождение, класс | `GET /characters/templates`, `POST /characters` | character-service | ✅ |
| Туториал (Intro) | Пошаговое обучение, первые квесты | `GET /quests/tutorial`, `POST /quests/progress` | quest-service | ✅ |
| Основной HUD | Карта, журнал, навыки, чат | `GET /world/state`, WebSocket `world.unrest.updates` | world-service, communication-service | ✅ |
| Combat Overlay | Боевой UI, статы оружия | `POST /combat/ballistics/simulate`, Kafka `combat.ballistics.events` | gameplay-service | ✅ |
| Orders Board | Социальные заказы | `GET /social/orders`, `POST /social/orders/{id}/applications`, Kafka `social.orders.lifecycle` | social-service | ✅ |
| Crafting Workshop | Крафт модов, очередь | `GET /crafting/blueprints`, `POST /crafting/jobs`, Kafka `economy.crafting.jobs` | economy-service | ✅ |
| Dialogue UI | Ветки диалогов, выбор решений | `GET /dialogues/{id}`, `POST /dialogues/choice` | narrative-service | ✅ |
| Inventory | Экипировка, моды | `GET /inventory`, `POST /inventory/equip` | inventory-service | ✅ |

## 3. Состояния и события

- Каждому экрану присвоены обязательные состояния (`loading`, `empty`, `ready`, `error`).
- Реальное время: HUD, Combat, Orders используют WebSocket/ Kafka подписки.
- Транзакции: Crafting и Orders требуют подтверждения банков через escrow (`economy-service`).

## 4. UX артефакты

- Макеты в Figma: `MVP_UI_2025_November.fig` (см. UI канбан).
- ASCII прототипы: в связанных файлах (`ui-main-game/ui-hud-core.md`, `ui-game-start/login-screen.md`).

## 5. Чек-лист готовности

- [x] Прописаны экраны MVP и статусы готовности.
- [x] Сведены зависимости от REST/Kafka.
- [x] Обновлены связи с `2025-11-08-gameplay-backend-sync.md` для Orders/Combat.
- [ ] Передать список экранов в `FRONT-WEB` roadmap (owner: UI Guild Lead).

---

**Следующее действие:** синхронизировать с `FRONT-WEB` backlog и включить экраны в sprint 2025-11-10.

