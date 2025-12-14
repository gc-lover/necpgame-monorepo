package handler

import (
	"context"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"necpgame/services/interactive-objects-service-go/internal/service"
)

// Handler implements the generated API interface
type Handler struct {
	interactiveService *service.InteractiveService
}

// NewHandler creates a new handler instance
func NewHandler(interactiveService *service.InteractiveService) *Handler {
	return &Handler{
		interactiveService: interactiveService,
	}
}

// Router returns the HTTP router with all endpoints
func (h *Handler) Router() http.Handler {
	// This would integrate with the generated ogen router
	// For now, return a basic mux
	mux := http.NewServeMux()

	// Health endpoint
	mux.HandleFunc("/health", h.handleHealth)

	// Interactive objects endpoints
	mux.HandleFunc("/objects", h.handleGetActiveObjects)
	mux.HandleFunc("/objects/", h.handleObjectByID)

	// Telemetry endpoint
	mux.HandleFunc("/telemetry", h.handleTelemetry)

	return mux
}

// handleHealth provides health check endpoint
func (h *Handler) handleHealth(w http.ResponseWriter, r *http.Request) {
	telemetry := h.interactiveService.GetTelemetry()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"status": "healthy",
		"timestamp": "%s",
		"active_objects": %d,
		"uptime_seconds": 3600
	}`, time.Now().Format(time.RFC3339), telemetry.ActiveObjects)
}

// handleGetActiveObjects handles GET /objects
func (h *Handler) handleGetActiveObjects(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	zoneID := r.URL.Query().Get("zone_id")
	if zoneID == "" {
		http.Error(w, "zone_id parameter required", http.StatusBadRequest)
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 50
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	objects, err := h.interactiveService.GetActiveObjects(ctx, zoneID)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to get objects: %v", err), http.StatusInternalServerError)
		return
	}

	// Limit results
	if len(objects) > limit {
		objects = objects[:limit]
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	fmt.Fprintf(w, `{"objects":[`)
	for i, obj := range objects {
		if i > 0 {
			fmt.Fprintf(w, ",")
		}
		fmt.Fprintf(w, `{
			"object_id": "%s",
			"object_type": "%s",
			"position": {"x": %f, "y": %f, "z": %f},
			"status": "%s",
			"zone_type": "%s"
		}`, obj.ID, obj.ObjectType, obj.Position.X, obj.Position.Y, obj.Position.Z, obj.Status, obj.ZoneType)
	}
	fmt.Fprintf(w, `],"total_count": %d}`, len(objects))
}

// handleObjectByID handles operations on specific objects
func (h *Handler) handleObjectByID(w http.ResponseWriter, r *http.Request) {
	// Extract object ID from URL path
	// This is a simplified implementation

	switch r.Method {
	case http.MethodGet:
		h.handleGetObject(w, r)
	case http.MethodPost:
		if r.URL.Path[len(r.URL.Path)-8:] == "/interact" {
			h.handleInteract(w, r)
		} else {
			http.Error(w, "Not implemented", http.StatusNotImplemented)
		}
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

// handleGetObject handles GET /objects/{id}
func (h *Handler) handleGetObject(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Extract object ID from path (simplified)
	objectID := r.URL.Path[len("/objects/"):]

	object, err := h.interactiveService.GetObject(ctx, objectID)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to get object: %v", err), http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"object_id": "%s",
		"object_type": "%s",
		"position": {"x": %f, "y": %f, "z": %f},
		"status": "%s",
		"zone_type": "%s",
		"created_at": "%s"
	}`, object.ID, object.ObjectType, object.Position.X, object.Position.Y, object.Position.Z,
		object.Status, object.ZoneType, object.CreatedAt.Format(time.RFC3339))
}

// handleInteract handles POST /objects/{id}/interact
func (h *Handler) handleInteract(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Extract object ID from path (simplified)
	path := r.URL.Path
	objectID := path[len("/objects/") : len(path)-9] // Remove "/interact"

	if r.Header.Get("Content-Type") != "application/json" {
		http.Error(w, "Content-Type must be application/json", http.StatusBadRequest)
		return
	}

	// Simplified interaction type parsing
	interactionType := "hack" // Default

	result, err := h.interactiveService.InteractWithObject(ctx, objectID, interactionType)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to interact with object: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"object_id": "%s",
		"interaction_type": "%s",
		"success": %t,
		"reward_type": "%s",
		"reward_amount": %d,
		"new_status": "%s"
	}`, objectID, result.InteractionType, result.Success,
		result.RewardType, result.RewardAmount, result.NewStatus)
}

// handleTelemetry provides service telemetry
func (h *Handler) handleTelemetry(w http.ResponseWriter, r *http.Request) {
	telemetry := h.interactiveService.GetTelemetry()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, `{
		"metric": "service_stats",
		"timeframe": "current",
		"data_points": [{
			"timestamp": "%s",
			"active_objects": %d,
			"interactions_processed": %d,
			"rewards_distributed": %d,
			"objects_destroyed": %d
		}],
		"average": %d,
		"peak": %d,
		"total": %d
	}`, time.Now().Format(time.RFC3339), telemetry.ActiveObjects,
		telemetry.InteractionsProcessed, telemetry.RewardsDistributed,
		telemetry.ObjectsDestroyed,
		telemetry.ActiveObjects, telemetry.ActiveObjects, telemetry.ActiveObjects)
}

// Issue: #1861
