# [АРХИВ] Класс: Фиксер — Строитель сети (2035)

**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** Устарела D&D логика. Хранится как исторический артефакт после перехода на shooter-механику.

> ⚠️ Shooter pivot: использовать shooter-шаблон при выполнении новых задач.

---

## 1. Синопсис
Независимый Night City. Фиксер Марко видит возможность создать новую сеть контактов. Завербовать ключевых людей, организовать сделки, защитить растущую сеть от конкурентов. Social + negotiation + protection квест для класса Fixer.

- ID: `quest-class-fixer-2035-network-builder`
- Тип: `side`
- Дает: `npc-marco-fix`
- Уровень: 5
- Класс: Fixer
- Эпоха: 2030-2045
- Локации: `loc-downtown-001`, `loc-watson-001`

## 2. Цели и ветвления
1) Принять задание у Марко.
2) Завербовать 4 ключевых контакта (торговцы, риппердоки, info-brokers).
3) Организовать 3 сделки между контактами.
4) Защитить сеть от рейда конкурентов (5 врагов).
5) Доложить Марко о созданной сети.

Ветвления:
- COOL 16 — харизма сети (завербовать +2 контакта, +XP 80).
- STREETWISE 15 — знание улиц (заметить угрозы заранее, уменьшение врагов на 2).
- NEGOTIATION 16 — мастер сделок (увеличение прибыли от сделок, +деньги 200).

## 3. D&D-параметры
- Инициатива: d20 + REF.
- AC: 12–14 (конкуренты-фиксеры), 15 (наёмники конкурентов).
- Урон: d6+2 (pistols), d8+3 (SMG).
- Состояния: «Запугивание» (penalty −2 к cool checks), «Предательство» (один контакт может изменить).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ МАРКО ФИКС                             │
│ «Независимый город. Новые возможности.» │
│ [Принять] [Кто нужен в сети?]          │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (24 узла)

NODE[start]: Марко → «Независимость — время строить сети.»
- [Принять] → NODE[accept]
- [Кто нужен?] → NODE[explain_network]

NODE[explain_network]: «Торговцы оружия, риппердоки, info-brokers. Сделай сеть.»
- [Понял, делаю] → NODE[accept]

NODE[accept]: «Список контактов передан.»
- [Искать контакты] → NODE[recruit_phase]

NODE[recruit_phase]: «Первый контакт: Анна Петрова (риппердок).»
- [COOL 16 Убедить харизмой] → success NODE[recruit_anna_success] / fail NODE[recruit_anna_hard]
- [Предложить деньги] → NODE[recruit_anna_money]

NODE[recruit_anna_success]: «Анна убеждена. В сети.» (+контакт)
- [Следующий контакт] → NODE[recruit_2]

NODE[recruit_anna_hard]: «Анна сомневается. Нужны гарантии.»
- [Дать гарантии] → NODE[recruit_anna_guarantee]

NODE[recruit_anna_money]: «Анна согласна за плату.» (+контакт, −100 eddies)
- [Следующий контакт] → NODE[recruit_2]

NODE[recruit_anna_guarantee]: «Гарантии даны. Анна в сети.» (+контакт)
- [Следующий контакт] → NODE[recruit_2]

NODE[recruit_2]: «Второй контакт: Виктор Вектор (info-broker).»
- [COOL 16 Убедить] → success NODE[recruit_victor_ok] / fail NODE[recruit_victor_no]
- [Blackmail] → NODE[recruit_victor_blackmail]

NODE[recruit_victor_ok]: «Виктор в сети.» (+контакт)
- [Следующий контакт] → NODE[recruit_3]

NODE[recruit_victor_no]: «Виктор отказывается.»
- [Продолжить без него] → NODE[recruit_3]

NODE[recruit_victor_blackmail]: «Виктор вынужден согласиться.» (+контакт, −репутация 5)
- [Следующий контакт] → NODE[recruit_3]

NODE[recruit_3]: «Третий контакт: торговец оружия.»
- [Сделать предложение] → NODE[recruit_dealer]

NODE[recruit_dealer]: «Торговец согласен.» (+контакт)
- [Четвёртый контакт] → NODE[recruit_4]

NODE[recruit_4]: «Четвёртый контакт: tech-специалист.»
- [COOL check] → success NODE[recruit_tech_ok] / fail NODE[recruit_tech_no]

NODE[recruit_tech_ok]: «Tech в сети.» (+контакт)
- [Организовать сделки] → NODE[deals_phase]

NODE[recruit_tech_no]: «Tech отказался.»
- [Организовать сделки] → NODE[deals_phase]

NODE[deals_phase]: «Сделки между контактами начинаются.»
- [NEGOTIATION 16 Оптимизировать] → success NODE[deals_optimized] / fail NODE[deals_basic]

NODE[deals_optimized]: «Сделки оптимизированы. Прибыль +200 eddies.» (+деньги, +XP 80)
- [Продолжить] → NODE[threat_detected]

NODE[deals_basic]: «Сделки прошли стандартно.»
- [Продолжить] → NODE[threat_detected]

NODE[threat_detected]: «Конкуренты узнали о сети!»
- [STREETWISE 15 Заметить угрозу] → success NODE[threat_spotted] / fail NODE[ambush]

NODE[threat_spotted]: «Угроза замечена. Подготовка.» (−2 врага)
- [Защитить сеть] → NODE[combat]

NODE[ambush]: «Засада конкурентов!»
- [Защищаться] → NODE[combat]

NODE[combat]: Бой (5 врагов если ambush, 3 если spotted, AC 12–15)
- win → NODE[combat_won]
- lose → NODE[combat_lost]

NODE[combat_won]: «Конкуренты устранены. Сеть в безопасности.»
- [Доложить Марко] → NODE[return]

NODE[combat_lost]: «Медпункт. Сеть под угрозой.» (штраф −10% награды)
- [Попытаться снова] → NODE[combat]

NODE[return]: Марко → «Отлично! Сеть работает. Ты настоящий фиксер.»
- [Получить награду] → NODE[complete]

NODE[complete]: «Квест завершён.» +620 XP, +850 eddies, +`item-corporate-data`, +репутация Independent 25.

## 6. Награды и баланс
- База: 620 XP, 850 eddies, `item-corporate-data`, +репутация Independent 25.
- Бонусы: COOL/STREETWISE/NEGOTIATION успехи дают +80–200 eddies, уменьшают врагов, увеличивают XP.
- Штрафы: провал −10% денег; blackmail −репутация 5; отказы контактов −эффективность сети.

## 7. API эндпоинты
- POST `/api/v1/quests/quest-class-fixer-2035-network-builder/accept`
- POST `/api/v1/skill-check` (cool 16, streetwise 15, negotiation 16)
- POST `/api/v1/combat/start` (конкуренты, 3–5 врагов)
- POST `/api/v1/quests/quest-class-fixer-2035-network-builder/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-class-fixer-2035-network-builder`.
- Objectives: accept → recruit-contacts (×4) → setup-deals (×3) → protect-network (5) → return.

## 9. Логи/Тесты
- Логи: recruitment attempts, deals, skill-check результаты, combat.
- Тесты: полный успех (все контакты); провал recruitment; blackmail путь; combat потеря.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (24 узла).

