# Хронология войн и конфликтов 2020–2093

**ID документа:** `lore-faction-wars-2020-2093`  
**Тип:** lore-timeline  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 22:11  
**Приоритет:** высокий  
**Связанные документы:** `factions-timeline-2020-2093.md`, `world/specter-helios-proxy-war.md`, `quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`, `dialogues/npc-kaede-ishikawa.md`, `world/city-unrest-escalations.md`  
**target-domain:** lore/world  
**target-мicroservices:** world-service, narrative-service, social-service, economy-service  
**target-frontend-модули:** modules/world/history, modules/narrative/timeline, modules/social/feeds  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 22:11
**api-readiness-notes:** Хронология конфликтов 2020–2093: ключевые войны, участники, флаги и интеграции с world/narrative системой.

---

## 1. Обзор
- Документ фиксирует крупные войны, прокси-конфликты и мятежи за 2020–2093 гг.
- Каждому событию сопоставлены фракции, триггеры, ключевые исходы, игровые крючки (quests, raids, dialogues).
- Используются реальные отсылки (NotPetya, SolarWinds, Ever Given) и CYBERPUNK сеттинг Night City/Neo-Earth.
- Хронология служит основой для world-state эмуляции и сюжетных арок.

## 2. Структурированная таблица конфликтов

| Годы | Название | Фракции | Кратко | Ключевые триггеры | Итоги |
|------|----------|---------|--------|--------------------|-------|
| 2020–2024 | **Global Net Skirmishes** | Netwatch, Anonymous Remnants, Arasaka ShadowOps | Кибервойны после утечек 2020-х | NotPetya, SolarWinds Redux | Появление Underlink, рост Shadow Markets |
| 2025–2032 | **Panamax Blockade Wars** | Maelstrom, NUSA Navy, Global Freight Corps | Боевые операции из-за Ever Given 2.0 и Panamax 2048 | Крах судоходства, кибер-пиратство | Возвышение Maelstrom, низкий доверие к NUSA |
| 2033–2040 | **Helios-Indra Corporate Feud** | Helios Corporation, Indra Quantum, Militech Advisors | Рейды за контроль энергетических сетей | Падение Indra в Персии, Solar Array Hijack | Helios получает контроль над Countermesh узлами |
| 2041–2049 | **Neo-Tokyo Data Purge** | Arasaka, Specter Cells, Neo Tokyo Gov | Армия ИИ Arasaka против городских повстанцев | Blackwall Glitch 2045, AR студии «Ghost in the Wires» | Arasaka уступает сегмент Specter, рождается Kaede Network |
| 2050–2057 | **Libertad Low Orbit War** | Space Libertad, NUSA Orbital, EVE SynCorp | Конфликт за орбитальные лифты и минералы | Collapse of MEME ETF, Anonymous Leak 2051 | Создание договора «Orbital Peace», скрытый Specter канал |
| 2058–2066 | **Gaia Terraform Rebellion** | Gaia EcoCells, Helios Terraform, Specter Mediators | Экосопротивление против корпоративного терраформинга | Кризис климатических систем, Blackwall seed | Подписание Mediator Accords, Specter получает международный мандат |
| 2067–2075 | **Night City Proxy Siege** | Specter, Helios, Maelstrom, Valentinos, World Gov | Многослойная прокси-война на основе Underlink и экономических кризисов | ``Specter Surge``, `Countermesh Conspiracy`, City Unrest escalations | Перемирие 2075, запуск Proxy War Leaderboard |
| 2076–2083 | **Shardfall Insurrection** | Helios Splinters, Ghost Tribes, Netwatch Reform | Восстание обманутых Helios на космических станциях | Darknet Scandal 2078, Ghost AR hymns | Формирование Ghost Sovereignty, новые классы рейдов |
| 2084–2090 | **Stellar Trade Wars** | Specter Alliance, EVE SynCorp, Space Libertad | Торговые войны за космо-линии, хакерские эскалации | Crash Hyperloop 2085, NFT Cargo Heist | Раздел сфер влияния, появление Specter Guild Contract Board |
| 2091–2093 | **Blackwall Renaissance Conflict** | Helios Blackwall Division, Specter Oracle, World Coalition | Возвращение Blackwall угрозы, попытка ИИ суверенитета | «Ghost in the Wires Live» сезон 15, Blackwall Symphony | Итоговая осада «Neon Uprising», открытая концовка для игроков |

## 3. Детальные описания ключевых войн

### 3.1 Global Net Skirmishes (2020–2024)
- **Суть:** ради войны киберколлективов после утечек 2020-х; цепь роя кибероружия.
- **Пасхалки:** ссылки на реальных хакеров (Kevin Mitnick, Anonymous 2020s), мемы Reddit WSB.
- **Игровые крючки:** тренировочные миссии Netwatch vs Hacktivists, AR архивы.
- **Флаги:** `flag.netwatch.vigilance`, `flag.shadowmarkets.spawn`.

### 3.2 Panamax Blockade Wars (2025–2032)
- **Сюжет:** Ever Given 2.0 блокирует Panamax, Maelstrom и Valentinos используют хаос.
- **Рацион:** контроль поставок, рост black market.
- **Активности:** `maelstrom-blockade-raid`, контрабанда, shooter skill tests Streetwise (threshold 0.66).
- **Пасхалки:** TikTok 2045 ремиксы «Ever Given is back», мемы о Suez.

### 3.3 Helios-Indra Corporate Feud (2033–2040)
- **Война корпораций:** Helios захватывает энергетические сети Indra.
- **Экономика:** создание `countermesh-alloy`, зачатки CM-Viper.
- **Дипломатия:** Militech Advisors играют на обе стороны.
- **Влияние:** формирование Helios Countermesh Ops.

### 3.4 Neo-Tokyo Data Purge (2041–2049)
- **Конфликт:** Arasaka использует ИИ армию, Specter организует подполье.
- **Одобрено:** появление Kaede Network, Kaori Watanabe.
- **Пасхалки:** Ghost in the Wires манга, Shinto Drones 2046.
- **Награды:** unlock `neo-tokyo data vault` activity.

### 3.5 Libertad Low Orbit War (2050–2057)
- **Лор:** Space Libertad и NUSA борются за орбитальные лифты.
- **Детали:** MEME ETF crash вызывает финансовый хаос.
- **Игровые режимы:** zero-g рейды, космо контракт `orbital peace treaty`.
- **Пасхалки:** мемы «STONKS2072» → «STONKS ZERO-G».

### 3.6 Gaia Terraform Rebellion (2058–2066)
- **Описание:** eco cells против Helios Terraform.
- **Системы:** климатические элементы, Blackwall seed.
- **Игровые последствия:** eco raid `Gaia Uprising`, diplomacy quests.
- **Пасхалки:** AR документалка Netflix «Terraform Tears».

### 3.7 Night City Proxy Siege (2067–2075)
- **Кульминация:** Specter, Helios, банды, правительство.
- **Ветви:** `Specter Surge`, `Helios Countermesh Conspiracy`, `Proxy War Leaderboard`.
- **Экономика:** `economy-specter-helios-balance`.
- **Соц эффекты:** NightHub трансляции, мемы «NotPetya 2077 Remix».

### 3.8 Shardfall Insurrection (2076–2083)
- **Космос:** госколонии бунтуют против Helios Splinters.
- **Механики:** захват станций, modular raids.
- **Пасхалки:** AR песнопения Ghost Tribes, упоминания «Boston Dynamics Choir».

### 3.9 Stellar Trade Wars (2084–2090)
- **Финансы:** борьба за космо маршруты, NFT грузоперевозки.
- **Игровые элементы:** `specter-guild-contract-board`, торговые миссии.
- **Пасхалки:** «Hyperloop Karaoke 2085», мемы про Cargo DAO.

### 3.10 Blackwall Renaissance Conflict (2091–2093)
- **Финал:** Helios Blackwall Division пытается создать суверенный ИИ.
- **Активности:** финальные рейды `Neon Uprising`, сюжетные ветки Specter Oracle.
- **Пасхалки:** «Blackwall Symphony», Ghost in the Wires Live сезон 15.
- **Исход:** открытая развилка для будущих сезонов.

## 4. Флаги и интеграции
- `flag.proxy_war.phase` — синхронизация с `specter-helios-proxy-war.md`.
- `flag.panamax.blockade_resolved`, `flag.gaias_seed` — используются в экономике и world events.
- `city_unrest` модификаторы привязаны к каждой войне (см. `city-unrest-escalations.md`).
- `specter-prestige`, `helios-cred`, `underlink-bonds` — динамика из `economy-specter-helios-balance.md`.

## 5. API и сервисы
- world-service: `GET /api/v1/world/history/wars`, `POST /api/v1/world/history/flag`.
- narrative-service: `POST /api/v1/narrative/history/war-event`.
- social-service: `POST /api/v1/social/feeds/history/broadcast`.
- economy-service: `POST /api/v1/economy/modifiers/history/apply`.

## 6. Телеметрия
- Метрики: `war_history_engagement`, `proxy_war_phase_time`, `raid_history_replay`.
- Events: `history_war_selected`, `history_flag_unlocked`.
- Dashboards: `war-history-overview`, `city-unrest-history`, `faction-prestige-history`.

## 7. История изменений
- 2025-11-07 22:11 — Создана хронология войн 2020–2093 с интеграцией в world/narrative системы.



