// Circuit breaker implementation for stock exchange protection
// Issue: #140893702

package stockprotection

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
)

// CircuitBreakerState represents the state of circuit breaker
type CircuitBreakerState string

const (
	CircuitBreakerActive   CircuitBreakerState = "active"
	CircuitBreakerHalted   CircuitBreakerState = "halted"
	CircuitBreakerCooldown CircuitBreakerState = "cooldown"
)

// CircuitBreakerConfig holds configuration for circuit breaker
type CircuitBreakerConfig struct {
	// Price change thresholds (percentage)
	PriceChangeThreshold float64 // e.g., 0.15 for 15%
	TimeWindow           time.Duration // e.g., 1 hour
	// Daily price limits
	DailyPriceLimit float64 // e.g., 0.20 for 20%
	// Halt durations
	InitialHaltDuration time.Duration // e.g., 5 minutes
	MaxHaltDuration     time.Duration // e.g., 1 hour
}

// CircuitBreaker manages trading halts for stocks
type CircuitBreaker struct {
	mu      sync.RWMutex
	config  CircuitBreakerConfig
	states  map[string]*CircuitBreakerStockState // keyed by stock symbol
}

// CircuitBreakerStockState holds state for individual stock
type CircuitBreakerStockState struct {
	Symbol           string
	State            CircuitBreakerState
	LastPrice        float64
	ReferencePrice   float64 // price at start of monitoring window
	DailyOpenPrice   float64
	LastHaltTime     time.Time
	HaltEndTime      time.Time
	HaltCount        int
	Reason           string
}

// NewCircuitBreaker creates a new circuit breaker instance
func NewCircuitBreaker(config CircuitBreakerConfig) *CircuitBreaker {
	return &CircuitBreaker{
		config: config,
		states: make(map[string]*CircuitBreakerStockState),
	}
}

// CheckPriceUpdate checks if price update should trigger circuit breaker
func (cb *CircuitBreaker) CheckPriceUpdate(ctx context.Context, symbol string, newPrice float64) (*CircuitBreakerEvent, error) {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	state, exists := cb.states[symbol]
	if !exists {
		// Initialize new stock state
		state = &CircuitBreakerStockState{
			Symbol:         symbol,
			State:          CircuitBreakerActive,
			LastPrice:      newPrice,
			ReferencePrice: newPrice,
			DailyOpenPrice: newPrice,
		}
		cb.states[symbol] = state
		return nil, nil
	}

	// Check if currently halted
	if state.State == CircuitBreakerHalted {
		if time.Now().Before(state.HaltEndTime) {
			return nil, fmt.Errorf("trading halted for %s until %v", symbol, state.HaltEndTime)
		}
		// Resume trading
		state.State = CircuitBreakerActive
		state.LastHaltTime = time.Now()
	}

	// Check price change threshold
	if state.ReferencePrice > 0 {
		priceChange := (newPrice - state.ReferencePrice) / state.ReferencePrice
		if abs(priceChange) >= cb.config.PriceChangeThreshold {
			// Trigger circuit breaker
			haltDuration := cb.calculateHaltDuration(state.HaltCount)
			state.State = CircuitBreakerHalted
			state.HaltEndTime = time.Now().Add(haltDuration)
			state.HaltCount++
			state.Reason = fmt.Sprintf("Price change %.2f%% exceeded threshold %.2f%%",
				priceChange*100, cb.config.PriceChangeThreshold*100)

			event := &CircuitBreakerEvent{
				ID:          uuid.New().String(),
				Symbol:      symbol,
				EventType:   "circuit_breaker_triggered",
				Reason:      state.Reason,
				HaltUntil:   state.HaltEndTime,
				PriceChange: priceChange,
				Timestamp:   time.Now(),
			}

			state.LastPrice = newPrice
			return event, nil
		}
	}

	// Check daily price limit
	if state.DailyOpenPrice > 0 {
		dailyChange := (newPrice - state.DailyOpenPrice) / state.DailyOpenPrice
		if abs(dailyChange) >= cb.config.DailyPriceLimit {
			// Trigger daily limit halt
			state.State = CircuitBreakerHalted
			state.HaltEndTime = time.Now().Add(24 * time.Hour) // Halt until next trading day
			state.Reason = fmt.Sprintf("Daily price limit %.2f%% exceeded",
				cb.config.DailyPriceLimit*100)

			event := &CircuitBreakerEvent{
				ID:          uuid.New().String(),
				Symbol:      symbol,
				EventType:   "daily_limit_triggered",
				Reason:      state.Reason,
				HaltUntil:   state.HaltEndTime,
				PriceChange: dailyChange,
				Timestamp:   time.Now(),
			}

			state.LastPrice = newPrice
			return event, nil
		}
	}

	state.LastPrice = newPrice
	return nil, nil
}

// CircuitBreakerEvent represents a circuit breaker event
type CircuitBreakerEvent struct {
	ID          string
	Symbol      string
	EventType   string // "circuit_breaker_triggered", "daily_limit_triggered"
	Reason      string
	HaltUntil   time.Time
	PriceChange float64
	Timestamp   time.Time
}

// GetStockState returns current state for a stock
func (cb *CircuitBreaker) GetStockState(symbol string) *CircuitBreakerStockState {
	cb.mu.RLock()
	defer cb.mu.RUnlock()

	if state, exists := cb.states[symbol]; exists {
		return state
	}
	return nil
}

// ResetDailyPrices resets daily reference prices (call at market open)
func (cb *CircuitBreaker) ResetDailyPrices() {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	for _, state := range cb.states {
		state.DailyOpenPrice = state.LastPrice
	}
}

// calculateHaltDuration calculates halt duration based on violation count
func (cb *CircuitBreaker) calculateHaltDuration(violationCount int) time.Duration {
	baseDuration := cb.config.InitialHaltDuration

	// Exponential backoff for repeated violations
	multiplier := 1 << violationCount // 2^violationCount
	duration := time.Duration(int64(baseDuration) * int64(multiplier))

	// Cap at max duration
	if duration > cb.config.MaxHaltDuration {
		duration = cb.config.MaxHaltDuration
	}

	return duration
}

// abs returns absolute value of float64
func abs(x float64) float64 {
	if x < 0 {
		return -x
	}
	return x
}

