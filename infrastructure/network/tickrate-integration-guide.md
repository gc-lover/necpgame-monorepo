# Руководство по интеграции тикрейта в существующий код

**Issue:** #142109960  
**Агент:** Network Engineer  
**Версия:** 1.0.0

## Обзор

Данное руководство описывает, как интегрировать конфигурацию тикрейта в существующий код realtime-gateway-go и другие сетевые сервисы.

## Текущее состояние

### realtime-gateway-go

В текущей реализации тикрейт жестко задан:

```go
// services/realtime-gateway-go/main.go
tickRate := 60
handler := server.NewGatewayHandler(tickRate, sessionMgr)
```

### Требования к интеграции

1. Загрузка конфигурации тикрейта из YAML файлов
2. Поддержка разных типов зон
3. Адаптивный тикрейт
4. Интеграция с Zone Manager

## Этапы интеграции

### Этап 1: Создание конфигурационного загрузчика

Создать пакет для загрузки конфигурации тикрейта:

```go
// services/realtime-gateway-go/server/tickrate/config.go
package tickrate

import (
    "gopkg.in/yaml.v3"
    "os"
)

type ZoneConfig struct {
    ZoneType        string   `yaml:"zone_type"`
    Protocol        string   `yaml:"protocol"`
    Tickrate        TickrateConfig `yaml:"tickrate"`
    Latency         LatencyConfig  `yaml:"latency"`
    Players         PlayersConfig  `yaml:"players"`
    Network         NetworkConfig  `yaml:"network"`
}

type TickrateConfig struct {
    Base int `yaml:"base"`
    Min  int `yaml:"min"`
    Max  int `yaml:"max"`
    Adaptive bool `yaml:"adaptive"`
}

type TickrateConfigs struct {
    Version string                `yaml:"version"`
    Zones   map[string]ZoneConfig `yaml:"zones"`
}

func LoadTickrateConfig(path string) (*TickrateConfigs, error) {
    data, err := os.ReadFile(path)
    if err != nil {
        return nil, err
    }
    
    var config TickrateConfigs
    if err := yaml.Unmarshal(data, &config); err != nil {
        return nil, err
    }
    
    return &config, nil
}
```

### Этап 2: Создание Tickrate Manager

Создать менеджер для управления тикрейтом:

```go
// services/realtime-gateway-go/server/tickrate/manager.go
package tickrate

import (
    "context"
    "sync"
    "time"
)

type TickrateManager struct {
    config        *TickrateConfigs
    currentZone   string
    currentTickrate int
    adaptiveEnabled bool
    mu            sync.RWMutex
}

func NewTickrateManager(configPath string) (*TickrateManager, error) {
    config, err := LoadTickrateConfig(configPath)
    if err != nil {
        return nil, err
    }
    
    return &TickrateManager{
        config: config,
        adaptiveEnabled: true,
    }, nil
}

func (tm *TickrateManager) GetTickrateForZone(zoneType string) int {
    tm.mu.RLock()
    defer tm.mu.RUnlock()
    
    zoneConfig, ok := tm.config.Zones[zoneType]
    if !ok {
        return 30
    }
    
    return zoneConfig.Tickrate.Base
}

func (tm *TickrateManager) SetZone(zoneType string) {
    tm.mu.Lock()
    defer tm.mu.Unlock()
    
    tm.currentZone = zoneType
    tm.currentTickrate = tm.GetTickrateForZone(zoneType)
}

func (tm *TickrateManager) GetCurrentTickrate() int {
    tm.mu.RLock()
    defer tm.mu.RUnlock()
    return tm.currentTickrate
}
```

### Этап 3: Интеграция с GatewayHandler

Обновить GatewayHandler для использования TickrateManager:

```go
// services/realtime-gateway-go/server/handler.go
import (
    "github.com/necpgame/realtime-gateway-go/server/tickrate"
)

type GatewayHandler struct {
    tickrateMgr     *tickrate.TickrateManager
    tickRate         int
    gameStateMgr     *GameStateManager
    // ... остальные поля
}

func NewGatewayHandler(tickrateMgr *tickrate.TickrateManager, sessionMgr SessionManagerInterface) *GatewayHandler {
    currentTickrate := tickrateMgr.GetCurrentTickrate()
    
    handler := &GatewayHandler{
        tickrateMgr:    tickrateMgr,
        tickRate:       currentTickrate,
        gameStateMgr:   NewGameStateManager(currentTickrate),
        sessionMgr:     sessionMgr,
        // ... остальная инициализация
    }
    return handler
}

func (h *GatewayHandler) UpdateTickrateForZone(zoneType string) {
    h.tickrateMgr.SetZone(zoneType)
    newTickrate := h.tickrateMgr.GetCurrentTickrate()
    
    h.mu.Lock()
    h.tickRate = newTickrate
    h.gameStateMgr = NewGameStateManager(newTickrate)
    h.mu.Unlock()
}
```

### Этап 4: Обновление main.go

Обновить main.go для загрузки конфигурации:

```go
// services/realtime-gateway-go/main.go
import (
    "github.com/necpgame/realtime-gateway-go/server/tickrate"
)

func main() {
    // ... существующий код
    
    tickrateConfigPath := getEnv("TICKRATE_CONFIG_PATH", "infrastructure/network/tickrate-config.yaml")
    tickrateMgr, err := tickrate.NewTickrateManager(tickrateConfigPath)
    if err != nil {
        logger.WithError(err).Fatal("Failed to load tickrate configuration")
    }
    
    // Установка зоны по умолчанию (можно из переменной окружения)
    defaultZone := getEnv("DEFAULT_ZONE_TYPE", "safe_zones")
    tickrateMgr.SetZone(defaultZone)
    
    handler := server.NewGatewayHandler(tickrateMgr, sessionMgr)
    
    // ... остальной код
}
```

### Этап 5: Добавление адаптивного тикрейта

Создать адаптивный модуль:

```go
// services/realtime-gateway-go/server/tickrate/adaptive.go
package tickrate

import (
    "context"
    "sync"
    "time"
)

type AdaptiveTickrate struct {
    manager        *TickrateManager
    playerCount    int
    networkLatency time.Duration
    cpuUsage       float64
    mu             sync.RWMutex
    adjustmentInterval time.Duration
    stopChan       chan struct{}
}

func NewAdaptiveTickrate(manager *TickrateManager) *AdaptiveTickrate {
    return &AdaptiveTickrate{
        manager: manager,
        adjustmentInterval: 5 * time.Second,
        stopChan: make(chan struct{}),
    }
}

func (at *AdaptiveTickrate) Start(ctx context.Context) {
    ticker := time.NewTicker(at.adjustmentInterval)
    defer ticker.Stop()
    
    for {
        select {
        case <-ctx.Done():
            return
        case <-at.stopChan:
            return
        case <-ticker.C:
            at.adjustTickrate()
        }
    }
}

func (at *AdaptiveTickrate) adjustTickrate() {
    at.mu.RLock()
    playerCount := at.playerCount
    latency := at.networkLatency
    cpu := at.cpuUsage
    at.mu.RUnlock()
    
    // Расчет адаптивного тикрейта согласно конфигурации
    // ... реализация согласно adaptive-tickrate-config.yaml
}

func (at *AdaptiveTickrate) UpdateMetrics(playerCount int, latency time.Duration, cpuUsage float64) {
    at.mu.Lock()
    defer at.mu.Unlock()
    
    at.playerCount = playerCount
    at.networkLatency = latency
    at.cpuUsage = cpuUsage
}
```

## Конфигурация окружения

Добавить переменные окружения:

```bash
# Тип зоны по умолчанию
DEFAULT_ZONE_TYPE=safe_zones

# Путь к конфигурации тикрейта
TICKRATE_CONFIG_PATH=infrastructure/network/tickrate-config.yaml

# Путь к конфигурации адаптивного тикрейта
ADAPTIVE_TICKRATE_CONFIG_PATH=infrastructure/network/adaptive-tickrate-config.yaml

# Включить адаптивный тикрейт
ADAPTIVE_TICKRATE_ENABLED=true
```

## Интеграция с Zone Manager

При переходе игрока в другую зону:

```go
func (h *GatewayHandler) OnPlayerZoneChange(playerID string, newZoneType string) {
    // Обновление тикрейта для новой зоны
    h.UpdateTickrateForZone(newZoneType)
    
    // Уведомление клиента о смене тикрейта
    h.notifyClientTickrateChange(playerID, h.tickRate)
}
```

## Мониторинг

Добавить метрики для мониторинга тикрейта:

```go
var (
    tickrateCurrent = prometheus.NewGaugeVec(
        prometheus.GaugeOpts{
            Name: "tickrate_current",
            Help: "Current tickrate",
        },
        []string{"zone_type"},
    )
    
    tickrateTarget = prometheus.NewGaugeVec(
        prometheus.GaugeOpts{
            Name: "tickrate_target",
            Help: "Target tickrate",
        },
        []string{"zone_type"},
    )
)
```

## Миграция

### Шаг 1: Добавить зависимости

```bash
cd services/realtime-gateway-go
go get gopkg.in/yaml.v3
```

### Шаг 2: Создать структуру пакетов

```
services/realtime-gateway-go/
  server/
    tickrate/
      config.go
      manager.go
      adaptive.go
```

### Шаг 3: Обновить существующий код

Постепенно обновлять существующий код для использования нового TickrateManager вместо жестко заданного значения.

### Шаг 4: Тестирование

1. Тестировать загрузку конфигурации
2. Тестировать переключение зон
3. Тестировать адаптивный тикрейт
4. Тестировать мониторинг

## Примеры использования

### Пример 1: Загрузка конфигурации

```go
tickrateMgr, err := tickrate.NewTickrateManager("infrastructure/network/tickrate-config.yaml")
if err != nil {
    log.Fatal(err)
}
```

### Пример 2: Получение тикрейта для зоны

```go
tickrate := tickrateMgr.GetTickrateForZone("pvp_small")
// tickrate = 128
```

### Пример 3: Установка зоны

```go
tickrateMgr.SetZone("gvg_200")
currentTickrate := tickrateMgr.GetCurrentTickrate()
// currentTickrate = 80
```

### Пример 4: Адаптивный тикрейт

```go
adaptive := tickrate.NewAdaptiveTickrate(tickrateMgr)
adaptive.UpdateMetrics(150, 45*time.Millisecond, 65.5)

ctx := context.Background()
go adaptive.Start(ctx)
```

## Важные замечания

1. **Обратная совместимость:** Сохранить возможность работы без конфигурации (fallback на значение по умолчанию)
2. **Производительность:** Загрузка конфигурации должна быть быстрой, кэширование конфигурации
3. **Безопасность:** Валидация значений конфигурации (min/max тикрейт)
4. **Мониторинг:** Все изменения тикрейта должны логироваться и отслеживаться

## Следующие шаги

1. Реализовать TickrateManager
2. Интегрировать с GatewayHandler
3. Добавить адаптивный тикрейт
4. Добавить метрики
5. Протестировать все сценарии

## Связанные файлы

- `infrastructure/network/tickrate-config.yaml` - конфигурация тикрейта
- `infrastructure/network/adaptive-tickrate-config.yaml` - конфигурация адаптивного тикрейта
- `infrastructure/network/tickrate-specification.yaml` - спецификация
- `services/realtime-gateway-go/main.go` - точка входа сервиса

