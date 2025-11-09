---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** MQ-2077-001 "Dogtown Checkpoint" — 21 узел, проверка конкордатов, боевые/социально-правовые проверки, последствия в 2077-актах.
---

# MQ-2077-001: "Dogtown Checkpoint"

ID: `MQ-2077-001`  
Эпоха: 2077  
Тип: Main Quest  
Классы: Solo/Lawman/Politician (фокус), Netrunner (сопровождение)  
Фракции: Barghest / NUSA / Корпы / Купола  
Уровень: 34–38

Кратко: На контрольно-пропускном пункте Dogtown вступают в силу разные конкордаты. Игрок решает проход дипломатического груза при противоречивых нормах.


---

## Диалоговое дерево (21 узел)

1. Брифинг (NUSA/Barghest).  
   Social DC 20: получить расширенный мандат
2. Проверка конкордатов (из 2060–2077).  
   Автоматические модификаторы DC
3. Корп-курьер спорит о юрисдикции.  
   Legal DC 22: утвердить норму | Failure → эскалация
4. Скан имплантов охраной.  
   Medtech DC 20: снизить инвазию | Failure → конфликт с гражданскими
5. Хакерский шум в сетях.  
   Hacking DC 22: локализовать | Failure → blackout (Combat DC 22)
6. Альтернативы пропуска:  
   A) Дипломатический иммунитет  
   B) Полный досмотр  
   C) Тихий коридор  
   D) Отказ
7. A: Иммунитет.  
   Social DC 22, Legal DC 22 | Success → быстрый проход, -Корпы
8. B: Досмотр.  
   Tech/Medtech DC 20: аккуратно | Failure → скандал
9. C: Тихий коридор.  
   Stealth DC 22 + Deception DC 22 | Risk heat
10. D: Отказ.  
    Intimidation DC 22 | Failure → бой
11. Barghest требует свою долю.  
    Trader DC 20: договор | Failure → минус репутация
12. Гражданские протестуют.  
    Media DC 20: снивелировать
13. Контр-хак корп.  
    Hacking DC 22: отражение | Failure → утечка
14. Столкновение патруля.  
    Combat DC 22 | Критуспех → элитный лут
15. Подделка пропусков обнаружена.  
    Investigation DC 20: найти источник
16. Внешний приказ NUSA.  
    Politician DC 20: перевести стрелки
17. Переговоры финальные.  
    Group Check team threshold 3
18. Итог решения (A/B/C/D).  
    Фиксация флагов
19. Репорт в конкордатный совет.  
    Reputation изменения
20. Побочные вызовы (hook).  
    WE-2077-***
21. Финал.

## Репутация
- A: +NUSA +15, -Корпы -10, +Barghest +5
- B: +DAO +10, +Жители +10, -Корпы -5
- C: +Корпы +10, -NetWatch -5, Heat +2
- D: +Barghest +10, -NUSA -10, -Жители -10

## Лут
- Элитные пропуски, тактические модули, эдди 400–1200

## Последствия
- Акты 2077: DC модифицируются по исходам; шанс городских протестов ↑ при B/D

## JSON-структура (пример)
```json
{
  "questId": "MQ-2077-001",
  "title": "Dogtown Checkpoint",
  "era": "2077",
  "type": "main",
  "classes": {"primary": ["Solo", "Lawman", "Politician"], "secondary": ["Netrunner"], "cooperative": true},
  "factions": {"primary": ["NUSA", "Barghest"], "secondary": ["Corpos", "DAO"]},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "skillChecks": [],
  "reputationChanges": {"A": {"NUSA": 15, "Corpos": -10, "Barghest": 5}, "B": {"DAO": 10, "Citizens": 10, "Corpos": -5}, "C": {"Corpos": 10, "NetWatch": -5, "heat": 2}, "D": {"Barghest": 10, "NUSA": -10, "Citizens": -10}},
  "loot": {"eddy": {"min": 400, "max": 1200}, "tables": ["TacticalModules", "Passes"]},
  "consequences": {"actModifiers": {"2077": {"dc": {"A": -2, "B": -1, "C": +2, "D": +3}}}}
}
```


