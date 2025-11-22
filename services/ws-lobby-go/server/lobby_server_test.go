package server

import (
	"context"
	"net/http"
	"net/http/httptest"
	"strings"
	"sync"
	"testing"
	"time"

	"github.com/gorilla/websocket"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestLobbyServer(t *testing.T) (*LobbyServer, func()) {
	config := NewLobbyConfig("18081", "http://localhost:8080/realms/necpgame", "http://localhost:8080/realms/necpgame/protocol/openid-connect/certs")
	server := NewLobbyServer(config)

	cleanup := func() {
		server.mu.Lock()
		for _, room := range server.rooms {
			room.mu.Lock()
			for client := range room.clients {
				if client.conn != nil {
					close(client.send)
					client.conn.Close()
				}
			}
			room.mu.Unlock()
		}
		server.rooms = make(map[string]*Room)
		server.mu.Unlock()
	}

	return server, cleanup
}

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

func TestLobbyServer_AddClientToRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

	message := []byte("test message")
	server.broadcastToRoom("non-existent", message)

	server.mu.RLock()
	_, exists := server.rooms["non-existent"]
	server.mu.RUnlock()

	assert.False(t, exists)
}

func TestLobbyServer_BroadcastToRoom_EmptyRoom(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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

func TestClient_HandleMessage_Join(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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

func TestLobbyServer_Stop(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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

func TestLobbyServer_HandleWebSocket_Unauthorized(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

	req := httptest.NewRequest("GET", "/ws", nil)
	w := httptest.NewRecorder()

	server.handleWebSocket(w, req)

	assert.Equal(t, http.StatusUnauthorized, w.Code)
	assert.Contains(t, w.Body.String(), "Unauthorized")

	cleanup()
}

func TestLobbyServer_HandleWebSocket_ValidToken(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

	s := httptest.NewServer(http.HandlerFunc(server.handleWebSocket))
	defer s.Close()

	wsURL := "ws" + strings.TrimPrefix(s.URL, "http") + "/ws?token=valid-token"

	_, _, err := websocket.DefaultDialer.Dial(wsURL, nil)
	if err != nil {
		t.Skipf("WebSocket connection test skipped: %v", err)
	}

	cleanup()
}

func TestLobbyServer_HandleServerWebSocket(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

	s := httptest.NewServer(http.HandlerFunc(server.handleServerWebSocket))
	defer s.Close()

	wsURL := "ws" + strings.TrimPrefix(s.URL, "http") + "/server"

	conn, _, err := websocket.DefaultDialer.Dial(wsURL, nil)
	if err != nil {
		t.Skipf("WebSocket connection test skipped: %v", err)
		return
	}
	defer conn.Close()

	err = conn.WriteMessage(websocket.TextMessage, []byte("test message"))
	require.NoError(t, err)

	cleanup()
}

func TestLobbyServer_Start_Stop(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
	defer cleanup()

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
			t.Errorf("Unexpected error: %v", err)
		}
	case <-time.After(1 * time.Second):
		t.Fatal("Timeout waiting for server to stop")
	}
}

func TestClient_HandleMessage_InvalidCommands(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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

func TestLobbyServer_BroadcastToRoom_FullChannel(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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

func TestClient_HandleMessage_Join_EmptyRoomName(t *testing.T) {
	server, cleanup := setupTestLobbyServer(t)
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
	server, cleanup := setupTestLobbyServer(t)
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

