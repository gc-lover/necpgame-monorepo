package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/necpgame/referral-service-go/models"
)

type ReferralRepository interface {
	GetReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error)
	CreateReferralCode(ctx context.Context, code *models.ReferralCode) error
	ValidateReferralCode(ctx context.Context, code string) (*models.ReferralCode, error)
	
	CreateReferral(ctx context.Context, referral *models.Referral) error
	GetReferral(ctx context.Context, id uuid.UUID) (*models.Referral, error)
	GetReferralsByPlayer(ctx context.Context, playerID uuid.UUID, status *models.ReferralStatus, limit, offset int) ([]models.Referral, int, error)
	UpdateReferral(ctx context.Context, referral *models.Referral) error
	
	GetMilestones(ctx context.Context, playerID uuid.UUID) ([]models.ReferralMilestone, error)
	CreateMilestone(ctx context.Context, milestone *models.ReferralMilestone) error
	UpdateMilestone(ctx context.Context, milestone *models.ReferralMilestone) error
	
	CreateReward(ctx context.Context, reward *models.ReferralReward) error
	GetRewardHistory(ctx context.Context, playerID uuid.UUID, rewardType *models.ReferralRewardType, limit, offset int) ([]models.ReferralReward, int, error)
	
	GetReferralStats(ctx context.Context, playerID uuid.UUID) (*models.ReferralStats, error)
	GetPublicReferralStats(ctx context.Context, code string) (*models.ReferralStats, error)
	
	GetLeaderboard(ctx context.Context, leaderboardType models.ReferralLeaderboardType, limit, offset int) ([]models.ReferralLeaderboardEntry, int, error)
	GetLeaderboardPosition(ctx context.Context, playerID uuid.UUID, leaderboardType models.ReferralLeaderboardType) (*models.ReferralLeaderboardEntry, int, error)
	
	CreateEvent(ctx context.Context, event *models.ReferralEvent) error
	GetEvents(ctx context.Context, playerID uuid.UUID, eventType *models.ReferralEventType, limit, offset int) ([]models.ReferralEvent, int, error)
}

type referralRepository struct {
	db *sqlx.DB
}

func NewReferralRepository(db *sqlx.DB) ReferralRepository {
	return &referralRepository{db: db}
}

func (r *referralRepository) GetReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error) {
	var code models.ReferralCode
	query := `SELECT id, player_id, code, is_active, created_at FROM referral_codes WHERE player_id = $1 AND is_active = true LIMIT 1`
	err := r.db.GetContext(ctx, &code, query, playerID)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &code, err
}

func (r *referralRepository) CreateReferralCode(ctx context.Context, code *models.ReferralCode) error {
	query := `INSERT INTO referral_codes (id, player_id, code, is_active, created_at) VALUES ($1, $2, $3, $4, $5)`
	_, err := r.db.ExecContext(ctx, query, code.ID, code.PlayerID, code.Code, code.IsActive, code.CreatedAt)
	return err
}

func (r *referralRepository) ValidateReferralCode(ctx context.Context, codeStr string) (*models.ReferralCode, error) {
	var code models.ReferralCode
	query := `SELECT id, player_id, code, is_active, created_at FROM referral_codes WHERE code = $1 AND is_active = true LIMIT 1`
	err := r.db.GetContext(ctx, &code, query, codeStr)
	if err == sql.ErrNoRows {
		return nil, errors.New("referral code not found")
	}
	return &code, err
}

func (r *referralRepository) CreateReferral(ctx context.Context, referral *models.Referral) error {
	query := `
		INSERT INTO referrals (id, referrer_id, referee_id, referral_code_id, registered_at, status, level_10_reached, welcome_bonus_given, referrer_bonus_given, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`
	_, err := r.db.ExecContext(ctx, query,
		referral.ID, referral.ReferrerID, referral.RefereeID, referral.ReferralCodeID,
		referral.RegisteredAt, referral.Status, referral.Level10Reached,
		referral.WelcomeBonusGiven, referral.ReferrerBonusGiven,
		referral.CreatedAt, referral.UpdatedAt)
	return err
}

func (r *referralRepository) GetReferral(ctx context.Context, id uuid.UUID) (*models.Referral, error) {
	var referral models.Referral
	query := `
		SELECT id, referrer_id, referee_id, referral_code_id, registered_at, status, level_10_reached, level_10_reached_at,
			welcome_bonus_given, referrer_bonus_given, created_at, updated_at
		FROM referrals WHERE id = $1
	`
	err := r.db.GetContext(ctx, &referral, query, id)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &referral, err
}

func (r *referralRepository) GetReferralsByPlayer(ctx context.Context, playerID uuid.UUID, status *models.ReferralStatus, limit, offset int) ([]models.Referral, int, error) {
	query := `
		SELECT id, referrer_id, referee_id, referral_code_id, registered_at, status, level_10_reached, level_10_reached_at,
			welcome_bonus_given, referrer_bonus_given, created_at, updated_at
		FROM referrals WHERE referrer_id = $1
	`
	args := []interface{}{playerID}
	
	if status != nil {
		query += ` AND status = $2`
		args = append(args, *status)
	}
	
	query += fmt.Sprintf(` ORDER BY created_at DESC LIMIT $%d OFFSET $%d`, len(args)+1, len(args)+2)
	args = append(args, limit, offset)
	
	var referrals []models.Referral
	err := r.db.SelectContext(ctx, &referrals, query, args...)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM referrals WHERE referrer_id = $1`
	if status != nil {
		countQuery += ` AND status = $2`
		r.db.GetContext(ctx, &total, countQuery, playerID, *status)
	} else {
		r.db.GetContext(ctx, &total, countQuery, playerID)
	}
	
	return referrals, total, nil
}

func (r *referralRepository) UpdateReferral(ctx context.Context, referral *models.Referral) error {
	query := `
		UPDATE referrals
		SET status = $2, level_10_reached = $3, level_10_reached_at = $4, welcome_bonus_given = $5, referrer_bonus_given = $6, updated_at = $7
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query,
		referral.ID, referral.Status, referral.Level10Reached, referral.Level10ReachedAt,
		referral.WelcomeBonusGiven, referral.ReferrerBonusGiven, referral.UpdatedAt)
	return err
}

func (r *referralRepository) GetMilestones(ctx context.Context, playerID uuid.UUID) ([]models.ReferralMilestone, error) {
	var milestones []models.ReferralMilestone
	query := `
		SELECT id, player_id, milestone_type, milestone_value, achieved_at, reward_claimed, reward_claimed_at
		FROM referral_milestones WHERE player_id = $1 ORDER BY milestone_value ASC
	`
	err := r.db.SelectContext(ctx, &milestones, query, playerID)
	return milestones, err
}

func (r *referralRepository) CreateMilestone(ctx context.Context, milestone *models.ReferralMilestone) error {
	query := `
		INSERT INTO referral_milestones (id, player_id, milestone_type, milestone_value, achieved_at, reward_claimed, reward_claimed_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`
	_, err := r.db.ExecContext(ctx, query,
		milestone.ID, milestone.PlayerID, milestone.MilestoneType, milestone.MilestoneValue,
		milestone.AchievedAt, milestone.RewardClaimed, milestone.RewardClaimedAt)
	return err
}

func (r *referralRepository) UpdateMilestone(ctx context.Context, milestone *models.ReferralMilestone) error {
	query := `
		UPDATE referral_milestones
		SET reward_claimed = $2, reward_claimed_at = $3
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query, milestone.ID, milestone.RewardClaimed, milestone.RewardClaimedAt)
	return err
}

func (r *referralRepository) CreateReward(ctx context.Context, reward *models.ReferralReward) error {
	query := `
		INSERT INTO referral_rewards (id, player_id, referral_id, reward_type, reward_amount, currency_type, distributed_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`
	_, err := r.db.ExecContext(ctx, query,
		reward.ID, reward.PlayerID, reward.ReferralID, reward.RewardType,
		reward.RewardAmount, reward.CurrencyType, reward.DistributedAt)
	return err
}

func (r *referralRepository) GetRewardHistory(ctx context.Context, playerID uuid.UUID, rewardType *models.ReferralRewardType, limit, offset int) ([]models.ReferralReward, int, error) {
	query := `SELECT id, player_id, referral_id, reward_type, reward_amount, currency_type, distributed_at FROM referral_rewards WHERE player_id = $1`
	args := []interface{}{playerID}
	
	if rewardType != nil {
		query += ` AND reward_type = $2`
		args = append(args, *rewardType)
	}
	
	query += fmt.Sprintf(` ORDER BY distributed_at DESC LIMIT $%d OFFSET $%d`, len(args)+1, len(args)+2)
	args = append(args, limit, offset)
	
	var rewards []models.ReferralReward
	err := r.db.SelectContext(ctx, &rewards, query, args...)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM referral_rewards WHERE player_id = $1`
	if rewardType != nil {
		countQuery += ` AND reward_type = $2`
		r.db.GetContext(ctx, &total, countQuery, playerID, *rewardType)
	} else {
		r.db.GetContext(ctx, &total, countQuery, playerID)
	}
	
	return rewards, total, nil
}

func (r *referralRepository) GetReferralStats(ctx context.Context, playerID uuid.UUID) (*models.ReferralStats, error) {
	stats := &models.ReferralStats{
		PlayerID: playerID,
		LastUpdated: time.Now(),
	}
	
	query := `
		SELECT 
			COUNT(*) as total_referrals,
			COUNT(*) FILTER (WHERE status = 'ACTIVE') as active_referrals,
			COUNT(*) FILTER (WHERE level_10_reached = true) as level_10_referrals
		FROM referrals WHERE referrer_id = $1
	`
	err := r.db.GetContext(ctx, stats, query, playerID)
	if err != nil {
		return nil, err
	}
	
	query = `SELECT COALESCE(SUM(reward_amount), 0) FROM referral_rewards WHERE player_id = $1`
	err = r.db.GetContext(ctx, &stats.TotalRewards, query, playerID)
	if err != nil {
		return nil, err
	}
	
	milestones, err := r.GetMilestones(ctx, playerID)
	if err == nil && len(milestones) > 0 {
		for i := len(milestones) - 1; i >= 0; i-- {
			if milestones[i].RewardClaimed {
				mt := milestones[i].MilestoneType
				stats.CurrentMilestone = &mt
				break
			}
		}
	}
	
	return stats, nil
}

func (r *referralRepository) GetPublicReferralStats(ctx context.Context, code string) (*models.ReferralStats, error) {
	referralCode, err := r.ValidateReferralCode(ctx, code)
	if err != nil {
		return nil, err
	}
	
	return r.GetReferralStats(ctx, referralCode.PlayerID)
}

func (r *referralRepository) GetLeaderboard(ctx context.Context, leaderboardType models.ReferralLeaderboardType, limit, offset int) ([]models.ReferralLeaderboardEntry, int, error) {
	var entries []models.ReferralLeaderboardEntry
	var query string
	
	switch leaderboardType {
	case models.LeaderboardTypeTopReferrers:
		query = `
			SELECT 
				ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC) as rank,
				referrer_id as player_id,
				COUNT(*) as total_referrals,
				COUNT(*) FILTER (WHERE status = 'ACTIVE') as active_referrals,
				COUNT(*) FILTER (WHERE level_10_reached = true) as level_10_referrals
			FROM referrals
			GROUP BY referrer_id
			ORDER BY total_referrals DESC
			LIMIT $1 OFFSET $2
		`
	case models.LeaderboardTypeTopMilestone:
		query = `
			SELECT 
				ROW_NUMBER() OVER (ORDER BY MAX(milestone_value) DESC) as rank,
				player_id,
				0 as total_referrals,
				0 as active_referrals,
				0 as level_10_referrals
			FROM referral_milestones
			WHERE reward_claimed = true
			GROUP BY player_id
			ORDER BY MAX(milestone_value) DESC
			LIMIT $1 OFFSET $2
		`
	case models.LeaderboardTypeTopRewards:
		query = `
			SELECT 
				ROW_NUMBER() OVER (ORDER BY SUM(reward_amount) DESC) as rank,
				player_id,
				0 as total_referrals,
				0 as active_referrals,
				0 as level_10_referrals
			FROM referral_rewards
			GROUP BY player_id
			ORDER BY SUM(reward_amount) DESC
			LIMIT $1 OFFSET $2
		`
	default:
		return nil, 0, errors.New("invalid leaderboard type")
	}
	
	err := r.db.SelectContext(ctx, &entries, query, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	
	var total int
	var countQuery string
	switch leaderboardType {
	case models.LeaderboardTypeTopReferrers:
		countQuery = `SELECT COUNT(DISTINCT referrer_id) FROM referrals`
	case models.LeaderboardTypeTopMilestone:
		countQuery = `SELECT COUNT(DISTINCT player_id) FROM referral_milestones WHERE reward_claimed = true`
	case models.LeaderboardTypeTopRewards:
		countQuery = `SELECT COUNT(DISTINCT player_id) FROM referral_rewards`
	}
	r.db.GetContext(ctx, &total, countQuery)
	
	return entries, total, nil
}

func (r *referralRepository) GetLeaderboardPosition(ctx context.Context, playerID uuid.UUID, leaderboardType models.ReferralLeaderboardType) (*models.ReferralLeaderboardEntry, int, error) {
	entries, _, err := r.GetLeaderboard(ctx, leaderboardType, 1000, 0)
	if err != nil {
		return nil, 0, err
	}
	
	for i, entry := range entries {
		if entry.PlayerID == playerID {
			return &entry, i + 1, nil
		}
	}
	
	return nil, 0, nil
}

func (r *referralRepository) CreateEvent(ctx context.Context, event *models.ReferralEvent) error {
	eventDataJSON, _ := json.Marshal(event.EventData)
	query := `INSERT INTO referral_events (id, player_id, event_type, event_data, created_at) VALUES ($1, $2, $3, $4, $5)`
	_, err := r.db.ExecContext(ctx, query, event.ID, event.PlayerID, event.EventType, eventDataJSON, event.CreatedAt)
	return err
}

func (r *referralRepository) GetEvents(ctx context.Context, playerID uuid.UUID, eventType *models.ReferralEventType, limit, offset int) ([]models.ReferralEvent, int, error) {
	query := `SELECT id, player_id, event_type, event_data, created_at FROM referral_events WHERE player_id = $1`
	args := []interface{}{playerID}
	
	if eventType != nil {
		query += ` AND event_type = $2`
		args = append(args, *eventType)
	}
	
	query += fmt.Sprintf(` ORDER BY created_at DESC LIMIT $%d OFFSET $%d`, len(args)+1, len(args)+2)
	args = append(args, limit, offset)
	
	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()
	
	var events []models.ReferralEvent
	for rows.Next() {
		var event models.ReferralEvent
		var eventDataJSON []byte
		
		if err := rows.Scan(&event.ID, &event.PlayerID, &event.EventType, &eventDataJSON, &event.CreatedAt); err != nil {
			continue
		}
		
		if len(eventDataJSON) > 0 {
			json.Unmarshal(eventDataJSON, &event.EventData)
		}
		
		events = append(events, event)
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM referral_events WHERE player_id = $1`
	if eventType != nil {
		countQuery += ` AND event_type = $2`
		r.db.GetContext(ctx, &total, countQuery, playerID, *eventType)
	} else {
		r.db.GetContext(ctx, &total, countQuery, playerID)
	}
	
	return events, total, nil
}

