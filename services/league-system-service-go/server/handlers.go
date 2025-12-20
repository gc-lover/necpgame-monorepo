// Package server Issue: #??? - HTTP handlers split from service.go
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/league-system-service-go/models"
	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// GetCurrentLeagueHandler handles GET /api/v1/league/current
func (s *LeagueService) GetCurrentLeagueHandler(w http.ResponseWriter, r *http.Request) {
	// Check circuit breaker and load shedding
	if err := s.checkCircuitBreakerAndLoadShedding(); err != nil {
		if err.Error() == "service overloaded" {
			s.respondError(w, http.StatusTooManyRequests, "Service temporarily overloaded")
		} else {
			s.respondError(w, http.StatusServiceUnavailable, "Circuit breaker open")
		}
		return
	}
	defer s.releaseLoadShedding()

	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	league, err := s.repo.GetCurrentLeague(ctx)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "No active league found")
			return
		}
		s.logger.Error("Failed to get current league", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get current league")
		return
	}

	s.respondJSON(w, http.StatusOK, league)
}

// GetLeagueStatisticsHandler handles GET /api/v1/league/{leagueId}/statistics
func (s *LeagueService) GetLeagueStatisticsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	leagueIDStr := chi.URLParam(r, "leagueId")
	leagueID, err := uuid.Parse(leagueIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid league ID")
		return
	}

	stats, err := s.repo.GetLeagueStatistics(ctx, leagueID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "League statistics not found")
			return
		}
		s.logger.Error("Failed to get league statistics", zap.Error(err), zap.String("league_id", leagueID.String()))
		s.respondError(w, http.StatusInternalServerError, "Failed to get league statistics")
		return
	}

	s.respondJSON(w, http.StatusOK, stats)
}

// GetLeagueCountdownHandler handles GET /api/v1/league/countdown
func (s *LeagueService) GetLeagueCountdownHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	countdown, err := s.calculateLeagueCountdown(ctx)
	if err != nil {
		s.logger.Error("Failed to calculate league countdown", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get league countdown")
		return
	}

	s.respondJSON(w, http.StatusOK, countdown)
}

// GetLeaguePhasesHandler handles GET /api/v1/league/phases
func (s *LeagueService) GetLeaguePhasesHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	phases, err := s.getLeaguePhases(ctx)
	if err != nil {
		s.logger.Error("Failed to get league phases", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get league phases")
		return
	}

	s.respondJSON(w, http.StatusOK, phases)
}

// RegisterForEndEventHandler handles POST /api/v1/league/end-event/register
func (s *LeagueService) RegisterForEndEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	// Extract user ID from context (set by JWT middleware)
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "User not authenticated")
		return
	}

	userUUID, err := uuid.Parse(userID)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid user ID")
		return
	}

	// Get current league
	league, err := s.repo.GetCurrentLeague(ctx)
	if err != nil {
		s.logger.Error("Failed to get current league for end event registration", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to register for end event")
		return
	}

	// Check if league is in endgame phase
	if league.Phase.Name != "ENDGAME" && league.Phase.Name != "FINALE" {
		s.respondError(w, http.StatusBadRequest, "End event registration is only available during ENDGAME or FINALE phases")
		return
	}

	// Register player for end event
	err = s.repo.RegisterPlayerForEndEvent(ctx, userUUID, league.ID)
	if err != nil {
		s.logger.Error("Failed to register player for end event", zap.Error(err), zap.String("user_id", userID), zap.String("league_id", league.ID.String()))
		s.respondError(w, http.StatusInternalServerError, "Failed to register for end event")
		return
	}

	eventTime := league.EndDate
	response := map[string]interface{}{
		"message":    "Successfully registered for end event",
		"event_time": eventTime.Format(time.RFC3339),
	}

	s.respondJSON(w, http.StatusOK, response)
}

// TriggerLeagueResetHandler handles POST /api/v1/league/reset/trigger (Admin only)
func (s *LeagueService) TriggerLeagueResetHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	// Check admin role (this should be extracted from JWT token)
	userClaims := r.Context().Value("user_claims")
	if userClaims == nil {
		s.respondError(w, http.StatusUnauthorized, "Authentication required")
		return
	}

	// Simple role check - in production this would be more sophisticated
	claims, ok := userClaims.(map[string]interface{})
	if !ok || claims["role"] != "admin" {
		s.respondError(w, http.StatusForbidden, "Admin access required")
		return
	}

	// Get current league
	league, err := s.repo.GetCurrentLeague(ctx)
	if err != nil {
		s.logger.Error("Failed to get current league for reset", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to trigger league reset")
		return
	}

	// Check if league can be reset
	if league.Status != models.LeagueStatusActive && league.Status != models.LeagueStatusFinishing {
		s.respondError(w, http.StatusBadRequest, "League must be ACTIVE or FINISHING to trigger reset")
		return
	}

	// Schedule reset for end time
	resetTime := league.EndDate

	// Update league status to FINISHING
	league.Status = models.LeagueStatusFinishing
	err = s.repo.UpdateLeagueStatus(ctx, league.ID, league.Status)
	if err != nil {
		s.logger.Error("Failed to update league status to finishing", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to trigger league reset")
		return
	}

	// Schedule actual reset (in production, this would use a job queue)
	go func() {
		time.Sleep(time.Until(resetTime))
		s.performLeagueReset(context.Background(), league.ID)
	}()

	response := map[string]interface{}{
		"message":    "League reset scheduled",
		"reset_time": resetTime.Format(time.RFC3339),
	}

	s.respondJSON(w, http.StatusAccepted, response)
}

// GetPlayerLegacyProgressHandler handles GET /api/v1/player/legacy-progress
func (s *LeagueService) GetPlayerLegacyProgressHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Extract user ID from context
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "User not authenticated")
		return
	}

	userUUID, err := uuid.Parse(userID)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid user ID")
		return
	}

	progress, err := s.repo.GetPlayerLegacyProgress(ctx, userUUID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Return default progress for new players
			defaultProgress := &models.PlayerLegacyProgress{
				PlayerID:     userUUID,
				LegacyPoints: 0,
				Titles:       []models.Title{},
				Cosmetics:    []models.Cosmetic{},
				LegacyItems:  []models.LegacyItem{},
				GlobalRating: 1000.0, // Default MMR
				Achievements: []string{},
			}
			s.respondJSON(w, http.StatusOK, defaultProgress)
			return
		}
		s.logger.Error("Failed to get player legacy progress", zap.Error(err), zap.String("user_id", userID))
		s.respondError(w, http.StatusInternalServerError, "Failed to get legacy progress")
		return
	}

	s.respondJSON(w, http.StatusOK, progress)
}

// GetHallOfFameHandler handles GET /api/v1/league/hall-of-fame
func (s *LeagueService) GetHallOfFameHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	// Parse query parameters
	leagueIDStr := r.URL.Query().Get("league_id")
	categoryStr := r.URL.Query().Get("category")
	limitStr := r.URL.Query().Get("limit")

	var leagueID *uuid.UUID
	if leagueIDStr != "" {
		id, err := uuid.Parse(leagueIDStr)
		if err != nil {
			s.respondError(w, http.StatusBadRequest, "Invalid league ID")
			return
		}
		leagueID = &id
	}

	category := models.CategoryStoryCompletion // default
	if categoryStr != "" {
		switch categoryStr {
		case "ECONOMY":
			category = models.CategoryEconomy
		case "PVP":
			category = models.CategoryPVP
		case "ALTERNATIVE_MODES":
			category = models.CategoryAlternativeModes
		case "ALL":
			category = "ALL" // Special case
		}
	}

	limit := 50 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	hallOfFame, err := s.repo.GetHallOfFame(ctx, leagueID, category, limit)
	if err != nil {
		s.logger.Error("Failed to get hall of fame", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get hall of fame")
		return
	}

	s.respondJSON(w, http.StatusOK, hallOfFame)
}

// GetLegacyShopItemsHandler handles GET /api/v1/league/legacy-shop/items
func (s *LeagueService) GetLegacyShopItemsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	items, err := s.repo.GetLegacyShopItems(ctx)
	if err != nil {
		s.logger.Error("Failed to get legacy shop items", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get legacy shop items")
		return
	}

	s.respondJSON(w, http.StatusOK, items)
}

// PurchaseLegacyItemHandler handles POST /api/v1/league/legacy-shop/purchase
func (s *LeagueService) PurchaseLegacyItemHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	// Extract user ID from context
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		s.respondError(w, http.StatusUnauthorized, "User not authenticated")
		return
	}

	userUUID, err := uuid.Parse(userID)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid user ID")
		return
	}

	var req struct {
		ItemID uuid.UUID `json:"item_id"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Get item details
	item, err := s.repo.GetLegacyShopItem(ctx, req.ItemID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "Item not found")
			return
		}
		s.logger.Error("Failed to get legacy shop item", zap.Error(err), zap.String("item_id", req.ItemID.String()))
		s.respondError(w, http.StatusInternalServerError, "Failed to purchase item")
		return
	}

	if !item.Available {
		s.respondError(w, http.StatusBadRequest, "Item is not available")
		return
	}

	// Get player progress
	progress, err := s.repo.GetPlayerLegacyProgress(ctx, userUUID)
	if err != nil && err != sql.ErrNoRows {
		s.logger.Error("Failed to get player progress for purchase", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to purchase item")
		return
	}

	// Check if player has enough points
	availablePoints := 0
	if progress != nil {
		availablePoints = progress.LegacyPoints
	}

	if availablePoints < item.Cost {
		s.respondJSON(w, http.StatusPaymentRequired, map[string]interface{}{
			"error":            "Insufficient Legacy Points",
			"required_points":  item.Cost,
			"available_points": availablePoints,
		})
		return
	}

	// Process purchase
	err = s.repo.PurchaseLegacyItem(ctx, userUUID, req.ItemID, item.Cost)
	if err != nil {
		s.logger.Error("Failed to process legacy item purchase", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to purchase item")
		return
	}

	response := map[string]interface{}{
		"message":                 "Item purchased successfully",
		"item":                    item,
		"legacy_points_remaining": availablePoints - item.Cost,
	}

	s.respondJSON(w, http.StatusOK, response)
}
