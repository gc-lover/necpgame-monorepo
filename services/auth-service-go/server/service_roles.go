// Issue: #136 - Role and permission management operations
package server

import (
	"context"
	"time"

	"go.uber.org/zap"

	"necpgame/services/auth-service-go/pkg/api"
)

// GetUserRoles получает роли пользователя
func (s *AuthService) GetUserRoles(ctx context.Context, userID string) (*api.UserRolesResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	roles, err := s.getUserRoles(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to get user roles", zap.Error(err))
		return nil, err
	}

	return &api.UserRolesResponse{
		UserID: userID,
		Roles:  roles,
	}, nil
}

// AssignRole назначает роль пользователю
func (s *AuthService) AssignRole(ctx context.Context, req *api.AssignRoleRequest) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Check if role exists
	if !s.roleExists(req.Role) {
		return &ValidationError{Field: "role", Message: "invalid role"}
	}

	if err := s.assignRole(ctx, req.UserId, req.Role); err != nil {
		s.logger.Error("Failed to assign role", zap.Error(err))
		return err
	}

	s.logger.Info("Role assigned",
		zap.String("user_id", req.UserId.String()),
		zap.String("role", req.Role))
	return nil
}

// RevokeRole отзывает роль у пользователя
func (s *AuthService) RevokeRole(ctx context.Context, req *api.RevokeRoleRequest) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if err := s.revokeRole(ctx, req.UserId, req.Role); err != nil {
		s.logger.Error("Failed to revoke role", zap.Error(err))
		return err
	}

	s.logger.Info("Role revoked",
		zap.String("user_id", req.UserId.String()),
		zap.String("role", req.Role))
	return nil
}

// CheckPermission проверяет разрешение пользователя
func (s *AuthService) CheckPermission(ctx context.Context, req *api.CheckPermissionRequest) (*api.CheckPermissionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	roles, err := s.getUserRoles(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user roles", zap.Error(err))
		return nil, err
	}

	hasPermission := false
	for _, role := range roles {
		if s.roleHasPermission(role, req.Permission) {
			hasPermission = true
			break
		}
	}

	return &api.CheckPermissionResponse{
		UserId:        req.UserId,
		Permission:    req.Permission,
		HasPermission: hasPermission,
	}, nil
}

// GetAllRoles получает список всех доступных ролей
func (s *AuthService) GetAllRoles(ctx context.Context, req *api.GetAllRolesRequest) (*api.UserRolesResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	roles := s.getAllAvailableRoles()
	return &UserRolesResponse{
		Roles: roles,
	}, nil
}
