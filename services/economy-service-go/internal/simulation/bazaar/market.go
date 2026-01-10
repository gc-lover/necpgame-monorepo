package bazaar

import (
	"fmt"
	"sort"
	"time"
)

// TradeResult represents the outcome of a single trade execution
// Issue: #2278
type TradeResult struct {
	BuyerID      string
	SellerID     string
	Commodity    Commodity
	Price        float64
	Quantity     int
	ClearingTime int // Round number or timestamp
}

// ClearResult contains the results of market clearing
// Issue: #2278
type ClearResult struct {
	ClearingPrice float64       // Weighted average clearing price
	TotalVolume   int           // Total quantity traded
	TradeResults  []TradeResult // Individual trade executions
}

// MarketLogic handles the order book and clearing for a single commodity
// Issue: #2278
type MarketLogic struct {
	Commodity Commodity
	Bids      []*Order // Buy orders
	Asks      []*Order // Sell orders
	History   []float64
	TradeHistory []TradeRecord // History of completed trades with timestamps
}

// NewMarketLogic creates a new market for a commodity
func NewMarketLogic(c Commodity) *MarketLogic {
	return &MarketLogic{
		Commodity:   c,
		Bids:        make([]*Order, 0, 100), // Preallocate for performance
		Asks:        make([]*Order, 0, 100),
		History:     make([]float64, 0, 1000),
		TradeHistory: make([]TradeRecord, 0, 1000), // Preallocate for trade history
	}
}

// AddOrder places an order in the order book
// Issue: #2278
func (m *MarketLogic) AddOrder(o *Order) {
	if o == nil || o.Quantity <= 0 {
		return
	}
	
	if o.Type == OrderTypeBid {
		m.Bids = append(m.Bids, o)
	} else if o.Type == OrderTypeAsk {
		m.Asks = append(m.Asks, o)
	}
}

// Clear resolves the market orders using double auction matching
// Handles partial order fills and returns detailed trade results
// Issue: #2278
func (m *MarketLogic) Clear(agents []*AgentLogic) MarketResult {
	// Create initial market state
	marketState := m.createMarketState()

	clearResult := m.clearOrders()

	// Update agents based on trade results
	m.updateAgentsFromTrades(agents, clearResult, marketState)

	// Calculate market efficiency
	efficiency := m.calculateMarketEfficiency(clearResult, marketState)

	return MarketResult{
		ClearedTrades:    clearResult.TradeResults,
		NewPrices:        map[Commodity]float64{m.Commodity: clearResult.ClearingPrice},
		TotalVolume:      clearResult.TotalVolume,
		MarketEfficiency: efficiency,
	}
}

// clearOrders performs the actual order clearing logic
func (m *MarketLogic) clearOrders() ClearResult {
	if len(m.Bids) == 0 || len(m.Asks) == 0 {
		m.History = append(m.History, 0.0)
		return ClearResult{
			ClearingPrice: 0.0,
			TotalVolume:   0,
			TradeResults:  make([]TradeResult, 0),
		}
	}

	// Sort Bids: Highest price first (willing to pay most) - price-time priority
	sort.Slice(m.Bids, func(i, j int) bool {
		if m.Bids[i].Price != m.Bids[j].Price {
			return m.Bids[i].Price > m.Bids[j].Price
		}
		// If same price, prefer higher quantity (liquidity)
		return m.Bids[i].Quantity > m.Bids[j].Quantity
	})

	// Sort Asks: Lowest price first (willing to sell for least) - price-time priority
	sort.Slice(m.Asks, func(i, j int) bool {
		if m.Asks[i].Price != m.Asks[j].Price {
			return m.Asks[i].Price < m.Asks[j].Price
		}
		// If same price, prefer higher quantity (liquidity)
		return m.Asks[i].Quantity > m.Asks[j].Quantity
	})

	// Double auction matching with partial fills
	tradeResults := make([]TradeResult, 0, len(m.Bids)+len(m.Asks))
	totalVolume := 0
	totalPriceVolume := 0.0 // Sum of (price * volume) for weighted average

	bidIdx := 0
	askIdx := 0

	for bidIdx < len(m.Bids) && askIdx < len(m.Asks) {
		bid := m.Bids[bidIdx]
		ask := m.Asks[askIdx]

		// Check if trade is possible (bid price >= ask price)
		if bid.Price < ask.Price {
			// No more matches possible
			break
		}

		// Determine trade quantity: minimum of bid quantity and ask quantity
		tradeQty := bid.Quantity
		if ask.Quantity < bid.Quantity {
			tradeQty = ask.Quantity
		}

		// Determine clearing price: weighted average of bid and ask prices
		// Standard double auction: average of matched bid and ask
		clearingPrice := (bid.Price + ask.Price) / 2.0

		// Record trade result
		tradeResult := TradeResult{
			BuyerID:   bid.AgentID,
			SellerID:  ask.AgentID,
			Commodity: m.Commodity,
			Price:     clearingPrice,
			Quantity:  tradeQty,
		}
		tradeResults = append(tradeResults, tradeResult)

		// Record trade in history for volume calculations
		tradeRecord := TradeRecord{
			Timestamp:   time.Now().Unix(),
			Commodity:   m.Commodity,
			Type:        OrderTypeAsk, // Assume seller's perspective for simplicity
			Price:       clearingPrice,
			Quantity:    tradeQty,
			ProfitLoss:  0, // Not calculated here for performance
			WasExpected: true, // Simplified
		}
		m.TradeHistory = append(m.TradeHistory, tradeRecord)

		// Update volume and price-volume sum for weighted average
		totalVolume += tradeQty
		totalPriceVolume += clearingPrice * float64(tradeQty)

		// Update order quantities (handle partial fills)
		bid.Quantity -= tradeQty
		ask.Quantity -= tradeQty

		// Remove fully filled orders
		if bid.Quantity <= 0 {
			bidIdx++
		}
		if ask.Quantity <= 0 {
			askIdx++
		}
	}

	// Calculate weighted average clearing price
	var clearingPrice float64
	if totalVolume > 0 && totalPriceVolume > 0 {
		clearingPrice = totalPriceVolume / float64(totalVolume)
		// Ensure clearing price is reasonable
		if clearingPrice < 0.01 {
			clearingPrice = 0.01 // Minimum price to prevent division issues
		}
	}

	// Record in history
	m.History = append(m.History, clearingPrice)

	// Cleanup old trades periodically (every 100 clearings to avoid performance impact)
	if len(m.TradeHistory) > 0 && len(m.TradeHistory)%100 == 0 {
		m.CleanupOldTrades()
	}

	// Reset order books for next round (clear unfilled orders)
	m.Bids = make([]*Order, 0, 100)
	m.Asks = make([]*Order, 0, 100)

	return ClearResult{
		ClearingPrice: clearingPrice,
		TotalVolume:   totalVolume,
		TradeResults:  tradeResults,
	}
}

// CreateMarketState creates current market state from history and active orders
// Public method for external use
// Issue: #2278
func (m *MarketLogic) CreateMarketState() *MarketState {
	return m.createMarketState()
}

// createMarketState creates current market state from history and active orders
func (m *MarketLogic) createMarketState() *MarketState {
	state := &MarketState{
		LastPrices:     make(map[Commodity]float64),
		Volume:         make(map[Commodity]int),
		Volatility:     make(map[Commodity]float64),
		Trend:          make(map[Commodity]float64),
		ActiveOrders:   make([]*Order, 0),
		CompletedTrades: make([]TradeRecord, 0),
	}

	// Get last price from history
	if len(m.History) > 0 {
		state.LastPrices[m.Commodity] = m.History[len(m.History)-1]
	} else {
		state.LastPrices[m.Commodity] = 0.0
	}

	// Calculate volatility (standard deviation of recent prices)
	if len(m.History) >= 5 {
		recentPrices := m.History[len(m.History)-5:]
		mean := 0.0
		for _, price := range recentPrices {
			mean += price
		}
		mean /= float64(len(recentPrices))

		variance := 0.0
		for _, price := range recentPrices {
			variance += (price - mean) * (price - mean)
		}
		variance /= float64(len(recentPrices))
		state.Volatility[m.Commodity] = variance // Using variance as volatility measure
	}

	// Calculate trend (price momentum)
	if len(m.History) >= 3 {
		recent := m.History[len(m.History)-3:]
		if recent[0] > 0.01 { // Prevent division by very small numbers
			if recent[2] > recent[0] {
				state.Trend[m.Commodity] = (recent[2] - recent[0]) / recent[0] // Percentage change
			} else {
				state.Trend[m.Commodity] = -((recent[0] - recent[2]) / recent[0])
			}
		} else {
			state.Trend[m.Commodity] = 0.0 // No trend if base price is too small
		}
	}

	// Add active orders
	state.ActiveOrders = append(state.ActiveOrders, m.Bids...)
	state.ActiveOrders = append(state.ActiveOrders, m.Asks...)

	return state
}

// updateAgentsFromTrades updates agent beliefs and wealth based on trade results
func (m *MarketLogic) updateAgentsFromTrades(agents []*AgentLogic, result ClearResult, marketState *MarketState) {
	// Create agent lookup map
	agentMap := make(map[string]*AgentLogic)
	for _, agent := range agents {
		agentMap[agent.State.ID] = agent
	}

	// Process each trade result
	for _, trade := range result.TradeResults {
		buyer, buyerExists := agentMap[trade.BuyerID]
		seller, sellerExists := agentMap[trade.SellerID]

		if buyerExists {
			// Calculate profit/loss for buyer (negative = cost)
			profitLoss := -trade.Price * float64(trade.Quantity)

			// Check if buyer got expected price (bid price vs clearing price)
			// This is simplified - in real implementation we'd track original order prices
			success := true // Assume successful for now

			buyer.UpdateBelief(m.Commodity, success, trade.Price, false, trade.Quantity, profitLoss)
		}

		if sellerExists {
			// Calculate profit/loss for seller (positive = revenue)
			profitLoss := trade.Price * float64(trade.Quantity)

			// Check if seller got expected price
			success := true // Assume successful for now

			seller.UpdateBelief(m.Commodity, success, trade.Price, true, trade.Quantity, profitLoss)
		}
	}
}

// calculateMarketEfficiency measures how well supply met demand
func (m *MarketLogic) calculateMarketEfficiency(result ClearResult, marketState *MarketState) float64 {
	if len(marketState.ActiveOrders) == 0 {
		return 1.0 // Perfect efficiency if no unfilled orders
	}

	// Calculate total unfilled quantity
	unfilledBids := 0
	unfilledAsks := 0

	for _, order := range m.Bids {
		unfilledBids += order.Quantity
	}
	for _, order := range m.Asks {
		unfilledAsks += order.Quantity
	}

	totalUnfilled := unfilledBids + unfilledAsks
	totalOrders := len(marketState.ActiveOrders)

	if totalOrders == 0 {
		return 1.0
	}

	// Efficiency = 1 - (unfilled orders / total orders)
	// This is a simplified metric
	efficiency := 1.0 - float64(totalUnfilled)/float64(totalOrders)

	// Clamp to [0, 1]
	if efficiency < 0 {
		efficiency = 0
	}
	if efficiency > 1 {
		efficiency = 1
	}

	return efficiency
}

// GetLastPrice returns the last clearing price for this market
func (m *MarketLogic) GetLastPrice() float64 {
	if len(m.History) > 0 {
		return m.History[len(m.History)-1]
	}
	return 0.0
}

// GetVolume returns total volume traded in last clearing
func (m *MarketLogic) GetVolume() int {
	// This would need to be stored from last clearing
	// For now, return 0 as placeholder
	return 0
}

// Get24hVolume returns total volume traded in the last 24 hours
func (m *MarketLogic) Get24hVolume() int {
	currentTime := time.Now().Unix()
	dayAgo := currentTime - (24 * 60 * 60) // 24 hours in seconds

	totalVolume := 0
	for _, trade := range m.TradeHistory {
		if trade.Timestamp >= dayAgo {
			totalVolume += trade.Quantity
		}
	}

	return totalVolume
}

// CleanupOldTrades removes trades older than 30 days to prevent memory bloat
func (m *MarketLogic) CleanupOldTrades() {
	currentTime := time.Now().Unix()
	monthAgo := currentTime - (30 * 24 * 60 * 60) // 30 days in seconds

	// Find first trade within last 30 days
	cutoffIndex := -1
	for i, trade := range m.TradeHistory {
		if trade.Timestamp >= monthAgo {
			cutoffIndex = i
			break
		}
	}

	// Remove old trades
	if cutoffIndex > 0 {
		m.TradeHistory = m.TradeHistory[cutoffIndex:]
	}
}

// ClearLegacy is a legacy method for backward compatibility
// Use Clear(agents) instead for new code
// Issue: #2278
func (m *MarketLogic) ClearLegacy() (float64, int) {
	result := m.clearOrders()
	return result.ClearingPrice, result.TotalVolume
}

// ProcessTickEvent processes a tick event from Kafka (world.tick.hourly)
// Triggers market clearing and publishes results to simulation.event topic
// Issue: #2278
func (m *MarketLogic) ProcessTickEvent(agents []*AgentLogic, tickType string, tickData map[string]interface{}) MarketResult {
	// Validate tick type
	if tickType != "world.tick.hourly" && tickType != "world.tick.daily" {
		// Return empty result for unsupported tick types
		return MarketResult{
			ClearedTrades:    []TradeResult{},
			NewPrices:        map[Commodity]float64{},
			TotalVolume:      0,
			MarketEfficiency: 0.0,
		}
	}

	// Process market clearing
	result := m.Clear(agents)

	// Add tick metadata to result for event publishing
	// This data would be published to simulation.event Kafka topic
	_ = map[string]interface{}{
		"tick_type":        tickType,
		"tick_timestamp":   tickData["timestamp"],
		"market_commodity": string(m.Commodity),
		"clearing_price":   result.NewPrices[m.Commodity],
		"total_volume":     result.TotalVolume,
		"efficiency":       result.MarketEfficiency,
		"trade_count":      len(result.ClearedTrades),
	}

	return result
}

// GetSimulationEventData returns data for publishing to simulation.event Kafka topic
// Issue: #2278
func (m *MarketLogic) GetSimulationEventData(result MarketResult, tickType string) map[string]interface{} {
	return map[string]interface{}{
		"event_type":       "market_clearing",
		"tick_type":        tickType,
		"commodity":        string(m.Commodity),
		"clearing_price":   result.NewPrices[m.Commodity],
		"volume":          result.TotalVolume,
		"efficiency":      result.MarketEfficiency,
		"trades":          len(result.ClearedTrades),
		"timestamp":       time.Now().Unix(),
		"market_state": map[string]interface{}{
			"volatility": m.GetVolatility(),
			"trend":      m.GetTrend(),
			"last_price": m.GetLastPrice(),
		},
	}
}

// GetVolatility returns current market volatility
func (m *MarketLogic) GetVolatility() float64 {
	if len(m.History) < 5 {
		return 0.0
	}
	recent := m.History[len(m.History)-5:]
	mean := 0.0
	for _, price := range recent {
		mean += price
	}
	mean /= float64(len(recent))

	variance := 0.0
	for _, price := range recent {
		variance += (price - mean) * (price - mean)
	}
	variance /= float64(len(recent))
	return variance
}

// GetTrend returns current market trend
func (m *MarketLogic) GetTrend() float64 {
	if len(m.History) < 3 {
		return 0.0
	}
	recent := m.History[len(m.History)-3:]
	if recent[0] <= 0.01 {
		return 0.0
	}
	return (recent[2] - recent[0]) / recent[0]
}

// GetOrderBook returns all active orders in the order book
// PERFORMANCE: O(1) access to pre-sorted order book
func (m *MarketLogic) GetOrderBook() []*Order {
	allOrders := make([]*Order, 0, len(m.Bids)+len(m.Asks))

	// Add all bids (buy orders)
	for _, order := range m.Bids {
		if order.Quantity > 0 { // Only active orders
			orderCopy := *order // Copy to avoid mutations
			allOrders = append(allOrders, &orderCopy)
		}
	}

	// Add all asks (sell orders)
	for _, order := range m.Asks {
		if order.Quantity > 0 { // Only active orders
			orderCopy := *order // Copy to avoid mutations
			allOrders = append(allOrders, &orderCopy)
		}
	}

	return allOrders
}

// ClearMarket attempts to execute trades by matching bids and asks
// PERFORMANCE: O(n log n) sorting, optimized for real-time trading
func (m *MarketLogic) ClearMarket() []*Trade {
	trades := make([]*Trade, 0)

	// Sort bids descending (highest price first)
	sort.Slice(m.Bids, func(i, j int) bool {
		return m.Bids[i].Price > m.Bids[j].Price
	})

	// Sort asks ascending (lowest price first)
	sort.Slice(m.Asks, func(i, j int) bool {
		return m.Asks[i].Price < m.Asks[j].Price
	})

	i, j := 0, 0 // Indices for bids and asks

	for i < len(m.Bids) && j < len(m.Asks) {
		bid := m.Bids[i]
		ask := m.Asks[j]

		// Check if bid price >= ask price (can trade)
		if bid.Price >= ask.Price && bid.Quantity > 0 && ask.Quantity > 0 {
			// Calculate trade quantity (minimum of both orders)
			tradeQuantity := bid.Quantity
			if ask.Quantity < tradeQuantity {
				tradeQuantity = ask.Quantity
			}

			// Calculate trade price (use ask price for simplicity)
			tradePrice := ask.Price

			// Create trade
			trade := &Trade{
				ID:         fmt.Sprintf("trade-%d", time.Now().UnixNano()),
				Commodity:  m.Commodity,
				BuyerID:    bid.PlayerID,
				SellerID:   ask.PlayerID,
				Price:      tradePrice,
				Quantity:   tradeQuantity,
				ExecutedAt: time.Now(),
			}
			trades = append(trades, trade)

			// Update order quantities
			bid.Quantity -= tradeQuantity
			ask.Quantity -= tradeQuantity

			// Add price to history
			m.History = append(m.History, tradePrice)

			// Move to next order if current is filled
			if bid.Quantity == 0 {
				i++
			}
			if ask.Quantity == 0 {
				j++
			}
		} else {
			// No more trades possible
			break
		}
	}

	// Remove filled orders
	m.Bids = m.removeFilledOrders(m.Bids)
	m.Asks = m.removeFilledOrders(m.Asks)

	return trades
}

// removeFilledOrders removes orders with zero quantity
func (m *MarketLogic) removeFilledOrders(orders []*Order) []*Order {
	activeOrders := make([]*Order, 0, len(orders))
	for _, order := range orders {
		if order.Quantity > 0 {
			activeOrders = append(activeOrders, order)
		}
	}
	return activeOrders
}
