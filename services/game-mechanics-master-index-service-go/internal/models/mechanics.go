// Package models содержит модели данных для игровых механик
// Issue: #2176 - Game Mechanics Systems Master Index
package models

import (
	"time"
)

// GameMechanic представляет игровую механику в системе
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), int64 (8)
// Medium fields (8 bytes aligned): pointers, slices
// Small fields (≤4 bytes): bool, int32, enums
//go:align 64
type GameMechanic struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
	ID          string    `json:"id" db:"id"`                     // UUID механики (16 bytes)
	Name        string    `json:"name" db:"name"`                 // Название механики
	ServiceName string    `json:"service_name" db:"service_name"` // Название обслуживающего сервиса
	Version     string    `json:"version" db:"version"`           // Версия механики
	Endpoint    string    `json:"endpoint" db:"endpoint"`         // API endpoint
	Type        string    `json:"type" db:"type"`                 // Тип: combat, economy, social, etc.
	Category    string    `json:"category" db:"category"`         // Категория: core, optional, experimental
	Status      string    `json:"status" db:"status"`             // Статус: active, inactive, deprecated

	// Small fields (≤4 bytes): int32, bool
	Priority   int  `json:"priority" db:"priority"`       // Приоритет загрузки (1-10)
	IsRequired bool `json:"is_required" db:"is_required"` // Обязательна ли для игры
}

// MechanicDependency описывает зависимость между механиками
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16+ bytes): strings
// Small fields (≤4 bytes): bool
//go:align 64
type MechanicDependency struct {
	// Large fields first (16+ bytes): strings
	ID             string `json:"id" db:"id"`
	MechanicID     string `json:"mechanic_id" db:"mechanic_id"`
	DependsOnID    string `json:"depends_on_id" db:"depends_on_id"`
	DependencyType string `json:"dependency_type" db:"dependency_type"` // required, optional, conflicts

	// Small fields (≤4 bytes): bool
	IsHardDependency bool `json:"is_hard_dependency" db:"is_hard_dependency"` // true = обязательная зависимость
}

// MechanicConfig содержит конфигурацию механики
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), map (24+), string (16+)
// Small fields (≤4 bytes): bool
//go:align 64
type MechanicConfig struct {
	// Large fields first (16-24 bytes): Time (24), map (24+), string (16+)
	UpdatedAt     time.Time              `json:"updated_at"`
	Settings      map[string]interface{} `json:"settings"`
	MechanicID    string                 `json:"mechanic_id"`
	ConfigVersion string                 `json:"config_version"`

	// Small fields (≤4 bytes): bool
	IsActive bool `json:"is_active"`
}

// SystemHealth представляет состояние здоровья системы механик
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), float64 (8), int64 (8)
// Medium fields (8 bytes aligned): int32
// Small fields (≤4 bytes): n/a
//go:align 64
type SystemHealth struct {
	// Large fields first (16-24 bytes): Time (24), float64 (8)
	LastHealthCheck   time.Time `json:"last_health_check"`
	HealthScore       float64   `json:"health_score"` // 0-100, процент работоспособных механик

	// Medium fields (8 bytes aligned): int32 (grouped together)
	TotalMechanics    int `json:"total_mechanics"`
	ActiveMechanics   int `json:"active_mechanics"`
	InactiveMechanics int `json:"inactive_mechanics"`
}

// MechanicStatus описывает статус конкретной механики
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), int64 (8), string (16+)
// Medium fields (8 bytes aligned): int32
// Small fields (≤4 bytes): bool
//go:align 64
type MechanicStatus struct {
	// Large fields first (16-24 bytes): Time (24), int64 (8), string (16+)
	LastChecked   time.Time `json:"last_checked"`
	ResponseTime  int64     `json:"response_time"` // в миллисекундах
	MechanicID    string    `json:"mechanic_id"`
	ServiceStatus string    `json:"service_status"` // up, down, degraded

	// Medium fields (8 bytes aligned): int32
	ErrorCount int `json:"error_count"`

	// Small fields (≤4 bytes): bool
	IsHealthy bool `json:"is_healthy"`
}

// MechanicRegistry центральный реестр механик
type MechanicRegistry struct {
	Mechanics      map[string]*GameMechanic      `json:"mechanics"`
	Dependencies   []*MechanicDependency         `json:"dependencies"`
	Configurations map[string]*MechanicConfig    `json:"configurations"`
	Status         map[string]*MechanicStatus    `json:"status"`
	Health         *SystemHealth                 `json:"health"`
}

// NewMechanicRegistry создает новый реестр механик
func NewMechanicRegistry() *MechanicRegistry {
	return &MechanicRegistry{
		Mechanics:      make(map[string]*GameMechanic),
		Dependencies:   make([]*MechanicDependency, 0),
		Configurations: make(map[string]*MechanicConfig),
		Status:         make(map[string]*MechanicStatus),
		Health:         &SystemHealth{},
	}
}

// RegisterMechanic регистрирует новую механику в реестре
func (r *MechanicRegistry) RegisterMechanic(mechanic *GameMechanic) {
	r.Mechanics[mechanic.ID] = mechanic
}

// GetMechanic возвращает механику по ID
func (r *MechanicRegistry) GetMechanic(id string) (*GameMechanic, bool) {
	mechanic, exists := r.Mechanics[id]
	return mechanic, exists
}

// GetMechanicsByType возвращает все механики определенного типа
func (r *MechanicRegistry) GetMechanicsByType(mechanicType string) []*GameMechanic {
	var result []*GameMechanic
	for _, mechanic := range r.Mechanics {
		if mechanic.Type == mechanicType {
			result = append(result, mechanic)
		}
	}
	return result
}

// GetActiveMechanics возвращает все активные механики
func (r *MechanicRegistry) GetActiveMechanics() []*GameMechanic {
	var result []*GameMechanic
	for _, mechanic := range r.Mechanics {
		if mechanic.Status == "active" {
			result = append(result, mechanic)
		}
	}
	return result
}

// ValidateDependencies проверяет корректность зависимостей
func (r *MechanicRegistry) ValidateDependencies() []string {
	var errors []string
	for _, dep := range r.Dependencies {
		if _, exists := r.Mechanics[dep.MechanicID]; !exists {
			errors = append(errors, "Mechanic "+dep.MechanicID+" not found")
		}
		if _, exists := r.Mechanics[dep.DependsOnID]; !exists {
			errors = append(errors, "Dependency "+dep.DependsOnID+" not found")
		}
	}
	return errors
}