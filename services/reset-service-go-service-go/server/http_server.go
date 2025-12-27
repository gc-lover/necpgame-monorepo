// Issue: #backend-reset_service_go

package server

import (
	"context"
	"net/http"

	"reset-service-go-service-go/pkg/api"
)

// MockSecurityHandler - простой mock security handler для тестирования
type MockSecurityHandler struct{}

func (m *MockSecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Для тестирования всегда разрешаем доступ
	return ctx, nil
}

type ResetservicegoService struct {
	api *api.Server
}

func NewResetservicegoService() *ResetservicegoService {
	mockSecurity := &MockSecurityHandler{}
	server, err := api.NewServer(NewHandler(), mockSecurity)
	if err != nil {
		panic(err) // In production, handle this properly
	}

	return &ResetservicegoService{
		api: server,
	}
}

func (s *ResetservicegoService) Handler() http.Handler {
	return s.api
}
