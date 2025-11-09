---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2077-002 "Линия снабжения Dogtown" — 20 узлов, логистика/боевые/соц, Barghest/NUSA/корпы.
---

# SQ-2077-002: "Линия снабжения Dogtown"

Эпоха: 2077  
Классы: Trader/Nomad (фокус), Solo (поддержка)  
Фракции: Barghest, NUSA, Corpos  
Уровень: 34–38

Кратко: Проложить и удержать линию снабжения через опасные кварталы Dogtown.


---

## Диалоговое дерево (20 узлов)

1. Контракт Barghest. Trader DC 20  
2. Маршрут. Analysis DC 20  
3. Договор с местными. Social DC 20  
4. Пост Barghest. Intimidation DC 22  
5. Корп-разведка. Stealth DC 22  
6. Закупки. Trader DC 20  
7. Охрана. Combat DC 22  
8. Дроны-разведчики. Tech DC 20  
9. Засада. Combat DC 22  
10. Обход. Stealth DC 22  
11. Сбой связи. Hacking DC 22  
12. Топливная логистика. Logistics DC 20  
13. Инцидент с гражданскими. Media DC 20  
14. Переговоры финал. Group threshold 3  
15. Запуск линии.  
16. Мониторинг.  
17. Инцидент-2.  
18. Поставка.  
19. Отчёт.  
20. Финал.

## Репутация
- +Barghest +10, +NUSA +8, -Corpos до -8

## Лут
- Контракты, модули транспорта, эдди 500–1500

## JSON (фрагмент)
```json
{
  "questId": "SQ-2077-002",
  "title": "Линия снабжения Dogtown",
  "era": "2077",
  "type": "side",
  "classes": {"primary": ["Trader", "Nomad"], "secondary": ["Solo"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"Barghest": {"max": 10}, "NUSA": {"max": 8}, "Corpos": {"min": -8}},
  "loot": {"eddy": {"min": 500, "max": 1500}, "tables": ["TransportModules", "Contracts"]}
}
```
