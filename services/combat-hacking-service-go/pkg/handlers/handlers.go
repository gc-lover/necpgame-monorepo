package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"time"

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

// Enemy hacking handlers
func (h *Handlers) ScanEnemyCyberware(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.EnemyScanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode enemy scan request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.TargetID == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ScanEnemy(req)
	if err != nil {
		h.logger.Printf("Failed to scan enemy: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func (h *Handlers) ExecuteEnemyHack(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.EnemyHackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode enemy hack request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.TargetID == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	if req.SkillLevel < 1 || req.SkillLevel > 3 {
		http.Error(w, "Invalid skill level", http.StatusBadRequest)
		return
	}

	result, err := h.repo.HackEnemy(req)
	if err != nil {
		h.logger.Printf("Failed to hack enemy: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Device hacking handlers
func (h *Handlers) ScanDevice(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.DeviceScanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode device scan request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.DeviceID == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ScanDevice(req)
	if err != nil {
		h.logger.Printf("Failed to scan device: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func (h *Handlers) HackDevice(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.DeviceHackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode device hack request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.DeviceID == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	if req.SkillLevel < 1 || req.SkillLevel > 3 {
		http.Error(w, "Invalid skill level", http.StatusBadRequest)
		return
	}

	result, err := h.repo.HackDevice(req)
	if err != nil {
		h.logger.Printf("Failed to hack device: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Network hacking handlers
func (h *Handlers) InfiltrateNetwork(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.NetworkInfiltrationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode network infiltration request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.NetworkID == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	if req.SkillLevel < 1 || req.SkillLevel > 3 {
		http.Error(w, "Invalid skill level", http.StatusBadRequest)
		return
	}

	result, err := h.repo.InfiltrateNetwork(req)
	if err != nil {
		h.logger.Printf("Failed to infiltrate network: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func (h *Handlers) ExtractNetworkData(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.DataExtractionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode data extraction request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.NetworkID == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ExtractData(req)
	if err != nil {
		h.logger.Printf("Failed to extract data: %v", err)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Combat support handler
func (h *Handlers) RequestCombatSupport(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.CombatSupportRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode combat support request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" {
		http.Error(w, "Missing player ID", http.StatusBadRequest)
		return
	}

	if req.SkillLevel < 1 || req.SkillLevel > 3 {
		http.Error(w, "Invalid skill level", http.StatusBadRequest)
		return
	}

	result, err := h.repo.RequestCombatSupport(req)
	if err != nil {
		h.logger.Printf("Failed to request combat support: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Anti-cheat validation handler
func (h *Handlers) ValidateHackingAttempt(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.HackingValidationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode validation request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.PlayerID == "" || req.ActionType == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ValidateHackingAttempt(req)
	if err != nil {
		h.logger.Printf("Failed to validate hacking attempt: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Active hacks management handlers
func (h *Handlers) GetActiveHacks(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	playerID := r.URL.Query().Get("player_id")
	if playerID == "" {
		// Try to get from path parameter
		playerID = r.PathValue("player_id")
		if playerID == "" {
			http.Error(w, "Missing player_id parameter", http.StatusBadRequest)
			return
		}
	}

	result, err := h.repo.GetActiveHacks(playerID)
	if err != nil {
		h.logger.Printf("Failed to get active hacks: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func (h *Handlers) CancelActiveHack(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	hackID := r.PathValue("hack_id")
	if hackID == "" {
		http.Error(w, "Missing hack_id parameter", http.StatusBadRequest)
		return
	}

	err := h.repo.CancelActiveHack(hackID)
	if err != nil {
		h.logger.Printf("Failed to cancel hack: %v", err)
		http.Error(w, err.Error(), http.StatusNotFound)
		return
	}

	response := map[string]string{
		"status": "cancelled",
		"hack_id": hackID,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// Enemy Hacking Handlers
// Issue: #143875915

// ScanEnemyCyberware handles enemy cyberware scanning
func (h *Handlers) ScanEnemyCyberware(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.EnemyScanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode enemy scan request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ScanEnemyCyberware(req)
	if err != nil {
		h.logger.Printf("Failed to scan enemy cyberware: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// HackEnemyCyberware handles enemy cyberware hacking
func (h *Handlers) HackEnemyCyberware(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.EnemyHackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode enemy hack request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.HackEnemyCyberware(req)
	if err != nil {
		h.logger.Printf("Failed to hack enemy cyberware: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Device Hacking Handlers
// Issue: #143875916

// ScanDevice handles device scanning
func (h *Handlers) ScanDevice(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.DeviceScanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode device scan request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ScanDeviceCyberware(req)
	if err != nil {
		h.logger.Printf("Failed to scan device: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// HackDevice handles device hacking
func (h *Handlers) HackDevice(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.DeviceHackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode device hack request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.HackDevice(req)
	if err != nil {
		h.logger.Printf("Failed to hack device: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Network Hacking Handlers
// Issue: #143875917

// InfiltrateNetwork handles network infiltration
func (h *Handlers) InfiltrateNetwork(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.NetworkInfiltrationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode network infiltration request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.InfiltrateNetwork(req)
	if err != nil {
		h.logger.Printf("Failed to infiltrate network: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// ExtractData handles data extraction
func (h *Handlers) ExtractData(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.DataExtractionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode data extraction request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ExtractData(req)
	if err != nil {
		h.logger.Printf("Failed to extract data: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Combat Support Handlers
// Issue: #143875918

// ActivateCombatSupport handles combat support activation
func (h *Handlers) ActivateCombatSupport(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.CombatSupportRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode combat support request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ActivateCombatSupport(req)
	if err != nil {
		h.logger.Printf("Failed to activate combat support: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// Anti-Cheat Handlers
// Issue: #143875919

// ValidateHackingAttempt handles hacking validation
func (h *Handlers) ValidateHackingAttempt(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var req models.HackingValidationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Printf("Failed to decode validation request: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	result, err := h.repo.ValidateHackingAttempt(req)
	if err != nil {
		h.logger.Printf("Failed to validate hacking attempt: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// GetActiveHacks handles active hacks retrieval
// Issue: #143875920
func (h *Handlers) GetActiveHacks(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	playerID := r.URL.Query().Get("player_id")
	if playerID == "" {
		http.Error(w, "Missing player_id parameter", http.StatusBadRequest)
		return
	}

	activeHacks := h.repo.GetActiveHacks(playerID)
	response := models.ActiveHacksResponse{
		PlayerID: playerID,
		ActiveHacks: activeHacks,
		TotalCount: len(activeHacks),
		LastUpdated: time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// Issue: #143875347, #143875814, #143875915, #143875916, #143875917, #143875918, #143875919, #143875920, #143875915, #143875916, #143875917, #143875918, #143875919, #143875920

