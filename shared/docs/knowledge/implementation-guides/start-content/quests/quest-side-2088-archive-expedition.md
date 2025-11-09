---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 20:50
**api-readiness-notes:** Полная спецификация квеста 2088 «Экспедиция за заслон» с 27 узлами диалогов. Соответствует `quest-side-2088-archive-expedition`.
---

# 2088 — Экспедиция за заслон (Archive Expedition)


---

## 1. Синопсис
Архивы за Blackwall. Элизабет Чен организует высокорисковую экспедицию для добычи артефактов из-за Blackwall. AI-сущности, безумие, потеря реальности — всё на кону. Netrunning + survival + combat квест.

- ID: `quest-side-2088-archive-expedition`
- Тип: `side`
- Дает: `npc-elizabeth-chen`
- Уровень: 11
- Эпоха: 2077-2093
- Локации: `loc-downtown-001` (точка входа)

## 2. Цели и ветвления
1) Принять задание у Элизабет.
2) Пересечь Blackwall (риск кибершока и безумия).
3) Сразиться с AI (10 врагов).
4) Забрать артефакты (×3).
5) Вернуться живым к Элизабет.

Ветвления:
- NETRUNNING-COMBAT 20 — выжить против AI (уменьшение врагов на 5, +XP 180).
- COOL 19 — сопротивление безумию (защита от insanity статуса).
- INT 19 — понять архивы (+данные, +XP 150).

## 3. Shooter параметры
- Инициатива: `reaction_time = 0.7 × REF + импланты`, для netrunner ветки учитывается `tech_latency`.
- Защита противников: `defenseRating 250`, `ice_resilience 285` для архивных стражей.
- Урон: `neural_dps = 180`, `reality_distortion = 240 burst`, мгновенный краш при `cool_resilience < 60`.
- Состояния: «Безумие» (random input), «Кибершок» (stun), «Reality Tear» (teleport + confusion).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ ЭЛИЗАБЕТ ЧЕН                           │
│ «Экспедиция смертельна. Но награда...» │
│ [Принять] [Опасности?]                 │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (27 узлов)

NODE[start]: Элизабет → «Архивы за Blackwall. Артефакты там бесценны.»
- [Принять] → NODE[accept]
- [Опасности?] → NODE[explain]
- [Почему я?] → NODE[why_me]

NODE[explain]: «AI-сущности. Безумие. Потеря реальности. Многие не вернулись.»
- [Всё равно иду] → NODE[accept]

NODE[why_me]: «Ты один из лучших. И достаточно безумен, чтобы попытаться.»
- [Хорошо] → NODE[accept]

NODE[accept]: «Снаряжение подготовлено. Координаты Blackwall переданы.»
- [Отправиться] → NODE[travel]

NODE[travel]: Точка входа → «Blackwall виден. Пульсирует. Ужасающий.»
- [Подойти] → NODE[approach_wall]

NODE[approach_wall]: «Blackwall перед тобой. Пересечь?»
- [COOL 19 Подавить страх] → success NODE[cross_safe] / fail NODE[cross_shaken]
- [Пересечь] → NODE[cross_shaken]

NODE[cross_safe]: «Страх подавлен. Пересечение безопасное.» (защита от insanity)
- [За Blackwall] → NODE[beyond]

NODE[cross_shaken]: «Страх проникает. Пересечение рискованное.» (уязвимость к insanity)
- [За Blackwall] → NODE[beyond]

NODE[beyond]: «За Blackwall. Реальность искажена. Архивы видны.»
- [INT 19 Анализ архивов] → success NODE[understand_archives] / fail NODE[archives_unknown]
- [Искать артефакты] → NODE[search_artifacts]

NODE[understand_archives]: «Архивы — база данных DataKrash эпохи. Бесценны.» (+XP 150, +данные)
- [Искать артефакты] → NODE[search_artifacts]

NODE[archives_unknown]: «Архивы непонятны. Просто забрать артефакты.»
- [Искать артефакты] → NODE[search_artifacts]

NODE[search_artifacts]: «Артефакты найдены. AI стражи активированы!»
- [Приготовиться] → NODE[combat_prep]

NODE[combat_prep]: «10 AI прокси атакуют.»
- [NETRUNNING-COMBAT 20 Оверклок] → success NODE[combat_reduced] / fail NODE[combat_full]
- [Бой] → NODE[combat_full]

NODE[combat_reduced]: «Оверклок успешен. 5 AI отключено сразу.» (бой 5 врагов, +XP 180)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_full]: Бой (10 врагов, AC 16–19, нейроуроны d10+5, reality distortion d12+6)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «AI нейтрализованы. Артефакты доступны.»
- [Забрать артефакты (×3)] → NODE[collect_artifacts]

NODE[combat_lost]: «Медпункт за Blackwall. Нейроповреждения тяжёлые.» (штраф −20% награды, риск insanity)
- [Попытаться снова] → NODE[combat_prep]

NODE[collect_artifacts]: «Артефакты собраны. Выход?»
- [COOL check Сохранить рассудок] → success NODE[return_safe] / fail NODE[return_mad]

NODE[return_safe]: «Рассудок сохранён. Возвращение чистое.»
- [Пересечь Blackwall назад] → NODE[cross_back]

NODE[return_mad]: «Безумие!!! Реальность разрушается.» (insanity status, случайные эффекты)
- [Попытаться вернуться] → NODE[cross_back]

NODE[cross_back]: «Пересечение Blackwall обратно. Реальность восстанавливается.»
- [Вернуться к Элизабет] → NODE[return]

NODE[return]: Элизабет → «Ты вернулся! Артефакты невероятны.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +1250 XP, +1600 eddies, +`item-blackwall-data` (×3), +репутация NetWatch 40.

## 6. Награды и баланс
- База: 1250 XP, 1600 eddies, `item-blackwall-data` (×3), +репутация NetWatch 40.
- Бонусы: NETRUNNING-COMBAT/COOL/INT успехи дают +150–180 XP, уменьшают врагов, защищают от безумия.
- Штрафы: провал −20% денег; insanity status; нейроповреждения.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-side-2088-archive-expedition/accept`
- POST `/api/v1/skill-check` (netrunning-combat 20, cool 19, int 19)
- POST `/api/v1/combat/start` (AI прокси, 5–10 врагов)
- POST `/api/v1/quests/quest-side-2088-archive-expedition/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-side-2088-archive-expedition`.
- Objectives: accept → cross-blackwall → fight-ai (10) → retrieve (×3) → return.

## 9. Логи/Тесты
- Логи: skill-check результаты, AI combat, insanity effects, artifacts.
- Тесты: netrunner успех; провал и безумие; полная потеря; успех с минимальными потерями.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (27 узлов).

