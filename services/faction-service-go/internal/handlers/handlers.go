//go:align 64
package handlers

import (
	"context"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/faction-service-go/internal/models"
	"necpgame/services/faction-service-go/internal/service"
	api "necpgame/services/faction-service-go/proto/openapi"
)

//go:align 64
type Handlers struct {
	service *service.Service
	logger  *zap.Logger
}

//go:align 64
type Config struct {
	Service *service.Service
	Logger  *zap.Logger
}

//go:align 64
func NewHandlers(config Config) *Handlers {
	return &Handlers{
		service: config.Service,
		logger:  config.Logger,
	}
}

//go:align 64
func (h *Handlers) FactionServiceHealthCheck(ctx context.Context, req *api.FactionServiceHealthCheckReq) (*api.HealthResponse, error) {
	health, err := h.service.GetSystemHealth(ctx)
	if err != nil {
		h.logger.Error("Health check failed", zap.Error(err))
		return &api.HealthResponse{
			Status:  "unhealthy",
			Message: "Service is experiencing issues",
			Timestamp: time.Now(),
		}, nil
	}

	return &api.HealthResponse{
		Status:  "healthy",
		Message: "Faction service is operational",
		Timestamp: time.Now(),
		Details: &api.HealthDetails{
			TotalFactions:   &health.TotalFactions,
			ActiveFactions:  &health.ActiveFactions,
			TotalDiplomacy:  &health.TotalDiplomacy,
			ActiveDiplomacy: &health.ActiveDiplomacy,
		},
	}, nil
}

//go:align 64
func (h *Handlers) FactionServiceBatchHealthCheck(ctx context.Context, req *api.FactionServiceBatchHealthCheckReq) (*api.HealthBatchSuccess, error) {
	results := make(map[string]api.HealthResponse)

	for _, serviceName := range req.Services {
		health, err := h.service.GetSystemHealth(ctx)
		if err != nil {
			results[serviceName] = api.HealthResponse{
				Status:  "unhealthy",
				Message: fmt.Sprintf("Service %s is experiencing issues", serviceName),
				Timestamp: time.Now(),
			}
		} else {
			results[serviceName] = api.HealthResponse{
				Status:  "healthy",
				Message: fmt.Sprintf("Service %s is operational", serviceName),
				Timestamp: time.Now(),
				Details: &api.HealthDetails{
					TotalFactions:   &health.TotalFactions,
					ActiveFactions:  &health.ActiveFactions,
					TotalDiplomacy:  &health.TotalDiplomacy,
					ActiveDiplomacy: &health.ActiveDiplomacy,
				},
			}
		}
	}

	return &api.HealthBatchSuccess{
		Services: results,
	}, nil
}

//go:align 64
func (h *Handlers) ListFactions(ctx context.Context, req *api.ListFactionsReq) (*api.FactionListResponse, error) {
	limit := 20
	offset := 0

	if req.Limit != nil && *req.Limit > 0 && *req.Limit <= 50 {
		limit = *req.Limit
	}

	if req.Page != nil && *req.Page > 0 {
		offset = (*req.Page - 1) * limit
	}

	filters := make(map[string]interface{})
	if req.Name != nil && *req.Name != "" {
		filters["name"] = *req.Name
	}
	if req.ReputationMin != nil {
		filters["reputation_min"] = *req.ReputationMin
	}
	if req.ReputationMax != nil {
		filters["reputation_max"] = *req.ReputationMax
	}
	if req.DiplomaticStatus != nil {
		filters["diplomatic_status"] = *req.DiplomaticStatus
	}

	factions, total, err := h.service.ListFactions(ctx, limit, offset, filters)
	if err != nil {
		h.logger.Error("Failed to list factions", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusInternalServerError,
			Response: api.ErrorResponse{
				Error:   "InternalServerError",
				Message: "Failed to retrieve factions",
			},
		}
	}

	factionSummaries := make([]api.FactionSummary, len(factions))
	for i, faction := range factions {
		factionSummaries[i] = api.FactionSummary{
			FactionId: faction.FactionID.String(),
			Name:      faction.Name,
			Reputation: &faction.Reputation,
		}
		if faction.Influence > 0 {
			factionSummaries[i].Influence = &faction.Influence
		}
		if faction.MemberCount > 0 {
			factionSummaries[i].MemberCount = &faction.MemberCount
		}
		if faction.Description != "" {
			factionSummaries[i].Description = &faction.Description
		}
		factionSummaries[i].CreatedAt = faction.CreatedAt
	}

	totalPages := (total + limit - 1) / limit

	return &api.FactionListResponse{
		Factions: factionSummaries,
		Pagination: api.PaginationResponse{
			Page:       (offset / limit) + 1,
			Limit:      limit,
			TotalCount: total,
			TotalPages: totalPages,
		},
	}, nil
}

//go:align 64
func (h *Handlers) CreateFaction(ctx context.Context, req *api.CreateFactionReq) (*api.FactionResponse, error) {
	// Extract user ID from context (would be set by auth middleware)
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusUnauthorized,
			Response: api.ErrorResponse{
				Error:   "Unauthorized",
				Message: "User authentication required",
			},
		}
	}

	maxMembers := 1000
	if req.MaxMembers != nil {
		maxMembers = *req.MaxMembers
	}

	faction, err := h.service.CreateFaction(ctx, req.Name, req.Description, userID, maxMembers)
	if err != nil {
		h.logger.Error("Failed to create faction", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: err.Error(),
			},
		}
	}

	return &api.FactionResponse{
		Faction: h.convertFactionToAPI(faction),
	}, nil
}

//go:align 64
func (h *Handlers) GetFaction(ctx context.Context, req *api.GetFactionReq) (*api.FactionResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	faction, err := h.service.GetFaction(ctx, factionID)
	if err != nil {
		h.logger.Error("Failed to get faction", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusNotFound,
			Response: api.ErrorResponse{
				Error:   "NotFound",
				Message: "Faction not found",
			},
		}
	}

	return &api.FactionResponse{
		Faction: h.convertFactionToAPI(faction),
	}, nil
}

//go:align 64
func (h *Handlers) UpdateFaction(ctx context.Context, req *api.UpdateFactionReq) (*api.FactionResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	updates := make(map[string]interface{})
	if req.Description != nil {
		updates["description"] = *req.Description
	}
	if req.DiplomaticStance != nil {
		updates["diplomatic_stance"] = *req.DiplomaticStance
	}

	faction, err := h.service.UpdateFaction(ctx, factionID, updates)
	if err != nil {
		h.logger.Error("Failed to update faction", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: err.Error(),
			},
		}
	}

	return &api.FactionResponse{
		Faction: h.convertFactionToAPI(faction),
	}, nil
}

//go:align 64
func (h *Handlers) DisbandFaction(ctx context.Context, req *api.DisbandFactionReq) error {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	err = h.service.DisbandFaction(ctx, factionID, req.ConfirmationCode)
	if err != nil {
		h.logger.Error("Failed to disband faction", zap.Error(err))
		return &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: err.Error(),
			},
		}
	}

	return nil
}

//go:align 64
func (h *Handlers) GetFactionDiplomacy(ctx context.Context, req *api.GetFactionDiplomacyReq) (*api.FactionDiplomacyResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	relations, err := h.service.GetDiplomaticRelations(ctx, factionID)
	if err != nil {
		h.logger.Error("Failed to get diplomatic relations", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusInternalServerError,
			Response: api.ErrorResponse{
				Error:   "InternalServerError",
				Message: "Failed to retrieve diplomatic relations",
			},
		}
	}

	apiRelations := make([]api.DiplomaticRelation, len(relations))
	for i, relation := range relations {
		apiRelations[i] = api.DiplomaticRelation{
			TargetFactionId:   relation.TargetFactionID.String(),
			TargetFactionName: &relation.TargetFactionName,
			Status:            relation.Status,
			Standing:          relation.Standing,
			EstablishedAt:     relation.EstablishedAt,
			LastActionAt:      relation.LastActionAt,
		}
	}

	return &api.FactionDiplomacyResponse{
		Diplomacy: apiRelations,
	}, nil
}

//go:align 64
func (h *Handlers) InitiateDiplomaticAction(ctx context.Context, req *api.InitiateDiplomaticActionReq) (*api.DiplomaticActionResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	targetFactionID, err := uuid.Parse(req.TargetFactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid target faction ID format",
			},
		}
	}

	message := ""
	if req.Message != nil {
		message = *req.Message
	}

	treatyTerms := ""
	if req.TreatyTerms != nil {
		treatyTerms = *req.TreatyTerms
	}

	action, err := h.service.InitiateDiplomaticAction(ctx, factionID, targetFactionID, req.ActionType, message, treatyTerms)
	if err != nil {
		h.logger.Error("Failed to initiate diplomatic action", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: err.Error(),
			},
		}
	}

	return &api.DiplomaticActionResponse{
		ActionId:              action.ActionID.String(),
		Status:                action.Status,
		TargetFactionResponseDeadline: action.ResponseDeadline,
		CreatedAt:             action.CreatedAt,
	}, nil
}

//go:align 64
func (h *Handlers) GetFactionTerritory(ctx context.Context, req *api.GetFactionTerritoryReq) (*api.FactionTerritoryResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	territories, err := h.service.GetFactionTerritories(ctx, factionID)
	if err != nil {
		h.logger.Error("Failed to get faction territories", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusInternalServerError,
			Response: api.ErrorResponse{
				Error:   "InternalServerError",
				Message: "Failed to retrieve territories",
			},
		}
	}

	apiTerritories := make([]api.Territory, len(territories))
	for i, territory := range territories {
		apiTerritories[i] = api.Territory{
			TerritoryId:   territory.TerritoryID.String(),
			Name:          &territory.Name,
			Boundaries:    territory.Boundaries,
			ControlLevel:  territory.ControlLevel,
			ClaimedAt:     territory.ClaimedAt,
			LastConflictAt: territory.LastConflictAt,
		}
	}

	return &api.FactionTerritoryResponse{
		Territories: apiTerritories,
	}, nil
}

//go:align 64
func (h *Handlers) ClaimTerritory(ctx context.Context, req *api.ClaimTerritoryReq) (*api.TerritoryClaimResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	claimType := "expansion"
	if req.ClaimType != nil {
		claimType = *req.ClaimType
	}

	justification := ""
	if req.Justification != nil {
		justification = *req.Justification
	}

	claim, err := h.service.ClaimTerritory(ctx, factionID, req.TerritoryCoordinates.CenterX, req.TerritoryCoordinates.CenterY, req.TerritoryCoordinates.Radius, claimType, justification)
	if err != nil {
		h.logger.Error("Failed to claim territory", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: err.Error(),
			},
		}
	}

	return &api.TerritoryClaimResponse{
		ClaimId:         claim.ClaimID.String(),
		Status:          claim.Status,
		TerritoryId:     &claim.ClaimID, // Would be set after approval
		EstablishedAt:   claim.EstablishedAt,
		DisputePeriodDays: claim.DisputePeriod,
	}, nil
}

//go:align 64
func (h *Handlers) GetFactionReputation(ctx context.Context, req *api.GetFactionReputationReq) (*api.FactionReputationResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	reputation, err := h.service.GetFaction(ctx, factionID)
	if err != nil {
		h.logger.Error("Failed to get faction reputation", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusNotFound,
			Response: api.ErrorResponse{
				Error:   "NotFound",
				Message: "Faction not found",
			},
		}
	}

	// Get reputation history (simplified)
	history := []api.ReputationEvent{}

	return &api.FactionReputationResponse{
		Reputation:       reputation.Reputation,
		InfluenceMetrics: &api.InfluenceMetrics{
			GlobalInfluence:   &reputation.Influence,
			RegionalInfluence: &reputation.Influence,
			DiplomaticPower:    &reputation.Reputation,
			EconomicInfluence:  &reputation.Influence,
		},
		ReputationHistory: &history,
	}, nil
}

//go:align 64
func (h *Handlers) AdjustFactionReputation(ctx context.Context, req *api.AdjustFactionReputationReq) (*api.FactionReputationResponse, error) {
	factionID, err := uuid.Parse(req.FactionId)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: "Invalid faction ID format",
			},
		}
	}

	reason := ""
	if req.Reason != nil {
		reason = *req.Reason
	}

	err = h.service.AdjustFactionReputation(ctx, factionID, req.AdjustmentType, req.Value, reason)
	if err != nil {
		h.logger.Error("Failed to adjust faction reputation", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrorResponse{
				Error:   "BadRequest",
				Message: err.Error(),
			},
		}
	}

	// Return updated reputation
	return h.GetFactionReputation(ctx, &api.GetFactionReputationReq{FactionId: req.FactionId})
}

//go:align 64
func (h *Handlers) GetFactionRankings(ctx context.Context, req *api.GetFactionRankingsReq) (*api.FactionRankingsResponse, error) {
	limit := 50
	offset := 0

	if req.Limit != nil && *req.Limit > 0 && *req.Limit <= 100 {
		limit = *req.Limit
	}

	if req.Page != nil && *req.Page > 0 {
		offset = (*req.Page - 1) * limit
	}

	category := "reputation"
	if req.Category != nil {
		category = *req.Category
	}

	factions, err := h.service.GetFactionRankings(ctx, category, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get faction rankings", zap.Error(err))
		return nil, &api.ErrorStatusCode{
			StatusCode: http.StatusInternalServerError,
			Response: api.ErrorResponse{
				Error:   "InternalServerError",
				Message: "Failed to retrieve rankings",
			},
		}
	}

	rankings := make([]api.FactionRanking, len(factions))
	for i, faction := range factions {
		rankings[i] = api.FactionRanking{
			FactionId: faction.FactionID.String(),
			Name:      faction.Name,
			Rank:      i + offset + 1,
			Score:     faction.Reputation, // Simplified, would vary by category
			Reputation: &faction.Reputation,
		}
		if faction.Influence > 0 {
			rankings[i].Influence = &faction.Influence
		}
	}

	totalCount := len(factions) // Simplified

	return &api.FactionRankingsResponse{
		Rankings: rankings,
		Pagination: api.PaginationResponse{
			Page:       (offset / limit) + 1,
			Limit:      limit,
			TotalCount: totalCount,
			TotalPages: (totalCount + limit - 1) / limit,
		},
		Category: &category,
	}, nil
}

//go:align 64
func (h *Handlers) convertFactionToAPI(faction *models.Faction) api.Faction {
	return api.Faction{
		FactionId:        faction.FactionID.String(),
		Name:             faction.Name,
		Description:      &faction.Description,
		LeaderId:         faction.LeaderID.String(),
		Reputation:       faction.Reputation,
		Influence:        faction.Influence,
		DiplomaticStance: faction.DiplomaticStance,
		MemberCount:      faction.MemberCount,
		MaxMembers:       faction.MaxMembers,
		ActivityStatus:   faction.ActivityStatus,
		Requirements: api.FactionRequirements{
			MinReputation:      &faction.Requirements.MinReputation,
			MinInfluence:       &faction.Requirements.MinInfluence,
			ApplicationRequired: &faction.Requirements.ApplicationReq,
			ApprovalRequired:    &faction.Requirements.ApprovalReq,
			MinMemberLevel:      &faction.Requirements.MinMemberLevel,
		},
		Statistics: api.FactionStatistics{
			WarsDeclared:      &faction.Statistics.WarsDeclared,
			WarsWon:           &faction.Statistics.WarsWon,
			AlliancesFormed:   &faction.Statistics.AlliancesFormed,
			TerritoriesClaimed: &faction.Statistics.TerritoriesClaimed,
			InfluenceGained:   &faction.Statistics.InfluenceGained,
			AverageMemberReputation: &faction.Statistics.AvgMemberRep,
			LastDiplomaticAction:   faction.Statistics.LastDiplomaticAct,
		},
		CreatedAt: faction.CreatedAt,
		UpdatedAt: faction.UpdatedAt,
	}
}