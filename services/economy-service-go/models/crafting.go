// Package models Issue: #140890170 - Crafting mechanics implementation
package models

import (
	"time"
)

// CraftingRecipe представляет рецепт крафта
type CraftingRecipe struct {
	ID          string        `json:"id" db:"id"`
	Name        string        `json:"name" db:"name"`
	Description string        `json:"description" db:"description"`
	Tier        int           `json:"tier" db:"tier"`
	Category    string        `json:"category" db:"category"`
	Quality     int           `json:"quality" db:"quality"`
	Duration    time.Duration `json:"duration" db:"duration"`
	SuccessRate float64       `json:"success_rate" db:"success_rate"`
	CreatedAt   time.Time     `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time     `json:"updated_at" db:"updated_at"`

	// Материалы для крафта
	Materials []RecipeMaterial `json:"materials"`

	// Результат крафта
	Output RecipeOutput `json:"output"`

	// Требования к крафту
	Requirements RecipeRequirements `json:"requirements"`
}

// RecipeMaterial представляет материал, необходимый для рецепта
type RecipeMaterial struct {
	ResourceID string `json:"resource_id" db:"resource_id"`
	Quantity   int    `json:"quantity" db:"quantity"`
	Quality    int    `json:"quality" db:"quality"`
	IsOptional bool   `json:"is_optional" db:"is_optional"`
}

// RecipeOutput представляет результат крафта
type RecipeOutput struct {
	ItemID   string `json:"item_id" db:"item_id"`
	Quantity int    `json:"quantity" db:"quantity"`
	Quality  int    `json:"quality" db:"quality"`
}

// RecipeRequirements представляет требования к крафту
type RecipeRequirements struct {
	SkillLevel    int      `json:"skill_level" db:"skill_level"`
	StationType   string   `json:"station_type" db:"station_type"`
	SpecialTools  []string `json:"special_tools" db:"special_tools"`
	Prerequisites []string `json:"prerequisites" db:"prerequisites"`
}

// CraftingOrder представляет заказ на крафт
type CraftingOrder struct {
	ID          string     `json:"id" db:"id"`
	PlayerID    string     `json:"player_id" db:"player_id"`
	RecipeID    string     `json:"recipe_id" db:"recipe_id"`
	StationID   string     `json:"station_id" db:"station_id"`
	Status      string     `json:"status" db:"status"` // pending, crafting, completed, failed
	Quality     int        `json:"quality" db:"quality"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	StartedAt   *time.Time `json:"started_at" db:"started_at"`
	CompletedAt *time.Time `json:"completed_at" db:"completed_at"`

	// Материалы, использованные в крафте
	UsedMaterials []UsedMaterial `json:"used_materials"`

	// Результат крафта
	Result *CraftingResult `json:"result"`
}

// UsedMaterial представляет использованный материал
type UsedMaterial struct {
	ResourceID string `json:"resource_id" db:"resource_id"`
	Quantity   int    `json:"quantity" db:"quantity"`
	Quality    int    `json:"quality" db:"quality"`
}

// CraftingResult представляет результат крафта
type CraftingResult struct {
	ItemID   string `json:"item_id" db:"item_id"`
	Quantity int    `json:"quantity" db:"quantity"`
	Quality  int    `json:"quality" db:"quality"`
	Success  bool   `json:"success" db:"success"`
}

// CraftingStation представляет станцию крафта
type CraftingStation struct {
	ID         string    `json:"id" db:"id"`
	Name       string    `json:"name" db:"name"`
	Type       string    `json:"type" db:"type"`
	Location   string    `json:"location" db:"location"`
	OwnerID    string    `json:"owner_id" db:"owner_id"`
	Tier       int       `json:"tier" db:"tier"`
	Efficiency float64   `json:"efficiency" db:"efficiency"`
	IsActive   bool      `json:"is_active" db:"is_active"`
	CreatedAt  time.Time `json:"created_at" db:"created_at"`
	UpdatedAt  time.Time `json:"updated_at" db:"updated_at"`

	// Статистика использования
	UsageStats StationUsageStats `json:"usage_stats"`
}

// StationUsageStats представляет статистику использования станции
type StationUsageStats struct {
	TotalOrders      int        `json:"total_orders" db:"total_orders"`
	SuccessfulOrders int        `json:"successful_orders" db:"successful_orders"`
	FailedOrders     int        `json:"failed_orders" db:"failed_orders"`
	AverageQuality   float64    `json:"average_quality" db:"average_quality"`
	LastUsedAt       *time.Time `json:"last_used_at" db:"last_used_at"`
}

// CraftingContract представляет контракт на крафт
type CraftingContract struct {
	ID          string         `json:"id" db:"id"`
	Title       string         `json:"title" db:"title"`
	Description string         `json:"description" db:"description"`
	ClientID    string         `json:"client_id" db:"client_id"`
	CrafterID   string         `json:"crafter_id" db:"crafter_id"`
	RecipeID    string         `json:"recipe_id" db:"recipe_id"`
	Status      string         `json:"status" db:"status"` // open, accepted, completed, cancelled
	Reward      ContractReward `json:"reward"`
	Deadline    *time.Time     `json:"deadline" db:"deadline"`
	CreatedAt   time.Time      `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time      `json:"updated_at" db:"updated_at"`
}

// ContractReward представляет награду за контракт
type ContractReward struct {
	Currency string  `json:"currency" db:"currency"`
	Amount   float64 `json:"amount" db:"amount"`
	Bonus    float64 `json:"bonus" db:"bonus"` // бонус за качество/скорость
}

// CraftingSkill представляет навык крафта игрока
type CraftingSkill struct {
	PlayerID       string    `json:"player_id" db:"player_id"`
	SkillType      string    `json:"skill_type" db:"skill_type"`
	Level          int       `json:"level" db:"level"`
	Experience     int       `json:"experience" db:"experience"`
	Specialization string    `json:"specialization" db:"specialization"`
	CreatedAt      time.Time `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time `json:"updated_at" db:"updated_at"`
}

// CraftingQualityModifier представляет модификатор качества
type CraftingQualityModifier struct {
	Type     string  `json:"type"`   // skill, material, station, luck
	Value    float64 `json:"value"`  // модификатор качества
	Source   string  `json:"source"` // источник модификатора
	IsActive bool    `json:"is_active"`
}

// CraftingFailure представляет провал крафта
type CraftingFailure struct {
	OrderID       string             `json:"order_id" db:"order_id"`
	Reason        string             `json:"reason" db:"reason"`
	LostMaterials []LostMaterial     `json:"lost_materials"`
	Penalties     map[string]float64 `json:"penalties"` // штрафы
	CreatedAt     time.Time          `json:"created_at" db:"created_at"`
}

// LostMaterial представляет потерянный материал
type LostMaterial struct {
	ResourceID string `json:"resource_id" db:"resource_id"`
	Quantity   int    `json:"quantity" db:"quantity"`
	Reason     string `json:"reason" db:"reason"` // damaged, consumed, destroyed
}

// CraftingAnalytics представляет аналитику крафта
type CraftingAnalytics struct {
	Period         string             `json:"period"` // day, week, month
	TotalOrders    int                `json:"total_orders"`
	SuccessRate    float64            `json:"success_rate"`
	AverageQuality float64            `json:"average_quality"`
	PopularRecipes []PopularRecipe    `json:"popular_recipes"`
	ResourceUsage  map[string]int     `json:"resource_usage"`
	MarketImpact   map[string]float64 `json:"market_impact"`
}

// PopularRecipe представляет популярный рецепт
type PopularRecipe struct {
	RecipeID       string  `json:"recipe_id"`
	Count          int     `json:"count"`
	SuccessRate    float64 `json:"success_rate"`
	AverageQuality float64 `json:"average_quality"`
}
