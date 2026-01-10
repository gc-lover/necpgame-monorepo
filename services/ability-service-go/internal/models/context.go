package models

import (
	"context"
	"github.com/google/uuid"
)

// Context keys for storing user information
type contextKey string

const (
	UserIDKey   contextKey = "user_id"
	UserTypeKey contextKey = "user_type"
)

// UserContext represents user information stored in context
type UserContext struct {
	UserID   uuid.UUID
	UserType string // "player" for abilities
}

// GetUserFromContext extracts user information from context
func GetUserFromContext(ctx context.Context) (*UserContext, bool) {
	userIDVal := ctx.Value(UserIDKey)
	userTypeVal := ctx.Value(UserTypeKey)

	if userIDVal == nil || userTypeVal == nil {
		return nil, false
	}

	userID, ok := userIDVal.(uuid.UUID)
	if !ok {
		return nil, false
	}

	userType, ok := userTypeVal.(string)
	if !ok {
		return nil, false
	}

	return &UserContext{
		UserID:   userID,
		UserType: userType,
	}, true
}

// SetUserInContext stores user information in context
func SetUserInContext(ctx context.Context, userID uuid.UUID, userType string) context.Context {
	ctx = context.WithValue(ctx, UserIDKey, userID)
	ctx = context.WithValue(ctx, UserTypeKey, userType)
	return ctx
}