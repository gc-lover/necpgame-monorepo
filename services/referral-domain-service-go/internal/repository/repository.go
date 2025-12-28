package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"services/referral-domain-service-go/internal/config"
)

// Repository handles data access for the Referral Domain
type Repository struct {
	db     *sqlx.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *sqlx.DB, redis *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// NewDBConnection creates a new database connection with MMOFPS optimizations
func NewDBConnection(url string, config *config.Config) (*sqlx.DB, error) {
	db, err := sqlx.Connect("postgres", url)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	// Configure connection pool for MMOFPS performance
	db.SetMaxOpenConns(config.DBMaxOpenConns)
	db.SetMaxIdleConns(config.DBMaxIdleConns)
	db.SetConnMaxLifetime(config.DBConnMaxLifetime)

	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// NewRedisClient creates a new Redis client with MMOFPS optimizations
func NewRedisClient(url string, config *config.Config) (*redis.Client, error) {
	opts, err := redis.ParseURL(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	// Configure Redis pool size for MMOFPS real-time requirements
	opts.PoolSize = config.RedisPoolSize

	client := redis.NewClient(opts)

	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// Referral Code operations

// CreateReferralCode creates a new referral code
func (r *Repository) CreateReferralCode(ctx context.Context, code *ReferralCode) error {
	query := `
		INSERT INTO referral_codes (id, character_id, code, prefix, created_at, expires_at, is_active, usage_count, max_usage)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.ExecContext(ctx, query,
		code.ID, code.CharacterID, code.Code, code.Prefix, code.CreatedAt,
		code.ExpiresAt, code.IsActive, code.UsageCount, code.MaxUsage)

	if err != nil {
		return fmt.Errorf("failed to create referral code: %w", err)
	}

	// Cache the code
	return r.cacheReferralCode(ctx, code)
}

// GetReferralCode retrieves a referral code by ID
func (r *Repository) GetReferralCode(ctx context.Context, id uuid.UUID) (*ReferralCode, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("referral_code:%s", id)
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var code ReferralCode
		if json.Unmarshal([]byte(cached), &code) == nil {
			return &code, nil
		}
	}

	query := `SELECT * FROM referral_codes WHERE id = $1`
	var code ReferralCode
	err := r.db.GetContext(ctx, &code, query, id)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("referral code not found")
		}
		return nil, fmt.Errorf("failed to get referral code: %w", err)
	}

	// Cache the result
	r.cacheReferralCode(ctx, &code)
	return &code, nil
}

// ValidateReferralCode validates a referral code and increments usage
func (r *Repository) ValidateReferralCode(ctx context.Context, code string) (*ReferralCode, error) {
	query := `
		UPDATE referral_codes
		SET usage_count = usage_count + 1
		WHERE code = $1 AND is_active = true AND (expires_at IS NULL OR expires_at > $2)
			AND (max_usage IS NULL OR usage_count < max_usage)
		RETURNING *
	`

	now := time.Now()
	var referralCode ReferralCode
	err := r.db.GetContext(ctx, &referralCode, query, code, now)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("invalid or expired referral code")
		}
		return nil, fmt.Errorf("failed to validate referral code: %w", err)
	}

	return &referralCode, nil
}

// Referral Registration operations

// CreateReferralRegistration creates a new referral registration
func (r *Repository) CreateReferralRegistration(ctx context.Context, registration *ReferralRegistration) error {
	query := `
		INSERT INTO referrals (id, referrer_id, referred_id, referral_code, status, registered_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		registration.ID, registration.ReferrerID, registration.ReferredID,
		registration.ReferralCode, registration.Status, registration.RegisteredAt,
		registration.CreatedAt, registration.UpdatedAt)

	return err
}

// UpdateReferralStatus updates the status of a referral registration
func (r *Repository) UpdateReferralStatus(ctx context.Context, registrationID uuid.UUID, status string, convertedAt *time.Time) error {
	query := `
		UPDATE referrals
		SET status = $2, milestone_reached_at = $3, updated_at = $4
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, registrationID, status, convertedAt, time.Now())
	return err
}

// GetReferralCodeByCode gets a referral code by its string value
func (r *Repository) GetReferralCodeByCode(ctx context.Context, code string) (*ReferralCode, error) {
	query := `SELECT * FROM referral_codes WHERE code = $1`
	var referralCode ReferralCode
	err := r.db.GetContext(ctx, &referralCode, query, code)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("referral code not found")
		}
		return nil, fmt.Errorf("failed to get referral code: %w", err)
	}
	return &referralCode, nil
}

// GetUserReferralCodes gets all referral codes for a user
func (r *Repository) GetUserReferralCodes(ctx context.Context, userID uuid.UUID) ([]*ReferralCode, error) {
	query := `SELECT * FROM referral_codes WHERE character_id = $1 ORDER BY created_at DESC`
	var codes []*ReferralCode
	err := r.db.SelectContext(ctx, &codes, query, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get user referral codes: %w", err)
	}
	return codes, nil
}

// GetReferralStatistics gets referral statistics for a user
func (r *Repository) GetReferralStatistics(ctx context.Context, userID uuid.UUID) (*ReferralStatistics, error) {
	query := `
		SELECT
			COUNT(DISTINCT r.id) as total_referrals,
			COUNT(DISTINCT CASE WHEN r.status IN ('active', 'milestone_reached') THEN r.id END) as converted_referrals,
			COUNT(DISTINCT CASE WHEN r.status = 'pending' THEN r.id END) as pending_referrals,
			COALESCE(SUM(rr.reward_amount), 0) as total_earnings
		FROM referrals r
		LEFT JOIN referral_rewards rr ON r.id = rr.referral_id
		WHERE r.referrer_id = $1
	`

	var stats ReferralStatistics
	stats.UserID = userID
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&stats.TotalReferrals, &stats.ConvertedReferrals,
		&stats.PendingReferrals, &stats.TotalEarnings)

	if err != nil {
		return nil, fmt.Errorf("failed to get referral statistics: %w", err)
	}

	return &stats, nil
}

// GetReferralMilestone gets a referral milestone by ID
func (r *Repository) GetReferralMilestone(ctx context.Context, milestoneID uuid.UUID) (*ReferralMilestone, error) {
	query := `SELECT * FROM referral_milestones WHERE id = $1`
	var milestone ReferralMilestone
	err := r.db.GetContext(ctx, &milestone, query, milestoneID)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("referral milestone not found")
		}
		return nil, fmt.Errorf("failed to get referral milestone: %w", err)
	}
	return &milestone, nil
}

// GetUserMilestone gets a user's milestone for a specific level
func (r *Repository) GetUserMilestone(ctx context.Context, userID uuid.UUID, level int) (*ReferralMilestone, error) {
	query := `SELECT * FROM referral_milestones WHERE character_id = $1 AND milestone_level = $2`
	var milestone ReferralMilestone
	err := r.db.GetContext(ctx, &milestone, query, userID, level)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil // Milestone not found
		}
		return nil, fmt.Errorf("failed to get user milestone: %w", err)
	}
	return &milestone, nil
}

// UpdateMilestoneRewardClaimed updates a milestone to mark reward as claimed
func (r *Repository) UpdateMilestoneRewardClaimed(ctx context.Context, milestoneID uuid.UUID) error {
	query := `
		UPDATE referral_milestones
		SET is_reward_claimed = true, reward_claimed_at = $2, updated_at = $2
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, milestoneID, time.Now())
	return err
}

// GetUserMilestoneReward gets a user's reward for a specific milestone
func (r *Repository) GetUserMilestoneReward(ctx context.Context, userID, milestoneID uuid.UUID) (*ReferralReward, error) {
	query := `SELECT * FROM referral_rewards WHERE user_id = $1 AND milestone_id = $2`
	var reward ReferralReward
	err := r.db.GetContext(ctx, &reward, query, userID, milestoneID)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil // No reward found
		}
		return nil, fmt.Errorf("failed to get user milestone reward: %w", err)
	}
	return &reward, nil
}

// CreateReferralReward creates a new referral reward
func (r *Repository) CreateReferralReward(ctx context.Context, reward *ReferralReward) error {
	query := `
		INSERT INTO referral_rewards (id, character_id, referral_id, reward_type, reward_amount, currency_type, item_id, status, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.ExecContext(ctx, query,
		reward.ID, reward.CharacterID, reward.ReferralID, reward.RewardType,
		reward.RewardAmount, reward.CurrencyType, reward.ItemID, reward.Status, reward.CreatedAt)

	return err
}

// GetReferralLeaderboard gets the top referrers by converted referrals
func (r *Repository) GetReferralLeaderboard(ctx context.Context, limit int) ([]ReferralStatistics, error) {
	query := `
		SELECT
			rr.referrer_id as user_id,
			COUNT(DISTINCT rr.id) as total_referrals,
			COUNT(DISTINCT CASE WHEN rr.status = 'converted' THEN rr.id END) as converted_referrals,
			COUNT(DISTINCT CASE WHEN rr.status = 'pending' THEN rr.id END) as pending_referrals,
			COALESCE(SUM(rew.amount), 0) as total_earnings
		FROM referral_registrations rr
		LEFT JOIN referral_rewards rew ON rr.id = rew.registration_id
		GROUP BY rr.referrer_id
		ORDER BY converted_referrals DESC, total_referrals DESC
		LIMIT $1
	`

	rows, err := r.db.QueryContext(ctx, query, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get referral leaderboard: %w", err)
	}
	defer rows.Close()

	var leaderboard []ReferralStatistics
	for rows.Next() {
		var stats ReferralStatistics
		err := rows.Scan(
			&stats.UserID, &stats.TotalReferrals, &stats.ConvertedReferrals,
			&stats.PendingReferrals, &stats.TotalEarnings)
		if err != nil {
			return nil, fmt.Errorf("failed to scan leaderboard row: %w", err)
		}
		leaderboard = append(leaderboard, stats)
	}

	return leaderboard, nil
}

// Cache helper methods
func (r *Repository) cacheReferralCode(ctx context.Context, code *ReferralCode) error {
	cacheKey := fmt.Sprintf("referral_code:%s", code.ID)
	data, err := json.Marshal(code)
	if err != nil {
		return err
	}
	return r.redis.Set(ctx, cacheKey, data, time.Hour).Err()
}

// Health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	return r.db.PingContext(ctx)
}

// Close closes database and Redis connections
func (r *Repository) Close() error {
	if err := r.redis.Close(); err != nil {
		return err
	}
	return r.db.Close()
}
