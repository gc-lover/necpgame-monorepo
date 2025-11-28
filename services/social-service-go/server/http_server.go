package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/pkg/api/chat"
	"github.com/necpgame/social-service-go/pkg/api/friends"
	"github.com/necpgame/social-service-go/pkg/api/guilds"
	"github.com/necpgame/social-service-go/pkg/api/mail"
	"github.com/necpgame/social-service-go/pkg/api/notifications"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr                  string
	router                *mux.Router
	server                *http.Server
	logger                *logrus.Logger
	friendsService        FriendsServiceInterface
	guildsService         GuildsServiceInterface
	chatService           ChatServiceInterface
	mailService           MailServiceInterface
	notificationsService  NotificationsServiceInterface
}

type ServerConfig struct {
	Addr                 string
	FriendsService       FriendsServiceInterface
	GuildsService        GuildsServiceInterface
	ChatService          ChatServiceInterface
	MailService          MailServiceInterface
	NotificationsService NotificationsServiceInterface
}

func NewHTTPServer(config *ServerConfig) *HTTPServer {
	r := mux.NewRouter()
	
	s := &HTTPServer{
		addr:                 config.Addr,
		router:               r,
		logger:               GetLogger(),
		friendsService:       config.FriendsService,
		guildsService:        config.GuildsService,
		chatService:          config.ChatService,
		mailService:          config.MailService,
		notificationsService: config.NotificationsService,
	}
	
	r.Use(s.loggingMiddleware)
	r.Use(s.metricsMiddleware)
	r.Use(s.corsMiddleware)
	
	friends.HandlerFromMux(NewFriendsHandlers(config.FriendsService), r)
	guilds.HandlerFromMux(NewGuildsHandlers(config.GuildsService), r)
	chat.HandlerFromMux(NewChatHandlers(config.ChatService), r)
	mail.HandlerFromMux(NewMailHandlers(config.MailService), r)
	notifications.HandlerFromMux(NewNotificationsHandlers(config.NotificationsService), r)
	
	r.HandleFunc("/health", s.healthCheck).Methods("GET")
	r.Handle("/metrics", promhttp.Handler()).Methods("GET")

	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return s
}

func (s *HTTPServer) Start() error {
	s.logger.WithField("addr", s.addr).Info("Starting HTTP server")
	return s.server.ListenAndServe()
}

func (s *HTTPServer) Stop(ctx context.Context) error {
	s.logger.Info("Stopping HTTP server")
		return s.server.Shutdown(ctx)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": time.Since(start).Milliseconds(),
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}
		next.ServeHTTP(w, r)
	})
}
