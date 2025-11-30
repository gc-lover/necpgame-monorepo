// Issue: #141888878, #141888890
package server

import (
	"context"
	"net/http"
	"sync"
	"time"

	"github.com/gorilla/websocket"
)

type LobbyServer struct {
	config   *LobbyConfig
	upgrader websocket.Upgrader
	rooms    map[string]*Room
	mu       sync.RWMutex
	httpSrv  *http.Server
}

type Room struct {
	clients map[*Client]bool
	mu      sync.RWMutex
}

type Client struct {
	conn   *websocket.Conn
	room   string
	server *LobbyServer
	send   chan []byte
	mu     sync.Mutex
}

func NewLobbyServer(config *LobbyConfig) *LobbyServer {
	return &LobbyServer{
		config: config,
		upgrader: websocket.Upgrader{
			CheckOrigin: func(r *http.Request) bool {
				return true
			},
		},
		rooms: make(map[string]*Room),
	}
}

func (s *LobbyServer) Start(ctx context.Context) error {
	mux := http.NewServeMux()
	mux.HandleFunc("/ws", s.handleWebSocket)
	mux.HandleFunc("/server", s.handleServerWebSocket)

	s.httpSrv = &http.Server{
		Addr:         ":" + s.config.Port,
		Handler:      mux,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
	}

	logger := GetLogger()
	logger.WithField("port", s.config.Port).Info("WebSocket Lobby listening")

	go func() {
		<-ctx.Done()
		logger := GetLogger()
		logger.Info("Shutting down HTTP server...")
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		s.httpSrv.Shutdown(shutdownCtx)
	}()

	return s.httpSrv.ListenAndServe()
}

func (s *LobbyServer) Stop() {
	if s.httpSrv != nil {
		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		s.httpSrv.Shutdown(ctx)
	}

	s.mu.Lock()
	defer s.mu.Unlock()

	for _, room := range s.rooms {
		room.mu.Lock()
		for client := range room.clients {
			close(client.send)
			if client.conn != nil {
				client.conn.Close()
			}
		}
		room.mu.Unlock()
	}
}

func (s *LobbyServer) handleWebSocket(w http.ResponseWriter, r *http.Request) {
	logger := GetLogger()
	token := r.URL.Query().Get("token")
	if !s.config.JwtValidator.Verify(token) {
		RecordWebSocketError("unauthorized")
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	conn, err := s.upgrader.Upgrade(w, r, nil)
	if err != nil {
		logger.WithError(err).Error("WebSocket upgrade error")
		RecordWebSocketError("upgrade")
		return
	}

	RecordWebSocketConnection("opened")
	client := &Client{
		conn:   conn,
		room:   "general",
		server: s,
		send:   make(chan []byte, 256),
	}

	s.addClientToRoom(client, "general")

	go client.writePump()
	go client.readPump()
}

func (s *LobbyServer) handleServerWebSocket(w http.ResponseWriter, r *http.Request) {
	logger := GetLogger()
	conn, err := s.upgrader.Upgrade(w, r, nil)
	if err != nil {
		logger.WithError(err).Error("Server WebSocket upgrade error")
		RecordWebSocketError("server_upgrade")
		return
	}

	logger.Info("Server WebSocket connected")
	defer func() {
		conn.Close()
		logger.Info("Server WebSocket disconnected")
	}()

	conn.SetReadDeadline(time.Now().Add(60 * time.Second))
	conn.SetPongHandler(func(string) error {
		conn.SetReadDeadline(time.Now().Add(60 * time.Second))
		return nil
	})

	for {
		messageType, message, err := conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				logger.WithError(err).Error("Server WebSocket read error")
				RecordWebSocketError("server_read")
			}
			break
		}

		if messageType == websocket.BinaryMessage || messageType == websocket.TextMessage {
			logger.WithField("size", len(message)).Info("Received GameState from server, broadcasting to clients")
			s.broadcastToRoom("general", message)
			RecordWebSocketMessage("gamestate", "broadcast")
		}
	}
}

func (s *LobbyServer) addClientToRoom(client *Client, roomName string) {
	s.mu.Lock()
	room, exists := s.rooms[roomName]
	if !exists {
		room = &Room{
			clients: make(map[*Client]bool),
		}
		s.rooms[roomName] = room
	}
	roomCount := len(s.rooms)
	s.mu.Unlock()

	room.mu.Lock()
	room.clients[client] = true
	room.mu.Unlock()

	client.room = roomName
	RecordWebSocketRoom(roomCount)
}

func (s *LobbyServer) removeClientFromRoom(client *Client) {
	client.mu.Lock()
	roomName := client.room
	client.mu.Unlock()

	s.mu.RLock()
	room, exists := s.rooms[roomName]
	s.mu.RUnlock()

	if !exists {
		return
	}

	room.mu.Lock()
	delete(room.clients, client)
	clientCount := len(room.clients)
	room.mu.Unlock()

	if clientCount == 0 {
		s.mu.Lock()
		delete(s.rooms, roomName)
		roomCount := len(s.rooms)
		s.mu.Unlock()
		RecordWebSocketRoom(roomCount)
	} else {
		s.mu.RLock()
		roomCount := len(s.rooms)
		s.mu.RUnlock()
		RecordWebSocketRoom(roomCount)
	}
}

func (s *LobbyServer) broadcastToRoom(roomName string, message []byte) {
	s.mu.RLock()
	room, exists := s.rooms[roomName]
	s.mu.RUnlock()

	if !exists {
		return
	}

	room.mu.RLock()
	for client := range room.clients {
		select {
		case client.send <- message:
		default:
			close(client.send)
			delete(room.clients, client)
		}
	}
	room.mu.RUnlock()
}

func (c *Client) readPump() {
	defer func() {
		c.server.removeClientFromRoom(c)
		c.conn.Close()
	}()

	c.conn.SetReadDeadline(time.Now().Add(60 * time.Second))
	c.conn.SetPongHandler(func(string) error {
		c.conn.SetReadDeadline(time.Now().Add(60 * time.Second))
		return nil
	})

	logger := GetLogger()
	for {
		_, message, err := c.conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				logger.WithError(err).Error("WebSocket error")
				RecordWebSocketError("read")
			}
			break
		}

		c.handleMessage(message)
	}
}

func (c *Client) writePump() {
	ticker := time.NewTicker(54 * time.Second)
	defer func() {
		ticker.Stop()
		c.conn.Close()
	}()

	for {
		select {
		case message, ok := <-c.send:
			c.conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if !ok {
				if err := c.conn.WriteMessage(websocket.CloseMessage, []byte{}); err != nil {
					logger := GetLogger()
					logger.WithError(err).Error("Failed to write WebSocket close message")
				}
				return
			}

			messageType := websocket.BinaryMessage
			if len(message) > 0 {
				isText := true
				for i := 0; i < len(message) && i < 100; i++ {
					if message[i] < 32 && message[i] != '\n' && message[i] != '\r' && message[i] != '\t' {
						isText = false
						break
					}
				}
				if isText {
					messageType = websocket.TextMessage
				}
			}

			w, err := c.conn.NextWriter(messageType)
			if err != nil {
				logger := GetLogger()
				logger.WithError(err).Error("Failed to get WebSocket writer")
				return
			}
			
			if _, err := w.Write(message); err != nil {
				logger := GetLogger()
				logger.WithError(err).Error("Failed to write WebSocket message")
				w.Close()
				return
			}

			n := len(c.send)
			for i := 0; i < n; i++ {
				nextMsg := <-c.send
				if messageType == websocket.TextMessage {
					if _, err := w.Write([]byte{'\n'}); err != nil {
						logger := GetLogger()
						logger.WithError(err).Error("Failed to write WebSocket newline")
						w.Close()
						return
					}
				}
				if _, err := w.Write(nextMsg); err != nil {
					logger := GetLogger()
					logger.WithError(err).Error("Failed to write WebSocket next message")
					w.Close()
					return
				}
			}

			if err := w.Close(); err != nil {
				logger := GetLogger()
				logger.WithError(err).Error("Failed to close WebSocket writer")
				return
			}
		case <-ticker.C:
			c.conn.SetWriteDeadline(time.Now().Add(10 * time.Second))
			if err := c.conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				logger := GetLogger()
				logger.WithError(err).Error("Failed to write WebSocket ping message")
				return
			}
		}
	}
}

func (c *Client) handleMessage(message []byte) {
	text := string(message)

	if len(text) >= 5 && text[:5] == "JOIN " {
		roomName := text[5:]
		c.server.removeClientFromRoom(c)
		c.server.addClientToRoom(c, roomName)
		c.send <- []byte("JOINED " + roomName)
		RecordWebSocketMessage("join", "success")
		return
	}

	if text == "LEAVE" {
		c.server.removeClientFromRoom(c)
		c.server.addClientToRoom(c, "general")
		c.send <- []byte("LEFT")
		RecordWebSocketMessage("leave", "success")
		return
	}

	if len(text) >= 4 && text[:4] == "MSG " {
		body := text[4:]
		roomMessage := "[" + c.room + "] " + body
		c.server.broadcastToRoom(c.room, []byte(roomMessage))
		RecordWebSocketMessage("msg", "success")
		return
	}

	c.server.broadcastToRoom(c.room, message)
	RecordWebSocketMessage("broadcast", "success")
}

