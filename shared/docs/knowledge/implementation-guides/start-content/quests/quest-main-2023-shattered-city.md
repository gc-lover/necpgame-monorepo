# Основной квест: Разбитый город (2023)

**Статус:** review  
**Версия:** 2.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-10  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-10 00:30  
**api-readiness-notes:** Shooter-рефактор с подробными стадиями, взаимодействиями и API событиями. Документ готов для API-SWAGGER.

---

## 1. Сводка

- **ID:** `quest-main-2023-shattered-city`  
- **Выдаёт:** `npc-anna-petrova` (Santo Domingo)  
- **Регион/Период:** Santo Domingo → Watson / 2023 (первые недели после DataKrash)  
- **Петля:** зачистка + ремонт сетевого узла + защита конвоев  
- **Основные роли:** assault / tech / negotiation  

### Синопсис
После взрыва мэйнфрейма 2023-го линии связи South Watson обрушились. Анна Петрова просит восстановить ключевую линию питания, чтобы держать район в связи. Игрок балансирует между боем с местными бандами, переговорами с мародёрами и ремонтом узла под обстрелом.

---

## 2. Метаданные

```yaml
quest-type: main
difficulty: medium
party: solo (coop-2 support)
expected-duration: 30-40m
repeatable: once
tags:
  - engineering
  - urban-combat
  - negotiation
```

---

## 3. Стадии

### Стадия A — «Signal Hunt»
- **Цель:** очистить доступ к коммуникационной панели  
- **Локация:** заброшенные склады, Santo Domingo  
- **Угрозы:** мародёры, ловушки, нестабильные конструкции

| Интеракция | Порог | Оборудование | Эффект | События |
| --- | --- | --- | --- | --- |
| Recon Sweep | `perception_sensor ≥ 0.52`, `accuracy ≥ 0.50` | Дрон `scan-lite`, имплант `optic_suite` | Помечает ловушки, открывает stealth-маршрут, +small loot | `quest.recon.scan`, `analytics.loot` |
| Presence Pact | `presence ≥ 0.58`, `suppression_score ≤ 0.25` | 150 eddies, опционально имплант `voice_mod` | Снижает агрессию мародёров (−1 encounter), `quest.social.presence` | 
| Engineering Bypass | `handling ≥ 0.52`, `tech_override ≥ 0.48` | Инструменты теха | Отключает авто-турель, открывает ранний доступ к панели | `combat.shooter.override` |

### Стадия B — «Repair Under Fire»
- **Цель:** отремонтировать сеть, удерживая позицию  
- **Локация:** узел `grid-node-23`  
- **Угрозы:** волны бандитов (до 3), газовые выбросы

| Интеракция | Порог | Оборудование | Эффект |
| --- | --- | --- | --- |
| Field Repair Loop | `tech_override ≥ 0.55`, `analysis ≥ 0.53` | `toolkit`, имплант `servo-glove` | Ремонт линейки узла, сокращает длительность обороны на 45 сек | `quest.objective.progress`, `combat.shooter.override` |
| Support Shield | `support_efficiency ≥ 0.47`, `resilience ≥ 0.50` | Генератор `shield-dome`, ability `support-boost` | Создаёт купол укрытия (HP 750), спасает NPC инженера | `combat.shooter.support`, `analytics.damage` |
| Suppression Fire | `accuracy ≥ 0.56`, `stability ≥ 0.50` | AR/LMG | Сбрасывает волну врагов, открывает окно ремонта, снижает входящий урон | `combat.shooter.suppress` |

### Стадия C — «Convoy Escort»
- **Цель:** довести питание до Watson и отбить засаду  
- **Локация:** автодорога между Santo Domingo и Watson  
- **Угрозы:** блок-пост банды «Rust Tigers», дроны, радиация

| Интеракция | Порог | Оборудование | Эффект |
| --- | --- | --- | --- |
| Negotiation Toll | `presence ≥ 0.60`, `compliance ≥ 0.55` | 250 eddies | Пропускает через блок-пост без боя, но парк лишается части лута | `quest.social.presence` |
| Drone Hijack | `hacking ≥ 0.58`, `analysis ≥ 0.52` | Нэтдек, модуль `net-control` | Перехватывает боевого дрона, даёт временного союзника | `combat.shooter.hack`, `analytics.drone` |
| Hazard Routing | `mobility ≥ 0.52`, `stealth_rating ≥ 0.53` | Спец-резина, карта маршрутов | Обходит радиоактивный коридор; без расхода ресурсов | `environment.hazard.avoid` |

### Стадия D — «Aftermath»
- **Цель:** вернуть питание в район и получить награды  
- **Локация:** Watson relay station  
- **События:** финальный отчёт, экономический эффект, возможный хук на экономику (товары появляются на рынке)

---

## 4. Shooter encounters

### Encounter 1 — Warehouse Skirmish

| Враг | Роль | defenseRating | HP | Accuracy | DPS | Особенности |
| --- | --- | --- | --- | --- | --- | --- |
| Marauder SMG | assault | 180 | 250 | 0.52 | 140 | Smoke ×1, flank AI |
| Marauder Sniper | support | 195 | 220 | 0.60 | 160 | Headshot ×1.8, удерживает дистанцию |
| Turret Mk0 | static | 210 | 320 | 0.62 | 180 | Hackable (Engineering Bypass) |

- **AI:** атакующие давят с фланга, снайпер держит высоту, турель контролирует центр  
- **Loot:** 160 eddies, `item-components-basic ×2`, шанс `schematic-sensor-array`

### Encounter 2 — Node Defense (волновой)

| Волна | Состав | Особенности |
| --- | --- | --- |
| Wave1 | 3 Raiders | Ближний бой, оглушающие гранаты |
| Wave2 | 2 Gunners + Drone | Дрон требует Suppression/ Hack |
| Wave3 | Elite Captain | Shield, summons (cooldown 40s) |

- **Таймер:** 180 сек базово (минус 45 при успешном Field Repair Loop)  
- **Anticheat:** отслеживаем макросный огонь, latencies > 200 мс → предупреждение  
- **Loot:** 250 eddies, `item-mod-heat-sink`, шансы на `implant-servo-glove`

### Encounter 3 — Rust Tigers Ambush

| Враг | Роль | defenseRating | HP | Accuracy | DPS | Особенности |
| --- | --- | --- | --- | --- | --- | --- |
| Tiger Bruiser | heavy | 210 | 360 | 0.48 | 170 | Rush + knockdown |
| Tiger Gunner | assault | 200 | 310 | 0.55 | 180 | Suppression 0.35 |
| Attack Drone | harassment | 170 | 200 | 0.50 | 130 | Есть shield pulse, hackable |

- **Альтернатива:** Negotiation Toll → встреча отменяется, но −250 eddies и нет дропа  
- **Loot:** 300 eddies, `item-components-advanced`, шанс `weapon-mod-thermo`

---

## 5. Hazards

| Hazard | Триггер | Эффект | Контрмеры | События |
| --- | --- | --- | --- | --- |
| Радиационные канализации | Провал Hazard Routing | −12 HP/5 мин, иммунитет выживших падает | Рад-плащ, инженерный override | `environment.hazard.radiation` |
| Газовые скопления | Длинный бой > 90 сек | Accuracy −20%, vision blur | Выстрел по вентилю, Shield купол | `environment.hazard.gas` |
| Обрушения складов | Провал Recon Sweep | Stagger 4 сек, урон 30 HP | Recon Sweep, дистанция | `environment.hazard.collapse` |

---

## 6. Награды

### Базовые
- 540 XP  
- 600 eddies  
- `item-components-basic ×2`, `item-cyberdeck-parts ×1`  
- Репутация: Watson Civil Council +12

### Бонусные
- Engineering Bypass, Field Repair Loop, Drone Hijack — дополнительный лут (`schematic-network-node`, `implant-holo-lens`)  
- Negotiation Toll — экономит ресурсы, но уменьшает лут  
- Hazard Routing — бонусный `supply-cache` и позитивная репутация +3  
- Провал социалки → extra encounter + шанс `item-damaged-cache`

### Экономический эффект
- После завершения квеста открывается событие «Market Revival» (новые товары в магазине `vendor-watson-grid`)  
- Появляется доступ к цепочке `quest-main-2027-rebuild-protocol`

---

## 7. API и телеметрия

- `POST /api/v1/quests/{id}/accept`  
- `POST /api/v1/quests/{id}/interaction` (type, metrics, threshold, latency, outcome, resources)  
- `POST /api/v1/combat/shooter` (encounterId, snapshot, validation)  
- `POST /api/v1/environment/hazard` (kind, duration, mitigation)  
- `POST /api/v1/quests/{id}/objective` (progress, stage)  
- `POST /api/v1/quests/{id}/complete` (rewards, reputation, economy-delta)

**Kafka события**
- `combat.shooter.encounter`  
- `quest.progress.main`  
- `analytics.quest.economy`  
- `anti-cheat.validation` (latency, aim variance, macro check)

**Античит SLA**
- Латентность > 200 мс при defence → флаг для review  
- Aim variance < 0.02 с accuracy > 0.8 → macro check  
- Rapid fire > 9 clicks/s без импланта — предупреждение

---

## 8. Логи

- Shooter-интеракции: пороги, результаты, потреблённые ресурсы  
- Combat: TTK, попадания, крит %, suppression, импланты  
- Engineering: время ремонта, успешные overrides, паника NPC  
- Economy: израсходованные eddies, полученный profit, появление товара  
- Hazard: время нахождения, mitigations, HP потери  
- Narrative: выборы в диалогах, репутация, доступ к цепочкам

---

## 9. Тестирование

1. **Basic Clear:** успех большинства интеракций, стандартный бой на узле  
2. **Negotiation Path:** все переговоры успех → минимум боёв, проверка наград  
3. **Combat Heavy:** провал социалки и Engineering → максимум встреч, проверка баланса  
4. **Tech Carry:** акцент на Field Repair Loop + Drone Hijack → короткий таймер защиты  
5. **Hazard Fail:** игрок игнорирует радиацию → проверка штрафов и логирования  
6. **Coop Duo:** assault + tech (2 игрока) → тест синхронизации и распределения ролей

---

## 10. История изменений

- v2.0.0 (2025-11-10) — Shooter-рефактор, обновлены стадии, API, награды  
- v1.0.0 (2025-11-05) — D&D-версия (архив)  

