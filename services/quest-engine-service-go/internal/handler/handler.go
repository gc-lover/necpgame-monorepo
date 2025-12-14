package handler

import (
	"fmt"
	"net/http"
	"time"

	"necpgame/services/quest-engine-service-go/internal/service"
)

type Handler struct {
	questService *service.QuestService
}

func NewHandler(questService *service.QuestService) *Handler {
	return &Handler{questService: questService}
}

func (h *Handler) Router() http.Handler {
	mux := http.NewServeMux()
	mux.HandleFunc("/health", h.handleHealth)
	mux.HandleFunc("/quests", h.handleGetActiveQuests)
	return mux
}

func (h *Handler) handleHealth(w http.ResponseWriter, r *http.Request) {
	telemetry := h.questService.GetTelemetry()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"status": "healthy",
		"timestamp": "%s",
		"active_quests": %d
	}`, time.Now().Format(time.RFC3339), telemetry.ActiveQuests)
}

func (h *Handler) handleGetActiveQuests(w http.ResponseWriter, r *http.Request) {
	playerID := r.URL.Query().Get("player_id")
	if playerID == "" {
		http.Error(w, "player_id required", http.StatusBadRequest)
		return
	}

	quests, err := h.questService.GetActiveQuests(r.Context(), playerID)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to get quests: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	fmt.Fprintf(w, `{"quests":[`)
	for i, quest := range quests {
		if i > 0 {
			fmt.Fprintf(w, ",")
		}
		fmt.Fprintf(w, `{
			"quest_id": "%s",
			"quest_type": "%s",
			"status": "%s",
			"progress_percentage": %.1f
		}`, quest.ID, quest.Type, quest.Status, quest.Progress)
	}
	fmt.Fprintf(w, `],"total_count": %d}`, len(quests))
}
