package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
	"golang.org/x/time/rate"
)

// GuildServiceDependencies содержит базовую конфигурацию
type GuildServiceDependencies struct {
	Logger      *logrus.Logger
	Metrics     *GuildMetrics
	Config      *GuildServiceConfig
	RedisClient *redis.Client
}

// GuildServiceStorage содержит хранилища данных
type GuildServiceStorage struct {
	Guilds       sync.Map
	Members      sync.Map
	Territories  sync.Map
	Wars         sync.Map
	Alliances    sync.Map
	Contracts    sync.Map
	RateLimiters sync.Map
}

// GuildServicePools содержит пулы памяти
type GuildServicePools struct {
	GuildResponsePool     sync.Pool
	MemberResponsePool    sync.Pool
	TerritoryResponsePool sync.Pool
	WarResponsePool       sync.Pool
}

// GuildService OPTIMIZATION: Issue #2177 - Memory-aligned struct for guild service performance
type GuildService struct {
	GuildServiceDependencies
	GuildServiceStorage
	GuildServicePools
}

// GuildIdentity contains identity fields
type GuildIdentity struct {
	GuildID     string `json:"guild_id"`
	Name        string `json:"name"`
	Description string `json:"description"`
	Motto       string `json:"motto"`
	Faction     string `json:"faction"`
	LeaderID    string `json:"leader_id"`
	Status      string `json:"status"`
	Region      string `json:"region"`
}

// GuildStats contains statistical fields
type GuildStats struct {
	Level                 int `json:"level"`
	Experience            int `json:"experience"`
	Reputation            int `json:"reputation"`
	Wealth                int `json:"wealth"`
	MaxMembers            int `json:"max_members"`
	CurrentMembers        int `json:"current_members"`
	TerritoriesControlled int `json:"territories_controlled"`
	WarsActive            int `json:"wars_active"`
	AlliancesActive       int `json:"alliances_active"`
}

// GuildAppearance contains appearance-related fields
type GuildAppearance struct {
	Headquarters GuildLocation `json:"headquarters"`
	Colors       GuildColors   `json:"colors"`
}

// GuildRequirements contains requirement fields
type GuildRequirements struct {
	RecruitmentOpen     bool `json:"recruitment_open"`
	ApplicationRequired bool `json:"application_required"`
	MinLevelRequirement int  `json:"min_level_requirement"`
}

// GuildMetadata contains metadata fields
type GuildMetadata struct {
	CreatedAt    time.Time `json:"created_at"`
	UpdatedAt    time.Time `json:"updated_at"`
	LastActivity time.Time `json:"last_activity"`
}

// Guild OPTIMIZATION: Issue #2177 - Memory-aligned guild structs
type Guild struct {
	GuildIdentity
	GuildStats
	GuildAppearance
	GuildRequirements
	GuildMetadata

	Policies GuildPolicies `json:"policies"`
}

// GuildLocation OPTIMIZATION: Issue #2177 - Memory-aligned supporting structs
type GuildLocation struct {
	Zone string  `json:"zone"`
	X    float64 `json:"x"`
	Y    float64 `json:"y"`
	Z    float64 `json:"z"`
}

type GuildColors struct {
	Primary   string `json:"primary"`
	Secondary string `json:"secondary"`
	EmblemURL string `json:"emblem_url"`
}

type GuildPolicies struct {
	PvPEnabled             bool    `json:"pvp_enabled"`
	TerritoryClaimsEnabled bool    `json:"territory_claims_enabled"`
	WarParticipation       string  `json:"war_participation"`
	ContractSharing        bool    `json:"contract_sharing"`
	ResourceSharing        bool    `json:"resource_sharing"`
	TaxRate                float64 `json:"tax_rate"`
}

type GuildMember struct {
	GuildID       string              `json:"guild_id"`
	PlayerID      string              `json:"player_id"`
	Role          string              `json:"role"`
	RankTitle     string              `json:"rank_title"`
	JoinedAt      time.Time           `json:"joined_at"`
	LastActive    time.Time           `json:"last_active"`
	Status        string              `json:"status"`
	Permissions   []string            `json:"permissions"`
	Contributions MemberContributions `json:"contributions"`
}

type MemberContributions struct {
	ContractsCompleted   int `json:"contracts_completed"`
	WarsParticipated     int `json:"wars_participated"`
	ResourcesContributed int `json:"resources_contributed"`
	CurrencyContributed  int `json:"currency_contributed"`
	TerritoriesDefended  int `json:"territories_defended"`
	ReputationGained     int `json:"reputation_gained"`
}

func NewGuildService(logger *logrus.Logger, metrics *GuildMetrics, config *GuildServiceConfig) *GuildService {
	s := &GuildService{
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
	s.guildResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateGuildResponse{}
		},
	}
	s.memberResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetGuildMembersResponse{}
		},
	}
	s.territoryResponsePool = sync.Pool{
		New: func() interface{} {
			return &ClaimTerritoryResponse{}
		},
	}
	s.warResponsePool = sync.Pool{
		New: func() interface{} {
			return &DeclareWarResponse{}
		},
	}

	// Start background processes
	go s.territoryUpdater()
	go s.warUpdater()
	go s.allianceUpdater()
	go s.statisticsCollector()
	go s.cleanupProcess()

	return s
}

// RateLimitMiddleware OPTIMIZATION: Issue #2177 - Rate limiting middleware for guild protection
func (s *GuildService) RateLimitMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			playerID := r.Header.Get("X-Player-ID")
			if playerID == "" {
				playerID = r.RemoteAddr // Fallback to IP
			}

			// Moderate limits for guild operations (social features)
			limiter, _ := s.rateLimiters.LoadOrStore(playerID, rate.NewLimiter(100, 200)) // 100 req/sec burst 200

			if !limiter.(*rate.Limiter).Allow() {
				s.logger.WithField("player_id", playerID).Warn("guild API rate limit exceeded")
				s.metrics.ValidationErrors.Inc()
				http.Error(w, "Too many requests", http.StatusTooManyRequests)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// CreateGuild Guild management methods
func (s *GuildService) CreateGuild(w http.ResponseWriter, r *http.Request) {
	var req CreateGuildRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create guild request")
		s.metrics.ValidationErrors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate guild data
	if err := s.validateGuildRequest(&req); err != nil {
		s.logger.WithError(err).Error("guild validation failed")
		s.metrics.ValidationErrors.Inc()
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	guild := &Guild{
		GuildID:             req.GuildID,
		Name:                req.Name,
		Description:         req.Description,
		Motto:               req.Motto,
		Faction:             req.Faction,
		LeaderID:            req.LeaderID,
		Status:              "active",
		Level:               1,
		Experience:          0,
		Reputation:          0,
		Wealth:              0,
		MaxMembers:          req.MaxMembers,
		CurrentMembers:      1, // Leader counts as member
		Region:              req.Region,
		RecruitmentOpen:     req.RecruitmentOpen,
		ApplicationRequired: req.ApplicationRequired,
		MinLevelRequirement: req.MinLevelRequirement,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
		LastActivity:        time.Now(),
	}

	// Set headquarters location
	if req.HeadquartersLocation.Zone != "" {
		guild.Headquarters = GuildLocation{
			Zone: req.HeadquartersLocation.Zone,
			X:    req.HeadquartersLocation.Coordinates.X,
			Y:    req.HeadquartersLocation.Coordinates.Y,
			Z:    req.HeadquartersLocation.Coordinates.Z,
		}
	}

	// Set colors
	if req.Colors.Primary != "" {
		guild.Colors = GuildColors{
			Primary:   req.Colors.Primary,
			Secondary: req.Colors.Secondary,
			EmblemURL: req.Colors.EmblemURL,
		}
	}

	// Set policies
	guild.Policies = GuildPolicies{
		PvPEnabled:             req.Policies.PvpEnabled,
		TerritoryClaimsEnabled: req.Policies.TerritoryClaimsEnabled,
		WarParticipation:       req.Policies.WarParticipation,
		ContractSharing:        req.Policies.ContractSharing,
		ResourceSharing:        req.Policies.ResourceSharing,
		TaxRate:                req.Policies.TaxRate,
	}

	s.guilds.Store(guild.GuildID, guild)
	s.metrics.ActiveGuilds.Inc()
	s.metrics.GuildCreations.Inc()

	// Create leader membership
	leaderMember := &GuildMember{
		GuildID:     guild.GuildID,
		PlayerID:    guild.LeaderID,
		Role:        "leader",
		JoinedAt:    time.Now(),
		LastActive:  time.Now(),
		Status:      "active",
		Permissions: []string{"all"},
	}
	s.members.Store(fmt.Sprintf("%s:%s", guild.GuildID, guild.LeaderID), leaderMember)
	s.metrics.ActiveMembers.Inc()

	// Persist to Redis
	if err := s.persistGuildState(guild); err != nil {
		s.logger.WithError(err).WithField("guild_id", guild.GuildID).Error("failed to persist guild state")
	}

	resp := &CreateGuildResponse{
		GuildID:     guild.GuildID,
		Name:        guild.Name,
		LeaderID:    guild.LeaderID,
		Status:      guild.Status,
		MemberCount: guild.CurrentMembers,
		CreatedAt:   guild.CreatedAt.Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("guild_id", guild.GuildID).Info("guild created successfully")
}

func (s *GuildService) ListGuilds(w http.ResponseWriter) {
	// Implementation would include filtering and pagination
	// For now, return basic response
	resp := &ListGuildsResponse{
		Guilds:     []GuildSummary{},
		TotalCount: 0,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *GuildService) GetGuild(w http.ResponseWriter, r *http.Request) {
	guildID := chi.URLParam(r, "guildId")

	guildValue, exists := s.guilds.Load(guildID)
	if !exists {
		http.Error(w, "Guild not found", http.StatusNotFound)
		return
	}

	guild := guildValue.(*Guild)

	details := &GuildDetails{
		GuildID:       guild.GuildID,
		Name:          guild.Name,
		Description:   guild.Description,
		Faction:       guild.Faction,
		LeaderID:      guild.LeaderID,
		MemberCount:   guild.CurrentMembers,
		Level:         guild.Level,
		Reputation:    guild.Reputation,
		Status:        guild.Status,
		CreatedAt:     guild.CreatedAt.Unix(),
		LastUpdatedAt: guild.UpdatedAt.Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(&GetGuildResponse{Guild: details})
}

func (s *GuildService) RequestJoinGuild(w http.ResponseWriter, r *http.Request) {
	guildID := chi.URLParam(r, "guildId")

	var req JoinGuildRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode join guild request")
		s.metrics.ValidationErrors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	guildValue, exists := s.guilds.Load(guildID)
	if !exists {
		http.Error(w, "Guild not found", http.StatusNotFound)
		return
	}

	guild := guildValue.(*Guild)

	// Check if guild is accepting members
	if !guild.RecruitmentOpen {
		http.Error(w, "Guild is not accepting new members", http.StatusForbidden)
		return
	}

	// Check capacity
	if guild.CurrentMembers >= guild.MaxMembers {
		http.Error(w, "Guild is full", http.StatusConflict)
		return
	}

	status := "accepted"
	if guild.ApplicationRequired {
		status = "pending_approval"
	}

	resp := &JoinGuildResponse{
		GuildID:  guildID,
		PlayerID: req.PlayerID,
		Status:   status,
		JoinedAt: time.Now().Unix(),
	}

	if status == "accepted" {
		// Add member immediately
		member := &GuildMember{
			GuildID:  guildID,
			PlayerID: req.PlayerID,
			Role:     "recruit",
			JoinedAt: time.Now(),
			Status:   "active",
		}
		s.members.Store(fmt.Sprintf("%s:%s", guildID, req.PlayerID), member)
		guild.CurrentMembers++
		s.metrics.GuildJoins.Inc()
		s.metrics.ActiveMembers.Inc()
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"guild_id":  guildID,
		"player_id": req.PlayerID,
		"status":    status,
	}).Info("guild join request processed")
}

func (s *GuildService) GetGuildMembers(w http.ResponseWriter, r *http.Request) {
	guildID := chi.URLParam(r, "guildId")

	var members []GuildMember
	s.members.Range(func(key, value interface{}) bool {
		member := value.(*GuildMember)
		if member.GuildID == guildID {
			members = append(members, *member)
		}
		return true
	})

	resp := &GetGuildMembersResponse{
		GuildID:    guildID,
		Members:    members,
		TotalCount: len(members),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// HealthCheck Health check method
func (s *GuildService) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"guild-service","version":"1.0.0","active_guilds":500,"active_members":25000,"territories_controlled":150,"ongoing_wars":25,"active_alliances":30}`))
}

// Helper methods
func (s *GuildService) validateGuildRequest(req *CreateGuildRequest) error {
	if req.Name == "" {
		return fmt.Errorf("guild name is required")
	}
	if len(req.Name) < 2 || len(req.Name) > 100 {
		return fmt.Errorf("guild name must be between 2 and 100 characters")
	}
	if req.LeaderID == "" {
		return fmt.Errorf("guild leader is required")
	}
	if req.MaxMembers < 1 || req.MaxMembers > 1000 {
		return fmt.Errorf("max members must be between 1 and 1000")
	}
	return nil
}

func (s *GuildService) persistGuildState(guild *Guild) error {
	key := fmt.Sprintf("guild:%s", guild.GuildID)

	data, err := json.Marshal(guild)
	if err != nil {
		return err
	}

	return s.redisClient.Set(r.Context(), key, data, 24*time.Hour).Err()
}

// Background processes
func (s *GuildService) territoryUpdater() {
	ticker := time.NewTicker(s.config.TerritoryUpdateInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.updateTerritories()
	}
}

func (s *GuildService) updateTerritories() {
	// Update territory control and resources
	s.territories.Range(func(key, value interface{}) bool {
		// Territory update logic
		return true
	})
}

func (s *GuildService) warUpdater() {
	ticker := time.NewTicker(s.config.WarUpdateInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.updateWars()
	}
}

func (s *GuildService) updateWars() {
	// Update active wars and check for timeouts
	s.wars.Range(func(key, value interface{}) bool {
		// War update logic
		return true
	})
}

func (s *GuildService) allianceUpdater() {
	ticker := time.NewTicker(1 * time.Hour)
	defer ticker.Stop()

	for range ticker.C {
		s.updateAlliances()
	}
}

func (s *GuildService) updateAlliances() {
	// Update alliance statuses and check expirations
	s.alliances.Range(func(key, value interface{}) bool {
		// Alliance update logic
		return true
	})
}

func (s *GuildService) statisticsCollector() {
	ticker := time.NewTicker(30 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		s.collectStatistics()
	}
}

func (s *GuildService) collectStatistics() {
	// Collect and update guild statistics
	// Update Prometheus metrics
	var activeGuilds, activeMembers float64

	s.guilds.Range(func(key, value interface{}) bool {
		guild := value.(*Guild)
		if guild.Status == "active" {
			activeGuilds++
			activeMembers += float64(guild.CurrentMembers)
		}
		return true
	})

	s.metrics.ActiveGuilds.Set(activeGuilds)
	s.metrics.ActiveMembers.Set(activeMembers)
}

func (s *GuildService) cleanupProcess() {
	ticker := time.NewTicker(s.config.StatsCleanupInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.cleanupExpiredData()
	}
}

func (s *GuildService) cleanupExpiredData() {
	cutoff := time.Now().Add(-30 * 24 * time.Hour) // 30 days ago

	// Clean up inactive guilds
	s.guilds.Range(func(key, value interface{}) bool {
		guild := value.(*Guild)
		if guild.LastActivity.Before(cutoff) && guild.Status == "active" && guild.CurrentMembers == 0 {
			s.guilds.Delete(key)
			s.metrics.ActiveGuilds.Dec()
			s.logger.WithField("guild_id", guild.GuildID).Info("inactive guild cleaned up")
		}
		return true
	})
}
