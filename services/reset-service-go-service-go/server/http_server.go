// Issue: #backend-reset_service_go

package server

import (
	"context"
	"net/http"

	"reset-service-go-service-go/pkg/api"
)

// MockSecurityHandler - простой mock security handler для тестирования
type MockSecurityHandler struct{}

// Реализуем SecurityHandler интерфейс
func (m *MockSecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Для тестирования всегда разрешаем доступ
	return context.WithValue(ctx, "user_id", "test-user"), nil
}

// TestWrapper оборачивает http.Handler для добавления mock токенов
type TestWrapper struct {
	handler http.Handler
}

func (tw *TestWrapper) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	// Добавляем mock Authorization header для тестирования
	if r.Header.Get("Authorization") == "" {
		r.Header.Set("Authorization", "Bearer mock-jwt-token-for-testing")
	}
	tw.handler.ServeHTTP(w, r)
}

// TestServer оборачивает api.Server для тестирования с mock аутентификацией
type TestServer struct {
	server *api.Server
}

func (ts *TestServer) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	// Добавляем mock Authorization header для тестирования
	if r.Header.Get("Authorization") == "" {
		r.Header.Set("Authorization", "Bearer mock-jwt-token-for-testing")
	}
	ts.server.ServeHTTP(w, r)
}



type ResetservicegoService struct {
	api http.Handler
}

// MockAuthMiddleware добавляет mock Bearer токен для тестирования
func MockAuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Добавляем mock Authorization header для тестирования
		if r.Header.Get("Authorization") == "" {
			r.Header.Set("Authorization", "Bearer mock-jwt-token-for-testing")
		}
		next.ServeHTTP(w, r)
	})
}

func NewResetservicegoService() *ResetservicegoService {
	mockSecurity := &MockSecurityHandler{}

	// Создаем handler с mock security
	handler := NewHandler()
	server, err := api.NewServer(handler, mockSecurity)
	if err != nil {
		panic(err) // In production, handle this properly
	}

	// Оборачиваем в TestWrapper для добавления mock токенов
	wrapper := &TestWrapper{handler: server}

	return &ResetservicegoService{
		api: wrapper,
	}
}

func (s *ResetservicegoService) Handler() http.Handler {
	return s.api
}
