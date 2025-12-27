package server

import (
	"context"
	"fmt"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// Server implements the api.ServerInterface
type Server struct {
	db        *pgxpool.Pool
	logger    *zap.Logger
	tokenAuth *jwtauth.JWTAuth
	handlers  *Handlers
}

// NewServer creates a new server instance
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Server {
	handlers := NewHandlers(db, logger, tokenAuth)
	return &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
		handlers:  handlers,
	}
}

// CalculateDamage implements api.ServerInterface
func (s *Server) CalculateDamage(ctx context.Context, req api.CalculateDamageReq) (api.CalculateDamageRes, error) {
	// Convert ogen request to our internal format
	damageReq := &api.DamageCalculationRequest{
		AttackerId:        req.AttackerID,
		TargetId:          req.TargetID,
		BaseDamage:        req.BaseDamage,
		WeaponType:        req.WeaponType,
		CriticalChance:    req.CriticalChance,
		CriticalMultiplier: req.CriticalMultiplier,
		ArmorRating:       req.ArmorRating,
		Penetration:       req.Penetration,
		EnvironmentType:   req.EnvironmentType,
		ImplantEffects:    req.ImplantEffects,
	}

	// Create HTTP request context for our handlers
	httpReq, err := s.createHTTPRequest(ctx, "POST", "/api/v1/combat/damage/calculate", damageReq)
	if err != nil {
		s.logger.Error("Failed to create HTTP request", zap.Error(err))
		return &api.CalculateDamageInternalServerError{
			Code:    "500",
			Message: "Internal server error",
		}, nil
	}

	// Create response recorder
	recorder := &responseRecorder{}

	// Call handler
	s.handlers.CalculateDamage(recorder, httpReq)

	// Parse response
	if recorder.statusCode != http.StatusOK {
		return &api.CalculateDamageBadRequest{
			Code:    fmt.Sprintf("%d", recorder.statusCode),
			Message: "Damage calculation failed",
		}, nil
	}

	// Parse successful response
	var result api.DamageCalculationResult
	if err := json.Unmarshal(recorder.body.Bytes(), &result); err != nil {
		s.logger.Error("Failed to parse response", zap.Error(err))
		return &api.CalculateDamageInternalServerError{
			Code:    "500",
			Message: "Response parsing failed",
		}, nil
	}

	return &api.CalculateDamageOK{
		AttackerID:     result.AttackerId,
		TargetID:       result.TargetId,
		TotalDamage:    result.TotalDamage,
		IsCriticalHit:  result.IsCriticalHit,
		ArmorReduction: result.ArmorReduction,
		WeaponBonus:    result.WeaponBonus,
		CalculatedAt:   result.CalculatedAt,
	}, nil
}

// ValidateDamage implements api.ServerInterface
func (s *Server) ValidateDamage(ctx context.Context, req api.ValidateDamageReq) (api.ValidateDamageRes, error) {
	// Convert ogen request to our internal format
	validationReq := &api.DamageValidationRequest{
		AttackerId:     req.AttackerID,
		TargetId:       req.TargetID,
		BaseDamage:     req.BaseDamage,
		ReportedDamage: req.ReportedDamage,
		WeaponType:     req.WeaponType,
		CriticalChance: req.CriticalChance,
		CriticalMultiplier: req.CriticalMultiplier,
		ArmorRating:    req.ArmorRating,
		Penetration:    req.Penetration,
		EnvironmentType: req.EnvironmentType,
		ImplantEffects: req.ImplantEffects,
	}

	// Create HTTP request context
	httpReq, err := s.createHTTPRequest(ctx, "POST", "/api/v1/combat/damage/validate", validationReq)
	if err != nil {
		s.logger.Error("Failed to create HTTP request", zap.Error(err))
		return &api.ValidateDamageInternalServerError{
			Code:    "500",
			Message: "Internal server error",
		}, nil
	}

	recorder := &responseRecorder{}
	s.handlers.ValidateDamage(recorder, httpReq)

	if recorder.statusCode != http.StatusOK {
		return &api.ValidateDamageBadRequest{
			Code:    fmt.Sprintf("%d", recorder.statusCode),
			Message: "Damage validation failed",
		}, nil
	}

	var result api.DamageValidationResult
	if err := json.Unmarshal(recorder.body.Bytes(), &result); err != nil {
		s.logger.Error("Failed to parse response", zap.Error(err))
		return &api.ValidateDamageInternalServerError{
			Code:    "500",
			Message: "Response parsing failed",
		}, nil
	}

	return &api.ValidateDamageOK{
		AttackerID:      result.AttackerId,
		TargetID:        result.TargetId,
		ReportedDamage:  result.ReportedDamage,
		ExpectedDamage:  result.ExpectedDamage,
		IsValid:         result.IsValid,
		ValidationScore: result.ValidationScore,
		ValidatedAt:     result.ValidatedAt,
	}, nil
}

// ApplyEffects implements api.ServerInterface
func (s *Server) ApplyEffects(ctx context.Context, req api.ApplyEffectsReq) (api.ApplyEffectsRes, error) {
	// Convert ogen request to our internal format
	effectsReq := &api.ApplyEffectsRequest{
		ParticipantId: req.ParticipantID,
		Effects:       make([]api.CombatEffect, len(req.Effects)),
	}

	for i, effect := range req.Effects {
		effectsReq.Effects[i] = api.CombatEffect{
			Type:       effect.Type,
			Value:      effect.Value,
			DurationMs: effect.DurationMs,
			ParticipantId: req.ParticipantID,
		}
	}

	// Create HTTP request context
	httpReq, err := s.createHTTPRequest(ctx, "POST", "/api/v1/combat/effects/apply", effectsReq)
	if err != nil {
		s.logger.Error("Failed to create HTTP request", zap.Error(err))
		return &api.ApplyEffectsInternalServerError{
			Code:    "500",
			Message: "Internal server error",
		}, nil
	}

	recorder := &responseRecorder{}
	s.handlers.ApplyEffects(recorder, httpReq)

	if recorder.statusCode != http.StatusOK {
		return &api.ApplyEffectsBadRequest{
			Code:    fmt.Sprintf("%d", recorder.statusCode),
			Message: "Effects application failed",
		}, nil
	}

	var result api.ApplyEffectsResult
	if err := json.Unmarshal(recorder.body.Bytes(), &result); err != nil {
		s.logger.Error("Failed to parse response", zap.Error(err))
		return &api.ApplyEffectsInternalServerError{
			Code:    "500",
			Message: "Response parsing failed",
		}, nil
	}

	// Convert back to ogen format
	appliedEffects := make([]api.CombatEffect, len(result.AppliedEffects))
	for i, effect := range result.AppliedEffects {
		appliedEffects[i] = api.CombatEffect{
			ID:           effect.Id,
			ParticipantID: effect.ParticipantId,
			Type:         effect.Type,
			Value:        effect.Value,
			DurationMs:   effect.DurationMs,
			AppliedAt:    effect.AppliedAt,
			ExpiresAt:    effect.ExpiresAt,
		}
	}

	return &api.ApplyEffectsOK{
		ParticipantID:  result.ParticipantId,
		AppliedEffects: appliedEffects,
		AppliedAt:      result.AppliedAt,
	}, nil
}

// GetActiveEffects implements api.ServerInterface
func (s *Server) GetActiveEffects(ctx context.Context, req api.GetActiveEffectsReq) (api.GetActiveEffectsRes, error) {
	// Create HTTP request context
	httpReq, err := s.createHTTPRequest(ctx, "GET", fmt.Sprintf("/api/v1/combat/effects/%s/active", req.ParticipantID.String()), nil)
	if err != nil {
		s.logger.Error("Failed to create HTTP request", zap.Error(err))
		return &api.GetActiveEffectsInternalServerError{
			Code:    "500",
			Message: "Internal server error",
		}, nil
	}

	recorder := &responseRecorder{}
	s.handlers.GetActiveEffects(recorder, httpReq)

	if recorder.statusCode != http.StatusOK {
		return &api.GetActiveEffectsNotFound{
			Code:    fmt.Sprintf("%d", recorder.statusCode),
			Message: "Effects not found",
		}, nil
	}

	var response api.ActiveEffectsResponse
	if err := json.Unmarshal(recorder.body.Bytes(), &response); err != nil {
		s.logger.Error("Failed to parse response", zap.Error(err))
		return &api.GetActiveEffectsInternalServerError{
			Code:    "500",
			Message: "Response parsing failed",
		}, nil
	}

	// Convert to ogen format
	effects := make([]api.CombatEffect, len(response.Effects))
	for i, effect := range response.Effects {
		effects[i] = api.CombatEffect{
			ID:           effect.Id,
			ParticipantID: effect.ParticipantId,
			Type:         effect.Type,
			Value:        effect.Value,
			DurationMs:   effect.DurationMs,
			AppliedAt:    effect.AppliedAt,
			ExpiresAt:    effect.ExpiresAt,
		}
	}

	return &api.GetActiveEffectsOK{
		ParticipantID: response.ParticipantId,
		Effects:       effects,
		Timestamp:     response.Timestamp,
	}, nil
}

// RemoveEffect implements api.ServerInterface
func (s *Server) RemoveEffect(ctx context.Context, req api.RemoveEffectReq) (api.RemoveEffectRes, error) {
	// Create HTTP request context
	httpReq, err := s.createHTTPRequest(ctx, "DELETE", fmt.Sprintf("/api/v1/combat/effects/%s", req.EffectID.String()), nil)
	if err != nil {
		s.logger.Error("Failed to create HTTP request", zap.Error(err))
		return &api.RemoveEffectInternalServerError{
			Code:    "500",
			Message: "Internal server error",
		}, nil
	}

	recorder := &responseRecorder{}
	s.handlers.RemoveEffect(recorder, httpReq)

	if recorder.statusCode == http.StatusNoContent {
		return &api.RemoveEffectNoContent{}, nil
	}

	return &api.RemoveEffectNotFound{
		Code:    fmt.Sprintf("%d", recorder.statusCode),
		Message: "Effect not found",
	}, nil
}

// HealthCheck implements api.ServerInterface
func (s *Server) HealthCheck(ctx context.Context, req api.HealthCheckReq) (api.HealthCheckRes, error) {
	// Create HTTP request context
	httpReq, err := s.createHTTPRequest(ctx, "GET", "/health", nil)
	if err != nil {
		s.logger.Error("Failed to create HTTP request", zap.Error(err))
		return &api.HealthCheckInternalServerError{
			Code:    "500",
			Message: "Internal server error",
		}, nil
	}

	recorder := &responseRecorder{}
	s.handlers.HealthCheck(recorder, httpReq)

	if recorder.statusCode != http.StatusOK {
		return &api.HealthCheckInternalServerError{
			Code:    fmt.Sprintf("%d", recorder.statusCode),
			Message: "Service unhealthy",
		}, nil
	}

	var response api.HealthResponse
	if err := json.Unmarshal(recorder.body.Bytes(), &response); err != nil {
		s.logger.Error("Failed to parse response", zap.Error(err))
		return &api.HealthCheckInternalServerError{
			Code:    "500",
			Message: "Response parsing failed",
		}, nil
	}

	return &api.HealthCheckOK{
		Status:    response.Status,
		Version:   response.Version,
		Timestamp: response.Timestamp,
	}, nil
}

// Helper methods

import (
	"bytes"
	"encoding/json"
	"net/http/httptest"
)

// responseRecorder captures HTTP response
type responseRecorder struct {
	statusCode int
	body       bytes.Buffer
	header     http.Header
}

func (r *responseRecorder) Header() http.Header {
	if r.header == nil {
		r.header = make(http.Header)
	}
	return r.header
}

func (r *responseRecorder) Write(data []byte) (int, error) {
	return r.body.Write(data)
}

func (r *responseRecorder) WriteHeader(statusCode int) {
	r.statusCode = statusCode
}

func (s *Server) createHTTPRequest(ctx context.Context, method, url string, body interface{}) (*http.Request, error) {
	var reqBody bytes.Buffer
	if body != nil {
		if err := json.NewEncoder(&reqBody).Encode(body); err != nil {
			return nil, err
		}
	}

	req := httptest.NewRequest(method, url, &reqBody).WithContext(ctx)
	req.Header.Set("Content-Type", "application/json")
	return req, nil
}

// CreateRouter creates Chi router with ogen handlers
func (s *Server) CreateRouter() *chi.Mux {
	return api.Handler(s)
}

// Issue: #2251
