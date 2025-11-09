---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 20:35
**api-readiness-notes:** Полная спецификация квеста 2065 «Серый театр» с 25 узлами диалогов. Соответствует `quest-main-2065-gray-theater`.
---

# [АРХИВ] Основной квест: Серый театр (2065)

**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** D&D-параметры устарели, документ хранится в архиве после перехода на shooter-механику.

> ⚠️ Shooter pivot: не использовать. Переписывать по shooter-шаблону при необходимости.

# 2065 — Серый театр (Gray Theater)


---

## 1. Синопсис
Прокси-война между Arasaka и Militech. NCPD просит помочь саботировать тайную операцию, которая может перерасти в открытый конфликт в Downtown. Stealth + sabotage + combat квест. Оффлайн-рейды и тактика.

- ID: `quest-main-2065-gray-theater`
- Тип: `main`
- Дает: `npc-sarah-miller`
- Уровень: 8
- Эпоха: 2060-2077
- Локации: `loc-downtown-001`, `loc-watson-001`

## 2. Цели и ветвления
1) Принять задание у Сары Миллер.
2) Проникнуть на корпоративный объект.
3) Саботировать операцию (украсть данные или уничтожить оборудование).
4) Устранить охрану (7 корпо-гвардейцев).
5) Уйти незамеченным.
6) Доложить Саре.

Ветвления:
- STEALTH 18 — тихое проникновение (уменьшение врагов на 4).
- TECH 17 — быстрый саботаж (−15% времени миссии).
- COOL 17 — стальные нервы (защита от panic статуса).

## 3. D&D-параметры
- Инициатива: d20 + REF.
- AC: 14–16 (корпо-гвардейцы), 17 (элитная охрана).
- Урон: d8+3 (SMG), d10+4 (assault rifles), d12+5 (heavy weapons).
- Состояния: «Паника» (penalty −2 к действиям), «Сигнализация» (+2 врага каждый ход), «Ослепление» (flashbangs).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ САРА МИЛЛЕР                            │
│ «Серый театр. Прокси-война. Останови.» │
│ [Принять] [Детали операции]            │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (25 узлов)

NODE[start]: Сара → «Корпы играют в войну. NCPD не может вмешаться официально.»
- [Принять] → NODE[accept]
- [Детали?] → NODE[details]

NODE[details]: «Arasaka складирует вооружение. Militech планирует налёт. Останови это до эскалации.»
- [Понял, делаю] → NODE[accept]

NODE[accept]: «Задание принято. Координаты объекта переданы.»
- [Отправиться] → NODE[travel]

NODE[travel]: Объект в Downtown → «Периметр патрулируется. Охрана усилена.»
- [STEALTH 18 Тихое проникновение] → success NODE[infiltrate_silent] / fail NODE[infiltrate_loud]
- [Силовой штурм] → NODE[infiltrate_loud]

NODE[infiltrate_silent]: «Незаметно внутри. Охрана не замечает.» (−4 врага)
- [Искать объект саботажа] → NODE[find_target]

NODE[infiltrate_loud]: «Сигнализация! Охрана поднята.» (+2 врага)
- [Прорываться] → NODE[find_target]

NODE[find_target]: «Склад вооружений. Серверная. Два пути саботажа.»
- [Украсть данные] → NODE[steal_data]
- [Уничтожить оборудование] → NODE[destroy_equipment]

NODE[steal_data]: «Доступ к серверной.»
- [TECH 17 Быстрая выгрузка] → success NODE[data_fast] / fail NODE[data_slow]

NODE[data_fast]: «Данные скопированы быстро.» (−15% времени, +XP 100)
- [Покинуть зону] → NODE[escape_prep]

NODE[data_slow]: «Данные скопированы медленно. Риск обнаружения.»
- [Покинуть зону] → NODE[escape_prep]

NODE[destroy_equipment]: «Размещение взрывчатки.»
- [Взорвать] → NODE[explosion]

NODE[explosion]: «ВЗРЫВ! Оборудование уничтожено. Тревога!» (+3 врага)
- [Эвакуация] → NODE[escape_prep]

NODE[escape_prep]: «Охрана приближается.»
- [COOL 17 Сохранять спокойствие] → success NODE[cool_head] / fail NODE[panic]
- [Бой] → NODE[combat]

NODE[cool_head]: «Нервы крепки. Маршрут эвакуации ясен.» (защита от паники)
- [Эвакуация] → NODE[combat]

NODE[panic]: «Паника!» (−2 к действиям)
- [Бой] → NODE[combat]

NODE[combat]: Бой (7 врагов если stealth failed, 3 если успешно, AC 14–16)
- win → NODE[escape_success]
- lose → NODE[escape_fail]

NODE[escape_success]: «Охрана нейтрализована. Уход чистый.»
- [Вернуться к Саре] → NODE[return]

NODE[escape_fail]: «Медпункт. Ранения.» (штраф −15% награды)
- [Попытаться снова] → NODE[escape_prep]

NODE[return]: Сара → «Отлично. Прокси-война остановлена. Пока.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +920 XP, +1200 eddies, +`item-corporate-data`, +репутация NCPD 22.

## 6. Награды и баланс
- База: 920 XP, 1200 eddies, `item-corporate-data`, +репутация NCPD 22.
- Бонусы: STEALTH/TECH/COOL успехи дают +100 XP, уменьшают врагов, защищают от статусов.
- Штрафы: провал −15% денег; сигнализация +враги; паника −2 к действиям.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-main-2065-gray-theater/accept`
- POST `/api/v1/skill-check` (stealth 18, tech 17, cool 17)
- POST `/api/v1/combat/start` (корпо-гвардейцы, 3–9 врагов)
- POST `/api/v1/quests/quest-main-2065-gray-theater/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-main-2065-gray-theater`.
- Objectives: accept → infiltrate → sabotage → eliminate (7) → escape → return.

## 9. Логи/Тесты
- Логи: skill-check результаты, stealth/combat, sabotage выбор.
- Тесты: чистый stealth; силовой; смешанный; провал и alarm.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (25 узлов).

