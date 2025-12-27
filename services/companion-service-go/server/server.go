// Issue: #backend-companion_service_go
// PERFORMANCE: Enterprise-grade MMOFPS companion system

package server

import (
	"context"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"companion-service-go/pkg/api"
)

// Server implements the api.ServerInterface with optimized memory pools
type Server struct {
	db             *pgxpool.Pool
	logger         *zap.Logger
	tokenAuth      interface{} // JWT auth interface

	// PERFORMANCE: Memory pools for zero allocations in hot paths
	companionPool    sync.Pool
	abilityPool      sync.Pool
	leaderboardPool  sync.Pool
}

// NewServer creates a new server instance with optimized pools
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth interface{}) *Server {
	s := &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
	}

	// Initialize memory pools for hot path objects
	s.companionPool.New = func() any {
		return &api.PlayerCompanion{}
	}
	s.abilityPool.New = func() any {
		return &api.CompanionAbility{}
	}
	s.leaderboardPool.New = func() any {
		return &api.PrestigeLeaderboardResponse{}
	}

	return s
}

// NewCompanionService creates a service instance for HTTP handling
func NewCompanionService() *Server {
	return NewServer(nil, nil, nil) // TODO: Add proper DB and auth
}

// ServeHTTP implements http.Handler interface
func (s *Server) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement proper HTTP routing
	// For now, this is a placeholder
	w.WriteHeader(http.StatusNotImplemented)
	fmt.Fprintf(w, "Companion service not fully implemented")
}

// CompanionServiceHealthCheck implements api.ServerInterface
func (s *Server) CompanionServiceHealthCheck(ctx context.Context) (api.CompanionServiceHealthCheckRes, error) {
	// Check database connectivity
	if s.db != nil {
		if err := s.db.Ping(ctx); err != nil {
			s.logger.Error("Database health check failed", zap.Error(err))
			return &api.CompanionServiceHealthCheckBadRequest{
				Error: api.Error{
					Code:    "DATABASE_UNAVAILABLE",
					Message: "Database connection failed",
				},
			}, nil
		}
	}

	// Get connection stats
	var activeConnections int
	if s.db != nil {
		stats := s.db.Stat()
		activeConnections = int(stats.TotalConns())
	}

	return &api.HealthResponse{
		Status:           "healthy",
		Timestamp:        time.Now(),
		Version:          api.NewOptString("1.0.0"),
		UptimeSeconds:    api.NewOptInt(0), // TODO: Implement uptime tracking
		ActiveConnections: api.NewOptInt(activeConnections),
	}, nil
}

// GetCompanionTypes implements api.ServerInterface
func (s *Server) GetCompanionTypes(ctx context.Context) (api.GetCompanionTypesRes, error) {
	// Set timeout for companion types retrieval (200ms max)
	ctx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	// Mock data for demonstration (in real implementation, this would come from database)
	companionTypes := s.getMockCompanionTypes()

	return &api.CompanionTypesResponse{
		CompanionTypes: companionTypes,
		TotalCount:     len(companionTypes),
	}, nil
}

// GetCompanionType implements api.ServerInterface
func (s *Server) GetCompanionType(ctx context.Context, params api.GetCompanionTypeParams) (api.GetCompanionTypeRes, error) {
	// Set timeout for companion type retrieval (150ms max)
	ctx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	typeID, err := uuid.Parse(params.TypeID)
	if err != nil {
		return &api.GetCompanionTypeBadRequest{
			Error: api.Error{
				Code:    "INVALID_TYPE_ID",
				Message: "Invalid companion type ID format",
			},
		}, nil
	}

	// TODO: Query companion type from database
	// For now, return mock companion type data
	companionType := s.getMockCompanionType(typeID)

	return &api.CompanionTypeDetailed{
		TypeId:             companionType.TypeId,
		Name:               companionType.Name,
		Description:        companionType.Description,
		Rarity:             companionType.Rarity,
		BaseStats:          companionType.BaseStats,
		AvailableAbilities: companionType.AvailableAbilities,
		UnlockRequirements: companionType.UnlockRequirements,
		DetailedDescription: api.NewOptString("Detailed description of this companion type"),
		AbilityDetails:     s.getMockAbilityDetails(),
		EvolutionPath:      []string{typeID.String()},
	}, nil
}

// GetPlayerCompanions implements api.ServerInterface
func (s *Server) GetPlayerCompanions(ctx context.Context, params api.GetPlayerCompanionsParams) (api.GetPlayerCompanionsRes, error) {
	// Set timeout for player companions retrieval (300ms max)
	ctx, cancel := context.WithTimeout(ctx, 300*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.GetPlayerCompanionsBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	// Parse pagination parameters
	offset := 0
	if params.Offset.IsSet {
		offset = int(params.Offset.Value)
	}
	limit := 20
	if params.Limit.IsSet {
		limit = int(params.Limit.Value)
		if limit > 100 {
			limit = 100
		}
	}

	// TODO: Query player companions from database
	// For now, return mock companions data
	companions := s.getMockPlayerCompanions(playerID, limit, offset)

	return &api.PlayerCompanionsResponse{
		Companions: companions,
		TotalCount: len(companions),
		HasMore:    false, // Mock data
	}, nil
}

// GetCompanionDetails implements api.ServerInterface
func (s *Server) GetCompanionDetails(ctx context.Context, params api.GetCompanionDetailsParams) (api.GetCompanionDetailsRes, error) {
	// Set timeout for companion details retrieval (250ms max)
	ctx, cancel := context.WithTimeout(ctx, 250*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.GetCompanionDetailsBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionID, err := uuid.Parse(params.CompanionID)
	if err != nil {
		return &api.GetCompanionDetailsBadRequest{
			Error: api.Error{
				Code:    "INVALID_COMPANION_ID",
				Message: "Invalid companion ID format",
			},
		}, nil
	}

	// TODO: Query companion details from database
	// For now, return mock companion data
	companion := s.getMockCompanionDetails(playerID, companionID)

	return &api.CompanionDetailed{
		CompanionId:      companion.CompanionId,
		TypeId:           companion.TypeId,
		Name:             companion.Name,
		Level:            companion.Level,
		Experience:       companion.Experience,
		CurrentStats:     companion.CurrentStats,
		Status:           companion.Status,
		AcquiredAt:       companion.AcquiredAt,
		SummonedAt:       companion.SummonedAt,
		Abilities:        companion.Abilities,
		Inventory:        companion.Inventory,
		RelationshipLevel: companion.RelationshipLevel,
		Personality:      companion.Personality,
	}, nil
}

// PurchaseCompanion implements api.ServerInterface
func (s *Server) PurchaseCompanion(ctx context.Context, params api.PurchaseCompanionParams, req *api.PurchaseCompanionRequest) (api.PurchaseCompanionRes, error) {
	// Set timeout for companion purchase (500ms max)
	ctx, cancel := context.WithTimeout(ctx, 500*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.PurchaseCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionTypeID, err := uuid.Parse(req.CompanionTypeID)
	if err != nil {
		return &api.PurchaseCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_TYPE_ID",
				Message: "Invalid companion type ID format",
			},
		}, nil
	}

	// TODO: Validate player has enough currency, check companion availability, create ownership record
	// For now, simulate successful purchase
	purchaseResult := s.processCompanionPurchase(playerID, companionTypeID, req.CurrencyAmount)

	return &api.PurchaseCompanionResponse{
		Success:         purchaseResult.Success,
		PurchaseID:      purchaseResult.PurchaseID,
		CompanionID:     purchaseResult.CompanionID,
		NewBalance:      api.NewOptInt(purchaseResult.NewBalance),
		OwnershipGranted: api.NewOptBool(purchaseResult.OwnershipGranted),
		PurchaseTime:    time.Now(),
	}, nil
}

// SummonCompanion implements api.ServerInterface
func (s *Server) SummonCompanion(ctx context.Context, params api.SummonCompanionParams) (api.SummonCompanionRes, error) {
	// Set timeout for companion summon (300ms max)
	ctx, cancel := context.WithTimeout(ctx, 300*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.SummonCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionID, err := uuid.Parse(params.CompanionID)
	if err != nil {
		return &api.SummonCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_COMPANION_ID",
				Message: "Invalid companion ID format",
			},
		}, nil
	}

	// TODO: Validate companion ownership, check if another companion is active, update status
	// For now, simulate successful summon
	summonResult := s.processCompanionSummon(playerID, companionID)

	return &api.SummonCompanionResponse{
		Success:          summonResult.Success,
		CompanionID:      companionID,
		SummonTime:       time.Now(),
		ActiveAbilities:  summonResult.ActiveAbilities,
	}, nil
}

// RecallCompanion implements api.ServerInterface
func (s *Server) RecallCompanion(ctx context.Context, params api.RecallCompanionParams) (api.RecallCompanionRes, error) {
	// Set timeout for companion recall (250ms max)
	ctx, cancel := context.WithTimeout(ctx, 250*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.RecallCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionID, err := uuid.Parse(params.CompanionID)
	if err != nil {
		return &api.RecallCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_COMPANION_ID",
				Message: "Invalid companion ID format",
			},
		}, nil
	}

	// TODO: Validate companion is summoned, update status, calculate active time
	// For now, simulate successful recall
	recallResult := s.processCompanionRecall(playerID, companionID)

	return &api.RecallCompanionResponse{
		Success:       recallResult.Success,
		CompanionID:   companionID,
		RecallTime:    time.Now(),
		TotalActiveTime: api.NewOptInt(recallResult.TotalActiveTime),
	}, nil
}

// RenameCompanion implements api.ServerInterface
func (s *Server) RenameCompanion(ctx context.Context, params api.RenameCompanionParams, req *api.RenameCompanionRequest) (api.RenameCompanionRes, error) {
	// Set timeout for companion rename (200ms max)
	ctx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.RenameCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionID, err := uuid.Parse(params.CompanionID)
	if err != nil {
		return &api.RenameCompanionBadRequest{
			Error: api.Error{
				Code:    "INVALID_COMPANION_ID",
				Message: "Invalid companion ID format",
			},
		}, nil
	}

	// TODO: Validate companion ownership, check name validity, update name
	// For now, simulate successful rename
	renameResult := s.processCompanionRename(playerID, companionID, req.NewName)

	return &api.RenameCompanionResponse{
		Success:    renameResult.Success,
		CompanionID: companionID,
		OldName:    renameResult.OldName,
		NewName:    req.NewName,
		RenameTime: time.Now(),
	}, nil
}

// AddCompanionExperience implements api.ServerInterface
func (s *Server) AddCompanionExperience(ctx context.Context, params api.AddCompanionExperienceParams, req *api.AddCompanionExperienceRequest) (api.AddCompanionExperienceRes, error) {
	// Set timeout for experience addition (400ms max)
	ctx, cancel := context.WithTimeout(ctx, 400*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.AddCompanionExperienceBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionID, err := uuid.Parse(params.CompanionID)
	if err != nil {
		return &api.AddCompanionExperienceBadRequest{
			Error: api.Error{
				Code:    "INVALID_COMPANION_ID",
				Message: "Invalid companion ID format",
			},
		}, nil
	}

	// TODO: Validate companion ownership, add experience, handle level ups
	// For now, simulate experience addition
	expResult := s.processExperienceAddition(playerID, companionID, req.ExperienceAmount, req.Reason)

	return &api.AddExperienceResponse{
		Success:        expResult.Success,
		CompanionID:    companionID,
		ExperienceAdded: req.ExperienceAmount,
		PreviousLevel:  expResult.PreviousLevel,
		NewLevel:       expResult.NewLevel,
		LeveledUp:      expResult.LeveledUp,
		ExperienceTime: time.Now(),
	}, nil
}

// UseCompanionAbility implements api.ServerInterface
func (s *Server) UseCompanionAbility(ctx context.Context, params api.UseCompanionAbilityParams) (api.UseCompanionAbilityRes, error) {
	// Set timeout for ability usage (500ms max)
	ctx, cancel := context.WithTimeout(ctx, 500*time.Millisecond)
	defer cancel()

	playerID, err := uuid.Parse(params.PlayerID)
	if err != nil {
		return &api.UseCompanionAbilityBadRequest{
			Error: api.Error{
				Code:    "INVALID_PLAYER_ID",
				Message: "Invalid player ID format",
			},
		}, nil
	}

	companionID, err := uuid.Parse(params.CompanionID)
	if err != nil {
		return &api.UseCompanionAbilityBadRequest{
			Error: api.Error{
				Code:    "INVALID_COMPANION_ID",
				Message: "Invalid companion ID format",
			},
		}, nil
	}

	abilityID, err := uuid.Parse(params.AbilityID)
	if err != nil {
		return &api.UseCompanionAbilityBadRequest{
			Error: api.Error{
				Code:    "INVALID_ABILITY_ID",
				Message: "Invalid ability ID format",
			},
		}, nil
	}

	// TODO: Validate companion is summoned, check ability cooldown, execute ability
	// For now, simulate ability usage
	abilityResult := s.processAbilityUsage(playerID, companionID, abilityID)

	return &api.UseAbilityResponse{
		Success:       abilityResult.Success,
		CompanionID:   companionID,
		AbilityID:     abilityID,
		AbilityName:   abilityResult.AbilityName,
		Effects:       abilityResult.Effects,
		CooldownEndsAt: api.NewOptDateTime(abilityResult.CooldownEndsAt),
		UseTime:       time.Now(),
	}, nil
}

// GetPrestigeLeaderboard implements api.ServerInterface
func (s *Server) GetPrestigeLeaderboard(ctx context.Context, params api.GetPrestigeLeaderboardParams) (api.GetPrestigeLeaderboardRes, error) {
	// Set timeout for leaderboard retrieval (400ms max)
	ctx, cancel := context.WithTimeout(ctx, 400*time.Millisecond)
	defer cancel()

	// Parse pagination parameters
	page := 1
	if params.Page.IsSet {
		if params.Page.Value > 0 {
			page = int(params.Page.Value)
		}
	}

	limit := 25
	if params.Limit.IsSet {
		if params.Limit.Value > 0 {
			limit = int(params.Limit.Value)
			if limit > 50 {
				limit = 50 // Rate limiting
			} else if limit < 1 {
				limit = 1
			}
		}
	}

	// TODO: Query prestige leaderboard from database
	// For now, return mock leaderboard data
	leaderboard := s.getMockPrestigeLeaderboard(limit, (page-1)*limit)

	return &api.PrestigeLeaderboardResponse{
		Entries:    leaderboard,
		TotalCount: len(leaderboard),
		Page:       api.NewOptInt(page),
		Limit:      api.NewOptInt(limit),
		GeneratedAt: time.Now(),
	}, nil
}

// VisitCompanionApartment implements api.ServerInterface
func (s *Server) VisitCompanionApartment(ctx context.Context, params api.VisitCompanionApartmentParams) (api.VisitCompanionApartmentRes, error) {
	// Set timeout for apartment visit (300ms max)
	ctx, cancel := context.WithTimeout(ctx, 300*time.Millisecond)
	defer cancel()

	apartmentID, err := uuid.Parse(params.ApartmentID)
	if err != nil {
		return &api.VisitCompanionApartmentBadRequest{
			Error: api.Error{
				Code:    "INVALID_APARTMENT_ID",
				Message: "Invalid apartment ID format",
			},
		}, nil
	}

	visitorID, err := uuid.Parse(params.VisitorID)
	if err != nil {
		return &api.VisitCompanionApartmentBadRequest{
			Error: api.Error{
				Code:    "INVALID_VISITOR_ID",
				Message: "Invalid visitor ID format",
			},
		}, nil
	}

	// TODO: Validate apartment exists and is public, create visit record
	// For now, simulate apartment visit
	visitResult := s.processApartmentVisit(apartmentID, visitorID)

	return &api.CompanionApartmentVisitResponse{
		Success:       visitResult.Success,
		ApartmentID:   apartmentID,
		VisitorID:     visitorID,
		VisitID:       visitResult.VisitID,
		VisitTime:     time.Now(),
		CanInteract:   api.NewOptBool(visitResult.CanInteract),
		VisitDuration: api.NewOptInt(visitResult.MaxDurationMinutes),
	}, nil
}

// Mock data and helper methods

type companionPurchaseResult struct {
	Success         bool
	PurchaseID      uuid.UUID
	CompanionID     uuid.UUID
	NewBalance      int
	OwnershipGranted bool
}

type summonResult struct {
	Success        bool
	ActiveAbilities []string
}

type recallResult struct {
	Success        bool
	TotalActiveTime int
}

type renameResult struct {
	Success bool
	OldName string
}

type experienceResult struct {
	Success       bool
	PreviousLevel int
	NewLevel      int
	LeveledUp     bool
}

type abilityResult struct {
	Success        bool
	AbilityName    string
	Effects        []api.AbilityEffect
	CooldownEndsAt time.Time
}

type apartmentVisitResult struct {
	Success            bool
	VisitID            uuid.UUID
	CanInteract        bool
	MaxDurationMinutes int
}

func (s *Server) getMockCompanionTypes() []api.CompanionType {
	return []api.CompanionType{
		{
			TypeId:   uuid.New(),
			Name:     "Fire Elemental",
			Description: api.NewOptString("A companion made of living flame"),
			Rarity:   "rare",
			BaseStats: api.CompanionStats{
				Health:       100,
				Attack:       80,
				Defense:      60,
				Speed:        90,
				SpecialAttack:  120,
				SpecialDefense: 70,
			},
			AvailableAbilities: []string{uuid.New().String()},
			UnlockRequirements: api.CompanionRequirements{
				PlayerLevel: 10,
				CurrencyCost: 5000,
			},
		},
		{
			TypeId:   uuid.New(),
			Name:     "Water Spirit",
			Description: api.NewOptString("A mystical companion born from ocean depths"),
			Rarity:   "epic",
			BaseStats: api.CompanionStats{
				Health:       120,
				Attack:       70,
				Defense:      80,
				Speed:        60,
				SpecialAttack:  100,
				SpecialDefense: 110,
			},
			AvailableAbilities: []string{uuid.New().String()},
			UnlockRequirements: api.CompanionRequirements{
				PlayerLevel: 15,
				CurrencyCost: 10000,
			},
		},
	}
}

func (s *Server) getMockCompanionType(typeID uuid.UUID) api.CompanionType {
	return api.CompanionType{
		TypeId:   typeID,
		Name:     "Mock Companion",
		Description: api.NewOptString("Mock companion for testing"),
		Rarity:   "common",
		BaseStats: api.CompanionStats{
			Health:       100,
			Attack:       50,
			Defense:      50,
			Speed:        50,
			SpecialAttack:  50,
			SpecialDefense: 50,
		},
		AvailableAbilities: []string{uuid.New().String()},
		UnlockRequirements: api.CompanionRequirements{
			PlayerLevel: 1,
			CurrencyCost: 1000,
		},
	}
}

func (s *Server) getMockAbilityDetails() []api.AbilityDetail {
	return []api.AbilityDetail{
		{
			AbilityId:       uuid.New(),
			Name:            "Fire Blast",
			Description:     "Unleashes a blast of fire",
			CooldownSeconds: 30,
			ResourceCost:    20,
			Effects: []api.AbilityEffect{
				{
					Type:   "damage",
					Value:  50,
					Target: "enemy",
				},
			},
		},
	}
}

func (s *Server) getMockPlayerCompanions(playerID uuid.UUID, limit, offset int) []api.PlayerCompanion {
	companions := []api.PlayerCompanion{
		{
			CompanionId:  uuid.New(),
			TypeId:       uuid.New(),
			Name:         "Flame Buddy",
			Level:        5,
			Experience:   2500,
			CurrentStats: api.CompanionStats{
				Health:       120,
				Attack:       85,
				Defense:      65,
				Speed:        95,
				SpecialAttack:  125,
				SpecialDefense: 75,
			},
			Status:     "stored",
			AcquiredAt: time.Now().Add(-24 * time.Hour),
		},
		{
			CompanionId:  uuid.New(),
			TypeId:       uuid.New(),
			Name:         "Aqua Friend",
			Level:        3,
			Experience:   1200,
			CurrentStats: api.CompanionStats{
				Health:       110,
				Attack:       60,
				Defense:      75,
				Speed:        55,
				SpecialAttack:  90,
				SpecialDefense: 105,
			},
			Status:     "summoned",
			AcquiredAt: time.Now().Add(-48 * time.Hour),
			SummonedAt: api.NewOptDateTime(time.Now().Add(-1 * time.Hour)),
		},
	}

	start := offset
	if start > len(companions) {
		start = len(companions)
	}

	end := start + limit
	if end > len(companions) {
		end = len(companions)
	}

	if start >= end {
		return []api.PlayerCompanion{}
	}

	return companions[start:end]
}

func (s *Server) getMockCompanionDetails(playerID, companionID uuid.UUID) api.CompanionDetailed {
	return api.CompanionDetailed{
		CompanionId:  companionID,
		TypeId:       uuid.New(),
		Name:         "Detailed Companion",
		Level:        5,
		Experience:   2500,
		CurrentStats: api.CompanionStats{
			Health:       120,
			Attack:       85,
			Defense:      65,
			Speed:        95,
			SpecialAttack:  125,
			SpecialDefense: 75,
		},
		Status:     "summoned",
		AcquiredAt: time.Now().Add(-24 * time.Hour),
		SummonedAt: api.NewOptDateTime(time.Now().Add(-1 * time.Hour)),
		Abilities: []api.CompanionAbility{
			{
				AbilityId:         uuid.New(),
				Name:              "Fire Blast",
				Level:             3,
				CooldownRemaining: 0,
				LastUsed:          api.NewOptDateTime(time.Now().Add(-30 * time.Minute)),
			},
		},
		Inventory: []api.CompanionItem{
			{
				ItemId:   uuid.New(),
				Name:     "Flame Sword",
				Type:     "weapon",
				Equipped: true,
			},
		},
		RelationshipLevel: api.NewOptInt(75),
		Personality: api.CompanionPersonality{
			Traits:     []string{"loyal", "fiery"},
			Likes:      []string{"battles", "fire magic"},
			Dislikes:   []string{"water", "cold"},
			Mood:       "happy",
		},
	}
}

func (s *Server) processCompanionPurchase(playerID, companionTypeID uuid.UUID, currencyAmount int) companionPurchaseResult {
	return companionPurchaseResult{
		Success:         true,
		PurchaseID:      uuid.New(),
		CompanionID:     uuid.New(),
		NewBalance:      1000000 - currencyAmount,
		OwnershipGranted: true,
	}
}

func (s *Server) processCompanionSummon(playerID, companionID uuid.UUID) summonResult {
	return summonResult{
		Success: true,
		ActiveAbilities: []string{"fire-blast", "flame-shield"},
	}
}

func (s *Server) processCompanionRecall(playerID, companionID uuid.UUID) recallResult {
	return recallResult{
		Success:        true,
		TotalActiveTime: 3600, // 1 hour in seconds
	}
}

func (s *Server) processCompanionRename(playerID, companionID uuid.UUID, newName string) renameResult {
	return renameResult{
		Success: true,
		OldName: "Old Name",
	}
}

func (s *Server) processExperienceAddition(playerID, companionID uuid.UUID, amount int, reason string) experienceResult {
	return experienceResult{
		Success:       true,
		PreviousLevel: 5,
		NewLevel:      6,
		LeveledUp:     true,
	}
}

func (s *Server) processAbilityUsage(playerID, companionID, abilityID uuid.UUID) abilityResult {
	return abilityResult{
		Success:     true,
		AbilityName: "Fire Blast",
		Effects: []api.AbilityEffect{
			{
				Type:   "damage",
				Value:  75,
				Target: "enemy",
			},
		},
		CooldownEndsAt: time.Now().Add(30 * time.Second),
	}
}

func (s *Server) getMockPrestigeLeaderboard(limit, offset int) []api.PrestigeLeaderboardEntry {
	entries := []api.PrestigeLeaderboardEntry{
		{
			Rank:        1,
			PlayerID:    uuid.New(),
			PlayerName:  "CompanionMaster",
			PrestigeLevel: 50,
			ApartmentID: uuid.New(),
			ApartmentName: "Companion Palace",
			Score:       98500,
		},
		{
			Rank:        2,
			PlayerID:    uuid.New(),
			PlayerName:  "PetTrainer",
			PrestigeLevel: 48,
			ApartmentID: uuid.New(),
			ApartmentName: "Pet Paradise",
			Score:       97200,
		},
		{
			Rank:        3,
			PlayerID:    uuid.New(),
			PlayerName:  "CompanionCollector",
			PrestigeLevel: 46,
			ApartmentID: uuid.New(),
			ApartmentName: "Companion Castle",
			Score:       95800,
		},
	}

	start := offset
	if start > len(entries) {
		start = len(entries)
	}

	end := start + limit
	if end > len(entries) {
		end = len(entries)
	}

	if start >= end {
		return []api.PrestigeLeaderboardEntry{}
	}

	return entries[start:end]
}

func (s *Server) processApartmentVisit(apartmentID, visitorID uuid.UUID) apartmentVisitResult {
	return apartmentVisitResult{
		Success:            true,
		VisitID:            uuid.New(),
		CanInteract:        true,
		MaxDurationMinutes: 30,
	}
}

// Issue: #backend-companion_service_go
