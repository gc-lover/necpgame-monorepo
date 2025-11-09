---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 20:40
**api-readiness-notes:** Полная спецификация квеста 2075 «Артефакт реальности» с 23 узлами диалогов. Соответствует `quest-side-2075-reality-artifact`.
---

# 2075 — Артефакт реальности (Reality Artifact)


---

## 1. Синопсис
«Грань намёков» — аномальные события в Night City. Элизабет обнаружила артефакт с необычными свойствами. Локация нестабильна. Охотники за артефактами уже в пути. Нужно исследовать, защитить и доставить артефакт.

- ID: `quest-side-2075-reality-artifact`
- Тип: `side`
- Дает: `npc-elizabeth-chen`
- Уровень: 9
- Эпоха: 2060-2077
- Локации: `loc-downtown-001`

## 2. Цели и ветвления
1) Принять задание у Элизабет.
2) Найти артефакт в аномальной зоне.
3) Исследовать свойства (риск психического воздействия).
4) Защитить от охотников (6 врагов).
5) Доставить Элизабет.

Ветвления:
- INT 18 — понять артефакт (+XP 140, дополнительные данные).
- PERCEPTION 17 — обнаружить охотников заранее (уменьшение врагов на 3).
- COOL 17 — сопротивление психическому воздействию (защита от статуса).

## 3. Shooter параметры
- Инициатива: `reaction_time = REF × 0.75`, модифицируется имплантом `neural_reflex`.
- Защита охотников: `defenseRating 210–225`, щиты с `psy_resistance 65`.
- Урон: пистолеты `dps 160`, винтовки `dps 210`, критический мультипликатор ×1.8.
- Состояния: «Психическое воздействие» (−15% accuracy), «Дезориентация» (дрейф камеры), «Галлюцинации» (спавн фантомов, урон фиктивный).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ ЭЛИЗАБЕТ ЧЕН                           │
│ «Артефакт нестабилен. Опасно. Нужен ты.»│
│ [Принять] [Что за артефакт?]           │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (23 узла)

NODE[start]: Элизабет → «Грань намёков активна. Артефакт найден.»
- [Принять] → NODE[accept]
- [Что за артефакт?] → NODE[explain]

NODE[explain]: «Неизвестного происхождения. Возможно, связан с Blackwall. Или чем-то за ним.»
- [Понял, иду] → NODE[accept]

NODE[accept]: «Координаты переданы. Будь осторожен.»
- [Отправиться] → NODE[travel]

NODE[travel]: Аномальная зона → «Воздух мерцает. Помехи в сенсорах.»
- [Войти в зону] → NODE[enter_zone]

NODE[enter_zone]: «Зона нестабильна. Артефакт виден.»
- [Подойти к артефакту] → NODE[approach_artifact]

NODE[approach_artifact]: «Артефакт излучает энергию. Психическое давление.»
- [COOL 17 Сопротивление] → success NODE[resist] / fail NODE[affected]
- [Исследовать] → NODE[investigate]

NODE[resist]: «Психика защищена.» (защита от статуса)
- [Исследовать] → NODE[investigate]

NODE[affected]: «Психическое воздействие!» (−3 к INT checks)
- [Исследовать] → NODE[investigate]

NODE[investigate]: «Артефакт активен. Данные собираются.»
- [INT 18 Анализ] → success NODE[analyze_success] / fail NODE[analyze_fail]

NODE[analyze_success]: «Артефакт — фрагмент Relic-технологии. Уникальный.» (+XP 140, +данные)
- [Забрать артефакт] → NODE[take_artifact]

NODE[analyze_fail]: «Артефакт непонятен. Данных недостаточно.»
- [Забрать артефакт] → NODE[take_artifact]

NODE[take_artifact]: «Артефакт взят. Охотники обнаружены!»
- [PERCEPTION 17 Заметить заранее] → success NODE[spot_hunters] / fail NODE[ambush]

NODE[spot_hunters]: «Охотники замечены. Засада предотвращена.» (−3 врага)
- [Приготовиться] → NODE[combat]

NODE[ambush]: «Засада! Охотники атакуют.»
- [Защищаться] → NODE[combat]

NODE[combat]: Бой (6 врагов если ambush, 3 если заметил, AC 13–15)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «Охотники нейтрализованы. Артефакт в безопасности.»
- [Покинуть зону] → NODE[exit_zone]

NODE[combat_lost]: «Медпункт. Артефакт почти потерян.» (штраф −15% награды)
- [Попытаться снова] → NODE[combat]

NODE[exit_zone]: «Зона покинута. Возвращение к Элизабет.»
- [Вернуться] → NODE[return]

NODE[return]: Элизабет → «Артефакт невероятен. Это прорыв.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +980 XP, +1280 eddies, +`item-relic-fragment`, +репутация Independent 25.

## 6. Награды и баланс
- База: 980 XP, 1280 eddies, `item-relic-fragment`, +репутация Independent 25.
- Бонусы: INT/PERCEPTION/COOL успехи дают +140 XP, уменьшают врагов, защищают от статусов.
- Штрафы: провал −15% денег; психическое воздействие −3 к INT checks.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-side-2075-reality-artifact/accept`
- POST `/api/v1/skill-check` (int 18, perception 17, cool 17)
- POST `/api/v1/combat/start` (охотники, 3–6 врагов)
- POST `/api/v1/quests/quest-side-2075-reality-artifact/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-side-2075-reality-artifact`.
- Objectives: accept → locate → investigate → defend (6) → return.

## 9. Логи/Тесты
- Логи: skill-check результаты, psychic effects, combat.
- Тесты: INT успех; PERCEPTION успех; провал resistance; полный провал.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (23 узла).

