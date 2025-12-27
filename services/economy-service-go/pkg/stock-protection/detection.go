// Detection algorithms for stock market manipulation
// Issue: #140893702

package stockprotection

import (
	"context"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
)

// ManipulationType represents type of market manipulation
type ManipulationType string

const (
	Spoofing       ManipulationType = "spoofing"
	WashTrading    ManipulationType = "wash_trading"
	InsiderTrading ManipulationType = "insider_trading"
	PumpAndDump    ManipulationType = "pump_and_dump"
	Layering       ManipulationType = "layering"
)

// AlertSeverity represents severity level of alert
type AlertSeverity string

const (
	SeverityLow    AlertSeverity = "low"
	SeverityMedium AlertSeverity = "medium"
	SeverityHigh   AlertSeverity = "high"
	SeverityCritical AlertSeverity = "critical"
)

// Trade represents a single trade
type Trade struct {
	ID        string
	Symbol    string
	Price     float64
	Quantity  int
	Side      string // "buy" or "sell"
	Timestamp time.Time
	UserID    string
}

// SurveillanceAlert represents a detected manipulation alert
type SurveillanceAlert struct {
	ID          string
	Symbol      string
	Type        ManipulationType
	Severity    AlertSeverity
	Confidence  float64 // 0.0 to 1.0
	Description string
	Trades      []Trade
	Timestamp   time.Time
	Status      string // "active", "investigating", "resolved", "false_positive"
}

// ManipulationDetector detects various forms of market manipulation
type ManipulationDetector struct {
	alerts []SurveillanceAlert
}

// NewManipulationDetector creates a new detector instance
func NewManipulationDetector() *ManipulationDetector {
	return &ManipulationDetector{
		alerts: make([]SurveillanceAlert, 0),
	}
}

// DetectSpoofing detects spoofing (placing large orders then canceling)
func (md *ManipulationDetector) DetectSpoofing(ctx context.Context, trades []Trade, orders []Order) []SurveillanceAlert {
	alerts := make([]SurveillanceAlert, 0)

	// Group orders and trades by user and symbol
	userOrders := make(map[string]map[string][]Order)
	for _, order := range orders {
		if userOrders[order.UserID] == nil {
			userOrders[order.UserID] = make(map[string][]Order)
		}
		userOrders[order.UserID][order.Symbol] = append(userOrders[order.UserID][order.Symbol], order)
	}

	for userID, symbolOrders := range userOrders {
		for symbol, userOrders := range symbolOrders {
			// Look for large orders that were quickly canceled
			for _, order := range userOrders {
				if order.Status == "canceled" && order.Quantity > 1000 {
					// Check if order was active for less than 5 minutes
					if order.CancelTime.Sub(order.CreatedTime) < 5*time.Minute {
						alert := SurveillanceAlert{
							ID:          uuid.New().String(),
							Symbol:      symbol,
							Type:        Spoofing,
							Severity:    SeverityMedium,
							Confidence:  0.75,
							Description: fmt.Sprintf("Large order (%d shares) canceled within 5 minutes - potential spoofing by user %s", order.Quantity, userID),
							Trades:      []Trade{}, // No trades, just order manipulation
							Timestamp:   time.Now(),
							Status:      "active",
						}
						alerts = append(alerts, alert)
					}
				}
			}
		}
	}

	return alerts
}

// DetectWashTrading detects wash trading (trading with oneself)
func (md *ManipulationDetector) DetectWashTrading(ctx context.Context, trades []Trade) []SurveillanceAlert {
	alerts := make([]SurveillanceAlert, 0)

	// Group trades by symbol and time window
	symbolTrades := make(map[string][]Trade)
	for _, trade := range trades {
		symbolTrades[trade.Symbol] = append(symbolTrades[trade.Symbol], trade)
	}

	for symbol, symbolTrades := range symbolTrades {
		// Look for patterns where same user is on both sides of trades
		userTrades := make(map[string][]Trade)
		for _, trade := range symbolTrades {
			userTrades[trade.UserID] = append(userTrades[trade.UserID], trade)
		}

		for userID, userTrades := range userTrades {
			if len(userTrades) < 2 {
				continue
			}

			// Check for round-trip trading (buy then sell at same price)
			buyTrades := make([]Trade, 0)
			sellTrades := make([]Trade, 0)

			for _, trade := range userTrades {
				if trade.Side == "buy" {
					buyTrades = append(buyTrades, trade)
				} else {
					sellTrades = append(sellTrades, trade)
				}
			}

			// Look for matching buy/sell pairs
			for _, buy := range buyTrades {
				for _, sell := range sellTrades {
					if math.Abs(buy.Price-sell.Price) < 0.01 && // Same price
						buy.Quantity == sell.Quantity && // Same quantity
						sell.Timestamp.Sub(buy.Timestamp) < 1*time.Hour { // Within 1 hour

						alert := SurveillanceAlert{
							ID:          uuid.New().String(),
							Symbol:      symbol,
							Type:        WashTrading,
							Severity:    SeverityHigh,
							Confidence:  0.85,
							Description: fmt.Sprintf("Wash trading detected: user %s bought and sold %d shares at $%.2f within 1 hour", userID, buy.Quantity, buy.Price),
							Trades:      []Trade{buy, sell},
							Timestamp:   time.Now(),
							Status:      "active",
						}
						alerts = append(alerts, alert)
					}
				}
			}
		}
	}

	return alerts
}

// DetectPumpAndDump detects pump and dump schemes
func (md *ManipulationDetector) DetectPumpAndDump(ctx context.Context, priceHistory []PricePoint) []SurveillanceAlert {
	alerts := make([]SurveillanceAlert, 0)

	if len(priceHistory) < 10 {
		return alerts
	}

	// Calculate price volatility and volume spikes
	for i := 10; i < len(priceHistory); i++ {
		window := priceHistory[i-10:i]

		// Calculate volatility (standard deviation of returns)
		returns := make([]float64, len(window)-1)
		for j := 1; j < len(window); j++ {
			returns[j-1] = (window[j].Price - window[j-1].Price) / window[j-1].Price
		}

		volatility := calculateStdDev(returns)

		// Check for sudden price spike with high volume
		avgVolume := calculateAverageVolume(window)
		if volatility > 0.05 && // 5% volatility threshold
			window[len(window)-1].Volume > avgVolume*3 { // 3x average volume

			alert := SurveillanceAlert{
				ID:          uuid.New().String(),
				Symbol:      window[0].Symbol,
				Type:        PumpAndDump,
				Severity:    SeverityMedium,
				Confidence:  0.70,
				Description: fmt.Sprintf("Pump and dump detected: high volatility (%.2f%%) with volume spike (%.1fx average)", volatility*100, window[len(window)-1].Volume/avgVolume),
				Trades:      []Trade{},
				Timestamp:   time.Now(),
				Status:      "active",
			}
			alerts = append(alerts, alert)
		}
	}

	return alerts
}

// Order represents a trading order
type Order struct {
	ID          string
	Symbol      string
	UserID      string
	Side        string
	Quantity    int
	Price       float64
	Status      string
	CreatedTime time.Time
	CancelTime  time.Time
}

// PricePoint represents a price point with volume
type PricePoint struct {
	Symbol   string
	Price    float64
	Volume   float64
	Timestamp time.Time
}

// calculateStdDev calculates standard deviation
func calculateStdDev(values []float64) float64 {
	if len(values) == 0 {
		return 0
	}

	mean := 0.0
	for _, v := range values {
		mean += v
	}
	mean /= float64(len(values))

	sumSquares := 0.0
	for _, v := range values {
		sumSquares += (v - mean) * (v - mean)
	}

	return math.Sqrt(sumSquares / float64(len(values)))
}

// calculateAverageVolume calculates average volume
func calculateAverageVolume(points []PricePoint) float64 {
	if len(points) == 0 {
		return 0
	}

	total := 0.0
	for _, p := range points {
		total += p.Volume
	}

	return total / float64(len(points))
}



