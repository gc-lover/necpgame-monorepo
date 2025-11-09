---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:21
**api-readiness-notes:** Перепроверено 2025-11-09 03:21; добавлены целевые модуль/микросервис, REST/Async API, схемы данных и интеграции с экономикой/крафтом описаны, блокеров нет.
**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (port 8083)  
**target-frontend-module:** modules/combat/shooting/advanced  
**version:** 1.0.0
---

# Система стрельбы (Advanced) — дистанции, рекошеты, кастомизация

**Статус:** approved  
**Дата:** 2025-11-08  
**Приоритет:** Высокий  
**Автор:** AI Brain Manager

**Связанные сервисы:** economy-service (крафт/обвесы), social-service (навыки/импланты), world-service (материалы окружения)

---

- **Status:** not_created
- **Suggested tasks:**
- **Last Updated:** 2025-11-08 09:37

---

## Обзор

Расширяем базовую систему стрельбы (см. `combat-shooting.md`) через три направления:

1. **Механика урона по дистанции:** каждое оружие имеет **профиль эффективности** — оптимальную дистанцию, падение урона, отклонение траектории.
2. **Рекошеты и закрученные выстрелы:** реализуем систему «Smart Ricochet» в духе Cyberpunk 2077 и добавляем «Curved Shot» (особый навык + имплант).
3. **Кастомизация и крафт оружия:** модульная система стволов, модов, чипов, обвесов; крафт обвесов через economy-service.

---

## 1. Механика урона по дистанции

### 1.1 Профили дистанции

| Класс оружия | Оптимальная дистанция | Падение урона | Бонусы |
|--------------|-----------------------|---------------|--------|
| Пистолеты | 0–20 м | -2% урона каждые +5 м | +10% крит шанс на ближней дистанции |
| Штурмовые винтовки | 15–60 м | -3% каждые +10 м | Стабильный урон |
| Снайперские винтовки | 50–200 м | -5% каждые -10 м (за ближнюю дистанцию) | +15% хит шанс при прицеливании |
| ПП (SMG) | 0–25 м | -4% каждые +5 м | +20% к скорости стрельбы в упор |
| Дробовики | 0–10 м | -10% каждые +5 м (рассеивание) | Мульти-хит, статусные эффекты |
| Энергетическое | 20–80 м | Низкое падение (-1%/10 м) | Зарядка увеличивает урон |
| Тяжёлое (LMG, РПГ) | 30–100 м | -4% каждые +15 м | Подавление, сплэш |

### 1.2 Формула урона

```
damage = baseDamage * distanceMultiplier * ammoModifier * statusBonus

distanceMultiplier = 1 - falloffRate * max(0, distance - optimalMax)/interval

ammoModifier: зависит от типа патронов (бронебойные, экспансивные)
statusBonus: эффекты от имплантов/модов (например, +10% против кибер-целей)
```

### 1.3 Паттерн отклонения

- После оптимальной дистанции добавляем **cone deviation**: траектория смещается в конусе, размер зависит от оружия и модов.
- Имплант «GyroStabilizer Mk.III» уменьшает конус на 30%.
- Навык «Precision Mastery» добавляет автокоррекцию (см. 2.2).

---

## 2. Рекошеты и закрученные выстрелы

### 2.1 Smart Ricochet 2.0

- Базируется на `Ricochet Auto-Targeting` из Cyberpunk 2077.
- Компоненты:
  - **Smart Scope** (обвес) → отмечает потенциальные поверхности (металл, керамика) и цели за укрытиями.
  - **Ammo: Ricochet Rounds** → патроны с отражающей оболочкой.
  - **Perk: Tactical Bounce** → уменьшает потерю урона при отскоке.
- Ограничения: максимум 2 рикошета в стандартном режиме (урон каждого -15%).

### 2.2 Curved Shot (закрученный выстрел)

- Требуется комбинация:
  - Имплант «Smart Wrist Tendons» (дополнительный сустав, повышает гибкость).
  - Навык «Trajectory Sculptor» (ветка Precision)
  - Обвес «Neuro-Gyro Coupler» (стабилизирует отклонение).
- Механика:
  - При активации (горячая клавиша) игрок может «пометить» кривую траекторию (до 30°).
  - Выстрел следует по bezier-траектории, обходя укрытия.
  - Стоимость: повышенный расход энергии импланта, кулдаун (15 сек).
  - Оружие: только Smart-class (пистолеты, винтовки), энергетические оружия.
- Урон: базовый *0.9 (за сложность), но +25% шанс статуса (электрошок, оглушение).

### 2.3 Комбо-навыки

- «Ricochet Mastery»: каждая успешная цепочка из 2 рекошетов снижает кулдаун Curved Shot на 5 сек.
- «Trajectory Memory»: после использования Curved Shot система запоминает траекторию и может автоматически повторить (при достаточных очках концентрации).

---

## 3. Кастомизация и крафт оружия

### 3.1 Модульная структура оружия

| Слот | Примеры модулей | Эффекты |
|------|-----------------|---------|
| Ствол | Heavy Barrel, Suppressed Barrel | +урон, +стабильность, -шанс обнаружения |
| Прицел | Smart Scope, Thermal Scope | Авто-наведение, тепловидение |
| Приклад | Stabilizer Stock, Folding Stock | Снижение отдачи, уменьшение веса |
| Мод чип | Targeting Chip, Crit Enhancer | Улучшение хит шанса, критов |
| Авто-помощь | Aim Assist Module, Burst Regulator | Поддержка стрельбы очередями |
| Патронник | Ammo Converter, Extended Mag | Смена типа боеприпасов, увеличение магазина |

### 3.2 Экономика и крафт

- **Материалы:** редкие сплавы, электронные платы, био-гелий (для имплантов).
- **Blueprints:** покупаются у фиксеров, добываются через квесты, разблокируются навыками.
- **Мастерские:**
  - Уличные (низкое качество, дёшево)
  - Корпоративные (высокое качество, ограниченный доступ)
  - Бункерные (восстановленные военные).

**Крафт модов (пример):**

```
Smart Scope Mk.II
- Материалы: Rare Circuit x2, Optical Lens x1, Micro Servo x1
- Навык: Engineering 4
- Время: 4 часа (игровых)
- Бонус: +10% хит шанс при рикошетах, раскрытие целей за укрытием на 3 сек
```

### 3.3 Уровни улучшения

- **MK I:** базовые моды, доступны всем.
- **MK II:** требует навыков (Engineering 3+, Ballistics 2+).
- **MK III:** сочетание имплантов и навыков (например, Curved Shot → требуется MK II Smart Scope + MK III Wrist Tendon).

### 3.4 Крафт обвесов и сервисные работы

- Игрок может **распылить** оружие на материалы (зависит от качества).
- **Модульные станции** позволяют перенастраивать слоты (смена ствола, приклада) без потери модов.
- **Сервисные контракты:** фиксер предлагает задания на создание уникальных модов (например, «доставь компоненты для Smart Ricochet Deluxe»).

---

## 4. Навыки и импланты

| Навык | Ветка | Эффект |
|-------|-------|--------|
| Trajectory Sculptor | Precision | Открывает Curved Shot, +10% контроль траектории |
| Tactical Bounce | Precision | -10% урон при рикошете, +1 число отскоков |
| Ballistic Engineer | Engineering | Ускоряет крафт, открывает MK II моды |
| Smart Ammo Synthesis | Engineering | Создание спецбоеприпасов (ricochet, explosive) |

| Имплант | Слот | Эффект |
|---------|------|--------|
| Smart Wrist Tendons | Руки | Активирует Curved Shot (с навыком) |
| Neuro-Gyro Coupler | Нервная система | Стабилизация при закрученных выстрелах |
| Ballistic Processor Mk.IV | Мозг | Улучшает расчёт траекторий, +15% крит на ближней дистанции |
| Auto-Loader Armature | Кибер-рука | Ускоряет смену модов, магазинов |

---

## 5. Интеграция с другими системами

- **Economy-service:** хранение чертежей, материалов, выдача заказов на крафт.
- **World-service:** материалы окружения для рикошетов (металл, стекло, бетон), влияние погодных условий.
- **Quest system:** задания на создание уникального оружия, обучение Curved Shot.
- **Живость мира:** NPC-профессионалы могут использовать ricochet/curved shot (враги высокого ранга).

---

## 6. Метрики и баланс

- **Accuracy KPI:** средний % попаданий по дистанциям; таргет — 70% на оптимальной дистанции.
- **Damage Falloff KPI:** сравнение фактического падения урона с целевой кривой (±5%).
- **Ricochet Usage:** доля игроков/ботов, использующих рикошеты; таргет — 15% в боях с укрытиями.
- **Curved Shot Impact:** средний урон и статус-эффекты при закрученных выстрелах; должен быть на 10–15% ниже прямых выстрелов, но с повышенной тактической ценностью.
- **Craft Adoption:** % оружия с модами MK II/III; помогает балансировать стоимость материалов и навыков.

---

## 7. REST API и схемы

### 7.1 `POST /combat/ballistics/simulate`
- **Request (Gameplay service):**
  ```json
  {
    "weaponId": "w-smg-neo",
    "ammoType": "ricochet_rounds",
    "distanceMeters": 32,
    "angleDegrees": 12,
    "flags": {
      "curvedShot": true,
      "smartAssist": true
    },
    "attackerImplants": ["smart-wrist-tendons", "gyro-stabilizer"],
    "targetArmor": "kevlar-plate-mk2",
    "environmentMaterial": "steel"
  }
  ```
- **Response 200:**
  ```json
  {
    "hitProbability": 0.78,
    "expectedDamage": 86.4,
    "ricochetPath": ["surface-12", "surface-07"],
    "statusEffects": ["stun"],
    "cooldown": "PT15S"
  }
  ```
- **Error 422:** некорректные комбинации модов/имплантов.

### 7.2 `PATCH /combat/weapon-mods/{weaponId}`
- Обновление модулей оружия, привязано к economy-service.
- Request содержит список слотов и модов, проверяется на совместимость.
- Response возвращает обновлённые статы (урон, отдача, потребление энергии).

### 7.3 `POST /crafting/weapon-jobs`
- Создание заказа на мод/обвес (economy-service).
- Request: чертёж, список материалов, мастерская.
- Response 201: jobId, ETA, требуемые навыки.

### 7.4 `GET /combat/skills/active`
- Позволяет UI запросить состояние активных боевых навыков (в т.ч. Curved Shot).
- Возвращает кулдауны, доступность и влияние имплантов.

### 7.5 Схемы данных
- `WeaponProfileDTO`: базовый урон, оптимальная дистанция, falloff, допустимые моды.
- `BallisticsSimulationRequest/Response`.
- `WeaponModUpdateRequest`, `WeaponModState`.
- `CraftingJobRequest`, `CraftingJobStatus`.
- `ActiveSkillsState` (перечень навыков, кулдауны, модификаторы).

---

## 8. Асинхронные события

| Topic | Producer | Payload |
|-------|----------|---------|
| `combat.ballistics.events` | gameplay-service | `{ combatId, attackerId, weaponId, mode, distance, ricochetCount, curvedShotUsed, damageDealt, statusEffects[] }` |
| `economy.crafting.jobs` | economy-service | `{ jobId, weaponId, modId, status, eta, materialsConsumed[] }` |
| `player.skills.cooldowns` | social-service | `{ playerId, skillId, cooldownEndsAt, modifiers[] }` |

- События подписываются telemetry-service (аналитика), monitoring-service (профилирование боёв) и achievement-service (ачивки за ricochet/curved-shot).
- `combat.ballistics.events` включают `traceId` для привязки к сессиям PvP/PvE.

---

## 9. Интеграция с API-SWAGGER

- `api/v1/gameplay/combat/ballistics.yaml` — описывает simulate, модификацию оружия и активные навыки (operationId: `simulateBallistics`, `updateWeaponMods`, `listActiveSkills`).
- `api/v1/economy/crafting/weapon-jobs.yaml` — очередь крафта модов (operationId: `createWeaponJob`, `getWeaponJob`).
- `api/v1/gameplay/equipment/weapon-mods.yaml` — схемы модов, слотов и ограничений.
- AsyncAPI пакеты:
  - `asyncapi/combat-ballistics.yaml` — тема `combat.ballistics.events`.
  - `asyncapi/economy-crafting.yaml` — тема `economy.crafting.jobs`.
- Безопасность: `BearerAuth` + `X-Player-Id` (для PvP персонализации), rate limit 180 r/min на simulate endpoint.

---

## 10. Следующие итерации (не блокирует)

1. Подготовить таблицы статус-эффектов для визуализации и аналитики.
2. Дополнить PvP/PvE конфигурации бронепробития (в отдельном balancing документе).
3. Создать автоматические тестовые сценарии для `simulateBallistics` (скрипт `scripts/test-ballistics.ps1`).

---

## История изменений

- 2025-11-08 — финализирована API интеграция, описаны REST/Async контракты и подготовлены задачи для API-SWAGGER.
- 2025-11-07 — первый драфт механик дистанций, рикошетов и кастомизации.
