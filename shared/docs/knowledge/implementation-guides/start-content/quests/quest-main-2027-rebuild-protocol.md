---
**Статус:** review  
**Версия:** 2.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-10 01:30  
**Приоритет:** высокий  
**api-readiness:** in-review  
**api-readiness-check-date:** 2025-11-10 01:30  
**api-readiness-notes:** Shooter-рефактор в процессе согласования с quest-engine и analytics. Требуется финальная валидация параметров и событийных потоков.
---

# Основной квест: Протокол восстановления (2027)

## 1. Сводка
- **ID:** `quest-main-2027-rebuild-protocol`
- **Выдаёт:** `npc-elizabeth-chen` (NetWatch HQ, Downtown)
- **Период:** 2027 — восстановление сетей после DataKrash
- **Основная петля:** восстановление узлов связи под огнём + оборона
- **Ключевые роли:** tech support, assault, hacking

## 2. Контекст и цели
1. Получить задание у Элизабет Чен.
2. Добраться до слепой зоны NetWatch и восстановить три узла связи.
3. Защитить восстановленные узлы от рейда «Rust Tigers».
4. Вернуться и отчитаться о восстановлении сети.

### Метрики прогресса
```yaml
quest-type: main
party: coop-2 (поддержка до 4)
expected-duration: 35-45m
repeatable: once
telemetry-topics:
  - combat.shooter.encounter
  - quest.progress.main
  - analytics.network.restore
```

## 3. Стадии и shooter-интеракции

### Стадия A — «Blind Zone Recon»
- **Локация:** industrial outskirts Santo Domingo
- **Цель:** обследовать область, обнаружить угрозы

| Событие | Требования | Ресурсы | Эффект | Логи |
| --- | --- | --- | --- | --- |
| Recon Sweep | `accuracy ≥ 0.54`, `perception_sensor ≥ 0.55` | Дрон `scan-mk2`, имплант `optic_suite` | Помечает засады, открывает stealth-маршрут | `quest.recon.scan`, `analytics.heatmap` |
| Presence Brief | `presence ≥ 0.58`, `suppression_score ≤ 0.25` | 180 eddies, имплант `voice_mod` | Переговоры с мародёрами → −1 encounter, +репутация Civilians +4 | `quest.social.presence`, `combat.shooter.social` |
| Hazard Marking | `analysis ≥ 0.52`, `mobility ≥ 0.48` | Тех-инструменты | Обходит ловушки, снижает урон от hazards на 30% | `environment.hazard.tag` |

### Стадия B — «Node Bring-Up»
- **Локация:** NetWatch relay cluster `NW-23`
- **Цель:** восстановить узлы 1–3, удерживая линию связи

| Узел | Интеракция | Порог | Оснащение | Результат |
| --- | --- | --- | --- | --- |
| Node 1 | Field Repair Loop | `tech_override ≥ 0.56`, `handling ≥ 0.53` | `servo-glove`, `engineering_toolkit` | Сокращает время восстановления на 45 сек, открывает bonus XP | `quest.objective.progress` |
| Node 2 | Defensive Hacking | `hacking ≥ 0.58`, `analysis ≥ 0.55` | Дек `net-control`, имплант `neuro-weave` | Снижает волну рейда на 2 врага, активирует авто-турель | `combat.shooter.override` |
| Node 3 | Support Stabilizer | `support_efficiency ≥ 0.47`, `resilience ≥ 0.50` | Генератор `shield-dome` | Создаёт щит на 900 HP, защищает инженера NPC | `combat.shooter.support` |

### Стадия C — «Rust Tigers Raid»
- **Локация:** временный лагерь у relay `NW-23`
- **Цель:** отразить атаку, сохранить uptime ≥ 85%

| Событие | Требования | Ресурсы | Эффект |
| --- | --- | --- | --- |
| Suppression Grid | `suppression_score ≥ 0.40`, `accuracy ≥ 0.55` | AR/LMG, ability `suppression_burst` | Замедляет рейдеров, снижает входящий урон на 20% | `combat.shooter.suppress` |
| Drone Hijack | `hacking ≥ 0.60`, `analysis ≥ 0.58` | Модуль `net-overclock` | Перехватывает боевого дрона → союзный огонь 45 сек | `combat.shooter.hack` |
| Evac Route Call | `mobility ≥ 0.52`, `presence ≥ 0.55` | Связь с NetWatch | Вызывает резервный отряд (delay 60 сек) → ускоряет завершение | `quest.signal.request` |

### Стадия D — «Uplink Confirmed»
- **Цель:** зафиксировать восстановление сети и вернуться к Элизабет
- **Интеракции:** финальный отчёт, вычисление экономического эффекта, триггер новых заданий NetWatch

## 4. Боевые сценарии

### Encounter 1 — Rust Tigers Vanguard
| Враг | Роль | defenseRating | HP | Accuracy | DPS | Особенности |
| --- | --- | --- | --- | --- | --- | --- |
| Tiger Gunner | assault | 205 | 320 | 0.56 | 175 | Smoke ×1, suppression 0.35 |
| Tiger Technician | support | 195 | 260 | 0.50 | 130 | EMP гранаты (disrupt shields) |
| Tiger Melee | bruiser | 210 | 360 | 0.42 | 160 | Rush + knockdown, устойчив к suppression |

### Encounter 2 — Drone Reinforcements (если провал Recon Sweep)
| Враг | Роль | defenseRating | HP | Accuracy | DPS | Особенности |
| --- | --- | --- | --- | --- | --- | --- |
| Attack Drone Mk2 | harassment | 180 | 210 | 0.58 | 140 | Перегревает технику, требует Drone Hijack |
| Turret Drop Pod | static | 220 | 350 | 0.62 | 190 | Активируется через 30 сек, можно отключить Engineering Override |

## 5. Hazards & Environment
| Hazard | Условие | Эффект | Контрмера | События |
| --- | --- | --- | --- | --- |
| Электрические дуги | Перегрузка узлов (>85% load) | 18 HP/тик, перегрев оборудования | Field Repair Loop, shield-dome | `environment.hazard.arc` |
| Газовые выбросы | Бой > 120 сек | Accuracy −20%, visibility | Vent override, поддержка дрона | `environment.hazard.gas` |
| Sniper Overwatch | Провал Recon Sweep | Target lock, крит 2× | Smoke deploy, suppression | `combat.shooter.sniper` |

## 6. Награды и последствия
- **База:** 560 XP, 650 eddies, `item-cyberdeck-netwatch-mk1`, репутация NetWatch +18.
- **Бонусы:**
  - Field Repair Loop успех → +70 XP, дополнительный loot cache.
  - Defensive Hacking успех → вводит `auto-turret blueprint`.
  - Suppression Grid успех → повышает шанс редкого оружия (AR Tactical Mk1).
- **Штрафы:** падение uptime < 70% → −15% награды, повтор Stage B.
- **Продолжение:** открывает цепочку `quest-main-2030-network-resurgence`, даёт доступ к NetWatch store tier 2.

## 7. API и телеметрия
- `POST /api/v1/quests/{questId}/accept`
- `POST /api/v1/quests/{questId}/interaction` — `{ interactionId, metrics, outcome, latency, resources }`
- `POST /api/v1/combat/shooter` — отчёт по встречам (encounterId, snapshot, validation)
- `POST /api/v1/network/restore` — статус узлов, uptime, hazards
- `POST /api/v1/quests/{questId}/objective` — прогресс узлов (1..3)
- `POST /api/v1/quests/{questId}/complete`

**WebSocket:** `wss://api.necp.game/v1/quests/{questId}/events` → события repair/raid/telemetry.  
**Kafka:** `quest.progress.main`, `combat.shooter.encounter`, `analytics.network.restore`, `anti-cheat.validation`.

## 8. Тест-кейсы
1. **Tech перфект:** успех всех engineering/hacking проверок, минимум врагов.
2. **Combat heavy:** провал Recon + Defensive Hacking → максимум рейдов, проверка выживаемости.
3. **Support focus:** Shield-дом и Suppression Grid удерживают инженера NPC.
4. **Failure recovery:** Defeat → повтор Stage B, проверка логов и наказаний.
5. **Latency spike:** симуляция задержки > 220 мс, проверка anti-cheat уведомления.

## 9. Логи и аналитика
- Shooter метрики: accuracy, stability, suppression, shields uptime.
- Repair telemetry: время на узел, материалы, расход энергии.
- Economic impact: новые заказы NetWatch, изменение цен на tech-компоненты.
- Anti-cheat: rapid fire, macro detection, coordinate spoofing.

## 10. История изменений
- v2.0.0 (2025-11-10) — Shooter-рефактор, обновлены стадии, API, награды.
- v1.0.0 (2025-11-06) — D&D версия (архив).

