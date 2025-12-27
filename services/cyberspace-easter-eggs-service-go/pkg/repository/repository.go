// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Repository layer for Easter Eggs Service - Enterprise-grade data access

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/lib/pq"

	"cyberspace-easter-eggs-service-go/pkg/models"
)

// RepositoryInterface defines the repository interface for dependency injection
type RepositoryInterface interface {
	// Easter egg CRUD operations
	GetEasterEgg(ctx context.Context, id string) (*models.EasterEgg, error)
	GetEasterEggsByCategory(ctx context.Context, category string, limit, offset int) ([]*models.EasterEgg, error)
	GetEasterEggsByDifficulty(ctx context.Context, difficulty string, limit, offset int) ([]*models.EasterEgg, error)
	CreateEasterEgg(ctx context.Context, egg *models.EasterEgg) error
	UpdateEasterEgg(ctx context.Context, egg *models.EasterEgg) error
	DeleteEasterEgg(ctx context.Context, id string) error

	// Player progress operations
	GetPlayerProgress(ctx context.Context, playerID, easterEggID string) (*models.PlayerEasterEggProgress, error)
	UpdatePlayerProgress(ctx context.Context, progress *models.PlayerEasterEggProgress) error
	GetPlayerProfile(ctx context.Context, playerID string) (*models.PlayerEasterEggProfile, error)
	GetPlayerDiscoveredEggs(ctx context.Context, playerID string) ([]string, error)

	// Discovery operations
	CreateDiscoveryAttempt(ctx context.Context, attempt *models.EasterEggDiscoveryAttempt) error
	GetDiscoveryAttempts(ctx context.Context, playerID, easterEggID string, limit int) ([]*models.EasterEggDiscoveryAttempt, error)
	RecordSuccessfulDiscovery(ctx context.Context, playerID, easterEggID string) error

	// Statistics operations
	GetEasterEggStatistics(ctx context.Context, easterEggID string) (*models.EasterEggStatistics, error)
	GetCategoryStatistics(ctx context.Context) ([]*models.EasterEggCategoryStats, error)
	UpdateEasterEggStats(ctx context.Context, easterEggID string) error

	// Hint operations
	GetHintsForEasterEgg(ctx context.Context, easterEggID string, maxLevel int) ([]*models.DiscoveryHint, error)
	RecordHintUsage(ctx context.Context, playerID, easterEggID string, hintLevel int) error

	// Event operations
	CreateEasterEggEvent(ctx context.Context, event *models.EasterEggEvent) error
	GetEasterEggEvents(ctx context.Context, easterEggID string, limit int) ([]*models.EasterEggEvent, error)

	// Challenge operations
	GetActiveChallenges(ctx context.Context) ([]*models.EasterEggChallenge, error)
	GetPlayerChallengeProgress(ctx context.Context, playerID, challengeID string) (int, error)

	// Health check
	HealthCheck(ctx context.Context) error
}

// Repository implements RepositoryInterface with PostgreSQL
type Repository struct {
	db *sql.DB
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB) RepositoryInterface {
	return &Repository{db: db}
}

// GetEasterEgg retrieves a single easter egg by ID
func (r *Repository) GetEasterEgg(ctx context.Context, id string) (*models.EasterEgg, error) {
	query := `
		SELECT id, name, category, difficulty, description, content,
			   location, discovery_method, rewards, lore_connections,
			   status, created_at, updated_at
		FROM easter_eggs WHERE id = $1 AND status = 'active'
	`

	var egg models.EasterEgg
	var locationData, discoveryData, rewardsData, loreData []byte

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&egg.ID, &egg.Name, &egg.Category, &egg.Difficulty, &egg.Description, &egg.Content,
		&locationData, &discoveryData, &rewardsData, &loreData,
		&egg.Status, &egg.CreatedAt, &egg.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("easter egg not found: %s", id)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get easter egg: %w", err)
	}

	// Unmarshal JSON data
	if err := json.Unmarshal(locationData, &egg.Location); err != nil {
		return nil, fmt.Errorf("failed to unmarshal location: %w", err)
	}
	if err := json.Unmarshal(discoveryData, &egg.DiscoveryMethod); err != nil {
		return nil, fmt.Errorf("failed to unmarshal discovery method: %w", err)
	}
	if err := json.Unmarshal(rewardsData, &egg.Rewards); err != nil {
		return nil, fmt.Errorf("failed to unmarshal rewards: %w", err)
	}
	if err := json.Unmarshal(loreData, &egg.LoreConnections); err != nil {
		return nil, fmt.Errorf("failed to unmarshal lore connections: %w", err)
	}

	return &egg, nil
}

// GetEasterEggsByCategory retrieves easter eggs by category with pagination
func (r *Repository) GetEasterEggsByCategory(ctx context.Context, category string, limit, offset int) ([]*models.EasterEgg, error) {
	query := `
		SELECT id, name, category, difficulty, description, content,
			   location, discovery_method, rewards, lore_connections,
			   status, created_at, updated_at
		FROM easter_eggs
		WHERE category = $1 AND status = 'active'
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, category, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to query easter eggs by category: %w", err)
	}
	defer rows.Close()

	var eggs []*models.EasterEgg
	for rows.Next() {
		var egg models.EasterEgg
		var locationData, discoveryData, rewardsData, loreData []byte

		err := rows.Scan(
			&egg.ID, &egg.Name, &egg.Category, &egg.Difficulty, &egg.Description, &egg.Content,
			&locationData, &discoveryData, &rewardsData, &loreData,
			&egg.Status, &egg.CreatedAt, &egg.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan easter egg: %w", err)
		}

		// Unmarshal JSON data
		if err := json.Unmarshal(locationData, &egg.Location); err != nil {
			continue // Skip invalid data
		}
		if err := json.Unmarshal(discoveryData, &egg.DiscoveryMethod); err != nil {
			continue
		}
		if err := json.Unmarshal(rewardsData, &egg.Rewards); err != nil {
			continue
		}
		if err := json.Unmarshal(loreData, &egg.LoreConnections); err != nil {
			continue
		}

		eggs = append(eggs, &egg)
	}

	return eggs, nil
}

// GetEasterEggsByDifficulty retrieves easter eggs by difficulty level
func (r *Repository) GetEasterEggsByDifficulty(ctx context.Context, difficulty string, limit, offset int) ([]*models.EasterEgg, error) {
	query := `
		SELECT id, name, category, difficulty, description, content,
			   location, discovery_method, rewards, lore_connections,
			   status, created_at, updated_at
		FROM easter_eggs
		WHERE difficulty = $1 AND status = 'active'
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, difficulty, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to query easter eggs by difficulty: %w", err)
	}
	defer rows.Close()

	var eggs []*models.EasterEgg
	for rows.Next() {
		var egg models.EasterEgg
		var locationData, discoveryData, rewardsData, loreData []byte

		err := rows.Scan(
			&egg.ID, &egg.Name, &egg.Category, &egg.Difficulty, &egg.Description, &egg.Content,
			&locationData, &discoveryData, &rewardsData, &loreData,
			&egg.Status, &egg.CreatedAt, &egg.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan easter egg: %w", err)
		}

		// Unmarshal JSON data (same as above)
		if err := json.Unmarshal(locationData, &egg.Location); err != nil {
			continue
		}
		if err := json.Unmarshal(discoveryData, &egg.DiscoveryMethod); err != nil {
			continue
		}
		if err := json.Unmarshal(rewardsData, &egg.Rewards); err != nil {
			continue
		}
		if err := json.Unmarshal(loreData, &egg.LoreConnections); err != nil {
			continue
		}

		eggs = append(eggs, &egg)
	}

	return eggs, nil
}

// CreateEasterEgg creates a new easter egg
func (r *Repository) CreateEasterEgg(ctx context.Context, egg *models.EasterEgg) error {
	locationData, _ := json.Marshal(egg.Location)
	discoveryData, _ := json.Marshal(egg.DiscoveryMethod)
	rewardsData, _ := json.Marshal(egg.Rewards)
	loreData, _ := json.Marshal(egg.LoreConnections)

	query := `
		INSERT INTO easter_eggs (id, name, category, difficulty, description, content,
								 location, discovery_method, rewards, lore_connections,
								 status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)
	`

	egg.ID = uuid.New().String()
	egg.CreatedAt = time.Now()
	egg.UpdatedAt = time.Now()
	egg.Status = "active"

	_, err := r.db.ExecContext(ctx, query,
		egg.ID, egg.Name, egg.Category, egg.Difficulty, egg.Description, egg.Content,
		locationData, discoveryData, rewardsData, loreData,
		egg.Status, egg.CreatedAt, egg.UpdatedAt,
	)

	return err
}

// UpdateEasterEgg updates an existing easter egg
func (r *Repository) UpdateEasterEgg(ctx context.Context, egg *models.EasterEgg) error {
	locationData, _ := json.Marshal(egg.Location)
	discoveryData, _ := json.Marshal(egg.DiscoveryMethod)
	rewardsData, _ := json.Marshal(egg.Rewards)
	loreData, _ := json.Marshal(egg.LoreConnections)

	query := `
		UPDATE easter_eggs
		SET name = $2, category = $3, difficulty = $4, description = $5, content = $6,
			location = $7, discovery_method = $8, rewards = $9, lore_connections = $10,
			updated_at = $11
		WHERE id = $1
	`

	egg.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		egg.ID, egg.Name, egg.Category, egg.Difficulty, egg.Description, egg.Content,
		locationData, discoveryData, rewardsData, loreData, egg.UpdatedAt,
	)

	return err
}

// DeleteEasterEgg marks an easter egg as disabled (soft delete)
func (r *Repository) DeleteEasterEgg(ctx context.Context, id string) error {
	query := `UPDATE easter_eggs SET status = 'disabled', updated_at = $2 WHERE id = $1`
	_, err := r.db.ExecContext(ctx, query, id, time.Now())
	return err
}

// GetPlayerProgress retrieves player progress for a specific easter egg
func (r *Repository) GetPlayerProgress(ctx context.Context, playerID, easterEggID string) (*models.PlayerEasterEggProgress, error) {
	query := `
		SELECT player_id, easter_egg_id, status, discovered_at, completed_at,
			   rewards_claimed, hint_level, visit_count, last_visited
		FROM player_easter_egg_progress
		WHERE player_id = $1 AND easter_egg_id = $2
	`

	var progress models.PlayerEasterEggProgress
	var rewardsData []byte
	var discoveredAt, completedAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, playerID, easterEggID).Scan(
		&progress.PlayerID, &progress.EasterEggID, &progress.Status,
		&discoveredAt, &completedAt, &rewardsData, &progress.HintLevel,
		&progress.VisitCount, &progress.LastVisited,
	)

	if err == sql.ErrNoRows {
		// Return default progress
		return &models.PlayerEasterEggProgress{
			PlayerID:    playerID,
			EasterEggID: easterEggID,
			Status:      "undiscovered",
			HintLevel:   0,
			VisitCount:  0,
			LastVisited: time.Now(),
		}, nil
	}

	if err != nil {
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	if discoveredAt.Valid {
		progress.DiscoveredAt = &discoveredAt.Time
	}
	if completedAt.Valid {
		progress.CompletedAt = &completedAt.Time
	}

	if err := json.Unmarshal(rewardsData, &progress.RewardsClaimed); err != nil {
		progress.RewardsClaimed = []string{}
	}

	return &progress, nil
}

// UpdatePlayerProgress updates or creates player progress
func (r *Repository) UpdatePlayerProgress(ctx context.Context, progress *models.PlayerEasterEggProgress) error {
	rewardsData, _ := json.Marshal(progress.RewardsClaimed)

	query := `
		INSERT INTO player_easter_egg_progress (
			player_id, easter_egg_id, status, discovered_at, completed_at,
			rewards_claimed, hint_level, visit_count, last_visited
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (player_id, easter_egg_id)
		DO UPDATE SET
			status = EXCLUDED.status,
			discovered_at = EXCLUDED.discovered_at,
			completed_at = EXCLUDED.completed_at,
			rewards_claimed = EXCLUDED.rewards_claimed,
			hint_level = EXCLUDED.hint_level,
			visit_count = EXCLUDED.visit_count,
			last_visited = EXCLUDED.last_visited
	`

	_, err := r.db.ExecContext(ctx, query,
		progress.PlayerID, progress.EasterEggID, progress.Status,
		progress.DiscoveredAt, progress.CompletedAt, rewardsData,
		progress.HintLevel, progress.VisitCount, progress.LastVisited,
	)

	return err
}

// GetPlayerProfile retrieves overall player easter egg profile
func (r *Repository) GetPlayerProfile(ctx context.Context, playerID string) (*models.PlayerEasterEggProfile, error) {
	query := `
		SELECT player_id, total_discovered, total_completed, favorite_category,
			   average_difficulty, total_hints_used, collection_progress,
			   achievement_level, last_activity, created_at, updated_at
		FROM player_easter_egg_profiles WHERE player_id = $1
	`

	var profile models.PlayerEasterEggProfile
	err := r.db.QueryRowContext(ctx, query, playerID).Scan(
		&profile.PlayerID, &profile.TotalDiscovered, &profile.TotalCompleted,
		&profile.FavoriteCategory, &profile.AverageDifficulty, &profile.TotalHintsUsed,
		&profile.CollectionProgress, &profile.AchievementLevel, &profile.LastActivity,
		&profile.CreatedAt, &profile.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		// Create default profile
		profile = models.PlayerEasterEggProfile{
			PlayerID:           playerID,
			TotalDiscovered:    0,
			TotalCompleted:     0,
			FavoriteCategory:   "",
			AverageDifficulty:  0,
			TotalHintsUsed:     0,
			CollectionProgress: 0,
			AchievementLevel:   "explorer",
			LastActivity:       time.Now(),
			CreatedAt:          time.Now(),
			UpdatedAt:          time.Now(),
		}

		// Insert default profile
		insertQuery := `
			INSERT INTO player_easter_egg_profiles (
				player_id, total_discovered, total_completed, favorite_category,
				average_difficulty, total_hints_used, collection_progress,
				achievement_level, last_activity, created_at, updated_at
			) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
		`
		_, err = r.db.ExecContext(ctx, insertQuery,
			profile.PlayerID, profile.TotalDiscovered, profile.TotalCompleted,
			profile.FavoriteCategory, profile.AverageDifficulty, profile.TotalHintsUsed,
			profile.CollectionProgress, profile.AchievementLevel, profile.LastActivity,
			profile.CreatedAt, profile.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to create player profile: %w", err)
		}

		return &profile, nil
	}

	return &profile, err
}

// GetPlayerDiscoveredEggs returns list of easter egg IDs discovered by player
func (r *Repository) GetPlayerDiscoveredEggs(ctx context.Context, playerID string) ([]string, error) {
	query := `SELECT easter_egg_id FROM player_easter_egg_progress WHERE player_id = $1 AND status IN ('discovered', 'completed')`

	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get discovered eggs: %w", err)
	}
	defer rows.Close()

	var eggIDs []string
	for rows.Next() {
		var eggID string
		if err := rows.Scan(&eggID); err != nil {
			continue
		}
		eggIDs = append(eggIDs, eggID)
	}

	return eggIDs, nil
}

// CreateDiscoveryAttempt records a discovery attempt
func (r *Repository) CreateDiscoveryAttempt(ctx context.Context, attempt *models.EasterEggDiscoveryAttempt) error {
	query := `
		INSERT INTO easter_egg_discovery_attempts (
			id, player_id, easter_egg_id, attempt_type, attempt_data,
			success, attempted_at, response_time, ip_address, user_agent
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	attempt.ID = uuid.New().String()
	attempt.AttemptedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		attempt.ID, attempt.PlayerID, attempt.EasterEggID, attempt.AttemptType,
		attempt.AttemptData, attempt.Success, attempt.AttemptedAt,
		attempt.ResponseTime, attempt.IPAddress, attempt.UserAgent,
	)

	return err
}

// GetDiscoveryAttempts retrieves discovery attempts for a player and easter egg
func (r *Repository) GetDiscoveryAttempts(ctx context.Context, playerID, easterEggID string, limit int) ([]*models.EasterEggDiscoveryAttempt, error) {
	query := `
		SELECT id, player_id, easter_egg_id, attempt_type, attempt_data,
			   success, attempted_at, response_time, ip_address, user_agent
		FROM easter_egg_discovery_attempts
		WHERE player_id = $1 AND easter_egg_id = $2
		ORDER BY attempted_at DESC
		LIMIT $3
	`

	rows, err := r.db.QueryContext(ctx, query, playerID, easterEggID, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get discovery attempts: %w", err)
	}
	defer rows.Close()

	var attempts []*models.EasterEggDiscoveryAttempt
	for rows.Next() {
		var attempt models.EasterEggDiscoveryAttempt
		err := rows.Scan(
			&attempt.ID, &attempt.PlayerID, &attempt.EasterEggID, &attempt.AttemptType,
			&attempt.AttemptData, &attempt.Success, &attempt.AttemptedAt,
			&attempt.ResponseTime, &attempt.IPAddress, &attempt.UserAgent,
		)
		if err != nil {
			continue
		}
		attempts = append(attempts, &attempt)
	}

	return attempts, nil
}

// RecordSuccessfulDiscovery updates player progress when easter egg is discovered
func (r *Repository) RecordSuccessfulDiscovery(ctx context.Context, playerID, easterEggID string) error {
	now := time.Now()

	// Update progress
	progressQuery := `
		INSERT INTO player_easter_egg_progress (
			player_id, easter_egg_id, status, discovered_at, visit_count, last_visited
		) VALUES ($1, $2, 'discovered', $3, 1, $3)
		ON CONFLICT (player_id, easter_egg_id)
		DO UPDATE SET
			status = 'discovered',
			discovered_at = COALESCE(player_easter_egg_progress.discovered_at, EXCLUDED.discovered_at),
			visit_count = player_easter_egg_progress.visit_count + 1,
			last_visited = EXCLUDED.last_visited
	`

	if _, err := r.db.ExecContext(ctx, progressQuery, playerID, easterEggID, now); err != nil {
		return fmt.Errorf("failed to update progress: %w", err)
	}

	// Update statistics
	if err := r.UpdateEasterEggStats(ctx, easterEggID); err != nil {
		// Log but don't fail the operation
		fmt.Printf("Failed to update stats for easter egg %s: %v\n", easterEggID, err)
	}

	// Create event
	event := &models.EasterEggEvent{
		ID:          uuid.New().String(),
		EventType:   "discovered",
		PlayerID:    playerID,
		EasterEggID: easterEggID,
		EventData:   map[string]interface{}{"timestamp": now},
		CreatedAt:   now,
		Processed:   false,
	}

	return r.CreateEasterEggEvent(ctx, event)
}

// GetEasterEggStatistics retrieves statistics for an easter egg
func (r *Repository) GetEasterEggStatistics(ctx context.Context, easterEggID string) (*models.EasterEggStatistics, error) {
	query := `
		SELECT easter_egg_id, total_discoveries, unique_players, average_discovery_time,
			   success_rate, popular_discovery_method, last_updated
		FROM easter_egg_statistics WHERE easter_egg_id = $1
	`

	var stats models.EasterEggStatistics
	err := r.db.QueryRowContext(ctx, query, easterEggID).Scan(
		&stats.EasterEggID, &stats.TotalDiscoveries, &stats.UniquePlayers,
		&stats.AverageDiscoveryTime, &stats.SuccessRate, &stats.PopularDiscoveryMethod,
		&stats.LastUpdated,
	)

	if err == sql.ErrNoRows {
		// Return default stats
		return &models.EasterEggStatistics{
			EasterEggID:             easterEggID,
			TotalDiscoveries:        0,
			UniquePlayers:           0,
			AverageDiscoveryTime:    0,
			SuccessRate:             0,
			PopularDiscoveryMethod:  "",
			LastUpdated:             time.Now(),
		}, nil
	}

	return &stats, err
}

// GetCategoryStatistics provides statistics grouped by category
func (r *Repository) GetCategoryStatistics(ctx context.Context) ([]*models.EasterEggCategoryStats, error) {
	query := `
		SELECT
			category,
			COUNT(*) as total_easter_eggs,
			SUM(total_discoveries) as discovered_count,
			AVG(success_rate) as discovery_rate,
			AVG(CASE
				WHEN difficulty = 'easy' THEN 1
				WHEN difficulty = 'medium' THEN 2
				WHEN difficulty = 'hard' THEN 3
				WHEN difficulty = 'legendary' THEN 4
				ELSE 2
			END) as average_difficulty
		FROM easter_eggs e
		LEFT JOIN easter_egg_statistics s ON e.id = s.easter_egg_id
		WHERE e.status = 'active'
		GROUP BY category
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to get category statistics: %w", err)
	}
	defer rows.Close()

	var stats []*models.EasterEggCategoryStats
	for rows.Next() {
		var stat models.EasterEggCategoryStats
		err := rows.Scan(
			&stat.Category, &stat.TotalEasterEggs, &stat.DiscoveredCount,
			&stat.DiscoveryRate, &stat.AverageDifficulty,
		)
		if err != nil {
			continue
		}
		stats = append(stats, &stat)
	}

	return stats, nil
}

// UpdateEasterEggStats recalculates statistics for an easter egg
func (r *Repository) UpdateEasterEggStats(ctx context.Context, easterEggID string) error {
	query := `
		INSERT INTO easter_egg_statistics (
			easter_egg_id, total_discoveries, unique_players, average_discovery_time,
			success_rate, popular_discovery_method, last_updated
		)
		SELECT
			$1,
			COUNT(CASE WHEN p.status IN ('discovered', 'completed') THEN 1 END),
			COUNT(DISTINCT p.player_id),
			AVG(CASE WHEN a.success THEN a.response_time END),
			COUNT(CASE WHEN a.success THEN 1 END)::float / GREATEST(COUNT(*), 1),
			(
				SELECT attempt_type
				FROM easter_egg_discovery_attempts
				WHERE easter_egg_id = $1 AND success = true
				GROUP BY attempt_type
				ORDER BY COUNT(*) DESC
				LIMIT 1
			),
			NOW()
		FROM player_easter_egg_progress p
		LEFT JOIN easter_egg_discovery_attempts a ON p.easter_egg_id = a.easter_egg_id
		WHERE p.easter_egg_id = $1
		ON CONFLICT (easter_egg_id)
		DO UPDATE SET
			total_discoveries = EXCLUDED.total_discoveries,
			unique_players = EXCLUDED.unique_players,
			average_discovery_time = EXCLUDED.average_discovery_time,
			success_rate = EXCLUDED.success_rate,
			popular_discovery_method = EXCLUDED.popular_discovery_method,
			last_updated = EXCLUDED.last_updated
	`

	_, err := r.db.ExecContext(ctx, query, easterEggID)
	return err
}

// GetHintsForEasterEgg retrieves available hints for an easter egg
func (r *Repository) GetHintsForEasterEgg(ctx context.Context, easterEggID string, maxLevel int) ([]*models.DiscoveryHint, error) {
	query := `
		SELECT id, easter_egg_id, hint_level, hint_text, hint_type, cost, is_enabled, created_at
		FROM easter_egg_hints
		WHERE easter_egg_id = $1 AND hint_level <= $2 AND is_enabled = true
		ORDER BY hint_level ASC
	`

	rows, err := r.db.QueryContext(ctx, query, easterEggID, maxLevel)
	if err != nil {
		return nil, fmt.Errorf("failed to get hints: %w", err)
	}
	defer rows.Close()

	var hints []*models.DiscoveryHint
	for rows.Next() {
		var hint models.DiscoveryHint
		err := rows.Scan(
			&hint.ID, &hint.EasterEggID, &hint.HintLevel, &hint.HintText,
			&hint.HintType, &hint.Cost, &hint.IsEnabled, &hint.CreatedAt,
		)
		if err != nil {
			continue
		}
		hints = append(hints, &hint)
	}

	return hints, nil
}

// RecordHintUsage records when a player uses a hint
func (r *Repository) RecordHintUsage(ctx context.Context, playerID, easterEggID string, hintLevel int) error {
	query := `
		UPDATE player_easter_egg_progress
		SET hint_level = GREATEST(hint_level, $3), total_hints_used = total_hints_used + 1
		WHERE player_id = $1 AND easter_egg_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, playerID, easterEggID, hintLevel)
	return err
}

// CreateEasterEggEvent creates an event for easter egg interactions
func (r *Repository) CreateEasterEggEvent(ctx context.Context, event *models.EasterEggEvent) error {
	eventData, _ := json.Marshal(event.EventData)

	query := `
		INSERT INTO easter_egg_events (id, event_type, player_id, easter_egg_id, event_data, created_at, processed)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.EventType, event.PlayerID, event.EasterEggID,
		eventData, event.CreatedAt, event.Processed,
	)

	return err
}

// GetEasterEggEvents retrieves events for an easter egg
func (r *Repository) GetEasterEggEvents(ctx context.Context, easterEggID string, limit int) ([]*models.EasterEggEvent, error) {
	query := `
		SELECT id, event_type, player_id, easter_egg_id, event_data, created_at, processed
		FROM easter_egg_events
		WHERE easter_egg_id = $1
		ORDER BY created_at DESC
		LIMIT $2
	`

	rows, err := r.db.QueryContext(ctx, query, easterEggID, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get events: %w", err)
	}
	defer rows.Close()

	var events []*models.EasterEggEvent
	for rows.Next() {
		var event models.EasterEggEvent
		var eventData []byte

		err := rows.Scan(
			&event.ID, &event.EventType, &event.PlayerID, &event.EasterEggID,
			&eventData, &event.CreatedAt, &event.Processed,
		)
		if err != nil {
			continue
		}

		if err := json.Unmarshal(eventData, &event.EventData); err != nil {
			event.EventData = make(map[string]interface{})
		}

		events = append(events, &event)
	}

	return events, nil
}

// GetActiveChallenges retrieves currently active challenges
func (r *Repository) GetActiveChallenges(ctx context.Context) ([]*models.EasterEggChallenge, error) {
	query := `
		SELECT id, title, description, easter_eggs, rewards, start_time, end_time, is_active, created_at
		FROM easter_egg_challenges
		WHERE is_active = true AND end_time > NOW()
		ORDER BY start_time DESC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to get active challenges: %w", err)
	}
	defer rows.Close()

	var challenges []*models.EasterEggChallenge
	for rows.Next() {
		var challenge models.EasterEggChallenge
		var easterEggsData, rewardsData []byte

		err := rows.Scan(
			&challenge.ID, &challenge.Title, &challenge.Description,
			&easterEggsData, &rewardsData, &challenge.StartTime,
			&challenge.EndTime, &challenge.IsActive, &challenge.CreatedAt,
		)
		if err != nil {
			continue
		}

		if err := json.Unmarshal(easterEggsData, &challenge.EasterEggs); err != nil {
			challenge.EasterEggs = []string{}
		}
		if err := json.Unmarshal(rewardsData, &challenge.Rewards); err != nil {
			challenge.Rewards = []models.EasterEggReward{}
		}

		challenges = append(challenges, &challenge)
	}

	return challenges, nil
}

// GetPlayerChallengeProgress gets progress for a specific challenge
func (r *Repository) GetPlayerChallengeProgress(ctx context.Context, playerID, challengeID string) (int, error) {
	query := `
		SELECT COUNT(*)
		FROM easter_egg_challenges c
		JOIN unnest(c.easter_eggs) as egg_id ON true
		JOIN player_easter_egg_progress p ON p.easter_egg_id = egg_id::text
		WHERE c.id = $1 AND p.player_id = $2 AND p.status IN ('discovered', 'completed')
	`

	var progress int
	err := r.db.QueryRowContext(ctx, query, challengeID, playerID).Scan(&progress)
	return progress, err
}

// HealthCheck performs a simple database health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	return r.db.PingContext(ctx)
}
