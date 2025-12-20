package api

import (
	"context"
	"testing"
)

type noopHandler struct{}

func (noopHandler) GetPosition(_ context.Context, _ GetPositionParams) (GetPositionRes, error) {
	return &CharacterPosition{}, nil
}

func (noopHandler) GetPositionHistory(_ context.Context, _ GetPositionHistoryParams) (GetPositionHistoryRes, error) {
	return &GetPositionHistoryOKApplicationJSON{}, nil
}

func (noopHandler) SavePosition(_ context.Context, _ *SavePositionRequest, _ SavePositionParams) (SavePositionRes, error) {
	return &CharacterPosition{}, nil
}

type noopSecurity struct{}

func (noopSecurity) HandleBearerAuth(ctx context.Context, _ OperationName, _ BearerAuth) (context.Context, error) {
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
