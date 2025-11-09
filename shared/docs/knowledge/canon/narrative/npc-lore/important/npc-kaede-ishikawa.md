# NPC — Каэдэ Исикава (двойной агент)

**ID персонажа:** `npc-kaede-ishikawa`  
**Тип:** npc-lore (dual agent)  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-08  
**Последнее обновление:** 2025-11-08 00:25  
**Приоритет:** высокий  
**Связанные документы:** `../dialogues/npc-kaede-ishikawa.md`, `../../quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`, `../../quests/raid/2025-11-07-raid-specter-surge.md`, `../../quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`  
**target-domain:** narrative  
**target-мicroservices:** narrative-service, world-service, social-service  
**target-frontend-модули:** modules/narrative/raids, modules/world  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:25
**api-readiness-notes:** Биография, мотивации и связи Каэдэ Исикавы подготовлены для narrative API.

---

## 1. Общая информация
- **Имя:** Каэдэ Исикава (Kaede Ishikawa)
- **Возраст:** 32
- **Происхождение:** бывший аналитик Helios Throne, завербован Specter HQ.
- **Роль:** двойной агент, поставляющий данные о Countermesh Ops и влияющий на City Unrest.

## 2. Биография
- Родилась в корпоративном анклаве Neo-Kyoto, получила образование в Helios Tactical Institute.
- Специализировалась на Countermesh сетях и Blackwall фильтрах.
- После катаклизма 2073 участвует в операциях Helios по стабилизации Underlink.
- Столкновение с последствиями Helios (гибель граждан) смещает лояльность; вступает в контакт с Kaori.
- Согласилась работать для обеих сторон, чтобы предотвратить полный коллапс сети.

## 3. Мотивации
- **Основная:** сохранить баланс между корпорациями и гражданскими, не допустив Cataclysm.
- **Вторичная:** защитить свой братский экипаж (`flag.kaede.family`), работающий на Helios.
- **Конфликт:** стремится минимизировать потери, даже если это требует лжи обеим сторонам.

## 4. Поведение и связи
- Поддерживает прямой канал с Kaori Watanabe, используя зашифрованные сигналы.
- Отчитывается перед Dr. Lysander, предоставляя отфильтрованные данные.
- При `specter-prestige` высоком уровне склоняется к Specter; при `rep.corp.helios ≥ 25` — к Helios.
- Может активировать миссии (Intel Board) или Helios Ops в зависимости от выбора игрока.

## 5. Триггеры и флаги
- `flag.kaede.loyalty` — {`specter`, `helios`, `balanced`}.
- `flag.kaede.family-threatened` — активируется при Cataclysm; Каэдэ просит помощи игрока.
- `flag.kaede.betrayal` — при провале игрока выполнить обещание Helios.

## 6. Narrative Hooks
- Q1 `Deep Cover`: Каэдэ предоставляет доступ к Helios `countermesh-log`.
- Q2 `Split Allegiance`: выступает медиатором между игроком и Lysander.
- `City Unrest` Crisis: Каэдэ предложит миссию «Evacuate Family» (PvE).
- `Specter HQ` апгрейд Tier 3: Каэдэ предоставляет аналитические панели.

## 7. Gameplay Integration
- При союзе с Specter — снижает стоимость `intel-countermesh` на 15%.
- При союзе с Helios — увеличивает шанс `helios-drone-chip`.
- При балансе — открывает смешанную ветку `neutral contracts`.
- Может стать «informant» NPC в Guild Contract Board.

## 8. API и телеметрия
- narrative-service: `GET /api/v1/narrative/npc/kaede`, `POST /api/v1/narrative/npc/kaede/state`.
- Events: `NPC_KAEDE_LoyaltyChanged`, `NPC_KAEDE_Request`.
- Telemetry: `kaede_interaction`, `kaede_loyalty_distribution`.

## 9. История изменений
- 2025-11-08 00:25 — Создан профиль NPC Каэдэ Исикавы, двойного агента Helios/Specter.

