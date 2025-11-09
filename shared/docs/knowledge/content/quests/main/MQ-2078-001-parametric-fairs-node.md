---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** MQ-2078-001 "Параметрические ярмарки" — 20 узлов, экономические/социальные проверки, лут-экономика, кросс-эпохи.
---

# MQ-2078-001: "Параметрические ярмарки"

ID: `MQ-2078-001`  
Эпоха: 2078–2093  
Тип: Main Quest  
Классы: Trader/Fixer/Politician (фокус), Techie/Media (поддержка)  
Фракции: Городские лиги / Корп-альянсы / Гильдии ремесленников  
Уровень: 38–44

Кратко: Город запускает серию параметрических ярмарок — гибкие рынки с динамическими тарифами. Игрок выбирает алгоритмы допуска, анти-манипуляционные меры, квоты для ремесленников.


---

## Диалоговое дерево (20 узлов)

1. Совет ярмарки.  
   Trader DC 22: получить место в дирекции
2. Фиксер-альянс предлагает "серые" биржи.  
   Deception DC 22: маскировать | Failure → аудит
3. Ремесленники требуют квоты.  
   Social DC 20: компромисс
4. Корпы требуют привилегии.  
   Legal DC 22: ограничить влияние | Failure → монополизация
5. Алгоритмы допуска.  
   Analysis DC 22: сбалансировать правила
6. Анти-манипуляционные меры.  
   Tech DC 22 + Media DC 20: защита и прозрачность
7. Альтернативы дизайна:  
   A) Ремесленный приоритет  
   B) Корп-приоритет  
   C) Баланс  
   D) Свободный рынок
8. A: Квоты ремесленников.  
   Trader DC 20, Social DC 20
9. B: Преференции корпов.  
   Legal DC 22, Deception DC 22
10. C: Баланс.  
    Analysis DC 22, Tech DC 22, Trader DC 22 (2 из 3)
11. D: Свободный рынок.  
    Intimidation DC 22 (продавить), Risk: волатильность
12. Медиа-кампания.  
    Media DC 20: репутация
13. Атака манипуляторов.  
    Hacking DC 22: отразить | Failure → взрывы цен
14. Распределение тарифов.  
    Trader DC 22: оптимизация дохода
15. Проверка качества.  
    Tech DC 20: отбор
16. Открытие ярмарки — первый день.  
    Group Check team threshold 3
17. Отчёт лигам.  
    Reputation изменения
18. Экономические эффекты.  
    Loot (eddy, лицензии)
19. Экспорт в соседние купола.  
    Logistics DC 20
20. Финал пути (A/B/C/D).

## Репутация
- A: +CraftGuilds +20, +Citizens +10, -Corpos -10
- B: +Corpos +20, -CraftGuilds -10, -Citizens -5
- C: +Leagues +15, +Corpos +10, +Citizens +10
- D: +Traders +20, волатильность (heat +2), -Regulators -10

## Лут
- Лицензии (редкость зависит от пути), эдди 600–1800, экономические баффы

## Последствия
- 2090+: устойчивость экономики (A/C), монополизация (B), высокая волатильность (D)

## JSON-структура (пример)
```json
{
  "questId": "MQ-2078-001",
  "title": "Параметрические ярмарки",
  "era": "2078-2093",
  "type": "main",
  "classes": {"primary": ["Trader", "Fixer", "Politician"], "secondary": ["Techie", "Media"], "cooperative": true},
  "factions": {"primary": ["Leagues"], "secondary": ["Corpos", "CraftGuilds"]},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "skillChecks": [],
  "reputationChanges": {"A": {"CraftGuilds": 20, "Citizens": 10, "Corpos": -10}, "B": {"Corpos": 20, "CraftGuilds": -10, "Citizens": -5}, "C": {"Leagues": 15, "Corpos": 10, "Citizens": 10}, "D": {"Traders": 20, "heat": 2, "Regulators": -10}},
  "loot": {"eddy": {"min": 600, "max": 1800}, "tables": ["Licenses", "MarketBuffs"]},
  "consequences": {"eraModifiers": {"2090+": {"economyStability": {"A": 0.9, "B": 0.6, "C": 0.85, "D": 0.5}, "volatility": {"D": 0.4}}}}
}
```


