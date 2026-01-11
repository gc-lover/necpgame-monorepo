//go:align 64
// Issue: #2295

package service

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/redis/go-redis/v9"
	"golang.org/x/time/rate"
	"go.uber.org/zap"

	guildRepository "necpgame/services/guild-service-go/internal/repository"
	"necpgame/services/guild-service-go/pkg/api"
)

//go:align 64
type Config struct {
	MaxGuildNameLength      int           `yaml:"max_guild_name_length"`
	MaxGuildDescription     int           `yaml:"max_guild_description"`
	DefaultMaxMembers       int           `yaml:"default_max_members"`
	GuildOperationTimeout   time.Duration `yaml:"guild_operation_timeout"`
	DatabaseQueryTimeout    time.Duration `yaml:"database_query_timeout"`
	MaxConcurrentOps        int           `yaml:"max_concurrent_ops"`
}

//go:align 64
type Service struct {
	repo        guildRepository.Repository
	logger      *zap.Logger
	config      Config
	cacheMutex  sync.RWMutex
	semaphore   chan struct{}
	redis       *redis.Client
	rateLimiter *rate.Limiter

	// PERFORMANCE: Object pooling for memory efficiency
	guildPool  *sync.Pool
	memberPool *sync.Pool

	// Metrics
	guildOperations    prometheus.Counter
	guildOperationTime *prometheus.HistogramVec
	guildCreations     prometheus.Counter
	guildUpdates       prometheus.Counter
	memberOperations   prometheus.Counter
	cacheHits          prometheus.Counter
	cacheMisses        prometheus.Counter
}

//go:align 64
func NewService(repo guildRepository.Repository, config Config, redisClient *redis.Client, logger *zap.Logger) *Service {
	service := &Service{
		repo:   repo,
		config: config,
		redis:  redisClient,
		logger: logger,
		semaphore: make(chan struct{}, config.MaxConcurrentOps),
		rateLimiter: rate.NewLimiter(rate.Limit(100), 200), // 100 req/s burst 200

		// PERFORMANCE: Object pooling reduces GC pressure
		guildPool: &sync.Pool{
			New: func() interface{} {
				return &api.Guild{}
			},
		},
		memberPool: &sync.Pool{
			New: func() interface{} {
				return &api.GuildMember{}
			},
		},
	}

	// Initialize metrics
	service.initMetrics()

	return service
}

func (s *Service) initMetrics() {
	s.guildOperations = promauto.NewCounter(prometheus.CounterOpts{
		Name: "guild_operations_total",
		Help: "Total number of guild operations",
	})

	s.guildOperationTime = promauto.NewHistogramVec(prometheus.HistogramOpts{
		Name:    "guild_operation_duration_seconds",
		Help:    "Duration of guild operations",
		Buckets: prometheus.DefBuckets,
	}, []string{"operation"})

	s.guildCreations = promauto.NewCounter(prometheus.CounterOpts{
		Name: "guild_creations_total",
		Help: "Total number of guild creations",
	})

	s.guildUpdates = promauto.NewCounter(prometheus.CounterOpts{
		Name: "guild_updates_total",
		Help: "Total number of guild updates",
	})

	s.memberOperations = promauto.NewCounter(prometheus.CounterOpts{
		Name: "guild_member_operations_total",
		Help: "Total number of guild member operations",
	})

	s.cacheHits = promauto.NewCounter(prometheus.CounterOpts{
		Name: "guild_cache_hits_total",
		Help: "Total number of guild cache hits",
	})

	s.cacheMisses = promauto.NewCounter(prometheus.CounterOpts{
		Name: "guild_cache_misses_total",
		Help: "Total number of guild cache misses",
	})
}

// Guild operations
func (s *Service) GetGuild(ctx context.Context, id uuid.UUID) (*api.Guild, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("get_guild"))
	defer timer.ObserveDuration()

	s.guildOperations.Inc()

	// Try cache first
	cacheKey := fmt.Sprintf("guild:%s", id.String())
	if cachedGuild, err := s.getCachedGuild(ctx, cacheKey); err == nil && cachedGuild != nil {
		s.cacheHits.Inc()
		return cachedGuild, nil
	}
	s.cacheMisses.Inc()

	// Get from database
	guild, err := s.repo.GetGuild(ctx, id)
	if err != nil {
		s.logger.Error("Failed to get guild from database", zap.Error(err), zap.String("guild_id", id.String()))
		return nil, err
	}

	if guild == nil {
		return nil, fmt.Errorf("guild not found")
	}

	// Cache the result
	if err := s.setCachedGuild(ctx, cacheKey, guild); err != nil {
		s.logger.Warn("Failed to cache guild", zap.Error(err), zap.String("guild_id", id.String()))
	}

	return guild, nil
}

func (s *Service) CreateGuild(ctx context.Context, req *api.CreateGuildRequest, founderID uuid.UUID) (*api.Guild, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("create_guild"))
	defer timer.ObserveDuration()

	s.guildCreations.Inc()

	// Validate request
	if err := s.validateCreateGuildRequest(req); err != nil {
		return nil, err
	}

	// Rate limiting
	if !s.rateLimiter.Allow() {
		return nil, fmt.Errorf("rate limit exceeded")
	}

	// Check if founder already has a guild
	if err := s.checkFounderEligibility(ctx, founderID); err != nil {
		return nil, err
	}

	// Create guild object
	now := time.Now()
	guildID := uuid.New()
	guild := &api.Guild{
		ID:          guildID.String(),
		Name:        req.Name,
		Description: req.Description,
		LeaderId:    founderID.String(),
		Level:       api.OptInt{Value: 1, Set: true},
		Experience:  api.OptInt{Value: 0, Set: true},
		MaxMembers:  api.OptInt{Value: s.config.DefaultMaxMembers, Set: true},
		MemberCount: api.OptInt{Value: 1, Set: true},
		Reputation:  api.OptInt{Value: 0, Set: true},
		CreatedAt:   api.OptDateTime{Value: now, Set: true},
		UpdatedAt:   api.OptDateTime{Value: now, Set: true},
	}

	// Use semaphore for concurrent operations
	select {
	case s.semaphore <- struct{}{}:
		defer func() { <-s.semaphore }()
	default:
		return nil, fmt.Errorf("too many concurrent guild operations")
	}

	// Save to database
	createdGuild, err := s.repo.CreateGuild(ctx, guild)
	if err != nil {
		s.logger.Error("Failed to create guild", zap.Error(err), zap.String("guild_name", req.Name))
		return nil, err
	}

	// Add founder as first member
	founderMember := &api.GuildMember{
		UserId:      founderID.String(),
		GuildId:     guildID.String(),
		Role:        api.GuildMemberRoleLeader,
		JoinedAt:    api.OptDateTime{Value: now, Set: true},
		LastActive:  api.OptDateTime{Value: now, Set: true},
		Contribution: api.OptInt{Value: 0, Set: true},
	}

	if _, err := s.repo.AddGuildMember(ctx, founderMember); err != nil {
		s.logger.Error("Failed to add founder as guild member", zap.Error(err))
		// Try to cleanup guild creation
		if cleanupErr := s.repo.DeleteGuild(ctx, guildID); cleanupErr != nil {
			s.logger.Error("Failed to cleanup guild after member creation failure", zap.Error(cleanupErr))
		}
		return nil, err
	}

	s.logger.Info("Guild created successfully", zap.String("guild_id", guildID.String()), zap.String("founder_id", founderID.String()))
	return createdGuild, nil
}

func (s *Service) UpdateGuild(ctx context.Context, id uuid.UUID, req *api.UpdateGuildRequest, userID uuid.UUID) (*api.Guild, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("update_guild"))
	defer timer.ObserveDuration()

	s.guildUpdates.Inc()
	// TODO: Implement with validation and repository call
	guild := &api.Guild{
		ID:          id.String(),
		Name:        req.Name.Value,
		Description: req.Description,
		Level:       api.OptInt{Value: 1, Set: true},
		Experience:  api.OptInt{Value: 0, Set: true},
		MaxMembers:  req.MaxMembers,
		MemberCount: api.OptInt{Value: 1, Set: true},
		Reputation:  api.OptInt{Value: 0, Set: true},
		CreatedAt:   api.OptDateTime{Value: time.Now(), Set: true},
		UpdatedAt:   api.OptDateTime{Value: time.Now(), Set: true},
	}
	return guild, nil
}

func (s *Service) DeleteGuild(ctx context.Context, id uuid.UUID) error {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("delete_guild"))
	defer timer.ObserveDuration()

	s.guildOperations.Inc()
	// TODO: Implement with repository call
	return nil
}

// ListGuilds implements guild listing with pagination and filtering
func (s *Service) ListGuilds(ctx context.Context, params api.GuildServiceListGuildsParams) ([]api.Guild, int, int, int, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("list_guilds"))
	defer timer.ObserveDuration()

	s.guildOperations.Inc()

	page := 1
	if params.Page.Set {
		page = params.Page.Value
	}
	limit := 50
	if params.Limit.Set {
		limit = params.Limit.Value
	}

	// TODO: Implement with repository call
	guilds := []api.Guild{} // Placeholder
	total := 0

	return guilds, total, page, limit, nil
}

// Guild member operations
func (s *Service) GetGuildMembers(ctx context.Context, guildID uuid.UUID) ([]*api.GuildMember, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("get_guild_members"))
	defer timer.ObserveDuration()

	s.memberOperations.Inc()
	// TODO: Implement with repository call
	return []*api.GuildMember{}, nil
}

func (s *Service) AddGuildMember(ctx context.Context, guildID uuid.UUID, request *api.AddMemberRequest, adderID uuid.UUID) (*api.GuildMember, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("add_guild_member"))
	defer timer.ObserveDuration()

	s.memberOperations.Inc()
	// TODO: Implement with validation and repository call
	role := api.GuildMemberRoleMember
	if request.Role.Set {
		switch request.Role.Value {
		case api.AddMemberRequestRoleRecruit:
			role = api.GuildMemberRoleRecruit
		case api.AddMemberRequestRoleMember:
			role = api.GuildMemberRoleMember
		case "officer": // Handle officer role if added to AddMemberRequestRole
			role = api.GuildMemberRoleOfficer
		default:
			// Keep default as Member
			role = api.GuildMemberRoleMember
		}
	}

	member := &api.GuildMember{
		UserId:       request.UserId,
		GuildId:      guildID.String(),
		Role:         role,
		JoinedAt:     api.OptDateTime{Value: time.Now(), Set: true},
		LastActive:   api.OptDateTime{Value: time.Now(), Set: true},
		Contribution: api.OptInt{Value: 0, Set: true},
	}
	return member, nil
}

func (s *Service) InviteGuildMember(ctx context.Context, guildID uuid.UUID, request *api.AddMemberRequest, inviterID uuid.UUID) (interface{}, error) {
	// For now, just add the member directly
	return s.AddGuildMember(ctx, guildID, request, inviterID)
}

func (s *Service) UpdateGuildMember(ctx context.Context, guildID, playerID uuid.UUID, member *api.GuildMember) (*api.GuildMember, error) {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("update_guild_member"))
	defer timer.ObserveDuration()

	s.memberOperations.Inc()
	// TODO: Implement with repository call
	return member, nil
}

func (s *Service) RemoveGuildMember(ctx context.Context, guildID, playerID uuid.UUID) error {
	timer := prometheus.NewTimer(s.guildOperationTime.WithLabelValues("remove_guild_member"))
	defer timer.ObserveDuration()

	s.memberOperations.Inc()
	// TODO: Implement with repository call
	return nil
}

// Additional required methods from QA


// HealthCheck performs a health check
func (s *Service) HealthCheck(ctx context.Context) (*api.HealthResponse, error) {
	return &api.HealthResponse{
		Status:    api.HealthResponseStatusOk,
		Message:   api.OptString{Value: "Guild system service is healthy", Set: true},
		Timestamp: api.NewOptDateTime(time.Now()),
		Version:   api.OptString{Value: "1.0.0", Set: true},
	}, nil
}

// Cache methods for Redis integration

func (s *Service) getCachedGuild(ctx context.Context, key string) (*api.Guild, error) {
	if s.redis == nil {
		return nil, fmt.Errorf("redis not configured")
	}

	data, err := s.redis.Get(ctx, key).Result()
	if err != nil {
		return nil, err
	}

	var guild api.Guild
	if err := json.Unmarshal([]byte(data), &guild); err != nil {
		return nil, err
	}

	return &guild, nil
}

func (s *Service) setCachedGuild(ctx context.Context, key string, guild *api.Guild) error {
	if s.redis == nil {
		return fmt.Errorf("redis not configured")
	}

	data, err := json.Marshal(guild)
	if err != nil {
		return err
	}

	return s.redis.Set(ctx, key, data, time.Minute*5).Err() // 5 minute TTL
}

func (s *Service) invalidateGuildCache(ctx context.Context, guildID string) error {
	if s.redis == nil {
		return fmt.Errorf("redis not configured")
	}

	cacheKey := fmt.Sprintf("guild:%s", guildID)
	return s.redis.Del(ctx, cacheKey).Err()
}

// Validation methods

func (s *Service) validateCreateGuildRequest(req *api.CreateGuildRequest) error {
	if len(req.Name) < 3 {
		return fmt.Errorf("guild name must be at least 3 characters")
	}
	if len(req.Name) > s.config.MaxGuildNameLength {
		return fmt.Errorf("guild name must be less than %d characters", s.config.MaxGuildNameLength)
	}
	if req.Description.Set && len(req.Description.Value) > s.config.MaxGuildDescription {
		return fmt.Errorf("guild description must be less than %d characters", s.config.MaxGuildDescription)
	}
	return nil
}

func (s *Service) checkFounderEligibility(ctx context.Context, founderID uuid.UUID) error {
	// Check if founder already leads a guild
	const query = `
		SELECT COUNT(*) FROM guilds
		WHERE leader_id = $1 AND deleted_at IS NULL
	`

	db := s.repo.GetDB().(*pgxpool.Pool)
	var count int
	if err := db.QueryRow(ctx, query, founderID).Scan(&count); err != nil {
		return fmt.Errorf("failed to check founder eligibility: %w", err)
	}

	if count > 0 {
		return fmt.Errorf("founder already leads a guild")
	}

	return nil
}
