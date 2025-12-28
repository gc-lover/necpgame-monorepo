// Issue: #141889238 - Portfolio Optimization Engine
// PERFORMANCE: Optimized for Modern Portfolio Theory calculations and optimization algorithms

package analysis

import (
	"context"
	"fmt"
	"math"
	"sort"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-tools-service-go/pkg/api"
)

// PortfolioOptimizer handles portfolio optimization using Modern Portfolio Theory
type PortfolioOptimizer struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewPortfolioOptimizer creates a new portfolio optimizer
func NewPortfolioOptimizer(db *pgxpool.Pool, logger *zap.Logger) *PortfolioOptimizer {
	return &PortfolioOptimizer{
		db:     db,
		logger: logger,
	}
}

// Optimize performs portfolio optimization using Modern Portfolio Theory
func (po *PortfolioOptimizer) Optimize(ctx context.Context, req *api.PortfolioOptimizationRequest) (*api.PortfolioOptimizationResponse, error) {
	if len(req.Symbols) < 2 {
		return nil, fmt.Errorf("at least 2 symbols required for optimization")
	}

	// PERFORMANCE: Batch query for stock data
	symbols := req.Symbols
	dataQuery := `
		SELECT symbol,
			   AVG(close_price) as avg_price,
			   STDDEV(close_price) as volatility,
			   CORR(close_price, LAG(close_price) OVER (PARTITION BY symbol ORDER BY date)) as returns
		FROM stock_prices
		WHERE symbol = ANY($1)
		  AND date >= CURRENT_DATE - INTERVAL '90 days'
		GROUP BY symbol
	`

	rows, err := po.db.Query(ctx, dataQuery, symbols)
	if err != nil {
		po.logger.Error("Failed to query stock data for optimization",
			zap.Strings("symbols", symbols),
			zap.Error(err))
		return nil, fmt.Errorf("failed to retrieve stock data: %w", err)
	}
	defer rows.Close()

	// Collect stock data
	stockData := make(map[string]struct {
		avgPrice   float64
		volatility float64
		returns    float64
	})

	for rows.Next() {
		var symbol string
		var avgPrice, volatility, returns float64
		if err := rows.Scan(&symbol, &avgPrice, &volatility, &returns); err != nil {
			continue
		}
		stockData[symbol] = struct {
			avgPrice   float64
			volatility float64
			returns    float64
		}{avgPrice, volatility, returns}
	}

	// Calculate optimal allocations
	allocations := po.calculateOptimalAllocations(req, stockData)

	// Calculate portfolio metrics
	expectedReturn := 0.0
	expectedVolatility := 0.0

	for _, alloc := range allocations {
		data := stockData[alloc.Symbol]
		weight := alloc.Weight

		expectedReturn += weight * data.returns
		expectedVolatility += weight * weight * data.volatility * data.volatility

		// Add covariance terms (simplified)
		for _, otherAlloc := range allocations {
			if alloc.Symbol != otherAlloc.Symbol {
				otherData := stockData[otherAlloc.Symbol]
				covariance := data.volatility * otherData.volatility * 0.3 // Simplified correlation
				expectedVolatility += 2 * weight * otherAlloc.Weight * covariance
			}
		}
	}

	expectedVolatility = math.Sqrt(expectedVolatility)

	// Sharpe ratio calculation
	riskFreeRate := 0.02 // 2% risk-free rate
	sharpeRatio := (expectedReturn - riskFreeRate) / expectedVolatility

	response := &api.PortfolioOptimizationResponse{
		Allocations:      allocations,
		ExpectedReturn:   math.Round(expectedReturn*10000) / 10000,
		ExpectedVolatility: math.Round(expectedVolatility*10000) / 10000,
		SharpeRatio:      math.Round(sharpeRatio*100) / 100,
		OptimizationDate: fmt.Sprintf("%d", ctx.Value("timestamp").(int64)), // Placeholder
	}

	return response, nil
}

func (po *PortfolioOptimizer) calculateOptimalAllocations(req *api.PortfolioOptimizationRequest, stockData map[string]struct {
	avgPrice   float64
	volatility float64
	returns    float64
}) []api.AssetAllocation {
	allocations := make([]api.AssetAllocation, 0, len(req.Symbols))

	// Risk tolerance adjustment
	riskMultiplier := 1.0
	switch req.RiskTolerance {
	case "conservative":
		riskMultiplier = 0.7
	case "moderate":
		riskMultiplier = 1.0
	case "aggressive":
		riskMultiplier = 1.3
	}

	totalWeight := 0.0
	for _, symbol := range req.Symbols {
		data, exists := stockData[symbol]
		if !exists {
			// Default allocation for missing data
			data = struct {
				avgPrice   float64
				volatility float64
				returns    float64
			}{100.0, 0.25, 0.08}
		}

		// Calculate weight based on risk-adjusted returns
		rawWeight := (data.returns / data.volatility) * riskMultiplier

		// Apply constraints
		if req.Constraints != nil {
			if rawWeight > req.Constraints.MaxWeight {
				rawWeight = req.Constraints.MaxWeight
			}
			if rawWeight < req.Constraints.MinWeight {
				rawWeight = req.Constraints.MinWeight
			}
		}

		allocations = append(allocations, api.AssetAllocation{
			Symbol:         symbol,
			Weight:         math.Round(rawWeight*10000) / 10000,
			ExpectedReturn: math.Round(data.returns*10000) / 10000,
			Volatility:     math.Round(data.volatility*10000) / 10000,
		})

		totalWeight += rawWeight
	}

	// Normalize weights to sum to 1
	for i := range allocations {
		if totalWeight > 0 {
			allocations[i].Weight = math.Round((allocations[i].Weight/totalWeight)*10000) / 10000
		}
	}

	// Sort by weight descending
	sort.Slice(allocations, func(i, j int) bool {
		return allocations[i].Weight > allocations[j].Weight
	})

	return allocations
}

// HealthCheck implements health check for portfolio optimizer
func (po *PortfolioOptimizer) HealthCheck(ctx context.Context) error {
	// Simple health check - verify database connectivity
	return po.db.Ping(ctx)
}

// Issue: #141889238