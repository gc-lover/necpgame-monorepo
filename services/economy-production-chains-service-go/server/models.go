package server

import "time"

type ProductionChain struct {
	ID             string  `json:"id"`
	Name           string  `json:"name,omitempty"`
	Description    string  `json:"description,omitempty"`
	ItemType       string  `json:"item_type,omitempty"`
	ItemTier       string  `json:"item_tier,omitempty"`
	BaseCost       float64 `json:"base_cost,omitempty"`
	StagesCount    int     `json:"stages_count,omitempty"`
	EstimatedTime  int     `json:"estimated_time,omitempty"`
}

type ProductionStage struct {
	ID                string               `json:"id"`
	Name              string               `json:"name,omitempty"`
	Description       string               `json:"description,omitempty"`
	StageType         string               `json:"stage_type,omitempty"`
	RequiredResources []ResourceRequirement `json:"required_resources,omitempty"`
	FailureChance     float64              `json:"failure_chance,omitempty"`
	StageNumber       int                  `json:"stage_number,omitempty"`
	EstimatedTime     int                  `json:"estimated_time,omitempty"`
}

type ResourceRequirement struct {
	ResourceID   string `json:"resource_id"`
	ResourceName string `json:"resource_name,omitempty"`
	Quantity     int    `json:"quantity,omitempty"`
	Optional     bool   `json:"optional,omitempty"`
}

type ProductionChainDetails struct {
	ProductionChain
	Stages       []ProductionStage `json:"stages,omitempty"`
	Requirements struct {
		Licenses []string `json:"licenses,omitempty"`
		Stations []string `json:"stations,omitempty"`
	} `json:"requirements,omitempty"`
}

type StartProductionRequest struct {
	StationID string `json:"station_id,omitempty"`
	Quantity  int    `json:"quantity"`
}

type CreateProductionOrderRequest struct {
	ChainID   string `json:"chain_id"`
	StationID string `json:"station_id,omitempty"`
	Quantity  int    `json:"quantity"`
}

type CreateRushOrderRequest struct {
	TimeReduction int `json:"time_reduction"`
}

type ProductionOrder struct {
	ID                  string     `json:"id"`
	ChainID             string     `json:"chain_id,omitempty"`
	ChainName           string     `json:"chain_name,omitempty"`
	Status              string     `json:"status,omitempty"`
	StartedAt           *time.Time `json:"started_at,omitempty"`
	EstimatedCompletion *time.Time `json:"estimated_completion,omitempty"`
	CreatedAt           *time.Time `json:"created_at,omitempty"`
	Quantity            int        `json:"quantity,omitempty"`
	CurrentStage        int        `json:"current_stage,omitempty"`
	TotalStages         int        `json:"total_stages,omitempty"`
}

type ProductionStageProgress struct {
	StageID     string     `json:"stage_id"`
	Status      string     `json:"status,omitempty"`
	StartedAt   *time.Time `json:"started_at,omitempty"`
	CompletedAt *time.Time `json:"completed_at,omitempty"`
	Progress    float64    `json:"progress,omitempty"`
	StageNumber int        `json:"stage_number,omitempty"`
}

type ProductionOrderDetails struct {
	ProductionOrder
	Stages     []ProductionStageProgress `json:"stages,omitempty"`
	StationID  string                    `json:"station_id,omitempty"`
	StationName string                   `json:"station_name,omitempty"`
	TotalCost  float64                   `json:"total_cost,omitempty"`
	RushOrder  bool                      `json:"rush_order,omitempty"`
}







