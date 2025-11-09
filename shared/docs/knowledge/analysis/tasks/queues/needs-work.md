# Очередь документов — статус `needs-work`
---

**Статус:** active  
**Версия:** 1.0.0  
**Последнее обновление:** 2025-11-09 10:19  

---

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 10:19  
**api-readiness-notes:** Перепроверено 2025-11-09 10:19: очередь фиксирует документы, требующие доработки, и не формирует API задачи.

- Лимит файла: ≤ 500 строк. При превышении создайте `needs-work_0001.md`, `needs-work_0002.md` и свяжите файлы.
- Формат записи:

```markdown
- **Документ:** .BRAIN/05-technical/backend/player-character-mgmt/README.md (v1.0.1)  
  **Проверено:** 2025-11-08 23:56 — Brain Manager  
  **Что доработать:** Восстановить компактный файл character-management.md перед задачей API.
```

- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/saint-petersburg/2061-2077/quest-034-nicholas-ii-ghost.md (v0.0.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Добавить версию, статус и приоритет, расширить этапы и ветвления, описать награды, зависимости и целевые API пакета quest-engine перед постановкой задачи.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/saint-petersburg/2061-2077/quest-035-kunstkamera-oddities.md (v0.0.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Readiness Checker  
  **Что доработать:** Добавить версию и статус, детализировать этапы и выборы, расписать награды и системные зависимости, определить целевые API каталоги quest-engine перед постановкой задачи.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/saint-petersburg/2061-2077/quest-036-catherine-palace-ball.md (v0.0.0)  
  **Проверено:** 2025-11-09 09:46 — Brain Readiness Checker  
  **Что доработать:** Привести к QUEST-TEMPLATE: зафиксировать версии/статус, расписать ветвления, аудиенцию, KPI наград и определить целевые API каталоги quest-engine и социального сервиса.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/saint-petersburg/2061-2077/quest-037-faberge-collection.md (v0.0.0)  
  **Проверено:** 2025-11-09 09:59 — Brain Readiness Checker  
  **Что доработать:** Детализировать глобальные маршруты, способы получения яиц, экономические параметры и интеграции, определить целевые API каталоги quest-engine/economy перед постановкой задачи.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/saint-petersburg/2061-2077/quest-038-isaac-cathedral-climb.md (v0.0.0)  
  **Проверено:** 2025-11-09 10:23 — Brain Readiness Checker  
  **Что доработать:** Привести к QUEST-TEMPLATE: добавить версию/статус, расписать ветвления (погодные условия, события), интеграции с туризмом/прогрессией и определить целевые API каталоги quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/culture/CYBERPUNK-CULTURE-MASTER-INDEX.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager  
  **Что доработать:** Определить целевые микросервисы и каталоги OpenAPI, зафиксировать фронтенд модули и KPI, связать культурные эпохи с economy/social/world системами перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/events/fifth-corporate-war-2085-2088.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager  
  **Что доработать:** Сформировать фазовую модель данных и KPI, определить целевые сервисы (world, narrative, economy), каталоги OpenAPI и фронтенд модули для событий войны перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/events/fifth-war-battles-detailed.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager  
  **Что доработать:** Оформить боевые сценарии как игровые события с API контрактами, определить сервисы world/narrative/economy, каталоги OpenAPI, KPI и фронтенд модули.
- **Документ:** .BRAIN/03-lore/_03-lore/events/fifth-war-heroes-and-victims.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager  
  **Что доработать:** Превратить нарративные профили в структурированные модели NPC/событий, определить зависимости с progression/social системами и указать целевые каталоги OpenAPI.
- **Документ:** .BRAIN/03-lore/_03-lore/factions/corporations/arasaka-internal-politics-2077-2093.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager  
  **Что доработать:** Структурировать фракции Arasaka и ключевые NPC как данные, определить сервисы и каталоги OpenAPI, зафиксировать интеграции с economy/social/world системами.
- **Документ:** .BRAIN/03-lore/_03-lore/factions/corporations/CORPORATE-POLITICS-MASTER-INDEX.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager  
  **Что доработать:** Описать структуры данных для корпоративных фракций, определить целевые сервисы, каталоги OpenAPI и фронтенд модули, связать сценарии с economy/social/world зависимостями.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/kiev/2020-2029/quest-006-golden-gate.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:37 — Brain Manager  
  **Что доработать:** Оформить квест по QUEST-TEMPLATE: расписать зависимости (локации, NPC, цепочки), определить целевой микросервис, каталог OpenAPI и фронтенд-модуль, связать этапы и награды с системами прогрессии и экономики.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/kiev/2020-2029/quest-007-sophia-cathedral.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:46 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE: описать зависимости (локации, NPC, духовная цепочка), определить микросервис quest-engine, каталоги OpenAPI и фронтенд-модуль, связать этапы и награды с системами прогрессии и экономики.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/kiev/2020-2029/quest-008-chernobyl-zone.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:59 — Brain Manager  
  **Что доработать:** Структурировать сценарий экспедиции по QUEST-TEMPLATE: оформить зависимости (фракции сталкеров, экономика артефактов, экстракт-механики), определить каталоги API quest-engine и фронтенд-модуль, расписать ветви радиационных угроз и наград.

- **Документ:** .BRAIN/02-gameplay/social/npc-hiring-types.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Manager  
  **Что доработать:** Сбалансировать стоимость и эффективность ролей найма, оформить API контракты social-service и фронтенд витрины.

- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-003-teotihuacan-pyramids.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Manager  
  **Что доработать:** Расширить квест ветвлениями и проверками, связать с системами эксплоринга, транспорта и экономики, определить микросервис, каталоги OpenAPI и фронтенд-модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-004-lucha-libre.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:00 — Brain Manager  
  **Что доработать:** Добавить сценарные ветки шоу, проверки навыков, интеграции с прогрессией, экономикой и социальным рейтингом, указать целевой микросервис и фронтенд-модуль.

- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-037-love-hotel.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:36 — Brain Readiness Checker  
  **Что доработать:** Добавить статус, версию и приоритет, расширить этапы с ветвлениями, уточнить награды и определить целевые сервисы/модули для подготовки API пакета.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-038-kendo-championship.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:45 — Brain Readiness Checker  
  **Что доработать:** Добавить статус, версию, приоритет, расписать сетку поединков и ветвления (победа/поражение), описать требования к навыкам/баффам, интеграции с progression/combat, определить целевые API каталоги и фронтенд модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/moscow/2061-2077/quest-035-implant-addiction.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:37 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, расписать ветвления лечения, связать с системами имплантов и указать целевые API/микросервисы и экономику наград.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/moscow/2061-2077/quest-036-underground-arena.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:45 — Brain Manager  
  **Что доработать:** Расписать турнирные стадии, NPC, систему ставок, интеграцию с economy/combat и указать целевые API каталоги quest-engine/betting.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/moscow/2061-2077/quest-038-corpo-wedding.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:31 — Brain Manager  
  **Что доработать:** Детализировать свадебные сценарии (категории расходов, NPC, подарки), интеграцию трансляций с social/economy системами и указать целевые каталоги API quest-engine/event-service.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/yerevan/2020-2029/quest-003-armenian-cognac.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:38 — Brain Manager  
  **Что доработать:** Структурировать дегустационный квест по QUEST-TEMPLATE, добавить ветвления и зависимости, определить микросервисы narrative/gameplay, каталоги API и фронтенд модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/locations/world-cities/chicago-detailed-2020-2093.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Manager  
  **Что доработать:** Добавить модели данных, REST/Events контракты и связь с фронтендом `modules/world/atlas` для world-service.
- **Документ:** .BRAIN/02-gameplay/economy/player-market/player-market-core.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:48 — Brain Manager  
  **Что доработать:** Детализировать статусы сделок, ограничения и комиссии, прописать интеграцию с инвентарём и экономикой, события/очереди и схемы данных, увязать с API и БД перед постановкой задач economy-service.
- **Документ:** .BRAIN/02-gameplay/economy/player-market/player-market-database.md (v1.0.0)  
  **Проверено:** 2025-11-09 10:07 — Brain Manager  
  **Что доработать:** Расширить схемы БД, описать миграции, ограничения, Events/CDC, интеграцию с API/UI и экономики перед постановкой задач economy-service.
- **Документ:** .BRAIN/02-gameplay/economy/player-market/player-market-api.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:42 — Brain Manager  
  **Что доработать:** Дополнить REST/WS спецификации кодами ошибок, авторизацией, схемами запросов/ответов, событиями и интеграцией с БД и аналитикой для подготовки задач economy-service.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/phoenix/2020-2029/quest-010-urban-sprawl.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:36 — Brain Manager  
  **Что доработать:** Расширить сценарную структуру, добавить ветвления, KPI, зависимости с quest-engine и narrative-service, определить целевые каталоги API и фронтенд модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/san-francisco/2020-2029/quest-001-golden-gate-jump.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:44 — Brain Manager  
  **Что доработать:** Привести квест к QUEST-TEMPLATE, детализировать ветвления (разрешения, аварии), описать интеграции с quest-engine, системами безопасности и экономикой наград, определить каталоги API и модуль `modules/narrative/quests`.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/london/2040-2060/quest-025-wembley-arena.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:37 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, оформить YAML-метаданные, этапы с проверками и ветвлениями, связать награды с прогрессией и экономикой и указать целевой пакет quest-engine (`gameplay-service`, modules/quests).
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/london/2040-2060/quest-027-jack-the-ripper-ai.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:04 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, добавить YAML-метаданные, ветвления, интеграцию с quest-engine, ai-service и юридическими системами, указать каталоги API и модуль `modules/quests`, детализировать награды и связи с прогрессией/экономикой перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/berlin/2061-2077/quest-036-berlin-tech-conference.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:36 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE: структурировать этапы с развилками, добавить зависимости (quest-engine, economy, social), определить микросервис, каталог API и фронтенд-модуль для Berlin Tech Summit.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/berlin/2061-2077/quest-037-quantum-computing.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:45 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, добавить ветвления с последствиями передачи технологии, описать зависимости (quest-engine, research, economy), определить микросервис, каталог API и фронтенд-модуль, расширить награды и KPI.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/new-york/2040-2060/quest-022-vertical-city.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Manager  
  **Что доработать:** Оформить квест по QUEST-TEMPLATE, определить микросервис quest-engine, каталоги API `api/v1/quests/...`, фронтенд-модуль и расширить ветки/награды, зависимости с социальными и экономическими системами.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/new-york/2040-2060/quest-024-red-sky-storm.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:13 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, описать зависимые системы, определить каталоги API и фронтенд-модуль, детализировать ветвления и KPI восстановления.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-010-most-livable-city.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Зафиксировать сценарные развилки, KPI livability, экономические и социальные зависимости, определить REST/WS каталоги для quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-001-white-house.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Расписать контроль доступа, NPC Secret Service, исходы туров и интеграции с дипломатическими/безопасными системами quest-engine, определить целевые каталоги API.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-002-capitol-building.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Разработать ветвления (тур, заседание, тревога), NPC и события Jan 6, интеграции с системами безопасности/политики и указать каталоги API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-003-lincoln-memorial.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Привести к QUEST-TEMPLATE, добавить ветвления, NPC и социальные эффекты (MLK), определить KPI и каталоги API для интеграции с quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-004-smithsonian-museums.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Сформировать маршруты, расписания, ветвления и KPI посещений Smithsonian, добавить NPC и определить интеграции с образовательными/экономическими системами и каталогами API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-005-washington-monument.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Описать бронирование, очереди, погодные ограничения, NPC и KPI посещаемости, определить интеграции с системами туризма/безопасности и каталоги API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-006-vietnam-memorial.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Привести к QUEST-TEMPLATE, добавить сценарии взаимодействия с ветеранами, моральные выборы, KPI и интеграции с системами эмоций/социальных связей и каталогами API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-007-pentagon.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Описать security clearance, ветви допуска/отказа, NPC фракций и интеграции с системами угроз; определить KPI и каталоги API quest-engine перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/asia/shanghai/2020-2029/quest-008-chinese-opera.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:38 — Brain Manager  
  **Что доработать:** Привести квест к QUEST-TEMPLATE, добавить ветвления, интеграции с системами и целевые API, детализировать награды перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/denver/2020-2029/quest-004-craft-beer-scene.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:42 — Brain Manager  
  **Что доработать:** Добавить YAML-метаданные, версию, статус, ветвления и интеграции по шаблону QUEST-TEMPLATE; расширить этапы, выборы и награды перед подготовкой API задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/denver/2020-2029/quest-005-skiing-resorts.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:44 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE: добавить YAML-метаданные, статус, ветвления, зависимости с системами (economy/progression) и детализировать награды.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/denver/2020-2029/quest-006-broncos-football.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:45 — Brain Manager  
  **Что доработать:** Привести к стандарту QUEST-TEMPLATE, расписать сценарные ветви (стадион, VIP, фанатские события), определить зависимости с quest-engine и экономикой мерча, добавить версии, статус и детализированные награды.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/denver/2020-2029/quest-007-altitude-sickness.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:50 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE: добавить YAML-метаданные, статус, ветвления, зависимости с системами здоровья и экономики, определить каталоги API и расширить награды.


  **Проверено:** 2025-11-09 09:43 — Brain Readiness Checker  
  **Что доработать:** Добавить версию и статус, детализировать этапы и выборы, расписать награды и системные зависимости, определить целевые API каталоги quest-engine перед постановкой задачи.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/kiev/2020-2029/quest-006-golden-gate.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:37 — Brain Manager  
  **Что доработать:** Оформить квест по QUEST-TEMPLATE: расписать зависимости (локации, NPC, цепочки), определить целевой микросервис, каталог OpenAPI и фронтенд-модуль, связать этапы и награды с системами прогрессии и экономики.

- **Документ:** .BRAIN/02-gameplay/social/npc-hiring-types.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Manager  
  **Что доработать:** Сбалансировать стоимость и эффективность ролей найма, оформить API контракты social-service и фронтенд витрины.

- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-003-teotihuacan-pyramids.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Manager  
  **Что доработать:** Расширить квест ветвлениями и проверками, связать с системами эксплоринга, транспорта и экономики, определить микросервис, каталоги OpenAPI и фронтенд-модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-004-lucha-libre.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:00 — Brain Manager  
  **Что доработать:** Добавить сценарные ветки шоу, проверки навыков, интеграции с прогрессией, экономикой и социальным рейтингом, указать целевой микросервис и фронтенд-модуль.

- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-037-love-hotel.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:36 — Brain Readiness Checker  
  **Что доработать:** Добавить статус, версию и приоритет, расширить этапы с ветвлениями, уточнить награды и определить целевые сервисы/модули для подготовки API пакета.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-038-kendo-championship.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:45 — Brain Readiness Checker  
  **Что доработать:** Добавить статус, версию, приоритет, расписать сетку поединков и ветвления (победа/поражение), описать требования к навыкам/баффам, интеграции с progression/combat, определить целевые API каталоги и фронтенд модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/moscow/2061-2077/quest-035-implant-addiction.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:37 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, расписать ветвления лечения, связать с системами имплантов и указать целевые API/микросервисы и экономику наград.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/cis/yerevan/2020-2029/quest-003-armenian-cognac.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:38 — Brain Manager  
  **Что доработать:** Структурировать дегустационный квест по QUEST-TEMPLATE, добавить ветвления и зависимости, определить микросервисы narrative/gameplay, каталоги API и фронтенд модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/locations/world-cities/chicago-detailed-2020-2093.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Manager  
  **Что доработать:** Добавить модели данных, REST/Events контракты и связь с фронтендом `modules/world/atlas` для world-service.
- **Документ:** .BRAIN/02-gameplay/economy/player-market/player-market-api.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:42 — Brain Manager  
  **Что доработать:** Дополнить REST/WS спецификации кодами ошибок, авторизацией, схемами запросов/ответов, событиями и интеграцией с БД и аналитикой для подготовки задач economy-service.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/phoenix/2020-2029/quest-010-urban-sprawl.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:36 — Brain Manager  
  **Что доработать:** Расширить сценарную структуру, добавить ветвления, KPI, зависимости с quest-engine и narrative-service, определить целевые каталоги API и фронтенд модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/san-francisco/2020-2029/quest-001-golden-gate-jump.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:44 — Brain Manager  
  **Что доработать:** Привести квест к QUEST-TEMPLATE, детализировать ветвления (разрешения, аварии), описать интеграции с quest-engine, системами безопасности и экономикой наград, определить каталоги API и модуль `modules/narrative/quests`.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/london/2040-2060/quest-025-wembley-arena.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:37 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, оформить YAML-метаданные, этапы с проверками и ветвлениями, связать награды с прогрессией и экономикой и указать целевой пакет quest-engine (`gameplay-service`, modules/quests).
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/london/2040-2060/quest-027-jack-the-ripper-ai.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:04 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, добавить YAML-метаданные, ветвления, интеграцию с quest-engine, ai-service и юридическими системами, указать каталоги API и модуль `modules/quests`, детализировать награды и связи с прогрессией/экономикой перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/berlin/2061-2077/quest-036-berlin-tech-conference.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:36 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE: структурировать этапы с развилками, добавить зависимости (quest-engine, economy, social), определить микросервис, каталог API и фронтенд-модуль для Berlin Tech Summit.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/berlin/2061-2077/quest-037-quantum-computing.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:45 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, добавить ветвления с последствиями передачи технологии, описать зависимости (quest-engine, research, economy), определить микросервис, каталог API и фронтенд-модуль, расширить награды и KPI.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/new-york/2040-2060/quest-022-vertical-city.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:43 — Brain Manager  
  **Что доработать:** Оформить квест по QUEST-TEMPLATE, определить микросервис quest-engine, каталоги API `api/v1/quests/...`, фронтенд-модуль и расширить ветки/награды, зависимости с социальными и экономическими системами.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/new-york/2040-2060/quest-024-red-sky-storm.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:13 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, описать зависимые системы, определить каталоги API и фронтенд-модуль, детализировать ветвления и KPI восстановления.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-010-most-livable-city.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Зафиксировать сценарные развилки, KPI livability, экономические и социальные зависимости, определить REST/WS каталоги для quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-001-white-house.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Расписать контроль доступа, NPC Secret Service, исходы туров и интеграции с дипломатическими/безопасными системами quest-engine, определить целевые каталоги API.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-002-capitol-building.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Разработать ветвления (тур, заседание, тревога), NPC и события Jan 6, интеграции с системами безопасности/политики и указать каталоги API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-003-lincoln-memorial.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Привести к QUEST-TEMPLATE, добавить ветвления, NPC и социальные эффекты (MLK), определить KPI и каталоги API для интеграции с quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-004-smithsonian-museums.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Сформировать маршруты, расписания, ветвления и KPI посещений Smithsonian, добавить NPC и определить интеграции с образовательными/экономическими системами и каталогами API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-005-washington-monument.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Описать бронирование, очереди, погодные ограничения, NPC и KPI посещаемости, определить интеграции с системами туризма/безопасности и каталоги API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-006-vietnam-memorial.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Readiness Checker  
  **Что доработать:** Привести к QUEST-TEMPLATE, добавить сценарии взаимодействия с ветеранами, моральные выборы, KPI и интеграции с системами эмоций/социальных связей и каталогами API quest-engine.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/asia/shanghai/2020-2029/quest-008-chinese-opera.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:38 — Brain Manager  
  **Что доработать:** Привести квест к QUEST-TEMPLATE, добавить ветвления, интеграции с системами и целевые API, детализировать награды перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/denver/2020-2029/quest-004-craft-beer-scene.md (v0.1.0)  
  **Проверено:** 2025-11-09 09:42 — Brain Manager  
  **Что доработать:** Добавить YAML-метаданные, версию, статус, ветвления и интеграции по шаблону QUEST-TEMPLATE; расширить этапы, выборы и награды перед подготовкой API задач.

- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/san-francisco/2020-2029/quest-002-alcatraz-escape.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:20 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, расписать ветвления побега, сценарии охраны, интеграции с quest-engine, системами безопасности и экономикой наград; определить каталоги API и модуль modules/narrative/quests.


- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-005-day-of-the-dead.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:22 — Brain Manager  
  **Что доработать:** Добавить ветки праздничных маршрутов, проверки навыков и интеграции с социальным рейтингом, экономикой и narrative событиями; указать целевой микросервис и фронтенд-модуль.

- **��������:** .BRAIN/03-lore/_03-lore/timeline-author/quests/europe/rome/2020-2029/quest-006-mafia-connection.md (v0.1.0)  
  **���������:** 2025-11-09 10:11 � Brain Manager  
  **��� ����������:** �������� ��������� ����� � QUEST-TEMPLATE: �������� ������/������, ��������� ��������� � ��������, ���������� ����������� � ����������, ���������� � ����������, ������� ������� ������������, �������� API pi/v1/quests/... � ��������-������ modules/narrative/quests ����� ����������� �����.
- **��������:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/atlanta/2020-2029/quest-004-atlanta-airport.md (v0.1.0)  
  **���������:** 2025-11-09 09:35 � Brain Readiness Checker  
  **��� ����������:** Apply QUEST-TEMPLATE, ������� ����� � quest-engine, ����������� � ����������, ���������� ������� � ������� �������� API ����� ����������� ������.
- **��������:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/atlanta/2020-2029/quest-005-civil-war-history.md (v0.1.0)  
  **���������:** 2025-11-09 09:43 � Brain Readiness Checker  
  **��� ����������:** ��������������� ����� �� QUEST-TEMPLATE, �������� NPC/�����, ������������� ����������/���������� ������� � �������� quest-engine API.
- **��������:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/atlanta/2020-2029/quest-006-southern-food.md (v0.1.0)  
  **���������:** 2025-11-09 09:45 � Brain Readiness Checker  
  **��� ����������:** ������� �������������� �����, ���������/����������, NPC � ������� API endpoint ��� quest-engine/economy/social.
- **��������:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/atlanta/2020-2029/quest-007-hip-hop-capital.md (v0.1.0)  
  **���������:** 2025-11-09 09:55 � Brain Readiness Checker  
  **��� ����������:** �������������� �������� ATL ������, NPC, KPI �������� � ���������� quest-engine/social/economy, ������� REST/WS ��������.
- **��������:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/atlanta/2020-2029/quest-008-aquarium.md (v0.1.0)  
  **���������:** 2025-11-09 10:10 � Brain Readiness Checker  
  **��� ����������:** ��������� QUEST-TEMPLATE, �������� NPC/������/KPI ���������, ���������� ���������� quest-engine/economy/social � ������� API ��������.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/san-francisco/2020-2029/quest-003-silicon-valley-startup.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:30 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, добавить ветвления (финансирование, IPO, провал), интеграции с quest-engine, economy и progression, определить каталоги API и модуль modules/narrative/quests перед постановкой задач.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-006-frida-kahlo-house.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:30 — Brain Manager  
  **Что доработать:** Добавить ветвления музейного визита, проверки навыков, связи с социальным рейтингом, арт-коллекциями и экономикой; определить целевой микросервис и фронтенд-модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-007-mariachi-music.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:43 — Brain Manager  
  **Что доработать:** Разработать ветки взаимодействия с марьячи, проверки навыков, связи с социальным рейтингом, экономикой и музыкальными системами; определить целевой микросервис и фронтенд-модуль.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/san-francisco/2020-2029/quest-005-fishermans-wharf.md (v0.1.0)  
  **Проверено:** 2025-11-09 10:38 — Brain Manager  
  **Что доработать:** Применить QUEST-TEMPLATE, добавить ветвления маршрутов, интеграции с quest-engine, economy и social, определить каталоги API и модуль modules/narrative/quests перед постановкой задач.
