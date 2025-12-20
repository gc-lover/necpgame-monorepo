// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// mockRepository is a minimal mock for benchmark tests
type mockRepository struct{}

func (m *mockRepository) CreateEvent(_ context.Context, _ *WorldEvent) error {
	return nil
}

func (m *mockRepository) GetEvent(_ context.Context, _ uuid.UUID) (*WorldEvent, error) {
	return nil, nil
}

func (m *mockRepository) GetEventByID(_ context.Context, _ uuid.UUID) (*WorldEvent, error) {
	return nil, nil
}

func (m *mockRepository) UpdateEvent(_ context.Context, _ *WorldEvent) error {
	return nil
}

func (m *mockRepository) DeleteEvent(_ context.Context, _ uuid.UUID) error {
	return nil
}

func (m *mockRepository) ListEvents(_ context.Context, _ EventFilter) ([]*WorldEvent, int, error) {
	return []*WorldEvent{}, 0, nil
}

func (m *mockRepository) GetActiveEvents(_ context.Context) ([]*WorldEvent, error) {
	return []*WorldEvent{}, nil
}

func (m *mockRepository) GetPlannedEvents(_ context.Context) ([]*WorldEvent, error) {
	return []*WorldEvent{}, nil
}

func (m *mockRepository) RecordActivation(_ context.Context, _ *EventActivation) error {
	return nil
}

func (m *mockRepository) RecordAnnouncement(_ context.Context, _ *EventAnnouncement) error {
	return nil
}

// BenchmarkCreateWorldEvent benchmarks CreateWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateWorldEvent(b *testing.B) {
	logger := zap.NewExample()
	defer logger.Sync()
	service := NewService(nil, nil, nil, logger)
	handlers := NewHandlers(service, logger)

	ctx := context.Background()
	req := &api.CreateWorldEventRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateWorldEvent(ctx, req)
	}
}

// BenchmarkGetWorldEvent benchmarks GetWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetWorldEvent(b *testing.B) {
	logger := zap.NewExample()
	defer logger.Sync()
	service := NewService(nil, nil, nil, logger)
	handlers := NewHandlers(service, logger)

	ctx := context.Background()
	params := api.GetWorldEventParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetWorldEvent(ctx, params)
	}
}

// BenchmarkUpdateWorldEvent benchmarks UpdateWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkUpdateWorldEvent(b *testing.B) {
	logger := zap.NewExample()
	defer logger.Sync()
	// Create mock repository to avoid nil pointer dereference
	mockRepo := &mockRepository{}
	service := NewService(mockRepo, nil, nil, logger)
	handlers := NewHandlers(service, logger)

	ctx := context.Background()
	req := &api.UpdateWorldEventRequest{
		// TODO: Fill request fields based on API spec
	}
	params := api.UpdateWorldEventParams{
		ID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.UpdateWorldEvent(ctx, req, params)
	}
}
