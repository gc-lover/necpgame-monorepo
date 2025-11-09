---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2020-002 "Контракт Trauma Team" — 20 узлов, мед/боевые/соц проверки, репутация Trauma Team/гильдий рипдоков.
---

# SQ-2020-002: "Контракт Trauma Team"

Эпоха: 2020–2030  
Классы: Medtech/Lawman (фокус), Solo (поддержка)  
Фракции: Trauma Team, Clinics Guild, Gangs  
Уровень: 7–11

Кратко: Trauma Team просит обеспечить безопасный коридор для экстренной эвакуации из спорной зоны.


---

## Диалоговое дерево (20 узлов)

1. Брифинг Trauma Team. Medicine DC 16  
2. Оценка угроз. Investigation DC 16  
3. Маршрут А (улицы) / B (крыши) / C (подземка)  
4. Согласование с NCPD. Lawman DC 16  
5. Договор с гильдией рипдоков. Social DC 16 / Trader DC 16  
6. Засада банды. Combat DC 18  
7. Мобильная хирургия. Medicine DC 18  
8. Взлом дверей. Tech DC 16 / Hacking DC 16  
9. Конфликт интересов (платная эвакуация). Ethics DC 16  
10. Допуск к крыше. Stealth DC 16  
11. Срыв снайпера. Perception DC 16 / Combat DC 18  
12. Экстракция. Group threshold 2  
13. Претензия конкурирующей клиники. Legal DC 16  
14. Медиапокрытие. Media DC 16  
15. Повреждённый борт. Tech DC 16  
16. Раздел выплат. Trader DC 16  
17. Отчёт TT. Social DC 16  
18. Финал: флаги и доступы.

## Репутация
- +Trauma Team +10, +Clinics +8 (этика), -Gangs до -8

## Лут
- Мед-компоненты, TT-допуски, эдди 300–800

## JSON (фрагмент)
```json
{
  "questId": "SQ-2020-002",
  "title": "Контракт Trauma Team",
  "era": "2020-2030",
  "type": "side",
  "classes": {"primary": ["Medtech", "Lawman"], "secondary": ["Solo"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"TraumaTeam": {"max": 10}, "Clinics": {"max": 8}, "Gangs": {"min": -8}},
  "loot": {"eddy": {"min": 300, "max": 800}, "tables": ["MedComponents", "TTAccess"]}
}
```
