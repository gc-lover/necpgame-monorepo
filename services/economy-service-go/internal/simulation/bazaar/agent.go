package bazaar

import (
	"math/rand"
)

// AgentLogic encapsulates the decision making capability of an economic actor
type AgentLogic struct {
	ID           string
	Money        float64
	Inventory    map[Commodity]int
	PriceBeliefs map[Commodity]*PriceBelief
}

// NewAgentLogic creates a new agent with initial state
func NewAgentLogic(id string, initialMoney float64) *AgentLogic {
	return &AgentLogic{
		ID:           id,
		Money:        initialMoney,
		Inventory:    make(map[Commodity]int),
		PriceBeliefs: make(map[Commodity]*PriceBelief),
	}
}

// SetPriceBelief initializes the belief for a commodity
func (a *AgentLogic) SetPriceBelief(c Commodity, min, max float64) {
	a.PriceBeliefs[c] = &PriceBelief{Min: min, Max: max}
}

// DecideTrade creates an order based on logic
// This is a simplified port of BazaarBot logic
func (a *AgentLogic) DecideTrade(c Commodity, isProducer bool) *Order {
	belief, exists := a.PriceBeliefs[c]
	if !exists {
		return nil
	}

	// Determine price based on belief
	// Random point between min and max
	price := belief.Min + rand.Float64()*(belief.Max-belief.Min)

	if isProducer {
		// Producer wants to sell
		qty := a.Inventory[c]
		if qty > 0 {
			return &Order{
				AgentID:   a.ID,
				Commodity: c,
				Type:      OrderTypeAsk,
				Price:     price,
				Quantity:  qty, // Sell all excess? logic can be refined
			}
		}
	} else {
		// Consumer wants to buy
		// Logic: Buy if inventory is low or for production
		// Simplified: Always try to buy 1 if have money
		if a.Money >= price {
			return &Order{
				AgentID:   a.ID,
				Commodity: c,
				Type:      OrderTypeBid,
				Price:     price,
				Quantity:  1,
			}
		}
	}

	return nil
}

// UpdateBelief updates price belief based on market outcome
func (a *AgentLogic) UpdateBelief(c Commodity, success bool, clearingPrice float64) {
	belief, exists := a.PriceBeliefs[c]
	if !exists {
		return
	}

	if success {
		// Successfully traded
		// If sold, maybe we could have sold higher?
		// If bought, maybe we could have bought lower?
		
		// Simple logic: contract range towards clearing price
		// Or expand range if we are confident?
		
		// BazaarBot Logic (approx):
		// If successful trade, we might be too generous/cheap.
		// Detailed logic needed here.
		
		// Placeholder: Shift belief towards clearing price
		belief.Min = (belief.Min + clearingPrice) / 2
		belief.Max = (belief.Max + clearingPrice) / 2 + 10 // keep some spread
	} else {
		// Failed to trade
		// If Seller: Price too high -> lower belief
		// If Buyer: Price too low -> raise belief
		// This requires knowing intended action. passing 'isSeller' might be needed.
		
		// Placeholder logic for iteration 1
		belief.Min *= 0.95
		belief.Max *= 0.95
	}
}
