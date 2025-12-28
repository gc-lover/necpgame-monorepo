// Issue: Implement integration-domain-service-go
// Business logic layer for integration domain service
// Enterprise-grade service with circuit breaker, health monitoring, and performance optimizations

package service

import (
	"context"
	"crypto/tls"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/internal/config"
	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/internal/metrics"
	"github.com/gc-lover/necpgame-monorepo/services/integration-domain-service-go/pkg/models"
	"go.uber.org/zap"
)

// IntegrationDomainService implements the core business logic
type IntegrationDomainService struct {
	config             *config.Config
	logger             *zap.Logger
	metrics            *metrics.Metrics
	circuitBreakers    map[string]*CircuitBreaker
	circuitBreakersMu  sync.RWMutex
	domainStatuses     map[string]models.DomainStatus
	domainStatusesMu   sync.RWMutex
	httpClient         *http.Client
	websocketManager   *WebSocketManager
}

// NewIntegrationDomainService creates a new integration domain service
func NewIntegrationDomainService(logger *zap.Logger, cfg *config.Config) (*IntegrationDomainService, error) {
	// Initialize HTTP client with timeouts
	httpClient := &http.Client{
		Timeout: cfg.HealthCheckTimeout,
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{
				InsecureSkipVerify: false, // Always verify in production
			},
			MaxIdleConns:        100,
			MaxIdleConnsPerHost: 10,
			IdleConnTimeout:     90 * time.Second,
		},
	}

	svc := &IntegrationDomainService{
		config:          cfg,
		logger:          logger,
		metrics:         metrics.NewMetrics(),
		circuitBreakers: make(map[string]*CircuitBreaker),
		domainStatuses:  make(map[string]models.DomainStatus),
		httpClient:      httpClient,
	}

	// Initialize circuit breakers for each integration domain
	domains := []string{"webhooks", "callbacks", "bridges", "events", "api-gateway"}
	for _, domain := range domains {
		cb := NewCircuitBreaker(cfg.CircuitBreakerTimeout, cfg.CircuitBreakerMaxRequests, cfg.CircuitBreakerInterval)
		svc.circuitBreakers[domain] = cb
	}

	// Initialize WebSocket manager if enabled
	if cfg.EnableWebSocket {
		svc.websocketManager = NewWebSocketManager(logger, cfg)
		go svc.websocketManager.Start()
	}

	// Start background health monitoring
	go svc.startHealthMonitoring()

	return svc, nil
}

// HealthCheck performs a comprehensive health check of the integration domain
func (s *IntegrationDomainService) HealthCheck(ctx context.Context) models.HealthResponse {
	startTime := time.Now()

	response := models.HealthResponse{
		Domain:    "integration-domain",
		Status:    "healthy",
		Timestamp: startTime,
	}

	// Check all integration subsystems
	domains := []string{"webhooks", "callbacks", "bridges", "events", "api-gateway"}
	allHealthy := true

	for _, domain := range domains {
		if status := s.checkDomainHealth(ctx, domain); status != "healthy" {
			allHealthy = false
			break
		}
	}

	if !allHealthy {
		response.Status = "degraded"
	}

	// Record metrics
	duration := time.Since(startTime)
	s.metrics.RecordHealthCheck(duration, response.Status == "healthy")

	s.logger.Info("Health check completed",
		zap.String("status", response.Status),
		zap.Duration("duration", duration))

	return response
}

// DomainHealthCheck checks the health of the integration domain specifically
func (s *IntegrationDomainService) DomainHealthCheck(ctx context.Context) models.HealthResponse {
	return s.HealthCheck(ctx)
}

// BatchHealthCheck performs health checks for multiple domains
func (s *IntegrationDomainService) BatchHealthCheck(ctx context.Context, req models.BatchHealthRequest) models.BatchHealthResponse {
	startTime := time.Now()
	results := make([]models.HealthResponse, 0, len(req.Domains))

	for _, domain := range req.Domains {
		result := models.HealthResponse{
			Domain:    domain,
			Status:    s.checkDomainHealth(ctx, domain),
			Timestamp: time.Now(),
		}
		results = append(results, result)
	}

	totalTimeMs := int(time.Since(startTime).Milliseconds())

	response := models.BatchHealthResponse{
		Results:     results,
		TotalTimeMs: totalTimeMs,
	}

	s.metrics.RecordBatchHealthCheck(len(req.Domains), totalTimeMs)

	return response
}

// checkDomainHealth checks the health of a specific domain
func (s *IntegrationDomainService) checkDomainHealth(ctx context.Context, domain string) string {
	cb, exists := s.circuitBreakers[domain]
	if !exists {
		return "unknown"
	}

	// Use circuit breaker to check domain health
	result := cb.Call(func() (interface{}, error) {
		return s.performHealthCheck(ctx, domain)
	})

	if result.Error != nil {
		s.logger.Warn("Domain health check failed",
			zap.String("domain", domain),
			zap.Error(result.Error))
		return "unhealthy"
	}

	status, ok := result.Value.(string)
	if !ok {
		return "unknown"
	}

	return status
}

// performHealthCheck performs the actual health check for a domain
func (s *IntegrationDomainService) performHealthCheck(ctx context.Context, domain string) (string, error) {
	// Get domain endpoint from configuration
	endpoint, exists := s.config.ServiceEndpoints[domain]
	if !exists {
		// Use default endpoint pattern for integration services
		switch domain {
		case "webhooks":
			endpoint = "http://webhook-service:8080/health"
		case "callbacks":
			endpoint = "http://callback-service:8080/health"
		case "bridges":
			endpoint = "http://bridge-service:8080/health"
		case "events":
			endpoint = "http://event-service:8080/health"
		case "api-gateway":
			endpoint = "http://api-gateway-service:8080/health"
		default:
			endpoint = fmt.Sprintf("http://%s-service:8080/health", domain)
		}
	}

	req, err := http.NewRequestWithContext(ctx, "GET", endpoint, nil)
	if err != nil {
		return "", err
	}

	resp, err := s.httpClient.Do(req)
	if err != nil {
		return "unhealthy", err
	}
	defer resp.Body.Close()

	if resp.StatusCode >= 200 && resp.StatusCode < 300 {
		return "healthy", nil
	}

	return "unhealthy", fmt.Errorf("status code: %d", resp.StatusCode)
}

// GetMetrics returns current metrics snapshot
func (s *IntegrationDomainService) GetMetrics() models.MetricsSnapshot {
	s.circuitBreakersMu.RLock()
	circuitBreakers := make([]models.CircuitBreakerState, 0, len(s.circuitBreakers))
	for name, cb := range s.circuitBreakers {
		circuitBreakers = append(circuitBreakers, cb.GetState(name))
	}
	s.circuitBreakersMu.RUnlock()

	s.domainStatusesMu.RLock()
	domains := make(map[string]models.DomainStatus)
	for k, v := range s.domainStatuses {
		domains[k] = v
	}
	s.domainStatusesMu.RUnlock()

	return models.MetricsSnapshot{
		Timestamp:          time.Now(),
		ActiveConnections:  s.websocketManager.GetActiveConnections(),
		TotalRequests:      s.metrics.GetTotalRequests(),
		SuccessfulRequests: s.metrics.GetSuccessfulRequests(),
		FailedRequests:     s.metrics.GetFailedRequests(),
		AverageResponseTime: s.metrics.GetAverageResponseTime(),
		CircuitBreakers:    circuitBreakers,
		Domains:           domains,
	}
}

// BroadcastHealthUpdate sends health update to all WebSocket connections
func (s *IntegrationDomainService) BroadcastHealthUpdate(health models.HealthResponse) {
	if s.websocketManager != nil {
		message := models.WebSocketHealthMessage{
			Type:             "health_update",
			MessageTimestamp: time.Now(),
			Health:          health,
		}
		s.websocketManager.Broadcast(message)
	}
}

// GetWebSocketManager returns the WebSocket manager
func (s *IntegrationDomainService) GetWebSocketManager() *WebSocketManager {
	return s.websocketManager
}

// startHealthMonitoring starts background health monitoring
func (s *IntegrationDomainService) startHealthMonitoring() {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)

			health := s.HealthCheck(ctx)
			if health.Status != "healthy" {
				// Broadcast alert if service is unhealthy
				alertMessage := models.WebSocketHealthMessage{
					Type:             "health_alert",
					MessageTimestamp: time.Now(),
					Health:          health,
				}
				if s.websocketManager != nil {
					s.websocketManager.Broadcast(alertMessage)
				}
			}

			cancel()
		}
	}
}


