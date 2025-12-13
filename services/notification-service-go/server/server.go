// Issue: #140874394
package server

import (
	"context"
	"database/sql"
	"net/http"
	"os"
	"os/signal"
	"sync"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gorilla/websocket"
	"go.uber.org/zap"
)

// NotificationServer представляет HTTP сервер для системы уведомлений
type NotificationServer struct {
	server     *http.Server
	logger     *zap.Logger
	db         *sql.DB
	jwtSecret  []byte

	// WebSocket менеджер для real-time уведомлений
	wsManager   *WebSocketManager
	notificationService *NotificationService
	middleware *AuthMiddleware
}

// WebSocketManager управляет WebSocket соединениями для real-time уведомлений
type WebSocketManager struct {
	clients   map[string]*WebSocketClient // userID -> client
	broadcast chan *NotificationMessage
	mutex     sync.RWMutex
	upgrader  websocket.Upgrader
	logger    *zap.Logger
}

// WebSocketClient представляет WebSocket клиента
type WebSocketClient struct {
	userID string
	conn   *websocket.Conn
	send   chan *NotificationMessage
	logger *zap.Logger
}

// NotificationMessage представляет сообщение уведомления для WebSocket
type NotificationMessage struct {
	Type string      `json:"type"`
	Data interface{} `json:"data"`
}

// NewNotificationServer создает новый сервер уведомлений
func NewNotificationServer(logger *zap.Logger, db *sql.DB, jwtSecret string) *NotificationServer {
	// Создаем WebSocket менеджер
	wsManager := &WebSocketManager{
		clients:   make(map[string]*WebSocketClient),
		broadcast: make(chan *NotificationMessage, 100),
		upgrader: websocket.Upgrader{
			ReadBufferSize:  1024,
			WriteBufferSize: 1024,
			CheckOrigin: func(r *http.Request) bool {
				// В продакшене нужно проверять origin
				return true
			},
		},
		logger: logger,
	}

	// Запускаем обработчик broadcast сообщений
	go wsManager.handleBroadcast()

	// Создаем сервис уведомлений
	service := NewNotificationService(db, wsManager, logger)

	// Создаем middleware
	authMiddleware := NewAuthMiddleware(logger, jwtSecret)

	// Создаем Chi роутер с оптимизациями
	r := chi.NewRouter()

	// Performance middleware
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Security middleware
	r.Use(authMiddleware.SecurityHeadersMiddleware)
	r.Use(authMiddleware.CORSMiddleware)

	// Logging middleware
	r.Use(authMiddleware.LoggingMiddleware)

	// Recovery middleware
	r.Use(authMiddleware.RecoveryMiddleware)

	// Health check endpoints (не требуют аутентификации)
	r.Get("/health", service.HealthCheckHandler)
	r.Get("/ready", service.ReadinessCheckHandler)
	r.Get("/metrics", service.MetricsHandler)

	// API endpoints
	r.Route("/api/v1", func(r chi.Router) {
		// WebSocket endpoint для real-time уведомлений
		r.Get("/notifications/ws", wsManager.HandleWebSocket)

		// REST endpoints (требуют Bearer token)
		r.Group(func(r chi.Router) {
			r.Use(authMiddleware.JWTAuth)

			r.Get("/notifications", service.GetNotificationsHandler)
			r.Post("/notifications", service.CreateNotificationHandler)
			r.Get("/notifications/{notification_id}", service.GetNotificationHandler)
			r.Put("/notifications/{notification_id}", service.UpdateNotificationHandler)
			r.Post("/notifications/{notification_id}/read", service.MarkAsReadHandler)
			r.Post("/notifications/bulk-read", service.MarkBulkAsReadHandler)
		})
	})

	server := &http.Server{
		Addr:         ":8083",
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &NotificationServer{
		server:              server,
		logger:              logger,
		db:                  db,
		jwtSecret:           []byte(jwtSecret),
		wsManager:           wsManager,
		notificationService: service,
		middleware:          authMiddleware,
	}
}

// Start запускает HTTP сервер с graceful shutdown
func (s *NotificationServer) Start() error {
	s.logger.Info("Starting notification server", zap.String("addr", s.server.Addr))

	// Канал для graceful shutdown
	shutdown := make(chan os.Signal, 1)
	signal.Notify(shutdown, os.Interrupt, syscall.SIGTERM)

	// Запускаем сервер в горутине
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Ждем сигнала завершения
	<-shutdown
	s.logger.Info("Shutting down server...")

	// Graceful shutdown с таймаутом
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	// Закрываем WebSocket соединения
	s.wsManager.Shutdown()

	if err := s.server.Shutdown(ctx); err != nil {
		s.logger.Error("Server forced to shutdown", zap.Error(err))
		return err
	}

	s.logger.Info("Server shutdown complete")
	return nil
}

// handleBroadcast обрабатывает broadcast сообщения для WebSocket клиентов
func (wm *WebSocketManager) handleBroadcast() {
	for {
		select {
		case message, ok := <-wm.broadcast:
			if !ok {
				return // Канал закрыт
			}

			// Отправляем сообщение всем подключенным клиентам
			wm.mutex.RLock()
			for userID, client := range wm.clients {
				select {
				case client.send <- message:
					// Сообщение отправлено
				default:
					// Канал клиента заблокирован, закрываем соединение
					wm.logger.Warn("Client send channel blocked, removing client", zap.String("user_id", userID))
					wm.removeClient(userID)
				}
			}
			wm.mutex.RUnlock()
		}
	}
}

// HandleWebSocket обрабатывает WebSocket соединения
func (wm *WebSocketManager) HandleWebSocket(w http.ResponseWriter, r *http.Request) {
	// Получаем user ID из контекста (устанавливается middleware)
	userIDVal := r.Context().Value("user_id")
	if userIDVal == nil {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	userID, ok := userIDVal.(string)
	if !ok {
		http.Error(w, "Invalid user ID", http.StatusBadRequest)
		return
	}

	// Устанавливаем WebSocket соединение
	conn, err := wm.upgrader.Upgrade(w, r, nil)
	if err != nil {
		wm.logger.Error("Failed to upgrade connection", zap.Error(err))
		return
	}

	// Создаем клиента
	client := &WebSocketClient{
		userID: userID,
		conn:   conn,
		send:   make(chan *NotificationMessage, 256),
		logger: wm.logger,
	}

	// Добавляем клиента
	wm.addClient(userID, client)

	// Запускаем обработчики для клиента
	go client.writePump()
	go client.readPump()

	wm.logger.Info("WebSocket client connected", zap.String("user_id", userID))
}

// addClient добавляет WebSocket клиента
func (wm *WebSocketManager) addClient(userID string, client *WebSocketClient) {
	wm.mutex.Lock()
	defer wm.mutex.Unlock()

	// Если клиент уже существует, закрываем старое соединение
	if existingClient, exists := wm.clients[userID]; exists {
		existingClient.conn.Close()
		close(existingClient.send)
	}

	wm.clients[userID] = client
}

// removeClient удаляет WebSocket клиента
func (wm *WebSocketManager) removeClient(userID string) {
	wm.mutex.Lock()
	defer wm.mutex.Unlock()

	if client, exists := wm.clients[userID]; exists {
		client.conn.Close()
		close(client.send)
		delete(wm.clients, userID)
	}
}

// BroadcastNotification отправляет уведомление через WebSocket
func (wm *WebSocketManager) BroadcastNotification(userID string, notification *Notification) {
	message := &NotificationMessage{
		Type: "notification",
		Data: notification,
	}

	select {
	case wm.broadcast <- message:
		wm.logger.Info("Notification broadcasted", zap.String("user_id", userID), zap.String("notification_id", notification.ID))
	default:
		wm.logger.Warn("Broadcast channel full, dropping notification", zap.String("user_id", userID))
	}
}

// Shutdown закрывает все WebSocket соединения
func (wm *WebSocketManager) Shutdown() {
	wm.mutex.Lock()
	defer wm.mutex.Unlock()

	close(wm.broadcast)

	for userID, client := range wm.clients {
		client.conn.Close()
		close(client.send)
		wm.logger.Info("WebSocket client disconnected on shutdown", zap.String("user_id", userID))
	}

	wm.clients = nil
}

// writePump обрабатывает отправку сообщений клиенту
func (c *WebSocketClient) writePump() {
	ticker := time.NewTicker(54 * time.Second)
	defer func() {
		ticker.Stop()
		c.conn.Close()
	}()

	for {
		select {
		case message, ok := <-c.send:
			if !ok {
				c.conn.WriteMessage(websocket.CloseMessage, []byte{})
				return
			}

			if err := c.conn.WriteJSON(message); err != nil {
				c.logger.Error("Failed to write message to client",
					zap.String("user_id", c.userID),
					zap.Error(err))
				return
			}

		case <-ticker.C:
			// Отправляем ping для поддержания соединения
			if err := c.conn.WriteMessage(websocket.PingMessage, nil); err != nil {
				c.logger.Error("Failed to ping client",
					zap.String("user_id", c.userID),
					zap.Error(err))
				return
			}
		}
	}
}

// readPump обрабатывает получение сообщений от клиента (для future use)
func (c *WebSocketClient) readPump() {
	defer func() {
		c.conn.Close()
	}()

	c.conn.SetReadLimit(512)
	c.conn.SetReadDeadline(time.Now().Add(60 * time.Second))
	c.conn.SetPongHandler(func(string) error {
		c.conn.SetReadDeadline(time.Now().Add(60 * time.Second))
		return nil
	})

	for {
		_, _, err := c.conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				c.logger.Error("WebSocket error",
					zap.String("user_id", c.userID),
					zap.Error(err))
			}
			break
		}
	}
}