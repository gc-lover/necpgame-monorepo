# Йоханнесбург: Геодезические шахты и синтетические саванны (2020-2093)

**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-08 00:21  
**Статус:** review  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:21
**api-readiness-notes:** Добавлены модели данных, REST/Kafka контракты world-service и UX-флоу хроники города; документ готов для постановки API задач.

---

## Краткая справка

- **Название:** GeoForge Johannesburg Nexus
- **Страна / Регион:** Южноафриканская федерация
- **Население:** ~9,7 млн (геокупола 3,5 млн, мегасаванны 4,1 млн, подземные шахты 2,1 млн)
- **Доминирующая корпорация:** GeoForge Extraction
- **Ключевые банды:** NeoZulu Collective, Chrome Miners Union, Veldt Phantoms
- **Ключевой символ:** Геодезический купол «Aurora Mine Sphere» над старой золотой шахтой

---

## Макроструктура города

### Вертикальные уровни

| Уровень | Имя | Социальный слой | Особенности |
|---------|-----|-----------------|-------------|
| 5 | Aurora Mine Sphere | Управленцы GeoForge, инвесторы | Геодезические купола, квантовые лаборатории, климат-контроль |
| 4 | Skyline Savannah Deck | Средний класс, тех-фермеры | Искусственные саванны, био-угодья, туристические курорты |
| 3 | Urban Forge Grid | Рабочие кварталы, рынки, сервисы | Реплицированные улицы, транспортные узлы, городские фермы |
| 2 | Deep Extraction Layer | Инженеры, Chrome Miners Union | Автоматизированные шахты, магнитные лифты, перерабатывающие заводы |
| 1 | Veldt Catacombs | Подпольные кланы, контрабандисты, беженцы | Подземные тоннели, скрытые поселения, архивы NeoZulu |

### Горизонтальные кластеры

1. **Aurora Core District** — штаб GeoForge, корпоративные банки, контроль добычи редкоземельных металлов.  
2. **Savannah Biopark** — гигантская био-структура с синтетическими саваннами, домами и VR-сафари.  
3. **Miner Union Hub** — инфраструктура Chrome Miners Union с мастерскими, рынками запчастей, залами переговоров.  
4. **Veldt Underground Network** — сеть туннелей NeoZulu, скрывающая архивы и контрабандные маршруты.

### Улицы и маршруты

- **GeoForge Axis:** центральная магистраль между Aurora Sphere и Urban Forge, охраняется корпоративными дронами.  
- **Savannah Loop:** транспортный пояс вокруг биосаванн, используется для туристических миссий и фермерских контрактов.  
- **Chrome Rail:** магнитная линия, спускающаяся в глубокие шахты, поддерживает эксплуатацию и боевые рейды.  
- **Veldt Vein:** сеть скрытых туннелей, по которым NeoZulu и Veldt Phantoms перевозят редкие артефакты.

---

## Ключевые NPC

| Имя | Роль | Фракция | Локация | Краткое описание |
|-----|------|---------|---------|-------------------|
| Номса «Aurora» Мбеки | Директор по добыче | GeoForge Extraction | Aurora Core District | Балансирует экспорт редкоземельных металлов и внутренние рынки |
| Тако Масунгу | Лидер Chrome Miners Union | Chrome Miners Union | Miner Union Hub | Отстаивает права шахтёров-киборгов, ведёт переговоры с корпорациями |
| Доктор Кайто Ран | Рипердок-геобиолог | Независимый | Savannah Biopark | Адаптирует био-импланты к саваннам, помогает легальным и теневым клиентам |
| Амандла «Veldt Shade» Кумало | Лидер NeoZulu Collective | Veldt Underground Network | Руководит подпольными операциями и контролирует артефактную контрабанду |

**Контактные цепочки:**  
- «Договор шахтёров»: Масунгу → Мбеки → международные трейдеры → распределение доходов.  
- «Тень саванн»: Кумало → доктор Кайто → нелегальные экспедиции Veldt Phantoms.

---

## Сюжетные узлы и легенды

1. **Протокол «Кимберлит»** — новый кристалл в шахтах; игрок решает, кому продать данные, влияя на доступ к ресурсам и репутацию.  
2. **«Саванна без памяти»** — био-нейросеть стирает воспоминания; расследование открывает квесты Chrome Miners Union и NeoZulu.  
3. **«Перекупщик сияния»** — на Savannah Bazaar появляется дилер, продающий нелегальные био-импланты, активирует линейки «контрабанда vs легализация».

---

## Экономика и геймплей

- GeoForge Exchange — редкоземельные металлы и контроль квот.  
- Union Market — запчасти, импланты, усилители кибернетики.  
- Savannah Bazaar — биомоды, экзотические существа, VR-сафари.  
- Экстракт-зоны в Deep Extraction Layer защищены мехами GeoForge; PvPvE рейды с высоким риском.  
- Социальные активности: шахтёрские испытания, VR-сафари, подпольные рейды NeoZulu.

---

## Данные и модели

### Сущности world-service

- **CityProfile**  
  - `cityId`, `name`, `population`, `dominantCorporation`, `keyFactions`, `signatureLandmark`, `securityLevel`, `economicIndex`, `climateControlStatus`, `lastUpdate`.  
- **VerticalLayer**  
  - `layerId`, `cityId`, `name`, `levelIndex`, `socialStrata`, `description`, `hazardLevel`, `accessRestrictions`.  
- **UrbanCluster**  
  - `clusterId`, `cityId`, `name`, `type`, `primaryActivities`, `controllingFaction`, `publicServices`, `threats`.  
- **TransitRoute**  
  - `routeId`, `cityId`, `name`, `mode`, `securityTier`, `usageMetrics`, `eventHooks` (списки событий).  
- **CityEventTemplate**  
  - `eventId`, `category` (resource, security, social), `trigger`, `impact`, `linkedQuests`, `cooldownHours`.  
- **FactionInfluence**  
  - `factionId`, `cityId`, `influenceScore`, `zonesControlled`, `tensionLevel`.

### Gameplay-service (миссии и активности)

- **CityMission**  
  - `missionId`, `cityId`, `missionType` (raid, escort, investigation, trade), `recommendedLoadouts`, `rewardTable`, `worldImpact`.  
- **CityActivityLog**  
  - `entryId`, `cityId`, `characterId`, `activityType`, `actionXp`, `factionReputationDelta`, `timestamp`.  
- **MarketModifier**  
  - `cityId`, `resourceType`, `priceModifier`, `supplyStatus`, `expiresAt`.

### События (Kafka)

- `world.city.layerStatusChanged` — изменение доступа к уровню (например, комендантский час).  
- `world.city.factionInfluenceUpdated` — обновление влияния фракций и зон контроля.  
- `world.city.eventTriggered` — запуск событий «Kimberlite Protocol», «Savannah Memory Wipe».  
- `gameplay.city.missionCompleted` — результаты миссий и их влияние на экономику и безопасность.  
- `economy.market.modifierApplied` — изменение цен на GeoForge Exchange/Union Market.

---

## API контракты

### REST (world-service)

1. `GET /api/v1/world/cities/{cityId}`  
   - Ответ: профиль города, вертикальные уровни, текущие модификаторы рынка и влияния фракций.  
   ```json
   {
     "cityId": "johannesburg",
     "name": "GeoForge Johannesburg Nexus",
     "population": 9700000,
     "dominantCorporation": "GeoForge Extraction",
     "signatureLandmark": "Aurora Mine Sphere",
     "securityLevel": 4,
     "verticalLayers": [
       {"layerId": "aurora-sphere", "levelIndex": 5, "socialStrata": "executive", "hazardLevel": 1}
     ],
     "factionInfluence": [
       {"factionId": "geoforge", "influenceScore": 68, "zonesControlled": ["aurora-core"], "tensionLevel": "medium"}
     ],
     "marketModifiers": [
       {"resourceType": "rare_earth", "priceModifier": 1.35, "expiresAt": "2025-11-10T12:00:00Z"}
     ]
   }
   ```

2. `GET /api/v1/world/cities/{cityId}/routes`  
   - Фильтры: `mode`, `securityTier`, `faction`.  
   - Используется для отображения GeoForge Axis, Savannah Loop, Chrome Rail, Veldt Vein.  

3. `POST /api/v1/world/cities/{cityId}/events/trigger`  
   - Тело: `{ "eventTemplateId": "kimberlite-protocol", "initiator": "player", "evidence": {...} }`  
   - Ответ: `{ "eventId": "uuid", "timelineEntryId": "uuid", "nextReview": "2025-11-09T06:00:00Z" }`  
   - Валидация: проверка условий запуска, отсутствие активного кулдауна.

4. `GET /api/v1/world/cities/{cityId}/factions`  
   - Возвращает влияние GeoForge, NeoZulu, Chrome Miners Union, Veldt Phantoms, уровень напряжения и возможные миссии.

### REST (gameplay-service)

1. `GET /api/v1/cities/{cityId}/missions`  
   - Ответ: каталог миссий (рейды в шахтах, защита караванов, подпольные контракты).  
   ```json
   {
     "missions": [
       {
         "missionId": "raid-chrome-rail",
         "missionType": "raid",
         "recommendedLoadouts": ["combat-extraction", "engineer-support"],
         "worldImpact": {
           "faction": "geoforge",
           "influenceDelta": -5,
           "economyModifier": {"resourceType": "rare_earth", "priceModifier": 0.9}
         },
         "cooldown": 6
       }
     ]
   }
   ```

2. `POST /api/v1/cities/{cityId}/missions/{missionId}/complete`  
   - Тело: `{ "characterId": "uuid", "outcome": "success", "participants": [...], "loot": [...], "actionXpGained": {...} }`  
   - Возвращает обновлённые влияния, уровни безопасности, репутацию.

3. `GET /api/v1/cities/{cityId}/activities/feed`  
   - Лента активности игроков и NPC, используется для хроники.

### GraphQL (world frontend gateway)

- `cityProfile(cityId)` — агрегирует данные профиля, влияния, актуальные события и ссылки на миссии.  
- `cityChronicle(cityId, cursor)` — возвращает хронологию (смены власти, рейды, экономические всплески).  
- `cityMarket(cityId)` — данные по биржам GeoForge/Union Market.

---

## UX-потоки

### Хроника города
1. Игрок открывает вкладку «Johannesburg Chronicle» на глобальной карте.  
2. Лента событий показывает смены влияния, активные миссии, рынок.  
3. При клике на событие открывается модал с картой, участниками, наградами и ссылками на ответные действия.

### Планирование миссий
1. На экране миссий игрок выбирает направление (рейд на Chrome Rail, сопровождение Savannah Loop).  
2. UI подтягивает рекомендованные loadouts, список участников, потенциал Action XP.  
3. Перед стартом отображается влияние на фракции и экономику (GeoForge, NeoZulu).  
4. После завершения — отчёт, обновление хроники и изменения цен на рынке.

### Управление логистикой и рынками
1. Экран GeoForge Exchange показывает динамику цен, активные модификаторы.  
2. Игрок может приложить ресурсы, чтобы стабилизировать рынок, или саботировать поставки.  
3. Влияние торговых решений отображается в хронике и влияет на миссии Chrome Miners Union.

### Подпольные операции NeoZulu
1. В Veldt Underground UI отображает скрытые маршруты и контракты.  
2. Для доступа требуется репутация NeoZulu; UI подсвечивает риск и возможные награды.  
3. Успешные операции меняют влияние фракций и открывают артефактные миссии.

---

## Аналитика и баланс

- `cityInfluenceDrift` — средние изменения влияния фракций за неделю; тревога при >±15.  
- `resourcePriceVolatility` — отслеживает скачки цен на редкоземельные металлы, триггерит стабилизационные события.  
- `missionSuccessRate` — успехи по типам миссий, помогает балансировать награды и сложность.  
- `undergroundTraffickingIndex` — активность Veldt Vein, влияет на безопасность и охоту корпораций.

---

## Связанные документы

- `../locations-overview.md`
- `./WORLD-CITIES-DETAIL-CATALOG-2093.md`
- `../../02-gameplay/world/world-state/living-world-kenshi-hybrid.md`
- `../../02-gameplay/economy/economy-world-impact.md`
- `../../02-gameplay/world/events/live-events-system.md`

---

## История изменений

- v1.1.0 (2025-11-08 00:21) — Добавлены модели данных, API контракты, UX-потоки, аналитика.  
- v1.0.0 (2025-11-07 20:06) — Создано детальное описание Йоханнесбурга.

