package main

import (
	"fmt"
	"math/rand"
	"time"

	"necpgame/services/economy-service-go/internal/simulation/bazaar"
)

func main() {
	fmt.Println("Economy Service Starting...")

	// Simulation Test
	simTest()
}

func simTest() {
	rand.Seed(time.Now().UnixNano())

	// Create a Market for Food
	market := bazaar.NewMarketLogic(bazaar.CommodityFood)

	// Create Buyer (Consumer)
	buyer := bazaar.NewAgentLogic("buyer-1", 100.0)
	// Believes Food is worth 5-15
	buyer.SetPriceBelief(bazaar.CommodityFood, 5, 15)

	// Create Seller (Producer) with inventory
	seller := bazaar.NewAgentLogic("seller-1", 100.0)
	seller.Inventory[bazaar.CommodityFood] = 10
	// Believes Food is worth 8-12
	seller.SetPriceBelief(bazaar.CommodityFood, 8, 12)

	// Agents decide
	buyOrder := buyer.DecideTrade(bazaar.CommodityFood, false)
	sellOrder := seller.DecideTrade(bazaar.CommodityFood, true)

	if buyOrder != nil {
		fmt.Printf("Buyer bid: %.2f (Qty: %d)\n", buyOrder.Price, buyOrder.Quantity)
		market.AddOrder(buyOrder)
	}

	if sellOrder != nil {
		fmt.Printf("Seller ask: %.2f (Qty: %d)\n", sellOrder.Price, sellOrder.Quantity)
		market.AddOrder(sellOrder)
	}

	// Clearing
	price, volume := market.Clear()
	fmt.Printf("Market Cleared: Price %.2f, Volume %d\n", price, volume)

	// Update Beliefs (mockup)
	successBuy := volume > 0 && buyOrder != nil && buyOrder.Price >= price
	if buyOrder != nil {
		buyer.UpdateBelief(bazaar.CommodityFood, successBuy, price)
		fmt.Printf("Buyer updated belief: %.2f - %.2f\n", 
			buyer.PriceBeliefs[bazaar.CommodityFood].Min,
			buyer.PriceBeliefs[bazaar.CommodityFood].Max)
	}
}
