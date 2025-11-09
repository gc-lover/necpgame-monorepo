# Task ID: API-TASK-037
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-06 23:00
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** none

---

## 📋 Краткое описание

Создать API спецификацию для механик стрельбы и основ боя.

**Что нужно сделать:** Создать полную API спецификацию для системы стрельбы, включая урон, типы оружия, части тела, TTK (Time To Kill) и механики покрытия.

---

## 🎯 Цель задания

Создать централизованную API спецификацию для боевой системы стрельбы в стиле киберпанк MMORPG. Система должна поддерживать:
- Расчет урона с учетом частей тела и типов урона
- Разные TTK (Time To Kill) для разных зон/режимов
- Движение и стрельбу с влиянием оружия/навыков
- Систему покрытий и укрытий с проникающими пулями

**Зачем это нужно:**
- Фундаментальная механика для всей боевой системы
- Основа для интеграции с другими системами (способности, импланты, прокачка)
- Критический элемент для геймплея (MMORPG + шутер)

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/combat/combat-shooting.md`
**Версия документа:** v1.1.0
**Дата последнего обновления:** 2025-11-03
**Статус документа:** review

**Что важно из этого документа:**
- Сочетание MMORPG и шутера (MMORPG как основа)
- Стиль стрельбы: аркадная база + элементы реализма
- TTK зависит от зоны/режима (высокий для PvE, низкий для PvP)
- Система урона: слабые точки + разные части тела + кибер-части + типы урона
- Движение и стрельба зависит от оружия/навыков
- Покрытие: проникающие пули + разрушаемые укрытия

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-overview.md` - обзор боевой системы
- `.BRAIN/02-gameplay/combat/combat-weapon-classes-detailed.md` - детальные классы оружия
- `.BRAIN/02-gameplay/combat/combat-abilities.md` - способности (влияют на стрельбу)
- Существующие API: `API-SWAGGER/api/v1/shared/common/responses.yaml` - стандартные ответы

### Связанные документы

- combat-implants-types.md - импланты влияют на точность/урон
- equipment-matrix.md - оружие и его характеристики

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/combat/shooting.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0 Specification (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                └── shooting.yaml  ← Создать этот файл
```

**Если файл уже существует:**
- Создать новый файл (файл не существует)

---

## ✅ Что нужно сделать (детальный план)

### Шаг 1: Анализ исходного документа

**Действия:**
1. Прочитать файл `.BRAIN/02-gameplay/combat/combat-shooting.md` (уже прочитан)
2. Выделить ключевые концепции:
   - Тип боевой системы: MMORPG основа + шутер экшен
   - Стиль стрельбы: аркадная + элементы реализма
   - TTK: зависит от зоны (открытый мир = высокий, PvP = низкий)
   - Система урона: части тела (голова x2, торс x1, конечности x0.7) + кибер-части + типы урона
   - Движение и стрельба: зависит от оружия/навыков (пистолеты = меньше штрафов, винтовки = больше)
   - Покрытие: проникающие пули (через укрытия) + разрушаемые укрытия
3. Выделить бизнес-правила:
   - Развитие персонажа влияет на TTK
   - Импланты влияют на точность/урон/скорострельность
   - Оружие определяет штрафы при движении
   - Материал укрытия влияет на проникновение пуль

**Ожидаемый результат:**
- Список концепций для отражения в API
- Список бизнес-правил

### Шаг 2: Определение API endpoints

**Что нужно создать:**

1. **POST `/api/v1/gameplay/combat/shoot`**
   - **Назначение:** Выполнить выстрел
   - **Параметры запроса:**
     - `shooter_id` (string, required) - ID стреляющего персонажа
     - `weapon_id` (string, required) - ID оружия
     - `target_id` (string, required) - ID цели
     - `target_body_part` (string, optional) - Целевая часть тела (head/torso/limbs)
     - `aim_point` (object, required) - Точка прицеливания (x, y, z)
     - `is_moving` (boolean, optional) - Двигается ли стреляющий
   - **Ответы:**
     - `200 OK` - Успешный выстрел (Shot result)
     - `400 Bad Request` - Невалидные параметры
     - `404 Not Found` - Оружие/цель не найдены
     - `409 Conflict` - Нет патронов

2. **POST `/api/v1/gameplay/combat/calculate-damage`**
   - **Назначение:** Рассчитать урон с учетом всех модификаторов
   - **Параметры запроса:**
     - `weapon_id` (string, required) - ID оружия
     - `target_body_part` (string, required) - Часть тела
     - `target_armor` (number, optional) - Броня цели
     - `target_implants` (array, optional) - Импланты цели
     - `damage_type` (string, required) - Тип урона
   - **Ответы:**
     - `200 OK` - Результат расчета урона

3. **GET `/api/v1/gameplay/combat/weapons/{weapon_id}`**
   - **Назначение:** Получить характеристики оружия
   - **Path параметры:**
     - `weapon_id` (string, required) - ID оружия
   - **Ответы:**
     - `200 OK` - Характеристики оружия
     - `404 Not Found` - Оружие не найдено

4. **GET `/api/v1/gameplay/combat/damage-modifiers`**
   - **Назначение:** Получить модификаторы урона (части тела, типы урона)
   - **Query параметры:**
     - `zone_type` (string, optional) - Тип зоны (open_world, pvp, arena)
   - **Ответы:**
     - `200 OK` - Список модификаторов

5. **POST `/api/v1/gameplay/combat/reload`**
   - **Назначение:** Перезарядить оружие
   - **Параметры запроса:**
     - `character_id` (string, required) - ID персонажа
     - `weapon_id` (string, required) - ID оружия
   - **Ответы:**
     - `200 OK` - Успешная перезарядка
     - `404 Not Found` - Оружие не найдено
     - `409 Conflict` - Нет патронов для перезарядки

6. **POST `/api/v1/gameplay/combat/cover-penetration`**
   - **Назначение:** Проверить проникновение пули через укрытие
   - **Параметры запроса:**
     - `weapon_id` (string, required) - ID оружия
     - `ammo_type` (string, required) - Тип боеприпасов
     - `cover_material` (string, required) - Материал укрытия
     - `cover_thickness` (number, required) - Толщина укрытия
   - **Ответы:**
     - `200 OK` - Результат проникновения (penetrates: boolean, damage_reduction: number)

### Шаг 3: Определение моделей данных

**Что нужно создать:**

1. **ShootRequest** (запрос выстрела)
   ```yaml
   ShootRequest:
     type: object
     required:
       - shooter_id
       - weapon_id
       - target_id
       - aim_point
     properties:
       shooter_id:
         type: string
         description: ID стреляющего персонажа
       weapon_id:
         type: string
         description: ID оружия
       target_id:
         type: string
         description: ID цели
       target_body_part:
         type: string
         enum: [head, torso, arms, legs, cyber_head, cyber_torso, cyber_arms, cyber_legs]
         description: Целевая часть тела
       aim_point:
         type: object
         required: [x, y, z]
         properties:
           x: { type: number }
           y: { type: number }
           z: { type: number }
       is_moving:
         type: boolean
         description: Двигается ли стреляющий
   ```

2. **ShootResult** (результат выстрела)
   ```yaml
   ShootResult:
     type: object
     properties:
       hit:
         type: boolean
         description: Попал ли выстрел
       body_part_hit:
         type: string
         enum: [head, torso, arms, legs, cyber_head, cyber_torso, cyber_arms, cyber_legs]
       damage_dealt:
         type: number
         description: Нанесенный урон
       damage_type:
         type: string
         enum: [physical, energy, chemical, thermal, emp, cyber, poison, electromagnetic]
       is_critical:
         type: boolean
       modifiers_applied:
         type: array
         items:
           type: object
           properties:
             modifier_type: { type: string }
             modifier_value: { type: number }
       target_status:
         type: object
         properties:
           hp_remaining: { type: number }
           is_dead: { type: boolean }
   ```

3. **DamageCalculationRequest**
   ```yaml
   DamageCalculationRequest:
     type: object
     required:
       - weapon_id
       - target_body_part
       - damage_type
     properties:
       weapon_id:
         type: string
       target_body_part:
         type: string
         enum: [head, torso, arms, legs, cyber_head, cyber_torso, cyber_arms, cyber_legs]
       target_armor:
         type: number
         minimum: 0
       target_implants:
         type: array
         items:
           type: string
       damage_type:
         type: string
         enum: [physical, energy, chemical, thermal, emp, cyber, poison, electromagnetic]
       zone_type:
         type: string
         enum: [open_world, pvp, arena, extraction, raid]
         description: Тип зоны для TTK модификатора
   ```

4. **DamageCalculationResult**
   ```yaml
   DamageCalculationResult:
     type: object
     properties:
       base_damage:
         type: number
       body_part_multiplier:
         type: number
         description: Модификатор части тела (head=2.0, torso=1.0, limbs=0.7)
       armor_reduction:
         type: number
       type_effectiveness:
         type: number
         description: Эффективность типа урона против цели
       final_damage:
         type: number
       estimated_ttk:
         type: number
         description: Примерное время убийства (секунды)
       modifiers:
         type: array
         items:
           type: object
           properties:
             name: { type: string }
             value: { type: number }
             description: { type: string }
   ```

5. **Weapon** (характеристики оружия)
   ```yaml
   Weapon:
     type: object
     required:
       - id
       - name
       - weapon_class
       - damage
       - fire_rate
       - accuracy
     properties:
       id:
         type: string
       name:
         type: string
       weapon_class:
         type: string
         enum: [pistol, revolver, assault_rifle, smg, shotgun, sniper, lmg, melee]
       damage:
         type: number
         minimum: 1
       fire_rate:
         type: number
         description: Выстрелов в секунду
       accuracy:
         type: number
         minimum: 0
         maximum: 100
       magazine_size:
         type: integer
       reload_time:
         type: number
         description: Время перезарядки (секунды)
       movement_penalty:
         type: number
         description: Штраф к точности при движении (%)
       damage_type:
         type: string
         enum: [physical, energy, chemical, thermal, emp, cyber]
       penetration:
         type: number
         description: Проникающая способность
   ```

6. **BodyPartModifiers**
   ```yaml
   BodyPartModifiers:
     type: object
     properties:
       organic:
         type: object
         properties:
           head: { type: number, example: 2.0 }
           torso: { type: number, example: 1.0 }
           arms: { type: number, example: 0.7 }
           legs: { type: number, example: 0.7 }
       cyber:
         type: object
         properties:
           cyber_head: { type: number, example: 1.5 }
           cyber_torso: { type: number, example: 0.8 }
           cyber_arms: { type: number, example: 0.5 }
           cyber_legs: { type: number, example: 0.5 }
   ```

7. **CoverPenetrationRequest**
   ```yaml
   CoverPenetrationRequest:
     type: object
     required:
       - weapon_id
       - ammo_type
       - cover_material
       - cover_thickness
     properties:
       weapon_id:
         type: string
       ammo_type:
         type: string
         enum: [standard, armor_piercing, energy, explosive]
       cover_material:
         type: string
         enum: [wood, metal, concrete, energy_shield]
       cover_thickness:
         type: number
         minimum: 0
         description: Толщина в сантиметрах
   ```

8. **CoverPenetrationResult**
   ```yaml
   CoverPenetrationResult:
     type: object
     properties:
       penetrates:
         type: boolean
         description: Проникает ли пуля
       damage_reduction:
         type: number
         description: Снижение урона при проникновении (%)
       cover_destroyed:
         type: boolean
         description: Разрушено ли укрытие
       cover_health_remaining:
         type: number
         description: Оставшаяся прочность укрытия
   ```

### Шаг 4: Создание OpenAPI спецификации

**Требования к файлу:**

1. **Структура OpenAPI 3.0:**
   ```yaml
   openapi: 3.0.3
   info:
     title: Combat Shooting API
     version: 1.0.0
     description: |
       API для механик стрельбы и основ боя.
       
       Источник концепции: .BRAIN/02-gameplay/combat/combat-shooting.md v1.1.0
       
       Особенности:
       - MMORPG как основа, шутер добавляет экшен
       - Аркадная стрельба с элементами реализма
       - TTK зависит от зоны/режима
       - Система урона с частями тела и типами урона
       - Кибер-части имеют разные модификаторы урона
   servers:
     - url: https://api.necp.game/v1
       description: Production server
   paths:
     # Endpoints здесь
   components:
     schemas:
       # Модели данных здесь
     responses:
       # Используем общие responses из shared/common/responses.yaml
   ```

2. **Обязательные элементы:**
   - ✅ OpenAPI версия: 3.0.3
   - ✅ Info блок с описанием и ссылкой на .BRAIN
   - ✅ Servers блок
   - ✅ Paths со всеми endpoints
   - ✅ Components/schemas со всеми моделями
   - ✅ Примеры запросов/ответов
   - ✅ Использовать общие responses через $ref
   - ✅ Валидация параметров

3. **Документация:**
   - Описание каждого endpoint с деталями
   - Описание каждой модели с примерами
   - Примеры использования
   - Описание бизнес-правил

### Шаг 5: Валидация и проверка

**Что проверить:**

1. **Валидация YAML:**
   - Синтаксис YAML корректен
   - OpenAPI спецификация валидна (используй валидатор)

2. **Проверка полноты:**
   - Все endpoints из документации включены
   - Все модели данных описаны
   - Все бизнес-правила отражены (TTK, части тела, типы урона, проникновение)

3. **Проверка соответствия:**
   - API соответствует документации из `.BRAIN`
   - Соответствует принципам проекта (SOLID, DRY, KISS)
   - Соответствует стандартам OpenAPI 3.0.3

4. **Проверка связности:**
   - Связанные API совместимы (weapons, abilities, implants)
   - Нет конфликтов с существующими API

5. **Проверка ограничения размера файла:**
   - Файл не должен превышать 400 строк
   - Если превышает - разбить на несколько файлов

---

## 🎨 Принципы и правила

### Общие принципы проекта

1. **API First** - сначала спецификация, потом реализация
2. **RESTful API** - стандартные HTTP методы и коды ответов
3. **Версионирование** - семантическое версионирование API (v1.0.0)
4. **Документация** - все endpoints, модели и примеры должны быть задокументированы
5. **DRY** - использовать $ref для общих компонентов (responses.yaml)

### Стиль кодирования

1. **Именование:**
   - Endpoints: kebab-case (`/api/v1/gameplay/combat/shoot`)
   - Модели: PascalCase (`ShootRequest`, `DamageCalculationResult`)
   - Параметры: snake_case (`shooter_id`, `target_body_part`)

2. **Формат:**
   - YAML формат
   - Отступы: 2 пробела
   - Строки не длиннее 100 символов

3. **Документация:**
   - Все поля должны иметь `description`
   - Все enum должны иметь описания значений
   - Примеры обязательны для основных endpoints

### Правила из workspace rules

1. **SOLID** - одна фича - один файл
2. **DRY** - использовать общие компоненты через $ref
3. **KISS** - простота решений
4. **Данные в БД** - не хардкодить значения в API
5. **Обработка ошибок - МИНИМАЛИСТИЧНО:**
   - НЕ создавай множество разных типов ошибок
   - Используй ЕДИНУЮ модель Error из shared/common/responses.yaml
   - Backend определяет HTTP статус по коду ошибки
6. **Именование моделей - ИЗБЕГАЙ КОНФЛИКТОВ:**
   - НЕ используй имена встроенных классов (Character, String, Error)
   - Используй префиксы: GameCharacter, ShootRequest
7. **Разбиение файлов:**
   - Paths (endpoints) ВСЕГДА в главном файле
   - Components (schemas, responses) МОЖНО выносить через $ref

---

## 📝 Примеры и шаблоны

### Пример структуры endpoint:

```yaml
paths:
  /gameplay/combat/shoot:
    post:
      summary: Выполнить выстрел
      description: |
        Выполняет выстрел из указанного оружия в указанную цель.
        Учитывает тип оружия, целевую часть тела, модификаторы движения,
        импланты и все остальные факторы для расчета урона.
      operationId: shoot
      tags:
        - Combat
        - Shooting
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ShootRequest'
            examples:
              headshot:
                summary: Выстрел в голову
                value:
                  shooter_id: "player_123"
                  weapon_id: "weapon_pistol_001"
                  target_id: "enemy_456"
                  target_body_part: "head"
                  aim_point:
                    x: 10.5
                    y: 2.3
                    z: 5.1
                  is_moving: false
              moving_shot:
                summary: Выстрел в движении
                value:
                  shooter_id: "player_123"
                  weapon_id: "weapon_assault_rifle_001"
                  target_id: "enemy_456"
                  target_body_part: "torso"
                  aim_point:
                    x: 12.0
                    y: 2.0
                    z: 8.5
                  is_moving: true
      responses:
        '200':
          description: Успешный выстрел
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ShootResult'
              examples:
                hit_headshot:
                  summary: Попадание в голову
                  value:
                    hit: true
                    body_part_hit: "head"
                    damage_dealt: 180
                    damage_type: "physical"
                    is_critical: true
                    modifiers_applied:
                      - modifier_type: "body_part_multiplier"
                        modifier_value: 2.0
                      - modifier_type: "critical_hit"
                        modifier_value: 1.5
                    target_status:
                      hp_remaining: 0
                      is_dead: true
                miss_example:
                  summary: Промах
                  value:
                    hit: false
                    body_part_hit: null
                    damage_dealt: 0
                    damage_type: "physical"
                    is_critical: false
                    modifiers_applied: []
                    target_status:
                      hp_remaining: 500
                      is_dead: false
        '400':
          $ref: 'api/v1/shared/common/responses.yaml#/components/responses/BadRequest'
        '404':
          $ref: 'api/v1/shared/common/responses.yaml#/components/responses/NotFound'
        '409':
          $ref: 'api/v1/shared/common/responses.yaml#/components/responses/Conflict'
```

### Пример структуры модели:

```yaml
components:
  schemas:
    ShootResult:
      type: object
      required:
        - hit
        - damage_dealt
        - damage_type
        - is_critical
      properties:
        hit:
          type: boolean
          description: Попал ли выстрел в цель
          example: true
        body_part_hit:
          type: string
          enum: [head, torso, arms, legs, cyber_head, cyber_torso, cyber_arms, cyber_legs]
          description: |
            Часть тела, в которую попал выстрел.
            Органические части: head (x2 урон), torso (x1), arms/legs (x0.7)
            Кибер-части: cyber_head (x1.5), cyber_torso (x0.8), cyber_arms/legs (x0.5)
          example: "head"
        damage_dealt:
          type: number
          minimum: 0
          description: Фактический нанесенный урон с учетом всех модификаторов
          example: 180
        damage_type:
          type: string
          enum: [physical, energy, chemical, thermal, emp, cyber, poison, electromagnetic]
          description: |
            Тип урона:
            - physical: Кинетическое оружие
            - energy: Лазеры, плазма
            - chemical: Яд (только органика)
            - thermal: Огненный урон
            - emp: Только кибер-части
            - cyber: Специальный урон по кибер-частям
            - poison: Отравление (органика)
            - electromagnetic: Электромагнитное оружие
          example: "physical"
        is_critical:
          type: boolean
          description: Критическое попадание
          example: true
        modifiers_applied:
          type: array
          description: Примененные модификаторы урона
          items:
            type: object
            properties:
              modifier_type:
                type: string
                example: "body_part_multiplier"
              modifier_value:
                type: number
                example: 2.0
        target_status:
          type: object
          description: Статус цели после выстрела
          properties:
            hp_remaining:
              type: number
              minimum: 0
              example: 0
            is_dead:
              type: boolean
              example: true
```

---

## 🔗 Связанные API и зависимости

### Зависимости (API, которые должны быть созданы/обновлены раньше):

- `api/v1/shared/common/responses.yaml` - существует (стандартные ответы)

### Связанные API (ссылаются друг на друга):

- `api/v1/gameplay/combat/abilities.yaml` - способности влияют на стрельбу
- `api/v1/gameplay/combat/implants.yaml` - импланты влияют на точность/урон
- `api/v1/gameplay/progression/skills.yaml` - навыки влияют на штрафы при движении
- `api/v1/gameplay/economy/equipment-matrix.yaml` - характеристики оружия

### Зависимости (задания, которые должны быть выполнены раньше):

- none (фундаментальная механика)

---

## ✅ Критерии приемки

Задание считается выполненным, если:

1. ✅ **Файл создан:** `api/v1/gameplay/combat/shooting.yaml` существует
2. ✅ **OpenAPI валидный:** Файл проходит валидацию OpenAPI 3.0.3
3. ✅ **Все endpoints:** Все 6 endpoints описаны (shoot, calculate-damage, get weapon, damage-modifiers, reload, cover-penetration)
4. ✅ **Все модели:** Все 8 моделей данных описаны (ShootRequest, ShootResult, DamageCalculationRequest, DamageCalculationResult, Weapon, BodyPartModifiers, CoverPenetrationRequest, CoverPenetrationResult)
5. ✅ **Документация:** Все элементы задокументированы с описаниями
6. ✅ **Примеры:** Примеры запросов/ответов включены для основных endpoints
7. ✅ **Соответствие:** API соответствует документации из `.BRAIN/02-gameplay/combat/combat-shooting.md`
8. ✅ **Валидация:** Все параметры имеют валидацию (required, enum, minimum, maximum)
9. ✅ **Ошибки:** Используются общие responses через $ref
10. ✅ **Размер файла:** Не превышает 400 строк (если превышает - разбить)
11. ✅ **DRY:** Использованы общие компоненты через $ref
12. ✅ **Именование:** Избегаются конфликты имен (не используются встроенные классы)

---

## ❓ Возможные вопросы и ответы

**Q: Что делать, если файл превысит 400 строк?**
A: Разбить на несколько файлов:
- shooting.yaml - основные endpoints (shoot, reload)
- damage-calculation.yaml - расчет урона
- cover-system.yaml - система укрытий и проникновения

**Q: Как обрабатывать критические попадания?**
A: Критический урон рассчитывается backend на основе характеристик оружия и навыков персонажа. API возвращает is_critical флаг и итоговый урон.

**Q: Как интегрировать с системой имплантов?**
A: Импланты влияют на характеристики оружия (точность, урон) через модификаторы. Backend применяет эти модификаторы при расчете урона. API принимает target_implants для расчета защиты.

**Q: Нужно ли версионирование с первого дня?**
A: Да, использовать семантическое версионирование. Начать с v1.0.0.

**Q: Как обрабатывать разные TTK для разных зон?**
A: Endpoint calculate-damage принимает zone_type параметр. Backend применяет соответствующие модификаторы TTK для зоны.

---

## 📞 Дополнительная информация

**Если возникли вопросы:**
- Проверить связанные документы в `.BRAIN/02-gameplay/combat/`
- Посмотреть примеры существующих API в `API-SWAGGER/api/v1/`
- Проверить правила в `.cursor/rules/api-swagger-rules.mdc`
- Проверить ARCHITECTURE.md для структуры директорий

**Полезные ссылки:**
- [OpenAPI Specification 3.0.3](https://swagger.io/specification/)
- [REST API Design Best Practices](https://restfulapi.net/)
- [API-SWAGGER Architecture](../../ARCHITECTURE.md)

---

## 📊 История выполнения

- `2025-11-06 23:00` - Задание создано
- _Назначение агенту: pending_
- _Начало выполнения: pending_
- _Завершение: pending_

---

**ВНИМАНИЕ:** Это задание для AI агента Cursor. Выполняй его пошагово, следуя всем инструкциям выше.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

