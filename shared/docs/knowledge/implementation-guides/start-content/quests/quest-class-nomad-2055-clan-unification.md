---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 21:05
**api-readiness-notes:** Полная спецификация Nomad квеста 2055 «Объединение кланов» с 26 узлами диалогов. Соответствует `quest-class-nomad-2055-clan-unification`.
---

# [АРХИВ] Класс: Номад — Объединение клана (2055)

**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** Содержит D&D проверки, используется лишь как историческая справка.

> ⚠️ Shooter pivot: при необходимости переосмысления использовать shooter-шаблон и `combat-shooter-core.md`.

# 2055 — Объединение кланов (Clan Unification)


---

## 1. Синопсис
Badlands разделены. Кланы номадов враждуют. Анна Петрова видит угрозу — объединённые враги уничтожат разрозненные кланы. Номад путешествует между кланами, проводит переговоры, защищает встречу лидеров. Diplomacy + survival + combat квест для класса Nomad.

- ID: `quest-class-nomad-2055-clan-unification`
- Тип: `side`
- Дает: `npc-anna-petrova`
- Уровень: 7
- Класс: Nomad
- Эпоха: 2045-2060
- Локации: Badlands routes, `loc-watson-001`

## 2. Цели и ветвления
1) Принять задание у Анны.
2) Посетить 4 клана и провести переговоры.
3) Организовать встречу лидеров.
4) Защитить встречу от налёта врагов (8 врагов).
5) Доложить Анне об альянсе.

Ветвления:
- COOL 17 — дипломатия (объединить кланы успешно, +XP 120, +репутация Nomads 10).
- TACTICS 17 — тактическая защита встречи (уменьшение врагов на 4).
- SURVIVAL 16 — навигация Badlands (быстрее путешествие, −случайные встречи).

## 3. D&D-параметры
- Инициатива: d20 + REF.
- AC: 13–15 (налётчики), 16 (лидеры банд).
- Урон: d8+3 (rifles), d10+4 (heavy weapons).
- Состояния: «Недоверие кланов» (−2 к diplomacy checks), «Засада» (внезапная атака), «Пыльная буря» (−2 к perception).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ АННА ПЕТРОВА                           │
│ «Кланы разрознены. Объедини их.»       │
│ [Принять] [Какие кланы?]               │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (26 узлов)

NODE[start]: Анна → «Badlands опасны. Кланы враждуют. Объедини их.»
- [Принять] → NODE[accept]
- [Какие кланы?] → NODE[explain_clans]

NODE[explain_clans]: «Железные волки, Красные всадники, Песчаные змеи, Степные ястребы.»
- [Понял, еду] → NODE[accept]

NODE[accept]: «Маршруты переданы. Будь осторожен.»
- [Отправиться] → NODE[travel_clan1]

NODE[travel_clan1]: «Путь к Железным волкам.»
- [SURVIVAL 16 Навигация] → success NODE[arrive_fast_1] / fail NODE[arrive_slow_1]

NODE[arrive_fast_1]: «Прибытие быстрое. Без встреч.»
- [Встретить лидера] → NODE[meet_clan1]

NODE[arrive_slow_1]: «Путь долгий. Случайная встреча с scavs.» (мини-бой 2 врага)
- [Встретить лидера] → NODE[meet_clan1]

NODE[meet_clan1]: Лидер Железных волков → «Зачем ты здесь?»
- [COOL 17 Убедить в альянсе] → success NODE[clan1_agree] / fail NODE[clan1_refuse]
- [Предложить выгоду] → NODE[clan1_deal]

NODE[clan1_agree]: «Железные волки согласны.» (+клан)
- [Следующий клан] → NODE[travel_clan2]

NODE[clan1_refuse]: «Железные волки отказываются.»
- [Попытаться снова] → NODE[meet_clan1]
- [Следующий клан] → NODE[travel_clan2]

NODE[clan1_deal]: «Железные волки согласны за долю ресурсов.» (+клан, −ресурсы)
- [Следующий клан] → NODE[travel_clan2]

NODE[travel_clan2]: «Путь к Красным всадникам.»
- [Продолжить] → NODE[meet_clan2]

NODE[meet_clan2]: Лидер Красных всадников → «Почему мы должны доверять?»
- [COOL 17 Дипломатия] → success NODE[clan2_agree] / fail NODE[clan2_fight]

NODE[clan2_agree]: «Красные всадники согласны.» (+клан)
- [Следующий клан] → NODE[travel_clan3]

NODE[clan2_fight]: «Красные всадники требуют поединок.» (дуэль 1v1)
- win → NODE[clan2_respect]
- lose → NODE[clan2_refuse]

NODE[clan2_respect]: «Уважение завоёвано. Красные всадники согласны.» (+клан)
- [Следующий клан] → NODE[travel_clan3]

NODE[clan2_refuse]: «Красные всадники отказываются.»
- [Следующий клан] → NODE[travel_clan3]

NODE[travel_clan3]: «Путь к Песчаным змеям.»
- [Продолжить] → NODE[meet_clan3]

NODE[meet_clan3]: «Песчаные змеи рассматривают предложение.» (автоматическое согласие)
- [Следующий клан] → NODE[travel_clan4]

NODE[travel_clan4]: «Путь к Степным ястребам.»
- [Продолжить] → NODE[meet_clan4]

NODE[meet_clan4]: «Степные ястребы согласны при условии нейтральной встречи.» (+клан)
- [Организовать встречу] → NODE[summit_prep]

NODE[summit_prep]: «Встреча лидеров назначена.»
- [TACTICS 17 Подготовить оборону] → success NODE[summit_defended] / fail NODE[summit_vulnerable]

NODE[summit_defended]: «Оборона подготовлена.» (−4 врага)
- [Начать встречу] → NODE[summit_start]

NODE[summit_vulnerable]: «Оборона слабая.»
- [Начать встречу] → NODE[summit_start]

NODE[summit_start]: «Встреча началась. Налёт!»
- [Защитить] → NODE[combat]

NODE[combat]: Бой (8 врагов если vulnerable, 4 если defended, AC 13–16)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «Налёт отражён. Альянс заключён.»
- [Вернуться к Анне] → NODE[return]

NODE[combat_lost]: «Встреча сорвана. Медпункт.» (штраф −15% награды)
- [Попытаться снова] → NODE[summit_prep]

NODE[return]: Анна → «Кланы объединены. Badlands безопаснее.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +880 XP, +1100 eddies, +репутация Nomads 40 (+10 если все кланы согласны).

## 6. Награды и баланс
- База: 880 XP, 1100 eddies, +репутация Nomads 40.
- Бонусы: COOL/TACTICS/SURVIVAL успехи дают +120 XP, уменьшают врагов, ускоряют путешествие.
- Штрафы: провал −15% денег; отказы кланов −эффективность альянса; дуэль проигрыш −клан.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-class-nomad-2055-clan-unification/accept`
- POST `/api/v1/skill-check` (cool 17, tactics 17, survival 16)
- POST `/api/v1/combat/start` (налётчики, 4–8 врагов; дуэль 1v1)
- POST `/api/v1/quests/quest-class-nomad-2055-clan-unification/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-class-nomad-2055-clan-unification`.
- Objectives: accept → visit-clans (×4) → negotiate-alliance → defend-meeting (8) → return.

## 9. Логи/Тесты
- Логи: clan negotiations, duels, skill-check результаты, combat.
- Тесты: все кланы согласны; часть отказывается; дуэль проигрыш; summit защищён/провален.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (26 узлов).

