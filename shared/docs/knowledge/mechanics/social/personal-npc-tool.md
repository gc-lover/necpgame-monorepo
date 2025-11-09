# Инструмент персональных NPC — Каркас

**Статус:** review - формализация завершена  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-03  
**Последнее обновление:** 2025-11-03  
**Приоритет:** высокий

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-03 20:05
**api-readiness-notes:** Документ готов к созданию API задач. Формализованы модели данных (PersonalNPC, ScenarioBlueprint, ScenarioInstance, Contract, License, Certificate, AuditLog, Ledger), события/сигналы и матрица прав (v1.0.0). TODO только для детализации конкретных сценариев и балансировки (не блокирует создание API).

---

- **Status:** created
- **Last Updated:** 2025-11-07 00:35
---

---

## Цель

Дать игрокам/кланам инструмент конфигурации персональных NPC, способных выполнять поручения в экономике/социальных/боевых сценариях, с безопасностью, аудитом и рыночной экосистемой сценариев.

## Область

- Принадлежность: личные, клановые/организационные NPC; перенос по контрактам
- Роли: базовые/средние/расширенные (открываются по прогрессу/репутации)
- Автоматизация: ручные поручения, шаблоны, правила/триггеры (расписания/KPI)
- Экономика: модели затрат по типу NPC (человек vs робот/андроид)
- Качество/ранг: рост от опыта/репутации; специализации/перки
- Безопасность: лимиты, лицензии/допуски, логи/аудит, анти-абуз
- Интеграции: economy/social/combat

## Схема данных (формализованная)

### Owner (владелец NPC)
- **id**: string (UUID) - уникальный идентификатор владельца
- **type**: enum [player, clan, organization] - тип владельца
- **playerId**: string? - идентификатор игрока (если type=player)
- **clanId**: string? - идентификатор клана (если type=clan)
- **organizationId**: string? - идентификатор организации (если type=organization)
- **reputation**: object - репутация с фракциями {factionId: standing}
- **limits**: object - лимиты владения {maxNPCs: int, maxScenarios: int}
- **licenses**: string[] - список идентификаторов лицензий владельца

### PersonalNPC (персональный NPC)
- **id**: string (UUID) - уникальный идентификатор NPC
- **ownerId**: string - идентификатор владельца
- **name**: string - имя NPC
- **type**: enum [human, robot, android] - тип NPC (человек, робот, андроид)
- **rank**: int (1-10) - ранг NPC (рост от опыта/репутации)
- **level**: int (1-50+) - уровень NPC
- **experience**: float - текущий опыт NPC
- **specializations**: string[] - специализации NPC (guard, courier, merchant, informant, engineer, medic, diplomat, fixer, quest_giver и т.д.)
- **roles**: RoleAssignment[] - назначенные роли и их параметры
- **stats**: NPCStats - характеристики NPC (skills, efficiency, loyalty)
- **costs**: NPCCosts - текущие расходы на содержание NPC
- **status**: enum [idle, working, resting, training, maintenance] - текущий статус NPC
- **createdAt**: datetime - дата создания NPC
- **lastMaintenance**: datetime? - дата последнего обслуживания

### RoleAssignment (назначение роли)
- **roleId**: string - идентификатор роли из RoleSet
- **priority**: int (1-10) - приоритет роли (для автоматизации)
- **conditions**: object? - условия активации роли (опционально)
- **assignedAt**: datetime - дата назначения роли
- **isActive**: boolean - активна ли роль в данный момент

### NPCStats (характеристики NPC)
- **skills**: object - навыки NPC {skillId: level}
- **efficiency**: float (0.0-200.0) - эффективность работы в процентах
- **loyalty**: float (0.0-100.0) - лояльность к владельцу в процентах
- **reputation**: object - репутация NPC с другими NPC/фракциями {entityId: standing}
- **health**: float (0.0-100.0) - здоровье NPC в процентах
- **energy**: float (0.0-100.0) - энергия NPC в процентах
- **stress**: float (0.0-100.0) - уровень стресса NPC

### NPCCosts (расходы на содержание NPC)
- **baseCost**: float - базовая стоимость содержания (валютные единицы в день)
- **maintenanceCost**: float? - стоимость обслуживания (для роботов/андроидов)
- **licenseCost**: float? - стоимость лицензий (если требуются)
- **insuranceCost**: float? - стоимость страховки (если застрахован)
- **totalDailyCost**: float - общая дневная стоимость содержания

### RoleSet (доступные роли)
- **id**: string - идентификатор роли
- **name**: string - название роли
- **category**: enum [basic, intermediate, advanced] - категория роли
- **unlockRequirements**: object? - требования для разблокировки {reputation: object, level: int, license: string[]}
- **allowedScenarios**: string[] - типы сценариев, доступные для роли
- **baseEfficiency**: float - базовая эффективность роли (0.0-100.0)
- **costMultiplier**: float - множитель стоимости содержания для роли

**Типы ролей:**
- **basic:** guard (охрана), courier (курьер), merchant (торговец)
- **intermediate:** informant (информатор), engineer (инженер), medic (медик)
- **advanced:** diplomat (дипломат), fixer (фиксер), quest_giver (квестодатель)

### ScenarioBlueprint (блупринт сценария)
- **id**: string (UUID) - уникальный идентификатор блупринта
- **name**: string - название сценария
- **description**: string - описание сценария
- **authorId**: string - идентификатор автора (игрок/клан/организация)
- **version**: string - версия блупринта (для версионирования)
- **category**: enum [economy, social, combat, logistics] - категория сценария
- **requiredRoles**: string[] - требуемые роли для выполнения сценария
- **steps**: ScenarioStep[] - набор шагов/правил сценария
- **parameters**: object? - параметры сценария (настройки, лимиты)
- **conditions**: object? - условия запуска сценария
- **rewards**: object? - награды за выполнение сценария
- **costs**: object? - затраты на выполнение сценария
- **isPublic**: boolean - публичный ли блупринт (для маркетплейса)
- **isVerified**: boolean - верифицирован ли блупринт (модерация)
- **price**: float? - цена блупринта (если продается)
- **createdAt**: datetime - дата создания
- **updatedAt**: datetime - дата обновления

### ScenarioStep (шаг сценария)
- **id**: string - идентификатор шага
- **order**: int - порядок выполнения шага
- **type**: enum [action, condition, loop, parallel] - тип шага
- **action**: string - действие шага (описание или код действия)
- **parameters**: object? - параметры действия
- **conditions**: object? - условия выполнения шага
- **onSuccess**: string? - следующий шаг при успехе
- **onFailure**: string? - следующий шаг при неудаче
- **timeout**: float? - таймаут выполнения шага (сек)

### ScenarioInstance (запущенный сценарий)
- **id**: string (UUID) - уникальный идентификатор инстанса
- **blueprintId**: string - идентификатор блупринта
- **npcId**: string - идентификатор NPC, выполняющего сценарий
- **ownerId**: string - идентификатор владельца
- **status**: enum [pending, running, paused, completed, failed, cancelled] - статус выполнения
- **currentStep**: int - текущий шаг выполнения
- **parameters**: object - параметры инстанса (переопределяют параметры блупринта)
- **kpi**: ScenarioKPI - показатели эффективности выполнения
- **startedAt**: datetime? - дата начала выполнения
- **completedAt**: datetime? - дата завершения
- **scheduledAt**: datetime? - запланированное время запуска (для расписаний)
- **duration**: float? - длительность выполнения (сек)
- **result**: object? - результат выполнения сценария

### ScenarioKPI (показатели эффективности)
- **successRate**: float (0.0-100.0) - процент успешности выполнения
- **efficiency**: float (0.0-200.0) - эффективность выполнения в процентах
- **timeToComplete**: float? - время выполнения (сек)
- **resourcesUsed**: object - использованные ресурсы {resourceId: amount}
- **rewardsEarned**: object - заработанные награды {rewardId: amount}
- **costsIncurred**: object - понесенные затраты {costId: amount}
- **issues**: string[] - список проблем при выполнении

### Contract (контракт владения/лизинга)
- **id**: string (UUID) - уникальный идентификатор контракта
- **type**: enum [ownership_transfer, lease, loan] - тип контракта
- **npcId**: string - идентификатор NPC
- **fromOwnerId**: string - идентификатор текущего владельца
- **toOwnerId**: string - идентификатор нового владельца/арендатора
- **terms**: ContractTerms - условия контракта
- **status**: enum [draft, active, fulfilled, breached, cancelled] - статус контракта
- **signedAt**: datetime? - дата подписания
- **expiresAt**: datetime? - дата истечения (если применимо)
- **fulfilledAt**: datetime? - дата выполнения

### ContractTerms (условия контракта)
- **duration**: float? - длительность контракта (дни)
- **payment**: object - условия оплаты {amount: float, currency: string, schedule: string}
- **obligations**: object[] - обязательства сторон
- **penalties**: object? - штрафы за нарушение условий
- **guarantees**: object? - гарантии (эскроу, депозит)
- **rights**: object - права сторон (владение, делегирование, администрирование, запуск сценариев)

### License (лицензия/сертификат)
- **id**: string (UUID) - уникальный идентификатор лицензии
- **ownerId**: string - идентификатор владельца лицензии
- **type**: enum [npc_count, scenario_count, role_access, zone_access] - тип лицензии
- **tier**: enum [L1, L2, L3] - уровень лицензии (L1 - базовый, L3 - максимальный)
- **scopes**: string[] - области действия лицензии {roles: string[], zones: string[], scenarios: string[]}
- **limits**: object - лимиты лицензии {maxNPCs: int, maxScenarios: int, maxRoles: int}
- **issuedAt**: datetime - дата выдачи
- **expiresAt**: datetime? - дата истечения (если применимо)
- **issuer**: string? - выдавший лицензию (фракция, организация, система)
- **status**: enum [active, suspended, revoked, expired] - статус лицензии

### Certificate (сертификат/допуск)
- **id**: string (UUID) - уникальный идентификатор сертификата
- **npcId**: string? - идентификатор NPC (если сертификат для NPC)
- **ownerId**: string - идентификатор владельца
- **type**: enum [role_certification, zone_access, scenario_approval] - тип сертификата
- **scope**: string - область действия (роль, зона, тип сценария)
- **requirements**: object - требования для получения сертификата {reputation: object, level: int, skills: object}
- **issuedAt**: datetime - дата выдачи
- **expiresAt**: datetime? - дата истечения
- **issuer**: string? - выдавший сертификат

### AuditLog (логи аудита)
- **id**: string (UUID) - уникальный идентификатор записи
- **timestamp**: datetime - дата и время события
- **entityType**: enum [npc, scenario, contract, license, owner] - тип сущности
- **entityId**: string - идентификатор сущности
- **action**: enum [create, update, delete, start, stop, transfer, breach, fulfill] - тип действия
- **actorId**: string - идентификатор инициатора действия (игрок/NPC/система)
- **details**: object - детали действия (изменения, параметры, результаты)
- **ipAddress**: string? - IP адрес (для безопасности)
- **signature**: string? - цифровая подпись (для валидации)

### Ledger (журнал операций)
- **id**: string (UUID) - уникальный идентификатор записи
- **transactionId**: string - идентификатор транзакции
- **timestamp**: datetime - дата и время транзакции
- **type**: enum [cost, reward, payment, transfer, fee] - тип операции
- **ownerId**: string - идентификатор владельца
- **npcId**: string? - идентификатор NPC (если применимо)
- **scenarioId**: string? - идентификатор сценария (если применимо)
- **amount**: float - сумма операции
- **currency**: string - валюта операции
- **description**: string - описание операции
- **reference**: string? - ссылка на связанную операцию/контракт

## Потоки

1) Конфигурация NPC → назначение ролей → выбор сценария (ручной/шаблон/правила) → запуск → мониторинг KPI → награды/издержки
2) Маркетплейс сценариев → покупка/продажа ScenarioBlueprint → инсталляция → локальная настройка
3) Передача NPC по Contract между Owner-слоями (личный ↔ клан/организация)

## Матрица прав и лимитов (формализованная)

### Иерархия прав на NPC

**Уровни прав:**
1. **Ownership (владение)** - полные права: владение, удаление, передача, администрирование
2. **Administration (администрирование)** - права управления: назначение ролей, создание сценариев, настройка параметров
3. **Delegation (делегирование)** - права выполнения: запуск сценариев, мониторинг работы, сбор наград
4. **Observation (наблюдение)** - права просмотра: мониторинг статуса, просмотр KPI, чтение логов

**Матрица прав по типу владельца:**
- **Player (игрок):** Ownership → Administration → Delegation → Observation (все уровни)
- **Clan member (член клана):** зависит от ранга в клане (clanRank)
  - Leader: Ownership (для клановых NPC)
  - Officer: Administration (для клановых NPC)
  - Member: Delegation (для клановых NPC)
  - Recruit: Observation (для клановых NPC)
- **Organization member (член организации):** зависит от роли в организации (orgRole)
  - Director: Ownership (для организационных NPC)
  - Manager: Administration (для организационных NPC)
  - Employee: Delegation (для организационных NPC)
  - Associate: Observation (для организационных NPC)

### Лимиты по лицензиям

**L1 (Базовый уровень):**
- Максимум NPC: 1-3 (зависит от репутации)
- Максимум сценариев: 1-2 одновременно
- Доступные роли: только basic (guard, courier, merchant)
- Доступные зоны: только безопасные зоны
- Максимум контрактов: 0-1 активных

**L2 (Средний уровень):**
- Максимум NPC: 3-10 (зависит от репутации)
- Максимум сценариев: 2-5 одновременно
- Доступные роли: basic + intermediate (informant, engineer, medic)
- Доступные зоны: безопасные + средние зоны
- Максимум контрактов: 1-3 активных

**L3 (Продвинутый уровень):**
- Максимум NPC: 10-50 (зависит от репутации)
- Максимум сценариев: 5-20 одновременно
- Доступные роли: все роли (basic + intermediate + advanced)
- Доступные зоны: все зоны (с учетом лицензий на зоны)
- Максимум контрактов: 3-10 активных

**Влияние репутации на лимиты:**
- Репутация с фракциями влияет на лимиты NPC/сценариев/контрактов (+10-50% к лимитам при высокой репутации)
- Репутация владельца влияет на доступ к ролям/зонам/сценариям (требования для разблокировки)
- Репутация NPC влияет на эффективность выполнения сценариев (+5-25% к efficiency при высокой репутации)

### Контракты переноса

**Типы контрактов:**
- **Ownership Transfer (передача владения):** полная передача NPC другому владельцу
- **Lease (аренда):** временная передача NPC на срок (с возвратом)
- **Loan (займ):** временная передача NPC с условием возврата

**Условия контракта:**
- **Срок:** duration в днях (для lease/loan)
- **Оплата:** amount, currency, schedule (разовая/периодическая)
- **Обязательства:** обязанности сторон (содержание, обслуживание, использование)
- **Штрафы:** penalties за нарушение условий (фикс/процент)
- **Гарантии:** эскроу/депозит для обеспечения выполнения

**Процесс переноса:**
1. Создание контракта (draft)
2. Предложение контракта другой стороне
3. Подписание контракта (active)
4. Передача NPC (переход в новое владение)
5. Мониторинг выполнения контракта
6. Завершение контракта (fulfilled) или нарушение (breached)

### Аудит и безопасность

**Логирование операций:**
- Все действия с NPC записываются в AuditLog
- Все финансовые операции записываются в Ledger
- Влияние на репутацию: нарушения → снижение репутации, успешное выполнение → повышение репутации

**Анти-абуз меры:**
- Лимиты на количество NPC/сценариев/контрактов по лицензиям
- Лимиты на частоту операций (защита от спама)
- Проверка валидности контрактов (предотвращение мошенничества)
- Аудит всех операций (выявление нарушений)
- Санкции за нарушения (штрафы, блокировки, черные списки)

## Экономика содержания (формализованная)

### Модель затрат для Human-type NPC

**Базовые расходы (в день):**
- **Зарплата:** baseSalary (зависит от ранга/уровня NPC)
- **Питание:** foodCost (зависит от активности NPC)
- **Жилье:** housingCost (зависит от уровня жилья)
- **Быт:** livingCost (коммунальные услуги, одежда и т.д.)
- **Страхование:** insuranceCost (опционально, снижает риск потери NPC)
- **Бонусы:** bonuses (зависит от эффективности и лояльности)

**Формула базовой стоимости:**
```
baseDailyCost = baseSalary + foodCost + housingCost + livingCost + insuranceCost + bonuses
```

**Диапазоны значений (примерные):**
- Ранг 1-3: baseSalary 50-150, totalDailyCost 100-300 ед валюты
- Ранг 4-6: baseSalary 150-400, totalDailyCost 300-700 ед валюты
- Ранг 7-10: baseSalary 400-1000, totalDailyCost 700-1500 ед валюты

### Модель затрат для Robot/Android-type NPC

**Базовые расходы (в день):**
- **Лицензии:** licenseCost (обновления ПО, доступ к функциям)
- **Техобслуживание:** maintenanceCost (зависит от износа и активности)
- **Энергия:** energyCost (зависит от потребления энергии)
- **Запчасти:** partsCost (зависит от повреждений/износа)
- **Страхование:** insuranceCost (опционально, для дорогих моделей)

**Формула базовой стоимости:**
```
baseDailyCost = licenseCost + maintenanceCost + energyCost + partsCost + insuranceCost
```

**Диапазоны значений (примерные):**
- Ранг 1-3: licenseCost 30-100, totalDailyCost 80-250 ед валюты
- Ранг 4-6: licenseCost 100-300, totalDailyCost 250-600 ед валюты
- Ранг 7-10: licenseCost 300-800, totalDailyCost 600-1500 ед валюты

### Гибридные схемы

**Комбинированные модели:**
- Human с киберимплантами: базовая стоимость human + стоимость имплантов
- Android с человеческими компонентами: базовая стоимость android + стоимость компонентов
- Модель выбирается владельцем/фракцией при создании NPC

**Оптимизация затрат:**
- Обслуживание через фракции (скидки для членов фракции)
- Групповое страхование (скидки при страховке нескольких NPC)
- Оптимизация расписания (снижение затрат при неактивном времени)

## Безопасность и ограничения (формализованные)

### Лимиты по количеству

**Базовые лимиты (зависят от лицензий):**
- **NPC:** maxNPCs (1-50, зависит от лицензии и репутации)
- **Сценарии:** maxScenarios (1-20 одновременно, зависит от лицензии)
- **Конкурирующие задачи:** maxConcurrentTasks (1-10 на NPC, зависит от ранга NPC)
- **Контракты:** maxContracts (0-10 активных, зависит от лицензии)

**Влияние репутации:**
- Высокая репутация с фракциями → +10-50% к лимитам
- Низкая репутация → -10-30% к лимитам
- Репутация владельца влияет на базовые лимиты

### Лицензии/сертификаты

**Типы лицензий:**
- **NPC Count License:** лимит на количество NPC
- **Scenario Count License:** лимит на количество сценариев
- **Role Access License:** доступ к определенным ролям
- **Zone Access License:** доступ к определенным зонам

**Получение лицензий:**
- Фракции: репутация с фракцией → доступ к лицензиям фракции
- Рынки: покупка лицензий на рынках игроков
- Контракты: получение лицензий через контракты с организациями
- Сертификация: прохождение сертификации для получения лицензий

### Логи/аудит

**Логирование действий:**
- Все действия с NPC записываются в AuditLog (create, update, delete, start, stop, transfer)
- Все финансовые операции записываются в Ledger (costs, rewards, payments, transfers)
- Влияние на репутацию: нарушения → снижение репутации, успешное выполнение → повышение репутации

**Анти-абуз меры:**
- Лимиты на частоту операций (защита от спама/ботов)
- Проверка валидности контрактов (предотвращение мошенничества)
- Аудит всех операций (выявление нарушений)
- Санкции за нарушения (штрафы, блокировки, черные списки)

## Интеграции

- Economy: магазины, логистика, страхование, тендеры
- Social: дипломатия, контракты, репутация, фракции
- Combat: охрана/эскорт, риск, взаимодействие с ИИ врагов

## События и сигналы (формализованные)

### События NPC

**События жизненного цикла:**
- `npc.created` - NPC создан (parameters: npcId, ownerId, type, name)
- `npc.updated` - NPC обновлен (parameters: npcId, changes, actorId)
- `npc.deleted` - NPC удален (parameters: npcId, reason, actorId)
- `npc.leveled_up` - NPC повысил уровень (parameters: npcId, oldLevel, newLevel)
- `npc.ranked_up` - NPC повысил ранг (parameters: npcId, oldRank, newRank)

**События статуса:**
- `npc.status_changed` - статус NPC изменился (parameters: npcId, oldStatus, newStatus)
- `npc.health_changed` - здоровье NPC изменилось (parameters: npcId, oldHealth, newHealth, reason)
- `npc.energy_changed` - энергия NPC изменилась (parameters: npcId, oldEnergy, newEnergy, reason)
- `npc.stress_changed` - стресс NPC изменился (parameters: npcId, oldStress, newStress, reason)

**События работы:**
- `npc.role_assigned` - роль назначена NPC (parameters: npcId, roleId, priority)
- `npc.role_removed` - роль удалена у NPC (parameters: npcId, roleId)
- `npc.maintenance_required` - требуется обслуживание NPC (parameters: npcId, reason, urgency)

### События сценариев

**События жизненного цикла:**
- `scenario.created` - сценарий создан (parameters: scenarioId, blueprintId, npcId, ownerId)
- `scenario.started` - сценарий запущен (parameters: scenarioId, startedAt)
- `scenario.paused` - сценарий приостановлен (parameters: scenarioId, reason)
- `scenario.resumed` - сценарий возобновлен (parameters: scenarioId)
- `scenario.completed` - сценарий завершен успешно (parameters: scenarioId, result, rewards)
- `scenario.failed` - сценарий завершен с ошибкой (parameters: scenarioId, reason, errors)
- `scenario.cancelled` - сценарий отменен (parameters: scenarioId, reason, actorId)

**События выполнения:**
- `scenario.step_started` - шаг сценария начат (parameters: scenarioId, stepId, stepOrder)
- `scenario.step_completed` - шаг сценария завершен (parameters: scenarioId, stepId, result)
- `scenario.step_failed` - шаг сценария провален (parameters: scenarioId, stepId, error)
- `scenario.kpi_updated` - KPI сценария обновлены (parameters: scenarioId, kpi)

**События нарушений:**
- `scenario.condition_breached` - нарушены условия сценария (parameters: scenarioId, condition, breachDetails)
- `scenario.timeout` - сценарий превысил таймаут (parameters: scenarioId, timeout, currentStep)

### События контрактов

**События жизненного цикла:**
- `contract.created` - контракт создан (parameters: contractId, type, fromOwnerId, toOwnerId)
- `contract.signed` - контракт подписан (parameters: contractId, signedAt)
- `contract.activated` - контракт активирован (parameters: contractId, npcId)
- `contract.fulfilled` - контракт выполнен (parameters: contractId, fulfilledAt, result)
- `contract.breached` - контракт нарушен (parameters: contractId, breachReason, penalties)
- `contract.cancelled` - контракт отменен (parameters: contractId, reason, actorId)

**События нарушений:**
- `contract.obligation_breached` - нарушено обязательство по контракту (parameters: contractId, obligation, breachDetails)
- `contract.payment_failed` - платеж по контракту не выполнен (parameters: contractId, payment, reason)

### События лицензий

**События жизненного цикла:**
- `license.issued` - лицензия выдана (parameters: licenseId, ownerId, type, tier)
- `license.activated` - лицензия активирована (parameters: licenseId)
- `license.suspended` - лицензия приостановлена (parameters: licenseId, reason)
- `license.revoked` - лицензия отозвана (parameters: licenseId, reason)
- `license.expired` - лицензия истекла (parameters: licenseId, expiredAt)

### События KPI

**События достижений:**
- `kpi.threshold_reached` - достигнут порог KPI (parameters: scenarioId, kpiType, threshold, value)
- `kpi.milestone_achieved` - достигнут milestone (parameters: scenarioId, milestoneId, rewards)
- `kpi.target_failed` - не достигнута цель KPI (parameters: scenarioId, targetId, reason)

### Обработка событий

**Подписки на события:**
- Владелец может подписаться на события своих NPC/сценариев/контрактов
- Система уведомлений: email, in-game, webhook (для интеграций)
- Фильтрация событий: по типу, приоритету, важности

**Реакции на события:**
- Автоматические реакции: триггеры для сценариев, уведомления, обновление статусов
- Пользовательские реакции: кастомные обработчики событий (для продвинутых сценариев)

## Связанные документы

- `03-lore/characters/characters-overview.md`
- `02-gameplay/economy/*`, `02-gameplay/social/*`, `02-gameplay/combat/*`

## TODO для дальнейшей проработки

- [ ] Детализация конкретных сценариев (economy, social, combat, logistics) - примеры ScenarioBlueprint
- [ ] Детализация формул экономики содержания (балансировка затрат по типам NPC)
- [ ] Детализация системы автоматизации (правила/триггеры для сценариев)
- [ ] Балансировка лимитов по лицензиям (L1-L3)
- [ ] Детализация интеграций с economy/social/combat (конкретные API endpoints)

---


## История изменений

- v0.1.0 (2025-11-03) — создан каркас инструмента
- v1.0.0 (2025-11-03) — формализованы модели данных, события и матрица прав
  - ✅ Формализованы модели данных (Owner, PersonalNPC, RoleSet, ScenarioBlueprint, ScenarioInstance, Contract, License, Certificate, AuditLog, Ledger)
  - ✅ Детализирована матрица прав и лимитов (иерархия прав, лимиты по лицензиям, контракты переноса)
  - ✅ Формализованы события и сигналы (NPC, сценарии, контракты, лицензии, KPI)
  - ✅ Детализирована экономика содержания (модели затрат для human/robot/android, гибридные схемы)
