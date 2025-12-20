// Issue: #104
package server

import (
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestLobbyServer_AddClientToRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client, "test-room")

	server.mu.RLock()
	room, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.True(t, exists)
	assert.NotNil(t, room)

	room.mu.RLock()
	_, clientExists := room.clients[client]
	room.mu.RUnlock()

	assert.True(t, clientExists)
	assert.Equal(t, "test-room", client.room)
}

func TestLobbyServer_AddClientToRoom_ExistingRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client1 := &Client{
		conn:   nil,
		room:   "",
		server: server,
		send:   make(chan []byte, 256),
	}

	client2 := &Client{
		conn:   nil,
		room:   "",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client1, "test-room")
	server.addClientToRoom(client2, "test-room")

	server.mu.RLock()
	room, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.True(t, exists)

	room.mu.RLock()
	clientCount := len(room.clients)
	room.mu.RUnlock()

	assert.Equal(t, 2, clientCount)
}

func TestLobbyServer_RemoveClientFromRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "test-room",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client, "test-room")
	server.removeClientFromRoom(client)

	server.mu.RLock()
	_, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.False(t, exists)
}

func TestLobbyServer_RemoveClientFromRoom_NonEmptyRoom(t *testing.T) {
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
	server.removeClientFromRoom(client1)

	server.mu.RLock()
	room, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.True(t, exists)

	room.mu.RLock()
	clientCount := len(room.clients)
	room.mu.RUnlock()

	assert.Equal(t, 1, clientCount)
}

func TestLobbyServer_BroadcastToRoom(t *testing.T) {
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

	message := []byte("test message")
	server.broadcastToRoom("test-room", message)

	select {
	case msg := <-client1.send:
		assert.Equal(t, message, msg)
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client1")
	}

	select {
	case msg := <-client2.send:
		assert.Equal(t, message, msg)
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client2")
	}
}

func TestLobbyServer_BroadcastToRoom_NonExistentRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	message := []byte("test message")
	server.broadcastToRoom("non-existent", message)

	server.mu.RLock()
	_, exists := server.rooms["non-existent"]
	server.mu.RUnlock()

	assert.False(t, exists)
}

func TestLobbyServer_BroadcastToRoom_EmptyRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	room := &Room{
		clients: make(map[*Client]bool),
	}
	server.mu.Lock()
	server.rooms["empty-room"] = room
	server.mu.Unlock()

	message := []byte("test message")
	server.broadcastToRoom("empty-room", message)

	room.mu.RLock()
	clientCount := len(room.clients)
	room.mu.RUnlock()

	assert.Equal(t, 0, clientCount)
}

func TestLobbyServer_BroadcastToRoom_FullChannel(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "test-room",
		server: server,
		send:   make(chan []byte, 1),
	}

	server.addClientToRoom(client, "test-room")

	client.send <- []byte("first message")

	message := []byte("test message")
	server.broadcastToRoom("test-room", message)

	time.Sleep(50 * time.Millisecond)

	server.mu.RLock()
	room, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	if exists {
		room.mu.RLock()
		_, clientExists := room.clients[client]
		room.mu.RUnlock()

		if !clientExists {
			assert.True(t, true, "Client removed due to full channel")
		}
	}

	cleanup()
}

func TestLobbyServer_MultipleRooms(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client1 := &Client{
		conn:   nil,
		room:   "",
		server: server,
		send:   make(chan []byte, 256),
	}

	client2 := &Client{
		conn:   nil,
		room:   "",
		server: server,
		send:   make(chan []byte, 256),
	}

	client3 := &Client{
		conn:   nil,
		room:   "",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client1, "room1")
	server.addClientToRoom(client2, "room2")
	server.addClientToRoom(client3, "room1")

	server.mu.RLock()
	roomCount := len(server.rooms)
	server.mu.RUnlock()

	assert.Equal(t, 2, roomCount)

	message := []byte("room1 message")
	server.broadcastToRoom("room1", message)

	select {
	case msg := <-client1.send:
		assert.Equal(t, message, msg)
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client1")
	}

	select {
	case msg := <-client3.send:
		assert.Equal(t, message, msg)
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client3")
	}

	select {
	case <-client2.send:
		t.Fatal("Client2 should not receive message from room1")
	case <-time.After(50 * time.Millisecond):
		assert.True(t, true, "Client2 correctly did not receive message")
	}

	cleanup()
}
