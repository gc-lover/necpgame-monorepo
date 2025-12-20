// Package server Issue: #136 - Authentication operations (login, register, logout)
// TODO: Implement authentication service methods when API types are properly generated
package server

import (
	"fmt"
	"necpgame/services/auth-service-go/pkg/api"
)

// Register регистрирует нового пользователя
func (s *AuthService) Register() (*api.RegisterResponse, error) {
	return nil, fmt.Errorf("not implemented")
}

// Login аутентифицирует пользователя
func (s *AuthService) Login() (*api.LoginResponse, error) {
	return nil, fmt.Errorf("not implemented")
}

// Logout выходит из системы
func (s *AuthService) Logout() error {
	return fmt.Errorf("not implemented")
}
