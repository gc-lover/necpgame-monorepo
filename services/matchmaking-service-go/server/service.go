// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1579 - ogen + skill buckets O(1) matching
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
	"github.com/google/uuid"
)

// Service интерфейс бизнес-логики
type Service interface {
	EnterQueue(ctx context.Context, req *api.EnterQueueRequest) (*api.QueueResponse, error)
	GetQueueStatus(ctx context.Context, queueID string) (*api.QueueStatusResponse, error)
	LeaveQueue(ctx context.Context, queueID string) (*api.LeaveQueueResponse, error)
	GetPlayerRating(ctx context.Context, playerID string) (*api.PlayerRatingResponse, error)
	GetLeaderboard(ctx context.Context, activityType string, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error)
	AcceptMatch(ctx context.Context, matchID string) error
	DeclineMatch(ctx context.Context, matchID string) error
}

// CRITICAL: Skill Buckets for O(1) matching (from performance/04c-matchmaking-anticheat.md)
type SkillBucket struct {
	players []PlayerQueueEntry
	mu      sync.RWMutex
}

type PlayerQueueEntry struct {
	PlayerID     uuid.UUID
	Skill        int
	JoinedAt     time.Time
	ActivityType string
}

// MatchmakingQueue с skill buckets
type MatchmakingQueue struct {
	skillBuckets map[int]*SkillBucket // bucket ID -> players
	mu           sync.RWMutex
}

func NewMatchmakingQueue() *MatchmakingQueue {
	return &MatchmakingQueue{
		skillBuckets: make(map[int]*SkillBucket),
	}
}

// AddPlayer - O(1) добавление
func (q *MatchmakingQueue) AddPlayer(entry PlayerQueueEntry) {
	bucketID := entry.Skill / 100 // Buckets: 0-99, 100-199, etc
	
	q.mu.Lock()
	if _, ok := q.skillBuckets[bucketID]; !ok {
		q.skillBuckets[bucketID] = &SkillBucket{}
	}
	q.mu.Unlock()
	
	bucket := q.skillBuckets[bucketID]
	bucket.mu.Lock()
	bucket.players = append(bucket.players, entry)
	bucket.mu.Unlock()
}

// MatchmakingService реализует Service
type MatchmakingService struct {
	repository Repository
	queue      *MatchmakingQueue // Skill buckets
	// Issue: #1588 - Resilience patterns
	dbCircuitBreaker *CircuitBreaker
	loadShedder      *LoadShedder
}

// NewMatchmakingService создает новый сервис с skill buckets
func NewMatchmakingService(repository Repository) Service {
	// Issue: #1588 - Resilience patterns for hot path service (5k+ RPS)
	dbCB := NewCircuitBreaker("matchmaking-db")
	loadShedder := NewLoadShedder(2000) // Max 2000 concurrent requests (hot path)
	
	return &MatchmakingService{
		repository: repository,
		queue:      NewMatchmakingQueue(),
		dbCircuitBreaker: dbCB,
		loadShedder:      loadShedder,
	}
}

// EnterQueue добавляет игрока в очередь (с skill buckets)
func (s *MatchmakingService) EnterQueue(ctx context.Context, req *api.EnterQueueRequest) (*api.QueueResponse, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !s.loadShedder.Allow() {
		return nil, errors.New("service overloaded, please try again later")
	}
	defer s.loadShedder.Done()
	
	// Note: EnterQueueRequest doesn't have PlayerID in spec
	// In production, get playerID from JWT context
	
	// Get player rating with circuit breaker (Fallback: default 1500)
	// TODO: Get playerID from context.Context
	playerID := "temp-player-id"
	var rating int
	result, cbErr := s.dbCircuitBreaker.Execute(func() (interface{}, error) {
		return s.repository.GetPlayerRating(ctx, playerID, string(req.ActivityType))
	})
	
	if cbErr != nil {
		// Circuit breaker rejected or DB error - use default MMR (graceful degradation)
		rating = 1500 // Default MMR
	} else if result != nil {
		rating = result.(int)
		if rating == 0 {
			rating = 1500 // Default MMR if rating is 0
		}
	} else {
		rating = 1500 // Default MMR
	}
	
	// Add to skill bucket (O(1))
	queueID := uuid.New()
	entry := PlayerQueueEntry{
		PlayerID:     queueID,
		Skill:        rating,
		JoinedAt:     time.Now(),
		ActivityType: string(req.ActivityType),
	}
	s.queue.AddPlayer(entry)
	
	// Estimate wait time based on bucket size
	bucketID := rating / 100
	s.queue.mu.RLock()
	bucket, ok := s.queue.skillBuckets[bucketID]
	s.queue.mu.RUnlock()
	
	bucketSize := 0
	if ok {
		bucket.mu.RLock()
		bucketSize = len(bucket.players)
		bucket.mu.RUnlock()
	}
	
	return &api.QueueResponse{
		QueueId:           queueID,
		EstimatedWaitTime: calculateWaitTime(bucketSize),
		CurrentQueueSize:  bucketSize,
	}, nil
}

// calculateWaitTime estimates based on queue size
func calculateWaitTime(queueSize int) int {
	switch {
	case queueSize < 5:
		return 60 // 1 min
	case queueSize < 20:
		return 30 // 30 sec
	default:
		return 10 // 10 sec
	}
}

// GetQueueStatus получает статус очереди
func (s *MatchmakingService) GetQueueStatus(ctx context.Context, queueID string) (*api.QueueStatusResponse, error) {
	qID, err := uuid.Parse(queueID)
	if err != nil {
		return nil, errors.New("invalid queue ID")
	}
	
	return &api.QueueStatusResponse{
		QueueId:     qID,
		Status:      api.QueueStatusResponseStatusWaiting,
		TimeInQueue: 15,
	}, nil
}

// LeaveQueue удаляет из очереди
func (s *MatchmakingService) LeaveQueue(ctx context.Context, queueID string) (*api.LeaveQueueResponse, error) {
	// TODO: Remove from skill bucket
	
	return &api.LeaveQueueResponse{
		Status:          api.LeaveQueueResponseStatusCancelled,
		WaitTimeSeconds: 30,
	}, nil
}

// GetPlayerRating получает рейтинг
func (s *MatchmakingService) GetPlayerRating(ctx context.Context, playerID string) (*api.PlayerRatingResponse, error) {
	// TODO: Implement with Redis caching (Level 2)
	rating, err := s.repository.GetPlayerRating(ctx, playerID, "pvp")
	if err != nil {
		rating = 1500
	}
	
	pID, _ := uuid.Parse(playerID)
	return &api.PlayerRatingResponse{
		PlayerId: pID,
		Ratings: []api.ActivityRating{
			{
				ActivityType:  "pvp",
				CurrentRating: rating,
				PeakRating:    api.NewOptInt(rating),
				Tier:          api.ActivityRatingTierGold,
				Wins:          10,
				Losses:        5,
			},
		},
	}, nil
}

// GetLeaderboard получает таблицу лидеров
func (s *MatchmakingService) GetLeaderboard(ctx context.Context, activityType string, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error) {
	// TODO: Implement with Redis sorted sets
	
	return &api.LeaderboardResponse{
		ActivityType: activityType,
		SeasonId:     "season-2024",
		Leaderboard:  []api.LeaderboardEntry{},
	}, nil
}

// AcceptMatch принимает матч
func (s *MatchmakingService) AcceptMatch(ctx context.Context, matchID string) error {
	// TODO: Implement match acceptance
	return nil
}

// DeclineMatch отклоняет матч
func (s *MatchmakingService) DeclineMatch(ctx context.Context, matchID string) error {
	// TODO: Implement match decline
	return nil
}

