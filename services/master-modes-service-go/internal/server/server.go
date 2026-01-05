package server

import (
	"net/http"

	"github.com/go-faster/errors"
	"go.uber.org/zap"

	"master-modes-service-go/internal/service"
	"master-modes-service-go/pkg/api"
)

// Server представляет HTTP сервер для Master Modes Service
type Server struct {
	handler *service.Handler
	logger  *zap.Logger
}

// NewServer создает новый HTTP сервер
func NewServer(svc *service.Service, logger *zap.Logger) (*Server, error) {
	handler := service.NewHandler(svc, logger)

	return &Server{
		handler: handler,
		logger:  logger,
	}, nil
}

// Handler возвращает HTTP handler для сервера
func (s *Server) Handler() http.Handler {
	// Создаем OpenAPI сервер
	srv, err := api.NewServer(s.handler)
	if err != nil {
		s.logger.Fatal("Failed to create API server", zap.Error(err))
	}

	return srv
}

// Close закрывает сервер и освобождает ресурсы
func (s *Server) Close() error {
	// В реальной реализации здесь может быть cleanup
	s.logger.Info("Server closed")
	return nil
}
