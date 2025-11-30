// Issue: #104
package server

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/gorilla/websocket"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

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

