---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-002: api/v1/gameplay/progression/skills-classes/skills-classes.yaml (completed 2025-11-09 13:55)
- Last Updated: 2025-11-09 13:55
---

# Навыки — классовые и подклассовые различия, эксклюзивы

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: матрицы модификаторов, ранги, структуры данных и REST API синхронизированы с progression-skills-mapping, блокеров нет.

**target-domain:** gameplay-progression  
**target-microservice:** gameplay-service (port 8083)  
**target-frontend-module:** modules/progression/skills

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-07 16:46  
**Приоритет:** Высокий

---

## Базовые модификаторы по классам (коэффициенты к профильным атрибутам навыков)

- Solo: Стрельба `REF +0.005`, Ближний бой `STR +0.005`
- Netrunner: Хакерство `INT +0.01`, Киберпространство `WILL +0.005`
- Techie: Крафт/Ремонт `TECH +0.01`
- Medtech: Поддержка/Защита `TECH +0.005`, `WILL +0.005`
- Nomad: Паркур/Вождение `AGI +0.005`, Стрельба в движении `REF +0.005`
- Fixer: Социальные `EMP +0.01`
- Rockerboy: Социальные/Ауры `EMP +0.01`, `COOL +0.005`
- Media: Социальные/Инфо `EMP +0.005`, Анализ `INT +0.005`
- Corpo: Социальные/Переговоры `COOL +0.01`
- Lawman: Стрельба/Контроль `REF +0.005`, Стойкость `BODY +0.005`
- Politician (авторский): Социальные/Фракции `EMP +0.01`, `COOL +0.005`
- Trader (авторский): Торговля/Экономика `EMP +0.005`, `INT +0.005`
- Teacher (авторский): Поддержка/Группа `EMP +0.005`, `WILL +0.005`

---

## Подклассовые различия (примеры бонусов)

- Solo:
  - Штурмовик: Стрельба (штурмовые/дробовики) `REF +0.005` дополнительно
  - Дуэлянт: Ближний бой (клинки) `AGI +0.005` дополнительно
- Netrunner:
  - Инфильтратор: Стелс/Хакерство `COOL +0.005`
  - Сетевой архитектор: Киберпространство `INT +0.005`
- Techie:
  - Механик: Ремонт/Модификации `TECH +0.005`
  - Инженер вооружений: Оружейные моды `TECH +0.005`, Стрельба (точность) `REF +0.005`
- Medtech:
  - Полевая поддержка: Поддержка/Лечение `WILL +0.005`
  - Биомоддер: Импланты `TECH +0.005`
- Nomad:
  - Скаут: Паркур/Мобильность `AGI +0.005`
  - Рейдер: Тяжёлое оружие `STR +0.005`
- Fixer:
  - Дипломат: Убеждение `EMP +0.005`
  - Шантажист: Запугивание `COOL +0.005`
- Rockerboy:
  - Лидер сцены: Ауры `EMP +0.005`
  - Провокатор: Соц. давлением `COOL +0.005`
- Media:
  - Репортёр: Анализ/Раскрытие `INT +0.005`
  - Инфлюенсер: Убеждение/Ауры `EMP +0.005`
- Corpo:
  - Переговорщик: Торг/Договоры `COOL +0.005`
  - Стратег: Анализ/Контроль `INT +0.005`
- Lawman:
  - Следователь: Анализ/Допрос `INT +0.005`
  - Штурмовик: Штурмовые/Контроль толпы `REF +0.005`
- Politician:
  - Популист: Массовые ауры `EMP +0.005`
  - Лоббист: Договоры/Фракции `COOL +0.005`
- Trader:
  - Брокер: Торговля/Аукционы `INT +0.005`
  - Логист: Экономика/Контракты `INT +0.005`
- Teacher:
  - Наставник: Ауры группы/обучение `WILL +0.005`
  - Тактик: Командные бафы/координация `COOL +0.005`

Примечание: конкретные названия подклассов/способностей уточняются в `classes-abilities.md`.

---

## Эксклюзивные навыки (примеры)

- Solo: «Адреналин-шторм» (active) — временный баф к урону и стойкости, КД высокий
- Netrunner: «Множественный взлом» (active) — параллельные протоколы, штраф к перегреву
- Techie: «Полевая сборка» (active) — быстрый мод/ремонт в бою
- Medtech: «Овердрайв регена» (support) — мощный HoT, ограничение по стэкингу
- Nomad: «Оффроуд-рывок» (active) — серийные рывки, окно уклонения
- Fixer: «Чёрный рынок» (support) — временные ценовые/бартерные бонусы
- Rockerboy: «Боевой гимн» (aura) — аура урона/точности, масштабируется от EMP
- Media: «Раскрытие» (active) — дебафф цели, раскрывает слабости (анализ)
- Corpo: «Контракт» (support) — временные бонусы союзнику, цена — репутация
- Lawman: «Подчинение» (active) — контроль толпы с иммунитетами у элиты
- Politician: «Харизма+» (aura) — усиление социальных проверок группы
- Trader: «Аукционный импульс» (support) — баф к торговле/крафту/контрактам
- Teacher: «Интенсив-тренинг» (aura) — ускорение прогресса навыков группы

Детальная механика эксклюзивов — в `classes-abilities.md` (классовые способности) и привязке к рангу/ресурсам — в `progression-skills.md`.

---

## Матрица «класс → ядро навыков»

| Класс | Основные навыки | Вторичные навыки | Заблокированные навыки |
| --- | --- | --- | --- |
| Solo | gunplay, melee, endurance | stealth, survival | advanced hacking (до Tier 3) |
| Netrunner | hacking, cyberspace, analysis | stealth, engineering | heavy weapons |
| Techie | crafting, repair, implants | gunplay, analysis | psi/charisma (требует перка) |
| Nomad | driving, parkour, survival | gunplay, scavenging | corporate negotiation |
| Fixer | negotiation, bribery, trade | analysis, stealth | heavy combat (без перка) |
| Medtech | healing, support, biochem | analysis, crafting | assault rifle mastery |

Эта таблица определяет доступность веток в UI (серые навыки до разблокировки).

---

## Ранги разблокировки и требования

| Tier | Уровень персонажа | Обязательные атрибуты | Дополнительные условия |
| --- | --- | --- | --- |
| 0 | 1 | — | Базовые навыки класса |
| 1 | 10 | профильный атрибут ≥ 10 | Квест «Вступление в класс» |
| 2 | 20 | профильный атрибут ≥ 14, вторичный ≥ 12 | Приобретён соответствующий перк |
| 3 | 35 | два профильных атрибута ≥ 16 | Сюжетный прогресс (Act II) |
| 4 | 50 | суммарный рейтинг атрибутов ≥ 150 | Репутация фракции ≥ 70, уникальный предмет |

Перки `Cross-Class` и `Guild Mentor` позволяют открывать соседние ветки, но с коэффициентом эффективности 0.75.

---

## JSON/YAML структура для экспорта в API

```yaml
class_skill_matrix:
  solo:
    baseline:
      gunplay:
        attribute: REF
        bonus: 0.005
      melee:
        attribute: STR
        bonus: 0.005
    subclasses:
      assault:
        modifiers:
          assault_rifles: { attribute: REF, bonus: 0.005 }
          suppression: { attribute: WILL, bonus: 0.003 }
        exclusives:
          - skill: adrenaline_storm
            unlock_tier: 2
            cost: { stamina: 40, cooldown: 120 }
    unlocks:
      tier1: { level: 10, requirements: { ref: ">=10" } }
      tier2: { level: 20, requirements: { ref: ">=14", str: ">=12" }, perk: solo_blades_mastery }
      tier3: { level: 35, quest: corpo_wars_act2 }
```

Файл генерации располагается в `api/v1/progression/skills-classes.yaml` и синхронизируется в CI.

---

## Структура данных (gameplay-service)

```sql
CREATE TABLE class_skill_modifiers (
    class_code VARCHAR(16) NOT NULL,
    skill_code VARCHAR(64) NOT NULL,
    attribute_code VARCHAR(8) NOT NULL,
    bonus NUMERIC(4,3) NOT NULL,
    PRIMARY KEY (class_code, skill_code)
);

CREATE TABLE subclass_skill_modifiers (
    class_code VARCHAR(16) NOT NULL,
    subclass_code VARCHAR(16) NOT NULL,
    skill_code VARCHAR(64) NOT NULL,
    attribute_code VARCHAR(8) NOT NULL,
    bonus NUMERIC(4,3) NOT NULL,
    PRIMARY KEY (class_code, subclass_code, skill_code)
);

CREATE TABLE class_skill_unlocks (
    class_code VARCHAR(16) NOT NULL,
    tier SMALLINT NOT NULL,
    min_level SMALLINT NOT NULL,
    requirements JSONB NOT NULL,
    PRIMARY KEY (class_code, tier)
);

CREATE TABLE exclusive_skills (
    skill_code VARCHAR(64) PRIMARY KEY,
    class_code VARCHAR(16) NOT NULL,
    subclass_code VARCHAR(16),
    unlock_tier SMALLINT NOT NULL,
    resource_cost JSONB,
    cooldown_seconds INTEGER,
    description TEXT
);
```

---

## REST API (gameplay-service)

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/progression/classes` | `GET` | Список классов, модификаторы и доступные подклассы |
| `/progression/classes/{classCode}` | `GET` | Полная информация по классу, включая unlock tiers |
| `/progression/classes/{classCode}/subclasses/{subClass}` | `GET` | Детали подкласса, бонусы и эксклюзивы |
| `/progression/classes/{classCode}/unlock` | `POST` | Применить разблокировку навыка (проверка условий) |
| `/progression/classes/{classCode}/metrics` | `GET` | Телеметрия использования навыков/модификаторов |

События шины `progression.classes.*` (unlock_attempted, unlock_success, modifier_applied, balance_override_applied) позволяют реагировать аналитике и UI.

---

## Баланс и телеметрия

- `class_skill_usage` агрегирует данные из боёв/миссий и сравнивает с матрицей.
- Автоматические алерты создаются при `usage_share > 65%` или `win_rate_delta > 10%`.
- Дизайнерские настройки (`balance_overrides.yaml`) могут временно менять бонусы; изменения логируются.

---

## Связанные документы

- `progression-skills.md` — базовые формулы, категории, ранги/прогресс
- `classes-abilities.md` — конкретные классовые способности
- `progression-attributes.md` — атрибуты и влияние на навыки

---

## История изменений

- v1.1.0 (2025-11-07 16:46) — Добавлены матрицы модификаторов, структура данных, REST API и процессы балансировки
- v1.0.0 (2025-11-05) — Стартовая версия