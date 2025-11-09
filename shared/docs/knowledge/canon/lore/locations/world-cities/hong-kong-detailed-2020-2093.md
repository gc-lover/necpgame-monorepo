# Гонконг: Вертикальные рынки и теневая гавань (2020-2093)

**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-08 00:23  
**Статус:** review  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:23
**api-readiness-notes:** Определены городские модели, REST/GraphQL/Kafka контракты и UX-потоки для Pearl Nexus; документ готов для постановки API задач.

---

## Краткая справка

- **Название:** Pearl Nexus Hong Kong
- **Регион:** Восточно-Азиатская синдикальная зона
- **Население:** ~11,9 млн (Sky Gardens 3,1 млн; Spires 5,6 млн; Underbay 3,2 млн)
- **Доминирующая корпорация:** Pearl Exchange Holdings
- **Ключевые банды:** Data Jackals, Red Tide Triads, Skyline Corsairs
- **Символ:** Парящий голографический «Жемчужный ромб» над Victoria Harbour

---

## Макроструктура

### Вертикальные уровни

| Уровень | Имя | Социальный слой | Особенности |
|---------|-----|-----------------|-------------|
| 5 | Sky Garden Arrays | Финансовая элита | Висячие сады, приватные аэродоки, климат-коморы |
| 4 | Pearl Exchange Spires | Брокеры, хакеры | Торговые залы, квантовые data-центры, VR-биржи |
| 3 | Harbour Grid | Средний класс | Многоуровневые рынки, учебные кластеры, культурные площадки |
| 2 | Underbay Platforms | Доковые рабочие, киберпираты | Полупогружённые платформы, рипердок-клиники, складские баржи |
| 1 | Tidal Catacombs | Контрабандисты, беглецы | Заброшенные туннели, скрытые серверные, тех-диаспора |

### Кластеры и маршруты

- **Pearl Exchange Quarter:** контролирует биржевые алгоритмы, выпускает городские облигации.  
- **Neon Sampan Bazaar:** плавучий рынок краденых данных, нелегальных имплантов.  
- **Sky Garden Belt:** стеклянные мосты и ботанические террасы, где обитают корпоративные семьи.  
- **Tidal Fringe:** доки и туннели Red Tide Triads, узлы пиратских рейдов.  
- **Pearl Spine Corridor:** вертикальная артерия внутри шпилей, VIP-дроно-мосты.  
- **Sampan Network:** водные маршруты между платформами, используется контрабандистами.  
- **Skyline Loop:** воздушный маршрут аэротакси, соединяет город с материком.  
- **Undergrid Tram:** автономная линия эвакуации и трафика серверов.

---

## Ключевые NPC и связи

| Имя | Роль | Фракция | Локация | Описание |
|-----|------|---------|---------|----------|
| Лянь Хуэй Мин | Финансовый архитектор | Pearl Exchange | Pearl Exchange Spires | Проектирует квантовые торги, ищет охрану для алгоритмов |
| Кассандра «Sampan Silk» Чоу | Торговка данными | Data Jackals | Neon Sampan Bazaar | Продаёт украденные профили, требует редкие импланты |
| Доктор Вэй Чжэнь | Рипердок-гибридолог | Независимая | Underbay Platforms | Делает водно-воздушные адаптации, работает с Corsairs |
| Командор Тсу Эн | Лидер | Red Tide Triads | Tidal Fringe | Курирует пиратские налёты, пытается захватить Skyline Loop |

**Контактные цепочки:**  
- «Жемчужный торг»: Лянь → Кассандра → корпоративные брокеры → доступ к биржам.  
- «Красная глубина»: Вэй Чжэнь → Skyline Corsairs → Тсу Эн → атаки на Pearl Exchange.

---

## Сюжетные узлы

1. **Падение жемчужины** — попытка Data Jackals вскрыть квантовый сейф. Игрок выбирает: защитить алгоритмы для Pearl Exchange или обнародовать данные.  
2. **Призрак Виктории** — расследование исчезновений в Tidal Catacombs, приводит к нелегальному симулятору воспоминаний.  
3. **Corsair Blockade** — Skyline Corsairs блокируют Skyline Loop; игрок определяет, кто получит контроль над воздушными путями.

---

## Экономика и активность

- **Биржи:** Pearl Exchange (легальные торги), Black Pearl Swap (деривативы в даркнете).  
- **Плавучие рынки:** Sampan Bazaar (данные, импланты), Underbay Clinics (гибридная хирургия).  
- **Экстракт-зоны:** подводные дата-коллекторы Red Tide, воздушные сборщики влаги в Sky Gardens.  
- **Социальные активности:** биржевые турниры, морские гонки дронов, VR-фестивали элитных семей.

---

## Модели данных

### World-service
- **CityProfile** (`cityId`, `name`, `population`, `dominantCorp`, `signatureLandmark`, `securityLevel`, `tradeFocus`, `lastUpdate`).  
- **VerticalLayer** (`layerId`, `cityId`, `name`, `level`, `socialStrata`, `hazardLevel`, `accessLevel`, `environmentalModifiers`).  
- **UrbanCluster** (`clusterId`, `cityId`, `name`, `primaryActivities`, `controllingFaction`, `eventHooks`).  
- **TransitRoute** (`routeId`, `mode`, `securityTier`, `capacity`, `factionInfluence`, `riskScore`).  
- **MarketChannel** (`channelId`, `cityId`, `type`, `liquidity`, `volatility`, `taxRate`).  
- **FactionInfluence** (`factionId`, `cityId`, `influenceScore`, `zones`, `tensionLevel`).

### Gameplay-service
- **CityMission** (`missionId`, `missionType`, `recommendedLoadouts`, `worldImpact`, `cooldownHours`).  
- **SmugglingRoute** (`routeId`, `ownerFactionId`, `resource`, `risk`, `reward`, `discoveryChance`).  
- **MarketSignal** (`signalId`, `cityId`, `source`, `trend`, `expiresAt`).

### Events (Kafka)
- `world.city.marketShifted` — изменение цен Pearl Exchange.  
- `world.city.routeThreatened` — атака Red Tide на Skyline Loop или Sampan Network.  
- `world.city.layerLockdown` — ограничение доступа к определённому уровню.  
- `gameplay.city.missionCompleted` — результат задания, изменения влияния и рынка.  
- `economy.city.blackMarketUpdate` — активность нелегальных рынков.

---

## API контракты

### REST (world-service)
1. `GET /api/v1/world/cities/hong-kong` — полное описание профиля, уровней, влияния и рыночных модификаторов.  
2. `GET /api/v1/world/cities/hong-kong/routes` — фильтры `mode`, `securityTier`, `faction`; возвращает Pearl Spine, Sampan Network, Skyline Loop, Undergrid Tram.  
3. `POST /api/v1/world/cities/hong-kong/events/trigger` — запуск событий (`pearl-fall`, `victoria-phantom`, `corsair-blockade`).  
4. `GET /api/v1/world/cities/hong-kong/factions` — влияние Pearl Exchange, Data Jackals, Red Tide, Skyline Corsairs, активные миссии.

### REST (gameplay-service)
1. `GET /api/v1/cities/hong-kong/missions` — рейды на дата-коллекторы, защита Skyline Loop, escort Sampan Network.  
2. `POST /api/v1/cities/hong-kong/missions/{missionId}/complete` — фиксирует исход, Action XP, репутационные изменения.  
3. `GET /api/v1/cities/hong-kong/market-signals` — биржевые сигналы, индексы волатильности, доступные арбитражные миссии.  
4. `POST /api/v1/cities/hong-kong/smuggling-routes/claim` — заявка на контроль контрабандного маршрута (ответ содержит риск/награды).

### GraphQL (frontend gateway)
- `cityProfile(cityId:"hong-kong")` — агрегирует данные профиля, влияния, текущих событий.  
- `cityChronicle(cityId:"hong-kong", cursor)` — лента событий (биржевые всплески, рейды, блокировки маршрутов).  
- `cityMarketSignals(cityId:"hong-kong")` — активные сигналы, рекомендации по миссиям.

---

## UX-потоки

### Биржевой интерфейс Pearl Exchange
1. Пользователь открывает дашборд: видит текущие индексы, сигналы, влияние фракций.  
2. При выборе сигнала UI предлагает миссии (арбитраж, обеспечение безопасности, взлом).  
3. После выполнения отображается аналитика по прибыли, влиянию и репутации.

### Неоновые сампаны
1. Игрок заходит в Neon Sampan Bazaar: список контрабандных контрактов Data Jackals.  
2. Интерфейс показывает риск маршрута, требуемые импланты, потенциальные награды.  
3. В ходе миссии UI обновляет статус (провал/успех), предупреждает о засадах Red Tide Triads.

### Skyline Loop Control
1. На карте воздушных путей отображаются зоны контроля Skyline Corsairs и Pearl Exchange.  
2. Игрок назначает охрану, перехваты или рейды.  
3. Результаты мгновенно появляются в хронике и влияют на цены и безопасность.

### Хроника Виктории
1. Экран «Victoria Harbour Chronicle» отображает события с фильтрами (экономика, безопасность, социальные).  
2. Клик по событию раскрывает подробности, триггеры, связанные миссии.  
3. Игрок может подписаться на обновления определённых маршрутов или фракций.

---

## Аналитика и баланс

- `marketVolatilityIndex` — отслеживает скачки стоимости ресурсов; тревога при >1.8.  
- `routeConflictRate` — частота атак на Skyline Loop/Sampan Network; влияет на генерацию оборонных миссий.  
- `blackMarketFlow` — активность на Neon Sampan Bazaar; при перегреве Pearl Exchange инициирует зачистки.  
- `factionInfluenceDrift` — изменения влияния фракций в неделю; помогает балансировать репутационные награды.

---

## Связанные документы

- `../locations-overview.md`
- `./WORLD-CITIES-DETAIL-CATALOG-2093.md`
- `../../02-gameplay/world/events/live-events-system.md`
- `../../02-gameplay/world/world-state/living-world-kenshi-hybrid.md`
- `../../02-gameplay/economy/economy-world-impact.md`

---

## История изменений

- v1.1.0 (2025-11-08 00:23) — Добавлены модели данных, API контракты, UX-потоки и аналитика.  
- v1.0.0 (2025-11-07 20:06) — Базовое описание города.

