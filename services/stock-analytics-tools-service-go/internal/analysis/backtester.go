// Issue: #141889238 - Strategy Backtesting Engine
// PERFORMANCE: Optimized for historical strategy testing and performance analysis

package analysis

import (
	"context"
	"fmt"
	"math"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-tools-service-go/pkg/api"
)

// Backtester handles strategy backtesting operations
type Backtester struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewBacktester creates a new strategy backtester
func NewBacktester(db *pgxpool.Pool, logger *zap.Logger) *Backtester {
	return &Backtester{
		db:     db,
		logger: logger,
	}
}

// Backtest performs strategy backtesting with historical data
func (bt *Backtester) Backtest(ctx context.Context, req *api.BacktestingRequest) (*api.BacktestingResponse, error) {
	if len(req.Symbols) == 0 {
		return nil, fmt.Errorf("symbols cannot be empty")
	}

	// PERFORMANCE: Query historical price data for backtesting period
	priceQuery := `
		SELECT symbol, date, open_price, high_price, low_price, close_price, volume
		FROM stock_prices
		WHERE symbol = ANY($1)
		  AND date BETWEEN $2 AND $3
		ORDER BY symbol, date
	`

	rows, err := bt.db.Query(ctx, priceQuery, req.Symbols, req.StartDate.Time, req.EndDate.Time)
	if err != nil {
		bt.logger.Error("Failed to query historical price data",
			zap.Strings("symbols", req.Symbols),
			zap.Time("start_date", time.Time(req.StartDate)),
			zap.Time("end_date", time.Time(req.EndDate)),
			zap.Error(err))
		return nil, fmt.Errorf("failed to retrieve historical data: %w", err)
	}
	defer rows.Close()

	// Organize data by symbol
	priceData := make(map[string][]api.OHLCVData)
	for rows.Next() {
		var symbol string
		var date time.Time
		var ohlcv api.OHLCVData

		if err := rows.Scan(&symbol, &date, &ohlcv.Open, &ohlcv.High, &ohlcv.Low, &ohlcv.Close, &ohlcv.Volume); err != nil {
			continue
		}

		ohlcv.Date = date.Format("2006-01-02")
		priceData[symbol] = append(priceData[symbol], ohlcv)
	}

	// Execute backtest based on strategy
	trades, equityCurve := bt.executeStrategy(req, priceData)

	// Calculate performance metrics
	performanceMetrics := bt.calculatePerformanceMetrics(trades, equityCurve, req)

	response := &api.BacktestingResponse{
		StrategyName:       req.Strategy.Name,
		Period:             api.Period{StartDate: req.StartDate, EndDate: req.EndDate},
		PerformanceMetrics: performanceMetrics,
		Trades:             trades,
		EquityCurve:        equityCurve,
	}

	return response, nil
}

func (bt *Backtester) executeStrategy(req *api.BacktestingRequest, priceData map[string][]api.OHLCVData) ([]api.TradeRecord, []api.EquityPoint) {
	var trades []api.TradeRecord
	var equityCurve []api.EquityPoint

	capital := req.InitialCapital
	position := 0.0
	entryPrice := 0.0

	// Use first symbol for simplicity (would need to handle multi-symbol strategies)
	symbol := req.Symbols[0]
	data := priceData[symbol]

	for i, ohlcv := range data {
		date, _ := time.Parse("2006-01-02", ohlcv.Date)

		// Simple moving average crossover strategy (example)
		if bt.shouldEnterPosition(data, i, req.Strategy) && position == 0 {
			// Enter long position
			shares := (capital * 0.1) / ohlcv.Close // Use 10% of capital
			commission := shares * req.CommissionPerTrade

			trades = append(trades, api.TradeRecord{
				Symbol:     symbol,
				Side:       "buy",
				Quantity:   shares,
				Price:      ohlcv.Close,
				Timestamp:  date.Format(time.RFC3339),
				Commission: commission,
			})

			position = shares
			entryPrice = ohlcv.Close
			capital -= (shares * ohlcv.Close) + commission

		} else if bt.shouldExitPosition(data, i, req.Strategy) && position > 0 {
			// Exit position
			commission := position * req.CommissionPerTrade

			trades = append(trades, api.TradeRecord{
				Symbol:     symbol,
				Side:       "sell",
				Quantity:   position,
				Price:      ohlcv.Close,
				Timestamp:  date.Format(time.RFC3339),
				Commission: commission,
			})

			profit := (ohlcv.Close - entryPrice) * position
			capital += (position * ohlcv.Close) - commission
			position = 0
			entryPrice = 0
		}

		// Record equity curve
		equityCurve = append(equityCurve, api.EquityPoint{
			Date:   ohlcv.Date,
			Equity: math.Round((capital+position*ohlcv.Close)*100) / 100,
		})
	}

	return trades, equityCurve
}

func (bt *Backtester) shouldEnterPosition(data []api.OHLCVData, index int, strategy api.TradingStrategy) bool {
	if index < 50 { // Need enough data for indicators
		return false
	}

	// Simple SMA crossover strategy
	fastPeriod := 20
	slowPeriod := 50

	if len(strategy.Parameters) > 0 {
		if fast, ok := strategy.Parameters["fast_ma"].(float64); ok {
			fastPeriod = int(fast)
		}
		if slow, ok := strategy.Parameters["slow_ma"].(float64); ok {
			slowPeriod = int(slow)
		}
	}

	fastSMA := bt.calculateSMA(data, index, fastPeriod)
	slowSMA := bt.calculateSMA(data, index, slowPeriod)

	prevFastSMA := bt.calculateSMA(data, index-1, fastPeriod)
	prevSlowSMA := bt.calculateSMA(data, index-1, slowPeriod)

	// Bullish crossover
	return fastSMA > slowSMA && prevFastSMA <= prevSlowSMA
}

func (bt *Backtester) shouldExitPosition(data []api.OHLCVData, index int, strategy api.TradingStrategy) bool {
	if index < 50 {
		return false
	}

	fastPeriod := 20
	slowPeriod := 50

	if len(strategy.Parameters) > 0 {
		if fast, ok := strategy.Parameters["fast_ma"].(float64); ok {
			fastPeriod = int(fast)
		}
		if slow, ok := strategy.Parameters["slow_ma"].(float64); ok {
			slowPeriod = int(slow)
		}
	}

	fastSMA := bt.calculateSMA(data, index, fastPeriod)
	slowSMA := bt.calculateSMA(data, index, slowPeriod)

	prevFastSMA := bt.calculateSMA(data, index-1, fastPeriod)
	prevSlowSMA := bt.calculateSMA(data, index-1, slowPeriod)

	// Bearish crossover
	return fastSMA < slowSMA && prevFastSMA >= prevSlowSMA
}

func (bt *Backtester) calculateSMA(data []api.OHLCVData, endIndex, period int) float64 {
	if endIndex < period-1 {
		return 0
	}

	sum := 0.0
	for i := endIndex - period + 1; i <= endIndex; i++ {
		sum += data[i].Close
	}

	return sum / float64(period)
}

func (bt *Backtester) calculatePerformanceMetrics(trades []api.TradeRecord, equityCurve []api.EquityPoint, req *api.BacktestingRequest) api.PerformanceMetrics {
	if len(equityCurve) == 0 {
		return api.PerformanceMetrics{}
	}

	initialCapital := req.InitialCapital
	finalEquity := equityCurve[len(equityCurve)-1].Equity

	totalReturn := (finalEquity - initialCapital) / initialCapital
	annualizedReturn := totalReturn // Simplified - would calculate based on time period

	// Calculate volatility from equity curve
	volatility := bt.calculateVolatilityFromEquity(equityCurve)

	// Calculate Sharpe ratio
	riskFreeRate := 0.02 // 2%
	sharpeRatio := 0.0
	if volatility > 0 {
		sharpeRatio = (annualizedReturn - riskFreeRate) / volatility
	}

	// Calculate max drawdown
	maxDrawdown := bt.calculateMaxDrawdown(equityCurve)

	// Calculate win rate
	winRate := bt.calculateWinRate(trades)

	// Calculate profit factor
	profitFactor := bt.calculateProfitFactor(trades)

	return api.PerformanceMetrics{
		TotalReturn:     math.Round(totalReturn*10000) / 10000,
		AnnualizedReturn: math.Round(annualizedReturn*10000) / 10000,
		Volatility:      math.Round(volatility*10000) / 10000,
		SharpeRatio:     math.Round(sharpeRatio*100) / 100,
		MaxDrawdown:     math.Round(maxDrawdown*10000) / 10000,
		WinRate:         math.Round(winRate*10000) / 10000,
		ProfitFactor:    math.Round(profitFactor*100) / 100,
	}
}

func (bt *Backtester) calculateVolatilityFromEquity(equityCurve []api.EquityPoint) float64 {
	if len(equityCurve) < 2 {
		return 0
	}

	returns := make([]float64, len(equityCurve)-1)
	for i := 1; i < len(equityCurve); i++ {
		returns[i-1] = (equityCurve[i].Equity - equityCurve[i-1].Equity) / equityCurve[i-1].Equity
	}

	return calculateVolatility(returns)
}

func (bt *Backtester) calculateMaxDrawdown(equityCurve []api.EquityPoint) float64 {
	if len(equityCurve) == 0 {
		return 0
	}

	peak := equityCurve[0].Equity
	maxDrawdown := 0.0

	for _, point := range equityCurve {
		if point.Equity > peak {
			peak = point.Equity
		}

		drawdown := (peak - point.Equity) / peak
		if drawdown > maxDrawdown {
			maxDrawdown = drawdown
		}
	}

	return maxDrawdown
}

func (bt *Backtester) calculateWinRate(trades []api.TradeRecord) float64 {
	if len(trades) == 0 {
		return 0
	}

	wins := 0
	for i := 1; i < len(trades); i += 2 { // Every other trade is a sell (exit)
		if trades[i].Price > trades[i-1].Price {
			wins++
		}
	}

	return float64(wins) / float64(len(trades)/2)
}

func (bt *Backtester) calculateProfitFactor(trades []api.TradeRecord) float64 {
	grossProfit := 0.0
	grossLoss := 0.0

	for i := 1; i < len(trades); i += 2 {
		profit := (trades[i].Price - trades[i-1].Price) * trades[i].Quantity
		if profit > 0 {
			grossProfit += profit
		} else {
			grossLoss += math.Abs(profit)
		}
	}

	if grossLoss == 0 {
		return 999 // Very high profit factor
	}

	return grossProfit / grossLoss
}

// HealthCheck implements health check for backtester
func (bt *Backtester) HealthCheck(ctx context.Context) error {
	// Simple health check - verify database connectivity
	return bt.db.Ping(ctx)
}

// Issue: #141889238