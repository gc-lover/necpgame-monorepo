# API: Equipment Matrix — Минимальные сущности

**Статус:** draft  
**Версия:** 0.1.0  
**Дата создания:** 2025-11-03  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 18:20
**api-readiness-check-date:** 2025-11-03
**api-readiness-notes:** Каркас сущностей для последующей спецификации OpenAPI в `API-SWAGGER`.

---

- **Status:** created
- **Last Updated:** 2025-11-07 05:15
---

---

## Цель

Задать минимальные модели данных для матрицы снаряжения, пригодные для начальной генерации OpenAPI задач (Brand, Item, Affix, GenerationRules, Contract, License).

---

## Entities (минимальные поля)

### Brand
- id: string
- name: string
- origin: enum [lore, authored, user]
- factionId?: string
- signatureBonuses: string[]
- visualStyle?: string
- status: enum [active, suspended]

### Item
- id: string
- type: enum [weapon, armor, implant, cyberdeck, mod]
- brandId: string
- rarity: enum [common, uncommon, rare, epic, legendary, artifact]
- seed: string
- level: int
- zoneTag?: string
- statsCore: object
- statsExtended?: object
- affixes: Affix[]

### Affix
- id: string
- kind: enum [simple, set, unique]
- tags: string[]
- values: object
- conditions?: object
- brandLock?: string | null

### GenerationRules (excerpt)
- id: string
- seedVersion: string
- rarityWeightsByZone: object
- affixPoolsByType: object
- constraints?: object

### Contract (supply)
- id: string
- clientId: string
- executorId: string
- spec: object
- schedule: object
- sla: object
- escrow: object
- status: enum [draft, active, fulfilled, breached, cancelled]

### License
- id: string
- ownerId: string
- tier: enum [L1, L2, L3]
- scopes: string[]
- issuedAt: datetime
- expiresAt?: datetime
- issuer?: string

---

## Notes
- Поля детализируются при подготовке OpenAPI; значения перечислений согласуются с экономика/боем.
- Соответствие: см. `02-gameplay/economy/equipment-matrix.md`.

## История изменений
- v0.1.0 (2025-11-03) — создан каркас сущностей
