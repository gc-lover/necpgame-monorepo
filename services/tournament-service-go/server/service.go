package server

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"sort"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// TournamentServiceConfig содержит базовую конфигурацию
type TournamentServiceDependencies struct {
	Logger      *logrus.Logger
	Metrics     *TournamentMetrics
	Config      *TournamentServiceConfig
	RedisClient *redis.Client
}

// TournamentServiceStorage содержит хранилища данных
type TournamentServiceStorage struct {
	Tournaments   sync.Map
	Matches       sync.Map
	Registrations sync.Map
	Rankings      sync.Map
	Leagues       sync.Map
}

// TournamentServicePools содержит пулы памяти
type TournamentServicePools struct {
	TournamentResponsePool sync.Pool
	MatchResponsePool      sync.Pool
	RankingResponsePool    sync.Pool
	LeagueResponsePool     sync.Pool
	RewardResponsePool     sync.Pool
	StatsResponsePool      sync.Pool
}

// OPTIMIZATION: Issue #2177 - Memory-aligned struct for tournament service performance
type TournamentService struct {
	TournamentServiceDependencies
	TournamentServiceStorage
	TournamentServicePools
}

// OPTIMIZATION: Issue #2177 - Memory-aligned tournament structs
type Tournament struct {
	TournamentID        string            `json:"tournament_id"`         // 16 bytes
	Name                string            `json:"name"`                  // 16 bytes
	Description         string            `json:"description"`           // 16 bytes
	GameMode            string            `json:"game_mode"`             // 16 bytes
	Format              string            `json:"format"`                // 16 bytes (e.g., "single_elimination", "round_robin")
	Status              string            `json:"status"`                // 16 bytes
	MaxParticipants     int               `json:"max_participants"`      // 8 bytes
	MinParticipants     int               `json:"min_participants"`      // 8 bytes
	CurrentParticipants int               `json:"current_participants"`  // 8 bytes
	RegistrationStart   time.Time         `json:"registration_start"`    // 24 bytes
	RegistrationEnd     time.Time         `json:"registration_end"`      // 24 bytes
	StartTime           time.Time         `json:"start_time"`            // 24 bytes
	EndTime             time.Time         `json:"end_time"`              // 24 bytes
	Rules               map[string]string `json:"rules"`                 // 8 bytes (map)
	Prizes              []Prize           `json:"prizes"`                // 24 bytes (slice)
	EntryFee            EntryFee          `json:"entry_fee"`             // ~32 bytes
	Visibility          string            `json:"visibility"`            // 16 bytes
	RegionRestrictions  []string          `json:"region_restrictions"`   // 24 bytes (slice)
	SkillRequirements   SkillRequirements `json:"skill_requirements"`    // ~64 bytes
	AutoProgression     bool              `json:"auto_progression"`      // 1 byte
	MatchTimeout        time.Duration     `json:"match_timeout"`         // 8 bytes
	AllowSpectators     bool              `json:"allow_spectators"`      // 1 byte
	StreamingEnabled    bool              `json:"streaming_enabled"`     // 1 byte
	CurrentRound        int               `json:"current_round"`         // 8 bytes
	TotalRounds         int               `json:"total_rounds"`          // 8 bytes
	Bracket             []Match           `json:"-"`                     // 24 bytes (slice, not serialized directly)
	CreatedAt           time.Time         `json:"created_at"`            // 24 bytes
	UpdatedAt           time.Time         `json:"updated_at"`            // 24 bytes
	LastActivity        time.Time         `json:"last_activity"`         // 24 bytes
}

// OPTIMIZATION: Issue #2177 - Memory-aligned Match struct
type Match struct {
	MatchID       string    `json:"match_id"`       // 16 bytes
	TournamentID  string    `json:"tournament_id"`  // 16 bytes
	Round         int       `json:"round"`          // 8 bytes
	Position      int       `json:"position"`       // 8 bytes
	Player1ID     string    `json:"player1_id"`     // 16 bytes
	Player2ID     string    `json:"player2_id"`     // 16 bytes
	WinnerID      string    `json:"winner_id"`      // 16 bytes
	Status        string    `json:"status"`         // 16 bytes (e.g., "scheduled", "in_progress", "completed")
	ScheduledTime time.Time `json:"scheduled_time"` // 24 bytes
	StartedTime   time.Time `json:"started_time"`   // 24 bytes
	CompletedTime time.Time `json:"completed_time"` // 24 bytes
	Score         MatchScore `json:"score"`         // ~64 bytes
	Spectators    []string  `json:"spectators"`     // 24 bytes (slice)
	StreamingURL  string    `json:"streaming_url"`  // 16 bytes
	Metadata      map[string]interface{} `json:"metadata"` // 8 bytes (map)
}

// OPTIMIZATION: Issue #2177 - Memory-aligned PlayerRanking struct
type PlayerRanking struct {
	PlayerID      string            `json:"player_id"`      // 16 bytes
	DisplayName   string            `json:"display_name"`   // 16 bytes
	Rating        int               `json:"rating"`         // 8 bytes (e.g., ELO)
	Rank          string            `json:"rank"`           // 16 bytes (e.g., "Bronze", "Silver")
	Wins          int               `json:"wins"`           // 8 bytes
	Losses        int               `json:"losses"`          // 8 bytes
	WinRate       float64           `json:"win_rate"`       // 8 bytes
	Streak        int               `json:"streak"`         // 8 bytes (win/loss streak)
	LastActive    time.Time         `json:"last_active"`    // 24 bytes
	Achievements  []string          `json:"achievements"`   // 24 bytes (slice)
	GameModeStats map[string]PlayerStats `json:"game_mode_stats"` // 8 bytes (map)
	OverallStats  PlayerStats       `json:"overall_stats"`  // ~64 bytes
	UpdatedAt     time.Time         `json:"updated_at"`     // 24 bytes
}

// OPTIMIZATION: Issue #2177 - Memory-aligned League struct
type League struct {
	LeagueID        string            `json:"league_id"`        // 16 bytes
	Name            string            `json:"name"`             // 16 bytes
	Description     string            `json:"description"`      // 16 bytes
	GameMode        string            `json:"game_mode"`        // 16 bytes
	Status          string            `json:"status"`           // 16 bytes (e.g., "active", "completed")
	CurrentSeason   int               `json:"current_season"`   // 8 bytes
	MaxTeams        int               `json:"max_teams"`        // 8 bytes
	CurrentTeams    int               `json:"current_teams"`    // 8 bytes
	SeasonStartTime time.Time         `json:"season_start_time"` // 24 bytes
	SeasonEndTime   time.Time         `json:"season_end_time"`   // 24 bytes
	Rules           map[string]string `json:"rules"`            // 8 bytes (map)
	Prizes          []Prize           `json:"prizes"`           // 24 bytes (slice)
	Region          string            `json:"region"`           // 16 bytes
	Teams           sync.Map          `json:"-"`                // map[string]*LeagueTeam - thread-safe
	CreatedAt       time.Time         `json:"created_at"`       // 24 bytes
	UpdatedAt       time.Time         `json:"updated_at"`       // 24 bytes
}

// Helper structs (memory-aligned)
type Prize struct {
	Position    int    `json:"position"`     // 8 bytes
	RewardType  string `json:"reward_type"`  // 16 bytes
	RewardValue string `json:"reward_value"` // 16 bytes
	Description string `json:"description"`  // 16 bytes
}

type EntryFee struct {
	CurrencyType string `json:"currency_type"` // 16 bytes
	Amount       int    `json:"amount"`        // 8 bytes
}

type SkillRequirements struct {
	MinLevel           int      `json:"min_level"`            // 8 bytes
	MaxLevel           int      `json:"max_level"`            // 8 bytes
	MinRank            string   `json:"min_rank"`             // 16 bytes
	MaxRank            string   `json:"max_rank"`             // 16 bytes
	RequiredAchievements []string `json:"required_achievements"` // 24 bytes (slice)
}

type MatchScore struct {
	Player1Score  int                    `json:"player1_score"`   // 8 bytes
	Player2Score  int                    `json:"player2_score"`   // 8 bytes
	WinnerID      string                 `json:"winner_id"`       // 16 bytes
	MatchDuration time.Duration          `json:"match_duration"`  // 8 bytes
	Statistics    map[string]interface{} `json:"statistics"`      // 8 bytes (map)
}

type PlayerStats struct {
	TotalMatches        int     `json:"total_matches"`         // 8 bytes
	TotalWins           int     `json:"total_wins"`            // 8 bytes
	TotalLosses         int     `json:"total_losses"`          // 8 bytes
	WinRate             float64 `json:"win_rate"`              // 8 bytes
	CurrentStreak       int     `json:"current_streak"`        // 8 bytes
	LongestWinStreak    int     `json:"longest_win_streak"`    // 8 bytes
	FavoriteGameMode    string  `json:"favorite_game_mode"`    // 16 bytes
	TotalPrizesWon      int     `json:"total_prizes_won"`      // 8 bytes
	TotalEarnings       int     `json:"total_earnings"`        // 8 bytes
	JoinedAt            time.Time `json:"joined_at"`           // 24 bytes
}

type Registration struct {
	RegistrationID string    `json:"registration_id"` // 16 bytes
	TournamentID   string    `json:"tournament_id"`   // 16 bytes
	PlayerID       string    `json:"player_id"`       // 16 bytes
	TeamID         string    `json:"team_id"`         // 16 bytes (optional)
	RegisteredAt   time.Time `json:"registered_at"`   // 24 bytes
	Status         string    `json:"status"`          // 16 bytes (e.g., "pending", "confirmed", "cancelled")
	Seed           int       `json:"seed"`            // 8 bytes
}

type LeagueTeam struct {
	TeamID      string    `json:"team_id"`      // 16 bytes
	Name        string    `json:"name"`         // 16 bytes
	MemberIDs   []string  `json:"member_ids"`   // 24 bytes (slice)
	CaptainID   string    `json:"captain_id"`   // 16 bytes
	Rating      int       `json:"rating"`       // 8 bytes
	Wins        int       `json:"wins"`         // 8 bytes
	Losses      int       `json:"losses"`       // 8 bytes
	CreatedAt   time.Time `json:"created_at"`   // 24 bytes
}

func NewTournamentService(logger *logrus.Logger, metrics *TournamentMetrics, config *TournamentServiceConfig) *TournamentService {
	s := &TournamentService{
		logger:  logger,
		metrics: metrics,
		config:  config,
	}

	// Initialize Redis client
	s.redisClient = redis.NewClient(&redis.Options{
		Addr:         config.RedisAddr,
		Password:     "",
		DB:           0,
		DialTimeout:  5 * time.Second,
		ReadTimeout:  3 * time.Second,
		WriteTimeout: 3 * time.Second,
		PoolSize:     10,
		MinIdleConns: 2,
	})

	// OPTIMIZATION: Issue #2177 - Initialize memory pools (zero allocations target!)
	s.tournamentResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateTournamentResponse{}
		},
	}
	s.matchResponsePool = sync.Pool{
		New: func() interface{} {
			return &Match{}
		},
	}
	s.rankingResponsePool = sync.Pool{
		New: func() interface{} {
			return &PlayerRanking{}
		},
	}
	s.leagueResponsePool = sync.Pool{
		New: func() interface{} {
			return &League{}
		},
	}
	s.rewardResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetTournamentRewardsResponse{}
		},
	}
	s.statsResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetGlobalStatisticsResponse{}
		},
	}

	// Start background goroutines for tournament lifecycle management
	go s.tournamentScheduler()
	go s.matchMonitor()
	go s.rankingUpdater()
	go s.leagueManager()

	return s
}

// OPTIMIZATION: Issue #2177 - Rate limiting middleware for tournament protection
func (s *TournamentService) RateLimitMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			playerID := r.Header.Get("X-Player-ID")
			if playerID == "" {
				playerID = r.RemoteAddr // Fallback to IP
			}

			// Moderate limits for tournament operations (social features)
			limiter, _ := s.rateLimiters.LoadOrStore(playerID, rate.NewLimiter(200, 400)) // 200 req/sec burst 400

			if !limiter.(*rate.Limiter).Allow() {
				s.logger.WithField("player_id", playerID).Warn("tournament API rate limit exceeded")
				s.metrics.ValidationErrors.Inc()
				http.Error(w, "Too many requests", http.StatusTooManyRequests)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// Health check method
func (s *TournamentService) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"status": "healthy",
		"service": "tournament-service",
		"version": "1.0.0",
		"active_tournaments": s.metrics.ActiveTournaments,
		"active_matches": s.metrics.ActiveMatches,
		"total_participants": s.metrics.TotalParticipants,
		"active_leagues": s.metrics.ActiveLeagues,
		"timestamp": time.Now().Unix(),
	})
}

// Tournament Management Handlers
func (s *TournamentService) CreateTournament(w http.ResponseWriter, r *http.Request) {
	var req CreateTournamentRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create tournament request")
		s.metrics.ValidationErrors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate tournament data
	if err := s.validateTournamentRequest(&req); err != nil {
		s.logger.WithError(err).Error("tournament validation failed")
		s.metrics.ValidationErrors.Inc()
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	tournament := &Tournament{
		TournamentID:        req.TournamentID,
		Name:                req.Name,
		Description:         req.Description,
		GameMode:            req.GameMode,
		Format:              req.TournamentFormat,
		Status:              "draft",
		MaxParticipants:     req.MaxParticipants,
		MinParticipants:     req.MinParticipants,
		RegistrationStart:   time.Unix(req.RegistrationStartTime, 0),
		RegistrationEnd:     time.Unix(req.RegistrationEndTime, 0),
		StartTime:           time.Unix(req.StartTime, 0),
		EndTime:             time.Unix(req.EndTime, 0),
		Rules:               req.Rules,
		Prizes:              req.Prizes,
		EntryFee:            req.EntryFee,
		Visibility:          req.Visibility,
		RegionRestrictions:  req.RegionRestrictions,
		SkillRequirements:   req.SkillRequirements,
		AutoProgression:     req.AutoProgression,
		MatchTimeout:        time.Duration(req.MatchTimeout) * time.Second,
		AllowSpectators:     req.AllowSpectators,
		StreamingEnabled:    req.StreamingEnabled,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
		LastActivity:        time.Now(),
	}

	s.tournaments.Store(tournament.TournamentID, tournament)
	s.metrics.ActiveTournaments.Inc()

	// Persist to Redis
	s.saveTournamentToRedis(r.Context(), tournament)

	resp := s.tournamentResponsePool.Get().(*CreateTournamentResponse)
	defer s.tournamentResponsePool.Put(resp)

	resp.TournamentID = tournament.TournamentID
	resp.Name = tournament.Name
	resp.Status = tournament.Status
	resp.MemberCount = tournament.CurrentParticipants
	resp.CreatedAt = tournament.CreatedAt.Unix()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("tournament_id", tournament.TournamentID).Info("tournament created successfully")
}

func (s *TournamentService) ListTournaments(w http.ResponseWriter, r *http.Request) {
	statusFilter := r.URL.Query().Get("status")
	gameModeFilter := r.URL.Query().Get("game_mode")
	limit := 20 // Default limit
	offset := 0 // Default offset

	var tournaments []*TournamentSummary
	s.tournaments.Range(func(key, value interface{}) bool {
		tournament := value.(*Tournament)

		if statusFilter != "" && tournament.Status != statusFilter {
			return true
		}
		if gameModeFilter != "" && tournament.GameMode != gameModeFilter {
			return true
		}

		summary := &TournamentSummary{
			TournamentID:    tournament.TournamentID,
			Name:            tournament.Name,
			GameMode:        tournament.GameMode,
			Status:          tournament.Status,
			ParticipantCount: tournament.CurrentParticipants,
			MaxParticipants: tournament.MaxParticipants,
			StartTime:       tournament.StartTime.Unix(),
			EndTime:         tournament.EndTime.Unix(),
			RegionRestrictions: tournament.RegionRestrictions,
			CreatedAt:       tournament.CreatedAt.Unix(),
		}
		tournaments = append(tournaments, summary)
		return true
	})

	// Apply pagination
	start := offset
	end := start + limit
	if end > len(tournaments) {
		end = len(tournaments)
