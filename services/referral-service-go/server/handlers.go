// Package server Issue: #1599 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/referral-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// ServiceHandlers implements api.Handler interface (ogen typed handlers!)
type ServiceHandlers struct {
	logger *logrus.Logger
}

// NewServiceHandlers creates new handlers
func NewServiceHandlers(logger *logrus.Logger) *ServiceHandlers {
	return &ServiceHandlers{logger: logger}
}

// GetReferralCode implements getReferralCode operation.
func (h *ServiceHandlers) GetReferralCode(ctx context.Context, _ api.GetReferralCodeParams) (api.GetReferralCodeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	codeID := uuid.New()
	playerID := uuid.New()
	code := "REF123"

	return &api.SchemasReferralCode{
		ID:        api.NewOptUUID(codeID),
		PlayerID:  api.NewOptUUID(playerID),
		Code:      api.NewOptString(code),
		IsActive:  api.NewOptBool(true),
		CreatedAt: api.NewOptDateTime(time.Now()),
	}, nil
}
