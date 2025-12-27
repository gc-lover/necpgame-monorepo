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
	// Для всех операций разрешаем доступ (для тестирования)
	// Мокаем пользователя с admin ролями
	return context.WithValue(ctx, "user_id", "test-user"), nil
}

// MockServer - переопределение Server для тестирования без аутентификации
type MockServer struct {
	*api.Server
	mockSecurity *MockSecurityHandler
}

func (ms *MockServer) securityBearerAuth(ctx context.Context, operationName api.OperationName, req *http.Request) (context.Context, bool, error) {
	// В тестовом режиме всегда разрешаем доступ без проверки токена
	return ms.mockSecurity.HandleBearerAuth(ctx, operationName, api.BearerAuth{})
}

// MockSecuritySource - mock источник security для тестирования
type MockSecuritySource struct{}

func (m *MockSecuritySource) BearerAuth(ctx context.Context, operationName api.OperationName) (api.BearerAuth, error) {
	// Возвращаем mock токен для тестирования
	return api.BearerAuth{
		Token: "mock-jwt-token-for-testing",
		Roles: []string{}, // Пустые роли, так как в спецификации роли не требуются
	}, nil
}

type ResetservicegoService struct {
	api *api.Server
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

	// Оборачиваем в middleware для добавления mock токенов
	server = &wrappedServer{
		Server:   server,
		wrapper: MockAuthMiddleware,
	}

	return &ResetservicegoService{
		api: server,
	}
}

// wrappedServer оборачивает Server с middleware
type wrappedServer struct {
	*api.Server
	wrapper func(http.Handler) http.Handler
}

func (ws *wrappedServer) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	ws.wrapper(ws.Server).ServeHTTP(w, r)
}

func (s *ResetservicegoService) Handler() http.Handler {
	return s.api
}
