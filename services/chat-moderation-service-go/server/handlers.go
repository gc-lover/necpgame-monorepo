// Issue: #1911
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-go/pkg/api"
)

// Context timeout constants for performance
const (
	DBTimeout    = 50 * time.Millisecond // P99 <50ms requirement
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetModerationRules returns all moderation rules
func (h *Handlers) GetModerationRules(ctx context.Context, params api.GetModerationRulesParams) (api.GetModerationRulesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	rules, total, err := h.service.GetModerationRules(ctx, params)
	if err != nil {
		return &api.GetModerationRulesInternalServerError{}, err
	}

	return rules, total, nil
}

// CreateModerationRule creates a new moderation rule
func (h *Handlers) CreateModerationRule(ctx context.Context, req *api.CreateModerationRuleRequest) (api.CreateModerationRuleRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	rule, err := h.service.CreateModerationRule(ctx, req)
	if err != nil {
		return &api.CreateModerationRuleInternalServerError{}, err
	}

	return rule, nil
}

// GetModerationRule returns a specific moderation rule
func (h *Handlers) GetModerationRule(ctx context.Context, params api.GetModerationRuleParams) (api.GetModerationRuleRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	rule, err := h.service.GetModerationRule(ctx, params.RuleID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.GetModerationRuleNotFound{}, nil
		}
		return &api.GetModerationRuleInternalServerError{}, err
	}

	return rule, nil
}

// UpdateModerationRule updates an existing moderation rule
func (h *Handlers) UpdateModerationRule(ctx context.Context, params api.UpdateModerationRuleParams, req *api.UpdateModerationRuleRequest) (api.UpdateModerationRuleRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	rule, err := h.service.UpdateModerationRule(ctx, params.RuleID.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.UpdateModerationRuleNotFound{}, nil
		}
		return &api.UpdateModerationRuleInternalServerError{}, err
	}

	return rule, nil
}

// DeleteModerationRule deletes a moderation rule
func (h *Handlers) DeleteModerationRule(ctx context.Context, params api.DeleteModerationRuleParams) (api.DeleteModerationRuleRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteModerationRule(ctx, params.RuleID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.DeleteModerationRuleNotFound{}, nil
		}
		return &api.DeleteModerationRuleInternalServerError{}, err
	}

	return &api.DeleteModerationRuleNoContent{}, nil
}

// GetModerationViolations returns moderation violations
func (h *Handlers) GetModerationViolations(ctx context.Context, params api.GetModerationViolationsParams) (api.GetModerationViolationsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	violations, total, err := h.service.GetModerationViolations(ctx, params)
	if err != nil {
		return &api.GetModerationViolationsInternalServerError{}, err
	}

	return violations, total, nil
}

// GetModerationViolation returns violation details
func (h *Handlers) GetModerationViolation(ctx context.Context, params api.GetModerationViolationParams) (api.GetModerationViolationRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	violation, err := h.service.GetModerationViolation(ctx, params.ViolationID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.GetModerationViolationNotFound{}, nil
		}
		return &api.GetModerationViolationInternalServerError{}, err
	}

	return violation, nil
}

// UpdateViolationStatus updates violation status
func (h *Handlers) UpdateViolationStatus(ctx context.Context, params api.UpdateViolationStatusParams, req *api.UpdateViolationStatusRequest) (api.UpdateViolationStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	violation, err := h.service.UpdateViolationStatus(ctx, params.ViolationID.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.UpdateViolationStatusNotFound{}, nil
		}
		return &api.UpdateViolationStatusInternalServerError{}, err
	}

	return violation, nil
}

// ApplyModerationAction applies a moderation action
func (h *Handlers) ApplyModerationAction(ctx context.Context, params api.ApplyModerationActionParams, req *api.ApplyModerationActionRequest) (api.ApplyModerationActionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	action, err := h.service.ApplyModerationAction(ctx, params.ViolationID.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.ApplyModerationActionNotFound{}, nil
		}
		return &api.ApplyModerationActionInternalServerError{}, err
	}

	return action, nil
}

// GetModerationLogs returns moderation logs
func (h *Handlers) GetModerationLogs(ctx context.Context, params api.GetModerationLogsParams) (api.GetModerationLogsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	logs, total, err := h.service.GetModerationLogs(ctx, params)
	if err != nil {
		return &api.GetModerationLogsInternalServerError{}, err
	}

	return logs, total, nil
}

// CheckMessage checks message for violations (HOT PATH - P99 <50ms)
func (h *Handlers) CheckMessage(ctx context.Context, req *api.CheckMessageRequest) (api.CheckMessageRes, error) {
	// HOT PATH: Use shorter timeout and zero allocations target
	ctx, cancel := context.WithTimeout(ctx, 25*time.Millisecond)
	defer cancel()

	start := time.Now()
	result, err := h.service.CheckMessage(ctx, req)
	processingTime := time.Since(start)

	if err != nil {
		return &api.CheckMessageInternalServerError{}, err
	}

	// Add processing time for monitoring
	result.ProcessingTimeMs = float64(processingTime.Nanoseconds()) / 1e6
	return result, nil
}
