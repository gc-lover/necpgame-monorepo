// Issue: #141886468
package server

import (
	"encoding/json"
	"errors"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type EngramSecurityHandlers struct {
	securityService EngramSecurityServiceInterface
	logger          *logrus.Logger
}

func NewEngramSecurityHandlers(securityService EngramSecurityServiceInterface) *EngramSecurityHandlers {
	return &EngramSecurityHandlers{
		securityService: securityService,
		logger:          GetLogger(),
	}
}

func (h *EngramSecurityHandlers) GetEngramProtection(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	engramIDStr := vars["engramId"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	protection, err := h.securityService.GetEngramProtection(r.Context(), engramID)
	if err != nil {
		if errors.Is(err, ErrEngramNotFound) {
			h.respondError(w, http.StatusNotFound, err.Error())
			return
		}
		h.logger.WithError(err).Error("Failed to get engram protection")
		h.respondError(w, http.StatusInternalServerError, "Failed to retrieve engram protection")
		return
	}

	h.respondJSON(w, http.StatusOK, protection)
}

func (h *EngramSecurityHandlers) EncodeEngram(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	engramIDStr := vars["engramId"]

	engramID, err := uuid.Parse(engramIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid engram ID")
		return
	}

	var req struct {
		ProtectionTier      int                `json:"protection_tier"`
		ProtectionSettings  *ProtectionSettings `json:"protection_settings,omitempty"`
		NetrunnerSkillLevel int                `json:"netrunner_skill_level"`
		EncodedBy           uuid.UUID          `json:"encoded_by"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if req.ProtectionTier < 1 || req.ProtectionTier > 5 {
		h.respondError(w, http.StatusBadRequest, "Invalid protection tier (must be 1-5)")
		return
	}

	if req.EncodedBy == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "encoded_by is required")
		return
	}

	protection, err := h.securityService.EncodeEngram(r.Context(), engramID, req.ProtectionTier, req.ProtectionSettings, req.EncodedBy, req.NetrunnerSkillLevel)
	if err != nil {
		if errors.Is(err, ErrEngramNotFound) || errors.Is(err, ErrInvalidProtectionTier) {
			h.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		h.logger.WithError(err).Error("Failed to encode engram")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode engram")
		return
	}

	response := map[string]interface{}{
		"engram_id":                protection.EngramID.String(),
		"protection_tier":          protection.ProtectionTier,
		"protection_tier_name":     protection.ProtectionTierName,
		"required_netrunner_level": protection.RequiredNetrunnerLevel,
		"protection_settings":      protection.ProtectionSettings,
		"encoded_at":               protection.EncodedAt.Format("2006-01-02T15:04:05Z07:00"),
		"encoded_by":               protection.EncodedBy.String(),
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EngramSecurityHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *EngramSecurityHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

