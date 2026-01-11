//go:align 64
package service

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"

	"necpgame/services/faction-service-go/internal/models"
	"necpgame/services/faction-service-go/internal/repository"
)

//go:align 64
type Config struct {
	MaxFactionNameLength    int           `yaml:"max_faction_name_length"`
	MaxFactionDescription   int           `yaml:"max_faction_description"`
	DefaultMaxMembers       int           `yaml:"default_max_members"`
	DiplomaticActionTimeout time.Duration `yaml:"diplomatic_action_timeout"`
	TerritoryDisputePeriod  time.Duration `yaml:"territory_dispute_period"`
	ReputationDecayRate     float64       `yaml:"reputation_decay_rate"`
}

//go:align 64
type Service struct {
	repo         repository.Repository
	logger       *zap.Logger
	config       Config
	factionPool  *sync.Pool
	diplomacyPool *sync.Pool
	territoryPool *sync.Pool

	// Metrics
	factionCreations    prometheus.Counter
	factionOperations   *prometheus.HistogramVec
	diplomacyActions    *prometheus.HistogramVec
	territoryClaims     prometheus.Counter
	reputationChanges   prometheus.Counter
	activeFactions      prometheus.Gauge
	activeDiplomacy     prometheus.Gauge
}

//go:align 64
func NewService(repo repository.Repository, logger *zap.Logger, config Config) (*Service, error) {
	// Initialize object pools for memory optimization
	factionPool := &sync.Pool{
		New: func() interface{} {
			return &models.Faction{}
		},
	}

	diplomacyPool := &sync.Pool{
		New: func() interface{} {
			return &models.DiplomaticRelation{}
		},
	}

	territoryPool := &sync.Pool{
		New: func() interface{} {
			return &models.Territory{}
		},
	}

	// Initialize metrics
	factionCreations := promauto.NewCounter(prometheus.CounterOpts{
		Name: "faction_creations_total",
		Help: "Total number of faction creations",
	})

	factionOperations := promauto.NewHistogramVec(prometheus.HistogramOpts{
		Name:    "faction_operations_duration_seconds",
		Help:    "Duration of faction operations",
		Buckets: prometheus.DefBuckets,
	}, []string{"operation"})

	diplomacyActions := promauto.NewHistogramVec(prometheus.HistogramOpts{
		Name:    "diplomacy_actions_duration_seconds",
		Help:    "Duration of diplomacy actions",
		Buckets: prometheus.DefBuckets,
	}, []string{"action_type"})

	territoryClaims := promauto.NewCounter(prometheus.CounterOpts{
		Name: "territory_claims_total",
		Help: "Total number of territory claims",
	})

	reputationChanges := promauto.NewCounter(prometheus.CounterOpts{
		Name: "reputation_changes_total",
		Help: "Total number of reputation changes",
	})

	activeFactions := promauto.NewGauge(prometheus.GaugeOpts{
		Name: "active_factions",
		Help: "Number of active factions",
	})

	activeDiplomacy := promauto.NewGauge(prometheus.GaugeOpts{
		Name: "active_diplomacy_actions",
		Help: "Number of active diplomacy actions",
	})

	return &Service{
		repo:              repo,
		logger:            logger,
		config:            config,
		factionPool:       factionPool,
		diplomacyPool:     diplomacyPool,
		territoryPool:     territoryPool,
		factionCreations:  factionCreations,
		factionOperations: factionOperations,
		diplomacyActions:  diplomacyActions,
		territoryClaims:   territoryClaims,
		reputationChanges: reputationChanges,
		activeFactions:    activeFactions,
		activeDiplomacy:   activeDiplomacy,
	}, nil
}

//go:align 64
func (s *Service) CreateFaction(ctx context.Context, name, description string, leaderID uuid.UUID, maxMembers int) (*models.Faction, error) {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("create"))
	defer timer.ObserveDuration()

	// Validate input
	if len(name) < 3 || len(name) > s.config.MaxFactionNameLength {
		return nil, fmt.Errorf("faction name must be between 3 and %d characters", s.config.MaxFactionNameLength)
	}

	if len(description) > s.config.MaxFactionDescription {
		return nil, fmt.Errorf("faction description must be less than %d characters", s.config.MaxFactionDescription)
	}

	if maxMembers <= 0 {
		maxMembers = s.config.DefaultMaxMembers
	}

	// Check if faction name already exists
	existing, _, err := s.repo.ListFactions(ctx, 1, 0, map[string]interface{}{"name": name})
	if err != nil {
		s.logger.Error("Failed to check existing factions", zap.Error(err))
		return nil, fmt.Errorf("failed to validate faction name: %w", err)
	}

	if len(existing) > 0 {
		return nil, fmt.Errorf("faction name already exists")
	}

	// Create faction object
	faction := s.factionPool.Get().(*models.Faction)
	defer s.factionPool.Put(faction)

	faction.FactionID = uuid.New()
	faction.Name = name
	faction.Description = description
	faction.LeaderID = leaderID
	faction.Reputation = 0
	faction.Influence = 0
	faction.DiplomaticStance = "neutral"
	faction.MemberCount = 1
	faction.MaxMembers = maxMembers
	faction.ActivityStatus = "active"
	faction.Requirements = models.FactionRequirements{
		MinReputation:    -100,
		MinInfluence:     0,
		ApplicationReq:   false,
		ApprovalReq:      true,
		MinMemberLevel:   1,
	}
	faction.Statistics = models.FactionStatistics{
		WarsDeclared:      0,
		WarsWon:           0,
		AlliancesFormed:   0,
		TerritoriesClaimed: 0,
		InfluenceGained:   0,
		AvgMemberRep:      0,
	}
	faction.CreatedAt = time.Now()
	faction.UpdatedAt = time.Now()

	// Save to repository
	if err := s.repo.CreateFaction(ctx, faction); err != nil {
		s.logger.Error("Failed to create faction", zap.Error(err), zap.String("faction_id", faction.FactionID.String()))
		return nil, fmt.Errorf("failed to create faction: %w", err)
	}

	s.factionCreations.Inc()
	s.activeFactions.Inc()

	s.logger.Info("Faction created successfully",
		zap.String("faction_id", faction.FactionID.String()),
		zap.String("name", name),
		zap.String("leader_id", leaderID.String()))

	return faction, nil
}

//go:align 64
func (s *Service) GetFaction(ctx context.Context, factionID uuid.UUID) (*models.Faction, error) {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("get"))
	defer timer.ObserveDuration()

	faction, err := s.repo.GetFaction(ctx, factionID)
	if err != nil {
		s.logger.Error("Failed to get faction", zap.Error(err), zap.String("faction_id", factionID.String()))
		return nil, fmt.Errorf("failed to get faction: %w", err)
	}

	return faction, nil
}

//go:align 64
func (s *Service) UpdateFaction(ctx context.Context, factionID uuid.UUID, updates map[string]interface{}) (*models.Faction, error) {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("update"))
	defer timer.ObserveDuration()

	// Get current faction
	faction, err := s.repo.GetFaction(ctx, factionID)
	if err != nil {
		return nil, fmt.Errorf("failed to get faction for update: %w", err)
	}

	// Apply updates
	if name, ok := updates["name"].(string); ok {
		if len(name) < 3 || len(name) > s.config.MaxFactionNameLength {
			return nil, fmt.Errorf("invalid faction name length")
		}
		faction.Name = name
	}

	if description, ok := updates["description"].(string); ok {
		if len(description) > s.config.MaxFactionDescription {
			return nil, fmt.Errorf("description too long")
		}
		faction.Description = description
	}

	if stance, ok := updates["diplomatic_stance"].(string); ok {
		faction.DiplomaticStance = stance
	}

	faction.UpdatedAt = time.Now()

	// Save updates
	if err := s.repo.UpdateFaction(ctx, faction); err != nil {
		s.logger.Error("Failed to update faction", zap.Error(err), zap.String("faction_id", factionID.String()))
		return nil, fmt.Errorf("failed to update faction: %w", err)
	}

	return faction, nil
}

//go:align 64
func (s *Service) DisbandFaction(ctx context.Context, factionID uuid.UUID, confirmationCode string) error {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("disband"))
	defer timer.ObserveDuration()

	// Generate confirmation code (in real implementation, this would be more sophisticated)
	expectedCode := "CONFIRM_DISBAND_" + factionID.String()[:8]

	if confirmationCode != expectedCode {
		return fmt.Errorf("invalid confirmation code")
	}

	// Get faction to check leadership and clean up
	faction, err := s.repo.GetFaction(ctx, factionID)
	if err != nil {
		return fmt.Errorf("failed to get faction for disband: %w", err)
	}

	if faction.MemberCount > 1 {
		return fmt.Errorf("cannot disband faction with multiple members")
	}

	// Delete faction
	if err := s.repo.DeleteFaction(ctx, factionID); err != nil {
		s.logger.Error("Failed to disband faction", zap.Error(err), zap.String("faction_id", factionID.String()))
		return fmt.Errorf("failed to disband faction: %w", err)
	}

	s.activeFactions.Dec()

	s.logger.Info("Faction disbanded successfully", zap.String("faction_id", factionID.String()))
	return nil
}

//go:align 64
func (s *Service) ListFactions(ctx context.Context, limit, offset int, filters map[string]interface{}) ([]*models.Faction, int, error) {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("list"))
	defer timer.ObserveDuration()

	factions, total, err := s.repo.ListFactions(ctx, limit, offset, filters)
	if err != nil {
		s.logger.Error("Failed to list factions", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to list factions: %w", err)
	}

	return factions, total, nil
}

//go:align 64
func (s *Service) InitiateDiplomaticAction(ctx context.Context, factionID, targetFactionID uuid.UUID, actionType, message, treatyTerms string) (*models.DiplomaticAction, error) {
	timer := prometheus.NewTimer(s.diplomacyActions.WithLabelValues(actionType))
	defer timer.ObserveDuration()

	if factionID == targetFactionID {
		return nil, fmt.Errorf("cannot initiate diplomatic action with self")
	}

	// Validate action type
	validActions := map[string]bool{
		"declare_war": true, "propose_alliance": true, "offer_peace": true,
		"break_alliance": true, "propose_trade": true,
	}
	if !validActions[actionType] {
		return nil, fmt.Errorf("invalid diplomatic action type")
	}

	action := &models.DiplomaticAction{
		ActionID:         uuid.New(),
		FactionID:        factionID,
		ActionType:       actionType,
		TargetFactionID:  targetFactionID,
		Status:           "pending",
		Message:          message,
		TreatyTerms:      treatyTerms,
		CreatedAt:        time.Now(),
		ResponseDeadline: &time.Time{},
	}

	*action.ResponseDeadline = time.Now().Add(s.config.DiplomaticActionTimeout)

	if err := s.repo.CreateDiplomaticAction(ctx, action); err != nil {
		s.logger.Error("Failed to create diplomatic action", zap.Error(err))
		return nil, fmt.Errorf("failed to initiate diplomatic action: %w", err)
	}

	s.activeDiplomacy.Inc()

	s.logger.Info("Diplomatic action initiated",
		zap.String("action_id", action.ActionID.String()),
		zap.String("action_type", actionType),
		zap.String("faction_id", factionID.String()),
		zap.String("target_faction_id", targetFactionID.String()))

	return action, nil
}

//go:align 64
func (s *Service) GetDiplomaticRelations(ctx context.Context, factionID uuid.UUID) ([]*models.DiplomaticRelation, error) {
	relations, err := s.repo.GetDiplomaticRelations(ctx, factionID)
	if err != nil {
		s.logger.Error("Failed to get diplomatic relations", zap.Error(err), zap.String("faction_id", factionID.String()))
		return nil, fmt.Errorf("failed to get diplomatic relations: %w", err)
	}

	return relations, nil
}

//go:align 64
func (s *Service) ClaimTerritory(ctx context.Context, factionID uuid.UUID, centerX, centerY, radius float64, claimType, justification string) (*models.TerritoryClaim, error) {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("claim_territory"))
	defer timer.ObserveDuration()

	if radius < 10 || radius > 1000 {
		return nil, fmt.Errorf("territory radius must be between 10 and 1000")
	}

	claim := &models.TerritoryClaim{
		ClaimID:       uuid.New(),
		FactionID:     factionID,
		CenterX:       centerX,
		CenterY:       centerY,
		Radius:        radius,
		ClaimType:     claimType,
		Status:        "pending",
		Justification: justification,
		EstablishedAt: time.Now(),
		DisputePeriod: int(s.config.TerritoryDisputePeriod.Hours() / 24), // Convert to days
	}

	if err := s.repo.CreateTerritoryClaim(ctx, claim); err != nil {
		s.logger.Error("Failed to create territory claim", zap.Error(err))
		return nil, fmt.Errorf("failed to claim territory: %w", err)
	}

	s.territoryClaims.Inc()

	s.logger.Info("Territory claim initiated",
		zap.String("claim_id", claim.ClaimID.String()),
		zap.String("faction_id", factionID.String()),
		zap.Float64("center_x", centerX),
		zap.Float64("center_y", centerY),
		zap.Float64("radius", radius))

	return claim, nil
}

//go:align 64
func (s *Service) GetFactionTerritories(ctx context.Context, factionID uuid.UUID) ([]*models.Territory, error) {
	territories, err := s.repo.GetFactionTerritories(ctx, factionID)
	if err != nil {
		s.logger.Error("Failed to get faction territories", zap.Error(err), zap.String("faction_id", factionID.String()))
		return nil, fmt.Errorf("failed to get faction territories: %w", err)
	}

	return territories, nil
}

//go:align 64
func (s *Service) AdjustFactionReputation(ctx context.Context, factionID uuid.UUID, adjustmentType string, value int, reason string) error {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("adjust_reputation"))
	defer timer.ObserveDuration()

	currentRep, err := s.repo.GetFactionReputation(ctx, factionID)
	if err != nil {
		return fmt.Errorf("failed to get current reputation: %w", err)
	}

	newRep := currentRep + value

	// Clamp reputation between -1000 and 1000
	if newRep < -1000 {
		newRep = -1000
	} else if newRep > 1000 {
		newRep = 1000
	}

	if err := s.repo.UpdateFactionReputation(ctx, factionID, newRep); err != nil {
		s.logger.Error("Failed to update faction reputation", zap.Error(err), zap.String("faction_id", factionID.String()))
		return fmt.Errorf("failed to adjust reputation: %w", err)
	}

	// Log reputation event
	event := &models.ReputationEvent{
		EventType:    adjustmentType,
		ValueChange:  value,
		Timestamp:    time.Now(),
		Description:  reason,
	}

	if err := s.repo.LogReputationEvent(ctx, factionID, event); err != nil {
		s.logger.Warn("Failed to log reputation event", zap.Error(err))
		// Don't fail the operation for logging errors
	}

	s.reputationChanges.Inc()

	s.logger.Info("Faction reputation adjusted",
		zap.String("faction_id", factionID.String()),
		zap.Int("old_reputation", currentRep),
		zap.Int("new_reputation", newRep),
		zap.Int("adjustment", value),
		zap.String("reason", reason))

	return nil
}

//go:align 64
func (s *Service) GetFactionRankings(ctx context.Context, category string, limit, offset int) ([]*models.Faction, error) {
	timer := prometheus.NewTimer(s.factionOperations.WithLabelValues("get_rankings"))
	defer timer.ObserveDuration()

	factions, err := s.repo.GetFactionRankings(ctx, category, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get faction rankings", zap.Error(err), zap.String("category", category))
		return nil, fmt.Errorf("failed to get faction rankings: %w", err)
	}

	return factions, nil
}

//go:align 64
func (s *Service) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	health, err := s.repo.GetSystemHealth(ctx)
	if err != nil {
		s.logger.Error("Failed to get system health", zap.Error(err))
		return nil, fmt.Errorf("failed to get system health: %w", err)
	}

	// Update gauge metrics
	s.activeFactions.Set(float64(health.ActiveFactions))
	s.activeDiplomacy.Set(float64(health.ActiveDiplomacy))

	return health, nil
}

//go:align 64
func (s *Service) ProcessReputationDecay(ctx context.Context) error {
	// This would be called by a background job to decay reputation over time
	// For now, just log that it would run
	s.logger.Info("Reputation decay processing started")

	// In a real implementation, this would:
	// 1. Find factions with reputation > 0
	// 2. Apply decay rate based on time since last activity
	// 3. Update reputation and log events

	return nil
}