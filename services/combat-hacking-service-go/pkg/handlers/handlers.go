package handlers

import (
	"encoding/json"
	"log"
	"net/http"

	"combat-hacking-service-go/pkg/models"
	"combat-hacking-service-go/pkg/repository"
)

type Handlers struct {
	repo   *repository.Repository
	logger *log.Logger
}

func NewHandlers(repo *repository.Repository, logger *log.Logger) *Handlers {
	return &Handlers{
		repo:   repo,
		logger: logger,
	}
}

func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]string{"status": "healthy"})
}

// ActivateScreenHackBlind handles screen hack blind skill activation
// Issue: #143875347
func (h *Handlers) ActivateScreenHackBlind(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.ScreenHackBlindRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate request
	if req.PlayerID == "" || req.ScreenPosition.X == 0 && req.ScreenPosition.Y == 0 && req.ScreenPosition.Z == 0 {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	// Check skill level bounds
	if req.SkillLevel < 1 || req.SkillLevel > 3 {
		http.Error(w, "Invalid skill level", http.StatusBadRequest)
		return
	}

	// Create blind zone
	zone, err := h.repo.CreateBlindZone(req)
	if err != nil {
		h.logger.Printf("Failed to create blind zone: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := models.ScreenHackBlindResponse{
		Success:         true,
		ZoneID:          zone.ID,
		Duration:        zone.Duration,
		Radius:          zone.Radius,
		DetectionRisk:   zone.DetectionRisk,
		CooldownRemaining: 25, // Base cooldown
		AffectedEnemies:   zone.AffectedEnemies,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// ActivateGlitchDoubles handles glitch doubles skill activation
// Issue: #143875814
func (h *Handlers) ActivateGlitchDoubles(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.GlitchDoublesRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate request
	if req.PlayerID == "" {
		http.Error(w, "Missing player ID", http.StatusBadRequest)
		return
	}

	if req.SkillLevel < 1 || req.SkillLevel > 3 {
		http.Error(w, "Invalid skill level", http.StatusBadRequest)
		return
	}

	if req.EnemyCount < 3 {
		http.Error(w, "Not enough enemies in range", http.StatusBadRequest)
		return
	}

	// Create glitch doubles
	phantoms, err := h.repo.CreateGlitchDoubles(req)
	if err != nil {
		h.logger.Printf("Failed to create glitch doubles: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := models.GlitchDoublesResponse{
		Success:          true,
		PhantomIDs:       make([]string, len(phantoms)),
		Duration:         phantoms[0].Duration,
		Range:            phantoms[0].Range,
		DetectionRisk:    phantoms[0].DetectionRisk,
		CooldownRemaining: 35, // Base cooldown
		AffectedEnemies:   len(phantoms),
	}

	for i, phantom := range phantoms {
		response.PhantomIDs[i] = phantom.ID
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// Issue: #143875347, #143875814

