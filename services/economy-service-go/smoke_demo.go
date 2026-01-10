package main

import (
	"fmt"
	"math/rand"
	"time"

	"necpgame/services/economy-service-go/internal/simulation/bazaar"
)

func main() {
	fmt.Println("=== BazaarBot Simulation Smoke Test ===")
	fmt.Println("Demonstrating price convergence with intelligent trading agents")
	fmt.Println()

	rand.Seed(time.Now().UnixNano())

	// Create a Market for Food
	market := bazaar.NewMarketLogic(bazaar.CommodityFood)

	// Create multiple agents with different personalities and beliefs
	agents := make([]*bazaar.AgentLogic, 0)

	// Create Buyers (Consumers) - 5 agents
	fmt.Println("Creating 5 buyer agents (consumers)...")
	for i := 0; i < 5; i++ {
		buyer := bazaar.NewAgentLogic(fmt.Sprintf("buyer-%d", i+1), 100.0)
		// Initial beliefs: Food is worth 5-15 (with some variation)
		buyer.SetPriceBelief(bazaar.CommodityFood, 5.0+float64(i), 15.0+float64(i))
		agents = append(agents, buyer)
		fmt.Printf("  %s: Initial wealth $%.0f, belief range [%.1f - %.1f]\n",
			buyer.State.ID, buyer.State.Wealth, buyer.State.PriceBeliefs[bazaar.CommodityFood].Min, buyer.State.PriceBeliefs[bazaar.CommodityFood].Max)
	}

	// Create Sellers (Producers) - 5 agents with inventory
	fmt.Println("\nCreating 5 seller agents (producers)...")
	for i := 0; i < 5; i++ {
		seller := bazaar.NewAgentLogic(fmt.Sprintf("seller-%d", i+1), 100.0)
		seller.State.Inventory[bazaar.CommodityFood] = 10 + i*5 // 10, 15, 20, 25, 30 units
		// Initial beliefs: Food is worth 8-12 (with some variation)
		seller.SetPriceBelief(bazaar.CommodityFood, 8.0+float64(i), 12.0+float64(i))
		agents = append(agents, seller)
		fmt.Printf("  %s: Initial wealth $%.0f, inventory %d units, belief range [%.1f - %.1f]\n",
			seller.State.ID, seller.State.Wealth, seller.State.Inventory[bazaar.CommodityFood],
			seller.State.PriceBeliefs[bazaar.CommodityFood].Min, seller.State.PriceBeliefs[bazaar.CommodityFood].Max)
	}

	fmt.Printf("\n=== Starting %d-round trading simulation ===\n", 10)
	fmt.Println("Each round: agents place orders → market clears → beliefs update")
	fmt.Println()

	// Track price convergence
	prices := make([]float64, 0)
	convergenceThreshold := 0.1 // 10% price stability

	for round := 1; round <= 10; round++ {
		fmt.Printf("--- Round %d ---\n", round)

		// Collect orders from all agents
		var orders []*bazaar.Order
		for _, agent := range agents {
			// Buyers (first 5) try to buy, sellers (last 5) try to sell
			isProducer := agent.State.ID[0] == 's' // seller-* agents
			order := agent.DecideTrade(bazaar.CommodityFood, nil, isProducer)
			if order != nil {
				orders = append(orders, order)
				fmt.Printf("  %s: %s %d units @ $%.2f\n",
					agent.State.ID, order.Type, order.Quantity, order.Price)
			}
		}

		// Add orders to market first
		for _, order := range orders {
			market.AddOrder(order)
		}

		// Clear the market
		trades := market.ClearMarket()

		// Update agents based on results
		for _, trade := range trades {
			// Find buyer and seller agents
			var buyer, seller *bazaar.AgentLogic
			for _, agent := range agents {
				if agent.State.ID == trade.BuyerID {
					buyer = agent
				}
				if agent.State.ID == trade.SellerID {
					seller = agent
				}
			}

			if buyer != nil {
				// Simple wealth and inventory update
				buyer.State.Wealth -= trade.Price * float64(trade.Quantity)
				if buyer.State.Inventory == nil {
					buyer.State.Inventory = make(map[bazaar.Commodity]int)
				}
				buyer.State.Inventory[bazaar.CommodityFood] += trade.Quantity
				fmt.Printf("  %s bought %d units @ $%.2f\n", buyer.State.ID, trade.Quantity, trade.Price)
			}

			if seller != nil {
				// Simple wealth and inventory update
				seller.State.Wealth += trade.Price * float64(trade.Quantity)
				if seller.State.Inventory == nil {
					seller.State.Inventory = make(map[bazaar.Commodity]int)
				}
				seller.State.Inventory[bazaar.CommodityFood] -= trade.Quantity
				fmt.Printf("  %s sold %d units @ $%.2f\n", seller.State.ID, trade.Quantity, trade.Price)
			}
		}

		// Record last price for convergence analysis
		if len(trades) > 0 {
			lastPrice := trades[len(trades)-1].Price
			prices = append(prices, lastPrice)
		}

		// Calculate total volume
		totalVolume := 0
		for _, trade := range trades {
			totalVolume += trade.Quantity
		}

		fmt.Printf("  Market cleared: %d trades, total volume: %d units\n",
			len(trades), totalVolume)

		if len(trades) > 0 {
			// Calculate average price
			totalValue := 0.0
			totalQty := 0
			for _, trade := range trades {
				totalValue += trade.Price * float64(trade.Quantity)
				totalQty += trade.Quantity
			}
			if totalQty > 0 {
				avgPrice := totalValue / float64(totalQty)
				fmt.Printf("  Average market price: $%.2f\n", avgPrice)
			}
		}
		fmt.Println()
	}

	// Analyze convergence
	fmt.Println("=== Price Convergence Analysis ===")
	if len(prices) >= 3 {
		// Check if prices stabilized in last few rounds
		recentPrices := prices[len(prices)-3:]
		minPrice := recentPrices[0]
		maxPrice := recentPrices[0]
		for _, p := range recentPrices {
			if p < minPrice {
				minPrice = p
			}
			if p > maxPrice {
				maxPrice = p
			}
		}

		priceRange := maxPrice - minPrice
		avgPrice := (minPrice + maxPrice) / 2
		convergenceRatio := 1 - (priceRange / avgPrice)

		fmt.Printf("Recent price range: $%.2f - $%.2f\n", minPrice, maxPrice)
		fmt.Printf("Average recent price: $%.2f\n", avgPrice)
		fmt.Printf("Convergence ratio: %.1f%% (higher = more stable)\n", convergenceRatio*100)

		if convergenceRatio >= convergenceThreshold {
			fmt.Printf("✅ SUCCESS: Price convergence achieved (%.1f%% stability)\n", convergenceRatio*100)
		} else {
			fmt.Printf("⚠️  PARTIAL: Some price convergence (%.1f%% stability)\n", convergenceRatio*100)
		}
	} else {
		fmt.Println("Not enough price data for convergence analysis")
	}

	// Show final agent states
	fmt.Println("\n=== Final Agent States ===")
	for _, agent := range agents {
		belief := agent.State.PriceBeliefs[bazaar.CommodityFood]
		fmt.Printf("%s: Wealth $%.0f, Belief [%.1f - %.1f]",
			agent.State.ID, agent.State.Wealth, belief.Min, belief.Max)
		if agent.State.Inventory[bazaar.CommodityFood] > 0 {
			fmt.Printf(", Inventory: %d units", agent.State.Inventory[bazaar.CommodityFood])
		}
		fmt.Println()
	}

	fmt.Println("\n=== BazaarBot Simulation Complete ===")
	fmt.Println("Issue: #2278 - BazaarBot Economy Logic Implementation")
}