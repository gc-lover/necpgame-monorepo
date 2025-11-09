# 2025-11-09 — Audit MVP Backend/Gameplay Документов

**Ответственный:** Brain Manager  
**Цель:** Подтвердить готовность ключевых документов `.BRAIN` (MVP ядро) и зафиксировать, что именно надо передать дальше, без создания задач в других репозиториях.

---

## 1. Auth Service (`.BRAIN/05-technical/backend/auth/README.md`)
- **Статус:** ready (v1.0.1, 2025-11-08 23:56)  
- **Покрытие:** регистрация, логин, refresh/logout, password reset, OAuth, события.  
- **Архитектура:** microservice `auth-service`, порт 8081, REST + Kafka.  
- **Замечания:** документ полностью описывает MVP, доп. правок не требуется. Готов к постановке задачи после подтверждения.

## 2. Character Lifecycle (`.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`)
- **Статус:** ready (v1.1.0, 2025-11-09 00:14)  
- **Покрытие:** CRUD персонажей, слоты, restore, switching, активные события.  
- **Архитектура:** `character-service`, порт 8082, события `Character*`, интеграции с progression/inventory.  
- **Замечания:** все ограничения (combat lock, restore window, slot limits) сформулированы. Можно создавать задачи после отмашки.

## 3. Progression (`.BRAIN/05-technical/backend/progression-backend.md`)
- **Статус:** ready (v1.0.0, 2025-11-08 23:39)  
- **Покрытие:** экспа, level-up, skill XP, очки атрибутов/навыков, события.  
- **Архитектура:** `gameplay-service`, порт 8083, подписки на combat/quest, публикация `character:level-up`.  
- **Замечания:** описаны формулы и антиспам (points, cap). Готово к оформлению задач.

## 4. Inventory Core (`.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`)
- **Статус:** ready (v1.0.1, 2025-11-08 23:39)  
- **Покрытие:** backpack, equipment, stash, weight, stacking, binding, templates.  
- **Архитектура:** `economy-service`/`inventory` модуль (см. README), схемы БД и сервисный слой прописаны.  
- **Замечания:** Part 1 полностью покрывает MVP. Part 2 (advanced) вынесен отдельно.

## 5. Combat Session (`.BRAIN/05-technical/backend/combat-session-backend.md`)
- **Статус:** ready (v1.0.0, 2025-11-07 05:30)  
- **Покрытие:** создание инстансов боя, turn order, damage loop, логи, события.  
- **Архитектура:** `gameplay-service`, порт 8083, взаимодействия с character/economy/quest сервисами.  
- **Замечания:** есть workflow, БД, интеграции. Готово к постановке задачи.

## 6. Trade System (`.BRAIN/05-technical/backend/trade-system.md`)
- **Статус:** ready (v1.0.0, 2025-11-09 00:18)  
- **Покрытие:** P2P trade, double confirmation, history, антифрод, дистанция, события.  
- **Архитектура:** `economy-service`, порт 8085, связь с inventory/character/world.  
- **Замечания:** полный флоу, таймауты, аудит. Готово.

## 7. Combat Implants (`.BRAIN/02-gameplay/combat/combat-implants-types.md`)
- **Статус:** ready (v1.1.0, 2025-11-09 00:47)  
- **Покрытие:** типы имплантов, синергии, требования, интеграции.  
- **Архитектура:** `gameplay-service`, модуль `modules/combat/implants`.  
- **Замечания:** документ очищен от старых задач, готов для второй волны спецификаций.

## 8. Combat Combos (`.BRAIN/02-gameplay/combat/combat-combos-synergies.md`)
- **Статус:** ready (v1.0.0, 2025-11-09 00:45)  
- **Покрытие:** соло/командные комбо, синергии, матрицы, scoring.  
- **Архитектура:** `gameplay-service`, модуль `modules/combat/combos`.  
- **Замечания:** соответствует требованиям, доп. задач пока не заводим.

## 9. Combat AI Matrix (`.BRAIN/02-gameplay/combat/combat-ai-enemies.md`)
- **Статус:** ready (v1.0.0, 2025-11-09 00:55)  
- **Покрытие:** поведенческие слои NPC, Kafka/WebSocket, shooter-проверки, рейдовые сценарии.  
- **Архитектура:** `gameplay-service`, порт 8083, WebSocket `wss://api.necp.game/v1/gameplay/raid/{raidId}`, интеграции с world/social/economy.  
- **Замечания:** удалены устаревшие статусы, документ готов к постановке задач по AI и телеметрии.

## 10. Arena System (`.BRAIN/02-gameplay/combat/arena-system.md`)
- **Статус:** ready (v1.0.0, 2025-11-09 00:55)  
- **Покрытие:** карты арен, режимы, рейтинги, экономика, интеграции (voice lobby, лидерборды, античит).  
- **Архитектура:** `gameplay-service`, модули `modules/combat/arenas`, orchestrator через gRPC, Kafka `arena.match.state`.  
- **Замечания:** структура соответствует микросервисной карте, до постановки задач дополнительно ничего не требуется.

## 11. Shooter Core (`.BRAIN/02-gameplay/combat/combat-shooter-core.md`)
- **Статус:** in-progress (v0.1.0, 2025-11-09 15:15)  
- **Покрытие:** боевое ядро 3D-шутера (оружие, баллистика, recoil, suppression, anti-cheat).  
- **Архитектура:** `gameplay-service`, модуль `modules/combat/mechanics`, REST `/combat/shooter/*`, события `combat.shooter.*`.  
- **Замечания:** требуется детализация параметров, синхронизация с quest engine и обновление мероприятий вместо skill checks.

---

### Общие выводы
- Все ключевые MVP документы `.BRAIN` актуальны и соответствуют архитектурной схеме (микросервисы, порты, интеграции).  
- До получения явной команды не создаём задания в `API-SWAGGER`; держим список готовых документов в `06-tasks/queues/ready.md`.  
- Следующий шаг после подтверждения — оформить задания в API-SWAGGER пакетами (auth → characters → progression → inventory → combat session → trade, затем боевые расширения).


