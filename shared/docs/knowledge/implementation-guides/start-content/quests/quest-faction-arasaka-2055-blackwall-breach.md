---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 20:30
**api-readiness-notes:** Полная спецификация квеста 2055 «Брешь в Blackwall» с 26 узлами диалогов. Соответствует `quest-faction-arasaka-2055-blackwall-breach`.
---

# 2055 — Брешь в Blackwall (Blackwall Breach)


---

## 1. Синопсис
Arasaka обнаружила брешь в Blackwall в секторе Downtown. AI-сущности просачиваются. Элизабет Чен от имени корпорации просит исследовать аномалию, сразиться с AI прокси и запечатать брешь. Netrunning + combat квест с высоким риском.

- ID: `quest-faction-arasaka-2055-blackwall-breach`
- Тип: `main`
- Дает: `npc-elizabeth-chen`
- Уровень: 7
- Эпоха: 2045-2060
- Локации: `loc-downtown-001`
- Фракция: Arasaka

## 2. Цели и ветвления
1) Принять задание у Элизабет.
2) Сканировать брешь (опасно, возможен кибераттак).
3) Сразиться с AI прокси (6 врагов).
4) Запечатать брешь (tech/netrunning).
5) Вернуться к Элизабет.

Ветвления:
- INT 17 — анализ бреши (+XP, улучшение понимания AI).
- NETRUNNING-COMBAT 18 — быстрая герметизация (уменьшение врагов на 3, +XP 120).
- COOL 16 — сопротивление кибершоку (защита от статусов).

## 3. D&D-параметры
- Инициатива: d20 + INT (для netrunning боя), d20 + REF (для физического боя).
- AC: 15 (AI combat proxy), 17 (Advanced AI guardians).
- Урон: d8+4 (energy attacks), d10+5 (neural damage).
- Состояния: «Кибершок» (stun), «Перегрев» (heat damage over time), «Системный сбой» (penalty to actions).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ ЭЛИЗАБЕТ ЧЕН                           │
│ «Брешь растёт. AI прорываются. Действуй.»│
│ [Принять] [Что за AI?]                 │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (26 узлов)

NODE[start]: Элизабет → «Blackwall дал трещину. Arasaka нужен специалист.»
- [Принять задание] → NODE[accept]
- [Что за AI?] → NODE[explain_ai]
- [Почему я?] → NODE[why_me]

NODE[explain_ai]: «За Blackwall — AI из эпохи DataKrash. Опасные. Не дай им закрепиться.»
- [Понял, принимаю] → NODE[accept]

NODE[why_me]: «У тебя есть навыки. И репутация. Arasaka платит хорошо.»
- [Хорошо, делаю] → NODE[accept]

NODE[accept]: «Задание принято. Координаты переданы.»
- [Отправиться к бреши] → NODE[travel]

NODE[travel]: Сектор Downtown → «Помехи в воздухе. Статика. Брешь видна.»
- [Подойти ближе] → NODE[approach]

NODE[approach]: «Брешь пульсирует. Скан начат.»
- [INT 17 Анализ бреши] → success NODE[analyze_success] / fail NODE[analyze_fail]
- [Сканировать стандартно] → NODE[scan_basic]

NODE[analyze_success]: «Брешь формировалась 3 дня. AI-сигнатуры идентифицированы.» (+XP 100, +данные)
- [Продолжить] → NODE[scan_done]

NODE[analyze_fail]: «Брешь нестабильна. Данных недостаточно.»
- [Продолжить] → NODE[scan_done]

NODE[scan_basic]: «Скан завершён. Брешь активна.»
- [Данные собраны] → NODE[scan_done]

NODE[scan_done]: «AI прокси обнаружены. Приближаются.»
- [Приготовиться] → NODE[combat_prep]

NODE[combat_prep]: «6 AI прокси. Netrunning бой начинается.»
- [NETRUNNING-COMBAT 18 Оверклок] → success NODE[combat_reduced] / fail NODE[combat_full]
- [Атаковать] → NODE[combat_full]

NODE[combat_reduced]: «Оверклок успешен. 3 прокси отключены сразу.» (бой 3 врага)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_full]: Бой (6 врагов, AC 15, нейроуроны d10+5)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «AI прокси деактивированы. Брешь открыта.»
- [COOL 16 Сопротивление кибершоку] → success NODE[seal_start_safe] / fail NODE[seal_start_risk]
- [Начать герметизацию] → NODE[seal_start_risk]

NODE[combat_lost]: «Медпункт. Нейроповреждения.» (штраф −15% награды)
- [Попытаться снова] → NODE[combat_prep]

NODE[seal_start_safe]: «Психика защищена. Герметизация безопасна.»
- [Запечатать брешь] → NODE[seal_process]

NODE[seal_start_risk]: «Риск кибершока. Герметизация рискованная.»
- [Запечатать брешь] → NODE[seal_process]

NODE[seal_process]: «Брешь запечатывается...» (d20 check: 12+ success, иначе partial)
- success → NODE[seal_complete]
- fail → NODE[seal_partial]

NODE[seal_complete]: «Брешь полностью закрыта. AI не проникнут.» (+максимум награды)
- [Вернуться к Элизабет] → NODE[return]

NODE[seal_partial]: «Брешь частично закрыта. Потребуется мониторинг.» (−10% XP)
- [Вернуться к Элизабет] → NODE[return]

NODE[return]: Элизабет → «Хорошая работа. Arasaka довольна.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +850 XP, +1100 eddies, +`item-blackwall-data`, +репутация Arasaka 30.

## 6. Награды и баланс
- База: 850 XP, 1100 eddies, `item-blackwall-data`, +репутация Arasaka 30.
- Бонусы: INT/NETRUNNING-COMBAT/COOL успехи дают +100–120 XP, уменьшают врагов, защищают от статусов.
- Штрафы: поражение −15% денег; частичная герметизация −10% XP.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-faction-arasaka-2055-blackwall-breach/accept`
- POST `/api/v1/skill-check` (int 17, netrunning-combat 18, cool 16)
- POST `/api/v1/combat/start` (AI prокси, 3–6 врагов)
- POST `/api/v1/quests/quest-faction-arasaka-2055-blackwall-breach/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-faction-arasaka-2055-blackwall-breach`.
- Objectives: accept → scan-breach → combat-ai (6) → seal-breach → return.

## 9. Логи/Тесты
- Логи: skill-check результаты, AI combat, seal attempts.
- Тесты: netrunner путь (overklock); силовой; смешанный; провал герметизации.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (26 узлов).

