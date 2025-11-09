---
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 21:35  
**Приоритет:** high  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 21:35
**api-readiness-notes:** Каталог авторских корпораций/банд: лор, история, механики, особенности, интеграция с world-service и social-service.
---

# Авторские корпорации и банды — Каталог

**target-domain:** lore/factions  
**target-microservice:** world-service (8086) / social-service (8084)  
**target-frontend-module:** modules/world/factions  
**интеграции:** world events, faction reputation, contracts, combat events

---

## 1. Цели каталога
- Расширить мир оригинальными фракциями (корпорации, банды, синдикаты) в стиле Cyberpunk.
- Предоставить лор, историю, ключевые лидеры, механики, игровые особенности и API-контуры.
- Обновить экосистему, в которой world-service и social-service смогут запускать события, менять репутации и выдавать награды.

## 2. Сводная таблица
| Код | Название | Тип | База операций | Основной фокус |
| --- | --- | --- | --- | --- |
| `corp-aeon-dynasty` | Aeon Dynasty Systems | Корпорация | Neo-Shanghai Orbital Ring | Альфа-квантовая логистика, нейрооблака |
| `corp-crescent-energy` | Crescent Energy Union | Корпорация | Riyadh-Energy Arcology | Возобновляемая энергия, пустынные экосистемы |
| `corp-mnemosyne` | Mnemosyne Archives | Корпорация | Reykjavik Data Vault | Память, кибернетические воспоминания, интеллект |
| `gang-ember-saints` | Ember Saints | Банда | Night City, Vista del Rey | Техно-культисты, огненные импланты |
| `gang-void-sirens` | Void Sirens | Банда | Lagos Deep Port | Нуль-гравитация, спейсинг, пиратство |
| `gang-basilisk-sons` | Basilisk Sons | Синдикат | Nomad Frontier | Экзо-броня, мехи, контрабанда |
| `guild-quantum-fable` | Quantum Fable Collective | Гильдия | Night City Holo Nexus | AR/VR нарративы, инфовойны |

## 3. Корпорации
### Aeon Dynasty Systems (`corp-aeon-dynasty`)
- **База:** орбитальная станция вокруг Neo-Shanghai.
- **История:** потомки древних корпо-домов, инвестировали в орбитальные солнечные фермы, приобрели контроль над космической логистикой.
- **Лидеры:**
  - `Liang "Celestial" Wen` — CEO, визионер, стремится к пост-человеческой империи.
  - `Chief Architect Hana Zhou` — руководитель квантовых облачных сетей.
- **Механики:**
  - `Orbital Supply Lines` — система доставки редких компонентов (economy-service), влияет на торговые маршруты.
  - `Celestial Contracts` — игроки заключают VIP-контракты (social-service) для доступа к орбитальным рейдам.
- **Особенности:**
  - Высокая репутация открывает квантовые импланты, низкая — вызывает "orbital lockdown".
  - Связь с world-boss `wb-eclipse-seraph`.
- **API контуры:**
  - `GET /world/factions/aeon/contracts`
  - `POST /world/factions/aeon/raid-access`
  - `GET /world/factions/aeon/reputation`

### Crescent Energy Union (`corp-crescent-energy`)
- **База:** мегаполис-аркология в Riyadh, управляет пустынными энергополями.
- **История:** синдикат нефтяных династий трансформировался в солнечно-ветровой консорциум; сотрудничает с Nomad Coalition.
- **Лидеры:**
  - `Amira Al-Faris` — генеральный директор, "Empress of Solar Dunes".
  - `Chief Engineer Yassin Barakat` — проектирует пустынные экзоскелеты.
- **Механики:**
  - `Desert Grid Control` — динамические эвенты sandstorm vs energy grid (world-service).
  - `Energy Bond Market` — экономические мини-игры, игроки инвестируют в энергооблигации (economy-service).
- **Особенности:**
  - Репутация влияет на стоимость топлива, доступ к Nomad караванам.
  - Связь с Nomad защитником Juniper и world events в Badlands.
- **API:**
  - `GET /world/factions/crescent/events`
  - `POST /economy/crescent/bonds`
  - `GET /social/factions/crescent/reputation`

### Mnemosyne Archives (`corp-mnemosyne`)
- **База:** Reykjavik Data Vault на острове Исландия.
- **История:** компания хранит цифровые воспоминания и "реставрирует" личности.
- **Лидеры:**
  - `Dr. Sofia "Mneme" Arvidsson` — главный куратор памяти.
  - `Security Chief Einar Kovac` — управляет "Memory Wardens".
- **Механики:**
  - `Memory Synthesis` — игроки могут заказывать восстановление потерянных воспоминаний (story перки).
  - `Archive Heists` — PvE задания по защиту/кражу личностей (combat + narrative).
- **Особенности:**
  - Высокая репутация дает доступ к "ghost allies" (NPC companion system).
  - Связано с Voodoo Boys и Loa Whisperer Etienne (Blackwall).
- **API:**
  - `POST /world/factions/mnemosyne/memory-contract`
  - `GET /world/factions/mnemosyne/archive-heists`
  - `POST /social/factions/mnemosyne/standing`

## 4. Банды
### Ember Saints (`gang-ember-saints`)
- **База:** Night City, Vista del Rey, подпольные "храмы огня".
- **История:** техно-культ бывших пожарных и пироманов, поклоняются очистительной силе плазмы.
- **Лидеры:** `Mother Pyra` — харизматичная лидер, `Ignis Twins` — дуэт близнецов-инженеров.
- **Механики:**
  - `Inferno Rituals` — события очистки районов, повышающие уровень безопасности за счёт агрессивных методов.
  - `Firebrand Mods` — специальные плазменные импланты (риски киберпсихоза).
- **Особенности:**
  - Игроки выбирают между очищающими эвентами и сохранением жителей.
  - Репутация дает доступ к огненным имплантам, но повышает кибердестабилизацию.
- **API:** `GET /world/gangs/ember/rituals`, `POST /combat/ember/firebrand`

### Void Sirens (`gang-void-sirens`)
- **База:** Lagos Deep Port, плавучие станции.
- **История:** бывшие космопираты, перешедшие на нуль-гравитационные станции; специализируются на "silent boarding".
- **Лидеры:** `Captain Nyla "Siren" Kalu`, `Engineer Adebayo "Drift"`.
- **Механики:**
  - `Zero-G Boarding` — миссии с нулевой гравитацией.
  - `Quantum Echo` — использование эхолокации для слежения.
- **Особенности:**
  - Рейды на орбитальные карго, влияние на торговые маршруты Aeon Dynasty.
- **API:** `POST /world/gangs/void/boarding`, `GET /economy/void/black-market`

### Basilisk Sons (`gang-basilisk-sons`)
- **База:** Nomad Frontier, мобильные караваны мехов.
- **История:** родственные кланы Nomad, пилотирующие мехи "Basilisk".
- **Лидеры:** `Marshal "Stone-Scale" Vega`.
- **Механики:**
  - `Mech Patrols` — события защиты караванов.
  - `Exo-Chassis Crafting` — rare кузница для Nomad мехов.
- **Особенности:**
  - Высокая репутация даёт доступ к мехам, провал — атаки на лагеря игроков.
- **API:** `GET /world/gangs/basilisk/patrols`, `POST /economy/basilisk/chassis`

## 5. Гильдии/Коллективы
### Quantum Fable Collective (`guild-quantum-fable`)
- **Тип:** AR/VR storytellers, хакеры нарративов.
- **База:** Night City Holo Nexus.
- **История:** фрилансеры, создающие импровизированные кампании в реальном времени; запускают "story heists".
- **Лидеры:** `Narrative Architect Lyra Voss`, `Producer Orion Lex`.
- **Механики:**
  - `Story Heist` — кросс-модовые события, где игроки участвуют в AR-набегах.
  - `Memory Reforge` — коллаборация с Mnemosyne Archives.
- **Особенности:**
  - Репутация открывает AR-магазины, уникальные эмоции и истории.
- **API:** `POST /world/guilds/quantum/story-heist`, `GET /social/guilds/quantum/reputation`

## 6. Интеграции с системами
- **World Events:**
  - Aeon Dynasty ↔ `wb-eclipse-seraph`
  - Crescent Energy ↔ Nomad/Badlands события
  - Ember Saints ↔ городские эвенты, уровень безопасности
  - Void Sirens ↔ морские/орбитальные миссии
- **Social-service:** каждая фракция добавляет ветки репутации, контракты, романтические сцены.
- **Economy-service:** торговля энергией, орбитальные поставки, контрабанда мехов.
- **Combat-session:** уникальные способности защитников, механики рейдов.

## 7. API Контуры (общее)
| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/world/factions/{factionId}` | `GET` | Основные данные, история, лидеры |
| `/world/factions/{factionId}/events` | `GET` | Активные/запланированные события |
| `/world/factions/{factionId}/reputation` | `GET` | Репутационные шкалы и награды |
| `/world/factions/{factionId}/contracts` | `POST` | Создание контрактов/квестов |
| `/world/factions/{factionId}/aftermath` | `POST` | Фиксация исходов событий |

## 8. Схемы данных (общее)
```sql
CREATE TABLE custom_factions (
    faction_id VARCHAR(64) PRIMARY KEY,
    faction_type VARCHAR(32) NOT NULL,
    name VARCHAR(120) NOT NULL,
    hq_location VARCHAR(120) NOT NULL,
    focus TEXT NOT NULL,
    history TEXT NOT NULL,
    leaders JSONB NOT NULL,
    mechanics JSONB NOT NULL,
    api_hooks JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE custom_faction_events (
    faction_id VARCHAR(64) REFERENCES custom_factions(faction_id) ON DELETE CASCADE,
    event_code VARCHAR(64) NOT NULL,
    description TEXT NOT NULL,
    triggers JSONB NOT NULL,
    rewards JSONB NOT NULL,
    world_state_effects JSONB,
    PRIMARY KEY (faction_id, event_code)
);
```

## 9. Готовность
- Каталог заполнен, включает лор, историю, лидеров, механики, API-контакты и схемы данных.
- Готов к использованию Brain Readiness Checker и API Task Creator.
- Связан с existing doc: `03-lore/factions/factions-overview.md`, `02-gameplay/world/world-bosses-catalog.md`, `02-gameplay/world/faction-cult-defenders.md`.

