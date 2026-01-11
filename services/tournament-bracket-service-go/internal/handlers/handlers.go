// Tournament Bracket Service Handlers - HTTP API implementation
// Issue: #2210
// Agent: Backend Agent

package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"go.uber.org/zap"

	"necpgame/services/tournament-bracket-service-go/internal/models"
	"necpgame/services/tournament-bracket-service-go/internal/service"
)

// Handlers handles HTTP requests for tournament brackets
type Handlers struct {
	service    *service.Service
	logger     *zap.Logger
	upgrader   websocket.Upgrader
	clients    map[string]*websocket.Conn
	clientMux  sync.RWMutex
}

// NewHandlers creates a new handlers instance
func NewHandlers(svc *service.Service, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: svc,
		logger:  logger,
		upgrader: websocket.Upgrader{
			ReadBufferSize:  1024,
			WriteBufferSize: 1024,
			CheckOrigin: func(r *http.Request) bool {
				// Allow connections from any origin in development
				// In production, implement proper CORS policy
				return true
			},
		},
		clients: make(map[string]*websocket.Conn),
	}
}

// Health check handler
func (h *Handlers) HandleHealth(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "tournament-bracket-service",
		"version":   "1.0.0",
		"timestamp": time.Now().UTC(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// Readiness check handler
func (h *Handlers) HandleReady(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	response := map[string]interface{}{
		"status":    "ready",
		"service":   "tournament-bracket-service",
		"timestamp": time.Now().UTC(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// BRACKET HANDLERS

// HandleBrackets handles bracket collection operations
func (h *Handlers) HandleBrackets(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		h.handleGetBrackets(w, r)
	case http.MethodPost:
		h.handleCreateBracket(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// HandleBracketByID handles individual bracket operations
func (h *Handlers) HandleBracketByID(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/brackets/")
	parts := strings.Split(path, "/")

	if len(parts) == 0 || parts[0] == "" {
		http.Error(w, "Bracket ID required", http.StatusBadRequest)
		return
	}

	bracketID := parts[0]

	switch r.Method {
	case http.MethodGet:
		h.handleGetBracket(w, r, bracketID)
	case http.MethodPut:
		h.handleUpdateBracket(w, r, bracketID)
	case http.MethodDelete:
		h.handleDeleteBracket(w, r, bracketID)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// HandleBracketOperations handles bracket-specific operations
func (h *Handlers) HandleBracketOperations(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/brackets/")
	parts := strings.Split(path, "/")

	if len(parts) < 2 {
		http.Error(w, "Invalid path", http.StatusBadRequest)
		return
	}

	bracketID := parts[0]
	operation := parts[1]

	switch operation {
	case "start":
		h.handleStartBracket(w, r, bracketID)
	case "advance":
		h.handleAdvanceBracket(w, r, bracketID)
	case "finish":
		h.handleFinishBracket(w, r, bracketID)
	default:
		http.Error(w, "Unknown operation", http.StatusNotFound)
	}
}

// ROUND HANDLERS

// HandleRounds handles round collection operations
func (h *Handlers) HandleRounds(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		h.handleGetRounds(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// HandleRoundByID handles individual round operations
func (h *Handlers) HandleRoundByID(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/rounds/")
	roundID := strings.TrimSuffix(path, "/")

	if roundID == "" {
		http.Error(w, "Round ID required", http.StatusBadRequest)
		return
	}

	switch r.Method {
	case http.MethodGet:
		h.handleGetRound(w, r, roundID)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// MATCH HANDLERS

// HandleMatches handles match collection operations
func (h *Handlers) HandleMatches(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		h.handleGetMatches(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// HandleMatchByID handles individual match operations
func (h *Handlers) HandleMatchByID(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/matches/")
	parts := strings.Split(path, "/")

	if len(parts) == 0 || parts[0] == "" {
		http.Error(w, "Match ID required", http.StatusBadRequest)
		return
	}

	matchID := parts[0]

	switch r.Method {
	case http.MethodGet:
		h.handleGetMatch(w, r, matchID)
	case http.MethodPut:
		h.handleUpdateMatch(w, r, matchID)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// HandleMatchOperations handles match-specific operations
func (h *Handlers) HandleMatchOperations(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/matches/")
	parts := strings.Split(path, "/")

	if len(parts) < 2 {
		http.Error(w, "Invalid path", http.StatusBadRequest)
		return
	}

	matchID := parts[0]
	operation := parts[1]

	switch operation {
	case "start":
		h.handleStartMatch(w, r, matchID)
	case "finish":
		h.handleFinishMatch(w, r, matchID)
	case "report":
		h.handleReportMatchResult(w, r, matchID)
	default:
		http.Error(w, "Unknown operation", http.StatusNotFound)
	}
}

// PARTICIPANT HANDLERS

// HandleParticipants handles participant collection operations
func (h *Handlers) HandleParticipants(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		h.handleGetParticipants(w, r)
	case http.MethodPost:
		h.handleAddParticipant(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// HandleParticipantByID handles individual participant operations
func (h *Handlers) HandleParticipantByID(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/participants/")
	participantID := strings.TrimSuffix(path, "/")

	if participantID == "" {
		http.Error(w, "Participant ID required", http.StatusBadRequest)
		return
	}

	switch r.Method {
	case http.MethodGet:
		h.handleGetParticipant(w, r, participantID)
	case http.MethodDelete:
		h.handleRemoveParticipant(w, r, participantID)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// WEBSOCKET HANDLERS

// HandleBracketWebSocket handles WebSocket connections for bracket updates
func (h *Handlers) HandleBracketWebSocket(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/ws/brackets/")
	bracketID := strings.TrimSuffix(path, "/")

	if bracketID == "" {
		http.Error(w, "Bracket ID required", http.StatusBadRequest)
		return
	}

	conn, err := h.upgrader.Upgrade(w, r, nil)
	if err != nil {
		h.logger.Error("Failed to upgrade connection", zap.Error(err))
		return
	}

	// Register client
	h.clientMux.Lock()
	h.clients[bracketID] = conn
	h.clientMux.Unlock()

	h.logger.Info("WebSocket client connected",
		zap.String("bracket_id", bracketID),
		zap.String("remote_addr", r.RemoteAddr))

	// Handle connection cleanup
	defer func() {
		h.clientMux.Lock()
		delete(h.clients, bracketID)
		h.clientMux.Unlock()
		conn.Close()
		h.logger.Info("WebSocket client disconnected", zap.String("bracket_id", bracketID))
	}()

	// Keep connection alive and handle messages
	for {
		messageType, message, err := conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				h.logger.Error("WebSocket error", zap.Error(err))
			}
			break
		}

		h.logger.Info("Received WebSocket message",
			zap.Int("type", messageType),
			zap.String("bracket_id", bracketID))

		// Echo message back for now
		if err := conn.WriteMessage(messageType, message); err != nil {
			h.logger.Error("Failed to write WebSocket message", zap.Error(err))
			break
		}
	}
}

// IMPLEMENTATION METHODS

func (h *Handlers) handleGetBrackets(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	tournamentID := r.URL.Query().Get("tournament_id")

	limitStr := r.URL.Query().Get("limit")
	limit := 50 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 1000 {
			limit = parsed
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if parsed, err := strconv.Atoi(offsetStr); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	brackets, err := h.service.ListBrackets(r.Context(), &tournamentID, nil, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get brackets", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}
	if err != nil {
		h.logger.Error("Failed to get brackets", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"brackets": brackets,
		"limit":    limit,
		"offset":   offset,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

func (h *Handlers) handleCreateBracket(w http.ResponseWriter, r *http.Request) {
	var req models.CreateBracketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	bracket, err := h.service.CreateBracket(r.Context(), &req)
	if err != nil {
		h.logger.Error("Failed to create bracket", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(bracket)
}

func (h *Handlers) handleGetBracket(w http.ResponseWriter, r *http.Request, bracketID string) {
	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	bracket, err := h.service.GetBracket(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to get bracket", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Bracket not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(bracket)
}

func (h *Handlers) handleUpdateBracket(w http.ResponseWriter, r *http.Request, bracketID string) {
	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	var req models.UpdateBracketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	bracket, err := h.service.UpdateBracket(r.Context(), id, &req)
	if err != nil {
		h.logger.Error("Failed to update bracket", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(bracket)
}

func (h *Handlers) handleDeleteBracket(w http.ResponseWriter, r *http.Request, bracketID string) {
	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	err = h.service.DeleteBracket(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to delete bracket", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

func (h *Handlers) handleStartBracket(w http.ResponseWriter, r *http.Request, bracketID string) {
	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	err = h.service.StartBracket(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to start bracket", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{"message": "Bracket started successfully", "bracket_id": "%s"}`, bracketID)
}

func (h *Handlers) handleAdvanceBracket(w http.ResponseWriter, r *http.Request, bracketID string) {
	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	err = h.service.AdvanceBracket(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to advance bracket", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{"message": "Bracket advanced successfully", "bracket_id": "%s"}`, bracketID)
}

func (h *Handlers) handleFinishBracket(w http.ResponseWriter, r *http.Request, bracketID string) {
	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	err = h.service.FinishBracket(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to finish bracket", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{"message": "Bracket finished successfully", "bracket_id": "%s"}`, bracketID)
}

func (h *Handlers) handleGetRounds(w http.ResponseWriter, r *http.Request) {
	bracketID := r.URL.Query().Get("bracket_id")
	if bracketID == "" {
		http.Error(w, "bracket_id parameter required", http.StatusBadRequest)
		return
	}

	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	rounds, err := h.service.GetRounds(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to get rounds", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{"rounds": rounds})
}

func (h *Handlers) handleGetRound(w http.ResponseWriter, r *http.Request, roundID string) {
	id, err := uuid.Parse(roundID)
	if err != nil {
		h.logger.Error("Invalid round ID", zap.String("round_id", roundID), zap.Error(err))
		http.Error(w, "Invalid round ID", http.StatusBadRequest)
		return
	}

	round, err := h.service.GetRound(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to get round", zap.String("round_id", roundID), zap.Error(err))
		http.Error(w, "Round not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(round)
}

func (h *Handlers) handleGetMatches(w http.ResponseWriter, r *http.Request) {
	bracketID := r.URL.Query().Get("bracket_id")
	if bracketID == "" {
		http.Error(w, "bracket_id parameter required", http.StatusBadRequest)
		return
	}

	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	matches, err := h.service.GetMatches(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to get matches", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"matches": matches,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

func (h *Handlers) handleGetMatch(w http.ResponseWriter, r *http.Request, matchID string) {
	id, err := uuid.Parse(matchID)
	if err != nil {
		h.logger.Error("Invalid match ID", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Invalid match ID", http.StatusBadRequest)
		return
	}

	match, err := h.service.GetMatch(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to get match", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Match not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(match)
}

func (h *Handlers) handleUpdateMatch(w http.ResponseWriter, r *http.Request, matchID string) {
	id, err := uuid.Parse(matchID)
	if err != nil {
		h.logger.Error("Invalid match ID", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Invalid match ID", http.StatusBadRequest)
		return
	}

	var req models.UpdateMatchRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	match, err := h.service.UpdateMatch(r.Context(), id, &req)
	if err != nil {
		h.logger.Error("Failed to update match", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(match)
}

func (h *Handlers) handleStartMatch(w http.ResponseWriter, r *http.Request, matchID string) {
	id, err := uuid.Parse(matchID)
	if err != nil {
		h.logger.Error("Invalid match ID", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Invalid match ID", http.StatusBadRequest)
		return
	}

	err = h.service.StartMatch(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to start match", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{"message": "Match started successfully", "match_id": "%s"}`, matchID)
}

func (h *Handlers) handleFinishMatch(w http.ResponseWriter, r *http.Request, matchID string) {
	id, err := uuid.Parse(matchID)
	if err != nil {
		h.logger.Error("Invalid match ID", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Invalid match ID", http.StatusBadRequest)
		return
	}

	err = h.service.FinishMatch(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to finish match", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{"message": "Match finished successfully", "match_id": "%s"}`, matchID)
}

func (h *Handlers) handleReportMatchResult(w http.ResponseWriter, r *http.Request, matchID string) {
	id, err := uuid.Parse(matchID)
	if err != nil {
		h.logger.Error("Invalid match ID", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Invalid match ID", http.StatusBadRequest)
		return
	}

	var req models.ReportMatchResultRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	result, err := h.service.ReportMatchResult(r.Context(), id, &req)
	if err != nil {
		h.logger.Error("Failed to report match result", zap.String("match_id", matchID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func (h *Handlers) handleGetParticipants(w http.ResponseWriter, r *http.Request) {
	bracketID := r.URL.Query().Get("bracket_id")
	if bracketID == "" {
		http.Error(w, "bracket_id parameter required", http.StatusBadRequest)
		return
	}

	id, err := uuid.Parse(bracketID)
	if err != nil {
		h.logger.Error("Invalid bracket ID", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Invalid bracket ID", http.StatusBadRequest)
		return
	}

	participants, err := h.service.GetParticipants(r.Context(), id)
	if err != nil {
		h.logger.Error("Failed to get participants", zap.String("bracket_id", bracketID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{"participants": participants})
}

func (h *Handlers) handleAddParticipant(w http.ResponseWriter, r *http.Request) {
	var req models.CreateParticipantRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	participant, err := h.service.AddParticipant(r.Context(), &req)
	if err != nil {
		h.logger.Error("Failed to add participant", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(participant)
}

func (h *Handlers) handleGetParticipant(w http.ResponseWriter, r *http.Request, participantID string) {
	participant, err := h.service.GetParticipant(r.Context(), participantID)
	if err != nil {
		h.logger.Error("Failed to get participant", zap.String("participant_id", participantID), zap.Error(err))
		http.Error(w, "Participant not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(participant)
}

func (h *Handlers) handleRemoveParticipant(w http.ResponseWriter, r *http.Request, participantID string) {
	err := h.service.RemoveParticipant(r.Context(), participantID)
	if err != nil {
		h.logger.Error("Failed to remove participant", zap.String("participant_id", participantID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}