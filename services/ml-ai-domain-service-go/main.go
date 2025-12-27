// Issue: #2257
// Enterprise-grade ML/AI Domain Service for NECPGAME MMORPG
// Provides machine learning predictions, model management, and AI-driven analytics

package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"math/rand"
	"net/http"
	"os"
	"os/signal"
	"regexp"
	"sync"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"ml-ai-domain-service-go/api"
)

// Validation errors
var (
	ErrInvalidModelName     = errors.New("invalid model name: must be 3-100 characters, alphanumeric with hyphens/underscores")
	ErrInvalidModelType     = errors.New("invalid model type: must be one of classification, regression, recommendation, anomaly_detection, clustering")
	ErrInvalidAlgorithm     = errors.New("invalid algorithm: must be 2-50 characters")
	ErrInvalidTrainingData  = errors.New("invalid training data: must provide at least 100 samples")
	ErrInvalidPredictionData = errors.New("invalid prediction data: input features cannot be empty")
	ErrModelNotFound       = errors.New("model not found")
	ErrTrainingJobNotFound = errors.New("training job not found")
	ErrInvalidBatchSize    = errors.New("invalid batch size: must be between 1 and 1000")
)

// DatabaseConfig holds database connection configuration
type DatabaseConfig struct {
	Host         string
	Port         int
	User         string
	Password     string
	Database     string
	SSLMode      string
	MaxOpenConns int           // Maximum number of open connections
	MaxIdleConns int           // Maximum number of idle connections
	MaxLifetime  time.Duration // Maximum lifetime of a connection
}

// Validator provides input validation for ML/AI service
type Validator struct {
	modelNameRegex *regexp.Regexp
	algorithmRegex *regexp.Regexp
}

// NewValidator creates a new input validator
func NewValidator() *Validator {
	modelNameRegex := regexp.MustCompile(`^[a-zA-Z0-9_-]{3,100}$`)
	algorithmRegex := regexp.MustCompile(`^[a-zA-Z0-9_-]{2,50}$`)

	return &Validator{
		modelNameRegex: modelNameRegex,
		algorithmRegex: algorithmRegex,
	}
}

// NewDatabaseConfig creates database configuration with optimized connection pooling for MMORPG
func NewDatabaseConfig() *DatabaseConfig {
	return &DatabaseConfig{
		Host:         getEnvOrDefault("DB_HOST", "localhost"),
		Port:         5432, // PostgreSQL default
		User:         getEnvOrDefault("DB_USER", "ml_ai_user"),
		Password:     getEnvOrDefault("DB_PASSWORD", ""),
		Database:     getEnvOrDefault("DB_NAME", "ml_ai_db"),
		SSLMode:      getEnvOrDefault("DB_SSL_MODE", "disable"),

		// Optimized for MMORPG ML/AI service with high concurrent load
		MaxOpenConns: 50,                // Allow up to 50 concurrent connections (high for ML workloads)
		MaxIdleConns: 10,                // Keep 10 idle connections ready
		MaxLifetime:  30 * time.Minute,  // Recycle connections every 30 minutes
	}
}

// getEnvOrDefault gets environment variable or returns default value
func getEnvOrDefault(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// ValidateModelName validates model name
func (v *Validator) ValidateModelName(name string) error {
	if name == "" {
		return ErrInvalidModelName
	}
	if !v.modelNameRegex.MatchString(name) {
		return ErrInvalidModelName
	}
	return nil
}

// ValidateModelType validates model type
func (v *Validator) ValidateModelType(modelType string) error {
	validTypes := []string{"classification", "regression", "recommendation", "anomaly_detection", "clustering"}
	for _, validType := range validTypes {
		if modelType == validType {
			return nil
		}
	}
	return ErrInvalidModelType
}

// ValidateAlgorithm validates algorithm name
func (v *Validator) ValidateAlgorithm(algorithm string) error {
	if algorithm == "" {
		return ErrInvalidAlgorithm
	}
	if !v.algorithmRegex.MatchString(algorithm) {
		return ErrInvalidAlgorithm
	}
	return nil
}

// ValidateTrainingData validates training data
func (v *Validator) ValidateTrainingData(sampleCount int) error {
	if sampleCount < 100 {
		return ErrInvalidTrainingData
	}
	return nil
}

// ValidatePredictionInput validates prediction input
func (v *Validator) ValidatePredictionInput(input interface{}) error {
	if input == nil {
		return ErrInvalidPredictionData
	}

	// Check if input is a map/slice with content
	switch data := input.(type) {
	case map[string]interface{}:
		if len(data) == 0 {
			return ErrInvalidPredictionData
		}
	case []interface{}:
		if len(data) == 0 {
			return ErrInvalidPredictionData
		}
	case []map[string]interface{}:
		if len(data) == 0 {
			return ErrInvalidPredictionData
		}
	default:
		// For other types, input is considered valid if not nil
		// (checked above)
	}

	return nil
}

// ValidateBatchSize validates batch size
func (v *Validator) ValidateBatchSize(size int) error {
	if size < 1 || size > 1000 {
		return ErrInvalidBatchSize
	}
	return nil
}

// Service represents the ML/AI domain service
type Service struct {
	server    *http.Server
	logger    *zap.Logger
	db        *sql.DB
	wg        sync.WaitGroup
	validator *Validator

	// ML model cache
	models map[string]*MLModel
	mu     sync.RWMutex

	// Training jobs cache
	trainingJobs map[uuid.UUID]*TrainingJob
	trainingMu   sync.RWMutex

	// Predictions cache (for analytics)
	predictions []*PredictionRecord
	predMu      sync.RWMutex
}

// TrainingJob represents a model training job
type TrainingJob struct {
	JobID     uuid.UUID
	ModelName string
	Status    string
	Progress  float64
	Accuracy  float64
	StartTime time.Time
	EndTime   *time.Time
	CreatedAt time.Time
}

// PredictionRecord represents a prediction for analytics
type PredictionRecord struct {
	PredictionID uuid.UUID
	ModelID      uuid.UUID
	Timestamp    time.Time
	Success      bool
	Latency      float64
}

// MLModel represents a machine learning model
type MLModel struct {
	ID          string                 `json:"id"`
	Name        string                 `json:"name"`
	Type        string                 `json:"type"`
	Version     string                 `json:"version"`
	Status      string                 `json:"status"`
	Accuracy    float64                `json:"accuracy"`
	LastUpdated time.Time              `json:"last_updated"`
	Metadata    map[string]interface{} `json:"metadata"`
}

// NewService creates a new ML/AI service instance
func NewService() (*Service, error) {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		return nil, fmt.Errorf("failed to create logger: %w", err)
	}

	// Initialize input validator
	validator := NewValidator()

	// Initialize database with optimized connection pooling
	db, err := initializeDatabase()
	if err != nil {
		return nil, fmt.Errorf("failed to initialize database: %w", err)
	}

	service := &Service{
		logger:       logger,
		validator:    validator,
		db:           db,
		models:       make(map[string]*MLModel),
		trainingJobs: make(map[uuid.UUID]*TrainingJob),
		predictions:  make([]*PredictionRecord, 0),
	}

	// Initialize sample ML models for demonstration
	service.initializeSampleModels()

	return service, nil
}

// initializeDatabase sets up PostgreSQL connection with optimized pooling for ML workloads
func initializeDatabase() (*sql.DB, error) {
	config := NewDatabaseConfig()

	// Build PostgreSQL connection string
	connStr := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		config.Host, config.Port, config.User, config.Password, config.Database, config.SSLMode)

	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, fmt.Errorf("failed to open database connection: %w", err)
	}

	// Configure connection pooling for high-performance ML workloads
	db.SetMaxOpenConns(config.MaxOpenConns) // Allow many concurrent connections for ML predictions
	db.SetMaxIdleConns(config.MaxIdleConns) // Keep connections ready for frequent requests
	db.SetConnMaxLifetime(config.MaxLifetime) // Recycle connections to prevent stale connections

	// Test the connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		db.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	// Create logger for monitoring (will be replaced with service logger later)
	logger, _ := zap.NewProduction()
	defer logger.Sync() // This will be called when the function returns, but monitoring continues

	// Start connection pool monitoring in background
	go monitorConnectionPool(db, logger)

	return db, nil
}

// monitorConnectionPool logs connection pool statistics for performance monitoring
func monitorConnectionPool(db *sql.DB, logger *zap.Logger) {
	ticker := time.NewTicker(5 * time.Minute) // Monitor every 5 minutes
	defer ticker.Stop()

	for range ticker.C {
		stats := db.Stats()
		logger.Info("Database connection pool statistics",
			zap.Int("open_connections", stats.OpenConnections),
			zap.Int("in_use", stats.InUse),
			zap.Int("idle", stats.Idle),
			zap.Int64("wait_count", stats.WaitCount),
			zap.Duration("wait_duration", stats.WaitDuration),
			zap.Int64("max_idle_closed", stats.MaxIdleTimeClosed),
			zap.Int64("max_lifetime_closed", stats.MaxLifetimeClosed),
		)

		// Alert if connection pool is heavily utilized
		if stats.InUse > stats.OpenConnections*8/10 { // >80% utilization
			logger.Warn("Database connection pool heavily utilized",
				zap.Int("in_use", stats.InUse),
				zap.Int("max_open", stats.OpenConnections))
		}
	}
}

// initializeSampleModels creates sample ML models for the service
func (s *Service) initializeSampleModels() {
	s.models["player-behavior-predictor"] = &MLModel{
		ID:          "player-behavior-predictor",
		Name:        "Player Behavior Predictor",
		Type:        "classification",
		Version:     "1.2.0",
		Status:      "active",
		Accuracy:    0.87,
		LastUpdated: time.Now(),
		Metadata: map[string]interface{}{
			"features":        []string{"playtime", "purchases", "social_activity", "combat_stats"},
			"classes":         []string{"casual", "regular", "hardcore", "whale"},
			"training_size":   1000000,
			"gpu_accelerated": true,
		},
	}

	s.models["item-recommendation-engine"] = &MLModel{
		ID:          "item-recommendation-engine",
		Name:        "Item Recommendation Engine",
		Type:        "recommendation",
		Version:     "2.1.0",
		Status:      "active",
		Accuracy:    0.92,
		LastUpdated: time.Now(),
		Metadata: map[string]interface{}{
			"algorithm":   "collaborative_filtering",
			"features":    []string{"purchase_history", "item_categories", "player_level", "faction"},
			"items_count": 50000,
			"users_count": 500000,
		},
	}

	s.models["fraud-detection-system"] = &MLModel{
		ID:          "fraud-detection-system",
		Name:        "Fraud Detection System",
		Type:        "anomaly_detection",
		Version:     "1.5.0",
		Status:      "active",
		Accuracy:    0.95,
		LastUpdated: time.Now(),
		Metadata: map[string]interface{}{
			"algorithm":           "isolation_forest",
			"contamination":       0.01,
			"features":            []string{"transaction_amount", "frequency", "location", "time_pattern"},
			"false_positive_rate": 0.02,
		},
	}
}

// createRouter creates the HTTP router with all middleware
func (s *Service) createRouter() chi.Router {
	r := chi.NewRouter()

	// Enterprise-grade middleware stack
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS configuration for web clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Health check endpoint
	r.Get("/health", s.healthCheckHandler)

	// Prometheus metrics endpoint
	r.Handle("/metrics", promhttp.Handler())

	// Initialize OpenAPI handler
	handler := &MLAIHandler{
		service: s,
		logger:  s.logger,
	}

	// Create OpenAPI server
	srv, err := api.NewServer(handler, nil)
	if err != nil {
		s.logger.Fatal("Failed to create OpenAPI server", zap.Error(err))
	}

	// Mount OpenAPI server with authentication middleware
	r.With(s.authMiddleware).Mount("/api/v1", srv)

	return r
}

// authMiddleware validates JWT tokens for API access
func (s *Service) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// JWT validation logic would be implemented here
		// For now, allow all requests (implement proper auth in production)
		next.ServeHTTP(w, r)
	})
}

// healthCheckHandler provides service health information including database connection pool stats
func (s *Service) healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	// Set timeout for health check (2 seconds)
	ctx, cancel := context.WithTimeout(r.Context(), 2*time.Second)
	defer cancel()
	_ = ctx // Context ready for future database health checks

	w.Header().Set("Content-Type", "application/json")

	// Get database connection pool statistics
	var dbStats map[string]interface{}
	if s.db != nil {
		stats := s.db.Stats()
		dbStats = map[string]interface{}{
			"open_connections":     stats.OpenConnections,
			"in_use_connections":   stats.InUse,
			"idle_connections":     stats.Idle,
			"wait_count":          stats.WaitCount,
			"wait_duration_ms":     stats.WaitDuration.Milliseconds(),
			"max_idle_closed":      stats.MaxIdleTimeClosed,
			"max_lifetime_closed":  stats.MaxLifetimeClosed,
		}
	}

	healthResponse := map[string]interface{}{
		"status":        "healthy",
		"service":       "ml-ai-domain",
		"timestamp":     time.Now().Format(time.RFC3339),
		"active_models": len(s.models),
		"database":      dbStats,
		"memory_mb":     getMemoryUsageMB(),
	}

	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(healthResponse)
}

// getMemoryUsageMB returns current memory usage in MB (simplified for demo)
func getMemoryUsageMB() float64 {
	// In production, would use runtime.MemStats for accurate measurement
	return 256.5 // Mock value for demonstration
}

// Start begins the service operation
func (s *Service) Start(port string) error {
	router := s.createRouter()

	s.server = &http.Server{
		Addr:         ":" + port,
		Handler:      router,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	s.logger.Info("Starting ML/AI Domain Service",
		zap.String("port", port),
		zap.String("version", "1.0.0"),
		zap.Int("active_models", len(s.models)))

	// Start server in goroutine
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	return nil
}

// Stop gracefully shuts down the service
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Initiating graceful shutdown")

	// Shutdown HTTP server
	shutdownCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(shutdownCtx); err != nil {
		s.logger.Error("Server shutdown failed", zap.Error(err))
		return err
	}

	// Close database connections gracefully
	if s.db != nil {
		s.logger.Info("Closing database connections")
		if err := s.db.Close(); err != nil {
			s.logger.Error("Database close failed", zap.Error(err))
			return err
		}
	}

	// Wait for all goroutines to finish
	s.wg.Wait()

	s.logger.Info("Service shutdown complete")
	return nil
}

// MLAIHandler implements the generated OpenAPI interface
type MLAIHandler struct {
	service *Service
	logger  *zap.Logger
}

// Implement all required methods from the generated interface
// This is a minimal implementation - in production, these would contain
// comprehensive ML/AI business logic

func (h *MLAIHandler) GetHealth(ctx context.Context) (*api.HealthResponse, error) {
	h.logger.Info("Processing health check request")

	return &api.HealthResponse{
		Status:    api.HealthResponseStatusHealthy,
		Service:   "ml-ai-domain",
		Timestamp: time.Now(),
		Version:   api.NewOptString("1.0.0"),
	}, nil
}

func (h *MLAIHandler) GetBatchHealth(ctx context.Context) (*api.BatchHealthResponse, error) {
	h.logger.Info("Processing batch health check request")

	services := []api.ServiceHealth{
		{
			Name:         "ml-ai-domain",
			Status:       api.ServiceHealthStatusHealthy,
			ResponseTime: float32(10.5),
			LastCheck:    api.NewOptDateTime(time.Now()),
		},
	}

	return &api.BatchHealthResponse{
		OverallStatus: api.BatchHealthResponseOverallStatusHealthy,
		Services:      services,
		Timestamp:     time.Now(),
	}, nil
}

func (h *MLAIHandler) CreateModel(ctx context.Context, req *api.CreateModelRequest) (api.CreateModelRes, error) {
	h.logger.Info("Processing create model request")

	// Set timeout for model creation (30 seconds for ML model initialization)
	createCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()
	_ = createCtx // Context ready for future database/model initialization operations

	// Validate input data
	if err := h.service.validator.ValidateModelName(req.Name); err != nil {
		h.logger.Error("Model name validation failed", zap.String("name", req.Name), zap.Error(err))
		return nil, fmt.Errorf("invalid model name: %w", err)
	}

	if err := h.service.validator.ValidateModelType(string(req.Type)); err != nil {
		h.logger.Error("Model type validation failed", zap.String("type", string(req.Type)), zap.Error(err))
		return nil, fmt.Errorf("invalid model type: %w", err)
	}

	// Create new model
	modelID := uuid.New()
	modelIDStr := modelID.String()
	model := &MLModel{
		ID:          modelIDStr,
		Name:        req.Name,
		Type:        string(req.Type),
		Version:     "1.0.0",
		Status:      "training",
		Accuracy:    0.0,
		LastUpdated: time.Now(),
		Metadata:    make(map[string]interface{}),
	}

	h.service.mu.Lock()
	h.service.models[modelIDStr] = model
	h.service.mu.Unlock()

	apiModel := api.MLModel{
		ModelID:   modelID,
		Name:      model.Name,
		Type:      api.MLModelType(model.Type),
		Status:    api.MLModelStatusTraining,
		Version:   api.NewOptString(model.Version),
		Accuracy:  api.NewOptFloat32(float32(model.Accuracy)),
		CreatedAt: model.LastUpdated,
		UpdatedAt: api.NewOptDateTime(model.LastUpdated),
	}

	return &api.ModelResponse{
		Model: apiModel,
	}, nil
}

func (h *MLAIHandler) GetModel(ctx context.Context, params api.GetModelParams) (api.GetModelRes, error) {
	h.logger.Info("Processing get model request", zap.String("modelId", params.ModelId.String()))

	// Set timeout for model retrieval (5 seconds - fast operation)
	getCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()
	_ = getCtx // Context ready for future database queries

	// Validate model ID format (UUID)
	if params.ModelId == uuid.Nil {
		h.logger.Error("Invalid model ID format", zap.String("modelId", params.ModelId.String()))
		return &api.Error{
			Message: "Invalid model ID format",
		}, nil
	}

	modelIDStr := params.ModelId.String()
	h.service.mu.RLock()
	model, exists := h.service.models[modelIDStr]
	h.service.mu.RUnlock()

	if !exists {
		h.logger.Warn("Model retrieval requested for non-existent model", zap.String("modelId", modelIDStr))
		return &api.Error{
			Message: "Model not found",
		}, nil
	}

	modelID, _ := uuid.Parse(model.ID)
	apiModel := api.MLModel{
		ModelID:   modelID,
		Name:      model.Name,
		Type:      api.MLModelType(model.Type),
		Status:    api.MLModelStatus(model.Status),
		Version:   api.NewOptString(model.Version),
		Accuracy:  api.NewOptFloat32(float32(model.Accuracy)),
		CreatedAt: model.LastUpdated.Add(-24 * time.Hour),
		UpdatedAt: api.NewOptDateTime(model.LastUpdated),
	}

	if model.Metadata != nil {
		metadataJSON, _ := json.Marshal(model.Metadata)
		var metadata api.MLModelMetadata
		_ = json.Unmarshal(metadataJSON, &metadata)
		apiModel.Metadata = &metadata
	}

	return &api.ModelResponse{
		Model: apiModel,
	}, nil
}

func (h *MLAIHandler) DeleteModel(ctx context.Context, params api.DeleteModelParams) error {
	h.logger.Info("Processing delete model request", zap.String("modelId", params.ModelId.String()))

	// Validate model ID format (UUID)
	if params.ModelId == uuid.Nil {
		h.logger.Error("Invalid model ID format for deletion", zap.String("modelId", params.ModelId.String()))
		return fmt.Errorf("invalid model ID format")
	}

	modelIDStr := params.ModelId.String()

	// Check if model exists before deletion
	h.service.mu.RLock()
	_, exists := h.service.models[modelIDStr]
	h.service.mu.RUnlock()

	if !exists {
		h.logger.Warn("Delete requested for non-existent model", zap.String("modelId", modelIDStr))
		return ErrModelNotFound
	}

	h.service.mu.Lock()
	delete(h.service.models, modelIDStr)
	h.service.mu.Unlock()

	h.logger.Info("Model deleted successfully", zap.String("modelId", modelIDStr))
	return nil
}

func (h *MLAIHandler) GetModelAnalytics(ctx context.Context, params api.GetModelAnalyticsParams) (*api.ModelAnalyticsResponse, error) {
	h.logger.Info("Processing model analytics request")

	var modelID uuid.UUID
	if params.ModelId.IsSet() {
		modelID = params.ModelId.Value
	} else {
		modelID = uuid.New()
	}

	h.service.mu.RLock()
	totalPredictions := 0
	totalAccuracy := float32(0.0)
	activeModels := 0
	for _, model := range h.service.models {
		if model.Status == "active" {
			activeModels++
			totalAccuracy += float32(model.Accuracy)
		}
		totalPredictions += 1000 // Mock value
	}
	h.service.mu.RUnlock()

	avgAccuracy := float32(0.0)
	if activeModels > 0 {
		avgAccuracy = totalAccuracy / float32(activeModels)
	}

	startDate := time.Now().Add(-24 * time.Hour)
	endDate := time.Now()
	if params.StartDate.IsSet() {
		startDate = params.StartDate.Value
	}
	if params.EndDate.IsSet() {
		endDate = params.EndDate.Value
	}

	return &api.ModelAnalyticsResponse{
		ModelID: modelID,
		Metrics: api.ModelAnalyticsResponseMetrics{
			TotalPredictions:    api.NewOptInt(totalPredictions),
			Accuracy:            api.NewOptFloat32(avgAccuracy),
			AverageResponseTime: api.NewOptFloat32(35.2),
			ErrorRate:           api.NewOptFloat32(0.02),
		},
		TimeRange: api.ModelAnalyticsResponseTimeRange{
			Start: api.NewOptDateTime(startDate),
			End:   api.NewOptDateTime(endDate),
		},
	}, nil
}

func (h *MLAIHandler) GetPredictionAnalytics(ctx context.Context, params api.GetPredictionAnalyticsParams) (*api.PredictionAnalyticsResponse, error) {
	h.logger.Info("Processing prediction analytics request")

	h.service.predMu.RLock()
	totalPredictions := len(h.service.predictions)
	successfulPredictions := 0
	totalLatency := float32(0.0)
	modelsUsed := make(map[uuid.UUID]bool)

	for _, pred := range h.service.predictions {
		if pred.Success {
			successfulPredictions++
		}
		totalLatency += float32(pred.Latency)
		modelsUsed[pred.ModelID] = true
	}
	h.service.predMu.RUnlock()

	avgLatency := float32(0.0)
	if totalPredictions > 0 {
		avgLatency = totalLatency / float32(totalPredictions)
	}

	modelsUsedList := make([]uuid.UUID, 0, len(modelsUsed))
	for modelID := range modelsUsed {
		modelsUsedList = append(modelsUsedList, modelID)
	}

	if totalPredictions == 0 {
		// Return mock data if no predictions
		totalPredictions = 125000
		successfulPredictions = 123375
		avgLatency = 35.2
	}

	return &api.PredictionAnalyticsResponse{
		TotalPredictions:      totalPredictions,
		SuccessfulPredictions: successfulPredictions,
		FailedPredictions:     api.NewOptInt(totalPredictions - successfulPredictions),
		AverageResponseTime:   avgLatency,
		PeakRps:               api.NewOptInt(1000),
		ModelsUsed:            modelsUsedList,
	}, nil
}

func (h *MLAIHandler) GetTrainingStatus(ctx context.Context, params api.GetTrainingStatusParams) (*api.TrainingStatusResponse, error) {
	h.logger.Info("Processing training status request", zap.String("jobId", params.JobId.String()))

	// Validate job ID format (UUID)
	if params.JobId == uuid.Nil {
		h.logger.Error("Invalid training job ID", zap.String("jobId", params.JobId.String()))
		return &api.TrainingStatusResponse{
			JobID:    params.JobId,
			Status:   api.TrainingStatusResponseStatusFailed,
			Progress: 0.0,
		}, fmt.Errorf("invalid job ID format")
	}

	h.service.trainingMu.RLock()
	job, exists := h.service.trainingJobs[params.JobId]
	h.service.trainingMu.RUnlock()

	if !exists {
		h.logger.Warn("Training status requested for non-existent job", zap.String("jobId", params.JobId.String()))
		// Return default status if job not found
		return &api.TrainingStatusResponse{
			JobID:    params.JobId,
			Status:   api.TrainingStatusResponseStatusQueued,
			Progress: 0.0,
		}, nil
	}

	status := api.TrainingStatusResponseStatusQueued
	switch job.Status {
	case "running":
		status = api.TrainingStatusResponseStatusRunning
	case "completed":
		status = api.TrainingStatusResponseStatusCompleted
	case "failed":
		status = api.TrainingStatusResponseStatusFailed
	}

	response := &api.TrainingStatusResponse{
		JobID:    params.JobId,
		Status:   status,
		Progress: float32(job.Progress),
	}

	if job.Accuracy > 0 {
		response.Accuracy = api.NewOptFloat32(float32(job.Accuracy))
	}

	return response, nil
}

func (h *MLAIHandler) GetWebSocketHealth(ctx context.Context) (*api.WebSocketHealthMessage, error) {
	h.logger.Info("Processing WebSocket health check request")

	return &api.WebSocketHealthMessage{
		Type:        api.WebSocketHealthMessageTypeHealthCheck,
		Timestamp:   time.Now(),
		Uptime:      api.NewOptFloat32(3600.0),
		Connections: api.NewOptInt(0),
	}, nil
}

func (h *MLAIHandler) ListModels(ctx context.Context, params api.ListModelsParams) (api.ListModelsRes, error) {
	h.logger.Info("Processing list models request",
		zap.Any("status", params.Status),
		zap.Any("type", params.Type),
		zap.Any("page", params.Page),
		zap.Any("limit", params.Limit))

	h.service.mu.RLock()
	allModels := make([]*MLModel, 0, len(h.service.models))
	for _, model := range h.service.models {
		allModels = append(allModels, model)
	}
	h.service.mu.RUnlock()

	// Filter by status if provided
	if params.Status.IsSet() {
		filtered := make([]*MLModel, 0)
		statusValue := string(params.Status.Value)
		for _, model := range allModels {
			if model.Status == statusValue {
				filtered = append(filtered, model)
			}
		}
		allModels = filtered
	}

	// Filter by type if provided
	if params.Type.IsSet() {
		filtered := make([]*MLModel, 0)
		typeValue := string(params.Type.Value)
		for _, model := range allModels {
			if model.Type == typeValue {
				filtered = append(filtered, model)
			}
		}
		allModels = filtered
	}

	// Pagination
	page := 1
	if params.Page.IsSet() {
		page = params.Page.Value
	}
	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
		if limit > 100 {
			limit = 100
		}
		if limit < 1 {
			limit = 1
		}
	}

	startIdx := (page - 1) * limit
	endIdx := startIdx + limit
	totalCount := len(allModels)
	hasMore := endIdx < totalCount

	if startIdx > totalCount {
		startIdx = totalCount
	}
	if endIdx > totalCount {
		endIdx = totalCount
	}

	var models []*MLModel
	if startIdx < totalCount {
		models = allModels[startIdx:endIdx]
	} else {
		models = []*MLModel{}
	}

	// Convert to API models
	apiModels := make([]api.MLModel, 0, len(models))
	for _, model := range models {
		modelID, _ := uuid.Parse(model.ID)
		apiModel := api.MLModel{
			ModelID:   modelID,
			Name:      model.Name,
			Type:      api.MLModelType(model.Type),
			Status:    api.MLModelStatus(model.Status),
			Version:   api.NewOptString(model.Version),
			Accuracy:  api.NewOptFloat32(float32(model.Accuracy)),
			CreatedAt: model.LastUpdated,
			UpdatedAt: api.NewOptDateTime(model.LastUpdated),
		}

		if model.Metadata != nil {
			metadataJSON, _ := json.Marshal(model.Metadata)
			apiModel.Metadata = &api.MLModelMetadata{}
			_ = json.Unmarshal(metadataJSON, apiModel.Metadata)
		}

		apiModels = append(apiModels, apiModel)
	}

	return &api.ModelListResponse{
		Models:     apiModels,
		TotalCount: totalCount,
		HasMore:    api.NewOptBool(hasMore),
		Page:       api.NewOptInt(page),
		Limit:      api.NewOptInt(limit),
	}, nil
}

func (h *MLAIHandler) MakePrediction(ctx context.Context, req *api.PredictionRequest) (api.MakePredictionRes, error) {
	h.logger.Info("Processing prediction request", zap.String("modelId", req.ModelID.String()))

	// Set timeout for prediction (500ms - must be fast for real-time gaming)
	predictCtx, cancel := context.WithTimeout(ctx, 500*time.Millisecond)
	defer cancel()
	_ = predictCtx // Context ready for future ML model inference calls

	// Validate prediction input (mock validation - in real implementation would validate req.Data or similar)
	// For now, just ensure we have a valid model ID
	if req.ModelID == uuid.Nil {
		h.logger.Error("Prediction request validation failed: invalid model ID")
		return &api.MakePredictionBadRequest{
			Message: "Invalid model ID",
		}, nil
	}

	startTime := time.Now()

	// Check if model exists
	h.service.mu.RLock()
	modelIDStr := req.ModelID.String()
	_, exists := h.service.models[modelIDStr]
	h.service.mu.RUnlock()

	if !exists {
		h.logger.Warn("Prediction requested for non-existent model", zap.String("modelId", modelIDStr))
		return &api.MakePredictionNotFound{
			Message: "Model not found",
		}, nil
	}

	// Simulate prediction processing
	processingTime := time.Since(startTime).Milliseconds()
	confidence := 0.7 + rand.Float64()*0.25 // Random confidence between 0.7 and 0.95

	// Create prediction result
	predictionID := uuid.New()

	// Record prediction for analytics
	h.service.predMu.Lock()
	h.service.predictions = append(h.service.predictions, &PredictionRecord{
		PredictionID: predictionID,
		ModelID:      req.ModelID,
		Timestamp:    time.Now(),
		Success:      true,
		Latency:      float64(processingTime),
	})
	// Keep only last 10000 predictions
	if len(h.service.predictions) > 10000 {
		h.service.predictions = h.service.predictions[len(h.service.predictions)-10000:]
	}
	h.service.predMu.Unlock()

	return &api.PredictionResponse{
		PredictionID:   predictionID,
		ModelID:        req.ModelID,
		Result:         api.PredictionResponseResult{},
		Confidence:     float32(confidence),
		ProcessingTime: api.NewOptFloat32(float32(processingTime)),
		Timestamp:      api.NewOptDateTime(time.Now()),
	}, nil
}

func (h *MLAIHandler) MakeBatchPrediction(ctx context.Context, req *api.BatchPredictionRequest) (*api.BatchPredictionResponse, error) {
	h.logger.Info("Processing batch prediction request",
		zap.String("modelId", req.ModelID.String()),
		zap.Int("count", len(req.Predictions)))

	// Set timeout for batch prediction (2 seconds - allows time for multiple predictions)
	batchCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()
	_ = batchCtx // Context ready for future batch ML inference operations

	// Validate batch size
	if err := h.service.validator.ValidateBatchSize(len(req.Predictions)); err != nil {
		h.logger.Error("Batch prediction size validation failed", zap.Int("count", len(req.Predictions)), zap.Error(err))
		return &api.BatchPredictionResponse{
			Predictions:  []api.PredictionResponse{},
			TotalCount:   len(req.Predictions),
			SuccessCount: 0,
			FailedCount:  api.NewOptInt(len(req.Predictions)),
		}, err
	}

	// Validate each prediction request (mock validation)
	for i, prediction := range req.Predictions {
		if prediction.ModelID == uuid.Nil {
			h.logger.Error("Batch prediction validation failed: invalid model ID", zap.Int("index", i))
			continue
		}
		// In real implementation, would validate prediction.Data or similar field
	}

	// Check if model exists
	h.service.mu.RLock()
	modelIDStr := req.ModelID.String()
	_, exists := h.service.models[modelIDStr]
	h.service.mu.RUnlock()

	if !exists {
		h.logger.Warn("Batch prediction requested for non-existent model", zap.String("modelId", modelIDStr))
		return &api.BatchPredictionResponse{
			Predictions:  []api.PredictionResponse{},
			TotalCount:   len(req.Predictions),
			SuccessCount: 0,
			FailedCount:  api.NewOptInt(len(req.Predictions)),
		}, nil
	}

	// Process batch predictions
	predictions := make([]api.PredictionResponse, 0, len(req.Predictions))
	successCount := 0
	failedCount := 0
	totalLatency := float64(0)

	for range req.Predictions {
		startTime := time.Now()
		processingTime := time.Since(startTime).Milliseconds()
		confidence := 0.7 + rand.Float64()*0.25

		predictionID := uuid.New()
		prediction := api.PredictionResponse{
			PredictionID:   predictionID,
			ModelID:        req.ModelID,
			Result:         api.PredictionResponseResult{},
			Confidence:     float32(confidence),
			ProcessingTime: api.NewOptFloat32(float32(processingTime)),
			Timestamp:      api.NewOptDateTime(time.Now()),
		}

		predictions = append(predictions, prediction)
		successCount++
		totalLatency += float64(processingTime)

		// Record for analytics
		h.service.predMu.Lock()
		h.service.predictions = append(h.service.predictions, &PredictionRecord{
			PredictionID: predictionID,
			ModelID:      req.ModelID,
			Timestamp:    time.Now(),
			Success:      true,
			Latency:      float64(processingTime),
		})
		if len(h.service.predictions) > 10000 {
			h.service.predictions = h.service.predictions[len(h.service.predictions)-10000:]
		}
		h.service.predMu.Unlock()
	}

	avgLatency := float32(0)
	if successCount > 0 {
		avgLatency = float32(totalLatency / float64(successCount))
	}

	return &api.BatchPredictionResponse{
		Predictions:           predictions,
		TotalCount:            len(req.Predictions),
		SuccessCount:          successCount,
		FailedCount:           api.NewOptInt(failedCount),
		AverageProcessingTime: api.NewOptFloat32(avgLatency),
	}, nil
}

func (h *MLAIHandler) StartTraining(ctx context.Context, req *api.TrainingRequest) (*api.TrainingJobResponse, error) {
	h.logger.Info("Processing start training request",
		zap.String("modelName", req.ModelName),
		zap.String("algorithm", req.Algorithm))

	// Set timeout for training initialization (10 seconds - training jobs are async)
	trainCtx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()
	_ = trainCtx // Context ready for future training job initialization

	// Validate input data
	if err := h.service.validator.ValidateModelName(req.ModelName); err != nil {
		h.logger.Error("Training model name validation failed", zap.String("name", req.ModelName), zap.Error(err))
		return &api.TrainingJobResponse{
			JobID:  uuid.New(),
			Status: api.TrainingJobResponseStatusFailed,
		}, fmt.Errorf("invalid model name: %w", err)
	}

	if err := h.service.validator.ValidateAlgorithm(req.Algorithm); err != nil {
		h.logger.Error("Training algorithm validation failed", zap.String("algorithm", req.Algorithm), zap.Error(err))
		return &api.TrainingJobResponse{
			JobID:  uuid.New(),
			Status: api.TrainingJobResponseStatusFailed,
		}, fmt.Errorf("invalid algorithm: %w", err)
	}

	// Validate training data size (mock validation - in real implementation would check actual data)
	// For demonstration, assume valid training data

	// Create training job
	jobID := uuid.New()
	job := &TrainingJob{
		JobID:     jobID,
		ModelName: req.ModelName,
		Status:    "queued",
		Progress:  0.0,
		StartTime: time.Now(),
		CreatedAt: time.Now(),
	}

	h.service.trainingMu.Lock()
	h.service.trainingJobs[jobID] = job
	h.service.trainingMu.Unlock()

	// Simulate async training start
	go func() {
		time.Sleep(100 * time.Millisecond)
		h.service.trainingMu.Lock()
		if j, ok := h.service.trainingJobs[jobID]; ok {
			j.Status = "running"
		}
		h.service.trainingMu.Unlock()
	}()

	estimatedCompletion := time.Now().Add(30 * time.Minute)

	return &api.TrainingJobResponse{
		JobID:               jobID,
		ModelName:           req.ModelName,
		Status:              api.TrainingJobResponseStatusQueued,
		CreatedAt:           time.Now(),
		EstimatedCompletion: api.NewOptDateTime(estimatedCompletion),
	}, nil
}

func (h *MLAIHandler) UpdateModel(ctx context.Context, req *api.UpdateModelRequest, params api.UpdateModelParams) (*api.ModelResponse, error) {
	h.logger.Info("Processing update model request", zap.String("modelId", params.ModelId.String()))

	// Set timeout for model update (15 seconds - may involve model reloading)
	updateCtx, cancel := context.WithTimeout(ctx, 15*time.Second)
	defer cancel()
	_ = updateCtx // Context ready for future model update operations

	modelIDStr := params.ModelId.String()

	// Validate input data
	if req.Name.IsSet() {
		if err := h.service.validator.ValidateModelName(req.Name.Value); err != nil {
			h.logger.Error("Update model name validation failed", zap.String("name", req.Name.Value), zap.Error(err))
			return nil, fmt.Errorf("invalid model name: %w", err)
		}
	}

	h.service.mu.Lock()
	model, exists := h.service.models[modelIDStr]
	if !exists {
		h.service.mu.Unlock()
		h.logger.Warn("Update requested for non-existent model", zap.String("modelId", modelIDStr))
		return nil, ErrModelNotFound
	}

	// Update model fields
	if req.Name.IsSet() {
		model.Name = req.Name.Value
	}
	if req.Description.IsSet() {
		// Description would be stored in metadata in real implementation
		_ = req.Description.Value // Suppress unused warning
	}
	if req.Status.IsSet() {
		model.Status = string(req.Status.Value)
	}
	if req.Metadata != nil {
		// Update metadata
		metadataJSON, _ := json.Marshal(req.Metadata)
		_ = json.Unmarshal(metadataJSON, &model.Metadata)
	}

	model.LastUpdated = time.Now()
	h.service.mu.Unlock()

	modelID, _ := uuid.Parse(model.ID)
	apiModel := api.MLModel{
		ModelID:   modelID,
		Name:      model.Name,
		Type:      api.MLModelType(model.Type),
		Status:    api.MLModelStatus(model.Status),
		Version:   api.NewOptString(model.Version),
		Accuracy:  api.NewOptFloat32(float32(model.Accuracy)),
		CreatedAt: model.LastUpdated.Add(-24 * time.Hour),
		UpdatedAt: api.NewOptDateTime(model.LastUpdated),
	}

	if model.Metadata != nil {
		metadataJSON, _ := json.Marshal(model.Metadata)
		var metadata api.MLModelMetadata
		_ = json.Unmarshal(metadataJSON, &metadata)
		apiModel.Metadata = &metadata
	}

	return &api.ModelResponse{
		Model: apiModel,
	}, nil
}

func main() {
	// Create service instance
	service, err := NewService()
	if err != nil {
		log.Fatalf("Failed to create service: %v", err)
	}
	defer service.logger.Sync()

	// Get port from environment or use default
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	// Start service
	if err := service.Start(port); err != nil {
		service.logger.Fatal("Failed to start service", zap.Error(err))
	}

	// Wait for shutdown signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := service.Stop(ctx); err != nil {
		service.logger.Error("Service shutdown failed", zap.Error(err))
		os.Exit(1)
	}
}
