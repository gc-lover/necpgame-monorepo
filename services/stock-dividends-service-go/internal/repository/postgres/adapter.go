package postgres

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/lib/pq"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/database"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/repository"
)

// PostgresRepository implements DividendsRepository for PostgreSQL
type PostgresRepository struct {
	db     *database.DB
	logger *zap.Logger
}

// NewPostgresRepository creates a new PostgreSQL repository for dividends
func NewPostgresRepository(db *database.DB, logger *zap.Logger) repository.DividendsRepository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

// GetDividendSchedule retrieves dividend schedule for a stock
func (r *PostgresRepository) GetDividendSchedule(ctx context.Context, stockID uuid.UUID) (*repository.DividendSchedule, error) {
	query := `
		SELECT id, stock_id, frequency, amount_per_share, declaration_date,
			   ex_dividend_date, record_date, payment_date, status, created_at, updated_at
		FROM dividend_schedules
		WHERE stock_id = $1
		ORDER BY declaration_date DESC
		LIMIT 1`

	var schedule repository.DividendSchedule
	err := r.db.Pool.QueryRow(ctx, query, stockID).Scan(
		&schedule.ID,
		&schedule.StockID,
		&schedule.Frequency,
		&schedule.AmountPerShare,
		&schedule.DeclarationDate,
		&schedule.ExDividendDate,
		&schedule.RecordDate,
		&schedule.PaymentDate,
		&schedule.Status,
		&schedule.CreatedAt,
		&schedule.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("no dividend schedule found for stock %s", stockID)
		}
		return nil, fmt.Errorf("failed to get dividend schedule: %w", err)
	}

	return &schedule, nil
}

// CreateDividendSchedule creates a new dividend schedule
func (r *PostgresRepository) CreateDividendSchedule(ctx context.Context, schedule *repository.DividendSchedule) error {
	query := `
		INSERT INTO dividend_schedules (id, stock_id, frequency, amount_per_share, declaration_date,
									   ex_dividend_date, record_date, payment_date, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)`

	_, err := r.db.Pool.Exec(ctx, query,
		schedule.ID,
		schedule.StockID,
		schedule.Frequency,
		schedule.AmountPerShare,
		schedule.DeclarationDate,
		schedule.ExDividendDate,
		schedule.RecordDate,
		schedule.PaymentDate,
		schedule.Status,
		schedule.CreatedAt,
		schedule.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create dividend schedule: %w", err)
	}

	return nil
}

// UpdateScheduleStatus updates the status of a dividend schedule
func (r *PostgresRepository) UpdateScheduleStatus(ctx context.Context, scheduleID uuid.UUID, status string) error {
	query := `
		UPDATE dividend_schedules
		SET status = $1, updated_at = $2
		WHERE id = $3`

	_, err := r.db.Pool.Exec(ctx, query, status, time.Now(), scheduleID)
	if err != nil {
		return fmt.Errorf("failed to update schedule status: %w", err)
	}

	return nil
}

// GetSchedulesForProcessing gets dividend schedules ready for processing
func (r *PostgresRepository) GetSchedulesForProcessing(ctx context.Context, scheduleIDs []uuid.UUID, processDate time.Time) ([]*repository.DividendSchedule, error) {
	query := `
		SELECT id, stock_id, frequency, amount_per_share, declaration_date,
			   ex_dividend_date, record_date, payment_date, status, created_at, updated_at
		FROM dividend_schedules
		WHERE status = 'record'
		AND payment_date <= $1`

	if len(scheduleIDs) > 0 {
		query += " AND id = ANY($2)"
	}

	rows, err := r.db.Pool.Query(ctx, query, processDate, pq.Array(scheduleIDs))
	if err != nil {
		return nil, fmt.Errorf("failed to get schedules for processing: %w", err)
	}
	defer rows.Close()

	var schedules []*repository.DividendSchedule
	for rows.Next() {
		var schedule repository.DividendSchedule
		err := rows.Scan(
			&schedule.ID,
			&schedule.StockID,
			&schedule.Frequency,
			&schedule.AmountPerShare,
			&schedule.DeclarationDate,
			&schedule.ExDividendDate,
			&schedule.RecordDate,
			&schedule.PaymentDate,
			&schedule.Status,
			&schedule.CreatedAt,
			&schedule.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan schedule: %w", err)
		}
		schedules = append(schedules, &schedule)
	}

	return schedules, nil
}

// GetDividendPayments retrieves dividend payment history for a player
func (r *PostgresRepository) GetDividendPayments(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]*repository.DividendPayment, int64, error) {
	// Get total count
	countQuery := `SELECT COUNT(*) FROM dividend_payments WHERE player_id = $1`
	var total int64
	err := r.db.Pool.QueryRow(ctx, countQuery, playerID).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	// Get payments
	query := `
		SELECT id, player_id, stock_id, shares_owned, dividend_per_share, gross_amount,
			   tax_rate, tax_amount, net_amount, drip_applied, drip_shares_purchased,
			   payment_date, schedule_id
		FROM dividend_payments
		WHERE player_id = $1
		ORDER BY payment_date DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Pool.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get dividend payments: %w", err)
	}
	defer rows.Close()

	var payments []*repository.DividendPayment
	for rows.Next() {
		var payment repository.DividendPayment
		err := rows.Scan(
			&payment.ID,
			&payment.PlayerID,
			&payment.StockID,
			&payment.SharesOwned,
			&payment.DividendPerShare,
			&payment.GrossAmount,
			&payment.TaxRate,
			&payment.TaxAmount,
			&payment.NetAmount,
			&payment.DRIPApplied,
			&payment.DRIPSharesPurchased,
			&payment.PaymentDate,
			&payment.ScheduleID,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan payment: %w", err)
		}
		payments = append(payments, &payment)
	}

	return payments, total, nil
}

// CreateDividendPayment creates a new dividend payment record
func (r *PostgresRepository) CreateDividendPayment(ctx context.Context, payment *repository.DividendPayment) error {
	query := `
		INSERT INTO dividend_payments (id, player_id, stock_id, shares_owned, dividend_per_share,
									  gross_amount, tax_rate, tax_amount, net_amount, drip_applied,
									  drip_shares_purchased, payment_date, schedule_id)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)`

	_, err := r.db.Pool.Exec(ctx, query,
		payment.ID,
		payment.PlayerID,
		payment.StockID,
		payment.SharesOwned,
		payment.DividendPerShare,
		payment.GrossAmount,
		payment.TaxRate,
		payment.TaxAmount,
		payment.NetAmount,
		payment.DRIPApplied,
		payment.DRIPSharesPurchased,
		payment.PaymentDate,
		payment.ScheduleID,
	)

	if err != nil {
		return fmt.Errorf("failed to create dividend payment: %w", err)
	}

	return nil
}

// GetDRIPSettings retrieves DRIP settings for a player
func (r *PostgresRepository) GetDRIPSettings(ctx context.Context, playerID uuid.UUID) (*repository.DRIPSettings, error) {
	query := `
		SELECT player_id, enabled, minimum_threshold, reinvest_tax_credits,
			   preferred_stocks, created_at, updated_at
		FROM drip_settings
		WHERE player_id = $1`

	var settings repository.DRIPSettings
	err := r.db.Pool.QueryRow(ctx, query, playerID).Scan(
		&settings.PlayerID,
		&settings.Enabled,
		&settings.MinimumThreshold,
		&settings.ReinvestTaxCredits,
		&settings.PreferredStocks,
		&settings.CreatedAt,
		&settings.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Create default settings if not exist
			err = r.CreateDefaultDRIPSettings(ctx, playerID)
			if err != nil {
				return nil, fmt.Errorf("failed to create default DRIP settings: %w", err)
			}
			// Try again
			return r.GetDRIPSettings(ctx, playerID)
		}
		return nil, fmt.Errorf("failed to get DRIP settings: %w", err)
	}

	return &settings, nil
}

// UpdateDRIPSettings updates DRIP settings for a player
func (r *PostgresRepository) UpdateDRIPSettings(ctx context.Context, playerID uuid.UUID, update *repository.DRIPSettingsUpdate) (*repository.DRIPSettings, error) {
	// First, ensure settings exist
	_, err := r.GetDRIPSettings(ctx, playerID)
	if err != nil {
		return nil, err
	}

	query := `
		UPDATE drip_settings
		SET enabled = $1,
			minimum_threshold = COALESCE($2, minimum_threshold),
			reinvest_tax_credits = COALESCE($3, reinvest_tax_credits),
			preferred_stocks = $4,
			updated_at = $5
		WHERE player_id = $6
		RETURNING player_id, enabled, minimum_threshold, reinvest_tax_credits,
				  preferred_stocks, created_at, updated_at`

	var settings repository.DRIPSettings
	var minThreshold float64
	var reinvestCredits bool

	err = r.db.Pool.QueryRow(ctx, query,
		update.Enabled,
		update.MinimumThreshold,
		update.ReinvestTaxCredits,
		pq.Array(update.PreferredStocks),
		time.Now(),
		playerID,
	).Scan(
		&settings.PlayerID,
		&settings.Enabled,
		&minThreshold,
		&reinvestCredits,
		&settings.PreferredStocks,
		&settings.CreatedAt,
		&settings.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to update DRIP settings: %w", err)
	}

	settings.MinimumThreshold = minThreshold
	settings.ReinvestTaxCredits = reinvestCredits

	return &settings, nil
}

// CreateDefaultDRIPSettings creates default DRIP settings for a player
func (r *PostgresRepository) CreateDefaultDRIPSettings(ctx context.Context, playerID uuid.UUID) error {
	defaultThreshold := 10.0

	query := `
		INSERT INTO drip_settings (player_id, enabled, minimum_threshold, reinvest_tax_credits,
								  preferred_stocks, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (player_id) DO NOTHING`

	now := time.Now()
	_, err := r.db.Pool.Exec(ctx, query,
		playerID,
		false, // disabled by default
		defaultThreshold,
		true, // reinvest tax credits by default
		pq.Array([]uuid.UUID{}),
		now,
		now,
	)

	if err != nil {
		return fmt.Errorf("failed to create default DRIP settings: %w", err)
	}

	return nil
}

// GetShareholdersForStock gets shareholders for a specific stock at a given record date
func (r *PostgresRepository) GetShareholdersForStock(ctx context.Context, stockID uuid.UUID, recordDate time.Time) ([]*repository.ShareholderInfo, error) {
	query := `
		SELECT p.player_id, p.shares_owned, d.enabled, d.minimum_threshold
		FROM player_stock_holdings p
		LEFT JOIN drip_settings d ON p.player_id = d.player_id
		WHERE p.stock_id = $1
		AND p.purchase_date <= $2`

	rows, err := r.db.Pool.Query(ctx, query, stockID, recordDate)
	if err != nil {
		return nil, fmt.Errorf("failed to get shareholders: %w", err)
	}
	defer rows.Close()

	var shareholders []*repository.ShareholderInfo
	for rows.Next() {
		var shareholder repository.ShareholderInfo
		var dripEnabled sql.NullBool
		var dripThreshold sql.NullFloat64

		err := rows.Scan(
			&shareholder.PlayerID,
			&shareholder.SharesOwned,
			&dripEnabled,
			&dripThreshold,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan shareholder: %w", err)
		}

		shareholder.DRIPEnabled = dripEnabled.Bool
		shareholder.DRIPMinThreshold = dripThreshold.Float64

		shareholders = append(shareholders, &shareholder)
	}

	return shareholders, nil
}