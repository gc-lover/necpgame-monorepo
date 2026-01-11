// API Handlers implementation with MMOFPS optimizations
// Issue: #1506
package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strings"
	"time"

	"go.uber.org/zap"
)

// Handler represents an API handler with dependencies
type Handler struct {
	logger     *zap.Logger
	service    Service
	repository Repository
}

// NewHandler creates a new handler instance
func NewHandler(logger *zap.Logger, service Service, repository Repository) *Handler {
	return &Handler{
		logger:     logger,
		service:    service,
		repository: repository,
	}
}

// SeasonsHandler handles season CRUD operations
func (h *Handler) SeasonsHandler(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		h.ListSeasons(w, r)
	case http.MethodPost:
		h.CreateSeason(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// SeasonByIDHandler handles individual season operations
func (h *Handler) SeasonByIDHandler(w http.ResponseWriter, r *http.Request) {
	// Extract season ID from URL path
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/seasons/")
	seasonID := strings.Split(path, "/")[0]

	if seasonID == "" {
		http.Error(w, "Season ID required", http.StatusBadRequest)
		return
	}

	switch r.Method {
	case http.MethodGet:
		h.GetSeason(w, r, seasonID)
	case http.MethodPut:
		h.UpdateSeason(w, r, seasonID)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// ChallengeProgressHandler handles challenge progress updates
func (h *Handler) ChallengeProgressHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	h.UpdateProgress(w, r)
}

// LeaderboardHandler handles leaderboard queries
func (h *Handler) LeaderboardHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract season ID from URL
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/leaderboards/")
	seasonID := path

	h.GetLeaderboard(w, r, seasonID)
}

// RewardsHandler handles reward claiming
func (h *Handler) RewardsHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from URL
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/rewards/")
	playerID := strings.TrimSuffix(path, "/claim")

	h.ClaimRewards(w, r, playerID)
}

// CurrencyHandler handles currency operations
func (h *Handler) CurrencyHandler(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/currency/")

	switch r.Method {
	case http.MethodGet:
		// GET /api/v1/currency/{player_id}/{season_id}
		parts := strings.Split(path, "/")
		if len(parts) >= 2 {
			playerID := parts[0]
			seasonID := parts[1]
			h.GetCurrencyBalance(w, r, playerID, seasonID)
		} else {
			http.Error(w, "Invalid currency path", http.StatusBadRequest)
		}
	case http.MethodPost:
		if strings.Contains(path, "/earn") {
			// POST /api/v1/currency/earn
			h.EarnCurrency(w, r)
		} else if strings.Contains(path, "/spend") {
			// POST /api/v1/currency/spend
			h.SpendCurrency(w, r)
		} else if strings.Contains(path, "/exchange") {
			// POST /api/v1/currency/exchange
			h.ExchangeCurrency(w, r)
		} else {
			http.Error(w, "Invalid currency operation", http.StatusBadRequest)
		}
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// Placeholder implementations - TODO: implement full business logic

func (h *Handler) ListSeasons(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	response := map[string]interface{}{
		"seasons": []map[string]interface{}{
			{
				"id":          "summer-2026",
				"name":        "Summer Championship 2026",
				"status":      "active",
				"start_date":  "2026-06-01T00:00:00Z",
				"end_date":    "2026-08-31T23:59:59Z",
			},
		},
		"pagination": map[string]interface{}{
			"page":  1,
			"limit": 20,
			"total": 1,
		},
	}
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) CreateSeason(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	response := map[string]interface{}{
		"id":          "summer-2026",
		"name":        "Summer Championship 2026",
		"status":      "upcoming",
		"start_date":  "2026-06-01T00:00:00Z",
		"end_date":    "2026-08-31T23:59:59Z",
	}
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) GetSeason(w http.ResponseWriter, r *http.Request, seasonID string) {
	w.Header().Set("Content-Type", "application/json")
	response := map[string]interface{}{
		"id":          seasonID,
		"name":        "Summer Championship 2026",
		"status":      "active",
		"start_date":  "2026-06-01T00:00:00Z",
		"end_date":    "2026-08-31T23:59:59Z",
		"challenges":  []string{"challenge-1", "challenge-2"},
	}
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) UpdateSeason(w http.ResponseWriter, r *http.Request, seasonID string) {
	w.Header().Set("Content-Type", "application/json")
	response := map[string]interface{}{
		"id":          seasonID,
		"name":        "Summer Championship 2026",
		"status":      "active",
		"start_date":  "2026-06-01T00:00:00Z",
		"end_date":    "2026-08-31T23:59:59Z",
	}
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) UpdateProgress(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	response := map[string]interface{}{
		"player_id":      "player-123",
		"challenge_id":   "challenge-1",
		"current_value":  35,
		"is_completed":   false,
		"progress_made":  true,
	}
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) GetLeaderboard(w http.ResponseWriter, r *http.Request, seasonID string) {
	w.Header().Set("Content-Type", "application/json")
	response := map[string]interface{}{
		"season_id":       seasonID,
		"total_players":   10000,
		"last_updated":    "2026-07-15T12:00:00Z",
		"entries": []map[string]interface{}{
			{
				"rank":               1,
				"player_name":        "CyberNinja_2077",
				"score":              125000,
				"challenges_completed": 23,
			},
			{
				"rank":               2,
				"player_name":        "NetRunner_X",
				"score":              118500,
				"challenges_completed": 22,
			},
		},
	}
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) ClaimRewards(w http.ResponseWriter, r *http.Request, playerID string) {
	w.Header().Set("Content-Type", "application/json")
	response := map[string]interface{}{
		"player_id":       playerID,
		"season_id":       "summer-2026",
		"claimed_at":      "2026-07-15T14:30:00Z",
		"rewards": []map[string]interface{}{
			{
				"type":   "currency",
				"amount": 50000,
			},
			{
				"type":   "item",
				"item_id": "legendary-cyberware",
			},
		},
	}
	json.NewEncoder(w).Encode(response)
}

// GetCurrencyBalance returns player's seasonal currency balance
func (h *Handler) GetCurrencyBalance(w http.ResponseWriter, r *http.Request, playerID, seasonID string) {
	currency, err := h.service.GetCurrencyBalance(r.Context(), playerID, seasonID)
	if err != nil {
		h.logger.Error("Failed to get currency balance", zap.Error(err))
		http.Error(w, "Failed to get currency balance", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(currency)
}

// EarnCurrency awards currency to player
func (h *Handler) EarnCurrency(w http.ResponseWriter, r *http.Request) {
	var req EarnCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	transaction, _, err := h.service.EarnSeasonalCurrency(r.Context(), req)
	if err != nil {
		h.logger.Error("Failed to earn currency", zap.Error(err))
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(transaction)
}

// SpendCurrency deducts currency from player
func (h *Handler) SpendCurrency(w http.ResponseWriter, r *http.Request) {
	var req SpendCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	transaction, _, err := h.service.SpendSeasonalCurrency(r.Context(), req)
	if err != nil {
		h.logger.Error("Failed to spend currency", zap.Error(err))
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(transaction)
}

// ExchangeCurrency converts seasonal currency to rewards
func (h *Handler) ExchangeCurrency(w http.ResponseWriter, r *http.Request) {
	var req ExchangeCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	exchange, err := h.service.ExchangeSeasonalCurrency(r.Context(), req)
	if err != nil {
		h.logger.Error("Failed to exchange currency", zap.Error(err))
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(exchange)
}

// Additional data structures for seasons API
type Service interface {
	CreateSeason(ctx context.Context, req CreateSeasonRequest) (*Season, error)
	GetSeason(ctx context.Context, seasonID string) (*Season, error)
	UpdateSeason(ctx context.Context, seasonID string, req UpdateSeasonRequest) (*Season, error)
	ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error)
	EarnSeasonalCurrency(ctx context.Context, req EarnCurrencyRequest) (*CurrencyTransaction, error)
	SpendSeasonalCurrency(ctx context.Context, req SpendCurrencyRequest) (*CurrencyTransaction, error)
	ExchangeSeasonalCurrency(ctx context.Context, req ExchangeCurrencyRequest) (*CurrencyExchange, error)
	GetCurrencyBalance(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error)
	UpdateChallengeProgress(ctx context.Context, req UpdateProgressRequest) (*ChallengeProgress, error)
	GetLeaderboard(ctx context.Context, seasonID string, limit int) (*SeasonLeaderboard, error)
}

type Repository interface {
	CreateSeason(ctx context.Context, season *Season) error
	GetSeason(ctx context.Context, seasonID string) (*Season, error)
	UpdateSeason(ctx context.Context, season *Season) error
	ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error)
	GetSeasonalCurrency(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error)
	CreateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error
	UpdateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error
	ExecuteCurrencyTransaction(ctx context.Context, tx *CurrencyTransaction, currency *SeasonalCurrency) error
	GetCurrencyTransactions(ctx context.Context, playerID, seasonID string, limit int) ([]*CurrencyTransaction, error)
	CreateCurrencyExchange(ctx context.Context, exchange *CurrencyExchange) error
	GetPlayerExchangeSpent(ctx context.Context, playerID, seasonID, exchangeType string) (int, error)
	GetDailyExchangeSpent(ctx context.Context, seasonID, exchangeType string) (int, error)
}

// MockService provides mock implementations for testing
type MockService struct {
	logger    *zap.Logger
	websocket *WebSocketServer
	seasons   map[string]*Season
}

func (m *MockService) CreateSeason(ctx context.Context, req CreateSeasonRequest) (*Season, error) {
	seasonID := fmt.Sprintf("season-%d", len(m.seasons)+1)
	season := &Season{
		ID:            seasonID,
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

	if m.seasons == nil {
		m.seasons = make(map[string]*Season)
	}
	m.seasons[seasonID] = season

	return season, nil
}

func (m *MockService) GetSeason(ctx context.Context, seasonID string) (*Season, error) {
	if season, exists := m.seasons[seasonID]; exists {
		return season, nil
	}
	return nil, fmt.Errorf("season not found")
}

func (m *MockService) UpdateSeason(ctx context.Context, seasonID string, req UpdateSeasonRequest) (*Season, error) {
	season, exists := m.seasons[seasonID]
	if !exists {
		return nil, fmt.Errorf("season not found")
	}

	if req.Name != "" {
		season.Name = req.Name
	}
	if req.Description != "" {
		season.Description = req.Description
	}
	if !req.StartDate.IsZero() {
		season.StartDate = req.StartDate
	}
	if !req.EndDate.IsZero() {
		season.EndDate = req.EndDate
	}
	if req.CurrencyLimit > 0 {
		season.CurrencyLimit = req.CurrencyLimit
	}
	if req.Status != "" {
		season.Status = req.Status
	}

	season.UpdatedAt = time.Now()
	season.Version++

	return season, nil
}

func (m *MockService) ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error) {
	var seasons []*Season
	for _, season := range m.seasons {
		if filter.Status == "" || season.Status == filter.Status {
			seasons = append(seasons, season)
		}
	}
	return seasons, nil
}

func (m *MockService) EarnSeasonalCurrency(ctx context.Context, req EarnCurrencyRequest) (*CurrencyTransaction, *CurrencyRollback, error) {
	transaction := &CurrencyTransaction{
		ID:           fmt.Sprintf("tx-%d", time.Now().Unix()),
		PlayerID:     req.PlayerID,
		SeasonID:     req.SeasonID,
		Type:         "earn",
		Amount:       req.Amount,
		BalanceAfter: req.Amount, // Mock balance
		Reason:       req.Reason,
		CreatedAt:    time.Now(),
	}

	rollback := &CurrencyRollback{
		playerID:   req.PlayerID,
		seasonID:   req.SeasonID,
		amount:     req.Amount,
		operation:  "earn",
		repository: nil, // Mock doesn't need real rollback
		logger:     m.logger,
	}

	return transaction, rollback, nil
}

func (m *MockService) SpendSeasonalCurrency(ctx context.Context, req SpendCurrencyRequest) (*CurrencyTransaction, *CurrencyRollback, error) {
	transaction := &CurrencyTransaction{
		ID:           fmt.Sprintf("tx-%d", time.Now().Unix()),
		PlayerID:     req.PlayerID,
		SeasonID:     req.SeasonID,
		Type:         "spend",
		Amount:       req.Amount,
		BalanceAfter: 1000 - req.Amount, // Mock balance
		Reason:       req.Reason,
		CreatedAt:    time.Now(),
	}

	rollback := &CurrencyRollback{
		playerID:   req.PlayerID,
		seasonID:   req.SeasonID,
		amount:     req.Amount,
		operation:  "spend",
		repository: nil, // Mock doesn't need real rollback
		logger:     m.logger,
	}

	return transaction, rollback, nil
}

func (m *MockService) ExchangeSeasonalCurrency(ctx context.Context, req ExchangeCurrencyRequest) (*CurrencyExchange, error) {
	return &CurrencyExchange{
		ID:             fmt.Sprintf("exchange-%d", time.Now().Unix()),
		PlayerID:       req.PlayerID,
		SeasonID:       req.SeasonID,
		CurrencyAmount: req.CurrencyAmount,
		ExchangeType:   req.ExchangeType,
		Reward:         map[string]interface{}{"type": "mock_reward"},
		ExchangeRate:   1.0,
		CreatedAt:      time.Now(),
	}, nil
}

func (m *MockService) GetCurrencyBalance(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error) {
	return &SeasonalCurrency{
		SeasonID:    seasonID,
		PlayerID:    playerID,
		Balance:     1000,
		EarnedTotal: 1500,
		SpentTotal:  500,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockService) UpdateChallengeProgress(ctx context.Context, req UpdateProgressRequest) (*ChallengeProgress, error) {
	return &ChallengeProgress{
		PlayerID:    req.PlayerID,
		ChallengeID: req.ChallengeID,
		CurrentValue: req.ProgressValue,
		IsCompleted: false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockService) GetLeaderboard(ctx context.Context, seasonID string, limit int) (*SeasonLeaderboard, error) {
	return &SeasonLeaderboard{
		SeasonID:    seasonID,
		TotalPlayers: 100,
		LastUpdated: time.Now(),
		Entries: []LeaderboardEntry{
			{Rank: 1, PlayerName: "Player1", Score: 1000},
			{Rank: 2, PlayerName: "Player2", Score: 900},
		},
	}, nil
}

// MockRepository provides mock implementations for testing
type MockRepository struct {
	challenges map[string]*Challenge
}

func (m *MockRepository) CreateSeason(ctx context.Context, season *Season) error {
	return nil
}

func (m *MockRepository) GetSeason(ctx context.Context, seasonID string) (*Season, error) {
	return &Season{
		ID:            seasonID,
		Name:          "Mock Season",
		Description:   "Mock season for testing",
		StartDate:     time.Now(),
		EndDate:       time.Now().Add(24 * time.Hour),
		Status:        "active",
		CurrencyLimit: 10000,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
		Version:       1,
	}, nil
}

func (m *MockRepository) UpdateSeason(ctx context.Context, season *Season) error {
	return nil
}

func (m *MockRepository) ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error) {
	return []*Season{
		{
			ID:            "season-1",
			Name:          "Summer Championship",
			Status:        "active",
			StartDate:     time.Now(),
			EndDate:       time.Now().Add(24 * time.Hour),
			CurrencyLimit: 10000,
			CreatedAt:     time.Now(),
			UpdatedAt:     time.Now(),
			Version:       1,
		},
	}, nil
}

func (m *MockRepository) GetChallenge(ctx context.Context, challengeID string) (*Challenge, error) {
	if m.challenges == nil {
		m.challenges = map[string]*Challenge{
			"challenge-1": {
				ID:               "challenge-1",
				SeasonID:         "season-1",
				Name:             "Data Fortress Assault",
				ChallengeType:    "hacking",
				Difficulty:       "hard",
				MaxScore:         10000,
				TimeLimitSeconds: 1800,
				MinParticipants:  1,
				MaxParticipants:  4,
				IsTeamBased:      false,
				EntryFee:         0,
				RewardMultiplier: 1.5,
				CreatedAt:        time.Now(),
				UpdatedAt:        time.Now(),
				Version:          1,
			},
		}
	}

	if challenge, exists := m.challenges[challengeID]; exists {
		return challenge, nil
	}
	return nil, fmt.Errorf("challenge not found")
}

func (m *MockRepository) GetChallengeObjectives(ctx context.Context, challengeID string) ([]*ChallengeObjective, error) {
	return []*ChallengeObjective{
		{
			ID:            "obj-1",
			ChallengeID:   challengeID,
			ObjectiveType: "kill_count",
			TargetValue:   50,
			Description:   "Eliminate 50 enemy combatants",
			ProgressType:  "cumulative",
			IsOptional:    false,
			RewardWeight:  0.3,
			CreatedAt:     time.Now(),
		},
		{
			ID:            "obj-2",
			ChallengeID:   challengeID,
			ObjectiveType: "score_threshold",
			TargetValue:   7500,
			Description:   "Achieve score of 7500 or higher",
			ProgressType:  "threshold",
			IsOptional:    false,
			RewardWeight:  0.7,
			CreatedAt:     time.Now(),
		},
	}, nil
}

func (m *MockRepository) GetSeasonalCurrency(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error) {
	return &SeasonalCurrency{
		SeasonID:    seasonID,
		PlayerID:    playerID,
		Balance:     1000,
		EarnedTotal: 1500,
		SpentTotal:  500,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockRepository) CreateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error {
	return nil
}

func (m *MockRepository) UpdateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error {
	return nil
}

func (m *MockRepository) ExecuteCurrencyTransaction(ctx context.Context, tx *CurrencyTransaction, currency *SeasonalCurrency) error {
	return nil
}

func (m *MockRepository) GetCurrencyTransactions(ctx context.Context, playerID, seasonID string, limit int) ([]*CurrencyTransaction, error) {
	return []*CurrencyTransaction{
		{
			ID:           "tx-1",
			PlayerID:     playerID,
			SeasonID:     seasonID,
			Type:         "earn",
			Amount:       500,
			BalanceAfter: 1000,
			Reason:       "challenge_completed",
			CreatedAt:    time.Now(),
		},
	}, nil
}

func (m *MockRepository) CreateCurrencyExchange(ctx context.Context, exchange *CurrencyExchange) error {
	return nil
}

func (m *MockRepository) GetPlayerExchangeSpent(ctx context.Context, playerID, seasonID, exchangeType string) (int, error) {
	return 0, nil
}

func (m *MockRepository) GetDailyExchangeSpent(ctx context.Context, seasonID, exchangeType string) (int, error) {
	return 0, nil
}

// Additional data structures needed for handlers
type SeasonFilter struct {
	Status string
	Limit  int
	Cursor string
}

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

type UpdateProgressRequest struct {
	PlayerID     string `json:"player_id"`
	ChallengeID  string `json:"challenge_id"`
	ProgressValue int    `json:"progress_value"`
}

type EarnCurrencyRequest struct {
	PlayerID string `json:"player_id"`
	SeasonID string `json:"season_id"`
	Amount   int    `json:"amount"`
	Reason   string `json:"reason"`
}

type SpendCurrencyRequest struct {
	PlayerID string `json:"player_id"`
	SeasonID string `json:"season_id"`
	Amount   int    `json:"amount"`
	Reason   string `json:"reason"`
}

type ExchangeCurrencyRequest struct {
	PlayerID       string `json:"player_id"`
	SeasonID       string `json:"season_id"`
	CurrencyAmount int    `json:"currency_amount"`
	ExchangeType   string `json:"exchange_type"`
}

type CurrencyTransaction struct {
	ID           string `json:"id"`
	PlayerID     string `json:"player_id"`
	SeasonID     string `json:"season_id"`
	Type         string `json:"type"`
	Amount       int    `json:"amount"`
	BalanceAfter int    `json:"balance_after"`
	Reason       string `json:"reason"`
	CreatedAt    time.Time `json:"created_at"`
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
	CreatedAt   time.Time `json:"created_at"`
	UpdatedAt   time.Time `json:"updated_at"`
}

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
	Tier     string `json:"tier"`
	MinScore int    `json:"min_score"`
	Rewards  []Reward `json:"rewards"`
}

type Reward struct {
	Type   string `json:"type"`
	Amount int    `json:"amount"`
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
	PlayerID         string             `json:"player_id"`
	ChallengeID      string             `json:"challenge_id"`
	CurrentValue     int                `json:"current_value"`
	IsCompleted      bool               `json:"is_completed"`
	CompletedAt      *time.Time         `json:"completed_at"`
	CreatedAt        time.Time          `json:"created_at"`
	UpdatedAt        time.Time          `json:"updated_at"`
	ObjectiveProgress []*ObjectiveProgress `json:"objective_progress,omitempty"`
}

type ObjectiveProgress struct {
	ObjectiveID   string    `json:"objective_id"`
	CurrentValue  int       `json:"current_value"`
	IsCompleted   bool      `json:"is_completed"`
	CompletedAt   *time.Time `json:"completed_at"`
}

type ChallengeObjective struct {
	ID            string                 `json:"id"`
	ChallengeID   string                 `json:"challenge_id"`
	ObjectiveType string                 `json:"objective_type"`
	TargetValue   int                    `json:"target_value"`
	Description   string                 `json:"description"`
	ProgressType  string                 `json:"progress_type"`
	IsOptional    bool                   `json:"is_optional"`
	RewardWeight  float64                `json:"reward_weight"`
	Metadata      map[string]interface{} `json:"metadata"`
	CreatedAt     time.Time              `json:"created_at"`
}

type SeasonLeaderboard struct {
	SeasonID     string            `json:"season_id"`
	TotalPlayers int               `json:"total_players"`
	LastUpdated  time.Time         `json:"last_updated"`
	Entries      []LeaderboardEntry `json:"entries"`
}

type LeaderboardEntry struct {
	Rank               int    `json:"rank"`
	PlayerName         string `json:"player_name"`
	Score              int    `json:"score"`
	ChallengesCompleted int    `json:"challenges_completed"`
}