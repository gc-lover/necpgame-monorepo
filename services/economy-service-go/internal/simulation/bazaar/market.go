package bazaar

import (
	"sort"
)

// MarketLogic handles the order book and clearing for a single commodity
type MarketLogic struct {
	Commodity Commodity
	Bids      []*Order // Buy orders
	Asks      []*Order // Sell orders
	History   []float64
}

// NewMarketLogic creates a new market for a commodity
func NewMarketLogic(c Commodity) *MarketLogic {
	return &MarketLogic{
		Commodity: c,
		Bids:      make([]*Order, 0),
		Asks:      make([]*Order, 0),
		History:   make([]float64, 0),
	}
}

// AddOrder places an order in the book
func (m *MarketLogic) AddOrder(o *Order) {
	if o.Type == OrderTypeBid {
		m.Bids = append(m.Bids, o)
	} else {
		m.Asks = append(m.Asks, o)
	}
}

// Clear resolves the market orders
// Returns clearing price and volume
func (m *MarketLogic) Clear() (float64, int) {
	// Sort Bids: Highest price first (willing to pay most)
	sort.Slice(m.Bids, func(i, j int) bool {
		return m.Bids[i].Price > m.Bids[j].Price
	})

	// Sort Asks: Lowest price first (willing to sell for least)
	sort.Slice(m.Asks, func(i, j int) bool {
		return m.Asks[i].Price < m.Asks[j].Price
	})

	// Find equilibrium
	volume := 0
	clearingPrice := 0.0

	// Walk the curves
	i, j := 0, 0
	for i < len(m.Bids) && j < len(m.Asks) {
		bid := m.Bids[i]
		ask := m.Asks[j]

		if bid.Price >= ask.Price {
			// Match!
			volume++
			// Determine price: average of bid/ask or last trade?
			// Adapting standard double auction rule
			clearingPrice = (bid.Price + ask.Price) / 2
			
			// Decrement quantities (simplified for unit trade)
			// In real code, handle partial fills
			
			i++
			j++
		} else {
			// No more matches possible
			break
		}
	}

	m.History = append(m.History, clearingPrice)
	
	// Reset books for next round
	m.Bids = make([]*Order, 0)
	m.Asks = make([]*Order, 0)

	return clearingPrice, volume
}
