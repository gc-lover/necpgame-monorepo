// Package server Issue: #???
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/league-system-service-go/models"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// LeagueRepository handles database operations for league system
type LeagueRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewLeagueRepository creates a new league repository
func NewLeagueRepository(db *sql.DB, logger *zap.Logger) *LeagueRepository {
	return &LeagueRepository{
		db:     db,
		logger: logger,
	}
}

// GetCurrentLeague retrieves the currently active league
func (r *LeagueRepository) GetCurrentLeague(ctx context.Context) (*models.League, error) {
	query := `
		SELECT id, name, start_date, end_date, status, phase, seed, time_acceleration, player_count, created_at, updated_at
		FROM league.leagues
		WHERE status = 'ACTIVE'
		ORDER BY created_at DESC
		LIMIT 1`

	var league models.League
	var phaseBytes []byte

	err := r.db.QueryRowContext(ctx, query).Scan(
		&league.ID, &league.Name, &league.StartDate, &league.EndDate,
		&league.Status, &phaseBytes, &league.Seed, &league.TimeAcceleration,
		&league.PlayerCount, &league.CreatedAt, &league.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	// Unmarshal phase JSON
	if err := json.Unmarshal(phaseBytes, &league.Phase); err != nil {
		r.logger.Warn("Failed to unmarshal league phase", zap.Error(err), zap.String("league_id", league.ID.String()))
	}

	return &league, nil
}

// GetLeagueStatistics retrieves statistics for a completed league
func (r *LeagueRepository) GetLeagueStatistics(ctx context.Context, leagueID uuid.UUID) (*models.LeagueStatistics, error) {
	query := `
		SELECT league_id, player_count, completion_rate, average_level,
		       total_quests_completed, total_economy_value, top_players,
		       faction_distribution, ending_distribution
		FROM league.league_statistics
		WHERE league_id = $1`

	var stats models.LeagueStatistics
	var topPlayersBytes, factionDistBytes, endingDistBytes []byte

	err := r.db.QueryRowContext(ctx, query, leagueID).Scan(
		&stats.LeagueID, &stats.PlayerCount, &stats.CompletionRate, &stats.AverageLevel,
		&stats.TotalQuestsCompleted, &stats.TotalEconomyValue,
		&topPlayersBytes, &factionDistBytes, &endingDistBytes,
	)

	if err != nil {
		return nil, err
	}

	// Unmarshal JSON fields
	if len(topPlayersBytes) > 0 {
		json.Unmarshal(topPlayersBytes, &stats.TopPlayers)
	}
	if len(factionDistBytes) > 0 {
		json.Unmarshal(factionDistBytes, &stats.FactionDistribution)
	}
	if len(endingDistBytes) > 0 {
		json.Unmarshal(endingDistBytes, &stats.EndingDistribution)
	}

	return &stats, nil
}

// RegisterPlayerForEndEvent registers a player for the league end event
func (r *LeagueRepository) RegisterPlayerForEndEvent(ctx context.Context, playerID, leagueID uuid.UUID) error {
	query := `
		INSERT INTO league.end_event_registrations (player_id, league_id, registered_at)
		VALUES ($1, $2, $3)
		ON CONFLICT (player_id, league_id) DO NOTHING`

	_, err := r.db.ExecContext(ctx, query, playerID, leagueID, time.Now())
	return err
}

// UpdateLeagueStatus updates the status of a league
func (r *LeagueRepository) UpdateLeagueStatus(ctx context.Context, leagueID uuid.UUID, status models.LeagueStatus) error {
	query := `UPDATE league.leagues SET status = $1, updated_at = $2 WHERE id = $3`

	_, err := r.db.ExecContext(ctx, query, status, time.Now(), leagueID)
	return err
}

// GetPlayerLegacyProgress retrieves a player's meta-progression
func (r *LeagueRepository) GetPlayerLegacyProgress(ctx context.Context, playerID uuid.UUID) (*models.PlayerLegacyProgress, error) {
	query := `
		SELECT player_id, legacy_points, titles, cosmetics, legacy_items,
		       global_rating, rating_history, achievements
		FROM league.player_legacy_progress
		WHERE player_id = $1`

	var progress models.PlayerLegacyProgress
	var titlesBytes, cosmeticsBytes, legacyItemsBytes, ratingHistoryBytes, achievementsBytes []byte

	err := r.db.QueryRowContext(ctx, query, playerID).Scan(
		&progress.PlayerID, &progress.LegacyPoints,
		&titlesBytes, &cosmeticsBytes, &legacyItemsBytes,
		&progress.GlobalRating, &ratingHistoryBytes, &achievementsBytes,
	)

	if err != nil {
		return nil, err
	}

	// Unmarshal JSON fields
	if len(titlesBytes) > 0 {
		json.Unmarshal(titlesBytes, &progress.Titles)
	}
	if len(cosmeticsBytes) > 0 {
		json.Unmarshal(cosmeticsBytes, &progress.Cosmetics)
	}
	if len(legacyItemsBytes) > 0 {
		json.Unmarshal(legacyItemsBytes, &progress.LegacyItems)
	}
	if len(ratingHistoryBytes) > 0 {
		json.Unmarshal(ratingHistoryBytes, &progress.RatingHistory)
	}
	if len(achievementsBytes) > 0 {
		json.Unmarshal(achievementsBytes, &progress.Achievements)
	}

	return &progress, nil
}

// GetHallOfFame retrieves Hall of Fame entries
func (r *LeagueRepository) GetHallOfFame(ctx context.Context, leagueID *uuid.UUID, category models.AchievementCategory, limit int) (*models.HallOfFame, error) {
	baseQuery := `
		SELECT h.league_id, l.name as league_name, h.player_id, h.player_name,
		       h.category, h.achievement, h.date, h.rank, h.reward_cosmetic
		FROM league.hall_of_fame h
		JOIN league.leagues l ON h.league_id = l.id
		WHERE 1=1`

	var args []interface{}

	if leagueID != nil {
		baseQuery += fmt.Sprintf(" AND h.league_id = $%d", len(args)+1)
		args = append(args, *leagueID)
	}

	if category != "ALL" {
		baseQuery += fmt.Sprintf(" AND h.category = $%d", len(args)+1)
		args = append(args, category)
	}

	query := baseQuery + " ORDER BY h.rank ASC" + fmt.Sprintf(" LIMIT $%d", len(args)+1)
	args = append(args, limit)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query hall of fame: %w", err)
	}
	defer rows.Close()

	var entries []models.HallOfFameEntry
	var leagueName string
	var lid uuid.UUID

	for rows.Next() {
		var entry models.HallOfFameEntry
		err := rows.Scan(
			&lid, &leagueName, &entry.PlayerID, &entry.PlayerName,
			&entry.Category, &entry.Achievement, &entry.Date, &entry.Rank, &entry.RewardCosmetic,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan hall of fame row: %w", err)
		}
		entries = append(entries, entry)
	}

	if err = rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating hall of fame rows: %w", err)
	}

	return &models.HallOfFame{
		LeagueID:   lid,
		LeagueName: leagueName,
		Entries:    entries,
	}, nil
}

// GetLegacyShopItems retrieves all available items in Legacy Shop
func (r *LeagueRepository) GetLegacyShopItems(ctx context.Context) ([]models.LegacyShopItem, error) {
	query := `
		SELECT id, name, description, type, cost, available, limited_time, expires_at
		FROM league.legacy_shop_items
		WHERE available = true
		ORDER BY cost ASC`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to query legacy shop items: %w", err)
	}
	defer rows.Close()

	var items []models.LegacyShopItem
	for rows.Next() {
		var item models.LegacyShopItem
		err := rows.Scan(
			&item.ID, &item.Name, &item.Description, &item.Type,
			&item.Cost, &item.Available, &item.LimitedTime, &item.ExpiresAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan legacy shop item row: %w", err)
		}
		items = append(items, item)
	}

	return items, rows.Err()
}

// GetLegacyShopItem retrieves a specific legacy shop item
func (r *LeagueRepository) GetLegacyShopItem(ctx context.Context, itemID uuid.UUID) (*models.LegacyShopItem, error) {
	query := `
		SELECT id, name, description, type, cost, available, limited_time, expires_at
		FROM league.legacy_shop_items
		WHERE id = $1`

	var item models.LegacyShopItem
	err := r.db.QueryRowContext(ctx, query, itemID).Scan(
		&item.ID, &item.Name, &item.Description, &item.Type,
		&item.Cost, &item.Available, &item.LimitedTime, &item.ExpiresAt,
	)

	return &item, err
}

// PurchaseLegacyItem processes a purchase from Legacy Shop
func (r *LeagueRepository) PurchaseLegacyItem(ctx context.Context, playerID, itemID uuid.UUID, cost int) error {
	// Begin transaction
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Check and deduct legacy points
	pointsQuery := `
		UPDATE league.player_legacy_progress
		SET legacy_points = legacy_points - $1
		WHERE player_id = $2 AND legacy_points >= $1`

	result, err := tx.ExecContext(ctx, pointsQuery, cost, playerID)
	if err != nil {
		return fmt.Errorf("failed to deduct legacy points: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("insufficient legacy points")
	}

	// Record purchase
	purchaseQuery := `
		INSERT INTO league.legacy_shop_purchases (player_id, item_id, cost, purchased_at)
		VALUES ($1, $2, $3, $4)`

	_, err = tx.ExecContext(ctx, purchaseQuery, playerID, itemID, cost, time.Now())
	if err != nil {
		return fmt.Errorf("failed to record purchase: %w", err)
	}

	// Grant item to player (this would depend on item type)
	// For now, just log the purchase
	r.logger.Info("Legacy item purchased",
		zap.String("player_id", playerID.String()),
		zap.String("item_id", itemID.String()),
		zap.Int("cost", cost),
	)

	return tx.Commit()
}

// CreateLeague creates a new league
func (r *LeagueRepository) CreateLeague(ctx context.Context, league *models.League) error {
	phaseJSON, err := json.Marshal(league.Phase)
	if err != nil {
		return fmt.Errorf("failed to marshal league phase: %w", err)
	}

	query := `
		INSERT INTO league.leagues (
			id, name, start_date, end_date, status, phase, seed, time_acceleration,
			player_count, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)`

	_, err = r.db.ExecContext(ctx, query,
		league.ID, league.Name, league.StartDate, league.EndDate, league.Status,
		phaseJSON, league.Seed, league.TimeAcceleration, league.PlayerCount,
		league.CreatedAt, league.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create league: %w", err)
	}

	return nil
}

// UpdateLeague updates an existing league
func (r *LeagueRepository) UpdateLeague(ctx context.Context, league *models.League) error {
	phaseJSON, err := json.Marshal(league.Phase)
	if err != nil {
		return fmt.Errorf("failed to marshal league phase: %w", err)
	}

	query := `
		UPDATE league.leagues SET
			name = $2, start_date = $3, end_date = $4, status = $5, phase = $6,
			seed = $7, time_acceleration = $8, player_count = $9, updated_at = $10
		WHERE id = $1`

	_, err = r.db.ExecContext(ctx, query,
		league.ID, league.Name, league.StartDate, league.EndDate, league.Status,
		phaseJSON, league.Seed, league.TimeAcceleration, league.PlayerCount, time.Now(),
	)

	if err != nil {
		return fmt.Errorf("failed to update league: %w", err)
	}

	return nil
}

// GetLeaguesByStatus retrieves leagues by status
func (r *LeagueRepository) GetLeaguesByStatus(ctx context.Context, status models.LeagueStatus, limit int) ([]*models.League, error) {
	query := `
		SELECT id, name, start_date, end_date, status, phase, seed, time_acceleration,
		       player_count, created_at, updated_at
		FROM league.leagues
		WHERE status = $1
		ORDER BY created_at DESC
		LIMIT $2`

	rows, err := r.db.QueryContext(ctx, query, status, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to query leagues by status: %w", err)
	}
	defer rows.Close()

	var leagues []*models.League
	for rows.Next() {
		var league models.League
		var phaseBytes []byte

		err := rows.Scan(
			&league.ID, &league.Name, &league.StartDate, &league.EndDate,
			&league.Status, &phaseBytes, &league.Seed, &league.TimeAcceleration,
			&league.PlayerCount, &league.CreatedAt, &league.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan league row: %w", err)
		}

		// Unmarshal phase
		if len(phaseBytes) > 0 {
			json.Unmarshal(phaseBytes, &league.Phase)
		}

		leagues = append(leagues, &league)
	}

	return leagues, rows.Err()
}

// UpdatePlayerLegacyPoints updates a player's legacy points
func (r *LeagueRepository) UpdatePlayerLegacyPoints(ctx context.Context, playerID uuid.UUID, points int) error {
	query := `
		INSERT INTO league.player_legacy_progress (player_id, legacy_points, created_at, updated_at)
		VALUES ($1, $2, $3, $4)
		ON CONFLICT (player_id) DO UPDATE SET
			legacy_points = league.player_legacy_progress.legacy_points + EXCLUDED.legacy_points,
			updated_at = EXCLUDED.updated_at`

	_, err := r.db.ExecContext(ctx, query, playerID, points, time.Now(), time.Now())
	return err
}

// ArchiveOldLeagues moves completed leagues to archived status
func (r *LeagueRepository) ArchiveOldLeagues(ctx context.Context, daysOld int) error {
	cutoffDate := time.Now().AddDate(0, 0, -daysOld)

	query := `
		UPDATE league.leagues
		SET status = 'ARCHIVED', updated_at = $1
		WHERE status = 'COMPLETED' AND end_date < $2`

	result, err := r.db.ExecContext(ctx, query, time.Now(), cutoffDate)
	if err != nil {
		return fmt.Errorf("failed to archive old leagues: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	r.logger.Info("Archived old leagues", zap.Int64("count", rowsAffected), zap.Int("days_old", daysOld))

	return nil
}

// AddHallOfFameEntry adds an entry to the Hall of Fame
func (r *LeagueRepository) AddHallOfFameEntry(ctx context.Context, leagueID uuid.UUID, entry *models.HallOfFameEntry) error {
	query := `
		INSERT INTO league.hall_of_fame (league_id, player_id, player_name, category, achievement, date, rank, reward_cosmetic)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		leagueID,
		entry.PlayerID,
		entry.PlayerName,
		entry.Category,
		entry.Achievement,
		entry.Date,
		entry.Rank,
		entry.RewardCosmetic,
	)

	if err != nil {
		r.logger.Error("Failed to add Hall of Fame entry", zap.Error(err))
		return fmt.Errorf("failed to add Hall of Fame entry: %w", err)
	}

	r.logger.Info("Added Hall of Fame entry",
		zap.String("league_id", leagueID.String()),
		zap.String("player_id", entry.PlayerID.String()),
		zap.Int("rank", entry.Rank))
	return nil
}

// UpdatePlayerLegacyProgress updates a player's legacy points
func (r *LeagueRepository) UpdatePlayerLegacyProgress(ctx context.Context, playerID uuid.UUID, points int) error {
	query := `
		UPDATE league.player_legacy_progress
		SET legacy_points = legacy_points + $1, last_updated = $2
		WHERE player_id = $3
	`

	result, err := r.db.ExecContext(ctx, query, points, time.Now(), playerID)
	if err != nil {
		r.logger.Error("Failed to update player legacy progress", zap.Error(err))
		return fmt.Errorf("failed to update player legacy progress: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		// No existing record, insert new one
		return r.insertPlayerLegacyProgress(ctx, playerID, points)
	}

	r.logger.Info("Updated player legacy progress",
		zap.String("player_id", playerID.String()),
		zap.Int("points_added", points))
	return nil
}

// insertPlayerLegacyProgress inserts a new legacy progress record
func (r *LeagueRepository) insertPlayerLegacyProgress(ctx context.Context, playerID uuid.UUID, points int) error {
	query := `
		INSERT INTO league.player_legacy_progress (player_id, legacy_points, last_updated)
		VALUES ($1, $2, $3)
	`

	_, err := r.db.ExecContext(ctx, query, playerID, points, time.Now())
	if err != nil {
		r.logger.Error("Failed to insert player legacy progress", zap.Error(err))
		return fmt.Errorf("failed to insert player legacy progress: %w", err)
	}

	r.logger.Info("Created new player legacy progress",
		zap.String("player_id", playerID.String()),
		zap.Int("initial_points", points))
	return nil
}

// AddPlayerTitle adds a title to a player's legacy progress
func (r *LeagueRepository) AddPlayerTitle(ctx context.Context, playerID uuid.UUID, title *models.Title) error {
	query := `
		INSERT INTO league.player_titles (player_id, title_id, name, description, rarity, unlocked_at, is_active)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (player_id, title_id) DO NOTHING
	`

	_, err := r.db.ExecContext(ctx, query,
		playerID,
		title.ID,
		title.Name,
		title.Description,
		title.Rarity,
		title.UnlockedAt,
		title.IsActive,
	)

	if err != nil {
		r.logger.Error("Failed to add player title", zap.Error(err))
		return fmt.Errorf("failed to add player title: %w", err)
	}

	r.logger.Info("Added player title",
		zap.String("player_id", playerID.String()),
		zap.String("title_name", title.Name))
	return nil
}
