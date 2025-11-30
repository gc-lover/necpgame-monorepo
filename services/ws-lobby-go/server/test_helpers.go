// Issue: #104
package server

import (
	"testing"
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

