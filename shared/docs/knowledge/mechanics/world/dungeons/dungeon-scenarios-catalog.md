---

---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:33
**api-readiness-notes:** Каталог подземелий: сценарии, фазы, модификаторы, связь с лором и системой прогрессии.
---

# Dungeon Scenarios Catalog - Подземные операции

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 20:33  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Instanced dungeon scenario design  
**Размер:** ~320 строк ✅  
**target-domain:** gameplay-world  
**target-microservice:** world-service (8086)  
**target-frontend-module:** modules/world/dungeons

---

## Краткое описание

Подземелья — инстансовые активности на 4–10 игроков. Каждый сценарий имеет фазы, модификаторы и лор-связи.

---

## Типы подземелий

| Тип | Описание | Игровой акцент | Лор |
|-----|----------|----------------|------|
| Heist | Взлом корп-хранилищ | Стелс, синхронные взломы | Arasaka, Militech |
| Ritual | Ритуалы Blackwall | Координация, контроль зон | Cult of the Wave |
| Overrun | Био-дроны | Экшн, выживание | Maelstrom, Biotechnica |
| Gauntlet | Гладиаторские испытания | Таймеры, боёвка | Night City Underground |
| Escort | Сопровождение грузов | Тактика, защита | Fixer Network |

---

## Структура сценария

1. Briefing — NPC куратор, выбор модификаторов
2. Infiltration — пазлы, скрытые цели
3. Core Encounter — боссы, взломы, волны врагов
4. Extraction — выбор исхода
5. Debrief — награды, разблокировка Hard Mode

---

## Примеры сценариев

### «Data Reliquary» (Heist)
- Локация: архив Arasaka
- Этапы: взлом терминалов → сбор Memory Keys → выбор исхода
- Особенности: таймер тревоги, stealth бонусы
- Награды: корп-кредиты, чертежи имплантов

### «Blackwall Echo» (Ritual)
- Локация: дата-центр NetWatch
- Этапы: сбор Anchor → защита ритуала → бой с Echo Guardian
- Особенности: голосовые ключи, AR-фазы
- Награды: Blackwall Shards, перки нетраннеров

### «Substructure 77» (Overrun)
- Локация: завод Biotechnica
- Этапы: очистка коридоров → оборона контроллера → босс био-мех
- Особенности: токсины, антидоты, инженерные задачи
- Награды: биокомпоненты, репутация Biotechnica

### «Neon Trials» (Gauntlet)
- Локация: подпольная арена
- Этапы: серии боёв → puzzle room → двойной босс
- Особенности: зрительские модификаторы
- Награды: косметика, подпольные контакты

### «Ghost Freight» (Escort)
- Локация: поезд через Badlands
- Этапы: подготовка защиты → оборона волн → выбор конца
- Особенности: смена окружения, необходимость инженеров
- Награды: лут кочевников, репутация Nomad Coalition

---

## Модификаторы

- Affixes: `Cyber Surge`, `Biohazard`, `Glitched Reality`, `Corporate Audit`
- Трудности: Normal → Apex с новыми механиками

---

## Награды

- Dungeon Tokens, Blueprint Unlocks, Guild Progress, Lore Unlocks

---

## Динамические эвенты

- Night Shift, Corp Counter-Op, Blackwall Flare, Community Challenge

---

## Интеграции

- Loot Hunt ключи, Voice Lobby каналы, Clan Wars бонусы, Battle Pass задания, Quest System ветки

---

## API контуры

- `GET /api/v1/dungeons/catalog`
- `POST /api/v1/dungeons/matchmaking/join`
- `POST /api/v1/dungeons/instance/{instanceId}/progress`
- `GET /api/v1/dungeons/rewards`

---

## Данные

`dungeon_catalog`, `dungeon_instances`, `dungeon_rewards` — UUID, JSONB, timestamps

---

## Лор и NPC

- Viktor «Vault» Reyes, Sister Aurora, Dr. Elise Hahn, Captain Mira «Forge`

---

## Готовность

- Сценарии, модификаторы, интеграции и API описаны.
- Документ готов к передаче в ДУАПИТАСК.


