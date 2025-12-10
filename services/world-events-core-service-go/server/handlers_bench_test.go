// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	api "github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
	"go.uber.org/zap"
)

// mockRepository is a minimal mock for benchmark tests
type mockRepository struct{}

func (m *mockRepository) CreateEvent(ctx context.Context, event *WorldEvent) error {
	return nil
}

func (m *mockRepository) GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	return nil, nil
}

func (m *mockRepository) GetEventByID(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	return nil, nil
}

func (m *mockRepository) UpdateEvent(ctx context.Context, event *WorldEvent) error {
	return nil
}

func (m *mockRepository) DeleteEvent(ctx context.Context, id uuid.UUID) error {
	return nil
}

func (m *mockRepository) ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error) {
	return []*WorldEvent{}, 0, nil
}

func (m *mockRepository) GetActiveEvents(ctx context.Context) ([]*WorldEvent, error) {
	return []*WorldEvent{}, nil
}

func (m *mockRepository) GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error) {
	return []*WorldEvent{}, nil
}

func (m *mockRepository) RecordActivation(ctx context.Context, activation *EventActivation) error {
	return nil
}

func (m *mockRepository) RecordAnnouncement(ctx context.Context, announcement *EventAnnouncement) error {
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
	params := api.GetWorldEventParams{
	}

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

