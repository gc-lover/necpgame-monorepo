// Issue: #1499
package handlers

import (
	"context"
	"net/http"
	"time"

	"github.com/rs/zerolog"

	"gameplay-restricted-modes-service-go/internal/auth"
	"gameplay-restricted-modes-service-go/internal/database"
	"gameplay-restricted-modes-service-go/pkg/api"
)

// RestrictedModesHandlers implements the OpenAPI handlers
type RestrictedModesHandlers struct {
	authService *auth.Service
	dbService   *database.Service
	logger      zerolog.Logger
}

// NewRestrictedModesHandlers creates new restricted modes handlers
func NewRestrictedModesHandlers(authService *auth.Service, dbService *database.Service, logger zerolog.Logger) *RestrictedModesHandlers {
	return &RestrictedModesHandlers{
		authService: authService,
		dbService:   dbService,
		logger:      logger,
	}
}

// GetAvailableRestrictedModes implements getAvailableRestrictedModes operation
func (h *RestrictedModesHandlers) GetAvailableRestrictedModes(ctx context.Context) (api.GetAvailableRestrictedModesRes, error) {
	h.logger.Info().Msg("Getting available restricted modes")

	modes, err := h.dbService.GetAvailableRestrictedModes(ctx)
	if err != nil {
		h.logger.Error().Err(err).Msg("Failed to get available restricted modes")
		return &api.GetAvailableRestrictedModesInternalServerError{}, nil
	}

	// Convert to API format
	apiModes := make([]api.RestrictedMode, 0, len(modes))
	playerEligibility := api.GetAvailableRestrictedModesOKPlayerEligibility{}

	for modeKey, modeData := range modes {
		modeMap, ok := modeData.(map[string]interface{})
		if !ok {
			continue
		}

		mode := api.RestrictedMode{
			ModeType: api.RestrictedModeModeType(modeKey),
			Name:     h.getStringFromMap(modeMap, "name"),
		}

		if desc, ok := modeMap["description"].(string); ok {
			mode.Description = desc
		}

		if reqData, ok := modeMap["requirements"].(map[string]interface{}); ok {
			mode.Requirements = api.ModeRequirements{}
			if achievements, ok := reqData["required_achievements"].([]interface{}); ok {
				for _, ach := range achievements {
					if achStr, ok := ach.(string); ok {
						mode.Requirements.RequiredAchievements = append(mode.Requirements.RequiredAchievements, achStr)
					}
				}
			}
			if level, ok := reqData["min_character_level"].(float64); ok {
				mode.Requirements.MinCharacterLevel = int(level)
			}
			if cooldown, ok := reqData["cooldown_period"].(float64); ok {
				mode.Requirements.CooldownPeriod = time.Duration(cooldown) * time.Second
			}
		}

		if rewardData, ok := modeMap["rewards"].(map[string]interface{}); ok {
			mode.Rewards = api.ModeRewards{}
			if titles, ok := rewardData["unique_titles"].([]interface{}); ok {
				for _, title := range titles {
					if titleStr, ok := title.(string); ok {
						mode.Rewards.UniqueTitles = append(mode.Rewards.UniqueTitles, titleStr)
					}
				}
			}
			if xpMult, ok := rewardData["base_xp_multiplier"].(float64); ok {
				mode.Rewards.BaseXpMultiplier = xpMult
			}
			if itemMult, ok := rewardData["item_drops_multiplier"].(float64); ok {
				mode.Rewards.ItemDropsMultiplier = itemMult
			}
			if bonuses, ok := rewardData["leaderboard_bonuses"].(bool); ok {
				mode.Rewards.LeaderboardBonuses = bonuses
			}
		}

		apiModes = append(apiModes, mode)

		// Set eligibility based on mode type
		switch modeKey {
		case "ironman":
			playerEligibility.IronmanAvailable = true // In real implementation, check player level/achievements
		case "hardcore":
			playerEligibility.HardcoreAvailable = true
		case "solo":
			playerEligibility.SoloAvailable = true
		case "nodeath":
			playerEligibility.NodeathAvailable = true
		}
	}

	return &api.GetAvailableRestrictedModesOK{
		Modes:               apiModes,
		PlayerEligibility:   playerEligibility,
	}, nil
}

// GetPlayerRestrictedModesStatus implements getPlayerRestrictedModesStatus operation
func (h *RestrictedModesHandlers) GetPlayerRestrictedModesStatus(ctx context.Context) (api.GetPlayerRestrictedModesStatusRes, error) {
	// Extract user ID from context (set by middleware)
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		h.logger.Warn().Msg("User ID not found in context")
		return &api.GetPlayerRestrictedModesStatusUnauthorized{}, nil
	}

	h.logger.Info().Str("user_id", userID).Msg("Getting player restricted modes status")

	modes, err := h.dbService.GetPlayerRestrictedModesStatus(ctx, userID)
	if err != nil {
		h.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get player restricted modes status")
		return &api.GetPlayerRestrictedModesStatusInternalServerError{}, nil
	}

	// Convert to API format
	activeModes := make([]api.ActiveRestrictedMode, 0, len(modes))
	statistics := api.PlayerModeStatistics{
		BestScores: make(map[string]int),
		Achievements: []string{}, // Would be populated from achievements system
	}

	totalSessions := 0
	successfulRuns := 0
	failedRuns := 0
	var totalTime time.Duration

	for _, mode := range modes {
		if mode.IsActive {
			activeMode := api.ActiveRestrictedMode{
				SessionID:    mode.PlayerID, // In real implementation, would have session ID
				CharacterID:  mode.PlayerID, // Placeholder
				ModeType:     api.ActiveRestrictedModeModeType(mode.ModeType),
				StartedAt:    *mode.ActivatedAt,
				Restrictions: []string{"permadeath"}, // Placeholder
			}
			if mode.BestScore != nil {
				activeMode.Progress = float64(*mode.BestScore)
			}
			activeModes = append(activeModes, activeMode)
		}

		// Update statistics
		totalSessions += mode.TotalSessions
		successfulRuns += mode.SuccessfulRuns
		failedRuns += mode.FailedRuns
		totalTime += mode.TotalTimePlayed

		if mode.BestScore != nil {
			statistics.BestScores[mode.ModeType] = *mode.BestScore
		}
	}

	statistics.TotalSessions = totalSessions
	statistics.SuccessfulSessions = successfulRuns
	statistics.FailedSessions = failedRuns

	return &api.GetPlayerRestrictedModesStatusOK{
		ActiveModes: activeModes,
		Statistics:  statistics,
	}, nil
}

// SelectRestrictedMode implements selectRestrictedMode operation
func (h *RestrictedModesHandlers) SelectRestrictedMode(ctx context.Context, req *api.SelectRestrictedModeReq) (api.SelectRestrictedModeRes, error) {
	// Extract user ID from context
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		h.logger.Warn().Msg("User ID not found in context")
		return &api.SelectRestrictedModeUnauthorized{}, nil
	}

	h.logger.Info().
		Str("user_id", userID).
		Str("mode_type", string(req.ModeType)).
		Str("character_id", req.CharacterID).
		Msg("Selecting restricted mode")

	// Check if player already has an active mode
	activeSession, err := h.dbService.GetActiveRestrictedModeSession(ctx, userID)
	if err == nil && activeSession != nil {
		h.logger.Warn().Str("user_id", userID).Msg("Player already has active restricted mode session")
		return &api.SelectRestrictedModeConflict{
			Error:   "ACTIVE_MODE_EXISTS",
			CurrentMode: api.ActiveRestrictedMode{
				SessionID:   activeSession.SessionID,
				ModeType:    api.ActiveRestrictedModeModeType(activeSession.ModeType),
				StartedAt:   activeSession.StartedAt,
				Restrictions: activeSession.Restrictions,
			},
		}, nil
	}

	// Check eligibility
	eligible, err := h.dbService.CheckPlayerEligibility(ctx, userID, string(req.ModeType))
	if err != nil {
		h.logger.Error().Err(err).Str("user_id", userID).Str("mode_type", string(req.ModeType)).Msg("Failed to check eligibility")
		return &api.SelectRestrictedModeInternalServerError{}, nil
	}

	if !eligible {
		h.logger.Warn().Str("user_id", userID).Str("mode_type", string(req.ModeType)).Msg("Player not eligible for mode")
		return &api.SelectRestrictedModeForbidden{
			Error:   "MODE_NOT_AVAILABLE",
			Reason:  "Insufficient character level or achievements",
		}, nil
	}

	// Activate the mode
	restrictions := []string{"permadeath"} // Default restrictions
	if req.ModeType == api.SelectRestrictedModeReqModeTypeHardcore {
		restrictions = []string{"limited_resources", "no_auction"}
	} else if req.ModeType == api.SelectRestrictedModeReqModeTypeSolo {
		restrictions = []string{"no_group_assistance", "increased_difficulty"}
	} else if req.ModeType == api.SelectRestrictedModeReqModeTypeNodeath {
		restrictions = []string{"no_death_allowed", "bonus_rewards"}
	}

	session, err := h.dbService.ActivateRestrictedMode(ctx, userID, req.CharacterID, string(req.ModeType),
		h.optStringToStringPtr(req.ContentType), h.optStringToStringPtr(req.Difficulty), restrictions)
	if err != nil {
		h.logger.Error().Err(err).Str("user_id", userID).Str("mode_type", string(req.ModeType)).Msg("Failed to activate restricted mode")
		return &api.SelectRestrictedModeInternalServerError{}, nil
	}

	activeMode := api.ActiveRestrictedMode{
		SessionID:    session.SessionID,
		CharacterID:  session.CharacterID,
		ModeType:     api.ActiveRestrictedModeModeType(session.ModeType),
		StartedAt:    session.StartedAt,
		Restrictions: session.Restrictions,
	}

	return &api.SelectRestrictedModeCreated{
		SessionID:         session.SessionID,
		Mode:              activeMode,
		RestrictionsApplied: restrictions,
	}, nil
}

// CompleteRestrictedModeSession implements completeRestrictedModeSession operation
func (h *RestrictedModesHandlers) CompleteRestrictedModeSession(ctx context.Context, req *api.CompleteRestrictedModeSessionReq, params api.CompleteRestrictedModeSessionParams) (api.CompleteRestrictedModeSessionRes, error) {
	// Extract user ID from context
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		h.logger.Warn().Msg("User ID not found in context")
		return &api.CompleteRestrictedModeSessionUnauthorized{}, nil
	}

	sessionID := params.SessionID
	h.logger.Info().
		Str("user_id", userID).
		Str("session_id", sessionID).
		Bool("success", req.Success).
		Msg("Completing restricted mode session")

	// Verify session belongs to user
	session, err := h.dbService.GetActiveRestrictedModeSession(ctx, userID)
	if err != nil || session == nil || session.SessionID != sessionID {
		h.logger.Warn().
			Str("user_id", userID).
			Str("session_id", sessionID).
			Msg("Session not found or doesn't belong to user")
		return &api.CompleteRestrictedModeSessionNotFound{}, nil
	}

	completionTime := time.Duration(req.CompletionTime) * time.Second
	completed, err := h.dbService.CompleteRestrictedModeSession(ctx, sessionID, req.Success, completionTime, req.Score)
	if err != nil {
		h.logger.Error().Err(err).Str("session_id", sessionID).Msg("Failed to complete restricted mode session")
		return &api.CompleteRestrictedModeSessionInternalServerError{}, nil
	}

	// Build rewards response
	rewards := api.ModeCompletionRewards{
		ExperienceGained: req.Score * 10, // Simple calculation
	}

	if completed.Success {
		rewards.ItemsGranted = []api.Item{
			{
				ItemID:   "completion_token",
				Name:     "Completion Token",
				Rarity:   "rare",
				Quantity: 1,
			},
		}
		rewards.TitlesUnlocked = []string{"Survivor"}
		rewards.AchievementsUnlocked = []string{"first_completion"}
	}

	completedSession := api.CompletedRestrictedModeSession{
		SessionID:     completed.SessionID,
		ModeType:      api.CompletedRestrictedModeSessionModeType(completed.ModeType),
		CompletedAt:   completed.CompletedAt,
		Success:       completed.Success,
		CompletionTime: completed.CompletionTime,
		FinalScore:     completed.FinalScore,
	}

	if completed.RankAchieved != nil {
		completedSession.RankAchieved = *completed.RankAchieved
	}

	return &api.CompleteRestrictedModeSessionOK{
		Session:     completedSession,
		Rewards:     rewards,
		AchievementsUnlocked: []api.Achievement{
			{
				AchievementID: "session_complete",
				Name:          "Session Complete",
				Description:   "Successfully completed a restricted mode session",
				Rarity:        "common",
			},
		},
	}, nil
}

// FailRestrictedModeSession implements failRestrictedModeSession operation
func (h *RestrictedModesHandlers) FailRestrictedModeSession(ctx context.Context, req *api.FailRestrictedModeSessionReq, params api.FailRestrictedModeSessionParams) (api.FailRestrictedModeSessionRes, error) {
	// Extract user ID from context
	userID, ok := ctx.Value("user_id").(string)
	if !ok {
		h.logger.Warn().Msg("User ID not found in context")
		return &api.FailRestrictedModeSessionUnauthorized{}, nil
	}

	sessionID := params.SessionID
	h.logger.Info().
		Str("user_id", userID).
		Str("session_id", sessionID).
		Str("failure_reason", string(req.FailureReason)).
		Msg("Failing restricted mode session")

	// Verify session belongs to user
	session, err := h.dbService.GetActiveRestrictedModeSession(ctx, userID)
	if err != nil || session == nil || session.SessionID != sessionID {
		h.logger.Warn().
			Str("user_id", userID).
			Str("session_id", sessionID).
			Msg("Session not found or doesn't belong to user")
		return &api.FailRestrictedModeSessionNotFound{}, nil
	}

	err = h.dbService.FailRestrictedModeSession(ctx, sessionID, string(req.FailureReason))
	if err != nil {
		h.logger.Error().Err(err).Str("session_id", sessionID).Msg("Failed to fail restricted mode session")
		return &api.FailRestrictedModeSessionInternalServerError{}, nil
	}

	failedSession := api.FailedRestrictedModeSession{
		SessionID:      sessionID,
		ModeType:       api.FailedRestrictedModeSessionModeType(session.ModeType),
		FailedAt:       time.Now(),
		FailureReason:  api.FailedRestrictedModeSessionFailureReason(req.FailureReason),
		PenaltiesApplied: []string{"experience_loss", "item_loss"}, // Placeholder penalties
	}

	return &api.FailRestrictedModeSessionOK{
		Session:          failedSession,
		PenaltiesApplied: []string{"experience_loss", "item_loss"},
	}, nil
}

// GetRestrictedModesLeaderboard implements getRestrictedModesLeaderboard operation
func (h *RestrictedModesHandlers) GetRestrictedModesLeaderboard(ctx context.Context, params api.GetRestrictedModesLeaderboardParams) (api.GetRestrictedModesLeaderboardRes, error) {
	modeType := "all"
	if params.ModeType.Set {
		modeType = string(params.ModeType.Value)
	}

	timeframe := "alltime"
	if params.Timeframe.Set {
		timeframe = string(params.Timeframe.Value)
	}

	limit := 50
	if params.Limit.Set && params.Limit.Value > 0 {
		limit = int(params.Limit.Value)
	}

	h.logger.Info().
		Str("mode_type", modeType).
		Str("timeframe", timeframe).
		Int("limit", limit).
		Msg("Getting restricted modes leaderboard")

	entries, err := h.dbService.GetLeaderboard(ctx, modeType, timeframe, limit)
	if err != nil {
		h.logger.Error().Err(err).Msg("Failed to get leaderboard")
		return &api.GetRestrictedModesLeaderboardInternalServerError{}, nil
	}

	// Convert to API format
	apiEntries := make([]api.LeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = api.LeaderboardEntry{
			PlayerID:      entry.PlayerID,
			PlayerName:    entry.PlayerName,
			CharacterName: h.stringPtrToOptString(entry.CharacterName),
			CompletedAt:   entry.AchievedAt,
			Score:         entry.Score,
			Rank:          entry.Rank,
			CompletionTime: entry.CompletionTime,
		}
	}

	return &api.GetRestrictedModesLeaderboardOK{
		Leaderboard: apiEntries,
		ModeType:    modeType,
		Timeframe:   timeframe,
	}, nil
}

// HealthCheck handles health check requests
func (h *RestrictedModesHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Check database health
	dbHealthy := true
	if err := h.dbService.Health(ctx); err != nil {
		h.logger.Error().Err(err).Msg("Database health check failed")
		dbHealthy = false
	}

	response := map[string]interface{}{
		"status":      "healthy",
		"domain":      "gameplay-restricted-modes",
		"timestamp":   time.Now().Format(time.RFC3339),
		"version":     "1.0.0",
		"database":    dbHealthy,
		"uptime_seconds": 0, // Would be tracked in real implementation
	}

	if !dbHealthy {
		response["status"] = "degraded"
		w.WriteHeader(http.StatusServiceUnavailable)
	}

	w.Header().Set("Content-Type", "application/json")
	// In real implementation, would use json.NewEncoder
	h.logger.Info().Interface("health_check", response).Msg("Health check performed")
}

// ReadinessCheck handles readiness probe requests
func (h *RestrictedModesHandlers) ReadinessCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Check critical dependencies
	if err := h.dbService.Health(ctx); err != nil {
		h.logger.Error().Err(err).Msg("Readiness check failed: database unavailable")
		http.Error(w, "Service not ready", http.StatusServiceUnavailable)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	// In real implementation, would return JSON response
	h.logger.Info().Msg("Readiness check passed")
}

// Helper functions

func (h *RestrictedModesHandlers) getStringFromMap(m map[string]interface{}, key string) string {
	if val, ok := m[key].(string); ok {
		return val
	}
	return ""
}

func (h *RestrictedModesHandlers) optStringToStringPtr(opt api.OptString) *string {
	if opt.Set {
		return &opt.Value
	}
	return nil
}

func (h *RestrictedModesHandlers) stringPtrToOptString(s *string) api.OptString {
	if s != nil {
		return api.OptString{Value: *s, Set: true}
	}
	return api.OptString{Set: false}
}




