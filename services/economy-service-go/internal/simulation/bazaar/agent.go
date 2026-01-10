package bazaar

import (
	"math"
	"math/rand"
	"time"
)

// AgentLogic encapsulates the decision making capability of an economic actor
// Implements BazaarBot-style intelligent trading agent (#2278)
type AgentLogic struct {
	State       *AgentState
	Personality *AgentPersonality
}

// NewAgentLogic creates a new agent with initial state and personality
func NewAgentLogic(id string, initialMoney float64) *AgentLogic {
	// Generate random personality traits
	personality := &AgentPersonality{
		RiskTolerance:    rand.Float64(), // 0.0 = conservative, 1.0 = aggressive
		ImpatienceFactor: rand.Float64(), // How quickly agent adjusts beliefs
		SocialInfluence:  rand.Float64(), // Influence from market trends
		LearningRate:     0.1 + rand.Float64()*0.4, // Learning speed
	}

	state := &AgentState{
		ID:           id,
		Wealth:       initialMoney,
		PriceBeliefs: make(map[Commodity]*PriceBelief),
		Inventory:    make(map[Commodity]int),
		Personality:  personality,
		LastTrades:   make([]TradeRecord, 0),
	}

	return &AgentLogic{
		State:       state,
		Personality: personality,
	}
}

// SetPriceBelief initializes the belief for a commodity
func (a *AgentLogic) SetPriceBelief(c Commodity, min, max float64) {
	a.State.PriceBeliefs[c] = &PriceBelief{Min: min, Max: max}
}

// DecideTrade creates an order based on BazaarBot logic
// Analyzes market conditions, personality, and inventory to make trading decisions
// Issue: #2278
func (a *AgentLogic) DecideTrade(c Commodity, marketState *MarketState, isProducer bool) *Order {
	belief, exists := a.State.PriceBeliefs[c]
	if !exists {
		return nil
	}

	// Adjust price belief based on market conditions and personality
	adjustedBelief := a.adjustBeliefForMarket(c, belief, marketState)

	// Calculate optimal price based on personality and market conditions
	price := a.calculateOptimalPrice(c, adjustedBelief, marketState, isProducer)

	// Decide quantity and whether to trade
	quantity := a.calculateQuantity(c, price, isProducer)

	if quantity <= 0 {
		return nil
	}

	orderType := OrderTypeBid
	if isProducer {
		orderType = OrderTypeAsk
	}

	return &Order{
		AgentID:   a.State.ID,
		Commodity: c,
		Type:      orderType,
		Price:     price,
		Quantity:  quantity,
	}
}

// adjustBeliefForMarket adjusts price beliefs based on market conditions and personality
func (a *AgentLogic) adjustBeliefForMarket(c Commodity, belief *PriceBelief, marketState *MarketState) *PriceBelief {
	lastPrice := marketState.LastPrices[c]
	trend := 0.0
	if t, exists := marketState.Trend[c]; exists {
		trend = t
	}

	// Social influence: agents adjust beliefs based on market trends
	socialAdjustment := trend * a.Personality.SocialInfluence

	// Impatience factor: how quickly agent reacts to price changes
	impatienceMultiplier := 1.0 + (a.Personality.ImpatienceFactor * math.Abs(trend))

	adjusted := &PriceBelief{
		Min: belief.Min * (1 + socialAdjustment/impatienceMultiplier),
		Max: belief.Max * (1 + socialAdjustment/impatienceMultiplier),
	}

	// Ensure beliefs stay reasonable
	if lastPrice > 0 {
		// Don't let beliefs drift too far from recent market prices
		maxDrift := 0.5 // 50% max drift from last price
		adjusted.Min = math.Max(adjusted.Min, lastPrice*(1-maxDrift))
		adjusted.Max = math.Min(adjusted.Max, lastPrice*(1+maxDrift))
	}

	return adjusted
}

// calculateOptimalPrice determines the best price to offer based on personality and strategy
// Issue: #2278
func (a *AgentLogic) calculateOptimalPrice(c Commodity, belief *PriceBelief, marketState *MarketState, isProducer bool) float64 {
	riskMultiplier := 0.5 + a.Personality.RiskTolerance*0.5 // 0.5 to 1.0

	// For initial market formation, use overlapping price ranges around midpoint
	midpoint := (belief.Min + belief.Max) / 2.0

	if isProducer {
		// Sellers: price from midpoint-0.5 to midpoint+0.5
		return midpoint - 0.5 + riskMultiplier
	} else {
		// Buyers: price from midpoint-0.5 to midpoint+0.5
		return midpoint - 0.5 + riskMultiplier
	}
}

// calculateQuantity determines how much to trade
func (a *AgentLogic) calculateQuantity(c Commodity, price float64, isProducer bool) int {
	if isProducer {
		// Selling: sell portion of inventory
		inventory := a.State.Inventory[c]
		if inventory <= 0 {
			return 0
		}

		// Risk-averse agents sell less, risk-seeking sell more
		sellRatio := 0.3 + a.Personality.RiskTolerance*0.4 // 30% to 70%

		quantity := int(float64(inventory) * sellRatio)
		return int(math.Max(1, float64(quantity)))
	} else {
		// Buying: calculate affordable quantity
		maxAffordable := int(a.State.Wealth / price)
		if maxAffordable <= 0 {
			return 0
		}

		// Risk-averse buy less, risk-seeking buy more
		buyRatio := 0.2 + a.Personality.RiskTolerance*0.3 // 20% to 50% of affordable

		quantity := int(float64(maxAffordable) * buyRatio)
		return int(math.Max(1, float64(quantity)))
	}
}

// UpdateBelief updates price belief based on market outcome
// Implements BazaarBot-style adaptive price belief adjustment
// Issue: #2278
func (a *AgentLogic) UpdateBelief(c Commodity, success bool, clearingPrice float64, wasSeller bool, quantity int, actualProfitLoss float64) {
	belief, exists := a.State.PriceBeliefs[c]
	if !exists || math.IsNaN(clearingPrice) || clearingPrice <= 0 {
		return
	}

	// Record the trade
	trade := TradeRecord{
		Timestamp:   time.Now().Unix(),
		Commodity:   c,
		Type:        OrderTypeBid,
		Price:       clearingPrice,
		Quantity:    quantity,
		ProfitLoss:  actualProfitLoss,
		WasExpected: success,
	}

	if wasSeller {
		trade.Type = OrderTypeAsk
	}

	// Keep only last 10 trades for memory efficiency
	a.State.LastTrades = append(a.State.LastTrades, trade)
	if len(a.State.LastTrades) > 10 {
		a.State.LastTrades = a.State.LastTrades[1:]
	}

	// Adaptive belief adjustment based on personality and market outcome
	learningRate := math.Max(0.01, math.Min(0.5, a.Personality.LearningRate)) // Clamp learning rate

	if success {
		// Successful trade: narrow belief range towards clearing price
		if wasSeller {
			// Seller successful: clearing price was acceptable, adjust range
			rangeSize := math.Max(0.1, belief.Max-belief.Min) // Ensure positive range
			newMin := belief.Min*(1-learningRate) + clearingPrice*learningRate
			newMax := belief.Max*(1-learningRate) + (clearingPrice+rangeSize*0.1)*learningRate

			belief.Min = math.Max(0.1, newMin) // Ensure positive
			belief.Max = math.Max(belief.Min+0.1, newMax)
		} else {
			// Buyer successful: clearing price was acceptable
			rangeSize := math.Max(0.1, belief.Max-belief.Min) // Ensure positive range
			newMin := belief.Min*(1-learningRate) + math.Max(0.1, clearingPrice-rangeSize*0.1)*learningRate
			newMax := belief.Max*(1-learningRate) + clearingPrice*learningRate

			belief.Min = math.Max(0.1, newMin) // Ensure positive
			belief.Max = math.Max(belief.Min+0.1, newMax)
		}
	} else if !success {
		// Failed trade: expand belief range to explore more
		rangeSize := math.Max(0.1, belief.Max-belief.Min)
		expansionFactor := math.Min(0.5, 0.2*(1+a.Personality.RiskTolerance)) // Risk-takers expand more, but cap it

		if wasSeller {
			// Seller failed: price too high, lower max belief more aggressively
			belief.Max = math.Max(belief.Min+0.1, belief.Max*(1-expansionFactor*1.5))
		} else {
			// Buyer failed: price too low, raise min belief more aggressively
			belief.Min = math.Max(0.1, belief.Min*(1+expansionFactor*1.5))
		}

		// Expand range more significantly
		belief.Min = math.Max(0.1, belief.Min-rangeSize*expansionFactor)
		belief.Max = math.Max(belief.Min+0.1, belief.Max+rangeSize*expansionFactor)
	}

	// Ensure beliefs stay within reasonable bounds and are valid numbers
	const minPrice = 0.1
	const maxPrice = 10000.0

	if math.IsNaN(belief.Min) || math.IsInf(belief.Min, 0) {
		belief.Min = minPrice
	}
	if math.IsNaN(belief.Max) || math.IsInf(belief.Max, 0) {
		belief.Max = maxPrice
	}

	belief.Min = math.Max(minPrice, math.Min(maxPrice, belief.Min))
	belief.Max = math.Max(belief.Min+0.1, math.Min(maxPrice, belief.Max))

	// Ensure minimum spread
	const minSpread = 0.5
	if belief.Max-belief.Min < minSpread {
		midpoint := (belief.Min + belief.Max) / 2
		belief.Min = math.Max(minPrice, midpoint-minSpread/2)
		belief.Max = math.Min(maxPrice, midpoint+minSpread/2)
	}

	// Update wealth (ensure it's not NaN)
	if !math.IsNaN(actualProfitLoss) && !math.IsInf(actualProfitLoss, 0) {
		a.State.Wealth += actualProfitLoss
		// Ensure wealth doesn't go negative or become invalid
		if math.IsNaN(a.State.Wealth) || a.State.Wealth < 0 {
			a.State.Wealth = 0
		}
	}
}
