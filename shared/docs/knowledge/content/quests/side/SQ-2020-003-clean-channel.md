---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2020-003 "Чистый канал" — 20 узлов, Netrunner/Tech проверки, NetWatch/Blackwall влияние.
---

# SQ-2020-003: "Чистый канал"

Эпоха: 2020–2030  
Классы: Netrunner (фокус), Techie (поддержка)  
Фракции: NetWatch, Voodoo Boys  
Уровень: 8–12

Кратко: Очистить городской канал связи от "фантомных" паразитов.


---

## Диалоговое дерево (20 узлов)

1. Заявка NetWatch. Social DC 14  
2. Скан каналов. Hacking DC 16  
3. Ложные узлы. Investigation DC 16  
4. Отсечка паразита. Tech DC 16  
5. Вмешательство Voodoo. Social DC 16  
6. Решение: уничтожить / изолировать / передать Voodoo  
7. Эскалация. Hacking DC 18  
8. Тех-ремонт. Tech DC 16  
9. Инфошум. Media DC 14  
10. Блок подполья. Stealth DC 16  
11. Бой с ICE. Combat (net) DC 18  
12. Чистая трасса. Analysis DC 16  
13. Аудит. Legal DC 14  
14. Допуски. Access grant  
15. Протоколы. Writing DC 14  
16. Проверка. Group threshold 2  
17. Релиз канала.  
18. Репорт.  
19. Репутация.  
20. Финал.

## Репутация
- +NetWatch +10 (чистый канал) | +Voodoo +8 (если их путь)

## Лут
- Доступы, данные, эдди 200–700

## JSON (фрагмент)
```json
{
  "questId": "SQ-2020-003",
  "title": "Чистый канал",
  "era": "2020-2030",
  "type": "side",
  "classes": {"primary": ["Netrunner"], "secondary": ["Techie"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"NetWatch": {"max": 10}, "VoodooBoys": {"max": 8}},
  "loot": {"eddy": {"min": 200, "max": 700}, "tables": ["NetAccess", "Data"]}
}
```
