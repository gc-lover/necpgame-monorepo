// Package main demonstrates the optimized Global State Manager for MMOFPS games
package main

import (
	"context"
	"fmt"
	"log"
	"time"

	"necpgame/scripts/core/error-handling"
	"necpgame/scripts/global-state"
)

func main() {
	// Initialize logger
	logger := errorhandling.NewLogger("global-state-demo")

	// Create optimized configuration for MMOFPS
	config := &globalstate.GlobalStateConfig{
		MaxStates:         100000,
		CacheTTL:          30 * time.Minute,
		UpdateBufferSize:  10000,
		CleanupInterval:   5 * time.Minute,
		StateSyncInterval: 1 * time.Minute,
		EnableCompression: true,
		MaxConcurrentOps:  1000,

		// MMOFPS Optimizations
		PlayerStateShards: 16,
		GameStateShards:   8,
		UpdateWorkers:     4,
		EventWorkers:      4,
		EnableSharding:    true,
		ShardLockTimeout:  10 * time.Millisecond,
	}

	// Initialize Global State Manager
	gsm, err := globalstate.NewGlobalStateManager(config, logger)
	if err != nil {
		log.Fatal("Failed to initialize Global State Manager:", err)
	}

	fmt.Println("🚀 Global State Manager Demo - MMOFPS Optimizations")
	fmt.Println("==================================================")

	// Demonstrate player state operations
	fmt.Println("\n📊 Testing Player State Operations:")

	playerID := "player_12345"

	// Create/Update player state
	err = gsm.UpdatePlayerState(playerID, func(state *globalstate.PlayerState) *globalstate.PlayerState {
		state.Health = 100.0
		state.Level = 15
		state.Position = globalstate.Position{
			X:     123.45,
			Y:     67.89,
			Z:     10.0,
			Zone:  "downtown",
			World: "san_francisco",
		}
		state.IsOnline = true
		return state
	})
	if err != nil {
		log.Fatal("Failed to update player state:", err)
	}
	fmt.Printf("✅ Player %s state updated\n", playerID)

	// Retrieve player state
	playerState, err := gsm.GetPlayerState(playerID)
	if err != nil {
		log.Fatal("Failed to get player state:", err)
	}
	fmt.Printf("✅ Retrieved player state: Level %d, Health %.1f, Zone %s\n",
		playerState.Level, playerState.Health, playerState.Position.Zone)

	// Demonstrate game state operations
	fmt.Println("\n🎮 Testing Game State Operations:")

	gameID := "match_abc123"

	// Create/Update game state
	err = gsm.UpdateGameState(gameID, func(state *globalstate.GameState) *globalstate.GameState {
		state.Status = globalstate.GameStatusRunning
		state.Players = []string{"player_12345", "player_67890"}
		state.MaxPlayers = 10
		state.Score = map[string]int{
			"player_12345": 1500,
			"player_67890": 1200,
		}
		state.StartTime = &[]time.Time{time.Now()}[0]
		return state
	})
	if err != nil {
		log.Fatal("Failed to update game state:", err)
	}
	fmt.Printf("✅ Game %s state updated with %d players\n", gameID, len([]string{"player_12345", "player_67890"}))

	// Retrieve game state
	gameState, err := gsm.GetGameState(gameID)
	if err != nil {
		log.Fatal("Failed to get game state:", err)
	}
	fmt.Printf("✅ Retrieved game state: Status %s, Players %d/%d\n",
		gameState.Status, len(gameState.Players), gameState.MaxPlayers)

	// Demonstrate global state operations
	fmt.Println("\n🌍 Testing Global State Operations:")

	err = gsm.SetGlobalState("server_status", "healthy")
	if err != nil {
		log.Fatal("Failed to set global state:", err)
	}
	fmt.Println("✅ Global server status set to 'healthy'")

	globalValue, err := gsm.GetGlobalState("server_status")
	if err != nil {
		log.Fatal("Failed to get global state:", err)
	}
	fmt.Printf("✅ Retrieved global state: server_status = %v\n", globalValue)

	// Demonstrate performance statistics
	fmt.Println("\n📈 Performance Statistics:")

	stats := gsm.GetStats()
	fmt.Printf("✅ Total Operations: %d\n", stats["total_operations"])
	fmt.Printf("✅ Cache Hit Rate: %.1f%%\n", stats["cache_hit_rate_percent"])
	fmt.Printf("✅ Player States: %d (avg shard size: %.1f)\n",
		stats["player_states_count"], stats["avg_player_shard_size"])
	fmt.Printf("✅ Game States: %d (avg shard size: %.1f)\n",
		stats["game_states_count"], stats["avg_game_shard_size"])
	fmt.Printf("✅ Sharding: %v (%d player shards, %d game shards)\n",
		stats["sharding_enabled"], stats["player_state_shards"], stats["game_state_shards"])
	fmt.Printf("✅ Worker Pools: %d update workers, %d event workers\n",
		stats["update_workers"], stats["event_workers"])

	// Demonstrate batch operations
	fmt.Println("\n🔄 Testing Batch Operations:")

	updates := []*globalstate.StateUpdate{
		{
			Type:      globalstate.UpdateTypeUpdate,
			EntityID:  "player_batch_1",
			StateType: globalstate.StateTypePlayer,
			Data: &globalstate.PlayerState{
				PlayerID: "player_batch_1",
				Level:    10,
				Health:   85.0,
			},
			Timestamp: time.Now(),
			Priority:  globalstate.UpdatePriorityNormal,
		},
		{
			Type:      globalstate.UpdateTypeUpdate,
			EntityID:  "player_batch_2",
			StateType: globalstate.StateTypePlayer,
			Data: &globalstate.PlayerState{
				PlayerID: "player_batch_2",
				Level:    12,
				Health:   92.0,
			},
			Timestamp: time.Now(),
			Priority:  globalstate.UpdatePriorityNormal,
		},
	}

	err = gsm.BatchUpdate(updates)
	if err != nil {
		log.Fatal("Failed to perform batch update:", err)
	}
	fmt.Printf("✅ Batch update completed for %d operations\n", len(updates))

	// Final statistics
	fmt.Println("\n🏁 Final Performance Statistics:")
	finalStats := gsm.GetStats()
	fmt.Printf("✅ Total Operations: %d\n", finalStats["total_operations"])
	fmt.Printf("✅ Cache Hit Rate: %.1f%%\n", finalStats["cache_hit_rate_percent"])
	fmt.Printf("✅ Player States: %d\n", finalStats["player_states_count"])
	fmt.Printf("✅ Game States: %d\n", finalStats["game_states_count"])

	// Graceful shutdown
	fmt.Println("\n🛑 Shutting down Global State Manager...")
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := gsm.Shutdown(ctx); err != nil {
		log.Fatal("Failed to shutdown gracefully:", err)
	}

	fmt.Println("✅ Global State Manager shut down successfully")
	fmt.Println("\n🎯 Demo completed! MMOFPS Global State Manager is optimized and ready for production.")
}
