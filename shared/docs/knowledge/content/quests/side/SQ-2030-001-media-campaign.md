---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2030-001 "Медиа-кампания" — 20 узлов, медиа/соцпроверки, влияние на DAO/активистов.
---

# SQ-2030-001: "Медиа-кампания"

Эпоха: 2030–2045  
Классы: Rockerboy/Media (фокус), Politician (поддержка)  
Фракции: DAO, Активисты, Корпы  
Уровень: 14–18

Кратко: Организация кампании против корпоративной цензуры в куполах.


---

## Диалоговое дерево (20 узлов)

1. Брифинг инициаторов. Media DC 16  
2. Выбор нарратива: Свобода | Безопасность | Баланс  
3. Подготовка контента. Tech DC 16 (доставка)  
4. Контрпропаганда корпов. Investigation DC 18  
5. Дебаты с регуляторами. Social DC 18  
6. Площадка концерта. Logistics DC 16  
7. Безопасность: охрана/дроны. Solo/Tech DC 16  
8. Вирусный запуск. Media DC 18  
9. Юр-угрозы. Legal DC 18  
10. Срыв провокаторов. Stealth DC 18  
11. Прямой эфир. Group check threshold 2  
12. Эскалация: корпы давят. Politician DC 18  
13. Атака ботов. Hacking DC 18  
14. Пик кампании. Media DC 20  
15. Репортажи. Media DC 16  
16. Сбор пожертвований. Trader DC 16  
17. Поддержка DAO. Social DC 18  
18. Ответ активистов. Social DC 18  
19. Отчёт и метрики. Analysis DC 16  
20. Финал: флаги пути и модификаторы.

## Репутация
- +DAO +10, +Активисты +12 (при победе)
- -Корпы до -10 (жёсткая линия)

## Лут
- Эдди 200–700, медиа-ресурсы, доступ к DAO-каналам

## JSON (фрагмент)
```json
{
  "questId": "SQ-2030-001",
  "title": "Медиа-кампания",
  "era": "2030-2045",
  "type": "side",
  "classes": {"primary": ["Rockerboy", "Media"], "secondary": ["Politician"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"DAO": {"max": 10}, "Activists": {"max": 12}, "Corpos": {"min": -10}},
  "loot": {"eddy": {"min": 200, "max": 700}, "tables": ["MediaResources"]}
}
```
