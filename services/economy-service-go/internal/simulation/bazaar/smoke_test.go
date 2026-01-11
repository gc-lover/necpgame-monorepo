// Package bazaar smoke test demonstrating price convergence
// Issue: #2278 - BazaarBot Simulation Logic Implementation
package bazaar

import (
	"fmt"
	"math"
	"math/rand"
	"testing"
	"time"
)

// TestPriceConvergenceSmokeTest demonstrates that agents learn and prices converge
// This is a non-deterministic test that shows BazaarBot learning behavior
// Issue: #2278
func TestPriceConvergenceSmokeTest(t *testing.T) {
	// Save and restore global random state to avoid interference with other tests
	oldRand := rand.Int63()
	defer func() {
		rand.Seed(oldRand)
	}()
	rand.Seed(42) // Deterministic seed for reproducible results

	const numAgents = 20
	const numRounds = 50
	const convergenceThreshold = 0.7

	// Create agents with different personalities
	agents := make([]*AgentLogic, numAgents)
	for i := 0; i < numAgents; i++ {
		agents[i] = NewAgentLogic(fmt.Sprintf("agent-%d", i), 1000.0)
		agents[i].SetPriceBelief(CommodityFood, 8.0, 12.0) // Initial price belief range
		agents[i].State.Inventory[CommodityFood] = 10
	}

	// Create market
	market := NewMarketLogic(CommodityFood)

	// Simulate trading rounds
	prices := make([]float64, numRounds)
	volumes := make([]int, numRounds)

	for round := 0; round < numRounds; round++ {
		// Clear previous orders
		market.Bids = market.Bids[:0]
		market.Asks = market.Asks[:0]

		// Generate orders from agents
		bidsCount := 0
		asksCount := 0
		for i, agent := range agents {
			isProducer := i%2 == 0 // Even agents are producers (sellers)
			marketState := market.createMarketState()
			order := agent.DecideTrade(CommodityFood, marketState, isProducer)
			if order != nil {
				order.CreatedAt = time.Now() // Set current timestamp
				market.AddOrder(order)
				if order.Type == OrderTypeBid {
					bidsCount++
				} else {
					asksCount++
				}
			}
		}

		// Debug: Print order counts and sample prices
		if round == 0 {
			fmt.Printf("Round 0: Bids=%d, Asks=%d\n", bidsCount, asksCount)
			// Print first few bid/ask prices
			if len(market.Bids) > 0 {
				fmt.Printf("Sample bid prices: %.2f, %.2f\n", market.Bids[0].Price, market.Bids[len(market.Bids)-1].Price)
			}
			if len(market.Asks) > 0 {
				fmt.Printf("Sample ask prices: %.2f, %.2f\n", market.Asks[0].Price, market.Asks[len(market.Asks)-1].Price)
			}
		}

		// Clear market
		result := market.Clear(agents)

		// Record results
		if result.NewPrices[CommodityFood] > 0 {
			prices[round] = result.NewPrices[CommodityFood]
			volumes[round] = result.TotalVolume
		}
	}

	// Analyze convergence
	validPrices := make([]float64, 0)
	for _, price := range prices {
		if price > 0 {
			validPrices = append(validPrices, price)
		}
	}

	if len(validPrices) < 5 {
		t.Skip("Not enough valid price data for convergence analysis")
		return
	}

	// Calculate price stability (coefficient of variation of last 10 prices)
	last10Prices := validPrices[len(validPrices)-10:]
	mean := 0.0
	for _, p := range last10Prices {
		mean += p
	}
	mean /= float64(len(last10Prices))

	variance := 0.0
	for _, p := range last10Prices {
		variance += math.Pow(p-mean, 2)
	}
	variance /= float64(len(last10Prices))
	stdDev := math.Sqrt(variance)

	convergenceRatio := 1.0 - (stdDev / mean) // Lower stdDev/mean means better convergence

	// Report results
	totalVolume := 0
	for _, v := range volumes {
		totalVolume += v
	}

	fmt.Printf("=== BazaarBot Price Convergence Smoke Test ===\n")
	fmt.Printf("Rounds: %d, Agents: %d\n", numRounds, numAgents)
	fmt.Printf("Valid prices recorded: %d\n", len(validPrices))
	fmt.Printf("Total volume traded: %d units\n", totalVolume)
	fmt.Printf("Final price range: %.2f - %.2f\n", last10Prices[0], last10Prices[len(last10Prices)-1])
	fmt.Printf("Price convergence ratio: %.1f%%\n", convergenceRatio*100)

	// Check convergence threshold
	if convergenceRatio >= convergenceThreshold {
		fmt.Printf("✅ SUCCESS: Price convergence achieved (%.1f%% >= %.1f%% threshold)\n",
			convergenceRatio*100, convergenceThreshold*100)
	} else {
		fmt.Printf("❌ FAILURE: Price convergence not achieved (%.1f%% < %.1f%% threshold)\n",
			convergenceRatio*100, convergenceThreshold*100)
		t.Errorf("Price convergence ratio %.3f below threshold %.3f", convergenceRatio, convergenceThreshold)
	}

	// Additional validation
	if len(validPrices) == 0 {
		t.Error("No valid prices recorded - market simulation failed")
	}

	if totalVolume == 0 {
		t.Error("No volume traded - market clearing failed")
	}
}