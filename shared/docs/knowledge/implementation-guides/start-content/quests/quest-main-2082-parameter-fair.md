---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 20:45
**api-readiness-notes:** Полная спецификация квеста 2082 «Параметрическая ярмарка» с 24 узлами диалогов. Соответствует `quest-main-2082-parameter-fair`.
---

# 2082 — Параметрическая ярмарка (Parameter Fair)


---

## 1. Синопсис
Первая параметрическая ярмарка в Night City. Граждане голосуют за параметры мира — правила, законы, социальные нормы. Виктор Вектор просит помочь защитить мероприятие от саботажа и обеспечить честное голосование. Meta-governance + combat + social квест.

- ID: `quest-main-2082-parameter-fair`
- Тип: `main`
- Дает: `npc-victor-vector`
- Уровень: 10
- Эпоха: 2077-2093
- Локации: `loc-downtown-001`

## 2. Цели и ветвления
1) Принять задание у Виктора.
2) Посетить ярмарку и проголосовать за параметры.
3) Предотвратить саботаж (8 врагов).
4) Обеспечить честное голосование.
5) Доложить Виктору.

Ветвления:
- COOL 18 — повлиять на голосование (+XP 150, изменение параметров мира).
- PERCEPTION 19 — обнаружить саботаж заранее (уменьшение врагов на 4).
- INVESTIGATION 18 — найти организаторов саботажа (+репутация, +XP).

## 3. D&D-параметры
- Инициатива: d20 + REF.
- AC: 13–15 (саботажники), 16 (лидеры).
- Урон: d8+3 (pistols), d10+4 (SMG), d12+5 (explosives).
- Состояния: «Паника толпы» (массовый хаос), «Взрывы» (area damage), «Дезинформация» (неверное голосование).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ ВИКТОР ВЕКТОР                          │
│ «Ярмарка параметров. Важное событие.»  │
│ [Принять] [Что такое параметры?]       │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (24 узла)

NODE[start]: Виктор → «Параметрическая ярмарка. Первая в истории города.»
- [Принять] → NODE[accept]
- [Что такое параметры?] → NODE[explain]

NODE[explain]: «Граждане голосуют за правила мира. Законы. Налоги. Свободы. Это мета-уровень управления.»
- [Понял, иду] → NODE[accept]

NODE[accept]: «Хорошо. Встретимся на ярмарке.»
- [Отправиться] → NODE[travel]

NODE[travel]: Площадь Downtown → «Толпа. Стенды. Голосование идёт.»
- [Подойти к стенду] → NODE[vote_start]

NODE[vote_start]: «Голосование за параметры города.»
- [Проголосовать честно] → NODE[vote_honest]
- [COOL 18 Повлиять на голосование] → success NODE[vote_influence] / fail NODE[vote_honest]

NODE[vote_honest]: «Голос подан честно.»
- [Патрулировать] → NODE[patrol]

NODE[vote_influence]: «Влияние оказано. Параметры изменятся в пользу игрока.» (+XP 150, +изменение мира)
- [Патрулировать] → NODE[patrol]

NODE[patrol]: «Патруль площади. Всё спокойно?»
- [PERCEPTION 19 Осмотр] → success NODE[detect_sabotage] / fail NODE[no_warning]
- [INVESTIGATION 18 Поиск организаторов] → success NODE[find_leaders] / fail NODE[patrol_continue]

NODE[detect_sabotage]: «Замечены подозрительные группы. Подрывники!» (−4 врага)
- [Приготовиться] → NODE[combat_prep]

NODE[find_leaders]: «Лидеры саботажа идентифицированы.» (+репутация 20, +XP 100)
- [Задержать] → NODE[arrest_leaders]

NODE[no_warning]: «Взрыв!!!»
- [Защитить толпу] → NODE[combat_prep]

NODE[patrol_continue]: «Продолжить патруль.»
- [Дальше] → NODE[combat_prep]

NODE[arrest_leaders]: «Лидеры задержаны.» (−3 врага)
- [Зачистить остатки] → NODE[combat_prep]

NODE[combat_prep]: «Саботажники атакуют ярмарку!»
- [Бой] → NODE[combat]

NODE[combat]: Бой (8 врагов если no warning, 4 если detect, 5 если arrest, AC 13–16)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «Саботаж предотвращён. Голосование защищено.»
- [Вернуться к Виктору] → NODE[return]

NODE[combat_lost]: «Медпункт. Ярмарка под угрозой.» (штраф −15% награды)
- [Попытаться снова] → NODE[combat_prep]

NODE[return]: Виктор → «Ярмарка спасена. Параметры приняты.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +1100 XP, +1400 eddies, +репутация Independent 30.

## 6. Награды и баланс
- База: 1100 XP, 1400 eddies, +репутация Independent 30.
- Бонусы: COOL/PERCEPTION/INVESTIGATION успехи дают +100–150 XP, уменьшают врагов, влияют на мир.
- Штрафы: провал −15% денег; массовые взрывы +урон.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-main-2082-parameter-fair/accept`
- POST `/api/v1/skill-check` (cool 18, perception 19, investigation 18)
- POST `/api/v1/combat/start` (саботажники, 4–8 врагов)
- POST `/api/v1/quests/quest-main-2082-parameter-fair/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-main-2082-parameter-fair`.
- Objectives: accept → attend → vote → prevent-sabotage (8) → return.

## 9. Логи/Тесты
- Логи: skill-check результаты, voting choices, combat.
- Тесты: влияние на голосование; обнаружение саботажа; провал и взрывы; успех.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (24 узла).

