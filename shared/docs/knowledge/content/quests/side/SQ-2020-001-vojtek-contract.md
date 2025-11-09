---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2020-001 "Контракт Войтека" — 20 узлов, боевые/социальные проверки, репутация Fixer/Gangs, лут.
---

# SQ-2020-001: "Контракт Войтека"

Эпоха: 2020–2030  
Классы: Solo/Fixer (фокус), Netrunner/Techie (поддержка)  
Фракции: Fixers, Maelstrom, Valentinos  
Уровень: 6–10

Кратко: Фиксер Войтек предлагает отжать у Maelstrom партию имплантов и продать конкурирующей сети клиник. Баланс между силой, стелсом и переговорами.


---

## Диалоговое дерево (20 узлов)

1. Войтек даёт вводные. Social DC 16: узнать детали → 2  
2. Выбор подхода: Сила (Solo) | Стелс (Stealth) | Договор (Social) → 3/4/5  
3. Сила: Рейд на склад. Combat DC 18 → 6  
4. Стелс: Проникновение. Stealth DC 18, Hacking DC 16 → 6  
5. Договор: Связной Maelstrom. Social/Intimidation DC 18 → 6  
6. Узел безопасности. Tech DC 16: отключить ловушки | Fail → Combat  
7. Контакт клиники-конкурента. Trader DC 16: аванс  
8. Засада Valentinos. Combat DC 18 | Social DC 18: разрулить  
9. Непредвиденная проверка NetWatch. Hacking DC 16: замести следы | Fail → heat  
10. Раздел лута с Войтеком. Deception DC 18 | Trader DC 16  
11. Maelstrom мстит. Combat DC 18 | Stealth DC 18  
12. Партия имплантов повреждена. Tech DC 16: восстановить  
13. Альтернатива: продать в серый рынок. Trader DC 18 | Risk: -Fixers  
14. Альтернатива: передать в Trauma Team. Social DC 18 | +Medtech  
15. Полиция нюхом чует сделку. Lawman DC 16: отвести  
16. Доставка: путь A (склад) / B (тоннели) / C (дроны)  
17. Инцидент на доставке. Random check → Combat/Stealth/Hacking  
18. Финальные переговоры. Group check threshold 2  
19. Выплата и доли. Trader DC 16: +eddy  
20. Финал: флаги репутации и будущих контрактов.

## Репутация
- +Fixers до +12 (по пути переговоров/чистоты сделки)
- +Valentinos до +5 (если договор) | -Maelstrom до -10 (силой)

## Лут
- Эдди 300–900, импланты (common/rare), доступ к контрактам Fixer

## JSON (фрагмент)
```json
{
  "questId": "SQ-2020-001",
  "title": "Контракт Войтека",
  "era": "2020-2030",
  "type": "side",
  "classes": {"primary": ["Solo", "Fixer"], "secondary": ["Netrunner", "Techie"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "skillChecks": [],
  "reputationChanges": {"Fixers": {"max": 12}, "Maelstrom": {"min": -10}, "Valentinos": {"max": 5}},
  "loot": {"eddy": {"min": 300, "max": 900}, "tables": ["ImplantsCommon", "ImplantsRare"]}
}
```
