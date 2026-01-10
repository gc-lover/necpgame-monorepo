package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"math/rand"
	"os"
	"time"

	"github.com/lib/pq"
	"github.com/segmentio/kafka-go"
	"necpgame/services/economy-service-go/internal/simulation/bazaar"
)

func main() {
	fmt.Println("Economy Service Starting...")

	// Simulation Test
	simTest()

	// Start Kafka consumer for hourly ticks (#2281)
	go startHourlyTickConsumer()

	// Keep service running
	select {}
}

// startHourlyTickConsumer starts Kafka consumer for hourly simulation ticks
// Issue: #2281 - Event-Driven Simulation Tick Infrastructure
func startHourlyTickConsumer() {
	// Kafka configuration with context timeout
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	kafkaURL := os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	if kafkaURL == "" {
		kafkaURL = "localhost:9092" // fallback for local development
	}

	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:  []string{kafkaURL},
		Topic:    "world.tick.hourly",
		GroupID:  "economy-service-group",
		MinBytes: 10e3, // 10KB
		MaxBytes: 10e6, // 10MB
	})
	defer reader.Close()

	log.Println("Starting hourly tick consumer for economy service...")
	log.Printf("Connected to Kafka brokers: %s", kafkaURL)

	for {
		// Use context with timeout for message reading
		msgCtx, msgCancel := context.WithTimeout(ctx, 30*time.Second)
		msg, err := reader.ReadMessage(msgCtx)
		msgCancel()

		if err != nil {
			if err == context.DeadlineExceeded {
				// Timeout is normal, continue polling
				continue
			}
			log.Printf("Error reading message: %v", err)
			time.Sleep(2 * time.Second)
			continue
		}

		log.Printf("Received hourly tick message from partition %d, offset %d", msg.Partition, msg.Offset)

		// Parse tick event according to world-tick-events.json schema
		var tickEvent TickEventMessage
		if err := json.Unmarshal(msg.Value, &tickEvent); err != nil {
			log.Printf("Error parsing tick event: %v. Raw message: %s", err, string(msg.Value))
			continue
		}

		gameHourStr := "N/A"
		if tickEvent.Data.GameHour != nil {
			gameHourStr = fmt.Sprintf("%d", *tickEvent.Data.GameHour)
		}
		log.Printf("Processing hourly tick: event_id=%s, tick_id=%s, game_hour=%s",
			tickEvent.EventID, tickEvent.Data.TickID, gameHourStr)

		// Process the tick - trigger market clearing
		if err := processHourlyTick(ctx, &tickEvent); err != nil {
			log.Printf("Error processing hourly tick: %v", err)
			// Continue processing next tick instead of stopping
			continue
		}

		log.Printf("Successfully processed hourly tick: tick_id=%s", tickEvent.Data.TickID)
	}
}

// TickEventMessage represents the tick event message structure from Kafka
// Issue: #2281 - Matches world-tick-events.json schema
type TickEventMessage struct {
	EventID   string    `json:"event_id"`
	EventType string    `json:"event_type"`
	Timestamp string    `json:"timestamp"`
	Version   string    `json:"version"`
	Source    string    `json:"source"`
	Data      TickDataMessage `json:"data"`
}

// TickDataMessage represents the tick data payload
// Issue: #2281
type TickDataMessage struct {
	TickID        string     `json:"tick_id"`
	TickType      string     `json:"tick_type"`
	GameHour      *int       `json:"game_hour,omitempty"`
	GameDay       *int       `json:"game_day,omitempty"`
	GameTime      string     `json:"game_time"`
	TickTimestamp string     `json:"tick_timestamp"`
	TriggeredBy   string     `json:"triggered_by"`
	Consumers     []string   `json:"consumers,omitempty"`
}

// MarketClearedEvent represents market clearing event for simulation.event topic
// Issue: #2281 - Matches simulation-events.json schema
type MarketClearedEvent struct {
	EventType string    `json:"event_type"`
	Commodity string    `json:"commodity"`
	Price     float64   `json:"price"`
	Volume    int       `json:"volume"`
	Timestamp time.Time `json:"timestamp"`
	MarketID  string    `json:"market_id"`
	Period    string    `json:"period"`
	TickID    string    `json:"tick_id,omitempty"`
	GameHour  *int      `json:"game_hour,omitempty"`
}

// processHourlyTick processes hourly simulation tick and triggers market clearing
// Issue: #2281 - Event-Driven Simulation Tick Infrastructure
func processHourlyTick(ctx context.Context, tickEvent *TickEventMessage) error {
	gameHourStr := "N/A"
	if tickEvent.Data.GameHour != nil {
		gameHourStr = fmt.Sprintf("%d", *tickEvent.Data.GameHour)
	}
	log.Printf("Processing hourly tick - triggering market clearing for game_hour=%s", gameHourStr)

	// Get Kafka configuration
	kafkaURL := os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	if kafkaURL == "" {
		kafkaURL = "localhost:9092"
	}

	// Initialize Kafka writer for publishing market results with timeout
	writer := kafka.NewWriter(kafka.WriterConfig{
		Brokers:      []string{kafkaURL},
		Topic:        "simulation.event",
		Balancer:     &kafka.LeastBytes{},
		WriteTimeout: 10 * time.Second,
		BatchSize:    1, // Send immediately for market clearing events
	})
	defer writer.Close()

	// Create market instance (in production this would be from dependency injection)
	// For now, use legacy Clear() method that returns (price, volume)
	market := bazaar.NewMarketLogic(bazaar.CommodityFood)

	// Trigger market clearing using legacy method
	// TODO: In future, use Clear(agents) with proper agent list from database
	price, volume := market.ClearLegacy()
	log.Printf("Market cleared - Commodity: %s, Price: %.2f, Volume: %d", bazaar.CommodityFood, price, volume)

	// Publish market results to simulation.event topic
	marketEvent := MarketClearedEvent{
		EventType:   "simulation.event.market_cleared",
		Commodity:   string(bazaar.CommodityFood),
		Price:       price,
		Volume:      volume,
		Timestamp:   time.Now().UTC(),
		MarketID:    fmt.Sprintf("market-%s", string(bazaar.CommodityFood)),
		Period:      "hourly",
		TickID:      tickEvent.Data.TickID,
		GameHour:    tickEvent.Data.GameHour,
	}

	if err := publishMarketResults(ctx, writer, marketEvent); err != nil {
		return fmt.Errorf("failed to publish market results: %w", err)
	}

	log.Printf("Market results published to Kafka topic 'simulation.event'")

	// Update all active markets in the system
	if err := updateAllActiveMarkets(ctx, writer, tickEvent); err != nil {
		log.Printf("Warning: Failed to update all active markets: %v", err)
		// Don't fail the whole tick processing if one market fails
	}

	// Persist market state to database
	if err := persistMarketState(ctx, bazaar.CommodityFood, price, volume, tickEvent); err != nil {
		log.Printf("Warning: Failed to persist market state: %v", err)
		// Don't fail the whole tick processing if persistence fails
	}

	return nil
}

// simTest demonstrates BazaarBot simulation with price convergence
// Issue: #2278
func simTest() {
	fmt.Println("=== BazaarBot Simulation Test ===")
	rand.Seed(time.Now().UnixNano())

	// Create a Market for Food
	market := bazaar.NewMarketLogic(bazaar.CommodityFood)

	// Create multiple agents with different personalities and beliefs
	agents := make([]*bazaar.AgentLogic, 0)

	// Create Buyers (Consumers)
	for i := 0; i < 5; i++ {
		buyer := bazaar.NewAgentLogic(fmt.Sprintf("buyer-%d", i+1), 100.0)
		// Initial beliefs: Food is worth 5-15
		buyer.SetPriceBelief(bazaar.CommodityFood, 5.0+float64(i), 15.0+float64(i))
		agents = append(agents, buyer)
	}

	// Create Sellers (Producers) with inventory
	for i := 0; i < 5; i++ {
		seller := bazaar.NewAgentLogic(fmt.Sprintf("seller-%d", i+1), 100.0)
		seller.State.Inventory[bazaar.CommodityFood] = 10 + i*5
		// Initial beliefs: Food is worth 8-12
		seller.SetPriceBelief(bazaar.CommodityFood, 8.0+float64(i), 12.0+float64(i))
		agents = append(agents, seller)
	}

	fmt.Printf("Created %d agents (5 buyers, 5 sellers)\n\n", len(agents))

	// Run multiple trading rounds to observe price convergence
	numRounds := 10
	for round := 0; round < numRounds; round++ {
		fmt.Printf("--- Round %d ---\n", round+1)

		// Create market state for agents to use in decisions
		marketState := market.CreateMarketState()

		// Agents decide on trades
		for _, agent := range agents {
			isProducer := agent.State.Inventory[bazaar.CommodityFood] > 0
			order := agent.DecideTrade(bazaar.CommodityFood, marketState, isProducer)

			if order != nil {
				market.AddOrder(order)
				if order.Type == bazaar.OrderTypeBid {
					fmt.Printf("  %s bids: %.2f (Qty: %d)\n", agent.State.ID, order.Price, order.Quantity)
				} else {
					fmt.Printf("  %s asks: %.2f (Qty: %d)\n", agent.State.ID, order.Price, order.Quantity)
				}
			}
		}

		// Clear market
		result := market.Clear(agents)
		fmt.Printf("  Market Cleared: Price %.2f, Volume %d, Efficiency %.2f%%\n",
			result.NewPrices[bazaar.CommodityFood], result.TotalVolume, result.MarketEfficiency*100)

		// Show price convergence
		if len(result.ClearedTrades) > 0 {
			fmt.Printf("  Executed %d trades\n", len(result.ClearedTrades))
			for _, trade := range result.ClearedTrades {
				fmt.Printf("    Trade: %s -> %s, Price %.2f, Qty %d\n",
					trade.SellerID, trade.BuyerID, trade.Price, trade.Quantity)
			}
		}

		fmt.Println()
	}

	// Show final agent beliefs and wealth
	fmt.Println("=== Final Agent States ===")
	for _, agent := range agents {
		belief := agent.State.PriceBeliefs[bazaar.CommodityFood]
		if belief != nil {
			fmt.Printf("  %s: Belief [%.2f - %.2f], Wealth %.2f, Inventory %d\n",
				agent.State.ID, belief.Min, belief.Max, agent.State.Wealth, agent.State.Inventory[bazaar.CommodityFood])
		}
	}

	fmt.Println("\n=== Simulation Complete ===")
}

// publishMarketResults publishes market clearing results to Kafka simulation.event topic
// Issue: #2281 - Event-Driven Simulation Tick Infrastructure
func publishMarketResults(ctx context.Context, writer *kafka.Writer, event MarketClearedEvent) error {
	// Serialize event to JSON
	eventJSON, err := json.Marshal(event)
	if err != nil {
		return fmt.Errorf("failed to marshal market event: %w", err)
	}

	// Create Kafka message with proper headers
	message := kafka.Message{
		Key:   []byte(event.MarketID),
		Value: eventJSON,
		Time:  event.Timestamp,
		Headers: []kafka.Header{
			{Key: "event_type", Value: []byte(event.EventType)},
			{Key: "commodity", Value: []byte(event.Commodity)},
			{Key: "period", Value: []byte(event.Period)},
			{Key: "tick_id", Value: []byte(event.TickID)},
		},
	}

	// Publish to Kafka with context timeout
	writeCtx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	err = writer.WriteMessages(writeCtx, message)
	if err != nil {
		return fmt.Errorf("failed to write market event to Kafka: %w", err)
	}

	log.Printf("Published market event: commodity=%s, price=%.2f, volume=%d, tick_id=%s",
		event.Commodity, event.Price, event.Volume, event.TickID)
	return nil
}

// updateAllActiveMarkets updates all active commodity markets in the system
// Issue: #2281 - Event-Driven Simulation Tick Infrastructure
func updateAllActiveMarkets(ctx context.Context, writer *kafka.Writer, tickEvent *TickEventMessage) error {
	commodities := []bazaar.Commodity{
		bazaar.CommodityFood,
		bazaar.CommodityWood,
		bazaar.CommodityMetal,
		bazaar.CommodityWeapon,
		bazaar.CommodityCrystal,
	}

	for _, commodity := range commodities {
		// Create market instance for each commodity
		market := bazaar.NewMarketLogic(commodity)

		// Clear market and get results (using legacy method - no agents in context)
		price, volume := market.ClearLegacy()

		// Create market cleared event for this commodity
		marketEvent := MarketClearedEvent{
			EventType:   "simulation.event.market_cleared",
			Commodity:   string(commodity),
			Price:       price,
			Volume:      volume,
			Timestamp:   time.Now().UTC(),
			MarketID:    fmt.Sprintf("market-%s", string(commodity)),
			Period:      "hourly",
			TickID:      tickEvent.Data.TickID,
			GameHour:    tickEvent.Data.GameHour,
		}

		// Publish results for each commodity
		if err := publishMarketResults(ctx, writer, marketEvent); err != nil {
			log.Printf("Warning: Failed to update market for %s: %v", commodity, err)
			// Continue with other commodities instead of failing completely
			continue
		}

		log.Printf("Updated market for %s: price=%.2f, volume=%d", commodity, price, volume)
	}

	return nil
}

// persistMarketState saves market state to database (placeholder implementation)
// Issue: #2281 - Event-Driven Simulation Tick Infrastructure
// TODO: Implement actual database persistence with proper connection pooling
func persistMarketState(ctx context.Context, commodity bazaar.Commodity, price float64, volume int, tickEvent *TickEventMessage) error {
	// TODO: Implement actual database persistence
	// This would typically involve:
	// 1. Using database connection pool from dependency injection
	// 2. Inserting/updating market_state table with context timeout
	// 3. Storing price, volume, timestamp, commodity, tick_id
	// 4. Handling transaction rollback on errors
	// 5. Using prepared statements for performance

	// For now, just log the persistence action
	log.Printf("Persisting market state: commodity=%s, price=%.2f, volume=%d, tick_id=%s",
		commodity, price, volume, tickEvent.Data.TickID)

	// Simulate database operation with context-aware delay
	select {
	case <-ctx.Done():
		return ctx.Err()
	case <-time.After(10 * time.Millisecond):
		// Simulated database write
	}

	// In production, this would be:
	/*
	db := getDBFromPool() // From dependency injection with connection pooling
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		INSERT INTO economy.market_states 
		(commodity, price, volume, period, tick_id, game_hour, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (commodity, period, created_at) 
		DO UPDATE SET price = EXCLUDED.price, volume = EXCLUDED.volume, tick_id = EXCLUDED.tick_id
	`

	_, err := db.ExecContext(ctx, query, 
		commodity, 
		price, 
		volume, 
		"hourly", 
		tickEvent.Data.TickID,
		tickEvent.Data.GameHour,
		time.Now().UTC())
	if err != nil {
		return fmt.Errorf("failed to persist market state: %w", err)
	}
	*/

	return nil
}
