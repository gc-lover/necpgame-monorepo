---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2045-002 "Картель эмиттеров" — 20 узлов, торговля/тех/юридика, влияние на локальные сети.
---

# SQ-2045-002: "Картель эмиттеров"

Эпоха: 2045–2060  
Классы: Trader/Techie (фокус), Legal (поддержка)  
Фракции: Local Emitters Consortium, Corpos  
Уровень: 22–26

Кратко: Разоблачить и расколоть картель производителей эмиттеров.


---

## Диалоговое дерево (20 узлов)

1. Сигнал от малого производителя. Social DC 16  
2. Сбор ценовых данных. Analysis DC 16  
3. Тех-экспертиза модулей. Tech DC 18  
4. Доказательства сговора. Investigation DC 18  
5. Корп-фронты. Stealth DC 18  
6. Контрпредложение картеля. Trader DC 18  
7. Юр-исковый пакет. Legal DC 18  
8. Медиа-выход. Media DC 16  
9. Взлом теневых чатов. Hacking DC 18  
10. Переговоры. Group threshold 2  
11. Раскол: дилеры/производители. Social DC 18  
12. Экстренное заседание консорциума. Politician DC 18  
13. Антимонопольный рейд. Lawman DC 18  
14. Замена модулей. Tech DC 18  
15. Контрактные бонусы. Trader DC 18  
16. Угроза саботажа. Combat DC 18  
17. Поддержка мелких. Grants  
18. Рынок стабилизирован.  
19. Метрики цен.  
20. Финал.

## Репутация
- +LocalEmitters +12, +Citizens +8, -Corpos -10

## Лут
- Контракты, компоненты, эдди 400–1000

## JSON (фрагмент)
```json
{
  "questId": "SQ-2045-002",
  "title": "Картель эмиттеров",
  "era": "2045-2060",
  "type": "side",
  "classes": {"primary": ["Trader", "Techie"], "secondary": ["Legal"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"LocalEmitters": {"max": 12}, "Corpos": {"min": -10}, "Citizens": {"max": 8}},
  "loot": {"eddy": {"min": 400, "max": 1000}, "tables": ["Contracts", "TechComponents"]}
}
```
