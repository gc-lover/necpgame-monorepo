package server

import (
	"context"
	"net/http"
	"sync"
	"time"

	"github.com/gorilla/websocket"
)

type WebSocketServer struct {
	addr     string
	upgrader websocket.Upgrader
	handler  WebSocketConnectionHandler
	httpSrv  *http.Server
	mu       sync.RWMutex
}

type WebSocketConnectionHandler interface {
	HandleConnection(ctx context.Context, conn *websocket.Conn) error
}

func NewWebSocketServer(addr string, handler WebSocketConnectionHandler) *WebSocketServer {
	return &WebSocketServer{
		addr: addr,
		upgrader: websocket.Upgrader{
			CheckOrigin: func(r *http.Request) bool {
				return true
			},
			ReadBufferSize:  32 * 1024,
			WriteBufferSize: 32 * 1024,
		},
		handler: handler,
	}
}

func (s *WebSocketServer) loggingMiddleware(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		logger := GetLogger()
		logger.WithFields(map[string]interface{}{
			"method":      r.Method,
			"path":        r.URL.Path,
			"query":       r.URL.RawQuery,
			"remote_addr": r.RemoteAddr,
			"headers":     r.Header,
		}).Info("HTTP request received")
		next(w, r)
	}
}

func (s *WebSocketServer) Start(ctx context.Context) error {
	mux := http.NewServeMux()
	mux.HandleFunc("/ws", s.loggingMiddleware(s.handleWebSocket))
	mux.HandleFunc("/server", s.loggingMiddleware(s.handleServerWebSocket))
	mux.HandleFunc("/session/heartbeat", s.loggingMiddleware(s.handleHeartbeat))
	mux.HandleFunc("/session/reconnect", s.loggingMiddleware(s.handleReconnect))
	mux.HandleFunc("/", s.loggingMiddleware(s.handleRoot))

	s.httpSrv = &http.Server{
		Addr:         s.addr,
		Handler:      mux,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
	}

	logger := GetLogger()
	logger.WithField("addr", s.addr).Info("WebSocket Realtime Gateway listening")

	go func() {
		<-ctx.Done()
		logger.Info("Shutting down WebSocket server...")
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		s.httpSrv.Shutdown(shutdownCtx)
	}()

	return s.httpSrv.ListenAndServe()
}

func (s *WebSocketServer) Stop() error {
	if s.httpSrv != nil {
		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		return s.httpSrv.Shutdown(ctx)
	}
	return nil
}

func (s *WebSocketServer) handleRoot(w http.ResponseWriter, r *http.Request) {
	logger := GetLogger()
	logger.WithFields(map[string]interface{}{
		"path":        r.URL.Path,
		"query":       r.URL.RawQuery,
		"remote_addr": r.RemoteAddr,
		"method":      r.Method,
		"headers":     r.Header,
	}).Info("handleRoot: WebSocket connection attempt")
	
	if r.URL.Path == "/server" {
		logger.Error("handleRoot: Path is /server but should be handled by handleServerWebSocket directly - routing issue detected!")
		s.handleServerWebSocket(w, r)
		return
	}
	
	if r.URL.Path == "/ws" {
		logger.Error("handleRoot: Path is /ws but should be handled by handleWebSocket directly - routing issue detected!")
		s.handleWebSocket(w, r)
		return
	}
	
	serverType := r.URL.Query().Get("type")
	if serverType == "server" {
		logger.Info("handleRoot: Connection with type=server query parameter, routing to server handler")
		s.handleServerWebSocket(w, r)
		return
	}
	
	token := r.URL.Query().Get("token")
	if token == "" {
		token = r.Header.Get("X-Auth-Token")
	}
	
	if token == "" {
		logger.WithField("path", r.URL.Path).Info("handleRoot: Connection without token in URL or headers, treating as client connection (token will be extracted from first message)")
		s.handleWebSocket(w, r)
		return
	}
	
	logger.WithFields(map[string]interface{}{
		"path":  r.URL.Path,
		"token": token,
	}).Info("handleRoot: Connection with token detected, routing to client handler")
	s.handleWebSocket(w, r)
}

func (s *WebSocketServer) handleWebSocket(w http.ResponseWriter, r *http.Request) {
	logger := GetLogger()
	logger.WithFields(map[string]interface{}{
		"path":        r.URL.Path,
		"query":       r.URL.RawQuery,
		"remote_addr": r.RemoteAddr,
		"method":      r.Method,
		"headers":     r.Header,
	}).Info("handleWebSocket: Client WebSocket connection attempt")
	
	if r.URL.Path == "/server" {
		logger.Error("handleWebSocket: Server path /server detected in client handler - routing error! Redirecting to handleServerWebSocket")
		s.handleServerWebSocket(w, r)
		return
	}
	
	token := r.URL.Query().Get("token")
	if token == "" {
		logger.Warn("handleWebSocket: Connection without token, allowing for testing (using default token)")
		token = "default"
	} else {
		logger.WithField("token", token).Info("handleWebSocket: Client connection with token")
	}

	conn, err := s.upgrader.Upgrade(w, r, nil)
	if err != nil {
		logger.WithError(err).WithFields(map[string]interface{}{
			"path":        r.URL.Path,
			"query":       r.URL.RawQuery,
			"remote_addr": r.RemoteAddr,
		}).Error("handleWebSocket: Failed to upgrade connection to WebSocket")
		RecordError("upgrade_failed")
		return
	}

	logger.WithFields(map[string]interface{}{
		"remote_addr": conn.RemoteAddr().String(),
		"token":       token,
		"path":        r.URL.Path,
	}).Info("handleWebSocket: Client WebSocket connection upgraded successfully, starting handler")
	RecordConnection("opened")
	
	ctx := context.Background()
	go func() {
		defer func() {
			logger.WithField("remote_addr", conn.RemoteAddr().String()).Info("WebSocket connection handler finished, closing connection")
			RecordConnection("closed")
			conn.Close()
		}()
		
		if err := s.handler.HandleConnection(ctx, conn); err != nil {
			logger.WithError(err).WithField("remote_addr", conn.RemoteAddr().String()).Error("Connection handler error")
			RecordError("handle_connection")
		}
	}()
}

func (s *WebSocketServer) handleServerWebSocket(w http.ResponseWriter, r *http.Request) {
	logger := GetLogger()
	logger.WithFields(map[string]interface{}{
		"path":        r.URL.Path,
		"query":       r.URL.RawQuery,
		"remote_addr": r.RemoteAddr,
		"method":      r.Method,
	}).Info("handleServerWebSocket: Dedicated Server WebSocket connection attempt")

	if r.URL.Path != "/server" && r.URL.Path != "/" {
		logger.WithField("path", r.URL.Path).Warn("handleServerWebSocket: Unexpected path, but allowing connection (WebSocket module may not transmit path)")
	}

	conn, err := s.upgrader.Upgrade(w, r, nil)
	if err != nil {
		logger.WithError(err).WithFields(map[string]interface{}{
			"path":        r.URL.Path,
			"query":       r.URL.RawQuery,
			"remote_addr": r.RemoteAddr,
		}).Error("handleServerWebSocket: Failed to upgrade Dedicated Server connection")
		RecordError("server_upgrade_failed")
		return
	}

	logger.WithField("remote_addr", conn.RemoteAddr().String()).Info("handleServerWebSocket: Dedicated Server connected successfully to /server endpoint")
	RecordConnection("server_opened")
	
	if handler, ok := s.handler.(*GatewayHandler); ok {
		handler.SetServerConnection(conn)
		logger.Info("handleServerWebSocket: Server connection registered in GatewayHandler")
	} else {
		logger.Error("handleServerWebSocket: Handler is not GatewayHandler, cannot register server connection")
	}
	
	ctx := context.Background()
	go func() {
		defer func() {
			RecordConnection("server_closed")
			if handler, ok := s.handler.(*GatewayHandler); ok {
				handler.SetServerConnection(nil)
			}
			conn.Close()
			logger.Info("Dedicated Server disconnected from /server endpoint")
		}()
		
		for {
			select {
			case <-ctx.Done():
				return
			default:
				messageType, data, err := conn.ReadMessage()
				if err != nil {
					if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
						logger.WithError(err).Error("Server WebSocket read error")
					}
					return
				}
				
				if messageType == websocket.BinaryMessage {
					RecordGameStateReceived()
					logger.WithFields(map[string]interface{}{
						"data_len": len(data),
						"source":   "dedicated_server",
					}).Info("handleServerWebSocket: Received GameState from Dedicated Server")
					
					if handler, ok := s.handler.(*GatewayHandler); ok {
						handler.BroadcastToClients(data)
						logger.WithField("data_len", len(data)).Info("handleServerWebSocket: Broadcasted GameState to all clients")
					} else {
						logger.Error("handleServerWebSocket: Handler is not GatewayHandler, cannot broadcast")
					}
				} else {
					logger.WithField("message_type", messageType).Warn("handleServerWebSocket: Received non-binary message from server, ignoring")
				}
			}
		}
	}()
}

