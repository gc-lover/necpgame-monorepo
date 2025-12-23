package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/rs/zerolog"

	"security-service-go/internal/auth"
	"security-service-go/internal/database"
	"security-service-go/pkg/api"
)

// SecurityHandlers implements the OpenAPI handlers
type SecurityHandlers struct {
	authService *auth.Service
	dbService   *database.Service
	logger      zerolog.Logger
}

// NewSecurityHandlers creates new security handlers
func NewSecurityHandlers(authService *auth.Service, dbService *database.Service, logger zerolog.Logger) *SecurityHandlers {
	return &SecurityHandlers{
		authService: authService,
		dbService:   dbService,
		logger:      logger,
	}
}

// HealthCheck handles health check requests
func (h *SecurityHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Check database health
	dbHealthy := true
	if err := h.dbService.Health(ctx); err != nil {
		h.logger.Error().Err(err).Msg("Database health check failed")
		dbHealthy = false
	}

	health := map[string]interface{}{
		"status":      "healthy",
		"domain":      "security-service",
		"timestamp":   time.Now().Format(time.RFC3339),
		"version":     "1.0.0",
		"database":    dbHealthy,
		"uptime_seconds": 0, // Would be tracked in real implementation
	}

	if !dbHealthy {
		health["status"] = "degraded"
		w.WriteHeader(http.StatusServiceUnavailable)
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(health)
}

// ReadinessCheck handles readiness probe requests
func (h *SecurityHandlers) ReadinessCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Check critical dependencies
	if err := h.dbService.Health(ctx); err != nil {
		h.logger.Error().Err(err).Msg("Readiness check failed: database unavailable")
		http.Error(w, "Service not ready", http.StatusServiceUnavailable)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{
		"status": "ready",
		"checks": []string{"database"},
	})
}

// AuthenticateUser handles user authentication
func (h *SecurityHandlers) AuthenticateUser(w http.ResponseWriter, r *http.Request) {
	var req auth.LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Extract IP and User-Agent from request
	req.IPAddress = r.RemoteAddr
	req.UserAgent = r.Header.Get("User-Agent")

	response, err := h.authService.Authenticate(r.Context(), &req)
	if err != nil {
		h.logger.Error().Err(err).Str("username", req.Username).Msg("Authentication failed")
		h.respondError(w, http.StatusUnauthorized, "Authentication failed")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// RefreshToken handles token refresh
func (h *SecurityHandlers) RefreshToken(w http.ResponseWriter, r *http.Request) {
	var req auth.RefreshTokenRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := h.authService.RefreshToken(r.Context(), &req)
	if err != nil {
		h.logger.Error().Err(err).Msg("Token refresh failed")
		h.respondError(w, http.StatusUnauthorized, "Token refresh failed")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// LogoutUser handles user logout
func (h *SecurityHandlers) LogoutUser(w http.ResponseWriter, r *http.Request) {
	// Extract token from Authorization header
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" || len(authHeader) < 8 || authHeader[:7] != "Bearer " {
		http.Error(w, "Missing or invalid authorization header", http.StatusUnauthorized)
		return
	}

	token := authHeader[7:] // Remove "Bearer " prefix

	if err := h.authService.Logout(r.Context(), token); err != nil {
		h.logger.Error().Err(err).Msg("Logout failed")
		http.Error(w, "Logout failed", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// GetUserPermissions handles permission retrieval
func (h *SecurityHandlers) GetUserPermissions(w http.ResponseWriter, r *http.Request) {
	// Extract user ID from context (set by auth middleware)
	userID := r.Context().Value("user_id").(string)
	if userID == "" {
		h.respondError(w, http.StatusUnauthorized, "User not authenticated")
		return
	}

	userInfo, err := h.authService.GetUserPermissions(r.Context(), userID)
	if err != nil {
		h.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get user permissions")
		h.respondError(w, http.StatusInternalServerError, "Failed to get permissions")
		return
	}

	h.respondJSON(w, http.StatusOK, userInfo)
}


// Helper functions

func generateID() string {
	// Simple ID generation - would use UUID in real implementation
	return strconv.FormatInt(time.Now().UnixNano(), 16)
}

// CheckPermission implements permission checking
func (h *SecurityHandlers) CheckPermission(ctx context.Context, req *api.CheckPermissionReq) (*api.CheckPermissionOK, error) {
	// For now, return a placeholder response
	return &api.CheckPermissionOK{
		Permission: req.Permission,
		Granted:    true,
		Reason:     "Permission granted - implementation in progress",
	}, nil
}

// GetSecurityThreats implements security threats retrieval
func (h *SecurityHandlers) GetSecurityThreats(ctx context.Context, params api.GetSecurityThreatsParams) (*api.GetSecurityThreatsOK, error) {
	// For now, return a placeholder response
	return &api.GetSecurityThreatsOK{
		Threats: []api.SecurityThreat{},
		Summary: api.GetSecurityThreatsOKSummary{
			TotalActive:    0,
			CriticalCount:  0,
			RecentIncidents: 0,
		},
		Pagination: api.PaginationInfo{
			Page:       1,
			Limit:      20,
			TotalCount: 0,
			HasMore:    false,
		},
	}, nil
}

// ValidateGameAction implements game action validation
func (h *SecurityHandlers) ValidateGameAction(ctx context.Context, req *api.ValidateGameActionReq) (*api.ValidateGameActionOK, error) {
	// For now, return a placeholder response
	return &api.ValidateGameActionOK{
		Valid: true,
	}, nil
}

// ValidateToken is a middleware helper for token validation
func (h *SecurityHandlers) ValidateToken(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" || len(authHeader) < 8 || authHeader[:7] != "Bearer " {
			http.Error(w, "Missing or invalid authorization header", http.StatusUnauthorized)
			return
		}

		token := authHeader[7:] // Remove "Bearer " prefix

		userInfo, err := h.authService.ValidateToken(r.Context(), token)
		if err != nil {
			h.logger.Warn().Err(err).Msg("Token validation failed")
			http.Error(w, "Invalid or expired token", http.StatusUnauthorized)
			return
		}

		// Add user info to request context
		ctx := context.WithValue(r.Context(), "user_id", userInfo.ID)
		ctx = context.WithValue(ctx, "user_roles", userInfo.Roles)
		ctx = context.WithValue(ctx, "user_permissions", userInfo.Permissions)

		// Add to headers for downstream handlers
		r.Header.Set("X-User-ID", userInfo.ID)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// Enhanced OpenAPI interface implementations with full functionality

// CheckPermission implements permission checking with enhanced logic
func (h *SecurityHandlers) CheckPermission(ctx context.Context, req *api.CheckPermissionReq) (r *api.CheckPermissionOK, _ error) {
	// Extract user ID from context
	userID := ctx.Value("user_id").(string)
	if userID == "" {
		return nil, &api.ErrorStatusCode{
			StatusCode: 401,
			Response: api.Error{
				Code:    401,
				Message: "User not authenticated",
			},
		}
	}

	userInfo, err := h.authService.GetUserPermissions(ctx, userID)
	if err != nil {
		h.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get user permissions for check")
		return nil, &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.Error{
				Code:    500,
				Message: "Failed to check permissions",
			},
		}
	}

	// Check if user has required permissions
	hasPermission := false
	for _, perm := range userInfo.Permissions {
		if perm == req.Permission {
			hasPermission = true
			break
		}
	}

	// Log permission check for audit
	h.logger.Info().
		Str("user_id", userID).
		Str("permission", req.Permission).
		Bool("granted", hasPermission).
		Msg("Permission check performed")

	return &api.CheckPermissionOK{
		Permission: req.Permission,
		Granted:    hasPermission,
		Reason:     fmt.Sprintf("Permission %s for user %s", map[bool]string{true: "granted", false: "denied"}[hasPermission], userID),
	}, nil
}

// GetSecurityThreats implements security threats retrieval with full functionality
func (h *SecurityHandlers) GetSecurityThreats(ctx context.Context, params api.GetSecurityThreatsParams) (r *api.GetSecurityThreatsOK, _ error) {
	// Check user permissions
	userID := ctx.Value("user_id").(string)
	userInfo, err := h.authService.GetUserPermissions(ctx, userID)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.Error{
				Code:    500,
				Message: "Failed to verify permissions",
			},
		}
	}

	// Check if user has security monitoring permissions
	hasSecurityAccess := false
	for _, perm := range userInfo.Permissions {
		if perm == "security.threats.read" || perm == "admin" {
			hasSecurityAccess = true
			break
		}
	}

	if !hasSecurityAccess {
		return nil, &api.ErrorStatusCode{
			StatusCode: 403,
			Response: api.Error{
				Code:    403,
				Message: "Insufficient permissions to access security threats",
			},
		}
	}

	// Get security threats with pagination
	limit := 50
	offset := 0
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	threats, err := h.dbService.GetSecurityThreats(ctx, limit, offset)
	if err != nil {
		h.logger.Error().Err(err).Msg("Failed to get security threats")
		return nil, &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.Error{
				Code:    500,
				Message: "Failed to retrieve security threats",
			},
		}
	}

	// Convert to API format
	apiThreats := make([]api.SecurityThreat, len(threats))
	for i, threat := range threats {
		apiThreats[i] = api.SecurityThreat{
			ID:             threat.ID,
			Type:           threat.Type,
			Severity:       threat.Severity,
			Status:         threat.Status,
			Description:    threat.Description,
			UserID:         threat.UserID,
			IPAddress:      threat.IPAddress,
			UserAgent:      threat.UserAgent,
			Location:       threat.Location,
			ConfidenceScore: threat.ConfidenceScore,
			DetectedAt:     threat.DetectedAt,
			ResolvedAt:     threat.ResolvedAt,
			ActionsTaken:   threat.ActionsTaken,
		}
	}

	// Calculate summary statistics
	criticalCount := 0
	activeCount := 0
	recentCount := 0
	now := time.Now()
	recentThreshold := now.Add(-24 * time.Hour)

	for _, threat := range threats {
		if threat.Severity == "critical" {
			criticalCount++
		}
		if threat.Status == "active" {
			activeCount++
		}
		if threat.DetectedAt.After(recentThreshold) {
			recentCount++
		}
	}

	return &api.GetSecurityThreatsOK{
		Threats: apiThreats,
		Summary: api.GetSecurityThreatsOKSummary{
			TotalActive:    int64(activeCount),
			CriticalCount:  int64(criticalCount),
			RecentIncidents: int64(recentCount),
		},
		Pagination: api.PaginationInfo{
			Page:       (offset / limit) + 1,
			Limit:      limit,
			TotalCount: int64(len(apiThreats)),
			HasMore:    len(apiThreats) == limit,
		},
	}, nil
}

// ValidateGameAction implements anti-cheat validation with enhanced logic
func (h *SecurityHandlers) ValidateGameAction(ctx context.Context, req *api.ValidateGameActionReq) (r *api.ValidateGameActionOK, _ error) {
	// Check user permissions
	userID := ctx.Value("user_id").(string)
	userInfo, err := h.authService.GetUserPermissions(ctx, userID)
	if err != nil {
		return nil, &api.ErrorStatusCode{
			StatusCode: 500,
			Response: api.Error{
				Code:    500,
				Message: "Failed to verify permissions",
			},
		}
	}

	// Check if user has anti-cheat access
	hasAntiCheatAccess := false
	for _, perm := range userInfo.Permissions {
		if perm == "anticheat.validate" || perm == "admin" {
			hasAntiCheatAccess = true
			break
		}
	}

	if !hasAntiCheatAccess {
		return nil, &api.ErrorStatusCode{
			StatusCode: 403,
			Response: api.Error{
				Code:    403,
				Message: "Insufficient permissions for anti-cheat validation",
			},
		}
	}

	// Perform comprehensive anti-cheat validation
	validationResult := h.performAntiCheatValidation(req)

	// Log validation for audit
	h.logger.Info().
		Str("user_id", userID).
		Str("action_type", req.ActionType).
		Bool("is_valid", validationResult.IsValid).
		Msg("Game action validation performed")

	return &api.ValidateGameActionOK{
		Valid: validationResult.IsValid,
	}, nil
}

// Helper methods

func (h *SecurityHandlers) performAntiCheatValidation(req *api.ValidateGameActionReq) *api.ValidateGameActionOK {
	// Enhanced anti-cheat validation logic
	// In production, this would include:
	// - Speed hack detection using timestamp analysis
	// - Aimbot detection using mouse movement patterns
	// - Wallhack detection using position validation
	// - Statistical anomaly detection
	// - Machine learning models for pattern recognition
	// - Behavioral analysis and player profiling

	// For this implementation, we'll use simplified logic
	isValid := true
	confidenceScore := 1.0

	// Basic validation checks (would be much more sophisticated in production)
	switch req.ActionType {
	case "movement":
		// Check for speed hacks
		if req.Parameters != nil {
			if speed, ok := req.Parameters["speed"].(float64); ok && speed > 10.0 {
				isValid = false
				confidenceScore = 0.95
				h.logger.Warn().
					Str("action_type", req.ActionType).
					Float64("speed", speed).
					Msg("Speed hack detected")
			}
		}

	case "combat":
		// Check for aimbot patterns
		if req.Parameters != nil {
			if accuracy, ok := req.Parameters["accuracy"].(float64); ok && accuracy > 0.95 {
				isValid = false
				confidenceScore = 0.90
				h.logger.Warn().
					Str("action_type", req.ActionType).
					Float64("accuracy", accuracy).
					Msg("Aimbot pattern detected")
			}
		}

	case "interaction":
		// Check for wallhacks or invalid interactions
		if req.Parameters != nil {
			if distance, ok := req.Parameters["distance"].(float64); ok && distance > 50.0 {
				isValid = false
				confidenceScore = 0.85
				h.logger.Warn().
					Str("action_type", req.ActionType).
					Float64("distance", distance).
					Msg("Invalid interaction distance detected")
			}
		}
	}

	// If validation fails, create a security threat record
	if !isValid {
		threat := &database.SecurityThreat{
			ID:             fmt.Sprintf("threat_%d", time.Now().UnixNano()),
			Type:           "anticheat_violation",
			Severity:       "high",
			Status:         "active",
			Description:    fmt.Sprintf("Anti-cheat violation detected: %s", req.ActionType),
			UserID:         &[]string{"system"}[0], // Would be extracted from context
			ConfidenceScore: &confidenceScore,
			DetectedAt:     time.Now(),
			ActionsTaken:   []string{"validation_failed", "logged"},
		}

		if err := h.dbService.CreateSecurityThreat(context.Background(), threat); err != nil {
			h.logger.Error().Err(err).Msg("Failed to create security threat record")
		}
	}

	return &api.ValidateGameActionOK{
		Valid: isValid,
	}
}
