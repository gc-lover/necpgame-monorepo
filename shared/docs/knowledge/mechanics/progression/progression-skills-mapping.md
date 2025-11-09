---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Навыки — соответствия предметам и имплантам

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: синхронизация с лодаутами, структура данных и API остаются консистентными, блокеров нет.

**target-domain:** gameplay-progression  
**target-microservice:** gameplay-service (port 8083)  
**target-frontend-module:** modules/progression/skills

**Статус:** approved  
**Версия:** 1.2.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-08 00:14  
**Приоритет:** Высокий

---

## Сводная таблица соответствий (v1)

| Навык/Категория | Требуемые предметы (type/subtype) | Теги/поля (equipment-matrix) | Импланты (slotType) | Синергийные теги |
|---|---|---|---|---|
| Стрельба: «Точный выстрел» | weapon/sniper_rifle, weapon/assault_rifle | StatsCore.accuracy, WeaponStats.adsBonus | combat (оптика), tactical (оптика) | brand: Tsunami, Kiroshi; tags: smartLock |
| Стрельба: «Стабилизация отдачи» | weapon/* | StatsCore.recoil, WeaponStats.handling | combat (стабилизатор) | brand: Militech; tags: recoilStability |
| Стрельба: «Контроль дыхания» | weapon/sniper_rifle | WeaponStats.adsBonus | tactical (оптика) | brand: Tsunami; tags: accuracy++ |
| Ближний бой: «Парирование» | weapon/melee | StatsExtended.damageType=melee | combat (киберруки/клинки) | tags: blades, melee |
| Паркур: «Кошачий прыжок» | armor/legs | ArmorStats.modSlots.utility | mobility (киберноги) | tags: mobility+ |
| Стелс: «Тихий шаг» | armor/legs, armor/body | ArmorStats.noiseDamp | mobility (Lynx Paws), defensive (киберкожа) | tags: lowNoise |
| Хакерство: «Быстрый протокол» | cyberdeck/* | CyberdeckStats.ioBandwidth, regenRate | os (Cyberdeck) | deckLevel: T2+ |
| Хакерство/Бой: «Нейрошок» | weapon/smart | StatsExtended.damageType=emp | os (Cyberdeck), tactical (оптика) | tags: emp, smart |
| Крафт: «Полевой ремонт» | mod/*, armor/*, weapon/* | ModStats.statDelta, ArmorStats.modSlots | defensive (инструм.), combat (универс.) | tags: repairEase |
| Социальные: «Командная аура» | armor/head (комм‑модули) | ArmorStats.energyBuffer | tactical (связь) | tags: aura, group |
| Поддержка: «Тактическая сеть» | armor/head, armor/body | ArmorStats.commSuite, Defense.shieldBonus | tactical (Netrunner Link) | tags: teamBuff |
| Вождение: «Дрифт-контроль» | vehicle/mod/drift_kit | VehicleStats.handling | mobility (drive assist) | tags: vehicleHandling |
| Биомодификация: «Стабилизация имплантов» | mod/biotech | ImplantStats.failureChance | defensive (immune system) | tags: implantStability |
| Торговля: «Экономический анализ» | item/datachips/trade | ItemStats.marketInfluence | neural (risk assessor) | tags: tradeBoost |

Примечания:
- Типы/подтипы предметов см. `economy/equipment-matrix.md` (разделы Weapon/Armor/Cyberdeck/Mod).
- Типы имплантов см. `combat-implants-types.md` (`slotType`: combat, tactical, defensive, mobility, os).
- Синергийные теги сопоставляются с `synergyTags`/`brand.signatureBonuses`.

---

## Таксономия тегов

- `skillTag`: gunplay, melee, stealth, hacking, crafting, support, trade, driving и т.д.
- `equipmentTag`: smartLock, recoilStability, lowNoise, shieldBonus, marketInfluence.
- `synergyTag`: aura, mobility+, emp, vehicleHandling — используется рекомендательной системой UI.

Полный список тегов и их локализации расположен в `progression-skill-tags.yaml`. Проверка корректности — через `scripts/validate-skill-tags.ps1`.

---

## Связь с боевыми лодаутами и PvE профилями

- Каждая запись навыка содержит `loadoutProfiles` (stormbreaker, safebearer, scout, stormrunner), что позволяет рекомендовать навыки для PvE экспедиций (`combat-loadouts-system.md`).
- Новый тег `extraction-support` используется для навыков, усиливающих перенос тяжёлых грузов и сенсорное сканирование в ARC Raiders-подобных миссиях.
- Навыки с тегом `event-reactive` получают модификаторы от событий (`world/events/live-events-system.md`) и синхронизируются с `threatAdaptationProfile`.
- Маппинг хранится в таблице `skill_loadout_profile` и в YAML-экспорте раздела `profiles`.

---

## Примеры требований для продвинутых навыков

- «Контроль дыхания»: прицел T2 (mod: sight), weapon: sniper_rifle, REF ≥ 12
- «Нейрошок»: Cyberdeck T2+, ioBandwidth ≥ X, INT ≥ 14, WILL ≥ 10
- «Кошачий прыжок»: mobility-имплант (Reinforced Tendons), AGI ≥ 10
- «Тихий шаг»: armor legs c `noiseDamp ≥ 10%`, Lynx Paws

---

## Структура данных (gameplay-service)

```sql
CREATE TABLE skill_item_mapping (
    skill_code VARCHAR(64) NOT NULL,
    item_category VARCHAR(64) NOT NULL,
    item_subcategory VARCHAR(64),
    required_stats JSONB,
    PRIMARY KEY (skill_code, item_category, COALESCE(item_subcategory, ''))
);

CREATE TABLE skill_implant_mapping (
    skill_code VARCHAR(64) NOT NULL,
    implant_slot VARCHAR(32) NOT NULL,
    required_tier SMALLINT,
    PRIMARY KEY (skill_code, implant_slot)
);

CREATE TABLE skill_synergy_tags (
    skill_code VARCHAR(64) NOT NULL,
    tag_type VARCHAR(16) NOT NULL, -- equipment | implant | synergy
    tag_value VARCHAR(64) NOT NULL,
    weight NUMERIC(4,2) DEFAULT 1.0,
    PRIMARY KEY (skill_code, tag_type, tag_value)
);

CREATE TABLE skill_unlock_requirements (
    skill_code VARCHAR(64) PRIMARY KEY,
    attributes JSONB,
    reputation JSONB,
    quest_ref VARCHAR(64)
);

CREATE TABLE skill_loadout_profile (
    skill_code VARCHAR(64) NOT NULL,
    loadout_profile VARCHAR(32) NOT NULL, -- stormbreaker | safebearer | scout | stormrunner
    weight NUMERIC(4,2) DEFAULT 1.0,
    PRIMARY KEY (skill_code, loadout_profile)
);
```

---

## YAML экспорт

```yaml
skills:
  precise_shot:
    tags: [gunplay, sniper]
    items:
      - category: weapon
        subcategory: sniper_rifle
        required_stats:
          StatsCore.accuracy: ">=0.75"
          WeaponStats.adsBonus: ">=0.10"
    implants:
      - slot: combat
        required_tier: 2
      - slot: tactical
        required_tier: 1
    synergy_tags:
      smartLock: 1.2
      brand.Tsunami: 1.1
    unlock_requirements:
      attributes: { ref: ">=12" }
      quest_ref: "corpo-wars-sniper"
    loadout_profiles:
      stormbreaker: 1.0
      scout: 0.8
```

Экспорт размещается в `api/v1/progression/skills-mapping.yaml` и употребляется для генерации OpenAPI/клиентских моделей.

---

## REST API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/progression/skills/mapping` | `GET` | Возвращает маппинг навыков к предметам/имплантам |
| `/progression/skills/mapping/{skillCode}` | `GET` | Детали по конкретному навыку |
| `/progression/skills/mapping/{skillCode}` | `PUT` | Обновление соответствий (админ) |
| `/progression/skills/tags` | `GET` | Список доступных тегов |
| `/progression/skills/validators/run` | `POST` | Запуск валидации соответствий |
| `/progression/skills/profiles` | `GET` | Выгрузка навыков по `loadoutProfile` и `eventCode` |

События `progression.skills_mapping.*` (`updated`, `validator_failed`, `validator_passed`) позволяют синхронизировать данные с UI/аналитикой.

---

## Автоматическая валидация

1. Любое изменение YAML инициирует скрипт `validate-skill-mapping` в CI.
2. Скрипт проверяет существование предметов/имплантов, корректность тегов и кросс-ссылок.
3. Итоговый отчёт публикуется в `06-tasks/reports/skill-mapping-validation.md`; критические ошибки помечаются как блокирующие.

---

## Связанные документы
- `progression-skills.md` — формулы/категории/ранги
- `progression-attributes-matrix.md` — требования и штрафы
- `economy/equipment-matrix.md` — типы предметов и статы
- `combat-implants-types.md` — типы имплантов и OS
- `combat-loadouts-system.md` — потребители маппинга в боевых лодаутах

---

## История изменений
- v1.2.0 (2025-11-08 00:14) — Добавлены профили лодаутов и теги для PvE экспедиций, расширены таблицы и API.
- v1.1.0 (2025-11-07 16:46) — Расширены таблицы соответствий, добавлены структуры данных, REST API и автоматическая валидация
- v1.0.0 (2025-11-05) — Создана базовая таблица соответствий навыков предметам и имплантам