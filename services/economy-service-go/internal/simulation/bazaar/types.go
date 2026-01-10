package bazaar

import "time"

// Commodity represents a tradable good in the simulation
type Commodity string

const (
	CommodityFood    Commodity = "Food"
	CommodityWood    Commodity = "Wood"
	CommodityMetal   Commodity = "Metal"
	CommodityWeapon  Commodity = "Weapon"
	CommodityCrystal Commodity = "Crystal" // NECPGAME specific
)

// PriceBelief represents an agent's belief about the price range of a commodity
type PriceBelief struct {
	Min float64
	Max float64
}

// OrderType represents whether an order is a bid (buy) or ask (sell)
type OrderType string

const (
	OrderTypeBid OrderType = "BID"
	OrderTypeAsk OrderType = "ASK"
)

// Order represents a market order
type Order struct {
	ID        string    // Order ID
	AgentID   string    // Agent who placed the order
	PlayerID  string    // Player who placed the order (for API)
	Commodity Commodity
	Type      OrderType
	Price     float64
	Quantity  int
	CreatedAt time.Time // When order was placed
}

// AgentState represents the internal state of a trading agent
type AgentState struct {
	ID           string
	Wealth       float64
	PriceBeliefs map[Commodity]*PriceBelief
	Inventory    map[Commodity]int
	Personality  *AgentPersonality
	LastTrades   []TradeRecord
}

// AgentPersonality defines behavioral traits of an agent
type AgentPersonality struct {
	RiskTolerance    float64 // 0.0 = risk-averse, 1.0 = risk-seeking
	ImpatienceFactor float64 // How quickly agent adjusts prices
	SocialInfluence  float64 // How much agent is influenced by market trends
	LearningRate     float64 // How quickly agent learns from trades
}

// TradeRecord represents a completed trade
type TradeRecord struct {
	Timestamp   int64
	Commodity   Commodity
	Type        OrderType
	Price       float64
	Quantity    int
	ProfitLoss  float64
	WasExpected bool // Whether the trade met expectations
}

// MarketState represents the current state of the market
type MarketState struct {
	LastPrices    map[Commodity]float64
	Volume        map[Commodity]int
	Volatility    map[Commodity]float64
	Trend         map[Commodity]float64 // Price momentum
	ActiveOrders  []*Order
	CompletedTrades []TradeRecord
}

// MarketResult represents the outcome of market clearing
// Trade represents a completed trade between buyer and seller
type Trade struct {
	ID         string    // Unique trade ID
	Commodity  Commodity // Commodity being traded
	BuyerID    string    // ID of buyer
	SellerID   string    // ID of seller
	Price      float64   // Execution price
	Quantity   int       // Quantity traded
	ExecutedAt time.Time // When trade was executed
}

// Issue: #2278
type MarketResult struct {
	ClearedTrades    []TradeResult   // Individual trade executions
	NewPrices        map[Commodity]float64
	TotalVolume      int
	MarketEfficiency float64 // How well supply met demand (0.0 to 1.0)
}