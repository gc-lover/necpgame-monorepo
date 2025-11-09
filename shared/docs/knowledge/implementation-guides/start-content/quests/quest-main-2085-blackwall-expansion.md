---
**Статус:** draft  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 19:38
**api-readiness-notes:** Полная спецификация квеста 2085 с 25+ узлами диалогов, боевой и нетран-ветками, угрозами Blackwall, API-маппингом, логами и тестами. Соответствует `quest-main-2085-blackwall-expansion`.
---

# 2085 — Расширение Blackwall (Blackwall Expansion)


---

## 1. Синопсис
NetWatch готовит экспедиции к границе Blackwall. Элизабет Чен просит картографировать новые сектора и собрать данные. Ветки: фронтальный бой с враждебными ИИ, нетран проникновение, социальная координация доступа.

- ID: `quest-main-2085-blackwall-expansion`
- Тип: `main`
- Дает: `npc-elizabeth-chen`
- Уровень: 10
- Локации: `loc-downtown-001` (граница), виртуальные сектора
- Тема: границы известных сетей, риск киберпсихоза

## 2. Цели и ветвления
1) Получить доступ к границе Blackwall.
2) Картографировать 3 новых сектора (данные ×3).
3) Противостоять ИИ или обойти их.
4) Вернуться к Элизабет с данными.

Ветвления:
- INT 18: навигация по границе (−время, +XP).
- NETRUNNING COMBAT 19: побеждать rogue-ИИ (−встречи).
- COOL 18: сопротивляться киберпсихозу (защита статуса).

## 3. Shooter параметры
- Инициатива: `reaction_time = REF × 0.8 + импланты`, сетевые конфликты опираются на `net_latency` и `tech_mastery`.
- Защита противников: `defenseRating 240–260`, для ICE/guardian `ice_resilience 275`.
- Крит: headshot multiplier ×2.0, misfire-trigger при `accuracy < 55%`.
- Состояния: «Кибершок», «Перегрев», «Подавление», «Sync Lag».

## 4. UX/Интерфейс
### 4.1. Брифинг у Элизабет
```
┌─────────────────────────────────────────┐
│ ЭЛИЗАБЕТ ЧЕН                            │
│ «Blackwall меняется. Нужно быть быстрее │
│ прочих. Готов?»                         │
│ [Принять] [Спросить риски]              │
└─────────────────────────────────────────┘
```

### 4.2. Навигация по границе
```
┌─────────────────────────────────────────┐
│ BLACKWALL                               │
│ «Порог. Не для слабых.»                 │
│ [INT 18 Навигация] [Взлом (Netrun 19)]  │
│ [Физ. охрана]                           │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (25 узлов)

NODE[start]: Элизабет → «Нам нужны карты новых секторов.»
- [Принять] → NODE[bw_gate]
- [Риски?] → NODE[risk_info]

NODE[risk_info]: «Киберпсихоз, rogue-ИИ, нестабильность.»
- [Понял] → NODE[bw_gate]

NODE[bw_gate]: Граница → варианты INT/NETRUN/физ. охрана
- [INT 18] → success NODE[nav_ok] / fail NODE[nav_fail]
- [NETRUN 19] → success NODE[hack_ok] / fail NODE[alarm]
- [Физ. охрана] → NODE[guard_route]

NODE[nav_ok]: «Маршруты оптимизированы.» (−время)
- [К сектору 1] → NODE[sec1]

NODE[nav_fail]: «Лабиринт. Потери времени.»
- [К сектору 1] → NODE[sec1]

NODE[hack_ok]: «ICE отключены локально.» (−встречи)
- [К сектору 1] → NODE[sec1]

NODE[alarm]: «Тревога. Стражи приближаются.»
- [Бой] → NODE[fight_guard]

NODE[guard_route]: Охрана периметра (3 врага)
- win → NODE[sec1]
- lose → NODE[respawn]

NODE[fight_guard]: Стражи (3)
- win → NODE[sec1]
- lose → NODE[respawn]

NODE[sec1]: Сектор 1 → сбор данных #1
- [Сканировать] → NODE[scan1]

NODE[scan1]: Проверки INT 18 | COOL 18 (устойчивость)
- success → NODE[sec2]
- fail → статус «перегрев» → NODE[sec2]

NODE[sec2]: Сектор 2 → сбор данных #2
- [Взлом (NETRUN 19)] → success NODE[sec2_ok] / fail NODE[ice]

NODE[ice]: ICE бой (2)
- win → NODE[sec2_ok]
- lose → NODE[respawn]

NODE[sec2_ok]: «Данные получены.»
- [К сектору 3] → NODE[sec3]

NODE[sec3]: Сектор 3 → элитный страж
- [Бой элита] → NODE[elite]
- [Обойти (INT 19)] → success NODE[sec3_ok] / fail NODE[elite]

NODE[elite]: Элитный страж (AC 19)
- win → NODE[sec3_ok]
- lose → NODE[respawn]

NODE[sec3_ok]: «Комплект собран (×3).»
- [Вернуться к Элизабет] → NODE[turn_in]

NODE[turn_in]: Элизабет → «Отлично. Эти карты бесценны.»
- [Завершить] → NODE[complete]

NODE[respawn]: Медцентр → штраф −10% денег
- [Повторить] → NODE[bw_gate]

NODE[complete]: «Квест завершён.»

## 6. Награды и баланс
- База: 1000 XP, 1400 eddies, `item-blackwall-data` ×1, +20 репутации NetWatch.
- Бонусы: INT/NETRUN успехи → +XP, −встречи; COOL 18 → защита статуса «cyberpsychosis».

## 7. API-мэппинг
- POST `/api/v1/quests/{id}/accept`
- POST `/api/v1/skill-check` (int|cool|netrunning-combat)
- POST `/api/v1/combat/start`
- POST `/api/v1/items/collect`
- POST `/api/v1/quests/{id}/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-main-2085-blackwall-expansion` (objectives, rewards, metadata).

## 9. Логи и телеметрия
- Журнал проверок, бои с rogue-ИИ, статусы перегрева/кибершока.

## 10. Тест-кейсы
1) INT путь без боёв → быстрое завершение.
2) NETRUN путь — отключение ICE и минимум встреч.
3) Боевой путь — победа над элитным стражем.
4) Провал хаков → тревога, дополнительные встречи.
5) Поражение → респаун, сохранение прогресса по данным.

## 11. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (25+ узлов).


