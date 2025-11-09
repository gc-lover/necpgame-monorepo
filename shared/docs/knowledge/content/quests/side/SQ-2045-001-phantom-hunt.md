---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2045-001 "Охота на фантомов" — 20 узлов, сетевые/боевые проверки, Blackwall-риски.
---

# SQ-2045-001: "Охота на фантомов"

Эпоха: 2045–2060  
Классы: Netrunner/Techie (фокус), Solo (поддержка)  
Фракции: NetWatch, ИИ-культы  
Уровень: 22–26

Кратко: Очистка локальной сети от фантомов, проникших через эмиттер.


---

## Диалоговое дерево (20 узлов)

1. Запрос NetWatch. Social DC 16  
2. Скан сети. Hacking DC 18  
3. Ловушка в узле. Tech DC 18 / Stealth DC 18  
4. Фантом-пакет. Hacking DC 20  
5. ИИ-шёпот. Will DC 18  
6. Культовый маяк. Investigation DC 18  
7. Решение: уничтожить/изолировать/трассировать  
8. Бой с фантомами. Combat DC 20  
9. Ремонт сети. Tech DC 18  
10. Встреча с культом. Social/Intimidation DC 18  
11. Срыв ритуала. Stealth DC 18  
12. Атака из-за заслона. Hacking DC 22  
13. Внеплановый аудит. Legal DC 18  
14. Сбор артефактов. Loot roll  
15. Отчёт NetWatch. Media DC 16  
16. Контрпредложение культа. Deception DC 18  
17. Защита эмиттера. Tech DC 18, Combat DC 20  
18. Стабилизация. Analysis DC 16  
19. Метрики чистоты.  
20. Финал.

## Репутация
- +NetWatch +10 (полная очистка) | +ИИ-культ +8 (сделка)

## Лут
- Data, Tech компоненты, редкий модуль эмиттера

## JSON (фрагмент)
```json
{
  "questId": "SQ-2045-001",
  "title": "Охота на фантомов",
  "era": "2045-2060",
  "type": "side",
  "classes": {"primary": ["Netrunner", "Techie"], "secondary": ["Solo"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"NetWatch": {"max": 10}, "AIChorus": {"max": 8}},
  "loot": {"tables": ["DataRare", "TechComponents", "EmitterModule"]}
}
```
