# Основной квест: Красный рассвет (2040)

**Статус:** review  
**Версия:** 2.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-10  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-10 00:30  
**api-readiness-notes:** Переписан под shooter-механику. Включены таблицы взаимодействий, баллистика, сетевые SLA и античит-события. Готов к подготовке API задач.

---

## 1. Кратко

- **ID:** `quest-main-2040-red-dawn`  
- **Тип:** main (early mid-game)  
- **Выдаёт:** `npc-marco-fix` (Watson Hub)  
- **Сеттинг:** Red Era — руины Downtown (web) → перенос в UE5 без изменений игровых правил  
- **Основная петля:** rescue + escort → extraction  
- **Параллельные роли:** assault / support / recon  

### Синопсис
2040 год: после DataKrash районы Downtown превратились в «красную» зону. Марко «Фикс» Санчес просит вывести группу выживших, пока радиация и банды не добили их. Игрок принимает решение — провести отряд через руины, удерживая скавов, настраивая защиту от радиации и поддерживая раненых.

---

## 2. Метаданные

```yaml
city: Night-City
region: Watson-Downtown
period: 2040-2060 (Red Era)
quest-type: main
difficulty: medium
party: coop-2-4 (solo допустимо)
expected-duration: 35-45m
repeatable: once
```

---

## 3. Структура стадий

### Стадия A — «Briefing & Prep»
- **Цель:** получить задачу, подготовить экипировку от радиации  
- **Локация:** Watson Fixers Hub  
- **Угрозы:** отсутствуют; таймер мягкий (7 мин)  
- **Shooter-интеракции**

| Событие | Порог (0–1) | Оснащение | Результат | Логи |
| --- | --- | --- | --- | --- |
| Presence Briefing | `presence ≥ 0.55` | Имплант `voice_mod` | Скидка на расходники (−15% eddies) | `combat.shooter.social`, `quest.choice` |
| Loadout Check | `handling ≥ 0.50` | `engineering_toolkit` | Бесплатный `item-rad-protection (temp)` | `inventory.grant`, `quest.objective-progress` |

### Стадия B — «Into the Red Zone»
- **Цель:** дойти до позиции выживших  
- **Локация:** Подземные туннели и дворы Downtown  
- **Угрозы:** радиация, patrol encounters

| Событие | Порог | Оснащение | Результат |
| --- | --- | --- | --- |
| Engineering Override | `handling ≥ 0.55`, `tech_override ≥ 0.50` | Инструментальный набор | Активирует старые экраны → −2 врага на Encounter #2, +20% XP, `combat.shooter.override` |
| Recon Sweep | `perception_sensor ≥ 0.55`, `accuracy ≥ 0.55` | Дрон `scan-mk2` | Выявляет патрули, открывает stealth-маршрут, `quest.recon.scan` |
| Scav Negotiation | `presence ≥ 0.60`, `suppression_score ≤ 0.20` | 200 eddies | Пропускает бой «Скавы», +`quest.social.presence`, без наград за бой |

### Стадия C — «Rescue & Escort»
- **Цель:** стабилизировать раненого и вывести группу  
- **Локация:** Разрушенное административное здание → эвакуационный коридор  
- **Shooter-интеракции**

| Событие | Порог | Оснащение | Результат |
| --- | --- | --- | --- |
| Field Triage | `support_efficiency ≥ 0.45`, `resilience ≥ 0.50` | `medkit_standard`, ability `support-heal` | Карлос может идти, +50 XP, +5 reputation, `quest.support.success` |
| Suppression Cover | `accuracy ≥ 0.58`, `stability ≥ 0.50` | AR/SMG, имплант `gyro` | Обеспечивает безопасный коридор, снижает урон группы, `combat.shooter.suppress` |
| Stealth Extract | `stealth_rating ≥ 0.58`, `mobility ≥ 0.52` | Плащи `thermo-cloak` | Обход Encounter #3 (банда), +stealth бонус лута, `quest.stealth.success` |

### Стадия D — «Extraction & Report»
- **Цель:** довести группу до safe zone, отчитаться Марко  
- **Локация:** Эвакуационный лифт → Watson Hub  
- **События:**  
  - Assault вариант (если провал Stealth)  
  - Альтернативный маршрут (радиационный коридор, −50 HP)  
  - Финальный диалог, выдача наград

---

## 4. Диалоговые опорные узлы

| Узел | Контекст | Ключевые ветки | Shooter связь |
| --- | --- | --- | --- |
| `marco_intro` | Брифинг | `accept`, `decline`, `prep` | Presence Briefing снижает стоимость расходников |
| `scav_contact` | Подвал, встреча со скавами | `negotiate`, `attack`, `bypass` | Связан с Scav Negotiation / Encounter A |
| `survivor_meet` | Здание выживших | `reassure`, `triage`, `rush` | Field Triage; влияет на скорость escort |
| `band_blockade` | Финальный коридор | `fight`, `stealth_route`, `radiation_detour` | Dictates Encounter B vs hazard |
| `marco_wrap` | Возврат к фиксеру | `reward`, `future_hook` | Фиксация наград, события `quest.complete` |

Диалоги оформляются по shooter-шаблону: лаконичные реплики + флаги `quest.choice`/`quest.branch`.

---

## 5. Shooter encounters

### Encounter A — Scav Patrol (обязательный / stealth отменяет)

| Враг | Роль | defenseRating | HP | Accuracy | DPS | Особенности |
| --- | --- | --- | --- | --- | --- | --- |
| Scav Gunner | mid-range | 185 | 280 | 0.54 | 150 | Smoke ×1, suppression 0.25 |
| Scav Bruiser | melee | 180 | 260 | 0.38 | 120 | Bleed 15%, rush |

- **Инициатива:** `reaction_time = max(0.35, 0.7 × REF – neuro_mod)`  
- **AI:** Gunner держит дистанцию, Bruiser пытается выйти из фланга  
- **Loot:** 50 eddies, `item-electronics-standard`, 30% `item-medkit-basic`

### Encounter B — Gang Blockade (финал, можно обойти)

| Враг | Роль | defenseRating | HP | Accuracy | DPS | Особенности |
| --- | --- | --- | --- | --- | --- | --- |
| Gang Leader | commander | 205 | 340 | 0.58 | 190 | Smoke/flash, call reinforcements (cooldown 30s) |
| Gang Shooter ×2 | assault | 198 | 300 | 0.55 | 170 | Voice командование, suppression 0.35 |
| Support Drone | utility | 150 | 160 | 0.45 | 120 | Shield pulse, hackable |

- **Комбо:** при успехе Stealth Extract нагрузка падает до Leader + 1 Shooter  
- **Loot:** 220 eddies, `item-components-advanced`, шанс 20% на `mod-recoil-delta`  
- **Телеметрия:** `combat.shooter.encounter`, `analytics.damage`, `anti-cheat.aim`  

---

## 6. Hazards & Environment

| Hazard | Условие | Эффект | Избежать / смягчить | События |
| --- | --- | --- | --- | --- |
| Радиация красной зоны | Открытые улицы, >5 мин | −10 HP / 5 мин | `item-rad-protection`, Engineering Override | `environment.hazard.radiation` |
| Обрушение перекрытий | Провал Recon Sweep или шум > 0.7 | −30 HP, stagger 3с | Предупреждающие mark’и, дрон `scan-mk2` | `environment.hazard.collapse` |
| Газовые выбросы | Подвалы, при длинном бою | Accuracy −20%, vision blur | Стрелять в клапаны, активировать вентиляцию | `environment.hazard.gas` |

---

## 7. Награды

### Базовые
- 520 XP  
- 520 eddies  
- `item-components-basic ×1`  
- Репутация: Independent +10

### Бонусы
- Engineering Override — +20% XP, шанс `item-mod-cooling`  
- Field Triage — +50 XP, доп. репутация +5  
- Stealth Extract — +`item-cache-stealth`, шанс `schematic-rad-shield`  
- Negotiation — экономия 200 eddies, но без боевого лута

### Отрицательные последствия
- Провал Field Triage → Карлос замедляет отряд (−10% скорость escort)  
- Провал Negotiation → доп. патруль на выходе  
- Хард-таймер радиации (<3 мин) → штраф −5 Reputation (Independent)

---

## 8. API, сети и античит

- **REST**
  - `POST /api/v1/quests/{id}/accept`
  - `POST /api/v1/quests/{id}/interaction` (payload: type, metrics, threshold, latency, outcome)
  - `POST /api/v1/combat/shooter`
  - `POST /api/v1/environment/hazard`
  - `POST /api/v1/quests/{id}/objective`
  - `POST /api/v1/quests/{id}/complete`

- **WebSocket** `wss://gameplay/quests/{id}/events` — нотификации escort, hazard, extraction
- **Kafka Topics**
  - `combat.shooter.encounter`
  - `quest.progress.main`
  - `analytics.quest.red-era`
  - `anti-cheat.shooter.validation`

- **Античит SLA**
  - Latency spike > 180 ms → предупредительный лог (`anti-cheat.latency`)  
  - Aim variance < 0.02 при accuracy > 0.8 → проверка макроса  
  - Rapid-fire (>8 clicks/s) без импланта → `anti-cheat.macro` событие

---

## 9. Логи и телеметрия

- Shooter-интеракции: threshold, фактические метрики, latency, выбор альтернатив  
- Combat telemetry: попадания, крит-урон, suppression, использование имплантов  
- Escort состояние: HP выживших, скорость, события паники  
- Hazard журнал: радиация, обрушения, газ, успешные Overrides  
- Решения игрока: negotiation, triage, stealth/assault, payout  
- Пост-боевая аналитика: TTK, ammo usage, damage taken

---

## 10. Тест-кейсы

1. **Golden Path:** Успех всех интеракций → минимум боёв, escort без ранений  
2. **Combat Focus:** Провал социалки + радиация → проверка балансировки Encounter B  
3. **Stealth Route:** Успешный Recon Sweep и Stealth Extract → обход финального боя  
4. **Support Carry:** Field Triage + Presence Negotiation → минимум потерь, проверка latency  
5. **Failure Recovery:** Смерть игрока/разрыв соединения во время escort → восстановление checkpoint  
6. **Coop Scaling:** 4 игрока, смешанные роли (assault/support/recon) → проверка синхронизации

---

## 11. История изменений

- v2.0.0 (2025-11-10) — Полный рефактор под shooter-модель  
- v1.0.0 (2025-11-05) — Исходная версия (D&D checks, принята в архив)

