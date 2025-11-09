---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Матрица атрибутов — Старт, рост, капы и требования

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: матрица, экспорт, валидаторы и связи с предметами остаются консистентными, блокеров нет.

**target-domain:** gameplay-progression  
**target-microservice:** gameplay-service (port 8083)  
**target-frontend-module:** modules/progression/attributes

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-07 16:46  
**Приоритет:** Высокий

---

## 1. Стартовые диапазоны и бонусы

- Базовый стартовый диапазон атрибутов: `3–8`
- Дополнительные классовые бонусы: как в `progression-attributes.md/Связь с классами`
- «Свой путь»: +1 к любому атрибуту по выбору

### Таблица стартовых значений (пример для основных классов)

| Класс | STR | REF | BODY | INT | TECH | COOL | AGI | EMP | WILL |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Solo | 8 | 8 | 8 | 5 | 4 | 7 | 6 | 5 | 5 |
| Netrunner | 4 | 7 | 5 | 9 | 7 | 5 | 6 | 6 | 6 |
| Nomad | 7 | 7 | 7 | 5 | 5 | 6 | 7 | 5 | 5 |
| Techie | 4 | 6 | 5 | 8 | 8 | 5 | 5 | 6 | 6 |
| Lawman | 7 | 6 | 7 | 5 | 4 | 7 | 5 | 5 | 7 |
| Medtech | 5 | 5 | 6 | 8 | 7 | 5 | 5 | 6 | 7 |
| Fixer | 5 | 6 | 5 | 6 | 5 | 6 | 6 | 8 | 7 |

Фактические значения вычисляются как `base_roll (3–8)` + классовый бонус, результат сохраняется в `character_creation_snapshot`.

---

## 2. Прирост по уровням

- Каждый уровень: `+1` очко атрибутов
- Каждые 5 уровней: дополнительное `+2` очка (свободное распределение)
- Источники вне уровней: перки, импланты, перерождения

**Таблица прироста (уровни 1–60):**

| Диапазон уровней | Очки за уровень | Бонус за диапазон | Кумулятивно |
| --- | --- | --- | --- |
| 1–10 | +1 | +2 (на 5 и 10 уровне) | 12 |
| 11–20 | +1 | +2 (на 15 и 20 уровне) | 24 |
| 21–40 | +1 | +4 (на 25, 30, 35, 40 уровне) | 48 |
| 41–60 | +2 | +4 (на 45, 50, 55, 60 уровне) | 92 |

> Начиная с 41 уровня выдаётся +2 очка за уровень, чтобы компенсировать рост стоимости перков и предметов эндгейма.

---

## 3. Капы

- Жесткий кап (база): `20`
- Софт-кап (импланты/перки): `25` (эффективность модификаторов выше 25 уменьшается на 50%)
- Абсолютный кап (с перерождениями): `30`

---

## 4. Требования к экипировке/имплантам (минимальные атрибуты)

| Тип | Требование | Наказание при несоблюдении |
|---|---|---|
| Тяжёлое оружие | STR ≥ 10 | Точность -20% |
| Снайперские винтовки | REF ≥ 12 | Точность -15% |
| Умное оружие | INT ≥ 12 или TECH ≥ 12 | Автонаведение -30% |
| Боевые экзоскелеты | BODY ≥ 12 | Расход STA +25% |
| Продвинутые кибердеки | INT ≥ 14 | Блок части протоколов |

Список расширяется в спецификациях предметов (`economy/equipment-matrix.md`) и имплантов (`combat-implants-types.md`).

---

## 5. Классовые модификаторы коэффициентов навыков (базово)

- Solo: +0.005 к REF в формулах стрельбы; +0.005 к STR в ближнем бою
- Netrunner: +0.01 к INT в хакерстве; +0.005 к WILL в киберпространстве
- Techie: +0.01 к TECH в крафте/ремонте
- Nomad: +0.005 к AGI в паркуре; +0.005 к REF при вождении (UI/сцены)
- Fixer: +0.005 к EMP в социальных проверках

Точные значения балансируются и могут отличаться по подклассам.

---

## 6. Проверка требований предметов и имплантов

- Каждая запись предмета в `equipment-matrix` имеет поле `attribute_requirements` со ссылкой на `attributes-matrix`.
- Цикл валидации при изменении матрицы:
  1. Обновить `attributes-matrix` (этот документ → YAML/JSON экспорт).
  2. Запустить скрипт `scripts/validate-item-attributes.ps1` — проверяет, что все предметы/импланты имеют валидные требования.
  3. В случае коллизий (например, STR > капа класса) формируется отчёт с рекомендациями.

### Типовые требования (extended)

| Категория | Атрибут | Порог | Примечание |
| --- | --- | --- | --- |
| Legendary melee | STR ≥ 14 | Доп. крит шанс при STR ≥ 18 |
| Prototype smart gun | INT ≥ 14 & TECH ≥ 12 | Убирает штраф к автонаведению |
| Heavy drone control | INT ≥ 12 & WILL ≥ 10 | Без порога AI реагирует медленнее |
| Advanced medtech kit | TECH ≥ 13 & EMP ≥ 11 | Снижает шанс отказа лечения |
| Corporate negotiations | EMP ≥ 12 & COOL ≥ 11 | Доступ к эксклюзивным веткам диалога |

Полный перечень требований поддерживается в `api/v1/economy/equipment/requirements.yaml`.

---

## 7. Структура данных (gameplay-service)

```sql
CREATE TABLE attribute_matrix (
    id SERIAL PRIMARY KEY,
    class_code VARCHAR(16) NOT NULL,
    attribute_code VARCHAR(8) NOT NULL,
    base_min SMALLINT NOT NULL,
    base_max SMALLINT NOT NULL,
    class_bonus SMALLINT NOT NULL,
    growth_rate NUMERIC(4,2) NOT NULL,
    hard_cap SMALLINT NOT NULL,
    soft_cap SMALLINT NOT NULL,
    rebirth_cap SMALLINT NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attribute_requirements (
    id SERIAL PRIMARY KEY,
    item_ref VARCHAR(64) NOT NULL,
    attribute_code VARCHAR(8) NOT NULL,
    min_value SMALLINT NOT NULL,
    penalty_description TEXT,
    failure_effect JSONB,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attribute_level_thresholds (
    level SMALLINT PRIMARY KEY,
    points_awarded SMALLINT NOT NULL,
    bonus_points SMALLINT NOT NULL,
    cumulative_points SMALLINT NOT NULL
);
```

Эти таблицы используются генератором данных для API и игровым UI (динамическое отображение требований).

---

## 8. REST/GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/progression/attributes/matrix` | `GET` | Экспорт стартовых/ростовых значений (класс → атрибут) |
| `/progression/attributes/matrix` | `PUT` | Админ обновление матрицы (bulk) |
| `/progression/attributes/requirements` | `GET` | Требования к предметам и имплантам |
| `/progression/attributes/requirements/{itemId}` | `PATCH` | Корректировка требований |
| `/progression/attributes/thresholds` | `GET` | Пороговые значения уровня/очков |

GraphQL тип `AttributeMatrix` публикуется для фронтенда (выпадающие меню выбора классов, подсветка недостающих атрибутов).

---

## 9. Синхронизация и валидация

- `progression-attributes.md` — источник формул; автоматический чек сравнивает коэффициенты.
- `progression-skills-mapping.md` — проверка `activity_tag` и коэффициентов влияния на навыки.
- `economy/equipment-matrix.md` + `combat-implants-types.md` — требования/бонусы.
- CI job `matrix-consistency` отрабатывает при изменении YAML → генерирует JSON для API-SWAGGER и проверяет капы.

---

## 6. Связанные документы

- `progression-attributes.md` — определения атрибутов и формулы
- `progression-skills.md` — таксономия навыков и базовые формулы
- `economy/equipment-matrix.md` — требования предметов
- `combat-implants-types.md` — влияние имплантов

---

## История изменений

- v1.1.0 (2025-11-07 16:46) — Добавлены таблицы хранения, API, расширенные требования и валидация
- v1.0.0 (2025-11-05) — Создана базовая матрица атрибутов



