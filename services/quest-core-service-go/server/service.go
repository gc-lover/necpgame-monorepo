// Issue: #1597, #1607
package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/quest-core-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

// Service contains business logic
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo       *Repository
	questCache *QuestCache // Issue: #1609 - 3-tier cache

	// Memory pooling for hot path structs (zero allocations target!)
	startQuestResponsePool sync.Pool
	questInstancePool sync.Pool
	questListResponsePool sync.Pool
	completeQuestResponsePool sync.Pool
	questRewardsPool sync.Pool
}

// NewService creates new service with memory pooling
func NewService(repo *Repository, redisClient *redis.Client) *Service {
	questCache := NewQuestCache(redisClient, repo)
	s := &Service{
		repo:       repo,
		questCache: questCache,
	}

	// Initialize memory pools (zero allocations target!)
	s.startQuestResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.StartQuestResponse{}
		},
	}
	s.questInstancePool = sync.Pool{
		New: func() interface{} {
			return &api.QuestInstance{}
		},
	}
	s.questListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.QuestListResponse{}
		},
	}
	s.completeQuestResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.CompleteQuestResponse{}
		},
	}
	s.questRewardsPool = sync.Pool{
		New: func() interface{} {
			return &api.QuestRewards{}
		},
	}

	return s
}

// StartQuest starts a new quest (invalidates cache) - Issue: #1609
func (s *Service) StartQuest(ctx context.Context, req *api.StartQuestRequest) (api.StartQuestRes, error) {
	questInstanceID := uuid.New()
	playerID := uuid.New()
	now := time.Now()
	state := api.QuestInstanceStateINPROGRESS
	currentObjective := api.NewOptInt(0)

	questInstance := api.QuestInstance{
		ID:               questInstanceID,
		QuestID:          req.QuestID,
		PlayerID:         playerID,
		State:            state,
		StartedAt:        now,
		CurrentObjective: currentObjective,
		ProgressData:     api.OptQuestInstanceProgressData{},
		UpdatedAt:        api.NewOptDateTime(now),
		CompletedAt:      api.OptNilDateTime{},
	}

	// Issue: #1607 - Use memory pooling
	response := s.startQuestResponsePool.Get().(*api.StartQuestResponse)
	// Note: Not returning to pool - struct is returned to caller

	response.QuestInstance = questInstance
	response.Dialogue = api.OptDialogueNode{}

	// Invalidate player quests cache
	s.questCache.InvalidatePlayerQuests(ctx, questInstance.PlayerID)

	return response, nil
}

// GetQuest returns quest by ID (with 3-tier cache) - Issue: #1609
func (s *Service) GetQuest(ctx context.Context, questID uuid.UUID) (api.GetQuestRes, error) {
	// Try cache first (L1 → L2 → L3)
	quest, err := s.questCache.GetQuest(ctx, questID)
	if err == nil && quest != nil {
		return quest, nil
	}

	// Fallback to mock data (will be replaced when repository is implemented)
	playerID := uuid.New()
	questDefinitionID := uuid.New()
	now := time.Now()
	state := api.QuestInstanceStateINPROGRESS
	currentObjective := api.NewOptInt(1)

	questInstance := &api.QuestInstance{
		ID:               questID,
		QuestID:          questDefinitionID,
		PlayerID:         playerID,
		State:            state,
		StartedAt:        now.Add(-24 * time.Hour),
		CurrentObjective: currentObjective,
		ProgressData:     api.OptQuestInstanceProgressData{},
		UpdatedAt:        api.NewOptDateTime(now),
		CompletedAt:      api.OptNilDateTime{},
	}

	// Cache the result
	s.questCache.storeInRedisQuest(ctx, questID, questInstance)
	s.questCache.storeInMemoryQuest(questID, questInstance)

	return questInstance, nil
}

// GetPlayerQuests returns player quests (with 3-tier cache) - Issue: #1609
func (s *Service) GetPlayerQuests(ctx context.Context, params api.GetPlayerQuestsParams) (api.GetPlayerQuestsRes, error) {
	// Try cache first (L1 → L2 → L3)
	quests, err := s.questCache.GetPlayerQuests(ctx, params.PlayerID)
	if err == nil && len(quests) > 0 {
		return &api.QuestListResponse{
			Quests: quests,
			Total:  len(quests),
		}, nil
	}
	questID1 := uuid.New()
	questID2 := uuid.New()
	questDefinitionID1 := uuid.New()
	questDefinitionID2 := uuid.New()
	now := time.Now()
	state1 := api.QuestInstanceStateINPROGRESS
	state2 := api.QuestInstanceStateCOMPLETED
	currentObjective1 := api.NewOptInt(1)
	currentObjective2 := api.NewOptInt(2)
	completedAt2 := now.Add(-1 * time.Hour)

	quests = []api.QuestInstance{
		{
			ID:               questID1,
			QuestID:          questDefinitionID1,
			PlayerID:         params.PlayerID,
			State:            state1,
			StartedAt:        now.Add(-48 * time.Hour),
			CurrentObjective: currentObjective1,
			ProgressData:     api.OptQuestInstanceProgressData{},
			UpdatedAt:        api.NewOptDateTime(now),
			CompletedAt:      api.OptNilDateTime{},
		},
		{
			ID:               questID2,
			QuestID:          questDefinitionID2,
			PlayerID:         params.PlayerID,
			State:            state2,
			StartedAt:        now.Add(-72 * time.Hour),
			CurrentObjective: currentObjective2,
			ProgressData:     api.OptQuestInstanceProgressData{},
			UpdatedAt:        api.NewOptDateTime(completedAt2),
			CompletedAt:      api.NewOptNilDateTime(completedAt2),
		},
	}

	// Issue: #1607 - Use memory pooling
	response := s.questListResponsePool.Get().(*api.QuestListResponse)
	// Note: Not returning to pool - struct is returned to caller

	response.Quests = quests
	response.Total = len(quests)

	// Cache the result
	s.questCache.storeInRedisPlayerQuests(ctx, params.PlayerID, quests)
	s.questCache.storeInMemoryQuestList(fmt.Sprintf("quests:player:%s", params.PlayerID.String()), quests)

	return response, nil
}

// CancelQuest cancels a quest (invalidates cache) - Issue: #1609
func (s *Service) CancelQuest(ctx context.Context, questID uuid.UUID) (api.CancelQuestRes, error) {
	playerID := uuid.New()
	questDefinitionID := uuid.New()
	now := time.Now()
	state := api.QuestInstanceStateCANCELLED
	currentObjective := api.NewOptInt(0)

	questInstance := &api.QuestInstance{
		ID:               questID,
		QuestID:          questDefinitionID,
		PlayerID:         playerID,
		State:            state,
		StartedAt:        now.Add(-24 * time.Hour),
		CurrentObjective: currentObjective,
		ProgressData:     api.OptQuestInstanceProgressData{},
		UpdatedAt:        api.NewOptDateTime(now),
		CompletedAt:      api.OptNilDateTime{},
	}

	// Invalidate caches
	s.questCache.InvalidateQuest(ctx, questID)
	s.questCache.InvalidatePlayerQuests(ctx, playerID)

	return questInstance, nil
}

// CompleteQuest completes a quest (invalidates cache) - Issue: #1609
func (s *Service) CompleteQuest(ctx context.Context, questID uuid.UUID, req *api.CompleteQuestRequest) (api.CompleteQuestRes, error) {
	playerID := uuid.New()
	questDefinitionID := uuid.New()
	now := time.Now()
	state := api.QuestInstanceStateCOMPLETED
	currentObjective := api.NewOptInt(3)
	experience := api.NewOptInt(1000)
	currency := api.NewOptInt(5000)

	questInstance := api.QuestInstance{
		ID:               questID,
		QuestID:          questDefinitionID,
		PlayerID:         playerID,
		State:            state,
		StartedAt:        now.Add(-48 * time.Hour),
		CurrentObjective: currentObjective,
		ProgressData:     api.OptQuestInstanceProgressData{},
		UpdatedAt:        api.NewOptDateTime(now),
		CompletedAt:      api.NewOptNilDateTime(now),
	}

	// Issue: #1607 - Use memory pooling
	rewards := s.questRewardsPool.Get().(*api.QuestRewards)
	// Note: Not returning to pool - struct is returned to caller

	rewards.Experience = experience
	rewards.Currency = currency
	rewards.Items = []api.QuestRewardsItemsItem{}
	rewards.Reputation = api.OptQuestRewardsReputation{}
	rewards.Titles = []string{}

	// Issue: #1607 - Use memory pooling
	response := s.completeQuestResponsePool.Get().(*api.CompleteQuestResponse)
	// Note: Not returning to pool - struct is returned to caller

	response.QuestInstance = questInstance
	response.Rewards = *rewards

	// Invalidate caches
	s.questCache.InvalidateQuest(ctx, questID)
	s.questCache.InvalidatePlayerQuests(ctx, playerID)

	return response, nil
}

// ImportQuestDefinition imports a quest definition from YAML content
func (s *Service) ImportQuestDefinition(ctx context.Context, questDef *QuestDefinition) error {
	// Check if quest already exists
	existing, err := s.repo.GetQuestDefinitionByID(ctx, questDef.QuestID)
	if err != nil && err.Error() != "not found" {
		return fmt.Errorf("failed to check existing quest: %w", err)
	}

	if existing != nil {
		// Update existing quest
		return s.repo.UpdateQuestDefinition(ctx, questDef)
	} else {
		// Create new quest
		return s.repo.CreateQuestDefinition(ctx, questDef)
	}
}

