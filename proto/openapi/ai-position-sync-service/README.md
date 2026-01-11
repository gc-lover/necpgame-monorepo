# AI Position Sync Service

## Обзор

**AI Position Sync Service** - высокопроизводительный сервис синхронизации движения в реальном времени для сущностей ИИ в NECPGAME MMOFPS RPG.

## Назначение домена

Обеспечивает субмиллисекундную синхронизацию позиций, предсказание движения, обнаружение столкновений и broadcasting состояния в реальном времени для до 500+ сущностей ИИ на зону с задержкой P99 <50ms.

## Целевые показатели производительности

- **P99 Latency**: <25ms для обновлений позиций
- **P99 Latency**: <10ms для запросов позиций
- **Память**: <30MB на зону
- **Точность позиций**: <0.1 unit отклонения
- **Одновременные сущности**: 500+ на зону поддерживается
- **Частота обновлений**: 20+ Hz поддерживается

## Архитектура

### SOLID/DRY Наследование домена

Сервис наследует из `game-entities.yaml`, предоставляя:
- Консистентную базовую структуру сущностей (id, timestamps, version) - **НЕ ДУБЛИРУЕМ!**
- Игровые доменные сущности через паттерн `allOf`
- Оптимистическую блокировку для конкурентных операций (поле version)
- Строгую типизацию с правилами валидации (min/max, patterns, enums)

### Ключевые компоненты

1. **Position Synchronization** - синхронизация позиций и broadcasting состояния
2. **Movement Prediction** - предсказание движения и pathfinding
3. **Collision Detection** - обнаружение и разрешение столкновений
4. **Zone Management** - зональное управление позициями
5. **Real-time Broadcasting** - broadcasting в реальном времени

## Оптимизации производительности

### Пространственное индексирование

```go
// Quad-tree для быстрого поиска ближайших сущностей
type SpatialIndex struct {
    bounds    AABB
    entities  []*Entity
    quadrants [4]*SpatialIndex
    maxEntities int
}

func (si *SpatialIndex) QueryNearby(position Vector3, radius float64) []*Entity {
    // O(log n) запросы ближайших сущностей
    nearby := make([]*Entity, 0, 16)
    si.queryNearbyRecursive(position, radius, &nearby)
    return nearby
}
```

### Прогнозирование движения с кешированием

```go
// Кеширование предсказаний движения
type MovementPredictor struct {
    cache   map[string]*PredictionCache
    ttl     time.Duration
    maxSize int
}

func (mp *MovementPredictor) Predict(entityID string, context *PredictionContext) *Prediction {
    key := mp.generateKey(entityID, context)

    if cached, exists := mp.cache[key]; exists && !mp.isExpired(cached) {
        return cached.Prediction
    }

    prediction := mp.calculatePrediction(entityID, context)
    mp.cache[key] = &PredictionCache{
        Prediction: prediction,
        Timestamp:  time.Now(),
    }

    return prediction
}
```

### SIMD векторизованные обновления позиций

```go
// Векторизованное обновление позиций для нескольких сущностей
func (pss *PositionSyncService) UpdateBatchPositions(updates []*PositionUpdate) []*UpdateResult {
    results := make([]*UpdateResult, len(updates))

    // Обработка по 8 сущностей одновременно с AVX инструкциями
    for i := 0; i < len(updates); i += 8 {
        end := i + 8
        if end > len(updates) {
            end = len(updates)
        }

        batch := updates[i:end]
        positions := pss.extractPositions(batch)
        velocities := pss.extractVelocities(batch)

        // SIMD: одновременное обновление всех позиций
        newPositions := pss.updatePositionsSIMD(positions, velocities, deltaTime)

        // Пакетная валидация столкновений
        collisions := pss.detectCollisionsBatch(newPositions)

        // Разрешение столкновений
        resolved := pss.resolveCollisionsSIMD(newPositions, collisions)

        pss.storeResults(results[i:end], resolved)
    }

    return results
}
```

### Memory Pooling для позиционных обновлений

```go
// Pool для контекстов обновлений позиций
var positionUpdatePool = sync.Pool{
    New: func() interface{} {
        return &PositionUpdateContext{
            EntityID:  "",
            Position:  &Vector3{},
            Velocity:  &Vector3{},
            Timestamp: time.Now(),
            ZoneID:    "",
        }
    },
}
```

## API Endpoints

### Синхронизация позиций

- `GET /positions/{entity_id}` - получение позиции сущности ИИ
- `PUT /positions/{entity_id}` - обновление позиции сущности ИИ
- `GET /positions/batch` - получение позиций нескольких сущностей
- `POST /positions/batch` - обновление позиций нескольких сущностей

### Предсказание движения

- `POST /movement/{entity_id}/predict` - предсказание движения сущности ИИ
- `POST /movement/pathfind` - расчет оптимального пути

### Обнаружение столкновений

- `POST /collision/detect` - обнаружение столкновений
- `POST /collision/resolve` - разрешение столкновений

### Управление зонами

- `GET /zones/{zone_id}/positions` - получение всех позиций ИИ в зоне
- `POST /zones/{zone_id}/sync` - синхронизация состояния зоны

### Broadcasting в реальном времени

- `POST /broadcast/positions` - broadcasting обновлений позиций
- `POST /broadcast/subscribe/{zone_id}` - подписка на broadcasts зоны

### Мониторинг производительности

- `GET /metrics/position-sync` - метрики производительности синхронизации позиций

## Пространственные алгоритмы

### Quad-tree индексация

```go
type QuadTree struct {
    boundary AABB
    capacity int
    entities []*SpatialEntity

    // Поддеревья для 4 квадрантов
    northeast *QuadTree
    northwest *QuadTree
    southeast *QuadTree
    southwest *QuadTree
}

func (qt *QuadTree) Insert(entity *SpatialEntity) bool {
    if !qt.boundary.Contains(entity.Position) {
        return false
    }

    if len(qt.entities) < qt.capacity && qt.northeast == nil {
        qt.entities = append(qt.entities, entity)
        return true
    }

    // Разделение и вставка в поддерево
    if qt.northeast == nil {
        qt.subdivide()
    }

    return qt.northeast.Insert(entity) ||
           qt.northwest.Insert(entity) ||
           qt.southeast.Insert(entity) ||
           qt.southwest.Insert(entity)
}
```

### Предсказание столкновений

```go
func (cd *CollisionDetector) PredictCollision(entityA, entityB *MovingEntity, timeHorizon float64) *CollisionPrediction {
    // Расчет относительной скорости
    relativeVelocity := entityB.Velocity.Subtract(entityA.Velocity)

    // Расчет времени до столкновения
    distance := entityB.Position.Distance(entityA.Position)
    combinedRadius := entityA.Radius + entityB.Radius

    if relativeVelocity.Length() < 0.001 {
        // Статические сущности - проверка расстояния
        return &CollisionPrediction{
            WillCollide: distance <= combinedRadius,
            TimeToCollision: 0,
        }
    }

    // Расчет времени до столкновения
    timeToCollision := cd.solveQuadratic(relativeVelocity, distance, combinedRadius)

    return &CollisionPrediction{
        WillCollide: timeToCollision >= 0 && timeToCollision <= timeHorizon,
        TimeToCollision: math.Max(0, timeToCollision),
        CollisionPoint: cd.calculateCollisionPoint(entityA, entityB, timeToCollision),
    }
}
```

## Broadcasting в реальном времени

### WebSocket broadcasting с compression

```go
type PositionBroadcaster struct {
    subscribers map[string][]*Subscriber
    compressor  *zlib.Compressor
    metrics     *BroadcastMetrics
}

func (pb *PositionBroadcaster) BroadcastZoneUpdate(zoneID string, updates []*PositionUpdate) error {
    subscribers := pb.subscribers[zoneID]
    if len(subscribers) == 0 {
        return nil
    }

    // Сериализация обновлений
    data, err := json.Marshal(updates)
    if err != nil {
        return err
    }

    // Компрессия для больших обновлений
    if len(data) > 1024 {
        data, err = pb.compressor.Compress(data)
        if err != nil {
            return err
        }
    }

    // Broadcasting всем подписчикам зоны
    broadcastStart := time.Now()
    successCount := 0

    for _, subscriber := range subscribers {
        if pb.sendToSubscriber(subscriber, data) {
            successCount++
        }
    }

    // Метрики производительности
    pb.metrics.RecordBroadcast(time.Since(broadcastStart), len(updates), successCount)

    return nil
}
```

## Мониторинг и Observability

### Метрики производительности

- **Sync Latency**: Задержка синхронизации (P50, P95, P99)
- **Throughput**: Обновления позиций в секунду
- **Prediction Accuracy**: Точность предсказаний движения
- **Collision Detection**: Эффективность обнаружения столкновений

### Critical Alerts

- Latency >25ms P99 для обновлений позиций
- Latency >10ms P99 для запросов позиций
- Точность позиций >0.1 unit
- Сбои broadcasting >0.1%

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
- ai-position-sync-deployment.yaml
- ai-position-sync-service.yaml
- ai-position-sync-configmap.yaml

### CI/CD Pipeline
1. **Build**: Go compilation с оптимизациями
2. **Test**: Unit, integration, performance tests
3. **Security**: Vulnerability scanning, secrets check
4. **Deploy**: Blue-green deployment с rollback

## Связанные компоненты

- **AI Enemy Coordinator Service** - централизованная оркестрация ИИ
- **AI Behavior Engine Service** - движок поведений ИИ
- **AI Combat Calculator Service** - расчеты урона/исцеления
- **Zone Service** - управление зонами мира
- **Real-time Gateway Service** - WebSocket broadcasting

## Issue
#2300 - [API] Design OpenAPI specifications for AI Enemy Services