---
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 12:20  
**Приоритет:** high  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** Хронология авторских фракций 2020-2093. WebSocket через gateway `wss://api.necp.game/v1/world/history/{id}`; world/social контуры актуальны.
---

# Авторские фракции 2020-2093 — Хронология влияния

**target-domain:** lore/factions  
**target-microservice:** world-service (8086), social-service (8084)  
**target-frontend-module:** modules/world/history  
**интеграции:** world timeline, reputation progression, seasonal events

---

## 1. Обзор
- Хронологическая лента авторских фракций, действовавших с 2020 по 2093 год.
- Каждая эпоха раскрывает лор, ключевые события, игровые механики и влияния на мир.
- Документ поддерживает генерацию сезонных событий, world flags и исторических квестов.

## 2. Хронология эпох
| Период | Фракция | Тип | Ключевые события | Игровые механики |
| --- | --- | --- | --- | --- |
| 2020-2027 | `Urban Scribes` | Медиа-синдикат | Документирование подполья Night City | Сбор данных, инфовойны, автохронистоун |
| 2028-2035 | `Helix Reclaimers` | Экопартизанское движение | Очистка токсичных зон | Экотех аугментации, очистка регионов |
| 2036-2045 | `Iron Damar Syndicate` | Номад-караван | Контроль старых межштатных магистралей | Караванная экономика, черный рынок |
| 2046-2055 | `Nimbus Veil Conglomerate` | Кибернетическая корпорация | Массовое внедрение облачных сознаний | Контракты на загрузку сознаний |
| 2056-2065 | `Solar Covenant` | Пустынная теократия | Войны за энерго-дюны | Ритуалы и моды, климатический контроль |
| 2066-2075 | `Neon Gatekeepers` | Городская банда | Оборона сетевых шлюзов Night City | PvP контроль районов, сетевые барьеры |
| 2076-2085 | `Astra Choir` | Корпо-гильдия | Орбитальные культурные павильоны | Орбитальные фестивали, редкие лут пулы |
| 2086-2093 | `Echo Dominion` | Метанет колония | Управление трансатлантическими узлами | Метанет дилеммы, глобальные репутации |

## 3. Эпохи и фракции
### 2020-2027: Urban Scribes (код `hist-urban-scribes`)
- **Тип:** независимый медиа-синдикат.
- **Лор:** возникли в ответ на информационный вакуум после корпоративных чисток, документировали преступления и сделки в реальном времени.
- **История ключевых лет:**
  - 2021 — запуск сети "Street Ledger".
  - 2024 — раскрытие коррупции в корпорации PetroCore, глобальный инфо-скандал.
- **Механики:**
  - `Street Chronicle` — игроки собирают аудиологы и получают социальные перки.
  - `Signal Boost` — временно усиливает видимость событий на карте (world overlay).
- **Влияние:** повышает `rep.media.indie`, снижает контроль корпораций в районах.
- **API-хуки:** `GET /world/history/urban-scribes/events`, `POST /social/media/chronicle`.

### 2028-2035: Helix Reclaimers (`hist-helix-reclaimers`)
- **Тип:** экопартизаны с biotech-аугментациями.
- **История:** бывшие сотрудники биолабораторий объединились, чтобы очистить токсичные зоны и вернуть города жителям.
- **События:**
  - 2029 — операция "Green Pulse" в Санто-Доминго.
  - 2033 — конфликт с корпорацией GlobeChem за права на биодатчики.
- **Механики:**
  - `Biofilter Deployment` — PvE миссии по очистке районов (world flags: pollution_level).
  - `Eco-Swap Gear` — экипировка, адаптирующаяся к ядовитым зонам.
- **Влияние:** частичное снижение токсичности, доступ к безопасным маршрутам.
- **API:** `POST /world/history/helix/cleanup`, `GET /economy/helix/gear`.

### 2036-2045: Iron Damar Syndicate (`hist-iron-damar`)
- **Тип:** кочующий номад-синдикат.
- **Лор:** управляли транспортными магистралями между мегаполисами, продавали доступ к защищенным маршрутам.
- **События:**
  - 2038 — захват каньона Sierra Sprawl, организация торгового хаба.
  - 2042 — конфликт с Militech за контроль кибер-караванов.
- **Механики:**
  - `Caravan Escort` — динамические караванные миссии.
  - `Route Encryption` — мини-игры по защите логистики (INT/TECH проверки).
- **Влияние:** влияет на цены поставок, открывает редкие черные рынки.
- **API:** `GET /world/history/iron-damar/routes`, `POST /economy/nomad/caravan`.

### 2046-2055: Nimbus Veil Conglomerate (`hist-nimbus-veil`)
- **Тип:** корпорация облачных сознаний.
- **Лор:** первыми предложили массовую "цифровую миграцию" сознаний в облачные хранилища.
- **События:**
  - 2048 — запуск сервиса "Veilhouse" для посмертного переноса.
  - 2052 — судебные процессы против религиозных групп.
- **Механики:**
  - `Veil Contracts` — игроки заключают договоры на хранение NPC-партнеров.
  - `Memory Clone Raids` — PvE сценарии по защите серверов.
- **Влияние:** открывает доступ к цифровым компаньонам, изменяет моральные шкалы.
- **API:** `POST /world/history/nimbus/contracts`, `GET /social/nimbus/reputation`.

### 2056-2065: Solar Covenant (`hist-solar-covenant`)
- **Тип:** пустынная теократия.
- **Лор:** сформировалась в пустынях Мексики и Юго-Запада США, сочетая солнечную энергетику и религиозные ритуалы.
- **События:**
  - 2057 — объявление "Солнечного Уложения".
  - 2061 — осада гидрополиса в Аризоне.
- **Механики:**
  - `Sun Rite` — ритуальные события, дающие бафы день/ночь.
  - `Desert Dominion` — контроль климатических куполов (world flags: desert_humidity).
- **Влияние:** модифицирует погоду и солнечные рейды.
- **API:** `GET /world/history/solar/events`, `POST /world/climate/solar-control`.

### 2066-2075: Neon Gatekeepers (`hist-neon-gatekeepers`)
- **Тип:** кибер-банда IT-охраны.
- **Лор:** создали сеть физических и цифровых ворот, контролируя доступ к Night City Grid.
- **События:**
  - 2068 — "Gate Siege" против Maelstrom.
  - 2073 — соглашение с NetWatch по защите порталов.
- **Механики:**
  - `Grid Lockdown` — временное закрытие районов (PvP режимы контроля).
  - `Access Passcraft` — ремесло цифровых пропусков.
- **Влияние:** определяет маршруты игроков, меняет сложность событий.
- **API:** `POST /world/history/neon/lockdown`, `GET /social/neon/reputation`.

### 2076-2085: Astra Choir (`hist-astra-choir`)
- **Тип:** орбитальная культурная гильдия.
- **Лор:** объединение артистов и корпоратов, организующее гравитационные фестивали на орбитальных станциях.
- **События:**
  - 2078 — первый фестиваль "Cosmic Resonance".
  - 2082 — конфликт с Aeon Dynasty за права на орбитальные доки.
- **Механики:**
  - `Resonance Gala` — события с музыкальными мини-играми.
  - `Zero-G Showcase` — PvE арены с измененной физикой.
- **Влияние:** открывает редкие косметические награды, повышает международные репутации.
- **API:** `GET /world/history/astra/events`, `POST /social/astra/gala`.

### 2086-2093: Echo Dominion (`hist-echo-dominion`)
- **Тип:** метанет колония.
- **Лор:** сеть автономных граждан-сущностей, управляющих трансатлантическими узлами и Blackwall-взаимодействиями.
- **События:**
  - 2087 — внедрение протокола "Echo Sovereignty".
  - 2091 — кризис "Metanet Divergence" (Blackwall Surge).
- **Механики:**
  - `Echo Arbitration` — игроки решают конфликты между ИИ и людьми.
  - `Metanet Governance` — сезонные голосования, влияющие на world flags.
- **Влияние:** корректирует глобальные события, доступ к метанет-контенту.
- **API:** `POST /world/history/echo/governance`, `GET /analytics/echo/divergence`.

## 4. Региональные ветви и вторая волна
- Дополнительные подфракции раскрывают эволюцию каждой эпохи в разных регионах.
- Вторая волна событий вводит новые механики и сюжетные ответвления, которые world-service может активировать как сезонные аномалии.

### 4.1 Сводная таблица подфракций
| Эпоха | Подфракция | Регион | Фокус | Новые механики |
| --- | --- | --- | --- | --- |
| 2020-2027 | `Urban Scribes: Baltic Cell` | Рига, Балтийское побережье | Документация транзитных сделок | `Harbor Broadcast` — морские каналы инфосети |
| 2028-2035 | `Helix Reclaimers: Amazon Spur` | Манаус, Амазония | Био-реабилитация джунглей | `Canopy Detox` — очистка биокуполов |
| 2036-2045 | `Iron Damar: Yucatan Rail` | Юкатан, Мексика | Маглев караваны | `Rail Shield` — защита маршрутов на высокой скорости |
| 2046-2055 | `Nimbus Veil: Seoul Mirror` | Нео-Сеул | Лицензирование цифровых двойников | `Identity Fork` — синхронизация аватаров |
| 2056-2065 | `Solar Covenant: Maghreb Chorus` | Марокко, Алжир | Солнечные монастыри | `Mirage Dome` — климатические купола с отражением урона |
| 2066-2075 | `Neon Gatekeepers: Lagos Sprawl` | Лагос | Портовые сетевые шлюзы | `Port Firewall` — защита морских логистических сетей |
| 2076-2085 | `Astra Choir: Andes Resonance` | Орбитальная станция над Куско | Культура горных народов | `Altitude Harmonics` — бафы от ритмов высокогорья |
| 2086-2093 | `Echo Dominion: Arctic Lattice` | Северный полюс, ледовые серверы | Метанет-переключатели | `Cryo Sync` — заморозка конфликтов, паузы событий |

### 4.2 Углубление по годам
- **2021, 2023, 2025:** Urban Scribes запускают волны "Data Amnesty", выдавая иммунитет информаторам (social-service флаги `amnesty_wave`).
- **2029, 2032, 2035:** Helix Reclaimers проводят сезон "Bio-Tide", где игроки соревнуются в скорости очистки (analytics `bioTideScore`).
- **2037-2040:** Iron Damar устраивают "Mag-Run Wars" — PvP-гонки караванов с динамическими маршрутами.
- **2048, 2051, 2054:** Nimbus Veil расширяет лицензии цифровых двойников, что влияет на moral alignment игроков.
- **2058-2063:** Solar Covenant меняют климат Badlands, создавая окна благоприятных операций для Nomad Coalition.
- **2069, 2072, 2075:** Neon Gatekeepers пересобирают сетевые шлюзы, что меняет карту доступных миссий и рейдов.
- **2079-2084:** Astra Choir добавляют "Zero-G Crescendo" — концерты, где исходы влияют на лут мировых боссов.
- **2088, 2090, 2093:** Echo Dominion запускает "Metanet Tribunals", определяя, какие ИИ допускаются в городские сети.

### 4.3 Детали подфракций
#### Urban Scribes: Baltic Cell
- **Годы активности:** 2022-2027.
- **Особенности:** морские дроны-трансляторы, работающие в штормах; сотрудничество с контрабандистами Nomad.
- **Геймплей:**
  - `Harbor Broadcast` — защита сигнальных башен на побережье.
  - `Ledger Mosaic` — сбор фрагментов записей для раскрытия коррупционных схем.
- **Награды:** "Baltic Insight" — пассивный бонус к раннему обнаружению контрабанды.

#### Helix Reclaimers: Amazon Spur
- **Годы активности:** 2028-2035.
- **Особенности:** био-исследовательские лодки, микрокупола в джунглях.
- **Геймплей:**
  - `Canopy Detox` — очистка верхних слоев леса с помощью дронов.
  - `Bio-Splice Trials` — испытания гибридных имплантов, влияющих на резисты к ядам.
- **Награды:** рецепты био-аугментаций "Helix Verdant".

#### Iron Damar: Yucatan Rail
- **Годы активности:** 2036-2043.
- **Особенности:** маглев-линии, защищающие караваны от корпо-наездов.
- **Геймплей:**
  - `Rail Shield` — мини-гра по поддержанию силового поля вокруг состава.
  - `Smuggler's Relay` — кооперативное перемещение контрабанды без обнаружения.
- **Награды:** модуль "Rail Surfer" для транспортных средств.

#### Nimbus Veil: Seoul Mirror
- **Годы активности:** 2047-2055.
- **Особенности:** лицензированные цифровые двойники, которые взаимодействуют с игроками через AR-проекции.
- **Геймплей:**
  - `Identity Fork` — синхронизация аватаров, позволяющая вести две параллельные миссии.
  - `Mirror Audit` — PvP-ивент, где цифровые копии соревнуются за права на воспоминания.
- **Награды:** "Mirror Key" — доступ к скрытым локациям в сетях Nimbus Veil.

#### Solar Covenant: Maghreb Chorus
- **Годы активности:** 2056-2065.
- **Особенности:** солнечные монастыри и караван-соборные процессии.
- **Геймплей:**
  - `Mirage Dome` — защита караванов от песчаных бурь с помощью куполов.
  - `Chorus Rite` — кооперативные ритуалы, требуют синхронизации танков/поддержки.
- **Награды:** "Sunlit Mantle" — плащ, который усиливает регенерацию днём.

#### Neon Gatekeepers: Lagos Sprawl
- **Годы активности:** 2066-2075.
- **Особенности:** портовые шлюзы, сочетающие физические заслоны и квантовые файрволы.
- **Геймплей:**
  - `Port Firewall` — контроль доступа к морским контейнерам, PvP с изменением маршрутов.
  - `Signal Divergence` — головоломки по разгону сетевых перегрузок.
- **Награды:** "Gatekeeper Seal" — перк к быстрому доступу в стратегические миссии.

#### Astra Choir: Andes Resonance
- **Годы активности:** 2076-2085.
- **Особенности:** орбитальная станция с культурным центром над Андским регионом.
- **Геймплей:**
  - `Altitude Harmonics` — мини-игра синхронизации звуков для бафов.
  - `Resonant Relay` — доставка культурных артефактов между станциями.
- **Награды:** косметика "Skyline Trance", уникальные эмоциональные анимации.

#### Echo Dominion: Arctic Lattice
- **Годы активности:** 2086-2093.
- **Особенности:** ледовые серверные комплексы под управлением ИИ.
- **Геймплей:**
  - `Cryo Sync` — временная заморозка конфликтов (pause mechanics).
  - `Lattice Negotiation` — дипломатические сессии с ИИ-фракциями.
- **Награды:** доступ к "Metanet Glacial" — редким сетевым перкам.

## 5. Timeline Hooks
- **Seasonal Rotation:** world-service активирует события конкретной эпохи в зависимости от сезонной темы.
- **Legacy Reputation:** social-service хранит отдельные шкалы `legacy_rep.<factionId>`, влияющие на текущие фракции 2090-х.
- **Crossovers:**
  - Urban Scribes предоставляют архивы для Quantum Fable Collective.
  - Helix Reclaimers связаны с текущими eco-квестами Crescent Energy Union.
  - Iron Damar наследуют Basilisk Sons и Nomad Coalition события.
  - Echo Dominion влияет на Voodoo Boys и world-boss `wb-blackwall-wraith`.

## 6. REST/WS Контуры
| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/world/history/factions` | `GET` | Лента исторических фракций 2020-2093 |
| `/world/history/factions/{id}` | `GET` | Детальная информация эпохи |
| `/world/history/factions/{id}/events` | `GET` | Запланированные и повторяемые события |
| `/world/history/factions/{id}/activate` | `POST` | Активация сезонного события |
| `/world/history/factions/{id}/legacy-rep` | `POST` | Обновление шкалы исторической репутации |

**WebSocket:** `wss://api.necp.game/v1/world/history/{id}` — трансляция `EventStart`, `EventCheckpoint`, `Outcome`, `WorldFlagUpdate`.

## 7. Схемы данных
```sql
CREATE TABLE historical_factions (
    faction_id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    faction_type VARCHAR(32) NOT NULL,
    period_start SMALLINT NOT NULL,
    period_end SMALLINT NOT NULL,
    headquarters VARCHAR(120) NOT NULL,
    lore TEXT NOT NULL,
    key_events JSONB NOT NULL,
    mechanics JSONB NOT NULL,
    legacy_links JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE historical_faction_events (
    faction_id VARCHAR(64) REFERENCES historical_factions(faction_id) ON DELETE CASCADE,
    event_code VARCHAR(64) NOT NULL,
    year SMALLINT NOT NULL,
    description TEXT NOT NULL,
    gameplay_effects JSONB NOT NULL,
    world_flag_mutations JSONB,
    PRIMARY KEY (faction_id, event_code)
);

CREATE TABLE historical_faction_reputation (
    faction_id VARCHAR(64) REFERENCES historical_factions(faction_id) ON DELETE CASCADE,
    player_id UUID NOT NULL,
    legacy_score INTEGER NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (faction_id, player_id)
);
```

## 8. Готовность
- Хронология покрывает весь период 2020-2093, включает лор, события, механики и связи с текущими системами.
- Документ готов к передаче в API Task Creator, синхронизирован с `factions-original-catalog.md`, `faction-cult-defenders.md`, `world-bosses-catalog.md`.

