// Issue: #136 - User management operations
package server

import (
	"context"
	"time"

	"go.uber.org/zap"
	"golang.org/x/crypto/bcrypt"

	"necpgame/services/auth-service-go/pkg/api"
)

// GetUserInfo получает информацию о пользователе
func (s *AuthService) GetUserInfo(ctx context.Context, req *api.GetUserRequest) (*api.UserInfo, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	user, err := s.getUserByID(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user", zap.Error(err))
		return nil, err
	}

	if user == nil {
		return nil, &NotFoundError{Message: "user not found"}
	}

	roles, err := s.getUserRoles(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user roles", zap.Error(err))
		return nil, err
	}

	return &api.UserInfo{
		Id:        user.ID,
		Email:     user.Email,
		Username:  user.Username,
		Status:    user.Status,
		Roles:     roles,
		CreatedAt: user.CreatedAt,
		UpdatedAt: user.UpdatedAt,
	}, nil
}

// UpdateUser обновляет информацию о пользователе
func (s *AuthService) UpdateUser(ctx context.Context, req *api.UpdateUserRequest) (*api.UserInfo, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	user, err := s.getUserByID(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user", zap.Error(err))
		return nil, err
	}

	if user == nil {
		return nil, &NotFoundError{Message: "user not found"}
	}

	// Update fields
	if req.Email != nil {
		user.Email = *req.Email
	}
	if req.Username != nil {
		user.Username = *req.Username
	}

	user.UpdatedAt = time.Now()

	if err := s.updateUser(ctx, user); err != nil {
		s.logger.Error("Failed to update user", zap.Error(err))
		return nil, err
	}

	roles, err := s.getUserRoles(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user roles", zap.Error(err))
		return nil, err
	}

	return &api.UserInfo{
		Id:        user.ID,
		Email:     user.Email,
		Username:  user.Username,
		Status:    user.Status,
		Roles:     roles,
		CreatedAt: user.CreatedAt,
		UpdatedAt: user.UpdatedAt,
	}, nil
}

// DeleteUser удаляет пользователя
func (s *AuthService) DeleteUser(ctx context.Context, req *api.DeleteUserRequest) error {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	// Check if user exists
	user, err := s.getUserByID(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user", zap.Error(err))
		return err
	}

	if user == nil {
		return &NotFoundError{Message: "user not found"}
	}

	// Delete user
	if err := s.deleteUser(ctx, req.UserId); err != nil {
		s.logger.Error("Failed to delete user", zap.Error(err))
		return err
	}

	// Invalidate all tokens
	if err := s.invalidateAllUserTokens(ctx, req.UserId); err != nil {
		s.logger.Error("Failed to invalidate user tokens", zap.Error(err))
		// Don't fail deletion for token invalidation failure
	}

	s.logger.Info("User deleted", zap.String("user_id", req.UserId.String()))
	return nil
}

// ChangePassword изменяет пароль пользователя
func (s *AuthService) ChangePassword(ctx context.Context, req *api.ChangePasswordRequest) error {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	user, err := s.getUserByID(ctx, req.UserId)
	if err != nil {
		s.logger.Error("Failed to get user", zap.Error(err))
		return err
	}

	if user == nil {
		return &NotFoundError{Message: "user not found"}
	}

	// Verify old password
	if err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(req.OldPassword)); err != nil {
		return &AuthenticationError{Message: "invalid old password"}
	}

	// Hash new password
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.NewPassword), bcrypt.DefaultCost)
	if err != nil {
		s.logger.Error("Failed to hash new password", zap.Error(err))
		return err
	}

	// Update password
	if err := s.updateUserPassword(ctx, req.UserId, string(hashedPassword)); err != nil {
		s.logger.Error("Failed to update password", zap.Error(err))
		return err
	}

	// Invalidate all existing tokens
	if err := s.invalidateAllUserTokens(ctx, req.UserId); err != nil {
		s.logger.Error("Failed to invalidate user tokens", zap.Error(err))
		// Don't fail password change for token invalidation failure
	}

	s.logger.Info("Password changed", zap.String("user_id", req.UserId.String()))
	return nil
}
