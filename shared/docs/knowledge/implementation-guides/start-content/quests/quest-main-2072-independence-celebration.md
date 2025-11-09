---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 20:10
**api-readiness-notes:** Полная спецификация квеста 2072 «Празднование независимости» с 24 узлами диалогов, ветками соц/расследование/бой. Соответствует `quest-main-2072-independence-celebration`.
---

# 2072 — Празднование независимости (Independence Celebration)


---

## 1. Синопсис
Найт-Сити отмечает годовщину независимости. Сара Миллер просит помочь обеспечить порядок: патрулировать Downtown, выявить заговорщиков, предотвратить атаки и завершить мероприятие без жертв.

- ID: `quest-main-2072-independence-celebration`
- Тип: `main`
- Дает: `npc-sarah-miller`
- Уровень: 8
- Локации: `loc-downtown-001`

## 2. Цели и ветвления
1) Принять задание у Сары.
2) Патрулировать площадь и прилегающие улицы.
3) Предотвратить инциденты (расследование угроз, деэскалация конфликтов, задержание).
4) Вернуться к Саре с отчётом.

Ветвления:
- PERCEPTION 16 — заранее раскрыть угрозы (уменьшить встречи).
- COOL 16 — деэскалация толпы/банд (+репутация NCPD, −бои).
- INVESTIGATION 15 — найти заговорщиков (+репутация NCPD, +XP).

## 3. D&D-параметры
- Инициатива: d20 + REF.
- AC: 12–14 (уличные нарушители), 15 (организаторы нападения).
- Состояния: «Подавление», «Ослепление» (светошум), «Страх» (после деэскалации).

## 4. UX экраны
```
┌─────────────────────────────────────────┐
│ САРА МИЛЛЕР                            │
│ «Нам нужна твоя помощь. Сегодня важно.» │
│ [Принять] [Спросить об угрозах]        │
└─────────────────────────────────────────┘
```

## 5. Диалоговое дерево (24 узла)

NODE[start]: Сара → «Готов?»
- [Принять] → NODE[p_patrol]
- [Угрозы?] → NODE[p_threats]

NODE[p_threats]: Сара → «Толпа, банды, возможный теракт.»
- [Понял] → NODE[p_patrol]

NODE[p_patrol]: Площадь → «Гул толпы.»
- [PERCEPTION 16 Осмотр] → success NODE[p_spot] / fail NODE[p_noise]
- [COOL 16 Деэскалация спора] → success NODE[p_cool_ok] / fail NODE[p_cool_fail]
- [INVESTIGATION 15 Поиск заговорщиков] → success NODE[p_invest_ok] / fail NODE[p_invest_fail]

NODE[p_spot]: «Замечены подозрительные пакеты.» (уменьшение встреч)
- [Сообщить NCPD] → NODE[p_route]

NODE[p_noise]: «Слишком шумно.»
- [Продолжить] → NODE[p_route]

NODE[p_cool_ok]: «Толпа успокоилась.» (+репутация NCPD)
- [Дальше] → NODE[p_route]

NODE[p_cool_fail]: «Стычка!» → бой (2 врага)
- win → NODE[p_route]

NODE[p_invest_ok]: «Канал общения заговорщиков найден.» (+XP, +репутация)
- [Выследить] → NODE[p_trace]

NODE[p_invest_fail]: «Ложный след.»
- [Продолжить патруль] → NODE[p_route]

NODE[p_trace]: Слежка → «Склад у переулка.»
- [Войти] → NODE[p_warehouse]
- [Подкрепление NCPD] → NODE[p_backup]

NODE[p_backup]: Патруль NCPD присоединяется (уменьшение врагов)
- [Штурм] → NODE[p_warehouse]

NODE[p_warehouse]: Склад → лидеры (3)
- [COOL 16 Сдаться!] → success NODE[p_surrender] / fail NODE[p_fight]
- [Штурм] → NODE[p_fight]

NODE[p_surrender]: «Они бросают оружие.»
- [Связать и вывести] → NODE[p_finish]

NODE[p_fight]: Бой (3–4 врага, AC 14–15)
- win → NODE[p_finish]
- lose → NODE[p_respawn]

NODE[p_finish]: Возврат к Саре
- [Отчитаться] → NODE[complete]

NODE[p_respawn]: Медпункт (штраф −10% денег)
- [Вернуться на патруль] → NODE[p_patrol]

NODE[complete]: «Квест завершён.»

## 6. Награды и баланс
- База: 680 XP, 900 eddies, +репутация NCPD 25.
- Бонусы: PERCEPTION/COOL/INVESTIGATION успехи снижают бои, дают доп. репутацию и XP.

## 7. API
- POST `/api/v1/quests/{id}/accept`
- POST `/api/v1/skill-check` (perception|cool|investigation)
- POST `/api/v1/combat/start`
- POST `/api/v1/quests/{id}/complete`

## 8. Соответствие JSON
- См. `mvp-data-json/quests.json`: `quest-main-2072-independence-celebration`.

## 9. Логи/Тесты
- Логи проверок/боёв/деэскалаций. Тесты: соц-путь; силовой; смешанный.

## 10. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация (24 узла).


