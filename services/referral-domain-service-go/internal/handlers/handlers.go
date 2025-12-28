package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/referral-domain-service-go/internal/config"
	"services/referral-domain-service-go/internal/service"
)

// Handler handles HTTP requests for the Referral Domain
type Handler struct {
	service *service.Service
	logger  *zap.Logger
	config  *config.Config
}

// NewHandler creates a new handler instance with MMOFPS optimizations
func NewHandler(svc *service.Service, logger *zap.Logger, config *config.Config) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
		config:  config,
	}
}

// HealthCheck handles health check requests
func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	if err := h.service.HealthCheck(ctx); err != nil {
		h.respondError(w, http.StatusServiceUnavailable, "Service unhealthy")
		return
	}

	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "referral-domain",
		"timestamp": time.Now(),
		"version":   "1.0.0",
	}

	h.respondJSON(w, http.StatusOK, response)
}

// AuthMiddleware validates JWT tokens
func (h *Handler) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// TODO: Implement JWT validation
		// For now, just pass through
		next.ServeHTTP(w, r)
	})
}

// Referral Codes handlers

// GetReferralCodes gets all referral codes for the authenticated user
func (h *Handler) GetReferralCodes(w http.ResponseWriter, r *http.Request) {
	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	codes, err := h.service.GetUserReferralCodes(ctx, userID)
	if err != nil {
		h.logger.Error("Failed to get referral codes", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get referral codes")
		return
	}

	h.respondJSON(w, http.StatusOK, codes)
}

// CreateReferralCode creates a new referral code
func (h *Handler) CreateReferralCode(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Code      string     `json:"code"`
		ExpiresAt *time.Time `json:"expires_at,omitempty"`
		MaxUses   *int       `json:"max_uses,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	code, err := h.service.CreateReferralCode(ctx, userID, req.Code, req.ExpiresAt, req.MaxUses)
	if err != nil {
		h.logger.Error("Failed to create referral code", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create referral code")
		return
	}

	h.respondJSON(w, http.StatusCreated, code)
}

// ValidateReferralCode validates a referral code
func (h *Handler) ValidateReferralCode(w http.ResponseWriter, r *http.Request) {
	codeID := chi.URLParam(r, "codeID")

	ctx := r.Context()
	code, err := h.service.ValidateReferralCode(ctx, codeID)
	if err != nil {
		h.logger.Warn("Referral code validation failed",
			zap.String("code", codeID),
			zap.Error(err))
		h.respondError(w, http.StatusBadRequest, "Invalid referral code")
		return
	}

	h.respondJSON(w, http.StatusOK, code)
}

// Referral Registration handlers

// CreateReferralRegistration creates a new referral registration
func (h *Handler) CreateReferralRegistration(w http.ResponseWriter, r *http.Request) {
	var req struct {
		RefereeID      uuid.UUID `json:"referee_id"`
		ReferralCodeID uuid.UUID `json:"referral_code_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get referrer ID from JWT
	referrerID := uuid.New() // Placeholder

	ctx := r.Context()
	err := h.service.RegisterReferral(ctx, referrerID, req.RefereeID, req.ReferralCodeID)
	if err != nil {
		h.logger.Error("Failed to create referral registration", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to create referral registration")
		return
	}

	h.respondJSON(w, http.StatusCreated, map[string]string{"status": "created"})
}

// Referral Statistics handlers

// GetReferralStatistics gets referral statistics for the authenticated user
func (h *Handler) GetReferralStatistics(w http.ResponseWriter, r *http.Request) {
	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	stats, err := h.service.GetReferralStatistics(ctx, userID)
	if err != nil {
		h.logger.Error("Failed to get referral statistics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get referral statistics")
		return
	}

	h.respondJSON(w, http.StatusOK, stats)
}

// GetReferralLeaderboard gets the referral leaderboard
func (h *Handler) GetReferralLeaderboard(w http.ResponseWriter, r *http.Request) {
	limitStr := r.URL.Query().Get("limit")
	limit := 10 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 {
			limit = parsed
		}
	}

	ctx := r.Context()
	leaderboard, err := h.service.GetReferralLeaderboard(ctx, limit)
	if err != nil {
		h.logger.Error("Failed to get referral leaderboard", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get referral leaderboard")
		return
	}

	h.respondJSON(w, http.StatusOK, leaderboard)
}

// Referral Rewards handlers

// ClaimReferralReward claims a referral reward
func (h *Handler) ClaimReferralReward(w http.ResponseWriter, r *http.Request) {
	var req struct {
		MilestoneID uuid.UUID `json:"milestone_id"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// TODO: Get user ID from JWT
	userID := uuid.New() // Placeholder

	ctx := r.Context()
	err := h.service.ClaimReferralReward(ctx, userID, req.MilestoneID)
	if err != nil {
		h.logger.Error("Failed to claim referral reward", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "claimed"})
}

// Utility methods

// GetReferralCode gets a specific referral code
func (h *Handler) GetReferralCode(w http.ResponseWriter, r *http.Request) {
	codeIDStr := chi.URLParam(r, "codeID")
	codeID, err := uuid.Parse(codeIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid code ID")
		return
	}

	ctx := r.Context()
	code, err := h.service.GetReferralCode(ctx, codeID)
	if err != nil {
		h.logger.Error("Failed to get referral code", zap.Error(err))
		h.respondError(w, http.StatusNotFound, "Referral code not found")
		return
	}

	h.respondJSON(w, http.StatusOK, code)
}

// UpdateReferralCode updates a referral code
func (h *Handler) UpdateReferralCode(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement update logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// DeleteReferralCode deletes a referral code
func (h *Handler) DeleteReferralCode(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement delete logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// GetReferralRegistrations gets referral registrations
func (h *Handler) GetReferralRegistrations(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement get registrations logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// GetReferralRegistration gets a specific referral registration
func (h *Handler) GetReferralRegistration(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement get registration logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// UpdateReferralRegistration updates a referral registration
func (h *Handler) UpdateReferralRegistration(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement update registration logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// GetReferralMilestones gets referral milestones
func (h *Handler) GetReferralMilestones(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement get milestones logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// CreateReferralMilestone creates a new referral milestone
func (h *Handler) CreateReferralMilestone(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement create milestone logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// GetReferralMilestone gets a specific referral milestone
func (h *Handler) GetReferralMilestone(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement get milestone logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// UpdateReferralMilestone updates a referral milestone
func (h *Handler) UpdateReferralMilestone(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement update milestone logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// GetReferralRewards gets referral rewards
func (h *Handler) GetReferralRewards(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement get rewards logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// GetReferralReward gets a specific referral reward
func (h *Handler) GetReferralReward(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement get reward logic
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{"status": "not implemented"})
}

// Helper methods

func (h *Handler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handler) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}
