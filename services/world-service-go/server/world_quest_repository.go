package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
)

func (r *worldRepository) GetQuestPool(ctx context.Context, poolType models.QuestPoolType, playerLevel *int) ([]models.QuestPoolEntry, error) {
	var entries []models.QuestPoolEntry
	query := `
		SELECT quest_id, weight, min_level, max_level, is_active
		FROM quest_pools
		WHERE pool_type = $1 AND is_active = true
	`
	args := []interface{}{poolType}
	
	if playerLevel != nil {
		query += ` AND min_level <= $2 AND (max_level IS NULL OR max_level >= $2)`
		args = append(args, *playerLevel)
	}
	
	err := r.db.SelectContext(ctx, &entries, query, args...)
	return entries, err
}

func (r *worldRepository) AssignQuest(ctx context.Context, playerID, questID uuid.UUID, poolType models.QuestPoolType) error {
	query := `
		INSERT INTO player_quests (id, player_id, quest_id, pool_type, assigned_at, reset_date, created_at, updated_at)
		VALUES (gen_random_uuid(), $1, $2, $3, NOW(), CURRENT_DATE, NOW(), NOW())
	`
	_, err := r.db.ExecContext(ctx, query, playerID, questID, poolType)
	return err
}

func (r *worldRepository) GetPlayerQuests(ctx context.Context, playerID uuid.UUID, poolType *models.QuestPoolType) ([]models.PlayerQuest, error) {
	var quests []models.PlayerQuest
	query := `
		SELECT id, player_id, quest_id, pool_type, assigned_at, completed_at, reset_date, created_at, updated_at
		FROM player_quests
		WHERE player_id = $1
	`
	args := []interface{}{playerID}
	
	if poolType != nil {
		query += ` AND pool_type = $2`
		args = append(args, *poolType)
	}
	
	err := r.db.SelectContext(ctx, &quests, query, args...)
	return quests, err
}

