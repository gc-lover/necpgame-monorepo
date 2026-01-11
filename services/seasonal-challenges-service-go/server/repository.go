// Repository layer with database optimizations for MMOFPS
// Issue: #1506
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// Repository defines data access interface
type Repository interface {
	// Season operations
	CreateSeason(ctx context.Context, season *Season) error
	GetSeason(ctx context.Context, seasonID string) (*Season, error)
	UpdateSeason(ctx context.Context, season *Season) error
	ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error)

	// Challenge operations
	GetChallenge(ctx context.Context, challengeID string) (*Challenge, error)
	GetChallengeObjectives(ctx context.Context, challengeID string) ([]*ChallengeObjective, error)
	GetChallengeProgress(ctx context.Context, playerID, challengeID string) (*ChallengeProgress, error)
	CreateChallengeProgress(ctx context.Context, progress *ChallengeProgress) error
	UpdateChallengeProgress(ctx context.Context, progress *ChallengeProgress) error

	// Leaderboard operations
	GetLeaderboard(ctx context.Context, seasonID string, limit int) (*SeasonLeaderboard, error)

	// Reward operations
	GetAvailableRewards(ctx context.Context, playerID, seasonID string) ([]Reward, error)
	MarkRewardsClaimed(ctx context.Context, playerID, seasonID string) error

	// Currency operations
	GetSeasonalCurrency(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error)
	CreateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error
	UpdateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error
	ExecuteCurrencyTransaction(ctx context.Context, tx *CurrencyTransaction, currency *SeasonalCurrency) error
	GetCurrencyTransactions(ctx context.Context, playerID, seasonID string, limit int) ([]*CurrencyTransaction, error)
	CreateCurrencyExchange(ctx context.Context, exchange *CurrencyExchange) error
	GetPlayerExchangeSpent(ctx context.Context, playerID, seasonID, exchangeType string) (int, error)
	GetDailyExchangeSpent(ctx context.Context, seasonID, exchangeType string) (int, error)
}

// PostgresRepository implements Repository with PostgreSQL
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewPostgresRepository creates a new PostgreSQL repository
func NewPostgresRepository(db *pgxpool.Pool, logger *zap.Logger) *PostgresRepository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

// Season operations implementation

func (r *PostgresRepository) CreateSeason(ctx context.Context, season *Season) error {
	query := `
		INSERT INTO seasons (id, name, description, start_date, end_date, status, currency_limit, created_at, updated_at, version)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	_, err := r.db.Exec(ctx, query,
		season.ID, season.Name, season.Description,
		season.StartDate, season.EndDate, season.Status,
		season.CurrencyLimit, season.CreatedAt, season.UpdatedAt, season.Version,
	)

	if err != nil {
		r.logger.Error("Failed to create season", zap.Error(err))
		return err
	}

	return nil
}

func (r *PostgresRepository) GetSeason(ctx context.Context, seasonID string) (*Season, error) {
	query := `
		SELECT id, name, description, start_date, end_date, status, currency_limit, created_at, updated_at, version
		FROM seasons WHERE id = $1
	`

	season := &Season{}
	err := r.db.QueryRow(ctx, query, seasonID).Scan(
		&season.ID, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.Status,
		&season.CurrencyLimit, &season.CreatedAt, &season.UpdatedAt, &season.Version,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrNotFound
		}
		r.logger.Error("Failed to get season", zap.String("season_id", seasonID), zap.Error(err))
		return nil, err
	}

	return season, nil
}

func (r *PostgresRepository) UpdateSeason(ctx context.Context, season *Season) error {
	query := `
		UPDATE seasons
		SET name = $1, description = $2, start_date = $3, end_date = $4,
		    status = $5, currency_limit = $6, updated_at = $7, version = $8
		WHERE id = $9 AND version = $10
	`

	result, err := r.db.Exec(ctx, query,
		season.Name, season.Description, season.StartDate, season.EndDate,
		season.Status, season.CurrencyLimit, season.UpdatedAt, season.Version,
		season.ID, season.Version-1, // Check against previous version
	)

	if err != nil {
		r.logger.Error("Failed to update season", zap.Error(err))
		return err
	}

	rowsAffected := result.RowsAffected()
	if rowsAffected == 0 {
		return ErrVersionConflict
	}

	return nil
}

func (r *PostgresRepository) ListSeasons(ctx context.Context, filter SeasonFilter) ([]*Season, error) {
	query := `
		SELECT id, name, description, start_date, end_date, status, currency_limit, created_at, updated_at, version
		FROM seasons
		WHERE ($1 = '' OR status = $1)
		ORDER BY start_date DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, filter.Status, filter.Limit)
	if err != nil {
		r.logger.Error("Failed to list seasons", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var seasons []*Season
	for rows.Next() {
		season := &Season{}
		err := rows.Scan(
			&season.ID, &season.Name, &season.Description,
			&season.StartDate, &season.EndDate, &season.Status,
			&season.CurrencyLimit, &season.CreatedAt, &season.UpdatedAt, &season.Version,
		)
		if err != nil {
			r.logger.Error("Failed to scan season", zap.Error(err))
			return nil, err
		}
		seasons = append(seasons, season)
	}

	return seasons, nil
}

// Challenge operations implementation

func (r *PostgresRepository) GetChallengeObjectives(ctx context.Context, challengeID string) ([]*ChallengeObjective, error) {
	query := `
		SELECT id, challenge_id, objective_type, target_value, description, progress_type,
		       is_optional, reward_weight, metadata, created_at
		FROM challenge_objectives
		WHERE challenge_id = $1
		ORDER BY id
	`

	rows, err := r.db.Query(ctx, query, challengeID)
	if err != nil {
		r.logger.Error("Failed to get challenge objectives", zap.String("challenge_id", challengeID), zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var objectives []*ChallengeObjective
	for rows.Next() {
		obj := &ChallengeObjective{}
		var metadataJSON []byte

		err := rows.Scan(
			&obj.ID, &obj.ChallengeID, &obj.ObjectiveType, &obj.TargetValue,
			&obj.Description, &obj.ProgressType, &obj.IsOptional, &obj.RewardWeight,
			&metadataJSON, &obj.CreatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan challenge objective", zap.Error(err))
			return nil, err
		}

		if len(metadataJSON) > 0 {
			json.Unmarshal(metadataJSON, &obj.Metadata)
		}

		objectives = append(objectives, obj)
	}

	return objectives, nil
}

func (r *PostgresRepository) GetChallenge(ctx context.Context, challengeID string) (*Challenge, error) {
	query := `
		SELECT id, season_id, name, challenge_type, difficulty, max_score, time_limit_seconds,
		       min_participants, max_participants, is_team_based, entry_fee, reward_multiplier,
		       created_at, updated_at, version
		FROM challenges WHERE id = $1
	`

	challenge := &Challenge{}
	err := r.db.QueryRow(ctx, query, challengeID).Scan(
		&challenge.ID, &challenge.SeasonID, &challenge.Name, &challenge.ChallengeType,
		&challenge.Difficulty, &challenge.MaxScore, &challenge.TimeLimitSeconds,
		&challenge.MinParticipants, &challenge.MaxParticipants, &challenge.IsTeamBased,
		&challenge.EntryFee, &challenge.RewardMultiplier, &challenge.CreatedAt,
		&challenge.UpdatedAt, &challenge.Version,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrNotFound
		}
		r.logger.Error("Failed to get challenge", zap.String("challenge_id", challengeID), zap.Error(err))
		return nil, err
	}

	return challenge, nil
}

// Challenge progress operations

func (r *PostgresRepository) GetChallengeProgress(ctx context.Context, playerID, challengeID string) (*ChallengeProgress, error) {
	query := `
		SELECT player_id, challenge_id, current_value, is_completed, completed_at, created_at, updated_at
		FROM challenge_progress
		WHERE player_id = $1 AND challenge_id = $2
	`

	progress := &ChallengeProgress{}
	var completedAt sql.NullTime

	err := r.db.QueryRow(ctx, query, playerID, challengeID).Scan(
		&progress.PlayerID, &progress.ChallengeID, &progress.CurrentValue,
		&progress.IsCompleted, &completedAt, &progress.CreatedAt, &progress.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrNotFound
		}
		r.logger.Error("Failed to get challenge progress", zap.Error(err))
		return nil, err
	}

	if completedAt.Valid {
		progress.CompletedAt = &completedAt.Time
	}

	return progress, nil
}

func (r *PostgresRepository) CreateChallengeProgress(ctx context.Context, progress *ChallengeProgress) error {
	query := `
		INSERT INTO challenge_progress (player_id, challenge_id, current_value, is_completed, completed_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err := r.db.Exec(ctx, query,
		progress.PlayerID, progress.ChallengeID, progress.CurrentValue,
		progress.IsCompleted, progress.CompletedAt, progress.CreatedAt, progress.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create challenge progress", zap.Error(err))
		return err
	}

	return nil
}

func (r *PostgresRepository) UpdateChallengeProgress(ctx context.Context, progress *ChallengeProgress) error {
	query := `
		UPDATE challenge_progress
		SET current_value = $1, is_completed = $2, completed_at = $3, updated_at = $4
		WHERE player_id = $5 AND challenge_id = $6
	`

	_, err := r.db.Exec(ctx, query,
		progress.CurrentValue, progress.IsCompleted, progress.CompletedAt, progress.UpdatedAt,
		progress.PlayerID, progress.ChallengeID,
	)

	if err != nil {
		r.logger.Error("Failed to update challenge progress", zap.Error(err))
		return err
	}

	return nil
}

// Leaderboard operations with performance optimization

func (r *PostgresRepository) GetLeaderboard(ctx context.Context, seasonID string, limit int) (*SeasonLeaderboard, error) {
	query := `
		WITH ranked_players AS (
			SELECT
				player_id,
				SUM(score) as total_score,
				COUNT(CASE WHEN is_completed THEN 1 END) as challenges_completed,
				SUM(currency_earned) as currency_earned,
				ROW_NUMBER() OVER (ORDER BY SUM(score) DESC) as rank
			FROM challenge_progress cp
			JOIN challenges c ON cp.challenge_id = c.id
			WHERE c.season_id = $1
			GROUP BY player_id
			ORDER BY total_score DESC
			LIMIT $2
		)
		SELECT
			rp.rank,
			rp.player_id,
			p.display_name,
			rp.total_score,
			rp.challenges_completed,
			rp.currency_earned
		FROM ranked_players rp
		LEFT JOIN players p ON rp.player_id = p.id
	`

	rows, err := r.db.Query(ctx, query, seasonID, limit)
	if err != nil {
		r.logger.Error("Failed to get leaderboard", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	leaderboard := &SeasonLeaderboard{
		SeasonID:    seasonID,
		LastUpdated: time.Now(),
		Entries:     []LeaderboardEntry{},
	}

	for rows.Next() {
		entry := LeaderboardEntry{}
		err := rows.Scan(
			&entry.Rank, &entry.PlayerID, &entry.PlayerName,
			&entry.Score, &entry.ChallengesCompleted, &entry.CurrencyEarned,
		)
		if err != nil {
			r.logger.Error("Failed to scan leaderboard entry", zap.Error(err))
			return nil, err
		}
		leaderboard.Entries = append(leaderboard.Entries, entry)
	}

	// Get total players count
	countQuery := `
		SELECT COUNT(DISTINCT player_id)
		FROM challenge_progress cp
		JOIN challenges c ON cp.challenge_id = c.id
		WHERE c.season_id = $1
	`

	err = r.db.QueryRow(ctx, countQuery, seasonID).Scan(&leaderboard.TotalPlayers)
	if err != nil {
		r.logger.Error("Failed to get total players count", zap.Error(err))
		return nil, err
	}

	return leaderboard, nil
}

// Reward operations

func (r *PostgresRepository) GetAvailableRewards(ctx context.Context, playerID, seasonID string) ([]Reward, error) {
	query := `
		SELECT type, amount, item_id, rarity
		FROM season_rewards sr
		WHERE sr.season_id = $1
		AND sr.player_id = $2
		AND NOT sr.claimed
		ORDER BY sr.created_at
	`

	rows, err := r.db.Query(ctx, query, seasonID, playerID)
	if err != nil {
		r.logger.Error("Failed to get available rewards", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var rewards []Reward
	for rows.Next() {
		reward := Reward{}
		err := rows.Scan(&reward.Type, &reward.Amount, &reward.ItemID, &reward.Rarity)
		if err != nil {
			r.logger.Error("Failed to scan reward", zap.Error(err))
			return nil, err
		}
		rewards = append(rewards, reward)
	}

	return rewards, nil
}

func (r *PostgresRepository) MarkRewardsClaimed(ctx context.Context, playerID, seasonID string) error {
	query := `
		UPDATE season_rewards
		SET claimed = true, claimed_at = NOW()
		WHERE season_id = $1 AND player_id = $2 AND NOT claimed
	`

	_, err := r.db.Exec(ctx, query, seasonID, playerID)
	if err != nil {
		r.logger.Error("Failed to mark rewards as claimed", zap.Error(err))
		return err
	}

	return nil
}

// Currency operations implementation

func (r *PostgresRepository) GetSeasonalCurrency(ctx context.Context, playerID, seasonID string) (*SeasonalCurrency, error) {
	query := `
		SELECT season_id, player_id, balance, earned_total, spent_total, last_earned_at, last_spent_at, created_at, updated_at
		FROM seasonal_currency
		WHERE player_id = $1 AND season_id = $2
	`

	currency := &SeasonalCurrency{}
	var lastEarnedAt, lastSpentAt sql.NullTime

	err := r.db.QueryRow(ctx, query, playerID, seasonID).Scan(
		&currency.SeasonID, &currency.PlayerID, &currency.Balance,
		&currency.EarnedTotal, &currency.SpentTotal, &lastEarnedAt, &lastSpentAt,
		&currency.CreatedAt, &currency.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrNotFound
		}
		r.logger.Error("Failed to get seasonal currency", zap.Error(err))
		return nil, err
	}

	if lastEarnedAt.Valid {
		currency.LastEarnedAt = &lastEarnedAt.Time
	}
	if lastSpentAt.Valid {
		currency.LastSpentAt = &lastSpentAt.Time
	}

	return currency, nil
}

func (r *PostgresRepository) CreateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error {
	query := `
		INSERT INTO seasonal_currency (season_id, player_id, balance, earned_total, spent_total, last_earned_at, last_spent_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		currency.SeasonID, currency.PlayerID, currency.Balance,
		currency.EarnedTotal, currency.SpentTotal, currency.LastEarnedAt, currency.LastSpentAt,
		currency.CreatedAt, currency.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create seasonal currency", zap.Error(err))
		return err
	}

	return nil
}

func (r *PostgresRepository) UpdateSeasonalCurrency(ctx context.Context, currency *SeasonalCurrency) error {
	query := `
		UPDATE seasonal_currency
		SET balance = $1, earned_total = $2, spent_total = $3, last_earned_at = $4, last_spent_at = $5, updated_at = $6
		WHERE player_id = $7 AND season_id = $8
	`

	_, err := r.db.Exec(ctx, query,
		currency.Balance, currency.EarnedTotal, currency.SpentTotal,
		currency.LastEarnedAt, currency.LastSpentAt, currency.UpdatedAt,
		currency.PlayerID, currency.SeasonID,
	)

	if err != nil {
		r.logger.Error("Failed to update seasonal currency", zap.Error(err))
		return err
	}

	return nil
}

func (r *PostgresRepository) ExecuteCurrencyTransaction(ctx context.Context, tx *CurrencyTransaction, currency *SeasonalCurrency) error {
	// Execute in transaction for atomicity
	txDB, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer txDB.Rollback(ctx)

	// Insert transaction record
	txQuery := `
		INSERT INTO currency_transactions (id, player_id, season_id, type, amount, balance_after, reason, metadata, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	txJSON, _ := json.Marshal(tx.Metadata)
	_, err = txDB.Exec(ctx, txQuery,
		tx.ID, tx.PlayerID, tx.SeasonID, tx.Type, tx.Amount, tx.BalanceAfter,
		tx.Reason, txJSON, tx.CreatedAt,
	)
	if err != nil {
		r.logger.Error("Failed to insert currency transaction", zap.Error(err))
		return err
	}

	// Update currency balance
	currencyQuery := `
		UPDATE seasonal_currency
		SET balance = $1, earned_total = $2, spent_total = $3,
		    last_earned_at = CASE WHEN $4::text = 'earn' THEN $5 ELSE last_earned_at END,
		    last_spent_at = CASE WHEN $4::text IN ('spend', 'exchange') THEN $5 ELSE last_spent_at END,
		    updated_at = $5
		WHERE player_id = $6 AND season_id = $7
	`

	_, err = txDB.Exec(ctx, currencyQuery,
		currency.Balance, currency.EarnedTotal, currency.SpentTotal,
		tx.Type, currency.UpdatedAt, currency.PlayerID, currency.SeasonID,
	)
	if err != nil {
		r.logger.Error("Failed to update currency balance", zap.Error(err))
		return err
	}

	// Commit transaction
	return txDB.Commit(ctx)
}

func (r *PostgresRepository) GetCurrencyTransactions(ctx context.Context, playerID, seasonID string, limit int) ([]*CurrencyTransaction, error) {
	query := `
		SELECT id, player_id, season_id, type, amount, balance_after, reason, metadata, created_at
		FROM currency_transactions
		WHERE player_id = $1 AND season_id = $2
		ORDER BY created_at DESC
		LIMIT $3
	`

	rows, err := r.db.Query(ctx, query, playerID, seasonID, limit)
	if err != nil {
		r.logger.Error("Failed to get currency transactions", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var transactions []*CurrencyTransaction
	for rows.Next() {
		tx := &CurrencyTransaction{}
		var metadataJSON []byte

		err := rows.Scan(
			&tx.ID, &tx.PlayerID, &tx.SeasonID, &tx.Type, &tx.Amount, &tx.BalanceAfter,
			&tx.Reason, &metadataJSON, &tx.CreatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan currency transaction", zap.Error(err))
			return nil, err
		}

		if len(metadataJSON) > 0 {
			json.Unmarshal(metadataJSON, &tx.Metadata)
		}

		transactions = append(transactions, tx)
	}

	return transactions, nil
}

func (r *PostgresRepository) CreateCurrencyExchange(ctx context.Context, exchange *CurrencyExchange) error {
	query := `
		INSERT INTO currency_exchanges (id, player_id, season_id, currency_amount, exchange_type, reward, exchange_rate, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	rewardJSON, _ := json.Marshal(exchange.Reward)
	_, err := r.db.Exec(ctx, query,
		exchange.ID, exchange.PlayerID, exchange.SeasonID, exchange.CurrencyAmount,
		exchange.ExchangeType, rewardJSON, exchange.ExchangeRate, exchange.CreatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create currency exchange", zap.Error(err))
		return err
	}

	return nil
}

// GetPlayerExchangeSpent returns total currency spent by player on specific exchange type this season
func (r *PostgresRepository) GetPlayerExchangeSpent(ctx context.Context, playerID, seasonID, exchangeType string) (int, error) {
	query := `
		SELECT COALESCE(SUM(currency_amount), 0)
		FROM currency_exchanges
		WHERE player_id = $1 AND season_id = $2 AND exchange_type = $3
	`

	var total int
	err := r.db.QueryRow(ctx, query, playerID, seasonID, exchangeType).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to get player exchange spent", zap.Error(err))
		return 0, err
	}

	return total, nil
}

// GetDailyExchangeSpent returns total currency spent on specific exchange type today
func (r *PostgresRepository) GetDailyExchangeSpent(ctx context.Context, seasonID, exchangeType string) (int, error) {
	query := `
		SELECT COALESCE(SUM(currency_amount), 0)
		FROM currency_exchanges
		WHERE season_id = $1 AND exchange_type = $2
		AND DATE(created_at) = CURRENT_DATE
	`

	var total int
	err := r.db.QueryRow(ctx, query, seasonID, exchangeType).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to get daily exchange spent", zap.Error(err))
		return 0, err
	}

	return total, nil
}

// ChallengeObjective represents a single objective within a challenge
type ChallengeObjective struct {
	ID            string                 `json:"id"`
	ChallengeID   string                 `json:"challenge_id"`
	ObjectiveType string                 `json:"objective_type"`
	TargetValue   int                    `json:"target_value"`
	Description   string                 `json:"description"`
	ProgressType  string                 `json:"progress_type"`
	IsOptional    bool                   `json:"is_optional"`
	RewardWeight  float64                `json:"reward_weight"`
	Metadata      map[string]interface{} `json:"metadata"`
	CreatedAt     time.Time              `json:"created_at"`
}

// Errors
var (
	ErrNotFound       = sql.ErrNoRows
	ErrVersionConflict = &VersionConflictError{"version conflict"}
)

// VersionConflictError for optimistic locking
type VersionConflictError struct {
	message string
}

func (e *VersionConflictError) Error() string {
	return e.message
}