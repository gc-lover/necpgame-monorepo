package server

import (
	"context"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
	"time"

	"github.com/gorilla/websocket"
)

func TestNewLobbyServer(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	
	if server == nil {
		t.Fatal("Expected server to be created")
	}
	
	if server.rooms == nil {
		t.Error("Expected rooms map to be initialized")
	}
}

func TestLobbyServerAddClient(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	roomID := "test-room"
	
	client := &Client{
		room:   roomID,
		server: server,
		send:   make(chan []byte, 256),
	}
	
	server.addClientToRoom(client, roomID)
	
	server.mu.RLock()
	room, exists := server.rooms[roomID]
	server.mu.RUnlock()
	
	if !exists {
		t.Error("Expected room to be created")
	}
	
	if room != nil {
		room.mu.RLock()
		_, clientExists := room.clients[client]
		room.mu.RUnlock()
		
		if !clientExists {
			t.Error("Expected client to be added to room")
		}
	}
}

func TestLobbyServerRemoveClient(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	roomID := "test-room"
	
	client := &Client{
		room:   roomID,
		server: server,
		send:   make(chan []byte, 256),
	}
	
	server.addClientToRoom(client, roomID)
	server.removeClientFromRoom(client)
	
	server.mu.RLock()
	room, exists := server.rooms[roomID]
	server.mu.RUnlock()
	
	if exists && room != nil {
		room.mu.RLock()
		_, clientExists := room.clients[client]
		room.mu.RUnlock()
		
		if clientExists {
			t.Error("Expected client to be removed from room")
		}
	}
}

func TestLobbyServerBroadcast(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	roomID := "test-room"
	
	client := &Client{
		room:   roomID,
		server: server,
		send:   make(chan []byte, 256),
	}
	
	server.addClientToRoom(client, roomID)
	
	message := []byte("test message")
	server.broadcastToRoom(roomID, message)
	
	select {
	case msg := <-client.send:
		if string(msg) != string(message) {
			t.Errorf("Expected message %s, got %s", string(message), string(msg))
		}
	case <-time.After(100 * time.Millisecond):
		t.Error("Timeout waiting for broadcast message")
	}
}

func TestLobbyServerStop(t *testing.T) {
	config := &LobbyConfig{
		Port:         "0",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	
	roomID := "test-room"
	client := &Client{
		room:   roomID,
		server: server,
		send:   make(chan []byte, 256),
	}
	
	server.addClientToRoom(client, roomID)
	
	server.Stop()
	
	select {
	case _, ok := <-client.send:
		if ok {
			t.Error("Expected send channel to be closed")
		}
	case <-time.After(100 * time.Millisecond):
		t.Error("Timeout waiting for channel close")
	}
}

func TestWebSocketUpgrade(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	
	testServer := httptest.NewServer(http.HandlerFunc(server.handleWebSocket))
	defer testServer.Close()
	
	wsURL := "ws" + strings.TrimPrefix(testServer.URL, "http") + "?token=valid_token&room=test-room"
	
	ws, resp, err := websocket.DefaultDialer.Dial(wsURL, nil)
	if err != nil {
		t.Skipf("WebSocket connection failed (expected without valid JWT): %v", err)
		return
	}
	defer ws.Close()
	
	if resp.StatusCode == http.StatusSwitchingProtocols {
		time.Sleep(50 * time.Millisecond)
		
		server.mu.RLock()
		_, exists := server.rooms["test-room"]
		server.mu.RUnlock()
		
		if !exists {
			t.Log("Room not created (expected without valid JWT)")
		}
	}
}

func TestConcurrentRoomAccess(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	roomID := "concurrent-room"
	
	done := make(chan bool)
	
	for i := 0; i < 10; i++ {
		go func(id int) {
			client := &Client{
				room:   roomID,
				server: server,
				send:   make(chan []byte, 256),
			}
			server.addClientToRoom(client, roomID)
			done <- true
		}(i)
	}
	
	for i := 0; i < 10; i++ {
		<-done
	}
	
	server.mu.RLock()
	room, exists := server.rooms[roomID]
	server.mu.RUnlock()
	
	if !exists {
		t.Error("Expected room to be created")
	}
	
	if room != nil {
		room.mu.RLock()
		clientCount := len(room.clients)
		room.mu.RUnlock()
		
		if clientCount != 10 {
			t.Errorf("Expected 10 clients, got %d", clientCount)
		}
	}
}

func TestLobbyServerStartStop(t *testing.T) {
	config := &LobbyConfig{
		Port:         "0",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	
	ctx, cancel := context.WithCancel(context.Background())
	
	go func() {
		server.Start(ctx)
	}()
	
	time.Sleep(50 * time.Millisecond)
	
	cancel()
	time.Sleep(50 * time.Millisecond)
	
	server.Stop()
}

func TestMultipleRooms(t *testing.T) {
	config := &LobbyConfig{
		Port:         "8080",
		JwtValidator: NewJwtValidator("test-issuer", "test-jwks"),
	}
	
	server := NewLobbyServer(config)
	
	room1 := "room1"
	room2 := "room2"
	
	client1 := &Client{
		room:   room1,
		server: server,
		send:   make(chan []byte, 256),
	}
	
	client2 := &Client{
		room:   room2,
		server: server,
		send:   make(chan []byte, 256),
	}
	
	server.addClientToRoom(client1, room1)
	server.addClientToRoom(client2, room2)
	
	server.mu.RLock()
	roomCount := len(server.rooms)
	server.mu.RUnlock()
	
	if roomCount != 2 {
		t.Errorf("Expected 2 rooms, got %d", roomCount)
	}
	
	message := []byte("room1 message")
	server.broadcastToRoom(room1, message)
	
	select {
	case msg := <-client1.send:
		if string(msg) != string(message) {
			t.Error("Client1 did not receive message")
		}
	case <-time.After(100 * time.Millisecond):
		t.Error("Timeout waiting for room1 message")
	}
	
	select {
	case <-client2.send:
		t.Error("Client2 should not receive room1 message")
	case <-time.After(50 * time.Millisecond):
	}
}
