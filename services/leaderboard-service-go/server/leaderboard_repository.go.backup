package server

import (
	"context"
	"database/sql"
	"fmt"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/necpgame/leaderboard-service-go/models"
)

type LeaderboardRepository interface {
	GetGlobalLeaderboard(ctx context.Context, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error)
	GetSeasonalLeaderboard(ctx context.Context, seasonID string, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error)
	GetClassLeaderboard(ctx context.Context, classID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error)
	GetFriendsLeaderboard(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, limit int) ([]models.LeaderboardEntry, error)
	GetGuildLeaderboard(ctx context.Context, guildID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error)
	
	GetPlayerRank(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, seasonID *string) (*models.PlayerRank, error)
	GetRankNeighbors(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, rangeSize int, seasonID *string) ([]models.LeaderboardEntry, error)
	
	GetLeaderboards(ctx context.Context, leaderboardType *models.LeaderboardType, limit, offset int) ([]models.Leaderboard, int, error)
	GetLeaderboard(ctx context.Context, leaderboardID uuid.UUID) (*models.Leaderboard, error)
	GetLeaderboardTop(ctx context.Context, leaderboardID uuid.UUID, limit, offset int) ([]models.LeaderboardEntry, int, error)
	GetLeaderboardPlayerRank(ctx context.Context, leaderboardID, playerID uuid.UUID) (*models.PlayerRank, error)
	GetLeaderboardRankAround(ctx context.Context, leaderboardID, playerID uuid.UUID, rangeSize int) ([]models.LeaderboardEntry, error)
	
	UpdateScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, score int64) error
	GetCharacterScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric) (*models.LeaderboardScore, error)
}

type leaderboardRepository struct {
	db *sqlx.DB
}

func NewLeaderboardRepository(db *sqlx.DB) LeaderboardRepository {
	return &leaderboardRepository{db: db}
}

func (r *leaderboardRepository) GetGlobalLeaderboard(ctx context.Context, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	query := fmt.Sprintf(`
		SELECT 
			ROW_NUMBER() OVER (ORDER BY score DESC) as rank,
			character_id,
			score,
			last_updated
		FROM leaderboard_scores
		WHERE metric = $1
		ORDER BY score DESC
		LIMIT $2 OFFSET $3
	`)
	
	var entries []models.LeaderboardEntry
	err := r.db.SelectContext(ctx, &entries, query, metric, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores WHERE metric = $1`
	r.db.GetContext(ctx, &total, countQuery, metric)
	
	return entries, total, nil
}

func (r *leaderboardRepository) GetSeasonalLeaderboard(ctx context.Context, seasonID string, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	query := fmt.Sprintf(`
		SELECT 
			ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
			ls.character_id,
			ls.score,
			ls.last_updated
		FROM leaderboard_scores ls
		INNER JOIN leaderboards l ON ls.leaderboard_id = l.id
		WHERE l.type = 'seasonal' AND l.season_id::text = $1 AND ls.metric = $2
		ORDER BY ls.score DESC
		LIMIT $3 OFFSET $4
	`)
	
	var entries []models.LeaderboardEntry
	err := r.db.SelectContext(ctx, &entries, query, seasonID, metric, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores ls INNER JOIN leaderboards l ON ls.leaderboard_id = l.id WHERE l.type = 'seasonal' AND l.season_id::text = $1 AND ls.metric = $2`
	r.db.GetContext(ctx, &total, countQuery, seasonID, metric)
	
	return entries, total, nil
}

func (r *leaderboardRepository) GetClassLeaderboard(ctx context.Context, classID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	query := fmt.Sprintf(`
		SELECT 
			ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
			ls.character_id,
			ls.score,
			ls.last_updated
		FROM leaderboard_scores ls
		INNER JOIN leaderboards l ON ls.leaderboard_id = l.id
		WHERE l.type = 'class' AND l.class_id = $1 AND ls.metric = $2
		ORDER BY ls.score DESC
		LIMIT $3 OFFSET $4
	`)
	
	var entries []models.LeaderboardEntry
	err := r.db.SelectContext(ctx, &entries, query, classID, metric, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores ls INNER JOIN leaderboards l ON ls.leaderboard_id = l.id WHERE l.type = 'class' AND l.class_id = $1 AND ls.metric = $2`
	r.db.GetContext(ctx, &total, countQuery, classID, metric)
	
	return entries, total, nil
}

func (r *leaderboardRepository) GetFriendsLeaderboard(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, limit int) ([]models.LeaderboardEntry, error) {
	query := fmt.Sprintf(`
		SELECT 
			ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
			ls.character_id,
			ls.score,
			ls.last_updated
		FROM leaderboard_scores ls
		INNER JOIN friendships f ON (f.player1_id = $1 AND f.player2_id = ls.character_id) OR (f.player2_id = $1 AND f.player1_id = ls.character_id)
		WHERE f.status = 'accepted' AND ls.metric = $2
		ORDER BY ls.score DESC
		LIMIT $3
	`)
	
	var entries []models.LeaderboardEntry
	err := r.db.SelectContext(ctx, &entries, query, characterID, metric, limit)
	return entries, err
}

func (r *leaderboardRepository) GetGuildLeaderboard(ctx context.Context, guildID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	query := fmt.Sprintf(`
		SELECT 
			ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
			ls.character_id,
			ls.score,
			ls.last_updated
		FROM leaderboard_scores ls
		INNER JOIN guild_members gm ON ls.character_id = gm.character_id
		WHERE gm.guild_id = $1 AND ls.metric = $2
		ORDER BY ls.score DESC
		LIMIT $3 OFFSET $4
	`)
	
	var entries []models.LeaderboardEntry
	err := r.db.SelectContext(ctx, &entries, query, guildID, metric, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores ls INNER JOIN guild_members gm ON ls.character_id = gm.character_id WHERE gm.guild_id = $1 AND ls.metric = $2`
	r.db.GetContext(ctx, &total, countQuery, guildID, metric)
	
	return entries, total, nil
}

func (r *leaderboardRepository) GetPlayerRank(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, seasonID *string) (*models.PlayerRank, error) {
	var query string
	var args []interface{}
	
	switch scope {
	case models.ScopeGlobal:
		query = `
			SELECT 
				character_id,
				ROW_NUMBER() OVER (ORDER BY score DESC) as rank,
				score,
				last_updated
			FROM leaderboard_scores
			WHERE metric = $1
		`
		args = []interface{}{metric}
	case models.ScopeSeasonal:
		query = `
			SELECT 
				ls.character_id,
				ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
				ls.score,
				ls.last_updated
			FROM leaderboard_scores ls
			INNER JOIN leaderboards l ON ls.leaderboard_id = l.id
			WHERE l.type = 'seasonal' AND l.season_id::text = $1 AND ls.metric = $2
		`
		if seasonID == nil {
			return nil, fmt.Errorf("season_id required for seasonal scope")
		}
		args = []interface{}{*seasonID, metric}
	default:
		return nil, fmt.Errorf("unsupported scope: %s", scope)
	}
	
	var rank models.PlayerRank
	err := r.db.GetContext(ctx, &rank, query+" AND character_id = $"+fmt.Sprintf("%d", len(args)+1), append(args, characterID)...)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	
	rank.Metric = metric
	rank.Scope = scope
	rank.SeasonID = seasonID
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores WHERE metric = $1`
	r.db.GetContext(ctx, &total, countQuery, metric)
	rank.TotalPlayers = total
	
	return &rank, nil
}

func (r *leaderboardRepository) GetRankNeighbors(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, rangeSize int, seasonID *string) ([]models.LeaderboardEntry, error) {
	rank, err := r.GetPlayerRank(ctx, characterID, metric, scope, seasonID)
	if err != nil || rank == nil {
		return nil, err
	}
	
	startRank := rank.Rank - rangeSize
	if startRank < 1 {
		startRank = 1
	}
	endRank := rank.Rank + rangeSize
	
	var query string
	var args []interface{}
	
	switch scope {
	case models.ScopeGlobal:
		query = fmt.Sprintf(`
			SELECT 
				rank,
				character_id,
				score,
				last_updated
			FROM (
				SELECT 
					ROW_NUMBER() OVER (ORDER BY score DESC) as rank,
					character_id,
					score,
					last_updated
				FROM leaderboard_scores
				WHERE metric = $1
			) ranked
			WHERE rank BETWEEN $2 AND $3
			ORDER BY rank
		`)
		args = []interface{}{metric, startRank, endRank}
	case models.ScopeSeasonal:
		query = fmt.Sprintf(`
			SELECT 
				rank,
				character_id,
				score,
				last_updated
			FROM (
				SELECT 
					ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
					ls.character_id,
					ls.score,
					ls.last_updated
				FROM leaderboard_scores ls
				INNER JOIN leaderboards l ON ls.leaderboard_id = l.id
				WHERE l.type = 'seasonal' AND l.season_id::text = $1 AND ls.metric = $2
			) ranked
			WHERE rank BETWEEN $3 AND $4
			ORDER BY rank
		`)
		if seasonID == nil {
			return nil, fmt.Errorf("season_id required for seasonal scope")
		}
		args = []interface{}{*seasonID, metric, startRank, endRank}
	default:
		return nil, fmt.Errorf("unsupported scope: %s", scope)
	}
	
	var entries []models.LeaderboardEntry
	err = r.db.SelectContext(ctx, &entries, query, args...)
	return entries, err
}

func (r *leaderboardRepository) GetLeaderboards(ctx context.Context, leaderboardType *models.LeaderboardType, limit, offset int) ([]models.Leaderboard, int, error) {
	query := `SELECT id, type, metric, season_id, class_id, is_active, created_at, updated_at FROM leaderboards WHERE 1=1`
	args := []interface{}{}
	
	if leaderboardType != nil {
		query += ` AND type = $1`
		args = append(args, *leaderboardType)
	}
	
	query += fmt.Sprintf(` ORDER BY created_at DESC LIMIT $%d OFFSET $%d`, len(args)+1, len(args)+2)
	args = append(args, limit, offset)
	
	var leaderboards []models.Leaderboard
	err := r.db.SelectContext(ctx, &leaderboards, query, args...)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboards WHERE 1=1`
	if leaderboardType != nil {
		countQuery += ` AND type = $1`
		r.db.GetContext(ctx, &total, countQuery, *leaderboardType)
	} else {
		r.db.GetContext(ctx, &total, countQuery)
	}
	
	return leaderboards, total, nil
}

func (r *leaderboardRepository) GetLeaderboard(ctx context.Context, leaderboardID uuid.UUID) (*models.Leaderboard, error) {
	var leaderboard models.Leaderboard
	query := `SELECT id, type, metric, season_id, class_id, is_active, created_at, updated_at FROM leaderboards WHERE id = $1`
	err := r.db.GetContext(ctx, &leaderboard, query, leaderboardID)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &leaderboard, err
}

func (r *leaderboardRepository) GetLeaderboardTop(ctx context.Context, leaderboardID uuid.UUID, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	query := fmt.Sprintf(`
		SELECT 
			ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
			ls.character_id,
			ls.score,
			ls.last_updated
		FROM leaderboard_scores ls
		WHERE ls.leaderboard_id = $1
		ORDER BY ls.score DESC
		LIMIT $2 OFFSET $3
	`)
	
	var entries []models.LeaderboardEntry
	err := r.db.SelectContext(ctx, &entries, query, leaderboardID, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores WHERE leaderboard_id = $1`
	r.db.GetContext(ctx, &total, countQuery, leaderboardID)
	
	return entries, total, nil
}

func (r *leaderboardRepository) GetLeaderboardPlayerRank(ctx context.Context, leaderboardID, playerID uuid.UUID) (*models.PlayerRank, error) {
	query := `
		SELECT 
			ls.character_id,
			ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
			ls.score,
			ls.last_updated
		FROM leaderboard_scores ls
		WHERE ls.leaderboard_id = $1 AND ls.character_id = $2
	`
	
	var rank models.PlayerRank
	err := r.db.GetContext(ctx, &rank, query, leaderboardID, playerID)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM leaderboard_scores WHERE leaderboard_id = $1`
	r.db.GetContext(ctx, &total, countQuery, leaderboardID)
	rank.TotalPlayers = total
	
	return &rank, nil
}

func (r *leaderboardRepository) GetLeaderboardRankAround(ctx context.Context, leaderboardID, playerID uuid.UUID, rangeSize int) ([]models.LeaderboardEntry, error) {
	rank, err := r.GetLeaderboardPlayerRank(ctx, leaderboardID, playerID)
	if err != nil || rank == nil {
		return nil, err
	}
	
	startRank := rank.Rank - rangeSize
	if startRank < 1 {
		startRank = 1
	}
	endRank := rank.Rank + rangeSize
	
	query := fmt.Sprintf(`
		SELECT 
			rank,
			character_id,
			score,
			last_updated
		FROM (
			SELECT 
				ROW_NUMBER() OVER (ORDER BY ls.score DESC) as rank,
				ls.character_id,
				ls.score,
				ls.last_updated
			FROM leaderboard_scores ls
			WHERE ls.leaderboard_id = $1
		) ranked
		WHERE rank BETWEEN $2 AND $3
		ORDER BY rank
	`)
	
	var entries []models.LeaderboardEntry
	err = r.db.SelectContext(ctx, &entries, query, leaderboardID, startRank, endRank)
	return entries, err
}

func (r *leaderboardRepository) UpdateScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, score int64) error {
	query := `
		INSERT INTO leaderboard_scores (id, character_id, score, metric, last_updated, created_at)
		VALUES (gen_random_uuid(), $1, $2, $3, NOW(), NOW())
		ON CONFLICT (character_id, metric) DO UPDATE SET
			score = EXCLUDED.score,
			last_updated = EXCLUDED.last_updated
	`
	_, err := r.db.ExecContext(ctx, query, characterID, score, metric)
	return err
}

func (r *leaderboardRepository) GetCharacterScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric) (*models.LeaderboardScore, error) {
	var score models.LeaderboardScore
	query := `SELECT id, leaderboard_id, character_id, score, metric, last_updated, created_at FROM leaderboard_scores WHERE character_id = $1 AND metric = $2 LIMIT 1`
	err := r.db.GetContext(ctx, &score, query, characterID, metric)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &score, err
}

