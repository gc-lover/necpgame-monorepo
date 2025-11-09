---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-10 00:15  
**api-readiness-notes:** Shooter-шаблон для побочных квестов. Содержит структуру узлов, shooter-интеракций, сетевых и античит-требований. Готов к использованию для подготовки документов в API-SWAGGER.
---

# Shooter Quest Template — Побочный квест (Web → UE5)

**Статус:** template  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-10  
**Приоритет:** высокий

---

## 1. Метаданные

```yaml
quest-id: CITY-PERIOD-###
city: [Город]
region: [Регион]
period: [2020-2029 | 2030-2039 | 2040-2060 | 2061-2077 | 2078-2093]
quest-type: [side | faction | daily | weekly | event | social | dynamic]
difficulty: [easy | medium | hard | extreme]
party: [solo | coop-2-4 | squad-5-10]
expected-duration: [15-30m | 30-60m | 1-2h]
repeatable: [once | repeatable | daily | weekly]
```

---

## 2. Кратко

- **Название:** [Короткое имя квеста]  
- **Синопсис (2–3 предложения):** [Основная конфигурация]  
- **Выдающий NPC / событие:** [Имя, локация]  
- **Основная петля:** [escort | assault | infiltration | retrieval | rescue]  
- **Ключевые особенности:** [Shooter mechanics, stealth/hacking альтернативы, социальные узлы]

---

## 3. Стадии квеста

Для каждой стадии описываем цели, окружение, угрозы и shooter-интеракции.

### Стадия 1: [Название]
- **Цель:** [Что нужно сделать]  
- **Локация:** [Город / Подзона / Инстанс]  
- **Угрозы:** [Враги, окружение, hazards]  
- **Shooter-интеракции:** (таблица)

| Событие | Требования (метрики ≥ 0–1) | Оснащение / ресурсы | Результат | Логи |
| --- | --- | --- | --- | --- |
| Presence Negotiation | `presence ≥ 0.60`, `suppression_score ≤ 0.20` | 200 eddies или имплант `voice_mod` | Пропускает бой, снижает тревогу | `combat.shooter.social`, `quest.choice` |
| Engineering Override | `handling ≥ 0.55`, `tech_override ≥ 0.50` | `engineering_toolkit`, имплант `tech-suite` | Активация укрытий, −враги, доп. XP | `combat.shooter.override`, `quest.objective-progress` |
| Recon Sweep | `perception_sensor ≥ 0.55`, `accuracy ≥ 0.55` | Дрон `scan-mk2` / имплант `optic_suite` | Открывает тайники, альтернативы | `quest.recon.scan`, `analytics.loot` |

*(добавьте/измените строки под нужные события)*

### Стадия 2: [Название]
- **Цель:**  
- **Shooter-интеракции:**  
- **Альтернативы:** stealth / hacking / social (описать условия)

### Стадия 3: [Название]
- **Финал / эвакуация / extraction:**  
- **Boss encounter / defense wave:** параметры врагов, `defenseRating`, HP, `accuracy`, `suppression`.  
- **Telemetry:** список `combat.shooter.*`, `quest.*`, `analytics.*` событий.

---

## 4. Узлы диалогов и решений

### Диалоговый узел N
- **Сцена / NPC:**  
- **Выборы:** перечислить реплики  
- **Связанные shooter-интеракции:**  
  - [Интеракция]: порог, эффект, лог  
- **Последствия:** ветки, разблокировки, штрафы  
- **Latency SLA:** макс. задержка для подтверждения выбора (если онлайн событие)

---

## 5. Shooter боевые сценарии

### Encounter A
- **Состав врагов:** (таблица)

| Враг | Роль | defenseRating | HP | Accuracy | DPS | Способности |
| --- | --- | --- | --- | --- | --- | --- |
| Scav Gunner | mid-range | 185 | 280 | 0.54 | 150 | Smoke ×1, suppression 0.25 |
| Scav Melee | bruiser | 180 | 260 | 0.38 | 120 | Bleed 15%, rush |

- **Инициатива / реакция:** `reaction_time = f(REF, имплант)`  
- **Тактика AI:** [Фланг, удержание, использование окружения]  
- **Loot Table:** [Пул наград + проценты]

### Encounter B
- Аналогично описать второй сценарий (при провале/альтернативе).

---

## 6. Hazards & Environment

| Hazard | Условие активации | Урон / эффект | Как избежать | События |
| --- | --- | --- | --- | --- |
| Радиация | Открытые руины | −10 HP / 5 мин | `rad_shield`, успех engineering | `environment.hazard.radiation` |
| Обрушение | Провал `perception_sensor ≥ 0.52` | −30 HP, stagger | Recon Sweep, safety drones | `environment.hazard.collapse` |

---

## 7. Награды и прогресс

### Базовые награды
- XP: [значение]  
- Валюта: [ediies / токены]  
- Лут: [список предметов]

### Бонусы за шоотер-интеракции
- Engineering Override: [бонусы, события]  
- Field Triage: [XP, репутация]  
- Presence Negotiation: [экономия ресурсов]  
- Recon Sweep: [доп. лут]

### Репутация и фракции
- `faction-id`: +/- значение  
- Открываемые цепочки / магазины / активности

---

## 8. API & Телеметрия

- REST:  
  - `POST /api/v1/quests/{id}/accept`  
  - `POST /api/v1/quests/{id}/interaction` (type, threshold, metrics, outcome, latency)  
  - `POST /api/v1/combat/shooter` (encounterId, snapshot, validation)  
  - `POST /api/v1/environment/hazard`  
  - `POST /api/v1/quests/{id}/objective` (progress)  
  - `POST /api/v1/quests/{id}/complete`

- WebSocket: realtime события `ws://.../quests/{id}/events` (опционально)  
- Events (Kafka): `combat.shooter.*`, `quest.progress.*`, `analytics.quest.*`  
- Anti-cheat: параметры валидации (макросы, latency spikes, aim variance)

---

## 9. Тест-кейсы (MVP)

1. **Перфектный ран:** все shooter-интеракции успешны → минимальные бои, максимум наград.  
2. **Боевой путь:** провал социалки/stealth → все столкновения, проверка баланса.  
3. **Stealth-only:** обход основного боя; проверка detection и наград.  
4. **Support focus:** активация Field Triage + escort, проверка latency и синхронизации.  
5. **Failure recovery:** смерть игрока / потеря соединения → проверка восстановлений.

---

## 10. История изменений

- v1.0.0 (2025-11-10) — Создан shooter-шаблон для побочных квестов  

---

## Сопутствующие документы

- `../quest-system.md` — общая система квестов  
- `../../02-gameplay/combat/combat-shooter-core.md` — ядро боевой системы  
- `../../02-gameplay/combat/combat-stealth-brief.md` — стелс механики  
- `../../05-technical/backend/quest-engine-backend.md` — сервис квестов  
- `../../06-tasks/config/readiness-tracker.yaml` — статус документов

