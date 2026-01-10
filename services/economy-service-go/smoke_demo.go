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
			order := agent.DecideTrade(bazaar.CommodityFood, market.GetState(), isProducer)
			if order != nil {
				orders = append(orders, order)
				fmt.Printf("  %s: %s %d units @ $%.2f\n",
					agent.State.ID, order.Type, order.Quantity, order.Price)
			}
		}

		// Clear the market
		result := market.ClearMarket(orders)

		// Update agents based on results
		for _, trade := range result.ClearedTrades {
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
				// Buyer perspective: bought at price, expected to pay less
				expectedPrice := (buyer.State.PriceBeliefs[bazaar.CommodityFood].Min +
					buyer.State.PriceBeliefs[bazaar.CommodityFood].Max) / 2
				wasExpected := trade.Price <= expectedPrice*1.2 // Within 20% of expectation
				buyer.UpdateBelief(bazaar.CommodityFood, false, trade.Price, wasExpected, trade.Quantity, trade.TotalValue)
			}

			if seller != nil {
				// Seller perspective: sold at price, expected to sell higher
				expectedPrice := (seller.State.PriceBeliefs[bazaar.CommodityFood].Min +
					seller.State.PriceBeliefs[bazaar.CommodityFood].Max) / 2
				wasExpected := trade.Price >= expectedPrice*0.8 // Within 20% of expectation
				seller.UpdateBelief(bazaar.CommodityFood, true, trade.Price, wasExpected, trade.Quantity, trade.TotalValue)
			}
		}

		// Update market state
		market.UpdateState(result)

		// Record price
		if result.NewPrices[bazaar.CommodityFood] > 0 {
			prices = append(prices, result.NewPrices[bazaar.CommodityFood])
		}

		fmt.Printf("  Market cleared: %d trades, total volume: %d units, efficiency: %.1f%%\n",
			len(result.ClearedTrades), result.TotalVolume, result.MarketEfficiency*100)

		if len(result.ClearedTrades) > 0 {
			fmt.Printf("  New market price: $%.2f\n", result.NewPrices[bazaar.CommodityFood])
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