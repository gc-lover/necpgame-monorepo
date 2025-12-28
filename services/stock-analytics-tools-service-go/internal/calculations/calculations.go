// Issue: #141889238 - Financial Calculations Engine
// PERFORMANCE: Optimized for complex mathematical computations in financial analysis

package calculations

import (
	"math"
)

// CalculateCorrelation calculates Pearson correlation coefficient between two series
func CalculateCorrelation(x, y []float64) float64 {
	if len(x) != len(y) || len(x) == 0 {
		return 0
	}

	n := float64(len(x))
	sumX := 0.0
	sumY := 0.0
	sumXY := 0.0
	sumX2 := 0.0
	sumY2 := 0.0

	for i := 0; i < len(x); i++ {
		sumX += x[i]
		sumY += y[i]
		sumXY += x[i] * y[i]
		sumX2 += x[i] * x[i]
		sumY2 += y[i] * y[i]
	}

	numerator := n*sumXY - sumX*sumY
	denominator := math.Sqrt((n*sumX2 - sumX*sumX) * (n*sumY2 - sumY*sumY))

	if denominator == 0 {
		return 0
	}

	return numerator / denominator
}

// CalculateVolatility calculates historical volatility using standard deviation of returns
func CalculateVolatility(prices []float64, annualizationFactor float64) float64 {
	if len(prices) < 2 {
		return 0
	}

	returns := make([]float64, len(prices)-1)
	for i := 1; i < len(prices); i++ {
		returns[i-1] = math.Log(prices[i] / prices[i-1])
	}

	mean := calculateMean(returns)
	variance := 0.0
	for _, ret := range returns {
		variance += (ret - mean) * (ret - mean)
	}
	variance /= float64(len(returns) - 1)

	return math.Sqrt(variance) * math.Sqrt(annualizationFactor)
}

// CalculateSharpeRatio calculates Sharpe ratio
func CalculateSharpeRatio(returns []float64, riskFreeRate float64) float64 {
	if len(returns) == 0 {
		return 0
	}

	meanReturn := calculateMean(returns)
	volatility := calculateStdDev(returns, meanReturn)

	if volatility == 0 {
		return 0
	}

	return (meanReturn - riskFreeRate) / volatility
}

// CalculateBeta calculates beta coefficient relative to market
func CalculateBeta(assetReturns, marketReturns []float64) float64 {
	if len(assetReturns) != len(marketReturns) || len(assetReturns) == 0 {
		return 0
	}

	covariance := calculateCovariance(assetReturns, marketReturns)
	marketVariance := calculateVariance(marketReturns)

	if marketVariance == 0 {
		return 0
	}

	return covariance / marketVariance
}

// CalculateValueAtRisk calculates Value at Risk using historical simulation
func CalculateValueAtRisk(returns []float64, confidenceLevel float64) float64 {
	if len(returns) == 0 {
		return 0
	}

	sortedReturns := make([]float64, len(returns))
	copy(sortedReturns, returns)

	// Simple sort (bubble sort for small arrays)
	for i := 0; i < len(sortedReturns); i++ {
		for j := 0; j < len(sortedReturns)-1-i; j++ {
			if sortedReturns[j] > sortedReturns[j+1] {
				sortedReturns[j], sortedReturns[j+1] = sortedReturns[j+1], sortedReturns[j]
			}
		}
	}

	index := int(float64(len(sortedReturns)) * (1 - confidenceLevel))
	if index >= len(sortedReturns) {
		index = len(sortedReturns) - 1
	}

	return -sortedReturns[index] // Negative because VaR is a loss
}

// Helper functions

func calculateMean(values []float64) float64 {
	if len(values) == 0 {
		return 0
	}

	sum := 0.0
	for _, v := range values {
		sum += v
	}
	return sum / float64(len(values))
}

func calculateVariance(values []float64) float64 {
	if len(values) < 2 {
		return 0
	}

	mean := calculateMean(values)
	variance := 0.0
	for _, v := range values {
		variance += (v - mean) * (v - mean)
	}
	return variance / float64(len(values)-1)
}

func calculateStdDev(values []float64, mean float64) float64 {
	if len(values) < 2 {
		return 0
	}

	variance := 0.0
	for _, v := range values {
		variance += (v - mean) * (v - mean)
	}
	return math.Sqrt(variance / float64(len(values)-1))
}

func calculateCovariance(x, y []float64) float64 {
	if len(x) != len(y) || len(x) < 2 {
		return 0
	}

	meanX := calculateMean(x)
	meanY := calculateMean(y)

	covariance := 0.0
	for i := 0; i < len(x); i++ {
		covariance += (x[i] - meanX) * (y[i] - meanY)
	}
	return covariance / float64(len(x)-1)
}

// Issue: #141889238
