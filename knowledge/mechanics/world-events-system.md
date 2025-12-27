# [Mechanics] World Events система

## Issue: #140875793
**Тип:** Mechanics
**Статус:** In Progress
**Ответственный:** Backend Agent

## Обзор системы

World Events система предоставляет динамические глобальные события, которые затрагивают весь игровой мир. Эти события создают уникальный контент, влияют на экономику, изменяют поведение NPC и предоставляют игрокам новые возможности для взаимодействия.

## Архитектурные компоненты

### 1. Event Manager (Основной менеджер событий)
```go
type EventManager struct {
    activeEvents    map[string]*WorldEvent
    eventTemplates  map[string]*EventTemplate
    eventScheduler  *EventScheduler
    eventProcessor  *EventProcessor
    metrics         *EventMetrics
    logger          *zap.Logger
}

func (em *EventManager) StartEvent(templateID string, region string) error {
    template, exists := em.eventTemplates[templateID]
    if !exists {
        return fmt.Errorf("event template not found: %s", templateID)
    }

    event := &WorldEvent{
        ID:          uuid.New(),
        TemplateID:  templateID,
        Region:      region,
        Status:      EventStatusActive,
        StartTime:   time.Now(),
        EndTime:     time.Now().Add(template.Duration),
        Participants: make(map[string]*EventParticipant),
        State:       template.InitialState,
    }

    em.activeEvents[event.ID.String()] = event
    em.notifyEventStart(event)

    return nil
}
```

### 2. Event Templates (Шаблоны событий)
```yaml
# Пример шаблона события "Внезапная буря"
storm_template:
  id: "storm_sudden"
  name: "Внезапная буря"
  description: "Мощная буря обрушивается на регион"
  duration: "2h"
  trigger_conditions:
    - weather_clear: true
    - time_of_day: "day"
    - player_density: ">10"

  effects:
    - type: "weather_change"
      params:
        weather_type: "storm"
        intensity: 0.8

    - type: "npc_behavior"
      params:
        behavior: "shelter"
        affected_npcs: "all"

    - type: "resource_modifier"
      params:
        resource: "travel_speed"
        modifier: -0.5
        duration: "30m"

  rewards:
    - type: "experience"
      amount: 500
      condition: "survived_storm"

    - type: "item"
      item_id: "storm_survivor_badge"
      chance: 0.1
```

### 3. Event Processor (Обработчик событий)
```go
type EventProcessor struct {
    eventQueue      chan *EventUpdate
    workerPool      *WorkerPool
    stateManager    *EventStateManager
    effectExecutor  *EffectExecutor
}

func (ep *EventProcessor) ProcessEventUpdate(update *EventUpdate) {
    switch update.Type {
    case EventUpdateTypeStart:
        ep.handleEventStart(update.EventID)

    case EventUpdateTypeProgress:
        ep.handleEventProgress(update.EventID, update.Progress)

    case EventUpdateTypeEffect:
        ep.handleEventEffect(update.EventID, update.Effect)

    case EventUpdateTypeEnd:
        ep.handleEventEnd(update.EventID)
    }
}

func (ep *EventProcessor) handleEventEffect(eventID string, effect *EventEffect) {
    // Применение эффекта к игровому миру
    switch effect.Type {
    case EffectTypeWeather:
        ep.applyWeatherEffect(effect.Params)

    case EffectTypeNPCBehavior:
        ep.applyNPCBehaviorEffect(effect.Params)

    case EffectTypeResourceModifier:
        ep.applyResourceModifierEffect(effect.Params)

    case EffectTypeSpawnEntity:
        ep.applySpawnEntityEffect(effect.Params)
    }
}
```

## Типы событий

### 1. Погодные события (Weather Events)
- **Внезапная буря** - изменяет погоду, влияет на передвижение
- **Солнечное затмение** - влияет на освещение, создает атмосферу
- **Магнитная буря** - влияет на электронику и коммуникации

### 2. Экономические события (Economic Events)
- **Рыночный бум** - повышение цен на определенные товары
- **Торговый караван** - временная возможность торговли редкими товарами
- **Экономический кризис** - падение цен, возможность для спекуляций

### 3. Социальные события (Social Events)
- **Фестиваль** - массовые собрания, мини-игры, награды
- **Протесты** - изменение поведения NPC, влияние на квесты
- **Церемония** - ритуальные события с участием игроков

### 4. Боевые события (Combat Events)
- **Вторжение монстров** - спавн агрессивных существ
- **Бандитский набег** - временное увеличение преступности
- **Военный конфликт** - зоны боевых действий

### 5. Мистические события (Mystical Events)
- **Портал в другой мир** - временные зоны с уникальными правилами
- **Духовное пробуждение** - влияние на способности игроков
- **Пророчество** - цепочка квестов, ведущая к крупному событию

## Система эффектов

### Эффекты на игровой мир
```go
type WorldEffect struct {
    Type       EffectType
    TargetType TargetType // global, regional, local
    TargetID   string
    Params     map[string]interface{}
    Duration   time.Duration
    Intensity  float64
}

func (we *WorldEffect) Apply(world *GameWorld) {
    switch we.TargetType {
    case TargetTypeGlobal:
        we.applyGlobalEffect(world)

    case TargetTypeRegional:
        we.applyRegionalEffect(world, we.TargetID)

    case TargetTypeLocal:
        we.applyLocalEffect(world, we.TargetID)
    }
}
```

### Эффекты на игроков
```go
type PlayerEffect struct {
    PlayerID  string
    EffectType string
    Value      interface{}
    Duration   time.Duration
    Source     string // ID события
}

func (pe *PlayerEffect) ApplyToPlayer(player *Player) {
    switch pe.EffectType {
    case "stat_modifier":
        pe.applyStatModifier(player)

    case "ability_grant":
        pe.applyAbilityGrant(player)

    case "resource_boost":
        pe.applyResourceBoost(player)

    case "movement_speed":
        pe.applyMovementSpeed(player)
    }
}
```

## Система участия (Participation System)

### Уровни участия
```go
type ParticipationLevel struct {
    Level       ParticipationType
    Threshold   int // очки участия
    Rewards     []Reward
    Bonuses     []Bonus
}

const (
    ParticipationObserver ParticipationType = iota
    ParticipationMinor
    ParticipationMajor
    ParticipationHero
    ParticipationLegend
)
```

### Система очков участия
```go
type ParticipationTracker struct {
    playerScores map[string]int
    eventID      string
    activities   map[string]*ActivityType
}

func (pt *ParticipationTracker) RecordActivity(playerID string, activity ActivityType) {
    score := pt.activities[activity].Score

    if _, exists := pt.playerScores[playerID]; !exists {
        pt.playerScores[playerID] = 0
    }

    pt.playerScores[playerID] += score
    pt.checkLevelUp(playerID)
}
```

## Система уведомлений

### Типы уведомлений
```go
type NotificationType struct {
    Type        NotificationCategory
    Priority    NotificationPriority
    Template    string
    Channels    []NotificationChannel
}

const (
    NotificationInfo NotificationPriority = iota
    NotificationWarning
    NotificationCritical
    NotificationEmergency
)
```

### Рассылка уведомлений
```go
func (em *EventManager) notifyEventStart(event *WorldEvent) {
    notification := &Notification{
        Type:    NotificationEventStart,
        Title:   "Новое мировое событие!",
        Message: fmt.Sprintf("Событие '%s' началось в регионе %s",
            event.Template.Name, event.Region),
        Data: map[string]interface{}{
            "event_id": event.ID,
            "region":   event.Region,
            "type":     event.Template.Type,
        },
    }

    // Отправка через все каналы
    em.notificationService.SendToRegion(event.Region, notification)
    em.notificationService.SendToParticipants(event.ID.String(), notification)
    em.notificationService.SendGlobal(notification)
}
```

## Система аналитики и метрик

### Метрики событий
```yaml
event_metrics:
  - name: active_events_total
    type: gauge
    help: "Total number of currently active world events"

  - name: event_participants_total
    type: histogram
    buckets: [0, 10, 50, 100, 500, 1000]
    help: "Number of participants per event"

  - name: event_duration_seconds
    type: histogram
    buckets: [300, 1800, 3600, 7200, 21600]
    help: "Duration of events in seconds"

  - name: event_completion_rate
    type: gauge
    help: "Rate of successful event completions"
```

### Аналитические отчеты
```go
type EventAnalytics struct {
    eventID       string
    startTime     time.Time
    endTime       time.Time
    participants  int
    engagement    float64
    completionRate float64
    revenueImpact float64
    playerRetention float64
}

func (ea *EventAnalytics) GenerateReport() *EventReport {
    return &EventReport{
        EventID:         ea.eventID,
        Duration:        ea.endTime.Sub(ea.startTime),
        TotalParticipants: ea.participants,
        AvgEngagementTime: ea.calculateAvgEngagement(),
        CompletionRate:   ea.completionRate,
        RevenueGenerated: ea.revenueImpact,
        RetentionImpact:  ea.playerRetention,
        Recommendations:  ea.generateRecommendations(),
    }
}
```

## Система баланса и настройки

### Параметры баланса
```yaml
balance_config:
  max_concurrent_events: 5
  event_cooldown_minutes: 60
  max_events_per_region: 2
  min_event_duration: "15m"
  max_event_duration: "8h"
  participation_decay_rate: 0.1  # очки участия уменьшаются со временем
```

### Система весов для выбора событий
```go
type EventWeightCalculator struct {
    baseWeights     map[string]float64
    regionFactors   map[string]float64
    timeFactors     map[string]float64
    playerFactors   map[string]float64
}

func (ewc *EventWeightCalculator) CalculateWeight(templateID string, context *EventContext) float64 {
    baseWeight := ewc.baseWeights[templateID]

    regionMultiplier := ewc.getRegionMultiplier(context.Region)
    timeMultiplier := ewc.getTimeMultiplier(context.CurrentTime)
    playerMultiplier := ewc.getPlayerMultiplier(context.PlayerCount)

    return baseWeight * regionMultiplier * timeMultiplier * playerMultiplier
}
```

## Интеграция с другими системами

### Интеграция с квестовой системой
```go
func (em *EventManager) generateEventQuests(event *WorldEvent) []*Quest {
    quests := []*Quest{}

    // Основной квест события
    mainQuest := &Quest{
        ID:          uuid.New(),
        Title:       fmt.Sprintf("Участие в событии: %s", event.Template.Name),
        Description: event.Template.Description,
        Objectives:  em.generateQuestObjectives(event),
        Rewards:     em.generateQuestRewards(event),
        EventID:     event.ID.String(),
    }

    quests = append(quests, mainQuest)

    // Дополнительные квесты
    for _, subEvent := range event.Template.SubEvents {
        subQuest := em.generateSubQuest(event, subEvent)
        quests = append(quests, subQuest)
    }

    return quests
}
```

### Интеграция с экономической системой
```go
func (em *EventManager) applyEconomicImpact(event *WorldEvent) {
    for _, effect := range event.Template.EconomicEffects {
        switch effect.Type {
        case EconomicEffectPriceChange:
            em.economyService.ModifyPrices(effect.Params)

        case EconomicEffectResourceSpawn:
            em.economyService.SpawnResources(effect.Params)

        case EconomicEffectMarketEvent:
            em.economyService.TriggerMarketEvent(effect.Params)
        }
    }
}
```

## Тестирование и валидация

### Unit тесты
```go
func TestEventManager_StartEvent(t *testing.T) {
    em := setupTestEventManager()
    templateID := "test_event"
    region := "test_region"

    err := em.StartEvent(templateID, region)
    assert.NoError(t, err)

    // Проверка что событие создано
    events := em.GetActiveEvents()
    assert.Len(t, events, 1)

    event := events[0]
    assert.Equal(t, templateID, event.TemplateID)
    assert.Equal(t, region, event.Region)
    assert.Equal(t, EventStatusActive, event.Status)
}
```

### Интеграционные тесты
```go
func TestEventFullLifecycle(t *testing.T) {
    // Настройка полной среды тестирования
    world := setupTestWorld()
    em := setupTestEventManager(world)

    // Запуск события
    eventID := em.StartEvent("festival_template", "central_region")

    // Симуляция участия игроков
    for i := 0; i < 100; i++ {
        playerID := fmt.Sprintf("player_%d", i)
        em.JoinEvent(eventID, playerID)
    }

    // Прогресс события
    em.AdvanceEvent(eventID, 0.5) // 50% прогресса

    // Проверка эффектов
    effects := world.GetActiveEffects()
    assert.True(t, len(effects) > 0)

    // Завершение события
    em.EndEvent(eventID)

    // Проверка наград
    rewards := em.GetEventRewards(eventID)
    assert.True(t, len(rewards) > 0)
}
```

## Мониторинг и поддержка

### Health checks
```go
func (em *EventManager) HealthCheck() *HealthStatus {
    return &HealthStatus{
        ActiveEvents:    len(em.activeEvents),
        QueueSize:       len(em.eventProcessor.eventQueue),
        LastEventTime:   em.getLastEventTime(),
        ErrorRate:       em.metrics.GetErrorRate(),
        MemoryUsage:     em.getMemoryUsage(),
        Status:          em.determineHealthStatus(),
    }
}
```

### Логирование
```go
func (em *EventManager) logEventLifecycle(event *WorldEvent, action string) {
    em.logger.Info("Event lifecycle",
        zap.String("event_id", event.ID.String()),
        zap.String("template_id", event.TemplateID),
        zap.String("region", event.Region),
        zap.String("action", action),
        zap.Int("participants", len(event.Participants)),
        zap.Duration("duration", event.EndTime.Sub(event.StartTime)),
    )
}
```

## Заключение

World Events система предоставляет мощный инструмент для создания динамического и увлекательного игрового мира. Правильная архитектура и баланс обеспечивают интересный геймплей, сохраняя производительность и стабильность системы.

---

**Статус:** Implementation ready
**Следующие шаги:**
1. Реализовать базовую инфраструктуру EventManager
2. Создать шаблоны для основных типов событий
3. Интегрировать с существующими системами
4. Настроить мониторинг и метрики
