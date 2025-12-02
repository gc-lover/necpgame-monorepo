// Issue: #150
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame/services/matchmaking-service-go/pkg/api"
	"github.com/oapi-codegen/runtime/types"
)

func parseUUID(s string) types.UUID {
	uuid := types.UUID{}
	copy(uuid[:], s)
	return uuid
}

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

// MatchmakingService реализует Service
type MatchmakingService struct {
	repository Repository
}

// NewMatchmakingService создает новый сервис
func NewMatchmakingService(repository Repository) Service {
	return &MatchmakingService{
		repository: repository,
	}
}

// EnterQueue добавляет игрока в очередь
func (s *MatchmakingService) EnterQueue(ctx context.Context, req *api.EnterQueueRequest) (*api.QueueResponse, error) {
	// TODO: Реализовать логику добавления в очередь
	queueID := parseUUID("550e8400-e29b-41d4-a716-446655440000")
	return &api.QueueResponse{
		QueueId:           queueID,
		EstimatedWaitTime: 30,
		CurrentQueueSize:  5,
	}, nil
}

// GetQueueStatus получает статус очереди
func (s *MatchmakingService) GetQueueStatus(ctx context.Context, queueID string) (*api.QueueStatusResponse, error) {
	// TODO: Реализовать получение статуса
	qID := parseUUID(queueID)
	status := api.QueueStatusResponseStatusWaiting
	return &api.QueueStatusResponse{
		QueueId:     qID,
		Status:      status,
		TimeInQueue: 15,
	}, nil
}

// LeaveQueue удаляет из очереди
func (s *MatchmakingService) LeaveQueue(ctx context.Context, queueID string) (*api.LeaveQueueResponse, error) {
	// TODO: Реализовать выход из очереди
	status := api.LeaveQueueResponseStatusCancelled
	return &api.LeaveQueueResponse{
		Status:          status,
		WaitTimeSeconds: 30,
	}, nil
}

// GetPlayerRating получает рейтинг
func (s *MatchmakingService) GetPlayerRating(ctx context.Context, playerID string) (*api.PlayerRatingResponse, error) {
	// TODO: Реализовать получение рейтинга
	return nil, errors.New("not implemented")
}

// GetLeaderboard получает таблицу лидеров
func (s *MatchmakingService) GetLeaderboard(ctx context.Context, activityType string, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error) {
	// TODO: Реализовать таблицу лидеров
	return nil, errors.New("not implemented")
}

// AcceptMatch принимает матч
func (s *MatchmakingService) AcceptMatch(ctx context.Context, matchID string) error {
	// TODO: Реализовать принятие матча
	return nil
}

// DeclineMatch отклоняет матч
func (s *MatchmakingService) DeclineMatch(ctx context.Context, matchID string) error {
	// TODO: Реализовать отклонение матча
	return nil
}

