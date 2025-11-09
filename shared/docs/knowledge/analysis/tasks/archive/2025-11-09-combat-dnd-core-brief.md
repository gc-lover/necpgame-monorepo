# Combat Shooter Core — Transition Plan

**Приоритет:** critical  
**Статус:** draft  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 14:50 (обновлено после отмены D&D)  
**Связанный документ:** _будет создан_ `.BRAIN/02-gameplay/combat/combat-shooter-core.md`

---

## Контекст
- Решение: отказаться от D&D-проверок и кубиков в пользу полноценного 3D-шутера (первый этап — web/webGL версия, затем перенос в UE5).
- Все документы, связанные с D&D (`combat-dnd-core`, `combat-dnd-integration-shooter`, `combat-dnd-mechanics-integration`, `attributes-dnd-mapping`), помечены как `deprecated` и возвращены в статус `needs-work`.
- Требуется новая спецификация, описывающая боевую петлю, баллистику, поведение оружия, сетевую синхронизацию и интеграцию с существующими подсистемами.

---

## Цели
1. Подготовить документ `combat-shooter-core.md`, который заменит D&D ядро.
2. Обновить связанные пакеты (`combat-wave`, `quest-engine`, `progression`, `economy`, `analytics`) — очистить ссылки на кубики.
3. Обеспечить совместимость с текущим roadmap: web-версия (WebGL/WebGPU) → UE5.

---

## REST / Gameplay backlog (новая модель)
| Endpoint | Описание | Приоритет |
| --- | --- | --- |
| `POST /combat/shooter/fire` | Регистрация выстрела (оружие, позиция, направление, тип пули) | P0 |
| `POST /combat/shooter/hit` | Подтверждение попадания (серверный авторитет, урон, хитбокс) | P0 |
| `POST /combat/shooter/reload` | Процессы перезарядки, тип магазина, скорость | P0 |
| `POST /combat/shooter/recoil` | Обновление отдачи и рассеивания (персонаж, модификаторы) | P1 |
| `GET /combat/shooter/weapon-stats` | Каталог оружия и параметров (TTK, баллистика) | P1 |
| `POST /combat/shooter/ability-modifiers` | Применение способностей/имплантов к оружию и стрельбе | P1 |
| `POST /combat/shooter/suppress` | События подавления (suppression) | P2 |

---

## WebSocket / Streaming
- `wss://api.necp.game/v1/combat/shooter/session/{sessionId}` — realtime события стрельбы, попаданий, убийств.
- `wss://api.necp.game/v1/combat/shooter/muzzle-trails/{sessionId}` — опциональный канал для визуализации трассеров/эффектов.

---

## Event Bus
- `combat.shooter.fire` / `combat.shooter.hit` / `combat.shooter.kill`.
- `combat.shooter.recoil-updated`, `combat.shooter.suppress`, `combat.shooter.reload`.
- Входящие события: `combat.session.events` (позиции игроков), `combat.abilities`, `combat.freerun`, `combat.stealth`, `quest.trigger`.

---

## Storage
- `shooter_weapon_catalog` — параметры оружия (тип, урон, скорострельность, точность, падение).
- `shooter_fire_log` — журнал выстрелов (оружие, пуля, позиция, latency).
- `shooter_hit_log` — попадания и нанесённый урон.
- `shooter_modifiers` — активные бафы/дебафы от имплантов/способностей.
- `shooter_telemetry` — агрегаты для аналитики (TTK, accuracy, heatmaps).

---

## Зависимости
- `combat-abilities`, `combat-implants`, `combat-combos`, `combat-freerun`, `combat-stealth`.
- `quest-engine` (теперь использует shooter events вместо D&D skill checks).
- `analytics-service` (реальная телеметрия боя, без кубиков).
- `economy-service` (стоимость оружия, боеприпасов, апгрейдов).
- `session-service` / `anti-cheat` (серверная авторитетность, защиты).

---

## Следующие шаги
1. Создать `combat-shooter-core.md` (описание классов оружия, баллистики, хитбоксов, damage model).
2. Обновить `combat-wave-package` и связанные брифы — убрать упоминания D&D, добавить shooter endpoints.
3. Подготовить таски в `TODO.md`/`ready.md` на основе новой спецификации.
4. Провести ревизию квестов/контента: заменить разделы “D&D параметры” на shooter-ориентированные требования (хитбоксы, stealth detection, weapon tiers).

---

## История
- 2025-11-09 15:05 — документ переформатирован в план перехода на шутер после отказа от D&D.

