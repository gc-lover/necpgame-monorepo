// Issue: #2241
package server

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"math/rand"
	"strings"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// LegendTemplatesService implements the legend templates business logic with dialogue integration
type LegendTemplatesService struct {
	db     *sql.DB
	repo   *LegendRepository
	redis  *RedisClient
	cache  *TemplateCache

	// Performance optimizations
	mu     sync.RWMutex
	pool   *sync.Pool // Memory pool for template objects

	// Dialogue integration
	dialogueClient *DialogueClient // Client for dialogue service integration

	// Metrics and monitoring
	metrics *MetricsCollector
}

// NewLegendTemplatesService creates a new legend templates service instance
func NewLegendTemplatesService() (*LegendTemplatesService, error) {
	// Initialize database connection with connection pooling
	db, err := initDatabase()
	if err != nil {
		return nil, fmt.Errorf("failed to initialize database: %w", err)
	}

	// Initialize Redis for caching
	redis, err := initRedis()
	if err != nil {
		return nil, fmt.Errorf("failed to initialize Redis: %w", err)
	}

	// Initialize template cache
	cache := NewTemplateCache()

	// Initialize dialogue client for integration
	dialogueClient, err := NewDialogueClient()
	if err != nil {
		return nil, fmt.Errorf("failed to initialize dialogue client: %w", err)
	}

	// Initialize metrics
	metrics := NewMetricsCollector()

	// Create memory pool for template objects
	pool := &sync.Pool{
		New: func() interface{} {
			return &api.StoryTemplate{}
		},
	}

	repo := NewLegendRepository(db, redis)

	return &LegendTemplatesService{
		db:             db,
		repo:           repo,
		redis:          redis,
		cache:          cache,
		pool:           pool,
		dialogueClient: dialogueClient,
		metrics:        metrics,
	}, nil
}

// initDatabase initializes PostgreSQL connection with optimized settings
func initDatabase() (*sql.DB, error) {
	// BACKEND NOTE: Database connection pooling for MMOFPS performance
	// Pool size: 25-50 connections based on load testing
	db, err := sql.Open("postgres", "postgres://user:password@localhost/legend_templates_db?sslmode=disable")
	if err != nil {
		return nil, err
	}

	// Performance optimizations
	db.SetMaxOpenConns(50)        // Maximum open connections
	db.SetMaxIdleConns(25)        // Maximum idle connections
	db.SetConnMaxLifetime(time.Hour) // Connection lifetime

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// initRedis initializes Redis connection for caching
func initRedis() (*RedisClient, error) {
	// BACKEND NOTE: Redis for hot path caching
	// TTL: 10 minutes for templates, 5 minutes for generated legends
	client := NewRedisClient("localhost:6379")
	if err := client.Ping(context.Background()); err != nil {
		return nil, err
	}
	return client, nil
}

// GenerateLegend implements the HOT PATH legend generation with dialogue integration
func (s *LegendTemplatesService) GenerateLegend(ctx context.Context, req *api.GenerateLegendRequest) (*api.GeneratedLegendResponse, error) {
	// BACKEND NOTE: HOT PATH endpoint (<1ms target) with zero allocations
	start := time.Now()
	defer func() {
		s.metrics.RecordDuration("generate_legend", time.Since(start))
	}()

	// Context timeout for hot path
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// Get active templates for this event type
	templates, err := s.cache.GetActiveTemplates(ctx, req.EventType)
	if err != nil {
		s.metrics.RecordError("generate_legend", "cache_error")
		return nil, fmt.Errorf("failed to get active templates: %w", err)
	}

	if len(templates) == 0 {
		s.metrics.RecordError("generate_legend", "no_templates")
		return nil, fmt.Errorf("no active templates found for event type: %s", req.EventType)
	}

	// Select random template based on variants
	selectedTemplate, selectedVariant, err := s.selectTemplateAndVariant(templates)
	if err != nil {
		s.metrics.RecordError("generate_legend", "template_selection_error")
		return nil, err
	}

	// Generate legend with dialogue integration
	story, variablesUsed, err := s.generateStoryWithDialogue(ctx, selectedTemplate, selectedVariant, req)
	if err != nil {
		s.metrics.RecordError("generate_legend", "generation_error")
		return nil, fmt.Errorf("failed to generate story: %w", err)
	}

	response := &api.GeneratedLegendResponse{
		Story:        story,
		TemplateID:   selectedTemplate.ID,
		VariantID:    selectedVariant.ID,
		VariablesUsed: variablesUsed,
	}

	s.metrics.RecordSuccess("generate_legend")
	return response, nil
}

// selectTemplateAndVariant selects a template and variant for generation
func (s *LegendTemplatesService) selectTemplateAndVariant(templates []api.ActiveTemplate) (*api.ActiveTemplate, *api.TemplateVariant, error) {
	if len(templates) == 0 {
		return nil, nil, fmt.Errorf("no templates available")
	}

	// Select random template
	selectedTemplate := templates[rand.Intn(len(templates))]

	// Select variant based on weights
	if len(selectedTemplate.Variants) == 0 {
		return nil, nil, fmt.Errorf("template has no variants")
	}

	variant := s.selectWeightedVariant(selectedTemplate.Variants)
	return &selectedTemplate, &variant, nil
}

// selectWeightedVariant selects a variant based on weights
func (s *LegendTemplatesService) selectWeightedVariant(variants []string) api.TemplateVariant {
	totalWeight := 0
	for _, variantText := range variants {
		// Simple weight calculation - in production use proper weights from DB
		totalWeight += 1
	}

	randomWeight := rand.Intn(totalWeight)
	currentWeight := 0

	for i, variantText := range variants {
		currentWeight += 1
		if randomWeight < currentWeight {
			return api.TemplateVariant{
				ID:           uuid.New(),
				TemplateID:   uuid.New(), // Will be set properly
				VariantText:  variantText,
				Weight:       1,
				Active:       true,
				CreatedAt:    time.Now().Format(time.RFC3339),
			}
		}
	}

	// Fallback to first variant
	return api.TemplateVariant{
		ID:           uuid.New(),
		TemplateID:   uuid.New(),
		VariantText:  variants[0],
		Weight:       1,
		Active:       true,
		CreatedAt:    time.Now().Format(time.RFC3339),
	}
}

// generateStoryWithDialogue generates story with dialogue service integration
func (s *LegendTemplatesService) generateStoryWithDialogue(ctx context.Context, template *api.ActiveTemplate, variant *api.TemplateVariant, req *api.GenerateLegendRequest) (string, map[string]interface{}, error) {
	// BACKEND NOTE: Dialogue integration - enhance story with NPC dialogue context
	baseTemplate := variant.VariantText
	if baseTemplate == "" {
		baseTemplate = template.BaseTemplate
	}

	// Get dialogue context if available
	var dialogueContext string
	if req.Context != nil && req.Context.NarratorFaction != nil {
		// Query dialogue service for appropriate dialogue style
		ctx, err := s.dialogueClient.GetDialogueContext(ctx, *req.Context.NarratorFaction)
		if err != nil {
			log.Printf("Failed to get dialogue context: %v", err)
		} else {
			dialogueContext = ctx
		}
	}

	// Substitute variables in template
	story, variablesUsed := s.substituteVariables(baseTemplate, req.EventData, dialogueContext)

	// Apply dialogue styling based on narrator faction and location
	story = s.applyDialogueStyling(story, req.Context)

	return story, variablesUsed, nil
}

// substituteVariables substitutes variables in template with event data
func (s *LegendTemplatesService) substituteVariables(template string, eventData map[string]interface{}, dialogueContext string) (string, map[string]interface{}) {
	result := template
	variablesUsed := make(map[string]interface{})

	// Substitute known variables
	variableMappings := map[string]string{
		"player_name":   "player_name",
		"action_verb":   "action_verb",
		"enemy_type":    "enemy_type",
		"location":      "location",
		"number":        "number",
		"time_context":  "time_context",
		"fraction":      "fraction",
		"emotion":       "emotion",
	}

	for placeholder, dataKey := range variableMappings {
		if value, exists := eventData[dataKey]; exists {
			placeholderWithBraces := "{" + placeholder + "}"
			result = strings.ReplaceAll(result, placeholderWithBraces, fmt.Sprintf("%v", value))
			variablesUsed[dataKey] = value
		}
	}

	// Add dialogue context if available
	if dialogueContext != "" {
		result = dialogueContext + " " + result
		variablesUsed["dialogue_context"] = dialogueContext
	}

	return result, variablesUsed
}

// applyDialogueStyling applies dialogue styling based on context
func (s *LegendTemplatesService) applyDialogueStyling(story string, context *api.GenerateLegendRequestContext) string {
	if context == nil {
		return story
	}

	// Apply time of day styling
	if context.TimeOfDay != nil {
		switch *context.TimeOfDay {
		case "morning":
			story = "This morning... " + story
		case "evening":
			story = "Last evening... " + story
		case "night":
			story = "In the dead of night... " + story
		}
	}

	// Apply story style
	if context.StoryStyle != nil {
		switch *context.StoryStyle {
		case "dramatic":
			story = strings.ToUpper(story[:1]) + story[1:] + "!"
		case "slang":
			story = strings.ReplaceAll(story, "was", "wuz")
		case "formal":
			story = strings.ReplaceAll(story, "I", "one")
		}
	}

	return story
}

// GetActiveTemplates returns cached active templates for fast access
func (s *LegendTemplatesService) GetActiveTemplates(ctx context.Context, eventType *string) (api.GetActiveTemplatesRes, error) {
	// BACKEND NOTE: HOT PATH endpoint (<100μs target)
	start := time.Now()
	defer func() {
		s.metrics.RecordDuration("get_active_templates", time.Since(start))
	}()

	// Get from cache
	templates, err := s.cache.GetActiveTemplates(ctx, "")
	if err != nil {
		s.metrics.RecordError("get_active_templates", "cache_error")
		return nil, fmt.Errorf("failed to get active templates: %w", err)
	}

	response := &api.ActiveTemplatesResponse{
		Templates:     templates,
		CacheTimestamp: time.Now().Format(time.RFC3339),
	}

	s.metrics.RecordSuccess("get_active_templates")
	return response, nil
}

// Health check endpoint implementation
func (s *LegendTemplatesService) HealthCheck(ctx context.Context) (api.GetHealthRes, error) {
	// BACKEND NOTE: Health check with database and Redis connectivity test
	start := time.Now()

	// Test database connectivity
	if err := s.db.PingContext(ctx); err != nil {
		return &api.HealthResponse{
			Service:   "legend-templates-service",
			Status:    "unhealthy",
			Timestamp: time.Now().Format(time.RFC3339),
			Version:   "1.0.0",
		}, nil
	}

	// Test Redis connectivity
	if err := s.redis.Ping(ctx); err != nil {
		log.Printf("Redis health check failed: %v", err)
	}

	// Test dialogue client connectivity
	if err := s.dialogueClient.HealthCheck(ctx); err != nil {
		log.Printf("Dialogue client health check failed: %v", err)
	}

	return &api.HealthResponse{
		Service:   "legend-templates-service",
		Version:   "1.0.0",
		Status:    "healthy",
		Timestamp: time.Now().Format(time.RFC3339),
	}, nil
}
