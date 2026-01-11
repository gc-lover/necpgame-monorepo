// Business Service layer with MMOFPS optimizations
// Issue: #1506
package server

import (
	"context"
	"fmt"
	"math"
	"time"

	"go.uber.org/zap"
)

// Service implements business logic for seasonal challenges
type SeasonalChallengesService struct {
	logger     *zap.Logger
	repository Repository
	websocket  *WebSocketServer
}

// RollbackAction represents an action that can be rolled back
type RollbackAction interface {
	Rollback(ctx context.Context) error
	Description() string
}

// CurrencyRollback represents a currency transaction that can be rolled back
type CurrencyRollback struct {
	playerID   string
	seasonID   string
	amount     int
	operation  string // "earn" or "spend"
	repository Repository
	logger     *zap.Logger
}

func (r *CurrencyRollback) Rollback(ctx context.Context) error {
	r.logger.Info("Rolling back currency transaction",
		zap.String("player_id", r.playerID),
		zap.String("season_id", r.seasonID),
		zap.Int("amount", r.amount),
		zap.String("operation", r.operation),
	)

	// Reverse the currency operation
	reverseTx := &CurrencyTransaction{
		ID:       generateUUID(),
		PlayerID: r.playerID,
		SeasonID: r.seasonID,
		Amount:   r.amount,
		Reason:   "rollback_" + r.operation,
		CreatedAt: time.Now(),
	}

	if r.operation == "earn" {
		// Reverse earn = spend the earned amount
		reverseTx.Type = "spend"
	} else if r.operation == "spend" {
		// Reverse spend = earn back the spent amount
		reverseTx.Type = "earn"
	} else {
		return fmt.Errorf("unknown operation type for rollback: %s", r.operation)
	}

	// Execute reverse transaction
	if err := r.repository.ExecuteCurrencyTransaction(ctx, reverseTx, nil); err != nil {
		r.logger.Error("Failed to execute currency rollback", zap.Error(err))
		return err
	}

	r.logger.Info("Currency rollback completed successfully")
	return nil
}

func (r *CurrencyRollback) Description() string {
	return fmt.Sprintf("Rollback currency %s of %d for player %s in season %s",
		r.operation, r.amount, r.playerID, r.seasonID)
}

// NewService creates a new service instance
func NewService(logger *zap.Logger, repository Repository, websocket *WebSocketServer) *SeasonalChallengesService {
	return &SeasonalChallengesService{
		logger:     logger,
		repository: repository,
		websocket:  websocket,
	}
}

// Season operations with optimistic locking for concurrent access
func (s *SeasonalChallengesService) CreateSeason(ctx context.Context, req CreateSeasonRequest) (*Season, error) {
	s.logger.Info("Creating new season",
		zap.String("name", req.Name),
		zap.Time("start_date", req.StartDate),
		zap.Time("end_date", req.EndDate),
	)

	// Business rule validation
	if req.EndDate.Before(req.StartDate) {
		return nil, fmt.Errorf("season end date cannot be before start date")
	}

	if req.CurrencyLimit <= 0 {
		req.CurrencyLimit = 10000 // Default
	}

	// Create season entity
	season := &Season{
		ID:            generateUUID(),
		Name:          req.Name,
		Description:   req.Description,
		StartDate:     req.StartDate,
		EndDate:       req.EndDate,
		Status:        "upcoming",
		CurrencyLimit: req.CurrencyLimit,
		RewardsPool:   req.RewardsPool,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
		Version:       1,
	}

	// Persist with optimistic locking
	if err := s.repository.CreateSeason(ctx, season); err != nil {
		s.logger.Error("Failed to create season", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Season created successfully", zap.String("season_id", season.ID))
	return season, nil
}

func (s *SeasonalChallengesService) GetSeason(ctx context.Context, seasonID string) (*Season, error) {
	s.logger.Debug("Retrieving season", zap.String("season_id", seasonID))

	season, err := s.repository.GetSeason(ctx, seasonID)
	if err != nil {
		s.logger.Error("Failed to get season", zap.String("season_id", seasonID), zap.Error(err))
		return nil, err
	}

	return season, nil
}

func (s *SeasonalChallengesService) UpdateSeason(ctx context.Context, seasonID string, req UpdateSeasonRequest) (*Season, error) {
	s.logger.Info("Updating season", zap.String("season_id", seasonID))

	// Get current season for optimistic locking
	current, err := s.repository.GetSeason(ctx, seasonID)
	if err != nil {
		return nil, err
	}

	// Check version for concurrent modifications
	if req.Version != current.Version {
		return nil, fmt.Errorf("season was modified by another request (version mismatch)")
	}

	// Apply updates
	if req.Name != "" {
		current.Name = req.Name
	}
	if req.Description != "" {
		current.Description = req.Description
	}
	if !req.StartDate.IsZero() {
		current.StartDate = req.StartDate
	}
	if !req.EndDate.IsZero() {
		current.EndDate = req.EndDate
	}
	if req.CurrencyLimit > 0 {
		current.CurrencyLimit = req.CurrencyLimit
	}
	if req.Status != "" {
		current.Status = req.Status
	}

	current.UpdatedAt = time.Now()
	current.Version++

	// Update with optimistic locking
	if err := s.repository.UpdateSeason(ctx, current); err != nil {
		s.logger.Error("Failed to update season", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Season updated successfully", zap.String("season_id", seasonID))
	return current, nil
}

func (s *SeasonalChallengesService) ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error) {
	s.logger.Debug("Listing seasons", zap.String("status", filter.Status))

	seasons, err := s.repository.ListSeasons(ctx, filter)
	if err != nil {
		s.logger.Error("Failed to list seasons", zap.Error(err))
		return nil, err
	}

	return seasons, nil
}

// Challenge progress operations with real-time updates
func (s *SeasonalChallengesService) UpdateChallengeProgress(ctx context.Context, req UpdateProgressRequest) (*ChallengeProgress, error) {
	s.logger.Info("Updating challenge progress",
		zap.String("player_id", req.PlayerID),
		zap.String("challenge_id", req.ChallengeID),
		zap.Int("progress_value", req.ProgressValue),
	)

	// Validate progress update
	if req.ProgressValue < 0 {
		return nil, fmt.Errorf("progress value cannot be negative")
	}

	// Get or create progress record
	progress, err := s.repository.GetChallengeProgress(ctx, req.PlayerID, req.ChallengeID)
	if err != nil {
		// Create new progress record
		progress = &ChallengeProgress{
			PlayerID:    req.PlayerID,
			ChallengeID: req.ChallengeID,
			CurrentValue: req.ProgressValue,
			IsCompleted: false,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}

		if err := s.repository.CreateChallengeProgress(ctx, progress); err != nil {
			s.logger.Error("Failed to create challenge progress", zap.Error(err))
			return nil, err
		}
	} else {
		// Update existing progress
		oldValue := progress.CurrentValue
		progress.CurrentValue += req.ProgressValue
		progress.UpdatedAt = time.Now()

		// Update objective progress based on the update
		if err := s.updateObjectiveProgress(ctx, progress, req); err != nil {
			s.logger.Error("Failed to update objective progress", zap.Error(err))
			// Continue with challenge update even if objective update fails
		}

		// Check for completion
		if s.isChallengeCompleted(progress) {
			progress.IsCompleted = true
			progress.CompletedAt = &time.Time{}
			*progress.CompletedAt = time.Now()

			// Trigger completion rewards
			if err := s.processCompletionRewards(ctx, progress); err != nil {
				s.logger.Error("Failed to process completion rewards", zap.Error(err))
			}
		}

		if err := s.repository.UpdateChallengeProgress(ctx, progress); err != nil {
			s.logger.Error("Failed to update challenge progress", zap.Error(err))
			return nil, err
		}

		s.logger.Info("Progress updated",
			zap.Int("old_value", oldValue),
			zap.Int("new_value", progress.CurrentValue),
			zap.Bool("completed", progress.IsCompleted),
		)
	}

	// Get challenge to determine season ID for proper broadcasting
	challenge, err := s.repository.GetChallenge(ctx, progress.ChallengeID)
	if err != nil {
		s.logger.Error("Failed to get challenge for season ID", zap.String("challenge_id", progress.ChallengeID), zap.Error(err))
		// Continue without broadcasting if we can't get season ID
		return progress, nil
	}

	// Broadcast real-time progress update to season subscribers
	event := &WSEvent{
		Type:        MsgProgressUpdate,
		SeasonID:    challenge.SeasonID,
		PlayerID:    progress.PlayerID,
		ChallengeID: progress.ChallengeID,
		Data: map[string]interface{}{
			"current_value":  progress.CurrentValue,
			"is_completed":   progress.IsCompleted,
			"challenge_id":   progress.ChallengeID,
		},
	}
	s.websocket.BroadcastToSeason(challenge.SeasonID, event)

	return progress, nil
}

// Leaderboard operations with caching for performance
func (s *SeasonalChallengesService) GetLeaderboard(ctx context.Context, seasonID string, limit int) (*SeasonLeaderboard, error) {
	s.logger.Debug("Retrieving leaderboard", zap.String("season_id", seasonID), zap.Int("limit", limit))

	// Try cache first (MMOFPS optimization)
	if cached, found := s.getCachedLeaderboard(seasonID); found {
		return cached, nil
	}

	// Fetch from database
	leaderboard, err := s.repository.GetLeaderboard(ctx, seasonID, limit)
	if err != nil {
		s.logger.Error("Failed to get leaderboard", zap.Error(err))
		return nil, err
	}

	// Cache for future requests
	s.cacheLeaderboard(seasonID, leaderboard)

	return leaderboard, nil
}

// Reward claiming with inventory validation
func (s *SeasonalChallengesService) ClaimSeasonRewards(ctx context.Context, req ClaimRewardsRequest) (*ClaimedRewards, error) {
	s.logger.Info("Claiming season rewards",
		zap.String("player_id", req.PlayerID),
		zap.String("season_id", req.SeasonID),
	)

	// Validate claim eligibility
	eligible, err := s.isEligibleForRewards(ctx, req.PlayerID, req.SeasonID)
	if err != nil {
		return nil, err
	}
	if !eligible {
		return nil, fmt.Errorf("player not eligible for rewards")
	}

	// Get available rewards
	rewards, err := s.repository.GetAvailableRewards(ctx, req.PlayerID, req.SeasonID)
	if err != nil {
		return nil, err
	}

	// Process reward claiming with inventory checks
	claimedRewards := &ClaimedRewards{
		PlayerID:  req.PlayerID,
		SeasonID:  req.SeasonID,
		ClaimedAt: time.Now(),
		Rewards:   rewards,
	}

	// Add to player inventory
	if err := s.addRewardsToInventory(ctx, claimedRewards); err != nil {
		s.logger.Error("Failed to add rewards to inventory", zap.Error(err))
		return nil, err
	}

	// Mark rewards as claimed
	if err := s.repository.MarkRewardsClaimed(ctx, req.PlayerID, req.SeasonID); err != nil {
		s.logger.Error("Failed to mark rewards as claimed", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Rewards claimed successfully",
		zap.String("player_id", req.PlayerID),
		zap.Int("reward_count", len(rewards)),
	)

	// Broadcast reward claim event to player
	event := &WSEvent{
		Type:     MsgRewardAvailable,
		SeasonID: req.SeasonID,
		PlayerID: req.PlayerID,
		Data: map[string]interface{}{
			"rewards": rewards,
			"claimed_at": claimedRewards.ClaimedAt,
		},
	}
	s.websocket.BroadcastToPlayer(req.PlayerID, event)

	return claimedRewards, nil
}

// Seasonal Currency operations with transaction safety

// EarnSeasonalCurrency awards currency to player for challenge progress
func (s *SeasonalChallengesService) EarnSeasonalCurrency(ctx context.Context, req EarnCurrencyRequest) (*CurrencyTransaction, *CurrencyRollback, error) {
	s.logger.Info("Earning seasonal currency",
		zap.String("player_id", req.PlayerID),
		zap.String("season_id", req.SeasonID),
		zap.Int("amount", req.Amount),
		zap.String("reason", req.Reason),
	)

	// Validate amount
	if req.Amount <= 0 {
		return nil, fmt.Errorf("currency amount must be positive")
	}

	// Check season currency limit
	season, err := s.repository.GetSeason(ctx, req.SeasonID)
	if err != nil {
		return nil, err
	}

	currentCurrency, err := s.repository.GetSeasonalCurrency(ctx, req.PlayerID, req.SeasonID)
	if err != nil && err != ErrNotFound {
		return nil, err
	}

	// Check if earning would exceed season limit
	if currentCurrency != nil {
		totalAfterEarn := currentCurrency.EarnedTotal + req.Amount
		if totalAfterEarn > season.CurrencyLimit {
			// Allow partial earning up to the limit
			req.Amount = season.CurrencyLimit - currentCurrency.EarnedTotal
			if req.Amount <= 0 {
				return nil, fmt.Errorf("season currency limit reached")
			}
		}
	}

	// Create transaction record
	transaction := &CurrencyTransaction{
		ID:       generateUUID(),
		PlayerID: req.PlayerID,
		SeasonID: req.SeasonID,
		Type:     "earn",
		Amount:   req.Amount,
		Reason:   req.Reason,
		Metadata: req.Metadata,
		CreatedAt: time.Now(),
	}

	// Execute transaction with optimistic locking
	if err := s.executeCurrencyTransaction(ctx, transaction); err != nil {
		return nil, nil, err
	}

	s.logger.Info("Currency earned successfully",
		zap.String("player_id", req.PlayerID),
		zap.Int("amount", req.Amount),
		zap.Int("new_balance", transaction.BalanceAfter),
	)

	// Create rollback action for this transaction
	rollback := &CurrencyRollback{
		playerID:   req.PlayerID,
		seasonID:   req.SeasonID,
		amount:     req.Amount,
		operation:  "earn",
		repository: s.repository,
		logger:     s.logger,
	}

	// Broadcast currency update event
	event := &WSEvent{
		Type:     "currency_update",
		SeasonID: req.SeasonID,
		PlayerID: req.PlayerID,
		Data: map[string]interface{}{
			"amount":       req.Amount,
			"reason":       req.Reason,
			"new_balance":  transaction.BalanceAfter,
			"transaction_id": transaction.ID,
		},
	}
	s.websocket.BroadcastToPlayer(req.PlayerID, event)

	return transaction, rollback, nil
}

// SpendSeasonalCurrency deducts currency for purchases/exchanges
func (s *SeasonalChallengesService) SpendSeasonalCurrency(ctx context.Context, req SpendCurrencyRequest) (*CurrencyTransaction, *CurrencyRollback, error) {
	s.logger.Info("Spending seasonal currency",
		zap.String("player_id", req.PlayerID),
		zap.String("season_id", req.SeasonID),
		zap.Int("amount", req.Amount),
		zap.String("reason", req.Reason),
	)

	// Validate amount
	if req.Amount <= 0 {
		return nil, fmt.Errorf("currency amount must be positive")
	}

	// Check current balance
	currentCurrency, err := s.repository.GetSeasonalCurrency(ctx, req.PlayerID, req.SeasonID)
	if err != nil {
		return nil, fmt.Errorf("insufficient currency balance")
	}

	if currentCurrency.Balance < req.Amount {
		return nil, fmt.Errorf("insufficient currency balance: has %d, need %d", currentCurrency.Balance, req.Amount)
	}

	// Create transaction record
	transaction := &CurrencyTransaction{
		ID:       generateUUID(),
		PlayerID: req.PlayerID,
		SeasonID: req.SeasonID,
		Type:     "spend",
		Amount:   req.Amount,
		Reason:   req.Reason,
		Metadata: req.Metadata,
		CreatedAt: time.Now(),
	}

	// Execute transaction with optimistic locking
	if err := s.executeCurrencyTransaction(ctx, transaction); err != nil {
		return nil, nil, err
	}

	s.logger.Info("Currency spent successfully",
		zap.String("player_id", req.PlayerID),
		zap.Int("amount", req.Amount),
		zap.Int("new_balance", transaction.BalanceAfter),
	)

	// Create rollback action for this transaction
	rollback := &CurrencyRollback{
		playerID:   req.PlayerID,
		seasonID:   req.SeasonID,
		amount:     req.Amount,
		operation:  "spend",
		repository: s.repository,
		logger:     s.logger,
	}

	// Broadcast currency update event
	event := &WSEvent{
		Type:     "currency_update",
		SeasonID: req.SeasonID,
		PlayerID: req.PlayerID,
		Data: map[string]interface{}{
			"amount":       -req.Amount, // Negative for spending
			"reason":       req.Reason,
			"new_balance":  transaction.BalanceAfter,
			"transaction_id": transaction.ID,
		},
	}
	s.websocket.BroadcastToPlayer(req.PlayerID, event)

	return transaction, rollback, nil
}

// ExchangeSeasonalCurrency converts seasonal currency to other rewards
func (s *SeasonalChallengesService) ExchangeSeasonalCurrency(ctx context.Context, req ExchangeCurrencyRequest) (*CurrencyExchange, error) {
	s.logger.Info("Exchanging seasonal currency",
		zap.String("player_id", req.PlayerID),
		zap.String("season_id", req.SeasonID),
		zap.Int("currency_amount", req.CurrencyAmount),
		zap.String("exchange_type", req.ExchangeType),
	)

	// Validate exchange
	if req.CurrencyAmount <= 0 {
		return nil, fmt.Errorf("currency amount must be positive")
	}

	// Check balance
	currentCurrency, err := s.repository.GetSeasonalCurrency(ctx, req.PlayerID, req.SeasonID)
	if err != nil {
		return nil, fmt.Errorf("insufficient currency balance")
	}

	if currentCurrency.Balance < req.CurrencyAmount {
		return nil, fmt.Errorf("insufficient currency balance: has %d, need %d", currentCurrency.Balance, req.CurrencyAmount)
	}

	// Validate exchange rates and availability
	exchangeRate, reward, err := s.validateExchange(ctx, req)
	if err != nil {
		return nil, err
	}

	// Create exchange record
	exchange := &CurrencyExchange{
		ID:             generateUUID(),
		PlayerID:       req.PlayerID,
		SeasonID:       req.SeasonID,
		CurrencyAmount: req.CurrencyAmount,
		ExchangeType:   req.ExchangeType,
		Reward:         reward,
		ExchangeRate:   exchangeRate,
		CreatedAt:      time.Now(),
	}

	// Execute exchange transaction
	spendTx := &CurrencyTransaction{
		ID:       generateUUID(),
		PlayerID: req.PlayerID,
		SeasonID: req.SeasonID,
		Type:     "exchange",
		Amount:   req.CurrencyAmount,
		Reason:   fmt.Sprintf("exchange_%s", req.ExchangeType),
		Metadata: req.Metadata,
		CreatedAt: time.Now(),
	}

	if err := s.executeCurrencyTransaction(ctx, spendTx); err != nil {
		return nil, err
	}

	// Create rollback action for currency spend
	spendRollback := &CurrencyRollback{
		playerID:   req.PlayerID,
		seasonID:   req.SeasonID,
		amount:     req.CurrencyAmount,
		operation:  "spend",
		repository: s.repository,
		logger:     s.logger,
	}

	// Grant reward to player
	if err := s.grantExchangeReward(ctx, exchange); err != nil {
		// Execute rollback to return currency to player
		s.logger.Error("Failed to grant exchange reward, executing rollback", zap.Error(err))
		if rollbackErr := spendRollback.Rollback(ctx); rollbackErr != nil {
			s.logger.Error("Critical: Failed to rollback currency transaction", zap.Error(rollbackErr))
			// Log critical error but still return original error
		}
		return nil, fmt.Errorf("failed to grant reward (currency rolled back): %w", err)
	}

	// Save exchange record
	if err := s.repository.CreateCurrencyExchange(ctx, exchange); err != nil {
		s.logger.Error("Failed to save exchange record", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Currency exchanged successfully",
		zap.String("player_id", req.PlayerID),
		zap.String("exchange_type", req.ExchangeType),
		zap.Int("currency_spent", req.CurrencyAmount),
	)

	// Broadcast exchange event
	event := &WSEvent{
		Type:     "currency_exchange",
		SeasonID: req.SeasonID,
		PlayerID: req.PlayerID,
		Data: map[string]interface{}{
			"exchange_type":   req.ExchangeType,
			"currency_spent":  req.CurrencyAmount,
			"reward":          reward,
			"exchange_rate":   exchangeRate,
			"new_balance":     spendTx.BalanceAfter,
		},
	}
	s.websocket.BroadcastToPlayer(req.PlayerID, event)

	return exchange, nil
}

// GetCurrencyBalance returns player's current seasonal currency balance
func (s *SeasonalChallengesService) GetCurrencyBalance(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error) {
	currency, err := s.repository.GetSeasonalCurrency(ctx, playerID, seasonID)
	if err != nil {
		if err == ErrNotFound {
			// Return zero balance for new players
			return &SeasonalCurrency{
				SeasonID:    seasonID,
				PlayerID:    playerID,
				Balance:     0,
				EarnedTotal: 0,
				SpentTotal:  0,
			}, nil
		}
		return nil, err
	}
	return currency, nil
}

// GetCurrencyTransactions returns player's currency transaction history
func (s *SeasonalChallengesService) GetCurrencyTransactions(ctx context.Context, playerID, seasonID string, limit int) ([]*CurrencyTransaction, error) {
	return s.repository.GetCurrencyTransactions(ctx, playerID, seasonID, limit)
}

// executeCurrencyTransaction performs atomic currency transaction
func (s *SeasonalChallengesService) executeCurrencyTransaction(ctx context.Context, tx *CurrencyTransaction) error {
	// Get or create currency record
	currency, err := s.repository.GetSeasonalCurrency(ctx, tx.PlayerID, tx.SeasonID)
	if err != nil && err != ErrNotFound {
		return err
	}

	if currency == nil {
		// Create new currency record
		currency = &SeasonalCurrency{
			SeasonID:    tx.SeasonID,
			PlayerID:    tx.PlayerID,
			Balance:     0,
			EarnedTotal: 0,
			SpentTotal:  0,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
		if err := s.repository.CreateSeasonalCurrency(ctx, currency); err != nil {
			return err
		}
	}

	// Calculate new balance
	switch tx.Type {
	case "earn":
		currency.Balance += tx.Amount
		currency.EarnedTotal += tx.Amount
	case "spend", "exchange":
		currency.Balance -= tx.Amount
		currency.SpentTotal += tx.Amount
	default:
		return fmt.Errorf("unknown transaction type: %s", tx.Type)
	}

	// Validate balance constraints
	if currency.Balance < 0 {
		return fmt.Errorf("insufficient balance for transaction")
	}

	currency.UpdatedAt = time.Now()
	tx.BalanceAfter = currency.Balance

	// Execute in transaction with optimistic locking
	return s.repository.ExecuteCurrencyTransaction(ctx, tx, currency)
}

// validateExchange checks if exchange is valid and returns exchange rate
func (s *SeasonalChallengesService) validateExchange(ctx context.Context, req ExchangeCurrencyRequest) (float64, interface{}, error) {
	// Define exchange rates and rules for MMOFPS seasonal currency
	exchangeRules := map[string]struct {
		minCurrency    int
		maxCurrency    int
		rate           float64
		dailyLimit     int
		playerLimit    int
		description    string
		rewardType     string
		rewardTemplate map[string]interface{}
	}{
		"premium_boost": {
			minCurrency:    1000,
			maxCurrency:    50000,
			rate:           0.8, // 1 currency = 0.8 premium points
			dailyLimit:     100000,
			playerLimit:    50000,
			description:    "Premium account boost",
			rewardType:     "premium_time",
			rewardTemplate: map[string]interface{}{
				"duration_hours": 24,
				"boost_multiplier": 1.5,
			},
		},
		"cyberware_unlock": {
			minCurrency:    2500,
			maxCurrency:    2500,
			rate:           1.0,
			dailyLimit:     50000,
			playerLimit:    2500, // Only one per season
			description:    "Exclusive cyberware implant",
			rewardType:     "item",
			rewardTemplate: map[string]interface{}{
				"item_id": "seasonal_cyberware_2077",
				"rarity": "epic",
				"name": "Neural Accelerator v2.0",
			},
		},
		"legendary_weapon": {
			minCurrency:    5000,
			maxCurrency:    5000,
			rate:           1.0,
			dailyLimit:     25000,
			playerLimit:    5000, // Only one per season
			description:    "Legendary weapon skin",
			rewardType:     "cosmetic",
			rewardTemplate: map[string]interface{}{
				"cosmetic_id": "legendary_weapon_skin_2026",
				"rarity": "legendary",
				"name": "Neon Phantom Skin",
				"applies_to": []string{"assault_rifle", "sniper_rifle"},
			},
		},
		"guild_experience": {
			minCurrency:    500,
			maxCurrency:    10000,
			rate:           2.0, // 1 currency = 2 guild XP
			dailyLimit:     50000,
			playerLimit:    25000,
			description:    "Guild experience boost",
			rewardType:     "guild_xp",
			rewardTemplate: map[string]interface{}{
				"xp_amount": 0, // Calculated from currency amount
				"guild_id": "", // Set from player context
			},
		},
		"title_unlock": {
			minCurrency:    1500,
			maxCurrency:    1500,
			rate:           1.0,
			dailyLimit:     30000,
			playerLimit:    1500, // Only one per season
			description:    "Exclusive seasonal title",
			rewardType:     "title",
			rewardTemplate: map[string]interface{}{
				"title_id": "champion_of_seasons",
				"name": "Champion of Seasons",
				"color": "#FFD700",
				"description": "Mastered all seasonal challenges",
			},
		},
		"experience_multiplier": {
			minCurrency:    750,
			maxCurrency:    3000,
			rate:           5.0, // 1 currency = 5% XP boost
			dailyLimit:     75000,
			playerLimit:    10000,
			description:    "Experience multiplier",
			rewardType:     "xp_boost",
			rewardTemplate: map[string]interface{}{
				"boost_percent": 0, // Calculated from currency amount
				"duration_hours": 24,
			},
		},
	}

	// Check if exchange type exists
	rule, exists := exchangeRules[req.ExchangeType]
	if !exists {
		return 0, nil, fmt.Errorf("unknown exchange type: %s", req.ExchangeType)
	}

	// Validate currency amount
	if req.CurrencyAmount < rule.minCurrency {
		return 0, nil, fmt.Errorf("minimum currency required: %d, provided: %d", rule.minCurrency, req.CurrencyAmount)
	}
	if req.CurrencyAmount > rule.maxCurrency {
		return 0, nil, fmt.Errorf("maximum currency allowed: %d, provided: %d", rule.maxCurrency, req.CurrencyAmount)
	}

	// Check player limit (seasonal total spent on this exchange type)
	playerSpent, err := s.getPlayerExchangeSpent(ctx, req.PlayerID, req.SeasonID, req.ExchangeType)
	if err != nil {
		return 0, nil, fmt.Errorf("failed to check player exchange limit: %v", err)
	}
	if playerSpent+req.CurrencyAmount > rule.playerLimit {
		return 0, nil, fmt.Errorf("player exchange limit exceeded: max %d, would be %d", rule.playerLimit, playerSpent+req.CurrencyAmount)
	}

	// Check daily limit
	dailySpent, err := s.getDailyExchangeSpent(ctx, req.SeasonID, req.ExchangeType)
	if err != nil {
		return 0, nil, fmt.Errorf("failed to check daily exchange limit: %v", err)
	}
	if dailySpent+req.CurrencyAmount > rule.dailyLimit {
		return 0, nil, fmt.Errorf("daily exchange limit exceeded: max %d, would be %d", rule.dailyLimit, dailySpent+req.CurrencyAmount)
	}

	// Calculate reward based on exchange type
	reward := make(map[string]interface{})
	for k, v := range rule.rewardTemplate {
		reward[k] = v
	}

	switch req.ExchangeType {
	case "guild_experience":
		reward["xp_amount"] = int(float64(req.CurrencyAmount) * rule.rate)
	case "experience_multiplier":
		reward["boost_percent"] = int(float64(req.CurrencyAmount) * rule.rate)
	default:
		// For fixed-rate exchanges, use template as-is
	}

	reward["description"] = rule.description
	reward["type"] = rule.rewardType
	reward["season_id"] = req.SeasonID
	reward["player_id"] = req.PlayerID

	return rule.rate, reward, nil
}

// grantExchangeReward gives the reward to player
func (s *SeasonalChallengesService) grantExchangeReward(ctx context.Context, exchange *CurrencyExchange) error {
	s.logger.Info("Granting exchange reward",
		zap.String("player_id", exchange.PlayerID),
		zap.String("exchange_type", exchange.ExchangeType),
		zap.Float64("exchange_rate", exchange.ExchangeRate),
		zap.Int("currency_spent", exchange.CurrencyAmount),
	)

	// Grant reward based on type
	switch exchange.ExchangeType {
	case "premium_boost":
		return s.grantPremiumBoost(ctx, exchange)
	case "cyberware_unlock":
		return s.grantItemReward(ctx, exchange)
	case "legendary_weapon":
		return s.grantCosmeticReward(ctx, exchange)
	case "guild_experience":
		return s.grantGuildExperience(ctx, exchange)
	case "title_unlock":
		return s.grantTitleReward(ctx, exchange)
	case "experience_multiplier":
		return s.grantXPBoost(ctx, exchange)
	case "experience_points":
		reward := exchange.Reward.(map[string]interface{})
		amount := reward["amount"].(int)
		return s.grantXP(ctx, exchange.PlayerID, amount, fmt.Sprintf("exchange_%s", exchange.ExchangeType))
	default:
		return fmt.Errorf("unknown reward type for granting: %s", exchange.ExchangeType)
	}
}

// getPlayerExchangeSpent returns total currency spent by player on specific exchange type this season
func (s *SeasonalChallengesService) getPlayerExchangeSpent(ctx context.Context, playerID, seasonID, exchangeType string) (int, error) {
	return s.repository.GetPlayerExchangeSpent(ctx, playerID, seasonID, exchangeType)
}

// getDailyExchangeSpent returns total currency spent on specific exchange type today
func (s *SeasonalChallengesService) getDailyExchangeSpent(ctx context.Context, seasonID, exchangeType string) (int, error) {
	return s.repository.GetDailyExchangeSpent(ctx, seasonID, exchangeType)
}

// grantPremiumBoost activates premium account boost
func (s *SeasonalChallengesService) grantPremiumBoost(ctx context.Context, exchange *CurrencyExchange) error {
	reward := exchange.Reward.(map[string]interface{})
	boostType := reward["boost_type"].(string)
	durationHours := reward["duration_hours"].(int)

	s.logger.Info("Granting premium boost",
		zap.String("player_id", exchange.PlayerID),
		zap.String("boost_type", boostType),
		zap.Int("duration_hours", durationHours),
	)

	return s.activatePremiumBoost(ctx, exchange.PlayerID, boostType, durationHours)
}

// Premium System Integration Methods

// checkPlayerPremiumStatus checks if player has active premium subscription
func (s *SeasonalChallengesService) checkPlayerPremiumStatus(ctx context.Context, playerID string) (*PremiumStatus, error) {
	// TODO: Integrate with actual premium service
	// For now, return mock premium status
	return &PremiumStatus{
		PlayerID:      playerID,
		IsActive:      true, // Mock: assume player has premium
		Tier:          "gold",
		ExpiresAt:     time.Now().Add(24 * time.Hour),
		Features:      []string{"challenge_bonus", "exclusive_rewards", "priority_matching"},
		RenewalDate:   time.Now().Add(30 * 24 * time.Hour),
	}, nil
}

// activatePremiumBoost activates temporary premium boost
func (s *SeasonalChallengesService) activatePremiumBoost(ctx context.Context, playerID, boostType string, durationHours int) error {
	boostID := generateUUID()
	expiresAt := time.Now().Add(time.Duration(durationHours) * time.Hour)

	// TODO: Store premium boost in database (would integrate with premium service)
	s.logger.Info("Activated premium boost",
		zap.String("player_id", playerID),
		zap.String("boost_id", boostID),
		zap.String("boost_type", boostType),
		zap.Int("duration_hours", durationHours),
		zap.Time("expires_at", expiresAt),
	)

	// Broadcast premium boost activation event
	event := &WSEvent{
		Type:     "premium_boost_activated",
		PlayerID: playerID,
		Data: map[string]interface{}{
			"boost_id":       boostID,
			"boost_type":     boostType,
			"duration_hours": durationHours,
			"expires_at":     expiresAt,
		},
	}
	s.websocket.BroadcastToPlayer(playerID, event)

	return nil
}

// applyPremiumBonuses applies premium bonuses to challenge rewards
func (s *SeasonalChallengesService) applyPremiumBonuses(ctx context.Context, playerID string, baseRewards map[string]interface{}) (map[string]interface{}, error) {
	premiumStatus, err := s.checkPlayerPremiumStatus(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to check premium status", zap.Error(err))
		return baseRewards, nil // Return base rewards on error
	}

	if !premiumStatus.IsActive {
		return baseRewards, nil // No bonuses for non-premium players
	}

	// Apply tier-based bonuses
	bonusMultiplier := s.getPremiumBonusMultiplier(premiumStatus.Tier)
	bonusesApplied := []string{}

	// Currency bonus
	if currency, ok := baseRewards["currency"].(int); ok {
		baseRewards["currency"] = int(float64(currency) * bonusMultiplier)
		bonusesApplied = append(bonusesApplied, "currency")
	}

	// XP bonus
	if xp, ok := baseRewards["xp"].(int); ok {
		baseRewards["xp"] = int(float64(xp) * bonusMultiplier)
		bonusesApplied = append(bonusesApplied, "xp")
	}

	// Add premium-exclusive rewards
	if premiumStatus.Tier == "diamond" || premiumStatus.Tier == "legendary" {
		baseRewards["premium_exclusive_item"] = map[string]interface{}{
			"item_id": "premium_challenge_token",
			"amount":  1,
		}
		bonusesApplied = append(bonusesApplied, "exclusive_item")
	}

	// Log applied bonuses
	if len(bonusesApplied) > 0 {
		s.logger.Info("Applied premium bonuses",
			zap.String("player_id", playerID),
			zap.String("tier", premiumStatus.Tier),
			zap.Float64("multiplier", bonusMultiplier),
			zap.Strings("bonuses", bonusesApplied),
		)
	}

	return baseRewards, nil
}

// getPremiumBonusMultiplier returns bonus multiplier based on premium tier
func (s *SeasonalChallengesService) getPremiumBonusMultiplier(tier string) float64 {
	switch tier {
	case "bronze":
		return 1.1 // 10% bonus
	case "silver":
		return 1.25 // 25% bonus
	case "gold":
		return 1.5 // 50% bonus
	case "diamond":
		return 2.0 // 100% bonus (double rewards)
	case "legendary":
		return 3.0 // 200% bonus (triple rewards)
	default:
		return 1.0 // No bonus
	}
}

// getPremiumChallengeEligibility checks if player can access premium-only challenges
func (s *SeasonalChallengesService) getPremiumChallengeEligibility(ctx context.Context, playerID string) (*PremiumEligibility, error) {
	premiumStatus, err := s.checkPlayerPremiumStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	eligibility := &PremiumEligibility{
		CanAccessPremiumChallenges: premiumStatus.IsActive,
		CanAccessExclusiveRewards:   premiumStatus.Tier == "diamond" || premiumStatus.Tier == "legendary",
		MaxDailyChallenges:          s.getMaxDailyChallenges(premiumStatus.Tier),
		BonusObjectivesUnlocked:     premiumStatus.IsActive,
	}

	return eligibility, nil
}

// getMaxDailyChallenges returns max challenges per day based on premium tier
func (s *SeasonalChallengesService) getMaxDailyChallenges(tier string) int {
	switch tier {
	case "bronze":
		return 3
	case "silver":
		return 5
	case "gold":
		return 8
	case "diamond":
		return 12
	case "legendary":
		return 20 // Unlimited practical
	default:
		return 2 // Free tier limit
	}
}

// grantItemReward adds item to player inventory
func (s *SeasonalChallengesService) grantItemReward(ctx context.Context, exchange *CurrencyExchange) error {
	// TODO: Integrate with inventory system
	s.logger.Info("Granting item reward",
		zap.String("player_id", exchange.PlayerID),
		zap.Any("reward", exchange.Reward),
	)
	return nil
}

// grantCosmeticReward unlocks cosmetic item
func (s *SeasonalChallengesService) grantCosmeticReward(ctx context.Context, exchange *CurrencyExchange) error {
	// TODO: Integrate with cosmetic system
	s.logger.Info("Granting cosmetic reward",
		zap.String("player_id", exchange.PlayerID),
		zap.Any("reward", exchange.Reward),
	)
	return nil
}

// grantGuildExperience adds XP to player's guild
func (s *SeasonalChallengesService) grantGuildExperience(ctx context.Context, exchange *CurrencyExchange) error {
	// TODO: Integrate with guild system
	s.logger.Info("Granting guild experience",
		zap.String("player_id", exchange.PlayerID),
		zap.Any("reward", exchange.Reward),
	)
	return nil
}

// grantTitleReward unlocks player title
func (s *SeasonalChallengesService) grantTitleReward(ctx context.Context, exchange *CurrencyExchange) error {
	// TODO: Integrate with title system
	s.logger.Info("Granting title reward",
		zap.String("player_id", exchange.PlayerID),
		zap.Any("reward", exchange.Reward),
	)
	return nil
}

// grantXPBoost activates experience multiplier
func (s *SeasonalChallengesService) grantXPBoost(ctx context.Context, exchange *CurrencyExchange) error {
	reward := exchange.Reward.(map[string]interface{})
	boostPercent := reward["boost_percent"].(int)
	durationHours := reward["duration_hours"].(int)

	s.logger.Info("Granting XP boost",
		zap.String("player_id", exchange.PlayerID),
		zap.Int("boost_percent", boostPercent),
		zap.Int("duration_hours", durationHours),
	)

	return s.applyXPBoost(ctx, exchange.PlayerID, boostPercent, durationHours)
}

// XP System Integration Methods

// grantXP awards experience points to player
func (s *SeasonalChallengesService) grantXP(ctx context.Context, playerID string, amount int, reason string) error {
	s.logger.Info("Granting XP to player",
		zap.String("player_id", playerID),
		zap.Int("amount", amount),
		zap.String("reason", reason),
	)

	// Get current player XP
	currentXP, currentLevel, err := s.getPlayerXP(ctx, playerID)
	if err != nil {
		return fmt.Errorf("failed to get player XP: %w", err)
	}

	newXP := currentXP + amount
	newLevel := s.calculateLevelFromXP(newXP)

	// Update player XP
	if err := s.updatePlayerXP(ctx, playerID, newXP, newLevel); err != nil {
		return fmt.Errorf("failed to update player XP: %w", err)
	}

	// Check for level up
	if newLevel > currentLevel {
		if err := s.handleLevelUp(ctx, playerID, currentLevel, newLevel); err != nil {
			s.logger.Error("Failed to handle level up", zap.Error(err))
			// Don't fail the entire operation for level up errors
		}
	}

	// Broadcast XP update event
	event := &WSEvent{
		Type:     "xp_update",
		PlayerID: playerID,
		Data: map[string]interface{}{
			"xp_gained":   amount,
			"total_xp":    newXP,
			"current_level": newLevel,
			"reason":      reason,
		},
	}
	s.websocket.BroadcastToPlayer(playerID, event)

	return nil
}

// applyXPBoost applies temporary XP multiplier
func (s *SeasonalChallengesService) applyXPBoost(ctx context.Context, playerID string, boostPercent, durationHours int) error {
	boostID := generateUUID()
	expiresAt := time.Now().Add(time.Duration(durationHours) * time.Hour)

	// TODO: Store XP boost in database (would integrate with XP service)
	s.logger.Info("Applied XP boost",
		zap.String("player_id", playerID),
		zap.String("boost_id", boostID),
		zap.Int("boost_percent", boostPercent),
		zap.Int("duration_hours", durationHours),
		zap.Time("expires_at", expiresAt),
	)

	// Broadcast XP boost event
	event := &WSEvent{
		Type:     "xp_boost_activated",
		PlayerID: playerID,
		Data: map[string]interface{}{
			"boost_id":      boostID,
			"boost_percent": boostPercent,
			"duration_hours": durationHours,
			"expires_at":    expiresAt,
		},
	}
	s.websocket.BroadcastToPlayer(playerID, event)

	return nil
}

// getPlayerXP retrieves current XP and level for player
func (s *SeasonalChallengesService) getPlayerXP(ctx context.Context, playerID string) (xp int, level int, err error) {
	// TODO: Integrate with actual XP service/database
	// For now, return mock data
	return 1250, 3, nil
}

// updatePlayerXP updates player's XP and level
func (s *SeasonalChallengesService) updatePlayerXP(ctx context.Context, playerID string, xp, level int) error {
	// TODO: Integrate with actual XP service/database
	s.logger.Info("Updated player XP",
		zap.String("player_id", playerID),
		zap.Int("xp", xp),
		zap.Int("level", level),
	)
	return nil
}

// calculateLevelFromXP converts total XP to level
func (s *SeasonalChallengesService) calculateLevelFromXP(totalXP int) int {
	// Simple level calculation: level = floor(sqrt(xp / 100)) + 1
	// This creates exponential XP requirements
	level := int(math.Floor(math.Sqrt(float64(totalXP)/100.0))) + 1
	if level < 1 {
		level = 1
	}
	return level
}

// handleLevelUp processes level up rewards and notifications
func (s *SeasonalChallengesService) handleLevelUp(ctx context.Context, playerID string, oldLevel, newLevel int) error {
	s.logger.Info("Player leveled up",
		zap.String("player_id", playerID),
		zap.Int("old_level", oldLevel),
		zap.Int("new_level", newLevel),
	)

	// Award level up bonus currency
	bonusCurrency := (newLevel - oldLevel) * 100 // 100 currency per level

	earnReq := EarnCurrencyRequest{
		PlayerID: playerID,
		SeasonID: "current_season", // TODO: Get actual current season
		Amount:   bonusCurrency,
		Reason:   "level_up_bonus",
	}

	_, _, err := s.EarnSeasonalCurrency(ctx, earnReq)
	if err != nil {
		return fmt.Errorf("failed to award level up currency: %w", err)
	}

	// Award level up XP (additional XP beyond what's needed for level)
	levelUpXPBonus := 50 * (newLevel - oldLevel)
	if err := s.grantXP(ctx, playerID, levelUpXPBonus, "level_up_bonus"); err != nil {
		s.logger.Error("Failed to grant level up XP bonus", zap.Error(err))
		// Don't fail level up for XP bonus error
	}

	// Broadcast level up event
	event := &WSEvent{
		Type:     "level_up",
		PlayerID: playerID,
		Data: map[string]interface{}{
			"old_level":      oldLevel,
			"new_level":      newLevel,
			"currency_bonus": bonusCurrency,
			"xp_bonus":       levelUpXPBonus,
		},
	}
	s.websocket.BroadcastToPlayer(playerID, event)

	return nil
}

// getChallengeXP calculates XP reward for challenge completion
func (s *SeasonalChallengesService) getChallengeXP(challenge *Challenge, completionScore int) int {
	// Base XP based on challenge difficulty and score
	baseXP := 0

	switch challenge.Difficulty {
	case "easy":
		baseXP = 50
	case "medium":
		baseXP = 100
	case "hard":
		baseXP = 200
	case "expert":
		baseXP = 400
	default:
		baseXP = 100
	}

	// Scale by completion score and challenge multiplier
	return int(float64(baseXP) * challenge.RewardMultiplier * (float64(completionScore) / 1000.0))
}

// Helper methods - TODO: implement full business logic

func (s *SeasonalChallengesService) isChallengeCompleted(progress *ChallengeProgress) bool {
	// Get challenge details and objectives
	challenge, err := s.repository.GetChallenge(context.Background(), progress.ChallengeID)
	if err != nil {
		s.logger.Error("Failed to get challenge for completion check", zap.String("challenge_id", progress.ChallengeID), zap.Error(err))
		return false
	}

	// Get all objectives for this challenge
	objectives, err := s.repository.GetChallengeObjectives(context.Background(), progress.ChallengeID)
	if err != nil {
		s.logger.Error("Failed to get challenge objectives", zap.String("challenge_id", progress.ChallengeID), zap.Error(err))
		return false
	}

	// If no objectives defined, fall back to simple score-based completion
	if len(objectives) == 0 {
		return progress.CurrentValue >= challenge.MaxScore
	}

	// Check each objective
	allRequiredCompleted := true
	optionalCompleted := 0
	totalOptional := 0

	for _, objective := range objectives {
		if objective.IsOptional {
			totalOptional++
			if s.isObjectiveCompleted(objective, progress) {
				optionalCompleted++
			}
		} else {
			// All required objectives must be completed
			if !s.isObjectiveCompleted(objective, progress) {
				allRequiredCompleted = false
				break
			}
		}
	}

	// Challenge is completed if all required objectives are done
	// Optional objectives are bonus but don't block completion
	return allRequiredCompleted
}

// updateObjectiveProgress updates progress for individual objectives based on challenge progress update
func (s *SeasonalChallengesService) updateObjectiveProgress(ctx context.Context, progress *ChallengeProgress, req UpdateProgressRequest) error {
	// Get challenge objectives
	objectives, err := s.repository.GetChallengeObjectives(ctx, progress.ChallengeID)
	if err != nil {
		return fmt.Errorf("failed to get challenge objectives: %w", err)
	}

	// Initialize objective progress map if not exists
	if progress.ObjectiveProgress == nil {
		progress.ObjectiveProgress = make([]*ObjectiveProgress, 0, len(objectives))
	}

	// Update progress for each objective based on its type
	for _, objective := range objectives {
		// Find existing progress for this objective
		var objProgress *ObjectiveProgress
		for _, op := range progress.ObjectiveProgress {
			if op.ObjectiveID == objective.ID {
				objProgress = op
				break
			}
		}

		// Create progress record if not exists
		if objProgress == nil {
			objProgress = &ObjectiveProgress{
				ObjectiveID:  objective.ID,
				CurrentValue: 0,
				IsCompleted:  false,
			}
			progress.ObjectiveProgress = append(progress.ObjectiveProgress, objProgress)
		}

		// Update objective progress based on type
		oldValue := objProgress.CurrentValue
		s.updateObjectiveValue(objective, objProgress, req)

		// Check if objective is now completed
		if !objProgress.IsCompleted && s.isObjectiveCompletedByValue(objective, objProgress.CurrentValue) {
			objProgress.IsCompleted = true
			objProgress.CompletedAt = &time.Time{}
			*objProgress.CompletedAt = time.Now()

			s.logger.Info("Objective completed",
				zap.String("objective_id", objective.ID),
				zap.String("objective_type", objective.ObjectiveType),
				zap.Int("target_value", objective.TargetValue),
				zap.Int("achieved_value", objProgress.CurrentValue),
			)
		}

		if oldValue != objProgress.CurrentValue {
			s.logger.Debug("Objective progress updated",
				zap.String("objective_id", objective.ID),
				zap.Int("old_value", oldValue),
				zap.Int("new_value", objProgress.CurrentValue),
			)
		}
	}

	return nil
}

// updateObjectiveValue updates the value for a specific objective based on progress update
func (s *SeasonalChallengesService) updateObjectiveValue(objective *ChallengeObjective, objProgress *ObjectiveProgress, req UpdateProgressRequest) {
	switch objective.ObjectiveType {
	case "kill_count":
		// Assume progress value represents kills
		if req.ProgressValue > 0 {
			objProgress.CurrentValue += req.ProgressValue
		}
	case "score_threshold":
		// Score-based objectives use the progress value directly
		if req.ProgressValue > objProgress.CurrentValue {
			objProgress.CurrentValue = req.ProgressValue
		}
	case "time_limit":
		// Time-based objectives (e.g., complete within time limit)
		// This would require time tracking logic
		objProgress.CurrentValue = req.ProgressValue
	case "collection":
		// Collection objectives (gather items)
		objProgress.CurrentValue += req.ProgressValue
	case "achievement":
		// Achievement-based objectives (boolean completion)
		if req.ProgressValue > 0 {
			objProgress.CurrentValue = objective.TargetValue // Mark as complete
		}
	default:
		// Unknown objective type, use generic progress
		objProgress.CurrentValue += req.ProgressValue
		s.logger.Warn("Unknown objective type for progress update",
			zap.String("type", objective.ObjectiveType))
	}
}

// isObjectiveCompletedByValue checks if objective is completed based on current value
func (s *SeasonalChallengesService) isObjectiveCompletedByValue(objective *ChallengeObjective, currentValue int) bool {
	switch objective.ProgressType {
	case "cumulative":
		return currentValue >= objective.TargetValue
	case "threshold":
		return currentValue >= objective.TargetValue
	case "best_run":
		// For best_run, we assume the current value is the best achieved
		return currentValue >= objective.TargetValue
	default:
		return currentValue >= objective.TargetValue
	}
}

// isObjectiveCompleted checks if a specific objective is completed based on progress
func (s *SeasonalChallengesService) isObjectiveCompleted(objective *ChallengeObjective, progress *ChallengeProgress) bool {
	// Find objective progress in player's progress
	var objProgress *ObjectiveProgress
	for _, op := range progress.ObjectiveProgress {
		if op.ObjectiveID == objective.ID {
			objProgress = op
			break
		}
	}

	// If no progress recorded yet, objective is not completed
	if objProgress == nil {
		return false
	}

	return s.isObjectiveCompletedByValue(objective, objProgress.CurrentValue)
}

func (s *SeasonalChallengesService) processCompletionRewards(ctx context.Context, progress *ChallengeProgress) error {
	s.logger.Info("Processing completion rewards",
		zap.String("player_id", progress.PlayerID),
		zap.String("challenge_id", progress.ChallengeID),
	)

	// Get challenge details for reward calculation
	challenge, err := s.repository.GetChallenge(ctx, progress.ChallengeID)
	if err != nil {
		return fmt.Errorf("failed to get challenge for rewards: %w", err)
	}

	// Get challenge objectives to calculate weighted rewards
	objectives, err := s.repository.GetChallengeObjectives(ctx, progress.ChallengeID)
	if err != nil {
		return fmt.Errorf("failed to get challenge objectives for rewards: %w", err)
	}

	// Calculate completion score based on objectives
	completionScore := s.calculateCompletionScore(progress, objectives)

	// Apply challenge multiplier
	baseCurrencyReward := int(float64(completionScore) * challenge.RewardMultiplier)

	// Calculate XP reward based on challenge difficulty and completion
	baseXPReward := s.getChallengeXP(challenge, completionScore)

	// Prepare base rewards for premium bonus calculation
	baseRewards := map[string]interface{}{
		"currency": baseCurrencyReward,
		"xp":       baseXPReward,
	}

	// Apply premium bonuses
	finalRewards, err := s.applyPremiumBonuses(ctx, progress.PlayerID, baseRewards)
	if err != nil {
		s.logger.Error("Failed to apply premium bonuses", zap.Error(err))
		finalRewards = baseRewards // Use base rewards on error
	}

	finalCurrencyReward := finalRewards["currency"].(int)
	finalXPReward := finalRewards["xp"].(int)

	// Award seasonal currency
	earnReq := EarnCurrencyRequest{
		PlayerID: progress.PlayerID,
		SeasonID: challenge.SeasonID,
		Amount:   finalCurrencyReward,
		Reason:   fmt.Sprintf("challenge_completed_%s", progress.ChallengeID),
	}

	_, _, err = s.EarnSeasonalCurrency(ctx, earnReq)
	if err != nil {
		return fmt.Errorf("failed to award completion currency: %w", err)
	}

	// Award XP
	if err := s.grantXP(ctx, progress.PlayerID, finalXPReward, fmt.Sprintf("challenge_completed_%s", progress.ChallengeID)); err != nil {
		s.logger.Error("Failed to award completion XP", zap.Error(err))
		// Don't fail entire reward process for XP error
	}

	// Award premium exclusive items if any
	if exclusiveItem, exists := finalRewards["premium_exclusive_item"]; exists {
		s.logger.Info("Awarding premium exclusive item",
			zap.String("player_id", progress.PlayerID),
			zap.Any("item", exclusiveItem),
		)
		// TODO: Integrate with inventory system to grant the item
	}

	s.logger.Info("Challenge completion rewards awarded",
		zap.String("player_id", progress.PlayerID),
		zap.String("challenge_id", progress.ChallengeID),
		zap.Int("completion_score", completionScore),
		zap.Float64("challenge_multiplier", challenge.RewardMultiplier),
		zap.Int("base_currency", baseCurrencyReward),
		zap.Int("base_xp", baseXPReward),
		zap.Int("final_currency", finalCurrencyReward),
		zap.Int("final_xp", finalXPReward),
		zap.Bool("premium_bonuses_applied", finalCurrencyReward > baseCurrencyReward || finalXPReward > baseXPReward),
	)

	return nil
}

// calculateCompletionScore calculates reward score based on completed objectives
func (s *SeasonalChallengesService) calculateCompletionScore(progress *ChallengeProgress, objectives []*ChallengeObjective) int {
	if len(objectives) == 0 {
		// Fallback to simple score-based calculation
		return progress.CurrentValue
	}

	totalScore := 0.0
	totalWeight := 0.0

	for _, objective := range objectives {
		weight := objective.RewardWeight
		if weight <= 0 {
			weight = 1.0 // Default weight
		}

		// Find objective progress
		objProgress := s.findObjectiveProgress(progress, objective.ID)
		if objProgress != nil && objProgress.IsCompleted {
			// Completed objective gets full weighted score
			totalScore += float64(objective.TargetValue) * weight
		} else if objProgress != nil {
			// Partial completion based on progress
			completionRatio := float64(objProgress.CurrentValue) / float64(objective.TargetValue)
			if completionRatio > 1.0 {
				completionRatio = 1.0
			}
			totalScore += float64(objective.TargetValue) * completionRatio * weight
		}

		if !objective.IsOptional {
			totalWeight += weight
		}
	}

	// Ensure minimum score for completion
	if totalScore < 100 {
		totalScore = 100
	}

	return int(totalScore)
}

// findObjectiveProgress finds progress for a specific objective
func (s *SeasonalChallengesService) findObjectiveProgress(progress *ChallengeProgress, objectiveID string) *ObjectiveProgress {
	for _, op := range progress.ObjectiveProgress {
		if op.ObjectiveID == objectiveID {
			return op
		}
	}
	return nil
}


func (s *SeasonalChallengesService) getCachedLeaderboard(seasonID string) (*SeasonLeaderboard, bool) {
	// TODO: Implement Redis caching
	return nil, false
}

func (s *SeasonalChallengesService) cacheLeaderboard(seasonID string, leaderboard *SeasonLeaderboard) {
	// TODO: Implement Redis caching
}

func (s *SeasonalChallengesService) isEligibleForRewards(ctx context.Context, playerID, seasonID string) (bool, error) {
	// TODO: Implement eligibility check
	return true, nil
}

func (s *SeasonalChallengesService) addRewardsToInventory(ctx context.Context, rewards *ClaimedRewards) error {
	// TODO: Implement inventory integration
	return nil
}

// Data structures - TODO: move to proper models package

type CreateSeasonRequest struct {
	Name          string              `json:"name"`
	Description   string              `json:"description"`
	StartDate     time.Time           `json:"start_date"`
	EndDate       time.Time           `json:"end_date"`
	CurrencyLimit int                 `json:"currency_limit"`
	RewardsPool   []SeasonRewardPool  `json:"rewards_pool"`
}

type UpdateSeasonRequest struct {
	Name          string              `json:"name"`
	Description   string              `json:"description"`
	StartDate     time.Time           `json:"start_date"`
	EndDate       time.Time           `json:"end_date"`
	CurrencyLimit int                 `json:"currency_limit"`
	RewardsPool   []SeasonRewardPool  `json:"rewards_pool"`
	Status        string              `json:"status"`
	Version       int                 `json:"version"`
}

type SeasonFilter struct {
	Status string
	Limit  int
	Cursor string
}

type UpdateProgressRequest struct {
	PlayerID     string `json:"player_id"`
	ChallengeID  string `json:"challenge_id"`
	ProgressValue int    `json:"progress_value"`
}

type ClaimRewardsRequest struct {
	PlayerID   string `json:"player_id"`
	SeasonID   string `json:"season_id"`
	ClaimType  string `json:"claim_type"`
	Tier       string `json:"tier"`
}

// Placeholder types - TODO: define properly
type Season struct {
	ID            string             `json:"id"`
	Name          string             `json:"name"`
	Description   string             `json:"description"`
	StartDate     time.Time          `json:"start_date"`
	EndDate       time.Time          `json:"end_date"`
	Status        string             `json:"status"`
	CurrencyLimit int                `json:"currency_limit"`
	RewardsPool   []SeasonRewardPool `json:"rewards_pool"`
	CreatedAt     time.Time          `json:"created_at"`
	UpdatedAt     time.Time          `json:"updated_at"`
	Version       int                `json:"version"`
}

type SeasonRewardPool struct {
	Tier     string    `json:"tier"`
	MinScore int       `json:"min_score"`
	Rewards  []Reward  `json:"rewards"`
}

type Reward struct {
	Type       string `json:"type"`
	Amount     int    `json:"amount"`
	ItemID     string `json:"item_id"`
	Rarity     string `json:"rarity"`
}

type Challenge struct {
	ID               string    `json:"id"`
	SeasonID         string    `json:"season_id"`
	Name             string    `json:"name"`
	ChallengeType    string    `json:"challenge_type"`
	Difficulty       string    `json:"difficulty"`
	MaxScore         int       `json:"max_score"`
	TimeLimitSeconds int       `json:"time_limit_seconds"`
	MinParticipants  int       `json:"min_participants"`
	MaxParticipants  int       `json:"max_participants"`
	IsTeamBased      bool      `json:"is_team_based"`
	EntryFee         int       `json:"entry_fee"`
	RewardMultiplier float64   `json:"reward_multiplier"`
	CreatedAt        time.Time `json:"created_at"`
	UpdatedAt        time.Time `json:"updated_at"`
	Version          int       `json:"version"`
}

type ChallengeProgress struct {
	PlayerID          string              `json:"player_id"`
	ChallengeID       string              `json:"challenge_id"`
	CurrentValue      int                 `json:"current_value"`
	IsCompleted       bool                `json:"is_completed"`
	CompletedAt       *time.Time          `json:"completed_at"`
	CreatedAt         time.Time           `json:"created_at"`
	UpdatedAt         time.Time           `json:"updated_at"`
	ObjectiveProgress []*ObjectiveProgress `json:"objective_progress,omitempty"`
}

type ObjectiveProgress struct {
	ObjectiveID  string     `json:"objective_id"`
	CurrentValue int        `json:"current_value"`
	IsCompleted  bool       `json:"is_completed"`
	CompletedAt  *time.Time `json:"completed_at"`
}

type PremiumStatus struct {
	PlayerID      string    `json:"player_id"`
	IsActive      bool      `json:"is_active"`
	Tier          string    `json:"tier"`
	ExpiresAt     time.Time `json:"expires_at"`
	Features      []string  `json:"features"`
	RenewalDate   time.Time `json:"renewal_date"`
}

type PremiumEligibility struct {
	CanAccessPremiumChallenges bool `json:"can_access_premium_challenges"`
	CanAccessExclusiveRewards   bool `json:"can_access_exclusive_rewards"`
	MaxDailyChallenges          int  `json:"max_daily_challenges"`
	BonusObjectivesUnlocked     bool `json:"bonus_objectives_unlocked"`
}

type SeasonLeaderboard struct {
	SeasonID      string            `json:"season_id"`
	TotalPlayers  int               `json:"total_players"`
	LastUpdated   time.Time         `json:"last_updated"`
	Entries       []LeaderboardEntry `json:"entries"`
}

type LeaderboardEntry struct {
	Rank                int    `json:"rank"`
	PlayerID            string `json:"player_id"`
	PlayerName          string `json:"player_name"`
	Score               int    `json:"score"`
	ChallengesCompleted int    `json:"challenges_completed"`
	CurrencyEarned      int    `json:"currency_earned"`
}

type ClaimedRewards struct {
	PlayerID  string    `json:"player_id"`
	SeasonID  string    `json:"season_id"`
	ClaimedAt time.Time `json:"claimed_at"`
	Rewards   []Reward  `json:"rewards"`
}

// Currency transaction structures
type CurrencyTransaction struct {
	ID           string                 `json:"id"`
	PlayerID     string                 `json:"player_id"`
	SeasonID     string                 `json:"season_id"`
	Type         string                 `json:"type"` // earn, spend, exchange
	Amount       int                    `json:"amount"`
	BalanceAfter int                    `json:"balance_after"`
	Reason       string                 `json:"reason"`
	Metadata     map[string]interface{} `json:"metadata,omitempty"`
	CreatedAt    time.Time              `json:"created_at"`
}

type CurrencyExchange struct {
	ID             string      `json:"id"`
	PlayerID       string      `json:"player_id"`
	SeasonID       string      `json:"season_id"`
	CurrencyAmount int         `json:"currency_amount"`
	ExchangeType   string      `json:"exchange_type"`
	Reward         interface{} `json:"reward"`
	ExchangeRate   float64     `json:"exchange_rate"`
	CreatedAt      time.Time   `json:"created_at"`
}

type SeasonalCurrency struct {
	SeasonID    string    `json:"season_id"`
	PlayerID    string    `json:"player_id"`
	Balance     int       `json:"balance"`
	EarnedTotal int       `json:"earned_total"`
	SpentTotal  int       `json:"spent_total"`
	LastEarnedAt *time.Time `json:"last_earned_at"`
	LastSpentAt *time.Time `json:"last_spent_at"`
	CreatedAt   time.Time `json:"created_at"`
	UpdatedAt   time.Time `json:"updated_at"`
}

// Request schemas
type EarnCurrencyRequest struct {
	PlayerID string                 `json:"player_id"`
	SeasonID string                 `json:"season_id"`
	Amount   int                    `json:"amount"`
	Reason   string                 `json:"reason"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

type SpendCurrencyRequest struct {
	PlayerID string                 `json:"player_id"`
	SeasonID string                 `json:"season_id"`
	Amount   int                    `json:"amount"`
	Reason   string                 `json:"reason"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

type ExchangeCurrencyRequest struct {
	PlayerID       string                 `json:"player_id"`
	SeasonID       string                 `json:"season_id"`
	CurrencyAmount int                    `json:"currency_amount"`
	ExchangeType   string                 `json:"exchange_type"`
	Metadata       map[string]interface{} `json:"metadata,omitempty"`
}

// Utility functions
func generateUUID() string {
	// TODO: Implement proper UUID generation
	return fmt.Sprintf("season-%d", time.Now().Unix())
}