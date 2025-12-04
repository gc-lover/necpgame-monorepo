// Issue: #1599 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/referral-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
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
func (h *ServiceHandlers) GetReferralCode(ctx context.Context, params api.GetReferralCodeParams) (api.GetReferralCodeRes, error) {
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
