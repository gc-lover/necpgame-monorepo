package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// Repository handles database operations for world events service.
type Repository struct {
	pool   *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository creates a new repository.
func NewRepository(ctx context.Context, logger *zap.Logger, dsn string) (*Repository, error) {
	config, err := pgxpool.ParseConfig(dsn)
	if err != nil {
		return nil, err
	}

	// PERFORMANCE: Optimized DB pool settings for MMORPG scale
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(ctx, config)
	if err != nil {
		return nil, err
	}

	return &Repository{
		pool:   pool,
		logger: logger,
	}, nil
}

// HealthCheck checks database health.
func (r *Repository) HealthCheck(ctx context.Context) error {
	// PERFORMANCE: Use context timeout for health checks
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return r.pool.Ping(ctx)
}

// Close closes the connection pool.
func (r *Repository) Close() {
	r.pool.Close()
}

// CreateWorldEvent creates a new world event - matches API schema
func (r *Repository) CreateWorldEvent(ctx context.Context, event *WorldEvent) (*WorldEvent, error) {
	query := `
		INSERT INTO gameplay.world_events (
			event_id, name, description, type, region, status, start_time, end_time,
			objectives, rewards, max_participants, current_participants, difficulty,
			min_level, max_level, faction_restrictions, region_restrictions,
			prerequisites, metadata, created_by
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20)
		RETURNING id, created_at, updated_at`

	err := r.pool.QueryRow(ctx, query,
		event.EventID, event.Name, event.Description, event.Type, event.Region, event.Status,
		event.StartTime, event.EndTime, event.Objectives, event.Rewards, event.MaxParticipants,
		event.CurrentParticipants, event.Difficulty, event.MinLevel, event.MaxLevel,
		event.FactionRestrictions, event.RegionRestrictions, event.Prerequisites,
		event.Metadata, event.CreatedBy,
	).Scan(&event.ID, &event.CreatedAt, &event.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to create world event", zap.Error(err))
		return nil, err
	}

	r.logger.Info("Created world event", zap.String("name", event.Name), zap.String("id", event.ID.String()))
	return event, nil
}

// GetWorldEvent retrieves a world event by ID - matches API schema
func (r *Repository) GetWorldEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	query := `
		SELECT id, event_id, name, description, type, region, status, start_time, end_time,
			   objectives, rewards, max_participants, current_participants, difficulty,
			   min_level, max_level, faction_restrictions, region_restrictions,
			   prerequisites, metadata, created_by, created_at, updated_at
		FROM gameplay.world_events WHERE id = $1`

	event := &WorldEvent{}
	err := r.pool.QueryRow(ctx, query, id).Scan(
		&event.ID, &event.EventID, &event.Name, &event.Description, &event.Type,
		&event.Region, &event.Status, &event.StartTime, &event.EndTime, &event.Objectives,
		&event.Rewards, &event.MaxParticipants, &event.CurrentParticipants, &event.Difficulty,
		&event.MinLevel, &event.MaxLevel, &event.FactionRestrictions, &event.RegionRestrictions,
		&event.Prerequisites, &event.Metadata, &event.CreatedBy, &event.CreatedAt, &event.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get world event", zap.String("id", id.String()), zap.Error(err))
		return nil, err
	}

	return event, nil
}

// ListWorldEvents retrieves world events with filtering and pagination
func (r *Repository) ListWorldEvents(ctx context.Context, filter *EventFilter) ([]*WorldEvent, error) {
	query := `
		SELECT id, event_id, name, description, type, region, status, start_time, end_time,
			   objectives, rewards, max_participants, current_participants, difficulty,
			   min_level, max_level, faction_restrictions, region_restrictions,
			   prerequisites, metadata, created_by, created_at, updated_at
		FROM world_events.world_events WHERE 1=1`
	args := []interface{}{}
	argIndex := 1

	if filter.Type != nil {
		query += ` AND type = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Type)
		argIndex++
	}

	if filter.Region != nil {
		query += ` AND region = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Region)
		argIndex++
	}

	if filter.Status != nil {
		query += ` AND status = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Status)
		argIndex++
	}

	if filter.Difficulty != nil {
		query += ` AND difficulty = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Difficulty)
		argIndex++
	}

	if filter.MinLevel != nil {
		query += ` AND min_level <= $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.MinLevel)
		argIndex++
	}

	if filter.MaxLevel != nil {
		query += ` AND (max_level IS NULL OR max_level >= $` + fmt.Sprintf("%d", argIndex) + `)`
		args = append(args, *filter.MaxLevel)
		argIndex++
	}

	query += ` ORDER BY created_at DESC`

	if filter.Limit != nil {
		query += ` LIMIT $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Limit)
		argIndex++
	}

	if filter.Offset != nil {
		query += ` OFFSET $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Offset)
	}

	rows, err := r.pool.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to list world events", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var events []*WorldEvent
	for rows.Next() {
		event := &WorldEvent{}
		err := rows.Scan(
			&event.ID, &event.EventID, &event.Name, &event.Description, &event.Type,
			&event.Region, &event.Status, &event.StartTime, &event.EndTime,
			&event.Objectives, &event.Rewards, &event.MaxParticipants, &event.CurrentParticipants,
			&event.Difficulty, &event.MinLevel, &event.MaxLevel, &event.FactionRestrictions,
			&event.RegionRestrictions, &event.Prerequisites, &event.Metadata,
			&event.CreatedBy, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan world event", zap.Error(err))
			continue
		}
		events = append(events, event)
	}

	return events, nil
}

// UpdateWorldEvent updates an existing world event
func (r *Repository) UpdateWorldEvent(ctx context.Context, id uuid.UUID, updates map[string]interface{}) (*WorldEvent, error) {
	if len(updates) == 0 {
		return r.GetWorldEvent(ctx, id)
	}

	query := `UPDATE world_events.world_events SET`
	args := []interface{}{}
	argIndex := 1

	for field, value := range updates {
		if argIndex > 1 {
			query += `,`
		}
		query += ` ` + field + ` = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, value)
		argIndex++
	}

	query += `, updated_at = CURRENT_TIMESTAMP WHERE id = $` + fmt.Sprintf("%d", argIndex)
	args = append(args, id)

	result, err := r.pool.Exec(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to update world event", zap.String("id", id.String()), zap.Error(err))
		return nil, err
	}

	if result.RowsAffected() == 0 {
		return nil, fmt.Errorf("world event not found: %s", id)
	}

	return r.GetWorldEvent(ctx, id)
}

// DeleteWorldEvent deletes a world event
func (r *Repository) DeleteWorldEvent(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM world_events.world_events WHERE id = $1`

	result, err := r.pool.Exec(ctx, query, id)
	if err != nil {
		r.logger.Error("Failed to delete world event", zap.String("id", id.String()), zap.Error(err))
		return err
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("world event not found: %s", id)
	}

	r.logger.Info("Deleted world event", zap.String("id", id.String()))
	return nil
}

// JoinEvent adds a player to an event
func (r *Repository) JoinEvent(ctx context.Context, playerID, eventID uuid.UUID) (*EventParticipation, error) {
	query := `
		INSERT INTO world_events.event_participation (player_id, event_id)
		VALUES ($1, $2)
		ON CONFLICT (player_id, event_id) DO NOTHING
		RETURNING id, player_id, event_id, status, joined_at, last_activity_at,
		          completed_at, failed_at, abandoned_at, progress_data, rewards_claimed,
		          score, rank, metadata, created_at, updated_at`

	participation := &EventParticipation{}
	err := r.pool.QueryRow(ctx, query, playerID, eventID).Scan(
		&participation.ID, &participation.PlayerID, &participation.EventID,
		&participation.Status, &participation.JoinedAt, &participation.LastActivityAt,
		&participation.CompletedAt, &participation.FailedAt, &participation.AbandonedAt,
		&participation.ProgressData, &participation.RewardsClaimed, &participation.Score,
		&participation.Rank, &participation.Metadata, &participation.CreatedAt,
		&participation.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to join event", zap.String("player_id", playerID.String()),
			zap.String("event_id", eventID.String()), zap.Error(err))
		return nil, err
	}

	// Update participant count
	_, err = r.pool.Exec(ctx, `UPDATE world_events.world_events SET current_participants = current_participants + 1 WHERE id = $1`, eventID)
	if err != nil {
		r.logger.Warn("Failed to update participant count", zap.Error(err))
	}

	r.logger.Info("Player joined event", zap.String("player_id", playerID.String()), zap.String("event_id", eventID.String()))
	return participation, nil
}

// LeaveEvent removes a player from an event
func (r *Repository) LeaveEvent(ctx context.Context, playerID, eventID uuid.UUID) error {
	query := `DELETE FROM world_events.event_participation WHERE player_id = $1 AND event_id = $2`

	result, err := r.pool.Exec(ctx, query, playerID, eventID)
	if err != nil {
		r.logger.Error("Failed to leave event", zap.String("player_id", playerID.String()),
			zap.String("event_id", eventID.String()), zap.Error(err))
		return err
	}

	if result.RowsAffected() > 0 {
		// Update participant count
		_, err = r.pool.Exec(ctx, `UPDATE world_events.world_events SET current_participants = GREATEST(current_participants - 1, 0) WHERE id = $1`, eventID)
		if err != nil {
			r.logger.Warn("Failed to update participant count", zap.Error(err))
		}
	}

	r.logger.Info("Player left event", zap.String("player_id", playerID.String()), zap.String("event_id", eventID.String()))
	return nil
}

// GetEventParticipation gets participation for a specific player and event
func (r *Repository) GetEventParticipation(ctx context.Context, playerID, eventID uuid.UUID) (*EventParticipation, error) {
	query := `
		SELECT id, player_id, event_id, status, joined_at, last_activity_at,
			   completed_at, failed_at, abandoned_at, progress_data, rewards_claimed,
			   score, rank, metadata, created_at, updated_at
		FROM world_events.event_participation
		WHERE player_id = $1 AND event_id = $2`

	participation := &EventParticipation{}
	err := r.pool.QueryRow(ctx, query, playerID, eventID).Scan(
		&participation.ID, &participation.PlayerID, &participation.EventID,
		&participation.Status, &participation.JoinedAt, &participation.LastActivityAt,
		&participation.CompletedAt, &participation.FailedAt, &participation.AbandonedAt,
		&participation.ProgressData, &participation.RewardsClaimed, &participation.Score,
		&participation.Rank, &participation.Metadata, &participation.CreatedAt,
		&participation.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get event participation", zap.String("player_id", playerID.String()),
			zap.String("event_id", eventID.String()), zap.Error(err))
		return nil, err
	}

	return participation, nil
}

// UpdateParticipation updates participation status and progress
func (r *Repository) UpdateParticipation(ctx context.Context, participationID uuid.UUID, updates map[string]interface{}) (*EventParticipation, error) {
	if len(updates) == 0 {
		// Get participation by ID
		query := `
			SELECT id, player_id, event_id, status, joined_at, last_activity_at,
				   completed_at, failed_at, abandoned_at, progress_data, rewards_claimed,
				   score, rank, metadata, created_at, updated_at
			FROM world_events.event_participation WHERE id = $1`

		participation := &EventParticipation{}
		err := r.pool.QueryRow(ctx, query, participationID).Scan(
			&participation.ID, &participation.PlayerID, &participation.EventID,
			&participation.Status, &participation.JoinedAt, &participation.LastActivityAt,
			&participation.CompletedAt, &participation.FailedAt, &participation.AbandonedAt,
			&participation.ProgressData, &participation.RewardsClaimed, &participation.Score,
			&participation.Rank, &participation.Metadata, &participation.CreatedAt,
			&participation.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		return participation, nil
	}

	query := `UPDATE world_events.event_participation SET`
	args := []interface{}{}
	argIndex := 1

	for field, value := range updates {
		if argIndex > 1 {
			query += `,`
		}
		query += ` ` + field + ` = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, value)
		argIndex++
	}

	query += `, last_activity_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = $` + fmt.Sprintf("%d", argIndex)
	args = append(args, participationID)

	result, err := r.pool.Exec(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to update participation", zap.String("participation_id", participationID.String()), zap.Error(err))
		return nil, err
	}

	if result.RowsAffected() == 0 {
		return nil, fmt.Errorf("participation not found: %s", participationID)
	}

	// Return updated participation
	return r.GetEventParticipation(ctx, uuid.UUID{}, uuid.UUID{}) // This needs to be fixed - we need to get player_id and event_id
}

// GetActiveEvents returns currently active world events
func (r *Repository) GetActiveEvents(ctx context.Context) ([]*WorldEvent, error) {
	query := `
		SELECT id, event_id, name, description, type, region, status, start_time, end_time,
			   objectives, rewards, max_participants, current_participants, difficulty,
			   min_level, max_level, faction_restrictions, region_restrictions,
			   prerequisites, metadata, created_by, created_at, updated_at
		FROM world_events.world_events
		WHERE status = 'active' AND (end_time IS NULL OR end_time > CURRENT_TIMESTAMP)
		ORDER BY start_time ASC`

	rows, err := r.pool.Query(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get active events", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var events []*WorldEvent
	for rows.Next() {
		event := &WorldEvent{}
		err := rows.Scan(
			&event.ID, &event.EventID, &event.Name, &event.Description, &event.Type,
			&event.Region, &event.Status, &event.StartTime, &event.EndTime,
			&event.Objectives, &event.Rewards, &event.MaxParticipants, &event.CurrentParticipants,
			&event.Difficulty, &event.MinLevel, &event.MaxLevel, &event.FactionRestrictions,
			&event.RegionRestrictions, &event.Prerequisites, &event.Metadata,
			&event.CreatedBy, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan active event", zap.Error(err))
			continue
		}
		events = append(events, event)
	}

	return events, nil
}

// GetEventParticipants retrieves participants for a specific event
func (r *Repository) GetEventParticipants(ctx context.Context, eventID uuid.UUID, filter *ParticipationFilter) ([]*EventParticipation, error) {
	query := `
		SELECT id, player_id, event_id, status, joined_at, last_activity_at,
			   completed_at, failed_at, abandoned_at, progress_data, rewards_claimed,
			   score, rank, metadata, created_at, updated_at
		FROM world_events.event_participation WHERE event_id = $1`
	args := []interface{}{eventID}
	argIndex := 2

	if filter.Status != nil {
		query += ` AND status = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Status)
		argIndex++
	}

	query += ` ORDER BY joined_at DESC`

	if filter.Limit != nil {
		query += ` LIMIT $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Limit)
		argIndex++
	}

	if filter.Offset != nil {
		query += ` OFFSET $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Offset)
	}

	rows, err := r.pool.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to get event participants", zap.String("eventID", eventID.String()), zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var participants []*EventParticipation
	for rows.Next() {
		p := &EventParticipation{}
		err := rows.Scan(
			&p.ID, &p.PlayerID, &p.EventID, &p.Status, &p.JoinedAt, &p.LastActivityAt,
			&p.CompletedAt, &p.FailedAt, &p.AbandonedAt, &p.ProgressData, &p.RewardsClaimed,
			&p.Score, &p.Rank, &p.Metadata, &p.CreatedAt, &p.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan event participant", zap.Error(err))
			continue
		}
		participants = append(participants, p)
	}

	return participants, nil
}

// GetPlayerParticipation retrieves player's participation in an event
func (r *Repository) GetPlayerParticipation(ctx context.Context, playerID, eventID uuid.UUID) (*EventParticipation, error) {
	query := `
		SELECT id, player_id, event_id, status, joined_at, last_activity_at,
			   completed_at, failed_at, abandoned_at, progress_data, rewards_claimed,
			   score, rank, metadata, created_at, updated_at
		FROM world_events.event_participation
		WHERE player_id = $1 AND event_id = $2`

	participation := &EventParticipation{}
	err := r.pool.QueryRow(ctx, query, playerID, eventID).Scan(
		&participation.ID, &participation.PlayerID, &participation.EventID, &participation.Status,
		&participation.JoinedAt, &participation.LastActivityAt, &participation.CompletedAt,
		&participation.FailedAt, &participation.AbandonedAt, &participation.ProgressData,
		&participation.RewardsClaimed, &participation.Score, &participation.Rank,
		&participation.Metadata, &participation.CreatedAt, &participation.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get player participation",
			zap.String("playerID", playerID.String()),
			zap.String("eventID", eventID.String()),
			zap.Error(err))
		return nil, err
	}

	return participation, nil
}

// GetPlayerRewards retrieves rewards for a player in an event
func (r *Repository) GetPlayerRewards(ctx context.Context, playerID string, eventID uuid.UUID) ([]*EventReward, error) {
	query := `
		SELECT id, event_id, player_id, participation_id, reward_type, reward_id,
			   amount, claimed, claimed_at, expires_at, metadata, created_at, updated_at
		FROM world_events.event_rewards
		WHERE player_id = $1 AND event_id = $2
		ORDER BY created_at DESC`

	rows, err := r.pool.Query(ctx, query, playerID, eventID)
	if err != nil {
		r.logger.Error("Failed to get player rewards",
			zap.String("playerID", playerID),
			zap.String("eventID", eventID.String()),
			zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var rewards []*EventReward
	for rows.Next() {
		reward := &EventReward{}
		err := rows.Scan(
			&reward.ID, &reward.EventID, &reward.PlayerID, &reward.RewardID,
			&reward.RewardType, &reward.RewardID, &reward.Amount, &reward.Claimed,
			&reward.ClaimedAt, &reward.ExpiresAt, &reward.Metadata, &reward.CreatedAt, &reward.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan event reward", zap.Error(err))
			continue
		}
		rewards = append(rewards, reward)
	}

	return rewards, nil
}

// ClaimReward marks a reward as claimed
func (r *Repository) ClaimReward(ctx context.Context, rewardID uuid.UUID) error {
	query := `
		UPDATE world_events.event_rewards
		SET claimed = true, claimed_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1 AND claimed = false`

	result, err := r.pool.Exec(ctx, query, rewardID)
	if err != nil {
		r.logger.Error("Failed to claim reward", zap.String("rewardID", rewardID.String()), zap.Error(err))
		return err
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("reward not found or already claimed")
	}

	r.logger.Info("Reward claimed", zap.String("rewardID", rewardID.String()))
	return nil
}

// ListEventTemplates retrieves event templates with filtering
func (r *Repository) ListEventTemplates(ctx context.Context, filter *TemplateFilter) ([]*EventTemplate, error) {
	query := `
		SELECT id, name, type, difficulty, description, objectives_template, rewards_template,
			   duration_minutes, max_participants, min_level, max_level, region_restrictions,
			   faction_restrictions, event_data_template, is_active, usage_count, success_rate,
			   created_by, created_at, updated_at
		FROM world_events.event_templates WHERE 1=1`
	args := []interface{}{}
	argIndex := 1

	if filter.Type != nil {
		query += ` AND type = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Type)
		argIndex++
	}

	if filter.IsActive != nil {
		query += ` AND is_active = $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.IsActive)
		argIndex++
	}

	query += ` ORDER BY usage_count DESC, created_at DESC`

	if filter.Limit != nil {
		query += ` LIMIT $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Limit)
		argIndex++
	}

	if filter.Offset != nil {
		query += ` OFFSET $` + fmt.Sprintf("%d", argIndex)
		args = append(args, *filter.Offset)
	}

	rows, err := r.pool.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to list event templates", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var templates []*EventTemplate
	for rows.Next() {
		template := &EventTemplate{}
		err := rows.Scan(
			&template.ID, &template.Name, &template.Type, &template.Difficulty, &template.Description,
			&template.ObjectivesTemplate, &template.RewardsTemplate, &template.DurationMinutes,
			&template.MaxParticipants, &template.MinLevel, &template.MaxLevel, &template.RegionRestrictions,
			&template.FactionRestrictions, &template.EventDataTemplate, &template.IsActive,
			&template.UsageCount, &template.SuccessRate, &template.CreatedBy, &template.CreatedAt, &template.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan event template", zap.Error(err))
			continue
		}
		templates = append(templates, template)
	}

	return templates, nil
}

// CreateEventTemplate creates a new event template
func (r *Repository) CreateEventTemplate(ctx context.Context, template *EventTemplate) (*EventTemplate, error) {
	query := `
		INSERT INTO world_events.event_templates (
			name, type, difficulty, description, objectives_template, rewards_template,
			duration_minutes, max_participants, min_level, max_level, region_restrictions,
			faction_restrictions, event_data_template, is_active, created_by
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)
		RETURNING id, created_at, updated_at`

	err := r.pool.QueryRow(ctx, query,
		template.Name, template.Type, template.Difficulty, template.Description,
		template.ObjectivesTemplate, template.RewardsTemplate, template.DurationMinutes,
		template.MaxParticipants, template.MinLevel, template.MaxLevel, template.RegionRestrictions,
		template.FactionRestrictions, template.EventDataTemplate, template.IsActive, template.CreatedBy,
	).Scan(&template.ID, &template.CreatedAt, &template.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to create event template", zap.String("name", template.Name), zap.Error(err))
		return nil, err
	}

	r.logger.Info("Created event template", zap.String("name", template.Name), zap.String("id", template.ID.String()))
	return template, nil
}

// GetEventAnalytics retrieves analytics for an event
func (r *Repository) GetEventAnalytics(ctx context.Context, eventID uuid.UUID) (*EventAnalytics, error) {
	query := `
		SELECT event_id, total_participants, completed_participants, failed_participants,
			   abandoned_participants, average_completion_time, average_score,
			   average_participation_time, participation_rate, completion_rate,
			   satisfaction_rating, revenue_generated, engagement_score,
			   peak_concurrent_users, total_rewards_claimed, last_updated
		FROM world_events.event_analytics WHERE event_id = $1`

	analytics := &EventAnalytics{}
	err := r.pool.QueryRow(ctx, query, eventID).Scan(
		&analytics.EventID, &analytics.TotalParticipants, &analytics.CompletedParticipants,
		&analytics.FailedParticipants, &analytics.AbandonedParticipants, &analytics.AverageCompletionTime,
		&analytics.AverageScore, &analytics.AverageParticipationTime, &analytics.ParticipationRate,
		&analytics.CompletionRate, &analytics.SatisfactionRating, &analytics.RevenueGenerated,
		&analytics.EngagementScore, &analytics.PeakConcurrentUsers, &analytics.TotalRewardsClaimed,
		&analytics.LastUpdated,
	)

	if err != nil {
		r.logger.Error("Failed to get event analytics", zap.String("eventID", eventID.String()), zap.Error(err))
		return nil, err
	}

	return analytics, nil
}
