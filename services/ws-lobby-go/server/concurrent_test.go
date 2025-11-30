// Issue: #104
package server

import (
	"sync"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestLobbyServer_ConcurrentAddRemove(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

	var wg sync.WaitGroup
	clients := make([]*Client, 10)

	for i := 0; i < 10; i++ {
		clients[i] = &Client{
			conn:   nil,
			room:   "",
			server: server,
			send:   make(chan []byte, 256),
		}
	}

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(idx int) {
			defer wg.Done()
			server.addClientToRoom(clients[idx], "test-room")
		}(i)
	}

	wg.Wait()

	server.mu.RLock()
	room, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.True(t, exists)

	room.mu.RLock()
	clientCount := len(room.clients)
	room.mu.RUnlock()

	assert.Equal(t, 10, clientCount)

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(idx int) {
			defer wg.Done()
			server.removeClientFromRoom(clients[idx])
		}(i)
	}

	wg.Wait()

	server.mu.RLock()
	_, exists = server.rooms["test-room"]
	server.mu.RUnlock()

	assert.False(t, exists)
}

func TestLobbyServer_ConcurrentBroadcast(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

	clients := make([]*Client, 5)
	for i := 0; i < 5; i++ {
		clients[i] = &Client{
			conn:   nil,
			room:   "test-room",
			server: server,
			send:   make(chan []byte, 256),
		}
		server.addClientToRoom(clients[i], "test-room")
	}

	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			server.broadcastToRoom("test-room", []byte("test message"))
		}()
	}

	wg.Wait()

	for i := 0; i < 5; i++ {
		messageCount := 0
		timeout := time.After(200 * time.Millisecond)
		for {
			select {
			case <-clients[i].send:
				messageCount++
			case <-timeout:
				goto done
			}
		}
	done:
		assert.Greater(t, messageCount, 0, "Client %d should receive at least one message", i)
	}

	cleanup()
}

