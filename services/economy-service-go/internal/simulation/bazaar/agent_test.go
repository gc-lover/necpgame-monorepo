package bazaar

import (
	"math"
	"testing"
)

// TestPriceBeliefsUpdate tests the UpdateBelief method for adaptive price belief adjustment
// Issue: #2278
func TestPriceBeliefsUpdate(t *testing.T) {
	// Create agent with initial belief
	agent := NewAgentLogic("test-agent", 100.0)
	agent.SetPriceBelief(CommodityFood, 10.0, 20.0)

	initialBelief := agent.State.PriceBeliefs[CommodityFood]
	if initialBelief == nil {
		t.Fatal("Expected price belief to be set")
	}

	initialMin := initialBelief.Min
	initialMax := initialBelief.Max

	// Test successful trade as seller
	clearingPrice := 15.0
	agent.UpdateBelief(CommodityFood, true, clearingPrice, true, 5, 50.0)

	updatedBelief := agent.State.PriceBeliefs[CommodityFood]
	if updatedBelief.Min == initialMin && updatedBelief.Max == initialMax {
		t.Error("Expected belief to be updated after successful trade")
	}

	// Verify belief range is still valid (min < max)
	if updatedBelief.Min >= updatedBelief.Max {
		t.Errorf("Invalid belief range: min (%.2f) >= max (%.2f)", updatedBelief.Min, updatedBelief.Max)
	}

	// Verify wealth was updated
	if agent.State.Wealth != 150.0 {
		t.Errorf("Expected wealth to be 150.0, got %.2f", agent.State.Wealth)
	}

	// Test failed trade as buyer
	agent.SetPriceBelief(CommodityFood, 10.0, 20.0)
	agent.UpdateBelief(CommodityFood, false, clearingPrice, false, 0, 0.0)

	failedBelief := agent.State.PriceBeliefs[CommodityFood]
	// Failed trade should expand belief range
	if math.Abs(failedBelief.Max-failedBelief.Min) <= math.Abs(initialMax-initialMin) {
		t.Error("Expected belief range to expand after failed trade")
	}
}

// TestDecideTrade tests the DecideTrade method for order generation
// Issue: #2278
func TestDecideTrade(t *testing.T) {
	agent := NewAgentLogic("test-agent", 100.0)
	agent.SetPriceBelief(CommodityFood, 10.0, 20.0)

	// Create market state
	marketState := &MarketState{
		LastPrices: map[Commodity]float64{CommodityFood: 15.0},
		Volume:     map[Commodity]int{CommodityFood: 100},
		Volatility: map[Commodity]float64{CommodityFood: 1.0},
		Trend:      map[Commodity]float64{CommodityFood: 0.1},
		ActiveOrders: []*Order{},
	}

	// Test buyer decision
	agent.State.Wealth = 100.0
	buyOrder := agent.DecideTrade(CommodityFood, marketState, false)

	if buyOrder == nil {
		t.Fatal("Expected buyer to generate an order")
	}

	if buyOrder.Type != OrderTypeBid {
		t.Errorf("Expected order type BID, got %s", buyOrder.Type)
	}

	if buyOrder.Price < agent.State.PriceBeliefs[CommodityFood].Min ||
		buyOrder.Price > agent.State.PriceBeliefs[CommodityFood].Max {
		t.Errorf("Order price %.2f outside belief range [%.2f - %.2f]",
			buyOrder.Price, agent.State.PriceBeliefs[CommodityFood].Min, agent.State.PriceBeliefs[CommodityFood].Max)
	}

	// Test seller decision
	agent.State.Inventory[CommodityFood] = 10
	sellOrder := agent.DecideTrade(CommodityFood, marketState, true)

	if sellOrder == nil {
		t.Fatal("Expected seller to generate an order")
	}

	if sellOrder.Type != OrderTypeAsk {
		t.Errorf("Expected order type ASK, got %s", sellOrder.Type)
	}

	if sellOrder.Quantity <= 0 {
		t.Error("Expected seller order quantity to be positive")
	}
}

// TestPersonalityInfluence tests that personality traits affect trading behavior
// Issue: #2278
func TestPersonalityInfluence(t *testing.T) {
	// Create risk-seeking agent
	riskSeeker := NewAgentLogic("risk-seeker", 100.0)
	riskSeeker.Personality.RiskTolerance = 0.9
	riskSeeker.SetPriceBelief(CommodityFood, 10.0, 20.0)

	// Create risk-averse agent
	riskAverse := NewAgentLogic("risk-averse", 100.0)
	riskAverse.Personality.RiskTolerance = 0.1
	riskAverse.SetPriceBelief(CommodityFood, 10.0, 20.0)

	marketState := &MarketState{
		LastPrices: map[Commodity]float64{CommodityFood: 15.0},
		Trend:      map[Commodity]float64{CommodityFood: 0.0},
		ActiveOrders: []*Order{},
	}

	// Risk-seeker should be more aggressive (higher price as seller)
	riskSeeker.State.Inventory[CommodityFood] = 10
	riskSeekerOrder := riskSeeker.DecideTrade(CommodityFood, marketState, true)

	riskAverse.State.Inventory[CommodityFood] = 10
	riskAverseOrder := riskAverse.DecideTrade(CommodityFood, marketState, true)

	if riskSeekerOrder.Price < riskAverseOrder.Price {
		t.Error("Expected risk-seeking seller to ask higher price than risk-averse seller")
	}
}
