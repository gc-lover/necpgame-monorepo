// Main stock protection module
// Issue: #140893702

package stockprotection

import (
	"context"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
)

// StockProtectionService provides comprehensive stock exchange protection
type StockProtectionService struct {
	mu                   sync.RWMutex
	circuitBreaker       *CircuitBreaker
	manipulationDetector *ManipulationDetector
	adminAPI             *AdminAPI
	alerts               []SurveillanceAlert
	enforcementActions   []EnforcementAction
	isRunning            bool
}

// Config holds configuration for the stock protection service
type Config struct {
	// Circuit breaker settings
	CircuitBreakerPriceThreshold float64
	CircuitBreakerTimeWindow     time.Duration
	CircuitBreakerDailyLimit     float64
	CircuitBreakerInitialHalt    time.Duration
	CircuitBreakerMaxHalt        time.Duration

	// Detection settings
	EnableSpoofingDetection    bool
	EnableWashTradingDetection bool
	EnablePumpAndDumpDetection bool

	// Monitoring settings
	AlertCheckInterval time.Duration
}

// NewStockProtectionService creates a new stock protection service
func NewStockProtectionService(config Config) *StockProtectionService {
	// Create circuit breaker
	cbConfig := CircuitBreakerConfig{
		PriceChangeThreshold: config.CircuitBreakerPriceThreshold,
		TimeWindow:           config.CircuitBreakerTimeWindow,
		DailyPriceLimit:      config.CircuitBreakerDailyLimit,
		InitialHaltDuration:  config.CircuitBreakerInitialHalt,
		MaxHaltDuration:      config.CircuitBreakerMaxHalt,
	}
	circuitBreaker := NewCircuitBreaker(cbConfig)

	// Create manipulation detector
	manipulationDetector := NewManipulationDetector()

	// Create admin API
	adminAPI := NewAdminAPI(circuitBreaker, manipulationDetector)

	return &StockProtectionService{
		circuitBreaker:       circuitBreaker,
		manipulationDetector: manipulationDetector,
		adminAPI:             adminAPI,
		alerts:               make([]SurveillanceAlert, 0),
		enforcementActions:   make([]EnforcementAction, 0),
		isRunning:            false,
	}
}

// RegisterRoutes registers admin API routes
func (s *StockProtectionService) RegisterRoutes(r chi.Router) {
	s.adminAPI.RegisterRoutes(r)
}

// Start starts the stock protection service
func (s *StockProtectionService) Start(ctx context.Context) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	if s.isRunning {
		return nil
	}

	s.isRunning = true

	// Start background monitoring
	go s.monitoringLoop(ctx)

	return nil
}

// Stop stops the stock protection service
func (s *StockProtectionService) Stop() {
	s.mu.Lock()
	defer s.mu.Unlock()

	s.isRunning = false
}

// CheckTrade validates a trade against protection rules
func (s *StockProtectionService) CheckTrade(ctx context.Context, symbol string, price float64, quantity int, userID string) error {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// Check circuit breaker
	event, err := s.circuitBreaker.CheckPriceUpdate(ctx, symbol, price)
	if err != nil {
		return err
	}

	// Log circuit breaker event if triggered
	if event != nil {
		// In a real implementation, this would be stored in database
		// For now, just log it
	}

	return nil
}

// ReportTrade reports a completed trade for manipulation detection
func (s *StockProtectionService) ReportTrade(ctx context.Context, trade Trade) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Store trade for analysis (in real implementation, this would be in a time-series database)
	// For now, we'll run detection on recent trades

	// Run manipulation detection
	alerts := s.detectManipulations([]Trade{trade}, []Order{})
	s.alerts = append(s.alerts, alerts...)
}

// ReportOrder reports an order for manipulation detection
func (s *StockProtectionService) ReportOrder(ctx context.Context, order Order) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Store order for analysis
	// Run spoofing detection
	alerts := s.manipulationDetector.DetectSpoofing(ctx, []Trade{}, []Order{order})
	s.alerts = append(s.alerts, alerts...)
}

// GetAlerts returns current surveillance alerts
func (s *StockProtectionService) GetAlerts() []SurveillanceAlert {
	s.mu.RLock()
	defer s.mu.RUnlock()

	return append([]SurveillanceAlert{}, s.alerts...)
}

// GetEnforcementActions returns current enforcement actions
func (s *StockProtectionService) GetEnforcementActions() []EnforcementAction {
	s.mu.RLock()
	defer s.mu.RUnlock()

	return append([]EnforcementAction{}, s.enforcementActions...)
}

// CreateEnforcementAction creates a new enforcement action
func (s *StockProtectionService) CreateEnforcementAction(action EnforcementAction) {
	s.mu.Lock()
	defer s.mu.Unlock()

	action.ID = generateID()
	action.CreatedAt = time.Now()
	action.Status = "active"

	s.enforcementActions = append(s.enforcementActions, action)
}

// ResetDailyPrices resets daily reference prices (call at market open)
func (s *StockProtectionService) ResetDailyPrices() {
	s.circuitBreaker.ResetDailyPrices()
}

// monitoringLoop runs periodic monitoring tasks
func (s *StockProtectionService) monitoringLoop(ctx context.Context) {
	ticker := time.NewTicker(1 * time.Minute) // Check every minute
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			if !s.isRunning {
				return
			}

			// Clean up old alerts (keep last 24 hours)
			s.cleanupOldAlerts()

			// Run periodic analysis
			s.runPeriodicAnalysis(ctx)
		}
	}
}

// cleanupOldAlerts removes alerts older than 24 hours
func (s *StockProtectionService) cleanupOldAlerts() {
	s.mu.Lock()
	defer s.mu.Unlock()

	cutoff := time.Now().Add(-24 * time.Hour)
	filtered := make([]SurveillanceAlert, 0)

	for _, alert := range s.alerts {
		if alert.Timestamp.After(cutoff) {
			filtered = append(filtered, alert)
		}
	}

	s.alerts = filtered
}

// runPeriodicAnalysis runs periodic analysis tasks
func (s *StockProtectionService) runPeriodicAnalysis(ctx context.Context) {
	// In a real implementation, this would:
	// 1. Analyze recent trading patterns
	// 2. Check for emerging manipulation patterns
	// 3. Update risk scores
	// 4. Generate automated alerts

	// For now, this is a placeholder
}

// detectManipulations runs all enabled manipulation detection algorithms
func (s *StockProtectionService) detectManipulations(trades []Trade, orders []Order) []SurveillanceAlert {
	alerts := make([]SurveillanceAlert, 0)

	// Run spoofing detection
	spoofingAlerts := s.manipulationDetector.DetectSpoofing(context.Background(), trades, orders)
	alerts = append(alerts, spoofingAlerts...)

	// Run wash trading detection
	washAlerts := s.manipulationDetector.DetectWashTrading(context.Background(), trades)
	alerts = append(alerts, washAlerts...)

	// Note: Pump and dump detection would require price history data
	// This would be implemented when we have access to historical price data

	return alerts
}

// generateID generates a unique ID (placeholder implementation)
func generateID() string {
	return time.Now().Format("alert_20060102_150405_") + string(rune(time.Now().UnixNano()%1000))
}


