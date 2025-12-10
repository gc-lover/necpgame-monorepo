package api

import (
	"context"
	"testing"
)

type noopHandler struct{}

func (noopHandler) GetPosition(ctx context.Context, params GetPositionParams) (GetPositionRes, error) {
	return &CharacterPosition{}, nil
}

func (noopHandler) GetPositionHistory(ctx context.Context, params GetPositionHistoryParams) (GetPositionHistoryRes, error) {
	return &GetPositionHistoryOKApplicationJSON{}, nil
}

func (noopHandler) SavePosition(ctx context.Context, req *SavePositionRequest, params SavePositionParams) (SavePositionRes, error) {
	return &CharacterPosition{}, nil
}

type noopSecurity struct{}

func (noopSecurity) HandleBearerAuth(ctx context.Context, operationName OperationName, t BearerAuth) (context.Context, error) {
	return ctx, nil
}

func TestNewServerNoop(t *testing.T) {
	srv, err := NewServer(noopHandler{}, noopSecurity{})
	if err != nil {
		t.Fatalf("expected no error, got %v", err)
	}
	if srv == nil {
		t.Fatal("expected server instance")
	}
}
