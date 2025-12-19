// Issue: #1856
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-bank-service-go/pkg/api"
	"github.com/google/uuid"
)

// Common errors
var (
	ErrGuildBankNotFound = errors.New("guild bank not found")
	ErrInsufficientFunds = errors.New("insufficient funds")
	ErrInvalidAmount     = errors.New("invalid amount")
	ErrAccessDenied      = errors.New("access denied")
)

// Service implements business logic for guild bank service
// SOLID: Single Responsibility - business logic only
// Issue: #1856 - Memory pooling for hot path (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	bankResponsePool            sync.Pool
	transactionListResponsePool sync.Pool
	depositResponsePool         sync.Pool
	withdrawalResponsePool      sync.Pool
}

// NewService creates new service with dependency injection and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.bankResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetGuildBankOK{}
		},
	}
	s.transactionListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ListBankTransactionsOK{}
		},
	}
	s.depositResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.DepositToGuildBankOK{}
		},
	}
	s.withdrawalResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.WithdrawFromGuildBankOK{}
		},
	}

	return s
}

// GetGuildBank retrieves guild bank information - BUSINESS LOGIC
func (s *Service) GetGuildBank(ctx context.Context, params api.GetGuildBankParams) (*api.GetGuildBankOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user is guild member
	// For now, allow access to any user (will be restricted later)

	// Call repository
	bank, err := s.repo.GetGuildBank(ctx, params.GuildID)
	if err != nil {
		if err == ErrGuildBankNotFound {
			return nil, errors.New("guild bank not found")
		}
		return nil, err
	}

	// Convert to API response
	response := s.bankResponsePool.Get().(*api.GetGuildBankOK)
	defer s.bankResponsePool.Put(response)

	// Convert internal model to API model
	response.Balance.Set(int64(bank.Balance))
	response.UpdatedAt.Set(bank.UpdatedAt)

	return response, nil
}

// DepositToGuildBank deposits money to guild bank - BUSINESS LOGIC
func (s *Service) DepositToGuildBank(ctx context.Context, params api.DepositToGuildBankParams, req *api.DepositRequest) (*api.DepositToGuildBankOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// Validate amount
	if req.Amount <= 0 {
		return nil, ErrInvalidAmount
	}

	// TODO: Check if user has sufficient funds in their account
	// This would integrate with economy-service

	// Convert amount to int64
	amount := int64(req.Amount)

	// Prepare description
	var description *string
	if req.Description.IsSet() {
		desc := req.Description.Value
		description = &desc
	}

	// Call repository
	err := s.repo.DepositToGuildBank(ctx, params.GuildID, userID, amount, description)
	if err != nil {
		return nil, err
	}

	// Return response
	response := s.depositResponsePool.Get().(*api.DepositToGuildBankOK)
	defer s.depositResponsePool.Put(response)

	response.Message.Set("Deposit successful")
	return response, nil
}

// WithdrawFromGuildBank withdraws money from guild bank - BUSINESS LOGIC
func (s *Service) WithdrawFromGuildBank(ctx context.Context, params api.WithdrawFromGuildBankParams, req *api.WithdrawalRequest) (*api.WithdrawFromGuildBankOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// Validate amount
	if req.Amount <= 0 {
		return nil, ErrInvalidAmount
	}

	// TODO: Check if user has permission to withdraw (guild leader/officer)
	// For now, allow any guild member

	// Convert amount to int64
	amount := int64(req.Amount)

	// Prepare description
	var description *string
	if req.Description.IsSet() {
		desc := req.Description.Value
		description = &desc
	}

	// Call repository
	err := s.repo.WithdrawFromGuildBank(ctx, params.GuildID, userID, amount, description)
	if err != nil {
		if err == ErrInsufficientFunds {
			return nil, errors.New("insufficient guild bank funds")
		}
		return nil, err
	}

	// Return response
	response := s.withdrawalResponsePool.Get().(*api.WithdrawFromGuildBankOK)
	defer s.withdrawalResponsePool.Put(response)

	response.Message.Set("Withdrawal successful")
	return response, nil
}

// ListBankTransactions retrieves bank transactions with pagination - BUSINESS LOGIC
func (s *Service) ListBankTransactions(ctx context.Context, params api.ListBankTransactionsParams) (*api.ListBankTransactionsOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user is guild member
	// For now, allow access to any user

	// Convert API parameters to internal types
	var limit *int
	if params.Limit.IsSet() {
		limitVal := int(params.Limit.Value)
		limit = &limitVal
	}

	var offset *int
	if params.Offset.IsSet() {
		offsetVal := int(params.Offset.Value)
		offset = &offsetVal
	}

	// Call repository
	transactions, err := s.repo.GetBankTransactions(ctx, params.GuildID, limit, offset)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := s.transactionListResponsePool.Get().(*api.ListBankTransactionsOK)
	defer s.transactionListResponsePool.Put(response)

	// TODO: Convert internal BankTransaction models to API Transaction models
	// This will be implemented when the API models are defined

	return response, nil
}

// CollectGuildTax collects tax from guild members - BUSINESS LOGIC
func (s *Service) CollectGuildTax(ctx context.Context, params api.CollectGuildTaxParams, req *api.TaxCollectionRequest) (*api.CollectGuildTaxOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user is guild leader
	// For now, allow any user

	// Validate tax rate
	if req.TaxRate < 0 || req.TaxRate > 1 {
		return nil, errors.New("tax rate must be between 0 and 1")
	}

	// Call repository
	collectedAmount, err := s.repo.CollectGuildTax(ctx, params.GuildID, float64(req.TaxRate))
	if err != nil {
		return nil, err
	}

	// Return response
	response := &api.CollectGuildTaxOK{}
	response.CollectedAmount.Set(collectedAmount)
	response.Message.Set("Tax collection completed")

	return response, nil
}

// GrantGuildReward grants reward to guild bank - BUSINESS LOGIC
func (s *Service) GrantGuildReward(ctx context.Context, params api.GrantGuildRewardParams, req *api.RewardRequest) (*api.GrantGuildRewardOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user has admin permissions
	// For now, allow any user

	// Validate amount
	if req.Amount <= 0 {
		return nil, ErrInvalidAmount
	}

	// Call repository
	err := s.repo.GrantGuildReward(ctx, params.GuildID, int64(req.Amount), req.Description)
	if err != nil {
		return nil, err
	}

	// Return response
	response := &api.GrantGuildRewardOK{}
	response.Message.Set("Reward granted successfully")

	return response, nil
}

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)
