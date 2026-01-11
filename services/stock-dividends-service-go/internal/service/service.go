package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/api"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/config"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/repository"
)

// DividendsService implements business logic for dividend management
type DividendsService struct {
	repo   repository.DividendsRepository
	logger *zap.Logger
	config *config.Config
}

// NewDividendsService creates a new dividends service instance
func NewDividendsService(repo repository.DividendsRepository, logger *zap.Logger, cfg *config.Config) *DividendsService {
	return &DividendsService{
		repo:   repo,
		logger: logger,
		config: cfg,
	}
}

// GetDividendSchedule retrieves dividend schedule for a stock
func (s *DividendsService) GetDividendSchedule(ctx context.Context, stockID uuid.UUID) (*api.DividendSchedule, error) {
	schedule, err := s.repo.GetDividendSchedule(ctx, stockID)
	if err != nil {
		return nil, fmt.Errorf("failed to get dividend schedule: %w", err)
	}

	return &api.DividendSchedule{
		ID:               schedule.ID,
		StockID:          schedule.StockID,
		Frequency:        api.DividendScheduleFrequency(schedule.Frequency),
		AmountPerShare:   float32(schedule.AmountPerShare),
		DeclarationDate:  schedule.DeclarationDate,
		ExDividendDate:   schedule.ExDividendDate,
		RecordDate:       schedule.RecordDate,
		PaymentDate:      schedule.PaymentDate,
		Status:           api.DividendScheduleStatus(schedule.Status),
		CreatedAt:        api.NewOptDateTime(schedule.CreatedAt),
		UpdatedAt:        api.NewOptDateTime(schedule.UpdatedAt),
	}, nil
}

// GetDividendPayments retrieves dividend payment history for a player
func (s *DividendsService) GetDividendPayments(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.DividendPayment, int64, error) {
	payments, total, err := s.repo.GetDividendPayments(ctx, playerID, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get dividend payments: %w", err)
	}

	apiPayments := make([]api.DividendPayment, len(payments))
	for i, payment := range payments {
		apiPayments[i] = api.DividendPayment{
			ID:                  payment.ID,
			PlayerID:            payment.PlayerID,
			StockID:             payment.StockID,
			SharesOwned:         payment.SharesOwned,
			DividendPerShare:    float32(payment.DividendPerShare),
			GrossAmount:         float32(payment.GrossAmount),
			TaxRate:             api.NewOptFloat32(float32(payment.TaxRate)),
			TaxAmount:           float32(payment.TaxAmount),
			NetAmount:           float32(payment.NetAmount),
			DripApplied:         api.NewOptBool(payment.DRIPApplied),
			DripSharesPurchased: api.NewOptInt(payment.DRIPSharesPurchased),
			PaymentDate:         payment.PaymentDate,
			ScheduleID:          payment.ScheduleID,
		}
	}

	return apiPayments, total, nil
}

// GetDRIPSettings retrieves DRIP settings for a player
func (s *DividendsService) GetDRIPSettings(ctx context.Context, playerID uuid.UUID) (*api.DRIPSettings, error) {
	settings, err := s.repo.GetDRIPSettings(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get DRIP settings: %w", err)
	}

	return &api.DRIPSettings{
		PlayerID:             settings.PlayerID,
		Enabled:              settings.Enabled,
		MinimumThreshold:     api.NewOptFloat32(float32(settings.MinimumThreshold)),
		ReinvestTaxCredits:    api.NewOptBool(settings.ReinvestTaxCredits),
		PreferredStocks:      settings.PreferredStocks,
		CreatedAt:            api.NewOptDateTime(settings.CreatedAt),
		UpdatedAt:            api.NewOptDateTime(settings.UpdatedAt),
	}, nil
}

// UpdateDRIPSettings updates DRIP settings for a player
func (s *DividendsService) UpdateDRIPSettings(ctx context.Context, playerID uuid.UUID, update *api.DRIPSettingsUpdate) (*api.DRIPSettings, error) {
	var minThreshold *float32
	if update.MinimumThreshold.Set {
		val := update.MinimumThreshold.Value
		minThreshold = &val
	}

	var reinvestCredits *bool
	if update.ReinvestTaxCredits.Set {
		val := update.ReinvestTaxCredits.Value
		reinvestCredits = &val
	}

	settings, err := s.repo.UpdateDRIPSettings(ctx, playerID, &repository.DRIPSettingsUpdate{
		Enabled:             update.Enabled,
		MinimumThreshold:    minThreshold,
		ReinvestTaxCredits:   reinvestCredits,
		PreferredStocks:     update.PreferredStocks,
	})
	if err != nil {
		return nil, fmt.Errorf("failed to update DRIP settings: %w", err)
	}

	return &api.DRIPSettings{
		PlayerID:             settings.PlayerID,
		Enabled:              settings.Enabled,
		MinimumThreshold:     api.NewOptFloat32(float32(settings.MinimumThreshold)),
		ReinvestTaxCredits:    api.NewOptBool(settings.ReinvestTaxCredits),
		PreferredStocks:      settings.PreferredStocks,
		CreatedAt:            api.NewOptDateTime(settings.CreatedAt),
		UpdatedAt:            api.NewOptDateTime(settings.UpdatedAt),
	}, nil
}

// CreateDividendSchedule creates a new dividend schedule
func (s *DividendsService) CreateDividendSchedule(ctx context.Context, req *api.CreateDividendScheduleRequest) (*repository.DividendSchedule, error) {
	schedule := &repository.DividendSchedule{
		ID:             uuid.New(),
		StockID:        req.StockID,
		Frequency:      string(req.Frequency),
		AmountPerShare: float64(req.AmountPerShare),
		DeclarationDate: req.DeclarationDate,
		ExDividendDate: req.ExDividendDate,
		RecordDate:     req.RecordDate,
		PaymentDate:    req.PaymentDate,
		Status:         "announced",
		CreatedAt:      time.Now(),
		UpdatedAt:      time.Now(),
	}

	err := s.repo.CreateDividendSchedule(ctx, schedule)
	if err != nil {
		return nil, fmt.Errorf("failed to create dividend schedule: %w", err)
	}

	return schedule, nil
}

// ProcessDividendPayments processes dividend payments for eligible shareholders
func (s *DividendsService) ProcessDividendPayments(ctx context.Context, req *api.ProcessDividendsRequest) (*api.ProcessDividendsResponse, error) {
	processDate := time.Now()
	if req.ProcessDate.Set {
		processDate = req.ProcessDate.Value
	}

	dryRun := false
	if req.DryRun.Set {
		dryRun = req.DryRun.Value
	}

	// Get all eligible schedules for processing
	schedules, err := s.repo.GetSchedulesForProcessing(ctx, req.ScheduleIds, processDate)
	if err != nil {
		return nil, fmt.Errorf("failed to get schedules for processing: %w", err)
	}

	var totalPayments, totalDRIPApplications int
	var totalAmountPaid, totalTaxWithheld float64

	for _, schedule := range schedules {
		// Get shareholders for this stock
		shareholders, err := s.repo.GetShareholdersForStock(ctx, schedule.StockID, schedule.RecordDate)
		if err != nil {
			s.logger.Error("Failed to get shareholders", zap.Error(err), zap.String("stock_id", schedule.StockID.String()))
			continue
		}

		for _, shareholder := range shareholders {
			// Calculate dividend amount
			dividendAmount := float64(shareholder.SharesOwned) * schedule.AmountPerShare
			if dividendAmount < s.config.Dividends.DRIPMinThreshold {
				continue // Skip if below minimum threshold
			}

			// Calculate tax
			taxAmount := dividendAmount * s.config.Dividends.TaxRate
			netAmount := dividendAmount - taxAmount

			// Check DRIP settings
			var dripApplied bool
			var dripShares int

			if shareholder.DRIPEnabled && netAmount >= shareholder.DRIPMinThreshold {
				dripApplied = true
				// Calculate how many shares can be purchased with net amount
				// This is a simplified calculation - in reality would need current stock price
				dripShares = int(netAmount / schedule.AmountPerShare)
				totalDRIPApplications++
			}

			if !dryRun {
				// Create payment record
				payment := &repository.DividendPayment{
					ID:                  uuid.New(),
					PlayerID:            shareholder.PlayerID,
					StockID:             schedule.StockID,
					SharesOwned:         shareholder.SharesOwned,
					DividendPerShare:    schedule.AmountPerShare,
					GrossAmount:         dividendAmount,
					TaxRate:             s.config.Dividends.TaxRate,
					TaxAmount:           taxAmount,
					NetAmount:           netAmount,
					DRIPApplied:         dripApplied,
					DRIPSharesPurchased: dripShares,
					PaymentDate:         time.Now(),
					ScheduleID:          schedule.ID,
				}

				err = s.repo.CreateDividendPayment(ctx, payment)
				if err != nil {
					s.logger.Error("Failed to create dividend payment", zap.Error(err))
					continue
				}

				// Process wallet transfer (simplified - would integrate with wallet-service)
				err = s.processWalletTransfer(ctx, shareholder.PlayerID, netAmount)
				if err != nil {
					s.logger.Error("Failed to process wallet transfer", zap.Error(err))
				}

				totalPayments++
				totalAmountPaid += netAmount
				totalTaxWithheld += taxAmount
			}
		}

		if !dryRun {
			// Update schedule status
			err = s.repo.UpdateScheduleStatus(ctx, schedule.ID, "paid")
			if err != nil {
				s.logger.Error("Failed to update schedule status", zap.Error(err))
			}
		}
	}

	return &api.ProcessDividendsResponse{
		ProcessedSchedules:    len(schedules),
		TotalPayments:         totalPayments,
		TotalAmountPaid:       float32(totalAmountPaid),
		TotalTaxWithheld:      float32(totalTaxWithheld),
		DripApplications:      totalDRIPApplications,
		Errors:                []string{}, // Would collect actual errors
		ProcessedAt:           time.Now(),
	}, nil
}

// processWalletTransfer simulates wallet transfer (would integrate with wallet-service)
func (s *DividendsService) processWalletTransfer(ctx context.Context, playerID uuid.UUID, amount float64) error {
	// TODO: Integrate with wallet-service
	s.logger.Info("Processing wallet transfer",
		zap.String("player_id", playerID.String()),
		zap.Float64("amount", amount))
	return nil
}