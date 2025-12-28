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
		INSERT INTO referral_codes (id, code, owner_id, is_active, expires_at, max_uses, current_uses, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	code.CreatedAt = time.Now()
	code.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		code.ID, code.Code, code.OwnerID, code.IsActive, code.ExpiresAt,
		code.MaxUses, code.CurrentUses, code.CreatedAt, code.UpdatedAt)

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
		SET current_uses = current_uses + 1, updated_at = $2
		WHERE code = $1 AND is_active = true AND (expires_at IS NULL OR expires_at > $2)
			AND (max_uses IS NULL OR current_uses < max_uses)
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
		INSERT INTO referral_registrations (id, referrer_id, referee_id, referral_code_id, status, registered_at, converted_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	registration.CreatedAt = time.Now()
	registration.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		registration.ID, registration.ReferrerID, registration.RefereeID,
		registration.ReferralCodeID, registration.Status, registration.RegisteredAt,
		registration.ConvertedAt, registration.CreatedAt, registration.UpdatedAt)

	return err
}

// GetReferralStatistics gets referral statistics for a user
func (r *Repository) GetReferralStatistics(ctx context.Context, userID uuid.UUID) (*ReferralStatistics, error) {
	query := `
		SELECT
			COUNT(DISTINCT rr.id) as total_referrals,
			COUNT(DISTINCT CASE WHEN rr.status = 'converted' THEN rr.id END) as converted_referrals,
			COUNT(DISTINCT CASE WHEN rr.status = 'pending' THEN rr.id END) as pending_referrals,
			COALESCE(SUM(rew.amount), 0) as total_earnings
		FROM referral_registrations rr
		LEFT JOIN referral_rewards rew ON rr.id = rew.registration_id
		WHERE rr.referrer_id = $1
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
