---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2060-001 "Серый театр" — 20 узлов, полит/медиа/юридические проверки, конкордатные последствия.
---

# SQ-2060-001: "Серый театр"

Эпоха: 2060–2077  
Классы: Fixer/Politician/Media (фокус)  
Фракции: Лиги, Корпы, Силовые  
Уровень: 28–32

Кратко: Операция влияния между куполами в серой зоне.


---

## Диалоговое дерево (20 узлов)

1. Брифинг клиента. Social DC 18  
2. Подкуп посредников. Trader DC 18  
3. Вброс в прессу. Media DC 18  
4. Юр-оценка рисков. Legal DC 20  
5. Контрвброс корпов. Investigation DC 20  
6. Угроза силовых. Intimidation DC 20  
7. Канал доставки. Logistics DC 18  
8. Хак-прикрытие. Hacking DC 20  
9. Тайная встреча. Stealth DC 20  
10. Дебрифинг союзников. Social DC 18  
11. Условный договор. Legal DC 20  
12. Непредвиденный инцидент. Random check  
13. Срыв провокации. Stealth DC 20  
14. Переговоры финал. Group threshold 3  
15. Медиа-релиз. Media DC 20  
16. Юр-фрейминг. Legal DC 20  
17. Откат санкций. Politician DC 20  
18. Финансовый отчёт. Analysis DC 18, эди  
19. Репутация распределена.  
20. Финал.

## Репутация
- +Лиги +12 | +Корпы +10 | +Силовые +10 (зависит от пути)

## Лут
- Эдди 500–1400, контракты, влияние

## JSON (фрагмент)
```json
{
  "questId": "SQ-2060-001",
  "title": "Серый театр",
  "era": "2060-2077",
  "type": "side",
  "classes": {"primary": ["Fixer", "Politician", "Media"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"Leagues": {"max": 12}, "Corpos": {"max": 10}, "Security": {"max": 10}},
  "loot": {"eddy": {"min": 500, "max": 1400}, "tables": ["Contracts", "Influence"]}
}
```
