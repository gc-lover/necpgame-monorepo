---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2030-002 "Купольный аудит" — 20 узлов, Legal/Tech, влияние на DAO и тарифы.
---

# SQ-2030-002: "Купольный аудит"

Эпоха: 2030–2045  
Классы: Legal/Techie (фокус), Politician (поддержка)  
Фракции: DAO, Корпы  
Уровень: 14–18

Кратко: Провести аудит сетевой и энергосистемы купола для корректировки тарифов и доступа.


---

## Диалоговое дерево (20 узлов)

1. Мандат DAO. Social DC 16  
2. Запрос логов. Legal DC 18  
3. Анализ энергопрофиля. Analysis DC 16  
4. Тех-осмотр узлов. Tech DC 16  
5. Корп-препятствия. Social/Legal DC 18  
6. Утечка информации. Hacking DC 18  
7. Баланс доступа. Ethics DC 18  
8. Интервью жителей. Media DC 16  
9. Юр-рамка тарифов. Legal DC 18  
10. Компенсации. Politician DC 16  
11. Контрэкспертиза. Investigation DC 18  
12. Конфликт интересов. Deception DC 18  
13. Протест. Social DC 18  
14. Испытания сети. Tech DC 18  
15. Резервные каналы. Hacking DC 18  
16. Публичный отчёт. Media DC 16  
17. Голосование DAO. Group threshold 2  
18. Внедрение. Tech/Legal DC 16  
19. Метрики.  
20. Финал.

## Репутация
- +DAO +12, -Корпы до -8 (при снижении привилегий)

## Лут
- Контракты, скидки, эдди 300–900

## JSON (фрагмент)
```json
{
  "questId": "SQ-2030-002",
  "title": "Купольный аудит",
  "era": "2030-2045",
  "type": "side",
  "classes": {"primary": ["Legal", "Techie"], "secondary": ["Politician"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"DAO": {"max": 12}, "Corpos": {"min": -8}},
  "loot": {"eddy": {"min": 300, "max": 900}, "tables": ["Contracts", "Discounts"]}
}
```
