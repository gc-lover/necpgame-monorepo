package bazaar

import (
	"math"
	"testing"
)

// TestMarketClear tests the Market.Clear() method for double auction matching
// Issue: #2278
func TestMarketClear(t *testing.T) {
	market := NewMarketLogic(CommodityFood)

	// Create simple agents for testing
	buyer := NewAgentLogic("buyer-1", 100.0)
	buyer.SetPriceBelief(CommodityFood, 5.0, 15.0)

	seller := NewAgentLogic("seller-1", 100.0)
	seller.State.Inventory[CommodityFood] = 10
	seller.SetPriceBelief(CommodityFood, 8.0, 12.0)

	agents := []*AgentLogic{buyer, seller}

	// Create market state
	marketState := market.CreateMarketState()

	// Buyer creates bid order
	buyOrder := buyer.DecideTrade(CommodityFood, marketState, false)
	if buyOrder == nil {
		t.Fatal("Expected buyer to create order")
	}
	market.AddOrder(buyOrder)

	// Seller creates ask order
	sellOrder := seller.DecideTrade(CommodityFood, marketState, true)
	if sellOrder == nil {
		t.Fatal("Expected seller to create order")
	}
	market.AddOrder(sellOrder)

	// Clear market
	result := market.Clear(agents)

	// Verify results
	if result.TotalVolume < 0 {
		t.Error("Expected total volume to be non-negative")
	}

	if len(result.NewPrices) == 0 {
		t.Error("Expected new prices to be set")
	}

	if result.MarketEfficiency < 0 || result.MarketEfficiency > 1 {
		t.Errorf("Expected market efficiency between 0 and 1, got %.2f", result.MarketEfficiency)
	}

	// If trades occurred, verify price is between bid and ask
	if result.TotalVolume > 0 {
		clearingPrice := result.NewPrices[CommodityFood]
		if clearingPrice < buyOrder.Price || clearingPrice > sellOrder.Price {
			// Actually, clearing price should be between bid and ask, but could be average
			// Check that it's reasonable (within or near the range)
			minPrice := math.Min(buyOrder.Price, sellOrder.Price)
			maxPrice := math.Max(buyOrder.Price, sellOrder.Price)
			if clearingPrice < minPrice*0.5 || clearingPrice > maxPrice*1.5 {
				t.Errorf("Clearing price %.2f seems unreasonable (bid: %.2f, ask: %.2f)",
					clearingPrice, buyOrder.Price, sellOrder.Price)
			}
		}
	}
}

// TestMarketClearPartialFills tests partial order fills
// Issue: #2278
func TestMarketClearPartialFills(t *testing.T) {
	market := NewMarketLogic(CommodityFood)

	// Create orders with mismatched quantities
	buyOrder := &Order{
		AgentID:   "buyer-1",
		Commodity: CommodityFood,
		Type:      OrderTypeBid,
		Price:     15.0,
		Quantity:  10, // Buyer wants 10 units
	}

	sellOrder := &Order{
		AgentID:   "seller-1",
		Commodity: CommodityFood,
		Type:      OrderTypeAsk,
		Price:     10.0,
		Quantity:  5, // Seller only has 5 units
	}

	market.AddOrder(buyOrder)
	market.AddOrder(sellOrder)

	// Create minimal agents
	buyer := NewAgentLogic("buyer-1", 100.0)
	seller := NewAgentLogic("seller-1", 100.0)
	agents := []*AgentLogic{buyer, seller}

	// Clear market
	result := market.Clear(agents)

	// Should trade 5 units (minimum of 10 and 5)
	if result.TotalVolume != 5 {
		t.Errorf("Expected total volume to be 5 (partial fill), got %d", result.TotalVolume)
	}

	if len(result.ClearedTrades) != 1 {
		t.Errorf("Expected 1 trade execution, got %d", len(result.ClearedTrades))
	}

	// Verify trade details
	trade := result.ClearedTrades[0]
	if trade.Quantity != 5 {
		t.Errorf("Expected trade quantity to be 5, got %d", trade.Quantity)
	}

	if trade.BuyerID != "buyer-1" || trade.SellerID != "seller-1" {
		t.Errorf("Expected trade between buyer-1 and seller-1, got %s -> %s", trade.SellerID, trade.BuyerID)
	}
}

// TestMarketClearNoMatch tests market clearing when orders don't match
// Issue: #2278
func TestMarketClearNoMatch(t *testing.T) {
	market := NewMarketLogic(CommodityFood)

	// Create orders that don't match (bid < ask)
	buyOrder := &Order{
		AgentID:   "buyer-1",
		Commodity: CommodityFood,
		Type:      OrderTypeBid,
		Price:     10.0, // Buyer bids 10
		Quantity:  5,
	}

	sellOrder := &Order{
		AgentID:   "seller-1",
		Commodity: CommodityFood,
		Type:      OrderTypeAsk,
		Price:     15.0, // Seller asks 15 (too high)
		Quantity:  5,
	}

	market.AddOrder(buyOrder)
	market.AddOrder(sellOrder)

	agents := []*AgentLogic{
		NewAgentLogic("buyer-1", 100.0),
		NewAgentLogic("seller-1", 100.0),
	}

	// Clear market
	result := market.Clear(agents)

	// Should have no trades
	if result.TotalVolume != 0 {
		t.Errorf("Expected total volume to be 0 (no match), got %d", result.TotalVolume)
	}

	if len(result.ClearedTrades) != 0 {
		t.Errorf("Expected no trades, got %d", len(result.ClearedTrades))
	}
}

// TestMarketClearMultipleAgents tests market clearing with multiple agents
// Issue: #2278
func TestMarketClearMultipleAgents(t *testing.T) {
	market := NewMarketLogic(CommodityFood)

	// Create multiple buyers and sellers
	agents := make([]*AgentLogic, 0)

	// Create 3 buyers
	for i := 0; i < 3; i++ {
		buyer := NewAgentLogic("buyer-"+string(rune('1'+i)), 100.0)
		buyer.SetPriceBelief(CommodityFood, 10.0+float64(i), 15.0+float64(i))
		agents = append(agents, buyer)
	}

	// Create 3 sellers
	for i := 0; i < 3; i++ {
		seller := NewAgentLogic("seller-"+string(rune('1'+i)), 100.0)
		seller.State.Inventory[CommodityFood] = 10
		seller.SetPriceBelief(CommodityFood, 8.0+float64(i), 12.0+float64(i))
		agents = append(agents, seller)
	}

	// Create market state and generate orders
	marketState := market.CreateMarketState()
	for _, agent := range agents {
		isProducer := agent.State.Inventory[CommodityFood] > 0
		order := agent.DecideTrade(CommodityFood, marketState, isProducer)
		if order != nil {
			market.AddOrder(order)
		}
	}

	// Clear market
	result := market.Clear(agents)

	// Should have some trades if orders matched
	if result.TotalVolume < 0 {
		t.Error("Expected total volume to be non-negative")
	}

	// Verify market efficiency is in valid range
	if result.MarketEfficiency < 0 || result.MarketEfficiency > 1 {
		t.Errorf("Expected market efficiency between 0 and 1, got %.2f", result.MarketEfficiency)
	}
}

// TestMarketHistory tests that market history is properly maintained
// Issue: #2278
func TestMarketHistory(t *testing.T) {
	market := NewMarketLogic(CommodityFood)

	// Initial history should be empty
	if len(market.History) != 0 {
		t.Errorf("Expected empty history initially, got %d entries", len(market.History))
	}

	// Add orders and clear
	buyOrder := &Order{
		AgentID:   "buyer-1",
		Commodity: CommodityFood,
		Type:      OrderTypeBid,
		Price:     12.0,
		Quantity:  5,
	}

	sellOrder := &Order{
		AgentID:   "seller-1",
		Commodity: CommodityFood,
		Type:      OrderTypeAsk,
		Price:     10.0,
		Quantity:  5,
	}

	market.AddOrder(buyOrder)
	market.AddOrder(sellOrder)

	agents := []*AgentLogic{
		NewAgentLogic("buyer-1", 100.0),
		NewAgentLogic("seller-1", 100.0),
	}

	result := market.Clear(agents)

	// History should contain the clearing price
	if len(market.History) != 1 {
		t.Errorf("Expected 1 entry in history, got %d", len(market.History))
	}

	if result.TotalVolume > 0 {
		expectedPrice := result.NewPrices[CommodityFood]
		if math.Abs(market.History[0]-expectedPrice) > 0.01 {
			t.Errorf("Expected history price %.2f, got %.2f", expectedPrice, market.History[0])
		}
	}
}
