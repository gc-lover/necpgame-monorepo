// Issue: #141889238 - Correlation Analysis Calculations
// PERFORMANCE: Optimized matrix operations for correlation analysis

package calculations

import (
	"context"
	"fmt"
	"math"

	"github.com/jackc/pgx/v5/pgxpool"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-tools-service-go/pkg/api"
)

// CalculateCorrelations computes correlation matrix between multiple stocks
func CalculateCorrelations(ctx context.Context, db *pgxpool.Pool, symbols []string, period string) (*api.CorrelationAnalysis, error) {
	if len(symbols) < 2 || len(symbols) > 50 {
		return nil, fmt.Errorf("number of symbols must be between 2 and 50")
	}

	// PERFORMANCE: Batch query for price data
	dataQuery := `
		SELECT symbol, ARRAY_AGG(close_price ORDER BY date) as prices
		FROM (
			SELECT symbol, date, close_price,
				   ROW_NUMBER() OVER (PARTITION BY symbol ORDER BY date DESC) as rn
			FROM stock_prices
			WHERE symbol = ANY($1)
			  AND date >= CASE
				WHEN $2 = '1M' THEN CURRENT_DATE - INTERVAL '1 month'
				WHEN $2 = '3M' THEN CURRENT_DATE - INTERVAL '3 months'
				WHEN $2 = '6M' THEN CURRENT_DATE - INTERVAL '6 months'
				WHEN $2 = '1y' THEN CURRENT_DATE - INTERVAL '1 year'
				WHEN $2 = '2y' THEN CURRENT_DATE - INTERVAL '2 years'
				ELSE CURRENT_DATE - INTERVAL '3 months'
			  END
		) t
		WHERE rn <= 252  -- Trading days in a year
		GROUP BY symbol
	`

	rows, err := db.Query(ctx, dataQuery, symbols, period)
	if err != nil {
		return nil, fmt.Errorf("failed to query price data: %w", err)
	}
	defer rows.Close()

	priceArrays := make(map[string][]float64)
	for rows.Next() {
		var symbol string
		var prices []float64
		if err := rows.Scan(&symbol, &prices); err != nil {
			continue
		}
		priceArrays[symbol] = prices
	}

	// Calculate correlation matrix
	matrix := calculateCorrelationMatrix(symbols, priceArrays)

	// Calculate average correlations for each symbol
	averageCorrelations := make(map[string]float64)
	for i, symbol := range symbols {
		sum := 0.0
		count := 0
		for j, otherSymbol := range symbols {
			if i != j {
				corr := matrix[i*len(symbols)+j]
				if !math.IsNaN(corr) {
					sum += corr
					count++
				}
			}
		}
		if count > 0 {
			averageCorrelations[symbol] = math.Round((sum/float64(count))*10000) / 10000
		} else {
			averageCorrelations[symbol] = 0
		}
	}

	return &api.CorrelationAnalysis{
		Symbols:             symbols,
		Period:              period,
		CorrelationMatrix:   matrix,
		AverageCorrelations: averageCorrelations,
	}, nil
}

func calculateCorrelationMatrix(symbols []string, priceArrays map[string][]float64) []float64 {
	n := len(symbols)
	matrix := make([]float64, n*n)

	for i := 0; i < n; i++ {
		for j := 0; j < n; j++ {
			if i == j {
				matrix[i*n+j] = 1.0 // Perfect correlation with itself
			} else {
				corr := calculatePearsonCorrelation(
					priceArrays[symbols[i]],
					priceArrays[symbols[j]],
				)
				matrix[i*n+j] = math.Round(corr*10000) / 10000
			}
		}
	}

	return matrix
}

func calculatePearsonCorrelation(x, y []float64) float64 {
	if len(x) != len(y) || len(x) == 0 {
		return math.NaN()
	}

	// Calculate means
	meanX := mean(x)
	meanY := mean(y)

	// Calculate covariance and variances
	covariance := 0.0
	varX := 0.0
	varY := 0.0

	for i := 0; i < len(x); i++ {
		dx := x[i] - meanX
		dy := y[i] - meanY

		covariance += dx * dy
		varX += dx * dx
		varY += dy * dy
	}

	// Avoid division by zero
	if varX == 0 || varY == 0 {
		return math.NaN()
	}

	return covariance / math.Sqrt(varX*varY)
}

func mean(values []float64) float64 {
	if len(values) == 0 {
		return 0
	}

	sum := 0.0
	for _, v := range values {
		sum += v
	}

	return sum / float64(len(values))
}

// Issue: #141889238
