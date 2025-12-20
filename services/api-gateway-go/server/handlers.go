// Package server Issue: #146073424
package server

import (
	"encoding/json"
	"net/http"
	"time"
)

// healthCheckHandler проверяет здоровье API Gateway
func (g *APIGateway) healthCheckHandler(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	response := map[string]interface{}{
		"status":    "healthy",
		"service":   "api-gateway",
		"version":   "1.0.0",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
	}

	json.NewEncoder(w).Encode(response)
}

// readinessCheckHandler проверяет готовность API Gateway
func (g *APIGateway) readinessCheckHandler(w http.ResponseWriter) {
	// Проверяем состояние circuit breakers
	allHealthy := true
	cbStatus := make(map[string]string)

	g.logger.Debug("Checking readiness")

	for serviceName, cb := range g.circuitBreakers {
		// Для readiness считаем сервис готовым, если circuit breaker не в состоянии open
		// В реальном приложении здесь нужно проверять доступность сервисов
		if cb.state == "open" {
			cbStatus[serviceName] = "unhealthy"
			allHealthy = false
		} else {
			cbStatus[serviceName] = "healthy"
		}
	}

	response := map[string]interface{}{
		"status":           map[bool]string{true: "ready", false: "not_ready"}[allHealthy],
		"service":          "api-gateway",
		"timestamp":        time.Now().UTC().Format(time.RFC3339),
		"circuit_breakers": cbStatus,
	}

	statusCode := http.StatusOK
	if !allHealthy {
		statusCode = http.StatusServiceUnavailable
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(response)
}

// metricsHandler предоставляет метрики API Gateway
func (g *APIGateway) metricsHandler(w http.ResponseWriter) {
	// Собираем метрики circuit breakers
	cbMetrics := make(map[string]interface{})
	for serviceName, cb := range g.circuitBreakers {
		cbMetrics[serviceName] = map[string]interface{}{
			"state":         cb.state,
			"failures":      cb.failures,
			"success_count": cb.successCount,
		}
	}

	// Собираем метрики rate limiter
	rateLimitMetrics := map[string]interface{}{
		"requests_per_minute": g.config.RateLimitRPM,
		"burst_limit":         g.config.BurstLimit,
		"active_clients":      len(g.rateLimiter.clients),
	}

	response := map[string]interface{}{
		"service":          "api-gateway",
		"version":          "1.0.0",
		"timestamp":        time.Now().UTC().Format(time.RFC3339),
		"circuit_breakers": cbMetrics,
		"rate_limiter":     rateLimitMetrics,
		"services":         g.getServiceHealthStatus(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

// getServiceHealthStatus проверяет статус всех сервисов
func (g *APIGateway) getServiceHealthStatus() map[string]interface{} {
	services := map[string]string{
		"auth":         g.config.AuthServiceURL,
		"user":         g.config.UserServiceURL,
		"combat":       g.config.CombatServiceURL,
		"notification": g.config.NotificationServiceURL,
		"romance":      g.config.RomanceServiceURL,
		"quest":        g.config.QuestServiceURL,
		"inventory":    g.config.InventoryServiceURL,
		"economy":      g.config.EconomyServiceURL,
	}

	status := make(map[string]interface{})

	for name, url := range services {
		// В упрощенной версии просто проверяем, что URL задан
		// В реальном приложении здесь нужно делать HTTP health checks
		status[name] = map[string]interface{}{
			"url":    url,
			"status": "configured", // В реальности: "healthy"/"unhealthy"
		}
	}

	return status
}
