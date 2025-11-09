# MVP Initial Data — Reference Pack

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Content Guild  
**Связанные документы:** `schema.md`, `migrations.md`, `mvp-text-version-plan.md`

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** Определён обязательный набор данных для начальной загрузки MVP. Готово к переносу в Liquibase `V1_4__seed_reference_data.sql`.

---

## 1. Общий состав

- Справочники: происхождения, классы, фракции, районы Night City.
- Базовые крафт-чертежи (оружие MK I, импланты базового уровня).
- Туториальные квесты и NPC (ссылка на `main-quests-outline.md`).
- Базовые социальные заказы (по одному на тип).

## 2. Таблицы и значения

### 2.1 `ref_origin`
- `street_kid`
- `corpo`
- `nomad`

### 2.2 `ref_class`
- `solo`
- `netrunner`
- `techie`

### 2.3 `ref_faction`
- `arasaka`
- `militech`
- `valentinos`
- `maelstrom`
- `ncpd`

### 2.4 `world_district_state`
- `watson` → `unrest_level: 40`
- `westbrook` → `unrest_level: 55`
- `heywood` → `unrest_level: 35`

### 2.5 `crafting_blueprint`
- `smart_scope_mk1`
- `ricochet_rounds`
- `gyro_stabilizer`

### 2.6 `order`
- `tutorial-protect-convoy`
- `tutorial-hack-network`
- `tutorial-supply-run`

## 3. Формат поставки

- CSV/JSON файлы в `data/mvp-seed/`.
- Скрипты загрузки через Liquibase `loadData`.

## 4. Контроль

- [x] Проверить соответствие API контрактам (`/crafting/blueprints`, `/social/orders`).
- [x] Согласовать с Narrative (туториальные NPC).
- [ ] Подготовить автоматический smoke-тест (`BACK-JAVA`).

---

**Следующее действие:** выгрузить данные в `BACK-JAVA/src/main/resources/db/changelog/data/` и обновить CI.
