package handlers

import (
	"context"
	"fmt"

	"go.uber.org/zap"

	"necpgame/services/economy-service-go/internal/simulation/bazaar"
	api "necpgame/services/economy-service-go/pkg/services/economy-service-go/pkg/api"
)

// EconomyHandlers implements the generated Handler interface
type EconomyHandlers struct {
	logger       *zap.Logger
	bazaarAgents []*bazaar.AgentLogic
	markets      map[bazaar.Commodity]*bazaar.MarketLogic
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
		logger:       logger,
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
// TODO: Fix OpenAPI generation issue - operation not found in bundled spec
func (h *EconomyHandlers) GetOrderBook(ctx context.Context, params interface{}) (interface{}, error) {
	h.logger.Info("GetOrderBook called - placeholder implementation")
	return nil, fmt.Errorf("not implemented - OpenAPI generation issue")
}

// PlaceOrder implements placeOrder operation.
func (h *EconomyHandlers) PlaceOrder(ctx context.Context, req *api.PlaceOrderRequest, params api.PlaceOrderParams) (*api.OrderResponse, error) {
	// Get user ID from context (set by SecurityHandler)
	userIDVal := ctx.Value("user_id")
	if userIDVal == nil {
		return nil, fmt.Errorf("user not authenticated")
	}
	userID, ok := userIDVal.(string)
	if !ok {
		return nil, fmt.Errorf("invalid user ID format")
	}

	h.logger.Info("PlaceOrder called",
		zap.String("commodity", string(params.Commodity)),
		zap.String("order_type", string(req.Type)),
		zap.Float64("price", req.Price),
		zap.Int("quantity", req.Quantity),
		zap.String("user_id", userID))

	// Get market for commodity
	market, exists := h.markets[bazaar.Commodity(params.Commodity)]
	if !exists {
		return nil, fmt.Errorf("market not found for commodity: %s", params.Commodity)
	}

	// Create order
	order := &bazaar.Order{
		ID:       fmt.Sprintf("order-%d", time.Now().UnixNano()),
		Type:     bazaar.OrderType(req.Type),
		Price:    req.Price,
		Quantity: req.Quantity,
		PlayerID: userID,
		CreatedAt: time.Now(),
	}

	// Add order to market
	err := market.AddOrder(order)
	if err != nil {
		h.logger.Error("Failed to add order to market", zap.Error(err))
		return nil, fmt.Errorf("failed to place order: %v", err)
	}

	// Try to clear market (execute trades)
	trades := market.ClearMarket()

	response := &api.OrderResponse{
		OrderID: order.ID,
		Status:  "placed",
		Trades:  convertTrades(trades),
	}

	h.logger.Info("Order placed successfully",
		zap.String("order_id", order.ID),
		zap.Int("trades_executed", len(trades)))

	return response, nil
}

// convertTrades converts bazaar trades to API format
func convertTrades(trades []*bazaar.Trade) []api.Trade {
	apiTrades := make([]api.Trade, len(trades))
	for i, trade := range trades {
		apiTrades[i] = api.Trade{
			ID:       trade.ID,
			BuyerID:  trade.BuyerID,
			SellerID: trade.SellerID,
			Price:    trade.Price,
			Quantity: trade.Quantity,
			ExecutedAt: trade.ExecutedAt,
		}
	}
	return apiTrades
}

// GetMarketPrice implements getMarketPrice operation.
func (h *EconomyHandlers) GetMarketPrice(ctx context.Context, params api.GetMarketPriceParams) (*api.MarketPrice, error) {
	return &api.MarketPrice{}, nil
}

// GetPlayerPortfolio implements getPlayerPortfolio operation.
func (h *EconomyHandlers) GetPlayerPortfolio(ctx context.Context, params api.GetPlayerPortfolioParams) (*api.PlayerPortfolio, error) {
	return &api.PlayerPortfolio{}, nil
}

// GetPlayerOrders implements getPlayerOrders operation.
func (h *EconomyHandlers) GetPlayerOrders(ctx context.Context, params api.GetPlayerOrdersParams) (api.PlayerOrders, error) {
	return nil, nil
}

// GetBazaarBotStatus implements getBazaarBotStatus operation.
func (h *EconomyHandlers) GetBazaarBotStatus(ctx context.Context) (*api.BazaarBotStatus, error) {
	return &api.BazaarBotStatus{}, nil
}

// GetBazaarBotAgents implements getBazaarBotAgents operation.
func (h *EconomyHandlers) GetBazaarBotAgents(ctx context.Context) (*api.BazaarBotAgents, error) {
	return &api.BazaarBotAgents{}, nil
}

// GetMarketHistory implements getMarketHistory operation.
func (h *EconomyHandlers) GetMarketHistory(ctx context.Context, params api.GetMarketHistoryParams) (*api.MarketHistory, error) {
	return &api.MarketHistory{}, nil
}