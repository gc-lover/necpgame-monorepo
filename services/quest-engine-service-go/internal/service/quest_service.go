package service

import (
	"context"
	"fmt"
	"sync/atomic"
	"time"

	"necpgame/services/quest-engine-service-go/internal/repository"
)

type QuestService struct {
	repo        *repository.Repository
	atomicStats *AtomicStatistics
}

type AtomicStatistics struct {
	activeQuests    int64
	questsCompleted int64
	guildWarsActive int64
}

func NewQuestService(repo *repository.Repository) *QuestService {
	return &QuestService{
		repo:        repo,
		atomicStats: &AtomicStatistics{},
	}
}

func (s *QuestService) GetActiveQuests(ctx context.Context, playerID string) ([]*repository.Quest, error) {
	return s.repo.GetActiveQuests(ctx, playerID)
}

func (s *QuestService) CreateQuest(ctx context.Context, questType, playerID, templateID string) (*repository.Quest, error) {
	quest := &repository.Quest{
		ID:         generateQuestID(),
		Type:       questType,
		PlayerID:   playerID,
		TemplateID: templateID,
		Status:     "active",
		CreatedAt:  time.Now(),
	}

	if err := s.repo.SaveQuest(ctx, quest); err != nil {
		return nil, fmt.Errorf("failed to save quest: %w", err)
	}

	atomic.AddInt64(&s.atomicStats.activeQuests, 1)
	return quest, nil
}

func (s *QuestService) CompleteQuest(ctx context.Context, questID, playerID string) error {
	quest, err := s.repo.GetQuest(ctx, questID)
	if err != nil {
		return fmt.Errorf("failed to get quest: %w", err)
	}

	if quest.PlayerID != playerID {
		return fmt.Errorf("quest does not belong to player")
	}

	quest.Status = "completed"
	quest.CompletedAt = &time.Time{}
	*quest.CompletedAt = time.Now()

	if err := s.repo.SaveQuest(ctx, quest); err != nil {
		return fmt.Errorf("failed to save completed quest: %w", err)
	}

	atomic.AddInt64(&s.atomicStats.activeQuests, -1)
	atomic.AddInt64(&s.atomicStats.questsCompleted, 1)

	return nil
}

func (s *QuestService) GetTelemetry() *ServiceTelemetry {
	return &ServiceTelemetry{
		ActiveQuests:    atomic.LoadInt64(&s.atomicStats.activeQuests),
		QuestsCompleted: atomic.LoadInt64(&s.atomicStats.questsCompleted),
		GuildWarsActive: atomic.LoadInt64(&s.atomicStats.guildWarsActive),
	}
}

type ServiceTelemetry struct {
	ActiveQuests    int64 `json:"active_quests"`
	QuestsCompleted int64 `json:"quests_completed"`
	GuildWarsActive int64 `json:"guild_wars_active"`
}

func generateQuestID() string {
	return fmt.Sprintf("quest_%d", time.Now().UnixNano())
}
