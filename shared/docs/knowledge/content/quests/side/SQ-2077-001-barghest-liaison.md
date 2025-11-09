---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2077-001 "Связной Barghest" — 20 узлов, Dogtown, Barghest/NUSA, боевые/социальные проверки.
---

# SQ-2077-001: "Связной Barghest"

Эпоха: 2077  
Классы: Lawman/Solo (фокус), Politician (поддержка)  
Фракции: Barghest, NUSA, Корпы  
Уровень: 34–38

Кратко: Наладить рабочий канал между Barghest и гражданскими поставщиками.


---

## Диалоговое дерево (20 узлов)

1. Брифинг NUSA. Politician DC 20  
2. Встреча с офицером Barghest. Social DC 20  
3. Проверка контрагента. Investigation DC 20  
4. Патрульные требования. Intimidation DC 22  
5. Корп-перехват. Stealth DC 22 / Combat DC 22  
6. Подпольный склад. Tech DC 20  
7. Договор поставок. Trader DC 20  
8. Юр-рамки. Legal DC 20  
9. Антикоррупционный фильтр. Analysis DC 20  
10. Гарантии безопасности. Combat DC 22 (демо)  
11. Инцидент на КПП. Social DC 20  
12. Давление NUSA. Politician DC 20  
13. Провокация корпов. Media DC 20  
14. Столкновение. Combat DC 22  
15. Медиа-нейтрализация. Media DC 20  
16. Финальные условия. Group threshold 3  
17. Подписи.  
18. Логистика запуска. Logistics DC 20  
19. Репорт.  
20. Финал.

## Репутация
- +Barghest +12, +NUSA +10, -Корпы до -8 (по пути)

## Лут
- Тактические модули, допуск, эдди 500–1500

## JSON (фрагмент)
```json
{
  "questId": "SQ-2077-001",
  "title": "Связной Barghest",
  "era": "2077",
  "type": "side",
  "classes": {"primary": ["Lawman", "Solo"], "secondary": ["Politician"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"Barghest": {"max": 12}, "NUSA": {"max": 10}, "Corpos": {"min": -8}},
  "loot": {"eddy": {"min": 500, "max": 1500}, "tables": ["TacticalModules", "Access"]}
}
```
