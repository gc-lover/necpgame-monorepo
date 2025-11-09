# [АРХИВ] Класс: Рокербой — Последний рубеж (2077)

**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** Устаревшие D&D элементы после pivot на shooter; хранится как исторический пример.

> ⚠️ Shooter pivot: не использовать в текущем бэклоге, следует переписать под shooter-шаблон.

---

## 1. Синопсис
Night City на грани. Корпоративные войны. Банды против корпораций. Relic кризис. Виктор Вектор видит решение — объединяющий концерт. Рокербой организует легендарное выступление, объединяет фракции через музыку, защищает событие от атак. Performance + social + combat квест для класса Rockerboy.

- ID: `quest-class-rockerboy-2077-final-stand`
- Тип: `side`
- Дает: `npc-victor-vector`
- Уровень: 9
- Класс: Rockerboy
- Эпоха: 2060-2077
- Локации: `loc-downtown-001` (большая арена)

## 2. Цели и ветвления
1) Принять задание у Виктора.
2) Подготовить арену для концерта.
3) Объединить 5 фракций через музыку (переговоры).
4) Провести финальный концерт.
5) Защитить от атаки врагов (9 врагов).
6) Вернуться к Виктору.

Ветвления:
- COOL 19 — объединить город (успешно убедить все фракции, +XP 160, +репутация 20).
- PERFORMANCE 19 — легендарный концерт (+XP 180, эффект на весь город).
- TACTICS 18 — защита арены (уменьшение врагов на 4).

## 3. D&D-параметры
- Инициатива: d20 + REF.
- AC: 14–16 (саботажники), 17 (корпо-киллеры).
- Урон: d8+3 (SMG), d10+4 (grenades), d12+5 (heavy weapons).
- Состояния: «Вдохновение» (толпа, +2 к performance), «Хаос» (паника, −2 к cool), «Звуковой барьер» (защита от звуковых атак).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ ВИКТОР ВЕКТОР                          │
│ «Город разрывается. Объедини музыкой.» │
│ [Принять] [Какие фракции?]             │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (28 узлов)

NODE[start]: Виктор → «2077. Кризис. Концерт может объединить город.»
- [Принять] → NODE[accept]
- [Какие фракции?] → NODE[explain_factions]

NODE[explain_factions]: «NCPD, Valentinos, Maelstrom, Nomads, Corpo-представители.»
- [Понял, делаю] → NODE[accept]

NODE[accept]: «Арена зарезервирована. Объедини фракции.»
- [Подготовить арену] → NODE[prepare_venue]

NODE[prepare_venue]: «Арена. Звук, свет, сцена.»
- [Проверить оборудование] → NODE[equipment_check]

NODE[equipment_check]: «Оборудование работает. Начинать переговоры?»
- [Да] → NODE[faction_1_ncpd]

NODE[faction_1_ncpd]: NCPD (Сара Миллер) → «Концерт? В кризис?»
- [COOL 19 Убедить] → success NODE[ncpd_agree] / fail NODE[ncpd_refuse]
- [Предложить безопасность] → NODE[ncpd_deal]

NODE[ncpd_agree]: «NCPD поддерживает концерт.» (+фракция)
- [Следующая фракция] → NODE[faction_2_valentinos]

NODE[ncpd_refuse]: «NCPD отказывается.»
- [Продолжить] → NODE[faction_2_valentinos]

NODE[ncpd_deal]: «NCPD согласны обеспечить порядок.» (+фракция)
- [Следующая фракция] → NODE[faction_2_valentinos]

NODE[faction_2_valentinos]: Valentinos (Марко) → «Музыка для всех?»
- [COOL check] → success NODE[valentinos_agree] / fail NODE[valentinos_refuse]

NODE[valentinos_agree]: «Valentinos поддерживают.» (+фракция)
- [Следующая фракция] → NODE[faction_3_maelstrom]

NODE[valentinos_refuse]: «Valentinos отказываются.»
- [Продолжить] → NODE[faction_3_maelstrom]

NODE[faction_3_maelstrom]: Maelstrom → «Концерт? Хаос лучше.»
- [COOL check Убедить] → success NODE[maelstrom_agree] / fail NODE[maelstrom_fight]

NODE[maelstrom_agree]: «Maelstrom согласны (удивительно).» (+фракция)
- [Следующая фракция] → NODE[faction_4_nomads]

NODE[maelstrom_fight]: «Maelstrom угрожают. Отбиться.» (мини-бой 3 врага)
- win → NODE[maelstrom_respect]
- lose → NODE[maelstrom_refuse]

NODE[maelstrom_respect]: «Уважение завоёвано.» (+фракция)
- [Следующая фракция] → NODE[faction_4_nomads]

NODE[maelstrom_refuse]: «Maelstrom отказываются.»
- [Продолжить] → NODE[faction_4_nomads]

NODE[faction_4_nomads]: Nomads (Анна) → «Концерт в городе?»
- [Пригласить] → NODE[nomads_agree]

NODE[nomads_agree]: «Nomads согласны прийти.» (+фракция)
- [Следующая фракция] → NODE[faction_5_corpo]

NODE[faction_5_corpo]: Corpo (Элизабет) → «Arasaka наблюдает.»
- [COOL check] → success NODE[corpo_agree] / fail NODE[corpo_neutral]

NODE[corpo_agree]: «Corpo-представители присутствуют.» (+фракция)
- [Провести концерт] → NODE[concert_prep]

NODE[corpo_neutral]: «Corpo нейтральны.»
- [Провести концерт] → NODE[concert_prep]

NODE[concert_prep]: «Концерт начинается. Толпа собирается.»
- [PERFORMANCE 19 Легендарное выступление] → success NODE[concert_legendary] / fail NODE[concert_good]

NODE[concert_legendary]: «ЛЕГЕНДАРНЫЙ КОНЦЕРТ! Город объединён!» (+XP 180, эффект на город)
- [Продолжить] → NODE[attack_incoming]

NODE[concert_good]: «Хороший концерт. Толпа довольна.»
- [Продолжить] → NODE[attack_incoming]

NODE[attack_incoming]: «АТАКА! Саботажники пытаются сорвать концерт!»
- [TACTICS 18 Защита] → success NODE[defense_prepared] / fail NODE[defense_weak]

NODE[defense_prepared]: «Оборона готова.» (−4 врага)
- [Защитить] → NODE[combat]

NODE[defense_weak]: «Оборона слабая.»
- [Защитить] → NODE[combat]

NODE[combat]: Бой (9 врагов если weak, 5 если prepared, AC 14–17)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «Атака отражена. Концерт завершён триумфально!»
- [Вернуться к Виктору] → NODE[return]

NODE[combat_lost]: «Концерт сорван. Медпункт.» (штраф −20% награды)
- [Попытаться снова] → NODE[concert_prep]

NODE[return]: Виктор → «Легенда! Город объединён музыкой!»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +1050 XP, +1350 eddies, +репутация Independent 45 (+20 если все фракции согласны).

## 6. Награды и баланс
- База: 1050 XP, 1350 eddies, +репутация Independent 45.
- Бонусы: COOL/PERFORMANCE/TACTICS успехи дают +160–180 XP, уменьшают врагов, объединяют город.
- Штрафы: провал −20% денег; отказы фракций −эффективность события; боевые потери.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-class-rockerboy-2077-final-stand/accept`
- POST `/api/v1/skill-check` (cool 19, performance 19, tactics 18)
- POST `/api/v1/combat/start` (саботажники, 5–9 врагов; мини-бои)
- POST `/api/v1/quests/quest-class-rockerboy-2077-final-stand/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-class-rockerboy-2077-final-stand`.
- Objectives: accept → prepare-venue → unite-factions (×5) → perform-final → defend-event (9) → return.

## 9. Логи/Тесты
- Логи: faction negotiations, performance roll, skill-check результаты, combat.
- Тесты: все фракции согласны; часть отказывается; легендарный концерт; провал защиты.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (28 узлов).

