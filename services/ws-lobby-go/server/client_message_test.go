// Issue: #104
package server

import (
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestClient_HandleMessage_Join(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "general",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client, "general")

	message := []byte("JOIN test-room")
	client.handleMessage(message)

	assert.Equal(t, "test-room", client.room)

	select {
	case msg := <-client.send:
		assert.Equal(t, "JOINED test-room", string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for JOINED message")
	}

	server.mu.RLock()
	_, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.True(t, exists)
}

func TestClient_HandleMessage_Leave(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "test-room",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client, "test-room")

	message := []byte("LEAVE")
	client.handleMessage(message)

	assert.Equal(t, "general", client.room)

	select {
	case msg := <-client.send:
		assert.Equal(t, "LEFT", string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for LEFT message")
	}
}

func TestClient_HandleMessage_Msg(t *testing.T) {
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

	message := []byte("MSG Hello, World!")
	client1.handleMessage(message)

	expectedMessage := "[test-room] Hello, World!"

	select {
	case msg := <-client1.send:
		assert.Equal(t, expectedMessage, string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client1")
	}

	select {
	case msg := <-client2.send:
		assert.Equal(t, expectedMessage, string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client2")
	}
}

func TestClient_HandleMessage_Broadcast(t *testing.T) {
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

	message := []byte("broadcast message")
	client1.handleMessage(message)

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

func TestClient_HandleMessage_InvalidCommands(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "test-room",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client, "test-room")

	invalidMessages := [][]byte{
		[]byte("INVALID"),
		[]byte("JOI"),
		[]byte("LEAV"),
		[]byte("MS"),
		[]byte(""),
	}

	for _, msg := range invalidMessages {
		client.handleMessage(msg)

		select {
		case <-client.send:
		case <-time.After(50 * time.Millisecond):
		}
	}

	server.mu.RLock()
	room, exists := server.rooms["test-room"]
	server.mu.RUnlock()

	assert.True(t, exists)

	room.mu.RLock()
	_, clientExists := room.clients[client]
	room.mu.RUnlock()

	assert.True(t, clientExists)

	cleanup()
}

func TestClient_HandleMessage_Join_EmptyRoomName(t *testing.T) {
	server, cleanup := setupTestLobbyServer()
	defer cleanup()

	client := &Client{
		conn:   nil,
		room:   "general",
		server: server,
		send:   make(chan []byte, 256),
	}

	server.addClientToRoom(client, "general")

	message := []byte("JOIN ")
	client.handleMessage(message)

	assert.Equal(t, "", client.room)

	select {
	case msg := <-client.send:
		assert.Equal(t, "JOINED ", string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for JOINED message")
	}

	cleanup()
}

func TestClient_HandleMessage_Msg_EmptyBody(t *testing.T) {
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

	message := []byte("MSG ")
	client1.handleMessage(message)

	expectedMessage := "[test-room] "

	select {
	case msg := <-client1.send:
		assert.Equal(t, expectedMessage, string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client1")
	}

	select {
	case msg := <-client2.send:
		assert.Equal(t, expectedMessage, string(msg))
	case <-time.After(100 * time.Millisecond):
		t.Fatal("Timeout waiting for message in client2")
	}

	cleanup()
}
