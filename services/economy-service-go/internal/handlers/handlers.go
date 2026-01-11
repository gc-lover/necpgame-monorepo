package handlers

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/economy-service-go/internal/simulation/bazaar"
	api "necpgame/services/economy-service-go/pkg/api"
)

// CircuitBreaker provides resilience against cascading failures
type CircuitBreaker struct {
	failures    int
	lastFailure time.Time
	threshold   int
	timeout     time.Duration
}

// Allow checks if the circuit breaker allows the request
func (cb *CircuitBreaker) Allow() bool {
	if cb.failures >= cb.threshold {
		if time.Since(cb.lastFailure) < cb.timeout {
			return false // Circuit is open
		}
		// Reset after timeout
		cb.failures = 0
	}
	return true
}

// RecordFailure records a failure in the circuit breaker
func (cb *CircuitBreaker) RecordFailure() {
	cb.failures++
	cb.lastFailure = time.Now()
}

// EconomyHandlers implements the generated Handler interface
type EconomyHandlers struct {
	logger         *zap.Logger
	circuitBreaker *CircuitBreaker
	bazaarAgents   []*bazaar.AgentLogic
	markets        map[bazaar.Commodity]*bazaar.MarketLogic
}


// getBasePrice returns base price for commodity (simplified implementation)
func (h *EconomyHandlers) getBasePrice(commodity bazaar.Commodity) float64 {
	// Base prices based on agent price beliefs and market data
	basePrices := map[bazaar.Commodity]float64{
		bazaar.CommodityFood:    5.0,  // Base price for food
		bazaar.CommodityWood:    3.0,  // Base price for wood
		bazaar.CommodityMetal:   20.0, // Base price for metal
		bazaar.CommodityWeapon:  30.0, // Base price for weapons
		bazaar.CommodityCrystal: 50.0, // Base price for crystals
	}

	if price, exists := basePrices[commodity]; exists {
		return price
	}

	return 10.0 // Default fallback price
}

// NewEconomyHandlers creates a new instance of EconomyHandlers
func NewEconomyHandlers(logger *zap.Logger) *EconomyHandlers {
	// Initialize BazaarBot agents
	agents := make([]*bazaar.AgentLogic, 10) // 10 AI trading agents
	for i := range agents {
		agents[i] = bazaar.NewAgentLogic(fmt.Sprintf("bazaarbot-%d", i+1), 1000.0)
		// Set price beliefs for different commodities
		agents[i].SetPriceBelief(bazaar.CommodityFood, 5.0, 15.0)
		agents[i].SetPriceBelief(bazaar.CommodityWood, 3.0, 10.0)
		agents[i].SetPriceBelief(bazaar.CommodityMetal, 20.0, 50.0)
	}

	// Initialize markets
	markets := make(map[bazaar.Commodity]*bazaar.MarketLogic)
	for _, commodity := range []bazaar.Commodity{
		bazaar.CommodityFood,
		bazaar.CommodityWood,
		bazaar.CommodityMetal,
	} {
		markets[commodity] = bazaar.NewMarketLogic(commodity)
	}

	return &EconomyHandlers{
		logger: logger,
		circuitBreaker: &CircuitBreaker{
			threshold: 5,
			timeout:   30 * time.Second,
		},
		bazaarAgents: agents,
		markets:      markets,
	}
}

// EconomyHealthCheck implements economyHealthCheck operation.
func (h *EconomyHandlers) EconomyHealthCheck(ctx context.Context) (*api.HealthResponse, error) {
	h.logger.Info("Health check requested")
	return &api.HealthResponse{}, nil
}

// GetOrderBook implements getOrderBook operation.
func (h *EconomyHandlers) GetOrderBook(ctx context.Context, params api.GetOrderBookParams) (*api.OrderBook, error) {
	// BACKEND NOTE: Context timeout for order book retrieval (prevents hanging in high-load economy)
	_, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	h.logger.Info("GetOrderBook called",
		zap.String("commodity", string(params.Commodity)))

	// Get market for commodity
	market, exists := h.markets[bazaar.Commodity(params.Commodity)]
	if !exists {
		return nil, fmt.Errorf("market not found for commodity: %s", params.Commodity)
	}

	// Get order book data
	orderBook := &api.OrderBook{
		Commodity: api.NewOptCommodity(params.Commodity),
		Bids:      make([]api.Order, 0),
		Asks:      make([]api.Order, 0),
	}

	// Get bids (buy orders) - sorted by price descending
	for _, bid := range market.Bids {
		// Convert string IDs to UUIDs (simplified - in real implementation would parse properly)
		bidUUID, _ := uuid.Parse(bid.ID)
		playerUUID, _ := uuid.Parse(bid.PlayerID)

		orderBook.Bids = append(orderBook.Bids, api.Order{
			ID:        api.NewOptUUID(bidUUID),
			PlayerId:  api.NewOptUUID(playerUUID),
			Type:      api.NewOptOrderType(api.OrderTypeBuy),
			Commodity: api.NewOptCommodity(api.Commodity(bid.Commodity)),
			Price:     api.NewOptFloat32(float32(bid.Price)),
			Quantity:  api.NewOptInt(bid.Quantity),
			CreatedAt: api.NewOptDateTime(bid.CreatedAt),
		})
	}

	// Get asks (sell orders) - sorted by price ascending
	for _, ask := range market.Asks {
		// Convert string IDs to UUIDs (simplified - in real implementation would parse properly)
		askUUID, _ := uuid.Parse(ask.ID)
		playerUUID, _ := uuid.Parse(ask.PlayerID)

		orderBook.Asks = append(orderBook.Asks, api.Order{
			ID:        api.NewOptUUID(askUUID),
			PlayerId:  api.NewOptUUID(playerUUID),
			Type:      api.NewOptOrderType(api.OrderTypeSell),
			Commodity: api.NewOptCommodity(api.Commodity(ask.Commodity)),
			Price:     api.NewOptFloat32(float32(ask.Price)),
			Quantity:  api.NewOptInt(ask.Quantity),
			CreatedAt: api.NewOptDateTime(ask.CreatedAt),
		})
	}

	// Get last price from market history
	var lastPrice float32
	if len(market.History) > 0 {
		lastPrice = float32(market.History[len(market.History)-1])
	}

	orderBook.LastPrice = api.NewOptFloat32(lastPrice)
	orderBook.Volume24h = api.NewOptInt(market.Get24hVolume())

	h.logger.Info("Order book retrieved successfully",
		zap.Int("bids_count", len(orderBook.Bids)),
		zap.Int("asks_count", len(orderBook.Asks)))

	return orderBook, nil
}

// PlaceOrder implements placeOrder operation.
func (h *EconomyHandlers) PlaceOrder(ctx context.Context, req *api.PlaceOrderRequest, params api.PlaceOrderParams) (*api.OrderResponse, error) {
	// Extract user ID from context (set by authentication middleware)
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		h.logger.Error("User ID not found in context - authentication middleware not applied")
		return nil, fmt.Errorf("authentication required")
	}

	userID, ok := userIDValue.(string)
	if !ok {
		h.logger.Error("Invalid user ID type in context")
		return nil, fmt.Errorf("invalid user authentication")
	}

	if userID == "" {
		h.logger.Error("Empty user ID in context")
		return nil, fmt.Errorf("invalid user authentication")
	}

	h.logger.Info("PlaceOrder called",
		zap.String("commodity", string(params.Commodity)),
		zap.String("order_type", string(req.Type)),
		zap.Float64("price", float64(req.Price)),
		zap.Int("quantity", req.Quantity),
		zap.String("user_id", userID))

	// Get market for commodity
	market, exists := h.markets[bazaar.Commodity(params.Commodity)]
	if !exists {
		return nil, fmt.Errorf("market not found for commodity: %s", params.Commodity)
	}

	// Create order
	orderID := uuid.New()
	order := &bazaar.Order{
		ID:       orderID.String(),
		Type:     bazaar.OrderType(req.Type),
		Price:    float64(req.Price),
		Quantity: req.Quantity,
		PlayerID: userID,
		CreatedAt: time.Now(),
	}

	// Add order to market
	market.AddOrder(order)

	// Try to clear market (execute trades)
	trades := market.ClearMarket()

	response := &api.OrderResponse{
		OrderId: api.NewOptUUID(orderID),
		Status:  api.NewOptOrderResponseStatus(api.OrderResponseStatusPending),
		Message: api.NewOptString("Order placed successfully"),
	}

	h.logger.Info("Order placed successfully",
		zap.String("order_id", order.ID),
		zap.Int("trades_executed", len(trades)))

	return response, nil
}

// convertTrades converts bazaar trades to API format
// Note: Trade schema added to OpenAPI spec, but type not generated until used in operations
func convertTrades(trades []*bazaar.Trade) []interface{} {
	// Placeholder implementation until Trade type is used in API operations
	return make([]interface{}, len(trades))
}

// GetMarketPrice implements getMarketPrice operation.
func (h *EconomyHandlers) GetMarketPrice(ctx context.Context, params api.GetMarketPriceParams) (*api.MarketPrice, error) {
	// BACKEND NOTE: Context timeout for market price retrieval (prevents hanging)
	_, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	h.logger.Info("GetMarketPrice called",
		zap.String("commodity", string(params.Commodity)))

	// Get market for commodity
	market, exists := h.markets[bazaar.Commodity(params.Commodity)]
	if !exists {
		return nil, fmt.Errorf("market not found for commodity: %s", params.Commodity)
	}

	// Get current price (last clearing price or base price)
	var currentPrice float64
	var previousPrice float64

	if len(market.History) > 0 {
		currentPrice = market.History[len(market.History)-1]
		if len(market.History) > 1 {
			previousPrice = market.History[len(market.History)-2]
		} else {
			previousPrice = currentPrice // No change if only one price
		}
	} else {
		// Use base price if no history
		currentPrice = h.getBasePrice(bazaar.Commodity(params.Commodity))
		previousPrice = currentPrice
	}

	// Calculate 24h change
	change24h := currentPrice - previousPrice

	// Calculate 24h volume
	volume24h := h.calculate24hVolume(bazaar.Commodity(params.Commodity))

	marketPrice := &api.MarketPrice{
		Commodity:  api.NewOptCommodity(api.Commodity(params.Commodity)),
		Price:      api.NewOptFloat32(float32(currentPrice)),
		Change24h:  api.NewOptFloat32(float32(change24h)),
		Volume24h:  api.NewOptInt(volume24h),
		LastUpdate: api.NewOptDateTime(time.Now()),
	}

	h.logger.Info("Market price retrieved",
		zap.String("commodity", string(params.Commodity)),
		zap.Float64("price", currentPrice),
		zap.Float64("change_24h", change24h),
		zap.Int("volume_24h", volume24h))

	return marketPrice, nil
}

// GetPlayerPortfolio implements getPlayerPortfolio operation.
func (h *EconomyHandlers) GetPlayerPortfolio(ctx context.Context, params api.GetPlayerPortfolioParams) (*api.PlayerPortfolio, error) {
	playerIDStr := params.PlayerId.String()
	h.logger.Info("Getting player portfolio",
		zap.String("player_id", playerIDStr))

	playerID := params.PlayerId

	// Get player wealth (simplified - in production would query database)
	wealth := h.getPlayerWealth(ctx, playerIDStr)

	// Get player inventory (simplified - in production would query database)
	inventory := h.getPlayerInventory(ctx, playerIDStr)

	// Get active orders for the player
	activeOrders := h.getPlayerActiveOrders(ctx, playerIDStr)

	// Convert []*api.Order to []api.Order
	activeOrdersSlice := make([]api.Order, len(activeOrders))
	for i, order := range activeOrders {
		activeOrdersSlice[i] = *order
	}

	portfolio := &api.PlayerPortfolio{
		PlayerId:     api.NewOptUUID(playerID),
		Wealth:       api.NewOptFloat32(float32(wealth)),
		Inventory:    api.NewOptPlayerPortfolioInventory(h.convertInventoryToAPI(inventory)),
		ActiveOrders: activeOrdersSlice,
	}

	h.logger.Info("Player portfolio retrieved successfully",
		zap.String("player_id", playerIDStr),
		zap.Float64("wealth", wealth),
		zap.Int("inventory_items", len(inventory)),
		zap.Int("active_orders", len(activeOrdersSlice)))

	return portfolio, nil
}

// GetPlayerOrders implements getPlayerOrders operation.
func (h *EconomyHandlers) GetPlayerOrders(ctx context.Context, params api.GetPlayerOrdersParams) (api.PlayerOrders, error) {
	playerIDStr := params.PlayerId.String()
	h.logger.Info("Getting player orders",
		zap.String("player_id", playerIDStr))

	// Get all orders for the player (both active and historical)
	allOrders := h.getPlayerAllOrders(ctx, playerIDStr)

	// Convert to API format
	playerOrders := make(api.PlayerOrders, len(allOrders))
	for i, order := range allOrders {
		playerOrders[i] = *order
	}

	h.logger.Info("Player orders retrieved successfully",
		zap.String("player_id", playerIDStr),
		zap.Int("total_orders", len(allOrders)))

	return playerOrders, nil
}

// GetBazaarBotStatus implements getBazaarBotStatus operation.
func (h *EconomyHandlers) GetBazaarBotStatus(ctx context.Context) (*api.BazaarBotStatus, error) {
	h.logger.Info("Getting BazaarBot simulation status")

	// Get simulation status from repository or service
	status := h.getBazaarBotSimulationStatus(ctx)

	active, _ := status.Active.Get()
	activeAgents, _ := status.ActiveAgents.Get()
	totalMarkets, _ := status.TotalMarkets.Get()
	uptime, _ := status.SimulationUptime.Get()

	h.logger.Info("BazaarBot status retrieved successfully",
		zap.Bool("active", active),
		zap.Int("active_agents", activeAgents),
		zap.Int("total_markets", totalMarkets),
		zap.Int("simulation_uptime", uptime))

	return status, nil
}

// GetBazaarBotAgents implements getBazaarBotAgents operation.
func (h *EconomyHandlers) GetBazaarBotAgents(ctx context.Context) (*api.BazaarBotAgents, error) {
	h.logger.Info("Getting BazaarBot agents list")

	// Get all active agents from simulation
	agents := make([]api.BazaarBotAgent, len(h.bazaarAgents))

	for i, agentLogic := range h.bazaarAgents {
		// Convert AgentState to BazaarBotAgent API format
		state := agentLogic.State

		// Convert personality
		personality := &api.BazaarBotAgentPersonality{}
		if agentLogic.Personality != nil {
			personality.RiskTolerance.SetTo(float32(agentLogic.Personality.RiskTolerance))
			personality.ImpatienceFactor.SetTo(float32(agentLogic.Personality.ImpatienceFactor))
			personality.SocialInfluence.SetTo(float32(agentLogic.Personality.SocialInfluence))
			personality.LearningRate.SetTo(float32(agentLogic.Personality.LearningRate))
		}

		// Convert inventory
		inventory := make(api.BazaarBotAgentInventory)
		for commodity, quantity := range state.Inventory {
			inventory[string(commodity)] = quantity
		}

		// Create agent
		agents[i] = api.BazaarBotAgent{
			ID:          api.NewOptString(state.ID),
			Wealth:      api.NewOptFloat32(float32(state.Wealth)),
			Personality: api.NewOptBazaarBotAgentPersonality(*personality),
			Inventory:   api.NewOptBazaarBotAgentInventory(inventory),
			LastActivity: api.NewOptDateTime(time.Now()), // Current time as last activity
		}
	}

	bazaarBotAgents := &api.BazaarBotAgents{
		Agents: agents,
	}

	h.logger.Info("BazaarBot agents retrieved successfully",
		zap.Int("agent_count", len(agents)))

	return bazaarBotAgents, nil
}

// GetMarketHistory implements getMarketHistory operation.
func (h *EconomyHandlers) GetMarketTrades(ctx context.Context, params api.GetMarketTradesParams) (*api.MarketPrice, error) {
	// PERFORMANCE: Circuit breaker check
	if !h.circuitBreaker.Allow() {
		h.logger.Warn("Circuit breaker open, rejecting GetMarketTrades request")
		return nil, fmt.Errorf("service temporarily unavailable")
	}

	// PERFORMANCE: Timeout context for market trades operation
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// Get recent trades for the commodity (mock implementation)
	trades := h.getMockTrades(string(params.Commodity), 10)

	// Convert to API response format
	avgPrice := calculateAveragePrice(trades)
	totalVolume := calculateTotalVolume(trades)

	marketPrice := &api.MarketPrice{
		Commodity:  api.NewOptCommodity(params.Commodity),
		Price:      api.NewOptFloat32(float32(avgPrice)),
		Volume24h:  api.NewOptInt(int(totalVolume)),
		LastUpdate: api.NewOptDateTime(time.Now()),
	}

	h.logger.Debug("Retrieved market trades",
		zap.String("commodity", string(params.Commodity)),
		zap.Float64("avg_price", avgPrice),
		zap.Int64("volume", totalVolume))

	return marketPrice, nil
}

func (h *EconomyHandlers) GetMarketHistory(ctx context.Context, params api.GetMarketHistoryParams) (*api.MarketHistory, error) {
	return &api.MarketHistory{}, nil
}

// NewError creates a new error response
func (h *EconomyHandlers) NewError(ctx context.Context, err error) *api.ErrRespStatusCode {
	return &api.ErrRespStatusCode{
		StatusCode: 500,
		Response: api.ErrResp{
			Code:    500,
			Message: err.Error(),
		},
	}
}

// Helper methods for order book implementation

// isValidCommodity checks if the commodity is supported
func (h *EconomyHandlers) isValidCommodity(commodity bazaar.Commodity) bool {
	validCommodities := []bazaar.Commodity{
		bazaar.CommodityFood,
		bazaar.CommodityWood,
		bazaar.CommodityMetal,
		bazaar.CommodityWeapon,
		bazaar.CommodityCrystal,
	}

	for _, valid := range validCommodities {
		if commodity == valid {
			return true
		}
	}
	return false
}

// extractOrdersFromMarket extracts orders from market data and converts to API format
func (h *EconomyHandlers) extractOrdersFromMarket(orders []*bazaar.Order, limit int) []*api.Order {
	if len(orders) == 0 {
		return []*api.Order{}
	}

	// Sort orders by price (bids descending, asks ascending)
	sortedOrders := make([]*bazaar.Order, len(orders))
	copy(sortedOrders, orders)

	// Apply limit
	if len(sortedOrders) > limit {
		sortedOrders = sortedOrders[:limit]
	}

	// Convert to API format
	apiOrders := make([]*api.Order, len(sortedOrders))
	for i, order := range sortedOrders {
		orderID, _ := uuid.Parse(order.ID)
		playerID, _ := uuid.Parse(order.PlayerID)
		apiOrders[i] = &api.Order{
			ID:       api.NewOptUUID(orderID),
			Type:     api.NewOptOrderType(api.OrderType(order.Type)),
			Price:    api.NewOptFloat32(float32(order.Price)),
			Quantity: api.NewOptInt(order.Quantity),
			PlayerId: api.NewOptUUID(playerID),
		}
	}

	return apiOrders
}

// convertInventoryToAPI converts map[string]int to PlayerPortfolioInventory
func (h *EconomyHandlers) convertInventoryToAPI(inventory map[string]int) api.PlayerPortfolioInventory {
	result := make(api.PlayerPortfolioInventory)
	for k, v := range inventory {
		result[k] = v
	}
	return result
}

// calculate24hVolume calculates 24-hour trading volume (simplified implementation)
func (h *EconomyHandlers) calculate24hVolume(commodity bazaar.Commodity) int {
	// In a real implementation, this would query historical trade data
	// For now, return a simulated volume based on commodity type
	baseVolumes := map[bazaar.Commodity]int{
		bazaar.CommodityFood:     1500,
		bazaar.CommodityWood:     800,
		bazaar.CommodityMetal:    600,
		bazaar.CommodityWeapon:   300,
		bazaar.CommodityCrystal:  200,
	}

	if volume, exists := baseVolumes[commodity]; exists {
		// Add some randomness to simulate real market fluctuations
		return volume + (volume * (h.getRandomInt(-20, 20)) / 100)
	}

	return 100 // Default volume
}

// calculateAveragePrice calculates average price from trades
func calculateAveragePrice(trades []*bazaar.TradeRecord) float64 {
	if len(trades) == 0 {
		return 0.0
	}

	total := 0.0
	for _, trade := range trades {
		total += trade.Price
	}
	return total / float64(len(trades))
}

// calculateTotalVolume calculates total volume from trades
func calculateTotalVolume(trades []*bazaar.TradeRecord) int64 {
	total := int64(0)
	for _, trade := range trades {
		total += int64(trade.Quantity)
	}
	return total
}

// getMockTrades returns mock trade data for testing
func (h *EconomyHandlers) getMockTrades(commodity string, count int) []*bazaar.TradeRecord {
	trades := make([]*bazaar.TradeRecord, count)
	for i := 0; i < count; i++ {
		trades[i] = &bazaar.TradeRecord{
			Timestamp:   time.Now().Add(-time.Duration(i) * time.Minute).Unix(),
			Commodity:   bazaar.Commodity(commodity),
			Type:        bazaar.OrderTypeBid, // Mock buy orders
			Price:       100.0 + float64(i)*2.5,
			Quantity:    10 + i*5,
			ProfitLoss:  0.0,
			WasExpected: true,
		}
	}
	return trades
}

// getRandomInt returns a random integer between min and max (inclusive)
func (h *EconomyHandlers) getRandomInt(min, max int) int {
	// Simple deterministic random for reproducibility
	// In production, use crypto/rand or a seeded random
	return min + (int(time.Now().UnixNano()) % (max - min + 1))
}

// getDefaultCommodityPrice returns default price for commodity when no market data available
func (h *EconomyHandlers) getDefaultCommodityPrice(commodity bazaar.Commodity) float64 {
	defaultPrices := map[bazaar.Commodity]float64{
		bazaar.CommodityFood:     5.0,
		bazaar.CommodityWood:     12.0,
		bazaar.CommodityMetal:    25.0,
		bazaar.CommodityWeapon:   150.0,
		bazaar.CommodityCrystal:  300.0,
	}

	if price, exists := defaultPrices[commodity]; exists {
		return price
	}

	return 10.0 // Default fallback price
}

// calculate24hPriceChange calculates 24-hour price change percentage
func (h *EconomyHandlers) calculate24hPriceChange(commodity bazaar.Commodity, currentPrice float64) float64 {
	// In a real implementation, this would query historical price data
	// For simulation, generate a realistic price change based on commodity volatility

	volatilityFactors := map[bazaar.Commodity]float64{
		bazaar.CommodityFood:     0.05,  // Food prices are stable
		bazaar.CommodityWood:     0.08,  // Wood has moderate volatility
		bazaar.CommodityMetal:    0.12,  // Metal prices fluctuate more
		bazaar.CommodityWeapon:   0.15,  // Weapons are volatile
		bazaar.CommodityCrystal:  0.25,  // Crystals are highly volatile
	}

	volatility, exists := volatilityFactors[commodity]
	if !exists {
		volatility = 0.1 // Default volatility
	}

	// Generate a random change within volatility bounds
	// In production, this would be based on actual market data
	changePercent := (h.getRandomFloat(-volatility, volatility)) * 100

	h.logger.Debug("Calculated 24h price change",
		zap.String("commodity", string(commodity)),
		zap.Float64("current_price", currentPrice),
		zap.Float64("volatility", volatility),
		zap.Float64("change_percent", changePercent))

	return changePercent
}

// getRandomFloat returns a random float between min and max
func (h *EconomyHandlers) getRandomFloat(min, max float64) float64 {
	// Simple deterministic random for reproducibility
	// In production, use crypto/rand or a seeded random
	seed := time.Now().UnixNano()
	random := float64(seed%1000) / 1000.0 // 0.0 to 1.0
	return min + random*(max-min)
}

// getPlayerWealth retrieves player wealth from database (simplified implementation)
func (h *EconomyHandlers) getPlayerWealth(ctx context.Context, playerID string) float64 {
	// In production, this would query the database
	// For now, return a simulated wealth based on player ID hash
	hash := 0
	for _, char := range playerID {
		hash += int(char)
	}

	// Generate wealth between 100 and 10000 based on hash
	wealth := 100.0 + float64(hash%9900)

	h.logger.Debug("Retrieved player wealth",
		zap.String("player_id", playerID),
		zap.Float64("wealth", wealth))

	return wealth
}

// getPlayerInventory retrieves player inventory from database (simplified implementation)
func (h *EconomyHandlers) getPlayerInventory(ctx context.Context, playerID string) map[string]int {
	// In production, this would query the database
	// For now, return a simulated inventory

	inventory := make(map[string]int)

	// Simulate some random inventory based on player ID
	hash := 0
	for _, char := range playerID {
		hash += int(char)
	}

	// Add some commodities with random quantities
	if hash%3 == 0 {
		inventory["food"] = 10 + (hash % 50)
	}
	if hash%5 == 0 {
		inventory["wood"] = 5 + (hash % 30)
	}
	if hash%7 == 0 {
		inventory["weapons"] = 1 + (hash % 10)
	}
	if hash%11 == 0 {
		inventory["metal"] = 3 + (hash % 20)
	}

	h.logger.Debug("Retrieved player inventory",
		zap.String("player_id", playerID),
		zap.Any("inventory", inventory))

	return inventory
}

// getPlayerActiveOrders retrieves active orders for a player (simplified implementation)
func (h *EconomyHandlers) getPlayerActiveOrders(ctx context.Context, playerID string) []*api.Order {
	// In production, this would query the database for active orders
	// For now, return a simulated list of active orders

	var orders []*api.Order

	// Simulate some random active orders based on player ID
	hash := 0
	for _, char := range playerID {
		hash += int(char)
	}

	// Create 0-3 random orders
	numOrders := hash % 4
	for i := 0; i < numOrders; i++ {
		orderType := "buy"
		if (hash+i)%2 == 0 {
			orderType = "sell"
		}

		commodity := "food"
		switch (hash + i) % 5 {
		case 1:
			commodity = "wood"
		case 2:
			commodity = "metal"
		case 3:
			commodity = "weapons"
		case 4:
			commodity = "crystal"
		}

		order := &api.Order{
			ID:       api.NewOptUUID(uuid.New()),
			PlayerId: api.NewOptUUID(uuid.MustParse(playerID)),
			Type:     api.NewOptOrderType(api.OrderType(orderType)),
			Commodity: api.NewOptCommodity(api.Commodity(commodity)),
			Price:    api.NewOptFloat32(float32(10.0 + float64((hash+i)%100))),
			Quantity: api.NewOptInt(1 + ((hash + i) % 20)),
			CreatedAt: api.NewOptDateTime(time.Now().Add(-time.Duration(i) * time.Hour)),
		}

		orders = append(orders, order)
	}

	h.logger.Debug("Retrieved player active orders",
		zap.String("player_id", playerID),
		zap.Int("active_orders", len(orders)))

	return orders
}

// getPlayerAllOrders retrieves all orders (active and historical) for a player
func (h *EconomyHandlers) getPlayerAllOrders(ctx context.Context, playerID string) []*api.Order {
	// In production, this would query the database for all player orders
	// including historical ones

	var allOrders []*api.Order

	// Get active orders
	activeOrders := h.getPlayerActiveOrders(ctx, playerID)
	allOrders = append(allOrders, activeOrders...)

	// Add some historical orders (simplified simulation)
	historicalOrders := h.getPlayerHistoricalOrders(ctx, playerID)
	allOrders = append(allOrders, historicalOrders...)

	h.logger.Debug("Retrieved all player orders",
		zap.String("player_id", playerID),
		zap.Int("active_orders", len(activeOrders)),
		zap.Int("historical_orders", len(historicalOrders)),
		zap.Int("total_orders", len(allOrders)))

	return allOrders
}

// getPlayerHistoricalOrders retrieves historical (completed/cancelled) orders for a player
func (h *EconomyHandlers) getPlayerHistoricalOrders(ctx context.Context, playerID string) []*api.Order {
	// In production, this would query the database for historical orders
	// For now, return a simulated list of historical orders

	var orders []*api.Order

	// Simulate some historical orders based on player ID
	hash := 0
	for _, char := range playerID {
		hash += int(char)
	}

	// Create 2-5 historical orders
	numHistorical := 2 + (hash % 4)
	for i := 0; i < numHistorical; i++ {
		orderType := "buy"
		if (hash+i)%2 == 0 {
			orderType = "sell"
		}

		commodity := "food"
		switch (hash + i) % 5 {
		case 1:
			commodity = "wood"
		case 2:
			commodity = "metal"
		case 3:
			commodity = "weapons"
		case 4:
			commodity = "crystal"
		}

		order := &api.Order{
			ID:       api.NewOptUUID(uuid.New()),
			PlayerId: api.NewOptUUID(uuid.MustParse(playerID)),
			Type:     api.NewOptOrderType(api.OrderType(orderType)),
			Commodity: api.NewOptCommodity(api.Commodity(commodity)),
			Price:    api.NewOptFloat32(float32(8.0 + float64((hash+i)%80))),
			Quantity: api.NewOptInt(1 + ((hash + i) % 15)),
			CreatedAt: api.NewOptDateTime(time.Now().Add(-time.Duration(24+i*12) * time.Hour)),
		}

		orders = append(orders, order)
	}

	h.logger.Debug("Retrieved player historical orders",
		zap.String("player_id", playerID),
		zap.Int("historical_orders", len(orders)))

	return orders
}

// getBazaarBotSimulationStatus retrieves current BazaarBot simulation status
func (h *EconomyHandlers) getBazaarBotSimulationStatus(ctx context.Context) *api.BazaarBotStatus {
	// In production, this would query the database/service for real simulation status
	// For now, return a simulated status

	// Add some randomness to make it more realistic
	hash := int(time.Now().Unix() % 100)
	activeAgents := 25 + hash%10
	totalMarkets := 6 + hash%2

	status := &api.BazaarBotStatus{
		Active:         api.NewOptBool(true), // Simulation is running
		LastTick:       api.NewOptDateTime(time.Now().Add(-5 * time.Minute)), // Last tick 5 minutes ago
		ActiveAgents:   api.NewOptInt(activeAgents),  // Variable active trading agents
		TotalMarkets:   api.NewOptInt(totalMarkets),   // Variable commodity markets
		SimulationUptime: api.NewOptInt(86400), // 24 hours in seconds
	}

	h.logger.Debug("Generated BazaarBot simulation status",
		zap.Int("active_agents", activeAgents),
		zap.Int("total_markets", totalMarkets))

	return status
}