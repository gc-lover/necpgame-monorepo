package server

import (
	"context"
	"errors"
	"math/rand"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

var (
	_                      = errors.New("engram not found")
	ErrInvalidTransferType = errors.New("invalid transfer type")
	_                      = errors.New("transfer failed")
	ErrInvalidCharacter    = errors.New("invalid character")
)

type EngramTransferServiceInterface interface {
	TransferEngram(ctx context.Context, engramID uuid.UUID, fromCharacterID uuid.UUID, toCharacterID uuid.UUID, transferType string, isCopy bool, newAttitudeType *string, transferPrice *float64) (*TransferEngramResult, error)
	LoanEngram(ctx context.Context, engramID uuid.UUID, fromCharacterID uuid.UUID, toCharacterID uuid.UUID, loanDurationDays int) (*LoanEngramResult, error)
	ExtractEngram(ctx context.Context, engramID uuid.UUID, extractorCharacterID uuid.UUID, targetCharacterID uuid.UUID, extractionMethod string, riskLevel float64) (*ExtractEngramResult, error)
	TradeEngram(ctx context.Context, engramID uuid.UUID, fromCharacterID uuid.UUID, tradeType string, targetCharacterID *uuid.UUID, price *float64, exchangeItemID *uuid.UUID) (*TradeEngramResult, error)
}

type TransferEngramResult struct {
	TransferID    uuid.UUID  `json:"transfer_id"`
	Success       bool       `json:"success"`
	NewEngramID   *uuid.UUID `json:"new_engram_id,omitempty"`
	TransferredAt time.Time  `json:"transferred_at"`
}

type LoanEngramResult struct {
	LoanID            uuid.UUID `json:"loan_id"`
	Success           bool      `json:"success"`
	ReturnDate        time.Time `json:"return_date"`
	TemporaryEngramID uuid.UUID `json:"temporary_engram_id"`
}

type ExtractEngramResult struct {
	ExtractionID        uuid.UUID  `json:"extraction_id"`
	Success             bool       `json:"success"`
	EngramDamaged       bool       `json:"engram_damaged"`
	DamagePercent       *float64   `json:"damage_percent,omitempty"`
	ExtractedEngramID   *uuid.UUID `json:"extracted_engram_id,omitempty"`
	TargetCharacterDied bool       `json:"target_character_died"`
}

type TradeEngramResult struct {
	TradeID    uuid.UUID  `json:"trade_id"`
	Success    bool       `json:"success"`
	TradedAt   time.Time  `json:"traded_at"`
	NewOwnerID *uuid.UUID `json:"new_owner_id,omitempty"`
}

type EngramTransferService struct {
	repo         EngramTransferRepositoryInterface
	creationRepo EngramCreationRepositoryInterface
	cache        *redis.Client
	logger       *logrus.Logger
}

func NewEngramTransferService(repo EngramTransferRepositoryInterface, creationRepo EngramCreationRepositoryInterface, cache *redis.Client) *EngramTransferService {
	return &EngramTransferService{
		repo:         repo,
		creationRepo: creationRepo,
		cache:        cache,
		logger:       GetLogger(),
	}
}

func (s *EngramTransferService) TransferEngram(ctx context.Context, engramID uuid.UUID, fromCharacterID uuid.UUID, toCharacterID uuid.UUID, transferType string, isCopy bool, newAttitudeType *string, transferPrice *float64) (*TransferEngramResult, error) {
	if fromCharacterID == toCharacterID {
		return nil, ErrInvalidCharacter
	}

	validTypes := map[string]bool{
		"voluntary": true, "cooperative": true, "forced": true, "trade": true,
	}
	if !validTypes[transferType] {
		return nil, ErrInvalidTransferType
	}

	transferID := uuid.New()
	newEngramID := uuid.UUID{}
	var hasNewEngram bool

	if isCopy {
		newEngramID = uuid.New()
		hasNewEngram = true
	}

	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			EngramID:     engramID,
			TransferType: transferType,
			IsCopy:       isCopy,
			Status:       "completed",
		},
		Parties: EngramTransferParties{
			FromCharacterID: fromCharacterID,
			ToCharacterID:   toCharacterID,
		},
		Conditions: EngramTransferConditions{
			NewAttitudeType: newAttitudeType,
			TransferPrice:   transferPrice,
		},
	}

	if hasNewEngram {
		transfer.Outcome.NewEngramID = &newEngramID
	}

	now := time.Now()
	transfer.Outcome.TransferredAt = &now

	err := s.repo.CreateTransfer(ctx, transfer)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create transfer")
		return nil, err
	}

	result := &TransferEngramResult{
		TransferID:    transferID,
		Success:       true,
		TransferredAt: now,
	}

	if hasNewEngram {
		result.NewEngramID = &newEngramID
	}

	return result, nil
}

func (s *EngramTransferService) LoanEngram(ctx context.Context, engramID uuid.UUID, fromCharacterID uuid.UUID, toCharacterID uuid.UUID, loanDurationDays int) (*LoanEngramResult, error) {
	if fromCharacterID == toCharacterID {
		return nil, ErrInvalidCharacter
	}

	if loanDurationDays < 1 || loanDurationDays > 365 {
		return nil, errors.New("loan duration must be 1-365 days")
	}

	loanID := uuid.New()
	temporaryEngramID := uuid.New()
	returnDate := time.Now().AddDate(0, 0, loanDurationDays)

	now := time.Now()
	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			ID:           uuid.New(),
			TransferID:   uuid.New(),
			EngramID:     engramID,
			TransferType: "loan",
			IsCopy:       true,
			Status:       "completed",
		},
		Parties: EngramTransferParties{
			FromCharacterID: fromCharacterID,
			ToCharacterID:   toCharacterID,
		},
		Conditions: EngramTransferConditions{
			LoanReturnDate: &returnDate,
		},
		Outcome: EngramTransferOutcome{
			NewEngramID:   &temporaryEngramID,
			TransferredAt: &now,
		},
		Metadata: EngramTransferMetadata{
			CreatedAt: now,
			UpdatedAt: now,
		},
	}

	err := s.repo.CreateTransfer(ctx, transfer)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create loan")
		return nil, err
	}

	return &LoanEngramResult{
		LoanID:            loanID,
		Success:           true,
		ReturnDate:        returnDate,
		TemporaryEngramID: temporaryEngramID,
	}, nil
}

func (s *EngramTransferService) ExtractEngram(ctx context.Context, engramID uuid.UUID, extractorCharacterID uuid.UUID, targetCharacterID uuid.UUID, _ string, riskLevel float64) (*ExtractEngramResult, error) {
	if riskLevel < 20 || riskLevel > 80 {
		return nil, errors.New("risk level must be 20-80%")
	}

	extractionID := uuid.New()
	extractedEngramID := uuid.New()

	engramDamaged := rand.Float64()*100 < riskLevel
	var damagePercent *float64
	if engramDamaged {
		dmg := 10.0 + rand.Float64()*40.0
		damagePercent = &dmg
	}

	targetDied := rand.Float64()*100 < (riskLevel * 0.3)

	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			EngramID:     engramID,
			TransferType: "extract",
			IsCopy:       false,
			Status:       "completed",
		},
		Parties: EngramTransferParties{
			FromCharacterID: targetCharacterID,
			ToCharacterID:   extractorCharacterID,
		},
		Conditions: EngramTransferConditions{
			ExtractionRiskPercent: &riskLevel,
		},
		Outcome: EngramTransferOutcome{
			EngramDamaged:       engramDamaged,
			DamagePercent:       damagePercent,
			TargetCharacterDied: targetDied,
			NewEngramID:         &extractedEngramID,
		},
	}

	now := time.Now()
	transfer.Outcome.TransferredAt = &now

	err := s.repo.CreateTransfer(ctx, transfer)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create extraction")
		return nil, err
	}

	return &ExtractEngramResult{
		ExtractionID:        extractionID,
		Success:             true,
		EngramDamaged:       engramDamaged,
		DamagePercent:       damagePercent,
		ExtractedEngramID:   &extractedEngramID,
		TargetCharacterDied: targetDied,
	}, nil
}

func (s *EngramTransferService) TradeEngram(ctx context.Context, engramID uuid.UUID, fromCharacterID uuid.UUID, tradeType string, targetCharacterID *uuid.UUID, price *float64, _ *uuid.UUID) (*TradeEngramResult, error) {
	validTypes := map[string]bool{"sell": true, "buy": true, "exchange": true}
	if !validTypes[tradeType] {
		return nil, ErrInvalidTransferType
	}

	tradeID := uuid.New()
	var newOwnerID *uuid.UUID

	if tradeType == "sell" && targetCharacterID != nil {
		newOwnerID = targetCharacterID
	} else if tradeType == "exchange" && targetCharacterID != nil {
		newOwnerID = targetCharacterID
	}

	now := time.Now()
	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			EngramID:     engramID,
			TransferID:   uuid.New(),
			TransferType: "trade",
			Status:       "completed",
		},
		Parties: EngramTransferParties{
			FromCharacterID: fromCharacterID,
		},
		Conditions: EngramTransferConditions{
			TransferPrice: price,
		},
		Metadata: EngramTransferMetadata{
			CreatedAt: now,
			UpdatedAt: now,
		},
	}

	if newOwnerID != nil {
		transfer.Parties.ToCharacterID = *newOwnerID
	}

	transferTime := time.Now()
	transfer.Outcome.TransferredAt = &transferTime

	err := s.repo.CreateTransfer(ctx, transfer)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create trade")
		return nil, err
	}

	return &TradeEngramResult{
		TradeID:    tradeID,
		Success:    true,
		TradedAt:   now,
		NewOwnerID: newOwnerID,
	}, nil
}
