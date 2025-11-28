package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-dividends-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	GetDividendSchedule(ctx context.Context, stockID uuid.UUID, limit, offset int) ([]api.DividendSchedule, int, error)
	GetPlayerDividendPayments(ctx context.Context, playerID uuid.UUID, stockID *uuid.UUID, status *string, fromDate, toDate *time.Time, limit, offset int) ([]api.DividendPayment, int, error)
	GetPlayerDRIPSettings(ctx context.Context, playerID uuid.UUID) (*api.DRIPSettings, error)
	UpdatePlayerDRIPSettings(ctx context.Context, playerID uuid.UUID, settings *api.DRIPSettingsUpdate) (*api.DRIPSettings, error)
	CreateDividendSchedule(ctx context.Context, schedule *api.DividendScheduleCreate) (*api.DividendSchedule, error)
	ProcessDividendPayment(ctx context.Context, scheduleID uuid.UUID) error
}

type InMemoryRepository struct {
	logger           *logrus.Logger
	schedules        map[uuid.UUID][]api.DividendSchedule
	payments         map[uuid.UUID][]api.DividendPayment
	dripSettings     map[uuid.UUID]*api.DRIPSettings
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:       logger,
		schedules:    make(map[uuid.UUID][]api.DividendSchedule),
		payments:     make(map[uuid.UUID][]api.DividendPayment),
		dripSettings: make(map[uuid.UUID]*api.DRIPSettings),
	}
}

func (r *InMemoryRepository) GetDividendSchedule(ctx context.Context, stockID uuid.UUID, limit, offset int) ([]api.DividendSchedule, int, error) {
	schedules, exists := r.schedules[stockID]
	if !exists {
		return []api.DividendSchedule{}, 0, nil
	}

	total := len(schedules)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.DividendSchedule{}, total, nil
	}

	return schedules[start:end], total, nil
}

func (r *InMemoryRepository) GetPlayerDividendPayments(ctx context.Context, playerID uuid.UUID, stockID *uuid.UUID, status *string, fromDate, toDate *time.Time, limit, offset int) ([]api.DividendPayment, int, error) {
	payments, exists := r.payments[playerID]
	if !exists {
		return []api.DividendPayment{}, 0, nil
	}

	var filtered []api.DividendPayment
	for _, payment := range payments {
		if stockID != nil && payment.StockId != *stockID {
			continue
		}
		if status != nil && string(payment.Status) != *status {
			continue
		}
		if fromDate != nil && payment.PaymentDate.Before(*fromDate) {
			continue
		}
		if toDate != nil && payment.PaymentDate.After(*toDate) {
			continue
		}
		filtered = append(filtered, payment)
	}

	total := len(filtered)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.DividendPayment{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) GetPlayerDRIPSettings(ctx context.Context, playerID uuid.UUID) (*api.DRIPSettings, error) {
	settings, exists := r.dripSettings[playerID]
	if !exists {
		return &api.DRIPSettings{
			PlayerId:     openapi_types.UUID(playerID),
			GlobalEnabled: false,
			Stocks:       []api.DRIPStockSettings{},
		}, nil
	}
	return settings, nil
}

func (r *InMemoryRepository) UpdatePlayerDRIPSettings(ctx context.Context, playerID uuid.UUID, update *api.DRIPSettingsUpdate) (*api.DRIPSettings, error) {
	now := time.Now()
	settings := &api.DRIPSettings{
		PlayerId:     openapi_types.UUID(playerID),
		GlobalEnabled: update.GlobalEnabled,
		Stocks:       make([]api.DRIPStockSettings, len(update.Stocks)),
		UpdatedAt:    &now,
	}

	for i, stock := range update.Stocks {
		settings.Stocks[i] = api.DRIPStockSettings{
			StockId:   stock.StockId,
			Enabled:   stock.Enabled,
			Threshold: stock.Threshold,
			StockSymbol: "",
		}
	}

	r.dripSettings[playerID] = settings
	return settings, nil
}

func (r *InMemoryRepository) CreateDividendSchedule(ctx context.Context, create *api.DividendScheduleCreate) (*api.DividendSchedule, error) {
	scheduleID := uuid.New()
	now := time.Now()
	
	schedule := &api.DividendSchedule{
		Id:              openapi_types.UUID(scheduleID),
		StockId:         create.StockId,
		StockSymbol:     "",
		Frequency:       api.DividendScheduleFrequency(create.Frequency),
		AmountPerShare:  create.AmountPerShare,
		DeclarationDate: create.DeclarationDate,
		ExDividendDate:  create.ExDividendDate,
		RecordDate:      create.RecordDate,
		PaymentDate:     create.PaymentDate,
		Status:          api.DividendScheduleStatusScheduled,
		CreatedAt:       &now,
		UpdatedAt:       &now,
	}

	stockID := uuid.UUID(create.StockId)
	r.schedules[stockID] = append(r.schedules[stockID], *schedule)
	return schedule, nil
}

func (r *InMemoryRepository) ProcessDividendPayment(ctx context.Context, scheduleID uuid.UUID) error {
	return nil
}

