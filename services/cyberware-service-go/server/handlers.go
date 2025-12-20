// Package server Issue: #2210 - HTTP handlers for cyberware service (extracted from service.go for better organization)
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"net/http"
	"runtime"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/cyberware-service-go/models"
)

// HealthCheckHandler handles health check requests
func (s *CyberwareService) HealthCheckHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

// ReadinessCheckHandler handles readiness check requests
func (s *CyberwareService) ReadinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	if err := s.db.PingContext(ctx); err != nil {
		s.logger.Error("Database health check failed", zap.Error(err))
		s.respondJSON(w, http.StatusServiceUnavailable, map[string]string{"status": "unhealthy", "error": "database"})
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ready"})
}

// MetricsHandler handles metrics requests
func (s *CyberwareService) MetricsHandler(w http.ResponseWriter, request *http.Request) {
	// Use memory pool for response
	response := s.responsePool.Get().(map[string]interface{})
	defer func() {
		// Clear and return to pool
		for k := range response {
			delete(response, k)
		}
		s.responsePool.Put(response)
	}()

	response["service"] = "cyberware-service"
	response["uptime"] = time.Since(s.startTime).String()
	response["version"] = "1.0.0"
	response["goroutines"] = runtime.NumGoroutine()

	s.respondJSON(w, http.StatusOK, response)
}

// GetImplantCatalogHandler Catalog handlers
func (s *CyberwareService) GetImplantCatalogHandler(w http.ResponseWriter, r *http.Request) {
	if err := s.checkCircuitBreakerAndLoadShedding(); err != nil {
		s.respondError(w, http.StatusServiceUnavailable, "Service temporarily unavailable")
		return
	}
	defer s.releaseLoadShedding()

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Parse query parameters
	implantType := r.URL.Query().Get("type")
	category := r.URL.Query().Get("category")
	rarity := r.URL.Query().Get("rarity")

	limit := 50 // default
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0 // default
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	implants, err := s.repo.GetImplantCatalog(ctx, implantType, category, rarity, limit, offset)
	totalCount := len(implants) // TODO: Implement proper pagination with COUNT query
	if err != nil {
		s.logger.Error("Failed to get implant catalog", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get implant catalog")
		return
	}

	response := map[string]interface{}{
		"implants":    implants,
		"total_count": totalCount,
		"has_more":    offset+limit < totalCount,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CyberwareService) GetImplantDetailHandler(w http.ResponseWriter, r *http.Request) {
	if err := s.checkCircuitBreakerAndLoadShedding(); err != nil {
		s.respondError(w, http.StatusServiceUnavailable, "Service temporarily unavailable")
		return
	}
	defer s.releaseLoadShedding()

	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	implantID := chi.URLParam(r, "implantId")
	if implantID == "" {
		s.respondError(w, http.StatusBadRequest, "Implant ID is required")
		return
	}

	implant, err := s.repo.GetImplantByID(ctx, implantID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "Implant not found")
			return
		}
		s.logger.Error("Failed to get implant detail", zap.Error(err), zap.String("implant_id", implantID))
		s.respondError(w, http.StatusInternalServerError, "Failed to get implant detail")
		return
	}

	// Get upgrade costs
	upgradeCosts, err := s.repo.GetImplantUpgradeCosts()
	if err != nil {
		s.logger.Warn("Failed to get upgrade costs", zap.Error(err), zap.String("implant_id", implantID))
		upgradeCosts = []map[string]interface{}{}
	}

	response := map[string]interface{}{
		"implant":       implant,
		"upgrade_costs": upgradeCosts,
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetCharacterImplantsHandler Character implant handlers
func (s *CyberwareService) GetCharacterImplantsHandler(w http.ResponseWriter, r *http.Request) {
	if err := s.checkCircuitBreakerAndLoadShedding(); err != nil {
		s.respondError(w, http.StatusServiceUnavailable, "Service temporarily unavailable")
		return
	}
	defer s.releaseLoadShedding()

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		s.respondError(w, http.StatusBadRequest, "Character ID is required")
		return
	}

	activeOnly := false
	if activeOnlyStr := r.URL.Query().Get("active_only"); activeOnlyStr == "true" {
		activeOnly = true
	}

	implants, err := s.repo.GetCharacterImplants(ctx, characterID, activeOnly)
	if err != nil {
		s.logger.Error("Failed to get character implants", zap.Error(err), zap.String("character_id", characterID))
		s.respondError(w, http.StatusInternalServerError, "Failed to get character implants")
		return
	}

	// Get limits for the character
	limits, err := s.repo.GetCharacterLimits(ctx, characterID)
	if err != nil {
		s.logger.Warn("Failed to get character limits", zap.Error(err), zap.String("character_id", characterID))
		limits = &models.ImplantLimitsState{
			MaxEnergy:   100,
			MaxHumanity: 100,
		}
	}

	response := map[string]interface{}{
		"implants":            implants,
		"total_energy_used":   limits.TotalEnergyUsed,
		"max_energy":          limits.MaxEnergy,
		"total_humanity_lost": limits.TotalHumanityLost,
		"max_humanity":        limits.MaxHumanity,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CyberwareService) AcquireImplantHandler(w http.ResponseWriter, r *http.Request) {
	if err := s.checkCircuitBreakerAndLoadShedding(); err != nil {
		s.respondError(w, http.StatusServiceUnavailable, "Service temporarily unavailable")
		return
	}
	defer s.releaseLoadShedding()

	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		s.respondError(w, http.StatusBadRequest, "Character ID is required")
		return
	}

	var req models.AcquireImplantRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate implant exists
	implant, err := s.repo.GetImplantByID(ctx, req.ImplantID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "Implant not found")
			return
		}
		s.logger.Error("Failed to validate implant", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to validate implant")
		return
	}

	// Check if character already owns this implant
	ownsImplant, err := s.repo.CharacterOwnsImplant(ctx, characterID, req.ImplantID)
	if err != nil {
		s.logger.Error("Failed to check implant ownership", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to check implant ownership")
		return
	}

	if ownsImplant {
		s.respondError(w, http.StatusConflict, "Character already owns this implant")
		return
	}

	// Perform acquisition based on payment method
	var transactionID string
	var remainingBalance float64

	switch req.PaymentMethod {
	case models.PaymentCredits:
		// Check credits balance and deduct
		balance, err := s.checkCreditsBalance(characterID, float64(implant.Cost))
		if err != nil {
			s.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		remainingBalance = balance

	case models.PaymentMaterials:
		// Check materials (simplified - would integrate with inventory service)
		s.logger.Info("Materials payment - would integrate with inventory service",
			zap.String("character_id", characterID),
			zap.String("implant_id", req.ImplantID))

	case models.PaymentQuestReward:
		// Quest rewards are free
		s.logger.Info("Quest reward acquisition",
			zap.String("character_id", characterID),
			zap.String("implant_id", req.ImplantID))
	}

	// Record acquisition
	acquisition := &models.ImplantAcquisition{
		ID:              uuid.New().String(),
		CharacterID:     characterID,
		ImplantID:       req.ImplantID,
		AcquisitionType: string(req.PaymentMethod),
		Cost:            map[string]interface{}{"amount": implant.Cost},
		AcquiredAt:      time.Now(),
	}

	if err := s.repo.RecordImplantAcquisition(ctx, acquisition); err != nil {
		s.logger.Error("Failed to record implant acquisition", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to record acquisition")
		return
	}

	response := map[string]interface{}{
		"success":           true,
		"implant_id":        req.ImplantID,
		"transaction_id":    transactionID,
		"remaining_balance": remainingBalance,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CyberwareService) InstallImplantHandler(w http.ResponseWriter, r *http.Request) {
	if err := s.checkCircuitBreakerAndLoadShedding(); err != nil {
		s.respondError(w, http.StatusServiceUnavailable, "Service temporarily unavailable")
		return
	}
	defer s.releaseLoadShedding()

	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	characterID := chi.URLParam(r, "characterId")
	if characterID == "" {
		s.respondError(w, http.StatusBadRequest, "Character ID is required")
		return
	}

	var req models.InstallImplantRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Check if character owns the implant
	ownsImplant, err := s.repo.CharacterOwnsImplant(ctx, characterID, req.ImplantID)
	if err != nil {
		s.logger.Error("Failed to check implant ownership", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to check ownership")
		return
	}

	if !ownsImplant {
		s.respondError(w, http.StatusNotFound, "Character does not own this implant")
		return
	}

	// Check limits
	limits, err := s.repo.GetCharacterLimits(ctx, characterID)
	if err != nil {
		s.logger.Error("Failed to get character limits", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get limits")
		return
	}

	implant, err := s.repo.GetImplantByID(ctx, req.ImplantID)
	if err != nil {
		s.logger.Error("Failed to get implant data", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get implant data")
		return
	}

	// TODO: Implement energy, humanity, and slot validation
	// Check energy limits
	// if limits.TotalEnergyUsed+implant.EnergyCost > limits.MaxEnergy {
	// 	s.respondError(w, http.StatusBadRequest, "Insufficient energy capacity")
	// 	return
	// }

	// Check humanity limits
	// if limits.TotalHumanityLost+implant.HumanityCost > limits.MaxHumanity {
	// 	s.respondError(w, http.StatusBadRequest, "Humanity limit exceeded")
	// 	return
	// }

	// Check slot availability
	// if err := s.checkSlotAvailability(ctx, limits, req.Slot, implant.SlotType); err != nil {
	// 	s.respondError(w, http.StatusBadRequest, err.Error())
	// 	return
	// }

	// Install implant
	characterImplant := &models.CharacterImplant{
		ID:           uuid.New().String(),
		CharacterID:  characterID,
		ImplantID:    req.ImplantID,
		Name:         implant.Name,
		Type:         implant.Type,
		Category:     implant.Category,
		CurrentLevel: 1,
		MaxLevel:     implant.MaxLevel,
		Slot:         req.Slot,
		IsActive:     true,
		InstalledAt:  &time.Time{},
		Effects:      implant.Effects,
	}

	if err := s.repo.InstallImplant(ctx, characterImplant); err != nil {
		s.logger.Error("Failed to install implant", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to install implant")
		return
	}

	// Update cyberpsychosis
	cyberpsychosisIncrease := implant.HumanityCost
	if err := s.repo.UpdateCyberpsychosis(ctx, characterID, cyberpsychosisIncrease); err != nil {
		s.logger.Warn("Failed to update cyberpsychosis", zap.Error(err))
	}

	// Update limits
	if err := s.repo.UpdateLimits(ctx, characterID, implant.EnergyCost, implant.HumanityCost, req.Slot); err != nil {
		s.logger.Error("Failed to update limits", zap.Error(err))
		// Don't fail the request, just log the error
	}

	response := map[string]interface{}{
		"success":                 true,
		"implant_id":              req.ImplantID,
		"slot":                    req.Slot,
		"cyberpsychosis_increase": cyberpsychosisIncrease,
		"energy_used":             implant.EnergyCost,
		"humanity_lost":           implant.HumanityCost,
		"warnings":                []string{}, // Could include compatibility warnings
	}

	s.respondJSON(w, http.StatusOK, response)
}

// Helper functions
func (s *CyberwareService) checkCreditsBalance(characterID string, amount float64) (float64, error) {
	// This would integrate with economy service
	// For now, assume sufficient balance
	s.logger.Info("Checking credits balance",
		zap.String("character_id", characterID),
		zap.Float64("amount", amount))
	return 1000.0 - amount, nil // Mock balance
}

func (s *CyberwareService) checkSlotAvailability(limits *models.ImplantLimitsState, slotType string) error {
	// Check slot usage
	if slotsUsed, ok := limits.SlotsUsed.(map[string]interface{}); ok {
		if used, ok := slotsUsed[slotType].(float64); ok {
			maxSlots := 3 // Default max slots per type
			if int(used) >= maxSlots {
				return fmt.Errorf("no available slots for %s", slotType)
			}
		}
	}
	return nil
}

// UninstallImplantHandler Placeholder implementations for remaining handlers
func (s *CyberwareService) UninstallImplantHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"success": true, "message": "Uninstall not implemented yet"})
}

func (s *CyberwareService) UpgradeImplantHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"success": true, "message": "Upgrade not implemented yet"})
}

func (s *CyberwareService) GetImplantLimitsHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"message": "Limits not implemented yet"})
}

func (s *CyberwareService) CheckCompatibilityHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"compatible": true, "message": "Compatibility check not implemented yet"})
}

func (s *CyberwareService) GetCyberpsychosisStateHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"current_level": 0, "message": "Cyberpsychosis not implemented yet"})
}

func (s *CyberwareService) GetActiveSynergiesHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"synergies": []interface{}{}, "message": "Synergies not implemented yet"})
}

func (s *CyberwareService) GetImplantVisualsHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"implants": []interface{}{}, "message": "Visuals not implemented yet"})
}

func (s *CyberwareService) UpdateImplantVisualsHandler(w http.ResponseWriter, request *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{"success": true, "message": "Visuals update not implemented yet"})
}

// Response helper functions
func (s *CyberwareService) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *CyberwareService) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}
