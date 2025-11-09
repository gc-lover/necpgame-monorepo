---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
**api-readiness-notes:** SQ-2060-002 "Пограничный аккорд" — 20 узлов, конкордаты, юр/соц/медиа проверки.
---

# SQ-2060-002: "Пограничный аккорд"

Эпоха: 2060–2077  
Классы: Politician/Legal (фокус), Media (поддержка)  
Фракции: Dome Leagues, Security  
Уровень: 28–33

Кратко: Соглашение о режимах пропуска между куполами.


---

## Диалоговое дерево (20 узлов)

1. Мандат лиги. Social DC 18  
2. Юр-проект. Legal DC 20  
3. Оценка угроз. Analysis DC 20  
4. Претензии силовых. Intimidation DC 20  
5. Баланс гражданских прав. Ethics DC 20  
6. Медиа-позиция. Media DC 20  
7. Учебная тревога. Combat DC 22  
8. Арбитраж. Legal DC 22  
9. Тайные каналы. Hacking DC 22  
10. Переговоры. Group threshold 3  
11. Конфликт интересов. Deception DC 20  
12. Поддержка жителей. Social DC 20  
13. Санкции. Trader DC 20  
14. Мигрантский коридор. Logistics DC 20  
15. Финализация норм. Legal DC 22  
16. Подписи.  
17. Пресс-релиз.  
18. Мониторинг.  
19. Метрики.  
20. Финал.

## Репутация
- +Leagues +12, +Citizens +10, +Security +8 (по балансу)

## Лут
- Контракты, доступы, эдди 500–1200

## JSON (фрагмент)
```json
{
  "questId": "SQ-2060-002",
  "title": "Пограничный аккорд",
  "era": "2060-2077",
  "type": "side",
  "classes": {"primary": ["Politician", "Legal"], "secondary": ["Media"], "cooperative": true},
  "dialogueTree": {"rootNode": 1, "nodes": []},
  "reputationChanges": {"Leagues": {"max": 12}, "Citizens": {"max": 10}, "Security": {"max": 8}},
  "loot": {"eddy": {"min": 500, "max": 1200}, "tables": ["Contracts", "Access"]}
}
```
