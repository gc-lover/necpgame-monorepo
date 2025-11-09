# Денвер: Высотный узел горных альянсов (2020-2093)

**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-08 00:34  
**Статус:** review  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:34
**api-readiness-notes:** Определены модели климат-щитовой сети, REST/Kafka/GraphQL контракты и UX-флоу; готово к постановке API задач.

---

## Краткая справка

- **Название:** Mile High Denver Grid
- **Регион:** Федерация свободных штатов Скалистых гор
- **Население:** ~8,6 млн (Sky Summit 2,1 млн; Mile High 4,3 млн; Frost Hollow 2,2 млн)
- **Доминирующая корпорация:** RockyGrid Alliance
- **Ключевые банды:** Altitude Syndicate, Chrome Broncos, Frostbiters
- **Символ:** голографическая арка «Summit Gate» над куполом Capitol Hex

---

## Макроструктура

### Вертикальные уровни

| Уровень | Имя | Социальный слой | Особенности |
|---------|-----|-----------------|-------------|
| 5 | Sky Summit Platforms | Корпоративная элита, аэроклиматологи | Парящие платформы, климат-антенны, наблюдательные станции |
| 4 | Mile High Terrace | Исследовательские кластеры, верхний средний класс | Стеклянные мосты, кампусы RockyGrid, VR-центры |
| 3 | Capitol Hex Core | Городские службы, торговцы | Шестиугольный купол, магистрали MagRail, рынки лицензий |
| 2 | Canyon Strip | Промышленность, мастерские | Ремонтная инфраструктура ховер-караванов, арены Chrome Broncos |
| 1 | Frost Hollow Shelters | Убежища выживших, подполье | Термо-тоннели, серверные Frostbiters, подпольные клиники |

### Кластеры и маршруты

- **Capitol Hex District:** центр политики, лицензирования климатических щитов, штаб RockyGrid.  
- **Aurora Skybelt:** парящие платформы с аэротакси, лабораториями погодных исследований.  
- **Canyon Strip Yards:** промышленная зона, мастерские Chrome Broncos, контрабандные рынки.  
- **Frost Hollow Tunnels:** сеть убежищ, где обосновались Frostbiters и независимые рипердоки.  
- **Summit Spine:** магистраль по вершинам, охраняемая дронами.  
- **Canyon Rush:** ховер-трек для гонок и контрабанды.  
- **Thermal Loop:** подземные теплые туннели, эвакуация и логистика.  
- **MagRail Rockies:** линия к Нео-Аспену и дата-центрам, экспорт минералов.

---

## Ключевые NPC и связи

| Имя | Роль | Фракция | Локация | Описание |
|-----|------|---------|---------|----------|
| Лина «Summit» Морено | Архитектор климат-щитовой сети | RockyGrid Alliance | Capitol Hex | Расширяет купола, ищет альтернативную энергию |
| Бакари Слоун | Караванный брокер | Altitude Syndicate | Canyon Strip | Управляет торговлей изотопами, посредничает между корпорацией и бандами |
| Доктор Айви Каган | Рипердок-полевик | Независимая | Frost Hollow | Имплантирует адаптационные модули выжившим в морозе |
| «Coach» Маркус Хэйл | Лидер Chrome Broncos | Chrome Broncos | Canyon Strip | Организует неоновые гонки и рейды в энергохранилища |

**Цепочки контактa:**  
- «Щит над ущельем»: Морено → Слоун → Хэйл → поставки из MagRail для расширения купола.  
- «Тепловая трещина»: Каган → Frostbiters → Altitude Syndicate → утечка данных о тайниках корпорации.

---

## Сюжетные узлы

1. **Буря Вертикали** — сверхячейка разрушает купола; игрок решает, защищать ли Capitol Hex или спасать Frost Hollow.  
2. **Пульс Платформ** — саботаж Aurora Skybelt, расследование и гонки по Canyon Rush.  
3. **MagRail Breach** — нападения на магистраль, связанные с поставками редких минералов.

---

## Экономика и активность

- **Capitol Exchange:** лицензии на климат-щиты, энерго-права.  
- **Canyon Bazaar:** охлаждающие компоненты, ховер-моды, нелегальные апгрейды.  
- **Frost Hollow Swap:** обмен выживания, ресурсы отопления, медпрепараты.  
- **Экстракт-зоны:** магнетитовые карьеры, высокогорные дата-центры с нестабильной погодой.  
- **Активности:** гонки Chrome Broncos, VR-фестивали в Skybelt, миссии метеостанций.

---

## Модели данных

### World-service
- **CityProfile** (`cityId`, `name`, `population`, `dominantCorp`, `climateShieldStatus`, `securityLevel`, `resourceFocus`, `lastUpdate`).  
- **ClimateShieldNode** (`nodeId`, `cityId`, `status`, `energyDraw`, `coverageArea`, `maintenanceLevel`, `riskScore`).  
- **VerticalLayer**, **UrbanCluster**, **TransitRoute** (Summit Spine, Canyon Rush, Thermal Loop, MagRail).  
- **WeatherAlert** (`alertId`, `severity`, `affectedLayers`, `eta`, `expectedDuration`).  
- **FactionInfluence** (`factionId`, `influenceScore`, `zonesControlled`, `tensionLevel`).

### Gameplay-service
- **CityMission** (`missionId`, `missionType`, `recommendedLoadouts`, `worldImpact`, `cooldown`).  
- **WeatherCrisisResponse** (`crisisId`, `type`, `requiredActions`, `reward`).  
- **HoverRaceEvent** (`eventId`, `trackId`, `entryFee`, `rewardPool`, `factionImpact`).

### Events (Kafka)
- `world.city.climateShieldStatusChanged` — изменение состояния куполов.  
- `world.city.weatherAlertIssued` — предупреждение о буре (Буря Вертикали).  
- `world.city.transitIncident` — события на Canyon Rush или MagRail Rockies.  
- `gameplay.city.missionCompleted` — влияние миссий на фракции и экономику.  
- `economy.city.resourceShipment` — поставки минералов через магистрали.

---

## API контракты

### REST (world-service)
1. `GET /api/v1/world/cities/denver` — профиль города, климат-щитовые узлы, влияние фракций.  
2. `GET /api/v1/world/cities/denver/climate-shields` — список узлов, энергия, риск, активные ремонты.  
3. `POST /api/v1/world/cities/denver/weather-alerts` — запуск событий (буря, саботаж платформ).  
4. `GET /api/v1/world/cities/denver/transit` — маршруты Summit Spine, Canyon Rush, Thermal Loop, MagRail.  
5. `POST /api/v1/world/cities/denver/transit/actions` — запрос на защиту маршрута, эвакуацию, саботаж.

### REST (gameplay-service)
1. `GET /api/v1/cities/denver/missions` — контракты по защите куполов, сопровождению конвоев, расследованию саботажа.  
2. `POST /api/v1/cities/denver/missions/{missionId}/complete` — изменения влияния, энергии, репутации.  
3. `GET /api/v1/cities/denver/weather-crises` — активные кризисы, нужные ресурсы, таймеры.  
4. `POST /api/v1/cities/denver/hover-races/{eventId}/register` — регистрация в гонках Chrome Broncos.

### GraphQL (frontend gateway)
- `cityProfile(cityId:"denver")` — агрегированные данные.  
- `cityClimateDashboard(cityId:"denver")` — список узлов, стабильность, активные кризисы.  
- `cityTransitStatus(cityId:"denver")` — состояние маршрутов, угрозы.  
- `cityChronicle(cityId:"denver", cursor)` — лента событий (бури, гонки, рейды).

---

## UX-потоки

### Climate Shield Console
1. Игрок открывает консоль: видит состояние узлов, предупреждения, энерго-потребление.  
2. UI предоставляет варианты действий (усилить, перераспределить, отключить).  
3. После выполнения отображается влияние на районы и репутацию фракций.

### Canyon Rush Control
1. Интерфейс показывает текущее расписание гонок и угрозы.  
2. Игрок может зарегистрироваться, назначить охрану или организовать саботаж.  
3. Результаты гонок фиксируются в хронике, влияют на экономику Chrome Broncos.

### Frost Hollow Operations
1. В подземном UI отображаются задания по спасению, отоплению, контрабанде.  
2. Выбор действия меняет `subzero_resilience` и уровень доверия Frostbiters.  
3. Успех открывает доступ к редким зимним имплантам.

### MagRail Logistics
1. Игрок просматривает конвои MagRail Rockies: груз, охрана, риски.  
2. Интерфейс позволяет усиливать охрану, устраивать засады или подвеску поставок.  
3. Изменения отражаются в ресурсных индексах и статусах фракций.

---

## Аналитика и баланс

- `climateShieldIntegrity` — общее здоровье куполов; тревога при <70%.  
- `transitStabilityIndex` — стабильность Canyon Rush/MagRail; регулирует метрики контрабанды.  
- `hoverRacePopularity` — интерес к гонкам; влияет на награды и спавн событий.  
- `frostHollowResilience` — устойчивость подполья к бурям, определяет сложность миссий.

---

## Связанные документы

- `../locations-overview.md`
- `./WORLD-CITIES-DETAIL-CATALOG-2093.md`
- `../../02-gameplay/world/world-state/living-world-kenshi-hybrid.md`
- `../../02-gameplay/economy/economy-world-impact.md`
- `../../02-gameplay/world/events/live-events-system.md`

---

## История изменений

- v1.1.0 (2025-11-08 00:34) — Добавлены модели данных, API контракты, UX-потоки, аналитика.  
- v1.0.0 (2025-11-07 20:06) — Базовое описание Денвера.

