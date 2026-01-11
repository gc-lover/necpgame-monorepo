# AI Combat Calculator Service

## Обзор

**AI Combat Calculator Service** - высокопроизводительный сервис математически точных расчетов боевых взаимодействий в NECPGAME MMOFPS RPG.

## Назначение домена

Обеспечивает точные расчеты урона, смягчения, критических ударов, элементальных взаимодействий, статус эффектов и исцеления для всех боевых взаимодействий ИИ врагов с математической точностью 99.999%.

## Целевые показатели производительности

- **P99 Latency**: <5ms для расчетов урона
- **P99 Latency**: <10ms для полного разрешения боя
- **Память**: <25MB на зону
- **Точность расчетов**: 99.999% точность
- **Одновременные расчеты**: 10000+ в секунду поддерживается

## Архитектура

### SOLID/DRY Наследование домена

Сервис наследует из `game-entities.yaml`, предоставляя:
- Консистентную базовую структуру сущностей (id, timestamps, version) - **НЕ ДУБЛИРУЕМ!**
- Игровые доменные сущности через паттерн `allOf`
- Оптимистическую блокировку для конкурентных операций (поле version)
- Строгую типизацию с правилами валидации (min/max, patterns, enums)

### Ключевые компоненты

1. **Damage Calculation** - расчет урона и смягчения
2. **Healing Calculation** - расчет исцеления и усиления
3. **Critical Hit Systems** - вероятность и множители критических ударов
4. **Elemental Interactions** - элементальные взаимодействия урона/сопротивления
5. **Status Effects** - применение и расчет статус эффектов
6. **Combat Resolution** - полное разрешение боевых взаимодействий

## Оптимизации производительности

### Векторизованные расчеты

```go
// SIMD векторизованные расчеты урона
func (cc *CombatCalculator) CalculateDamageBatch(requests []*DamageRequest) []*DamageResult {
    // Пакетная обработка с SIMD инструкциями
    results := make([]*DamageResult, len(requests))

    for i := 0; i < len(requests); i += 4 {
        // Обработка 4 запросов одновременно
        baseDamages := cc.loadBaseDamages(requests[i:i+4])
        mitigations := cc.calculateMitigationsSIMD(requests[i:i+4])
        modifiers := cc.applyModifiersSIMD(requests[i:i+4])

        finalDamages := cc.finalizeDamagesSIMD(baseDamages, mitigations, modifiers)
        cc.storeResults(results[i:i+4], finalDamages)
    }

    return results
}
```

### Предварительно рассчитанные таблицы смягчения

```go
// Кешированные таблицы смягчения брони
type MitigationTable struct {
    mu     sync.RWMutex
    tables map[string]*ArmorMitigationTable
}

func (mt *MitigationTable) GetMitigation(damageType string, armorRating int32) float64 {
    mt.mu.RLock()
    defer mt.mu.RUnlock()

    if table, exists := mt.tables[damageType]; exists {
        return table.Lookup(armorRating)
    }
    return mt.calculateAndCache(damageType, armorRating)
}
```

### Memory Pooling для контекстов расчетов

```go
// Pool для контекстов боевых расчетов
var combatContextPool = sync.Pool{
    New: func() interface{} {
        return &CombatContext{
            Attacker:     &CombatParticipant{},
            Defender:     &CombatParticipant{},
            DamageSource: &DamageSource{},
            Modifiers:    make([]*DamageModifier, 0, 8),
            Result:       &CombatResult{},
        }
    },
}
```

## Типы урона и взаимодействий

### Основные типы урона

#### Physical Damage
- **Armor Penetration**: Проникновение сквозь броню
- **Critical Multipliers**: Множители критических ударов
- **Falloff Calculations**: Расчет падения урона с расстоянием

#### Energy Damage
- **Shield Interactions**: Взаимодействие с энергетическими щитами
- **Overcharge Effects**: Эффекты перегрузки
- **Feedback Damage**: Обратный урон

#### Elemental Damage
- **Type Interactions**: Взаимодействия типов элементов
- **Resistance Calculations**: Расчет сопротивлений
- **Status Effect Triggers**: Триггеры статус эффектов

### Элементальные взаимодействия

```go
// Матрица элементальных взаимодействий
var elementalMatrix = map[string]map[string]float64{
    "fire": {
        "ice":   1.5,  // Fire melts ice
        "water": 0.5,  // Water extinguishes fire
        "earth": 1.0,  // Neutral
    },
    "ice": {
        "fire":  0.5,  // Fire melts ice
        "water": 1.2,  // Ice in water
        "earth": 0.8,  // Ice on earth
    },
    // ... дополнительные взаимодействия
}
```

## Расчеты критических ударов

### Динамическая вероятность крита

```go
func (cc *CombatCalculator) CalculateCritChance(attacker, defender *CombatStats, situationalMods []string) float64 {
    baseChance := attacker.CriticalChance

    // Ситуативные модификаторы
    for _, mod := range situationalMods {
        switch mod {
        case "backstab":
            baseChance *= 2.0
        case "weak_spot":
            baseChance *= 1.5
        case "low_health":
            baseChance *= 1.3
        }
    }

    // Модификаторы защиты
    defenseMod := defender.CriticalDefense
    finalChance := math.Min(baseChance * (1 - defenseMod), 1.0)

    return finalChance
}
```

### Множители критического урона

- **Weapon Types**: Разные типы оружия имеют разные базовые множители
- **Ability Effects**: Способности могут модифицировать критический урон
- **Status Effects**: Статус эффекты влияют на критический урон

## Статус эффекты

### Типы статус эффектов

#### Damage Over Time (DoT)
- **Bleeding**: Кровотечение с постепенным уроном
- **Burning**: Горение с уроном огнем
- **Poison**: Отравление с уроном ядом

#### Crowd Control
- **Stun**: Оглушение, предотвращает действия
- **Freeze**: Заморозка, снижает скорость
- **Slow**: Замедление движения и атаки

#### Buff/Debuff
- **Weaken**: Снижение силы атаки
- **Amplify**: Увеличение урона/исцеления
- **Shield**: Защита от урона

### Механика стекинга

```go
type StatusEffectStack struct {
    EffectType    string
    MaxStacks     int
    CurrentStacks int
    DurationMs    int64
    RefreshOnApply bool
}

func (ses *StatusEffectStack) ApplyStack() bool {
    if ses.CurrentStacks < ses.MaxStacks {
        ses.CurrentStacks++
        return true
    }

    if ses.RefreshOnApply {
        ses.DurationMs = ses.MaxDurationMs
        return true
    }

    return false
}
```

## API Endpoints

### Основные расчеты

- `POST /damage/calculate` - расчет урона для взаимодействия
- `POST /healing/calculate` - расчет исцеления
- `POST /damage/critical` - расчет критических ударов
- `POST /combat/resolve` - разрешение боя

### Элементальные взаимодействия

- `POST /elemental/interaction` - расчет элементальных взаимодействий

### Статус эффекты

- `POST /status-effects/apply` - применение статус эффекта
- `POST /status-effects/modify` - модификация статус эффекта

### Пакетная обработка

- `POST /damage/batch` - пакетный расчет урона
- `POST /combat/batch-resolve` - пакетное разрешение боевых взаимодействий

### Мониторинг производительности

- `GET /metrics/combat-performance` - метрики производительности боевых расчетов

## Мониторинг и Observability

### Метрики производительности

- **Calculation Latency**: Задержка расчетов (P50, P95, P99)
- **Throughput**: Количество расчетов в секунду
- **Accuracy**: Точность расчетов и ошибки
- **Memory Usage**: Использование памяти

### Critical Alerts

- Latency >5ms P99 для расчетов урона
- Latency >10ms P99 для разрешения боя
- Точность расчетов <99.999%
- Ошибки расчетов >0.001%

## Безопасность

### API Security
- JWT authentication для всех сервисов
- Rate limiting по игроку/сервису
- Input validation и sanitization
- OWASP Top 10 compliance

### Data Security
- Зашифрованные подключения к БД
- PII data protection
- Audit logging для sensitive operations
- Secure key management

## Развертывание

### Kubernetes Manifests
- ai-combat-calculator-deployment.yaml
- ai-combat-calculator-service.yaml
- ai-combat-calculator-configmap.yaml

### CI/CD Pipeline
1. **Build**: Go compilation с оптимизациями
2. **Test**: Unit, integration, performance tests
3. **Security**: Vulnerability scanning, secrets check
4. **Deploy**: Blue-green deployment с rollback

## Связанные компоненты

- **AI Enemy Coordinator Service** - централизованная оркестрация ИИ
- **AI Behavior Engine Service** - движок поведений ИИ
- **AI Position Sync Service** - синхронизация движения
- **Combat Service** - основная боевая логика
- **Gameplay Service** - игровая механика

## Issue
#2300 - [API] Design OpenAPI specifications for AI Enemy Services