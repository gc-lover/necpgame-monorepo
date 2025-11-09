# Мадрид: Солнечные аркологии и катакомбы движения (2020-2093)

**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-08 00:26  
**Статус:** review  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:26
**api-readiness-notes:** Добавлены структуры данных, REST/GraphQL/Kafka контракты и UX-флоу; документ готов к постановке API задач.

---

## Краткая справка

- **Название:** Solaris Matador Madrid (Solaris Matador Holdings)
- **Регион:** Иберийский соларный союз
- **Население:** ~10,2 млн (солнечные купола 3,5 млн; центральные уровни 4,1 млн; подземные галереи 2,6 млн)
- **Ключевые банды:** Corrida Circuit, Las Mariposas, SubSol Cartel
- **Символ:** кибернетическая арена «Plaza del Sol» с голографическими стенами

---

## Макроструктура

### Вертикальные уровни

| Уровень | Имя | Социальный слой | Особенности |
|---------|-----|-----------------|-------------|
| 5 | Sol Crown Canopy | Советы Solaris, культурная элита | Кристаллические панели, дирижабли-курьеры, солнечные обсерватории |
| 4 | Plaza del Sol Nexus | Технокультурный средний класс | VR-арены коррид, университеты, творческие гильдии |
| 3 | Madrid Concourse | Жилые кварталы, сервисы | Каскадные террасы, плазы, транспортные узлы |
| 2 | Railcat Loop | Логисты, инженеры, рипердоки | Автодепо, рельсовые лаборатории, имплант-клиники |
| 1 | SubSol Catacombs | Подпольные сообщества, беженцы | Катакомбы сопротивления, энерго-подполье, данные SubSol |

### Кластеры и маршруты

- **Solaris Crown District:** штаб и диспетчеризация глобальных солнечных сетей.  
- **Plaza del Sol Arena:** комплекс аркадных коррид, шоу Corrida Circuit.  
- **Arroyo Market Belt:** ремесленные рынки, кафе, мастерские Las Mariposas.  
- **SubSol Tunnels:** подпольные энергорынки и сети сопротивления.  
- **Avenida Prismática:** центральная солнечная магистраль.  
- **Railcat Circuit:** кольцо высокоскоростных поездов и хабов.  
- **Corrida Lattice:** сеть VIP-галерей и арен.  
- **SubSol Spiral:** спираль туннелей для энергодележей и криптовалют.

---

## Ключевые NPC и связи

| Имя | Роль | Фракция | Локация | Описание |
|-----|------|---------|---------|----------|
| Гонсало Лазаро | Директор solaire-операций | Solaris Matador | Solaris Crown | Контролирует распределение энергии и расширение сетей |
| Лусия «Mariposa» Эскудеро | Торговка модами | Las Mariposas | Arroyo Market | Курирует кастомные импланты и культурные сделки |
| Доктор Томас Рохо | Рипердок-радиолог | Railcat Syndicate | Railcat Loop | Восстанавливает гонщиков и хакеров для корриды |
| «Toro» Эрнесто Хименес | Предводитель | Corrida Circuit | Plaza del Sol | Организует нелегальные бои, связан с SubSol Cartel |

**Цепочки контактов:**  
- «Солнечный поток»: Лазаро → Эскудеро → кварталы → защита сетей.  
- «Катакомбный сигнал»: Рохо → Торро → SubSol → нападения на распределители.

---

## Сюжетные узлы

1. **Солнечный разлом** — перегрев панелей; игрок решает, поддержать ли корпорацию или SubSol, влияя на энергомоды и безопасность районов.  
2. **Коррида теней** — диверсия на VIP-турнире, раскрывающая союз Corrida Circuit и SubSol.  
3. **Railcat Heist** — серия краж на Railcat Loop, требующих защиты или саботажа логистики.

---

## Экономика и активность

- **Рынки:** Arroyo Market (моды, ремёсла), Plaza del Sol (контракты, зрелища), SubSol (теневая энергия).  
- **Экстракт-зоны:** солнечные фермы и буферные генераторы за городом.  
- **Активности:** VR-фиесты, дрон-корриды, Railcat заезды, экспедиции в катакомбы.

---

## Модели данных

### World-service
- **CityProfile** (`cityId`, `name`, `population`, `dominantCorp`, `energyOutput`, `securityLevel`, `cultureIndex`, `lastUpdate`).  
- **VerticalLayer** (`layerId`, `name`, `level`, `socialStrata`, `energyAllocation`, `hazardLevel`).  
- **UrbanCluster** (`clusterId`, `primaryActivities`, `factionControl`, `eventHooks`).  
- **TransitRoute** (`routeId`, `mode`, `capacity`, `maintenanceStatus`, `riskScore`).  
- **EnergyGridNode** (`nodeId`, `outputMw`, `stabilityIndex`, `ownership`, `linkedClusters`).  
- **FactionInfluence** (`factionId`, `influenceScore`, `zones`, `tensionLevel`).

### Gameplay-service
- **CityMission** (`missionId`, `missionType`, `recommendedLoadouts`, `worldImpact`, `cooldownHours`).  
- **EnergyCrisisEvent** (`eventId`, `trigger`, `affectedNodes`, `responseOptions`).  
- **CorridaTournament** (`tournamentId`, `tier`, `entryRequirements`, `rewardTable`, `factionImpact`).

### Events (Kafka)
- `world.city.energyGridShifted` — изменения генерации или распределения.  
- `world.city.transitIncident` — происшествия на Railcat Circuit/SubSol Spiral.  
- `world.city.arenaEventTriggered` — запуск «Корриды теней» и других сюжетных событий.  
- `gameplay.city.missionCompleted` — результаты миссий, влияние на фракции и экономику.  
- `economy.city.marketShifted` — изменение цен на энергорынках.

---

## API контракты

### REST (world-service)
1. `GET /api/v1/world/cities/madrid` — профиль города, уровни, влияние фракций, статус энергосетей.  
2. `GET /api/v1/world/cities/madrid/energy-grid` — список узлов, их нагрузка, риски, активные события.  
3. `POST /api/v1/world/cities/madrid/events/trigger` — запуск событий (`solar-rift`, `corrida-shadows`, `railcat-heist`).  
4. `GET /api/v1/world/cities/madrid/transit` — данные по маршрутам Avenida Prismática, Railcat Circuit, SubSol Spiral.

### REST (gameplay-service)
1. `GET /api/v1/cities/madrid/missions` — контракты: защита солнечных ферм, стабилизация катакомб, сопровождение Railcat.  
2. `POST /api/v1/cities/madrid/missions/{missionId}/complete` — фиксирует исход, изменения энергии, репутацию, рынок.  
3. `GET /api/v1/cities/madrid/arena-tournaments` — расписание коррид, требования, награды.  
4. `POST /api/v1/cities/madrid/energy-response` — вмешательство игроков в кризис (выбор режима распределения).

### GraphQL (frontend gateway)
- `cityProfile(cityId:"madrid")` — агрегированная информация по профилю, событиям, влиянию.  
- `cityEnergyDashboard(cityId:"madrid")` — энергетические узлы, стабильность, активные кризисы.  
- `cityChronicle(cityId:"madrid", cursor)` — лента ключевых событий.  
- `cityArenaTournaments(cityId:"madrid")` — турниры, рейтинги, награды.

---

## UX-потоки

### Энергетический дашборд
1. Пользователь открывает Energy Dashboard: обзор генерации, проблемных узлов, активных кризисов.  
2. UI предлагает решения (поддержка Solaris, саботаж SubSol, перенаправление энергии).  
3. После выбора отображается влияние на районы, репутацию и экономику.

### Коррида и культурные события
1. Экран Plaza del Sol показывает активные турниры, ставки, VIP-приглашения.  
2. Игрок выбирает миссию (участие, охрана, расследование).  
3. Результаты фиксируются в хронике и влияют на культурный рейтинг города.

### Railcat Logistics
1. В интерфейсе Railcat отображаются графики поездов и угрозы.  
2. Игрок может формировать конвои, наём охраны, саботаж.  
3. Успешные операции меняют уровень безопасности и поток ресурсов.

### SubSol Underground
1. На карте катакомб показываются активные контракты SubSol, риски и награды.  
2. UI предупреждает о ловушках, запросах сопротивления, скрытых маршрутах.  
3. Решения игрока отражаются на показателях напряжения и доступности рынков.

---

## Аналитика и баланс

- `energyStabilityIndex` — устойчивость сетей; тревога при падении <0.65.  
- `arenaPopularityScore` — посещаемость коррид; влияет на доходы и миссии.  
- `railcatSecurityLevel` — безопасность логистики, инициирует оборонные задания.  
- `subsolTension` — активность подполья, регулирует сложность миссий.

---

## Связанные документы

- `../locations-overview.md`
- `./WORLD-CITIES-DETAIL-CATALOG-2093.md`
- `../../02-gameplay/world/world-state/living-world-kenshi-hybrid.md`
- `../../02-gameplay/economy/economy-world-impact.md`
- `../../02-gameplay/world/events/live-events-system.md`

---

## История изменений

- v1.1.0 (2025-11-08 00:26) — Добавлены модели данных, API контракты, UX-потоки, аналитика.  
- v1.0.0 (2025-11-07 20:06) — Базовое описание Мадрида.

