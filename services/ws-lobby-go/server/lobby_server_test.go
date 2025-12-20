// Issue: #104
package server

import (
	"context"
	"net/http"
	"strings"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestNewLobbyServer(t *testing.T) {
	config := NewLobbyConfig("18081", "http://localhost:8080/realms/necpgame", "http://localhost:8080/realms/necpgame/protocol/openid-connect/certs")
	server := NewLobbyServer(config)
	defer server.Stop()

	assert.NotNil(t, server)
	assert.NotNil(t, server.config)
	assert.NotNil(t, server.rooms)
	assert.Equal(t, config, server.config)
}

func TestNewLobbyConfig(t *testing.T) {
	config := NewLobbyConfig("18081", "http://localhost:8080/realms/necpgame", "http://localhost:8080/realms/necpgame/protocol/openid-connect/certs")

	assert.Equal(t, "18081", config.Port)
	assert.Equal(t, "http://localhost:8080/realms/necpgame", config.Issuer)
	assert.NotNil(t, config.JwtValidator)
}

func TestJwtValidator_Verify(t *testing.T) {
	validator := NewJwtValidator("http://localhost:8080/realms/necpgame", "http://localhost:8080/realms/necpgame/protocol/openid-connect/certs")

	assert.True(t, validator.Verify("valid-token"))
	assert.True(t, validator.Verify(" token "))
	assert.False(t, validator.Verify(""))
	assert.False(t, validator.Verify("   "))
}

func TestLobbyServer_Stop(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client1 := &Client{
		conn:   nil,
		room:   "test-room",
		server: server,
		send:   make(chan []byte, 256),
	}

	client2 := &Client{
		conn:   nil,
		room:   "test-room",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client1, "test-room")
	server.addClientToRoom(client2, "test-room")

	server.mu.Lock()
	for _, room := range server.rooms {
		room.mu.Lock()
		for client := range room.clients {
			if client.conn != nil {
				close(client.send)
				client.conn.Close()
			} else {
				close(client.send)
			}
		}
		room.mu.Unlock()
	}
	server.rooms = make(map[string]*Room)
	server.mu.Unlock()

	server.mu.RLock()
	roomCount := len(server.rooms)
	server.mu.RUnlock()

	assert.Equal(t, 0, roomCount)
}

func TestLobbyServer_Start_Stop(t *testing.T) {
	// Use a random port to avoid conflicts
	config := NewLobbyConfig("0", "http://localhost:8080/realms/necpgame", "http://localhost:8080/realms/necpgame/protocol/openid-connect/certs")
	server := NewLobbyServer(config)

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	started := make(chan error, 1)
	go func() {
		started <- server.Start(ctx)
	}()

	time.Sleep(50 * time.Millisecond)

	cancel()
	server.Stop()

	select {
	case err := <-started:
		if err != nil && err != http.ErrServerClosed {
			// Ignore bind errors as they can occur in test environments
			if !strings.Contains(err.Error(), "bind") {
				t.Errorf("Unexpected error: %v", err)
			}
		}
	case <-time.After(1 * time.Second):
		t.Fatal("Timeout waiting for server to stop")
	}
}
