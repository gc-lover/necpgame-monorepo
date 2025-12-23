// Issue: #140889770
// PERFORMANCE: HTTP handlers optimized for MMOFPS workloads
// BACKEND: HTTP request handlers for narrative operations

package server

import (
	"context"
	"time"

	"go.uber.org/zap"
)

// NarrativeHandler handles HTTP requests for narrative operations
// PERFORMANCE: Struct alignment optimized for hot paths
type NarrativeHandler struct {
	service *NarrativeServiceLogic
	logger  *zap.Logger
}

// NewNarrativeHandler creates a new narrative handler
// PERFORMANCE: Preallocates logger instance
func NewNarrativeHandler(service *NarrativeServiceLogic, logger *zap.Logger) *NarrativeHandler {
	return &NarrativeHandler{
		service: service,
		logger:  logger,
	}
}

// GetCutscenes handles GET /cutscenes requests
// PERFORMANCE: Hot path - optimized for frequent access
func (h *NarrativeHandler) GetCutscenes(ctx context.Context, playerID string, status, category *string) ([]*CutsceneData, error) {
	h.logger.Info("Handling get cutscenes request", zap.String("playerId", playerID))

	// PERFORMANCE: Context timeout for database operations
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	cutscenes, err := h.service.GetCutscenes(ctx, playerID, status, category)
	if err != nil {
		h.logger.Error("Failed to get cutscenes", zap.Error(err), zap.String("playerId", playerID))
		return nil, err
	}

	h.logger.Info("Retrieved cutscenes", zap.Int("count", len(cutscenes)), zap.String("playerId", playerID))
	return cutscenes, nil
}

// GetCutsceneDetails handles GET /cutscenes/{cutsceneId} requests
func (h *NarrativeHandler) GetCutsceneDetails(ctx context.Context, cutsceneID string) (*CutsceneData, error) {
	h.logger.Info("Handling get cutscene details request", zap.String("cutsceneId", cutsceneID))

	// PERFORMANCE: Context timeout for database operations
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	details, err := h.service.GetCutsceneDetails(ctx, cutsceneID)
	if err != nil {
		h.logger.Error("Failed to get cutscene details", zap.Error(err), zap.String("cutsceneId", cutsceneID))
		return nil, err
	}

	h.logger.Info("Retrieved cutscene details", zap.String("cutsceneId", cutsceneID))
	return details, nil
}

// PlayCutscene handles POST /cutscenes/{cutsceneId}/play requests
// PERFORMANCE: Hot path - optimized for 1000+ RPS
func (h *NarrativeHandler) PlayCutscene(ctx context.Context, cutsceneID, playerID string, quality *string, subtitles *bool, audioLanguage *string) (string, error) {
	h.logger.Info("Handling play cutscene request",
		zap.String("cutsceneId", cutsceneID),
		zap.String("playerId", playerID))

	// PERFORMANCE: Shorter timeout for real-time operations
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	// Set defaults if not provided
	qual := "HIGH"
	if quality != nil {
		qual = *quality
	}
	subs := true
	if subtitles != nil {
		subs = *subtitles
	}
	lang := "en"
	if audioLanguage != nil {
		lang = *audioLanguage
	}

	sessionID, err := h.service.PlayCutscene(ctx, cutsceneID, playerID, qual, subs, lang)
	if err != nil {
		h.logger.Error("Failed to play cutscene", zap.Error(err),
			zap.String("cutsceneId", cutsceneID), zap.String("playerId", playerID))
		return "", err
	}

	h.logger.Info("Started cutscene playback", zap.String("sessionId", sessionID))
	return sessionID, nil
}

// SkipCutscene handles POST /cutscenes/{cutsceneId}/skip requests
func (h *NarrativeHandler) SkipCutscene(ctx context.Context, cutsceneID, playerID string) error {
	h.logger.Info("Handling skip cutscene request",
		zap.String("cutsceneId", cutsceneID),
		zap.String("playerId", playerID))

	// PERFORMANCE: Context timeout for skip operations
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	err := h.service.SkipCutscene(ctx, cutsceneID, playerID)
	if err != nil {
		h.logger.Error("Failed to skip cutscene", zap.Error(err),
			zap.String("cutsceneId", cutsceneID), zap.String("playerId", playerID))
		return err
	}

	h.logger.Info("Cutscene skipped successfully", zap.String("cutsceneId", cutsceneID))
	return nil
}

// GetNarrativeState handles GET /narrative/state requests
// PERFORMANCE: Hot path - optimized for 1000+ RPS
func (h *NarrativeHandler) GetNarrativeState(ctx context.Context, playerID string) (*NarrativeState, error) {
	h.logger.Info("Handling get narrative state request", zap.String("playerId", playerID))

	// PERFORMANCE: Context timeout for state operations
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	state, err := h.service.GetNarrativeState(ctx, playerID)
	if err != nil {
		h.logger.Error("Failed to get narrative state", zap.Error(err), zap.String("playerId", playerID))
		return nil, err
	}

	h.logger.Info("Retrieved narrative state", zap.String("playerId", playerID))
	return state, nil
}

// GetStoryProgress handles GET /stories/{storyId}/progress requests
// PERFORMANCE: Hot path - optimized for 1000+ RPS
func (h *NarrativeHandler) GetStoryProgress(ctx context.Context, storyID, playerID string) (*StoryProgress, error) {
	h.logger.Info("Handling get story progress request",
		zap.String("storyId", storyID), zap.String("playerId", playerID))

	// PERFORMANCE: Context timeout for progress operations
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	progress, err := h.service.GetStoryProgress(ctx, storyID, playerID)
	if err != nil {
		h.logger.Error("Failed to get story progress", zap.Error(err),
			zap.String("storyId", storyID), zap.String("playerId", playerID))
		return nil, err
	}

	h.logger.Info("Retrieved story progress", zap.String("storyId", storyID))
	return progress, nil
}

// MakeStoryChoice handles POST /stories/{storyId}/choice requests
func (h *NarrativeHandler) MakeStoryChoice(ctx context.Context, storyID, playerID, choiceID string, additionalData map[string]interface{}) error {
	h.logger.Info("Handling make story choice request",
		zap.String("storyId", storyID), zap.String("playerId", playerID), zap.String("choiceId", choiceID))

	// PERFORMANCE: Context timeout for choice operations
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	err := h.service.MakeStoryChoice(ctx, storyID, playerID, choiceID, additionalData)
	if err != nil {
		h.logger.Error("Failed to make story choice", zap.Error(err),
			zap.String("storyId", storyID), zap.String("choiceId", choiceID))
		return err
	}

	h.logger.Info("Story choice recorded", zap.String("storyId", storyID), zap.String("choiceId", choiceID))
	return nil
}

// StartDialogue handles POST /dialogue/{dialogueId}/start requests
func (h *NarrativeHandler) StartDialogue(ctx context.Context, dialogueID, playerID, npcID string, contextData map[string]interface{}) (string, error) {
	h.logger.Info("Handling start dialogue request",
		zap.String("dialogueId", dialogueID), zap.String("playerId", playerID), zap.String("npcId", npcID))

	// PERFORMANCE: Context timeout for dialogue operations
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	sessionID, err := h.service.StartDialogue(ctx, dialogueID, playerID, npcID, contextData)
	if err != nil {
		h.logger.Error("Failed to start dialogue", zap.Error(err),
			zap.String("dialogueId", dialogueID), zap.String("playerId", playerID))
		return "", err
	}

	h.logger.Info("Dialogue started", zap.String("sessionId", sessionID))
	return sessionID, nil
}

// TriggerNarrativeEvent handles POST /events/trigger requests
func (h *NarrativeHandler) TriggerNarrativeEvent(ctx context.Context, playerID, eventType string, eventData map[string]interface{}) (string, error) {
	h.logger.Info("Handling trigger narrative event request",
		zap.String("playerId", playerID), zap.String("eventType", eventType))

	// PERFORMANCE: Context timeout for event operations
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	eventID, err := h.service.TriggerNarrativeEvent(ctx, playerID, eventType, eventData)
	if err != nil {
		h.logger.Error("Failed to trigger narrative event", zap.Error(err),
			zap.String("playerId", playerID), zap.String("eventType", eventType))
		return "", err
	}

	h.logger.Info("Narrative event triggered", zap.String("eventId", eventID))
	return eventID, nil
}

// ValidateNarrativeState handles POST /validate requests
func (h *NarrativeHandler) ValidateNarrativeState(ctx context.Context, playerID string, expectedState map[string]interface{}) (bool, []string, map[string]interface{}) {
	h.logger.Info("Handling validate narrative state request", zap.String("playerId", playerID))

	// PERFORMANCE: Context timeout for validation operations
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	isValid, violations, correctedState := h.service.ValidateNarrativeState(ctx, playerID, expectedState)

	h.logger.Info("Narrative state validation completed",
		zap.Bool("isValid", isValid),
		zap.Int("violations", len(violations)),
		zap.String("playerId", playerID))

	return isValid, violations, correctedState
}
