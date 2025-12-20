// Package server Issue: #1856
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-bank-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetGuildBankBalance returns guild bank balance - TYPED response!
func (h *Handlers) GetGuildBankBalance(ctx context.Context, params api.GetGuildBankBalanceParams) (api.GetGuildBankBalanceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	bank, err := h.service.GetGuildBank(ctx, api.GetGuildBankParams{GuildID: params.GuildID})
	if err != nil {
		// Check for specific error types
		if err.Error() == "guild bank not found" {
			return &api.GetGuildBankBalanceNotFound{}, nil
		}
		return &api.GetGuildBankBalanceInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return bank, nil
}

// DepositToGuildBank deposits funds to guild bank - TYPED response!
func (h *Handlers) DepositToGuildBank(ctx context.Context, params api.DepositToGuildBankParams, req api.DepositRequest) (api.DepositToGuildBankRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.DepositToGuildBank(ctx, params, &req)
	if err != nil {
		// Check for validation errors
		if err == ErrInvalidAmount {
			return &api.DepositToGuildBankBadRequest{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.DepositToGuildBankUnauthorized{}, nil
		}
		return &api.DepositToGuildBankInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// WithdrawFromGuildBank withdraws funds from guild bank - TYPED response!
func (h *Handlers) WithdrawFromGuildBank(ctx context.Context, params api.WithdrawFromGuildBankParams, req api.WithdrawRequest) (api.WithdrawFromGuildBankRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.WithdrawFromGuildBank(ctx, params, &req)
	if err != nil {
		// Check for validation errors
		if err == ErrInvalidAmount {
			return &api.WithdrawFromGuildBankBadRequest{}, nil
		}
		if err == ErrInsufficientFunds {
			return &api.WithdrawFromGuildBankBadRequest{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.WithdrawFromGuildBankUnauthorized{}, nil
		}
		if err == ErrAccessDenied {
			return &api.WithdrawFromGuildBankForbidden{}, nil
		}
		return &api.WithdrawFromGuildBankInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// GetGuildBankTransactions returns bank transactions - TYPED response!
func (h *Handlers) GetGuildBankTransactions(ctx context.Context, params api.GetGuildBankTransactionsParams) (api.GetGuildBankTransactionsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	transactions, err := h.service.ListBankTransactions(ctx, api.ListBankTransactionsParams{
		GuildID: params.GuildID,
		Limit:   params.Limit,
		Offset:  params.Offset,
	})
	if err != nil {
		return &api.GetGuildBankTransactionsInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return transactions, nil
}
