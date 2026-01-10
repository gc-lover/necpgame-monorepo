package bazaar

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
	AgentID   string
	Commodity Commodity
	Type      OrderType
	Price     float64
	Quantity  int
}
