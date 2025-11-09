# Исправления временных несоответствий

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-06 23:52  
**Приоритет:** высокий

---

## Краткое описание

Решения для 3 найденных временных несоответствий в квестовой системе.

---

## Проблема 1: Voodoo Boys в 2020-2030

### Несоответствие
**Проблема:** Voodoo Boys упоминаются в квестах 2020-2030, но основаны только в 2033

**Затронутые квесты:**
- Любые упоминания Voodoo Boys до 2033
- MQ-2020-005 "Чистый канал" (выбор Voodoo Boys)

### Решение
**Вариант 1 (РЕКОМЕНДУЕМЫЙ): Proto-Voodoo**
- Переименовать фракцию в 2020-2032: "Proto-Voodoo" или "Haitian Underground"
- Указать что это предшественники Voodoo Boys
- В 2033: формальное основание Voodoo Boys из Proto-Voodoo

**Вариант 2: Перенос квестов**
- Перенести все Voodoo Boys квесты в 2033+
- Заменить в 2020-2030 на другую фракцию (например, независимые нетраннеры)

**Имплементация:**
```yaml
# Обновить квесты:
MQ-2020-005:
  choice_voodoo: "choice_proto_voodoo"
  npc_faction: "Proto-Voodoo (Haitian Underground)"

# Добавить в лор:
factions:
  Proto-Voodoo:
    founded: 2025
    disbanded: 2033
    successor: Voodoo Boys
```

---

## Проблема 2: Arasaka в период изгнания (2030-2039)

### Несоответствие
**Проблема:** Открытые квесты Arasaka 2030-2039, но корпорация изгнана из США (2023-2039)

**Затронутые квесты:**
- Все Arasaka faction quests 2030-2039
- Любые упоминания Arasaka в Night City

### Решение
**РЕКОМЕНДУЕМЫЙ: Тайные операции**

**Классификация квестов:**
1. **Открытые Arasaka квесты:** Только за пределами USA (Токио, Берлин, и т.д.)
2. **Тайные операции:** В USA через подставные компании
   - Требуют: High corpo skill (16+) или высокая репутация
   - Метка: `[CLASSIFIED]` или `[SHADOW OPS]`
   - Reward: Больше обычного (риск компенсируется)

**Имплементация:**
```yaml
# Квесты 2030-2039 в USA:
SQ-2030-ARASAKA-001:
  name: "[CLASSIFIED] Arasaka Shadow Op"
  region: "Night City"
  requirements:
    corpo_skill: 16
    OR:
      reputation_arasaka: 75
  note: "Arasaka действует через подставные компании"
  risk: high
  reward_multiplier: 1.5

# Квесты 2030-2039 вне USA:
SQ-2030-ARASAKA-002:
  name: "Arasaka Tokyo Operations"
  region: "Tokyo"
  requirements: normal
  note: "Arasaka открыто действует в Японии"
```

---

## Проблема 3: Центр Night City 2023-2031 (руины)

### Несоответствие
**Проблема:** Квесты в центре Night City 2023-2031, но центр в руинах после взрыва

**Затронутые квесты:**
- Квесты в Downtown/City Center 2023-2031
- Квесты в Corpo Plaza 2023-2062 (новая башня только в 2062)

### Решение
**РЕКОМЕНДУЕМЫЙ: Зоны опасности**

**Классификация локаций:**

**2023-2031: Downtown (DANGER ZONE)**
- Доступность: Ограничена
- Требования: 
  - Level 15+ (опасно)
  - Radiation protection (спецснаряжение)
  - NCPD permit (специальное разрешение)
- Квесты: 
  - Salvage operations (утилизация)
  - Rescue missions (поиск выживших)
  - Corp espionage (тайные операции в руинах)
- Reward multiplier: 2.0 (опасность компенсируется)

**2031-2062: Downtown (RECONSTRUCTION)**
- Доступность: Частичная
- Районы:
  - Rich districts: 2031+ (восстановлены)
  - Poor districts: 2040+ (частично)
  - Corpo Plaza: 2062+ (новая башня)

**Имплементация:**
```yaml
# Локации с временными ограничениями:
locations:
  night_city_downtown:
    periods:
      - range: "2023-2031"
        status: danger_zone
        requirements:
          level: 15
          items: ["radiation_suit"]
          permit: "NCPD_salvage_permit"
        quest_types: ["salvage", "rescue", "espionage"]
        reward_multiplier: 2.0
      
      - range: "2031-2040"
        status: reconstruction
        accessible_districts: ["rich"]
      
      - range: "2040-2062"
        status: partial_recovery
        accessible_districts: ["rich", "poor"]
      
      - range: "2062-2093"
        status: fully_recovered
        accessible_districts: ["all"]
        new_locations: ["Arasaka Tower 2.0", "Corpo Plaza"]

# Обновить квесты:
quests_2023_2031_downtown:
  add_tag: "DANGER_ZONE"
  add_requirement:
    location_permit: "NCPD_salvage"
  add_warning: "Радиоактивная зона - требуется защита"
```

---

## Имплементация исправлений

### Шаг 1: Обновить quest-system документы
- Добавить пометки [PROTO-VOODOO] для 2020-2032
- Добавить пометки [CLASSIFIED] для Arasaka 2030-2039
- Добавить пометки [DANGER ZONE] для центра 2023-2031

### Шаг 2: Обновить faction-evolution.yaml
```yaml
Voodoo_Boys:
  predecessor: "Proto-Voodoo (Haitian Underground)"
  proto_period: "2025-2032"
  official_founding: "2033"
  
Arasaka:
  periods:
    - range: "2020-2023"
      status: "active_war"
      regions: ["Night City", "Global"]
    - range: "2023-2030"
      status: "exiled_from_usa"
      regions: ["Tokyo", "Asia", "Europe"]
    - range: "2030-2039"
      status: "shadow_operations_usa"
      regions: ["Global + Shadow USA"]
    - range: "2039-2093"
      status: "fully_returned"
      regions: ["Global"]
```

### Шаг 3: Обновить location-timeline.yaml
```yaml
Night_City_Downtown:
  status_timeline:
    - period: "2020-2023"
      status: "active"
    - period: "2023-2031"
      status: "danger_zone"
      accessibility: "restricted"
    - period: "2031-2062"
      status: "reconstruction"
      accessibility: "partial"
    - period: "2062-2093"
      status: "fully_recovered"
      accessibility: "full"
```

---

## Влияние на игровой процесс

### Позитивные эффекты

1. **Историческая точность:** Лор согласован с Cyberpunk 2077
2. **Геймплей вариативность:** DANGER ZONE квесты = высокий риск/награда
3. **Репутация важнее:** Arasaka shadow ops требуют репутации
4. **Прогрессия:** Опасные зоны для high-level игроков

### Новые механики

**DANGER ZONE система:**
- Radiation damage (периодический урон)
- Hostile environment (сложнее лечиться)
- Limited visibility (туман/дым)
- Higher tier enemies (мутанты, сумасшедшие)
- Better loot (2x multiplier)

**Shadow Operations:**
- Classified quests (не показываются в обычном списке)
- High requirements (skill 16+ или reputation 75+)
- Better rewards (1.5x multiplier)
- Risk of exposure (провал = война с фракцией)

---

## Связанные документы

- [Timeline Check](./timeline-check.md) - где найдены проблемы
- [Narrative Master Arc](../narrative-master-arc.md) - главная линия
- [Faction Evolution](./faction-evolution.yaml) - будет обновлён

---

## История изменений

- v1.0.0 (2025-11-06 23:52) - Решения для 3 временных несоответствий

