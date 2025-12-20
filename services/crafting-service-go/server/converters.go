// Package server Issue: #2203 - API model converters
package server

import (
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/crafting-service-go/pkg/api"
)

// convertRecipeRequirementsToAPI converts internal RecipeRequirements to API RecipeRequirements
func (h *CraftingHandler) convertRecipeRequirementsToAPI(req RecipeRequirements) api.RecipeRequirements {
	result := api.RecipeRequirements{}

	if req.SkillLevel != nil {
		var skillLevel api.OptInt
		skillLevel.SetTo(*req.SkillLevel)
		result.SkillLevel = skillLevel
	}

	if req.StationType != nil {
		var stationType api.OptRecipeRequirementsStationType
		stationType.SetTo(api.RecipeRequirementsStationType(*req.StationType))
		result.StationType = stationType
	}

	if req.ZoneAccess != nil && *req.ZoneAccess != "" {
		var zoneAccess api.OptString
		zoneAccess.SetTo(*req.ZoneAccess)
		result.ZoneAccess = zoneAccess
	}

	return result
}

// convertRecipeToAPI converts internal Recipe to API Recipe
func (h *CraftingHandler) convertRecipeToAPI(recipe Recipe) api.Recipe {
	var createdAt api.OptDateTime
	if recipe.CreatedAt != (time.Time{}) {
		createdAt.SetTo(recipe.CreatedAt)
	}

	var updatedAt api.OptDateTime
	if recipe.UpdatedAt != nil {
		updatedAt.SetTo(*recipe.UpdatedAt)
	}

	var description api.OptString
	if recipe.Description != "" {
		description.SetTo(recipe.Description)
	}

	// Convert materials
	var materials []api.RecipeMaterial
	for _, m := range recipe.Materials {
		material := api.RecipeMaterial{
			ItemID:   m.ItemID,
			Quantity: m.Quantity,
		}
		if m.QualityMin != nil {
			var qualityMin api.OptInt
			qualityMin.SetTo(*m.QualityMin)
			material.QualityMin = qualityMin
		}
		materials = append(materials, material)
	}

	var requirements api.OptRecipeRequirements
	if recipe.Requirements != nil {
		req := h.convertRecipeRequirementsToAPI(*recipe.Requirements)
		requirements.SetTo(req)
	}

	return api.Recipe{
		ID:           recipe.ID,
		Name:         recipe.Name,
		Description:  description,
		Category:     api.RecipeCategory(recipe.Category),
		Tier:         recipe.Tier,
		Quality:      recipe.Quality,
		Duration:     recipe.Duration,
		SuccessRate:  float32(recipe.SuccessRate),
		Materials:    materials,
		Requirements: requirements,
		CreatedAt:    createdAt,
		UpdatedAt:    updatedAt,
	}
}

// convertCreateRecipeReqToInternal converts API CreateRecipeRequest to internal Recipe
func (h *CraftingHandler) convertCreateRecipeReqToInternal(req api.CreateRecipeRequest) *Recipe {
	recipe := &Recipe{
		Name:     req.Name,
		Category: string(req.Category),
		Tier:     req.Tier,
		Duration: req.Duration,
	}

	if req.SuccessRate.IsSet() {
		recipe.SuccessRate = float64(req.SuccessRate.Value)
	} else {
		recipe.SuccessRate = 0.95 // Default
	}

	if req.Description.IsSet() {
		recipe.Description = req.Description.Value
	}

	if req.Quality.IsSet() {
		recipe.Quality = req.Quality.Value
	} else {
		recipe.Quality = 50 // Default
	}

	// Convert materials
	for _, m := range req.Materials {
		var qualityMin *int
		if m.QualityMin.IsSet() {
			qm := m.QualityMin.Value
			qualityMin = &qm
		}

		recipe.Materials = append(recipe.Materials, RecipeMaterial{
			ItemID:     m.ItemID,
			Quantity:   m.Quantity,
			QualityMin: qualityMin,
		})
	}

	// Convert requirements
	if req.Requirements.IsSet() {
		requirements := RecipeRequirements{}

		if req.Requirements.Value.ZoneAccess.IsSet() {
			zoneAccess := req.Requirements.Value.ZoneAccess.Value
			requirements.ZoneAccess = &zoneAccess
		}

		if req.Requirements.Value.SkillLevel.IsSet() {
			requirements.SkillLevel = &req.Requirements.Value.SkillLevel.Value
		}

		if req.Requirements.Value.StationType.IsSet() {
			stationType := string(req.Requirements.Value.StationType.Value)
			requirements.StationType = &stationType
		}

		recipe.Requirements = &requirements
	}

	return recipe
}

// convertUpdateRecipeReqToInternal converts API UpdateRecipeRequest to internal Recipe
func (h *CraftingHandler) convertUpdateRecipeReqToInternal(req api.UpdateRecipeRequest) *Recipe {
	recipe := &Recipe{}

	if req.Name.IsSet() {
		recipe.Name = req.Name.Value
	}

	if req.Description.IsSet() {
		recipe.Description = req.Description.Value
	}

	if req.Quality.IsSet() {
		recipe.Quality = req.Quality.Value
	}

	if req.Duration.IsSet() {
		recipe.Duration = req.Duration.Value
	}

	if req.SuccessRate.IsSet() {
		recipe.SuccessRate = float64(req.SuccessRate.Value)
	}

	// Convert materials
	for _, m := range req.Materials {
		var qualityMin *int
		if m.QualityMin.IsSet() {
			qm := m.QualityMin.Value
			qualityMin = &qm
		}

		recipe.Materials = append(recipe.Materials, RecipeMaterial{
			ItemID:     m.ItemID,
			Quantity:   m.Quantity,
			QualityMin: qualityMin,
		})
	}

	return recipe
}

// convertOrderToAPI converts internal Order to API Order
func (h *CraftingHandler) convertOrderToAPI(order *Order) api.Order {
	var stationID api.OptNilUUID
	if order.StationID != nil {
		stationID.SetTo(*order.StationID)
	}

	var startedAt api.OptNilDateTime
	if order.StartedAt != nil {
		startedAt.SetTo(*order.StartedAt)
	}

	var completedAt api.OptNilDateTime
	if order.CompletedAt != nil {
		completedAt.SetTo(*order.CompletedAt)
	}

	var updatedAt api.OptNilDateTime
	if order.UpdatedAt != nil {
		updatedAt.SetTo(*order.UpdatedAt)
	}

	var progress api.OptFloat32
	progress.SetTo(float32((*order).Progress))

	return api.Order{
		ID:              (*order).ID,
		PlayerID:        (*order).PlayerID,
		RecipeID:        (*order).RecipeID,
		StationID:       stationID,
		Status:          api.OrderStatus((*order).Status),
		QualityModifier: float32((*order).QualityModifier),
		StationBonus:    float32((*order).StationBonus),
		Progress:        progress,
		StartedAt:       startedAt,
		CompletedAt:     completedAt,
		CreatedAt:       (*order).CreatedAt,
		UpdatedAt:       updatedAt,
	}
}

// convertStationToAPI converts internal Station to API Station
func (h *CraftingHandler) convertStationToAPI(station Station) api.Station {
	var ownerID api.OptNilUUID
	if station.OwnerID != nil {
		ownerID.SetTo(*station.OwnerID)
	}

	var currentOrderID api.OptNilUUID
	if station.CurrentOrderID != nil {
		currentOrderID.SetTo(*station.CurrentOrderID)
	}

	var lastMaintenance api.OptDateTime
	if station.LastMaintenance != nil {
		lastMaintenance.SetTo(*station.LastMaintenance)
	}

	var createdAt api.OptDateTime
	if station.CreatedAt != (time.Time{}) {
		createdAt.SetTo(station.CreatedAt)
	}

	var updatedAt api.OptDateTime
	if station.UpdatedAt != nil {
		updatedAt.SetTo(*station.UpdatedAt)
	}

	var description api.OptString
	if station.Description != "" {
		description.SetTo(station.Description)
	}

	var maintenanceCost api.OptInt
	maintenanceCost.SetTo(station.MaintenanceCost)

	var isAvailable api.OptBool
	isAvailable.SetTo(station.IsAvailable)

	return api.Station{
		ID:              station.ID,
		Name:            station.Name,
		Description:     description,
		Type:            api.StationType(station.Type),
		Efficiency:      float32(station.Efficiency),
		ZoneID:          station.ZoneID,
		OwnerID:         ownerID,
		CurrentOrderID:  currentOrderID,
		IsAvailable:     isAvailable,
		MaintenanceCost: maintenanceCost,
		LastMaintenance: lastMaintenance,
		CreatedAt:       createdAt,
		UpdatedAt:       updatedAt,
	}
}

// convertStationBookingToAPI converts internal StationBooking to API StationBooking
func (h *CraftingHandler) convertStationBookingToAPI(booking StationBooking) api.StationBooking {
	var priority api.OptInt
	priority.SetTo(booking.Priority)

	return api.StationBooking{
		StationID:   booking.StationID,
		PlayerID:    booking.PlayerID,
		BookedUntil: booking.BookedUntil,
		Priority:    priority,
		CreatedAt:   booking.CreatedAt,
	}
}

// convertProductionChainToAPI converts internal ProductionChain to API ProductionChain
func (h *CraftingHandler) convertProductionChainToAPI(chain ProductionChain) api.ProductionChain {
	var startedAt api.OptNilDateTime
	if chain.StartedAt != nil {
		startedAt.SetTo(*chain.StartedAt)
	}

	var completedAt api.OptNilDateTime
	if chain.CompletedAt != nil {
		completedAt.SetTo(*chain.CompletedAt)
	}

	var createdAt api.OptDateTime
	if chain.CreatedAt != (time.Time{}) {
		createdAt.SetTo(chain.CreatedAt)
	}

	var updatedAt api.OptDateTime
	if chain.UpdatedAt != nil {
		updatedAt.SetTo(*chain.UpdatedAt)
	}

	var description api.OptString
	if chain.Description != "" {
		description.SetTo(chain.Description)
	}

	var category api.OptProductionChainCategory
	if chain.Category != "" {
		category.SetTo(api.ProductionChainCategory(chain.Category))
	}

	var complexity api.OptInt
	complexity.SetTo(chain.Complexity)

	var currentStage api.OptInt
	currentStage.SetTo(chain.CurrentStage)

	var playerID api.OptUUID
	playerID.SetTo(chain.PlayerID)

	var totalProgress api.OptFloat32
	totalProgress.SetTo(float32(chain.TotalProgress))

	// Convert stages
	var stages []api.ChainStage
	for _, s := range chain.Stages {
		var status api.OptChainStageStatus
		status.SetTo(api.ChainStageStatus(s.Status))

		stages = append(stages, api.ChainStage{
			OrderID:      s.OrderID,
			Sequence:     s.Sequence,
			Dependencies: s.Dependencies,
			Status:       status,
		})
	}

	return api.ProductionChain{
		ID:            chain.ID,
		Name:          chain.Name,
		Description:   description,
		Category:      category,
		Complexity:    complexity,
		Stages:        stages,
		Status:        api.ProductionChainStatus(chain.Status),
		CurrentStage:  currentStage,
		PlayerID:      playerID,
		TotalProgress: totalProgress,
		StartedAt:     startedAt,
		CompletedAt:   completedAt,
		CreatedAt:     createdAt,
		UpdatedAt:     updatedAt,
	}
}

// convertCreateChainReqToInternal converts API CreateChainRequest to internal ProductionChain
func (h *CraftingHandler) convertCreateChainReqToInternal(req api.CreateChainRequest) *ProductionChain {
	chain := &ProductionChain{
		Name: req.Name,
	}

	if req.Description.IsSet() {
		chain.Description = req.Description.Value
	}

	if req.Category.IsSet() {
		chain.Category = string(req.Category.Value)
	}

	// Convert stages
	for i, s := range req.Stages {
		stage := ChainStage{
			OrderID:      s.RecipeID, // Note: This should be order ID, but we use recipe ID for now
			Sequence:     i + 1,      // Sequence starts from 1
			Dependencies: s.Dependencies,
			Status:       "pending",
		}
		chain.Stages = append(chain.Stages, stage)
	}

	chain.Complexity = len(chain.Stages)

	return chain
}
