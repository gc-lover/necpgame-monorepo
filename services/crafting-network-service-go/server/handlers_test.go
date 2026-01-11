//go:align 64
// Issue: #2286 - QA Testing
// Unit tests for crafting-network-service-go handlers

package server

import (
	"context"
	"sync"
	"testing"
	"time"

	"github.com/google/uuid"
	"necpgame/services/crafting-network-service-go/pkg/api"
)

func TestCraftingNetworkHandler_HealthCheck(t *testing.T) {
	// Setup test configuration
	config := &Config{
		MaxWorkers:      10,
		WorkerPool:      make(chan struct{}, 10),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Setup object pools
	sessionPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingSession{}
		},
	}
	progressPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingProgress{}
		},
	}
	materialPool := &sync.Pool{
		New: func() interface{} {
			return &MaterialUpdate{}
		},
	}

	// Create handler
	handler := NewCraftingNetworkHandler(config, sessionPool, progressPool, materialPool)

	// Test health check
	ctx := context.Background()
	result, err := handler.CraftingNetworkServiceHealthCheck(ctx)

	if err != nil {
		t.Fatalf("Health check failed: %v", err)
	}

	// Check result type
	switch res := result.(type) {
	case *api.CraftingNetworkServiceHealthCheckOK:
		if !res.Status.Set {
			t.Error("Health check status not set")
		}
		if res.Status.Value != "healthy" {
			t.Errorf("Expected status 'healthy', got '%s'", res.Status.Value)
		}
		if !res.Timestamp.Set {
			t.Error("Health check timestamp not set")
		}
	default:
		t.Errorf("Unexpected result type: %T", res)
	}
}

func TestCraftingNetworkHandler_WebSocketConnection(t *testing.T) {
	// Setup test configuration
	config := &Config{
		MaxWorkers:      10,
		WorkerPool:      make(chan struct{}, 10),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Setup object pools
	sessionPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingSession{}
		},
	}
	progressPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingProgress{}
		},
	}
	materialPool := &sync.Pool{
		New: func() interface{} {
			return &MaterialUpdate{}
		},
	}

	// Create handler
	handler := NewCraftingNetworkHandler(config, sessionPool, progressPool, materialPool)

	// Test WebSocket connection parameters
	sessionID := uuid.New()
	playerID := uuid.New()
	params := api.CraftingSessionWebSocketParams{
		SessionID: sessionID,
		PlayerID:  playerID,
	}

	ctx := context.Background()
	result, err := handler.CraftingSessionWebSocket(ctx, params)

	if err != nil {
		t.Fatalf("WebSocket connection failed: %v", err)
	}

	// Check result type - should be switching protocols for WebSocket upgrade
	switch res := result.(type) {
	case *api.CraftingSessionWebSocketSwitchingProtocols:
		// Expected result for WebSocket upgrade
		t.Log("WebSocket upgrade initiated correctly")
	default:
		t.Errorf("Unexpected result type: %T", res)
	}
}

func TestCraftingNetworkHandler_UDPConnection(t *testing.T) {
	// Setup test configuration
	config := &Config{
		MaxWorkers:      10,
		WorkerPool:      make(chan struct{}, 10),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	// Setup object pools
	sessionPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingSession{}
		},
	}
	progressPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingProgress{}
		},
	}
	materialPool := &sync.Pool{
		New: func() interface{} {
			return &MaterialUpdate{}
		},
	}

	// Create handler
	handler := NewCraftingNetworkHandler(config, sessionPool, progressPool, materialPool)

	// Test UDP connection request
	playerID := uuid.New()
	sessionID := uuid.New()
	req := &api.UdpCraftingSessionConnectReq{
		PlayerID: playerID,
	}
	params := api.UdpCraftingSessionConnectParams{
		SessionID: sessionID,
	}

	ctx := context.Background()
	result, err := handler.UdpCraftingSessionConnect(ctx, req, params)

	if err != nil {
		t.Fatalf("UDP connection failed: %v", err)
	}

	// Check result type
	switch res := result.(type) {
	case *api.UdpCraftingSessionConnectOK:
		if res.SessionToken == "" {
			t.Error("Session token not generated")
		}
		if res.ServerEndpoint == "" {
			t.Error("Server endpoint not provided")
		}
		t.Logf("UDP connection established: token=%s, endpoint=%s", res.SessionToken, res.ServerEndpoint)
	default:
		t.Errorf("Unexpected result type: %T", res)
	}
}

func TestCraftingNetworkServer_WorkerPool(t *testing.T) {
	config := &Config{
		MaxWorkers:      2,
		WorkerPool:      make(chan struct{}, 2),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	server := NewCraftingNetworkServer(config)

	// Test acquiring workers
	ctx := context.Background()

	// Should acquire first worker successfully
	err := server.AcquireNetworkWorker(ctx)
	if err != nil {
		t.Fatalf("Failed to acquire first worker: %v", err)
	}

	// Should acquire second worker successfully
	err = server.AcquireNetworkWorker(ctx)
	if err != nil {
		t.Fatalf("Failed to acquire second worker: %v", err)
	}

	// Third worker should timeout
	err = server.AcquireNetworkWorker(ctx)
	if err == nil {
		t.Error("Expected timeout when acquiring third worker")
	}

	// Release workers
	server.ReleaseNetworkWorker()
	server.ReleaseNetworkWorker()

	// Should be able to acquire again
	err = server.AcquireNetworkWorker(ctx)
	if err != nil {
		t.Fatalf("Failed to acquire worker after release: %v", err)
	}
	server.ReleaseNetworkWorker()
}

func BenchmarkCraftingNetworkHandler_HealthCheck(b *testing.B) {
	config := &Config{
		MaxWorkers:      100,
		WorkerPool:      make(chan struct{}, 100),
		CacheTTL:        10 * time.Minute,
		ReadTimeout:     15 * time.Second,
		WriteTimeout:    15 * time.Second,
		IdleTimeout:     60 * time.Second,
		MaxHeaderBytes:  1 << 16,
		WebSocketPort:   8081,
		UDPPort:         9999,
	}

	sessionPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingSession{}
		},
	}
	progressPool := &sync.Pool{
		New: func() interface{} {
			return &CraftingProgress{}
		},
	}
	materialPool := &sync.Pool{
		New: func() interface{} {
			return &MaterialUpdate{}
		},
	}

	handler := NewCraftingNetworkHandler(config, sessionPool, progressPool, materialPool)

	ctx := context.Background()

	b.ResetTimer()
	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			_, err := handler.CraftingNetworkServiceHealthCheck(ctx)
			if err != nil {
				b.Fatalf("Health check failed: %v", err)
			}
		}
	})
}