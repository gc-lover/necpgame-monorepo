package repository

import (
	"context"
	"time"

	"github.com/google/uuid"
)

// DividendSchedule represents a dividend schedule in the database
type DividendSchedule struct {
	ID             uuid.UUID `db:"id"`
	StockID        uuid.UUID `db:"stock_id"`
	Frequency      string    `db:"frequency"`
	AmountPerShare float64   `db:"amount_per_share"`
	DeclarationDate time.Time `db:"declaration_date"`
	ExDividendDate  time.Time `db:"ex_dividend_date"`
	RecordDate     time.Time `db:"record_date"`
	PaymentDate    time.Time `db:"payment_date"`
	Status         string    `db:"status"`
	CreatedAt      time.Time `db:"created_at"`
	UpdatedAt      time.Time `db:"updated_at"`
}

// DividendPayment represents a dividend payment in the database
type DividendPayment struct {
	ID                  uuid.UUID `db:"id"`
	PlayerID            uuid.UUID `db:"player_id"`
	StockID             uuid.UUID `db:"stock_id"`
	SharesOwned         int       `db:"shares_owned"`
	DividendPerShare    float64   `db:"dividend_per_share"`
	GrossAmount         float64   `db:"gross_amount"`
	TaxRate             float64   `db:"tax_rate"`
	TaxAmount           float64   `db:"tax_amount"`
	NetAmount           float64   `db:"net_amount"`
	DRIPApplied         bool      `db:"drip_applied"`
	DRIPSharesPurchased int       `db:"drip_shares_purchased"`
	PaymentDate         time.Time `db:"payment_date"`
	ScheduleID          uuid.UUID `db:"schedule_id"`
}

// DRIPSettings represents DRIP settings for a player
type DRIPSettings struct {
	PlayerID           uuid.UUID   `db:"player_id"`
	Enabled            bool        `db:"enabled"`
	MinimumThreshold   float64     `db:"minimum_threshold"`
	ReinvestTaxCredits bool        `db:"reinvest_tax_credits"`
	PreferredStocks    []uuid.UUID `db:"preferred_stocks"`
	CreatedAt          time.Time   `db:"created_at"`
	UpdatedAt          time.Time   `db:"updated_at"`
}

// DRIPSettingsUpdate represents an update to DRIP settings
type DRIPSettingsUpdate struct {
	Enabled            bool
	MinimumThreshold   *float32
	ReinvestTaxCredits *bool
	PreferredStocks    []uuid.UUID
}

// ShareholderInfo represents shareholder information for dividend processing
type ShareholderInfo struct {
	PlayerID         uuid.UUID
	SharesOwned      int
	DRIPEnabled      bool
	DRIPMinThreshold float64
}

// DividendsRepository defines interface for dividend operations
type DividendsRepository interface {
	// Dividend schedules
	GetDividendSchedule(ctx context.Context, stockID uuid.UUID) (*DividendSchedule, error)
	CreateDividendSchedule(ctx context.Context, schedule *DividendSchedule) error
	UpdateScheduleStatus(ctx context.Context, scheduleID uuid.UUID, status string) error
	GetSchedulesForProcessing(ctx context.Context, scheduleIDs []uuid.UUID, processDate time.Time) ([]*DividendSchedule, error)

	// Dividend payments
	GetDividendPayments(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]*DividendPayment, int64, error)
	CreateDividendPayment(ctx context.Context, payment *DividendPayment) error

	// DRIP settings
	GetDRIPSettings(ctx context.Context, playerID uuid.UUID) (*DRIPSettings, error)
	UpdateDRIPSettings(ctx context.Context, playerID uuid.UUID, update *DRIPSettingsUpdate) (*DRIPSettings, error)
	CreateDefaultDRIPSettings(ctx context.Context, playerID uuid.UUID) error

	// Shareholders
	GetShareholdersForStock(ctx context.Context, stockID uuid.UUID, recordDate time.Time) ([]*ShareholderInfo, error)
}