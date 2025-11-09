---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2078-001 "Войны лицензий" — 20 узлов, эконом/юрид/медиа проверки, экспорт.
---

# SQ-2078-001: "Войны лицензий"

Эпоха: 2078–2093  
Классы: Trader/Fixer (фокус), Legal/Media (поддержка)  
Фракции: Лиги, Корп-альянсы, Гильдии  
Уровень: 40–44

Кратко: Битва за экспортные лицензии между гильдиями и корп-альянсами.


---

## Диалоговое дерево (20 узлов)

1. Объявление тендера. Trader DC 22  
2. Подготовка заявки. Analysis DC 22  
3. Юр-рамки. Legal DC 22  
4. Контрпредложение корпов. Trader DC 22  
5. Антиманипуляционный аудит. Tech DC 22  
6. Медиа-кампания. Media DC 20  
7. Лобби гильдий. Social DC 20  
8. Саботаж конкурентов. Investigation DC 22  
9. Срыв саботажа. Stealth DC 22  
10. Защита каналов. Hacking DC 22  
11. Переговоры финал. Group threshold 3  
12. Дефицит поставок. Logistics DC 20  
13. Протекционизм соседей. Politician DC 20  
14. Коррекция тарифов. Trader DC 22  
15. Подписание лотов.  
16. Апелляции. Legal DC 22  
17. Медиа-отчет. Media DC 20  
18. Экспортный старт. Logistics DC 20  
19. Метрики экспорта.  
20. Финал.

## Репутация
- +Leagues +12, +CraftGuilds +12, -Corpos до -10 (по пути)

## Лут
- Лицензии, рыночные баффы, эдди 700–1800

## JSON (фрагмент)
```json
{
  "questId": "SQ-2078-001",
  "title": "Войны лицензий",
  "era": "2078-2093",
  "type": "side",
  "classes": {"primary": ["Trader", "Fixer"], "secondary": ["Legal", "Media"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"Leagues": {"max": 12}, "CraftGuilds": {"max": 12}, "Corpos": {"min": -10}},
  "loot": {"eddy": {"min": 700, "max": 1800}, "tables": ["Licenses", "MarketBuffs"]}
}
```
