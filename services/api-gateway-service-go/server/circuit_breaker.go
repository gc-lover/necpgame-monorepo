// Package server provides HTTP server implementation for the API gateway.
package server

import (
	"net/http"
	"sync"
	"time"

	"go.uber.org/zap"
)

// CircuitBreaker представляет circuit breaker для upstream сервиса
type CircuitBreaker struct {
	name            string
	state           CircuitState
	failures        int
	lastFailureTime time.Time
	config          *APIGatewayConfig
	logger          *zap.Logger
	mutex           sync.RWMutex
}

// CircuitState представляет состояние circuit breaker
type CircuitState int

const (
	StateClosed CircuitState = iota
	StateOpen
	StateHalfOpen
)

// String возвращает строковое представление состояния
func (s CircuitState) String() string {
	switch s {
	case StateClosed:
		return "closed"
	case StateOpen:
		return "open"
	case StateHalfOpen:
		return "half-open"
	default:
		return "unknown"
	}
}

// CircuitBreakerManager управляет circuit breakers для всех сервисов
type CircuitBreakerManager struct {
	breakers map[string]*CircuitBreaker
	config   *APIGatewayConfig
	logger   *zap.Logger
	mutex    sync.RWMutex
}

// NewCircuitBreakerManager создает новый circuit breaker manager
func NewCircuitBreakerManager(config *APIGatewayConfig, logger *zap.Logger) *CircuitBreakerManager {
	manager := &CircuitBreakerManager{
		breakers: make(map[string]*CircuitBreaker),
		config:   config,
		logger:   logger,
	}

	// Создаем circuit breakers для всех сервисов
	services := []string{"auth", "combat", "inventory", "quest", "social", "notification", "romance"}

	for _, service := range services {
		manager.breakers[service] = &CircuitBreaker{
			name:   service,
			state:  StateClosed,
			config: config,
			logger: logger,
		}
	}

	// Запускаем мониторинг
	go manager.monitor()

	return manager
}

// Middleware возвращает middleware для circuit breaker
func (cbm *CircuitBreakerManager) Middleware(serviceName string) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			cb := cbm.getBreaker(serviceName)
			if cb == nil {
				cbm.logger.Warn("Circuit breaker not found", zap.String("service", serviceName))
				next.ServeHTTP(w, r)
				return
			}

			// Проверяем состояние circuit breaker
			if !cb.allow() {
				cbm.logger.Warn("Circuit breaker open",
					zap.String("service", serviceName),
					zap.String("state", cb.state.String()))

				w.Header().Set("X-Circuit-State", cb.state.String())
				http.Error(w, `{"error": "Service Unavailable", "message": "Circuit breaker is open"}`, http.StatusServiceUnavailable)
				return
			}

			// Создаем response writer с захватом статуса
			rw := &responseWriter{
				ResponseWriter: w,
				statusCode:     http.StatusOK,
			}

			// Выполняем запрос
			next.ServeHTTP(rw, r)

			// Обновляем circuit breaker на основе ответа
			cb.recordResult(rw.statusCode >= 500)
		})
	}
}

// getBreaker получает circuit breaker для сервиса
func (cbm *CircuitBreakerManager) getBreaker(serviceName string) *CircuitBreaker {
	cbm.mutex.RLock()
	defer cbm.mutex.RUnlock()
	return cbm.breakers[serviceName]
}

// monitor запускает мониторинг circuit breakers
func (cbm *CircuitBreakerManager) monitor() {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for range ticker.C {
		cbm.mutex.RLock()
		for name, cb := range cbm.breakers {
			cb.checkState()

			// Логируем состояние если оно изменилось
			if cb.state != StateClosed {
				cbm.logger.Info("Circuit breaker state",
					zap.String("service", name),
					zap.String("state", cb.state.String()),
					zap.Int("failures", cb.failures))
			}
		}
		cbm.mutex.RUnlock()
	}
}

// allow проверяет, разрешен ли запрос через circuit breaker
func (cb *CircuitBreaker) allow() bool {
	cb.mutex.Lock()
	defer cb.mutex.Unlock()

	switch cb.state {
	case StateClosed:
		return true
	case StateOpen:
		// Проверяем, прошло ли время для перехода в half-open
		if time.Since(cb.lastFailureTime) > cb.config.CircuitBreakerInterval {
			cb.state = StateHalfOpen
			cb.failures = 0
			cb.logger.Info("Circuit breaker transitioning to half-open",
				zap.String("service", cb.name))
			return true
		}
		return false
	case StateHalfOpen:
		return true
	default:
		return false
	}
}

// recordResult записывает результат запроса
func (cb *CircuitBreaker) recordResult(failed bool) {
	cb.mutex.Lock()
	defer cb.mutex.Unlock()

	if failed {
		cb.failures++
		cb.lastFailureTime = time.Now()

		// Переходим в open state при превышении порога
		if cb.failures >= int(cb.config.CircuitBreakerMaxRequests) {
			cb.state = StateOpen
			cb.logger.Warn("Circuit breaker opened",
				zap.String("service", cb.name),
				zap.Int("failures", cb.failures))
		}
	} else {
		// Успешный запрос
		if cb.state == StateHalfOpen {
			// Переходим обратно в closed
			cb.state = StateClosed
			cb.failures = 0
			cb.logger.Info("Circuit breaker closed",
				zap.String("service", cb.name))
		}
	}
}

// checkState проверяет и обновляет состояние circuit breaker
func (cb *CircuitBreaker) checkState() {
	cb.mutex.Lock()
	defer cb.mutex.Unlock()

	if cb.state == StateOpen {
		// Проверяем, прошло ли время для перехода в half-open
		if time.Since(cb.lastFailureTime) > cb.config.CircuitBreakerInterval {
			cb.state = StateHalfOpen
			cb.failures = 0
			cb.logger.Info("Circuit breaker auto-transitioning to half-open",
				zap.String("service", cb.name))
		}
	}
}

// responseWriter оборачивает ResponseWriter для захвата статуса
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}
