---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2078-002 "Манипуляции на ярмарках" — 20 узлов, анти-манипуляции, хак/медиа/торговля.
---

# SQ-2078-002: "Манипуляции на ярмарках"

Эпоха: 2078–2093  
Классы: Netrunner/Trader (фокус), Media (поддержка)  
Фракции: Leagues, Corpos, CraftGuilds  
Уровень: 40–44

Кратко: Выявить и пресечь рыночные манипуляции в параметрических ярмарках.


---

## Диалоговое дерево (20 узлов)

1. Жалоба гильдии. Social DC 20  
2. Трассировка ботов. Hacking DC 22  
3. Анализ паттернов. Analysis DC 22  
4. Подстава. Deception DC 22  
5. Юр-позиция. Legal DC 22  
6. Медиа-прикрытие. Media DC 20  
7. Контратака корп. Hacking DC 22  
8. Блокировка каналов. Tech DC 22  
9. Эконом-метрики. Trader DC 22  
10. Переговоры. Group threshold 3  
11. Санкции. Politician DC 20  
12. Апелляция. Legal DC 22  
13. Пресс-релиз. Media DC 20  
14. Техническая очистка. Tech DC 22  
15. Восстановление цен. Trader DC 22  
16. Компенсации гильдиям. Social DC 20  
17. Мониторинг.  
18. Лицензирование.  
19. Метрики.  
20. Финал.

## Репутация
- +Leagues +12, +CraftGuilds +12, -Corpos до -10

## Лут
- Лицензии, рыночные баффы, эдди 700–1800

## JSON (фрагмент)
```json
{
  "questId": "SQ-2078-002",
  "title": "Манипуляции на ярмарках",
  "era": "2078-2093",
  "type": "side",
  "classes": {"primary": ["Netrunner", "Trader"], "secondary": ["Media"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"Leagues": {"max": 12}, "CraftGuilds": {"max": 12}, "Corpos": {"min": -10}},
  "loot": {"eddy": {"min": 700, "max": 1800}, "tables": ["Licenses", "MarketBuffs"]}
}
```
