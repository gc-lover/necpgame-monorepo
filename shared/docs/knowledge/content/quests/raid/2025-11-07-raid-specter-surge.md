# Рейд — Specter Surge

**ID квеста:** `raid-2025-11-07-specter-surge`  
**Тип:** raid  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 20:55  
**Приоритет:** высокий  
**Связанные документы:** `../side/2025-11-07-quest-neon-ghosts.md`, `../dialogues/npc-aisha-frost.md`, `../../02-gameplay/world/events/world-events-framework.md`, `../../05-technical/global-state/global-state-management.md`  
**target-domain:** narrative  
**target-мicroservices:** world-service, combat-service, social-service, economy-service  
**target-frontend-module:** modules/world, modules/combat, modules/guild/raids  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:55
**api-readiness-notes:** Рейдовая операция Specter Surge: фазы синхронизации пилотов и мехов, D&D проверки, API карта world/combat/social/economy готовая к постановке задач.

---

## 1. Контекст
- **Локация:** подземный узел Underlink-Delta, соединяющий логистику Ghosts с сетью Helios Throne.
- **Угроза:** Helios активирует протокол `Obsidian Flood`, разворачивая мех-стражей и дроны подавления.
- **Цель:** Specter-отряд синхронизирует пилотов с мехами, удерживает каналы Ghosts и выводит данные о ротации корп-сил `helios-countermesh`.

## 2. Условия входа
- **Требования по персонажу:** уровень 45+, выполнение квеста `2025-11-07-quest-neon-ghosts.md`, статус `flag.neon.elite == true`.
- **Команда:** 6 игроков (3 пилота, 3 техно-оператора), минимум один инженер с модом `ghost-squad-ticket`.
- **Мировые флаги:** `underlink.stability` >= 40, `city.unrest.level` >= 35, активен `specter.overlay`.
- **Подготовка:** загрузка протокола `specter-sync` через терминал гильдии, слоты имплантов `specter-link` свободны.

## 3. Фазы рейда

| Фаза | Название | Описание | Успех | Провал |
|------|----------|----------|-------|--------|
| I | Ghost Link Calibration | Пилоты проводят D&D калибровку нейро-связей и чистят канал Underlink. | Разблокирует `specter.sync` и снижает `city.unrest.level` на 3. | Срабатывает `overheat`, таймер +240 c, рост тревоги Helios. |
| II | Countermesh Breach | Взлом сетей Helios, отключение мех-щитов. | Открывает мех-арсенал, снижает `helios.alert` на 2. | Появляется волна `helios-sentinel`, штраф к урону. |
| III | Dual-Core Assault | Синхронный бой пилотов и мехов против `Obsidian Colossus`. | `raid.reward.core-fragment`, доступ к фазе IV. | Запускается `core-reboot`, здоровье босса 75%, дебафф `sync-lag`. |
| IV | Data Extraction Run | Техно-операторы выносят данные и удерживают коридоры. | Ключ `ghost-data-vault`, +8 `rep.fixers.neon`. | Потеря одного пакета данных, -6 `rep.fixers.neon`. |
| V | Specter Evacuation | Координированный выход через гильдейский шаттл. | Триггер `specter-celebration`, эффект `underlink.stability +10`. | Контр-удар Helios, шанс `ghost-blacklist`, эвакуация через альтернативный маршрут. |

## 4. Узлы и проверки D&D

| Фаза | Узел | Тип проверки | DC | Модификаторы | Успех | Провал |
|------|------|--------------|----|--------------|-------|--------|
| I | sync-calibration.neural-link | Arcana | 21 | `+2` при активном модуле `specter-link` | Бафф `sync-flow`, ускорение перезарядок | Дебафф `sync-lag`, кулдаун 120 c |
| II | breach-countermesh.oscillation | Hacking | 22 | `+1` за `ghost-drone` | Отключает `helios.shield`, снижает входящий урон | Активирует `countermesh-spike`, урон по команде |
| III | dual-assault.tactics-call | Tactics | 23 | `+2` при `rep.fixers.neon` >= 30 | Открывает маневр `specter-blitz`, крит шанс +15% | Контратака `colossus-cleave`, урон мехам |
| IV | data-extraction.ghost-sprint | Athletics | 21 | `+1` за бафф `sync-flow` | Быстрая эвакуация пакетов, таймер -90 c | Потерян пакет, активируется `helios-tracer` |
| V | evac-coordination.drop-window | Leadership | 22 | `+1` при `underlink.stability` >= 60 | Сокращает время эвакуации на 50%, бонусная репутация | Контр-обстрел дронов, репутация -5 |

## 5. Механики и события
- **Specter Sync Loop:** world-service и combat-service обрабатывают `SPECTER_SYNC_STATE` каждые 30 секунд; провал добавляет дебафф `sync-lag`.
- **Dual-Control Combat:** пилоты управляют мехами (combat-service), техно-операторы активируют мини-игры взлома (world-service).
- **City Unrest Feedback:** успешные проверки снижают `city.unrest.level`; провал повышает и запускает мировой ивент `world.event.city_unrest`.
- **Ghost Logistics Support:** economy-service выдает баффы `ghost-consumable` при выполнении доставки фазой IV.
- **Reputation Split:** social-service распределяет репутацию между Ghosts, Helios и Maelstrom в зависимости от выбора в фазах II и V.

## 6. Награды и прогресс
- **Репутации:** `rep.fixers.neon` +12 (успех), `rep.corp.helios` -10, бонус +5 при крит-успехе в фазе V.
- **Предметы:** `specter-core-fragment`, `ghost-squad-ticket`, `helios-blackbox`, шанс на `specter-legendary-implant`.
- **Флаги:** `flag.specter.raid_cleared`, `flag.helios.countermesh_disabled`, `flag.neon.blacklist` при катастрофическом провале.
- **World-state:** `underlink.stability` +10, `city.unrest.level` -8, открывается событие `world.event.specter_parade`.
- **Прогресс гильдий:** разблокировка контракта `guild.contract.specter-ops` и weekly-варианты.

## 7. API и события (карта)

| Сервис | REST | WebSocket / события | Примечания |
|--------|------|---------------------|------------|
| world-service | `POST /api/v1/world/raid/specter-sync`, `PATCH /api/v1/world/state/city-unrest` | `SPECTER_SYNC_STATE`, `CITY_UNREST_UPDATE` | Обновляет синхронизацию и мировые модификаторы |
| combat-service | `POST /api/v1/combat/raids/encounter`, `POST /api/v1/combat/mechs/actions` | `COMBAT_EVENT_RAID`, `MECH_STATE_SYNC` | Управление боями пилот+мех |
| social-service | `POST /api/v1/social/reputation/batch`, `POST /api/v1/social/flags/toggle` | `SOCIAL_RATING_ALERT`, `REPUTATION_CHANGE` | Репутация и флаги Ghosts/Helios |
| economy-service | `POST /api/v1/economy/contracts/activate`, `POST /api/v1/economy/rewards/claim` | `ECONOMY_CONTRACT_UPDATE` | Лут и контракты Ghosts |
| narrative-service | `POST /api/v1/narrative/cutscenes/trigger` | `NARRATIVE_BEAT_UPDATE` | Кат-сцены Specter и финальные эпилоги |

## 8. Метрики и SLA
- **SLA:** `SPECTER_SYNC_STATE` <= 150 мс, `MECH_STATE_SYNC` <= 80 мс, `CITY_UNREST_UPDATE` <= 200 мс.
- **Observability:** Grafana панели `specter-raid-overview`, `mech-sync-heatmap`, `city-unrest-monitor`.
- **Алерты:** PagerDuty `SpecterSyncHighLatency` при > 180 мс, `CountermeshFailure` при 3 провалах подряд.
- **KPIs:** средний успех фазы III >= 65%; среднее время рейда <= 27 минут; коэффициент эвакуации >= 90%.

## 9. Связанные материалы
- `../side/2025-11-07-quest-neon-ghosts.md`
- `../dialogues/npc-aisha-frost.md`
- `../../02-gameplay/world/events/world-events-framework.md`
- `../../05-technical/global-state/global-state-management.md`
- `../../05-technical/ui/main-game/ui-system.md`

## 10. История изменений
- 2025-11-07 20:55 — Создан рейд Specter Surge, определены фазы, проверки D&D и карта API.
